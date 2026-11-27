package mars.terrain;

/**
 * Which generator a world uses on Mars. Decided once per world, when its Mars level first loads,
 * and then saved with the world: new worlds get {@link #NOISE_V1}; a save that already has Mars
 * data keeps {@link #LEGACY_FLAT} (the pre-W1 flat slab) forever.
 */
public enum TerrainMode {
  LEGACY_FLAT("legacy_flat"),
  NOISE_V1("noise_v1");

  private final String id;

  TerrainMode(String id) {
    this.id = id;
  }

  public String id() {
    return id;
  }

  /** The mode saved under {@code id}, or null if the text is not a known mode. */
  public static TerrainMode fromId(String id) {
    for (TerrainMode m : values()) {
      if (m.id.equals(id)) {
        return m;
      }
    }
    return null;
  }
}
