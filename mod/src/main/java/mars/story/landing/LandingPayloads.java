package mars.story.landing;

import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** The three small payloads of the Landing UI (landing-spec 3.10). */
public final class LandingPayloads {
  private LandingPayloads() {}

  private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> mk(String path) {
    return new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("mars", path));
  }

  /**
   * Server to client: the checklist panel. {@code show} false hides it. One line per step: its
   * language key and status ordinal (0 pending, 1 done, 2 auto, 3 skipped); {@code active} is the
   * index of the active line or -1; {@code hintKey} and the distance chip belong to that line.
   */
  public record Panel(
      boolean show,
      List<String> textKeys,
      List<Integer> statuses,
      int active,
      String hintKey,
      int chipBlocks,
      String chipDir)
      implements CustomPacketPayload {
    public static final Type<Panel> TYPE = mk("landing_panel");
    public static final StreamCodec<RegistryFriendlyByteBuf, Panel> CODEC =
        StreamCodec.of(
            (buf, p) -> {
              buf.writeBoolean(p.show());
              buf.writeCollection(p.textKeys(), (b, k) -> b.writeUtf(k));
              buf.writeCollection(p.statuses(), (b, k) -> b.writeVarInt(k));
              buf.writeVarInt(p.active() + 1);
              buf.writeUtf(p.hintKey());
              buf.writeVarInt(p.chipBlocks() + 1);
              buf.writeUtf(p.chipDir());
            },
            buf ->
                new Panel(
                    buf.readBoolean(),
                    buf.readList(b -> b.readUtf()),
                    buf.readList(b -> b.readVarInt()),
                    buf.readVarInt() - 1,
                    buf.readUtf(),
                    buf.readVarInt() - 1,
                    buf.readUtf()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }

    public static Panel hidden() {
      return new Panel(false, List.of(), List.of(), -1, "", -1, "");
    }
  }

  /** Server to client: open the intro screen (two buttons). */
  public record Intro() implements CustomPacketPayload {
    public static final Type<Intro> TYPE = mk("landing_intro");
    public static final StreamCodec<RegistryFriendlyByteBuf, Intro> CODEC =
        StreamCodec.unit(new Intro());

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }
  }

  /** Client to server: the player chose. The server checks the player is really in OFFERED. */
  public record Choice(boolean start) implements CustomPacketPayload {
    public static final Type<Choice> TYPE = mk("landing_choice");
    public static final StreamCodec<RegistryFriendlyByteBuf, Choice> CODEC =
        StreamCodec.composite(ByteBufCodecs.BOOL, Choice::start, Choice::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }
  }
}
