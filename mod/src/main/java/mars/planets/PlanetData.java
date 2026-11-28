package mars.planets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mars.index.IndexDefinitions;
import mars.index.sim.PlanetSim;
import mars.index.sim.PlanetState;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * One planet's Planetary Index, saved with its dimension (ARCHITECTURE.md 1.5, 7). The pure {@link
 * PlanetSim} does the work; this class only adds saving and loading. The pace comes from the server
 * config ({@code group.size}, {@code pace.classroom}), standard solo when it is not loaded.
 */
public final class PlanetData extends SavedData {
  private static final Logger LOG = LoggerFactory.getLogger("mars");
  static final String NAME = "mars_planet";

  private final PlanetSim sim;

  private PlanetData(PlanetSim sim) {
    this.sim = sim;
  }

  public PlanetSim sim() {
    return sim;
  }

  /** {@code planetId} (the dimension, "mars:planet_1") picks the planet's frost baseline. */
  public static SavedData.Factory<PlanetData> factory(String planetId) {
    return new SavedData.Factory<>(() -> createNew(planetId), (t, r) -> load(planetId, t, r));
  }

  private static PlanetData createNew(String planetId) {
    IndexDefinitions.Defs d = IndexDefinitions.require();
    PlanetSim sim =
        new PlanetSim(
            d.index(),
            d.machines(),
            d.gates(),
            d.frost().forPlanet(planetId),
            d.life(),
            mars.MarsServerConfig.pace());
    sim.configureHazards(d.hazards(), seedFor(planetId));
    return new PlanetData(sim);
  }

  /**
   * The planet's random seed for a new world: the world seed mixed with the dimension name, so two
   * worlds have different dust devils and later storms and two planets in one world differ. Saved
   * with the planet, so it never changes after the first save. 1 when no server is running.
   */
  static long seedFor(String planetId) {
    var server = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
    long world = server == null ? 0 : server.getWorldData().worldGenOptions().seed();
    long h = world * 0x9E3779B97F4A7C15L + planetId.hashCode();
    h ^= h >>> 31;
    return h == 0 ? 1 : h;
  }

  /** Read a saved planet. A save from a newer build is refused with an exception, not read. */
  public static PlanetData load(
      String planetId, CompoundTag tag, HolderLookup.Provider registries) {
    return fromState(planetId, fromNbt(tag));
  }

  /** Build a planet from a saved state (used by load and by rollback). */
  public static PlanetData fromState(String planetId, PlanetState state) {
    IndexDefinitions.Defs d = IndexDefinitions.require();
    PlanetSim.Loaded loaded =
        PlanetSim.fromState(
            state,
            d.index(),
            d.machines(),
            d.gates(),
            d.frost().forPlanet(planetId),
            d.life(),
            mars.MarsServerConfig.pace());
    if (!loaded.report().ignored().isEmpty()) {
      LOG.warn(
          "Planet save has values this build doesn't know (kept out): {}",
          loaded.report().ignored());
    }
    if (!loaded.report().defaulted().isEmpty()) {
      LOG.info(
          "Planet save lacked {}; they start at their start values.", loaded.report().defaulted());
    }
    if (!loaded.unknownMachineTypes().isEmpty()) {
      LOG.warn(
          "Planet save has machines this build has no data for (kept, no effect): {}",
          loaded.unknownMachineTypes());
    }
    loaded.sim().configureHazards(d.hazards(), seedFor(planetId));
    return new PlanetData(loaded.sim());
  }

  /** A brand-new planet at its start values (for rollback to the start). */
  public static PlanetData fresh(String planetId) {
    return createNew(planetId);
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    return toNbt(sim.toState(), tag);
  }

