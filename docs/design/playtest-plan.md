# Playtest Plan — Solo Designer Playtests

**Owner:** Lead Game Designer · **Status:** Draft 2 (2026-11-26): functionality and instructional passes, one scenario per goal ID, flow and motivation checks, observation prompts, root-cause sheet · **Depends on:** [DESIGN.md](../../DESIGN.md), [balance-sheet.md](balance-sheet.md), [story-bible.md](story-bible.md), [ROADMAP.md](../../ROADMAP.md)

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

## Two separate passes (DESIGN.md, Fidelity levels)

Every session from S1 on runs **two passes over the same build**, recorded on separate sheets so "it works" is never mistaken for "it teaches".

| | **Functionality pass** ("does it work?") | **Instructional effectiveness pass** ("does it teach?") |
|---|---|---|
| Question | Do triggers fire, values move, logs record, text fit, saves load? | Could a player in Lens A or B say what the scenario was meant to teach, in their own words, after playing it? |
| How | Debug commands allowed (`/mars index set`, `/mars gate force`); check the action log and screenshots at 1280×720 | Play without debug commands and without the design docs; answer the scenario's "say it" prompt out loud **before** reading the result text |
| Pass when | Every check in the scenario's Functionality column is true, with the log line or screenshot as proof | The "say it" answer matches the scenario's target idea for that lens. **Lens judgment, not learning evidence** (solo adult tester) |
| Owner of fixes | Technical Lead (bugs), Lead Game Designer (data) | Lead Game Designer and Lead Instructional Designer together |

Order: functionality first; an instructional pass on a broken build measures nothing.

## Test scenarios: one per goal ID

Each scenario checks that its goal can be completed in play: taught, practiced, given feedback, and measured. Setup gives the quickest way to reach the moment (debug allowed in the functionality pass only).

| Goal | Scenario | Setup | Functionality pass: passes when | Instructional pass: "say it" prompt and target |
|---|---|---|---|---|
| A1 | Compare outside with Earth | Fresh world; finish Landing; reach Dome stable | Monitor shows pressure, temperature, air mix; Dome stable "On Earth" line shows; milestones draw on the log gauge | "How is the air outside different from Earth's?" Target: much thinner (under 1 %), colder, mostly CO2 |
| A2 | Three states of water | Warm ice near a heater at ~1 kPa; again at 5 kPa and > 0 °C local | F4a shows the correct band text; ice vanishes or boils away; at 5 kPa water stays and refreezes at night; Monitor water line shows the boiling point | "Why did the ice vanish before and stay as water now?" Target: pressure, not only warmth, decides whether water stays liquid |
| A3 | First emitter warms the planet | Phase 2; start one emitter | F5 issued on Start, locks on Monitor open or 1 sol; temperature trend up with "Emitter" cause line | "How does the emitter warm Mars?" Target: its gases trap heat that would escape to space; not CO2; not the ozone hole |
| A4 | Two solar events | Phase 1 event, then one after pressure doubles | Warnings at T−1 and T−0.25 sol; F2/F7 lock at the shelter line; dose lower at the second event; first exposure outside blocks once | "Why is the surface dangerous in a solar event, and why less now?" Target: thin air, no global magnetic field; more air absorbs more |
| A5 | Seeding moves both gases | Phase 3; place one seed kit | F8 issued; O2 rises, CO2 falls on the Monitor; mature patch slows uptake | "What did the plants do to the air?" Target: take in CO2, give off O2; slows as they mature |
| B1 | Name the cause | Any phase; let a value change from two causes (e.g. Outgassers and a storm) | Every change has a cause line; "What caused it?" page offers the event | "What changed pressure today?" Target: names both causes from the Monitor |
| B2 | Inside a loop | Hydrosphere; add 3 machines; then shroud 2 | F6 fires; fauna pressure F rises, then falls with shrouds; raids follow F | "Why do more machines bring more thawlings, and is that a loop?" Target: reinforcing loop through signature; shrouds lower the pull |
| B3 | Forecast tally improves | Play through a phase answering every forecast | Tally per phase in the journal; expired forecasts logged as unanswered | "Did your forecasts get better? Why?" Target: names a pattern learned (e.g. pressure before water) |
| B4 | Hold the band under drift | Phase 3 hold; two drift events | Hold pauses then resumes; Air window shows edges and time-to-edge; F10 fires on emitter wear | "What did you give up to hold the band?" Target: names a real cost (power, land, output) |
| B5 | A fix makes a new problem | Build a warm machine on a scanned ice lens | Cracked ground warning, then collapse; failure report names melting ground ice | "What went wrong, and why was it hard to see coming?" Target: the warming that helped also melted the ground |
| C1 | Through the first storm | Phase 1; first storm forecast | F1 issued 1 sol ahead; solar drops 60 %; battery reserve carries the dome | "What did you do before the storm, and why?" Target: stored power or cut use for life-critical needs first |
| C2 | Fix it so it stays fixed | Cause a seal breach twice; in a group test, have one account claim it and another repair it | First report has Hana's tip; second is more specific; after a holding fix, BELL's positive closing line; no repeat from the same cause; **C2 credit goes to the repairing account (`actor`), not the claimer**; no hint contains a banned token (CR-H2) | "What did you change the second time?" Target: fixed the cause (outside edge), not the symptom |
| C3 | Data before building | Before placing a third Outgasser | Action log records Monitor opened before the build | "What did the Monitor tell you before you built?" Target: reads the trend or estimate |
| D1 | The thawling choice | Reach Phase 3 with any fauna strategy | Report 2 question offered (deferrable, re-offered once per session); answer saved to reflections.csv | "Was waking them worth it? One reason from what you saw." Target: a claim plus evidence (D1 rubric) |
| D2 | Mars teaches about Earth | Reach the beacon; read Mensah's replies | Every gate entry has an "On Earth" line; final question offered; HS read more opens | "What does your planet teach about Earth?" Target: one matched mechanism (greenhouse, loop, carbon cycle) |

