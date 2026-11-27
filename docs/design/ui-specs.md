# UI Specs — Mars Terraforming

**Owner:** Lead Game Designer (screens, flows, wording) · Visual system: Lead Artist ([style guide](../art/style-guide.md) §4, §7, §11) · **Status:** Draft 1 (2026-11-26). Layout and wording are **fluid** (TEAM.md 8.1). Nothing here has been playtested. · **Depends on:** [DESIGN.md](../../DESIGN.md), [LEARNING.md](../../LEARNING.md) §4.6–4.7, [balance sheet](balance-sheet.md), [messages/](messages/README.md)

**Rules every screen follows**
- Fits 420×236 GUI px (style guide §4.5). Color is never the only signal: every state also has a shape, pattern, or word (style guide §4.1).
- Words a 12-year-old knows; science words defined on first use. No "lesson", "objective", "quiz", "learner" (TEAM.md 8.4).
- Every number has an icon or a picture next to it. Every value shows a trend arrow (shape: up, flat, down).
- Middle school (MS) and high school (HS) bands see the same layout; HS adds numbers and "read more" (LEARNING.md §5 grade-band rules).

- **PROCESS.md §4 UI rules apply to every screen:** one message at a time at the top; no modal pop-ups for progress; HUD panels hidden over full-screen sheets; values drawn larger than their labels; the next step marked; a dark backing strip behind every line over the 3D world, with HUD variants of icons drawn for light journal pages; only actions the player can do now (locked recipes hidden, not greyed out; on gentle guidance one quiet line says what unlocks more); real instrument and machine names; a deliberate pixel font for the journal and Monitor (Lead Artist), with every glyph we use checked (°, −, ×, ₂ if used, curly quotes); thousands separators and units on every number through one formatting helper ("1,500 BU", "−20 °C", "0.57 kPa").
- **Size check:** 420×236 GUI px is 1280×720 at GUI scale 3. Every screen is checked by screenshot at 1280×720 (PROCESS.md §2).

---

## 0. Message delivery (interruption option I-C)

Follows TEAM.md 8.6 as decided (option I-C: LEARNING.md §4.6 rules 1–11 as the Stage 3 test configuration, with the two flow guards).

| Surface | What goes there | Modal? | Counts toward the pop-up cap? |
|---|---|---|---|
| **Top line** (one at a time, backing strip) | The newest colony-log line; others queue behind it (≤ ~3 per minute, similar lines merged: "3 filters worn") | No | No (log-line cap) |
| **Side panel** (right edge, collapsible) | Next-gate bar, "just finished" note (stays until the next real action: placing, mining, or opening a machine), forecast prompts, Shift Briefings, "Report ready to send", unread messages | No | Forecast prompts and briefings: yes, when they open automatically at a natural pause |
| **Full-screen sheet** (HUD hidden) | Journal, Monitor, Keeper's Report, Keeper's Record, machine screens | Opened by the player, or by the game only at a natural pause | Keeper's Report auto-open: yes |
| **Blocking warning** | Only life-threatening firsts: first O2 critical, first breach, first solar-event exposure outside | Yes | No (safety) |

- **"Card"** in the message files means a side-panel card, never a modal window.
- **Gate reached:** the world changes and the side panel shows "Phase 2 reached" with what unlocked. No modal. **Keeper's Report (confirms the Producer's fix for the PROCESS.md vs. I-C rule 3 conflict):** one log line ("Keeper's Report ready to send") plus a side-panel entry. The report opens full screen **only when the player chooses it, or at the next natural pause** (with a "Later" button). It never opens over danger or building, so it is never a modal pop-up for progress. An unsent report is re-offered **at most once per session** plus the "Since last shift" recap line (flow guard 1).
- **Sequence at a gate:** report → Mensah's reply (after its delay) → Shift Briefing. One item visible at a time; together they count as one pop-up.
- **Forecasts:** shown in the side panel at the next natural pause; result first in the queue; deferred forecasts lock as "expired", never wrong. No new forecast or report pop-up in the last ~5 minutes of a class (teacher end-of-session signal). **Monitor open with an unanswered F3, F5, or F8:** the forecast card shows first ("Log your forecast before you look"; Answer or Skip), then the Monitor. Looking never silently closes a forecast (LEARNING.md lock-point rule, CR-F9).
- **Caps are config values** (`ui.popupMinMinutes` ≈ 10, `ui.logLinesPerMinute` ≈ 3), set from Stage 3 action-log counts (flow guard 2).
- **Groups:** one player's prompt never pauses others.