  static CompoundTag toNbt(PlanetState s, CompoundTag out) {
    out.putInt("schema", s.schema());
    out.putLong("simTicks", s.simTicks());
    out.putInt("pendingTicks", s.pendingTicks());
    CompoundTag values = new CompoundTag();
    s.values().forEach(values::putDouble);
    out.put("values", values);
    CompoundTag machines = new CompoundTag();
    s.machines()
        .forEach(
            (type, positions) ->
                machines.put(
                    type, new LongArrayTag(positions.stream().mapToLong(l -> l).toArray())));
    out.put("machines", machines);
    out.putDouble("batteryCharge", s.batteryCharge());
    CompoundTag eff = new CompoundTag();
    s.efficiency().forEach((pos, f) -> eff.putDouble(Long.toString(pos), f));
    out.put("efficiency", eff);
    CompoundTag gates = new CompoundTag();
    s.gates()
        .forEach(
            (id, g) -> {
              CompoundTag t = new CompoundTag();
              t.putBoolean("latched", g.latched());
              t.putString("cause", g.cause().name());
              t.putLong("latchedSimTicks", g.latchedSimTicks());
              t.putDouble("heldSols", g.heldSols());
              t.putDouble("falseSols", g.falseSols());
              gates.put(id, t);
            });
    out.put("gates", gates);
    var h = s.history();
    CompoundTag ht = new CompoundTag();
    ht.putInt("stride", h.stride());
    ht.putDouble("nextSol", h.nextSol());
    ListTag solTag = new ListTag();
    ListTag valueTag = new ListTag();
    for (var x : h.samples()) {
      solTag.add(DoubleTag.valueOf(x.sol()));
      valueTag.add(DoubleTag.valueOf(x.value()));
    }
    ht.put("sols", solTag);
    ht.put("values", valueTag);
    ListTag events = new ListTag();
    for (var e : h.events()) {
      CompoundTag et = new CompoundTag();
      et.putDouble("sol", e.sol());
      et.putString("kind", e.kind().name());
      et.putString("id", e.id());
      events.add(et);
    }
    ht.put("events", events);
    out.put("history", ht);
    CompoundTag frost = new CompoundTag();
    frost.putDouble("storedKpa", s.frost().storedKpa());
    frost.putDouble("buriedReleasedKpa", s.frost().buriedReleasedKpa());
    out.put("frost", frost);
    ListTag domes = new ListTag();
    for (var d :
        s.domes() == null ? java.util.List.<mars.dome.DomeSeals.Dome>of() : s.domes().domes()) {
      CompoundTag dt = new CompoundTag();
      dt.putLong("seed", d.seed());
      dt.putString("status", d.status().name());
      dt.putInt("volume", d.volume());
      domes.add(dt);
    }
    out.put("domes", domes);
    if (s.life() != null) { // schema 7; absent in older saves, which stay on the legacy stand-in
      CompoundTag life = new CompoundTag();
      life.putDouble("o2", s.life().o2());
      life.putDouble("water", s.life().water());
      out.put("life", life);
    }
    if (s.hazards() != null) { // schema 8
      var hz = s.hazards();
      CompoundTag ht8 = new CompoundTag();
      ht8.putLong("seed", hz.seed());
      CompoundTag dust = new CompoundTag();
      hz.dust()
          .forEach(
              (pos, sv) -> {
                CompoundTag t = new CompoundTag();
                t.putDouble("progress", sv.progress());
                t.putBoolean("partly", sv.partlyCleaned());
                dust.put(Long.toString(pos), t);
              });
      ht8.put("dust", dust);
      if (hz.storm() != null) {
        var st = hz.storm();
        CompoundTag t = new CompoundTag();
        t.putString("mode", st.mode().name());
        t.putInt("index", st.index());
        t.putLong("onset", st.onsetTick());
        t.putLong("length", st.lengthTicks());
        t.putLong("lastEnd", st.lastEndTick());
        t.putInt("retries", st.retries());
        t.putBoolean("reminded", st.reminded());
        t.putBoolean("voided", st.voided());
        t.putIntArray("byCap", st.stormsByCap().stream().mapToInt(i -> i).toArray());
        ht8.put("storm", t);
      }
      out.put("hazards", ht8);
    }
    return out;
  }

