# Stage 3 Plan: Vertical Slice (gameplay Phase 1 complete, with the minimal learning layer)

**Owner:** Producer (planning); each slice is owned by the role named in its row · **Written:** 2026-11-27, docs only, before any Stage 3 code · **Stage:** 3 Vertical Slice (ROADMAP.md) · **Mirrors:** [stage2-plan.md](stage2-plan.md) · **Follows:** PROCESS.md §1 (plan, risk gate, build, verify, report, retro)
**Related:** ROADMAP.md Stage 3 (scope list, exit criteria, "What we want to learn"), LEARNING.md §3.5, DESIGN.md Phase 1, [balance-sheet.md](../design/balance-sheet.md) §2, §2b, §5.3, [budget.md](../production/budget.md) §2.1, §6, [risk-register.md](../production/risk-register.md), [test-strategy.md](test-strategy.md), [Stage 2 checkpoint](../team/reports/2026-11-27-producer-stage2-checkpoint.md)

**Status of this file:** the plan. Nothing here is built. Every estimate is **unverified**. The plan is the Producer's view; the Technical Lead should re-estimate each slice before starting it (the Stage 2 lesson, checkpoint section 6.4).

## 1. What Stage 3 must prove

From ROADMAP: the first gameplay phase at near-final quality, as the template for Phases 2 and 3, with a thin learning layer. **Exit criteria:** the owner gets from spawn to the Phase 2 gate without help; the time is within ±25% of the balance-sheet curve (standard 6.5 to 10 h to 5 kPa, classroom 2 to 3 h); the learning layer works end to end (own vs. team evidence, CSV by actor ID); the class server runs the slice for several planets; QA exit criteria met; build version recorded.

Stage 3 is also the **staged deployment**: it produces the measured rates that decide whether Stage 4 fits (budget.md §4). That makes measurement a deliverable, not a by-product (slice 10, and the per-slice line in section 3).

## 2. Starting point (what Stage 2 left, so nothing is built twice)

