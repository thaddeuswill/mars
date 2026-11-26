# Test Strategy: NeoForge Equivalents of the Handoff Lessons

**Owner:** Technical Lead · **Status:** Draft 1, 2026-11-26 · **Stability:** flexible (changes at stage boundaries)
**Inputs:** [PROCESS.md](../../PROCESS.md) §2–3 and §6, [docs/team/reference/GAME_DEV_HANDOFF.md](../team/reference/GAME_DEV_HANDOFF.md) (a Godot project: the ideas carry over, the engine details don't), *Design and Development of Training Games* Chapter 9 (development and testing), [ARCHITECTURE.md](../../ARCHITECTURE.md), [build-plan.md](build-plan.md).
**Scope:** software testing: does the game and its supporting tech work, on target platforms, throughout. **Usability and learning playtests are separate** (Producer's QA plan; Lead Game Designer's playtest plan).

> No code exists yet. Everything here is a plan. Items marked **[owner]** need the owner's own machine or hardware; **[unverified]** means a fact from knowledge not checked this session.

---

## 1. The layers

| Layer | What it runs | Minecraft needed? | Time per run (target) | Catches |
|---|---|---|---|---|
| **L0 Parse gate** | Java compile, Spotless, JSON schema validation of `pack/data` and `pack/assets`, KubeJS syntax check | No | < 1 min | Broken code, broken content files, broken scripts, before anything boots |
| **L1 Unit tests** (JUnit 5) | Pure simulation core, gates, holds, story scheduling, forecasts, IDs, export, scrubbing, migrations | No | < 2 min | Logic errors |
| **L2 Solver** | Reachability of every gate and story beat over the pure model, across variants | No | < 5 min | Soft-locks, unreachable content, content-rule violations |
| **L3 GameTests** | NeoForge GameTest server with small structures: sealing, machines, pause, Shift Board, Mekanism/Create integration | Yes, headless server | < 5 min | In-world behavior |
| **L4 Headless playthrough** | A dedicated server plays the real game start to finish with fake players | Yes, headless server | < 20 min per variant (with a test pace preset) | Integration of everything; the regression gate |
| **L5 Screenshot run** | A real client at 1280×720 under a virtual display, scripted camera and screens | Yes, client + virtual display | < 15 min | Visual bugs checks can't see |
| **L6 Platform and hardware runs** | The pack on Windows and macOS, on weak lab-class PCs, and the Stage 3 load test | Yes | Manual | Platform and performance problems **[owner]** |

Definition of done for builds (PROCESS.md §2) = L0–L4 pass across the variants (section 4), L5 screenshots looked at by a person, L6 at stage exits.

**CI (decided, owner, round 3):** basic CI from **Stage 1 exit** runs **L0–L2** (parse gate, unit tests, solver) on every push: no Minecraft needed, minutes per run. L3–L4 stay manual regression gates (PROCESS.md §3) until a CI machine can run a headless server; revisit at Stage 3. CI host is the owner's choice (GitHub Actions is the obvious one if the repo is on GitHub; its free-tier limits are **unverified**).

---

## 2. Fail fast, never hang

### 2.1 Parse gate (L0)
Runs before every test task; a failure stops the run with the file and field named.
- `compileJava` + `spotlessCheck` (Google Java Style).
- **Content validation** (`validateContent` Gradle task, pure Java): every JSON in `pack/data/mars/` and `pack/assets/mars/` against its schema in `mod/src/main/resources/schemas/`; cross-references resolved (every `input` in a keyframe file is a registered parameter, derived value, or signal; every message `after` points to a real message; every goal ID is A1–D2; every lang key exists in `en_us.json`; every `mars:` sound has a subtitle).
- **KubeJS syntax check:** KubeJS logs script errors and keeps loading rather than stopping **[unverified]**, so a broken script would only show as a missing recipe. The gate parses every file in `pack/kubejs/` with Rhino's parser (the same engine KubeJS uses; MPL-2.0) and fails on a syntax error. A boot-time check (2.2) then fails the run if KubeJS reported any script error in its log.

### 2.2 Boot guard and watchdog (L3–L5)
A dev-only test harness mod (`mod/src/testmod/`, never shipped) adds:
- **Step log:** each lifecycle point (mod constructed, common setup, server starting, level loaded, playthrough started, each playthrough step) is written to stdout as `STEP <id>`.
- **Boot guard:** a daemon thread started in the mod constructor. If `STEP playthrough_started` isn't reached within `selftest.bootTimeoutSec` (default 180 s), it prints `FAIL boot_guard last_step=<id>` and halts the JVM with exit code 3.
- **Watchdog:** during the playthrough, if no new step arrives for 60 s of wall time, or the whole run passes its hard limit, it prints `FAIL watchdog last_step=<id> elapsed=<s>` and halts with exit code 4. Vanilla's dedicated-server watchdog (`max-tick-time`, kills the server if one tick takes too long) stays on as a second line **[unverified that it applies to the GameTest server]**.
- **Gradle timeout** on every run task as a third line, so CI never hangs even if the JVM ignores everything above.

---

## 3. The automated playthrough (L4)

**Play the real game through the real code.** A headless dedicated server with the full pack (Mekanism, Create, KubeJS) and the test harness mod:
1. Creates a fresh world in its own folder (section 6), sets the variant's preset.
2. Spawns fake players (NeoForge `net.neoforged.neoforge.common.util.FakePlayerFactory.get(ServerLevel, GameProfile)`, **verified on the 1.21.1 branch** ([source](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/src/main/java/net/neoforged/neoforge/common/util/FakePlayerFactory.java)); fake players have no network connection, so actions call the server handlers directly) assigned to planet 1 (and 2 for the group case).
3. Performs actions through **the same server handlers the client's packets call**: place blocks, use items, claim on the Shift Board, answer forecasts, submit reports. No admin or debug shortcuts: anything done through `AdminControl` carries `Cause.ADMIN`, and a playthrough that used it would prove nothing.
4. Runs time with vanilla `/tick sprint` (ticks as fast as the machine can) and a **test pace preset** (`pace.rateMultiplier` high, short holds). Multipliers scale rates and holds only, never thresholds (DESIGN.md), so every gate is reached at its real value.
5. Script: survive in the dome → seal it → Dome stable → build Regolith Outgassers → Phase 2 (5 kPa) → processors, emitters → Thaw → Hydrosphere → Phase 3 → seed biomes → Breathable (helmet off works, helmet-off condition is live) → Equilibrium → build the beacon → ending cards shown.
6. After each step, checks (section 5). At the end: export CSVs and run the privacy test on them.
7. Exit code 0 on pass. On fail: non-zero, and the **first failing check id** on the last line: `FAIL check=gate.phase2.reached expected="pressure ≥ 5 kPa latched" read="pressure 4.71 kPa, not latched, sol 38.2"`.

**What it can't prove:** that a human finds the path (that's usability); that it runs on Windows **[owner]**; client rendering (L5).

