# Forecasts, Reflections, Earth Links, and Keeper's Record

**Owner:** Lead Instructional Designer · **Status:** Draft 1, 2026-11-26 · **Depends on:** [LEARNING.md](../../LEARNING.md), [story bible](../design/story-bible.md), [science review](../science/accuracy-review.md)

This file specifies the learning content that lives in the game: what each forecast, reflection question, Earth link, and Keeper's Record page must do. The Lead Game Designer writes the final in-game wording in the story bible's voices; the SME checks every science claim; I review the result against the goal and criterion.

Every entry here becomes an entry in the shared feedback format (DOCUMENTATION.md 5.1) when it's built. Fields not listed here (`id`, `delivery`, `motivational_purpose`, `status`) are filled in by the Lead Game Designer and Technical Lead.

**Status of science content:** all sample answers and explanations are **draft, SME check needed** unless marked otherwise. Nothing here may ship as fact without that check.

---

## 1. Forecasts (prediction prompts)

**How forecasts work** (LEARNING.md 3.2, 4.6):
- BELL asks: "Log your forecast." One question, 2–4 choices.
- Each player answers on their own screen. Other players' answers stay hidden until the forecast resolves.
- Issued at least ~1 sol (active preset) before the outcome, so it can wait for a natural pause.
- Expires, unscored, when the outcome becomes visible.
- The result is shown at the next natural pause, first in the queue: right or wrong, plus a 1–2 sentence explanation either way.
- At least 3 forecasts per phase, so the B3 criterion (own accuracy per phase) has enough data.
- HS variant adds a "why" follow-up choice. Only the main question counts toward B3; the "why" counts toward the goal's Keeper's Record page.

| ID | Phase / trigger | Question (MS) | Choices (correct first here; shuffle in game) | HS "why" follow-up | Goals | Notes |
|---|---|---|---|---|---|---|
| F1 | P1 / first dust storm warning (story beat 4) | "A dust storm reaches the dome in about 1 sol. What will happen to your solar power?" | It drops · It rises · It stays the same | "Why?" Dust blocks sunlight · Wind turns the panels · Cold makes panels stronger | B3, C1 | Pair with BELL's storm forecast. Avoid any claim about damaging wind (DESIGN.md fantasy audit: Mars storms can't do that) |
| F2 | P1 / first solar event, ~1 sol ahead (story beat 5) | "A solar event is coming. Where will a Keeper get the smallest radiation dose?" | Inside, under thick shielding · Out on open ground · On top of a hill · Next to a running machine | "Why does Mars's surface get more radiation than Earth's?" Thin air and no global magnetic field · Mars is closer to the Sun · Red soil gives off radiation | A4, B3 | "Closer to the Sun" is a deliberate misconception distractor (Mars is farther) |
| F3 | P1 / first pressure source running (depends on consistency review C-01) | "Your [pressure source] is adding gas to the air. What will the Monitor show for pressure over the next sol?" | Pressure rises slowly · Pressure jumps to Earth's level · Nothing changes | "Why so slowly?" A planet's air is huge compared to one machine · The machine is broken · Gas leaks to space at once | A1, B1, B3 | Wording waits for the Lead Game Designer's Phase 1 pressure source |
| F4 | P2 / temperature near an extractor first above 0 °C while pressure is low (story beat 9, Thaw) | "It's above 0 °C near the extractor, but the air is still very thin. What will the ice do?" | Turn to vapor (or melt and quickly boil away), with no puddles · Melt into puddles · Stay frozen | "Why?" Low pressure lowers water's boiling point · Ice on Mars is not water · It's still too cold | A2, B3 | SME wording for the counter: accuracy-review.md Part D |
| F5 | P2 / first greenhouse emitter switched on | "You start the greenhouse emitter. What will the surface temperature do over the next few sols?" | Rise slowly · Drop · Stay the same | "Why?" Its gases trap heat that would escape to space · It blocks sunlight · It burns fuel that heats the air directly | A3, B3 | Emitter makes factory-made greenhouse gases, not CO2 (SME B4) |
| F6 | P2 / player count of active emitters and extractors passes a threshold after first fauna (Hydrosphere period) | "You add three more extractors. What will happen to thawling activity near your machines?" | It goes up · It goes down · No change | "What kind of loop is this?" Reinforcing: more machines bring more fauna, which damage machines, so you build more · Balancing · Not a loop | B2, B5, B3 | Fiction: thawlings. Explanation must say the behavior is invented, the loop type is real |
| F7 | P2 / first solar event after pressure has at least doubled since F2 | "The air is much thicker than at your first solar event. What will this one do at the surface?" | Less radiation reaches the ground · More · The same | "Why?" More air means more matter to absorb radiation · Thicker air reflects the Sun · Fauna block it | A4, B3 | Spaced retrieval of F2. Depends on DESIGN.md "spikes weaken as the atmosphere thickens" |
| F8 | P3 / first large flora seeding | "You seed a large field of flora. What will happen to O2 and CO2?" | O2 rises, CO2 falls · Both rise · Both fall | "Why?" Plants take in CO2 and give off O2 · Plants make CO2 at night only · Soil releases O2 | A5, B3 | — |
| F9 | P3 / O2 rising toward the top of its band | "O2 keeps rising. What risk is coming?" | Fires start more easily · The air gets too thin · Plants stop growing | "Why?" More O2 makes things burn more easily · O2 is poisonous at any level · O2 makes the air heavier | A5, B4, B3 | Fire-safety band 19–23% (SME B2) |
| F10 | P3 / player turns emitters down (or upkeep fails) during the equilibrium push | "Your emitters are turned down to save power. What will temperature do over the next sols?" | Fall slowly · Rise · Stay the same | "What's the trade-off?" Less power used, but the planet cools and water may freeze · No trade-off · Less fauna, nothing else | B4, A3, B3 | — |

