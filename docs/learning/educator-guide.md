# Educator Guide: Mars Terraforming (draft)

**Owner:** Lead Instructional Designer · **Status:** Draft 1, 2026-11-26 · **Audience:** teachers and club leaders · **Depends on:** [LEARNING.md](../../LEARNING.md), [ARCHITECTURE.md](../../ARCHITECTURE.md) §6–8, [docs/tech/school-deployment.md](../tech/school-deployment.md), [rubrics.md](rubrics.md), [prompts-and-reflections.md](prompts-and-reflections.md)

> **Requires Minecraft Java Edition. It can't run in Minecraft Education.**
> Minecraft Education is a separate product built on Bedrock and doesn't support community mods; Education licenses don't work for Java Edition (`docs/tech/school-deployment.md`, findings 1–3).

**Team note (remove before publishing).** This project is a design exercise; no real class will use this guide. It is written as if one would. Before publishing: the NGSS codes need a word-for-word check on nextgenscience.org; the Earth data sources for debrief 2 need the SME to open and cite them; the server setup guide (Technical Lead) must exist; and a real teacher should review it (risk R-29). **External review (owner decision, round 3):** before Stage 3 the team seeks three outside reviewers: a planetary scientist, a climate modeler, and science teachers. The teachers review the transfer-task answer key, the forecast distractors and explanations, the report rubrics, and this guide; their requests are in [teacher-review-pack.md](teacher-review-pack.md). Until they report, those items are marked "pending external review". Quick version for the Stage 3 slice: [teacher-quickstart-slice.md](teacher-quickstart-slice.md).

---

## 1. About the unit

Students run a colony on a hostile Mars and change the planet, in three phases, until the air is breathable and the colony can call the ships. The game doesn't stop to lecture: the planet itself responds to what students build, and the science is in that response.

**Learning objectives** (codes checked word for word on nextgenscience.org, 2026-11-26):

| ID | By the end, learners can… | NGSS (MS) | NGSS (HS) | Priority |
|---|---|---|---|---|
| A1 | Compare Mars and Earth on pressure, temperature, atmosphere, and radiation, using data | MS-ESS1-3 | — | Secondary |
| A2 | Explain why liquid water can't stay on Mars's surface and what must change | MS-PS1-4 | extends MS-PS1-4 | **Primary** |
| A3 | Explain how greenhouse gases warm a planet | MS-ESS3-5 | HS-ESS2-4 | **Primary** |
| A4 | Explain why Mars's surface gets more radiation, and choose a protection | — | HS-PS4-4 | Lower |
| A5 | Describe the CO2/O2 exchange and why balance matters | MS-LS1-6 | HS-LS2-5, HS-ESS2-6 | **Primary** |
| B1 | Identify cause-and-effect links between planet variables | MS-LS2-4 | HS-ESS2-2 | **Primary** |
| B2 | Recognize reinforcing and balancing feedback loops | — | HS-ESS2-2 | **Primary** |
| B3 | Predict the effect of a change before making it | (practice) | HS-ESS3-6 | Secondary |
| B4 | Manage trade-offs between competing goals and justify the choice | MS-ETS1-2 | HS-ETS1-3, HS-LS2-6 | **Primary** |
| B5 | Recognize unintended consequences | MS-ETS1-2 (related: MS-ESS3-3, with an Earth example) | HS-ETS1-3 | Secondary |
| C1 | Plan for limited resources, life-critical needs first | MS-ETS1-1, -2 | HS-ETS1-1, -2 | Secondary |
| C2 | Diagnose and fix a failing system so it stays fixed | MS-ETS1-4 (-3) | HS-ETS1-2, -4 | Secondary |
| C3 | Use data to make decisions | (practice: analyzing and interpreting data) | (same) | Secondary |
| D1 | Argue a position on terraforming that weighs life already there | (practice: argument from evidence) | (same) | Lower |
| D2 | Connect a game mechanism to Earth's climate or ecosystems | MS-ESS3-5 | HS-ESS2-4, HS-ESS3-6 | **Primary** |

