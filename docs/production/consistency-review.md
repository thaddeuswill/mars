# Consistency Review

**Reviewer:** Producer · **Date:** 2026-11-26 · **Stage:** 1 (Pre-production)

**Scope:** `README.md`, `DESIGN.md`, `LEARNING.md`, `ARCHITECTURE.md`, `ROADMAP.md`, and `.claude/agents/*.md`. Not reviewed: `docs/design/` and `docs/science/`, which other roles are writing right now.

**Method:** I read every document in full and cross-checked numbers, decisions, goal IDs, roadmap coverage, and architecture support for each feature. I checked every finding against the five design pillars. This was a document review only. Nothing was played or built.

## Severity levels

| Level | Meaning |
|---|---|
| **Blocker** | Stops a roadmap stage, or makes a settled decision impossible to deliver. Fix before Stage 1 exits. |
| **Major** | Two documents disagree on something we will build or measure. If not fixed, it causes rework. Fix before the work it affects starts. |
| **Minor** | Stale wording, missing cross-reference, or unclear text. Fix at the owner's next edit. |

## Summary

| Severity | Count | Fixed by Producer in ROADMAP.md | Handed off |
|---|---|---|---|
| Blocker | 2 | 0 (ROADMAP wording aligned for C-01; design fix still needed) | 2 |
| Major | 19 | 1 (M-18) | 18 |
| Minor | 20 | 0 | 20 |

---

## Blockers

### C-01 · Phase 1 has no way to reach the Phase 2 gate
- **Where:** DESIGN.md §2 Phase Gates and §1 Planetary Index; DESIGN.md §5 Milestones (item 1); ROADMAP.md Stage 2 and Stage 3.
- **Conflict:** "Phase 2 begins" needs pressure ≥ 5 kPa, but the atmospheric processor is *unlocked by* that same gate. The only other pressure source listed is "regolith outgassing", and no machine, block, or event for it is designed. DESIGN Milestone 1 (vertical slice) and ROADMAP Stage 2 both use an atmospheric processor to move pressure in Phase 1. ROADMAP Stage 3 also said "Dome stable" unlocks the move to Phase 2, but DESIGN says Dome stable unlocks airlocks, EVA suit tier 2, and the first outpost, and that Phase 2 starts at 5 kPa.
- **Pillars:** works against *The planet is the quest log*: the first gate can't be reached through world state.
- **Owner:** Lead Game Designer.
- **Fix:** Design the Phase 1 pressure source (for example, a basic regolith outgasser that unlocks at Dome stable), or move a tier-1 atmospheric processor to the Dome stable gate. Then update the gate table and Milestone 1 to match. *Producer already aligned the ROADMAP.md Stage 3 wording with DESIGN.md and added the missing item.*

### C-02 · A class of students vs. the "1–6 players" audience
- **Where:** DESIGN.md Decisions (Audience: roughly 1–6 players); LEARNING.md §0.4 (teacher runs "a small server"), §5 (classroom mode), §5.3 (class overview); ARCHITECTURE.md §2.4 (fauna by active emitters), §2.2 (threshold multiplier).
- **Conflict:** A typical class is far larger than 6 players, but no document says how a class plays: one shared world, several small worlds, or teams. The answer changes the balance (fauna scales with machine count, so 25 builders give a fauna explosion), server hardware, the starting dome (25 players in one dome), and the learning measures (see M-14).
- **Pillars:** works against *Compact before wide* (a crowded dome forces wide building) and *Pressure, not grind*.
- **Owner:** Lead Instructional Designer and Lead Game Designer, with the Technical Lead for hosting. The user decides.
- **Fix:** Decide the classroom group model. **Recommendation:** groups of 3–5 students share one planet. Either run several small worlds, or (later) one server with one Mars instance per group. ARCHITECTURE.md §5 "Planet as a parameter" already keys the Index by dimension, so this is feasible. Record the decision in DESIGN.md and LEARNING.md §5.

---

## Major

