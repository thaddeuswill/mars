# Instructional Designer report: Landing, briefing and journal text pass, 2026-11-28

Type of work: docs only, desk review. No Java, no pack, no game run, no learner data. Readability = a **rough** Flesch-Kincaid script (vowel-group syllables, throwaway, in the session scratchpad), **not the CR-T6 check**. Changes are **proposed**, key ids unchanged. Edits: `docs/design/landing-spec.md` (new section 12 plus marked in-place edits), `docs/design/messages/phase-1.md`, new `docs/learning/items-phase1-misconceptions.md`.

## 0. Handoffs received (from the Game Designer, slice 4 report)
| Handoff | Answer |
|---|---|
| Grade 6 to 7 pass on all keys | Accepted. Section 1 |
| Does the storm sentence make F1 trivial | Accepted. **Yes**; sentence removed (section 2) |
| "Hull holds back that difference" | Accepted, **reworded** (section 3) |
| Kilopascals needs more than the inline definition? | Answered: no, inline "(kilopascals)" is enough; add the push-direction sentence instead (section 1) |
| On Earth comparison | Accepted with one wording change (section 3) |

## 1. Reading-level and content-rule pass
All 27 Landing, briefing and journal texts, rough FK (word counts from the script; hyphenated words count as two, so they run 1 to 3 above the spec's counts, all still under caps):

| Text | FK | Note |
|---|---|---|
| Steps 1, 2, 2.suit, 4 to 8 and skip lines, BELL logs, intro | -1.6 to 3.4 | pass |
| `landing.step.3` | 8.1 | artifact: two 4-word sentences and the in-game name "Planetary Monitor". Exempt as a name; changed for clarity instead (W1) |
| `landing.skip.2` | 6.6 | same kind of artifact ("Monitor: planet data."); pass |
| Briefing | 2.3 (38 words, not 40) | pass |
| `journal.about`, `sol` | 0.8, -0.7 | pass |
| `journal.a1.pressure` | 1.8 | pass |
| `journal.power`, `gamerules`, `dome_stable` | 4.2, 3.6, 2.3 | pass |
| `journal.sky.stylized` | 3.8 | SME verbatim, not edited |
| HS read-mores | 0.7, 3.4 | pass |

Short sentences make FK look easy. The real risks are words, checked by hand: "kilopascals" (defined on the spot, fine), "hull" and "relay" (story words, in context), "Landing grace" (a game term; now explained), "scatters" (SME verbatim). **Gaps the script cannot see, and the fixes (spec section 12):**

| # | Finding | Rule | Fix |
|---|---|---|---|
| W1 | Step 3 "Read the outside air" is ambiguous: the Monitor has an `Air` row (tank) and an `Outside air` line (planet) | CR-H1 spirit, CR-T7 | "Find the Outside air line." |
| W2 | Storm sentence (section 2) | CR-F9, CR-T8, B3 | Removed |
| W3 | Game-rule line says "nothing runs out while you do the checklist", but L2 makes grace a clock (sol 1.2): untrue for a slow player | CR-H1 (claim must be true of the simulation) | "for your first {grace_minutes} minutes"; value read from `life_support.json`, not typed |
| W4 | "100 times the force", "holds back that difference" (section 3) | accuracy, grade 6 to 8 force/pressure confusion | Reworded |
| W5 | "95 %" | CR-T7(b) spirit; `journal.dust.floor` already says "60 percent" | "95 percent" |
| W6 | Nothing says which way air pushes; "vacuum sucks" is unaddressed | misconception (section 5) | Add one sentence to `journal.a1.pressure` |
| W7 | Goal tags overclaim: `about`, `sol` tagged C1; briefing tagged A1; sky tagged A1 | alignment honesty | about/sol none (orientation); briefing C1; sky B1, A1 weak |

Other content-rule checks: CR-T1 (no learner/lesson/objective/quiz/test in any text) and CR-T5 pass by reading, scan not run by script. CR-T4 pass (longest entry 54 words, cap 80). One of three spec claims fails my reading of CR-F9: see section 2.

## 2. Does the storm sentence make F1 trivial? Yes. Remove it.
F1 asks "What will happen to your solar power? It drops, it rises, it stays the same." The sentence "Dust storms cut solar power, so store some" **is** the keyed answer.
- The spec's mitigation ("ships only with F1", "F1 is not issued before the briefing") does not work. Briefing 1 is copied to the journal under Briefings and stays readable while F1 is open, so CR-F9's intent (a message must not give the answer while the forecast is open) is broken even though the briefing was delivered earlier. **The F9 check as written would not catch this**; I propose the check also scan journal-retrievable text (to the Technical Lead, below).
- Learning cost: F1 is the Phase 1 baseline for B3 (own Phase 3 accuracy at least 20 points above own Phase 1). A baseline inflated by reading measures recall and shrinks the measurable gain. Generating a prediction before the answer is shown is the point (generation and prediction effects).
- Motivation cost: LEARNING and the F1 spec call it "a chance to be right about the planet"; the briefing would take that away.
- The C1 planning cue is worth keeping, and it works without the cause: "Mars weather can change fast, so keep some power in reserve." True, names no direction, names no machine that does not exist, and also reads sensibly with solar events. Side benefit: no `storms_built` switch, one text.
- The causal sentence moves to the F1 result, where it already is ("Dust in the air blocks sunlight").
- Risk: a player might read "weather" and guess storms. That is fine; guessing "power drops" from weather is prediction, not reading the answer.

## 3. "Hull holds back that difference" and the On Earth comparison
- **Arithmetic:** 101.3 / 0.636 = 159 (calc). "More than 100 times" is true and consistent with the A1 entry (101 and 0.6 give about 168). Keep.
- **"Force of the air"** (W4): force depends on area; pressure is the idea. Grades 6 to 8 mix them. "pushes on you more than 100 times as hard as the air outside this dome" stays at the learners' level and is correct as a comparison of pressure. Note the sentence compares Earth air to the air **outside the dome**, so it makes no claim about the dome's own inside pressure.
- **"Your hull holds back that difference"** (HS read more) has two faults. It can be read as thin Mars air pushing in (a vacuum-sucks picture), and it quietly asserts the dome is at Earth's 101 kPa, which the docs do not establish. New wording: "Inside the dome, air pushes out on the hull harder than the thin air outside pushes in. The hull holds that difference." Direction is right and no inside value is claimed. **SME to confirm.**
- **"Your body pushes back"**: correct in spirit (body fluids and air spaces match the outside pressure), but a 12-year-old may picture muscles. Proposed: "the air inside your body pushes back just as hard." Fallback "because you grew up in it" stays if the SME declines (true but explains less).
- **Reveals nothing a forecast asks:** F3 asks the rate of pressure rise; F4a/F4b ask boiling and ice; the 0.6 and 101 values are static starting facts that F3's HS result also states. `reveals: none` holds. **Assessment note:** the A1 HS post-test criterion is "expresses a difference as a ratio". The journal now gives the ratio away. The post-test data task must use its own table (temperature or radiation for the ratio part, or numbers other than 0.6/101), so it measures transfer, not recall.

## 4. Journal entries against the goals
No new NGSS codes. The codes already verified in `docs/science/ngss-verification.md` stay as in LEARNING section 6: A1 MS-ESS1-3 (moderate, **assessed with data, not recall**), A2 MS-PS1-4 (DCI PS1.A), A4 HS-PS4-4 (moderate, EM only, none at MS), C1 MS/HS-ETS1-1, C2 MS-ETS1-4 and HS-ETS1-2/4. Journal text is exposition, so none of these entries "meets" a PE; they supply the content a data task uses.

| Goal | What the Phase 1 texts give | Gap |
|---|---|---|
| A1 | Pressure (a1, dome_stable), atmosphere makeup (airmix), Earth contrast | **Temperature and radiation not covered** in this slice's texts (A1 lists four variables). Temperature arrives with the Monitor temperature row, radiation with the solar-event lines |
| A2 | None in these texts | Expected: A2 is taught by F4a/F4b and the Mensah reply. Not a gap for slice 4 |
| A4 | None | **Gap in this slice**: first radiation text is `log.solar.*` and F2 (phase-1.md §3, §6). Fine if F2 is in the slice; otherwise the mini test A4 item has no teaching before it |
| C1 | Briefing (watch air, water, power; keep reserve), `journal.power` rule (life support first), step 2 tank | Good for "life-critical first". Planning with a real choice comes at the first storm (F1) |
| C2 | "If something nearly fails, ask why" (habit prompt) | **Prompt only**: no failure feedback in these texts; L8 correctly says the Landing patch is not evidence. First real C2 is the slice 6 breach report |

Alignment table (LEARNING 0.7) status for these goals: content present for A1 (partial), C1; A2, A4, C2 content exists elsewhere (forecasts, failure reports) and is not built in this slice. Not a design gap, but a slice gap for the mini test (section 6).

## 5. Misconception items
Written, tagged A1, SME check pending: `docs/learning/items-phase1-misconceptions.md` (MS and HS stems, one key each, distractors from the misconception).
- **Oxygen is easy on Mars** (M-A1-O2): taught by the airmix line. HS key uses a **calc**: oxygen push about 0.001 kPa on Mars against about 21 kPa on Earth.
- **Vacuum sucks** (M-A1-V): taught by the new W6 sentence and the W4 hull line. The HS item needs a dome pressure value; I used 70 kPa as **invented** and flagged it.

## 6. What the slice 8 mini test should reuse
| Reuse | From | Rule |
|---|---|---|
| The **numbers** as the A1 data table: 0.6 / 101.3 kPa, 95.1 % CO2, 0.145 % O2 (Part A, sourced) | a1, airmix | Give the table; ask for comparison and one consequence. Never reuse the journal sentences (recall) |
| Two misconception items | section 5 | Same stems pre and post, order shuffled, record the chosen distractor |
| C1 scenario shape | briefing "keep some power in reserve", power rule | New scenario, different resource (not a storm and solar) so F1 is not echoed |
| A4 item | F2 and `log.solar.*` | New scenario (a different shelter choice), after F2 exists |
| **Do not reuse** | Landing steps, skip lines, BELL logs, about, sol | Controls text; teaches no goal; naming them as test content would test the tutorial |
| **Do not reuse** | the ratio ("more than 100 times") for the HS A1 ratio part | The journal gives it away (section 3) |

## 7. Decisions needed (at most 3)
1. **Remove the briefing storm sentence and put the causal line only in the F1 result?** ill-structured (instruction against the Game Designer's wish for a clear threat cue). **Recommend yes**: neutral "keep some power in reserve" keeps the planning cue and protects F1/B3.
2. **For the Phase 1 mini test, which two misconception items: my two new ones (oxygen, vacuum) or the LEARNING.md pair (21 % always breathable, water boils at 100 C)?** ill-structured. **Recommend oxygen plus boiling at 100 C**: the second is taught directly by F4a, which is in the slice; keep vacuum in the bank for the full post-test, since its teaching is one sentence in this slice.
3. **Make the Landing grace line say the minutes ({grace_minutes}), or just "at the start"?** well-structured. **Recommend the minutes from data**: it is the honest form of L2 and a teacher can read it; the cost is one placeholder.

## 8. Handoffs
> **To:** Game Designer · **Problem type:** well-structured
> **Problem:** apply W1 to W7 to the build-ready spec if accepted (already marked in the files).
> **Context:** `docs/design/landing-spec.md` section 12; `messages/phase-1.md` §1, §2, §9.
> **Needed from them:** accept or revert each row; if W2 is accepted, remove `storms_built` from the briefing `names` gating.
> **Done when:** the spec text matches `en_us.json` keys.

> **To:** SME · **Problem type:** well-structured
> **Problem:** confirm (a) "the air inside your body pushes back just as hard", (b) the hull sentence (direction of push, no inside value), (c) the oxygen HS calc (about 0.001 against about 21 kPa), (d) 24 h 40 min sol, (e) the molecule line, and (f) a dome pressure to use in M-A1-V HS.
> **Done when:** each marked verified or replaced.

> **To:** Technical Lead (and Codex for the content test) · **Problem type:** well-structured
> **Problem:** extend CR-F9 so the check also scans journal-retrievable copies (a briefing copy stays readable while a forecast is open); `{grace_minutes}` placeholder read from `life_support.json` `landingEndSol` x 20; align `life.blackout` ("bunk") with the code (shelter).
> **Done when:** a test fails when a journal entry with `reveals: F1` is retrievable while F1 is open.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** `landing.skip.2` and similar stay as written. Beat 6 line is still missing (noted earlier).

## 9. Risks
- The "keep some power in reserve" cue is weaker than the storm sentence for Lens A players who skip reading; F1 still teaches through its result. Watch in S1.
- All FK numbers are a rough script; the real CR-T6 check has not run on any of these.
- The F9 loophole (journal copies) probably applies to other entries too (for example `journal.power` before F2 on shelter); not audited here.
- A4 has no teaching text in this slice; the mini test A4 item depends on F2 being built.

## 10. Shared model updates
- **Lesson:** "delivered earlier" is not a defense for a forecast leak when the text stays retrievable in the journal. CR-F9 should cover any copy a player can reopen while the forecast is open.
- **Lesson:** a goal tag on an entry claims a measure; orientation text (about, sol) should be tagged "none".
- **Lesson:** a tutorial line that says "while you do X" must match the real clock or trigger (grace is a clock).
- **Proposed rule note for content-rules CR-F9:** add "including journal-retrievable copies". Owning doc: `docs/learning/content-rules.md` (mine; not changed here, awaiting your accept).
