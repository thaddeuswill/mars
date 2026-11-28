package mars.story.landing;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import mars.journal.JournalState;
import mars.landing.LandingState;
import mars.landing.Phase;
import mars.landing.Status;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Every player's Landing and journal on one planet, saved with the planet's dimension (landing-spec
 * 3.1: a UUID is fine here; the learning log never sees it). Read and written only on the server
 * thread. Cost: one map lookup per use; the file holds a few hundred bytes per player.
 */
public final class LandingData extends SavedData {
  static final String NAME = "mars_landing";

  /** One player's record. */
  public static final class Rec {
    public final LandingState landing = new LandingState();
    public final JournalState journal = new JournalState();

    /** The journal book has been handed to this player once. */
    public boolean granted;

    /** Monitor lines this player has been told about (the first-time "New on the Monitor"). */
    public final java.util.Set<String> revealedLines = new java.util.LinkedHashSet<>();
  }

  private final Map<UUID, Rec> players = new LinkedHashMap<>();

  public static LandingData get(ServerLevel level) {
    return level
        .getDataStorage()
        .computeIfAbsent(new SavedData.Factory<>(LandingData::new, LandingData::load), NAME);
  }

  /** The player's record, created empty on first use (marks the data dirty: it may change). */
  public Rec rec(UUID id) {
    setDirty();
    return players.computeIfAbsent(id, k -> new Rec());
  }

  /** Test seam: drop a player's record (the test world keeps saved data between runs). */
  public void forgetForTest(UUID id) {
    players.remove(id);
    setDirty();
  }

  /** True when at least one player has finished or skipped the Landing (the storm guard). */
  public boolean anyOver() {
    return players.values().stream().anyMatch(r -> r.landing.phase().over());
  }

  public int count() {
    return players.size();
  }

  /** Test seam for the save round trip. */
  public static LandingData loadForTest(CompoundTag tag, HolderLookup.Provider registries) {
    return load(tag, registries);
  }

  static LandingData load(CompoundTag tag, HolderLookup.Provider registries) {
    LandingData d = new LandingData();
    CompoundTag all = tag.getCompound("players");
    for (String key : all.getAllKeys()) {
      UUID id;
      try {
        id = UUID.fromString(key);
      } catch (IllegalArgumentException e) {
        continue;
      }
      CompoundTag t = all.getCompound(key);
      Rec r = new Rec();
      List<Status> steps = new ArrayList<>();
      for (Tag s : t.getList("steps", Tag.TAG_STRING)) {
        steps.add(parse(Status.class, s.getAsString(), Status.PENDING));
      }
      LandingState l =
          LandingState.fromSaved(
              new LandingState.Saved(
                  parse(Phase.class, t.getString("phase"), Phase.NOT_OFFERED),
                  steps,
                  t.getLong("activeSince"),
                  t.getBoolean("briefingShown"),
                  t.getBoolean("wentOut"),
                  t.getBoolean("outerDoor"),
                  t.getBoolean("ovenPlaced"),
                  t.getBoolean("patched")));
      d.players.put(id, copy(r, l, t));
    }
    return d;
  }

  private static Rec copy(Rec unused, LandingState l, CompoundTag t) {
    Rec fresh = new Rec();
    fresh.landing.restore(l.toSaved());
    List<String> un = new ArrayList<>();
    for (Tag s : t.getList("unlocked", Tag.TAG_STRING)) {
      un.add(s.getAsString());
    }
    List<String> seen = new ArrayList<>();
    for (Tag s : t.getList("seen", Tag.TAG_STRING)) {
      seen.add(s.getAsString());
    }
    fresh.journal.restore(un, seen);
    fresh.granted = t.getBoolean("granted");
    for (Tag s : t.getList("revealed", Tag.TAG_STRING)) {
      fresh.revealedLines.add(s.getAsString());
    }
    return fresh;
  }

  private static <E extends Enum<E>> E parse(Class<E> type, String name, E fallback) {
    try {
      return Enum.valueOf(type, name);
    } catch (IllegalArgumentException e) {
      return fallback;
    }
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag all = new CompoundTag();
    players.forEach(
        (id, r) -> {
          var s = r.landing.toSaved();
          CompoundTag t = new CompoundTag();
          t.putString("phase", s.phase().name());
          ListTag steps = new ListTag();
          s.steps().forEach(x -> steps.add(StringTag.valueOf(x.name())));
          t.put("steps", steps);
          t.putLong("activeSince", s.activeSinceTick());
          t.putBoolean("briefingShown", s.briefingShown());
          t.putBoolean("wentOut", s.wentOut());
          t.putBoolean("outerDoor", s.outerDoorUsed());
          t.putBoolean("ovenPlaced", s.ovenPlaced());
          t.putBoolean("patched", s.patchedByMe());
          ListTag un = new ListTag();
          r.journal.unlocked().forEach(x -> un.add(StringTag.valueOf(x)));
          t.put("unlocked", un);
          ListTag seen = new ListTag();
          r.journal.seen().forEach(x -> seen.add(StringTag.valueOf(x)));
          t.put("seen", seen);
          t.putBoolean("granted", r.granted);
          ListTag rev = new ListTag();
          r.revealedLines.forEach(x -> rev.add(StringTag.valueOf(x)));
          t.put("revealed", rev);
          all.put(id.toString(), t);
        });
    tag.put("players", all);
    return tag;
  }
}
