# Brief: early survival and exploration, first sample-return quest (Codex lane)

Written 2026-11-28 by the Claude coordinator, after the owner relayed Codex's proposal: own the **early survival and exploration phase** (landing and first EVA, surveying and sampling, first outpost), starting with the **first sample-return quest** as one small milestone. Accepted as a lane, with the boundaries below. Owner rules still apply: design exercise (no real students), standard pace only, invented numbers marked INVENTED, never hard-block the player, made-up things labelled, every invented machine gets a real reference, real field tools instead of pickaxes (world-and-blocks.md 0.2 items 5a to 5c), orbital resupply drops tech but never progress.

## The milestone (Codex's words, kept)
Check supplies, pick a site, collect a sample, return to analyze it, and unlock a reason to go farther.

## What already exists (read these, do not redo them)
| Need | Where | State |
|---|---|---|
| Real hand and field tools, 12-tool shortlist, wear ideas (Viking arm jam, Curiosity drill stall, InSight mole in duricrust) | `docs/science/field-tools.md` | Researched by the SME; **not yet mapped to Minecraft tool functions and tiers** (that mapping is open: Codex can take it) |
| Owner rules on tools, drops, make-it-work | `docs/design/world-and-blocks.md` 0.2 | Settled |
| Gate drops, caches, supply notes, request item | `docs/design/recipe-inputs-ruling.md`, `gate-input-items.md` | Design done; recipe changes **being built by the Technical Lead agent right now** |
| Report to orbit (findings report at each gate, reply, auto-release) | `docs/design/report-to-orbit.md`, `docs/learning/report-to-orbit-review.md` | Design accepted; not built. A sample is natural evidence in that report |
| Terrain with ground ice lenses, ores, craters, buttes, canyon | `docs/design/world-and-blocks.md`, `docs/science/mars-terrain-reference.md` | Built (W1, W2); survey scan **not built** |
| Gates, Monitor, power grid, machine states, caches | `ARCHITECTURE.md` 2.19 to 2.23, `HANDOFF.md` section 00 | Built |
| Monitor pages (paged screen) | `docs/design/monitor-pages.md` | Spec done; **build queued** |
| Journal, guidance, story entries as data | `pack/data/mars/story/journal/`, `docs/design/journal-and-guidance-minimum.md` | Built |
| Suit oxygen, EVA range, walking-home rule | none | **Not built, not designed.** Owner closed "find a suit mod": we build our own. This is open ground for the Codex lane |
| Landing checklist and first shift briefing text | `docs/design/messages/phase-1.md` sections 1 to 2 | Text only; the checklist is not built |

## Proposed first deliverables (Codex to confirm or change)
1. Design doc `docs/design/first-sample-return-quest.md`: the loop above in numbered steps, with the starter kit (from the lander chest), the supply and habitat checks (power, oxygen, water: use the existing Monitor numbers), the first EVA range rule (a simple, honest version: suit air as a countdown on a gauge; do not invent an Index parameter), the site choice (what the player sees to decide where to go: a hand lens, a survey scan, the terrain), the sample item (labelled, with where and when it was taken), the analysis at the habitat (a small powered bench or the Monitor; answer one practical question such as "is there ice here"), and the reward (a reason to go farther: a new site, the next drop, or a report to orbit). Mark invented numbers.
2. The tool mapping the SME handed to the Game Designer: 12 tools to Minecraft functions, tiers and wear.
3. Then build slices in small verified commits, with tests and a real-client screenshot, as in `AGENTS.md`.

## File scope (claim in BACKLOG, pull before each push)
| Area | Owner | Notes |
|---|---|---|
| New quest, sample, scoop, drill, analysis bench, suit air gauge, EVA rule: new files and packages (suggest `mars.quests`, `mars.survey`, `pack/data/mars/quests/`, `pack/data/mars/samples/`) | **Codex** | Keep Minecraft out of pure packages; ArchUnit rules apply |
| `mars.monitor.*`, `mars.client.Monitor*`, Monitor lang keys, paged Monitor | **Claude** (queued) | Do not edit until the paged Monitor lands; ask first for a new Monitor row |
| `GateCaches`, `CacheLedger`, `StarterChest`, gate JSON `cache` fields, drop items, recipes for machines | **Claude** (orbital resupply in flight) | If the quest needs a new starter item or reward, request it here or add it after that job lands |
| Report to orbit release, ledgers, Record | **Claude** | The quest can emit a "sample filed" fact that the report later reads |
| Power grid, `PlanetSim`, Index parameters, machine data | **Claude** | Shared: a new powered bench must use the existing machine JSON and `machine_state` pattern (`GatedLoadBlock`, `MachineLook`) |
| Inventory and survival changes | **Shared** | Announce in BACKLOG before editing: suit air, the starter chest, anything that changes what the player holds |

## Rules to keep
- Pull before each push; one task, one commit; never force-push; do not edit a file an agent is mid-edit on (check `git status` and the newest commits).
- Report only what you ran: the build and boot test must print `RESULT PASS` (`cd mod && ./gradlew spotlessApply build runGameTestServer`); the current baseline is in `HANDOFF.md` section 00 (402 unit tests, 173 boot checks at the Slice B tip, plus whatever lands).
- Real screenshots via `tools/screenshots/run-shots.sh` with an absolute script path; the script ends with `quit`; use the real generated pad (top block y 80); `tools/screenshots/out` is gitignored, commit only cited shots with `git add -f`.
- Text keys: every new lang key must exist (`LangKeysTest`); wording rules CR-T1 to CR-T8; US spelling.
- A sample is evidence, not progress: it must never change an Index value.

## Open questions for the owner (routed through the coordinator)
1. Suit air: a countdown gauge only, or a refill-at-the-habitat loop? Recommend countdown plus a refill at the habitat, so the first EVA is a short trip with a clear "get home" rule.
2. Should the sample analysis answer one fixed question per gate (like the findings report), or be free exploration? Recommend one fixed practical question for the first quest.
3. Where does this quest sit in the gate order: before Dome stable (Landing) or after? Recommend between Landing and Dome stable, so it teaches the tools before the first gate drop.
