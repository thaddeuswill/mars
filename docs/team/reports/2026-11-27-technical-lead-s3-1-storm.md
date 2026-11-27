# Report: Technical Lead, Stage 3 slice S3-1 (dust storm on the power grid), 2026-11-27

Branch `claude/elegant-brown-anu1ci`. Two commits (core, wiring) plus this docs commit. All numbers are the invented ones from the balance sheet storm table.

## 0. Handoffs received
- **Game Designer (storm length report):** `dust_storm.json` uses the table values, content test against the balance sheet. Accepted and done (`StormScheduleTest.dataEqualsTheBalanceSheetStormTable`). The "fix stage3-plan line 53" part was already done by the coordinator (storm lengths marked settled); I only added the build status.
- **Producer (stage 3 first slice):** tasks 4, 5, 6 (partly), 7 (wording applied). Accepted.
- Wording from `ui-specs.md` section 10 (Game Designer): applied with two label changes (see Decisions).

## 1. Done (real output)
Commands run from `mod/`: `./gradlew spotlessApply build --offline` passed; unit tests **233 run, 0 failed** (was 207). `./gradlew runGameTestServer --offline` printed **`MARS-SELFTEST RESULT PASS passed=106 failed=0`** (was 98; 8 new `storm.*` checks, one old check updated from schema 5 to 6). I ran the tests with a deliberate break (solar factor forced to 1.0): 4 tests failed, then I restored it.

| Part | Files |
|---|---|
| Data | `pack/data/mars/storms/dust_storm.json`; `monitor/lines.json` (two declared lines); `assets/mars/lang/en_us.json` (12 keys) |
| Pure core | `mod/src/main/java/mars/hazards/storm/` (`StormTuning`, `StormSchedule`); `PanelDustTuning.stagesAtStormEnd`; `PlanetSim` (attachStorms, stepStorm, notices, supply factor, operator start/end); `PlanetState` schema 6; ArchUnit rule lists the new package |
| Save | `PlanetData` NBT "storm" tag; seed from `StormSchedule.seedFor(worldSeed, planetId)`; `PlanetManager.seed`; `PlanetCheckpoint` keeps the checkpoint's schedule |
| Wiring | `planets/StormEvents` (chat lines, dust on panel blocks), `PlanetEvents.onLevelTick`, `admin/StormCommands` (`/mars storm start [hold]\|end\|status`, op level 2, logged), `MonitorView.stormRow`, `Fmt.duration`, `hidingLines` now also filters detail lines and cause lines, `MonitorScreen` passes the value as an argument to a value key |
| Tests | `StormScheduleTest` (10: balance-sheet match, 13/14/16 and 3.75/4/4.5 timings, gap range, determinism, save/restore equals a straight run, late load, operator), `StormSimTest` (10: 60 % cut and give-back, stage x storm, notices and dust only at the end, battery drain, paused 100,000 ticks, schema 6 round trip, schema 5 migration, pace scales timing only, cut and dust equal in both modes), `StormRowTest` (stages, CR-F9 words, CR-T7 limits, CR-T8 hiding, `Fmt.duration`), `MonitorLayoutTest` (storm states stay in bound), `StormGameTests` (real panel and command dispatcher, wire, NBT, old save, permission) |
| Docs | `ARCHITECTURE.md` 2.22, `docs/tech/launch-guide.md`, `docs/tech/stage3-plan.md` status, `BACKLOG.md` #49, `HANDOFF.md` |
| Screenshots | `tools/screenshots/s3_1_storm.shots`; PNGs in `tools/screenshots/out/20261127-194615/` (new timestamps checked, looked at) |

**What I saw** (real client, 1280x720, software GL): warning shot "Storm arrives in / about 10 min" and no mention of power; storm shot "Storm active / about 1.5 sols left" with the cause line "Storm: less sunlight -60 %", power supply 2.0 vs 4.0 drawn, battery 9.0 of 10; after the end "Storm is over / Sky is clearing", "Check your panels for dust.", the Power row shows "Solar Panel x1 dusty, makes less" and supply 4.1 (stage 2 = 82 %); two sols later "No storm due". Panel heights: 154 to 159 GUI px; in the worst stacked case (storm + short + dusty + frost) the plot drops to rows only, as it already did for the same stacked state without a storm. My first screenshot showed "Storm in progress" colliding with its value, so I shortened two labels (below).

