package mars.story.landing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import mars.dome.DomeTemplate;
import mars.dome.SealCheck;
import mars.hull.HullBlocks;
import mars.hull.HullDoorUsedEvent;
import mars.hull.SealKitPatchedEvent;
import mars.hull.StarterDome;
import mars.hull.WorldProbe;
import mars.journal.Journal;
import mars.journal.JournalEntry;
import mars.landing.Change;
import mars.landing.Facts;
import mars.landing.LandingState;
import mars.landing.Phase;
import mars.landing.Status;
import mars.landing.Step;
import mars.landing.Targets;
import mars.machines.MarsMachines;
import mars.machines.TankReadEvent;
import mars.planets.GateLatchedEvent;
import mars.planets.PlanetManager;
import mars.power.PowerGrid;
import mars.story.StoryDefinitions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * The Minecraft side of the Landing and the journal (landing-spec section 3 and 5): turns game
 * events into calls on the pure {@link LandingState}, then shows the result (panel, BELL lines,
 * journal book) and posts a {@link LandingEvent} for the learning log. The server is the only
 * authority; the client draws what it is sent.
 *
 * <p>Cost: the hooks do one record lookup each. The once-a-second pass per player on a planet reads
 * a few blocks and one flood fill of Hab B (108 cells) only while step 5 is open.
 */
public final class LandingService {
  private LandingService() {}

  /** Briefing delay after the checklist ends: 3 seconds (landing-spec section 4). */
  static final int BRIEFING_DELAY_TICKS = 60;

  /** In-memory only (a restart simply redoes these): when each player's briefing is due. */
  private static final Map<UUID, Long> BRIEFING_DUE = new HashMap<>();

  private static final Map<UUID, List<String>> POINTERS = new HashMap<>();
  private static final Map<UUID, Step> HINT_SENT = new HashMap<>();
  private static final Map<UUID, String> LAST_PANEL = new HashMap<>();
  private static final java.util.Set<UUID> REFRESH_BOOK = new java.util.HashSet<>();

  private static boolean ready() {
    return StoryDefinitions.isLoaded();
  }

  private static StoryDefinitions.Defs defs() {
    return StoryDefinitions.require();
  }

  public static LandingData.Rec rec(ServerPlayer p) {
    return LandingData.get(p.serverLevel()).rec(p.getUUID());
  }

  private static boolean onPlanet(ServerPlayer p) {
    return ready() && PlanetManager.isPlanet(p.serverLevel());
  }

  private static long now(ServerLevel level) {
    return level.getServer().getTickCount();
  }

  // ---- parameters for journal text ----

  /** {hold_sols} and {grace_minutes} come from the data files, never typed (landing-spec W3). */
  public static Map<String, String> params(ServerLevel level) {
    var sim = PlanetManager.data(level).sim();
    String hold = "3";
    for (var g : sim.gates().defs()) {
      if (g.id().equals("mars:dome_stable")) {
        hold = trim(sim.gates().holdSols(g));
      }
    }
    String grace = "24";
    if (sim.life() != null) {
      grace = trim(sim.life().params().landingEndSol() * sim.pace().solTicks() / 20.0 / 60.0);
    }
    return Map.of("hold_sols", hold, "grace_minutes", grace);
  }

  private static String trim(double v) {
    return v == Math.rint(v) ? String.valueOf((long) v) : String.valueOf(v);
  }

  // ---- entry points from the game ----