---

## 1. The Planetary Monitor: overall layout

A block in the dome (and later in outposts). Opening it is a **natural pause** (TEAM.md 8.6).

| Area | Content | Goals |
|---|---|---|
| Top bar | **Next-gate goal** (§3) | C3, B1 |
| Left column | **Dome** tab: dome O2 reserve, water, power, filter wear, open failures (links to the Shift Board) | C1, C2 |
| Center | **Planet** tab: the Index values shown in this phase (§2), each a row with icon, value, trend arrow, and a one-line top cause ("Outgassers +0.30/sol") | A1–A5, B1, C3 |
| Row click | Opens the value page: graph of the last 10 sols with cause lines labeled **on the graph** (DESIGN.md, Multimedia: text next to what it explains) | B1, C3 |
| Right strip | Milestones and alerts: space weather (§4), storm forecast, fauna page link (from Thaw) | A4, C1, B2 |
| Bottom | "Read more" (HS default open; MS closed) | — |

**Cause breakdown default per phase (fading hook; ID owns the schedule, LEARNING.md §4.8):** Phase 1: open on every value row. Phase 2: open on values new this phase (temperature, hydrosphere, fauna), closed on pressure. Phase 3: closed by default, one click opens. **Re-scaffold:** whenever a value leaves its band (or a hold pauses), that row's breakdown opens by itself and stays open until the value is back in band.

---

## 2. Progressive disclosure of parameters by phase

The Monitor shows only the values in play (DESIGN.md, Cognitive load). A new row appears with a BELL log line ("New on the Monitor: Water") and a one-time outline on the row. Values not yet shown still exist in the Index; they simply aren't on screen.

| Shown from | Planet rows added | Derived and extra displays | Why then |
|---|---|---|---|
| Start (Landing) | **Pressure** (log-scale gauge with milestones, style guide §11.1), **Temperature**, **Water** line ("Water boils here at {boil_c} °C") | "Air mix" fact line: about 95 % CO2, almost no O2 (static text in Phase 1; Landing step 3 points at it; A1) | Phase 1 is about pressure; the water line must be there before F4a so its "Check:" pointer has something to point at |
| First ice change (F4a) | (water line gets its outline highlight and a BELL line) | HS: triple-point notch explained | A2 moment: the boiling point sits next to the event |
| First panel at dust stage 1 | Dome tab power row gets the cause line **"Panel dust −{n} %"** (separate from "Storm: less sunlight −{n} %"); looking at a panel shows its stage bar | — | Power drift needs a named cause the moment it starts (DESIGN.md, Solar panel dust) |
| Dome stable | Pressure cause lines get "Outgasser" | — | First lever |
| Phase 2 begins | Temperature cause lines (emitters, polar frost) | HS: polar frost loss as a named sink (B2) | Second lever |
| Thaw | **Hydrosphere** (% surface water), **Fauna** page (fauna pressure F, signatures by machine, map with quiet zones) | Ground-ice survey layer | Water and fauna arrive together |
| Phase 3 begins | **O2**, **CO2** as live rows; **Breathable air** panel (§5); **Biomass** | Carbon flow view, opened from the CO2 row (HS default; MS via read more) | Balance phase |
| Breathable / Equilibrium holds | **Hold counters** in the top bar (§3) | — | The goal becomes "hold" |

Count of planet rows on screen: 2 (Phase 1) → 4 (late Phase 2) → 6 (Phase 3). Choice load rises with the phase (TEAM.md, Interactivity).

---

## 3. Next-gate goal display (top bar)

