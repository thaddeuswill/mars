package mars.learning;

import java.security.SecureRandom;
import java.util.HexFormat;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/** An opaque world identifier keeps external learning files separate for different saves. */
final class LearningWorldData extends SavedData {
  private final String id;
  private final CompoundTag planets;

  private LearningWorldData(String id, CompoundTag planets) {
    this.id = ActionEvent.requireWorld(id);
    this.planets = planets;
  }

  static LearningWorldData get(MinecraftServer server) {
    return server
        .overworld()
        .getDataStorage()
        .computeIfAbsent(
            new SavedData.Factory<>(LearningWorldData::create, LearningWorldData::load),
            "mars_learning_world");
  }

  private static LearningWorldData create() {
    byte[] random = new byte[3];
    new SecureRandom().nextBytes(random);
    var data = new LearningWorldData("w-" + HexFormat.of().formatHex(random), new CompoundTag());
    data.setDirty();
    return data;
  }

  private static LearningWorldData load(CompoundTag tag, HolderLookup.Provider registries) {
    return new LearningWorldData(tag.getString("id"), tag.getCompound("planets"));
  }

  String id() {
    return id;
  }

  int planet(String dimension) {
    if (!planets.contains(dimension)) {
      planets.putInt(dimension, planets.size() + 1);
      setDirty();
    }
    return planets.getInt(dimension);
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    tag.putString("id", id);
    tag.put("planets", planets.copy());
    return tag;
  }
}
