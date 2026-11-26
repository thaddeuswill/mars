package mars.dimension.ice;

/** What warmed ice does (DESIGN.md §2; SME round-2 table). IDs match {@code ice_rule.json}. */
public enum IceResult {
  STAY_ICE("stay_ice"),
  SUBLIMATE("sublimate"),
  BOIL_AWAY("boil_away"),
  SHORT_PUDDLE("short_puddle"),
  STABLE_WATER("stable_water");

  private final String id;

  IceResult(String id) {
    this.id = id;
  }

  public String id() {
    return id;
  }

  static IceResult byId(String id, String where) {
    for (IceResult r : values()) {
      if (r.id.equals(id)) {
        return r;
      }
    }
    throw new IllegalArgumentException(where + ": unknown result '" + id + "'");
  }
}
