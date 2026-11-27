# Paper Prototype: The First Dust Storm (Stage 2a)

**Owner:** Lead Game Designer · **Status:** Draft 1 (2026-11-26), ready for the joint pass with the Lead Instructional Designer and Lead Artist · **Experience spec:** DESIGN.md, "First experience to design in detail"; art and audio storyboard: [keyframes §3.7a](../../art/planetary-keyframes.md) · **Text:** [messages/story-beats.md](../messages/story-beats.md) beat 4, [phase-1.md](../messages/phase-1.md) F1

**Goals:** C1 (plan for a power cut), B3 (predict, then check), B1 (cause line on the Monitor), C2 (if something fails).
**What this prototype must answer:** Does warning → forecast → storm → recovery read as one experience? Does any cue give away F1 before it locks? Can a Lens A player prepare in time, with only the in-game information?

**Method:** a facilitator runs the sheets with a tester (the user, or an adult playing Lens A). Paper cards stand in for screens; a kitchen timer stands in for sols (**1 sol = 4 real minutes** on paper, so the whole run fits in ~15 minutes). Straw-man materials only: index cards, a printed Monitor, sticky notes. No art.

---

## Sheet 1: Materials

| Item | Paper stand-in |
|---|---|
| Monitor (Dome tab) | Printed card: Power **42 %** with a trend arrow, battery **3 bars of 6**, solar output **"full sun"**, O2 reserve, a cause-line box |
| HUD | Strip of card: suit O2 bar, storm icon slot with a countdown box |
| Side panel | Column of sticky notes, one visible at a time |
| Sky | Three colored cards: clear tan, hazy tan (fog only), storm brown |
| Forecast card F1 | Index card: question, three choices, "Later" |
| Machines | Cards: 2 solar panels, 1 battery, O2 generator, Regolith Oven; each with a "Costs" line (power per sol) |
| Actions the tester may take | Action cards: build a battery (2 min), switch off the Oven, clean panels, store power, do nothing |
| Result card | F1 result text (right / wrong versions) and the Monitor graph with the cause callout "Dust on panels −60 %" |

## Sheet 2: Timeline (facilitator script)

| Step | Paper time | Facilitator does | Tester sees | Lock-point check |
|---|---|---|---|---|
| 0. Baseline | 0:00 | Tester runs the dome for 2 minutes | Clear sky card, Monitor | — |
| 1. Warning | 2:00 (T−1 sol) | Place the storm icon (countdown "~1 sol"); top-line note "Dust storm forming. Arrival in about 1 sol. Expected length: about 2 sols."; swap to the **hazy** sky card (fog only; sun as bright as before) | Warning, haze | Haze is fog only; **solar output unchanged**. Warning doesn't mention solar |
| 2. Forecast | Next natural pause (tester opens the Monitor or "enters the dome") | Show the F1 card **before** the Monitor if they open it (look-first rule) | F1 with "Check: solar power on the Monitor's Dome tab." | Unanswered at onset = expired, not wrong |
| 3. Preparation | 2:00–6:00 | Let the tester play action cards; note each with time | Costs lines | Record whether any life-critical action came first (C1 criterion) |
| 4. Onset | 6:00 | F1 locks. Swap to the **storm** sky card. Set solar to 40 % (60 % cut × cap 1.0) | Storm | Lock happens before solar drops |
| 5. Power drop | 6:00–14:00 (2 sols) | Move the battery bars down by the tester's demand; if it hits 0, show the `power_shortfall` failure card (+ Hana's tip, first time) | Battery falling; cause line "Dust on panels −60 %" | — |
| 6. Clearing | 14:00 | Swap to clear sky; panels stay "dusty" until cleaned | Clear sky | — |
| 7. Result | Next natural pause | F1 result first in the queue; Monitor graph with the dip | Result and graph | — |
| 8. Debrief | After | Questions below | — | — |

## Sheet 3: Observation (fill during the run)

| Time | What the tester did or said | Question asked out loud (word for word) | Stuck > 1 min? | Looked at the sky? Monitor? | Note |
|---|---|---|---|---|---|
| | | | | | |

Also note: the time from warning to first preparation action; whether the tester connected haze to power **before** the forecast (good observation, not a leak, as long as solar hadn't dropped); whether "Later" was used and whether F1 expired.

## Sheet 4: Debrief questions (tester, after the run)

1. Before the storm hit, what did you think would happen to your power? Why?
2. What did you do to get ready? Which one mattered most?
3. Where did you see the cause of the power drop?
4. Did anything tell you the answer to the forecast before you chose? What?
5. Was there a moment you didn't know what to do?
6. Fun 1–5. What would make it more tense? Less confusing?

## Sheet 5: Pass criteria

| Check | Pass when |
|---|---|
| One experience | Tester describes warning → storm → recovery as one event in their own words |
| Lock point | No tester says the forecast was "obvious from the warning"; solar never dropped before lock |
| C1 | A life-critical action (battery, cutting a non-critical load) happens before onset |
| B1 | Tester points to the cause line without help |
| Text | Every line read in under 10 seconds; no word above grade 6–7 |
| Joint-pass outputs | Decisions on: haze strength, storm length, whether the Shift Board slip appears for a near-miss |

## Sheet 6: Root cause of anything that goes wrong

Use the root-cause sheet in [playtest-plan.md](../playtest-plan.md): interface? gameplay–instruction conflict? wrong expertise level? science or text?

**Caution:** a paper run with an adult is design judgment, not learning evidence (playtest plan).
