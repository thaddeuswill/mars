# Instructional Designer: Monitor wording, plot goal line and open items, 2026-11-27

Type of work: desk review of the language file (`pack/assets/mars/lang/en_us.json`), specs and prior reports. Readability screened with a rough Flesch-Kincaid script (syllable heuristic; a screen, not proof). No real learners, no game run. Docs only.

## 0. Handoffs received
| From | Handoff | Answer |
|---|---|---|
| Game Designer (plot-scale report) | Grade 6-7 check of plot keys; NGSS practice names; opinion on decision 2 | Accepted, answered in 1 to 3 |
| Technical Lead (slice 5 report) | Wording check of slice 5 keys | Accepted, section 1 |
| Technical Lead (slice 4 report) | Dome stable journal entry and its "On Earth" trigger | Accepted, section 4b (text exists; trigger proposed) |
| Game Designer (slice 5 decisions) | Landing step 6 "battery and Ovens" | Accepted, section 4c |
| Artist / SME | Rose-sky label before the player sees it | Accepted, section 4a; trigger is the Game Designer's call |

## 1. Done
Edited: `docs/design/ui-specs.md` (new section 9.2 with final wording; 9.1 heading updated), `docs/learning/content-rules.md` (new CR-T7, CR-T8). This report.

### 1.1 Reading level and content rules
Every key screens below grade 7 (computed: plot goal -4, rescaled 0.5, caption 3.7, frost read-more 5.3, levelling off 4.2, oven tooltip 6.4, grant.starting 1.7). Short labels give meaningless scores, so the real findings are about vocabulary and misreading:

| Key | Verdict | Problem | Final wording (also in ui-specs 9.2) |
|---|---|---|---|
| `monitor.plot.goal` | Pass for Phase 2; fails for Phase 3 and Breathable | "Phase 3 begins: 30 kPa" says pressure alone opens the gate; it also needs 0 °C | Keep "%s: %s" for pressure-only gates; new `monitor.plot.goal.partial` "Pressure goal: %s" |
| `monitor.plot.rescaled` | Replace | "squashed" reads as damage, and nothing says the pressure did not drop. That misreading is the main risk of option D | "The graph now goes up to %s. The line looks lower, but the pressure did not drop." |
| caption | Pass | Match the row label "Air pressure" | Keep; only if room |
| `monitor.goal.eta*` | Change | "~" and "<" are symbols, not words, for a 12-year-old | "about %s sols", "about 1 sol", "under 1 sol" (keep "~" only if the panel is too narrow) |
| `monitor.goal.not_rising` | Pass | Short; cause lines below say why | Keep |
| `grant.starting` | Change | "pack" collides with "modpack"; "x" | "You have %s × %s in your inventory. Place them, then check Power on the Monitor." |
| `monitor.cause.polar_frost` | Pass, with a risk | "Frost" suggests water frost on Earth, feeding a Phase 2 water misconception | Keep; option "Polar CO2 frost" if the panel fits (Game Designer). Read-more fixes it either way |
| `monitor.cause.polar_frost_release` | Pass | Helper must print "+" | Keep |
| `monitor.estimate.levelling_off` | Change | British spelling for a US-standards audience | "Leveling off: ..." (key id unchanged) |
| `.levelling_off.hs` | Change | "The two" has no referent | "Frost and machines balance near %s. More machines move that balance up." |
| `monitor.oven.tooltip` | Pass | "Water comes later" is a promise | Keep; delete if Oven water is cut |
| `monitor.event.placed.regolith_oven` | Pass | none | Keep |

Content rules: CR-T1 and CR-T5 pass (no banned words, all positive). CR-F9: the one real problem is the time-to-goal line, which hands over the answer to B3-type pressure forecasts (see 2). New rules CR-T7 (US spelling, no symbols for words, length budgets, which are *invented*) and CR-T8 (estimate lines respect open forecasts and `guidance`). CR-F10: the frost line and its read-more resolve no forecast that I know of; this is **unverified**, and the Technical Lead should declare it in the `resolve_source` check.

## 2. Opinion (ill-structured, one perspective): goal line from sol 0, and the Phase 3 case
**Frame.** Guidance (show the target) against progressive disclosure (reveal it when relevant). Needs Game Designer (appeal, pacing), SME (honest data display), Technical Lead.