### M-01 · Classroom play periods: 16 vs. 14
- **Where:** LEARNING.md §5.1 ("about 16 class periods of ~40 min play") vs. §5.2 unit plan (play periods 2–4, 6–11, 13–17 = **14**).
- **Conflict:** 14 × 40 min ≈ 9.3 h, below the settled ~11–12 h. 16 × 40 min ≈ 10.7 h. Neither matches exactly.
- **Owner:** Lead Instructional Designer.
- **Fix:** Make the table and unit plan agree. Either the unit plan grows to about 22–23 periods with 17 play periods, or the classroom target becomes about 9–10 h. The second option needs a user decision because ~11–12 h is a settled number.

### M-02 · The classroom "gate threshold multiplier ~0.3" doesn't work on every gate
- **Where:** LEARNING.md §5.1; DESIGN.md Multiplayer Scaling (gate multiplier, default 1.0); ARCHITECTURE.md §2.2 and §5 (Config presets).
- **Conflict:** (a) Multiplying a temperature threshold doesn't scale difficulty: −20 °C × 0.3 = −6 °C is *harder*, and 0 °C × 0.3 stays 0 °C. (b) At 0.3, Phase 2 would start at 1.5 kPa and Phase 3 at 9 kPa. The numbers students see would no longer match the science taught in the journal. (c) The same multiplier serves multiplayer scaling, so classroom and multiplayer settings collide.
- **Pillars:** puts at risk *Every change is visible*: the visible thresholds become arbitrary.
- **Owner:** Lead Game Designer (rule), Technical Lead (config), SME (science check).
- **Fix:** Keep the thresholds at their real values and compress classroom pacing through **rates** (machine output multiplier) and hold times. Alternatively, apply any multiplier to the *distance from the start value*, never to the raw value. Use separate config keys for classroom pacing and multiplayer scaling.

### M-03 · Breathable is a latched gate, but helmet-off is a live check
- **Where:** DESIGN.md §1 ("the one exception is the helmet-off state: it is a live check") and §2 (Breathable gate "Helmet optional"); ARCHITECTURE.md §2.2 ("once reached, a gate is … never re-evaluated").
- **Conflict:** ARCHITECTURE has no concept of a live condition, so as written the helmet-off state would latch forever.
- **Owner:** Technical Lead.
- **Fix:** Add "live conditions" (evaluated each step, never latched) beside latched gates in ARCHITECTURE §2.2 and the `Gates` API. Helmet-off is the first one.

### M-04 · Breathable and equilibrium values may teach a misconception (**unverified**: SME to confirm)
- **Where:** DESIGN.md §1 (O2 19–23 %, pressure ≥ 60 kPa, CO2 < 1 %) and §2 (Breathable: "O2 in band", with no pressure or CO2 condition).
- **Conflict:** Whether air is breathable depends on oxygen *partial pressure* (fraction × total pressure), not the fraction alone. Breathable could be reached at 30 kPa, the Phase 3 start. At 21 % of 30 kPa, that is far below Earth sea level (about 21 kPa O2 at 101 kPa). The gate also ignores CO2, which is toxic at high levels. Also, 60 kPa with < 1 % CO2 and ~21 % O2 leaves ~78 % of the gas unaccounted for (no nitrogen or buffer gas parameter). The game would teach "oxygen percentage = breathable", the kind of misconception LEARNING.md §0.2 is meant to fix.
- **Pillars:** learning outcome A1, A5; risks the honesty in "Science accuracy notes".
- **Owner:** SME (verify), then Lead Game Designer (gate and parameters).
- **Fix:** SME reviews the breathable condition. Likely result: base Breathable on O2 partial pressure plus a CO2 ceiling, and either add a buffer gas parameter or add a journal accuracy note that explains the simplification.