---

## 4. The solver (L2): every gate and every beat reachable

Pure Java over the simulation core (ARCHITECTURE.md §4: the Index math, gates, holds, and story scheduling have no Minecraft dependency), so it runs in seconds.

- **State:** Index values, latched gates, machines built (by type and tier), elapsed sols, resources as a simple budget.
- **Actions:** build, maintain, or switch off a machine type that the current gates unlock; seed a biome; apply a fauna option. Costs from the balance sheet data.
- **Search:** greedy toward the next gate with a fallback breadth-first search on small states. Success = every gate latched, every story beat delivered, ending reached.
- **Soft-lock = failure.** An unreachable gate or beat prints the gate, the closest state reached, and which condition was short.
- **Recovery check:** from the worst plausible state after each gate (all machines broken, a storm at the phase cap, fauna at the planet cap), the next gate must still be reachable. Gates lock in, so the player can't lose a phase, but they can get stuck below the next one.
- **Story beats:** every message's trigger must fire in at least one solver run; `after` chains must resolve; nothing waits on a gate that the classroom preset skips.

**Variants** (PROCESS.md §2), for both L2 and L4:

| Variant | Preset | Group size | Notes |
|---|---|---|---|
| Standard solo | standard | 1 | |
| Classroom, small group | classroom | 1 configured, 1 playing | Absent-player case: rates use configured size |
| Classroom, full group | classroom | 5 | Group rate scale `1/(1+0.35×4)` |
| Stress ("everything at once") | classroom | 5 | Storm, radiation spike, fauna raid, and a drift event at the same time during a hold; server cap on fauna reached; a rollback mid-hold |
| Set phase | classroom | 4 | Teacher starts at Phase 2: skipped beats filed, forecasts `void`, no credit given |

