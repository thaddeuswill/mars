# Playtest Plan — Solo Designer Playtests

**Owner:** Lead Game Designer · **Status:** Draft 1 (2026-11-26) · **Depends on:** [DESIGN.md](../../DESIGN.md), [balance-sheet.md](balance-sheet.md), [story-bible.md](story-bible.md), [ROADMAP.md](../../ROADMAP.md)

## Who plays and what that can tell us

**The tester is one adult designer (the user), playing solo.** No students are recruited. The project is a design exercise; we still design as if 12–18-year-olds would play it.

| A solo adult playtest **can** tell us | It **cannot** tell us |
|---|---|
| Whether the build works, and where it breaks | Learning gain for ages 12–18 (the tester already knows the science) |
| Whether the balance-sheet curves match real play time | How a real 12-year-old reads, plans, or gives up |
| Whether every Index change is visible and has a named cause | Group dynamics on a 3–5 player planet |
| Whether holds, waits, and grind points feel idle | Classroom timing with a teacher, pauses, and absences |
| Text length, clarity, and tone, judged against the guide | Real fun ratings from the target age |

To cover the gap, the tester plays each session **twice in their head**: once as the build is, and once while watching for the two target players below. Notes from those lenses are **design judgment, not evidence**, and reports must say so.

## The two lenses

| | **Lens A: a 12-year-old, new to tech modpacks** | **Lens B: an experienced 17-year-old** |
|---|---|---|
| Knows | Vanilla Minecraft survival; little science vocabulary | Mekanism/Create or similar; high school science |
| Reads | Short lines only; skips anything over ~2 sentences | Skims text, opens "read more" when curious |
| Likely failure | Gets lost in recipe chains; doesn't know what to do next; doesn't connect a number to a cause | Optimizes and finds exploits; gets bored by waits; ignores the journal |
| Watch for | Any moment with no obvious next step for more than ~2 minutes; any word above grade 6–7; any number with no picture or icon; color as the only signal | Dominant strategies (e.g. only lethal defense); idle holds; journal skipped because it's slow; "the science is fake" moments |
| Success looks like | Can say what the Monitor shows and why it changed | Chooses non-lethal or mixed fauna answers because they work better, not because they're told |

## Sessions by roadmap stage

| Session | Build | Goal | Length |
|---|---|---|---|
| P1 | Stage 2 prototype (Index, Monitor, one machine) | Do the numbers move, and can you see why? | ~1 h |
| P2 | Stage 2 prototype, classroom config | Same, with `pace.rateMultiplier` = 4 and short holds; check thresholds are unchanged | ~45 min |
| S1 | Stage 3 vertical slice (Phase 1 complete) | Phase 1 start to 5 kPa, standard pace | ~8 h over several sittings |
| S2 | Stage 3 slice, classroom config, `group.size` = 4 | Phase 1 in classroom pacing (solo tester, group rates) | ~2.5 h |
| A1+ | Stage 5 alpha and later | Full phases; fauna options; holds and drift events; ending cards | Per phase |

Group play (3–5 people) cannot be tested by one person. S2 checks only the *rates* a group would see. A real group test is listed under Gaps.

## Tasks (session S1, the main one)

Play without the design docs open. Give each task up to the time shown before looking anything up.

| # | Task | Time limit | Checks |
|---|---|---|---|
| 1 | From spawn, find out what is wrong with the dome | 5 min | Opening clarity (story beat 1); C1 |
| 2 | Keep O2, water, and power positive for 1 sol | 20 min | C1 loop; HUD readability |
| 3 | Answer the first failure report without help | 10 min | Failure report gives a cause and a hint, not the fix; C2 |
| 4 | Survive the first dust storm; log a forecast first | 1 storm | Prediction prompt; storm plan; B3, C1 |
| 5 | Reach Dome stable | — | Hold clarity: do you know the counter is running? |
| 6 | Build the first Regolith Outgasser and point to its effect on the Monitor | 10 min | C-01 fix; cause shown on Monitor; B1, C3 |
| 7 | Explain to yourself, out loud, why pressure dipped during the storm | 2 min | Causes are visible; polar frost and solar loss named |
| 8 | Carry ice near a heater; describe what happens | 5 min | Ice-to-water rule, first A2 moment |
| 9 | Use the airlock dust-off after an EVA | — | Perchlorate step is a quick habit, not a chore |
| 10 | Reach 5 kPa (Phase 2 begins); send Keeper's Report 1 | — | Pacing; debrief length |

