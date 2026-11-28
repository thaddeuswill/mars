package mars.story.landing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mars.journal.Journal;
import mars.journal.JournalState;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.WrittenBookContent;

/**
 * The Keeper's Journal as a vanilla written book (landing-spec 5.1): built on the server for each
 * player from the unlocked entries, so no client code is needed. A signed book cannot be edited.
 * Page links use the vanilla "change page" click event; whether they work in a real 1.21.1 client
 * is checked by the screenshot run, not assumed.
 */
public final class JournalBook {
  static final String MARK = "mars_journal";

  private JournalBook() {}

  public static boolean isJournal(ItemStack s) {
    if (!s.is(Items.WRITTEN_BOOK)) {
      return false;
    }
    CustomData d = s.get(DataComponents.CUSTOM_DATA);
    return d != null && d.copyTag().getBoolean(MARK);
  }

  public static ItemStack build(
      JournalState state, Journal journal, boolean hs, Map<String, String> params) {
    Journal.Book b = journal.build(state, hs, params, "Keeper's Journal", "New");
    List<Filterable<Component>> pages = new ArrayList<>();
    for (List<Journal.Seg> page : b.pages()) {
      MutableComponent c = Component.empty();
      for (Journal.Seg seg : page) {
        Style st = Style.EMPTY;
        if (seg.bold()) {
          st = st.withBold(true);
        }
        if (seg.jumpToPage() > 0) {
          st =
              st.withClickEvent(
                      new ClickEvent(
                          ClickEvent.Action.CHANGE_PAGE, String.valueOf(seg.jumpToPage())))
                  .withColor(ChatFormatting.DARK_BLUE)
                  .withUnderlined(true);
        }
        c.append(Component.literal(seg.text()).withStyle(st));
      }
      pages.add(Filterable.passThrough(c));
    }
    ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
    stack.set(
        DataComponents.WRITTEN_BOOK_CONTENT,
        new WrittenBookContent(Filterable.passThrough("Keeper's Journal"), "BELL", 0, pages, true));
    int unread = state.unread().size();
    stack.set(
        DataComponents.CUSTOM_NAME,
        unread == 0
            ? Component.translatable("journal.item")
            : Component.translatable("journal.item.new", unread));
    CompoundTag tag = new CompoundTag();
    tag.putBoolean(MARK, true);
    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    return stack;
  }

  /** Rebuild every journal in the player's inventory (there is normally one). */
  public static void refresh(
      ServerPlayer p, JournalState state, Journal journal, Map<String, String> params) {
    var inv = p.getInventory();
    for (int i = 0; i < inv.getContainerSize(); i++) {
      if (isJournal(inv.getItem(i))) {
        inv.setItem(i, build(state, journal, false, params));
      }
    }
  }

  public static boolean has(ServerPlayer p) {
    var inv = p.getInventory();
    for (int i = 0; i < inv.getContainerSize(); i++) {
      if (isJournal(inv.getItem(i))) {
        return true;
      }
    }
    return false;
  }
}