L2 runs every variant on every build. L4 runs standard and classroom-5 on every build; all variants at stage exits.

---

## 5. Checks that can be trusted

| Rule (PROCESS.md §3) | How we do it |
|---|---|
| Say where a thing **must** be | A `check(id, expected, actual)` helper that takes a positive expectation. "Gate latched **and** recipe X craftable **and** Monitor page Y visible", not "no error". |
| Print what the check read | Every check prints `id`, `expected`, and `read` on pass and fail (`read="pressure 5.02 kPa, latched at sol 41.6"`). A check that read nothing (empty list, missing value) fails as `read=<empty>`. |
| Prove it can fail | Each new check lands with a note of the run where it failed against the old code or a deliberately broken fixture. For the pure core, mutation testing with PIT (`pitest`, Apache-2.0, checked round 3) at stage exits. |
| Restore global state | L1/L2: no static mutable state (ARCHITECTURE.md §3), JUnit `@TempDir` for every file. L3/L4: each GameTest runs in its own structure; tests that change config, `planets.json`, the paused flag, or the Mars data folder snapshot them first and restore in a `finally`. A final check asserts the paused flag and config equal their starting values. |
| Identify by ID, never by display name | The NeoForge analogue of the Godot node-name bug: logic must never find a block, item, entity, machine, gate, or message by its **display name or translated text**. Use registry IDs (`mars:atmospheric_processor`), our machine IDs (`m-…`), and data IDs. An ArchUnit rule forbids calls to `Component.getString()` and `getHoverName()` outside `mars.client` and the export's goal-title lookup. Entities found by UUID, never by custom name. |

### 5.1 Content rules as tests (L2)

The Lead Instructional Designer's rule list, [docs/learning/content-rules.md](../learning/content-rules.md) (CR-F1…CR-T6), is the **source of truth** for learning-content rules; this table only shows how the main ones are wired. **Feasibility:** every rule in its sections 1–4 is automatable with the layers above. Where they run: CR-F1–F5, CR-H5 in the solver (L2); CR-H1, CR-H3, CR-H4, CR-H6, CR-R1, CR-R3 as runtime assertions in the playthrough (L4: every failure report checked against the live simulation state and the Monitor's cause list, which the contribution `SourceId`s already provide); CR-F6–F8, CR-H2, CR-R2, CR-T1–T6 as file scans in the parse gate (L0); CR-R4 is the privacy test. CR-T6 (reading grade) reports without failing the build at first, as the rule says. Field names follow content-rules.md (`horizon_sols`, `keyed_choice`, `outcome_check`, per-type banned-token lists); the names below are the same ideas.

| Rule | Test |
|---|---|
| Every forecast has a correct answer **under the current physics** | Each forecast JSON gets an `outcome_check` per choice and a `keyed_choice` (CR-F1, CR-F2). The test puts the pure model in the forecast's trigger state, applies the triggering action, runs to `outcome_visible`, and checks the outcome matches the choice marked correct. Catches the F4 lesson (TEAM.md learning log: a physics change silently broke a forecast). |
| Story beats match gate conditions | Each story message gets an optional `asserts` condition (e.g. beat 7 "the sky shifts" asserts `mars:pressure >= 5`). The solver checks it holds at every delivery. Needs the Lead Game Designer to add `asserts` to beats that make a claim about the world (handoff). |
| A hint only appears for a problem that really exists | Every failure-report hint is keyed on a failure type; the test checks each failure type can occur (some hazard or wear rule produces it) and that no hint is keyed to an unknown type. |
| A hint never states the answer | Forecasts and failure reports get a banned-token list (CR-H2; written by the Lead Game Designer) (e.g. "vapor", "sublimate" for F4a). The test fails if any hint text, in either band, contains an answer term. Words matched on the English language file. |
| Thresholds never scale | Already enforced by design (no scaling field); a test loads every preset and checks every gate threshold is identical. |

