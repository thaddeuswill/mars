# Report to orbit: file your findings, unlock the next job and the parts

Owner: Lead Game Designer. Written 2026-11-28. Status: proposal v1, owner said "accept and cont with rec" (2026-11-28), so decisions A to F below are taken as the recommended option. Scope: standard pace; never hard-block. Marks: INVENTED = our number or item; NOT VERIFIED = not checked; Game design = a story device, not real.

Saved by the coordinator from the Game Designer subagent's hand-back text (the subagent's file write was refused by the harness). Nothing here was run or built.

Owner request: "we should have a report to the iss with the findings to get quest unlocked."
Serves C3, B1, C2, A1 to A5 (per gate), D2, B3 (reply ties to forecasts). Builds on `recipe-inputs-ruling.md` (drops per gate), `gate-input-items.md` section 7 (report page), `messages/phase-1.md` sections 9 and 10, story bible 4.1 and 4.4. Learning review: `docs/learning/report-to-orbit-review.md`.

## 0. One paragraph
When a gate latches, a Findings report is ready on the Monitor. Any player opens it, reads before/after numbers, makes three taps (cause, evidence, claim), presses Send to orbit. The Relay Orbiter carries it. A reply comes from a named voice, the gate's resupply drop lands and the next Shift Briefing shows. If nobody files, the same drop and briefing arrive on a 2-sol timer (INVENTED). Filing is faster and is the only way to get the reply and the Record page. It is never required.

## 1. What "quest line" means here
Our quest line is gates plus Shift Briefings (story rules 1 and 2: story never gates progress, no NPC questlines). The report is not a quest giver. It (a) makes the player explain what the planet just did (C3, B1); (b) gets acknowledgment; (c) releases parts and the briefing early. The gate stays a world condition; placement locks stay as built.
Reward rule: the drop is identical for every filed report. Picks change feedback text, never items.

## 2. Who receives it (labels, game design)
- In-world recipient: Mission Control, through the Relay Orbiter. The Earth-side science desk reading your data includes crew on the ISS. Label: "Game design: the ISS circles Earth and cannot reach Mars. Real Mars data goes through relay orbiters to the Deep Space Network."
- The ISS is never the sender of the drop (the Relay Orbiter is).
- Real reference: Mars relay orbiters carry rover and lander data to Earth: NTRS 20150008785, VERIFIED (`docs/science/machine-references.md`). Details of NASA mission operations (reports, review, timing): NOT VERIFIED; SME to confirm one line.
- Delay: real one-way light time about 3 to 22 minutes (story bible 4.1, [verify]); the game reply delay is 0.5 sol x story.delayScale.
- Journal line `journal.report.orbit` (grade 6-7): "Your report goes to Mission Control through a relay satellite. Real Mars missions work this way. The ISS circles Earth, so it cannot talk to Mars directly. In this game its crew read your report on Earth. That part is game design."