**Purpose:** a clear, specific, attainable goal at all times (flow: clear goals; Zeigarnik: a visible unfinished task). Answers the unclear task "raise pressure" with an ending: *how much, and how close am I?*

**Layout:** `Next: Phase 2 · Pressure 5 kPa · [progress bar] 62 % · ~4 sols at this rate`

| Element | Rule |
|---|---|
| Gate name | Player language ("Phase 2", "Thaw", "Breathable air") with the gate-diamond icon |
| Condition(s) | One chip per condition. Multi-condition gates (Phase 3: pressure **and** temperature) show one bar per condition; the gate is the **lowest** bar |
| Progress | For pressure: progress along the **log scale** from the previous gate's value to the target (so Phase 1's 0.6 → 5 kPa doesn't sit at 0–10 % for hours). For temperature: linear from previous gate value. Hydrosphere: linear from 0 |
| Estimate | "~N sols at this rate" from the 1-sol net trend. Shown only when the trend is up; otherwise "Not rising: check causes" (link to the value page). Rounded to whole sols; "< 1 sol" below 1 |
| Dome stable (machine-state gate) | "Life support: {n}/{hold_sols} sols with no failures" with tick boxes; resets visibly on a failure |
| Holds (Breathable, Equilibrium) | "Holding: 4/7 sols" with a ring; if a value leaves the band: "Paused: O2 out of band · resets in {t} sol if not fixed" (hold rule, balance sheet §4) |
| After Equilibrium | "Next: build and light the beacon" with component checklist (DESIGN.md content lists) |
| Never shown | Scores, ranks, XP, or anything about other groups |

Also shown as a small HUD chip (gate icon + bar) that the player can turn off in settings. It never flashes.

**Recommended builds (paradox of choice).** Under the bar, a short **"Useful now"** list (≤ 3 machines) for the current gate, from the gate data (e.g. Phase 1 after Dome stable: Regolith Outgasser, battery, solar panel). The same list is pinned at the top of the crafting/recipe view, marked with a small clipboard icon, so players aren't facing every recipe at once. It recommends, never locks; Lens B players can ignore it. Lists live in datapack JSON next to the gates.

| Gate being worked toward | "Useful now" (draft) |
|---|---|
| Dome stable | Seal kit, filter cartridge, battery |
| Phase 2 begins | Regolith Outgasser, solar panel, battery |
| Thaw | Atmospheric processor, super-greenhouse emitter, transit tube |
| Hydrosphere | Ice extractor, meltwater treatment, insulation shroud |
| Phase 3 begins | Emitter, processor, quiet-zone marker |
| Breathable | Biome seed kit, soil treatment, algae vat |
| Equilibrium | Carbon scrubber, biome seed kit, beacon components |

---

## 4. Solar-event warning (~1 sol ahead): F2 and F7

**Goal:** time to predict and to plan (A4, B3, C1), then an unmistakable safety action. The forecast must **lock before** any message reveals the answer.

| Time | What appears | Interrupts? |
|---|---|---|
| T−1 sol | Log line `log.solar.warn`; HUD **sun-burst icon** (hexagon outline, style guide status shapes) with countdown "~1 sol"; Monitor "Space weather" row. Forecast F2 (first event) or F7 (first after pressure doubled) is queued for the next natural pause | No |
| T−0.25 sol | **Forecast locks.** Log line `log.solar.shelter` names the shelter; HUD icon gains a bold border and the countdown switches to minutes; one soft two-tone chime (not the alarm) | No |
| Onset | HUD radiation meter appears when outside (bar + rising-dose arrow); log `log.solar.onset` | No, except the **first-ever onset while outside**: a blocking warning (rule 3: life-threatening first) |
| During | Dose builds only while exposed; shielding blocks and depth stop it (DESIGN.md §3) | — |
| End | Log `log.solar.end`; HUD icon clears; forecast result queued first at the next natural pause | No |