### 5.2 Feasibility of every content rule (round 3 answer to the Lead Instructional Designer)

| Rule | Feasible? | Layer | How, in one line | Needs from content |
|---|---|---|---|---|
| CR-F1 keyed answer matches physics | Yes | L2 | Pure model from trigger state for `horizon_sols`, compare with `outcome_check` | `trigger`, `horizon_sols`, `keyed_choice`, `outcome_check` per forecast |
| CR-F2 exactly one keyed answer | Yes | L0 + L2 | Schema (one `keyed_choice`) + run every choice's `outcome_check` | Same |
| CR-F3 minimum lead ≥ ~1 sol, both presets | Yes | L2 | Solver logs issue tick and outcome-visible tick per preset | — |
| CR-F4 every forecast reachable | Yes | L2 | Solver trigger coverage | — |
| CR-F5 ≥ 3 forecasts per phase | Yes | L2 | Count per phase per solver run | — |
| CR-F6 goal IDs, NGSS resolve, both bands' text | Yes | L0 | Cross-reference scan | — |
| CR-F7 FI never names its source idea | Yes | L0 | Banned-phrase scan on FI text keys | Banned list (ID) |
| CR-F8 length budgets | Yes | L0 | Word counts | — |
| **CR-F9 lock before reveal** | **Yes** | L2 + L4 | **Solver:** for each message with `reveals: [F…]`, check it is never delivered to a player whose forecast is still open; the interruption gate (ARCHITECTURE.md 9.6) already holds such messages, so the test proves the hold works. **Playthrough:** runtime assertion on every delivery. Also checks screens: opening the Monitor with an open `monitor_opened`-lock forecast must show the forecast card first | `reveals` on every message that could give a forecast away (Lead Game Designer); `lock` on every forecast |
| **F4a outcome check** | **Yes** | L2 + L4 | "No liquid water at the block 0.25 sol after the state change", under every ice-rule branch; plus a data test: puddle-drying time < 0.25 sol | Puddle-drying time in the balance data |
| **Threshold vs. curve** (content-rules §1.1 step 2) | **Yes** | L2 | Solver records the sol each named physical threshold is first passed per variant; fails if a forecast's regime lasts < ~2 sols or is passed before the forecast can be issued; prints threshold, sol, and dependent forecasts | Forecasts and beats declare the threshold they rest on (`rests_on: ["mars:boiling_point", …]`) |
| Step 3 outcome drift diff | Yes | L2 | Store last passing outcomes as a JSON fixture in the repo; print changes | — |
| Step 5 story beats vs. physics | Yes | L2 | `asserts` on beats (Lead Game Designer has added them) | — |
| Step 6 Record triggers don't orphan | Yes | L0 | Every forecast ID referenced by a Record trigger exists | — |
| CR-H1 report only for a real failure | Yes | L4 | Runtime assertion: the named cause is active in the simulation at send time | — |
| CR-H2 hint never states the answer | Yes | L0 | Banned-token lists per failure type and forecast, both bands | Banned tokens (Lead Game Designer, written) |
| CR-H3 named cause = largest cause | Yes | L4 | Compare with the Monitor cause list (from contribution `SourceId`s) | — |
| CR-H4 hint levels and outcome form | Yes | L1 + L4 | Unit tests of the fade rules; runtime check against per-player fade state | — |
| CR-H5 briefings name only unlocked things | Partly | L0 + L2 | Briefing lines declare the machine and value IDs they name (`names: [...]`); the solver checks they're unlocked at delivery. Free prose without declared IDs can't be checked reliably | `names` field on briefing and worked-example lines |
| CR-H6 drift alerts name a real drift | Yes | L4 | Runtime assertion | — |
| CR-R1 Record never fills from planet, admin, or another's events | Yes | L1 + loader | Loader already rejects planet-scope rules (ARCHITECTURE.md 8.5); unit test on cause and actor | — |
| CR-R2 no Record page from activity alone | Yes | L0 | Trigger types against an allowed list (answer, held fix, ≥ 8-word answer) | — |
| CR-R3 scored once per player | Yes | L1 + L4 | Export versus log; `late`, `expired`, `void`, `replay` never scored | — |
| CR-R4 no identity in exports | Yes | Every build | Privacy test | — |
| CR-T1–T5 text scans | Yes | L0 | Banned words and phrases, fiction-note keys, word counts | Lists (ID, Lead Game Designer) |
| CR-T6 reading grade | Yes, as a report | L0 | Flesch–Kincaid over English text; warns, doesn't fail | — |
| Section 5 (manual items) | No | — | Science correctness, usefulness, tone, rubric calibration stay human reviews | — |


