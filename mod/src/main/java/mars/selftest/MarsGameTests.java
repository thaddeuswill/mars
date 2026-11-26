package mars.selftest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Stage 1 boot checks in the GameTest server ({@code ./gradlew runGameTestServer}). The GameTest
 * server builds its own world without datapack dimensions, so the Mars level itself is checked by
 * the dedicated-server self-test ({@code ./gradlew runSelftestServer}).
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class MarsGameTests {
  private MarsGameTests() {}

  @GameTest(template = "empty")
  public static void bootChecks(GameTestHelper helper) {
    SelfTest.step("gametest_started");
    boolean ok = BootChecks.run(helper.getLevel().getServer(), false);
    SelfTest.step("gametest_finished");
    if (ok) {
      helper.succeed();
    } else {
      helper.fail("Mars boot checks failed; see MARS-SELFTEST lines");
    }
  }
}
