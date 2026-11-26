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
| Roadmap, scope, effort budget, QA plan, risk register (Producer) | 🟡 |
| Story bible fixes, forecast text, UI specs, onboarding, Shift Briefings (Game Designer) | 🟡 |
| Fading, worked examples, educator guide, delayed post-test (Instructional Designer) | 🟡 |
| Architecture decisions, export data, sound hooks (Technical Lead) | 🟡 |
| Audio direction, sound list, weather mast (Lead Artist) | 🟡 |
| Consensus on goal priority tiers and interruption rules | 🟡 |
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
| 11 | Attached `GAME_DEV_HANDOFF.md` (no message) | Assumed: apply the lessons that fit this project | `PROCESS.md`, this backlog, and role handoffs created | 🟡 (assumption: owner to confirm) |

## Open questions for the owner

| Question | Recommendation | Status |
|---|---|---|
| Licenses for our own code, assets, and teaching materials | Choose before release (not urgent) | ⏸ |
| External review of the science and learning design by a real expert (planetary scientist or science teacher) | Optional but valuable; we have no external SME | ⏸ |
| Is `GAME_DEV_HANDOFF.md` meant to change how we work? | Applied as guidance: PROCESS.md and BACKLOG.md | ⏸ |