---

## 6. Determinism and isolation

- **Seeded generated content.** Drift events, forecast choice order, and recap template picks are seeded from stable IDs: `hash(worldId, planet, eventKind, sequenceNumber)` for planet content and `hash(worldId, forecastId, pseudonym)` for a player's choice order. Never from wall-clock time or an unseeded `Random`.
- **Test:** generate, save the world, reload, regenerate, compare (same drift schedule, same choice order). Plus a save/load round trip of every saved-data class.
- **Parallel isolation.** Each run gets its own game directory, its own `mars-data` root (system property `mars.dataRoot`, test-only), its own server port (or none for GameTest), and a wrapper that writes a log and an `rc=` line at the end. Gradle test forks use `@TempDir`. Two runs on the same folder are refused by a lock file.

---

## 7. Save safety

| Rule | How |
|---|---|
| Schema version | Every NBT saved-data class and every Mars data JSON has `schema` (ARCHITECTURE.md §5 "Schema version in saved data"). |
| Migration per bump | One pure function per step (`v1 → v2`), unit-tested on a stored fixture file of the old version. Migrations chain. |
| Never overwrite a save from a newer build | On load, if any `schema` is newer than this build knows, the server **refuses to start** with a message naming the file and both versions. Vanilla would otherwise create fresh default data and the planet's progress would be overwritten at the next save. |
| Backup copy | Mars data JSON is written atomically (write to a temp file, then rename) and the previous version is kept as `.bak`. World data is covered by the session backups (ARCHITECTURE.md §7.3). |
| Corruption test | A test corrupts the main file on purpose (truncate, garbage bytes): Mars data JSON must load from `.bak` with a warning; corrupt **world** saved data must stop the server with a message pointing to `/mars rollback`, never silently start a fresh planet. |

---

## 8. Screenshot runs at 1280×720 (L5)

**Feasibility:** **likely feasible headless, to confirm in Stage 1.** This session's Linux environment has `Xvfb`, `xvfb-run`, and Mesa's software OpenGL driver (`swrast_dri.so`) installed (checked 2026-11-26). Minecraft 1.17+ needs OpenGL 3.2 core **[unverified version]**; Mesa's software renderer (llvmpipe) normally provides more than that **[unverified on this machine]**. Running the client also needs the game assets downloaded from Mojang's servers, which this network may block **[unverified]**.