**My view: show the 5 kPa line from sol 0 (the owner's decision stands).**
- It is an advance organizer (Ausubel) and goal-setting: the player's only lever is pressure, and the plot is where they can read progress (C3, B1).
- Nothing is hidden by waiting: 5 kPa is in the public gate table.
- It is data, not a hint: it gives the distance, not the method. It supports B3 (extend the slope), where the player does the work.
- Progressive disclosure fits things that overload; one solid line and two numbers do not.

**Risks to watch.** (1) At sol 0 the line is about 2 px, the goal is far, and Dome stable (the Outgasser unlock) is the Next goal. A 12-year-old may ask what to do with a line they cannot yet push. Mitigation: the Next goal row names Dome stable first. (2) With two Ovens only, the early slope is tiny (Oven +0.01 per sol, invented), so the goal can look unreachable: a motivation risk. (3) The time-to-goal estimate would remove the need to read the plot: CR-T8. First playtest check (observed, not asked as a quiz): at sol 2, can the player say what the solid line is and what moves the number?

**Phase 3 case: yes, needs different text.** Pressure can pass 30 kPa while temperature is below 0 °C. The line is then below the series and the label "Phase 3 begins: 30 kPa" is false-ish. Use "Pressure goal: 30 kPa" (also for Breathable), and the Next goal row must show the unmet temperature condition. That is a productive moment: a goal that is half met teaches that a gate needs two conditions (A2, C3). No extra explanation text on the plot. Handoff to the Game Designer for the Next goal row.

## 3. NGSS practice names
Checked against the official source: NGSS Appendix F lists eight practices; number 4 is "Analyzing and Interpreting Data" and number 5 is "Using Mathematics and Computational Thinking" (the January 2013 draft used a longer name for 5; the final release shortened it). Both are already in `LEARNING.md` 6.2 and `docs/science/ngss-verification.md`. The Game Designer cited names only, no codes, so no codes needed. For C3 the plot supports the grade 6-8 progression of analysis (patterns in data, correlation versus cause) only partly; no PE is claimed (C3 stays "practice only" in the alignment table). Source: Appendix F PDF at nextgenscience.org (via search result summary; **page not opened in full**, so mark as verified by search summary only).

## 4. Open items
### 4a. Phase 2 rose-sky label (SME round 2 requirement)
Facts from the code: `pack/assets/mars/keyframes/sky.json` interpolates from 0.6 kPa on, so the tint starts moving with the first pressure rise; it is rose-grey at 5 kPa and clear by 5 to 15 kPa. The label must exist before the player notices (an unlabeled stylized sky will be taken for fact: Artist review).
- **Text (SME-required, verbatim, both bands):** "More air scatters more blue light, so the sky slowly loses its dusty tan. Nobody has seen a thickened Mars sky. These in-between colors are our best guess, made stronger so you can see them."
- **Recommended trigger:** journal entry `journal.sky.stylized` unlocks when "Dome stable" latches (before the Outgasser exists, so before any sky change), with a one-line pointer in the chat: "New journal entry: the sky." The same text repeats in the Phase 2 begins gate entry (SME's original placement).
- Goals: A1 (air makes the sky), D2 is not claimed. No forecast is answered (CR-F9 pass). Trigger and delivery belong to the Game Designer; **handed back, not decided.**

### 4b. Dome stable journal "On Earth" trigger
The text is drafted: `docs/design/messages/phase-1.md` section 9 (`earth.gate.dome_stable`, plus `journal.dome_stable.airmix`). Proposed trigger: the latch of gate `mars:dome_stable`, once per planet, delivered to every player's journal on that planet, plus a chat pointer "New journal entry: Dome stable". Rules: fills no Keeper's Record page (planet-scope event; CR-R1); a late joiner gets it on join. The wording does not depend on what the stand-in life-support state is (CR-H5). One wording concern for the SME: "Earth's thick air protects us for free" is vague (pressure or radiation?). Suggested: "Earth's air pushes on you with about 100 times more force than Mars's air. You never notice, because your body pushes back." (0.6 of 101.3 kPa is under 1 %, so "about 100 times" is arithmetic; the body claim is **to confirm by the SME**). Wiring: Technical Lead.

### 4c. Landing step 6
Proposed: "Place your two Ovens. Switch on the battery to power them." (11 words). The cause-and-effect order matches `grant.starting`. Detection must be both actions. Whether "backup battery" in the Landing is the same Battery block as the grid Battery is **unverified**; the Game Designer owns `phase-1.md` and the step.

## 5. Decisions needed
1. US spelling "Leveling" and words instead of "~", "<" (CR-T7). Well-structured; recommend yes. Owner or Game Designer.
2. Show time-to-goal estimates under gentle and standard, hide under expert and for open forecasts (CR-T8). Ill-structured (guidance against B3 practice). Recommend yes.
3. "Polar CO2 frost" against "Polar frost". Well-structured once panel width is known. Recommend CO2 if it fits.

## 6. Handoffs
> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** (a) the sky-label trigger (4a); (b) Landing step 6 text in `phase-1.md`; (c) the Next goal row must show the unmet temperature condition when pressure is past 30 kPa and Phase 3 is open; (d) approve `Pressure goal: %s` and the CO2 option.
> **Context:** `docs/design/ui-specs.md` 9.2; this report sections 2 and 4.
> **Needed from them:** accept or change. **Done when:** `phase-1.md` and ui-specs agree.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** apply the new keys in 9.2 to `en_us.json`; add `monitor.plot.goal.partial`; add `reveals` to estimate lines and hide time-to-goal for open forecasts and `guidance` = expert (CR-T8); declare no forecast resolves on the frost line (CR-F10); wire the Dome stable entry to the gate latch.
> **Done when:** the CR-T7 scan and a CR-T8 test pass; both can fail on a bad key.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** confirm the replacement "On Earth" sentence in 4b ("body pushes back") and the Phase 3 / Breathable plot label.
> **Done when:** noted in `phase-1.md` section 9.

## 7. Risks
- Option D: a reader who misses the rescale line may read the step as a failure (new wording answers it directly; test in the first playtest).
- Early flat plot plus tiny Oven slope can read as "nothing happens" (see 2).
- Dome stable arrives with a burst: chat line, Outgasser unlock, journal entry, Earth line, sky entry. Keep the pointer lines short or stagger them.
- FK scores come from a rough syllable script; run the project's real CR-T6 check when it exists.

## 8. Shared model updates
- Decision (proposed, owning doc `docs/design/ui-specs.md` 9.2): plot goal label is "Pressure goal: %s" when the gate has other conditions.
- New content rules CR-T7 and CR-T8 in `docs/learning/content-rules.md`.
- Lesson: a Monitor feature that shows a prediction (time to goal) can silently take over a learning task (B3) and reveal forecast answers; every such line needs a `reveals` declaration and a `guidance` rule.
- Lesson: a label built from a gate name and one of its conditions is only true for gates with one condition.
