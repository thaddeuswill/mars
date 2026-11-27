# Technical Lead report: Stage 3 slice 2 (starting dome, life support, real Dome stable), 2026-11-27

## 0. Handoffs received
Owner "accept cont with recc": slice 1 accepted; build the starting dome at the start of slice 2; keep "Too big to check" as its own Monitor word. **Accepted, done** (the word is unchanged in `en_us.json`; wording is the Instructional Designer's). Spec `life-support-spec.md`: S1 (cap 2,000) and S2 (full block or nothing) **confirmed**, marked in the spec with the differences below. Codex owns `mars/learning/**`: not touched. No EULA step, no downloads.

## 1. Done (sub-steps A to G, each committed and pushed green)

| Step | What | Where |
|---|---|---|
| A | Starting dome: pure `DomeTemplate` (23 x 16 plan from `dome-layout.md`), `StarterDome` placer, `StarterDomeData` (once per planet, saved with the planet level), cracked hull plate block, oxygen tank and water reclaimer blocks, one solar array on the east apron (dust stage 1), arrival in the shelter, `group.size` and `pace.classroom` server config | `mod/src/main/java/mars/dome/DomeTemplate.java`, `mars/hull/StarterDome*.java`, `mars/machines/RegisteredMachineBlock.java` |
| B | Life support model (pure, ArchUnit-covered), `life_support.json`, `PowerGrid` priority tiers 1/2/3, `PlanetState` schema 7, old saves load | `mars/life/*`, `mars/power/PowerGrid.java`, `mars/index/sim/PlanetSim.java`, `pack/data/mars/life_support.json`, `machines/*.json` |
| C | `dome_stable.json` = all of sealed, air_ok, water_ok, power_ok, then the 3-sol hold (classroom 1, thresholds never scaled, classroom reserve 1 sol). Stand-in `lifeSupportOk()` kept only for planets without a life model | `pack/data/mars/gates/dome_stable.json`, `PlanetSim.java` |
| D | Recipe cost data (standard and classroom, from balance sheet 2d.3) and crafting-table recipes for hull plate, oxygen tank, hull door | `pack/data/mars/recipe_costs.json`, `pack/data/mars/recipe/*.json` |
| E | Monitor: Air row, Water row (only when a problem), "Waiting: ..." on the Dome stable goal naming the first failing state; Blackout game rule (slows at empty tanks, blackout after 120 s, wake in the shelter, nothing lost) | `MonitorView.java`, `MonitorLayout.java`, `mars/planets/BlackoutEvents.java`, `mars/life/BlackoutTimer.java`, `en_us.json` |
| F | Tests (below) | `mod/src/test/...`, `mars/selftest/StarterDomeGameTests.java` |
| G | Screenshots | `tools/screenshots/slice2_life.shots`, `tools/screenshots/out/20261127-223929/life_1..7_*.png` |

Jar `mod/build/libs/mars-0.3.1-stage3-b.jar`, sha256 `cf374796cdc36506c1f48fa5fa12c0a174ac40d631319257ec5ed2dfbf1189d2`.

### Dome stable timing, actual against the balance sheet 2d.7 table (unit tests, the real JSON, the real `PlanetSim`)
| Case | Table (sol) | Actual (sol) |
|---|---|---|
| Solo standard | 9.2 | 9.21 |
| Group 2 standard | 10.7 | 10.73 |
| Group 3 | 10.2 | 10.23 |
| Group 4 | 11.2 | 11.23 (3.74 h; limit 3.75 h) |
| Group 5 | 10.8 | 10.83 |
| Classroom, group 4 | 2.5 | 2.50 |

The gap of 0.01 to 0.03 sol is the test's 1/48 sol granularity. The test assumes the sheet's build rules (extra makers 0.6 then 0.4 sol after t0; panels in time). The pressure curve against the sheet is unchanged within the old test tolerance except where tiers matter (see Decisions).

### Verification (what I ran, from `mod/`, offline)
- `./gradlew spotlessCheck build test --rerun --offline`: BUILD SUCCESSFUL, **248 unit tests, 0 failures** (214 at slice 1; new: `DomeTemplateTest` 6, `LifeSupportTest` 14, `RecipeCostsTest` 4, `BlackoutTimerTest` 2, `MonitorLifeViewTest` 6; others edited).
- `./gradlew runGameTestServer --offline`: **`MARS-SELFTEST RESULT PASS passed=108 failed=0`** (95 at slice 1). New checks with real blocks: template places 3 makers, 5 tanks, reclaimer, battery, panel and the cracked plate; every required room SEALED while Hab B reads OPEN_TO_OUTSIDE at the crack; patch seals Hab B (108 air blocks); open door joins rooms; start fill 14 / 60 / 5 / capacity 200; one maker for one player makes +2 a sol; breaking the maker starves; breaking a tank lowers capacity to 160; a hole in Ops drains 4 + 5 units in half a sol; Blackout after exactly 120 s with no item or health lost and the count resets; a schema 6 world gets no dome.
- **Guard-removal proofs (they fail when the guard is removed):** (1) leak guard `sealed ? 0 : 10` replaced by 0 and the Landing guard replaced by `true`: 5 life tests failed (breach, Landing, group scaling, both timing tests); restored. (2) Tier mapping forced to tier 3: 2 tests failed (tiers, unpowered maker); restored. (3) Emptying the `not_airtight` tag made the Hab B leak check fail in the GameTest ("status=SEALED"); restored.
- **Screenshot run** (`pgrep -f runShotsClient` empty first; absolute path; four runs of about 50 s, the last is `out/20261127-223929/`; I looked at the images of the runs at 22:36 and 22:38 (`life_1` to `life_4`, `life_6`, `life_7`) and at `life_5` of the last run; the last run re-created all seven with the same script and only wording changed in between): I looked at `life_1` (dome from outside, the east-side solar array and the glass annex roof), `life_2` (the cracked plate seen from outside), `life_3` (shelter: five tanks, bunks, door), `life_4` (Ops: lectern, Monitor between windows, two crafting benches), `life_5` (Monitor, sealed, waiting for air, plot kept), `life_6` (breach: Air 10 / 200 falling -8, "Air falling: about 1.2 sols left", Dome Leaking, "Waiting: the dome must hold air."), `life_7` (Air 0 / 200, "Tanks empty", game-rule line). Earlier runs showed two layout faults that I fixed and re-shot: a "Waiting" sentence that ran off the panel, and the game-rule line that overflowed.
- Time and runs: I did not time the session. About 45 Gradle invocations, 4 screenshot runs (about 50 s each), 3 mutation runs. A full `build test` is about 6 s and `runGameTestServer` about 15 s once warm.

## 2. Decisions I made (flag if you disagree)
1. **Cap 2,000 and full block or nothing: confirmed** (spec S1, S2).
2. **Gate key `all`**, not `all_of`: the condition parser already has `all`/`any`/`not`; no new key.
3. **Life support clock = absolute sol 1.2 (`landingEndSol`)**; before it nothing is made, used or leaked, and all of air_ok, water_ok, power_ok are false so no hold can start in the Landing. This is what makes the table timing (the sheet's t0) come out.
4. **Seeds:** the Monitor, every oxygen maker, reclaimer, tank and battery register as dome seeds, so "every volume with life support must be sealed" (layout 5.4) holds. Answers the slice 1 question whether the maker should be a seed: yes, and more.
5. **O2 capacity = tanks placed x 40, at most 200** (a lost tank lowers the capacity until replaced; extra tanks do not raise it). **Water is a fixed 120 store**: there are no water tank blocks (spec had 2 tanks of 60).
6. **Power tiers replace the slice 5 equal share** (balance sheet 2d.4 says so). Effect on old numbers: with one panel, an Outgasser and two Ovens, the Ovens now run full and the Outgasser gets what is left (equilibrium 5.35 kPa, was 5.6). I updated the affected tests (`FrostSinkTest` rows 3 and 5, `PowerSimTest`, `SoloCurveComparisonTest` tolerance 0.06 to 0.08 for the storm run) and said why in the code. **The Game Designer should update the 2c decision 4 table rows 3 and 5** (4.225 instead of 4.7 for the stage 2 panel row).
7. **Legacy worlds:** a planet saved before schema 7 loads with no life model (the stand-in `lifeSupportOk()` still feeds the four gate states), is not given a dome and shows no Air row. Reason: a build at the old arrival point must not be overwritten, and a world mid-hold must not suddenly need air. A new world gets everything.
8. **Water row is quiet** while the water lasts (balance sheet 2d.6 lists water only as a problem state). This also keeps the history plot in the normal state: with the row always shown the plot was dropped.
9. **The "Waiting: ..." line outranks the history plot** in `MonitorLayout.fit` (the other goal detail still gives way first). Reason: it is the failure feedback for C2 and was never visible in a busy state. Cost: in a failing state the plot is dropped. Owner-accepted earlier rule was "detail line drops, plot stays"; this reverses it for this one line. Panel heights (GUI px, scale 3): normal with plot about 149, breach about 146 without plot, busiest (breach, low air, dusty panel) 156 without plot; target 130; the 270 px screen fits all.
10. **Blackout built small** (player effects only): so the Monitor's game-rule line is true. Not built: suits, the 30 s empty-suit case, suit refill, "Dehydrated", HUD chips.
11. **Recipes:** the sheet's machine costs are 10 to 24 items; a crafting grid holds 9. See Decisions needed.
12. **Classroom door cost** follows the spec table (3 Fe), which the "divide by 1.3, round up" rule would make 4; flagged in the data and test.
13. **Beds:** the template uses vanilla red beds as bunks (five pairs). A bed does nothing special on Mars yet; Blackout wakes you at the shelter spawn, not in a particular bunk.
14. **Dome interior is dark** (no light blocks yet): seen in `life_3`. A light block or glow is an art or design item.

## 3. Not verified or not built
- **Not built:** machine recipes; classroom recipe switching (a datapack recipe cannot read a config value); suits and suit O2; water tank blocks; HUD chips ("Unsealed", air chip); Landing flow and its grace as a story state (the grace is a sol number only); Dehydrated; failure reports with Hana's tips (slice 4/6); the airlock interlock and dust-off (slice 6).
- **Not verified:** a dedicated server and a real restart (EULA); multiplayer with two clients; a real survival player blacking out (the GameTest drives the second counter with a fake player; the screenshot player is creative); the starting dome on a real chunk border with another player nearby; `/mars go` placing the dome in a world whose spawn chunk is not loaded (it loads chunk 0,0 first; tested in the screenshot run only); redstone and pistons on the dome; Windows and macOS; GUI scales 2 and 4 for the new rows; the new wording at reading level (straw-man).
- **Pressure curve:** I did not re-run the full "curve within +-20 %" test beyond the existing `SoloCurveComparisonTest`, whose storm run now sits up to 0.079 kPa under the sheet (tolerance raised to 0.08, see Decision 6).
- The shot script uses `/mars index advance`, which skips real time; it does not prove real-time behaviour of the Landing.
- A re-run of the 7 screenshots after the last wording change was done only for the sealed shot (`life_5`); the other six are from the run just before and were not changed by it.

## 4. Decisions needed (owner)
1. **Machine recipes (ill-structured, needs the Game Designer and Artist).** The six machines cost 10 to 24 items; the 3 x 3 grid holds 9. Options: (a) two-step crafting with a "machine parts" intermediate item (needs an item and art; small code); (b) a fabricator block that takes stacks (new block, GUI; the plan's later stages may want it anyway); (c) KubeJS custom recipe with item counts when KubeJS is in the pack (cannot be verified here; KubeJS is not installed in this environment). **Recommend (a) for Stage 3**, (b) for Stage 4. Until then a lost machine comes from the creative tab or `/give`.
2. **Waiting line over the plot** (Decision 9): keep? Recommend yes (feedback beats history while something is failing).
3. **Water row only when a problem** (Decision 8): keep? Recommend yes.
4. **Legacy worlds** (Decision 7): is "keep the stand-in, no dome" right for your own test worlds? Recommend: start a new world for slice 2; old worlds are for rollback only.

## 5. Handoffs
> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** balance sheet 2c decision 4 table rows 3 and 5 assumed an equal power share; 2d.4 tiers change them.
> **Context:** `FrostSinkTest` now asserts 5.35 kPa (1 Outgasser + 2 Ovens, 1 clean panel) and 4.225 kPa (stage 2 panel row); formula 0.6 + S / 0.02 with S = 0.095 and 0.0725.
> **Needed from them:** update the table; confirm that Ovens before Outgassers in a storm is intended.
> **Done when:** the sheet and `FrostSinkTest` agree.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** straw-man strings need the content-rules pass: `monitor.row.air`, `monitor.row.water`, `monitor.air.falling`, `monitor.air.empty`, `monitor.air.blackout` (game-rule label), `monitor.water.low`, `monitor.water.none`, `monitor.gate.dome_stable.waiting.{landing,sealed,air_ok,water_ok,power_ok}`, `life.blackout`, plus the slice 1 dome keys.
> **Context:** `pack/assets/mars/lang/en_us.json`. The waiting sentences are short on purpose (they share a line with the percent); keep them under about 35 characters. "Too big to check" is its own word, unchanged.
> **Needed from them:** final wording and reading level; whether "reserve" is one idea too many for the air_ok sentence.
> **Done when:** keys final; key ids unchanged.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** new straw-man textures (script-made, 16 px): `hull_plate_cracked`, `o2_tank`, `water_reclaimer`; the dome interior has no light and the machine textures were not redone.
> **Context:** `tools/screenshots/out/20261127-223929/life_2..4`.
> **Needed from them:** replacements under the same names; a light or glow for the interior if the Game Designer agrees.
> **Done when:** same names, 16 x 16.

> **To:** Codex (learning lane) · **Problem type:** well-structured
> **Problem:** new gameplay events are available for the action log: `SealChangedEvent` (slice 1), and now Blackout (`mars.planets.BlackoutEvents.secondFor` returns true when a player blacks out; no event class yet) and life-support state changes (readable from `PlanetSim.life().states()`).
> **Needed from them:** say if you want a NeoForge event for Blackout and for air_ok / water_ok / power_ok changes; I will add them (small). Nothing in `mars/learning/**` was changed.
> **Done when:** the events you need exist and a learning test fires them.

> **To:** Lead Game Designer · **Problem type:** ill-structured (small)
> **Problem:** a solo player in the starting dome gets Dome stable at sol 9.2 only if they do nothing wrong; a second maker (11 items) would bring it near sol 6. Machine recipes are missing, so a lost maker cannot be rebuilt.
> **Your view (Technical Lead):** the loop works as designed in data; the playtest question is whether the 5-sol wait for the reserve feels like pressure or like waiting. The Monitor says what it is waiting for.
> **Needed from them:** a call after the owner's playtest; the recipe route (Decision 1).
> **Done when:** recipes exist or the creative route is accepted for the slice.

## 6. Risks
- **Machine recipes missing** means a player who loses the only maker cannot rebuild it in survival. Mitigation: creative tab or `/give`; decision pending.
- **Busiest Monitor state is 156 GUI px, no plot**; fits scale 3 and 4 on 1080p but not on a very short window.
- **A bumpy planet** needs a site check before `StarterDome.place` (it assumes flat ground and clears the dome box). Planet 1 is flat.
- **The 2,000-block cap and 10 seeds** cost about 10 fills of about 150 to 300 cells every 5 s in the starting dome (computed, not measured on weak hardware).
- **Blackout counters** are in memory, so a restart forgives the count (fine); with several players on one planet each is separate.
- **Dome stable now needs about 3.1 h solo** instead of the stand-in's 1 h (R-73): say "prototype timing" to the owner.
- A server that has `group.size` above 1 gets more makers and a lower terraforming rate (group scale 1 / (1 + 0.35 (n - 1))): existing rule, now visible because `group.size` is a real config.

## 7. Shared model updates
- **New terms:** **seed** (any block that registers a room for sealing: Monitor, maker, reclaimer, tank, battery); **tier** (power priority 1 life support, 2 Ovens, 3 the rest); **legacy world** (saved before schema 7: stand-in kept, no dome).
- **Decision recorded for the log (proposal):** Dome stable = sealed AND air_ok AND water_ok AND power_ok, then the 3-sol hold (classroom 1 sol, air reserve 1 sol); life support starts at sol 1.2 (end of Landing); owner-accepted numbers are in `life_support.json` and compared by tests. Power tiers replace the equal share.
- **Lesson:** GameTests that share one world leave blocks behind; a test that makes a hole must scrub a margin first (a neighbour's plate closed my hole).
- **Lesson:** a vanilla crafting grid holds 9 items; any cost table with counts above 9 needs a design decision before KubeJS or datapack recipes are written.
- **Lesson:** adding a second row to a UI that already sits at its height budget silently removes the plot; check `MonitorLayout` numbers in the same change.
