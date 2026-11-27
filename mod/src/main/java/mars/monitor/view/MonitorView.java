package mars.monitor.view;

import java.util.ArrayList;
import java.util.List;
import mars.core.Ids;
import mars.core.ParameterId;
import mars.index.sim.PlanetSim;
import mars.index.sim.PlanetaryIndexModel;
import mars.power.PowerGrid;

/**
 * What the Planetary Monitor shows, as plain data built on the server from the real Index
 * (ARCHITECTURE.md 2.9, ui-specs §2 for Phase 1: pressure, temperature, and the water line). The
 * client only draws it and holds no planet state. Labels are language keys; numbers are already
 * formatted here by {@link Fmt}, so every reader of the log or screen sees the same text.
 */
public record MonitorView(double sols, List<Row> rows, History history) {

  /** A view with no pressure history (new planet, or a test that does not need one). */
  public MonitorView(double sols, List<Row> rows) {
    this(sols, rows, History.EMPTY);
  }

  /** At most this many plot points are sent; the plot is about 200 px wide. */
  public static final int MAX_PLOT_POINTS = 60;

  /** One plot point: sol and pressure. */
  public record Point(double sol, double value) {}

  /**
   * A plot marker: a vertical line at {@code sol} with a label. The label is {@code labelKey}
   * formatted with the translation of {@code argKey} when that is not null.
   */
  public record Marker(double sol, String labelKey, String argKey) {}

  /** The pressure plot: points in time order, markers, and the unit for the axis label. */
  public record History(List<Point> points, List<Marker> markers, String unit) {
    public static final History EMPTY = new History(List.of(), List.of(), "kPa");

    /** True when there are at least two points and they span a sol or more (else it says so). */
    public boolean drawable() {
      return points.size() >= 2 && points.get(points.size() - 1).sol() >= points.get(0).sol() + 1.0;
    }
  }

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

  /** How a row's state is coloured (always paired with a word or a shape, never colour alone). */
  public enum Tone {
    NORMAL,
    GOOD,
    SHORT,
    NONE
  }

