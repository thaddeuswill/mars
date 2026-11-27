# Journal and `guidance`: minimum Stage 2 design

Owner: Lead Game Designer. Status: **proposed** (owner decides, section 9). Docs only; no code touched. Nothing here was built or run. All sizes are **unverified** estimates; all numbers marked *invented* are placeholders.

## 1. Problem and why it is ill-structured

Two shipped specs have no mechanism in code:

1. **Journal.** `docs/design/messages/phase-1.md` (sections 9 and 12) and `ui-specs.md` 9.2 promise entries such as `journal.dome_stable.airmix`, the "On Earth" line, and `journal.sky.stylized` (SME-required label for the rose sky). They must arrive when a gate latches, reach every player on the planet, late joiners included.
2. **`guidance` + open forecasts.** CR-T8 hides the time-to-goal estimate under `guidance` = expert and while a forecast about that value is open. No `guidance` value, no forecast state, and no `reveals` data exist (`pack/data/mars/learning/forecasts/` is empty; no grade band in code).

Why ill-structured: it trades fun and immersion (a journal that feels like the colony's, not a chat dump) against cost; and it trades help against the B3 practice task (the estimate can do the learner's thinking). Needs the Technical Lead (cost, sync), Instructional Designer (CR-T8, B3), SME (text accuracy), Artist (screen look). This document gives the Game Designer's view only.

Settled, not reopened: `guidance` levels and meaning (DESIGN.md "Difficulty as information"; ARCHITECTURE.md 2.x: gentle is the classroom default, expert is not offered in classroom mode, thresholds never change with it); gates lock in; the Monitor shows the goal line from sol 0; journal entries do not fill Keeper's Record pages (CR-R1).

## 2. Key idea: nothing new to save for the journal

Gates lock in (invariant I-2). So **an entry is unlocked exactly when its gate is latched**. The unlocked list is a pure function of `PlanetData` gate state:

`unlocked(planet) = entries where gate_latched(entry.unlock.gate)`

Consequences: no per-player journal storage, no delivery queue, and late joiners get everything automatically (the function gives the same answer to anyone). Admin latches (`/mars gate latch`) unlock entries too, because the text is true either way (CR-H5 holds: the wording names no stand-in state). The only per-player thing is "have I seen it", used for one join pointer.

## 3. What the player sees

### 3.1 Journal (Option A, recommended)
- **Latch moment.** After the existing "Gate reached" / "Unlocked" lines, **one** extra line per gate (not per entry), so the Dome stable burst grows by one line, not three: "New journal entries: 3. Press J." (the key is *invented*, rebindable; the line shows the bound key).
- **Join.** A player who has not opened some unlocked entries gets the same one line once on login. Nobody gets it twice for the same entries.
- **Screen.** Plain, one panel in the Monitor's 232 px style (`MonitorStyle`): left list of titles (newest last, unread marked with a dot and the word "new", so color is not the only signal), right pane with the entry text, and a line "Unlocked at: Dome stable". Text is the `.ms` variant until a grade band exists in code, then `.hs`/`.ms` per band. No images, no animation, no quiz. Placeholder art only (SM straw-man); the Artist styles it later.
- **Command twin.** `/mars journal` prints the list and `/mars journal <n>` prints one entry in chat. It exists for dedicated servers, for tests, and as the fallback if the keybind is taken. Allowed for everyone.

Walkthroughs:
- *12-year-old, never used a modpack.* Dome stable latches, chat says to press J, J opens a short list, "The sky" is marked new. Three short entries, each under 40 words. The risk is that the chat pointer is missed during the burst: the unread dot on join and the reopen-any-time list cover it.
- *Experienced 17-year-old.* Will ignore the pointer; finds the journal when curious. Nothing blocks play and nothing is graded.

### 3.2 `guidance` and the time-to-goal line
- Default: **standard** in standard play; **gentle** in classroom mode (settled). Expert is refused in classroom mode (settled).
- Under **expert**, `monitor.goal.eta*` lines are not drawn. The bar and percent stay (they show progress, not a time).
- While any forecast that **asks about** pressure is open for that player, every line that declares `reveals: ["mars:pressure"]` is not drawn for that player: the ETA lines and "Leveling off" (it names a pressure). It reappears when the forecast closes. No message says why a line is missing (a "hidden" note would itself be a hint).
- Per player: two players on one planet can see different Monitor lines. That is correct: forecasts are per player.