Full objectives with conditions and criteria: LEARNING.md §0.6. Priority decides what gets the most attention if time is short; every objective stays on the post-test.

**Where the unit fits** (requirements analysis):

| Question | Answer |
|---|---|
| Before it | Students should know states of matter and basic photosynthesis (plants use CO2 and light, give off O2). No Minecraft experience required, but it helps |
| After it | Natural next units: Earth's climate and human impacts; ecosystems and the carbon cycle; engineering design. The transfer task (period 22) is the bridge |
| Role in the curriculum | An **anchoring experience**: the phenomena students explain in class come from their own planet. It replaces some lab and lecture time. It doesn't replace teaching Earth's climate data, which happens in debriefs |
| LMS | **Not needed.** No LMS link at launch (an opt-in one may come after release). You use CSV exports (§8) and paper or spreadsheet rubrics |

**Language note.** In this guide we say "learner" and "objective". In the game, students are *players* and *Keepers*; the game never says "lesson", "quiz", or "test". Please keep that in class talk during play ("your colony", "your forecast"), and save "objectives" for debriefs.

## 2. Setup requirements

### 2.1 What a school needs

| Need | Detail |
|---|---|
| **Minecraft Java Edition** account for each student | A Microsoft account with Java Edition. Child accounts need **multiplayer enabled** by the account's family organizer, or students can't join (they see greyed-out Multiplayer buttons). Allow time; the setting can take a while to apply. Minecraft Education licenses don't carry over |
| A launcher that installs modpacks | A third-party launcher that reads the pack file, or a pre-built folder you copy to each PC. Students shouldn't install mod loaders by hand in class |
| Install rights | School IT must allow the launcher and Java on lab PCs |
| **One server machine** | Runs the whole class (below). Needs Java 21 and enough memory; numbers to be measured in testing (Technical Lead) |
| Local network | Students reach the server on the school network. The usual Minecraft server port is TCP 25565 (**unverified** for your setup; check with IT). No internet traffic is needed during play once accounts have signed in |
| Student PCs | Recent Minecraft Java hardware requirements went up; whether they apply to this pack's Minecraft version (1.21.1) is **unverified**. Test one lab PC before the unit |
| Time | Target: setup in under 30 minutes once the PCs and accounts are ready |

