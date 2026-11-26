package mars.selftest;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import mars.dimension.MarsDimensions;
import mars.dimension.ice.IceResult;
import mars.dimension.ice.IceRule;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Stage 1 boot checks (test-strategy L3). Each check prints what it expected and what it read. Runs
 * only in the GameTest server ({@code ./gradlew runGameTestServer}).
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class MarsGameTests {
  private MarsGameTests() {}

  @GameTest(template = "empty")
  public static void bootChecks(GameTestHelper helper) {
    SelfTest.step("gametest_started");
    MinecraftServer server = helper.getLevel().getServer();
    boolean ok = true;

    boolean loaded = ModList.get().isLoaded("mars");
    ok &= SelfTest.check("mod.loaded", "mars in mod list", "isLoaded=" + loaded, loaded);

    ServerLevel mars = server.getLevel(MarsDimensions.PLANET_1);
    ok &=
        SelfTest.check(
            "dimension.registered",
            "level mars:planet_1 exists",
            mars == null ? "null" : mars.dimension().location().toString(),
            mars != null);

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

      mars.getChunk(0, 0); // generate the chunk at the origin
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

    boolean command = server.getCommands().getDispatcher().getRoot().getChild("mars") != null;
    ok &=
        SelfTest.check(
            "command.registered", "/mars command present", "present=" + command, command);

    ResourceLocation rule = ResourceLocation.fromNamespaceAndPath("mars", "terrain/ice_rule.json");
    Optional<Resource> res = server.getResourceManager().getResource(rule);
    if (SelfTest.check(
        "data.ice_rule",
        "found in the mod's data",
        res.isPresent() ? "found" : "missing",
        res.isPresent())) {
      try (Reader r = new InputStreamReader(res.get().open(), StandardCharsets.UTF_8)) {
        IceRule ice = IceRule.parse(r, rule.toString());
        // Phase 1 heater case: 2 kPa, 40 °C local, boiling point ~17.5 °C -> boils away.
        IceResult got = ice.evaluate(2.0, 40.0, 17.5);
        ok &=
            SelfTest.check("data.ice_rule.eval", "boil_away", got.id(), got == IceResult.BOIL_AWAY);
      } catch (Exception e) {
        ok &= SelfTest.check("data.ice_rule.parse", "parses", e.toString(), false);
      }
    } else {
      ok = false;
    }

    SelfTest.step("gametest_finished");
    if (ok) {
      helper.succeed();
    } else {
      helper.fail("Mars boot checks failed; see MARS-SELFTEST lines");
    }
  }
}