  static PlanetState fromNbt(CompoundTag tag) {
    int schema = tag.getInt("schema");
    CompoundTag values = tag.getCompound("values");
    Map<String, Double> vs = new LinkedHashMap<>();
    for (String k : values.getAllKeys()) {
      vs.put(k, values.getDouble(k));
    }
    CompoundTag machines = tag.getCompound("machines");
    Map<String, List<Long>> ms = new LinkedHashMap<>();
    for (String type : machines.getAllKeys()) {
      Tag t = machines.get(type);
      List<Long> list = new ArrayList<>();
      if (t instanceof LongArrayTag arr) {
        for (long l : arr.getAsLongArray()) {
          list.add(l);
        }
      }
      ms.put(type, list);
    }
    Map<Long, Double> eff = new LinkedHashMap<>();
    CompoundTag effTag = tag.getCompound("efficiency");
    for (String k : effTag.getAllKeys()) {
      eff.put(Long.parseLong(k), effTag.getDouble(k));
    }
    Map<String, mars.gates.GateTracker.State> gs = new LinkedHashMap<>();
    CompoundTag gateTag = tag.getCompound("gates");
    for (String id : gateTag.getAllKeys()) {
      CompoundTag t = gateTag.getCompound(id);
      gs.put(
          id,
          new mars.gates.GateTracker.State(
              t.getBoolean("latched"),
              mars.gates.GateTracker.Cause.valueOf(t.getString("cause")),
              t.getLong("latchedSimTicks"),
              t.getDouble("heldSols"),
              t.getDouble("falseSols")));
    }
    // Saves before schema 4 have no history: it starts empty.
    var samples = new ArrayList<mars.index.sim.PressureHistory.Sample>();
    var events = new ArrayList<mars.index.sim.PressureHistory.Event>();
    CompoundTag ht = tag.getCompound("history");
    double[] sols = readDoubles(ht, "sols");
    double[] vals = readDoubles(ht, "values");
    for (int i = 0; i < Math.min(sols.length, vals.length); i++) {
      samples.add(new mars.index.sim.PressureHistory.Sample(sols[i], vals[i]));
    }
    ListTag et = ht.getList("events", Tag.TAG_COMPOUND);
    for (int i = 0; i < et.size(); i++) {
      CompoundTag e = et.getCompound(i);
      try {
        events.add(
            new mars.index.sim.PressureHistory.Event(
                e.getDouble("sol"),
                mars.index.sim.PressureHistory.Kind.valueOf(e.getString("kind")),
                e.getString("id")));
      } catch (IllegalArgumentException unknownKind) {
        // An event kind from a newer build: skip it rather than refuse the save.
      }
    }
    var history =
        new mars.index.sim.PressureHistory.State(
            Math.max(1, ht.getInt("stride")), ht.getDouble("nextSol"), samples, events);
    // Saves before schema 6 have no "domes" list: no domes registered.
    var domeList = new ArrayList<mars.dome.DomeSeals.Dome>();
    ListTag dl = tag.getList("domes", Tag.TAG_COMPOUND);
    for (int i = 0; i < dl.size(); i++) {
      CompoundTag dt = dl.getCompound(i);
      try {
        domeList.add(
            new mars.dome.DomeSeals.Dome(
                dt.getLong("seed"),
                mars.dome.DomeSeals.Status.valueOf(dt.getString("status")),
                dt.getInt("volume")));
      } catch (IllegalArgumentException unknownStatus) {
        // A status from a newer build: the dome is rechecked on the first tick anyway.
        domeList.add(
            new mars.dome.DomeSeals.Dome(
                dt.getLong("seed"), mars.dome.DomeSeals.Status.UNKNOWN, dt.getInt("volume")));
      }
    }
    mars.hazards.HazardState hazards = null;
    if (tag.contains("hazards", Tag.TAG_COMPOUND)) {
      CompoundTag h = tag.getCompound("hazards");
      Map<Long, mars.hazards.dust.PanelDustModel.Saved> dust = new LinkedHashMap<>();
      CompoundTag dt = h.getCompound("dust");
      for (String k : dt.getAllKeys()) {
        CompoundTag t = dt.getCompound(k);
        dust.put(
            Long.parseLong(k),
            new mars.hazards.dust.PanelDustModel.Saved(
                t.getDouble("progress"), t.getBoolean("partly")));
      }
      mars.hazards.storm.StormScheduler.State storm = null;
      if (h.contains("storm", Tag.TAG_COMPOUND)) {
        CompoundTag t = h.getCompound("storm");
        try {
          List<Integer> byCap = new ArrayList<>();
          for (int i : t.getIntArray("byCap")) {
            byCap.add(i);
          }
          storm =
              new mars.hazards.storm.StormScheduler.State(
                  mars.hazards.storm.StormScheduler.Mode.valueOf(t.getString("mode")),
                  t.getInt("index"),
                  t.getLong("onset"),
                  t.getLong("length"),
                  t.getLong("lastEnd"),
                  t.getInt("retries"),
                  t.getBoolean("reminded"),
                  t.getBoolean("voided"),
                  byCap);
        } catch (IllegalArgumentException unknownMode) {
          // A mode from a newer build: the schedule restarts from the loaded time.
        }
      }
      hazards = new mars.hazards.HazardState(h.getLong("seed"), dust, storm);
    }
    return new PlanetState(
        schema,
        vs,
        tag.getLong("simTicks"),
        tag.getInt("pendingTicks"),
        ms,
        tag.getDouble("batteryCharge"),
        eff,
        gs,
        history,
        // Saves before schema 5 have no "frost" tag: the reservoir starts empty.
        new mars.index.sim.FrostSink.State(
            tag.getCompound("frost").getDouble("storedKpa"),
            tag.getCompound("frost").getDouble("buriedReleasedKpa")),
        new mars.dome.DomeSeals.State(java.util.List.copyOf(domeList)),
        tag.contains("life", Tag.TAG_COMPOUND)
            ? new mars.life.LifeSupport.State(
                tag.getCompound("life").getDouble("o2"), tag.getCompound("life").getDouble("water"))
            : null,
        hazards);
  }

  private static double[] readDoubles(CompoundTag t, String key) {
    ListTag l = t.getList(key, Tag.TAG_DOUBLE);
    double[] out = new double[l.size()];
    for (int i = 0; i < out.length; i++) {
      out[i] = l.getDouble(i);
    }
    return out;
  }
}
