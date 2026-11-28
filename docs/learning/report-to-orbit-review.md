# Report to Orbit: learning review

Owner: Lead Instructional Designer. Date: 2026-11-28. Status: Draft 1, DESK REVIEW, no learners involved. Every statement about effect is a design prediction. Saved by the coordinator from the Instructional Designer subagent's hand-back text (the subagent's file write was refused by the harness); the owner accepted the recommendations on 2026-11-28.

Reads: LEARNING.md (0.6, 4.8), content-rules.md, prompts-and-reflections.md section 2, gate-input-items.md section 7, messages/phase-1.md sections 9 to 10, ngss-verification.md. Written before `docs/design/report-to-orbit.md` existed; the checklist in 5.2 is the test for its templates.
Practice names (by name only, as in ngss-verification.md): Analyzing and interpreting data (4), Constructing explanations and designing solutions (6), Engaging in argument from evidence (7), Obtaining, evaluating, and communicating information (8). Numbering not re-fetched (unverified). The report is a practice, so it adds no new performance expectation; PE codes stay as in LEARNING.md section 6.

## 0. Three things to settle first (owner took the recommendations)
| # | Issue | Why | Recommendation |
|---|---|---|---|
| 1 | Overlap with Keeper's Reports and Mensah's replies | Two reports at one gate doubles the load and teaches players to skip | One screen, two layers: structured picks at every gate; the Keeper's Report question is the optional free-text line at the 4 gates that have one; the orbit reply and Mensah's reply are one message |
| 2 | "ISS" is wrong for Mars | False science in a science game; feeds an A1 misconception | "Relay" or "mission control" as sender; ISS only as the labeled Earth-side science desk |
| 3 | Is the reply a gate? | If quality blocks, it becomes a quiz and breaks LEARNING.md 4.9 and story rule 1 | Filing may gate the briefing and drop; quality never does; skip path after a fixed wait |

## 1. How the report carries learning
### 1.1 Structure (claim, evidence, reasoning)
| Part | Player does | MS | HS | Practice |
|---|---|---|---|---|
| Claim | Picks what changed the planet since the last gate | 1 of 3 sentences | 1 of 4 (one plausible but wrong cause), or one typed line | Constructing explanations and designing solutions |
| Evidence | Picks lines from the report page (before/after with arrows, Monitor cause lines) | 1 line from 4 to 5 (2 off-topic) | Up to 2 from 6 to 7 (one is the side effect) | Analyzing and interpreting data |
| Reasoning | Picks why that evidence supports that claim | 1 of 3 "because" sentences | One pick, plus an optional "what else could explain it?" | Engaging in argument from evidence |
| Send | Optional one-line note | Sentence starter, fading | None | Obtaining, evaluating, and communicating information |
Learning-science names: claim-evidence-reasoning scaffold (McNeill and Krajcik; citation unverified), retrieval practice, self-explanation, expecting to teach (already in LEARNING.md section 2), fading. Picks come first because they take a minute, score themselves and suit a 12-year-old reader. Typing stays optional in the Keeper's Report layer, where the D1 and D2 rubrics apply.

### 1.2 What each gate's report serves
The claim is retrospective (about a change already visible), so it is not a forecast.
| Gate | Claim is about | Evidence from | Goals | Practice emphasis | Note |
|---|---|---|---|---|---|
| Dome stable | What kept life support up, or almost failed | Power and demand lines; the failure that happened | C1, C2, C3, A1 | Analyzing and interpreting data | Keeper's Report 1's question fits here better than at Phase 2 begins |
| Phase 2 begins | What raised pressure to 5 kPa | Pressure plot; Outgasser contribution line (CR-F10: attributed line, not net) | B1, C3, A1 | Analyzing and interpreting data | First report is nearly fully scaffolded |
| Thaw | Why temperature rose, or what holds it | Temperature, greenhouse and frost lines | A3, B1, A2 | Constructing explanations | A3 needs a greenhouse source by this gate |
| Phase 3 begins | What had to rise together (pressure and warmth) | Both lines before and after | A2, B1, B2 (HS: the loop) | Engaging in argument from evidence | Touches the water idea (F4a, F4b) without answering them |
| Hydrosphere | Why water now stays liquid | Pressure, temperature, hydrosphere lines | A2, B1 | Constructing explanations | Mensah's Everest contrast fits the reply |
| Breathable | Why the air is breathable | O2, CO2, pressure lines | A5, B4, B1 | Analyzing and interpreting data | HS: partial pressure, not percentage alone |
| Equilibrium | What was given up to hold balance; what could drift it back | Six lines plus the side effect | B4, B2, B5, D1, D2 | Engaging in argument from evidence | Report 3's question lives here |
D2 lives in the reply's Earth line, not the form. D1 stays in the Keeper's Report 2 and Final free-text layer, where the rubric is.

