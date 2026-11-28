# Technical Lead report: conductor ores, carbonate vein, starter chest, ripple sand (BACKLOG #49)

**Date:** 2026-11-28 · **Role:** Technical Lead · **Branch:** claude/elegant-brown-anu1ci · **Inputs:** `docs/science/mars-conductor-and-carbonate-ores.md` (SME, owner accepted), `docs/design/machine-priorities.md` R1, R3, R4.

## 0. Handoffs received
| From | Handoff | Answer |
|---|---|---|
| Coordinator / owner | Add two rare INVENTED ores, a starter chest, a small carbonate vein, `ripple_sand`; record the compost note | Accepted. `meteorite_iron` not built (section 3). Compost: nothing built, recorded below. |

## 1. Done
| Item | Where |
|---|---|
| Blocks `sulfide_vein` (raw copper), `magnetic_ore` (redstone), `carbonate_rock` (item `mars:carbonate`), `ripple_sand`; tooltips on the first three | `mod/src/main/java/mars/world/MarsTerrainBlocks.java`, `pack/assets/mars/lang/en_us.json` (`block.mars.<id>.tooltip`, SME one-liners; carbonate line shortened) |
| Placement (INVENTED, data): sulfide in `highland_mesa` + `rocky_flats` (one cluster per 96 chunks, size 5, y 20 to 75); magnetic in `highland_mesa` only (1 per 256, size 4, y 10 to 60); carbonate in all three land biomes (1 per 85, size 6, y 40 to 78) | `pack/data/mars/worldgen/{configured,placed}_feature/*.json`, `biome/*.json`; notes `pack/data/mars/terrain/conductor_ores.json` |
| Loot (silk touch keeps block; fortune), tags (pickaxe; stone tool for sulfide and carbonate; iron tool for magnetic, as vanilla redstone ore) | `pack/data/mars/loot_table/blocks/`, `pack/data/minecraft/tags/block/` |
| Carbonate item tag for the seed kit, gypsum fallback written in data | `pack/data/mars/tags/item/seed_kit_carbonate.json`, `terrain/conductor_ores.json` |
| Starter chest: 4 copper ingots + 8 redstone dust in a vanilla chest item named "Lander Supply Chest", given in the same once-per-world claim as the two Ovens; chat line "The lander left you a supply chest with copper and redstone. Place it and open it." (15 words, US spelling, CR-T7) | `pack/data/mars/terrain/starter_chest.json`, `mars/session/StarterChest.java`, `mars/planets/GrantEvents.java` |
| Dune ripple stripe now `mars:ripple_sand` (data only; legacy_flat never reads it) | `pack/data/mars/planets/planet_1.json` |
| Placeholder textures (add-only script) and doc | `tools/art-preview/gen_conductor_placeholder_textures.py`, `docs/art/w1-placeholder-textures.md` |
| Tests | `ConductorOresContentTest` (7 tests), boot checks `ores.placed`, `ores.loot`, `ores.starter_chest_data`, `grant.starter_chest` |
| Docs | ARCHITECTURE 2.23a, `docs/tech/launch-guide.md`, BACKLOG #49, HANDOFF section 00 |

