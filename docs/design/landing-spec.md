# Landing, Shift Briefing 1 and the Phase 1 journal (Stage 3 slice 4, build-ready spec)

**Owner:** Lead Game Designer · **Status:** **proposed**, not built, not playtested (design walkthrough only) · **Written:** 2026-11-27, docs only · **For:** Technical Lead (build), Codex (`mars/learning/**`), Lead Instructional Designer (wording check), SME (two science lines) · **Related:** [stage3-plan.md](../tech/stage3-plan.md) slice 4, [dome-layout.md](dome-layout.md), [messages/phase-1.md](messages/phase-1.md), [ui-specs.md](ui-specs.md) §0, §3, §8, §9.2, [story-bible.md](story-bible.md) §5.2, §6, [content-rules.md](../learning/content-rules.md), [balance-sheet.md](balance-sheet.md) §2d

**Tags:** *invented* (design number), *calc*, *sourced*, *unverified*, *proposed* (a decision waiting for the owner or a teammate). Goals: C1 (life-critical needs first, controls schema), A1 (why air matters), C2 (failure and repair, light touch), D2 (Earth link), B1 (every change visible). Pillars: the planet is the quest log; every change is visible; pressure, not grind; compact before wide.

**What is not in this file:** the "Useful now" lists and the Next goal row for an unmet temperature condition (both are slice 4 rows in the plan but were not part of this request); the first Outgasser worked example (`messages/phase-1.md` §4, already specified); forecasts F1 to F4a.

---

## 1. Decisions (all proposed)

