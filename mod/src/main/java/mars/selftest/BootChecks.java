package mars.selftest;

import com.mojang.authlib.GameProfile;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import mars.dimension.MarsDimensions;
import mars.dimension.ice.IceResult;
import mars.dimension.ice.IceRule;
import mars.machines.MarsMachines;
import mars.machines.SolarPanelBlock;
import mars.planets.TravelCommands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

/**
 * Stage 1 boot checks, shared by the GameTest run and the dedicated-server self-test. Each check
 * prints what it expected and what it read.
 */
public final class BootChecks {
  private BootChecks() {}

  /** Round solar array footprint rule (ARCHITECTURE.md 2.18), in the overworld at y 200. */
  static boolean footprintChecks(MinecraftServer server) {
    boolean ok = true;
    ServerLevel level = server.overworld();
    BlockPos c = new BlockPos(8, 200, 8);
    BlockState panel = MarsMachines.SOLAR_PANEL.get().defaultBlockState();
    level.setBlockAndUpdate(c, panel);
    ok &=
        SelfTest.check(
            "footprint.fillers",
            "8 canopy cells pointing at the centre",
            describeFootprint(level, c),
            countCanopy(level, c) == 8);

    BlockState corner = level.getBlockState(c.offset(-1, 0, -1));
    String cornerShape = corner.getShape(level, c.offset(-1, 0, -1)).bounds().toString();
    ok &=
        SelfTest.check(
            "footprint.corner_shape",
            "corner cell: x and z 0.4375..1.0, y 0.625..0.6875 (array edge only)",
            cornerShape,
            cornerShape.contains("0.4375")
                && cornerShape.contains("0.625")
                && cornerShape.contains("0.6875"));
    String centreShape = level.getBlockState(c).getCollisionShape(level, c).bounds().toString();
    ok &=
        SelfTest.check(
            "footprint.centre_shape",
            "centre collision 0..1 wide, 0..0.6875 high (base, post, array)",
            centreShape,
            centreShape.contains("0.0, 0.0, 0.0") && centreShape.contains("0.6875"));

    level.setBlockAndUpdate(c, panel.setValue(SolarPanelBlock.DUST_STAGE, 3));
    ok &=
        SelfTest.check(
            "footprint.dust_change_keeps_fillers",
            "still 8 canopy cells after dust_stage=3",
            "count=" + countCanopy(level, c),
            countCanopy(level, c) == 8);

    // Breaking an overhang cell as a survival player breaks the whole array and drops one panel.
    net.neoforged.neoforge.common.util.FakePlayer fake =
        net.neoforged.neoforge.common.util.FakePlayerFactory.get(
            level,
            new com.mojang.authlib.GameProfile(
                java.util.UUID.fromString("6d617273-0000-4000-8000-000000000002"),
                "[MarsFootprint]"));
    fake.setPos(c.getX() + 0.5, c.getY() + 1, c.getZ() - 2.5);
    boolean destroyed = fake.gameMode.destroyBlock(c.offset(1, 0, -1));
    java.util.List<net.minecraft.world.entity.item.ItemEntity> drops =
        level.getEntitiesOfClass(
            net.minecraft.world.entity.item.ItemEntity.class,
            new net.minecraft.world.phys.AABB(c).inflate(3));
    String dropRead = drops.stream().map(e -> e.getItem().toString()).toList().toString();
    ok &=
        SelfTest.check(
            "footprint.break_overhang",
            "centre and all 8 cells gone, one mars:solar_panel dropped",
            "destroyed="
                + destroyed
                + ", centre="
                + level.getBlockState(c)
                + ", cells="
                + countCanopyAny(level, c)
                + ", drops="
                + dropRead,
            level.getBlockState(c).isAir()
                && countCanopyAny(level, c) == 0
                && drops.size() == 1
                && drops.get(0).getItem().is(MarsMachines.SOLAR_PANEL.get().asItem()));
    drops.forEach(e -> e.discard());

    // Removing the centre clears the cells.
    level.setBlockAndUpdate(c, panel);
    level.setBlockAndUpdate(c, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
    ok &=
        SelfTest.check(
            "footprint.remove_centre",
            "no canopy cells left",
            "cells=" + countCanopyAny(level, c),
            countCanopyAny(level, c) == 0);

    // A blocked cell: the placement rule says no (players get "needs a clear 3 × 3 space").
    level.setBlockAndUpdate(c.offset(1, 0, 1), Blocks.STONE.defaultBlockState());
    boolean clear = SolarPanelBlock.footprintClear(level, c);
    ok &=
        SelfTest.check(
            "footprint.blocked",
            "footprintClear=false with stone in one cell",
            "footprintClear=" + clear,
            !clear);
    level.setBlockAndUpdate(
        c.offset(1, 0, 1), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
    boolean clearAgain = SolarPanelBlock.footprintClear(level, c);
    ok &=
        SelfTest.check(
            "footprint.clear",
            "footprintClear=true once the cell is empty",
            "footprintClear=" + clearAgain,
            clearAgain);
    return ok;
  }

  private static int countCanopy(ServerLevel level, BlockPos c) {
    int n = 0;
    for (int dx = -1; dx <= 1; dx++) {
      for (int dz = -1; dz <= 1; dz++) {
        BlockPos p = c.offset(dx, 0, dz);
        BlockState s = level.getBlockState(p);
        if (s.getBlock() instanceof mars.machines.SolarPanelCanopyBlock
            && mars.machines.SolarPanelCanopyBlock.centreOf(p, s).equals(c)) {
          n++;
        }
      }
    }
    return n;
  }

  private static int countCanopyAny(ServerLevel level, BlockPos c) {
    int n = 0;
    for (int dx = -1; dx <= 1; dx++) {
      for (int dz = -1; dz <= 1; dz++) {
        if (level.getBlockState(c.offset(dx, 0, dz)).getBlock()
            instanceof mars.machines.SolarPanelCanopyBlock) {
          n++;
        }
      }
    }
    return n;
  }

  private static String describeFootprint(ServerLevel level, BlockPos c) {
    StringBuilder b = new StringBuilder();
    for (int dz = -1; dz <= 1; dz++) {
      for (int dx = -1; dx <= 1; dx++) {
        BlockState s = level.getBlockState(c.offset(dx, 0, dz));
        b.append(
            s.getBlock() instanceof mars.machines.SolarPanelCanopyBlock
                ? "C"
                : s.is(MarsMachines.SOLAR_PANEL.get()) ? "P" : ".");
      }
      b.append(dz < 1 ? "/" : "");
    }
    return b.toString();
  }

  /** Runs every check; returns true if all passed. */
  public static boolean run(MinecraftServer server, boolean expectLevel) {
    boolean ok = true;
    boolean loaded = ModList.get().isLoaded("mars");
    ok &= SelfTest.check("mod.loaded", "mars in mod list", "isLoaded=" + loaded, loaded);

    ResourceLocation typeId = ResourceLocation.fromNamespaceAndPath("mars", "mars");
    boolean typeReg =
        server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).containsKey(typeId);
    ok &=
        SelfTest.check(
            "data.dimension_type",
            "mars:mars in the dimension type registry",
            "present=" + typeReg,
            typeReg);
    ResourceLocation biomeId = ResourceLocation.fromNamespaceAndPath("mars", "regolith_flats");
    boolean biomeReg =
        server.registryAccess().registryOrThrow(Registries.BIOME).containsKey(biomeId);
    ok &=
        SelfTest.check(
            "data.biome",
            "mars:regolith_flats in the biome registry",
            "present=" + biomeReg,
            biomeReg);

    // Decode data/mars/dimension/planet_1.json with vanilla's own codec against the live
    // registries: proves the dimension file is valid even where the level isn't created.
    ResourceLocation dimFile =
        ResourceLocation.fromNamespaceAndPath("mars", "dimension/planet_1.json");
    String decoded;
    boolean dimOk;
    Optional<Resource> dimRes = server.getResourceManager().getResource(dimFile);
    if (dimRes.isEmpty()) {
      decoded = "file missing";
      dimOk = false;
    } else {
      try (Reader r = new InputStreamReader(dimRes.get().open(), StandardCharsets.UTF_8)) {
        var json = com.google.gson.JsonParser.parseReader(r);
        var result =
            net.minecraft.world.level.dimension.LevelStem.CODEC.parse(
                net.minecraft.resources.RegistryOps.create(
                    com.mojang.serialization.JsonOps.INSTANCE, server.registryAccess()),
                json);
        dimOk = result.result().isPresent();
        decoded =
            result
                .result()
                .map(
                    stem ->
                        "type="
                            + stem.type().unwrapKey().map(k -> k.location().toString()).orElse("?")
                            + ", generator="
                            + stem.generator().getClass().getSimpleName())
                .orElseGet(() -> "error: " + result.error().map(Object::toString).orElse("?"));
      } catch (Exception e) {
        decoded = e.toString();
        dimOk = false;
      }
    }
    ok &=
        SelfTest.check(
            "data.dimension_decodes",
            "planet_1.json decodes as a dimension of type mars:mars",
            decoded,
            dimOk && decoded.contains("type=mars:mars"));

    ServerLevel mars = server.getLevel(MarsDimensions.PLANET_1);
    if (expectLevel) {
      ok &=
          SelfTest.check(
              "dimension.level_loaded",
              "level mars:planet_1 exists",
              mars == null ? "null" : mars.dimension().location().toString(),
              mars != null);
    } else {
      SelfTest.info(
          "dimension.level_loaded",
          "read="
              + (mars == null ? "null" : mars.dimension().location())
              + " (the GameTest server builds its own world without datapack dimensions;"
              + " the level itself is checked by runSelftestServer)");
    }

    if (mars != null) {
      // Travel FIRST, before any check loads chunk 0,0: the screenshots showed /mars go failing on
      // an unloaded chunk while a later-ordered check passed.
      FakePlayer fake =
          FakePlayerFactory.get(
              server.overworld(),
              new GameProfile(
                  UUID.fromString("6d617273-0000-4000-8000-000000000001"), "[MarsSelfTest]"));
      boolean sent = TravelCommands.sendToMars(fake);
      String where =
          fake.level().dimension().location()
              + String.format(" at %.1f %.1f %.1f", fake.getX(), fake.getY(), fake.getZ());
      ok &=
          SelfTest.check(
              "travel.go",
              "player in mars:planet_1 at 0.5 64.0 0.5",
              "sent=" + sent + ", " + where,
              sent
                  && fake.level().dimension().equals(MarsDimensions.PLANET_1)
                  && Math.abs(fake.getY() - 64.0) < 0.01);

      String type =
          mars.dimensionTypeRegistration()
              .unwrapKey()
              .map(k -> k.location().toString())
              .orElse("?");
      ok &=
          SelfTest.check(
              "dimension.type",
              "mars:mars, min_y 0, height 256",
              type + ", min_y " + mars.getMinBuildHeight() + ", height " + mars.getHeight(),
              type.equals("mars:mars") && mars.getMinBuildHeight() == 0 && mars.getHeight() == 256);

      mars.getChunk(0, 0);
      BlockState surface = mars.getBlockState(new BlockPos(0, 63, 0));
      BlockState above = mars.getBlockState(new BlockPos(0, 64, 0));
      ok &=
          SelfTest.check(
              "dimension.terrain",
              "red_sand at y=63, air at y=64",
              surface + " / " + above,
              surface.is(Blocks.RED_SAND) && above.isAir());

      String biome =
          mars.getBiome(new BlockPos(0, 64, 0))
              .unwrapKey()
              .map(k -> k.location().toString())
              .orElse("?");
      ok &=
          SelfTest.check(
              "dimension.biome", "mars:regolith_flats", biome, biome.equals("mars:regolith_flats"));
    }

    // Machines (BACKLOG #26): registered with the expected IDs and block-state properties.
    java.util.List<String> missing = new java.util.ArrayList<>();
    for (String id :
        java.util.List.of(
            "regolith_outgasser",
            "regolith_oven",
            "solar_panel",
            "battery",
            "oxygen_maker",
            "planetary_monitor")) {
      ResourceLocation rl = ResourceLocation.fromNamespaceAndPath("mars", id);
      if (!net.minecraft.core.registries.BuiltInRegistries.BLOCK.containsKey(rl)
          || !net.minecraft.core.registries.BuiltInRegistries.ITEM.containsKey(rl)) {
        missing.add(id);
      }
    }
    ok &=
        SelfTest.check(
            "machines.registered",
            "6 machine blocks and items",
            missing.isEmpty() ? "all 6 present" : "missing " + missing,
            missing.isEmpty());
    BlockState panel =
        MarsMachines.SOLAR_PANEL
            .get()
            .defaultBlockState()
            .setValue(SolarPanelBlock.DUST_STAGE, 4)
            .setValue(SolarPanelBlock.PARTLY_CLEANED, true);
    String props = panel.toString();
    ok &=
        SelfTest.check(
            "machines.panel_state",
            "solar_panel has facing, dust_stage 0-4, partly_cleaned",
            props,
            props.contains("dust_stage=4")
                && props.contains("partly_cleaned=true")
                && props.contains("facing"));
    ServerLevel testLevel = server.overworld();
    BlockPos at = new BlockPos(0, 200, 0);
    testLevel.setBlockAndUpdate(at, panel);
    BlockState placed = testLevel.getBlockState(at);
    ok &=
        SelfTest.check(
            "machines.panel_placed",
            "placed panel keeps dust_stage=4, partly_cleaned=true",
            placed.toString(),
            placed.is(MarsMachines.SOLAR_PANEL.get())
                && placed.getValue(SolarPanelBlock.DUST_STAGE) == 4
                && placed.getValue(SolarPanelBlock.PARTLY_CLEANED));
    testLevel.removeBlock(at, false);
    ok &= footprintChecks(server);

    boolean command = server.getCommands().getDispatcher().getRoot().getChild("mars") != null;
    ok &=
        SelfTest.check(
            "command.registered", "/mars command present", "present=" + command, command);

    ResourceLocation rule = ResourceLocation.fromNamespaceAndPath("mars", "terrain/ice_rule.json");
    Optional<Resource> res = server.getResourceManager().getResource(rule);
    boolean found = res.isPresent();
    ok &=
        SelfTest.check(
            "data.ice_rule", "found in the mod's data", found ? "found" : "missing", found);
    if (found) {
      try (Reader r = new InputStreamReader(res.get().open(), StandardCharsets.UTF_8)) {
        IceRule ice = IceRule.parse(r, rule.toString());
        IceResult got = ice.evaluate(2.0, 40.0, 17.5);
        ok &=
            SelfTest.check(
                "data.ice_rule.eval",
                "boil_away at 2 kPa, 40 °C",
                got.id(),
                got == IceResult.BOIL_AWAY);
      } catch (Exception e) {
        ok &= SelfTest.check("data.ice_rule.parse", "parses", e.toString(), false);
      }
    }
    return ok;
  }
}
