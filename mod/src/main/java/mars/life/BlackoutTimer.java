package mars.life;

import java.util.HashMap;
import java.util.Map;

/**
 * Counts whole seconds a player has been short of breath and says when they black out (balance
 * sheet 2d.6: Blackout is a game rule; after the threshold the player wakes in their bunk and loses
 * nothing). Pure Java; keys are whatever the caller uses to tell players apart, kept in memory
 * only. Cost: one map update per short-of-breath player per second.
 */
public final class BlackoutTimer<K> {
  private final int thresholdSeconds;
  private final Map<K, Integer> seconds = new HashMap<>();

  public BlackoutTimer(int thresholdSeconds) {
    this.thresholdSeconds = thresholdSeconds;
  }

  /**
   * One second passed for {@code who}. {@code shortOfBreath} false resets the count. Returns true
   * on the second the player blacks out (the count then starts over).
   */
  public boolean tick(K who, boolean shortOfBreath) {
    if (!shortOfBreath) {
      seconds.remove(who);
      return false;
    }
    int s = seconds.merge(who, 1, Integer::sum);
    if (s >= thresholdSeconds) {
      seconds.remove(who);
      return true;
    }
    return false;
  }

  public int seconds(K who) {
    return seconds.getOrDefault(who, 0);
  }

  public void forget(K who) {
    seconds.remove(who);
  }
}
