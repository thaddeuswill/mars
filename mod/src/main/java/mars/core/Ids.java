package mars.core;

/** Well-known parameter IDs (ARCHITECTURE.md 2.5.2). Data files may define more. */
public final class Ids {
  public static final ParameterId PRESSURE = ParameterId.of("mars:pressure");
  public static final ParameterId TEMPERATURE = ParameterId.of("mars:temperature");
  public static final ParameterId HYDROSPHERE = ParameterId.of("mars:hydrosphere");
  public static final ParameterId O2_FRACTION = ParameterId.of("mars:o2_fraction");
  public static final ParameterId CO2_PARTIAL = ParameterId.of("mars:co2_partial");
  public static final ParameterId BIOMASS = ParameterId.of("mars:biomass");
  public static final ParameterId O2_PARTIAL = ParameterId.of("mars:o2_partial");
  public static final ParameterId BOILING_POINT = ParameterId.of("mars:boiling_point");

  private Ids() {}
}
