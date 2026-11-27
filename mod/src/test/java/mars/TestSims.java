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
    Path f = TestData.data("machines/regolith_outgasser.json");
    try (Reader r = Files.newBufferedReader(f, StandardCharsets.UTF_8)) {
      JsonObject o = JsonParser.parseReader(r).getAsJsonObject();
      MachineDef d = MachineDef.parse(o, f.getFileName().toString());
      return Map.of(d.id(), d);
    }
  }

  public static PlanetSim sim(Pace pace) throws Exception {
    return new PlanetSim(index(), defs(), pace);
  }
}
