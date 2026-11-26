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

      // Travel: a fake player (no network connection) is sent with the same code as /mars go.
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
    }

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
