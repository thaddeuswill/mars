package mars.sky;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Server to client: what the client needs to draw the sky, fog and dust: the planet's air pressure
 * in kPa, the storm strength (0 calm to 1, the active storm's cap), the pre-storm haze (0 to 1) and
 * the planet's fixed wind bearing (compass degrees, 0 = north, the direction it blows toward; -1
 * when unknown). The client never gets any other Index value for this.
 */
public record SkyPayload(double pressureKpa, float storm, float haze, float windDeg)
    implements CustomPacketPayload {
  public static final Type<SkyPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath("mars", "sky"));

  public static final StreamCodec<RegistryFriendlyByteBuf, SkyPayload> CODEC =
      StreamCodec.composite(
          ByteBufCodecs.DOUBLE,
          SkyPayload::pressureKpa,
          ByteBufCodecs.FLOAT,
          SkyPayload::storm,
          ByteBufCodecs.FLOAT,
          SkyPayload::haze,
          ByteBufCodecs.FLOAT,
          SkyPayload::windDeg,
          SkyPayload::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
