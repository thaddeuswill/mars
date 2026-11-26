# Report: Technical Lead, Stage 1 build (BACKLOG #18) and in-game screenshots (BACKLOG #19)

- **Role:** Technical Lead
- **Date:** 2026-11-26
- **Outputs:** `mod/` (standalone Gradle build: NeoForge 21.1.267, ModDevGradle 2.0.148, Gradle 9.2.1 wrapper, Spotless 8.1.0, JUnit 5.11.4, ArchUnit 1.4.1, Gson 2.11.0; no Parchment because maven.parchmentmc.org is unreachable), `pack/data/mars/` (dimension, dimension type, biome, parameters, terrain), `tools/screenshots/`, `docs/tech/launch-guide.md`, updates to `ARCHITECTURE.md`, `docs/tech/build-plan.md` (§1a), `docs/tech/test-strategy.md` (§8). Jar: `mod/build/libs/mars-0.1.0-stage1.jar` (92 KB, sha256 `2b2953ee…`, gitignored; delivered to the owner directly).
- **Everything below was run in this Linux sandbox unless stated.**

## Results
- **Build:** `./gradlew build` successful; forced `./gradlew test --rerun`: **37 passed, 0 failed**.
- **SME ice table:** 15 cases all pass (e.g. "ice at 2.00 kPa, 18.5 °C (boiling point 17.57 °C): expected boil_away, read boil_away"; "4.99 kPa, 20 °C: short_puddle"; "5.00 kPa, 10 °C: stable_water"; "0.60 kPa, 40 °C: sublimate"; "75 kPa, 0 °C: stay_ice"). Boiling point 2.00 / 7.07 / 17.57 / 24.14 / 29.02 / 32.94 °C at 0.7 / 1 / 2 / 3 / 4 / 5 kPa, and 100.00 °C at 101.325 kPa; NaN below the triple point.
- **Index model:** one Outgasser for 10 sols reads 1.600 kPa (expected 1.600); classroom ×4, group 4, 1 sol reads 0.7951 (expected 0.7951); O2 partial pressure 16.50 kPa at 75 kPa and 22%.
- **Threshold-vs-curve:** triple point passed at sol 0.59; 5 kPa at sol 23.55 (the balance sheet expects about 25); heater ice boils away from sol 0.59 to 23.55. ArchUnit: the pure core has no Minecraft, NeoForge, or Mojang dependencies. 13 JSON content files parse. F4a puddle-drying reads 0.1 sol (limit 0.25).
- **Checks proven able to fail:** a deliberately broken data copy failed 12 of 37 tests for the right reasons (puddle drying read 0.3; boiling point read 12.00 °C at 0.7 kPa; wrong ice results); corrupted Antoine coefficients failed threshold-vs-curve ("boil-away regime lasted 0.0 sols"); the boot guard with a 1 s timeout printed `FAIL boot_guard last_step=common_setup elapsed=2s exit=3`.
- **Headless boot:** `./gradlew runGameTestServer` exit 0: `RESULT PASS passed=7 failed=0` (mod loaded; mars:mars dimension type and mars:regolith_flats biome present; `planet_1.json` decoded with vanilla's codec; command registered; ice rule found and evaluates). **Limit:** the GameTest server builds its own world without datapack dimensions, so it can't confirm the level exists.
- **Real client under Xvfb (software GL):** rc=0, 64 s, four PNGs; FPS counter read 6–20 at 1280×720; sound off (no audio device); offline account. Screenshots: player on Mars (tan sky, chat shows "You are on Mars (planet 1)…"), iron/glass/concrete/sea lantern on flat red sand, an overview, and the overworld after `/mars back`. Vanilla clouds still show on Mars.
- **Bug found by the screenshots (missed by the boot checks):** `/mars go` dropped the player into the void ("Dev fell out of the world"): the landing height was read before the target chunk loaded. Fixed by loading the chunk first; the travel check had been passing only because a later check loaded the chunk. Evidence kept in `tools/screenshots/out/20261126-144512-bug-fell-out-of-world/`.

## Not proven
The dedicated server with the Mars level (needs the owner to accept the Minecraft EULA; an attempt to write `eula=true` was blocked by the safety check and not worked around); the jar loaded from a normal NeoForge `mods` folder (dev runs load compiled classes); Windows and macOS; an automated playthrough (not a Stage 1 item).

## Downloaded (Gradle or Maven only; no third-party program fetched from GitHub and run)
Gradle 9.2.1 distribution (services.gradle.org); NeoForge 21.1.267 and NeoForm (maven.neoforged.net); Minecraft 1.21.1 client, server, libraries, assets (Mojang hosts); Spotless, google-java-format, foojay-resolver 1.0.0 (Gradle plugin portal, Maven Central); JUnit, ArchUnit, Gson (Maven Central).

## Definition of done (build)
Parse/compile gate met; unit tests met; boot test with watchdog met (GameTest server only); screenshot run at 1280×720 met (every PNG looked at); automated playthrough not built; owner acceptance pending.

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| Accept the Minecraft EULA for the dedicated-server self-test (owner only): read https://aka.ms/MinecraftEULA, create `mod/run-selftest/eula.txt` containing `eula=true`, run `./gradlew runSelftestServer` | Owner's call | Well-structured |
| Do the inactive self-test classes ship in the jar? (they do nothing without `-Dmars.selftest=true`) | Keep for Stage 1–3 diagnostics; exclude at release, as the dev helper already is | Well-structured |
| CI host (from round 3) | GitHub Actions for layers L0–L2 | Well-structured |

## Handoffs
- **Game Designer, SME, Instructional Designer:** the ice rule file moved from `data/mars/dimension/ice_rule.json` to `data/mars/terrain/ice_rule.json` (Minecraft loads every JSON in `dimension/` as a dimension). Update any references; the coordinator found only an old historical report citing the old path.
- **Lead Artist:** block models and blockstates are in `pack/assets/mars/` but those blocks aren't registered yet; the screenshot tool is ready for art previews once they are.
- **Producer:** route the EULA decision to the owner; schedule owner checklist P1 (Windows install) and the jar-from-`mods`-folder test; update the Stage 1 checkboxes (skeleton builds and loads an empty Mars dimension; Spotless and tests set up).

## Risks
Dedicated server, normal NeoForge install, Windows, and macOS never run (the real-client screenshots are the strongest evidence the dimension works); the GameTest server can't confirm the level exists; Gson 2.11.0 is from knowledge as Minecraft 1.21.1's version; Stage 1 commands `/mars go` and `/mars back` are open to everyone (Stage 3 replaces them); no Parchment mappings (less readable parameter names).

## Shared model updates
- **Lessons:** screenshots caught a bug that passing checks missed (order checks so each tests the real starting state; keep "a person looks at every screenshot" in the definition of done); Minecraft's datapack folder names are a registry namespace, so never put our own JSON in `data/<ns>/dimension/`, `worldgen/`, `dimension_type/`, or other vanilla registry folders (a content test now guards `dimension/`).
- **Decisions:** ice rule path `data/mars/terrain/ice_rule.json`; `mod/` is a standalone Gradle build without Parchment; the dev screenshot helper is client-only, opt-in, and excluded from the shipped jar.
