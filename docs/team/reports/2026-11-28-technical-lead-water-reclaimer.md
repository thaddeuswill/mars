# Technical Lead: water reclaimer registered (BACKLOG #49)

Date 2026-11-28. Branch `claude/elegant-brown-anu1ci`.

## Handoffs received
Lead Artist (`docs/art/water-reclaimer.md`): register block with facing, blockstate, item model, lang, loot, shape for 1.9 blocks, tooltip. Accepted, all done. Shape decision below. The Artist's model, textures and `docs/art` files were not edited except the status line.

## Done (all run)
- `mod/.../machines/WaterReclaimerBlock.java` (new), `MarsMachines.java` (block, item with tooltip, creative tab), `pack/assets/mars/blockstates/water_reclaimer.json` and `models/item/water_reclaimer.json` (Oven pattern), `pack/data/mars/loot_table/blocks/water_reclaimer.json`, lang keys (name, tooltip, `monitor.cause.running`, `monitor.event.placed.water_reclaimer`).
- `pack/data/mars/machines/water_reclaimer.json`: no contributions, draw 1.5 units per sol (INVENTED, stated in the file's `source`).
- `pack/data/mars/recipe/water_reclaimer.json`: shaped, iron ingot x5, glass, copper ingot x2, bucket (amounts INVENTED; all four are makeable on Mars per world-and-blocks 3.6).
- Monitor: `PlanetSim.placedTypes()` and `contributionCount()`; the Power row lists each placed power-drawing machine type that makes no Index contribution, with "needs power", "low power" or "running" (`MonitorView`, `MonitorScreen` colour).
- Tests: `mod/src/test/java/mars/machines/WaterReclaimerTest.java` (3 tests: draw from data and INVENTED mark, Monitor line in both states, lang/tooltip/loot/recipe), `MachineAssetsTest` and the boot registry check now include the reclaimer, new GameTest `waterReclaimerPlacesAndDrawsPower`.
- Docs: ARCHITECTURE 2.23b, launch guide, BACKLOG #49, HANDOFF section 00, `docs/art/water-reclaimer.md` status line.

## Real output
`./gradlew build runGameTestServer` from `mod/`: BUILD SUCCESSFUL, 288 unit tests, 0 failures (baseline 283; 3 are mine, the Artist's uncommitted `TextureSpecTest` change accounts for the rest, not checked); `MARS-SELFTEST RESULT PASS passed=133 failed=0` (baseline 130, +3 reclaimer checks; `machines.registered` now says 7). Not run: dedicated server, jar from a real mods folder, a real key press.

## What I saw (real client, `tools/screenshots/out/20261128-012239/`, shots made 01:21 to 01:23, script `tools/screenshots/water_reclaimer.shots`, on the real generated pad)
- Front: tall two-tier rack about twice the Oven's height; dark frame posts, upper canister visible through the frame, lower tank with a blue lamp, an orange-yellow spout at the foot, a plate with a pink slash, and the pink dashed band on the plinth. Reads as a machine and matches the spec's silhouette.
- East side: pale ribbed tube beside the canister, dashed band on the plinth, top plate with hatch.
- Not matching or unclear: the lower front window and the plate are small and dark at this distance (water steps not readable in-game); lamp shows blue, not the "hollow square standby" from the spec (could not tell at this size, unverified). The Oven's top corners show magenta pixels (placeholder marker, not mine). First attempt was black because my test pad filled a cave under the real terrain; the final script uses the real pad (y 81).
- Process note: my first script had no `quit`, which left the client JVM running; it overlapped a second run briefly (killed my own stale processes by PID). Artist's concurrent client run (`20261128-011135`) may have been disturbed by that overlap; re-run theirs if the folder is missing images.

## Design gap (written, not invented)
No water or life-support parameter exists in the Index data, so the reclaimer changes no Index row and is not wired to "Dome stable". Its draw does count in grid demand, so the stand-in ("grid covers demand") can now fail if the player builds many loads. `docs/design/machine-priorities.md` row 1 already says "Unclear"; the decision belongs to Game Designer and SME.

## Shape decision
One cell: outline 16 px, collision 24 px (like a vanilla fence), model 30 px overhangs the cell above (visual only). A two-block pair with an invisible filler was not built (placement and break handling for a purely visual gain). Untested: culling of the overhang at a chunk-section edge, and a player placing a block into the overhang.

## Decisions needed
1. Should the reclaimer be part of Dome stable when life support becomes real? Recommend yes (tier 1 load, machine-priorities). Ill-structured: Game Designer, SME, Instructional Designer.
2. Accept the one-cell shape with overhang? Recommend yes; revisit if culling shows. Well-structured.

## Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** In-game the front window, water steps and lamp state are hard to read at normal distance.
> **Context:** `tools/screenshots/out/20261128-012239/reclaimer_front.png`
> **Needed from them:** check whether the front window/lamp need more contrast; add lamp states and dust overlay when wanted.
> **Done when:** a close-up shot shows the water steps and lamp.

## Risks
Draw 1.5 units is invented. Overhang culling unchecked. A load with no benefit may confuse players ("why build it?"): the tooltip says only what it is; the Game Designer should decide what it gives.

## Shared model updates
- Lesson: a screenshot script needs `quit` at the end, or the client stays alive and blocks the next run.
- Lesson: screenshot scripts for the new terrain must use the real pad (top block y 80) and not fill a test pad, or they build inside a cave.
