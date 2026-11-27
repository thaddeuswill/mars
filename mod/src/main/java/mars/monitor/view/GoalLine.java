package mars.monitor.view;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import mars.core.Ids;
import mars.gates.Condition;
import mars.gates.GateDef;

/**
 * The goal line of the pressure plot (ui-specs 9.1, option D): the pressure threshold of the next
 * gate that is not yet reached and has a pressure condition, read from the gate data so it is the
 * same number the gate uses (invariant I-1: thresholds never scale with pace). {@code partial} is
 * true when the gate has other conditions too, so the label must say "Pressure goal".
 */
public record GoalLine(String nameKey, double value, boolean partial) {
  /** Factor from the goal to the top of the plot. A drawing choice, not a threshold (invented). */
  public static final double TOP_FACTOR = 1.2;

  /**
   * The next unlatched pressure gate by order; if all are latched, the last pressure gate (nothing
   * later is defined yet); null if no gate has a pressure condition.
   */
  public static GoalLine next(List<GateDef> defs, Predicate<String> latched) {
    List<GateDef> sorted = defs.stream().sorted(Comparator.comparingInt(GateDef::order)).toList();
    GoalLine last = null;
    for (GateDef d : sorted) {
      Condition.Compare p = pressureCompare(d.condition());
      if (p == null) {
        continue;
      }
      GoalLine line =
          new GoalLine(
              d.nameKey(),
              p.value(),
              !(d.condition() instanceof Condition.Compare) || d.hold() != null);
      if (!latched.test(d.id())) {
        return line;
      }
      last = line;
    }
    return last;
  }

  private static Condition.Compare pressureCompare(Condition c) {
    if (c instanceof Condition.Compare cmp && cmp.parameter().equals(Ids.PRESSURE)) {
      return cmp;
    }
    if (c instanceof Condition.All all) {
      for (Condition part : all.parts()) {
        Condition.Compare found = pressureCompare(part);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }
}