  /**
   * One Monitor row. {@code valueKey} replaces {@code value} when the number has no meaning (the
   * boiling point below the triple point). {@code trend} and {@code trendRate} may be null. {@code
   * emphasis} marks the one goal or problem row that is drawn large. {@code side} is a
   * right-aligned text on the value line (goal progress). The detail line is {@code detailKey} with
   * {@code detailArgs} on the left and {@code detailRightKey} (formatted with {@code detailRight})
   * and a battery-style bar ({@code bar} 0..1, or -1 for none) on the right.
   */
  public record Row(
      String labelKey,
      String value,
      String valueKey,
      Trend trend,
      String trendRate,
      List<CauseLine> causes,
      Tone tone,
      boolean emphasis,
      String side,
      String detailKey,
      List<String> detailArgs,
      String detailRightKey,
      String detailRight,
      double bar,
      String noteKey) {

    /** Without a note. */
    public Row(
        String labelKey,
        String value,
        String valueKey,
        Trend trend,
        String trendRate,
        List<CauseLine> causes,
        Tone tone,
        boolean emphasis,
        String side,
        String detailKey,
        List<String> detailArgs,
        String detailRightKey,
        String detailRight,
        double bar) {
      this(
          labelKey,
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
          null);
    }

    public Row(
        String labelKey,
        String value,
        String valueKey,
        Trend trend,
        String trendRate,
        List<CauseLine> causes) {
      this(
          labelKey,
          value,
          valueKey,
          trend,
          trendRate,
          causes,
          Tone.NORMAL,
          false,
          null,
          null,
          List.of(),
          null,
          null,
          -1);
    }

    public Row with(Tone tone, boolean emphasis) {
      return new Row(
          labelKey,
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

    /** The same row with a wrapped note under its lines (two lines on the panel). */
    public Row withNote(String noteKey) {
      return new Row(
          labelKey,
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

  /**
   * "Regolith Outgasser ×2: +0.20 kPa/sol". {@code sourceKey} is a language key. {@code reasonKey}
   * (nullable) is a plain-words reason the screen shows instead of the rate ("needs power").
   */
  public record CauseLine(
      String sourceKey, int count, String rate, String reasonKey, int direction) {
    public CauseLine(String sourceKey, int count, String rate, String reasonKey) {
      this(sourceKey, count, rate, reasonKey, 0);
    }

    public CauseLine(String sourceKey, int count, String rate) {
      this(sourceKey, count, rate, null, 0);
    }

    /**
     * A planet-side line (the polar frost): {@code sourceKey} is a sentence with the rate in it,
     * drawn with a shape (down for a loss, up for a gain) and a sign, never colour alone.
     */
    public boolean isPlanetSide() {
      return direction != 0;
    }
  }

  /** Below this net rate (kPa per sol) a pressure reads steady: it rounds to 0.00 on screen. */
  public static final double PRESSURE_STEADY = 0.005;

  public static final double EPSILON = 1e-9;

  public static MonitorView of(PlanetSim sim) {
    PlanetaryIndexModel model = sim.model();
    var snap = model.snapshot();
    List<Row> rows = new ArrayList<>();

    ParameterId pressure = Ids.PRESSURE;
    double p = snap.get(pressure);
    double sources = model.netRatePerSol(pressure);
    double frostRate = sim.frostRatePerSol();
    double net = sources + frostRate;
    List<CauseLine> causes = new ArrayList<>();
    var powerState = sim.power().state(sim.stepSols());
    for (PlanetaryIndexModel.Cause c : model.causes(pressure)) {
      boolean needsPower = sim.machineDrawsPower(c.source());
      String reason =
          !needsPower
              ? null
              : switch (powerState) {
                case UNPOWERED -> "monitor.cause.needs_power";
                case SHORT -> "monitor.cause.low_power";
                default -> null;
              };
      causes.add(
          new CauseLine(sourceKey(c.source()), c.count(), rate(c.ratePerSol(), "kPa"), reason));
    }
    // The polar frost is a cause line like any machine, but it is the planet pushing back: a sign
    // and a shape, no warning colour. Hidden while its rate rounds to 0.00 (at the baseline, and
    // until about 0.85 kPa).
    if (Math.round(Math.abs(frostRate) * 100) != 0) {
      boolean gives = frostRate > 0;
      causes.add(
          new CauseLine(
              gives ? "monitor.cause.polar_frost_release" : "monitor.cause.polar_frost",
              0,
              Fmt.signed(frostRate, 2) + "\u00a0kPa",
              null,
              gives ? 1 : -1));
    }
    boolean levelling =
        frostRate < 0 && sources > 0 && Math.abs(net) < PRESSURE_STEADY && pressureGoalOpen(sim);
    Row pressureRow =
        new Row(
                "monitor.row.pressure",
                Fmt.withUnit(p, p < 10 ? 2 : 1, "kPa"),
                null,
                Math.abs(net) < PRESSURE_STEADY ? Trend.STEADY : trend(net),
                rate(net, "kPa"),
                causes)
            .with(Tone.NORMAL, true);
    // "Levelling off": the sink balances the sources and the pressure goal is still ahead.
    rows.add(levelling ? pressureRow.withNote("monitor.estimate.levelling_off") : pressureRow);

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
    gateRows(sim, rows);
    return new MonitorView(model.sols(), List.copyOf(rows), historyOf(sim));
  }

  /** True while a pressure goal (a gate that compares pressure) has not latched yet. */
  private static boolean pressureGoalOpen(PlanetSim sim) {
    for (mars.gates.GateDef d : sim.gates().defs()) {
      if (!sim.gates().isLatched(d.id())
          && d.condition() instanceof mars.gates.Condition.Compare c
          && c.parameter().equals(Ids.PRESSURE)) {
        return true;
      }
    }
    return false;
  }

  /**
   * The plot data: the stored samples thinned to at most {@link #MAX_PLOT_POINTS} (first and last
   * always kept), plus the current value as the last point. Markers come from the stored events.
   */
  static History historyOf(PlanetSim sim) {
    var stored = sim.history().samples();
    List<Point> pts = new ArrayList<>();
    for (var s : stored) {
      pts.add(new Point(s.sol(), s.value()));
    }
    double now = sim.model().sols();
    double p = sim.model().snapshot().get(Ids.PRESSURE);
    if (pts.isEmpty() || now > pts.get(pts.size() - 1).sol() + 1e-9) {
      pts.add(new Point(now, p));
    }
    if (pts.size() > MAX_PLOT_POINTS) {
      List<Point> thin = new ArrayList<>(MAX_PLOT_POINTS);
      int n = pts.size();
      for (int i = 0; i < MAX_PLOT_POINTS; i++) {
        thin.add(pts.get((int) Math.round(i * (n - 1) / (double) (MAX_PLOT_POINTS - 1))));
      }
      pts = thin;
    }
    List<Marker> marks = new ArrayList<>();
    for (var e : sim.history().events()) {
      String path = e.id().substring(e.id().indexOf(':') + 1);
      marks.add(
          switch (e.kind()) {
            case MACHINE_PLACED -> new Marker(e.sol(), "monitor.event.placed." + path, null);
            case GATE_LATCHED ->
                new Marker(
                    e.sol(),
                    "gate.reached",
                    sim.gates().defs().stream()
                        .filter(d -> d.id().equals(e.id()))
                        .map(mars.gates.GateDef::nameKey)
                        .findFirst()
                        .orElse("gate." + e.id().replace(':', '.')));
          });
    }
    return new History(List.copyOf(pts), List.copyOf(marks), sim.model().unit(Ids.PRESSURE));
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
    int dusty = sim.reducedCount("mars:solar_panel");
    if (dusty > 0) {
      lines.add(new CauseLine("block.mars.solar_panel", dusty, "", "monitor.cause.dusty"));
    }
    boolean problem =
        grid.state(sim.stepSols()) != PowerGrid.State.IDLE
            && grid.state(sim.stepSols()) != PowerGrid.State.POWERED;
    Tone tone =
        switch (grid.state(sim.stepSols())) {
          case IDLE, POWERED -> Tone.GOOD;
          case SHORT -> Tone.SHORT;
          case UNPOWERED -> Tone.NONE;
        };
    boolean hasBattery = grid.capacity() > 0;
    return java.util.Optional.of(
        new Row(
            "monitor.row.power",
            "",
            key,
            null,
            null,
            lines,
            tone,
            problem,
            null,
            "monitor.power.summary",
            List.of(Fmt.signed(grid.supplyPerSol(), 1), Fmt.signed(-grid.demandPerSol(), 1)),
            null,
            hasBattery
                ? Fmt.number(grid.charge(), 1) + " / " + Fmt.number(grid.capacity(), 1)
                : null,
            hasBattery ? Math.max(0, Math.min(1, grid.charge() / grid.capacity())) : -1));
  }

  /**
   * Gate state in plain language, one goal line: the first gate not yet reached, with its progress
   * on the value line and what it needs on the detail line, plus "Goals 1 / 2" when some are
   * reached. When every gate is reached the line reads "Goals reached 2 / 2". Gates lock in, so a
   * reached goal is never lost.
   */
  private static void gateRows(PlanetSim sim, List<Row> rows) {
    var tracker = sim.gates();
    int reached = 0;
    for (mars.gates.GateDef d : tracker.defs()) {
      if (tracker.isLatched(d.id())) {
        reached++;
      }
    }
    String count = reached + " / " + tracker.defs().size();
    for (mars.gates.GateDef d : tracker.defs()) {
      if (tracker.isLatched(d.id())) {
        continue;
      }
      String base = "monitor.gate." + d.id().substring(d.id().indexOf(':') + 1);
      String side = null;
      String detailKey = base + ".progress";
      var st = tracker.state(d.id());
      if (d.hold() != null) {
        double need = tracker.holdSols(d);
        side = Fmt.number(Math.min(st.heldSols(), need), 1) + " / " + Fmt.number(need, 0) + " sols";
        if (st.falseSols() > 0) {
          detailKey = base + ".waiting";
        }
      } else if (d.condition() instanceof mars.gates.Condition.Compare c) {
        String unit = sim.model().unit(c.parameter());
        double now = sim.model().current(c.parameter());
        side = Fmt.number(now, 2) + " / " + Fmt.number(c.value(), 0) + " " + unit;
      }
      rows.add(
          new Row(
              "monitor.row.next_goal",
              "",
              d.nameKey(),
              null,
              null,
              List.of(),
              Tone.NORMAL,
              true,
              side,
              detailKey,
              List.of(),
              reached > 0 ? "monitor.goals.count" : null,
              reached > 0 ? count : null,
              -1));
      return;
    }
    if (reached > 0) {
      rows.add(new Row("monitor.row.goals_reached", count, null, null, null, List.of()));
    }
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
