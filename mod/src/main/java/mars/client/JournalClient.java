package mars.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * The journal key. J is an invented default (rebindable in Controls, category Mars). Client only:
 * registered from {@code MarsMod} only when running on a client. Cost: one boolean check per client
 * tick while no screen is open.
 */
public final class JournalClient {
  static final KeyMapping KEY =
      new KeyMapping("key.mars.journal", InputConstants.KEY_J, "key.categories.mars");

  private JournalClient() {}

  public static void init(IEventBus modBus) {
    modBus.addListener((RegisterKeyMappingsEvent e) -> e.register(KEY));
    NeoForge.EVENT_BUS.addListener(
        (ClientTickEvent.Post e) -> {
          Minecraft mc = Minecraft.getInstance();
          while (KEY.consumeClick()) {
            if (mc.player != null && mc.screen == null) {
              requestOpen();
            }
          }
        });
  }

  /** Ask the server for the journal; the answer opens the screen. */
  public static void requestOpen() {
    PacketDistributor.sendToServer(new mars.journal.JournalRequestPayload());
  }
}
