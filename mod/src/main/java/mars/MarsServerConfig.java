package mars;

import mars.index.sim.Pace;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Server-authoritative options: placement rules, and the pacing of a world ({@code group.size},
 * {@code pace.classroom}; balance sheet 5.1). Pacing options are read when a planet is created or
 * loaded, never from who is online.
 */
public final class MarsServerConfig {
  public static final ModConfigSpec SPEC;
  public static final ModConfigSpec.BooleanValue CREATIVE_GATE_BYPASS;
  public static final ModConfigSpec.IntValue GROUP_SIZE;
  public static final ModConfigSpec.BooleanValue CLASSROOM;

  static {
    var builder = new ModConfigSpec.Builder();
    CREATIVE_GATE_BYPASS =
        builder
            .comment(
                "Allow Creative players to place gate-locked machines before the required goal.")
            .define("creativeGateBypass", true);
    GROUP_SIZE =
        builder
            .comment(
                "Players in the group on each planet (1 to 5). Life support, start fills and the"
                    + " pace of terraforming scale with this number, never with who is online.")
            .defineInRange("group.size", 1, 1, 5);
    CLASSROOM =
        builder
            .comment(
                "Classroom mode: rates x4, short holds, a one-sol air reserve and cheaper recipes."
                    + " Never changes a physical threshold.")
            .define("pace.classroom", false);
    SPEC = builder.build();
  }

  private MarsServerConfig() {}

  /**
   * The pace of this world. Falls back to the standard solo pace when the config is not loaded
   * (unit tests, early start-up).
   */
  public static Pace pace() {
    try {
      int n = GROUP_SIZE.get();
      return CLASSROOM.get() ? Pace.classroom(n) : new Pace(1.0, n, 24_000, 100);
    } catch (IllegalStateException notLoaded) {
      return Pace.STANDARD;
    }
  }
}