Count per phase: P1 3, P2 4, P3 3.

**Stage 3 vertical slice:** F1 (and F2 if the radiation spike is in the slice).

---

## 2. Keeper's Report reflections

One question per report. The player's answer is part of the report sent to Earth (story bible 6.4). Answers are individual evidence, scored with `rubrics.md`. In classroom mode the teacher expects an answer; the game never blocks progress for it. Deferral rules: LEARNING.md 4.6.

| Report | Gate | MS question | HS question | Goals | Rubric |
|---|---|---|---|---|---|
| 1 | Phase 2 begins | "What almost failed in the dome, and what would you do differently next time?" Sentence starter: "The ___ almost failed because ___. Next time I would ___." | Same, plus: "What did the Monitor show that helped you find the cause?" | C2, C1, C3 | Not rubric-scored; C2 and C3 evidence for the Keeper's Record and teacher check-in |
| 2 | Phase 3 begins | "Was waking the thawlings worth it? Give one reason from what you saw." Starter: "I think ___ because I saw ___." | "Was waking the thawlings worth it? Give your reason, and the strongest reason someone could give against you." | D1, B5, B2 | D1, B5 |
| 3 | Equilibrium | "To hold the balance, what did you have to give up? Was it worth it?" | "A colonist claims: 'Once the planet is balanced, it will stay balanced.' Is that claim right? Use what happened on your planet as evidence." | B4, B2 (HS: HS-LS2-6 claim evaluation) | B4 is scored by post-test; Report 3 is formative |
| Final | Beacon lit (story bible 6.5) | "The ships are coming. What should the colonists know before they land?" | Same, plus: "What does your planet teach about Earth?" | D1, D2 | D1, D2 |

**Changes from the story bible's draft:** "Was waking them worth it?" now asks for a reason from what the player saw, so it can be scored as argument from evidence (D1 rubric). The HS versions ask for a counter-reason or a claim evaluation (NGSS: argument from evidence; HS-LS2-6).

**Group play:** every player gets their own copy of each report question. A player who was absent when the gate fired finds it waiting ("Since last shift: report not sent").

---

## 3. Earth links (D2)

Every gate journal entry ends with an "On Earth" line (story bible 6.3). Mensah's reply to each Keeper's Report is the longer Earth link. Each link must name **one real Earth mechanism that matches what the player just did on Mars**, so D2 is about transfer, not trivia.

| Where | Mars experience | Earth link must teach | Goals / NGSS (6.1) | SME check needed for |
|---|---|---|---|---|
| Gate: Dome stable | Keeping air in, life support | Earth's thick air protects us for free; Mars's air pressure is less than 1% of Earth's | A1, D2 | Ratio wording |
| Report 1 reply (Phase 2 begins) | Pressure rising; water still can't stay liquid | On Earth, thick air keeps puddles and oceans liquid; water's boiling point drops on high mountains | A1, A2, D2 | Mountain boiling example |
| Gate: Thaw | Ice turns to vapor | Same physics on Earth: freeze-dried food and frost that vanishes without melting (sublimation) | A2, D2 | Examples |
| Gate: first emitter (F5) | Adding greenhouse gas warms the planet | Earth's greenhouse gases (CO2, methane, water vapor) trap heat; the greenhouse effect is **not** the ozone hole | A3, D2; MS-ESS3-5, HS-ESS2-4 | All |
| **Report 2 reply (Phase 3 begins)** and class debrief period 14 | Warming had side effects; reinforcing loop with fauna | **Evidence of Earth's warming over the past century**: CO2 and temperature records, with human activities as the main cause; a real reinforcing loop (thawing permafrost releasing stored carbon, or ice–albedo); Earth's emissions as a side effect, not a goal | A3, B2, B5, D2; MS-ESS3-5, HS-ESS2-4, HS-ESS2-2 | All. The classroom debrief needs a real CO2 and temperature graph from a named source (MS-ESS3-5 condition) |
| Gate: first flora | Plants take in CO2 and give off O2 | Earth's oxygen comes from photosynthesis on land and in the ocean | A5, D2 | Ocean share, if a number is used |
| Report 3 reply (Equilibrium) | Holding a band; balancing loops | Earth's carbon cycle: air, plants, soil, ocean; balancing loops that keep it steady, and how human activity is shifting it | A5, B2, D2; HS-ESS2-6, HS-ESS3-6 | All |
| Ending, Mensah's last line | Building a planet by hand | What Earth can learn: we already change a planet; we can do it with a plan | D2, D1 | Tone check |

