package mars.machines;

import mars.power.MachineLook;
import net.minecraft.util.StringRepresentable;

/** Block-state value of {@code machine_state}; mirrors {@link MachineLook}. */
public enum MachineState implements StringRepresentable {
  OFF("off"),
  IDLE("idle"),
  RUNNING("running");

  private final String name;

  MachineState(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return name;
  }

  public static MachineState of(MachineLook look) {
    return switch (look) {
      case OFF -> OFF;
      case IDLE -> IDLE;
      case RUNNING -> RUNNING;
    };
  }
}
