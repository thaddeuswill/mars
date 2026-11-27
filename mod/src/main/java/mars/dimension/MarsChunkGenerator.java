package mars.dimension;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import mars.terrain.Landforms;
import mars.terrain.LegacyFlat;
import mars.terrain.TerrainMode;
import mars.terrain.TerrainParams;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

/**
 * The Mars chunk generator (ARCHITECTURE.md 2.20). It is the vanilla noise generator (data: noise
 * settings, density functions, surface rule, biomes) plus one extra pass: after the carvers and
 * before any feature, craters, dunes and the starting pad reshape the columns of the chunk
 * (landform maths in the pure {@link mars.terrain.Landforms}). A world that already had Mars data
 * before W1 runs in {@link TerrainMode#LEGACY_FLAT}: the old flat slab, no features, no shaping.
 * The mode is set by {@link TerrainModeData} when the world's Mars level loads; generating before
 * that is a bug and throws.
 */
public final class MarsChunkGenerator extends NoiseBasedChunkGenerator {
  public static final MapCodec<MarsChunkGenerator> CODEC =
      RecordCodecBuilder.mapCodec(
          i ->
              i.group(
                      BiomeSource.CODEC.fieldOf("biome_source").forGetter(g -> g.getBiomeSource()),
                      NoiseGeneratorSettings.CODEC
                          .fieldOf("settings")
                          .forGetter(g -> g.generatorSettings()))
                  .apply(i, i.stable(MarsChunkGenerator::new)));

  private volatile TerrainMode mode;
  private volatile Shaper shaper;

  public MarsChunkGenerator(
      BiomeSource biomeSource, net.minecraft.core.Holder<NoiseGeneratorSettings> settings) {
    super(biomeSource, settings);
  }

  @Override
  protected MapCodec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  public void setMode(TerrainMode mode) {
    this.mode = mode;
  }

  public TerrainMode mode() {
    TerrainMode m = mode;
    if (m == null) {
      throw new IllegalStateException(
          "Mars terrain mode not set: the world's Mars level must load before its chunks generate");
    }
    return m;
  }

  // ---- generation ----

  @Override
  public CompletableFuture<ChunkAccess> fillFromNoise(
      Blender blender, RandomState random, StructureManager structures, ChunkAccess chunk) {
    if (mode() == TerrainMode.LEGACY_FLAT) {
      fillLegacyFlat(chunk);
      return CompletableFuture.completedFuture(chunk);
    }
    return super.fillFromNoise(blender, random, structures, chunk);
  }

  @Override
  public void buildSurface(
      WorldGenRegion region, StructureManager structures, RandomState random, ChunkAccess chunk) {
    if (mode() == TerrainMode.NOISE_V1) {
      super.buildSurface(region, structures, random, chunk);
    }
  }

  @Override
  public void applyCarvers(
      WorldGenRegion region,
      long seed,
      RandomState random,
      BiomeManager biomes,
      StructureManager structures,
      ChunkAccess chunk,
      GenerationStep.Carving step) {
    if (mode() != TerrainMode.NOISE_V1) {
      return;
    }
    super.applyCarvers(region, seed, random, biomes, structures, chunk, step);
    if (step == GenerationStep.Carving.AIR) {
      ResourceLocation dim = region.getLevel().dimension().location();
      TerrainParams params = PlanetTerrainData.get(dim);
      if (params == null) {
        throw new IllegalStateException(
            "No planet terrain data for " + dim + " (data/mars/planets)");
      }
      shape(chunk, params, region.getSeed());
    }
  }

  @Override
  public void applyBiomeDecoration(
      WorldGenLevel level, ChunkAccess chunk, StructureManager structures) {
    if (mode() == TerrainMode.NOISE_V1) {
      super.applyBiomeDecoration(level, chunk, structures);
    }
  }

  @Override
  public int getBaseHeight(
      int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
    if (mode() == TerrainMode.LEGACY_FLAT) {
      return LegacyFlat.topY(level.getMinBuildHeight()) + 1;
    }
    return super.getBaseHeight(x, z, type, level, random);
  }

