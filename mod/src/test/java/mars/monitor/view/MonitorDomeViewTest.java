package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import mars.TestSims;
import mars.dome.DomeSeals;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import org.junit.jupiter.api.Test;

/** The Monitor's Dome line: absent without a dome, one word with one. */
class MonitorDomeViewTest {
  private static Optional<MonitorView.Row> dome(PlanetSim s) {
    return MonitorView.of(s).rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.dome"))
        .findFirst();
  }

  private static PlanetSim withStatus(DomeSeals.Status st) throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.domes().restore(new DomeSeals.State(List.of(new DomeSeals.Dome(1L, st, 50))));
    return s;
  }

  @Test
  void noDomeNoLine() throws Exception {
    assertTrue(dome(TestSims.sim(Pace.STANDARD)).isEmpty());
  }

  @Test
  void eachStatusHasItsOwnWordAndOnlyProblemsAreLarge() throws Exception {
    var cases =
        java.util.Map.of(
            DomeSeals.Status.SEALED, "monitor.dome.sealed",
            DomeSeals.Status.LEAKING, "monitor.dome.leaking",
            DomeSeals.Status.TOO_BIG, "monitor.dome.too_big",
            DomeSeals.Status.UNKNOWN, "monitor.dome.checking");
    for (var c : cases.entrySet()) {
      var row = dome(withStatus(c.getKey())).orElseThrow();
      assertEquals(c.getValue(), row.valueKey());
      boolean problem =
          c.getKey() == DomeSeals.Status.LEAKING || c.getKey() == DomeSeals.Status.TOO_BIG;
      assertEquals(problem, row.emphasis(), c.getKey().name());
      assertEquals(
          problem
              ? MonitorView.Tone.SHORT
              : c.getKey() == DomeSeals.Status.SEALED
                  ? MonitorView.Tone.GOOD
                  : MonitorView.Tone.NORMAL,
          row.tone());
    }
  }

  @Test
  void theWordsExistInTheEnglishFile() throws Exception {
    String lang =
        java.nio.file.Files.readString(mars.TestData.pack().resolve("assets/mars/lang/en_us.json"));
    for (String k :
        List.of(
            "monitor.row.dome",
            "monitor.dome.sealed",
            "monitor.dome.leaking",
            "monitor.dome.too_big",
            "monitor.dome.checking",
            "dome.breach",
            "dome.repaired",
            "block.mars.hull_plate",
            "block.mars.hull_glass",
            "block.mars.hull_door")) {
      assertTrue(lang.contains("\"" + k + "\""), k);
    }
  }
}
