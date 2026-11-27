package mars.planets;

import java.util.UUID;
import mars.core.Guidance;
import mars.learning.PlayerSettings;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Per-player learning settings, saved once per world in the overworld's data folder, keyed by
 * player UUID (no name stored; local to the server's computer; nothing is sent anywhere).
 */
public final class PlayerSettingsData extends SavedData {
  static final String NAME = "mars_player_settings";
  static final int SCHEMA = 1;
  private final PlayerSettings settings;

  private PlayerSettingsData(PlayerSettings settings) {
    this.settings = settings;
  }

  public PlayerSettings settings() {
    return settings;
  }

  public static PlayerSettingsData get(MinecraftServer server) {
    return server
        .overworld()
        .getDataStorage()
        .computeIfAbsent(
            new SavedData.Factory<>(
                () -> new PlayerSettingsData(new PlayerSettings()), PlayerSettingsData::load),
            NAME);
  }

  public static PlayerSettingsData load(CompoundTag tag, HolderLookup.Provider registries) {
    PlayerSettings s = new PlayerSettings();
    CompoundTag players = tag.getCompound("players");
    for (String key : players.getAllKeys()) {
      CompoundTag p = players.getCompound(key);
      UUID id;
      try {
        id = UUID.fromString(key);
      } catch (IllegalArgumentException e) {
        continue;
      }
      PlayerSettings.Entry e = s.entry(id);
      if (p.contains("guidance")) {
        e.guidance = Guidance.parse(p.getString("guidance")).orElse(null);
      }
      for (Tag t : p.getList("journal_seen", Tag.TAG_STRING)) {
        e.seen.add(t.getAsString());
      }
      for (Tag t : p.getList("journal_notified", Tag.TAG_STRING)) {
        e.notified.add(t.getAsString());
      }
    }
    return new PlayerSettingsData(s);
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    tag.putInt("schema", SCHEMA);
    CompoundTag players = new CompoundTag();
    settings
        .all()
        .forEach(
            (id, e) -> {
              CompoundTag p = new CompoundTag();
              if (e.guidance != null) {
                p.putString("guidance", e.guidance.id());
              }
              ListTag seen = new ListTag();
              e.seen.forEach(x -> seen.add(StringTag.valueOf(x)));
              p.put("journal_seen", seen);
              ListTag told = new ListTag();
              e.notified.forEach(x -> told.add(StringTag.valueOf(x)));
              p.put("journal_notified", told);
              players.put(id.toString(), p);
            });
    tag.put("players", players);
    return tag;
  }
}
