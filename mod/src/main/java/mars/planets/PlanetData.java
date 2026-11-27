package mars.planets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mars.hazards.storm.StormSchedule;
import mars.index.IndexDefinitions;
import mars.index.sim.Pace;
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
 * PlanetSim} does the work; this class only adds saving and loading. The pace (standard or
 * classroom, from {@link PaceData}) is passed in; it is not saved with the planet.
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
  public static SavedData.Factory<PlanetData> factory(String planetId, Pace pace, long worldSeed) {
    return new SavedData.Factory<>(
        () -> createNew(planetId, pace, worldSeed),
        (t, r) -> load(planetId, pace, worldSeed, t, r));
  }

  private static PlanetData createNew(String planetId, Pace pace, long worldSeed) {
    IndexDefinitions.Defs d = IndexDefinitions.require();
    PlanetSim sim =
        new PlanetSim(d.index(), d.machines(), d.gates(), d.frost().forPlanet(planetId), pace);
    sim.attachStorms(d.storm(), d.dust(), StormSchedule.seedFor(worldSeed, planetId));
    return new PlanetData(sim);
  }

  /** Read a saved planet. A save from a newer build is refused with an exception, not read. */
  public static PlanetData load(
      String planetId,
      Pace pace,
      long worldSeed,
      CompoundTag tag,
      HolderLookup.Provider registries) {
    return fromState(planetId, fromNbt(tag), pace, worldSeed);
  }

  /** Build a planet from a saved state (used by load and by rollback). */
  public static PlanetData fromState(
      String planetId, PlanetState state, Pace pace, long worldSeed) {
    IndexDefinitions.Defs d = IndexDefinitions.require();
    PlanetSim.Loaded loaded =
        PlanetSim.fromState(
            state, d.index(), d.machines(), d.gates(), d.frost().forPlanet(planetId), pace);
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
    // A save from before schema 6 has no storm schedule: attachStorms starts a fresh one.
    loaded.sim().attachStorms(d.storm(), d.dust(), StormSchedule.seedFor(worldSeed, planetId));
    return new PlanetData(loaded.sim());
  }

  /** A brand-new planet at its start values (for rollback to the start). */
  public static PlanetData fresh(String planetId, Pace pace, long worldSeed) {
    return createNew(planetId, pace, worldSeed);
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
    var st = s.storm();
    if (st != null) {
      CompoundTag storm = new CompoundTag();
      storm.putLong("rng", st.rng());
      storm.putString("phase", st.phase().name());
      storm.putString("kind", st.kind().name());
      storm.putDouble("startSol", st.startSol());
      storm.putDouble("endSol", st.endSol());
      storm.putDouble("lastEndSol", st.lastEndSol());
      storm.putInt("stormsEnded", st.stormsEnded());
      storm.putDouble("cap", st.cap());
      out.put("storm", storm);
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
    // Saves before schema 6 have no "storm" tag: the sim starts a fresh schedule.
    mars.hazards.storm.StormSchedule.State storm = null;
    if (tag.contains("storm", Tag.TAG_COMPOUND)) {
      CompoundTag st = tag.getCompound("storm");
      try {
        storm =
            new mars.hazards.storm.StormSchedule.State(
                st.getLong("rng"),
                mars.hazards.storm.StormSchedule.Phase.valueOf(st.getString("phase")),
                mars.hazards.storm.StormSchedule.Kind.valueOf(st.getString("kind")),
                st.getDouble("startSol"),
                st.getDouble("endSol"),
                st.getDouble("lastEndSol"),
                st.getInt("stormsEnded"),
                st.getDouble("cap"));
      } catch (IllegalArgumentException unknownName) {
        // A phase or kind from a newer build: start a fresh schedule rather than refuse the save.
      }
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
        storm);
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
