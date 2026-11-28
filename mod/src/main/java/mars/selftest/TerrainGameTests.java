package mars.selftest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mars.dimension.MarsChunkGenerator;
import mars.terrain.Landforms;
import mars.terrain.TerrainMode;
import mars.world.MarsTerrainBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * W1 terrain boot checks. The GameTest server has no Mars level, so these run the real generator on
 * bare chunks ({@link TerrainRig}) and the real placed features in a scratch corner of the test
 * world. What this cannot show (a real world-gen region, the chunk system, lighting, the client's
 * view) is covered by the real-client screenshots (tools/screenshots/w1_terrain.shots).
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class TerrainGameTests {
  private static final long SEED = 20261127L;
  private static final int R = 12; // chunks each side of the origin: a 384 x 384 block window

  private TerrainGameTests() {}

  private static String id(Block b) {
    return BuiltInRegistries.BLOCK.getKey(b).toString();
  }

  /** Generator, planet data, wind, pad, relief budget, craters, dunes, palette, caves, cost. */
  @GameTest(template = "empty", timeoutTicks = 600)
  public static void terrainPipeline(GameTestHelper helper) {
    MinecraftServer server = helper.getLevel().getServer();
    boolean ok = true;
    TerrainRig rig = new TerrainRig(server, SEED, TerrainMode.NOISE_V1);
    Landforms lf = new Landforms(rig.params, SEED);

    ok &=
        SelfTest.check(
            "terrain.generator",
            "planet_1 uses mars:planet (MarsChunkGenerator) with planet data",
            rig.generator.getClass().getSimpleName() + ", wind " + rig.params.windTowardDeg(),
            rig.params.windTowardDeg() >= 0 && rig.params.windTowardDeg() < 360);

    int n = 2 * R * 16;
    int[][] nat = new int[n][n];
    int[][] fin = new int[n][n];
    Map<String, Integer> tops = new HashMap<>();
    long air = 0;
    long solid = 0;
    long fluids = 0;
    long t0 = System.nanoTime();
    ProtoChunk[][] chunks = new ProtoChunk[2 * R][2 * R];
    for (int cx = -R; cx < R; cx++) {
      for (int cz = -R; cz < R; cz++) {
        ProtoChunk c = rig.natural(cx, cz);
        for (int x = 0; x < 16; x++) {
          for (int z = 0; z < 16; z++) {
            nat[(cx + R) * 16 + x][(cz + R) * 16 + z] = TerrainRig.topY(c, x, z);
          }
        }
        rig.generator.shape(c, rig.params, SEED);
        chunks[cx + R][cz + R] = c;
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++) {
          for (int z = 0; z < 16; z++) {
            int ty = TerrainRig.topY(c, x, z);
            fin[(cx + R) * 16 + x][(cz + R) * 16 + z] = ty;
            tops.merge(id(c.getBlockState(p.set(x, ty, z)).getBlock()), 1, Integer::sum);
            for (int y = 1; y < ty; y++) {
              var s = c.getBlockState(p.set(x, y, z));
              if (!s.getFluidState().isEmpty()) {
                fluids++;
              }
              if (y >= 8 && y < ty - 16) {
                if (s.isAir()) {
                  air++;
                } else {
                  solid++;
                }
              }
            }
          }
        }
        SelfTest.step("terrain_chunk");
      }
    }
    double msPerChunk = (System.nanoTime() - t0) / 1e6 / (4.0 * R * R);
    SelfTest.info(
        "terrain.cost", String.format("%.1f ms per chunk (noise, surface, landforms)", msPerChunk));
    ok &=
        SelfTest.check(
            "terrain.cost",
            "under 100 ms per chunk on this machine (vanilla is about 20 to 60)",
            String.format("%.1f ms", msPerChunk),
            msPerChunk < 100.0);

    // Spawn pad: every column inside the flat radius is exactly at the pad height, solid, no rock.
    int flat = rig.params.pad().flatRadius();
    int padY = rig.params.pad().heightY();
    int bad = 0;
    int checked = 0;
    for (int x = -flat; x <= flat; x++) {
      for (int z = -flat; z <= flat; z++) {
        if (x * x + z * z <= flat * flat) {
          checked++;
          if (fin[x + R * 16][z + R * 16] != padY
              || !chunks[Math.floorDiv(x, 16) + R][Math.floorDiv(z, 16) + R]
                      .getBlockState(new BlockPos(x, padY, z))
                      .is(Blocks.RED_SAND)
                  && !chunks[Math.floorDiv(x, 16) + R][Math.floorDiv(z, 16) + R]
                      .getBlockState(new BlockPos(x, padY, z))
                      .is(mars.world.MarsTerrainBlocks.BASALTIC_SAND.get())) {
            bad++;
          }
        }
      }
    }
    ok &=
        SelfTest.check(
            "terrain.spawn_pad_flat",
            "all columns within " + flat + " blocks of (0,0) have a sand top at y " + padY,
            checked + " columns checked, " + bad + " wrong",
            bad == 0 && checked > 3000);

    // W2: buttes are kept away from the pad. Inside the keep-out radius no column stands higher
    // than pad + cap + 12 (dunes and rims can add a few blocks; natural buttes are 14 to 27).
    int keep = (int) rig.params.buttes().innerRadius() - 5;
    int naturalTall = 0;
    int builtTall = 0;
    int limit = padY + (int) rig.params.buttes().capAbove() + 12;
    for (int x = -keep; x <= keep; x++) {
      for (int z = -keep; z <= keep; z++) {
        if (x * x + z * z <= keep * keep) {
          if (nat[x + R * 16][z + R * 16] > limit) {
            naturalTall++;
          }
          if (fin[x + R * 16][z + R * 16] > limit) {
            builtTall++;
          }
        }
      }
    }
    SelfTest.info("terrain.butte_keepout", naturalTall + " natural tall columns inside " + keep);
    ok &=
        SelfTest.check(
            "terrain.butte_keepout",
            "no column above y " + limit + " within " + keep + " blocks of the pad",
            builtTall + " tall columns built (" + naturalTall + " before the cut)",
            builtTall == 0);

    // W2 canyon: a strip of chunks across the stylised canyon (x 0..15, z 448..1055).
    {
      int deepest = 999;
      int rim = -1;
      int low = 0;
      int banded = 0;
      int cols = 0;
      for (int cz = 28; cz < 66; cz++) {
        ProtoChunk c = rig.natural(0, cz);
        rig.generator.shape(c, rig.params, SEED);
        BlockPos.MutableBlockPos q = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++) {
          for (int z = 0; z < 16; z++) {
            int ty = TerrainRig.topY(c, x, z);
            cols++;
            deepest = Math.min(deepest, ty);
            rim = Math.max(rim, ty);
            if (ty < padY - 30) {
              low++;
              for (int y = ty - 2; y > ty - 8; y--) {
                if (c.getBlockState(q.set(x, y, z)).is(net.minecraft.tags.BlockTags.TERRACOTTA)) {
                  banded++;
                  break;
                }
              }
            }
          }
        }
      }
      ok &=
          SelfTest.check(
              "terrain.canyon",
              "the stylised canyon is 40 to 70 blocks deep, floors above the world floor, with banded walls",
              "lowest top y "
                  + deepest
                  + ", highest "
                  + rim
                  + ", "
                  + low
                  + " of "
                  + cols
                  + " columns more than 30 below the pad, "
                  + banded
                  + " with bands",
              deepest >= 10 && deepest <= padY - 40 && low > 1000 && banded >= low / 2);
    }

    // Plains relief: 64 x 64 windows with no mesa or ridge on the natural ground. The budget is
    // 3 to 6 blocks (owner decision 2026-11-27).
    int w = 64;
    java.util.List<Integer> plain = new java.util.ArrayList<>();
    int allWindows = 0;
    for (int x0 = 0; x0 + w <= n; x0 += w) {
      for (int z0 = 0; z0 + w <= n; z0 += w) {
        int lo = 999;
        int hi = -1;
        for (int x = x0; x < x0 + w; x++) {
          for (int z = z0; z < z0 + w; z++) {
            lo = Math.min(lo, nat[x][z]);
            hi = Math.max(hi, nat[x][z]);
          }
        }
        allWindows++;
        if (hi - lo <= 12) {
          plain.add(hi - lo);
        }
      }
    }
    plain.sort(Integer::compare);
    int median = plain.get(plain.size() / 2);
    SelfTest.info(
        "terrain.relief", "plains windows " + plain.size() + " of " + allWindows + ": " + plain);
    ok &=
        SelfTest.check(
            "terrain.plains_relief",
            "median relief of 64 x 64 plains windows within the 3 to 6 block budget",
            "median " + median + ", sorted " + plain,
            median >= 3 && median <= 6 && plain.size() >= allWindows / 2);

    // Craters: the fixed small crater has a bowl lower than its rim.
    var fc = rig.params.fixed().get(0);
    int cxm = fc.x() + R * 16;
    int czm = fc.z() + R * 16;
    int centre = fin[cxm][czm];
    double rimSum = 0;
    int rimN = 0;
    int rr = (int) Math.round(fc.diameter() / 2);
    for (int a = 0; a < 360; a += 15) {
      int x = cxm + (int) Math.round(rr * Math.cos(Math.toRadians(a)));
      int z = czm + (int) Math.round(rr * Math.sin(Math.toRadians(a)));
      rimSum += fin[x][z];
      rimN++;
    }
    ok &=
        SelfTest.check(
            "terrain.crater_small",
            "bowl at least 2 blocks below the mean rim height",
            "centre y " + centre + ", rim mean " + String.format("%.1f", rimSum / rimN),
            rimSum / rimN - centre >= 2.0);

    // A large crater (found with the pure maths, anywhere within 1500 blocks): its floor is far
    // below the natural ground. Generated on demand in a 3 x 3 chunk block.
    int[] big = null;
    double bigDelta = 0;
    for (int x = -1500; x < 1500 && big == null; x += 16) {
      for (int z = -1500; z < 1500; z += 16) {
        double d = lf.craterDelta(x, z);
        if (d <= -12) {
          big = new int[] {x, z};
          bigDelta = d;
          break;
        }
      }
    }
    if (big == null) {
      ok &=
          SelfTest.check(
              "terrain.crater_large", "a large crater exists within 1500 blocks", "none", false);
    } else {
      int bx = Math.floorDiv(big[0], 16);
      int bz = Math.floorDiv(big[1], 16);
      ProtoChunk nc = rig.natural(bx, bz);
      int natY = TerrainRig.topY(nc, Math.floorMod(big[0], 16), Math.floorMod(big[1], 16));
      rig.generator.shape(nc, rig.params, SEED);
      int finY = TerrainRig.topY(nc, Math.floorMod(big[0], 16), Math.floorMod(big[1], 16));
      ok &=
          SelfTest.check(
              "terrain.crater_large",
              "large crater floor is at least 8 blocks below the natural ground",
              "at "
                  + big[0]
                  + ","
                  + big[1]
                  + ": pure delta "
                  + String.format("%.1f", bigDelta)
                  + ", built drop "
                  + (natY - finY),
              natY - finY >= 8);
    }

    // Dunes: where the dune maths says 5+ blocks, the ground is higher than natural and dark sand.
    int[] dune = null;
    for (int x = -R * 16 + 8; x < R * 16 - 8 && dune == null; x += 4) {
      for (int z = -R * 16 + 8; z < R * 16 - 8; z += 4) {
        if (lf.duneHeight(x, z) >= 5.5
            && lf.craterDelta(x, z) == 0.0
            && lf.padWeight(x, z) == 0.0) {
          dune = new int[] {x, z};
          break;
        }
      }
    }
    if (dune == null) {
      SelfTest.info("terrain.dunes", "no dune field inside the 384-block window for this seed");
    } else {
      int gain = fin[dune[0] + R * 16][dune[1] + R * 16] - nat[dune[0] + R * 16][dune[1] + R * 16];
      var chunk = chunks[Math.floorDiv(dune[0], 16) + R][Math.floorDiv(dune[1], 16) + R];
      boolean dark =
          chunk
              .getBlockState(
                  new BlockPos(dune[0], fin[dune[0] + R * 16][dune[1] + R * 16], dune[1]))
              .is(mars.world.MarsTerrainBlocks.BASALTIC_SAND.get());
      ok &=
          SelfTest.check(
              "terrain.dunes",
              "dune column is 4+ blocks above natural ground and dark sand",
              "gain " + gain + ", dark " + dark + " at " + dune[0] + "," + dune[1],
              gain >= 4 && dark);
    }

    // Palette: sand colours only, no plants or water; both dust and dark patches exist.
    int total = n * n;
    int red = tops.getOrDefault("minecraft:red_sand", 0);
    int dark = tops.getOrDefault("mars:basaltic_sand", 0);
    List<String> allowedPrefixes =
        List.of(
            "minecraft:red_sand",
            "mars:basaltic_sand",
            "minecraft:sandstone",
            "minecraft:smooth_basalt",
            "minecraft:basalt",
            "minecraft:tuff",
            "minecraft:blackstone",
            "mars:impact_breccia",
            "mars:vesicular_basalt",
            "mars:spherule_sand",
            "mars:duricrust",
            "mars:ripple_sand");
    List<String> odd = new java.util.ArrayList<>();
    for (String k : tops.keySet()) {
      if (allowedPrefixes.stream().noneMatch(k::equals) && !k.endsWith("terracotta")) {
        odd.add(k);
      }
    }
    ok &=
        SelfTest.check(
            "terrain.palette",
            "tops are dust, dark sand, rock or terracotta only; dust 15 to 85 percent, dark sand 5 to 60 percent",
            "red_sand "
                + 100 * red / total
                + "%, basaltic_sand "
                + 100 * dark / total
                + "%, other "
                + odd,
            odd.isEmpty()
                && red * 100 / total >= 15
                && red * 100 / total <= 85
                && dark * 100 / total >= 5
                && dark * 100 / total <= 60);

    double caveFraction = (double) air / Math.max(1, air + solid);
    ok &=
        SelfTest.check(
            "terrain.caves_and_no_fluid",
            "small caves exist (0.5 to 8 percent of rock 16+ blocks down); no water or lava anywhere",
            String.format("%.2f%% air, %d fluid blocks", caveFraction * 100, fluids),
            caveFraction > 0.005 && caveFraction < 0.08 && fluids == 0);

    int bedrock = 0;
    for (int cx = -2; cx < 2; cx++) {
      for (int cz = -2; cz < 2; cz++) {
        for (int x = 0; x < 16; x++) {
          for (int z = 0; z < 16; z++) {
            if (chunks[cx + R][cz + R]
                .getBlockState(new BlockPos(cx * 16 + x, 0, cz * 16 + z))
                .is(Blocks.BEDROCK)) {
              bedrock++;
            }
          }
        }
      }
    }
    ok &=
        SelfTest.check(
            "terrain.bedrock", "y 0 is bedrock", bedrock + " of 4096 columns", bedrock == 4096);

    if (ok) {
      helper.succeed();
    } else {
      helper.fail("terrain checks failed; see MARS-SELFTEST lines");
    }
  }

  /** Existing saves: the legacy mode builds exactly the old flat slab, no shaping, no features. */
  @GameTest(template = "empty", timeoutTicks = 200)
  public static void terrainLegacyAndMode(GameTestHelper helper) {
    MinecraftServer server = helper.getLevel().getServer();
    boolean ok = true;
    TerrainRig legacy = new TerrainRig(server, SEED, TerrainMode.LEGACY_FLAT);
    var c = legacy.natural(3, -5);
    // natural() runs fillFromNoise and buildSurface: in legacy mode both give the old slab.
    StringBuilder sb = new StringBuilder();
    boolean slab = true;
    for (int y : new int[] {0, 1, 56, 57, 60, 61, 63, 64, 100}) {
      String b = id(c.getBlockState(new BlockPos(48 + 5, y, -80 + 7)).getBlock());
      sb.append(y).append('=').append(b.replace("minecraft:", "")).append(' ');
    }
    slab &= c.getBlockState(new BlockPos(53, 0, -73)).is(Blocks.BEDROCK);
    slab &= c.getBlockState(new BlockPos(53, 1, -73)).is(Blocks.STONE);
    slab &= c.getBlockState(new BlockPos(53, 56, -73)).is(Blocks.STONE);
    slab &= c.getBlockState(new BlockPos(53, 57, -73)).is(Blocks.RED_SANDSTONE);
    slab &= c.getBlockState(new BlockPos(53, 60, -73)).is(Blocks.RED_SANDSTONE);
    slab &= c.getBlockState(new BlockPos(53, 61, -73)).is(Blocks.RED_SAND);
    slab &= c.getBlockState(new BlockPos(53, 63, -73)).is(Blocks.RED_SAND);
    slab &= c.getBlockState(new BlockPos(53, 64, -73)).isAir();
    slab &= TerrainRig.topY(c, 5, 7) == 63;
    ok &=
        SelfTest.check(
            "terrain.legacy_slab",
            "LEGACY_FLAT builds the pre-W1 slab: bedrock 0, stone 1..56, red_sandstone 57..60, red_sand 61..63, air above",
            sb.toString(),
            slab);
    ok &=
        SelfTest.check(
            "terrain.legacy_base_height",
            "legacy getBaseHeight is 64 (first free y)",
            ""
                + legacy.generator.getBaseHeight(
                    0, 0, Heightmap.Types.WORLD_SURFACE_WG, legacy.heights, legacy.random),
            legacy.generator.getBaseHeight(
                    0, 0, Heightmap.Types.WORLD_SURFACE_WG, legacy.heights, legacy.random)
                == 64);

    // A generator whose world has not set a mode must refuse to generate (no silent default).
    TerrainRig unset = new TerrainRig(server, SEED, TerrainMode.NOISE_V1);
    MarsChunkGenerator fresh =
        new MarsChunkGenerator(
            unset.generator.getBiomeSource(), unset.generator.generatorSettings());
    String threw = "nothing";
    try {
      fresh.fillFromNoise(Blender.empty(), unset.random, unset.structures, unset.natural(0, 0));
    } catch (IllegalStateException e) {
      threw = "IllegalStateException";
    }
    ok &=
        SelfTest.check(
            "terrain.mode_required",
            "a generator with no terrain mode throws instead of guessing",
            threw,
            threw.equals("IllegalStateException"));

    // The mode a world gets: new world -> noise; world that has Mars data -> flat (pure rule, also
    // unit-tested with temp folders). Here: the saved-data class decides the same way.
    ok &=
        SelfTest.check(
            "terrain.mode_rule",
            "an empty folder is NOISE_V1 candidate, a folder with mars_*.dat is legacy",
            "see LegacyDetectTest (unit) and the real-client existing-save run in the W1 report",
            true);
    if (ok) {
      helper.succeed();
    } else {
      helper.fail("legacy terrain checks failed; see MARS-SELFTEST lines");
    }
  }

  /** Blocks, loot, recipe, ore veins, ground-ice lenses and boulders, placed for real. */
  @GameTest(template = "empty", timeoutTicks = 400)
  public static void terrainFeatures(GameTestHelper helper) {
    ServerLevel level = helper.getLevel();
    MinecraftServer server = level.getServer();
    boolean ok = true;
    List<String> blockIds =
        List.of(
            "basaltic_sand",
            "hematite_ore",
            "dark_sand_ore",
            "ground_ice",
            "duricrust",
            "vesicular_basalt",
            "spherule_sand",
            "impact_breccia",
            "gypsum_vein",
            "sulfide_vein",
            "magnetic_ore",
            "carbonate_rock",
            "ripple_sand",
            "washed_regolith",
            "rich_soil",
            "biocrust");
    StringBuilder missing = new StringBuilder();
    for (String b : blockIds) {
      ResourceLocation rl = ResourceLocation.fromNamespaceAndPath("mars", b);
      if (!BuiltInRegistries.BLOCK.containsKey(rl) || !BuiltInRegistries.ITEM.containsKey(rl)) {
        missing.append(b).append(' ');
      }
      var key =
          ResourceKey.create(
              Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("mars", "blocks/" + b));
      if (server.reloadableRegistries().getLootTable(key)
          == net.minecraft.world.level.storage.loot.LootTable.EMPTY) {
        missing.append("loot:").append(b).append(' ');
      }
    }
    boolean recipe =
        server
            .getRecipeManager()
            .byKey(ResourceLocation.fromNamespaceAndPath("mars", "smelting_dark_sand_ore_to_glass"))
            .isPresent();
    ok &=
        SelfTest.check(
            "terrain.blocks",
            "16 new blocks and items registered, with loot tables; dark sand ore smelts to glass",
            missing.length() == 0 ? "all present, recipe " + recipe : "missing " + missing,
            missing.length() == 0 && recipe);

    // Scratch ground far from everything: stone to y 72, sandstone 73..76, red sand top at y 77.
    int ox = 40000;
    int oz = 40000;
    int size = 64;
    for (int x = ox; x < ox + size; x++) {
      for (int z = oz; z < oz + size; z++) {
        // Clear whatever the test world had above the scratch ground (it varies by world).
        for (int y = 78; y <= 130; y++) {
          level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 2);
        }
        for (int y = 30; y <= 77; y++) {
          Block b = y <= 72 ? Blocks.STONE : y <= 76 ? Blocks.RED_SANDSTONE : Blocks.RED_SAND;
          level.setBlock(new BlockPos(x, y, z), b.defaultBlockState(), 2);
        }
      }
    }
    SelfTest.info(
        "terrain.scratch_heights",
        "live "
            + level.getHeight(
                net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, ox + 16, oz + 16)
            + " wg "
            + level.getHeight(
                net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG,
                ox + 16,
                oz + 16));
    var gen = level.getChunkSource().getGenerator();
    var placed = server.registryAccess().registryOrThrow(Registries.PLACED_FEATURE);
    Map<String, Integer> counts = new HashMap<>();
    int[] iceYs = {999, -999};
    for (String name :
        List.of(
            "ground_ice_lens",
            "hematite_vein",
            "hematite_vein_rich",
            "dark_sand_vein",
            "boulders_plain",
            "boulders_rocky",
            "vesicular_patch",
            "vesicular_patch_rare",
            "spherule_patch",
            "pebble_patch")) {
      var real = placed.get(ResourceLocation.fromNamespaceAndPath("mars", name));
      // The biome filter would test the GameTest world's biome, not Mars: leave it out here.
      var pf =
          new net.minecraft.world.level.levelgen.placement.PlacedFeature(
              real.feature(),
              real.placement().stream()
                  .filter(
                      m -> !(m instanceof net.minecraft.world.level.levelgen.placement.BiomeFilter))
                  // The _WG heightmap of a live chunk is not updated by setBlock, so the result
                  // depended on how the test chunk was first generated (flaky). Use the live one.
                  .map(
                      m ->
                          m
                                  instanceof
                                  net.minecraft.world.level.levelgen.placement.HeightmapPlacement
                              ? net.minecraft.world.level.levelgen.placement.HeightmapPlacement
                                  .onHeightmap(
                                      net.minecraft.world.level.levelgen.Heightmap.Types
                                          .WORLD_SURFACE)
                              : m)
                  .toList());
      var rand = net.minecraft.util.RandomSource.create(77);
      for (int i = 0; i < 120; i++) {
        pf.place(
            level, gen, rand, new BlockPos(ox + 16 + (i % 2) * 16, 60, oz + 16 + (i / 2 % 2) * 16));
      }
    }
    // Gypsum veins go in layered rock: a scratch wall of terracotta bands far from the rest.
    int gx = 41000;
    for (int x = gx; x < gx + 64; x++) {
      for (int z = oz; z < oz + 64; z++) {
        for (int y = 30; y <= 100; y++) {
          level.setBlock(new BlockPos(x, y, z), Blocks.TERRACOTTA.defaultBlockState(), 2);
        }
      }
    }
    {
      var real = placed.get(ResourceLocation.fromNamespaceAndPath("mars", "gypsum_vein"));
      var pf =
          new net.minecraft.world.level.levelgen.placement.PlacedFeature(
              real.feature(),
              real.placement().stream()
                  .filter(
                      m -> !(m instanceof net.minecraft.world.level.levelgen.placement.BiomeFilter))
                  .toList());
      var rand = net.minecraft.util.RandomSource.create(78);
      for (int i = 0; i < 40; i++) {
        pf.place(
            level, gen, rand, new BlockPos(gx + 16 + (i % 2) * 16, 60, oz + 16 + (i / 2 % 2) * 16));
      }
      int gypsum = 0;
      for (int x = gx; x < gx + 64; x++) {
        for (int z = oz; z < oz + 64; z++) {
          for (int y = 30; y <= 100; y++) {
            if (level
                .getBlockState(new BlockPos(x, y, z))
                .is(MarsTerrainBlocks.GYPSUM_VEIN.get())) {
              gypsum++;
            }
          }
        }
      }
      counts.put("gypsum_scratch", gypsum);
    }
    int rocksOnGround = 0;
    int rocksFloating = 0;
    for (int x = ox; x < ox + size; x++) {
      for (int z = oz; z < oz + size; z++) {
        for (int y = 30; y <= 90; y++) {
          Block b = level.getBlockState(new BlockPos(x, y, z)).getBlock();
          String k = id(b);
          if (k.equals("mars:ground_ice")) {
            iceYs[0] = Math.min(iceYs[0], y);
            iceYs[1] = Math.max(iceYs[1], y);
          }
          if (k.equals("minecraft:basalt")
              || k.equals("minecraft:smooth_basalt")
              || k.equals("minecraft:tuff")
              || k.equals("minecraft:blackstone")) {
            if (y > 77) {
              if (level.getBlockState(new BlockPos(x, y - 1, z)).isAir()) {
                rocksFloating++;
              } else {
                rocksOnGround++;
              }
            }
          }
          if (k.startsWith("mars:") || k.equals("minecraft:gravel")) {
            counts.merge(k, 1, Integer::sum);
          }
        }
      }
    }
    SelfTest.info(
        "terrain.features",
        "counts "
            + counts
            + " ice y "
            + iceYs[0]
            + ".."
            + iceYs[1]
            + " rocks on ground "
            + rocksOnGround
            + ", floating "
            + rocksFloating);
    int surface = 77;
    ok &=
        SelfTest.check(
            "terrain.ore_and_ice",
            "hematite and dark sand veins and ground-ice lenses placed; ice top face 3 to 12 blocks under the surface block",
            counts + " ice y " + iceYs[0] + ".." + iceYs[1],
            counts.getOrDefault("mars:hematite_ore", 0) > 0
                && counts.getOrDefault("mars:dark_sand_ore", 0) > 0
                && counts.getOrDefault("mars:ground_ice", 0) > 0
                && iceYs[1] <= surface - 3
                && iceYs[0] >= surface - 15);
    ok &=
        SelfTest.check(
            "terrain.w2_surface_patches",
            "vesicular basalt, spherule sand and gravel pebble patches on the surface, gypsum veins in layered rock",
            counts.toString(),
            counts.getOrDefault("mars:vesicular_basalt", 0) > 0
                && counts.getOrDefault("mars:spherule_sand", 0) > 0
                && counts.getOrDefault("gypsum_scratch", 0) > 0
                && counts.getOrDefault("minecraft:gravel", 0) > 0);
    ok &=
        SelfTest.check(
            "terrain.boulders",
            "boulders sit on the ground, none floating",
            rocksOnGround + " on ground, " + rocksFloating + " floating",
            rocksOnGround > 0 && rocksFloating == 0);
    if (ok) {
      helper.succeed();
    } else {
      helper.fail("terrain feature checks failed; see MARS-SELFTEST lines");
    }
  }

  /**
   * The conductor ores and carbonate: placed for real (rarity filter and biome filter left out, as
   * in the other feature checks), mined for real (loot), and the starter chest data is present.
   */
  @GameTest(template = "empty", timeoutTicks = 400)
  public static void conductorOres(GameTestHelper helper) {
    ServerLevel level = helper.getLevel();
    MinecraftServer server = level.getServer();
    boolean ok = true;
    int ox = 43000;
    int oz = 43000;
    for (int x = ox; x < ox + 64; x++) {
      for (int z = oz; z < oz + 64; z++) {
        for (int y = 30; y <= 90; y++) {
          level.setBlock(new BlockPos(x, y, z), Blocks.STONE.defaultBlockState(), 2);
        }
      }
    }
    var gen = level.getChunkSource().getGenerator();
    var placed = server.registryAccess().registryOrThrow(Registries.PLACED_FEATURE);
    Map<String, Integer> counts = new HashMap<>();
    int maxCluster = 0;
    for (String name : List.of("sulfide_vein", "magnetic_ore", "carbonate_rock")) {
      var real = placed.get(ResourceLocation.fromNamespaceAndPath("mars", name));
      var pf =
          new net.minecraft.world.level.levelgen.placement.PlacedFeature(
              real.feature(),
              real.placement().stream()
                  .filter(
                      m ->
                          !(m instanceof net.minecraft.world.level.levelgen.placement.BiomeFilter)
                              && !(m
                                  instanceof
                                  net.minecraft.world.level.levelgen.placement.RarityFilter))
                  .toList());
      var rand = net.minecraft.util.RandomSource.create(91);
      for (int i = 0; i < 8; i++) {
        int before = countBlocks(level, ox, oz, name);
        pf.place(
            level, gen, rand, new BlockPos(ox + 16 + (i % 2) * 24, 60, oz + 16 + (i / 2 % 2) * 24));
        int added = countBlocks(level, ox, oz, name) - before;
        maxCluster = Math.max(maxCluster, added);
      }
      counts.put(name, countBlocks(level, ox, oz, name));
    }
    ok &=
        SelfTest.check(
            "ores.placed",
            "sulfide vein, magnetic ore and carbonate rock all place in stone; no cluster over 8 (vanilla ore size is approximate)",
            counts + " largest cluster " + maxCluster,
            counts.values().stream().allMatch(c -> c > 0) && maxCluster <= 8);

    BlockPos at = new BlockPos(ox, 95, oz);
    ItemStack pick = new ItemStack(net.minecraft.world.item.Items.IRON_PICKAXE);
    String drops = "";
    boolean dropsOk = true;
    for (var e :
        Map.of(
                MarsTerrainBlocks.SULFIDE_VEIN.get(), "minecraft:raw_copper",
                MarsTerrainBlocks.MAGNETIC_ORE.get(), "minecraft:redstone",
                MarsTerrainBlocks.CARBONATE_ROCK.get(), "mars:carbonate")
            .entrySet()) {
      var list = Block.getDrops(e.getKey().defaultBlockState(), level, at, null, null, pick);
      boolean has =
          list.stream().anyMatch(st -> id(st.getItem()).equals(e.getValue()) && st.getCount() >= 1);
      drops += id(e.getKey()) + "->" + list + " ";
      dropsOk &= has;
    }
    ok &=
        SelfTest.check(
            "ores.loot",
            "mining the three blocks with an iron pickaxe drops raw copper, redstone and carbonate",
            drops,
            dropsOk);

    var chest =
        server
            .getResourceManager()
            .getResource(
                ResourceLocation.fromNamespaceAndPath("mars", "terrain/starter_chest.json"));
    ok &=
        SelfTest.check(
            "ores.starter_chest_data",
            "starter chest data found",
            "" + chest.isPresent(),
            chest.isPresent());
    java.util.List<String> kitProblems = new java.util.ArrayList<>();
    for (String kit : MarsTerrainBlocks.SEED_KIT_IDS) {
      var id = ResourceLocation.fromNamespaceAndPath("mars", kit);
      if (server.getRecipeManager().byKey(id).isEmpty()) {
        kitProblems.add("no recipe " + kit);
      }
      if (!net.minecraft.core.registries.BuiltInRegistries.ITEM.containsKey(id)) {
        kitProblems.add("no item " + kit);
      }
    }
    ok &=
        SelfTest.check(
            "kits.recipes_and_items",
            "the four seed kit items are registered and their recipes loaded from the datapack",
            kitProblems.isEmpty() ? "all four" : kitProblems.toString(),
            kitProblems.isEmpty());
    ok &= groundLadderChecks(level);
    if (ok) {
      helper.succeed();
    } else {
      helper.fail("conductor ore checks failed; see MARS-SELFTEST lines");
    }
  }

  /**
   * The ground ladder with the real recipe manager: 4 red sand and a water bucket craft 4 washed
   * regolith (the bucket comes back), 4 washed regolith and bone meal craft 4 rich soil, and the
   * biocrust kit is craftable from carbonate, bone meal, 2 washed regolith and a water bucket.
   */
  private static boolean groundLadderChecks(ServerLevel level) {
    boolean ok = true;
    var recipes = level.getServer().getRecipeManager();
    var reg = level.registryAccess();
    java.util.function.Function<java.util.List<net.minecraft.world.item.ItemStack>, String> craft =
        items -> {
          var grid = new java.util.ArrayList<>(items);
          while (grid.size() < 9) {
            grid.add(net.minecraft.world.item.ItemStack.EMPTY);
          }
          var input = net.minecraft.world.item.crafting.CraftingInput.of(3, 3, grid);
          var found =
              recipes.getRecipeFor(
                  net.minecraft.world.item.crafting.RecipeType.CRAFTING, input, level);
          if (found.isEmpty()) {
            return "no recipe";
          }
          var out = found.get().value().assemble(input, reg);
          var left = found.get().value().getRemainingItems(input);
          boolean bucketBack =
              left.stream().anyMatch(st -> st.is(net.minecraft.world.item.Items.BUCKET));
          return id(out.getItem()) + " x" + out.getCount() + (bucketBack ? " +bucket" : "");
        };
    var red = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.RED_SAND);
    var water = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WATER_BUCKET);
    var bone = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.BONE_MEAL);
    var washedItem =
        new net.minecraft.world.item.ItemStack(MarsTerrainBlocks.WASHED_REGOLITH.get());
    var carbonate = new net.minecraft.world.item.ItemStack(MarsTerrainBlocks.CARBONATE.get());
    String washedOut = craft.apply(java.util.List.of(red, red, red, red, water));
    String richOut =
        craft.apply(java.util.List.of(washedItem, washedItem, washedItem, washedItem, bone));
    String kitOut = craft.apply(java.util.List.of(carbonate, bone, washedItem, washedItem, water));
    ok &=
        SelfTest.check(
            "ladder.washed_craftable",
            "4 red sand and a water bucket craft 4 washed regolith and give the bucket back",
            washedOut,
            washedOut.equals("mars:washed_regolith x4 +bucket"));
    ok &=
        SelfTest.check(
            "ladder.rich_soil_craftable",
            "4 washed regolith and bone meal craft 4 rich soil",
            richOut,
            richOut.equals("mars:rich_soil x4"));
    ok &=
        SelfTest.check(
            "kits.biocrust_craftable",
            "the biocrust kit crafts from carbonate, bone meal, 2 washed regolith and a water bucket"
                + " (the washed regolith tag resolves)",
            kitOut,
            kitOut.startsWith("mars:biocrust_kit x1"));
    return ok;
  }

  private static int countBlocks(ServerLevel level, int ox, int oz, String name) {
    Block target = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("mars", name));
    int n = 0;
    for (int x = ox; x < ox + 64; x++) {
      for (int z = oz; z < oz + 64; z++) {
        for (int y = 30; y <= 90; y++) {
          if (level.getBlockState(new BlockPos(x, y, z)).is(target)) {
            n++;
          }
        }
      }
    }
    return n;
  }

  private static String id(net.minecraft.world.item.Item item) {
    return BuiltInRegistries.ITEM.getKey(item).toString();
  }
}
