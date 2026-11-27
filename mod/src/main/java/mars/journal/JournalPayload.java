package mars.journal;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Server to client: the journal entries this player has unlocked, as language keys (the client
 * translates; nothing here is computed by the client). {@code unread} is the server's answer, so
 * the "new" mark follows the player across devices.
 */
public record JournalPayload(List<Entry> entries) implements CustomPacketPayload {
  /**
   * One entry: id, title key, text key (without the {@code .ms}/{@code .hs} suffix), gate name key.
   */
  public record Entry(
      String id, String titleKey, String textKey, String gateNameKey, boolean unread) {}

  public static final Type<JournalPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath("mars", "journal"));

  public static final StreamCodec<RegistryFriendlyByteBuf, JournalPayload> CODEC =
      StreamCodec.of(
          (buf, p) -> buf.writeCollection(p.entries, JournalPayload::write),
          buf -> new JournalPayload(buf.readList(JournalPayload::read)));

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  private static void write(FriendlyByteBuf b, Entry e) {
    b.writeUtf(e.id);
    b.writeUtf(e.titleKey);
    b.writeUtf(e.textKey);
    b.writeUtf(e.gateNameKey);
    b.writeBoolean(e.unread);
  }

  private static Entry read(FriendlyByteBuf b) {
    return new Entry(b.readUtf(), b.readUtf(), b.readUtf(), b.readUtf(), b.readBoolean());
  }
}