## 4. Data shape

All JSON follows the existing `schema: 1` pattern (see `pack/data/mars/gates/dome_stable.json`) and is read by a reload listener like the gate files.

### 4.1 Journal entry: `pack/data/mars/story/journal/<name>.json`
```json
{
  "schema": 1,
  "id": "mars:journal/sky_stylized",
  "order": 30,
  "unlock": { "gate": "mars:dome_stable" },
  "title_key": "journal.sky.stylized.title",
  "text_key": "journal.sky.stylized",
  "goal_ids": ["A1"],
  "reveals": [],
  "names": ["mars:pressure"],
  "source": "phase-1.md section 12 (SME-required text); ui-specs 9.2 / IDer report 4a"
}
```
- `text_key` resolves to `<key>.ms` and `<key>.hs` in `en_us.json` (existing variant convention); a missing `.hs` falls back to `.ms` (the text in `phase-1.md` is "same as MS" for most entries).
- `unlock.gate` must name a gate file. Only `gate` is supported in the minimum.
- `reveals` is the CR-F9 field: forecast ids the text could give away. `names` is the CR-H5 field. Both are checked by tests (section 7), not used at runtime in the minimum.
- Stage 2 entries (all from existing text): `journal.dome_stable.airmix`, `earth.gate.dome_stable` (title "On Earth"), `journal.sky.stylized`. All unlock at `mars:dome_stable`. The sky label must exist before the first pressure rise, and Dome stable latches before the Outgasser unlocks (`dome_stable.json` unlocks it), so the SME condition holds. `phase_2_begins` repeats the sky text in its own entry (SME's original placement): one file `journal/sky_stylized_repeat.json` with the same `text_key`, `unlock.gate: mars:phase_2_begins`; the screen shows a repeated text once (dedupe by `text_key`).

### 4.2 Player settings: world-level SavedData
`data/mars_player_settings.dat` (overworld data storage; one file, not per planet), shape:
```json
{ "schema": 1,
  "players": { "<uuid>": { "guidance": "standard", "journal_seen": ["mars:journal/sky_stylized"] } } }
```
- Keyed by player UUID, local to the server's computer. No name is stored. Future exports map to `P-XXXXX` (existing rule). It does not travel to any service.
- `guidance` absent means "use the default for this planet" (so changing classroom mode later does not strand old values).
- Open forecasts are **session-only** in the minimum (not saved): they are rebuilt from forecast issue state when forecasts exist (ARCHITECTURE.md 2.6.1). Until then only the debug command sets them.

### 4.3 Monitor line declarations: `pack/data/mars/monitor/lines.json`
```json
{ "schema": 1,
  "lines": {
    "monitor.goal.eta":          { "reveals": ["mars:pressure"], "hidden_under": ["expert"] },
    "monitor.goal.eta.one":      { "reveals": ["mars:pressure"], "hidden_under": ["expert"] },
    "monitor.goal.eta.under_one":{ "reveals": ["mars:pressure"], "hidden_under": ["expert"] },
    "monitor.estimate.levelling_off":    { "reveals": ["mars:pressure"] },
    "monitor.estimate.levelling_off.hs": { "reveals": ["mars:pressure"] }
  } }
```
Key ids are the current lang keys (check against `en_us.json`; the exact ETA key names are **unverified**). Meaning: `reveals` = values this line states or implies; `hidden_under` = guidance levels that never see it. A line not listed has no declaration and the content test fails if its key matches an estimate pattern (section 7).

Forecast files (later) gain `"asks_about": ["mars:pressure"]`. That one field is what "open forecast" means for Monitor masking.

### 4.4 Code placement (Minecraft stays out of pure packages)
| Piece | Package | Pure Java? |
|---|---|---|
| `Guidance` enum (GENTLE, STANDARD, EXPERT), `Guidance.effective(classroom, stored)`, parse/format | `mars.core` | Yes |
| `LineRules` (`visible(lineKey, guidance, openAskedValues)`), data records for 4.3 | `mars.learning` | Yes |
| `JournalEntryDef` record + `Journal.unlocked(defs, latchedGateIds)` | `mars.learning` | Yes |
| `OpenForecasts` (per-player set of asked-about value ids) | `mars.learning` | Yes |
| Reload listeners, SavedData, `/mars guidance`, `/mars journal`, payload, screen | `mars.planets` / `mars.admin` / `mars.client` | No (Minecraft) |

ArchUnit: `mars.learning` is added to the pure rule if it is not already (Technical Lead to confirm; the existing rule names `mars.core` and `mars.index.sim`).

## 5. Triggers and flow

| Event | Action |
|---|---|
| Gate latches (any cause) | `announceGates` already runs; after its lines, for each entry whose gate just latched: add to the planet's unlocked view; send one pointer line per gate to online players on that planet |
| Player joins | Compute `unlocked(planet)` minus `journal_seen`; if non-empty, one pointer line; send the unlocked id list in the sync payload |
| Journal screen opens | Client sends "seen" for the shown ids (or on close); server writes `journal_seen` |
| Monitor payload is built for a player | Server applies `LineRules.visible` with that player's effective guidance and open set; hidden lines are omitted, not blanked |
| `guidance` changes | Rebuild the Monitor payload for that player now; log line `guidance.changed` (local log only until the action log exists) |

## 6. Who sets `guidance`

| Case | Who | How | Refused when |
|---|---|---|---|
| Standard play, own level | The player | `/mars guidance <gentle|standard|expert>` (no permission needed) | never |
| Another player's level | An operator (permission 2) | `/mars guidance <level> <player>` | the target is in classroom mode and the level is expert |
| Classroom mode | The teacher (operator) only | same op form; the player form replies "Your teacher sets this." | players change it; expert is refused |
| Default | the system | standard (standard play), gentle (classroom) | n/a |

Rationale: guidance only removes *extra* help; thresholds and failure reports never change, so a self-set level is not a cheat. A command is enough for the minimum; a toggle on the journal screen and the teacher overview buttons come later (Stage 5 teacher tools). The player form is not owner-only because singleplayer owners are often not "operators" in a LAN or small-server sense, and a 17-year-old who wants expert should not need an admin. `/mars guidance` with no argument prints the current level and what it hides, in one short line.

## 7. Tests that would prove it

Unit (pure, no Minecraft), each of which can fail:
1. `Guidance.effective`: classroom + stored expert gives gentle; classroom + nothing gives gentle; standard + nothing gives standard.
2. `LineRules.visible`: ETA line hidden under expert; shown under gentle and standard; hidden for any guidance while `mars:pressure` is open; "Leveling off" shown under expert but hidden while open; an unrelated open forecast (`mars:temperature`) does not hide it.
3. `Journal.unlocked`: nothing before the latch; all three Dome stable entries after; unchanged after a pressure drop (lock-in); same list for two players; a late-joiner function call with the same latched set gives the same list.
Content (read the real pack JSON, like the existing gate content tests):
4. Every journal entry names an existing gate; has `.ms` text and `.hs` or a fallback; ids unique; `order` unique per gate; text is under the length budget (CR-T7, *invented* 45 words for journal entries).
5. **CR-T8 scan:** every `en_us.json` key under `monitor.goal.eta*` or `monitor.estimate.*` appears in `lines.json` with a non-empty `reveals`. Adding an undeclared estimate key fails the build.
6. **CR-F9 scan:** every journal entry has a `reveals` field (empty is fine). Once forecasts exist, an entry whose `reveals` names a forecast must not be deliverable while that forecast is open for the player (vacuous until then).
Boot / GameTest (Technical Lead's harness):
7. Latch `mars:dome_stable` by `/mars gate latch`: an online player gets exactly one pointer line, and `/mars journal` lists 3 entries.
8. A second player who joins after the latch gets the join pointer once, and not again after opening the journal.
9. `/mars guidance expert` as a non-op in standard play works; in classroom mode it is refused; the Monitor payload for that player has no ETA.
10. Debug open forecast (op, below) removes the ETA and "Leveling off" for that player only and restores them on close.
Debug command for 10: `/mars debug forecast <open|close> <value_id>` (op only, clearly labelled debug, creates no learning event and fills no Record page, CR-R1/R2). It is the only way to set the open state before real forecasts exist.

## 8. Deliberately left out

| Left out | Why / when |
|---|---|
| Event-triggered entries (`journal.outgasser.puff`, `journal.sound.thin_air`, `journal.mast.balloon`) | Need per-player or per-planet "first time" flags. Add `unlock: {event}` in a later slice; the data shape allows it |
| Entry search, tabs, images, audio logs, unread HUD badge | Not needed to teach; extra screen cost |
| Message archive, Keeper's Record, Keeper's Report pages | Separate Stage 3/4 screens (ARCHITECTURE.md client list) |
| Full forecast lifecycle (issue, lock, answer, resolve) | Only the "open" bit and `asks_about` exist now |
| `guidance` effects on hints, "Useful now", "Unlocks at..." lines | Those lines are not built; each will read `Guidance` when it is |
| Per-planet teacher `guidance` and overview buttons | Stage 5 teacher tools; the op command covers it |
| Persistence of open forecasts | Rebuilt from forecast state later |
| Logging to the action log | Not built; log lines only |
| Grade band selection | Not in code; `.ms` text until it exists |
| Translations | English only |

## 9. Options and recommendation

| | Option A (recommended) | Option B (cheaper) |
|---|---|---|
| Journal | Derived unlock + sync + plain screen + key + chat pointer + `/mars journal` | Same data and derived unlock; **no screen**: gate latch prints the entry titles, `/mars journal [n]` prints text in chat |
| Guidance, ETA masking, open-forecast bit | Full (sections 4.2 to 4.4) | Same, identical |
| Fun / immersion | Entries are an object in the colony world; text stays re-readable | Chat dump; scrolls away; breaks "no pop-ups, one message at a time" feel |
| Learning | SME's sky label is reliably re-readable before the sky moves | Label exists but is easy to lose (A1 weaker) |
| Cost (unverified, Technical Lead to confirm) | about 700 to 900 lines of Java and tests, 1 new payload, 1 screen, 1 keybind; roughly 1.5 to 2 days | about 350 to 450 lines; roughly 1 day |
| Upgrade path | n/a | Screen added later without touching data or tests |

**Recommendation: Option A.** The SME made the sky label a hard requirement, and chat lines are lost within a minute during the Dome stable burst. If the owner is short on credits, build B first as the first commit of A: the data shape, derived unlock, `guidance`, `LineRules`, and all tests are the same, and the screen is the only delta. In both, **do the guidance and CR-T8 part first**: it is cheap, it prevents the Monitor from giving away B3 answers, and it is already shipped in wording.

### Assumptions (challenge these)
- The Monitor payload can be filtered per recipient. **Unverified**: I did not confirm whether `MonitorSender` builds one view per planet. If it does, the cost is a small per-player copy, not a rewrite.
- A world-level SavedData is acceptable beside the planet saves.
- A non-operator may change their own guidance in standard play (owner call, below).
- Journal key J is free (*invented*); rebindable.
- Entries are all gate-triggered for now.

### What would make it better defined
A first playtest at Dome stable: does the player open the journal within 2 sols without being told? (observe, do not quiz). Plus the Technical Lead's answer on per-recipient Monitor payloads.

## 10. Alignment (traceability)

| Element | Goal IDs | Pillar | Note |
|---|---|---|---|
| Journal entries | A1, D2 (Earth link) | the planet is the quest log (world-triggered, no questline) | No Record page fills (CR-R1) |
| `guidance` | supports B3 practice (does not claim a new goal) | pressure, not grind | Thresholds never change |
| CR-T8 masking | B3, C3 | your progress provokes the planet | Protects the learner's own forecasting |
| Debug forecast command | none | n/a | Test aid; never in the action log as learning evidence |

Fantasy and Authenticity audit: no fictional element added. The journal is an in-world logbook; the entries' science is the SME's text.