### M-05 · World pause isn't designed in ARCHITECTURE
- **Where:** LEARNING.md §5.1 (world pauses between classes) and §5.3 (pause/resume freezes simulation, hazards, fauna); ARCHITECTURE.md §1.4 (lists "Pause world" only); ROADMAP.md Stage 4.
- **Conflict:** No module or algorithm owns pause. Open points: the index step, duration-gate counters, machine wear, storms, mob spawning, random block ticks, the day cycle, and third-party machines (Mekanism, Create) that keep producing while paused. "In-game days" for hold gates also needs a definition that survives a pause, for example simulation time, not the daylight cycle.
- **Owner:** Technical Lead.
- **Fix:** Add a "Session control" section to ARCHITECTURE: a single paused flag in `core`; what each module does when paused; how third-party mods are frozen (or the simpler rule "pause = stop the server, and counters use simulation time"); and the definition of an in-game day.

### M-06 · Rollback would delete the assessment data
- **Where:** LEARNING.md §5.3 (rollback to session start); ARCHITECTURE.md §1.5 (action log is "a local file in the world folder").
- **Conflict:** Restoring a world backup also restores the old log file, so the log of the rolled-back session is lost. That evidence is exactly what a teacher needs after griefing. ARCHITECTURE also has no backup or snapshot mechanism.
- **Owner:** Technical Lead.
- **Fix:** Store the action log outside the restorable world data (or exclude it from rollback), add an automatic session-start backup, and log the rollback itself as an event.

### M-07 · "Anonymous" logging uses real player UUIDs, and usernames can be exported
- **Where:** ARCHITECTURE.md §1.3 (`LearningEvent(UUID player, …)`), §1.5 ("anonymous player IDs"); LEARNING.md §3.1 ("no personal data"), §5.4 (local ID→username mapping; CSV uses IDs "unless the teacher chooses otherwise"); settled decision "Nothing personal leaves the server's computer".
- **Conflict:** A Minecraft UUID is a permanent account ID, not an anonymous one. Keeping a mapping makes the data *pseudonymous*, not anonymous. A CSV with usernames leaves the server's computer as soon as a teacher emails it.
- **Owner:** Technical Lead (ID scheme), Lead Instructional Designer (wording, export options). The user decides on the username option.
- **Fix:** Replace the UUID with a per-world random ID before anything is logged. Keep the mapping in a separate local file that is never exported. Change "anonymous" to "pseudonymous (anonymous outside the server)" where it applies. **Recommendation:** drop the username export option.

### M-08 · CSV export, NGSS codes in the export, and the ID mapping have no architecture
- **Where:** LEARNING.md §5.3 (export log to CSV), §5.4 (ID mapping), §6.4 ("teacher export shows the matching NGSS codes"); ROADMAP.md Stage 1 ("CSV export for teachers"); ARCHITECTURE.md §1.5 (JSON Lines only).
- **Owner:** Technical Lead, with the Lead Instructional Designer for the column list.
- **Fix:** Add the CSV export (columns, goal-ID→NGSS lookup from data, ID-only), the ID-mapping file, and their locations to ARCHITECTURE §1.5. The ID writes the "done when" column list.

### M-09 · Grade band has no architecture
- **Where:** LEARNING.md §0.1, §5.1 (per player in standard mode, per server in classroom mode), §5.3, §6.4; ARCHITECTURE.md §1.4 (teacher can "set grade band", nothing else).
- **Conflict:** Nothing says where the grade band is stored, or how journal entries, prompts, and quiz items select their middle or high school variant.
- **Owner:** Technical Lead.
- **Fix:** Add grade band to the data formats (per-player value, server override), and a band key on journal, prompt, and quiz JSON.

### M-10 · Teacher "set phase" and debug "set value" conflict with the contribution-only API
- **Where:** LEARNING.md §5.3 (set phase); ROADMAP.md Stage 2 (debug commands to set every index value); ARCHITECTURE.md §1.2 (teacher commands write to the Index), §1.3 ("never write values directly"), §4.2 (one-way data flow).
- **Owner:** Technical Lead.
- **Fix:** Add an explicit admin API (`setValue`, `latchGate`) that only the `admin` module can call, is logged, and posts the normal events.