### 1.3 No hand-holding, no leaked forecast answers
- Any option or reply text that could reveal an open forecast's answer declares `reveals` (CR-F9). Check F4a and F4b first: they can fall between Thaw and Hydrosphere.
- No future-tense claims. The next briefing says where to look, not the reply.
- Evidence lines use attributed contributions (CR-F10), so a report stays true if the player cleaned panels or used a battery.
- Distractors are real variables or real causes of another change. Silly options teach nothing and invite elimination.
- No score and no right-answer mark. The player sees a fit sentence only.

## 2. Reply feedback design
### 2.1 Principles
1. Natural consequence first: the drop and briefing arriving is the consequence. 2. At most 40 words (invented budget), naming one thing the player did. 3. Positive wording (CR-T5). 4. Never blocks, never lowers the drop. 5. Process feedback for new gate topics, outcome feedback for familiar ones (LEARNING.md 4.8).

### 2.2 Anatomy
| Part | Words | Content | Always? |
|---|---|---|---|
| Receipt | 3 to 6 | "Report received, Keeper." plus the drop line | Yes |
| Fit line | 8 to 15 | Names the player's evidence pick and how it fits or what it leaves open | Yes |
| Earth or look-again line | 10 to 20 | Tier 1: one Earth link (D2). Tier 2: "A second look:" plus one question pointing at a Monitor line. Tier 3: the same, plus an offer to reopen the report page | Yes |

### 2.3 Tiers (data-keyed, not a judgment)
Each gate's JSON keys which claim, evidence and reasoning combinations are fit, partial or weak.
| Tier | Pattern | Reply |
|---|---|---|
| 1 | Claim, evidence and reasoning agree | Confirms with the player's own line, plus the Earth line |
| 2 | Evidence supports a different claim, or reasoning doesn't tie them | Says what the evidence does show, then one look-again question |
| 3 | Evidence unrelated to any claim | Short and kind, one question, "See your report page" |
Fading, per player (LEARNING.md 4.8):
| Reports | Level | Rule |
|---|---|---|
| 1 to 2 | Fit line, Earth or look-again, and a pointer ("Check: pressure on the Monitor") | New content gets process feedback |
| 3 to 5 | Fit line, plus look-again only on tier 2 or 3; no pointer | The player's own tier-1 count moves the level |
| 6 to 7 | Receipt and one line; full text one click away | Expertise reversal |
| Any time | Two tier-2/3 reports in a row bring the pointer back next time | Re-scaffold on error; logged as `scaffold: pointer` |
The Earth line shows at every gate the first time, then only on tier 1. It stays in the journal archive.

### 2.4 Weak claim, skip, groups
| Situation | Behavior | Reason |
|---|---|---|
| Weak or partial pick | Unlocks as normal; reply nudges; reopening is optional and never changes the drop | A weak pick is evidence of a misconception, not a fault |
| Reopens and changes a pick | The first filing is the one analyzed (as CR-R3); the second is logged `revised` | Keeps first-attempt data honest |
| Skips ("Later") | Briefing and drop arrive after a fixed wait (2 sols, invented); the reply says "No report yet. Here is your drop." | The report must never be a hard gate |
| Group | One drop and one briefing per planet, released when any player files or the wait ends. Each player files and gets their own reply | Avoids one player blocking the group; individual evidence per LEARNING.md 3.0 |
| Forecast open | The form opens; wording stays clear of the forecast's answer | CR-F9 |

