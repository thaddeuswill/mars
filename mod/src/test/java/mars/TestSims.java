package mars;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import mars.index.sim.IndexData;
import mars.index.sim.MachineDef;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;

/** Builds planets from the real content files in ../pack for tests. */
public final class TestSims {
  public static final String OUTGASSER = "mars:regolith_outgasser";

  private TestSims() {}

  public static IndexData.Loaded index() throws Exception {
    return IndexData.loadDirectory(TestData.data("parameters"));
  }

  public static Map<String, MachineDef> defs() throws Exception {
    Map<String, MachineDef> out = new java.util.HashMap<>();
    try (var files = Files.list(TestData.data("machines"))) {
      for (Path f : files.filter(x -> x.toString().endsWith(".json")).toList()) {
        try (Reader r = Files.newBufferedReader(f, StandardCharsets.UTF_8)) {
          JsonObject o = JsonParser.parseReader(r).getAsJsonObject();
          MachineDef d = MachineDef.parse(o, f.getFileName().toString());
          out.put(d.id(), d);
        }
      }
    }
    return out;
  }

  /** The real gate files from pack/data/mars/gates. */
  public static java.util.List<mars.gates.GateDef> gates() throws Exception {
    java.util.List<mars.gates.GateDef> out = new java.util.ArrayList<>();
    try (var files = Files.list(TestData.data("gates"))) {
      for (Path f : files.filter(x -> x.toString().endsWith(".json")).toList()) {
        try (Reader r = Files.newBufferedReader(f, StandardCharsets.UTF_8)) {
          out.add(
              mars.gates.GateDef.parse(
                  JsonParser.parseReader(r).getAsJsonObject(), f.getFileName().toString()));
        }
      }
    }
    return out;
  }

  /** The real polar-frost file, for planet 1 (baseline 0.6 kPa). */
  public static mars.index.sim.FrostSink.Params frost() throws Exception {
    try (Reader r =
        Files.newBufferedReader(TestData.data("sinks/polar_frost.json"), StandardCharsets.UTF_8)) {
      return mars.index.sim.FrostSink.Def.parse(
              JsonParser.parseReader(r).getAsJsonObject(), "polar_frost.json")
          .forPlanet("mars:planet_1");
    }
  }

  /**
   * Pressure after {@code sols} in the cold branch from {@code start}, with a net source of {@code
   * sourcePerSol} (kPa per sol) and the real frost numbers: baseline + (P0 - baseline) e^(-kt) + (S
   * / k)(1 - e^(-kt)). The sim steps in 1/240 sol, so compare with about 1e-3.
   */
  public static double coldPressure(double start, double sourcePerSol, double sols)
      throws Exception {
    var f = frost();
    double k = f.coldFractionPerSol();
    double decay = Math.exp(-k * sols);
    return f.baselineKpa() + (start - f.baselineKpa()) * decay + sourcePerSol / k * (1 - decay);
  }

  /** The real hazard data: panel dust and the storm schedule. */
  public static mars.hazards.HazardParams hazards() throws Exception {
    try (Reader d =
            Files.newBufferedReader(
                TestData.data("hazards/panel_dust.json"), StandardCharsets.UTF_8);
        Reader st =
            Files.newBufferedReader(
                TestData.data("hazards/dust_storm.json"), StandardCharsets.UTF_8)) {
      return new mars.hazards.HazardParams(
          mars.hazards.dust.PanelDustTuning.parse(d, "panel_dust.json"),
          mars.hazards.storm.StormTuning.parse(st, "dust_storm.json"));
    }
  }

  /** A planet with the real hazards switched on and a fixed seed. */
  public static PlanetSim hazardSim(Pace pace, long seed) throws Exception {
    PlanetSim s = sim(pace);
    s.configureHazards(hazards(), seed);
    return s;
  }

  public static final String OVEN = "mars:regolith_oven";
  public static final String PANEL = "mars:solar_panel";
  public static final String BATTERY = "mars:battery";

  /** Add {@code n} clean solar panels (positions 1000 up) so powered machines run at full rate. */
  public static PlanetSim withPanels(PlanetSim sim, int n) {
    for (int i = 0; i < n; i++) {
      sim.addMachine(PANEL, 1000L + i);
    }
    return sim;
  }

  public static PlanetSim sim(Pace pace) throws Exception {
    return new PlanetSim(index(), defs(), gates(), frost(), pace);
  }

  /** The real life-support numbers from pack/data/mars/life_support.json. */
  public static mars.life.LifeParams life() throws Exception {
    try (Reader r =
        Files.newBufferedReader(TestData.data("life_support.json"), StandardCharsets.UTF_8)) {
      return mars.life.LifeParams.parse(
          JsonParser.parseReader(r).getAsJsonObject(), "life_support.json");
    }
  }

  /** A planet with the real life-support model (the game's default since schema 7). */
  public static PlanetSim lifeSim(Pace pace) throws Exception {
    return new PlanetSim(index(), defs(), gates(), frost(), life(), pace);
  }
}