### M-11 · Colony Record, prediction answers, and teacher overview have no storage or UI module
- **Where:** LEARNING.md §4.3, §5.3; ARCHITECTURE.md §1.2 (`learning` module), §1.5 (data formats table).
- **Conflict:** The data formats table lists no storage for Colony Records, prediction answers, per-phase accuracy, or grade band. The `client` module lists no journal, Colony Record, or teacher overview screens.
- **Owner:** Technical Lead.
- **Fix:** Add them to §1.5 and to the `client` module row.

### M-12 · Journal: FTB Quests or custom?
- **Where:** DESIGN.md §4 (Optional guidance: FTB Quests as a lore journal); ARCHITECTURE.md §1.2 (`learning` module owns journal, prompts, Colony Record).
- **Conflict:** The two documents imply different builds. A custom journal with prediction prompts, two grade bands, and a Colony Record is a large piece of work. FTB Quests is a checklist UI, and its redistribution terms are **unverified**.
- **Pillars:** a quest checklist works against *The planet is the quest log*.
- **Owner:** Technical Lead and Lead Game Designer.
- **Fix:** Decide in Stage 1. **Recommendation:** custom journal in the `learning` module. Remove FTB Quests from DESIGN §4, or mark it rejected.

### M-13 · KubeJS described as holding "global state" and tuning
- **Where:** DESIGN.md §4 ("Scripting the Planetary Index | KubeJS | Global state, recipe gating, event hooks") and the paragraph after it ("KubeJS stays the place for recipes, gate unlocks, and tuning"); settled decision "KubeJS for recipes; datapack JSON for content and tuning"; ARCHITECTURE.md §1.1, §1.5 (Index and gates live in Java and datapack).
- **Owner:** Lead Game Designer.
- **Fix:** Change the row to "Recipes, recipe gating, third-party integration". Say that gates and tuning live in datapack JSON.

### M-14 · Individual measures vs. shared global progress
- **Where:** LEARNING.md §3 Success criteria ("70 % of learners reach the Phase 3 gate"), §0.6 (A5 "holds O2 in band", C1 "survives the first dust storm"), §4.4 (beacon as "final proof"); settled decision "Global progress".
- **Conflict:** On a shared world, gates, the dome, and the O2 band belong to the whole group. One strong player can reach them for everyone. These criteria can't separate learners.
- **Owner:** Lead Instructional Designer.
- **Fix:** Reword group outcomes as group measures, and base individual measures on per-player evidence: predictions, "Explain it" checks, own contributions, and own failure recoveries. Depends on C-02.

### M-15 · Objectives table is missing A4 and B4
- **Where:** LEARNING.md §0.6 has no rows for A4 (radiation) or B4 (trade-offs). The "ISD strengths" table claims "every objective has a measure in 0.6".
- **Owner:** Lead Instructional Designer.
- **Fix:** Add behavior, condition, and criterion for A4 and B4. Split D1 and D2 into separate rows, or say why they share one.

### M-16 · Long hold times risk idle play
- **Where:** DESIGN.md §2 (Equilibrium 14 in-game days, Breathable 7). At 20 real minutes per Minecraft day, that is about 4.7 h and 2.3 h, so the equilibrium hold alone is about a third of Phase 3's ~14 h.
- **Pillars:** works against *Pressure, not grind*, and against DESIGN's own goal of "active rather than idle".
- **Owner:** Lead Game Designer.
- **Fix:** In the balance sheet, show what the player *does* during holds (drift events, storms, fauna pushes that must be answered), or shorten the holds. Test in the prototype.

### M-17 · Visible feedback covers only pressure and O2
- **Where:** ARCHITECTURE.md §2.5 (sky and fog keyed to pressure and O2); DESIGN.md §1 (six parameters); LEARNING.md §4.1.
- **Conflict:** CO2 falling, temperature before the Thaw, and biomass have no defined visible response.
- **Pillars:** works against *Every change is visible*.
- **Owner:** Lead Artist (visual keyframes per parameter), Technical Lead (inputs to the client).
- **Fix:** Artist specifies a visible cue for each of the six parameters. TL widens §2.5 to take any parameter as an input.