- Real-time length: 1 sol = 20 minutes of simulation time in every preset, so the warning is ~20 minutes of play in both standard and classroom mode. The classroom rate multiplier doesn't shorten a sol.
- Rule 5 (end of class): if the T−1 sol warning would fall in the last ~5 minutes of a classroom session, the event is scheduled after the next resume instead.
- HS read more on the Monitor row: "Expected surface dose: {dose_pct} % of the first event", shown only **after** lock (it gives away F7).
- Accessibility: icon shape + countdown text + chime with subtitle "[Solar event warning chime]" (style guide §4.7).

---

## 5. Breathable-air panel (the "Air window")

Accepts the Lead Artist's **Air window** (style guide §11.2) and the Lead Instructional Designer's recommendation (LEARNING.md §4.7): one gauge for both bands, three zones in words and shapes, the two numbers underneath. Goals: A1, A5, B4, C3.

**Air check row** (both bands): three small gauges, each with a target notch and a check or cross shape.
| Check | MS label | HS label | Condition |
|---|---|---|---|
| Pressure | Thick enough | Pressure ≥ 75 kPa | pressure ≥ 75 kPa |
| O2 | Oxygen push | O2 partial pressure ≥ 16 kPa | O2 fraction × pressure ≥ 16 kPa |
| CO2 | Low CO2 | CO2 partial pressure ≤ 0.5 kPa | ≤ 0.5 kPa |

When all three show a check **and** the hold is complete, the helmet icon changes shape from closed to open.

**Zoomed O2 gauge** (15–27 % O2). Zone wording (final, changes the Artist's draft labels):
| Zone | Range | Label (MS and HS) | Pattern + pictogram (Artist) |
|---|---|---|---|
| Too little oxygen | below 16 kPa ÷ pressure | **Too little oxygen** | Sparse dots, closed helmet |
| Breathable | lower edge to 23 % | **Breathable** | Clear, bold outline, open helmet |
| Fire risk | above 23 % | **Fire risk** | Diagonal hatch, flame |

*Change from the Artist's draft:* "Too thin to breathe" → "Too little oxygen", because "too thin" is the **pressure** check's job; mixing them would blur the two causes the panel exists to separate. "Breathe here" → "Breathable" to match the journal and LEARNING.md.

**States the panel must handle** (from the balance sheet numbers; no numbers changed):
| Pressure | Lower edge (16 ÷ p) | Panel shows |
|---|---|---|
| ≥ 75 kPa | ≤ 21.3 % | Normal: the Breathable zone is open; the edge slides down as pressure rises (window widens on screen) |
| 69.6–75 kPa | 21.3–23 % | Breathable zone drawn **outlined but hatched lightly**, with the note "Not thick enough yet" next to the cross on "Thick enough" |
| < 69.6 kPa | > 23 % | No Breathable zone at all. Text: "No safe oxygen level at this pressure. Raise pressure first." |
| < 60 kPa | > 26.7 % (off scale) | Same text; edge marker pinned to the top with an arrow |

**Time-to-edge estimate** (LEARNING.md §4.7 item 4): under the gauge, "Fire risk in ~{t} sol at this rate" or "Too little oxygen in ~{t} sol", shown only when an edge is under 2 sols away. MS shows "soon" (< 0.5 sol) or "in about a sol"; HS shows the number.

**Numbers underneath:** MS: O2 % only, partial pressure in "read more". HS: O2 %, O2 partial pressure, CO2 partial pressure, and the Air window chart (style guide §11.2 level 2) one click away.

**Not used:** a single "breathability %" number (hides the two causes).

**World cue:** from Phase 3 the **weather mast** (§7) carries O2 and CO2 **gauges** (indicator bars, never tinted gas) showing the same values, so the panel has an honest in-world twin. Its barometer shows pressure from the start.

---

## 6. HUD

Minimal, vanilla-style. Left: suit O2 (bubble icon + bar + number), suit power. Right: active warnings as shaped icons (storm, solar event, radiation dose, perchlorate exposure), each with a short word. Top center: optional next-gate chip (§3). No HUD element flashes more than 3 times per second (style guide §4.6). Messages arrive as one colony-log line at the top (§0); everything else follows option I-C (§0). All HUD panels hide when a full-screen sheet opens.

---

## 7. Lead Artist proposals: decisions

| Proposal | Decision | Spec |
|---|---|---|
| **Weather mast** | **Accept** (user adopted), with the SME's instrument fixes. | Instruments: windsock, lift balloon, red spirit thermometer (not mercury), dial barometer (kPa), and **O2 and CO2 gauges** (indicator bars, never tinted tubes). One stands outside the dome from the start (Landing step 7 passes it); craftable after Dome stable for outposts. **The gas gauges are fitted at Phase 3 begins** (BELL: "Gas gauges fitted to the weather mast"), matching the Monitor's progressive disclosure, so a Phase 1 player isn't shown a value the Monitor hides (Technical Lead's flag; my recommendation, lower cognitive load). The empty gauge brackets are visible from the start. Not required for any gate. Goals: A1, A4, A5, C3. *Needs the Artist's and ID's agreement (handoff)* |
| **Quiet-zone marker post** | **Accept, with a rule change.** | The quiet zone is defined by the rule (no active machine within 48 blocks; balance sheet §6), not by the marker. Placing a marker **names** the zone, draws its 48-block ring on the Monitor fauna map, and warns ("Machine inside quiet zone") if a machine is switched on inside the ring. A zone works without a marker; the marker makes it visible and plannable (C3, B2, D1) |
| **Thawling defensive behavior** (rear up, frills flared, short shove, curl and roll away; no lunge; no gore) | **Accept.** | Numbers added to the balance sheet §6 (draft): juveniles shove with knockback only, 0 damage; later-life-stage adults shove for 1 damage (half a heart) plus knockback, once, then flee. They never chase. This keeps "they only fight back if attacked" true and keeps lethal defense a choice with no reward beyond protecting a machine |
| **Air window** | **Accept**, zone labels changed (§5) | — |
| **Shift Board** visuals (style guide §11.3) | Accept | Status words "Open / Claimed / Fixed" with the three shapes |
| **Ending cards** (style guide §11.4) | Accept | Same frame for every card |

