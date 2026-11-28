package mars.planets;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import mars.session.CacheLedger;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * The gate supply-cache claims of one planet, saved in that planet's own data folder (so the ledger
 * is "per player per planet"). See {@link CacheLedger}.
 */
public final class CacheData extends SavedData {
  static final String NAME = "mars_cache";
  private final CacheLedger ledger;

  private CacheData(CacheLedger ledger) {
    this.ledger = ledger;
  }

  public CacheLedger ledger() {
    return ledger;
  }

  public static CacheData get(ServerLevel level) {
    return level
        .getDataStorage()
        .computeIfAbsent(
            new SavedData.Factory<>(() -> new CacheData(new CacheLedger()), CacheData::load), NAME);
  }

  /** Self-test only: forget every claim on this planet (refuses outside the GameTest server). */
  public static CacheData resetForSelfTest(ServerLevel level) {
    if (!Boolean.getBoolean("mars.selftest.anyLevelIsPlanet")) {
      throw new IllegalStateException("resetForSelfTest is only for the GameTest server");
    }
    CacheData fresh = new CacheData(new CacheLedger());
    level.getDataStorage().set(NAME, fresh);
    fresh.setDirty();
    return fresh;
  }

  static CacheData load(CompoundTag tag, HolderLookup.Provider registries) {
    Map<String, Set<UUID>> saved = new java.util.LinkedHashMap<>();
    CompoundTag gates = tag.getCompound("claims");
    for (String gate : gates.getAllKeys()) {
      Set<UUID> players = new HashSet<>();
      for (Tag t : gates.getList(gate, Tag.TAG_INT_ARRAY)) {
        players.add(NbtUtils.loadUUID(t));
      }
      saved.put(gate, players);
    }
    Map<UUID, Double> requests = new java.util.LinkedHashMap<>();
    CompoundTag req = tag.getCompound("requests");
    for (String key : req.getAllKeys()) {
      requests.put(UUID.fromString(key), req.getDouble(key));
    }
    return new CacheData(new CacheLedger(saved, requests));
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag gates = new CompoundTag();
    ledger
        .toMap()
        .forEach(
            (gate, players) -> {
              ListTag list = new ListTag();
              players.forEach(u -> list.add(NbtUtils.createUUID(u)));
              gates.put(gate, list);
            });
    tag.put("claims", gates);
    // Schema 2: the last resupply request time (planet sols) per player. Schema 1 saves have none.
    CompoundTag req = new CompoundTag();
    ledger.requestsToMap().forEach((u, sols) -> req.putDouble(u.toString(), sols));
    tag.put("requests", req);
    tag.putInt("schema", 2);
    return tag;
  }
}