## 2. What I ran (real output)
| Check | Result |
|---|---|
| `./gradlew spotlessCheck build runGameTestServer --offline` | BUILD SUCCESSFUL. **283 unit tests, 0 failures** (baseline 273, +10: my 7 content tests plus 3 that appeared with the Artist's untracked `mars/art` tests in the working tree). **`MARS-SELFTEST RESULT PASS passed=130 failed=0`** (baseline 126, +4 mine). |
| First boot run | Failed once: `ores.placed` expected clusters up to 6, got 8 (vanilla ore `size` is approximate: carbonate size 6 gave 8). I relaxed the bound to 8; nothing else changed. |
| Boot reads | `ores.loot`: carbonate_rock -> 1 mars:carbonate, magnetic_ore -> 4 redstone, sulfide_vein -> 1 raw_copper (iron pickaxe). `grant.starter_chest`: chests=1 copper=4 redstone=8; second player and a re-arrival get nothing. |
| legacy_flat existing-save boot checks, re-run | `terrain.legacy_slab`, `terrain.legacy_base_height`, `terrain.mode_required`, `terrain.mode_rule` all PASS. The real-client existing-save run was **not re-run**. |
| Real client (`tools/screenshots/conductor_ores.shots`, absolute path, no other client running) | `tools/screenshots/out/20261128-005152/ores_03_staged_row.png`, timestamp 00:52:54, LOOKED at it. |

## 3. What I saw and did not build
- **Seen (staged):** a close-up of the new blocks on the pad, set with `/setblock`, not natural. Front row: ripple sand (dark brown), carbonate (pale grey, banded), magnetic ore (dark, metallic speckle), sulfide vein (grey-brown, rust and gold flecks). Back row for comparison: duricrust, gypsum, vanilla redstone ore, vanilla copper ore. The vanilla copper shows green, ours does not (SME: no green). They are distinguishable at a glance. Placeholders, not art.
- **Not seen:** the ores naturally in a canyon or crater wall. I tried `/place feature` along the canyon (two shots, wrong framing, folder deleted); the ores were not identifiable. Natural placement is proven by the boot checks (`ores.placed`) and the biome lists, not by a picture.
- **Not built:** `meteorite_iron` (not trivial: surface placement and a drop rule); a "southern half" filter and a "none within 64 blocks of spawn" filter for the ores (no such placement exists; veins are underground, the pad is flat); the seed kit recipe itself (it does not exist yet; recipes should use `#mars:seed_kit_carbonate`).
- **Density is not measured.** Rarity values were chosen to match the SME targets per biome, but the share of the map in each biome was not measured, so real counts per 256 x 256 are probably lower than 1 to 2 (sulfide), 1 (magnetic), 3 (carbonate). Underground ore is exposed only where craters and the canyon cut it; players may also dig. Tune the `chance` numbers after a walk.
- The ripple_sand placeholder file is the Artist's (their script wrote it after mine); I did not commit it.

## 4. Compost to bone meal (item 5)
Nothing built. Vanilla composter already gives bone meal. SME warning kept: the soil unit also needs bone meal (`world-and-blocks.md` 3.6), so the first planter must work with washed regolith and no bone meal. Journal label is the Instructional Designer's ("Game-sized. Real growers on Mars would recycle plant waste to feed the soil...").

## 5. Decisions needed
| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Worlds that already handed out the Ovens do not get the chest (one shared flag). Add a separate flag so they get it on next login? | Well-structured | No: design exercise, new worlds are the target. Use `/give` for an old world. |
| 2 | Add a map-half placement filter (custom placement modifier) for magnetic ore? | Ill-structured (cost vs. fidelity of a small science hook) | No, for now. Biome choice (highland_mesa) carries the "old high ground" idea. |
| 3 | Build `meteorite_iron` (REAL)? | Ill-structured | Later, with a W2 biome pass; it needs placement and a drop rule. |

## 6. Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Replace three placeholder ore textures and the carbonate item.
> **Context:** `docs/art/w1-placeholder-textures.md` (new section), files `sulfide_vein.png`, `magnetic_ore.png`, `carbonate_rock.png`, `textures/item/carbonate.png`; staged view `tools/screenshots/out/20261128-005152/ores_03_staged_row.png`. Also commit your `ripple_sand.png`: my commit references it but does not include it.
> **Done when:** real PNGs with the same names are in `pack/assets/mars/textures/`.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** Confirm the starter chest amounts and that one carbonate per kit is right; write the seed kit recipe against `#mars:seed_kit_carbonate`.
> **Done when:** recipe exists and uses the tag.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Review the three tooltip labels (grade 6 to 7, CR-T7) and write the compost journal label.
> **Done when:** wording agreed; keys `block.mars.*.tooltip` updated.

## 7. Risks
- Ore density unmeasured (section 3); too rare would block machines after the chest runs out. The chest buys time; a walk-through should count veins.
- Ripple sand texture belongs to the Artist's uncommitted work; the branch tip lacks `ripple_sand.png` until they commit.
- Vanilla ore size can exceed the nominal size (8 for 6); SME "4 to 8 blocks" for carbonate is met, "3 to 6" for sulfide may be exceeded by 1 to 2.

## 8. Shared model updates
- Lesson: vanilla ore `size` is a ceiling on a spread, not an exact count; test with a bound, not equality.
- Decision (pending owner nod): the starter chest shares the Oven grant's once-per-world flag.
- Lesson: `pgrep -f runShotsClient` run from a Bash tool call matches the waiting shell itself; use `pgrep -f "[r]unShotsClient"`.
