package mars.client;

import mars.story.landing.LandingPayloads;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * The Landing intro: BELL's line and two buttons (landing-spec 3.3). Closing it with Escape makes
 * no choice; the offer stays and the commands do the same. It shows no science and no question.
 */
public final class LandingIntroScreen extends Screen {
  public LandingIntroScreen() {
    super(Component.translatable("landing.panel.title"));
  }

  @Override
  protected void init() {
    int cx = width / 2;
    int y = height / 2 + 6;
    addRenderableWidget(
        Button.builder(Component.translatable("landing.button.start"), b -> choose(true))
            .bounds(cx - 105, y, 100, 20)
            .build());
    addRenderableWidget(
        Button.builder(Component.translatable("landing.button.skip"), b -> choose(false))
            .bounds(cx + 5, y, 100, 20)
            .build());
  }

  private void choose(boolean start) {
    PacketDistributor.sendToServer(new LandingPayloads.Choice(start));
    onClose();
  }

  @Override
  public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    super.render(g, mouseX, mouseY, partialTick);
    g.drawCenteredString(font, title, width / 2, height / 2 - 30, 0xFFE6D7A3);
    g.drawCenteredString(
        font, Component.translatable("landing.intro"), width / 2, height / 2 - 14, 0xFFFFFFFF);
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
