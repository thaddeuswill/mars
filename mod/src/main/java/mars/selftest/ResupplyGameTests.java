package mars.selftest;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mars.core.Ids;
import mars.gates.GateTracker;
import mars.machines.MarsMachines;
import mars.planets.CacheData;
import mars.planets.GateCaches;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Orbital resupply (docs/design/recipe-inputs-ruling.md; BACKLOG #49): the changed machine recipes
 * through the real recipe manager, the per-gate drop table through the real gate caches, and the
 * resupply request item with its pass timer. Own batch: it resets the shared planet.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class ResupplyGameTests {
  private ResupplyGameTests() {}

  private static Item mars(String id) {
    return BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("mars", id));
  }

  private static String id(Item i) {
    return BuiltInRegistries.ITEM.getKey(i).toString();
  }

  private static int count(FakePlayer p, Item item) {
    return p.getInventory().countItem(item);
  }

  private static FakePlayer player(ServerLevel level, String last, String name) {
    return FakePlayerFactory.get(
        level, new GameProfile(UUID.fromString("6d617273-0000-4000-8000-0000000000" + last), name));
  }

  /** What the 3x3 grid crafts, or "no recipe" (empty strings pad the grid). */
  private static String craft(ServerLevel level, boolean shapeless, Item... cells) {
    List<ItemStack> grid = new ArrayList<>();
    for (Item i : cells) {
      grid.add(i == Items.AIR ? ItemStack.EMPTY : new ItemStack(i));
    }
    while (grid.size() < 9) {
      grid.add(ItemStack.EMPTY);
    }
    CraftingInput input = CraftingInput.of(3, 3, grid);
    var found =
        level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
    if (found.isEmpty()) {
      return "no recipe";
    }
    ItemStack out = found.get().value().assemble(input, level.registryAccess());
    return id(out.getItem()) + " x" + out.getCount();
  }

  @GameTest(template = "empty", batch = "mars_resupply", timeoutTicks = 100)
  public static void changedMachineRecipesResolveThroughTheRealRecipeManager(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    Item iron = Items.IRON_INGOT;
    Item air = Items.AIR;
    Item core = mars("machine_core");
    Item cartridge = mars("filter_cartridge");
    Item coil = mars("heater_coil");
    Item bearing = mars("pump_bearing");
    Item bed = mars("sorbent_bed");
    String processor = craft(level, false, iron, cartridge, iron, air, core, air, iron, air, iron);
    String emitter = craft(level, false, coil, core, air, iron, air, iron, iron, air, iron);
    String extractor = craft(level, false, iron, core, iron, iron, bearing, iron, air, iron, air);
    String scrubber = craft(level, false, iron, bed, iron, iron, core, iron, iron, bed, iron);
    String outgasser = craft(level, false, iron, iron, iron, iron, coil, iron, iron, iron, iron);
    String request = craft(level, true, iron, iron, iron, iron, coil);
    // The old hopper and bucket recipes must be gone: the same cells with a hopper find nothing.
    String oldProcessor =
        craft(
            level,
            false,
            iron,
            cartridge,
            iron,
            Items.COPPER_INGOT,
            Items.HOPPER,
            Items.COPPER_INGOT,
            iron,
            Items.REDSTONE,
            iron);
    String bedRecipe =
        craft(level, true, Items.RED_SANDSTONE, Items.RED_SANDSTONE, Items.GLASS_PANE, iron);
    boolean ok =
        SelfTest.check(
            "resupply.recipes_resolve",
            "processor, emitter, extractor, scrubber, outgasser and the request craft from the ruling's ingredients",
            "processor="
                + processor
                + " emitter="
                + emitter
                + " extractor="
                + extractor
                + " scrubber="
                + scrubber
                + " outgasser="
                + outgasser
                + " request="
                + request,
            processor.equals("mars:atmospheric_processor x1")
                && emitter.equals("mars:super_greenhouse_emitter x1")
                && extractor.equals("mars:ice_extractor x1")
                && scrubber.equals("mars:carbon_scrubber x1")
                && outgasser.equals("mars:regolith_outgasser x1")
                && request.equals("mars:resupply_request x1"));
    ok &=
        SelfTest.check(
            "resupply.old_recipes_gone",
            "the old hopper processor recipe and the sorbent bed craft recipe no longer exist",
            "oldProcessor=" + oldProcessor + " bed=" + bedRecipe,
            oldProcessor.equals("no recipe") && bedRecipe.equals("no recipe"));
    if (ok) {
      h.succeed();
    } else {
      h.fail("changedMachineRecipesResolveThroughTheRealRecipeManager failed; see MARS-SELFTEST");
    }
  }

  @GameTest(template = "empty", batch = "mars_resupply", timeoutTicks = 100)
  public static void everyGateDropMatchesTheTableAndComesOncePerPlayer(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    CacheData.resetForSelfTest(level);
    var sim = data.sim();
    FakePlayer p = player(level, "21", "[DropOne]");
    p.getInventory().clearContent();
    for (var g : sim.gates().defs()) {
      sim.gates().latch(g.id(), GateTracker.Cause.ADMIN, 0);
    }
    int delivered = GateCaches.deliver(p);
    int again = GateCaches.deliver(p);
    // The drop table of docs/design/recipe-inputs-ruling.md section 5, summed over the 7 gates.
    boolean counts =
        count(p, mars("heater_coil")) == 4
            && count(p, Items.FURNACE) == 2
            && count(p, Items.COAL) == 32
            && count(p, mars("filter_cartridge")) == 3
            && count(p, mars("machine_core")) == 18
            && count(p, Items.BUCKET) == 2
            && count(p, mars("pump_bearing")) == 2
            && count(p, mars("resin_pack")) == 4
            && count(p, mars("sorbent_bed")) == 4
            && count(p, Items.GLASS) == 8
            && count(p, mars("signal_board")) == 5
            && count(p, mars("activation_cell")) == 1;
    boolean ok =
        SelfTest.check(
            "resupply.drops_match_table",
            "all 7 gate drops: 4 coil, 2 furnace, 32 coal, 3 cartridge, 18 core, 2 bucket, 2 bearing, 4 resin, 4 bed, 8 glass, 5 board, 1 cell",
            "delivered="
                + delivered
                + " core="
                + count(p, mars("machine_core"))
                + " coal="
                + count(p, Items.COAL)
                + " bed="
                + count(p, mars("sorbent_bed")),
            delivered == 7 && counts);
    ok &=
        SelfTest.check(
            "resupply.drops_once",
            "a second arrival gives nothing more",
            "again=" + again + " core=" + count(p, mars("machine_core")),
            again == 0 && count(p, mars("machine_core")) == 18);
    CacheData.resetForSelfTest(level);
    if (ok) {
      h.succeed();
    } else {
      h.fail("everyGateDropMatchesTheTableAndComesOncePerPlayer failed; see MARS-SELFTEST");
    }
  }

  @GameTest(template = "empty", batch = "mars_resupply", timeoutTicks = 100)
  public static void theResupplyRequestResendsHalfOncePerPassAndMovesNoIndexRow(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    CacheData.resetForSelfTest(level);
    var sim = data.sim();
    FakePlayer p = player(level, "22", "[RequestOne]");
    p.getInventory().clearContent();
    Item request = MarsMachines.RESUPPLY_REQUEST.get();
    Item core = mars("machine_core");
    Item bearing = mars("pump_bearing");
    p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(request, 3));
    ItemStack held = p.getItemInHand(InteractionHand.MAIN_HAND);
    held.use(level, p, InteractionHand.MAIN_HAND);
    boolean ok =
        SelfTest.check(
            "resupply.nothing_yet",
            "before any gate has dropped anything the request sends nothing and is kept",
            "core=" + count(p, core) + " requests=" + count(p, request),
            count(p, core) == 0 && count(p, request) == 3);
    for (String g : List.of("mars:dome_stable", "mars:phase_2_begins", GATE_THAW)) {
      sim.gates().latch(g, GateTracker.Cause.ADMIN, 0);
    }
    GateCaches.deliver(p);
    int cores0 = count(p, core);
    int bearings0 = count(p, bearing);
    double[] before = indexValues(sim);
    p.getInventory().removeItem(p.getItemInHand(InteractionHand.MAIN_HAND));
    p.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(request, 3));
    p.getItemInHand(InteractionHand.MAIN_HAND).use(level, p, InteractionHand.MAIN_HAND);
    int cores1 = count(p, core);
    int bearings1 = count(p, bearing);
    ok &=
        SelfTest.check(
            "resupply.sends_half_of_latest",
            "latest gate is the Thaw (2 bearings, 4 cores): a request sends 1 bearing and 2 cores and is used up",
            "cores +"
                + (cores1 - cores0)
                + " bearings +"
                + (bearings1 - bearings0)
                + " requests="
                + count(p, request),
            cores1 - cores0 == 2 && bearings1 - bearings0 == 1 && count(p, request) == 2);
    p.getItemInHand(InteractionHand.MAIN_HAND).use(level, p, InteractionHand.MAIN_HAND);
    ok &=
        SelfTest.check(
            "resupply.cooldown_holds",
            "a second request inside the 3-sol pass sends nothing and keeps the item",
            "cores +" + (count(p, core) - cores1) + " requests=" + count(p, request),
            count(p, core) == cores1 && count(p, request) == 2);
    ok &=
        SelfTest.check(
            "resupply.index_untouched",
            "no Index row changed from the drops and the request",
            java.util.Arrays.toString(before)
                + " -> "
                + java.util.Arrays.toString(indexValues(sim)),
            java.util.Arrays.equals(before, indexValues(sim)));
    sim.model().advanceSols(3.2);
    p.getItemInHand(InteractionHand.MAIN_HAND).use(level, p, InteractionHand.MAIN_HAND);
    ok &=
        SelfTest.check(
            "resupply.next_pass_sends",
            "after 3.2 sols the next request sends again",
            "cores +" + (count(p, core) - cores1) + " requests=" + count(p, request),
            count(p, core) - cores1 == 2 && count(p, request) == 1);
    CacheData.resetForSelfTest(level);
    if (ok) {
      h.succeed();
    } else {
      h.fail("theResupplyRequestResendsHalfOncePerPass failed; see MARS-SELFTEST lines");
    }
  }

  private static final String GATE_THAW = "mars:thaw";

  private static double[] indexValues(mars.index.sim.PlanetSim sim) {
    return java.util.stream.Stream.of(
            Ids.PRESSURE,
            Ids.TEMPERATURE,
            Ids.HYDROSPHERE,
            Ids.O2_FRACTION,
            Ids.CO2_PARTIAL,
            Ids.BIOMASS)
        .mapToDouble(sim.model()::current)
        .toArray();
  }
}