  @Override
  public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState random) {
    if (mode() == TerrainMode.LEGACY_FLAT) {
      int min = level.getMinBuildHeight();
      BlockState[] states = new BlockState[level.getHeight()];
      for (int i = 0; i < states.length; i++) {
        String id = LegacyFlat.blockAt(min + i, min);
        states[i] = id == null ? Blocks.AIR.defaultBlockState() : block(id);
      }
      return new NoiseColumn(min, states);
    }
    return super.getBaseColumn(x, z, level, random);
  }

  // ---- legacy flat slab ----

  /** Fills the chunk with exactly the pre-W1 slab (bedrock, stone, red sandstone, red sand). */
  public static void fillLegacyFlat(ChunkAccess chunk) {
    int min = chunk.getMinBuildHeight();
    Heightmap ocean = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
    Heightmap surface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
    int top = LegacyFlat.topY(min);
    for (int y = min; y <= top; y++) {
      BlockState state = block(LegacyFlat.blockAt(y, min));
      LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(y));
      for (int x = 0; x < 16; x++) {
        for (int z = 0; z < 16; z++) {
          section.setBlockState(x, y & 15, z, state, false);
          ocean.update(x, y, z, state);
          surface.update(x, y, z, state);
        }
      }
    }
  }

  private static BlockState block(String id) {
    return BuiltInRegistries.BLOCK
        .getOptional(ResourceLocation.parse(id))
        .orElseThrow(() -> new IllegalStateException("Unknown block in terrain data: " + id))
        .defaultBlockState();
  }

  // ---- landform shaping (crater, dune, pad) ----

  /** Block states resolved from the planet data once per data load. */
  private record ShapeBlocks(
      BlockState rim, BlockState ejecta, BlockState duneSand, List<BlockState> rocks) {}

  private record Shaper(TerrainParams params, long seed, Landforms landforms, ShapeBlocks blocks) {}

  private Shaper shaperFor(TerrainParams params, long seed) {
    Shaper s = shaper;
    if (s == null || s.params != params || s.seed != seed) {
      s =
          new Shaper(
              params,
              seed,
              new Landforms(params, seed),
              new ShapeBlocks(
                  block(params.surface().rimBlock()),
                  block(params.surface().ejectaBlock()),
                  block(params.surface().duneSand()),
                  params.rocks().blocks().stream().map(MarsChunkGenerator::block).toList()));
      shaper = s;
    }
    return s;
  }

  /**
   * Reshapes the columns of one chunk. Public so that a boot check can run it on a bare chunk.
   * Cost: one pass over 256 columns, plus block writes only where the ground changes.
   */
  public void shape(ChunkAccess chunk, TerrainParams params, long seed) {
    Shaper s = shaperFor(params, seed);
    Landforms lf = s.landforms;
    int minX = chunk.getPos().getMinBlockX();
    int minZ = chunk.getPos().getMinBlockZ();
    int minY = chunk.getMinBuildHeight();
    int maxY = chunk.getMaxBuildHeight();
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    for (int lx = 0; lx < 16; lx++) {
      for (int lz = 0; lz < 16; lz++) {
        int x = minX + lx;
        int z = minZ + lz;
        int topY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, lx, lz);
        Landforms.Column col = lf.at(x, z, topY);
        int target = Math.max(minY + 6, Math.min(maxY - 8, col.targetY()));
        BlockState top = chunk.getBlockState(pos.set(x, topY, z));
        boolean dune = col.kind() == Landforms.Kind.DUNE;
        if (target > topY) {
          BlockState fill = dune ? s.blocks.duneSand : top;
          for (int y = topY + 1; y <= target; y++) {
            chunk.setBlockState(pos.set(x, y, z), fill, false);
          }
        } else if (target < topY) {
          for (int y = topY; y > target; y--) {
            chunk.setBlockState(pos.set(x, y, z), Blocks.AIR.defaultBlockState(), false);
          }
          chunk.setBlockState(pos.set(x, target, z), top, false);
          chunk.setBlockState(pos.set(x, target - 1, z), top, false);
        }
        switch (col.kind()) {
          case RIM -> chunk.setBlockState(pos.set(x, target, z), s.blocks.rim, false);
          case EJECTA -> {
            if (lf.roll(x, z, 1) < params.surface().ejectaBlockChance() * col.ejectaStrength()) {
              chunk.setBlockState(pos.set(x, target, z), s.blocks.ejecta, false);
            }
          }
          case DUNE -> {
            for (int y = target; y > target - 2; y--) {
              chunk.setBlockState(pos.set(x, y, z), s.blocks.duneSand, false);
            }
          }
          default -> {}
        }
        if (col.rockChance() > 0 && lf.roll(x, z, 2) < col.rockChance()) {
          List<BlockState> rocks = s.blocks.rocks;
          int pick = (int) (lf.roll(x, z, 3) * rocks.size());
          chunk.setBlockState(pos.set(x, target + 1, z), rocks.get(pick), false);
        }
      }
    }
    Heightmap.primeHeightmaps(
        chunk, EnumSet.of(Heightmap.Types.WORLD_SURFACE_WG, Heightmap.Types.OCEAN_FLOOR_WG));
  }
}
