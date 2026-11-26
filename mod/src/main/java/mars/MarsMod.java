package mars;

import mars.planets.TravelCommands;
import mars.selftest.SelfTest;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Entry point of the Mars companion mod. Stage 1: an empty Mars dimension and travel commands. */
@Mod(MarsMod.MOD_ID)
public final class MarsMod {
  public static final String MOD_ID = "mars";
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  public MarsMod(IEventBus modBus) {
    SelfTest.installIfEnabled(modBus);
    SelfTest.step("mod_constructed");
    NeoForge.EVENT_BUS.addListener(TravelCommands::register);
    LOG.info("Mars companion mod constructed (stage 1 skeleton)");
  }
}
