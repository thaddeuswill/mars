package mars.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Keys of the planet dimensions. The dimensions themselves are data: {@code
 * data/mars/dimension/planet_<n>.json} with the {@code mars:mars} dimension type.
 */
public final class MarsDimensions {
  public static final ResourceKey<Level> PLANET_1 =
      ResourceKey.create(
          Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("mars", "planet_1"));

  private MarsDimensions() {}
}