| # | Decision | Why (one line) | Alternatives | Revisit if |
|---|---|---|---|---|
| L1 | **Landing is eight steps, per player, any order, nothing can fail.** The active step is the lowest pending one; a later step still ticks if done early | Controls first, no schema needed; experienced players are not held in a line | Strict order (blocks Lens B), team-wide steps (one player does all) | Playtest S1 shows players skipping the same step |
| L2 | **The Landing grace is the clock, sol 0 to 1.2 (24 minutes of play), not "until the checklist is done".** This is what the Technical Lead built (`life_support.json` `landingEndSol` 1.2) and what balance-sheet 2d.7 timing needs. `dome-layout.md` §5.8 ("starts when Landing is done or skipped") is **replaced by this** | Table timing (Dome stable at sol 9.2 solo) assumes t0 = 1.2; a slow player is still safe because the tanks start full and Hab B is not a required volume | Grace until the last present player finishes (moves t0 for slow groups, breaks the table); owner question Q2 | Median Landing time in S1 is over 20 minutes |
| L3 | **Step 6 text is "Place your two Ovens. Check Power on the Monitor."** The Battery has no switch (it is a plain grid block, pre-placed in the first slot), so "Switch on the battery" would ask for an action that does not exist. The battery stays in the hint and gets the outline. Detection: two Ovens on the planet and the grid reports both **powered** | Resolves open item (c); keeps cause then effect (place, then read the Power row); owner's "battery and Ovens" is kept as the hint | Add a battery on/off state (a new mechanic with no learning goal; cut) | A playtester asks what the battery does |
| L4 | **Rose-sky label unlocks at the latch of `mars:dome_stable`, or at the first Outgasser placement, whichever comes first** (creative and `/mars gate latch` placements bypass the gate). It unlocks silently (unread dot) with one chat pointer shared with the Dome stable entry. The same text repeats in the Phase 2 begins gate entry (SME's original place) | Resolves open item (a). The sky tint starts moving with the first pressure rise, and the Outgasser (the lever that moves it) unlocks at that latch, so the label is in the journal before any visible change | Phase 2 begins only (too late, the tint is visible by then); a Landing entry (13+ sols too early, forgotten) | Playtest: player says the sky "turned pink" before reading the entry |
| L5 | **Dome stable "On Earth" entry: trigger is the latch of `mars:dome_stable`, once per planet, delivered to every player's journal (late joiners get it on join); no Keeper's Record page (planet-scope event, CR-R1). Replacement sentence: "On Earth, air pushes on you with more than 100 times the force of the air outside this dome. You never notice, because your body pushes back."** The Instructional Designer's "about 100 times" is a little low: 101.3 / 0.636 is about 159 (*calc*), so "more than 100 times" is true and simpler to defend. Second sentence is **pending SME confirmation**; fallback if the SME declines it: "You never notice, because you grew up in it." | Resolves open item (b); the old "protects us for free" was vague (pressure or radiation?) | Keep the old line (SME flagged it) | SME answer |
| L6 | **Journal is the vanilla written-book screen, filled by the server per player.** No custom screen in Stage 3. Entry data lives in JSON so the Stage 4 custom journal reuses it | Cheapest honest option; see §5.1 | Custom screen, chat log, static lectern book | Stage 4 journal build; or a playtester cannot find old entries |
| L7 | **Two steps degrade honestly until their blocks exist.** Step 2 (suit O2) is the **air tank read-out** now and the suit text later; step 7 is **out and back through the airlock** now and adds the dust-off station later. Each step has a data flag (`suits_built`, `dust_off_built`) that switches the text. Nothing in the spec promises a thing that is not in the game | Suits, suit O2 and the dust-off station are not built (slice 2 report, slice 6 plan) | Build a suit stub in slice 4 (owner question Q3) | Suits land |
| L8 | **A Landing patch is not C2 evidence.** The patch emits `landing.step`, not `failure.fixed`. C2 needs a fix the player found without a scripted prompt (LEARNING 4.8) | Keeps evidence honest | Emit `failure.fixed` with a guided flag (the event has no such field) | Codex adds a `guided` field |
| L9 | **Step 5 says "has a crack", not "is leaking".** Hab B is not a required volume, so it loses no tank air; the Monitor's Air row stays steady. "Leaking" would fail CR-H1 (the claim must be true of the simulation) and a careful player would notice | Truthful text | Make Hab B required (breaks the layout) | Hab B becomes required |

---

## 2. The seal kit (item)

| Field | Spec |
|---|---|
| Id and name | `mars:seal_kit`, "Seal Kit". Stack 16 |
| What it is | A sealant patch cartridge. **[FICTION: stand-in]** No science claim is made; the journal does not describe how it works |
| Use | Right-click a block in tag `mars:patchable` (today only `mars:hull_plate_cracked`). The block becomes `mars:hull_plate`, one kit is used (not in creative), a short sound plays, the action bar says "Patched." The sealing check re-runs through the existing block-change hook (the slice 2 GameTest already proves break and place; a replace is the same hook) |
| Use on anything else | No change, no item used. Action bar: "A seal kit patches a cracked hull plate." |
| Recipe | Shapeless, 1 iron ingot + 1 brick (the sheet's "1 Fe, 1 B" stand-ins, balance sheet 2d.3; 2 items, fits the grid; same in classroom) |
| Where the first ones are | A vanilla barrel named "Locker" at the `L` cell of Ops (`dome-layout.md` §2), filled by `StarterDome` with **group.size + 1** kits (at most 6; *invented*). One is the Landing kit, one spare. Not restocked; the recipe covers loss |
| Why not just place a plate | A kit patches in place (no gap while you work), costs 2 items instead of 2 plates, and teaches "patch the weak plate" for later breaches. Breaking the cracked plate and placing a hull plate still works (and is what the slice 2 GameTest does) |
| World trace (optional, cheap) | A vanilla sign beside the crack: "Cracks start at corners. —H." (Hana, story bible §4.3). Not a hint of the fix |
| Goals | C1, C2 (light), A1 (a sealed room holds air) |
| Useful now | "Seal kit" is already first in the Dome stable "Useful now" row (`ui-specs.md` §3) |

---

## 3. Landing (BELL's first-shift checklist, story beat 0)

### 3.1 Principles

- In story: BELL's first-shift checklist. Controls first, science second; **no science question and no forecast before it is done or skipped** (DESIGN.md, Landing).
- **Nothing fails.** Every step can be skipped; none has a timer; the dome is in grace (3.6).
- Per player. Saved with the player in the planet's world data, like the starting grant (a UUID is fine there). **The learning log never holds the UUID** (it uses the pseudonym; Codex's `PlayerId`).
- Not a modal. Panel on the left or right edge, never over the centre of the screen (ui-specs §0).
- Design estimate: 15 to 25 minutes with exploring, about 5 for a focused player (*invented*; walking is about 150 blocks in total, under a minute at vanilla speed, *calc, unverified*; reading and looking dominate). Measure in playtest S1; exit target under 25 minutes (DESIGN.md).

### 3.2 State machine (per player)

```
NOT_OFFERED --(first join on a planet, after the opening card)--> OFFERED
OFFERED --[Start checklist]--> RUNNING
OFFERED --[I know the drill]--> SKIPPED_ALL
RUNNING --(every step is DONE, AUTO or SKIPPED)--> DONE
RUNNING --(/mars landing skip, or the panel's Skip all)--> SKIPPED_ALL
DONE or SKIPPED_ALL --> briefing.shift1 shows once --> (state is final; never offered again)
```

Each step has its own status: `PENDING` -> `DONE` (the player did it) | `AUTO` (the world was already in that state, for example a teammate patched the crack) | `SKIPPED` (the player skipped that step). `landingState` per player = {phase, steps[8], stepActiveSince}. The active step is the lowest `PENDING`. A later step that is done early ticks at once (L1).

**Persistence.** Survives logout, restart and `/mars pause`. A player who leaves in RUNNING resumes in RUNNING. A late joiner (any time after the first player) starts in OFFERED. After sol 1.2 the offer still appears (nothing in it is time-limited).

**Group rule.** Steps tied to planet state (5 Hab B sealed, 6 two Ovens powered) become `AUTO` for a player who reaches them after a teammate did them. `AUTO` is logged as such and credits nobody. Ovens are granted once per world (balance sheet 2c decision 2), so in a group only one player holds them; the others' step 6 becomes `AUTO` when the planet has two powered Ovens, or they use **Skip this step**.

### 3.3 The eight steps

"Detected when" is in game state the Technical Lead can read. All positions come from `DomeTemplate`; "Ops", "shelter", "Hab B" are template rooms.

| # | Trigger (step becomes active) | Completion condition (server side) | Text (word cap 12) | Hint (after 3 min active, plus a distance chip) | Notes and fail-safe |
|---|---|---|---|---|---|
| intro | First join on a planet, after the opening card | Player presses **Start checklist** or **I know the drill** | "Storm's over, Keeper. Damage logged. First-shift checklist is ready." | n/a | The two buttons are the last screen of the opening card (3.10). `/mars landing start` and `/mars landing skip` do the same |
| 1 Move, open a door | OFFERED -> RUNNING | The player right-clicks a `mars:hull_door` and the door opens (`RightClickBlock` on `HullDoorBlock`, door state `OPEN` true after the click) | "Walk to the shelter door and open it." (8) | "W A S D to walk. Right-click the door." | Teaches walk, look and use. The shelter door is the one in the shelter's east wall (x7, z4) |
| 2 Air supply | Any time | **Now:** right-click a `mars:o2_tank`. The action bar shows "Tank: 14 of 40" (new key `tank.level`, the player's own tank and its capacity). **Later (`suits_built`):** the suit's O2 is refilled at a wall tank | **Now:** "Right-click the air tank by your bunk. Read the level." (10). **Later:** "Find your suit O2. Top it up at the wall tank." (10) | "The tanks stand along the shelter's south wall." | Teaches that O2 is a stored supply (C1, A1) before the Monitor. No number the player must remember |
| 3 Monitor | Any time | The server receives the Monitor-open request from this player and sends the view (`MonitorSender`). No reading check: opening is the completion (a quiz here would be a science question before the end of Landing) | "Open the Planetary Monitor. Read the outside air." (8) | "The Monitor is on the north wall of Ops." | The Monitor must show an **Outside air** line in Phase 1 (see 3.11, item T4). The journal entry on air pressure unlocks now (5.3) |
| 4 Seal kit | Any time | The player's inventory holds at least one `mars:seal_kit` (checked once a second while the step is active, and on pickup) | "Take a seal kit from the locker in Ops." (9) | "The locker is the barrel in Ops, south-west corner. Right-click it." | Teaches containers. If the locker is empty (kits used or `/give` skipped), the step shows "Craft one: 1 iron ingot and 1 brick." in the hint only |
| 5 Patch | Any time | The cracked plate is replaced by a hull plate **by this player's kit** (`DONE`), or the Hab B volume already reads SEALED when the step becomes active (`AUTO`). `SealChangedEvent` (not breach) for Hab B marks the world change; the kit's use handler remembers the player | "Patch the crack in Hab B with your seal kit." (10) | "Hold the kit. Right-click the cracked plate in Hab B's west wall." | BELL line (3.8). Does **not** emit `failure.fixed` (L8). The crack may be broken by hand and replaced with a plate instead: counts as `DONE` too |
| 6 Ovens and power | Any time | The planet has at least two `mars:regolith_oven` machines and the power grid reports both **powered** (`PowerGrid.State.POWERED`). `DONE` if this player placed one of them (machine.built); `AUTO` if both were placed by others before | "Place your two Ovens. Check Power on the Monitor." (9) | "The battery and panel power them. The east bays are marked." | If an Oven shows short on power the hint becomes "Ovens need power. Is a panel running?" (no fix named beyond the cause line). BELL line (3.8) |
| 7 Airlock | Any time | The player was **outside** (more than 2 blocks outside the dome's bounding box, from the template) and is **inside** again, and used a hull door on the way (the airlock's outer door). **Later (`dust_off_built`):** also right-clicked the dust-off station after coming in | **Now:** "Go out through the airlock and come back in." (9). **Later:** "Go out through the airlock. Use the dust-off station coming back." (11) | "The airlock is at the south end of the dome." | No suit or air rule applies yet (suits not built; the airlock interlock is slice 6). "Passes the weather mast" in the older text is dropped: the mast is not built. The first-trip journal notes wait for the first trip after Landing (`messages/phase-1.md` §12) |
| 8 Journal | Any time | The player right-clicks their journal book (3.10 and 5.1) | "Open your journal from the hotbar. Your notes live here." (10) | "Press its number key, then right-click." | The book is in the starting grant (5.1), so it is in the hotbar from join |
| done | The last step ticks | n/a | "Checklist done. Logging." | n/a | Then briefing.shift1 (section 4) |

Per-step **Skip this step** is always available (a small link in the panel, or `/mars landing skip_step`), so nobody is stuck on a missing item. Skipping is logged (3.9) and is never wrong.

### 3.4 What each step teaches (controls only)

| Step | Control or schema learned | Goal |
|---|---|---|
| 1 | Walk, look, right-click a block | C1 (controls schema) |
| 2 | Right-click reads a block; O2 is a stored supply | C1, A1 |
| 3 | The Monitor is where the planet's numbers live | C3 (precursor), A1 |
| 4 | Open a container, pick up an item | C1 |
| 5 | Use an item on a block; a sealed room is a fix you can see | C1, C2 (light) |
| 6 | Place a machine; power has to reach it; read the Power row | C1, B1 |
| 7 | Doors in sequence, going outside | C1 |
| 8 | The journal exists and is a book you carry | C1 |

### 3.5 Skip rules

| Rule | Spec |
|---|---|
| **I know the drill** (intro button, `/mars landing skip`, panel "Skip all") | All unfinished steps become `SKIPPED`. BELL shows three lines (below), then the briefing. The skipper still gets the journal book, the Ovens and the kit access: skipping withholds nothing |
| **Skip this step** | Marks only that step `SKIPPED`; the panel ticks it with a dash |
| Skipped steps and the world | Hab B keeps its crack if nobody patched it. It is not a required room, so nothing is lost. BELL logs the crack once on the Monitor's Dome line; Hana's sign stays at the crack. The first seal-breach failure report (slice 6) carries her tip |
| Time | No timers. A step shows its hint after 3 minutes active (*invented*) |
| Nothing fails | No step can be failed, only done, auto or skipped. No health, air or item is at risk during Landing |

Skip summary (three lines, `landing.skip.*`; the first and third switch with `suits_built` and `dust_off_built`):

| Key | Text now | Text when the block exists |
|---|---|---|
| `landing.skip.1` | "Air tanks line the shelter wall. Right-click one to read it." | "Suit O2: bubble icon. Refill at wall tanks." |
| `landing.skip.2` | "Monitor: planet data. Journal: your notes and messages." | same |
| `landing.skip.3` | "Seal kits patch cracked plates. The Ops locker has some." | "Dust off after every trip outside." |

### 3.6 What the dome does meanwhile (grace)

From the built data (`life_support.json`, slice 2 report decision 3): from sol 0 to sol 1.2 (24 minutes of play at 20 minutes a sol) **nothing is made, used or leaked**, and `air_ok`, `water_ok` and `power_ok` are all false, so no Dome stable hold can start.

| Question | Answer |
|---|---|
| Air consumed | None until sol 1.2. Start fill: 14 O2 per player (1.75 sols of use), water 60, battery 5 (balance sheet 2d.2) |
| What the Monitor says | The Dome stable goal row reads "Waiting: landing" (existing key `monitor.gate.dome_stable.waiting.landing`). Air and Water rows are full and steady |
| If the player is still on the checklist at sol 1.2 | Nothing happens to them. Use and making start together; the pre-built makers cover use for solo, and for a group of 4 the tanks last about 4.7 sols (balance sheet 2d.7, *calc*). Hab B's crack never drains a tank (L9) |
| A fast player or a skipper | Gains nothing in time (the clock decides, L2) but may use the rest of the grace to build (extra panels, makers, crafting). BELL does not announce the end of grace |
| Pause | The grace clock is the simulation clock, so `/mars pause` stops it |
| Late joiner after sol 1.2 | Offered the checklist as normal. Nothing in it needs the grace |
| A group | One grace for the planet. Every player runs their own checklist inside it |

### 3.7 Voice (BELL)

BELL is calm, exact, never guesses, never jokes or comforts (story bible §4.1, §8.2). It speaks in short colony-log lines (at most 20 words, at most about 3 a minute). Ticks are **silent** (a box fills); BELL speaks only when the world has changed, which keeps Landing quiet and makes the two cause-and-effect moments clear.

| Key | When | Text (BELL) | Check |
|---|---|---|---|
| `landing.intro` | OFFERED | "Storm's over, Keeper. Damage logged. First-shift checklist is ready." | Story beat 1 asserts at least one open breach: true (the crack) |
| `landing.log.5` | Step 5 `DONE` or `AUTO` | "Hab B sealed. Cracked plate replaced." | Asserts the volume reads SEALED |
| `landing.log.6` | Step 6 `DONE` or `AUTO` | "Two Ovens on the grid. Power use is up." | Asserts both powered |
| `landing.done` | DONE | "Checklist done. Logging." | none |

### 3.8 Texts, one table (for `en_us.json`; the Instructional Designer owns the grade 6 to 7 pass, **not run by me**)

| Key | Text | Words |
|---|---|---|
| `landing.intro` | Storm's over, Keeper. Damage logged. First-shift checklist is ready. | 9 |
| `landing.button.start` / `.skip` | Start checklist / I know the drill | 2 / 4 |
| `landing.step.1` | Walk to the shelter door and open it. | 8 |
| `landing.step.2` | Right-click the air tank by your bunk. Read the level. | 10 |
| `landing.step.2.suit` | Find your suit O2. Top it up at the wall tank. | 10 |
| `landing.step.3` | Open the Planetary Monitor. Read the outside air. | 8 |
| `landing.step.4` | Take a seal kit from the locker in Ops. | 9 |
| `landing.step.5` | Patch the crack in Hab B with your seal kit. | 10 |
| `landing.step.6` | Place your two Ovens. Check Power on the Monitor. | 9 |
| `landing.step.7` | Go out through the airlock and come back in. | 9 |
| `landing.step.7.dust` | Go out through the airlock. Use the dust-off station coming back. | 11 |
| `landing.step.8` | Open your journal from the hotbar. Your notes live here. | 10 |
| `landing.hint.1` to `.8` | See 3.3 | 7 to 12 |
| `landing.skip_step` | Skip this step | 3 |
| `tank.level` | Tank: %s of %s | 4 |
| `seal_kit.patched` / `.no_target` | Patched. / A seal kit patches a cracked hull plate. | 1 / 8 |

No banned words (CR-T1), no symbols for words (CR-T7b), US spelling, every key under the 12-word cap (CR-T4). Box glyphs in the panel use plain ASCII (`[ ]`, `[x]`, `[-]`) so no font glyph needs checking; the state is also a word for screen readers and colour-blind players is never the only signal (a dash and a check differ in shape).

### 3.9 Events for the learning log

Names follow ARCHITECTURE section 8.3's list where one exists. `ActionEvent` today has five kinds (`machine.built`, `machine.removed`, `monitor.viewed`, `gate.reached`, `failure.reported`); subjects must match `mars:[a-z0-9_]+`; player-scoped kinds carry a player, planet-scoped kinds none. Landing events measure **no goal** (they are controls telemetry; the Instructional Designer decides if any feeds C1).

| Event | Kind status | Scope | Subject | Emitted when |
|---|---|---|---|---|
| `machine.built` | exists (`MACHINE_BUILT`) | player | `mars:regolith_oven` | An Oven is placed (hook is open on Codex's list, not built) |
| `monitor.viewed` | exists (`MONITOR_VIEWED`) | player | `mars:monitor_main` | The Monitor opens for the player (hook not built) |
| `gate.reached` | exists (`GATE_REACHED`) | planet | `mars:dome_stable` | Gate latch (hook not built) |
| `failure.reported` | exists (`SEAL_BREACHED`) | planet | `mars:seal_breach` | Built (breaks only; the starting crack is not a break event, it is the initial state: **do not emit at world creation**) |
| `landing.step` | **new kind** | player | `mars:landing_step_1` to `_8`, with suffix `_auto` or `_skipped` when not `DONE` (for example `mars:landing_step_5_auto`) | A step leaves `PENDING` |
| `landing.skipped` | **new kind** | player | `mars:landing_all` | "I know the drill" or Skip all |
| `landing.done` | **new kind** | player | `mars:landing_done` | Phase becomes DONE (not on skip-all) |
| `briefing.opened` | **new kind** (listed in ARCHITECTURE 8.3) | player | `mars:briefing_shift1` | The card is shown or the page opened |
| `journal.opened` | **new kind** (listed in 8.3) | player | `mars:journal_contents`, or `mars:journal_<entry id>` for an entry page | The book is opened (the server sees the right-click, not the page turns, so the subject is the contents page; per-entry subjects only if the page change is detectable) |

Elapsed time per step comes from the timestamps (session and sol are already in every event). `failure.fixed` is **not** emitted by the Landing patch (L8).

### 3.10 UI surfaces (cheapest honest, in order of cost)

| Surface | Spec | Cost note for the Technical Lead |
|---|---|---|
| Intro | The last screen of the opening card, with two buttons. Same screen for everyone; commands do the same | One small client screen. If the opening card does not exist yet, use the commands plus the panel |
| Checklist panel | Pinned panel, one line per step: `[ ]` pending, `[x]` done, `[-]` skipped or auto; the active line is bold and shows its distance ("14 blocks, south"). A footer link "Skip this step" and "Skip all" | A client HUD layer (like the Monitor, new code). **Fallback with no client code:** a per-player sidebar sent by packets from the server. TL's call |
| World cue | None required. Soft outline and arrow icon on the target is Artist polish (ui-specs §0 rule: color never the only signal) | Not in this slice |
| BELL lines | The top line, one at a time (ui-specs §0) | Existing log line surface |

### 3.11 Things the Technical Lead must make true for Landing

| # | Item | Detail |
|---|---|---|
| T1 | `mars:seal_kit` item, recipe, use handler, `mars:patchable` tag | Section 2 |
| T2 | Locker | A named vanilla barrel placed by `StarterDome`, filled with group.size + 1 kits |
| T3 | Tank read-out | Right-click `mars:o2_tank`: action bar `tank.level` (player's O2 and capacity from the life model) |
| T4 | **Outside air line on the Monitor** | Static in Phase 1: "Outside air: about 95 % CO2, almost no O2" (text from the SME's Part A, same as `journal.dome_stable.airmix`). **Name clash:** the Monitor's `Air` row is the dome's tank; "Outside air" is the planet. If the panel is full (busiest state 156 px, no plot), put the composition on the pressure row's second line instead. This line did not exist in `en_us.json` when checked |
| T5 | Per-player Landing state and checks | 3.2, 3.3. Cheap checks: use events for steps 1, 2, 8; the Monitor request for step 3; a once-a-second pass for steps 4, 6, 7 only while a step is active |
| T6 | Flags `suits_built`, `dust_off_built` in a small data file | Switch text keys; both false today |
| T7 | Commands `/mars landing start|skip|skip_step|reset` | `reset` for the owner's playtests (admin) |

---

## 4. Shift Briefing 1

| Field | Value |
|---|---|
| `id` | `briefing.shift1` |
| Trigger | The player's Landing becomes DONE or SKIPPED_ALL. Once per player. A late joiner who finishes or skips later gets it then |
| When it shows | 3 seconds after "Checklist done. Logging." (or straight after the skip summary), in the side panel as a card. Not a modal. It stays until the player closes it or opens the journal; it never blocks movement. A copy goes to the journal under **Briefings** |
| Counts toward the pop-up cap | Yes (ui-specs §0), once. The first forecast cannot appear before it (DESIGN.md: no forecast before Landing is done) |
| Goals | C1, A1 (advance organizer) |
| `names` (CR-H5) | `air`, `water`, `power`, `life_support`, `relay` (the Dome stable unlock). The storm sentence is gated by `storms_built` (slice 5); until then it is not shown |

**Text, full (40 words, cap 45):**

> **Shift Briefing.** This shift: keep the dome alive. Watch air, water, and power. Dust storms cut solar power, so store some. Keep life support steady for {hold_sols} sols and the relay link comes back. If something nearly fails, ask why.

**Text until storms exist (32 words):** the same without "Dust storms cut solar power, so store some."

`{hold_sols}` is read from `dome_stable.json` (3 standard, 1 classroom), never typed. Changes from `messages/phase-1.md` §2: names air, water and power as the Monitor rows (the old "O2" is a letter-and-number the 12-year-old has not met); "run life support with no failures" becomes "keep it steady" (the gate tolerates short failures, balance sheet 2c decision 7); adds "store some" as the one planning cue; keeps "ask why" as the C2 habit.

**Check:** the text claims nothing about the planet, so no `asserts` condition. It reveals no forecast answer (CR-F9: F1's answer is "solar power drops", and "storms cut solar power" would reveal it, **so the storm sentence ships only with F1** and F1 is not issued before the briefing). Flagged for the Instructional Designer: confirm the storm sentence does not make F1 trivial.

---

## 5. The journal

### 5.1 What it is (minimal UI), and why

| Option | Cost | Honest? | Verdict |
|---|---|---|---|
| **A. Vanilla written-book item, rebuilt by the server for each player** (chosen) | Small: one item (a `written_book` with a custom name, or a thin `Item`), a builder that turns unlocked entries into pages, one use hook. **No client code**, works on a dedicated server | Yes for Stage 3: real reading UI (pages, wrap, clickable contents), one per player, cannot be edited (a signed book), carried in the hotbar | **Chosen** |
| B. Custom journal screen with tabs, pictures, Messages, Forecasts | Large (client screen, payloads, layout, font). It is the Stage 4 journal (`ui-specs.md` §8) | Yes, final | Later |
| C. Chat log (print each entry when unlocked, `/mars journal` to list) | Smallest | **No**: entries scroll away, a class cannot reread, and the story bible says nothing is lost in the archive | Rejected |
| D. A lectern with one shared book | Small | **No**: no per-player entries; breaks "journal is per player" (DESIGN.md group play) | Rejected as the journal. The Ops lectern `J` is kept as the place to **get a new copy** (right-click with no journal in your inventory) |

**How A works (for the Technical Lead):**
- The book is in the **starting grant** (like the Ovens), slot 9. Lost: right-click the Ops lectern, or `/mars journal give`.
- Unlocked entry ids are saved per player. The server rewrites the book's pages whenever an entry unlocks (and again just before it opens), so the player never sees stale content.
- **Page 1 is the contents.** It lists entries by section (Briefings, Shift notes, Game rules, later Messages), the new ones marked "New". Each line is a clickable page jump (vanilla supports page links in written books; not verified on 1.21.1 here, **unverified**, fall back to a plain list). The item name carries the count: "Keeper's Journal (2 new)".
- One chat pointer when something unlocks: "New in your journal: %s." (at most one per unlock event; entries that unlock together share one line; see 5.4).
- **Two depths.** Each entry is main text (MS and HS) then, for the high school band only, a page headed "Read more". The band is a per-player setting the Technical Lead exposes (default MS; the teacher sets HS). The book is rebuilt when the band changes. **Band source is not built** (the log has a `Band` field; no setting feeds it yet).
- Page budget: an entry of 45 to 80 words is about two pages (about 14 lines of about 19 characters each, *calc, unverified*). Phase 1's slice 4 entries are about 8 entries, under 25 pages, under the book's page limit.
- Honest gaps to the full spec (`ui-specs.md` §8): no tabs, no pictures or graphs (the Monitor plot covers the graph), no Forecasts tally, no Keeper's Record. These arrive with the Stage 4 screen; **entry data is JSON** (`pack/data/mars/journal/*.json`: id, section, trigger, goal_ids, `text.ms`, `text.hs_more`, `fiction_note`, `earth_line`, `reveals`, `names`) so the screen reads the same files.

### 5.2 Entry list (Phase 1, slice 4)

Entries and their triggers. "Shown" = unlocked in the book. Every entry carries `goal_ids`, `reveals` (none unless stated) and `names`. Entries still to come in later slices keep their triggers in `messages/phase-1.md` (§8 F4a Earth link, §12 four notes, §14 dust).

| Id | Section | Trigger | Goals | Text depth |
|---|---|---|---|---|
| `journal.about` | Shift notes | Join (the book's first page) | C1 | MS only: the fiction line |
| `journal.sol` | Shift notes | Step 8 (first journal open) | C1 | MS only |
| `journal.a1.pressure` | Shift notes | Step 3 (first Monitor open) | A1 | MS + HS read more |
| `journal.power` | Shift notes | Step 6 `DONE` or `AUTO` | C1, B1 | MS only |
| `journal.gamerules` | Game rules | Landing DONE or SKIPPED_ALL | C1, A1 | MS only |
| `briefing.shift1` | Briefings | Briefing shown (section 4) | C1, A1 | MS only |
| `journal.dome_stable` | Shift notes | Latch of `mars:dome_stable` | A1, D2 | MS + HS read more |
| `journal.sky.stylized` | Shift notes | Latch of `mars:dome_stable` or first Outgasser placed (L4); text repeated at Phase 2 begins | A1 | MS only (SME verbatim) |

### 5.3 Entry texts

Format for every gate entry (story bible 6.3): **what you saw, the real science, fiction note if needed, "On Earth", read more.** Word caps: entry 80 (story bible 6.1). Counts below are mine (script), the grade 6 to 7 screen is the Instructional Designer's.

**`journal.about`** (first page; MS and HS)
> This story is fiction. The science is as real as we could make it. Where the game bends real life, the journal says so.

(24 words.) The opening-card line, verbatim from the story bible. Fiction notes for speed, power and fauna come with their own entries later (story bible 3.5).

**`journal.sol`** (MS and HS; **unverified**: the 24 h 40 min figure is common knowledge, not sourced in the repo; SME to confirm)
> A sol is one Mars day. It lasts about 24 hours and 40 minutes. In this game, a sol is 20 minutes of play.

(24 words.) Time compression named at first use. BELL uses "sol" from the first line.

**`journal.a1.pressure`** (trigger: step 3)
> **Air pressure.** Air has weight, so it pushes on everything. How hard it pushes is air pressure. We measure it in kPa (kilopascals). On Earth at sea level, air pressure is about 101 kPa. Outside Cairn Dome it is about 0.6 kPa. That is very thin air.

(45 words.) *Read more (HS):* "1 kPa is 1,000 pascals. A pascal is one newton of push on each square meter. Pressure comes from gas molecules hitting a surface, so thin air hits less often and less hard." Sources: 0.6 kPa and 101 kPa, SME Part A (sourced, `docs/science/accuracy-review.md`). The molecule line is standard physics (**SME to confirm wording**). `reveals`: none (static values; no forecast asks the starting pressure).

**`journal.power`** (trigger: step 6)
> **Power.** Solar panels make power. A battery stores extra power for later. Machines use power every sol. **Game rule:** when power runs short, life support gets it first. Check the Power row on the Monitor to see what you make and what you use.

(43 words.) "For later" avoids claiming a night cycle: there is no night on the grid yet. The game-rule label follows balance sheet 2d.9 (the words "Game rule" are in the text, not in small print). `names`: solar panel, battery, Power row (all exist).

**`journal.gamerules`** (trigger: Landing DONE or SKIPPED_ALL; **the game-rule labels**, balance sheet 2d.9)
> **Game rules.** Some rules make the game gentler than real life. Landing grace: nothing runs out while you do the checklist. Blackout: if your air runs out, you wake in the shelter and lose nothing. Real low oxygen is dangerous, and your body may not warn you.

(45 words.) Covers the two game rules that exist now (Landing grace, Blackout). "Free suit refill" and "Dehydrated" get their line when suits and thirst exist (the entry grows; no new entry). Source for the last sentence: SME `life-support-review.md` §4 (sourced; no medical detail used). **Inconsistency to fix:** `en_us.json` `life.blackout` says "you wake up in your bunk"; the code wakes the player at the shelter spawn (slice 2 decision 13). This entry says "in the shelter" because that is true; the Technical Lead or Instructional Designer should align the Blackout line.

**`briefing.shift1`** in the book: the card text of section 4, under Briefings.

**`journal.dome_stable`** (trigger: gate latch; delivered to every player on the planet)
> **Dome stable.** Air, water, and power held for {hold_sols} sols. *Outside air: about 95 % carbon dioxide, almost no oxygen, and very thin. You can't breathe it.* *On Earth, air pushes on you with more than 100 times the force of the air outside this dome. You never notice, because your body pushes back.*

(Seen 10, air 17, Earth 27: 54 words.) The middle line is `journal.dome_stable.airmix` (SME Part A: CO2 95.1 %, O2 0.145 %). The Earth line replaces `earth.gate.dome_stable` (decision L5). *Read more (HS):* "Earth at sea level is about 101 kPa. Outside Cairn Dome it is about 0.6 kPa. Your hull holds back that difference." (The old "has to make up the difference" is replaced: the dome does not make pressure here, the hull holds it; Instructional Designer to confirm.) Rules: no Keeper's Record page fills (planet-scope, CR-R1); the wording does not depend on the stand-in state (CR-H5); a late joiner gets the entry on join. `reveals`: none.

**`journal.sky.stylized`** (trigger: L4; **SME-required wording, verbatim from `docs/science/round2-review.md` §2.1, both bands, do not edit**)
> More air scatters more blue light, so the sky slowly loses its dusty tan. Nobody has seen a thickened Mars sky. These in-between colors are our best guess, made stronger so you can see them.

(35 words.) It replaces the older text of `journal.sky.stylized` in `messages/phase-1.md` §12. It also appears in the Phase 2 begins gate entry. `reveals`: none (it describes sky colour, not the rate of pressure, so it does not answer F3; the delivery precedes F3, which opens at the first Outgasser Start). Goals A1; no D2 claim.

### 5.4 Delivery rules

| Rule | Spec |
|---|---|
| Unlock is silent plus one pointer | Each unlock sets the unread dot and the "(n new)" count. At most one chat pointer per moment: when several entries unlock together (the Dome stable burst: the Dome stable entry, the sky entry, Outgasser unlock, Hana's message once it exists), **one** line: "New in your journal: Dome stable, the sky." (11 words). This answers the Instructional Designer's "burst" risk |
| During Landing | Unlocks are collected; no chat pointer until the checklist ends. Step 8 is the pointer |
| Never a modal | The journal opens only when the player right-clicks it (or the lectern) |
| Planet-scope entries (`journal.dome_stable`, `journal.sky.stylized`) | Delivered to every player on the planet; an offline player has them on next join |
| Personal entries (`journal.a1.pressure`, `journal.power`) | Per player, from that player's own action. A teammate's action gives it as `AUTO` only when the world was already in that state and the player opens the journal (no credit) |
| Keeper's Record | None of these entries fills a page (CR-R2: reading is not evidence) |

---

## 6. Content-rule checks (design review, not run in code)

| Rule | Result |
|---|---|
| CR-T1 banned words | None used ("checklist" and "briefing" are fine). Scan on the keys is still to run |
| CR-T4 lengths | Steps 8 to 11 words (cap 12); log lines under 20; briefing 40 (cap 45); entries 24 to 54 (cap 80). Counted by a throwaway script on the strings above, not a build check |
| CR-T5 positive wording | Pass: nothing says "you failed" or "wrong"; every state is done, auto or skipped |
| CR-T6 grade 6 to 7 | **Not run.** The one risk word is "kilopascals" (defined on the spot) |
| CR-T7 | US spelling ("color", "Leveling" untouched here); no "~" or "<" |
| CR-F9 / CR-T8 | No forecast open during or before Landing. The storm sentence of the briefing waits for F1. The sky label and the A1 entry give no forecast answer (declared `reveals: none`) |
| CR-H1 | Step 5 says "has a crack" only while the crack exists; BELL's line 5 and 6 fire only when the volume or the grid says so (L9) |
| CR-H2 | Hints name the place and the control, not a fix to a learning problem: Landing is controls. No Hana tip names the fix |
| CR-H5 | `names` fields declared for the briefing and entries; dust-off station and weather mast are not named until built |
| CR-R1 / CR-R2 | No Record page fills from any Landing or journal event |
| Fiction flags | Seal kit is a stand-in (section 2). No new fictional element needs the six-rule audit beyond the kit; **I add one row to the DESIGN.md audit table in the handoff (not edited here)** |

---

## 7. Who supplies what

### 7.1 Codex's `mars/learning` can already supply

(Read from `ActionEvent.java`, `LearningEvents.java`, and the HANDOFF note; not run.)

- The `ActionEvent` record with context (world id, planet, session, sol, simulation tick, band `MS`/`HS`, cause, pseudonymous `PlayerId`) and the **five kinds** above; the subject rule `mars:[a-z0-9_]+`.
- A session-scoped JSON Lines writer (`SessionActionLogs`), the pseudonym store, the seal-breach hook (planet scope).
- A `Band` enum (MS/HS) on every event, but **no player setting that sets it** (the journal needs one).

### 7.2 What Codex needs to add (small, `mars/learning/**`)

New kinds `landing.step`, `landing.skipped`, `landing.done`, `briefing.opened`, `journal.opened` (section 3.9); the placement, Monitor and gate hooks already on their open list (Oven placement, Monitor open, gate latch); the journal entry JSON schema (5.1) and a builder from entry ids to pages if they own "Journal content" (the package doc says so); the band setting. Codex decides whether the builder sits in `mars/learning` or `mars/story`.

### 7.3 What the Technical Lead must build

Seal kit, locker, tank read-out, Outside air line, per-player Landing state machine, the panel (or sidebar fallback), the intro buttons, commands, flags, the journal item and unlock hooks, the Dome stable latch triggers (and the first-Outgasser trigger), the first-grant of the journal book. The Landing needs the Monitor-open, Oven-placement and gate-latch hooks from Codex's list before it can emit events; the state machine itself needs none of them.

### 7.4 Not built yet and this spec works around

| Missing | Effect on this spec |
|---|---|
| Suits and suit O2 | Step 2 reads the tank (L7) |
| Dust-off station, airlock interlock | Step 7 is out and back (L7) |
| Weather mast | Dropped from step 7 |
| Machine recipes | Ovens come from the starting grant; the kit has its own two-item recipe |
| Storms and F1 | The briefing storm sentence waits (section 4) |
| Hana's relay message (beat 6) | Not in `messages/phase-1.md`; the briefing promises "the relay link comes back", so beat 6 needs at least a one-line BELL log ("Relay link restored.") at the latch. I list this as a small gap |

---

## 8. Design walkthroughs (not playtests)

**As a 12-year-old new to modpacks.** Opening card, then two big buttons; "Start checklist" is the obvious one. Step 1 tells me the keys. I open the door, click the tank (a number appears: nice), find the Monitor and the screen is busy, but the step only asks me to look. The locker and the kit are easy. In Hab B the crack is visible (hint gives the place and a distance). Ovens: I have them in the hotbar; I do not know where to put them, so the hint says "east bays are marked". I check Power. Airlock: two doors, out and in. Journal: the number key then right-click. **Risks:** step 6 (where is the east bay, what is a grid), and the Monitor being dense at step 3. **Mitigation:** the soft outline on the bay (Artist, later) and "Skip this step".

**As an experienced 17-year-old.** I press "I know the drill", read three lines, and get the briefing. I can place the Ovens and panels at once. I notice Dome stable cannot start until sol 1.2 ("Waiting: landing"), which is up to 24 minutes. **Risk:** feels slow if I finished in 3 minutes. **Mitigation:** I can build extra makers and a second panel during grace (the balance sheet shows a second maker brings Dome stable near sol 6), and the wait is the Monitor's visible reason, not a hidden timer.

**As a teacher with a class (group of 4).** Four players each run their own checklist; the crack gets patched once, the Ovens get placed once, and the others' steps show "done by the team". Everyone gets the briefing.

---

## 9. Questions for the owner (at most 3)

1. **Journal as the vanilla written book for Stage 3?** (ill-structured: cost against the multimedia and tabs of the full spec.) **Recommend yes.** It is cheap, safe on a server, per player, and the entry JSON carries over to the Stage 4 screen. Cost: no pictures, no tabs, no Forecasts or Record page for now.
2. **Should the Landing grace end when the player finishes the checklist, or stay at the fixed sol 1.2?** (well-structured once the table is read.) **Recommend fixed sol 1.2** (what is built). Ending it at "all present players done" would move Dome stable earlier for quick players and break the balance table timing; a fast player can build during the grace.
3. **Build a minimal suit (suit O2 and the 17-minute number) before Landing, or ship step 2 as the tank read-out?** (ill-structured: it touches balance sheet 2d, art, and the Blackout rule.) **Recommend the tank read-out now**, suits with slice 6 (airlock and dust-off). Cost: Landing step 2 and 7 teach less of the real Phase 1 loop until slice 6.

---

## 10. Tests and "done when"

| Test | Proves | Prove it can fail |
|---|---|---|
| Unit: Landing state machine (steps in any order, AUTO for planet-state steps, skip-all, skip-step, persistence round trip, late joiner) | L1 | Remove the any-order rule: a test that does step 5 first fails |
| Unit: every `landing.*` and journal text under its word cap; no banned words; no "~" or "<" | CR-T1, T4, T7 | Pad step 3 to 13 words |
| Unit: briefing `{hold_sols}` equals the gate JSON in both presets | Thresholds are data | Hard-code 3 |
| Unit: the sky text equals the SME's sentence byte for byte (a constant in a test) | The verbatim rule | Change "best guess" |
| GameTest: seal kit on the cracked plate seals Hab B; on other blocks does nothing and uses nothing | Section 2 | Make the kit consume on a wrong target |
| GameTest: the Landing grace holds (the existing slice 2 test) and a player with RUNNING Landing at sol 1.3 loses nothing | L2 | Set `landingEndSol` to 0 |
| GameTest: two fake players, one patches; the other's step 5 is `AUTO` | Group rule | Credit both |
| GameTest: the Dome stable latch unlocks both entries once per player and gives one pointer line | 5.4 | Send two lines |
| Boot check: a new world gives each player a journal in the hotbar; a lost one can be re-issued | 5.1 | Skip the grant |
| Screenshot: intro card, panel with 3 steps ticked, the briefing card, the journal contents page, the Dome stable page | UI | Looked at by the Technical Lead |
| Owner walkthrough as Lens A, timed (S1) | Landing under 25 minutes; at least 6 of 8 steps `DONE` or `AUTO` without a hint | Observation sheet |

**Done when:** a new player can run the checklist or skip it, the dome never fails during Landing, the briefing appears once, the journal opens from the hotbar with the Phase 1 entries, and the Dome stable latch unlocks the two entries.

---

## 11. Assumptions and risks

**Assumptions.** (1) A 12-year-old reads a 10-word step in under 10 seconds (*invented*). (2) Written-book page jumps work on NeoForge 21.1 (**unverified**). (3) The Monitor can fit one more static line or the pressure row's detail (**unverified**). (4) Hab B is never a required volume (true in the template today). (5) The band setting will exist (not built).

**Risks.** The panel and the intro screen are the first new client UI of Stage 3 (cost not in my estimate). The Dome stable burst is now one pointer but still five things at once for Lens A (Outgasser unlock, journal, relay, sky, goal change): stagger by order of the pointer text if playtest shows overload. Step 7's degraded form may feel pointless to Lens B (skip is there). The vanilla book looks plain beside the Monitor palette (Artist: a resource-pack texture for the book item and a title).
