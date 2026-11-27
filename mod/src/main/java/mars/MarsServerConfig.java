package mars;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Server-authoritative options for placement rules on Mars. */
public final class MarsServerConfig {
  public static final ModConfigSpec SPEC;
  public static final ModConfigSpec.BooleanValue CREATIVE_GATE_BYPASS;

  static {
    var builder = new ModConfigSpec.Builder();
    CREATIVE_GATE_BYPASS =
        builder
            .comment(
                "Allow Creative players to place gate-locked machines before the required goal.")
            .define("creativeGateBypass", true);
    SPEC = builder.build();
  }

  private MarsServerConfig() {}
}