## Flow and motivation checks (added from user research)

| Check | Scenario | Pass when |
|---|---|---|
| Flow: clear tasks | Every 10 minutes, without the docs, say what you're working toward | Can name the next gate or the current hold every time |
| Flow: clear, immediate feedback | After each new machine, find its effect | Sees "Effect on planet" at once and a cause line within 1 sol |
| Flow: concentration | Count interruptions during building or danger | Zero non-safety interruptions mid-task; pop-ups within the config cap |
| Flow: attainable goal | Phase 3 Air window, Lens A | Tester can say how to widen the window (raise pressure) |
| Flow channel | Note "bored" and "overwhelmed" moments on the sheet | No boredom stretch > 5 min; no overwhelm moment with > 1 new system introduced |
| Control / no hidden DDA | Compare raids against fauna pressure F | Raids track F; no change in difficulty without a visible cause |
| Fun: noise vs. boredom | At each phase end: "what pattern did you just learn? what's still noise?" | One pattern named per phase; noise items listed for redesign |
| Paradox of choice | First visit to the recipe view after each gate | Tester picks a sensible build within 2 min using "Useful now" |
| Rewards not controlling | Rate whether any reward felt like a bribe | No reward named as "I did it only for the reward" |
| Baseline acknowledgment | At every gate and hold end | A visible "just finished" note, world change, and BELL line each time |
| Zeigarnik without compulsion | Note any urge to "complete" something unrelated to a goal | None found, or each one tied to a goal ID |
| Player types | One run as explorer (archive, sites), one as achiever (fastest gates) | Both find enough to do; neither needs PvP |
| Guidance levels | Same Phase 1 stretch on gentle, standard, expert (expert in standard mode only) | At every level failure reports name the cause, forecast results explain, safety warnings fire; the level appears in planets.csv and players.csv; expert isn't offered in classroom mode |
| Look before you leap | With F3 unanswered, open the Monitor | The forecast card shows first; Skip logs "unanswered"; the forecast is never silently closed |
| Lock points | For every forecast, note the first cue that hinted the answer | Every lock comes before that cue (CR-F9) |

## Observation prompts (Chapter 9)

Direct observation matters most for a learning game. When anyone other than the designer plays (the user, adult friends in a group test, SME at alpha), the observer notes:
- **Questions the tester asks** out loud, word for word. Each one is a missing piece of guidance.
- **Interactions between testers** in group tests: who explains what to whom, who holds the keyboard, who is left out.
- **"Do you need help?"** Ask every ~15 minutes, and note the answer *and* what you see. Testers often don't realize they're stuck.
- **Breezers and strugglers.** A tester who breezes through shows what a strong player does (record their path as a model); one who struggles shows where guidance is missing (record the exact moment).
- **Silent stops:** any pause over 2 minutes with no action.

**Caution on evidence.** Alpha testers here are the user and SME-level adults, not ages 12–18. Don't draw strong conclusions from a non-representative group; check findings with the user and the SME before changing a stable or flexible item.

## Root-cause sheet (for every issue found)

Analyze each symptom from several perspectives before fixing it; then the team prioritizes together (TEAM.md 8.7).

| # | Symptom (what was seen, with time) | Interface? | Gameplay–instruction conflict? | Wrong expertise level? (Lens A vs. B) | Science or text? | Most likely root cause | Goal IDs touched | Severity (Blocker / Major / Minor) | Owner |
|---|---|---|---|---|---|---|---|---|---|
| | | | | | | | | | |

Rule: if players find a learning moment hard or confusing, check first whether the strategy is misaligned (DESIGN.md, Core Design Check) before adding more text or hints.

## Analysis and prioritizing

After each session, the designer files a short report in `docs/design/` (e.g. `playtest-S1-report.md`) with:
- Measured times and curves vs. the balance sheet.
- Each issue through the root-cause sheet above.
- Issues ranked: **Blocker** (can't continue or teaches something false) → **Major** (idle over 5 min, stuck over 5 min, text far over budget, a lens likely to quit) → **Minor**.
- For each change: goal IDs touched (handoff to the Lead Instructional Designer if any), and its effect on fun and on learning (fun–learning matrix).
- Which findings are measured (times, curves, bugs) and which are lens judgments.

## Gaps this plan leaves open

| Gap | Effect | Option |
|---|---|---|
| No players aged 12–18 | Learning gain and age-fit are unproven | Lens notes plus readability tools; any real age-group test would need consent for minors (out of scope now) |
| No group of 3–5 | Colony posts, Shift Board, group fauna load untested | The tester runs 2–3 accounts on a local server to check rules mechanically, or invites adult friends |
| Tester knows the design | Discovery and clarity are overrated | Leave gaps of days between sessions; play from the in-game journal only |
