# Technical Lead report: Stage 3 slice 4 (Landing, seal kit, journal, Shift Briefing 1), 2026-11-28

## 0. Handoffs received
Landing spec T1 to T7 and ID wording W1 to W7: accepted and built with the changed texts. Owner decisions (written-book journal, grace fixed at sol 1.2 with `{grace_minutes}`, step 2 = tank read-out, storm sentence removed with no `storms_built` gate): built as stated. Questioned, not blocking: spec says the briefing is "38 words"; the accepted text is 41 (cap 45); a test asserts the cap.

## 1. Done (all pushed; jar `0.4.0-stage3-i`, sha256 `6114deec59f25f3ee111b90681434edf0f849618fe1aa9e19673bb52a36d20ce`)
- **A** seal kit item, recipe (1 iron ingot + 1 brick), tag `mars:patchable`, use handler, Ops locker with group size + 1 kits; GameTest. **B** tank right-click read-out ("Tank: 3 of 40" per tank at start); "Outside air" Monitor line (pressure row second line, +9 GUI px; gives way first on a full panel; pinned while the player's step 3 is open); layout tests.
- **C** `mars.landing` pure state machine and `landing.json`; 25 unit tests (each step's condition, any order, AUTO rule, skip, late joiner, save round trip, hint timer, text caps).
- **D** hooks, checklist panel (client HUD layer, chosen over a scoreboard sidebar: one small payload, readable wrapped text, about 100 GUI px tall at the left edge), intro screen with two buttons, chat buttons, `/mars landing status|start|skip|skip_step|step n|reset`, `/mars journal give`, BELL lines in chat.
- **E** journal as a server-built written book from 8 JSON entries; contents page with page-jump links and "(New)"; unlock tokens (join, step, landing over, briefing, gate latch, first Outgasser); planet entries to all players and caught up on join; one chat pointer (held while the checklist runs until step 8); Relay line at the latch; briefing card as chat lines 3 s after the checklist; `life.blackout` text now says "shelter" (matches the code); `monitor.gate.dome_stable.waiting.landing` now says "Waiting: the Landing grace is running." (the old "finish the Landing first" was false under decision L2).
- **F** GameTests (real blocks, fake player): locker, kit wrong target and patch, tank read-out, whole Landing 1 to 8 incl. real door, Monitor, kit use, Oven placement, airlock, journal click, briefing once, Dome stable entries once, save round trip, group AUTO. Forecast-leak test (`JournalTest`): a pattern for F1's answer wording must match each entry's declared `reveals` (all declare none and none match); it catches the removed storm sentence.
- **Guard-removal proof:** removing the "only while RUNNING" guard in `LandingState.move` made `LandingStateTest.aNewPlayerIsOfferedOnceAndNothingTicksBeforeTheyStart` fail ("an offered Landing does not tick"); restored, green.
- **G** screenshots, looked at: `tools/screenshots/out/20261128-032313/landing_1` (intro), `20261128-032623/` landing_4 and _7 (panel, 3 and 7 steps ticked), `landing_6` (crack gone after a real right-click with the kit; before-shot `landing_5`), `landing_8` (journal contents page open, links and "(New)" marks), `landing_9` (briefing and pointer lines in chat), `landing_3` in `20261128-032313` (Outside air line fits the 250 px panel).

Run counts: unit tests 295 (was 260), boot checks 134 (was 120). Gradle invocations this session: about 45 (most 5 to 15 s, cached); 6 screenshot runs of about 50 s. Wall time for the part after the usage reset: not measured precisely, roughly 1 hour.

## 2. Decisions needed
1. Outside air line is dropped on a full panel except during Landing step 3 (otherwise Phase 1 loses the history plot, the owner's adopted option D). Recommend keep. Ill-structured (Monitor density against a static fact); ID and Artist may want the line always visible.
2. Briefing delivered as chat lines, not a side panel. Recommend keep for Stage 3, side card with the Stage 4 journal screen. Well-structured.

## 3. Handoffs
> **To:** Codex · **Problem type:** well-structured
> **Problem:** Landing and journal events are not in the learning log.
> **Context:** `mars.story.landing.LandingEvent(player, type, subject)` is posted on the NeoForge bus for `landing.step` (subjects `mars:landing_step_N`, suffix `_auto` or `_skipped`), `landing.skipped` (`mars:landing_all`), `landing.done`, `briefing.opened` (`mars:briefing_shift1`), `journal.opened` (`mars:journal_contents`). `ActionEvent.Kind` has none of these. Also needed: the open Oven placement, Monitor-open and gate-latch hooks (`GateLatchedEvent` is posted now) and the band setting (the journal is MS only).
> **Needed from them:** new kinds and a subscriber. **Done when:** a Landing run writes these lines to the session log.

> **To:** Instructional Designer / Game Designer · **Problem type:** well-structured
> **Problem:** wording and spec follow-ups. **Context:** the briefing is 41 words not 38; the HS "Read more" pages exist in data but are not shown (no band setting); the Dome stable Earth line and `journal.sol` (24 h 40 min) are still marked unverified pending the SME; I changed the Monitor waiting-landing line and `life.blackout` as above; new keys use my wording for hints 4 (plus `landing.hint.4.empty`, unused) and 6. **Needed:** confirm or reword.

> **To:** Lead Artist · journal book item texture and a title look (vanilla written book now); seal kit icon is a 16 px placeholder I drew.

## 4. Risks
- Not verified: written-book page-jump links (the contents page shows link styling; I did not click one), multiplayer, a late joiner in a real client, a dedicated server (the new payloads use lambdas, but no server run with a client), GUI scales other than 3 for the panel, classroom/pause behaviour of hints.
- `player.tickCount % 20` pass: with many players on a planet this is one record lookup each per second; the Hab B flood fill runs once a second only while step 5 is open.
- The Landing intro opens on every login while OFFERED (by design); a player who closes it can start from the chat buttons.
- In-memory timers (briefing 3 s, hint sent) reset on restart: harmless, they re-arm.
- Step 4 can complete from a kit crafted or found, not only from the locker (by spec: "inventory holds a kit").
- The dev client step `use x y z` calls the real right-click path, so the kit proof is a real-client one for the patch; the journal right-click used `useitem`.

## 5. Shared model updates
- Decision L2 consequence: the Monitor waiting-landing text now describes the grace, not the checklist.
- New seams: `LandingEvent`, `GateLatchedEvent`, `HullDoorUsedEvent`, `SealKitPatchedEvent`, `TankReadEvent` on the NeoForge bus.
- Lesson: GameTest worlds persist saved data between runs; any test that records saved state must clear it (setOriginForTest(null), forgetForTest).