## 2. Decisions needed
1. **Two Monitor labels changed for width** (well-structured): `ui-specs.md` section 10 says "Storm in progress" and "No storm expected"; both are wider than the 86 px label column and overlapped. Now "Storm active" and "No storm due". Recommend accepting; Game Designer to update section 10. Alternative: widen the column for every row.
2. **Cut-off of storms after Phase 1** (ill-structured, low stakes): storms keep recurring at cap 1.0 every 12 to 20 sols, and panels can only get dustier (no brush or dust-off yet, S3 later). After about 3 storms a panel sits at stage 4 (60 %). Options: (a) leave it (honest to the build order, playtest shows the pain), (b) stop scheduling after Phase 2 begins, (c) add the brush first. Recommend (a) for the S3-1 playtest, then (c). Needs Game Designer.
3. **Not hidden under expert guidance** (ill-structured): ui-specs section 10 says the storm countdown is "still hidden under expert like other estimates". I did not do it: hiding it needs an alternate label, and the same lead is already in the chat line. Recommend leave visible; Instructional Designer to confirm.
4. **First storm timing** (well-structured, resolved by me): the first storm starts exactly at the floor (sol 14, classroom 4) so the curve and the paper prototype hold; only the gaps are random. Say if you want it random after the floor.
5. **Story order** (still open from the Game Designer): the order line puts the first storm before Dome stable, but sol 14 is after it (sol 9). The data follows the curve.

## 3. Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** no sky or fog change during a storm. **Context:** the sky follows pressure only (`SkyPayload` has one number). A storm tint needs a payload field (storm level 0..1), a `SkyState` input and `sky.json` tint values; `planetary-keyframes.md` 3.7a has the look. **Needed:** tint and haze values (hazy at the warning, brown during, fog only before onset so F1 is not answered). **Done when:** values in a file the Technical Lead can read; then one small slice.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** update `docs/design/ui-specs.md` section 10 label wording (decision 1) and answer decision 2 (storms after Phase 1). **Done when:** section 10 matches `en_us.json`.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** run the grade 6 to 7 readability screen on the 12 new lang keys; confirm decision 3; the value id `mars:solar_power` in `monitor/lines.json` is my name for what F1 will open (S3-2 should use it or change both). **Done when:** wording passes or is changed in `en_us.json`.

> **To:** Producer · **Problem type:** well-structured
> **Problem:** S3-1 measurement for the budget: one working session, about 6 build-and-test cycles (3 unit, 2 boot, 2 screenshot runs), about 3 hours of agent time (unverified estimate; plan said 10 to 11 dev-days). **Done when:** recorded in the budget table.

## 4. Risks
- **Unloaded panels:** at a storm's end a panel in an unloaded chunk loads that chunk once to change its block. Fine for one event; with many far panels it is a short hitch. Not tested with a far panel.
- **Not run:** a real dedicated server (EULA is the owner's), a real mod-folder jar, a storm across a real save and quit (only NBT round trip and unit tests), the scheduled (not commanded) storm in a real client (unit tests and the sim cover it; the screenshots used `/mars index advance` and the command).
- **Notices wait for a step:** storm chat lines and dust are applied on the next planet step after `/mars index advance`, not instantly; harmless.
- **Rollback and dust:** a checkpoint restores the storm schedule but not the panels' dust blocks (blocks are never rolled back); after a rollback, panels can be dustier than the schedule implies. Documented limit, same as other blocks.
- **Switching pace mid-storm** keeps the saved start and end sols; only the next gap uses the new mode.
- The balance sheet says a 2-sol storm is survivable on one battery: the test confirms it for 1 panel + 1 Outgasser (battery stays above 0) and shows two Outgassers on that grid run flat in about 1.7 sols (C1 material, physical, not scaled).

## 5. Shared model updates
- **Decision (owner: `ARCHITECTURE.md` 2.22):** storms run on the planet's own step, so pause and the session clock apply to them with no extra code; pace scales only length, gap and warning; the cut and dust are physical and live in `panel_dust.json`.
- **Lesson:** a label that fits in English text at 17 characters can still be too wide for its 86 px column; the 24-character rule is not enough, take a screenshot.
- **Term:** "storm notice" = the queued event the game layer turns into chat lines and panel dust (like gate announcements).
