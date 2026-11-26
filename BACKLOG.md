# Backlog

Every request from the owner, in their words, with what's wanted and how we'll know it's done. Requests that arrive mid-task are added here and routed; they only go straight into work in progress if they refine the exact thing being built. Owner: Producer; the coordinator adds entries as they arrive.

Status: ⬜ not started · 🟡 in progress · ✅ done · ⏸ waiting on the owner

## Current iteration: Round 2

**Goal:** clear all open handoffs from round 1, check the science, bring the plan and documents up to date, and apply the owner's new research (Chapters 3–6 and 9, process handoff).

**Settled up front** (TEAM.md Decision Log): custom Mars world, gates lock in, global progress, groups of 3–5, 75 kPa, one server with a Mars world per group, Minecraft 1.21.1, custom journal, reflections in a separate file, Lead Artist owns audio, owner playtests personally, design exercise (design as if real students will use it).

**Risks** (see `docs/production/risk-register.md`): too much content for one release; no external SME; heavy scope in classroom mode.

| Item | Status |
|---|---|
| Science and NGSS verification of new claims (SME) | 🟡 |
| Roadmap, scope, effort budget, QA plan, risk register, document map, README (Producer) | 🟡 Drafts done; accepted pending consensus and owner decisions |
| Iteration model (ACEDT, Chapter 9) in ROADMAP; QA testing tasks; process files reviewed (Producer) | 🟡 Drafts done |
| Story bible fixes, forecast text, UI specs, onboarding, Shift Briefings (Game Designer) | 🟡 |
| Fading, worked examples, educator guide, delayed post-test (Instructional Designer) | 🟡 |
| Architecture decisions, export data, sound hooks (Technical Lead) | 🟡 |
| Audio direction, sound list, weather mast (Lead Artist) | 🟡 |
| Consensus on goal priority tiers and interruption rules | ✅ Decided by the owner (T-C, I-C); Game Designer agrees (rule 3 can live with it) |
| Full-team story review before Stage 3 | ⬜ |

## Requests

