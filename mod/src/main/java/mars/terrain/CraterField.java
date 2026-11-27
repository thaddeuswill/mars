package mars.terrain;

import java.util.ArrayList;
import java.util.List;

/**
 * Where craters are. Stateless: a crater is a function of the world seed and a grid cell, so any
 * chunk finds the same craters without asking its neighbours. Cost: per column about 9 hashed cells
 * per level (5 levels), no allocation except the small result list.
 */
final class CraterField {
  private final TerrainParams params;
  private final long seed;
  private final List<Crater> fixed = new ArrayList<>();

  CraterField(TerrainParams params, long seed) {
    this.params = params;
    this.seed = seed;
    for (TerrainParams.FixedCrater f : params.fixed()) {
      fixed.add(
          smallCrater(f.x(), f.z(), f.diameter(), Hash.mix(seed, 0xF1ED, f.x(), f.z()), false));
    }
  }

  /** Craters whose reach covers (x, z). */
  List<Crater> near(double x, double z) {
    List<Crater> out = new ArrayList<>(4);
    for (Crater c : fixed) {
      if (reaches(c, x, z)) {
        out.add(c);
      }
    }
    for (int i = 0; i < params.smallLevels().size(); i++) {
      TerrainParams.CraterLevel l = params.smallLevels().get(i);
      int span =
          (int) Math.ceil(l.diameterMax() / 2 * params.craterShape().reachRadii() / l.cell());
      collect(out, i, l.cell(), l.chance(), l.diameterMin(), l.diameterMax(), span, x, z, false);
    }
    TerrainParams.LargeLevel g = params.large();
    int span = (int) Math.ceil(g.diameterMax() / 2 * params.craterShape().reachRadii() / g.cell());
    collect(out, 100, g.cell(), g.chance(), g.diameterMin(), g.diameterMax(), span, x, z, true);
    return out;
  }

  private void collect(
      List<Crater> out,
      int level,
      int cell,
      double chance,
      double dMin,
      double dMax,
      int span,
      double x,
      double z,
      boolean large) {
    long cellX = (long) Math.floor(x / cell);
    long cellZ = (long) Math.floor(z / cell);
    for (long i = cellX - span; i <= cellX + span; i++) {
      for (long j = cellZ - span; j <= cellZ + span; j++) {
        Crater c = craterInCell(level, cell, chance, dMin, dMax, i, j, large);
        if (c != null && reaches(c, x, z)) {
          out.add(c);
        }
      }
    }
  }

  /** The crater of one grid cell, or null when the cell has none. */
  Crater craterInCell(
      int level, int cell, double chance, double dMin, double dMax, long i, long j, boolean large) {
    long h = Hash.mix(seed, 0xC0FFEE + level, i, j);
    if (Hash.unit(h, 0) >= chance) {
      return null;
    }
    // Log-uniform diameter inside the octave: more small ones than big ones.
    double d = dMin * Math.pow(dMax / dMin, Hash.unit(h, 1));
    double cx = (i + Hash.unit(h, 2)) * cell;
    double cz = (j + Hash.unit(h, 3)) * cell;
    if (large) {
      TerrainParams.LargeLevel g = params.large();
      double depth = Hash.lerp(g.depthMin(), g.depthMax(), Hash.unit(h, 4)) * d;
      double rim =
          Math.max(params.craterShape().rimRatio() * d, params.craterShape().minRimBlocks());
      return new Crater(
          cx,
          cz,
          d / 2,
          depth,
          rim,
          params.craterShape().rimWidthFraction(),
          d / 2 * params.craterShape().reachRadii(),
          true,
          Hash.lerp(g.floorMin(), g.floorMax(), Hash.unit(h, 5)),
          Hash.lerp(g.peakMin(), g.peakMax(), Hash.unit(h, 6)) * d);
    }
    return smallCrater(cx, cz, d, h, true);
  }

  private Crater smallCrater(double cx, double cz, double d, long h, boolean ignored) {
    TerrainParams.CraterShape s = params.craterShape();
    double depth = Hash.lerp(s.depthMin(), s.depthMax(), Hash.unit(h, 4)) * d;
    double rim = Math.max(s.rimRatio() * d, s.minRimBlocks());
    return new Crater(
        cx, cz, d / 2, depth, rim, s.rimWidthFraction(), d / 2 * s.reachRadii(), false, 0, 0);
  }

  private static boolean reaches(Crater c, double x, double z) {
    double dx = x - c.cx();
    double dz = z - c.cz();
    return dx * dx + dz * dz < c.reach() * c.reach();
  }
}
