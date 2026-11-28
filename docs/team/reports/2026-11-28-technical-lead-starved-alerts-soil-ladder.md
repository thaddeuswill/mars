# Technical Lead: starved-machine alert and the staged ground ladder (BACKLOG #49)

Date 2026-11-28. Branch `claude/elegant-brown-anu1ci`.

## Handoffs received
Inspiration index items 1 (starved alerts) and 2 (ground ladder, supplies `washed_regolith`). Accepted, both done in a small first version. Seed kit doc open item "does `mars:washed_regolith` exist?": it does now.

## Done (all run)
**Job 1, starved-machine alert**
- `MonitorView.alertRow` (new), `Tone.ALERT`, `alertKey`, `alertTotal`, `isAlert`; `MonitorScreen.drawAlert` (warning triangle plus text); `MonitorStyle.ALARM` (0xFFFF6FD0, used for nothing else) and `ALERT_ROW_H` 12; `MonitorLayout.rowHeight`.
- Rule: grid SHORT or UNPOWERED AND a placed machine type draws power AND changes an Index row. The reclaimer (no Index effect) is not alerted; the Power row still lists it. One line only: one type "Regolith Outgasser x2 starved of power"; several types "N machines starved of power".
- Lang: `monitor.row.alert`, `monitor.alert.starved.one|many|mixed`.
- Tests (new): `StarvedAlertTest` (6: no alert idle or powered, no power, share of power, 12 machines still one line, reclaimer not alerted, lang text and CR-T7b); `MonitorStyleTest` alarm contrast at least 4.5 to 1 over white, black and a Mars world, and different from every other Monitor colour; `MonitorLayoutTest` alert is 12 px whatever the count and the panel (161 GUI px with the alert and 12 machines, bound 179) fits at 240 and 360. `LangKeysTest` and the CR-T7 scan stayed green.

**Job 2, ground ladder (raw red sand -> washed regolith -> rich soil -> biocrust)**
- Blocks in `MarsTerrainBlocks`: `washed_regolith`, `rich_soil`, `biocrust` (plain cubes, labelled tooltips), blockstates, block and item models, loot (drops itself), shovel tag, lang names.
- Honest labels: washed regolith "Real Mars soil holds toxic salts called perchlorates, and they dissolve in water. Washing soil to remove them is game design, not verified." (basis: `docs/science/round2-review.md` 1.5; no source says washing makes Mars soil farmable). Rich soil and biocrust: "Made up for the game". No gas names (forecast F8 guard).
- Recipes (vanilla shapeless, amounts INVENTED): `mars:washed_regolith` 4 red sand + water bucket -> 4 (bucket returned); `mars:rich_soil` 4 `#mars:washed_regolith` + 1 bone meal -> 4. Tag `mars:washed_regolith` now holds the item, so the existing biocrust kit recipe resolves.
- Loop check: bone meal comes from crops (composter), the first planter works on washed regolith and water (world-and-blocks 3.6), rich soil makes no bone meal and no mars recipe makes bone meal or red sand (unit test). Bone meal cost: 1 per 4 rich soil; a 3 x 3 patch is about 2 bone meal plus 1 for the kit. INVENTED; the soil unit and kits still compete for bone meal as the seed kit doc says.
- Data: `pack/data/mars/ladder/ground_ladder.json` (four steps, action text, honesty mark each).
- Textures: none existed, so placeholders from the new `tools/art-preview/gen_ground_ladder_textures.py` (never overwrites; Artist conventions: noise on a base colour, opaque, no green). Listed in `docs/art/w1-placeholder-textures.md` (appended section). No existing texture or art file edited.
- Tests (new): `GroundLadderContentTest` (4: files and 16 x 16 textures and shovel tag, recipe chain and no loop, tag and kit, honest labels); `TerrainContentTest` includes the three ids; boot checks `ladder.washed_craftable`, `ladder.rich_soil_craftable`, `kits.biocrust_craftable` (real recipe manager, `CraftingInput`; also reads the returned bucket) and `terrain.blocks` now covers 16 blocks with loot tables.
- Docs: ARCHITECTURE 2.23f, BACKLOG #49, HANDOFF section 00, launch guide.