### M-18 · Roadmap missing work described elsewhere · **Fixed by Producer**
- **Where:** ROADMAP.md vs. DESIGN.md §2–3 and the role files.
- **Was missing:** airlocks and EVA suit tier 2 in the right stage (Dome stable is a Phase 1 gate); a Phase 1 pressure source; surface water and algae vats; orbital mirrors, ice-comet impacts, regolith outgassing; carbon scrubbers; story bible; UI specs; science reference; art direction; QA plan; player guide; accessibility pass; "Explain it" check; classroom deployment (launching, joining, school IT); playtester recruitment and consent for minors; LMS integration needs; educator package at release.
- **Fix:** Added to ROADMAP.md in this review. Scope impact is in the risk register (R-01).

### M-19 · The vertical slice has no learning layer
- **Where:** ROADMAP.md Stage 3 (no journal, prompts, failure reports, Colony Record, or action log; all in Stage 4); LEARNING.md ADDIE table (Development in Stages 2–4, "learning design isn't bolted on at the end"); ROADMAP Stage 3 goal ("near-final quality, as the template").
- **Conflict:** The template for every phase doesn't include the learning half of the game, so the Stage 3 playtest can't check any learning goal (A1, C1, C3).
- **Owner:** Producer (scope), Lead Instructional Designer.
- **Fix:** **Decision needed.** Recommendation: move a thin Phase 1 learning layer into Stage 3 (one gate journal entry, one prediction prompt, one failure report, a Colony Record skeleton, the action log), and remove those items from Stage 4.

---

## Minor

