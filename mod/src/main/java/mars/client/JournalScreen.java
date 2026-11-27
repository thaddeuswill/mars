package mars.client;

import static mars.client.MonitorStyle.*;

import java.util.ArrayList;
import java.util.List;
import mars.journal.JournalPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * The Keeper's Journal (straw-man, BACKLOG #49): a plain panel in the Monitor's colours, a list of
 * entry titles on the left (unread ones carry a dot and the word "new", so colour is not the only
 * signal) and the selected entry's text on the right, with where it was unlocked. Up and Down or a
 * click choose an entry; Esc or the journal key closes. No images, no animation, no quiz. Draws
 * only what the server sent. Placeholder look; the Artist styles it in Stage 3.
 */
public final class JournalScreen extends Screen {
  private static final int LIST_W = 104;
  private static final int PANEL_H = 128;
  private static final int ROW = 12;
  private static final int SELECTED = 0xFF3A2C22;

  private final List<JournalPayload.Entry> entries;
  private final List<Boolean> unread = new ArrayList<>();
  private int selected;
  private int panelX;
  private int panelY;

  public JournalScreen(List<JournalPayload.Entry> entries) {
    super(Component.translatable("journal.title"));
    this.entries = entries;
    entries.forEach(e -> unread.add(e.unread()));
    int first = unread.indexOf(true);
    this.selected = first < 0 ? 0 : first;
    markRead(selected);
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  @Override
  public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    // No dimming, like the Monitor: the world stays visible behind the panel.
  }

  private void markRead(int i) {
    if (i >= 0 && i < entries.size() && unread.get(i)) {
      unread.set(i, false);
      PacketDistributor.sendToServer(new mars.journal.JournalSeenPayload(entries.get(i).id()));
    }
  }

  /** The text of an entry: the middle-school version until a grade band exists in code. */
  private static Component body(JournalPayload.Entry e) {
    String ms = e.textKey() + ".ms";
    return Component.translatable(I18n.exists(ms) ? ms : e.textKey());
  }

  @Override
  public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    super.render(g, mouseX, mouseY, partialTick);
    if (width < 40 || height < 48) {
      return;
    }
    int panelW = Math.min(PANEL_W, width - 2 * MARGIN);
    int panelH = Math.min(PANEL_H, height - 4);
    panelX = (width - panelW) / 2;
    panelY = Math.max(2, (height - panelH) / 2);
    int right = panelX + panelW - PAD;
    g.enableScissor(0, 0, width, height);
    g.fill(panelX - 1, panelY - 1, panelX + panelW + 1, panelY + panelH + 1, EDGE);
    g.fill(panelX, panelY, panelX + panelW, panelY + panelH, BACKING);
    g.drawString(font, title, panelX + PAD, panelY + 5, VALUE, false);
    Component hint = Component.translatable("journal.screen.close_hint");
    g.drawString(font, hint, right - font.width(hint), panelY + 5, LABEL, false);
    int top = panelY + HEADER_H;
    int listRight = panelX + PAD + LIST_W;
    g.fill(listRight + 3, top, listRight + 4, panelY + panelH - PAD, PLOT_AXIS);
    if (entries.isEmpty()) {
      g.drawWordWrap(
          font,
          Component.translatable("journal.list.empty"),
          panelX + PAD,
          top + 2,
          panelW - 2 * PAD,
          LABEL);
      g.disableScissor();
      return;
    }
    String newWord = Component.translatable("journal.screen.new").getString();
    for (int i = 0; i < entries.size(); i++) {
      int y = top + i * ROW;
      if (y + ROW > panelY + panelH - PAD) {
        break;
      }
      if (i == selected) {
        g.fill(panelX + PAD - 2, y - 1, listRight + 1, y + ROW - 1, SELECTED);
      }
      g.drawString(
          font,
          Component.translatable(entries.get(i).titleKey()),
          panelX + PAD,
          y + 1,
          i == selected ? VALUE : STEADY,
          false);
      if (unread.get(i)) {
        int w = font.width(newWord);
        int tx = listRight - w - 2;
        g.fill(tx - 7, y + 4, tx - 3, y + 8, CAUSE);
        g.drawString(font, newWord, tx, y + 1, CAUSE, false);
      }
    }
    JournalPayload.Entry e = entries.get(selected);
    int tx = listRight + 9;
    int tw = right - tx;
    g.drawString(font, Component.translatable(e.titleKey()), tx, top + 1, VALUE, false);
    int ty = top + LINE_H + 4;
    for (var line : font.split(body(e), tw)) {
      g.drawString(font, line, tx, ty, VALUE, false);
      ty += LINE_H;
    }
    Component at =
        Component.translatable(
            "journal.entry.unlocked_at", Component.translatable(e.gateNameKey()));
    g.drawString(font, at, tx, panelY + panelH - PAD - 8, LABEL, false);
    g.disableScissor();
  }

  @Override
  public boolean mouseClicked(double mx, double my, int button) {
    int top = panelY + HEADER_H;
    int listRight = panelX + PAD + LIST_W;
    if (mx >= panelX + PAD - 2 && mx <= listRight && my >= top) {
      int i = (int) ((my - top) / ROW);
      if (i >= 0 && i < entries.size()) {
        select(i);
        return true;
      }
    }
    return super.mouseClicked(mx, my, button);
  }

  private void select(int i) {
    selected = i;
    markRead(i);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (JournalClient.KEY.matches(keyCode, scanCode)) {
      onClose();
      return true;
    }
    if (!entries.isEmpty()) {
      if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN) {
        select(Math.min(entries.size() - 1, selected + 1));
        return true;
      }
      if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_UP) {
        select(Math.max(0, selected - 1));
        return true;
      }
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }
}