## Real output
`./gradlew spotlessApply` then `./gradlew build runGameTestServer` from `mod/`: BUILD SUCCESSFUL, **348 unit tests, 0 failures** (baseline 333; mine add 12; the other 3 come from uncommitted Artist test changes in the working tree, not checked), `MARS-SELFTEST RESULT PASS passed=142 failed=0` (baseline 139, +3 mine). A first boot run failed because `CraftingInput.of(3,3,...)` needs 9 stacks; fixed by padding. Not run: dedicated server, jar from a real mods folder, a real key press.

## What I saw (real client, `tools/screenshots/out/20261128-112205/`, script `tools/screenshots/starved_alert.shots`, real pad y 81, shots made 11:22:48)
- `alert_1_two_outgassers_no_power.png`: the first row under the title is a pink warning triangle and "Regolith Outgasser x2 starved of power"; below it the existing cause line "needs power" and the Power row "No power". Readable; the alert is clearly the loudest line.
- `alert_2_powered_no_alert.png`: with two panels and a battery (power 100%, "Enough power") no alert and the panel is back to its normal layout.
- `alert_3_mixed_short_dusty.png`: two Outgassers and two Ovens on a dusty panel: one line "4 machines starved of power", the plot is dropped (rows only) as designed.
- First run of shot 2 showed the alert with one panel: that was correct (5 supply against 8 demand), so I added a second panel for the "no alert" shot. The pink is close to the made-up-band purple in block art; they never share a surface. Not checked: colour-blind simulation (shape and text carry the meaning, contrast measured by the test).
- Ladder blocks and recipe were checked by boot checks and unit tests only; **not seen in a real client**.

## Decisions needed
1. Alarm colour: pink 0xFFFF6FD0 (distinct from red "No power", amber "Short"). Accept or let the Artist pick the final one in Stage 3 (change `MonitorStyle.ALARM` only). Well-structured; recommend accept.
2. Should the reclaimer ever alert? Today no (no Index effect); decide with the Dome stable question from the reclaimer report. Ill-structured (Game Designer, SME).
3. Should rich soil stay table-crafted, or move to the soil unit (phase2-machine-rates section 3)? Table crafting is the interim; the unit would replace it. Ill-structured; recommend keep until the unit exists.

## Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Replace the three ground ladder placeholders (`washed_regolith`, `rich_soil`, `biocrust` in `pack/assets/mars/textures/block/`); the biocrust should read as a crust of living things without green or a lawn.
> **Context:** `docs/art/w1-placeholder-textures.md` (last section).
> **Needed from them:** real 16 x 16 art, same file names.
> **Done when:** a real-client close-up shows the four steps side by side and they are told apart at normal distance.

> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** The ladder has no placing rules yet: hoe-to-farmland on washed regolith (planter) and kit placement on rich soil are not built.
> **Needed from them:** confirm the planter rule and what rich soil gives a crop (yield only, or a gate).
> **Done when:** a rule is in `world-and-blocks.md`, then the Technical Lead builds it (W3).

## Risks
Recipe amounts are invented. Bone meal supply is unmeasured against demand (kits, rich soil, soil unit). The alert shows whenever the grid is short, including a brief dip at dusk or a storm: no hysteresis was added (not tested over a storm in play). Biocrust is a plain block: placing a kit still does nothing (W3).

## Shared model updates
- Lesson: `CraftingInput.of(w, h, list)` needs exactly w x h stacks; pad with `ItemStack.EMPTY` in boot checks.
- Lesson: a "no alert" screenshot has to be checked against the real power numbers, or it shows a correct alert (my first shot 2).
- New term: **starved** = a powered machine that feeds the Index while the grid is short or empty.