**Plan:**
- `xvfb-run -a -s "-screen 0 1366x800x24" ./gradlew runClient --args="--width 1280 --height 720"` with the test harness mod in "shots" mode: load a fixed test world, step through scripted views (each gate snapshot from planetary keyframes §4, the Monitor at each disclosure stage, the Air window, the Shift Board, a failure report, the journal, the teacher overview), take a screenshot of each, and quit. Also at GUI scale 3 (the 420×236 limit, ARCHITECTURE.md §3.3).
- **A person looks at every screenshot that matters for the change** (PROCESS.md §2). Agents can look at the PNGs too; the owner reviews them at acceptance.
- **Readability checks** run on the same screenshots (Lead Artist round 2 request, accepted):
  - **Text on its real background:** the shots harness records the screen rectangle and colour of every text line it draws (a test-only hook in our screen code, like the handoff's `last_rect`). The check samples the screenshot's pixels behind each rectangle, takes the worst-case background, and computes the WCAG contrast ratio against the text colour: ≥ 4.5:1 for body text, ≥ 3:1 for icons (style guide §4.4). It prints the line, the ratio, and the background colour it read. Lines over the 3D world must have their backing strip (PROCESS.md §4).
  - **Glyph existence:** a unit test (no client needed) loads our font providers and the vanilla font definitions, and fails if any character used in `en_us.json` (curly quotes, true minus sign, arrows, °, ×, subscripts like the "2" in O₂ if used) has no glyph in the font that will draw it. It prints the key, the character, and its code point. A runtime twin in the shots run checks the font's "missing glyph" box never appears in the recorded text.
- Software rendering is slow and doesn't show real GPU bugs. It is for layout and colour, not performance.

**Result (Stage 1, 2026-11-26): it works here.** `tools/screenshots/run-shots.sh` started the real 1.21.1 client under Xvfb with Mesa llvmpipe at 1280×720, created a test world, ran the script, and saved 4 PNGs in about 64 s (second run, downloads cached). Minecraft's own FPS counter read 6–20 frames per second, which is enough for screenshots, not for performance judgments. Sound was off (no audio device). The first run stopped at the first-launch welcome screen; the second showed a real bug that the boot checks had missed (`/mars go` dropped the player into the void). Both are kept in `tools/screenshots/out/` as evidence.

**If the headless client doesn't work here:** the owner takes the screenshots **[owner]**: a dev client launched with `--width 1280 --height 720`, the harness's "shots" mode bound to a key so one press runs the whole scripted set, files saved to `run/screenshots/<build>/`, and the owner (or an agent reading the PNGs) reviews them against a checklist per change.

---

## 9. Back end in stages (Chapter 9: build "just what you need")

The early prototype builds just enough infrastructure to learn what's feasible; later prototypes show the front-to-back "plumbing" for each key capability.

| Back-end piece | Stage 2 prototype: "is it feasible?" | Stage 3 vertical slice: plumbing end to end | Stage 4+ |
|---|---|---|---|
| Data-driven gameplay (Index, gates, parameters in JSON) | One parameter (pressure), one gate, one machine, all from JSON; debug commands | All Phase 1 parameters, signals, holds, live condition | All phases |
| Instructional and assessment logic | Can the log tell **who** did what well enough for the feedback we designed? Prototype: a fake player repairs a machine; the log shows actor and machine ID; a pure function computes "repeat after own fix" from it | Forecast lifecycle (F1, F4a), one failure report with fading, Keeper's Record with 2 pages + "Cairn team" | All forecasts, reports, fading rules |
| Replay and data collection (action log) | Log writer with pseudonyms; privacy test | Full event list for Phase 1; rollback marks `superseded` | — |
| Export | — | `players.csv`, `events.csv`, `planets.csv`, `goals.csv`, `reflections.csv` with scrubbing, opened in a spreadsheet | — |
| APIs for instructor tools | Pause/resume, debug set | Teacher overview (one planet), export button, backup and rollback | Several planets, set phase, grade band |
| Client visuals driven by data | Sky tint from pressure (keyframe file) | Fog, storm overlay, weather mast, ambient sound layers | Clouds, full keyframes |
| Environment integration | — | Class server option A with 2 planets; a teacher-setup walkthrough draft | Load test; educator guide; LMS plumbing prototype (below) |

**LMS plumbing prototype (decided, owner, round 3): Stage 5.** A throwaway, offline converter outside the pack turns an exported `events.csv` into xAPI statements for a local test LRS on the developer's machine. It never ships, sends nothing over a network, and uses only pseudonymous IDs (ARCHITECTURE.md §11).

---

## 10. Target platforms and what needs the owner

| Platform | What we can test here | What needs the owner **[owner]** |
|---|---|---|
| Linux server (dedicated, headless) | L0–L4 fully | — |
| Linux client | L5 (if 8 works) | — |
| Windows client and server (most likely school setup) | Nothing | Install the pack from the `.mrpack` with a common launcher; run the class server from the start script; join with 2 clients |
| macOS client | Nothing | Launch and join |
| Weak hardware (integrated GPU, 8 GB RAM) | Nothing | FPS at the classroom preset in the dome, in a storm, at equilibrium; the Stage 3 load test machine |
| Child Microsoft accounts joining a server | Nothing | One test with a child account's multiplayer setting (school-deployment.md finding 6) |

Every stage exit repeats the owner checklist; the results go in the stage's debrief (TEAM.md 8.7).