**Rules for Earth links**
- Fiction stays on the Mars side. An Earth link never implies the thawlings are real, and never pairs them with real Earth organisms without a clear "this part is real" label (accuracy-review.md Part D).
- Plain causes, no blame: say Earth's warming is mainly caused by human activities (the scientific consensus the SME's sources support), framed as a side effect, which is the B5 lesson. Suggested edit to the story bible sample: "We did it by accident" → "We did it without meaning to, as a side effect of burning fuel." (Lead Game Designer's call; SME to check.)
- One Earth idea per line. "Read more" (HS) can add a second example or a number.

---

## 4. Keeper's Record pages

Header: *"What the colonists will need to know."* One page per goal. A page fills from the player's **own** evidence only (LEARNING.md 3.0). The "Cairn team" section lists group milestones separately.

| Goal | Page title (colony language, draft) | Fills when the player… (any one) |
|---|---|---|
| A1 | How Mars is different from home | Gets F3 right, or a "What caused it?" check on a Phase 1 hazard right |
| A2 | Why water won't stay liquid here, and what would change that | Gets F4 right (HS: and its "why") |
| A3 | How to warm a planet | Gets F5 right (HS: and its "why") |
| A4 | Staying safe from the Sun's storms | Gets F2 or F7 right |
| A5 | Plants, air, and keeping the balance | Gets F8 or F9 right |
| B1 | What changes what | Gets 3 "What caused it?" checks right |
| B2 | Loops that grow and loops that settle | Gets F6's "why" right (HS), or answers Report 2 (MS) |
| B3 | Reading the planet ahead | Own forecast accuracy in the current phase beats the previous phase |
| B4 | What it costs to hold the balance | Gets F10 right and answers Report 3 |
| B5 | When a fix causes a new problem | Answers Report 2 |
| C1 | Getting through a storm | Gets F1 right, and placed or refueled life support before the first storm |
| C2 | Fixing it so it stays fixed | Own repair of a failure type with no repeat from the same cause that phase |
| C3 | Checking the data first | Opened the Monitor before at least half of own major builds (minimum 3 builds) |
| D1 | Sharing a planet | Answers Report 2 or the final question |
| D2 | What Mars teaches about Earth | Answers the final question, or opens 3 "On Earth" read-more pages |

A filled page shows the player's own words where there are some (report answers), which supports the "writing for the colonists" frame. Pages for answering a report fill on *answering*; quality is scored by the teacher, not the game.

**Framing response (story bible handoff):** accepted. "What the colonists will need to know" turns the record into something written for others, which fits the expecting-to-teach effect (LEARNING.md section 2; citation unverified) and keeps player language. The system and team term stays "Colony Record"; the in-game title is "Keeper's Record".

---

## 5. Reading level and grade-band rules

| Rule | Detail |
|---|---|
| Main text level | Grade 6–7 for all main text, both bands. Short sentences, one idea per sentence, common words; a science word is defined the first time it appears |
| Check | Every text gets a readability-formula check (e.g. Flesch–Kincaid grade) before ID review, plus a read-aloud check with playtesters. A formula is a screen, not proof |
| Length budgets | As in story bible 6.1 (log line ≤ 20 words, failure report ≤ 35, journal ≤ 80 + read more, message ≤ 60 + read more). Forecast question ≤ 25 words; each choice ≤ 10 words; result explanation ≤ 40 words |
| Reading-time planning rate | Plan at **100 words per minute** for students in this age range. This is a deliberately conservative design assumption to cover weaker readers, **not** a research norm; published fluency norms were not checked in this session (**unverified**). At 100 wpm, the story bible's ~780 words of essential classroom messages take about 8 minutes, inside its 15-minute budget |
| MS band | Main text only; one "On Earth" line; sentence starters on reflection questions; forecasts without the "why" follow-up |
| HS band | Same main text; "read more" adds numbers and a second Earth example; "why" follow-ups on forecasts; counter-argument or claim-evaluation reflection questions; Monitor shows carbon per reservoir (if built) |
| Never band-gated | Safety warnings, fiction notes, and the core explanation of each forecast result appear in both bands |
| Accessibility | Nothing time-limited; everything replayable from the archive; key information also shown visually |