The content lists the Artist asked for (crops, biome types, maintenance parts, beacon components) are in DESIGN.md, "Content lists".

---

## 7a. Recipe view and session end

**Locked items: "Unlocks at…".** Locked recipes are hidden, never greyed out (PROCESS.md §4). On **gentle** and **standard** guidance, the recipe view ends with one quiet line naming the next unlock in planet terms: *"More machines unlock at Phase 2 (pressure 5 kPa)."* On **expert**, no line.

**Shift log** (optional, classroom): at the teacher's end-of-session pause, a three-line side-panel card from the player's own data (messages/phase-1.md §13).

## 8. Journal

**Keeper's Report sheet.** Delivered as one log line plus a side-panel entry (§0). Contents: a **"Your shift"** block first (own events only, in positive terms: machines you built, failures you fixed, forecasts you logged and got right, times you checked the Monitor before building; missed forecast topics link to their blank Keeper's Record pages), then the team summary (built from data), own forecast tally, one reflection question, a text box, **Send** and **Later**. Beside every free-text box, in both bands (`report.name_note`): *"Don't write your name or anyone else's. Your report goes to Earth with your Keeper number only."* In classroom mode the note adds (`report.name_note.class`): *"Your teacher can read this."* (LEARNING.md §5.4; reflections.csv scrubbing is the Technical Lead's backstop.)

Tabs: **Shift notes** (gate entries, Hana's notes, briefings) · **Messages** (archive, unread marked with a dot and a count) · **Forecasts** (own tally per phase: "Phase 1: 3 of 4 right") · **What caused it?** (self-check page; final name of the old "Explain it") · **Keeper's Record** (header "What the colonists will need to know"; own-evidence pages and the "Cairn team" section).

Every page pairs text with a small picture or Monitor graph (DESIGN.md, Multimedia). Text budgets: story bible §6.1.

---

## 9. Monitor wording (Stage 2 straw man, reviewed 2026-11-27) and pause design

**Status:** wording is **fluid**; the straw man is in `pack/assets/mars/lang/en_us.json` (`monitor.*`). Reading level target: grade 6-7. Units and numbers come from the one formatting helper (§ rules above), not from the lang strings. Not playtested.

| Key | Straw man | Recommended final | Why |
|---|---|---|---|
| `monitor.title` | Planetary Monitor | Planetary Monitor | Real name in the vocabulary; keep |
| `monitor.sol` | Sol %s | Sol %s | Short; the first-use definition ("1 sol = one Mars day") goes in the journal, not the screen. Add "(paused)" as a suffix when paused (below) |
| `monitor.no_planet` | The Monitor has no planet to read here. | This Monitor only works on Mars. Travel there to read a planet. | Says what to do, not only what is wrong |
| `monitor.row.pressure` | Pressure | Air pressure | "Pressure" alone is ambiguous for a 12-year-old; HS band may show "Pressure" with the unit in the line |
| `monitor.row.temperature` | Temperature | Temperature | Keep |
| `monitor.row.water_boils` | Water boils at | Water boils at | Keep. It is a derived value (TEAM.md "Boiling point at current pressure"); a "read more" line says why it moves with pressure (A1) |
| `monitor.water.no_liquid` | No liquid water at this pressure | Too thin for liquid water | About half the length, so it fits the panel without shrinking. Read-more (HS default open): "Below 0.61 kPa, ice turns straight into vapor." (0.61 kPa is the water triple point; **SME to confirm the sentence**) |
| `monitor.trend.rising` | Rising | Rising + up-arrow shape | Word stays; add the shape (colour and glyph never the only signal). Check the arrow glyph in the pixel font first |
| `monitor.trend.steady` | Steady | Steady + flat-bar shape | Same |
| `monitor.trend.falling` | Falling | Falling + down-arrow shape | Same |

**Keys still missing (propose when slices 3 and 4 land):** `monitor.cause.outgasser` = "Outgassers: +%s per sol" (one line per source, never a bare number, B1); `monitor.cause.none` = "Nothing is changing this right now."; `monitor.trend.paused` = "Paused".

**Stale arrival line (Technical Lead handoff).** I found no "The planet is empty for now" text left in `mod/src` or `pack` (grep, 2026-11-27); `docs/team/reports/2026-11-27-technical-lead-arrival-copy.md` suggests it was already replaced. If it returns, use: "You are on Mars. Place a Regolith Outgasser and a Planetary Monitor, then watch the pressure."

**Slice 4 gate wording (new, straw-man, not reviewed by the Lead Game Designer; in `en_us.json`).** `monitor.row.next_goal` "Next goal"; `monitor.row.goals_reached` "Goals reached" (value "1 / 2"); `gate.mars.dome_stable` "Dome stable"; `gate.mars.phase_2_begins` "Phase 2 begins"; `monitor.gate.dome_stable.progress` "Life support, no failures" with "1.0 / 3 sols"; `monitor.gate.dome_stable.waiting` "Life support is not steady. The count waits."; `monitor.gate.phase_2_begins.progress` "Air pressure" with "0.60 / 5 kPa"; chat `gate.reached` "Goal reached: %s", `gate.unlocked` "You can now build: %s"; action bar `gate.locked.placement` "Not yet. Reach "%s" first." Differences from section 3: no tick boxes yet (a number, not boxes), and the hold counter pauses for a short failure and resets after 1 sol (balance sheet 4), where section 3 says a failure resets visibly. The Game Designer should pick one.

### Pause design (one line; **not built yet**)

When the colony is paused, every player sees a **"Colony paused" chip** (two vertical bars plus the words, never colour only) at the top centre of the HUD, the sky and dust stay frozen, the Monitor shows "Sol N (paused)" and still opens read-only, and any action that would change the planet (place or break a machine, switch one on, craft) is **blocked with one log line**: "The colony is paused. Your teacher will resume it." (standard play: "The server owner paused the colony."). Movement, looking, the journal and the Monitor stay available so a paused class can still discuss what is on screen. Goals: C1 (nothing to fix while paused is honest), B1 (read the Monitor while time is stopped). The Technical Lead lists the HUD line and the blocked actions as not built (slice 2 report); they are slice 9 (classroom) work unless the owner wants them earlier.