| ID | Where | Issue | Owner | Proposed fix |
|---|---|---|---|---|
| m-01 | LEARNING §0.6 (A5), §4.4 | Uses standard hold times (7 and 14 days) only; classroom is 2 and 3. | Instr. Designer | Write "for the Breathable/Equilibrium hold time of the active preset". |
| m-02 | LEARNING §5.1 | Dome stable hold (3 days) is missing from the classroom table. | Instr. Designer + Game Designer | Add a row (keep 3 or shorten). |
| m-03 | LEARNING §0.6, §3 criteria, §4.4 | B3 criterion stated three ways: "+20 points", "rises", "clearly higher". | Instr. Designer | Use the §0.6 criterion everywhere. |
| m-04 | LEARNING §1 B3 vs. alignment table | B3 "taught by" is Monitor trends in one place, prediction prompts in the other. | Instr. Designer | List both in both places. |
| m-05 | LEARNING alignment "Gap found" | Says A4 lacks a prediction prompt, but every A4 column in the table is filled. | Instr. Designer | Either add a "prompt / Colony Record trigger" column or reword the gap. |
| m-06 | LEARNING §6.1 | C3 and D1 have no NGSS row; all codes still **unverified** (tracked in ROADMAP Stage 1). | Instr. Designer + SME | Add rows or mark "no PE, practice only". |
| m-07 | LEARNING §0 intro | Says items are marked *(assumed)*, but none are. | Instr. Designer | Mark the real assumptions (e.g. 45–60 min periods, hardware). |
| m-08 | LEARNING §2 (educator guide) | "Pacing for 40 hours of play across class sessions" is stale; classroom mode is ~11–12 h. | Instr. Designer | Point to §5 classroom mode. |
| m-09 | LEARNING §5.1 | "Resource grind: Full recipes" names grind as a standard feature. | Instr. Designer | Rename row to "Recipe cost: standard / cheaper preset". |
| m-10 | LEARNING Open Questions | "How visible is the learning?" has an assumed answer that isn't in the §5.1 table. | Instr. Designer | Add a "Journal and prompt depth" row to §5.1, or keep open and remove the assumption. |
| m-11 | LEARNING §0.4 | Classroom constraints omit Minecraft Java accounts and licensing. | Instr. Designer + TL | Add it (see risk R-09). |
| m-12 | LEARNING §0.6 C1 | "Within 2 attempts" is undefined in a game with no retry structure. | Instr. Designer | Define an attempt (e.g. a dome failure event). |
| m-13 | DESIGN Multiplayer Scaling | Text says "options to evaluate in the prototype", then "Chosen". The "+25 % per player" example suggests player-count scaling, but the chosen multiplier is static. | Game Designer | Mark the list as "options considered" and state that the multiplier is static. |
| m-14 | DESIGN §"Implementation Plan (proposal)" | Heading says proposal, but the section holds settled decisions (gates lock in). | Game Designer | Rename "Implementation Plan"; mark what is still a proposal. |
| m-15 | DESIGN §5 Milestones vs. ROADMAP Stages 2–3 | Order differs (DESIGN: slice before Index; ROADMAP: Index prototype first). Two plans for the same thing. | Game Designer | Replace DESIGN §5 with a link to ROADMAP.md. |
| m-16 | DESIGN §1 Biomass | Equilibrium value is "threshold", with no number. | Game Designer | Set it in the balance sheet. |
| m-17 | DESIGN §2 Thaw; LEARNING §0.2 | Gate named "Thaw" fires at −20 °C, while the game teaches that melting depends on temperature *and* pressure. Which threshold actually turns ice blocks into water is unclear. | Game Designer + SME | State the ice→water rule (temperature and pressure); consider renaming the gate. |
| m-18 | LEARNING §4.1 | "Sky shifts from tan toward pink" as pressure rises is **unverified** science. | SME + Lead Artist | SME checks; Artist sets keyframes. |
| m-19 | ARCHITECTURE §3, §1.2 hazards | States "Java 21 (required by NeoForge 1.21.1)" and a custom sealing system as fact, though both version and oxygen-mod decisions are still open in ROADMAP Stage 1. Index "keyed by dimension" vs. "one per world" is compatible but unstated. | Technical Lead | Mark as "pending Stage 1"; add one line on Index-per-dimension. |
| m-20 | README.md; role files | README has no owner, doesn't link `docs/`, and LMS integration appears only in the TL and ID role files (not in LEARNING or ARCHITECTURE). | Producer (README, user to confirm), TL + ID (LMS) | Producer takes README; ID adds an "LMS needs" note (recommend: post-release, opt-in). |

---

## Pillar check

| Pillar | Findings that work against it | Note |
|---|---|---|
| The planet is the quest log | C-01, M-12 | The Colony Record is a goal checklist. That is fine as long as it never gates progress. Keep it that way. Dome stable is a machine-state condition, not an Index threshold; acceptable, but say so in DESIGN §2. |
| Every change is visible | M-02, M-17, m-18 | — |
| Pressure, not grind | C-02, M-16, m-09 | Mekanism/Create recipe depth for 12-year-olds new to tech mods is a grind risk (R-04). |
| Compact before wide | C-02 | Classroom group size decides whether this pillar survives in class. |
| Your progress provokes the planet | C-02 | Fauna-by-machines works for 1–6 players but explodes with a full class. |

## Checked and consistent

- Pacing split 8 / 18 / 14 h = 40 h: DESIGN, ROADMAP, Game Designer role file agree.
- Classroom ~11–12 h and the ~20-period unit plan: DESIGN, LEARNING, ROADMAP, all role files agree (apart from M-01).
- Classroom phase split (3 / 6 / 5 play periods) has the same proportions as the standard split.
- Settled decisions (global progress, gates lock in, custom dimension, ages 12–18, NGSS, ISD/ADDIE, local logging) are the same in every role file.
- Goal IDs A1–D2 are used the same way everywhere except the gaps in M-15 and m-04.
- Gate lock-in is the same in DESIGN §1, ARCHITECTURE §2.2, and LEARNING §4.5 (apart from M-03).