A one-page version of this table for IT staff is planned (Technical Lead's server setup guide).

### 2.2 How the class is hosted

**One server, one Mars per group** (decided). You run **one server program** on one machine. Each group of 3–5 students gets **its own Mars** (a separate planet in the same server), with its own atmosphere, story, and creatures. All planets start from the same map, so groups can compare in debriefs. You start, pause, back up, and export the whole class from one place.

| Option | Use when |
|---|---|
| **One server, one Mars per group** (standard) | Almost always |
| One server program per group (fallback) | Spare machines, or one group's problems must not affect others |
| A student opens their own game to the network | **Not supported for classes**: the learning records would end up on student PCs, outside your control |

Teacher tools (all are buttons in the class overview; no typing needed): pause and resume, backup, rollback (per planet), assign students to planets, set phase, set grade band, raise or lower a planet's pace, **wrap up** (stops new crises and prompts in the last minutes), export, and forget (delete the ID list).

### 2.3 Before the first class

1. Install and start the server, then switch it to classroom pace (§2.4). It starts **paused**.
2. Open one planet per group; set each planet's **group size** (3–5; classroom pace refuses 1 or 2) and **grade band** (middle or high school). Leave **guidance** on **gentle** (the classroom default: the game points at problems and explains what's locked). You may choose **standard** for a confident group; it gives fewer hints but still names every cause. Guidance never changes the science or the numbers.
   **Subtitles are on by default** in the classroom pack (useful in noisy rooms and for students who can't use sound). Every important sound also has a visual cue.
3. Assign students to planets (or give out join codes).
4. Print the **player ID slips** (one per student) from the class overview. The slips carry **IDs only**; the game never prints names (it doesn't store them anywhere it could print from). Hand them out yourself, writing a name on a slip by hand if that helps you, and keep them in the classroom. Students write their ID, never their name, on pre/post tests.
5. Print the pre-test, the paper reflection worksheet (§6) as a backup, and the Mars/Earth data table for debrief 1.
6. Test one student PC joining the server.

### 2.4 Choosing the pace (classroom mode)

**Status: the command is built and passed automated server tests. It has not been typed in a real client or on a dedicated server, so its chat text is untested.** The class overview buttons in §2.2 are planned; until they exist, you set the pace with a command. You need operator rights (permission level 2).

**Switch the world to classroom pace** (once, before the first class):

| Command | What it does |
|---|---|
| `/mars pace` | Shows the current pace: standard or classroom, the rate multiplier, the group size, and whether the creative gate bypass is on. |
| `/mars pace classroom` | Classroom pace with the default group size (4). |
| `/mars pace classroom 3` (or 4, 5) | Classroom pace for groups of that size. |
| `/mars pace standard` | Back to home pace (the default for a new world). |

The choice is saved with the world. It applies to the whole world, so **every planet on the server shares one group size**. If your groups differ (say 3, 4 and 5), pick the most common size.

**What changes in classroom pace**

| Change | Plain meaning |
|---|---|
| Rates x4 (with a group-size adjustment) | Machines move the planet about four times faster than in standard pace. A bigger group sets a smaller adjustment, because more students build more machines. A group of 4 ends up near the speed of one solo player, while building goes faster. |
| Shorter holds | A hold is how long a condition must stay true before a gate counts. "Dome stable" is 1 sol instead of 3. |
| Creative gate bypass is off | In standard pace a creative-mode player can place blocks that survival players have not unlocked yet. In classroom pace they can't. This keeps a teacher's test account from skipping what students must earn. |
| Guidance is teacher-only | Only the operator can change a student's guidance level (`/mars guidance <level> <player>`). The **expert** level is refused in classroom worlds. |

**What does not change.** Physical thresholds are the same in every pace and every group size: 5 kPa for Phase 2, the triple point, 0 °C, the breathable-air condition, and the equilibrium band. Faster pace makes the planet reach them sooner; it never moves them. A group that finishes in classroom pace has met the same science targets as a home player. The planet also keeps its numbers, goals, machines, and history when you switch.

**Check the mode.** Type `/mars pace`. The Monitor will also show a "Classroom pace" row once that row is built (**pending: not yet in the game**). Until then, the Monitor does not tell students or you which pace is on.

**If you forget.** The world runs in standard pace, and nothing on screen warns you. Run `/mars pace classroom` as soon as you notice. It works at any time and the planet keeps its numbers. From that moment, rates and holds follow classroom pace. Time already played at the slower pace is not made up, so a group that began in standard pace is behind the unit plan: use a higher pace for that planet, or shorten debriefs (§11). You can also switch back with `/mars pace standard` if you chose wrong.

**Pre-class checklist** (about 5 minutes; all untested in a real class)

1. Server started; a test join works (§2.3, step 6).
2. `/mars pace classroom <group size>` run, and `/mars pace` shows classroom, the right group size, and creative bypass off.
3. Your own test account is not in creative mode. (In classroom pace it can't skip gates, but a creative account still isn't a fair test of a student's experience.)
4. Each student's guidance is set the way you want. Gentle is the classroom suggestion (§2.3). Remember that students can't change it themselves.
5. Planets paused until class starts (§2.3, step 1); resume when students are seated.
6. Player ID slips, pre-test, and paper worksheet printed (§2.3, steps 4 and 5).
7. After the first 10 minutes of play, ask a group what the Monitor says about its goal. If the estimate looks far too slow for a 40-minute period, re-check step 2.

## 3. Groups, colony posts, and fairness

- **Groups of 3–5** share one planet. Mix experienced and new Minecraft players.
- **Colony posts** are suggested roles: Life Support, Atmosphere, Survey, Biology, Data. Posts **rotate each phase** (the game suggests them at each phase start). Make sure every student has at least one turn on the Data post or at the Monitor before a big build.
- **Shift Board credit.** When a student repairs a failure, the repair counts as *their* evidence, even if a teammate claimed it on the Shift Board. Claiming is still useful for coordinating.
- **Personal pieces stay personal.** Each student answers their own forecasts (hidden from teammates until resolved), their own "What caused it?" checks, and their own Keeper's Reports. The planet's progress belongs to the group.
- **Grade only individual evidence** (§7). A strong player can reach every gate for the group; that doesn't mean every student learned it.
- **Absent students** find a "Since last shift" summary and any unsent reports waiting. The planet's speed doesn't change when someone is absent.

## 4. The 23-period unit plan (17 play periods)

Periods are 45–60 minutes with about 40 minutes of play *(assumed)*. Phase split 4 / 7 / 6 play periods, from the design's classroom time model. **Groups move at different speeds**: debriefs use what groups have seen so far; a group far behind can get a higher pace; a group ahead keeps playing (optional story and "read more" pages are the extension).

**Every play period:** resume planets → students read "Since last shift" (and, at a phase start, BELL's Shift Briefing; you can project it as the opener) → ~40 minutes of play → press **Wrap up** about 5 minutes before the end → pause → one closing question. During play, point at the Monitor ("What does it say changed?"), don't give answers.

| Period | Activity | Gameplay phase | Main objectives | Teacher notes |
|---|---|---|---|---|
| 1 | Pre-test (20 min); Mars intro (5 min); form groups; colony posts and the Shift Board; first-shift checklist (controls) | — | — | Hand out ID slips. Pre-test is not graded |
| 2 | Play: survive in the dome; first failures | Phase 1 | C1, C2, A1 | Students new to Minecraft spend this period on the checklist; that's fine |
| 3 | Play: first dust storm (forecast F1) | Phase 1 | C1, B3 | Ask groups how they prepared for the storm |
| 4 | Play: first solar event (F2); ice near a heater (F4a) | Phase 1 | A4, A2 | — |
| 5 | Play: Dome stable; Hana's notes on the first Outgasser (F3); pressure climbs | Phase 1 | B1, C3, A1 | Some groups reach Phase 2 now; Keeper's Report 1 arrives in game |
| 6 | **Debrief 1**: Mars vs. Earth, pressure and water (§5.1) | — | A1, A2, C1, C2 | Bring the Mars/Earth data table |
| 7 | Play: processors and emitters (F5) | Phase 2 | A3, B1 | — |
| 8 | Play: expansion, outposts | Phase 2 | B1, C3 | — |
| 9 | Play: the Thaw; water stays (F4b); something stirs | Phase 2 | A2, B5 | — |
| 10 | Play: the thawlings; the secret | Phase 2 | D1, B5 | Remind students the creatures are fiction (the game says so) |
| 11 | Play: fauna options; ground collapse | Phase 2 | B2, B4, B5 | — |
| 12 | Play: Hydrosphere; more machines, more raids (F6) | Phase 2 | B2 | — |
| 13 | Play: toward Phase 3; second solar event (F7) | Phase 2 | A4, B3 | Phase 2 often spills into period 15; that's expected |
| 14 | **Debrief 2**: greenhouse effect with Earth data; loops; side effects; "Was waking them worth it?" (§5.2) | — | A3, B2, B5, D1, D2 | Bring Earth's CO2 and temperature graphs |
| 15 | Play: first flora (F8); Shift Briefing 3 | Phase 3 | A5 | — |
| 16 | Play: O2 rising (F9); the Air window | Phase 3 | A5, B4 | — |
| 17 | Play: drift events; holding the balance | Phase 3 | B4, C2, C3 | — |
| 18 | Play: breathable air; helmet off | Phase 3 | A5 | — |
| 19 | Play: equilibrium hold (F10) | Phase 3 | B4 | — |
| 20 | Play: the beacon; ending cards; final reflection | Phase 3 | D1, D2 | Groups that finish early compare ending cards |
| 21 | **Debrief 3**: carbon cycle; stability and change; evaluating a claim (§5.3) | — | A5, B4, B2, D2 | — |
| 22 | **Transfer task**: draw the feedback loops in Earth's carbon cycle | — | B1, B2, D2 | Scored with the rubric and answer key (`rubrics.md`) |
| 23 | Post-test (25 min); closing discussion (§5.4) | — | All | Collect ID slips if you plan the delayed post-test (§9) |
| ~4 weeks later | Optional delayed post-test (§9) | — | A2, A3, A5, B1, B2, B4, B5, D2 | ~25 min |

**End of unit:** export the data (§8), then use **Forget all** to delete the list that links student accounts to player IDs (§10).

## 5. Debrief discussion questions

Each debrief: **recall** (2 min: "Last time we talked about…"), **compare planets** (use the class overview to pick examples, never to rank groups), **questions** below, **link to Earth**. MS and HS variants where they differ. Codes in brackets are NGSS performance expectations (PE), practices (SEP), or crosscutting concepts (CCC).

### 5.1 Debrief 1 (period 6): Mars vs. Earth, pressure and water

| # | Question | Objective | NGSS |
|---|---|---|---|
| 1 | Using the Mars/Earth data table: which difference mattered most for staying alive in the dome? Why? *(HS: express it as a ratio, e.g. "less than 1 % of Earth's pressure".)* | A1 | MS-ESS1-3; SEP Analyzing and interpreting data; CCC Scale, proportion, and quantity |
| 2 | What happened to ice near your heater? What did the Monitor say water's boiling point was? What would have to change for water to stay? | A2 | MS-PS1-4 (DCI PS1.A: state changes with pressure) |
| 3 | *(HS)* Describe the pattern between pressure and boiling point you saw on the Monitor. Where on Earth does water boil below 100 °C? | A2 | extends MS-PS1-4; CCC Patterns |
| 4 | What did your group do first before the storm, and why that order? What would you change? | C1 | MS-ETS1-1 / HS-ETS1-1 (criteria and constraints) |
| 5 | What almost failed in your dome? How did you find the cause? Did it stay fixed? | C2 | MS-ETS1-4 / HS-ETS1-4 |
| 6 | Where was the safest place during the solar event? *(HS: why does Mars's surface get more radiation than Earth's?)* | A4 | HS-PS4-4 (HS only) |
| 7 | Why did one planet's pressure climb faster than another's? | B1 | CCC Cause and effect |

### 5.2 Debrief 2 (period 14): greenhouse effect, loops, side effects

Bring a graph of atmospheric CO2 (NOAA Global Monitoring Laboratory, Mauna Loa record, gml.noaa.gov/ccgg/trends) and of global surface temperature (NASA GISTEMP v4, data.giss.nasa.gov/gistemp). Named and cited by the SME; **dated copies still need to be opened and saved** before the guide ships. Using Earth's data here is what makes MS-ESS3-5 count.

**Teacher note on the graphs:**
- **Baselines differ.** GISTEMP shows temperature relative to the 1951–1980 average; the commonly quoted "about 1.1 °C of warming" is relative to 1850–1900. So the graph's latest value won't match 1.1 °C. A student who notices this is doing exactly what MS-ESS3-5 asks: praise it.
- **The CO2 record starts in 1958.** For the full past century, pair it with an ice-core record (a source is still to be named by the SME).

| # | Question | Objective | NGSS |
|---|---|---|---|
| 1 | Look at the two Earth graphs. What do you notice? What questions do you have about what caused the rise in temperature? | A3, D2 | **MS-ESS3-5** (asking questions to clarify evidence); SEP Asking questions |
| 2 | How did your emitters warm your planet? How is that like Earth's greenhouse gases? How is the greenhouse effect **different** from the ozone hole? | A3 | MS-ESS3-5 / **HS-ESS2-4** |
| 3 | Draw the loop on your planet: more machines → more thawlings → damaged machines → … Is it reinforcing or balancing? *(It's reinforcing when you answer damage by building more machines.)* Find a balancing loop on your planet (hint: polar frost). | B2 | **HS-ESS2-2**; CCC Systems and system models |
| 4 | What side effect did nobody plan for (ground collapse, salty meltwater, the thawlings)? What caused it? What's a real Earth example (thawing permafrost)? | B5 | MS-ETS1-2 / HS-ETS1-3 |
| 5 | **Was waking the thawlings worth it?** Give one reason from what you saw. *(HS: and the strongest reason against you.)* Reminder: the thawlings are made up; no life has been found on Mars. | D1 | No PE (values aren't assessed by NGSS); SEP Engaging in argument from evidence |
| 6 | Earth's warming is mainly a side effect of burning fuel. How is that like what happened on your planet? How is it different? | D2, B5 | MS-ESS3-5 / HS-ESS2-4 |

Guidance for question 5: there is no right answer and no side is graded; score the quality of the argument (D1 rubric), not the position.

### 5.3 Debrief 3 (period 21): carbon cycle, stability and change

| # | Question | Objective | NGSS |
|---|---|---|---|
| 1 | Follow one bit of carbon on your planet: air → plant → … → back to the air. What moved it each time? | A5 | **MS-LS1-6 / HS-LS2-5**; CCC Energy and matter |
| 2 | Why did breathable air need **both** enough pressure and enough oxygen? *(HS: use O2 partial pressure.)* | A5 | MS-LS1-6 (context) / HS-LS2-5 (context) |
| 3 | What did your group have to give up to hold the balance? Compare two options you had, against at least one goal *(HS: two prioritized goals, including a side effect)*. | B4 | **MS-ETS1-2 / HS-ETS1-3** |
| 4 | *(HS)* A colonist claims: "Once the planet is balanced, it will stay balanced." Is that right? Use evidence from your planet (drift events). | B4, B2 | **HS-LS2-6**; CCC Stability and change |
| 5 | Where does Earth's carbon go: air, plants, soil, ocean, rock? How is human activity shifting it? | D2, A5 | HS-ESS2-6 (if the Monitor's carbon view was used); HS-ESS3-6 |
| 6 | Compare ending cards across planets. What choices led to different planets? (Cards don't rank; don't rank them in class either.) | D1, B4 | SEP Argument from evidence |

### 5.4 Closing discussion (period 23)

1. "The ships are coming. What should the colonists know before they land?" (D1, D2)
2. "What does your planet teach about Earth?" (D2; MS-ESS3-5 / HS-ESS2-4)
3. "What surprised you most, and what did you get wrong at first?" (reflection; links to misconceptions)

## 6. Paper reflection fallback (and oral answers)

Use the paper worksheet if `reflections.csv` is switched off, if your school doesn't allow free text in exported files, or for any student who prefers paper. The questions are the same as in the game. A student may also **answer orally**; score it with the same rubric (useful for weak writers).

**Worksheet text** (one question per page; students write their **player ID**, not their name):

> **Keeper's Report** · Player ID: ________ · Report: 1 / 2 / 3 / Final
> *This goes in your report. Don't write your name or personal details.*

| Report | When | Middle school question (with sentence starter) | High school question |
|---|---|---|---|
| 1 | After Phase 1 | What almost failed in the dome, and what would you do differently next time? *"The ___ almost failed because ___. Next time I would ___."* | Same, plus: What did the Monitor show that helped you find the cause? |
| 2 | After Phase 2 | Was waking the thawlings worth it? Give one reason from what you saw. *"I think ___ because I saw ___."* | Was waking the thawlings worth it? Give your reason, and the strongest reason someone could give against you. |
| 3 | At equilibrium | To hold the balance, what did you have to give up? Was it worth it? *(starter optional)* | A colonist claims: "Once the planet is balanced, it will stay balanced." Is that claim right? Use what happened on your planet as evidence. |
| Final | Beacon lit | The ships are coming. What should the colonists know before they land? | Same, plus: What does your planet teach about Earth? |

Score with [rubrics.md](rubrics.md): Report 2 → D1 and B5; Final → D1 and D2; Report 1 → C2/C3 evidence (not rubric-scored); Report 3 → formative (B4 is scored by the post-test).

## 7. Assessment and grading

| Evidence | Level | Use for grades? |
|---|---|---|
| Pre/post test, transfer task, rubric-scored reflections (written or oral) | Individual, summative | **Yes**: the only evidence to grade with |
| Own forecasts, "What caused it?" checks, own repairs, Monitor checks | Individual, formative | **No**: chances to act differ in a group (who held the keyboard, who was absent). Use them to decide who needs help |
| Gates, dome survival, breathable air, equilibrium, beacon | Group | **No**: never one student's evidence |

Criterion for rubric-scored objectives: **3 of 4 or higher**. Return rubric scores to students before the next debrief, so the written reflection gets feedback.

## 8. Reading the CSV exports

**How:** press **Export** in the class overview (or `/mars export all`). Files appear in the server's Mars data folder under `exports/<date-time>/`. They open in any spreadsheet program (UTF-8; formulas are blocked for safety).

**Privacy:** every file uses **player IDs** (`P-7K3QX`) only. There are no usernames, and no option to add them. The list that links IDs to student accounts stays on the server and is never exported. You can see who is who only in the class overview on the server.

**The five files**

| File | One row per | Answers |
|---|---|---|
| `players.csv` | Student | *What has each student shown? Who needs help?* |
| `planets.csv` | Group planet | *Where is each group?* |
| `events.csv` | Logged event | Details, when you need to look into something |
| `goals.csv` | Objective | Objective titles, NGSS codes, priority. A key for reading the other files |
| `reflections.csv` | Written answer | The text of each Keeper's Report answer, with empty rubric columns for you to fill. **On by default in classroom mode**; you can switch it off |

**Columns to look at first (`players.csv`)**

| Question | Columns | How to read them |
|---|---|---|
| Is the student taking part? | `sessions_present`, `sols_played` | Low numbers explain thin evidence |
| What help did they have? | `guidance` (also in `planets.csv`) | Gentle or standard. Compare students on the same setting; standard gives fewer hints |
| Is their predicting improving? | `forecast_accuracy_p1`, `_p2`, `_p3`, `b3_criterion_met` | `b3_criterion_met` = Phase 3 accuracy at least 20 points above Phase 1, with ≥ 3 forecasts per phase. "not enough data" is common for absent students |
| Are they guessing or missing prompts? | `forecasts_expired`, `forecasts_late` | Many expired = prompts missed (busy or absent), not wrong. Not scored either way |
| Do their fixes hold? | `failures_fixed`, `repeat_failures_after_own_fix` | Credit goes to the student **who did the repair**, not to a teammate who only claimed it on the Shift Board (decided). Repeats after their own fix = talk to them about root causes |
| Do they check data first? | `major_builds`, `monitor_checks_before_builds` | About half or more is the target |
| Have they sent reports? | `report_1` … `report_final` | "no" = remind them, or use the paper worksheet |
| Which objectives have they shown? | `<goal>_shown`, `<goal>_evidence` (e.g. `A2_shown`, `A2_evidence`) | One piece of the student's **own** evidence. Formative; not mastery |

**Example (made-up data, for illustration only):**

| player_id | planet | forecast_accuracy_p1 | forecast_accuracy_p3 | b3_criterion_met | failures_fixed | repeat_failures_after_own_fix | report_2 |
|---|---|---|---|---|---|---|---|
| P-7K3QX | 2 | 0.33 | 0.75 | yes | 4 | 0 | yes |
| P-Q2M8D | 2 | 0.50 | 0.50 | no | 0 | 0 | no |

Read as: P-7K3QX's predictions improved and their fixes held. P-Q2M8D didn't repair anything and hasn't sent Report 2: check whether they're getting keyboard time, and offer the paper worksheet.

**`planets.csv`:** `current_phase`, `<gate>_reached` and `<gate>_sol` show where each group is; `storms_survived` and `core_failures_per_storm` show how the dome held; fauna option columns (`shrouds`, `lures`, `quiet_zones`, `defenses`) are useful for debrief 2. They are **not** scores.

**`events.csv`:** one row per event. `superseded = yes` marks events from a stretch you rolled back; they're kept, not deleted. `result` for forecasts is `correct`, `incorrect`, `late`, `expired` (never answered), or `void` (the outcome never happened, or the stretch was rolled back). Only `correct` and `incorrect` count.

**`reflections.csv` and what name scrubbing can't catch.** Before export, the game replaces any **Minecraft username** of a player on your server found in an answer with that player's ID. The `names_replaced` column says how many were replaced in that answer. Scrubbing **can't** catch:

| Not caught | Example | What to do |
|---|---|---|
| Real names and nicknames | "Me and Sarah fixed the dome" | Read answers before sharing them anywhere; the in-game note asks students not to write names |
| Misspelled or split usernames | "Stevee", "Ste ve" | Same |
| Other personal details | School, town, age, family | Same |
| **Wrong replacements** | A student whose username is an ordinary word (e.g. "Water"): that word is replaced in **everyone's** answers, which can make a science answer hard to read | `names_replaced` > 0 tells you to read that answer with care; the original is visible only in the class overview on the server |

So `reflections.csv` is **not anonymous** in the way the other files are. Treat it like student work: keep it on school systems and don't post it.

## 9. Optional delayed post-test

Part of the planned learning study (Stage 6). About **4 weeks** after the post-test (3–6 weeks is fine; note the actual gap). 9 items, ~25 minutes, on paper. Students write their player ID from the slip. **Don't review the unit before it**; the point is to see what stuck.

What it contains: four items repeated from the post-test (A2, A3, A5, B4), two new-scenario items (B1, B2), two misconception checks, and one open question about an Earth situation that doesn't name any science idea. That last one shows whether students use systems thinking on their own (side effects, loops, trade-offs). Scoring rubric and blueprint: LEARNING.md §3.6.

## 10. Data notice for students and families (plain language)

> In this game, your computer and the school's game server keep a record of what you do in the game (like which machines you built and your forecasts), so your teacher can see what you learned. The record uses a code (like P-7K3QX), not your name. Nothing is sent over the internet. Your written answers to the game's questions can be read by your teacher; please don't write your name or personal details in them. At the end of the unit, your teacher deletes the list that connects your code to your account.

Legal requirements (COPPA, FERPA, GDPR, or local law) have **not been reviewed** (risk R-20). Follow your school's data policy; if it doesn't allow free text in files, switch off `reflections.csv` and use the paper worksheet.

## 11. Shorter units

Use **Set phase** to start a planet at a later phase. The overview marks the objectives skipped. Teach those in class instead:

| Shortcut | Periods (about) | Objectives that lose in-game teaching | Cover them with |
|---|---|---|---|
| Start at Phase 2 | ~18 (drop periods 2–6) | A4, C1, most of A1; part of A2 (the Phase 1 ice) | A short lesson with the Mars/Earth data table; a radiation demo; the Phase 1 "ice near a heater" as a teacher demo in game |
| Phase 2 only, end at Phase 3 begins | ~12 | A4, C1, A1, A5, B4 | Debrief 3 questions as a lesson; A5 and B4 post-test items become stretch items |
| Full unit, debriefs shortened to half periods | ~21 | None | Combine the transfer task with debrief 3 |

## 12. Open items before publishing

- NGSS codes: word-for-word check **done** (SME, round 3).
- Earth graphs for debrief 2: named and cited (SME round 3); dated copies still to be opened and saved; an ice-core CO2 source still to be named.
- The transfer-task answer key, forecast distractors, rubrics, and this guide: review by outside science teachers (owner decision; [teacher-review-pack.md](teacher-review-pack.md)).
- Server setup guide and IT one-pager (Technical Lead).
- Player ID slips printable from the class overview (Technical Lead).
- Teacher review of this whole guide (risk R-29).
