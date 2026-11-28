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

  public record GoalLine(String nameKey, double value, boolean partial) {}

  /** The pressure plot: points in time order, markers, and the unit for the axis label. */
  public record History(List<Point> points, List<Marker> markers, String unit, GoalLine goalLine) {
    public History(List<Point> points, List<Marker> markers, String unit) {
      this(points, markers, unit, null);
    }

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
      String noteKey,
      Goal goal) {

    /** Without a note or goal bar. */
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
          null,
          null);
    }

    /** Without a goal bar. */
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
        double bar,
        String noteKey) {
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
          noteKey,
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
          noteKey,
          goal);
    }

    /** The same row with one detail line under its main line (no arguments). */
    public Row withDetail(String detailKey) {
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
          List.of(),
          detailRightKey,
          detailRight,
          bar,
          noteKey,
          goal);
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
          noteKey,
          goal);
    }
  }

  /**
   * The next-gate bar under the goal line: {@code fraction} 0..1 (log scale for pressure, see
   * {@link GoalProgress}), its {@code percent} text, and the optional time estimate: {@code etaKey}
   * is a language key formatted with {@code etaArg} ("~%s sols at this rate"), or null for none.
   */
  public record Goal(double fraction, String percent, String etaKey, String etaArg) {}

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

  /** The Outside air line that gives way first on a full panel. */
  public static final String OUTSIDE_AIR = "monitor.pressure.outside_air";

  /** The same text, kept when the panel is full (the plot gives way instead): Landing step 3. */
  public static final String OUTSIDE_AIR_PINNED = "monitor.pressure.outside_air.pinned";

  /**
   * The view with the Outside air line pinned: shown even when the plot has to go. The server uses
   * it for a player whose Landing step 3 ("Find the Outside air line") is still open.
   */
  public MonitorView withPinnedOutsideAir() {
    List<Row> out = new ArrayList<>();
    for (Row r : rows) {
      out.add(OUTSIDE_AIR.equals(r.detailKey()) ? r.withDetail(OUTSIDE_AIR_PINNED) : r);
    }
    return new MonitorView(sols, List.copyOf(out), history);
  }

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
    pressureRow = levelling ? pressureRow.withNote("monitor.estimate.levelling_off") : pressureRow;
    // Outside air (landing-spec T4): what the planet's air is made of, static in Phase 1. It is
    // the pressure row's own second line (9 GUI px), so no row is added to a busy panel.
    if (!sim.gates().isLatched("mars:phase_2_begins")) {
      pressureRow = pressureRow.withDetail(OUTSIDE_AIR);
    }
    rows.add(pressureRow);

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
    airRow(sim).ifPresent(rows::add);
    waterRow(sim).ifPresent(rows::add);
    domeRow(sim).ifPresent(rows::add);
    gateRows(sim, rows, net);
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
    return new History(
        List.copyOf(pts), List.copyOf(marks), sim.model().unit(Ids.PRESSURE), pressureGoal(sim));
  }

  private static GoalLine pressureGoal(PlanetSim sim) {
    GoalLine last = null;
    for (var gate : sim.gates().defs()) {
      double value = pressureMinimum(gate.condition());
      if (value > 0) {
        last =
            new GoalLine(
                gate.nameKey(), value, !(gate.condition() instanceof mars.gates.Condition.Compare));
        if (!sim.gates().isLatched(gate.id())) {
          return last;
        }
      }
    }
    return last;
  }

  /** A lower pressure bound required by the gate, including conjunctions. */
  private static double pressureMinimum(mars.gates.Condition condition) {
    if (condition instanceof mars.gates.Condition.Compare c
        && c.parameter().equals(Ids.PRESSURE)
        && (c.op() == mars.gates.Condition.Op.GE || c.op() == mars.gates.Condition.Op.GT)) {
      return c.value();
    }
    if (condition instanceof mars.gates.Condition.All all) {
      return all.parts().stream().mapToDouble(MonitorView::pressureMinimum).max().orElse(0);
    }
    return 0;
  }

  /** Sols of air left under which the Air row asks for attention (balance sheet 2d.6, Low). */
  static final double AIR_LOW_SOLS = 2.0;

  /** Sols of air left under which the state is Critical (10 minutes at standard pace). */
  static final double AIR_CRITICAL_SOLS = 0.5;

  /**
   * The Air row (balance sheet 2d.6): stored oxygen out of the tank capacity, with the net a sol as
   * the trend. A falling net adds one planet-side line ("Air falling: about N sols left"); empty
   * tanks add the Blackout line. Shows only on a planet with the life-support model that has a
   * report. Text is straw-man wording (keys in en_us.json) for the Instructional Designer.
   */
  private static java.util.Optional<Row> airRow(PlanetSim sim) {
    var life = sim.life();
    if (life == null || life.report() == null) {
      return java.util.Optional.empty();
    }
    var r = life.report();
    List<CauseLine> lines = new ArrayList<>();
    boolean empty = r.o2Stored() <= EPSILON && r.started();
    boolean falling = r.started() && r.o2Net() < -EPSILON;
    boolean low = falling && r.solsOfAirLeft() < AIR_LOW_SOLS;
    if (empty) {
      lines.add(new CauseLine("monitor.air.empty", 0, "", null, -1));
      lines.add(new CauseLine("monitor.air.blackout", 0, "", null, -1));
    } else if (falling) {
      lines.add(
          new CauseLine(
              "monitor.air.falling", 0, Fmt.number(Math.max(0.1, r.solsOfAirLeft()), 1), null, -1));
    }
    Tone tone = empty || low ? Tone.SHORT : falling ? Tone.NORMAL : Tone.GOOD;
    Trend trend = r.started() ? trend(r.o2Net()) : Trend.STEADY;
    return java.util.Optional.of(
        new Row(
                "monitor.row.air",
                Fmt.number(r.o2Stored(), 0) + " / " + Fmt.number(r.o2Capacity(), 0),
                null,
                trend,
                Fmt.signed(r.o2Net(), 0) + "/sol",
                lines)
            .with(tone, empty || low));
  }

  /**
   * The Water row (balance sheet 2d.6: water speaks up only when it is a problem). Quiet while the
   * water lasts at least {@code waterLastSols} (or is not falling); then the stored water, the net
   * a sol and one line ("Water low: about N sols left" or "No water"). Keeping it quiet is also
   * what leaves room for the history plot in the normal state (see the slice 2 report).
   */
  private static java.util.Optional<Row> waterRow(PlanetSim sim) {
    var life = sim.life();
    if (life == null || life.report() == null) {
      return java.util.Optional.empty();
    }
    var r = life.report();
    List<CauseLine> lines = new ArrayList<>();
    boolean none = r.waterStored() <= EPSILON && r.started();
    boolean lasting = r.waterNetPerSol() >= -EPSILON;
    boolean low = r.started() && !lasting && r.waterSolsLeft() < life.params().waterLastSols();
    if (!none && !low) {
      return java.util.Optional.empty();
    }
    if (none) {
      lines.add(new CauseLine("monitor.water.none", 0, "", null, -1));
    } else if (low) {
      lines.add(new CauseLine("monitor.water.low", 0, Fmt.number(r.waterSolsLeft(), 1), null, -1));
    }
    return java.util.Optional.of(
        new Row(
                "monitor.row.water",
                Fmt.number(r.waterStored(), 0)
                    + " / "
                    + Fmt.number(life.params().waterTankCapacity(), 0),
                null,
                r.started() ? trend(r.waterNetPerSol()) : Trend.STEADY,
                Fmt.signed(r.waterNetPerSol(), 1) + "/sol",
                lines)
            .with(none || low ? Tone.SHORT : Tone.GOOD, none));
  }

  /**
   * The first life-support state that is not true, as a key suffix for "Waiting: ..." on the Dome
   * stable goal; null when all four are true or the planet has no life-support model.
   */
  private static String failingState(PlanetSim sim) {
    var life = sim.life();
    if (life == null || life.report() == null) {
      return null;
    }
    if (!life.report().started()) {
      return "landing";
    }
    var st = life.states();
    return !st.sealed()
        ? "sealed"
        : !st.airOk() ? "air_ok" : !st.waterOk() ? "water_ok" : !st.powerOk() ? "power_ok" : null;
  }

  /**
   * The Dome row: shows only when the planet has a registered dome (a Planetary Monitor placed).
   * One word: Sealed, Leaking, Too big to check, or Checking. A problem is drawn large like a
   * stalled machine; the cap is in the dome rules, not on the panel. Straw-man wording (keys in
   * en_us.json) until the Instructional Designer's pass.
   */
  private static java.util.Optional<Row> domeRow(PlanetSim sim) {
    var status = sim.domes().overall();
    if (status == null) {
      return java.util.Optional.empty();
    }
    String key =
        switch (status) {
          case SEALED -> "monitor.dome.sealed";
          case LEAKING -> "monitor.dome.leaking";
          case TOO_BIG -> "monitor.dome.too_big";
          case UNKNOWN -> "monitor.dome.checking";
        };
    boolean problem =
        status == mars.dome.DomeSeals.Status.LEAKING
            || status == mars.dome.DomeSeals.Status.TOO_BIG;
    Tone tone =
        switch (status) {
          case SEALED -> Tone.GOOD;
          case LEAKING, TOO_BIG -> Tone.SHORT;
          case UNKNOWN -> Tone.NORMAL;
        };
    return java.util.Optional.of(
        new Row(
            "monitor.row.dome",
            "",
            key,
            null,
            null,
            List.of(),
            tone,
            problem,
            null,
            null,
            List.of(),
            null,
            null,
            -1));
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
  private static void gateRows(PlanetSim sim, List<Row> rows, double pressureNet) {
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
      Goal goal = null;
      var st = tracker.state(d.id());
      if (d.hold() != null) {
        double need = tracker.holdSols(d);
        side = Fmt.number(Math.min(st.heldSols(), need), 1) + " / " + Fmt.number(need, 0) + " sols";
        if (st.falseSols() > 0) {
          detailKey = base + ".waiting";
        }
        String failing = d.id().equals("mars:dome_stable") ? failingState(sim) : null;
        if (failing != null) {
          detailKey = base + ".waiting." + failing; // names the state that is not true yet
        }
        double held = Math.min(st.heldSols(), need);
        goal = new Goal(held / need, GoalProgress.percent(held / need, false), null, null);
      } else if (d.condition() instanceof mars.gates.Condition.Compare c) {
        String unit = sim.model().unit(c.parameter());
        double now = sim.model().current(c.parameter());
        side = Fmt.number(now, 2) + " / " + Fmt.number(c.value(), 0) + " " + unit;
        boolean isPressure = c.parameter().equals(Ids.PRESSURE);
        double f =
            GoalProgress.fraction(now, sim.model().start(c.parameter()), c.value(), isPressure);
        String etaKey = null;
        String etaArg = null;
        if (isPressure) {
          long eta = GoalProgress.etaSols(now, c.value(), pressureNet, PRESSURE_STEADY);
          etaKey =
              eta < 0
                  ? "monitor.goal.not_rising"
                  : eta == 0
                      ? "monitor.goal.eta_soon"
                      : eta == 1 ? "monitor.goal.eta_one" : "monitor.goal.eta";
          etaArg = eta > 1 ? Long.toString(eta) : null;
        }
        goal = new Goal(f, GoalProgress.percent(f, false), etaKey, etaArg);
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
              -1,
              null,
              goal));
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
