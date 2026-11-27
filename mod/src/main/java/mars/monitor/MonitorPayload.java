package mars.monitor;

import java.util.List;
import mars.monitor.view.MonitorView;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Server to client: the Monitor's contents, already formatted (nothing here is computed by the
 * client).
 */
public record MonitorPayload(MonitorView view) implements CustomPacketPayload {
  public static final Type<MonitorPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath("mars", "monitor"));

  public static final StreamCodec<RegistryFriendlyByteBuf, MonitorPayload> CODEC =
      StreamCodec.of((buf, p) -> write(buf, p.view), buf -> new MonitorPayload(read(buf)));

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  static void write(FriendlyByteBuf buf, MonitorView v) {
    buf.writeDouble(v.sols());
    buf.writeCollection(v.rows(), MonitorPayload::writeRow);
    MonitorView.History h = v.history();
    buf.writeUtf(h.unit());
    buf.writeCollection(
        h.points(),
        (b, p) -> {
          b.writeDouble(p.sol());
          b.writeDouble(p.value());
        });
    buf.writeCollection(
        h.markers(),
        (b, m) -> {
          b.writeDouble(m.sol());
          b.writeUtf(m.labelKey());
          b.writeNullable(m.argKey(), FriendlyByteBuf::writeUtf);
        });
  }

  static MonitorView read(FriendlyByteBuf buf) {
    double sols = buf.readDouble();
    List<MonitorView.Row> rows = buf.readList(MonitorPayload::readRow);
    String unit = buf.readUtf();
    List<MonitorView.Point> points =
        buf.readList(b -> new MonitorView.Point(b.readDouble(), b.readDouble()));
    List<MonitorView.Marker> markers =
        buf.readList(
            b ->
                new MonitorView.Marker(
                    b.readDouble(), b.readUtf(), b.readNullable(FriendlyByteBuf::readUtf)));
    return new MonitorView(sols, rows, new MonitorView.History(points, markers, unit));
  }

  private static void writeRow(FriendlyByteBuf buf, MonitorView.Row r) {
    buf.writeUtf(r.labelKey());
    buf.writeUtf(r.value());
    buf.writeNullable(r.valueKey(), FriendlyByteBuf::writeUtf);
    buf.writeNullable(r.trend(), FriendlyByteBuf::writeEnum);
    buf.writeNullable(r.trendRate(), FriendlyByteBuf::writeUtf);
    buf.writeCollection(
        r.causes(),
        (b, c) -> {
          b.writeUtf(c.sourceKey());
          b.writeVarInt(c.count());
          b.writeUtf(c.rate());
          b.writeNullable(c.reasonKey(), FriendlyByteBuf::writeUtf);
          b.writeVarInt(c.direction() + 1);
        });
    buf.writeEnum(r.tone());
    buf.writeBoolean(r.emphasis());
    buf.writeNullable(r.side(), FriendlyByteBuf::writeUtf);
    buf.writeNullable(r.detailKey(), FriendlyByteBuf::writeUtf);
    buf.writeCollection(r.detailArgs(), FriendlyByteBuf::writeUtf);
    buf.writeNullable(r.detailRightKey(), FriendlyByteBuf::writeUtf);
    buf.writeNullable(r.detailRight(), FriendlyByteBuf::writeUtf);
    buf.writeDouble(r.bar());
    buf.writeNullable(r.noteKey(), FriendlyByteBuf::writeUtf);
  }

  private static MonitorView.Row readRow(FriendlyByteBuf buf) {
    String label = buf.readUtf();
    String value = buf.readUtf();
    String valueKey = buf.readNullable(FriendlyByteBuf::readUtf);
    MonitorView.Trend trend = buf.readNullable(b -> b.readEnum(MonitorView.Trend.class));
    String trendRate = buf.readNullable(FriendlyByteBuf::readUtf);
    List<MonitorView.CauseLine> causes =
        buf.readList(
            b ->
                new MonitorView.CauseLine(
                    b.readUtf(),
                    b.readVarInt(),
                    b.readUtf(),
                    b.readNullable(FriendlyByteBuf::readUtf),
                    b.readVarInt() - 1));
    MonitorView.Tone tone = buf.readEnum(MonitorView.Tone.class);
    boolean emphasis = buf.readBoolean();
    String side = buf.readNullable(FriendlyByteBuf::readUtf);
    String detailKey = buf.readNullable(FriendlyByteBuf::readUtf);
    List<String> detailArgs = buf.readList(FriendlyByteBuf::readUtf);
    String detailRightKey = buf.readNullable(FriendlyByteBuf::readUtf);
    String detailRight = buf.readNullable(FriendlyByteBuf::readUtf);
    double bar = buf.readDouble();
    String noteKey = buf.readNullable(FriendlyByteBuf::readUtf);
    return new MonitorView.Row(
        label,
        value,
        valueKey,
        trend,
        trendRate,
        causes,
        tone,
        emphasis,
        side,
        detailKey,
        detailArgs,
        detailRightKey,
        detailRight,
        bar,
        noteKey);
  }
}