## 3. Assessment evidence list
One `report.filed` event per report, plus `report.skipped`, `report.revised` and `report.reply_opened`. Actor is the pseudonymous ID only (ARCHITECTURE.md section 8; CR-R4). Nothing leaves the server's computer. No data exists yet.
| Field | Meaning | Use |
|---|---|---|
| event | `report.filed`, `report.skipped`, `report.revised`, `report.reply_opened` | Counts filed vs skipped |
| actor | Pseudonym | Individual evidence |
| gate | Gate id | Per-gate analysis |
| report_version | Template and option-set version | Compare after edits |
| band | MS or HS | Which options were shown |
| claim_id, evidence_ids[], reasoning_id | Option IDs, never text | Core evidence for B1, A-goals, C3; misconception patterns |
| tier | 1, 2 or 3 | Formative signal, never a grade |
| scaffold | `pointer`, `starter`, or none | Separates scaffolded picks (4.8) |
| sol, session | Simulation time, not wall-clock | Ordering; survives pause (I-3) |
| active_seconds | Form open time, capped (e.g. 300) | Spots speed-clicking; never shown to the player |
| edits | Pick changes before sending | Weak deliberation signal |
| free_text | Boolean and word count only; text goes to reflections.csv if on, nowhere else | Privacy (CR-R4) |
| forecasts_open | Count of this player's open forecasts when filed | Verifies the CR-F9 lock in the log |
| reply_opened, earth_line_opened | Whether the player opened them | Reach and interest |
| drop_claimed | Whether the drop was picked up | Not learning evidence |
Allowed uses: formative counts, hint fading, the overview. Not allowed: a grade, a ranking, any name. A tier is individual evidence and is never aggregated into a group score (3.0). Formative-evaluation triggers (invented thresholds, to calibrate on the first playtest): a distractor chosen by more than about one third of players at a gate is a misconception candidate or an unclear option; tier 3 above about one quarter means the gate's teaching or options need work; a skip rate above about one third means fatigue. Reports never replace the post-test, and B3 stays the forecast measure.

## 4. Risks and version-1 responses
| Risk | Cause | Version 1 response | Signal |
|---|---|---|---|
| Report fatigue (7 reports in about 40 h, plus 4 reflections) | Same form each gate | One screen; picks not typing; options change per gate because they come from that gate's own numbers; free text optional; reply shrinks with fading | Skip rate per gate; active_seconds near 0 |
| Guessing | Pick forms are easy to click through | Plausible distractors; option order shuffled per player with a saved seed; off-topic-but-real evidence lines; reasoning tied to the evidence pick; no score | Position bias in claim_id; active_seconds under about 5 |
| Copying | Shared screens | Options use the player's own numbers; logged per actor; no points, so low payoff | Identical pick strings across actors with different values |
| Answer-giving replies | Reply wants to teach | Tier 2 and 3 name what the evidence shows and ask a question; never state a forecast answer or the fix; `reveals` and banned tokens | Checklist 5.2 |
| Report becomes a quiz | "Unlock the quest" feels like a test | Quality never blocks; skip path; same drop for all; no score and no "wrong" | A playtester says "I have to get it right" |
| Boilerplate replies | 7 gates times 3 tiers is too many texts | Version 1 writes tiers 1 and 2 for 3 gates (Dome stable, Phase 2 begins, Phase 3 begins) plus a generic tier 3, then extends | Playtester notices repeats |
| False science from fiction | ISS name, resupply from orbit | The Earth-side desk label; the drop uses items already in gate-input-items.md | SME review |
| Reading load | 3 picks plus a reply | Grade 6-7 text; one-sentence options; replies at most 40 words | 5.1 screen; read-aloud (not done) |
| Stale keys after a physics change | Same cause as forecast keys that broke before | Option keys are data. Proposed rule CR-O1: every report option's fit key matches current gate data; prove it can fail by changing a gate threshold in a test datapack | Technical Lead feasibility |