Stage 2 slices 1 to 8 are built and verified (jar 0.2.0-stage2; 174 unit tests, 82 boot checks at the last run, per BACKLOG #54). Already in place and reused: planet values and Index simulation, session clock and pause, power grid (solar panel with dust-stage output, battery), Outgasser, Oven, polar-frost sink, gates as data (Dome stable on a **stand-in**, Phase 2 begins), Monitor with history plot, next-goal bar and option D scale, sky and fog keyframes, admin commands and checkpoints, classroom gate switch (`creativeGateBypass`), starting-Oven grant. Empty `mars.hazards`, `mars.learning`, `mars.story`, `mars.fauna` packages exist (placeholders); `mars.hazards.dust` and `mars.dimension.ice` have some content (**not re-read for this plan; the Technical Lead should say how much**).

### 2.1 Carry-over from Stage 2 (deferred on purpose) and where it lands

| Deferred item | Source | Lands in slice | Owner role |
|---|---|---|---|
| Life support replacing the Dome stable stand-in (pressure back on the balance-sheet curve, about 3 h to Dome stable) | balance-sheet §2 build note; HANDOFF §3 | 2 | Technical Lead |
| Shortfall priority tiers (`priority` in machine JSON: life support, Ovens, terraforming) | balance-sheet §5.2 | 2 | Technical Lead, Game Designer |
| Recipes for lost machines (Outgasser, Oven, panel, battery) | slice 5 report; HANDOFF §3 | 2 | Technical Lead (KubeJS), Game Designer (cost) |
| Dome stable "On Earth" trigger and journal entry; SME to confirm the replacement sentence | HANDOFF §0 items 3b, 24 | 4 | Instructional Designer, SME |
| Rose-sky (5 kPa) journal label trigger | HANDOFF §0 | 4 | Game Designer, Instructional Designer |
| Landing step 6 text ("battery and Ovens") in `docs/design/messages/phase-1.md` | HANDOFF §0 | 4 | Game Designer |
| Next goal row showing an unmet temperature condition | HANDOFF §0 | 4 | Game Designer (spec), Technical Lead |
| "Useful now" lists (up to three builds beside the next-gate bar) | slice 7 report | 4 | Game Designer (content), Technical Lead |
| Forecast `resolve_source` JSON and the CR-F10 test | AGENTS.md section 6; CX-4 | 5 | Technical Lead (Codex drafts the test spec, section 6) |
| Panel dust remainder: accumulation by time, storm-end dust, rain n/a in Phase 1, dust-devil roll, dust cause line, brush, wiper | budget 6.2; ARCHITECTURE 2.17 | 5 | Technical Lead |
| Storms (visibility, solar penalty) | ROADMAP | 5 | Technical Lead, Artist |
| Machine wear and maintenance parts | ROADMAP | 6 | Technical Lead, Game Designer |
| Perchlorate dust-off station (Phase 1) | ROADMAP | 6 | Technical Lead, Game Designer, SME |
| Classroom switch for gate bypass | R-63 | Done (BACKLOG #54); playtest records the mode | Producer (report label) |
| Pack config and classroom pacing (stage2-plan slice 9) | stage2-plan | 9 | Technical Lead |
| Third-party machine pause GameTest (Mekanism, Create) | slice 2 | Stays deferred: those mods are Stage 4 and late | Technical Lead |
| Pause blocks player actions; HUD pause line | HANDOFF §5 | 9 | Technical Lead |
| Sun texture and sky follow-ups (night and dusk frames, dashed horizon question) | BACKLOG queued | Slice 0 (see 4) | Technical Lead / Codex (screenshot script) |

### 2.2 Open owner items (not blockers; the plan names when each starts to bite)

| Item | When it bites | Fallback if still open |
|---|---|---|
| **EULA** for the dedicated-server self-test and a real restart (R-62) | Slice 9 (class server) cannot be claimed verified without it | Owner reloads a singleplayer world by hand and reports; the class-server exit criterion stays "not covered" |
| **Three external reviewers** (planetary scientist, climate modeler, teachers; R-51) | Before Stage 3 content is final: needed by slice 5 (F1 distractors, ice rule) and slice 7 (F4a, F2) | Content proceeds as "accepted, pending external review"; changes after replies are budgeted at about 10% of the content items they touch (Producer guess, unverified) |
| **Platform checklist** (Windows, macOS, weak hardware; first required at Stage 3 exit) | Slice 10 | Record "not covered"; weak-hardware gap is the one that matters (R-08) |
| **Jar from a real `mods` folder / Launcher profile** | Every slice's owner playtest depends on it working | Owner's own install; pending since slice 8 |

## 3. Slices, in order

Order rule: each slice is **shippable** (the build passes its checks, `./gradlew build` jar loads) and **playtestable by the owner** with a stated 15 to 30 minute session. Early slices build the physical loop (survive, then terraform); the learning plumbing comes before the content that emits events into it; the riskiest content (storms and forecasts) comes before the last-mile art, so a failure shows while there is still budget. Each slice ends with the same checks as Stage 2: unit tests, GameTest boot (`RESULT PASS`), a real-client screenshot looked at, a "prove it can fail" run, and a report with the elapsed-time line (section 3.1).

**Estimate unit.** The Stage 2 effort rate is only partly measured (budget 6.4 asked for it from slice 5 on). What exists: Technical Lead slice 5 about 15 min wall and about 30 Gradle runs; slice 6 about 35 min and 12 runs; slice 7 about 8 min and about 13 runs plus four 42 s screenshot runs; slices 1 to 4 "about one session" each, slice 8 not recorded. So a **thin, well-specified Technical Lead slice cost about 10 to 35 minutes of agent time and 12 to 30 Gradle runs**, a sample of three. Stage 3 slices are larger and carry content, art and design inputs the agent does not control, so the estimates below are in **"sessions" (one session = one Technical Lead report, about one Stage 2 slice)**, as a ratio to what was measured, with a wide range. They are not hours and not dev-days; the budget.md Stage 3 rows (human-equivalent hours) are **not** reduced from them (R-68). Owner time is the real calendar driver and is shown separately.

| # | Slice | Primary owner | What is delivered | Proof (tests and checks) | Technical Lead sessions (unverified) | Other roles' deliverables | Needs first |
|---|---|---|---|---|---|---|---|
| **0** | **Close-out and jar for owner** | Technical Lead, Codex | Sky queue items done (sun, night and dusk screenshot shots); launch guide checked; version bump `0.3.0-stage3-a` rule; the owner's real-`mods`-folder test requested | Existing 174 tests and 82 boot checks still pass; screenshots timestamped | 0.5 | none | Nothing |
| **1** | **Spawn site, starting dome, sealing and breach** | Technical Lead | Mars spawn site with terrain; starting dome structure sized for 5 (straw-man blocks allowed; M-01, M-02 are F-S/F-E assets); flood-fill sealing per ARCHITECTURE 2.3 with cap and cache; breach: break glass, air vents, "sealed" flag synced to client for the O2 HUD; seal breach event emitted for later logging | Unit: flood fill (open, closed, cap hit, boundary edit invalidates cache); GameTest: place and break a wall, flag flips within a stated tick count; screenshot of dome and a breach | 3 to 5 | Game Designer: dome footprint and what is inside (bunks 5, ovens spot, mast spot). Artist: straw-man dome frame and glass models, frost rim not needed yet. SME: none | Game Designer's dome spec (section 5, input G1) |
| **2** | **Life-support loop and the real Dome stable** | Technical Lead, Game Designer | Oxygen maker (CO2, 3x MOXIE), water reclaimer, food stub; use scales with real player count (DESIGN dome rule); suit O2 refill from wall tanks; `priority` tiers in machine JSON (life support never cut while lower tiers draw); Dome stable gate on the real state (sealed, O2 and water positive, power covers use), 3-sol hold, hold reset as data; **recipes** for panel, battery, Oven, Outgasser, oxygen maker (KubeJS); old stand-in removed; pressure curve measured against balance-sheet §2 | Unit: priority sharing, scaling with player count, gate state; the balance-sheet curve test (curve within ±20% for the reachable parts); GameTest: unpowered oxygen maker starves, breach drains O2; screenshot of Monitor Dome tab | 4 to 7 | Game Designer: invented numbers for O2 and water use per player, tank sizes, recipe costs (all labelled invented, in balance sheet §2b). SME: check O2 and water use against ISS-scale figures, labelled verified or not. Artist: oxygen maker and reclaimer final-essential or straw-man models (M-05 exists as placeholder). Instructional Designer: none | Game Designer's life-support numbers (input G2) |
| **3** | **Learning plumbing: action log, pseudonymous IDs, CSV, `guidance`, interruption config** | Technical Lead | `mars.learning`: pseudonym mapping (UUID never written), JSON Lines action log with the Stage 2 events (placement, gate latch, Monitor open, seal breach), batched background writer, CSV export (all goals, one row per actor ID), `guidance` setting (gentle, standard, expert; expert not offered in classroom), interruption caps as config values, `forget` of the mapping | Unit: log writer accepts only a pseudonym (ArchUnit rule: no UUID type in the writer), name scrubbing, CSV columns; GameTest: two fake players, correct actor ID per event; privacy check that no UUID or username appears in any file written | 4 to 6 | Instructional Designer: event list and CSV columns per goal (LEARNING §3.5, ARCHITECTURE 8.3, exists; confirm it matches). SME: none | Event schema confirmed (input I1) |
| **4** | **Story queue, Landing, Briefing, journal, "Useful now"** | Game Designer (content), Technical Lead (hooks) | Message queue with the interruption rules; Landing (about 20 to 30 min, skippable, first forecast only after it); Shift Briefing 1; first Outgasser worked example (skippable); journal skeleton with the Dome stable entry and its "On Earth" line; rose-sky 5 kPa label; Next goal row for an unmet temperature condition; "Useful now" lists; starting-grant and Monitor wording keys applied | Unit: queue caps and deferral, Landing flow state machine, content rules CR-T7, CR-T8, reading-level check on every string; GameTest: Landing skip; screenshot of each message surface; owner walks it as Lens A | 4 to 6 | Game Designer: Landing step 6 text, rose-sky trigger, "Useful now" content, story beats 1, 3. Instructional Designer: wording check, Briefing notice line, journal entry reading level, trigger for "On Earth". SME: confirm the "On Earth" sentence. Artist: message panel and journal look (reuse Monitor palette), fonts straw-man | Landing and message text final (inputs G3, I2, S1) |
| **5** | **Dust, storm and the first forecast (F1)** | Technical Lead | Panel dust accumulation by time (3 sols per stage, scaled by pace), brush and wiper, dust cause line; dust storm event with about 1 sol warning, visibility and solar penalty (storm cap per phase), storm-end dust; forecast system: data-driven forecasts, hidden answers in a group, **lock point**, `resolve_source` JSON, **CR-F10 test**; F1 content; "What caused it?" prompt | Unit: stage timing vs. balance sheet, storm cut formula, lock point never after a hint, CR-F10 test fails on a leaking answer; GameTest: storm event emits warning then solar drop; screenshots: storm sky, dusty panel | 6 to 10 (largest code slice; panel dust alone was costed at 11.5 to 12.5 dev-days by the Technical Lead) | Game Designer: storm tuning, F1 text and distractors, Hana's tip. Instructional Designer: F1 lock point and feedback format (DOCUMENTATION §5.1). SME: dust numbers and the F1 key; external teacher reviewer's reply if in. Artist: storm sky keyframes, dust overlay textures, storm sound AU stubs | Stage 2 panel-dust re-cut (input T1); F1 distractors (input I3) |
| **6** | **Wear, airlock, EVA tier 2, perchlorate dust-off, failure report** | Technical Lead | Machine wear and maintenance parts (filter every 2 sols is the anchor); airlock with dust-off station and perchlorate exposure effect; EVA suit tier 2 unlocked by Dome stable; failure report for seal breach with Hana's tip and per-player fading state | Unit: wear timers as data, exposure effect, fading state per pseudonym; GameTest: airlock cycle, skipped dust-off builds exposure; screenshot of airlock | 5 to 8 | Game Designer: part intervals, exposure effect strength (invented, labelled). SME: perchlorate claims verified or not. Instructional Designer: failure report text (C2). Artist: airlock, dust-off station, suit tier 2 models | Slice 1 dome and slice 3 plumbing done |
| **7** | **Radiation event, ice-to-vapor, F2 and F4a** | Technical Lead | Radiation spike with about 1 sol warning, shielding, dosimeter; ice rule Phase 1 part (ice vapor below the triple point); forecasts F4a and F2; sample tray at the water reclaimer | Unit: boiling point at pressure, F4a trigger below 5 kPa; GameTest: spike with and without shielding; screenshot of vapor wisps | 4 to 7 | Game Designer: spike frequency, F2 text. SME: ice rule and F4a numbers (invented puddle time marked), external reviewer replies. Instructional Designer: F2, F4a flow. Artist: vapor wisps, aurora hint optional, dosimeter | Slice 5 forecast system |
| **8** | **Keeper's Record skeleton, mini pre/post test, group planets** | Technical Lead, Instructional Designer | Record with C1 and C2 pages (own evidence only) and the "Cairn team" section; the 6-item test as paper plus log events; planet registry, player assignment, group rate scale; teacher overview skeleton | Unit: evidence per actor ID never leaks across players; GameTest: two players on one planet, two planets | 5 to 8 | Instructional Designer: 6 test items, rubric, teacher quick-start update. Game Designer: group-role text. Artist: Record look | Slice 3, 5 and 7 |
| **9** | **Pack config, class server, load test** | Technical Lead | `pace.*` and `group.size` as server config and classroom preset; pause blocks actions and shows a HUD line; server setup guide tested; 4 planets by 4 simulated players | Unit (Pace), GameTest config read; load test result: memory, CPU, tick time (L6) | 3 to 5 plus owner machine time | Instructional Designer: IT sheet draft. Owner: EULA, one class-server run | EULA (or declared "not covered") |
| **10** | **Art, audio, accessibility, playtests, measurement** | Producer (coordination), Artist | Final essential Phase 1 art (F-E) and the three pixel fonts with glyph test; AU-01 to AU-04, AU-11 with subtitles; accessibility pass; S1 (standard) and S2 (classroom, group size 4) sessions; effort log per content item; Stage 4 re-estimate and cut list applied | Glyph test; observation sheets (playtest-plan.md); the "What we want to learn" table filled from log and sheets | Technical Lead 2 to 4 (fixes) | Artist: fonts, textures, sounds. Game Designer: playtest report. Instructional Designer: survey and learning read-out. SME: sound-physics check. Producer: re-estimate | All earlier slices |

**Art and audio run alongside slices 1 to 9**, not after: the Artist delivers each slice's rows in the table above and the rest of the Stage 3 asset-list rows in slice 10. If the Artist's rows are late, the slice ships on straw-man assets (fidelity plan SM) and the gap is recorded, because "compact before wide" and the owner's playtest do not need final art to judge the loop.

**Total (Technical Lead only): about 40 to 66 sessions, unverified, from a 3-slice rate applied to bigger slices.** The honest reading: Stage 3 is roughly **5 to 8 times the size of Stage 2** in Technical Lead slices (Stage 2 had 8), and Stage 2 was thin by design. The budget row (590 h vs. 180 h for Stage 2, 3.3 times) is therefore the lower bound. If slices 5 and 6 come in at the top of their ranges, the Stage 3 Technical Lead row is short (section 7, R-69).

### 3.1 Per-slice reporting line (carried from Stage 2 lesson)

Every slice report adds: **elapsed working time, Gradle runs, number of tests added, number of owner-visible surprises.** The Producer keeps one running table (budget.md 6.4). The Stage 4 re-estimate in slice 10 uses this table, not the sessions guesses above.

## 4. Slice 0 detail (small, safe, first)

The queue items in BACKLOG and HANDOFF §0 that the Technical Lead already owes: apply the approved Monitor wording keys (BACKLOG #55 is Codex's), sun texture check (done by Codex per #50, #51), night and dusk frames and the dashed-horizon question (done in #49: dashed horizon still visible, needs an Artist or Technical Lead decision), send the 0.6 and 75 kPa shots to the Artist. Not a slice for the Technical Lead alone: **Codex** can finish the language keys and screenshot pack; the Technical Lead decides the horizon line in slice 1 when the dome gives a new horizon reference.

## 5. The first three slices to start next, and their inputs

Pick: **slice 1 (dome and sealing), slice 2 (life support and Dome stable), slice 3 (learning plumbing)**. Reasons: 1 and 2 give the owner something to survive in and put the pressure curve back on the designed timing (the Stage 2 caveat the owner will notice most); 3 is invisible to play but every later slice emits events into it, and the privacy and per-actor-ID guarantee is the most expensive thing to retrofit (R-13, R-20). Slice 3 can run in parallel with slice 2 only if a second code worker exists (not assumed; one Technical Lead means serial).

| Input | Needed by | Owner role | Ready? | Done when |
|---|---|---|---|---|
| **G1** Dome spec: footprint, interior layout for 5, wall height, where Ovens, oxygen maker, mast, airlock go; straw-man ok | Slice 1 | Game Designer, with Artist for the look | **No.** DESIGN says "sized for up to five"; no dimension spec found. Not a blocker for the Technical Lead to start a parametrised dome, but a layout is needed by the end of slice 1 | One page `docs/design/dome-spec.md` with block dimensions and a sketch |
| **G2** Life-support numbers: O2 and water use per player per sol, tank sizes, oxygen maker output, reclaimer rate, recipe costs; every number tagged invented or sourced | Slice 2 | Game Designer, SME to check | **No.** Balance sheet has pressure and power numbers only; life support is not tabled | Rows added to balance-sheet §2b; SME verdict on each (verified, invented, or unverified) |
| **S0** Sealing algorithm and caps | Slice 1 | Technical Lead | Yes (ARCHITECTURE 2.3) | n/a |
| **T1** Panel-dust re-cut against what slice 3 built | Slice 5 (but ask now) | Technical Lead | No | A number in the next report |
| **I1** Event schema and CSV columns confirmed against LEARNING §3.5 and ARCHITECTURE 8.3 | Slice 3 | Instructional Designer | Partly: the spec exists; the check against built Stage 2 events does not | Short report: list of events and columns, mismatches fixed |
| **G3, I2, S1** Landing step 6, Dome stable "On Earth" sentence and trigger, rose-sky label | Slice 4 | Game Designer, Instructional Designer, SME | Partly: texts in `docs/design/messages/phase-1.md`, handoffs open from HANDOFF §0 | Strings final with reading-level mark |
| **I3** F1 distractors reviewed | Slice 5 | Instructional Designer; external teachers | Reviewer not yet found | Reviewed or marked "pending external review" |

Start order: Technical Lead begins slice 1 as soon as G1 exists in any form (a rough sketch is enough); Game Designer writes G2 and SME checks it **while slice 1 is being built**, so slice 2 does not wait.

## 6. What Codex can take safely in parallel

Codex (peer worker, cheap for docs and tests) should not touch the Technical Lead's code areas while a slice is in flight: `mod/src/main/java/mars/**` except where named below, `pack/data/mars/machines|gates|sinks`, `PlanetState*`, `Monitor*`, `mars/client/**`.

| # | Task | File scope (Codex may write) | Done when | Depends on |
|---|---|---|---|---|
| CX-A | **Forecast and CR-F10 test spec**: a written test plan for the `resolve_source` JSON and the "answer never leaks before lock point" rule, with fixtures; no Java | `docs/tech/forecast-test-spec.md` (new) | Spec lists fixtures and expected failures; Technical Lead accepts or amends | Slice 5 not started |
| CX-B | **Content tests that need no new code paths**: a test that every string key referenced in `docs/design/messages/phase-1.md` exists in `en_us.json` and is under the word cap; reading-level check script | `mod/src/test/java/**` (new test files only), `tools/` script | Tests pass, one can be shown failing on a broken copy | Reading `en_us.json` read-only |
| CX-C | **Balance-sheet vs data tests for the numbers in slice 2** (written as soon as G2 exists) | new test files in `mod/src/test/java/` | Test fails on a changed JSON, passes on the real one | G2 |
| CX-D | **Screenshot scripts** for slices 1 to 3 (dome interior, dome breach, Monitor Dome tab) | `tools/screenshots/*.shots` | Scripts run with an absolute path, images inspected | The slice's build |
| CX-E | **Docs**: teacher quick-start update, IT sheet draft check, launch-guide refresh for each build, BACKLOG and HANDOFF upkeep | `docs/learning/teacher-quickstart-slice.md` is the Instructional Designer's file, so Codex proposes edits in a report; launch guide and BACKLOG are fine to edit | Launch guide matches the commands | Each slice |
| CX-F | **Observation sheets** for S1 and S2 (blank, from the "What we want to learn" table) | `docs/design/playtest-sheets/` (new) | Sheets exist, each question has a data source | None |
| CX-G | **Platform checklist form and record template** for the owner (one page) | `docs/production/platform-checklist.md` (new) | Owner can fill it in 5 minutes | None |
| CX-H | **Name check** (Thawlings, Cairn Dome, BELL, crew names) if a web host is allowed; otherwise a list of search terms | `docs/production/name-check.md` (new) | Each name has a result or "not searched" | Network access (R-36) |

Not for Codex: anything in `mod/src/main/java/mars/{hazards,learning,planets,machines,power,gates}`, balance numbers (Game Designer's), science claims (SME's), learning wording (Instructional Designer's), and the Decision Log (owner and Producer).

## 7. Risks for the plan (rows added to risk-register.md; see there for ownership)

| # | Risk | Check that catches it |
|---|---|---|
| R-69 | **Stage 3 is 5 to 8 times Stage 2 in code slices; the budget row (590 h) may be low.** | Per-slice reporting line; Producer re-checks after slices 2 and 5; cut list item ordering applies (budget 4.3). If slice 5 plus 6 exceed 14 sessions, ask the owner for scope (section 8 Q3) |
| R-70 | **Inputs not ready (dome spec G1, life-support numbers G2).** Slices wait on the Game Designer. | Section 5 table; Producer asks both before slice 1 starts |
| R-71 | **One Technical Lead is a serial bottleneck**; Stage 3 has no parallel code worker. | Codex lanes in section 6 stay out of code; consider letting Codex take slice 3 plumbing under a file claim if the Technical Lead is slice-bound (owner decision, section 8 Q2) |
| R-72 | **Learning plumbing late means no data from owner playtests.** | Slice 3 before content slices; if it slips, slices 4 to 7 add events later but S1 data is lost |
| R-73 | **Slice 2 makes Dome stable arrive at about 3 h instead of the stand-in's 1 h.** The owner may feel the game slowed. | Say so in the slice 2 owner message; label timing "prototype timing" until measured |

## 8. Questions for the owner (few, with a recommendation)

1. **Build order.** Approve slices 1 to 3 as next? **Recommend yes.** If you would rather see learning-flavoured content first, swap slice 3 and 4; cost: the Landing would have no action log under it.
2. **Second code worker.** May Codex take slice 3 (learning plumbing) under a file claim, so it runs beside the Technical Lead's slices 1 and 2? **Recommend yes, if you accept two workers in `mod/` at once with strict file scopes (`mars/learning/**` only).** If not, slice 3 waits and Stage 3 takes longer.
3. **If slices 5 and 6 run long**, which should go first to the cut list? **Recommend** trimming machine wear and maintenance parts to the single filter anchor ("filters every 2 sols") and moving the rest to Stage 4, before touching storms or the Phase 1 dust-off (budget cut item 8 keeps the dust-off). Storms carry F1 (C1, B3, Primary); seal-breach carries C2. Trade-off: less "upkeep" feel in Phase 1, which the Game Designer should confirm.

## 9. Ways this plan could be wrong

- The "sessions" ratio comes from three thin slices. Content and art inputs may dominate, and then Technical Lead sessions are the wrong unit.
- I did not re-read the code in `mars.hazards.dust` or `mars.dimension.ice`; slice 5 and 7 could be smaller if those hold more than placeholders.
- I did not verify the current unit-test and boot-check counts beyond what the reports state (174 and 82 at the last run).
- Slice dependencies are my reading of ROADMAP and ARCHITECTURE. The Technical Lead may re-order (for example, storms before the learning plumbing) with a reason.
- External reviewers and the owner's platform checks have no date. Both can move Stage 3 content after the fact.