## Observation sheet (fill during play)

| Time (real) | Sol | Pressure | What I was doing | Stuck? (min) | Idle? (min) | Lens A note | Lens B note | Bug / fix idea |
|---|---|---|---|---|---|---|---|---|
| | | | | | | | | |

Also record, once per session:
- **Real time to each gate** vs. the balance sheet (§2 for Phase 1: Dome stable ~3.0 h, 5 kPa ~8.3 h; classroom 5 kPa ~2.4–2.7 h).
- **Pressure at sols 5, 10, 15, 20** vs. the balance sheet curve.
- **Longest stretch with nothing useful to do** (target: under 5 minutes).
- **Every text box over its budget** (story bible §6.1).
- **Every time color was the only signal.**
- **Every "this isn't how Mars works" moment**, for the SME.

## Questions after each session (answer in writing)

1. Rate fun 1–5. Then guess the rating Lens A and Lens B would give, and why.
2. Where did you feel most capable? Most lost?
3. Name every cause of a pressure change you saw. Which ones could you see in the world, not just on the Monitor?
4. Was any wait idle? How long?
5. Did any text feel like homework? Which?
6. What would Lens A not understand? What would Lens B skip or exploit?
7. Did anything suggest Mars has life, or that terraforming is fast, without a label nearby?
8. One change you'd make first.

## Phase-specific checks (alpha and later)

| Area | Check | Pass when |
|---|---|---|
| Fauna options | Play one Phase 2 run with only lethal defense, one with shrouds and quiet zones | Raids stay high in the lethal run; the mixed run is easier to hold. Neither is labeled right or wrong. |
| Later life stage | Read every fauna text | No "mutate" or "mutation" anywhere |
| Ground collapse | Build one outpost on ice, one on rock | Collapse is visible, explained, and avoidable |
| Holds | Equilibrium hold, standard and classroom | At least one drift event per 1.5 sols; no idle stretch over 5 minutes; the counter pauses, then resets only after a long break |
| Breathable | Try to reach Breathable with low pressure and high O2% | The Monitor shows O2 partial pressure clearly, and the failure is explained |
| Ending cards | Reach the beacon with each fauna outcome (debug commands allowed) | Each card reads as a description, not a grade |
| Thresholds | Compare gate numbers across standard, classroom, and `group.size` = 4 | Identical in all three |

## Analysis and prioritizing

After each session, the designer files a short report in `docs/design/` (e.g. `playtest-S1-report.md`) with:
- Measured times and curves vs. the balance sheet.
- Issues ranked: **Blocker** (can't continue or teaches something false) → **Major** (idle over 5 min, stuck over 5 min, text far over budget, a lens likely to quit) → **Minor**.
- For each change: goal IDs touched (handoff to the Lead Instructional Designer if any), and its effect on fun and on learning (fun–learning matrix).
- Which findings are measured (times, curves, bugs) and which are lens judgments.

## Gaps this plan leaves open

| Gap | Effect | Option |
|---|---|---|
| No players aged 12–18 | Learning gain and age-fit are unproven | Lens notes plus readability tools; any real age-group test would need consent for minors (out of scope now) |
| No group of 3–5 | Colony posts, Shift Board, group fauna load untested | The tester runs 2–3 accounts on a local server to check rules mechanically, or invites adult friends |
| Tester knows the design | Discovery and clarity are overrated | Leave gaps of days between sessions; play from the in-game journal only |
