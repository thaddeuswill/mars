package mars.monitor.view;

import java.util.ArrayList;
import java.util.List;
import mars.core.Ids;
import mars.core.ParameterId;
import mars.index.sim.PlanetSim;
import mars.index.sim.PlanetaryIndexModel;

/**
 * What the Planetary Monitor shows, as plain data built on the server from the real Index
 * (ARCHITECTURE.md 2.9, ui-specs §2 for Phase 1: pressure, temperature, and the water line). The
 * client only draws it and holds no planet state. Labels are language keys; numbers are already
 * formatted here by {@link Fmt}, so every reader of the log or screen sees the same text.
 */
public record MonitorView(double sols, List<Row> rows) {

  /** A trend in one value's direction; steady below {@link #EPSILON} per sol. */
  public enum Trend {
    RISING("monitor.trend.rising"),
    STEADY("monitor.trend.steady"),
    FALLING("monitor.trend.falling");

    private final String key;

    Trend(String key) {
      this.key = key;
    }

    public String key() {
      return key;
    }
  }

  /**
   * One Monitor row. {@code valueKey} replaces {@code value} when the number has no meaning (the
   * boiling point below the triple point). {@code trend} and {@code trendRate} may be null.
   */
  public record Row(
      String labelKey,
      String value,
      String valueKey,
      Trend trend,
      String trendRate,
      List<CauseLine> causes) {}

  /** "Regolith Outgasser ×2: +0.20 kPa/sol". {@code sourceKey} is a language key. */
  public record CauseLine(String sourceKey, int count, String rate) {}

  public static final double EPSILON = 1e-9;

  public static MonitorView of(PlanetSim sim) {
    PlanetaryIndexModel model = sim.model();
    var snap = model.snapshot();
    List<Row> rows = new ArrayList<>();

    ParameterId pressure = Ids.PRESSURE;
    double p = snap.get(pressure);
    double net = model.netRatePerSol(pressure);
    List<CauseLine> causes = new ArrayList<>();
    for (PlanetaryIndexModel.Cause c : model.causes(pressure)) {
      causes.add(new CauseLine(sourceKey(c.source()), c.count(), rate(c.ratePerSol(), "kPa")));
    }
    rows.add(
        new Row(
            "monitor.row.pressure",
            Fmt.withUnit(p, p < 10 ? 2 : 1, "kPa"),
            null,
            trend(net),
            rate(net, "kPa"),
            causes));

    double t = snap.get(Ids.TEMPERATURE);
    rows.add(
        new Row(
            "monitor.row.temperature",
            Fmt.withUnit(t, 0, "°C"),
            null,
            trend(model.netRatePerSol(Ids.TEMPERATURE)),
            rate(model.netRatePerSol(Ids.TEMPERATURE), "°C"),
            List.of()));

    double boil = snap.get(Ids.BOILING_POINT);
    if (Double.isNaN(boil)) {
      rows.add(
          new Row("monitor.row.water_boils", "", "monitor.water.no_liquid", null, null, List.of()));
    } else {
      rows.add(
          new Row(
              "monitor.row.water_boils", Fmt.withUnit(boil, 0, "°C"), null, null, null, List.of()));
    }
    powerRow(sim).ifPresent(rows::add);
    return new MonitorView(model.sols(), List.copyOf(rows));
  }

  /**
   * The Power row: shows only when the planet has any power machine. The state word is the answer
   * to "why is my Outgasser slow?"; the cause lines give supply, demand and battery in power units
   * (1 unit = 1 kWh, balance sheet 2b).
   */
  private static java.util.Optional<Row> powerRow(PlanetSim sim) {
    var grid = sim.power();
    if (grid.supplyPerSol() <= 0 && grid.demandPerSol() <= 0 && grid.capacity() <= 0) {
      return java.util.Optional.empty();
    }
    String key =
        switch (grid.state(sim.stepSols())) {
          case IDLE, POWERED -> "monitor.power.powered";
          case SHORT -> "monitor.power.short";
          case UNPOWERED -> "monitor.power.unpowered";
        };
    List<CauseLine> lines = new ArrayList<>();
    int panels = sim.machineCount("mars:solar_panel");
    if (panels > 0) {
      lines.add(
          new CauseLine(
              "block.mars.solar_panel", panels, units(grid.supplyPerSol(), "/sol", true)));
    }
    int users = sim.machineCount("mars:regolith_outgasser");
    if (users > 0) {
      lines.add(
          new CauseLine(
              "block.mars.regolith_outgasser", users, units(-grid.demandPerSol(), "/sol", true)));
    }
    int batteries = sim.machineCount("mars:battery");
    if (batteries > 0) {
      lines.add(
          new CauseLine(
              "block.mars.battery",
              batteries,
              Fmt.number(grid.charge(), 1) + " / " + Fmt.number(grid.capacity(), 1) + " units"));
    }
    return java.util.Optional.of(new Row("monitor.row.power", "", key, null, null, lines));
  }

  private static String units(double v, String per, boolean signed) {
    return (signed ? Fmt.signed(v, 1) : Fmt.number(v, 1)) + " units" + per;
  }

  /**
   * {@code mars:regolith_outgasser} becomes the block's name key {@code
   * block.mars.regolith_outgasser}.
   */
  static String sourceKey(String source) {
    return "block." + source.replace(':', '.');
  }

  private static Trend trend(double net) {
    return net > EPSILON ? Trend.RISING : net < -EPSILON ? Trend.FALLING : Trend.STEADY;
  }

  private static String rate(double perSol, String unit) {
    return Fmt.signed(perSol, 2) + " " + unit + "/sol";
  }
}
