package mars.sky;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Server to client: the planet's air pressure in kPa, and nothing else, so the client can draw the
 * sky and fog. The client does not need (and never gets) any other Index value for this.
 */
public record SkyPayload(double pressureKpa) implements CustomPacketPayload {
  public static final Type<SkyPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath("mars", "sky"));

  public static final StreamCodec<RegistryFriendlyByteBuf, SkyPayload> CODEC =
      StreamCodec.composite(ByteBufCodecs.DOUBLE, SkyPayload::pressureKpa, SkyPayload::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