  @SubscribeEvent
  public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
    if (e.getEntity() instanceof ServerPlayer p) {
      arrive(p);
    }
  }

  @SubscribeEvent
  public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
    if (e.getEntity() instanceof ServerPlayer p) {
      arrive(p);
    }
  }

  /** First join (or return) on a planet: journal grant, offer, panel. Idempotent. */
  public static void arrive(ServerPlayer p) {
    if (!onPlanet(p)) {
      return;
    }
    ServerLevel level = p.serverLevel();
    var r = rec(p);
    fire(p, r, "join");
    catchUp(p, level, r);
    if (!r.granted || !JournalBook.has(p) && !r.granted) {
      grantBook(p, r);
    }
    List<Change> c = r.landing.offer();
    emit(p, c);
    if (r.landing.phase() == Phase.OFFERED) {
      PacketDistributor.sendToPlayer(p, new LandingPayloads.Intro());
      p.sendSystemMessage(offerLine());
    }
    sendPanel(p, r, true);
  }

  private static Component offerLine() {
    return bell(Component.translatable("landing.intro"))
        .append(Component.literal(" "))
        .append(button("landing.button.start", "/mars landing start"))
        .append(Component.literal(" "))
        .append(button("landing.button.skip", "/mars landing skip"));
  }

  private static MutableComponent button(String key, String command) {
    return Component.literal("[")
        .append(Component.translatable(key))
        .append("]")
        .withStyle(
            s ->
                s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                    .withColor(ChatFormatting.AQUA)
                    .withUnderlined(true));
  }

  public static MutableComponent bell(Component text) {
    return Component.literal("BELL: ").withStyle(ChatFormatting.GRAY).append(text);
  }

  /** The player chose on the intro screen (or by command). Only an OFFERED player may. */
  public static void onChoice(ServerPlayer p, boolean start) {
    if (!onPlanet(p)) {
      return;
    }
    var r = rec(p);
    if (r.landing.phase() != Phase.OFFERED) {
      return;
    }
    if (start) {
      startLanding(p);
    } else {
      skipAll(p);
    }
  }

  public static void startLanding(ServerPlayer p) {
    if (!onPlanet(p)) {
      return;
    }
    var r = rec(p);
    apply(p, r, r.landing.start(now(p.serverLevel())));
    sendPanel(p, r, true);
  }

  public static void skipAll(ServerPlayer p) {
    if (!onPlanet(p)) {
      return;
    }
    var r = rec(p);
    apply(p, r, r.landing.skipAll());
  }

  public static void skipStep(ServerPlayer p) {
    if (!onPlanet(p)) {
      return;
    }
    var r = rec(p);
    Step a = r.landing.active();
    if (a != null) {
      apply(p, r, r.landing.skipStep(a, now(p.serverLevel())));
    }
  }

  /** Operator test aid ({@code /mars landing step n}): tick one step as if the player did it. */
  public static void markFor(ServerPlayer p, Step step) {
    if (onPlanet(p)) {
      var r = rec(p);
      apply(p, r, r.landing.mark(step, Status.DONE, now(p.serverLevel())));
    }
  }

  public static void resetFor(ServerPlayer p) {
    var r = rec(p);
    r.landing.reset();
    r.journal.clear();
    r.granted = false;
    BRIEFING_DUE.remove(p.getUUID());
    HINT_SENT.remove(p.getUUID());
    POINTERS.remove(p.getUUID());
    LAST_PANEL.remove(p.getUUID());
    arrive(p);
  }

  // ---- observations ----

  public static boolean wantsOutsideAir(ServerPlayer p) {
    if (!onPlanet(p)) {
      return false;
    }
    var r = rec(p);
    Phase ph = r.landing.phase();
    return (ph == Phase.OFFERED || ph == Phase.RUNNING)
        && r.landing.status(Step.MONITOR) == Status.PENDING;
  }

  /** True once this player's Landing is over, finished or skipped. */
  public static boolean landingOver(ServerPlayer p) {
    return onPlanet(p) && rec(p).landing.phase().over();
  }

  public static void onMonitorOpened(ServerPlayer p) {
    if (onPlanet(p)) {
      var r = rec(p);
      apply(p, r, r.landing.onMonitorOpened(now(p.serverLevel())));
    }
  }

  @SubscribeEvent
  public static void onTankRead(TankReadEvent e) {
    ServerPlayer p = e.player();
    if (onPlanet(p)) {
      var r = rec(p);
      apply(p, r, r.landing.onTankRead(now(p.serverLevel())));
    }
  }

  @SubscribeEvent
  public static void onDoor(HullDoorUsedEvent e) {
    ServerPlayer p = e.player();
    if (!onPlanet(p)) {
      return;
    }
    var r = rec(p);
    BlockPos origin = StarterDome.data(p.serverLevel()).origin();
    if (origin != null && e.pos().getZ() == origin.getZ() + DomeTemplate.SIZE_Z - 1) {
      r.landing.onOuterDoorUsed();
    }
    if (e.open()) {
      apply(p, r, r.landing.onDoorOpened(now(p.serverLevel())));
    }
  }

  @SubscribeEvent
  public static void onKit(SealKitPatchedEvent e) {
    ServerPlayer p = e.player();
    if (onPlanet(p)) {
      var r = rec(p);
      apply(p, r, r.landing.onPatched(true, now(p.serverLevel())));
    }
  }

  @SubscribeEvent
  public static void onPlace(BlockEvent.EntityPlaceEvent e) {
    if (!(e.getEntity() instanceof ServerPlayer p)
        || !(e.getLevel() instanceof ServerLevel level)
        || !onPlanet(p)) {
      return;
    }
    var state = e.getPlacedBlock();
    var r = rec(p);
    if (state.is(MarsMachines.REGOLITH_OVEN.get())) {
      r.landing.onOvenPlaced();
    } else if (state.is(MarsMachines.REGOLITH_OUTGASSER.get())) {
      firePlanet(level, "placed:mars:regolith_outgasser");
    } else if (state.is(HullBlocks.HULL_PLATE.get())) {
      BlockPos origin = StarterDome.data(level).origin();
      if (origin != null
          && e.getPos().equals(origin.offset(DomeTemplate.BREACH_X, 2, DomeTemplate.BREACH_Z))) {
        apply(p, r, r.landing.onPatched(false, now(level)));
      }
    }
  }

  @SubscribeEvent
  public static void onGate(GateLatchedEvent e) {
    ServerLevel level = e.level();
    firePlanet(level, "gate:" + e.gateId());
    if (e.gateId().equals("mars:dome_stable")) {
      for (ServerPlayer p : level.players()) {
        p.sendSystemMessage(bell(Component.translatable("landing.relay")));
      }
    }
  }

  /** The journal: right-click rebuilds the book first, then the vanilla book screen opens it. */
  @SubscribeEvent
  public static void onUseItem(PlayerInteractEvent.RightClickItem e) {
    if (!(e.getEntity() instanceof ServerPlayer p) || !onPlanet(p)) {
      return;
    }
    if (!JournalBook.isJournal(e.getItemStack())) {
      return;
    }
    var r = rec(p);
    ServerLevel level = p.serverLevel();
    InteractionHand hand = e.getHand();
    p.setItemInHand(hand, JournalBook.build(r.journal, defs().journal(), false, params(level)));
    r.journal.markAllSeen();
    REFRESH_BOOK.add(p.getUUID());
    emitCustom(p, "journal.opened", "journal_contents");
    apply(p, r, r.landing.onJournalOpened(now(level)));
  }

  /** The Ops lectern hands out a new journal to a player who has none (landing-spec 5.1). */
  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onLectern(PlayerInteractEvent.RightClickBlock e) {
    if (e.getLevel().isClientSide
        || !(e.getEntity() instanceof ServerPlayer p)
        || !onPlanet(p)
        || !e.getLevel().getBlockState(e.getPos()).is(Blocks.LECTERN)
        || JournalBook.has(p)) {
      return;
    }
    grantBook(p, rec(p));
    e.setCanceled(true);
    e.setCancellationResult(InteractionResult.SUCCESS);
  }

  public static void grantBook(ServerPlayer p, LandingData.Rec r) {
    var stack = JournalBook.build(r.journal, defs().journal(), false, params(p.serverLevel()));
    var inv = p.getInventory();
    if (inv.getItem(8).isEmpty()) {
      inv.setItem(8, stack);
    } else if (!inv.add(stack)) {
      p.drop(stack, false);
    }
    r.granted = true;
  }

  // ---- journal unlocks ----

  /** Unlock the entries a token fires for this player; queue the pointer line. */
  static void fire(ServerPlayer p, LandingData.Rec r, String token) {
    Journal j = defs().journal();
    List<String> titles = new ArrayList<>();
    for (JournalEntry e : j.fired(token)) {
      if (r.journal.unlock(e.id())) {
        titles.add(e.title());
      }
    }
    if (titles.isEmpty()) {
      return;
    }
    POINTERS.computeIfAbsent(p.getUUID(), k -> new ArrayList<>()).addAll(titles);
    JournalBook.refresh(p, r.journal, j, params(p.serverLevel()));
    flushPointer(p, r);
  }

  /** Every player on the planet gets planet-scope entries; others are caught up on join. */
  static void firePlanet(ServerLevel level, String token) {
    if (!ready()) {
      return;
    }
    for (ServerPlayer p : level.players()) {
      fire(p, LandingData.get(level).rec(p.getUUID()), token);
    }
  }

  /** A player who was offline gets the planet-scope entries whose cause already happened. */
  private static void catchUp(ServerPlayer p, ServerLevel level, LandingData.Rec r) {
    var sim = PlanetManager.data(level).sim();
    for (JournalEntry e : defs().journal().entries()) {
      if (!e.planetScope()) {
        continue;
      }
      for (String t : e.triggers()) {
        boolean happened =
            t.startsWith("gate:")
                ? sim.gates().isLatched(t.substring(5))
                : t.startsWith("placed:") && sim.machineCount(t.substring(7)) > 0;
        if (happened) {
          fire(p, r, t);
        }
      }
    }
  }

  /**
   * One pointer line, and not while the checklist runs unless step 8 is done (landing-spec 5.4).
   */
  private static void flushPointer(ServerPlayer p, LandingData.Rec r) {
    boolean quiet =
        r.landing.phase() == Phase.RUNNING && r.landing.status(Step.JOURNAL) == Status.PENDING;
    List<String> titles = POINTERS.get(p.getUUID());
    if (quiet || titles == null || titles.isEmpty()) {
      return;
    }
    p.sendSystemMessage(
        Component.translatable("journal.pointer", String.join(", ", titles))
            .withStyle(ChatFormatting.GRAY));
    titles.clear();
  }

  // ---- applying changes ----

  static void apply(ServerPlayer p, LandingData.Rec r, List<Change> changes) {
    if (changes.isEmpty()) {
      return;
    }
    ServerLevel level = p.serverLevel();
    var cfg = defs().landing();
    for (Change c : changes) {
      switch (c.kind()) {
        case STEP -> {
          Step s = c.step();
          String suffix =
              c.status() == Status.AUTO ? "_auto" : c.status() == Status.SKIPPED ? "_skipped" : "";
          emitCustom(p, "landing.step", "landing_step_" + s.number() + suffix);
          if (c.status() != Status.SKIPPED) {
            fire(p, r, "step:" + s.name());
          }
          if ((s == Step.PATCH || s == Step.OVENS) && c.status() != Status.SKIPPED) {
            p.sendSystemMessage(bell(Component.translatable("landing.log." + s.number())));
          }
        }
        case FINISHED -> {
          p.sendSystemMessage(bell(Component.translatable("landing.done")));
          emitCustom(p, "landing.done", "landing_done");
          over(p, r);
        }
        case SKIPPED_ALL -> {
          emitCustom(p, "landing.skipped", "landing_all");
          for (String k : cfg.skipKeys()) {
            p.sendSystemMessage(bell(Component.translatable(k)));
          }
          over(p, r);
        }
        default -> {}
      }
    }
    flushPointer(p, r);
    sendPanel(p, r, true);
  }

  private static void over(ServerPlayer p, LandingData.Rec r) {
    fire(p, r, "landing_over");
    BRIEFING_DUE.put(p.getUUID(), now(p.serverLevel()) + BRIEFING_DELAY_TICKS);
  }

  private static void showBriefing(ServerPlayer p, LandingData.Rec r) {
    ServerLevel level = p.serverLevel();
    JournalEntry e = defs().journal().get("briefing.shift1");
    r.landing.briefingShown();
    if (e != null) {
      String text = Journal.fill(e.text(), params(level));
      p.sendSystemMessage(
          Component.literal("== ")
              .append(Component.translatable("briefing.title"))
              .append(" ==")
              .withStyle(ChatFormatting.GOLD));
      p.sendSystemMessage(Component.literal(text).withStyle(ChatFormatting.WHITE));
    }
    emitCustom(p, "briefing.opened", "briefing_shift1");
    fire(p, r, "briefing_shown");
    flushPointer(p, r);
  }

  private static void emit(ServerPlayer p, List<Change> changes) {
    // OFFERED is not an event of its own in the spec; nothing to post.
  }

  private static void emitCustom(ServerPlayer p, String type, String subject) {
    NeoForge.EVENT_BUS.post(new LandingEvent(p, type, subject));
  }

  // ---- the once-a-second pass ----

  @SubscribeEvent
  public static void onPlayerTick(PlayerTickEvent.Post e) {
    if (!(e.getEntity() instanceof ServerPlayer p) || p.tickCount % 20 != 0 || !onPlanet(p)) {
      return;
    }
    secondFor(p);
  }

  /** One second for one player; public so a GameTest can drive it. */
  public static void secondFor(ServerPlayer p) {
    ServerLevel level = p.serverLevel();
    var r = rec(p);
    long now = now(level);
    if (REFRESH_BOOK.remove(p.getUUID())) {
      JournalBook.refresh(p, r.journal, defs().journal(), params(level));
    }
    if (r.landing.briefingDue()) {
      Long due = BRIEFING_DUE.computeIfAbsent(p.getUUID(), k -> now + BRIEFING_DELAY_TICKS);
      if (now >= due) {
        BRIEFING_DUE.remove(p.getUUID());
        showBriefing(p, r);
      }
    }
    if (r.landing.phase() != Phase.RUNNING) {
      return;
    }
    List<Change> all = new ArrayList<>();
    if (r.landing.status(Step.KIT) == Status.PENDING && holdsKit(p)) {
      all.addAll(r.landing.onKitHeld(now));
    }
    BlockPos origin = StarterDome.data(level).origin();
    if (origin != null) {
      all.addAll(r.landing.onPosition(outside(p, origin), now));
      all.addAll(r.landing.onFacts(facts(level, r.landing, origin), now));
    }
    if (r.landing.phase() == Phase.RUNNING) {
      Step a = r.landing.active();
      if (a != null
          && r.landing.hintDue(now, defs().landing().hintDelayTicks())
          && HINT_SENT.get(p.getUUID()) != a) {
        HINT_SENT.put(p.getUUID(), a);
        p.sendSystemMessage(
            bell(Component.translatable(defs().landing().hintKey(a)))
                .append(Component.literal(" "))
                .append(button("landing.skip_step", "/mars landing skip_step")));
      }
    }
    apply(p, r, all);
    sendPanel(p, r, false);
  }

  private static boolean holdsKit(ServerPlayer p) {
    var inv = p.getInventory();
    for (int i = 0; i < inv.getContainerSize(); i++) {
      if (inv.getItem(i).is(HullBlocks.SEAL_KIT.get())) {
        return true;
      }
    }
    return false;
  }

  /** More than 2 blocks outside the dome's box (landing-spec step 7). */
  static boolean outside(ServerPlayer p, BlockPos origin) {
    double x = p.getX() - origin.getX();
    double z = p.getZ() - origin.getZ();
    double y = p.getY() - origin.getY();
    return x < -2
        || x > DomeTemplate.SIZE_X + 2
        || z < -2
        || z > DomeTemplate.SIZE_Z + 2
        || y > DomeTemplate.SIZE_Y + 8
        || y < -8;
  }

  static Facts facts(ServerLevel level, LandingState landing, BlockPos origin) {
    boolean sealed = false;
    if (landing.status(Step.PATCH) == Status.PENDING) {
      BlockPos crack = origin.offset(DomeTemplate.BREACH_X, 2, DomeTemplate.BREACH_Z);
      if (!level.getBlockState(crack).is(HullBlocks.HULL_PLATE_CRACKED.get())) {
        BlockPos habB = origin.offset(3, 1, 11);
        sealed =
            SealCheck.fill(new WorldProbe(level), habB.getX(), habB.getY(), habB.getZ(), 2000)
                    .status()
                == SealCheck.Status.SEALED;
      }
    }
    boolean powered = false;
    if (landing.status(Step.OVENS) == Status.PENDING) {
      var sim = PlanetManager.data(level).sim();
      powered =
          sim.machineCount("mars:regolith_oven") >= 2
              && sim.power().state(sim.stepSols()) == PowerGrid.State.POWERED;
    }
    return new Facts(sealed, powered);
  }

  // ---- panel ----

  static void sendPanel(ServerPlayer p, LandingData.Rec r, boolean force) {
    LandingPayloads.Panel panel = panelFor(p, r);
    String key = panel.toString();
    if (!force && key.equals(LAST_PANEL.get(p.getUUID()))) {
      return;
    }
    LAST_PANEL.put(p.getUUID(), key);
    PacketDistributor.sendToPlayer(p, panel);
  }

  static LandingPayloads.Panel panelFor(ServerPlayer p, LandingData.Rec r) {
    if (r.landing.phase() != Phase.RUNNING) {
      return LandingPayloads.Panel.hidden();
    }
    var cfg = defs().landing();
    List<String> keys = new ArrayList<>();
    List<Integer> st = new ArrayList<>();
    for (Step s : Step.values()) {
      keys.add(cfg.stepKey(s));
      st.add(r.landing.status(s).ordinal());
    }
    Step a = r.landing.active();
    int chip = -1;
    String dir = "";
    String hint = "";
    if (a != null) {
      if (r.landing.hintDue(now(p.serverLevel()), cfg.hintDelayTicks())) {
        hint = cfg.hintKey(a);
      }
      BlockPos origin = StarterDome.data(p.serverLevel()).origin();
      int[] t = Targets.of(a);
      if (origin != null && t != null) {
        double dx = origin.getX() + t[0] + 0.5 - p.getX();
        double dz = origin.getZ() + t[2] + 0.5 - p.getZ();
        chip = (int) Math.round(Math.sqrt(dx * dx + dz * dz));
        dir = Targets.direction(dx, dz);
      }
    }
    return new LandingPayloads.Panel(true, keys, st, a == null ? -1 : a.ordinal(), hint, chip, dir);
  }

  /** Test seam: forget in-memory timers (a GameTest world keeps the JVM between tests). */
  public static void resetMemory() {
    BRIEFING_DUE.clear();
    POINTERS.clear();
    HINT_SENT.clear();
    LAST_PANEL.clear();
    REFRESH_BOOK.clear();
  }

  /** Test seam: make the briefing due now instead of in 3 seconds. */
  public static void forceBriefingNow(ServerPlayer p) {
    BRIEFING_DUE.put(p.getUUID(), 0L);
  }

  /** Briefing due tick for a player (test seam). */
  public static Long briefingDue(UUID id) {
    return BRIEFING_DUE.get(id);
  }
}
