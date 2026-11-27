package mars.journal;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Client to server: "open my journal" (the J key). Carries nothing; the server knows who sent it.
 */
public record JournalRequestPayload() implements CustomPacketPayload {
  public static final Type<JournalRequestPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath("mars", "journal_request"));

  public static final StreamCodec<RegistryFriendlyByteBuf, JournalRequestPayload> CODEC =
      StreamCodec.unit(new JournalRequestPayload());

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
