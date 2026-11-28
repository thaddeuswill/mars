# Technical Lead report: orbital resupply (machine core, per-gate drops, resupply request)

Date 2026-11-28 · BACKLOG #49 · Branch `claude/elegant-brown-anu1ci` · Source: `docs/design/recipe-inputs-ruling.md` v2 section 7 (owner-accepted)

## 0. Handoffs received
Ruling v2 section 7 (11 rows), owner answers (sender Relay Orbiter, coal in drops, request 4 iron + 1 coil with a 3-sol timer, four later gate parts drop-only). **Accepted**, with the deviations in section 4.

## 1. Done (all run)
| Ruling row | Built |
|---|---|
| 1 `machine_core` | Item, drop-only (no recipe), tooltip "Game design: sent from orbit. You cannot craft it.", lang name, placeholder icon (`gen_gate_item_textures.py`, never overwrites). Items have no loot table (only blocks do), so no loot file. |
| 2 to 4, 6 machine recipes | Processor 4 iron + core + cartridge; emitter 4 iron + core + coil; extractor 5 iron + core + bearing; scrubber 6 iron + core + 2 beds (iron counts as built). |
| 5 | Outgasser (8 iron + coil), reclaimer, vat, oven, battery, panel unchanged, as the ruling says. |
| 7 drop-only parts | Items `resin_pack`, `signal_board`, `activation_cell` registered (they did not exist; the gate caches need them), `sorbent_bed` craft recipe deleted; none has a recipe. Placeholder icons for all. |
| 8 `resupply_request` | Shapeless 4 iron + 1 coil. `ResupplyRequestItem.use` calls `GateCaches.request`: latest latched gate with a drop, half of each line rounded up (at least 1 of every line, so the key part is always there), same hand-out code, `grant.refill` lines. Pass timer `pass_sols` 3 in `pack/data/mars/terrain/resupply_request.json`, kept per player in `CacheLedger` against the planet's simulation clock (a pause freezes it). Early request or no drop yet: item kept, line says the wait or "nothing to send". No cap. `CacheData` schema 2 (`requests`; schema 1 loads). |
| 9 cache lists | All 7 gates have a `cache` (table in `GateInputItemsTest.DROPS`): Dome stable 4 coil, 2 furnace, 16 coal; Phase 2 3 cartridge, 6 core, 2 bucket, 16 coal; Thaw 2 bearing, 4 core; Hydrosphere 4 resin, 2 core; Phase 3 4 bed (raised from 2), 4 core, 8 glass; Breathable 5 board, 2 core; Equilibrium 1 cell. Starter chest adds table 1, furnace 2, bucket 2, coal 16. |
| 10 chat | Every supply note now reads "Sent from orbit by the Relay Orbiter. <one short sentence>" (14 to 16 words, US spelling, no forecast words); new `grant.refill`, `.note`, `.wait`, `.none`. Starter chest line shortened to 17 words. |
| Journal | `story/journal/sent_from_orbit.json`, unlocks at Dome stable (cheap, so built), text trimmed from 47 to 43 words for the 45-word budget. Journal is now 5 entries (tests and two boot checks updated). |
| 11 tests | See below. |

**Test output (from `mod/`, `./gradlew spotlessApply build runGameTestServer`):** BUILD SUCCESSFUL, **413 unit tests, 0 failures** (baseline 402; +11), **`MARS-SELFTEST RESULT PASS passed=182 failed=0`** (baseline 173; +9).
- Unit: `ResupplyTest` (5: data, refill rule, every real gate refill >= 1 per line, drops are tech only, a release touches no Index row, session classes know nothing of the Index), `CacheLedgerTest` +2 (timer, save/load), `GateInputItemsTest` rewritten around the drop table, exact machine ingredients with a ban on hopper/furnace/bucket/redstone/copper, request recipe, drop-only parts have no recipe, wording scan extended (Relay Orbiter and "Sent from orbit" present, refill lines allowed to say "sols"). Changed: journal counts, starter chest content, vanilla-first recipe test.
- Boot (new `ResupplyGameTests`, 7 checks): recipes resolve through the real recipe manager by crafting grids (`resupply.recipes_resolve`, `resupply.old_recipes_gone` incl. the old hopper processor and the bed recipe), `resupply.drops_match_table` (all 7 gates: 4 coil, 2 furnace, 32 coal, 3 cartridge, 18 core, 2 bucket, 2 bearing, 4 resin, 4 bed, 8 glass, 5 board, 1 cell), `resupply.drops_once`, `resupply.nothing_yet`, `resupply.sends_half_of_latest`, `resupply.cooldown_holds`, `resupply.index_untouched` (all six rows equal), `resupply.next_pass_sends`. `grant.starter_chest` now also checks the lander stock. Existing `cache.*` checks pass unchanged.

