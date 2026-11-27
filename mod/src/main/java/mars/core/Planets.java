package mars.core;

/**
 * Which dimensions count as planets (ARCHITECTURE.md 1.3, 6): every dimension in the {@code mars}
 * namespace has its own Planetary Index. Pure, so it can be unit-tested.
 */
public final class Planets {
  public static final String NAMESPACE = "mars";

  private Planets() {}

  /**
   * @param anyLevelIsPlanet test seam: the GameTest server has no Mars level, so its run
   *     configuration sets {@code mars.selftest.anyLevelIsPlanet=true}. False in every normal run.
   */
  public static boolean isPlanetNamespace(String namespace, boolean anyLevelIsPlanet) {
    return anyLevelIsPlanet || NAMESPACE.equals(namespace);
  }
}