## 5. Example replies and the template checklist
### 5.1 Three example replies (MS text; HS adds a "Read more" with numbers)
Drafts; the Earth and science lines still need SME verification. Readability: a rough Flesch-Kincaid formula with a simple syllable counter, a screen and not proof. The grades come out very low (1 to 3) because the sentences are very short, and the formula cannot see word difficulty. The read-aloud test with playtesters was NOT done.
| # | Where | Text | Words | FK screen |
|---|---|---|---|---|
| R1 | Dome stable, tier 1 (fits) | "Report received, Keeper. Your claim fits your numbers. The dome held because power stayed above demand. That is the line to watch next time a storm is coming." | 28 | 3.2 |
| R2 | Any gate with a pressure pick, tier 2 (partly fits) | "Thanks, Keeper. You picked the pressure line as your proof. Good pick. A second look: did temperature change too? Check the Monitor and see if your claim still holds." | 29 | 2.1 |
| R3 | Any gate, tier 3 (weak) | "Report received. Your pick is a start, and the supply drop is on its way. Look at the two lines on your report page. Which one moved most? Tell us what you see." | 33 | 1.3 |
R1 has no Earth line, because gate 1 already carries a lot to read; the Phase 2 begins tier-1 reply should use the Everest line from `earth.report1.reply`. R3 puts the drop in the second sentence, so a weak pick is never a punishment, and it does not say "wrong". None of the three uses a CR-T1 banned word or states an open forecast's answer.

### 5.2 Checklist for the Game Designer's templates
1. Every claim, evidence, reasoning and reply text has goal_ids, and NGSS codes resolve through ngss_map.json (CR-F6; DOCUMENTATION.md section 3). 2. No text gives the answer to a forecast open for that player; any text that could carries `reveals` (CR-F9). 3. A one-cause claim uses `resolve_source` evidence, not net change (CR-F10). 4. The reply never blocks or changes the drop or briefing; quality affects wording only. 5. A skip path exists and delivers the same drop after a fixed wait. 6. Positive wording only: no "wrong", "failed", "you forgot", "incorrect", "mistake" (CR-T5). 7. No banned player words: learner, lesson, objective, quiz, test, score (CR-T1). 8. Reply at most 40 words; each option at most 20 words (invented budgets). 9. Grade 6-7 text: FK grade at most 7 plus a playtester read-aloud before it is called final (CR-T6). 10. US spelling; no "~" or "<" where a word fits (CR-T7). 11. Every distractor is a real variable or a real cause of another change. 12. Option order is shuffled per player by a saved seed, and the keyed fit does not depend on position. 13. Tier 2 and 3 replies name what the evidence shows and ask one question; they never state the fix. 14. Each gate has tiers 1 and 2 written, and a generic tier 3 exists. 15. Every science line (numbers, Earth link) is sourced, marked verified or unverified, and SME-checked. 16. Any fiction (relay, ISS, drop) passes the six rules in DESIGN.md and is in the audit table. 17. Drop contents match gate-input-items.md; nothing new is invented in the report doc. 18. Logging uses only the section 3 fields: no text, no name, no UUID (CR-R4; ARCHITECTURE section 8). 19. An open forecast restricts the wording but does not prevent filing. 20. The goals in the placement table (1.2) match the alignment table in LEARNING.md.

## 6. Alignment impact (desk check)
No goal gains or loses coverage. The report adds practice and feedback entries for B1, C3, A2, A3, A5 and B4, and a formative measure for B1 and C3. It replaces no post-test measure. Edits to LEARNING.md alignment rows wait for the owner's go. Not done: any playtest, any read-aloud test, verifying the practice numbering. All thresholds in sections 3 and 4 are invented.

## 7. Handoffs
- Lead Game Designer: fold or replace Keeper's Report 1, 2, 3 and Final with the report screen's optional free-text layer; move Report 1's question to Dome stable; place each gate's tier-1 and tier-2 replies; check that A3 has a source by Thaw. Done when the template passes checklist 5.2.
- SME: verify Earth lines (Everest, the "more than 100 times" line), decide the relay and ISS naming, confirm distractors are real variables.
- Technical Lead: feasibility of the `report.filed` event and CR-O1; per-player shuffle seed; skip-wait timer using `story.delayScale`.