| # | Owner's words | Wanted | Done when | Status |
|---|---|---|---|---|
| 1 | "Mars Terraforming Modpack Design … total conversion Minecraft modpack centered on progressive planetary terraforming" | A design for a three-phase terraforming modpack | `DESIGN.md` exists with phases and a build plan | ✅ |
| 2 | "global progress, singleplayer and small servers, around 40 hours" | Scope, audience, and length decisions | Recorded in DESIGN.md Decisions | ✅ |
| 3 | "custom mars world, gates lock in, agree on scaling" | Custom dimension, locked gates, scaling by infrastructure | Recorded and reflected in architecture | ✅ |
| 4 | "i need you to create these agent prompts to work along side of you" | An agent per LGD role | Six agent files in `.claude/agents/` | ✅ |
| 5 | "run all three first assignments in parallel" | Story bible, science review, consistency review | Three reports filed | ✅ |
| 6 | "groups of 3-5 students, go with your recommendations" | Classroom group model and the recommended decisions applied | Decision Log rows; round 1 handoffs applied | ✅ |
| 7 | "btw this isnt for actual student i like using student design for these types of games" | Student-centered design as an exercise, not a real deployment | Recorded in TEAM.md; owner chose "Keep it all as a design exercise" | ✅ |
| 8 | "go with 75 kPa, I'll playtest it myself" | Equilibrium pressure 75 kPa; owner as the playtester | Recorded; playtest plan written for one adult | ✅ |
| 9 | "go with your recommendations, artist owns audio, start the round" | Round 2 launched with those decisions | Round 2 agents run | 🟡 |
| 10 | Research pages and notes (Chapters 2–6, 9; fantasy, fun, fidelity, flow, motivation, iteration, testing) | Each applied to the right document and role | Entries in the TEAM.md Decision Log; owning documents updated | 🟡 |
| 11 | Attached `GAME_DEV_HANDOFF.md` (no message) | Assumed: apply the lessons that fit this project | `PROCESS.md`, this backlog, and role handoffs created | ✅ Confirmed by "go with your recommendations" |
| 12 | "go with your recommendations" | Settle the open decisions with the recommended options: goal priorities T-C, interruption rules I-C, licensing plan, external review, handoff lessons | Rows in the TEAM.md Decision Log | ✅ |
| 13 | Chapter 9 pages (ACEDT): product, task, customer/SME, and local iteration; test versions; staged deployment; concept formation; objectives broad then drill down (relayed mid-task by the coordinator) | Map onto roadmap stages with a cadence; Stage 2 as a cheap first iteration; external SME risk; integration testing | ROADMAP "How We Iterate"; Stage 2 fail-cheap scope and alternatives; Stage 3 data list; R-51; QA plan test versions and integration passes; consensus brief §1.7 | 🟡 Producer drafts done; TEAM.md entry by coordinator |
| 14 | Chapter 9 pages: testing tasks (formative feedback, playtesting, software, usability, validation), environment integration, early adopters and champions, straw-man assets and face validity (relayed mid-task) | Each applied in the QA plan and roadmap | One QA plan row per testing task; staged environment integration in ROADMAP; straw-man assets in Stage 2 | 🟡 Producer drafts done |
| 15 | Coordinator mid-task request to the SME: apply PROCESS.md section 1 (no invented real numbers) and list which verdicts need an external expert | A value audit and an external-review list | `docs/science/round2-review.md` §10–11 | ✅ |
| 16 | "change what it covers and do the reccomendation" | Assumed: treat every pending recommendation as decided, and have round 3 carry them out | Decision Log rows (round 3 decisions); round 3 agents launched | 🟡 (interpretation: owner to correct if meant otherwise) |
| 17 | Lead Artist round 2 requests relayed by the coordinator: budget three pixel fonts, sound production hours, the Stage 2 face-validity review; proposed defaults: one original ending music piece, Perseverance recording only if licensed | Fold into budget, cut list, and roadmap | budget.md revision 2 (§2.1, §2.3, cut items 3 and 5); ROADMAP Stages 2–4 | 🟡 Producer drafts done |
| 18 | "ill just wait for the launchable product" | Skip the paper playtest; build toward a launchable pack. Stage 1 first: a mod skeleton that builds, launches, and loads an empty Mars world; Stage 2 is the first playable | A built mod jar, a headless server boot test, launch instructions for the owner | 🟡 (network allowlist extended with the owner's approval) |
| 19 | "is there a way to load up blender and check what you have and what it looks like and screenshot that and show me for iteraations" | A way to render the art, look at it, and show the owner pictures to iterate on | `tools/art-preview/`; renders shown to the owner; the Artist produces a first batch of placeholder assets and contact sheets for review | 🟡 (Blender works here; proof render done; owner approved the recommended plan: source review of the Blockbench MCP before running it, and real in-game screenshots via a dev-only client helper after Stage 1)|

## Open questions for the owner

| Question | Recommendation | Status |
|---|---|---|
| Licenses for our own code, assets, and teaching materials | Code MIT; art, audio, story, journal, educator materials CC BY 4.0; LICENSE files at release | ✅ Decided (request 12) |
| External review of the science and learning design by a real expert (planetary scientist or science teacher) | Three targeted reviewers before Stage 3 (#16) plus one broad review at Stage 5 (#12); the owner finds them (owner-actions.md) | ✅ Decided; owner to identify a reviewer later |
| Is `GAME_DEV_HANDOFF.md` meant to change how we work? | Applied as guidance: PROCESS.md and BACKLOG.md | ✅ Confirmed |
| Goal priority tiers and interruption rules | T-C and I-C (consensus-round2.md) | ✅ Decided (request 12); Lead Game Designer's position still to be recorded |
| How deep should Mekanism and Create go? | Optional and late, unlocked at Hydrosphere | ✅ Decided (#16) |
| Must the owner play a full 40-hour standard run in Stage 5? | No: full classroom-preset run plus spot checks | ✅ Decided (#16) |
| Include the optional delayed post-test (retention) in Stage 6? | Yes, as a design artifact | ✅ Decided (#16) |
| Stage 2 as a timeboxed, throwaway first iteration starting on paper | Yes | ✅ Decided (#16) |
| Keeper's Report: full-screen when offered, or a log line plus side panel | Log line plus side panel; full screen only by the player's choice or at the next natural pause | ✅ Settled by the team (Game Designer and Instructional Designer agreed in Round 2) |
| Do the three reviewers before Stage 3 replace the Stage 5 external review, or add to it? | Add to it: the early three check the riskiest verdicts; Stage 5 checks the whole journal and educator guide (16 h of SME time) | ⏸ |
| If Stage 4 re-estimates more than ~15% over budget after the slice, which release-scope option? | Not yet: the team frames options before the Stage 3 debrief (budget.md §4.2) | Later (after Stage 3) |
