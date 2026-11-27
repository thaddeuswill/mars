package mars.dimension;

import com.mojang.serialization.MapCodec;
import mars.terrain.TerrainMode;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registers the Mars chunk generator type and wires the per-world terrain mode. */
public final class MarsWorldgen {
  public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> GENERATORS =
      DeferredRegister.create(Registries.CHUNK_GENERATOR, "mars");

  static {
    GENERATORS.register("planet", () -> MarsChunkGenerator.CODEC);
  }

  private MarsWorldgen() {}

  public static void register(IEventBus modBus) {
    GENERATORS.register(modBus);
    NeoForge.EVENT_BUS.addListener(MarsWorldgen::onLevelLoad);
    NeoForge.EVENT_BUS.addListener(
        (AddReloadListenerEvent e) -> e.addListener(new PlanetTerrainData.Listener()));
  }

  /** Before any chunk of a Mars level generates, tell its generator which mode the world uses. */
  private static void onLevelLoad(LevelEvent.Load event) {
    if (event.getLevel() instanceof ServerLevel level
        && level.getChunkSource().getGenerator() instanceof MarsChunkGenerator gen) {
      TerrainMode mode = TerrainModeData.resolve(level.getServer());
      gen.setMode(mode);
      mars.selftest.SelfTest.info("terrain_mode", level.dimension().location() + " " + mode.id());
    }
  }
}
