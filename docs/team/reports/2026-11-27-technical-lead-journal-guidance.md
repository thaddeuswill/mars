# Technical Lead report: journal and guidance (BACKLOG #49), two commits

Design: `docs/design/journal-and-guidance-minimum.md` (owner accepted Option A, built as B first, then the screen).

## 0. Handoffs received
- Owner decisions (players set their own guidance in standard play; classroom teacher/operator only, expert refused; J key invented and rebindable; screen title "Keeper's Journal"; debug-only `/mars debug forecast open|close`): accepted and built.
- Game Designer assumption "Monitor payload can be filtered per recipient, unverified": **checked, true, and cheaper than assumed.** `MonitorSender.open` already builds one `MonitorView` per call and sends it to that one player, so no per-player payload flag was needed. `MonitorSender.viewFor(player)` applies `MonitorView.hidingLines(filter)`. Nothing is sent about hidden lines.
- Design doc guessed the ETA keys as `monitor.goal.eta.one` / `.under_one`; the real keys are `monitor.goal.eta`, `eta_one`, `eta_soon`. `lines.json` uses the real ones.

## 1. Done
Commit 1 (`ba3154a`, no screen) and commit 2 (J-key screen).
- Pure: `mars.core.Guidance`; `mars.learning` `LineRules`, `JournalEntryDef`, `Journal`, `PlayerSettings`, `OpenForecasts` (added to the ArchUnit pure rule).
- Data: `pack/data/mars/monitor/lines.json`; `pack/data/mars/story/journal/{airmix,on_earth,sky_stylized}.json` (all unlock at `mars:dome_stable`); lang keys in `en_us.json`.
- Minecraft glue: `mars.planets` `LearningDefinitions` (reload listener), `PlayerSettingsData` (SavedData `mars_player_settings`, UUID keys only), `LearningService`; `mars.admin.LearningCommands` (`/mars guidance [level] [player]`, `/mars journal [n]`, `/mars debug forecast open|close [value]`); `MonitorView.hidingLines`.
- Pointer line: one per gate latch (not per entry), once on login or arrival for entries a player was not yet told about. Text "New journal entries: N. Press J." (the key is the player's bound key).
- Commit 2: `mars.journal` payloads and server handler, `mars.client.JournalClient` (key J, category Mars), `JournalScreen`; dev-script verb `journal`; `tools/screenshots/journal.shots`.
- Docs: ARCHITECTURE.md 2.20, launch-guide commands, BACKLOG #49.
- **Fixed on the way:** `PlanetSim.toState` kept empty machine types, so a fresh planet with an Oven placed and removed differed from the same planet after save and load. That made `save.round_trip` fail once my GameTest left a fresh planet (order-dependent, hidden before). Empty types are now left out.

Journal texts: final text from `phase-1.md` sections 9 and 12. The "On Earth" sentence is marked `"status": "awaiting SME confirmation of the 1 percent sentence"` in `on_earth.json` (a test checks the mark). Titles ("The air outside", "On Earth", "The sky") are **invented**. The Phase 2 repeat entry of the sky text was not built (not requested).

## Real output
- `./gradlew build`: BUILD SUCCESSFUL, **199 unit tests, 0 failures** (baseline 184; +15: Guidance 3, learning/content 10, view hiding 2).
- `./gradlew runGameTestServer`: `RESULT PASS passed=87 failed=0` (baseline 81; +6 in `LearningGameTests`: empty before latch; one pointer per player and `/mars journal` lists 3; late joiner told once and 2 of 3 stay unread; non-op sets own expert and loses the ETA, another player keeps it, cannot set others; classroom rules; debug forecast hides per player and close restores, non-op refused).
- Screenshots (real client, Xvfb, fresh files): `tools/screenshots/out/20261127-142523/journal_1_chat_pointer.png` (chat shows "Goal reached: Dome stable", "You can now build...", then one line "New journal entries: 3. Press J.") and `journal_2_screen_first_open.png` (panel "Keeper's Journal", list with "On Earth" and "The sky" marked with a dot and "new", the selected first entry text on the right, "Unlocked at: Dome stable", "Esc to close"). I looked at both; text fits at GUI scale 3, 1280x720.

## Not verified
- A real key press of J (the script sends the same request through the dev verb; the pointer line did show "J", so the mapping is registered).
- A dedicated server (payload registration by lambda follows the existing rule; not run, and the boot test is a GameTest server).
- Classroom mode through the command dispatcher: `Pace.classroom()` is always false in this build, so the rule is tested by calling `LearningService.setGuidance(classroom=true)` directly.
- The GameTest "pointer line" check counts lines sent (`LearningService.pointerLinesSent`), not what a client received; the screenshot shows the real line arriving once.
- Click and arrow selection in the screen; UI with the Monitor open (the Monitor is one-shot: a guidance change shows on its next open).
- Reading level / CR-T7 of the journal texts (final SME/ID text, not rescreened); the 45-word budget is invented and only checked by test.

## 2. Decisions needed
None blocking. Optional (well-structured): build the Phase 2 repeat of the sky entry (`sky_stylized_repeat`, dedupe by text key)? Recommend yes when Phase 2 content is touched; it is one JSON file plus dedupe in the screen.

## 3. Handoffs
> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Needed from them:** confirm the "less than 1 % of the force" sentence in `pack/data/mars/story/journal/on_earth.json`. **Done when:** the `status` field is removed.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Needed from them:** check the invented titles and "New journal entries: N. Press J." wording; confirm journal text may stay `.ms` until a grade band exists. **Done when:** wording approved or changed in `en_us.json`.

> **To:** Lead Artist · **Problem type:** ill-structured (look)
> **Needed from them:** style the journal panel in Stage 3 (it reuses `MonitorStyle` colours; placeholder).

## 4. Risks
- Open forecasts are session-only: a relog drops the debug state (by design until forecasts exist).
- Unread/notified sets grow by one id per entry per player: tiny, but a player list with many UUIDs grows the one save file.
- The Monitor and journal screens share `MonitorStyle`; a restyle should keep both in step.

## 5. Shared model updates
- Lesson: the Monitor view is built per recipient on every open, so per-player content (CR-T8) is a filter on the view, not a new payload.
- Lesson: GameTests share one persistent world; a test that latches gates or adds machines must reset the planet (`resetForSelfTest`) and use fresh player UUIDs, or it makes later tests fail depending on order.
- New terms: "told" (the pointer line was sent) vs "seen" (the entry was opened) in `PlayerSettings`.
