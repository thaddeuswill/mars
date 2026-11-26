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
| 15 | Coordinator mid-task request to the SME: apply PROCESS.md section 1 (no invented real numbers) and list which verdicts need an external expert | A value audit and an external-review list | `docs/science/round2-review.md` §10–11 | ✅ |
| 12 | "go with your recommendations" | Settle the open decisions with the recommended options: goal priorities T-C, interruption rules I-C, licensing plan, external review, handoff lessons | Rows in the TEAM.md Decision Log | ✅ |
| 13 | Chapter 9 pages (ACEDT): product, task, customer/SME, and local iteration; test versions; staged deployment; concept formation; objectives broad then drill down (relayed mid-task by the coordinator) | Map onto roadmap stages with a cadence; Stage 2 as a cheap first iteration; external SME risk; integration testing | ROADMAP "How We Iterate"; Stage 2 fail-cheap scope and alternatives; Stage 3 data list; R-51; QA plan test versions and integration passes; consensus brief §1.7 | 🟡 Producer drafts done; TEAM.md entry by coordinator |
| 14 | Chapter 9 pages: testing tasks (formative feedback, playtesting, software, usability, validation), environment integration, early adopters and champions, straw-man assets and face validity (relayed mid-task) | Each applied in the QA plan and roadmap | One QA plan row per testing task; staged environment integration in ROADMAP; straw-man assets in Stage 2 | 🟡 Producer drafts done |
| 16 | "change what it covers and do the reccomendation" | Assumed: treat every pending recommendation as decided, and have round 3 carry them out | Decision Log rows (round 3 decisions); round 3 agents launched | 🟡 (interpretation: owner to correct if meant otherwise) |

## Open questions for the owner

| Question | Recommendation | Status |
|---|---|---|
| Licenses for our own code, assets, and teaching materials | Code MIT; art, audio, story, journal, educator materials CC BY 4.0; LICENSE files at release | ✅ Decided (request 12) |
| External review of the science and learning design by a real expert (planetary scientist or science teacher) | One review at Stage 5 (alpha); the owner finds the reviewer | ✅ Decided; owner to identify a reviewer later |
| Is `GAME_DEV_HANDOFF.md` meant to change how we work? | Applied as guidance: PROCESS.md and BACKLOG.md | ✅ Confirmed |
| Goal priority tiers and interruption rules | T-C and I-C (consensus-round2.md) | ✅ Decided (request 12); Lead Game Designer's position still to be recorded |
| How deep should Mekanism and Create go? (cut list item 10; appeal for tech-pack fans vs. scope and load) | Keep them optional and late, with minimal recipe hooks; decide after the in-house machine list | ⏸ |
| Must the owner play a full 40-hour standard run in Stage 5? | No: classroom-preset full run (~11 h) plus standard spot checks; saves ~20 h of owner time | ⏸ |
| Include the optional delayed post-test (retention) in Stage 6? | Yes, as a design artifact; ~14 h | ⏸ |
| Stage 2 as a timeboxed, throwaway first iteration starting with paper and spreadsheet prototypes | Yes (ROADMAP Stage 2) | ⏸ |
| Keeper's Report: full-screen when offered, or a log line plus side panel that the player opens (PROCESS.md §4 vs. interruption rule 3) | Log line plus side panel; full screen only when the player chooses it (consensus-round2.md §3) | ⏸ Team first (ID, GD); owner only if they disagree |
