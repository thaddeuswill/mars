# Build Plan: Companion Mod Gradle Skeleton

**Owner:** Technical Lead · **Status:** Plan only, 2026-11-26. **Nothing has been built or run.** · **Stage:** 1 (ROADMAP: "Companion mod skeleton: builds, loads, and registers an empty Mars dimension")
**Related:** [ARCHITECTURE.md](../../ARCHITECTURE.md) §3 (coding practices, 3.1 version, 3.2 layout), [test-strategy.md](test-strategy.md), [mod-licenses.md](mod-licenses.md)

## 1. Starting point: the official MDK

Start from NeoForge's 1.21.1 ModDevGradle MDK template, read 2026-11-26 ([gradle.properties](https://raw.githubusercontent.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle/main/gradle.properties), [build.gradle](https://raw.githubusercontent.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle/main/build.gradle), [wrapper](https://raw.githubusercontent.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle/main/gradle/wrapper/gradle-wrapper.properties)):

| Setting | MDK value | Our value |
|---|---|---|
| `minecraft_version` | 1.21.1 | 1.21.1 (settled) |
| `neo_version` | 21.1.267 | 21.1.267; pinned per roadmap stage |
| ModDevGradle plugin | 2.0.148 | same |
| Gradle wrapper | 9.2.1 | same (this session's machine has Gradle 8.14.3 installed; the wrapper downloads its own) |
| Parchment mappings | 2024.11.17 for 1.21.1 | same |
| Java toolchain | 21 | 21 (this session's machine has OpenJDK 21.0.13) |
| `mod_id` | examplemod | `mars` |
| `mod_license` | All Rights Reserved | Left at the MDK default until release (no licence file until then, mod-licenses.md "Our own licences") |
| Run configs | client, server, gameTestServer, data | same, plus `selftest` and `shots` (section 5) |

## 1a. What was actually built (Stage 1, 2026-11-26)

- `mod/` is a **standalone** Gradle build (`mod/settings.gradle`, wrapper in `mod/`), not a subproject of a root build. Simpler, and the only build we have.
- **No Parchment mappings:** `maven.parchmentmc.org` is not reachable from the build environment. Parameter names are Mojang's defaults; nothing else changes.
- Spotless 8.1.0 with google-java-format; JUnit 5.11.4, ArchUnit 1.4.1, Gson 2.11.0 for pure tests (pure tests run without Minecraft on the classpath).
- Run configurations added: `gameTestServer` (boot checks), `selftestServer` (dedicated server; needs the owner's EULA acceptance), `shotsClient` (dev screenshots).
- Owner instructions: [launch-guide.md](launch-guide.md).

## 2. Repository layout

```
settings.gradle            includes :mod (root project = repo)
mod/
  build.gradle             from the MDK, plus Spotless, ArchUnit, content tasks
  gradle.properties
  src/main/java/mars/<module>/        core, planets, session, index, gates, dimension,
                                      hazards, machines, fauna, story, learning, admin, client
  src/main/resources/META-INF/        neoforge.mods.toml (from the MDK template)
  src/main/resources/schemas/         JSON schemas for every content type
  src/main/templates/                 MDK metadata templates
  src/test/java/                      JUnit 5 + ArchUnit (pure; no Minecraft)
  src/testmod/java/                   dev-only harness mod: boot guard, watchdog, playthrough, shots
pack/                                 see ARCHITECTURE.md §3.2 (assets/ and data/ are copied into the jar)
gradlew, gradlew.bat, gradle/wrapper/ committed
```

One Gradle project (`:mod`) is enough. The pure simulation core is a package (`mars.core`, `mars.index`…) tested without Minecraft, not a separate project; ArchUnit keeps it free of `net.minecraft` imports. **Revisit if** compile times hurt: then split the pure core into its own Java project with no Minecraft on the classpath at all.

## 3. Plugins and dependencies

| Item | Why | Licence (checked?) | Shipped? |
|---|---|---|---|
| `net.neoforged.moddev` 2.0.148 | NeoForge dev environment | LGPL-2.1 (checked round 3) | No |
| `com.diffplug.spotless` | Google Java Style | Apache-2.0 (checked, mod-licenses.md) | No |
| JUnit 5 | Unit tests | EPL-2.0 (checked round 3) | No |
| ArchUnit | Module-boundary and "no display-name lookup" rules | Apache-2.0 (checked round 3) | No |
| Rhino (parser only, test scope) | KubeJS syntax gate | MPL-2.0 (main branch checked) | No |
| PIT (`pitest`) | Mutation testing of the pure core, stage exits | Apache-2.0 (checked round 3) | No |
| Mekanism, Create (optional, ARCHITECTURE.md 2.15), KubeJS | `compileOnly` APIs + `localRuntime` jars for dev and GameTests | MIT / MIT code, ARR assets / LGPL-3.0 (checked) | By reference in `pack/mods/` |
| Rhino, Better Advanced Tooltips | Required by KubeJS 2101.7.2 (Modrinth, round 3) | MPL-2.0 / MIT | By reference |

Versions of the test libraries are chosen when the skeleton is built, from their official release pages; none is pinned here.

## 4. Tasks

| Task | Does | Runs in |
|---|---|---|
| `processResources` | Copies `../pack/assets/**` and `../pack/data/**` into the jar, alongside `src/main/resources` | Every build |
| `validateContent` | Schema and cross-reference checks on all content (test-strategy §2.1) | Before `test` |
| `kubejsSyntax` | Parses `pack/kubejs/**/*.js` with Rhino | Before `test` |
| `spotlessCheck` | Formatting | `check` |
| `test` | JUnit 5 + ArchUnit + solver (L1–L2) | `check` |
| `runGameTestServer` | NeoForge GameTests (L3) | `check` on CI |
| `runSelftest` | Headless dedicated server playthrough per variant (L4), with timeout | Manual / CI |
| `runShots` | Client under `xvfb-run` at 1280×720 (L5) | Manual |
| `exportPack` | `packwiz modrinth export` → `.mrpack`; licence check of every mod row; fails if Create's jar is in an offline bundle | Release candidates |
| `exportClassroomBundle` | Offline server bundle for schools: our jar, KubeJS, Rhino, Better Advanced Tooltips; **no Create** (ARR assets), Mekanism optional | Stage 4+ |

**Hot reload (checked round 3).** ModDevGradle runs load the mod's source set output (`mods { mars { sourceSet sourceSets.main } }`, [MDG README](https://raw.githubusercontent.com/neoforged/ModDevGradle/main/README.md)), which includes the `processResources` output where `pack/assets` and `pack/data` are copied. So a content edit needs `./gradlew processResources`, then `/reload` (data) or F3+T (assets). **Likely fine; confirm in the skeleton.** MDG also has built-in JUnit support (`neoForge { unitTest { enable() } }`) for tests that need Minecraft classes on the classpath; pure tests don't need it.

## 5. Run configurations

| Run | Based on | Extra |
|---|---|---|
| `client` | MDK `client()` | `--width 1280 --height 720` by default, so developers see the smallest supported window |
| `server` | MDK `server()` | `--nogui`; `run/server/` game dir; `mars.dataRoot` set to `run/server/mars-data` |
| `gameTestServer` | MDK | `neoforge.enabledGameTestNamespaces=mars` |
| `selftest` | `server()` + testmod | `mars.selftest.variant=<name>`, own game directory per variant, watchdog on |
| `shots` | `client()` + testmod | `mars.shots=true`; meant for `xvfb-run` |
| `data` | MDK `data()` | Data generators for blockstates/models where hand-written JSON is tedious |

## 6. Stage 1 exit checklist (from ROADMAP)

- [ ] `./gradlew build` passes on a fresh clone (Linux here; Windows **[owner]**)
- [ ] The client loads and `/execute in mars:planet_1 run tp ...` reaches an empty Mars dimension
- [ ] `spotlessCheck`, `test` (with one trivial pure test and the ArchUnit rules) pass
- [ ] `validateContent` fails on a deliberately broken JSON fixture and passes on the real content (proves it can fail)
- [ ] The boot guard fails a deliberately broken boot with `FAIL boot_guard last_step=…` instead of hanging
- [ ] `packwiz` pack in `pack/` with NeoForge 21.1.267, Mekanism, Create, KubeJS; a dedicated server boots from it
- [ ] One headless client screenshot at 1280×720 under `xvfb-run`, or a recorded reason it doesn't work here (test-strategy §8)
- [ ] **Basic CI (decided, owner, round 3):** L0–L2 (`spotlessCheck`, `validateContent`, `kubejsSyntax`, `test` with the solver) on every push