**Item 3, vanilla dependencies left in the mod's own recipes (grep of `pack/data/mars/recipe`):** hopper and furnace: none. Bucket: `water_reclaimer` (bucket, plus copper; ruling says no change) and a **water bucket** in `algae_vat`, `washed_regolith`, `biocrust_kit`, `wetland_kit`. A bucket is 3 iron in vanilla, and the lander stock plus two drops hand out buckets, so none is a hard block. Redstone and copper are not in any machine recipe now (they remain for lamps and the seed kits as before).

## 2. What I saw (real client, software GL, 1280x720)
Script `tools/screenshots/orbital_resupply.shots` (real pad, y 81, ends with `quit`; `pgrep` empty first). Run `tools/screenshots/out/20261128-182531/`; source PNG timestamps 18:26:07 to 18:26:10 match the copies (`cmp`); `client.log` has 0 'Unknown block'.
- `resupply_chat_phase2.png`: chat "Goal reached: Phase 2 begins", then "Supply drop received: 3 x Filter Cartridge / 6 x Machine Core / 2 x Bucket / 16 x Coal." and "Sent from orbit by the Relay Orbiter. Dust must be caught before air enters a machine." Hotbar: coil 4, furnace 2, coal 32, cartridge 3, core 6, bucket 2 with the placeholder icons.
- `resupply_chat_thaw.png`: "Supply drop received: 2 x Pump Bearing / 4 x Machine Core", the Thaw note; hotbar core count 10.
- `resupply_inventory_phase2.png`: the dev `inventory` step opens the **creative** screen in this test world, so it shows the creative tabs with the hotbar at the bottom, not the survival inventory grid. I only added the step (`DevTools` `inventory` and `close`).
Not seen: right-clicking the request in a real client (the script has no use-item step; the use path is covered by the boot checks through `ItemStack.use`), the icons at large size, animation.

## 3. Decisions needed
1. **Key part wording (well-structured).** "At least 1 key part" is met by rounding every line up, so no key list exists in data. If the Game Designer wants the refill to drop coal or glass lines, add a flag later. Recommendation: keep.
2. **Vat and reclaimer buckets (ill-structured, small).** The ruling leaves them. A learner with 2 buckets can still craft more (3 iron each). Recommendation: keep; revisit if playtest shows bucket shortage.
3. **Request in creative (well-structured).** The item is not consumed in creative (`instabuild`), so a tester can spam it, still gated by the timer. Recommendation: keep.

## 4. Deviations and gaps
- The three extra items (`resin_pack`, `signal_board`, `activation_cell`) exist now because the drops name them; nothing consumes them yet (their machines are not built).
- Hydrosphere's gate `unlocks` stays empty; the resin pack has no use until meltwater treatment exists.
- No "Sent from orbit" corner mark on icons (Artist), no Monitor hint when the player holds no core (ruling section 10), no hold-to-explain card. Report-to-orbit release not built (as ordered).
- Old worlds: gates already latched deliver the new lines (cores, coal, furnaces, resin and so on) on next login, because the ledger is per gate: **a player who already claimed a gate cache before this change gets none of the new items** (claim is per gate). They can craft a request once any gate has latched, but it needs a latched gate with a drop. Worth a note for the owner's existing saves.
- The request is not given to the player automatically; it is craftable only (ruling).

## 5. Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** replace five placeholder icons (`machine_core`, `resupply_request`, `resin_pack`, `signal_board`, `activation_cell`); `sorbent_bed` too; add the small "Sent from orbit" corner mark.
> **Context:** `tools/art-preview/gen_gate_item_textures.py`, `docs/art/w1-placeholder-textures.md` (not updated by me).
> **Done when:** real PNGs under the same names.

> **To:** Instructional Designer · **Problem type:** well-structured
> **Problem:** wording check (CR-T7, CR-F9) of `gate.supply.*`, `grant.refill*`, the five item tooltips, journal `journal.sent_from_orbit.ms` (43 words, goals A1 and B4 as the ruling lists).
> **Done when:** confirmed or replaced.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** balance sheet and DESIGN fantasy audit table need the machine core, resupply request and Relay Orbiter rows (ruling section 11); core counts are first guesses.
> **Done when:** rows added.

## 6. Risks
- Core counts (18 in total over the line) are unplayed; the 17-year-old who builds many emitters will use requests (3-sol pass).
- A request in the first sols after a gate sends only half of a drop that may be mostly coal; players might see "refill" as coal. By design, tune after a playtest.
- Old saves: see section 4.

## 7. Shared model updates
- **New terms (as the ruling proposes):** *orbital resupply*, *machine core*, *resupply request*, *Relay Orbiter*. Owning document: `docs/design/recipe-inputs-ruling.md`, built in ARCHITECTURE 2.23j.
- **Decision (Technical Lead, within scope):** refill = half of every line rounded up; the pass timer runs on the planet's simulation clock, so pausing freezes it.
- **Lesson:** the `inventory` dev step shows the creative screen in the test world; use the hotbar for item shots.
