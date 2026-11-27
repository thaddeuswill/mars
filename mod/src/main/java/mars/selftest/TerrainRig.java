package mars.selftest;

import mars.dimension.MarsChunkGenerator;
import mars.dimension.MarsDimensions;
import mars.dimension.PlanetTerrainData;
import mars.terrain.TerrainMode;
import mars.terrain.TerrainParams;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.blending.Blender;

/**
 * Runs the real Mars generator pipeline (biomes, noise fill, surface rule, landform shaping) on
 * bare chunks, with no level and no world on disk. This is the headless way to check generation:
 * the GameTest server has no Mars level (docs/tech/test-strategy.md). Not covered: features (ores,
 * ice, boulders), which need a world-gen region; those are checked separately by placing the placed
 * features into a test level.
 */
final class TerrainRig {
  private final MinecraftServer server;
  final MarsChunkGenerator generator;
  final RandomState random;
  final StructureManager structures;
  final Registry<Biome> biomes;
  final BiomeManager biomeManager;
  final TerrainParams params;
  final long seed;
  final LevelHeightAccessor heights = LevelHeightAccessor.create(0, 256);

  TerrainRig(MinecraftServer server, long seed, TerrainMode mode) {
    this.server = server;
    RegistryAccess access = server.registryAccess();
    LevelStem stem = decodeStem(server);
    if (!(stem.generator() instanceof MarsChunkGenerator real)) {
      throw new IllegalStateException("planet_1 generator is " + stem.generator().getClass());
    }
    // A private instance: the registry's own one belongs to a real Mars level, if there is one.
    this.generator = new MarsChunkGenerator(real.getBiomeSource(), real.generatorSettings());
    this.generator.setMode(mode);
    this.seed = seed;
    this.random =
        RandomState.create(
            generator.generatorSettings().value(), access.lookupOrThrow(Registries.NOISE), seed);
    // No structures exist on Mars. A manager that finds none keeps the noise fill from asking a
    // real level for chunks (which needs the server thread and can stall a test).
    this.structures =
        new StructureManager(null, null, null) {
          @Override
          public java.util.List<net.minecraft.world.level.levelgen.structure.StructureStart>
              startsForStructure(
                  ChunkPos pos,
                  java.util.function.Predicate<
                          net.minecraft.world.level.levelgen.structure.Structure>
                      filter) {
            return java.util.List.of();
          }
        };
    this.biomes = access.registryOrThrow(Registries.BIOME);
    this.biomeManager =
        new BiomeManager(
            (x, y, z) -> generator.getBiomeSource().getNoiseBiome(x, y, z, random.sampler()),
            BiomeManager.obfuscateSeed(seed));
    this.params = PlanetTerrainData.get(MarsDimensions.PLANET_1.location());
    if (params == null) {
      throw new IllegalStateException("no planet terrain data for planet_1");
    }
  }

  /**
   * Decodes data/mars/dimension/planet_1.json the way the game does (the GameTest world lacks it).
   */
  static LevelStem decodeStem(MinecraftServer server) {
    var res =
        server
            .getResourceManager()
            .getResource(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                    "mars", "dimension/planet_1.json"))
            .orElseThrow(() -> new IllegalStateException("dimension/planet_1.json missing"));
    try (java.io.Reader r =
        new java.io.InputStreamReader(res.open(), java.nio.charset.StandardCharsets.UTF_8)) {
      return LevelStem.CODEC
          .parse(
              net.minecraft.resources.RegistryOps.create(
                  com.mojang.serialization.JsonOps.INSTANCE, server.registryAccess()),
              com.google.gson.JsonParser.parseReader(r))
          .getOrThrow(msg -> new IllegalStateException("planet_1.json: " + msg));
    } catch (java.io.IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /** Noise fill and surface rule only: the ground before craters, dunes and the pad. */
  ProtoChunk natural(int cx, int cz) {
    ProtoChunk c = new ProtoChunk(new ChunkPos(cx, cz), UpgradeData.EMPTY, heights, biomes, null);
    await(generator.createBiomes(random, Blender.empty(), structures, c));
    await(generator.fillFromNoise(Blender.empty(), random, structures, c));
    if (generator.mode() == TerrainMode.LEGACY_FLAT) {
      return c; // the legacy slab already has its surface; the surface rule is skipped in the game
      // too
    }
    generator.buildSurface(
        c,
        new WorldGenerationContext(generator, c),
        random,
        structures,
        biomeManager,
        biomes,
        Blender.empty());
    return c;
  }

  /**
   * Waits for a generator future while still running server tasks: the noise fill asks the level
   * for structure data, which needs the server thread.
   */
  private <T> void await(java.util.concurrent.CompletableFuture<T> f) {
    f.join();
  }

  /** The finished chunk: natural ground plus landform shaping. */
  ProtoChunk full(int cx, int cz) {
    ProtoChunk c = natural(cx, cz);
    generator.shape(c, params, seed);
    return c;
  }

  static int topY(ChunkAccess c, int lx, int lz) {
    return c.getHeight(Heightmap.Types.WORLD_SURFACE_WG, lx, lz);
  }
}
