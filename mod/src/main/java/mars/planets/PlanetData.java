package mars.planets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mars.index.IndexDefinitions;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.index.sim.PlanetState;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * One planet's Planetary Index, saved with its dimension (ARCHITECTURE.md 1.5, 7). The pure {@link
 * PlanetSim} does the work; this class only adds saving and loading. Slice 1 always uses {@link
 * Pace#STANDARD}; pacing config is a later slice (docs/tech/stage2-plan.md).
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

  public static SavedData.Factory<PlanetData> factory() {
    return new SavedData.Factory<>(PlanetData::createNew, PlanetData::load);
  }

  private static PlanetData createNew() {
    IndexDefinitions.Defs d = IndexDefinitions.require();
    return new PlanetData(new PlanetSim(d.index(), d.machines(), d.gates(), Pace.STANDARD));
  }

  /** Read a saved planet. A save from a newer build is refused with an exception, not read. */
  public static PlanetData load(CompoundTag tag, HolderLookup.Provider registries) {
    IndexDefinitions.Defs d = IndexDefinitions.require();
    PlanetSim.Loaded loaded =
        PlanetSim.fromState(fromNbt(tag), d.index(), d.machines(), d.gates(), Pace.STANDARD);
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
    return new PlanetData(loaded.sim());
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
    return new PlanetState(
        schema,
        vs,
        tag.getLong("simTicks"),
        tag.getInt("pendingTicks"),
        ms,
        tag.getDouble("batteryCharge"),
        eff,
        gs);
  }
}
