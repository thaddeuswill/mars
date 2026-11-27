package mars.journal;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Client to server: the player read this entry (clears its "new" mark). */
public record JournalSeenPayload(String entryId) implements CustomPacketPayload {
  public static final Type<JournalSeenPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath("mars", "journal_seen"));

  public static final StreamCodec<RegistryFriendlyByteBuf, JournalSeenPayload> CODEC =
      ByteBufCodecs.stringUtf8(128)
          .<RegistryFriendlyByteBuf>cast()
          .map(JournalSeenPayload::new, JournalSeenPayload::entryId);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