## 3. What the player does (under 2 minutes)
Entry: "Report to orbit" tab on the Planetary Monitor (see the Monitor pages spec: it is one of the Monitor's pages); a dot shows on the goal row and journal tab when ready. Later option: a Comms Terminal block opening the same screen.
| Step | Screen (one idea each) | Input | Time |
|---|---|---|---|
| 1 Look | "Findings: {gate_name}": six rows before/after (planet report page), two focus rows marked, side-effect line. Read-only | none | 20 s |
| 2 Cause | "What caused the change?" 3 buttons (<=10 words) | tap | 15 s |
| 3 Evidence | "Which line shows it best?" 3 buttons, each a real line from step 1 | tap | 15 s |
| 4 Claim | "Our claim:" 3 sentences (<=14 words) | tap | 20 s |
| 5 Send | "Send to orbit" / "Later" | tap | 5 s |
No free text. Gates 2, 5, 7 also offer the existing Keeper's Report reflection (free text, own answer, deferrable) after Send as a separate optional card; the findings report never waits for it. Choices carry a shape (square, circle, triangle) as well as text: colour is never the only signal. "Later" closes without penalty.

## 4. What orbit does (times INVENTED, scaled by story.delayScale)
| t after Send | Event |
|---|---|
| 0 s | BELL receipt: "Report logged. Relay pass in about 1 minute." (<=20 words) |
| 30-60 s | Drop lands (gate cache list, ruling v2 section 5), chat "Supply drop received: N x Item", "Sent from orbit" label; existing cache code, moved (section 7) |
| ~0.5 sol | Reply from the named voice, message archive, with a feedback line |
| with reply | Next Shift Briefing (gates 2 and 5) or one "Next job" line (others), kept in the journal |
The drop does not wait for the reply. The briefing waits for the reply so the voice can set it up.
Feedback line (positive wording, CR-T5):
- Cause matches: "Your cause matches the data: {cause_line}"
- Other cause: "Good thinking. The data points to this cause: {cause_line} Look at the {row} line."
- Evidence is a focus line: "That line shows it clearly: {row}, {v0} to {v1}."
- Other evidence: "That line moved too. The strongest sign is {focus_row}, {v0} to {v1}."
- Claim supported: "Your claim holds up. {claim_note}"
- Claim a stretch: "Test that claim on your planet. {claim_note}"
Nothing is scored or shown as points. Picks go to the Record and the local log as ids (no text, no names).

## 5. How it gates without blocking
| Case | Result |
|---|---|
| Latch, player files | Drop in ~1 min, reply ~0.5 sol, briefing with reply |
| Latch, nobody files | Auto-release after 2 sols (INVENTED). BELL: "Orbit sent your parts anyway. Your report is still open." Briefing shows with the drop; no named reply yet |
| Filed late | No second drop; reply arrives as normal; Record page fills |
| Short of parts | Resupply request item works, filed or not (ruling v2 section 6) |
| Needs a part before 2 sols | Coil (2 iron), cartridge and bearing are craftable; cores via the request |
| `/mars gate latch` or creative | Release at once, reason ADMIN |
| Group, simultaneous send | Server accepts first; second sees "Already filed by Keeper {n}" and a read-only copy |
Story rule 1 "story never gates progress" holds because the report only speeds parts and briefings. Cost of skipping: about 40 min at standard pace. Existing spec change: `phase-1.md` section 10 says Mensah replies on "Report 1 sent (or gate + 1 sol if deferred)"; new rule: reply and briefing fire on filing; the deferred path is auto-release plus a late reply. Instructional Designer to re-check (LEARNING.md 4.6).

## 6. Per-gate content
Rules: grade 6-7, US spelling, positive wording, no forecast give-aways (CR-F9, CR-T8): each def has `reveals`, and the report is not offered while a listed forecast is open. The first cause/evidence/claim option is the correct or supported one in data; display order is shuffled. One crew voice per phase (story bible 4.4); Mensah only at gates 2, 5, 7.
| # | Gate | Focus rows; side line | Cause options (first correct) | Claim options (first supported) | Voice | reveals (confirm) | Goals |
|---|---|---|---|---|---|---|---|
| 1 | Dome stable | Pressure, Temp; "Your machines use {d} units a sol." | "Panels and battery kept power on." / "The storms stopped by themselves." / "The dome needs no power." | "Every machine I add needs more power." / "Dust is the only thing that cuts power." / "Power stops mattering now." | Hana | F1, F3 | C1, B1, C3, A3 |
| 2 | Phase 2 begins | Pressure, CO2; "Frost took back {f} kPa on cold nights." | "Cold ground freezes some CO2 out of the air." / "Wind carried air away." / "The Outgasser leaks air." | "Cold nights slow my pressure gain." / "Pressure will rise at one speed forever." / "Frost cannot change pressure." | Mensah | F4a | A1, B2, B1, C3, D2 |
| 3 | Thaw | Temp, Water; "{n} blocks of ground sank near hot machines." | "Heat melted ice under the soil." / "Wind dug holes." / "Machines were too heavy." | "Hot machines can melt hidden ice." / "Hot machines are safe anywhere." / "Ground never moves on Mars." | Basu | F4b | B1, D2, B5, C3 |
| 4 | Hydrosphere | Water, Temp; "Meltwater test: {s} percent salt level." | "Melted ice carries salts from the soil." / "Ice is always salty." / "Water makes salt from air." | "Meltwater needs treating before use." / "Meltwater is always safe to drink." / "Salt level never changes." | Basu | F6 | B5, D2, C3 |
| 5 | Phase 3 begins | Temp, CO2; "Heat leaks {l} C each sol." | "Heat leaks to space unless emitters keep up." / "The Sun gets weaker each sol." / "Dust warms the night." | "I must keep emitters running to hold heat." / "Warm air stays warm by itself." / "Heat loss only happens at night." | Mensah | none known | A3, B2, C3 |
| 6 | Breathable | Oxygen, Life; "Plants used {c} kPa of CO2." | "Living things and machines turned CO2 into oxygen." / "Oxygen leaked in from space." / "Cold air makes oxygen." | "Plants and machines trade one gas for another." / "More oxygen is always better." / "Oxygen stays put without help." | Moreno | F8 (answer-adjacent) | A5, B1, B4, C3 |
| 7 | Equilibrium | all six; "Repairs this hold: {r}. Wear events: {e}." | "Holding a planet steady takes upkeep." / "Machines never wear out." / "The beacon repairs everything." | "Balance needs upkeep, so I keep watching." / "Once balanced, always balanced." / "Repairs are only for beginners." | Mensah, then Moreno | F9, F10 | C2, B4, B2, D2 |
Evidence options at each gate: the side line, plus the two focus rows (the first listed is the match). F-numbers in `reveals` are the Game Designer's best match; Technical Lead and Instructional Designer confirm against the forecast data.
`claim_note` (<=14 words): 1 "Your side line shows what each machine draws." 2 "Check pressure after a cold night and compare." 3 "A survey scan shows ice before you build." 4 "Treat meltwater first, then use it for planters." 5 "Watch the Temp row when emitters stop." 6 "The Air window shows how much oxygen is safe." 7 "Wear events show where upkeep went."
Reply texts (<=60 words; `{...}` are stored values; draft, SME check pending for "warm machines melt hidden ice" and the meltwater salt line):
1. Hana: "Hana here. {d} units a sol, and the dome holds. Nice work. Every machine you add draws more power, so keep an eye on the battery. Heater coils are on their way for your Outgasser."
2. Mensah: "Keeper, this is Sefa Mensah at mission control. Your report came in with the ISS desk. {existing earth.report1.reply Everest text}"
3. Basu: "Basu here. Warm machines melt ice you cannot see, and {n} blocks sank near yours. A survey scan shows where the ice lies before you build. New pump bearings are on the way."
4. Basu: "Basu again. Meltwater carries salts, so treat it before you drink it or plant in it. Warm, wet places draw life, so look around your wet machines. Resin packs are on the way."
5. Mensah: "Mensah here. A thick enough sky keeps heat in. Yours is getting there. {existing Report 2 Earth link, phase-3.md}"
6. Moreno: "Moreno here. You turned one gas into another and a world began to breathe. More oxygen is not always better, so watch the Air window. Signal boards are on their way."
7. Mensah: "The fleet reads your numbers tonight. A steady planet is one that gets care." Then the existing Moreno "Your call now" hand-over (story bible 5.4). The activation cell lands with the BELL receipt.
Gate 1 deliberately has Hana, not Mensah, so Mensah's first message stays at Phase 2 begins.

## 7. Link to the supply drops (the code change that matters)
| Today | New |
|---|---|
| Gate cache list granted on latch, once per player per planet (`CacheLedger`) | Gate latches as before. Grant moves to a release event per planet and gate, reason REPORT, TIMER or ADMIN. Each player claims their cache once from a released gate (per-player ledger stays) |
| Gate JSON cache lists | Unchanged (ruling v2 section 5) |
| Briefing trigger "Report sent or deferred" | "Release of that gate" |
| Placement lock | Unchanged; follows the gate |

## 8. Reuse
- Planet report page (`gate-input-items.md` 7): step 1 is that page; same stored snapshot (PlanetState schema bump). First build may store only "values at this latch" and take "before" from the previous gate.
- Keeper's Record: one page per gate, "What we found at {gate}". Fills for the filer only when three picks are made (own action, CR-R1). Others: read-only copy.
- One report per gate per planet; planet state holds filed_by (Keeper number, never a name) and filed_at. "First filer gets credit" is a record, not a prize.
- Journal: tab "Reports" lists seven pages with a dot for ready.
- Forecasts: claims and replies never state a forecast answer (CR-F9).

## 9. Data and state
Pure model (no Minecraft imports; ArchUnit as for `mars.core`): package `mars.learning.report`: `ReportDef` (record from JSON: gate id, focus rows, side-line key, options with correct/supported flags, voice, reply key, reveals, goal ids), `ReportState` (per planet and gate: snapshot values, filedBy, filedAtSol, 3 picks, releasedAtSol, releaseReason), `ReportRules` (release(latchedAtSol, filedAtSol, nowSol, timerSols); canFile(def, openForecasts); compose(def, picks, values)).
Files: `pack/data/mars/story/reports/<gate>.json` (7) in the style of `story/journal/*.json`; text under lang keys `report.<gate>.*`; `timing.json` (auto_release_sols 2.0, uplink_seconds 45, reply_sols 0.5, each marked INVENTED).
State: `ReportLedger` inside per-planet `PlanetState` (schema bump); per-player claim flags stay in `CacheLedger`; Record fill in the existing Record store. Logging local and anonymous: pick ids only.
Shape example: `{"schema":1,"id":"mars:reports/thaw","unlock":{"gate":"mars:thaw"},"focus":["mars:temperature","mars:hydrosphere"],"side_line":"report.thaw.side","cause":[{"id":"melt","key":"report.thaw.cause.melt","correct":true}],"evidence":[{"id":"side","match":true}],"claim":[{"id":"hidden_ice","supported":true}],"voice":"basu","reply_key":"report.thaw.reply","reveals":["f4b"],"goal_ids":["B1","D2","B5","C3"]}`

## 10. Slices and tests (effort UNVERIFIED)
- R1 (1 to 1.5 d): ReportRules, ReportState, ledger, release event, move the cache grant to release, timer, admin latch releases at once, BELL receipt, dev command `/mars report file <gate>`. Done when: latch gate 1 gives no drop; filing gives it once; advancing 2 sols gives it once; the request item still works.
- R2 (2 d): Monitor page with the 5-step screen, 7 defs and lang keys, feedback composition, snapshot store, reveals check. Done when any latched gate can be filed, the reply arrives after 0.5 sol, and a second player sees "already filed".
- R3 (1 d): journal Reports tab, Record fill (filer only), briefing trigger on release, late path, pick-id log.
- R4 optional (1 to 2 d): Comms Terminal block, co-sign.
Tests: (1) release returns REPORT if filed before the timer, TIMER if not, never before latch; (2) every cache drop granted exactly once per player per planet whatever the reason; (3) text scan: no banned words (CR-T1, CR-T5), exactly one correct option per list, reveals ids exist, word budgets; (4) the correct cause key equals the report-page "Why" line key; (5) release code never writes a parameter (tech, not progress); (6) boot with two players: second filer gets "already filed", Record fills only for the first; (7) CR-F9: a report with an open listed forecast is not offered. Note: the boot check "latch then items arrive" must become "latch, then file or advance 2 sols".

## 11. Walkthroughs (design walk, not real players)
12-year-old: a dot appears at gate 1, they read two lines, tap three shape-labelled buttons, Send. Coils land, Hana talks about power. Risk: tapping the first button every time; the reply still names the real cause. 17-year-old: files in 40 s, uses evidence to check numbers; at gate 3 skips filing and gets bearings 2 sols later, losing nothing. Risk: treats it as a checkbox; drops never change with picks, so nothing is gamed.

## 12. Owner decisions (taken as the recommended option, 2026-11-28)
A. Auto-release timer 2 sols (40 min, INVENTED). B. Monitor page first, Comms Terminal block only if playtest wants it. C. Filer only gets the Record page in R1 to R3; co-sign in R4. D. ISS as the Earth-side science desk with the label, never the sender. E. Hydrosphere side line is the salt level. F. Keep the three Keeper's Report reflections as an optional second card at gates 2, 5, 7.

## 13. Proposals (not applied)
- TEAM.md term: Findings report = structured choice-based report to orbit at each gate, one per gate per planet; release = the moment a gate's drop and next briefing are delivered (report, timer or admin). Keep "Keeper's Report" for the personal reflection.
- Decision Log: "The Findings report speeds the gate's drop and briefing; a 2-sol timer always delivers them. Rationale: owner request, teaches C3 and B1 at every gate, never hard-blocks. Alternatives: report required (hard block, breaks story rule 1); free-text report (parsing, name scrubbing); no report. Trade-off: seven reports of content and one changed trigger. Revisit if playtest shows clicking without reading, or never opening it."
- DESIGN.md fantasy audit: add "Findings report and ISS science desk (game design)".
- Outside this file: `phase-1.md` section 10 trigger; `recipe-inputs-ruling.md` section 5 "Fallback" column gains "timer release".

## 14. Risks
Players may click through without reading (mitigated by identical drops; learning value depends on the feedback line, to test in a playtest). Seven content sets plus replies add SME and Instructional Designer review load. Moving the cache grant from latch to release changes built, tested behaviour (keep the old path behind ADMIN). The 2-sol timer is INVENTED and could feel like a stall at gates 1 and 2 for a player without a coil. Naming the ISS may blur "the ISS cannot reach Mars" (label and Earth-side placement are the control). `reveals` mappings are a guess until checked against the forecast data.
