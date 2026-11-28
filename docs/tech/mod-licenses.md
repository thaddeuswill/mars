# Mod Licenses, Versions, and Redistribution

**Owner:** Technical Lead · **Checked:** 2026-11-26 · **Stage:** 1 (Pre-production)

Covers every candidate mod and tool named in `ARCHITECTURE.md` and `DESIGN.md` §4. ROADMAP Stage 1 says redistribution must be checked before we build on a mod. This file is that check. The Producer keeps the release permissions table (risk R-23) and can link here.

## How this was checked

- **License:** read from the `LICENSE` file in each project's official source repository, on the branch for Minecraft 1.21.1, through `raw.githubusercontent.com`. This is a primary source.
- **1.21.1 support:** read from the same branch's `gradle.properties` (the version numbers the authors build against).
- **Not reachable this session:** `modrinth.com`, `api.modrinth.com`, `docs.neoforged.net`, `neoforged.net`, the GitHub API, and `edusupport.minecraft.net` were blocked by the session network. So the Modrinth and CurseForge distribution flags, and "last updated" dates on the mod sites, are **not checked**. They need a recheck with those hosts allowed.
- A license says what *we* may do with the files. A platform flag (the CurseForge "allow distribution" switch) separately controls whether launchers and tools may download the file. Both matter (see "Redistribution rules" below).

## Results

| Mod / tool | Role in the pack | License (primary source) | 1.21.1 NeoForge build seen | Redistribution in our pack | Status |
|---|---|---|---|---|---|
| **NeoForge** | Mod loader | LGPL-2.1 ([LICENSE.txt, 1.21.1 branch](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/LICENSE.txt)) | Yes (it is the loader) | Players install it through the launcher; we don't ship it | OK |
| **Mekanism** | Gas processing, industry | MIT, "Copyright (c) 2017-2025 Aidan C. Brady" ([LICENSE, 1.21.x branch](https://raw.githubusercontent.com/mekanism/Mekanism/1.21.x/LICENSE)) | Yes: `mod_version=10.7.19`, `minecraft_version=1.21.1`, `java_version=21` ([gradle.properties](https://raw.githubusercontent.com/mekanism/Mekanism/1.21.x/gradle.properties)) | MIT allows it; keep the notice | **OK** |
| **Create** | Mechanical industry | **Split:** code MIT; **assets All Rights Reserved** (`src/main/resources/assets/`) ([LICENSE.md, mc1.21.1/dev](https://raw.githubusercontent.com/Creators-of-Create/Create/mc1.21.1/dev/LICENSE.md)) | Yes: `mod_version = 6.0.11`, `minecraft_version = 1.21.1` ([gradle.properties](https://raw.githubusercontent.com/Creators-of-Create/Create/mc1.21.1/dev/gradle.properties)) | Shipping the **unmodified jar** by reference (launcher downloads it) is the normal case. **Don't copy, edit, or re-texture Create's assets** in our resource pack without permission. | OK with that limit |
| ↳ Flywheel (bundled in Create) | Rendering library | MIT ([LICENSE.md](https://raw.githubusercontent.com/Engine-Room/Flywheel/1.21.1/dev/LICENSE.md)) | Bundled by Create (jar-in-jar) | Comes inside Create | OK |
| ↳ Ponder (bundled in Create) | In-game tutorial scenes | MIT ([LICENSE](https://raw.githubusercontent.com/Creators-of-Create/Ponder/mc1.21.1/dev/LICENSE)) | Bundled by Create | Comes inside Create | OK |
| ↳ Registrate (bundled in Create) | Registration library | MPL-2.0 (checked on the 1.20 branch only: [LICENSE](https://raw.githubusercontent.com/tterrag1098/Registrate/1.20/LICENSE)); 1.21 branch **unverified** | Bundled by Create | Comes inside Create | OK (recheck 1.21 branch) |
| **KubeJS** | Recipe scripting | LGPL-3.0 ([LICENSE.txt, 2101 branch](https://raw.githubusercontent.com/KubeJS-Mods/KubeJS/2101/LICENSE.txt); README: "distributed under the GNU Lesser General Public License v3.0") | Yes: `mod_version=2101.7.2`, `minecraft_version=1.21.1` ([gradle.properties](https://raw.githubusercontent.com/KubeJS-Mods/KubeJS/2101/gradle.properties)) | Unmodified jar by reference: fine. Our scripts are our own work, not a derivative of KubeJS. | **OK** |
| ↳ Rhino (required by KubeJS) | JavaScript engine | MPL-2.0 ([LICENSE](https://raw.githubusercontent.com/KubeJS-Mods/Rhino/main/LICENSE)); `main` branch, 1.21.1 branch not checked | KubeJS 2101 requires `rhino` (mods.toml, `type = "required"`) | Unmodified jar by reference | OK |
| ↳ Architectury API | Not required by KubeJS 2101 on NeoForge (its mods.toml lists only NeoForge and Rhino as required) | LGPL-3.0 ([LICENSE.md](https://raw.githubusercontent.com/architectury/architectury-api/1.21/LICENSE.md)) | — | Only if another mod needs it | Not needed now |
| **ProbeJS** | Dev-only typings for KubeJS | **GPL-3.0** ([LICENSE, 1.21 branch](https://raw.githubusercontent.com/Prunoideae/ProbeJS/1.21/LICENSE)) | 1.21 branch exists; exact 1.21.1 build **unverified** | **Dev only. Never ship it in the pack.** We don't need to; it only generates editor typings. | OK as a dev tool |
| **TerraBlender** | Biome injection (only if we use datapack worldgen) | LGPL-3.0 ([LICENSE, 1.21.1](https://raw.githubusercontent.com/Glitchfiend/TerraBlender/1.21.1/LICENSE)) | Yes: 1.21.1 branch, `neoforge_version=21.1.23` ([gradle.properties](https://raw.githubusercontent.com/Glitchfiend/TerraBlender/1.21.1/gradle.properties)) | Unmodified jar by reference | OK; likely **not needed** (a custom dimension uses its own biome source) |
| **FTB Quests** | Was: lore journal. **Rejected** (user decision: custom journal) | No license file in the repo ([README, 1.21.1/main](https://raw.githubusercontent.com/FTBTeam/FTB-Quests/1.21.1/main/README.md)); its required library **FTB Library is "All Rights Reserved", "visible source"** ([LICENSE.md](https://raw.githubusercontent.com/FTBTeam/FTB-Library/1.21.1/main/LICENSE.md), [README](https://raw.githubusercontent.com/FTBTeam/FTB-Library/1.21.1/main/README.md)). FTB Quests' own terms **unverified**. | A 1.21.1 build `2101.1.28` appears on CurseForge (search result, not fetched) | ARR means use only on the platform's terms or with permission | **Rejected.** The decision removes the risk (R-24 can close). |
| **packwiz** | Pack as text files in git | MIT ([LICENSE](https://raw.githubusercontent.com/packwiz/packwiz/main/LICENSE)) | n/a (tool) | Dev tool; not shipped | OK |
| **Spotless** | Formatting in the build | Apache-2.0 ([LICENSE.txt](https://raw.githubusercontent.com/diffplug/spotless/main/LICENSE.txt)) | n/a (build tool) | Not shipped | OK |
| **JUnit 5, ArchUnit** | Unit tests, module-boundary tests | **Not checked this session** (commonly EPL-2.0 and Apache-2.0) | n/a | Test-only; not shipped | Recheck |
| **Oxygen/suit mod** | Only if a standalone one fits | No candidate named yet | — | — | Open (ROADMAP Stage 1) |

## Round 3 recheck (2026-11-26, network open)

Modrinth's API (`api.modrinth.com/v2`) was reachable; CurseForge (`curseforge.com`, `api.curseforge.com`) and `support.modrinth.com` were **still blocked**, so CurseForge distribution flags remain **unchecked**.

| Mod / tool | Modrinth project licence | Latest Modrinth build for NeoForge 1.21.1 | Notes |
|---|---|---|---|
| Mekanism | MIT | 10.7.19.85 (2026-04-10), 21 builds for 1.21.1 | Required on client and server (Modrinth side flags) |
| Create | "Create Mod License" → [LICENSE.md](https://raw.githubusercontent.com/Creators-of-Create/Create/HEAD/LICENSE.md): **assets All Rights Reserved, code MIT** | 6.0.10+mc1.21.1 (2026-04-21) | **Correction:** the assets are inside the jar we would bundle, so **shipping Create's jar in our offline classroom bundle redistributes ARR assets. Not allowed without permission.** A `.mrpack` that references Modrinth's copy is fine (the launcher downloads it from Modrinth). Create is optional and left out of the classroom bundle anyway (ARCHITECTURE.md 2.15) |
| KubeJS | LGPL-3.0-only | 2101.7.2-build.381 (2026-11-01) | **New finding:** this build lists **two required dependencies** on Modrinth: Rhino (MPL-2.0) and **Better Advanced Tooltips** (MIT). Add both to the pack. My round 1 check of KubeJS's `mods.toml` saw only Rhino; the newer build added one |
| Rhino | MPL-2.0 | 2101.2.7-build.85 release (2026-06-08); a 2101.2.8 beta (2026-07-19) | Licence file confirmed on `main` ([LICENSE](https://raw.githubusercontent.com/KubeJS-Mods/Rhino/main/LICENSE)); no 1.21.1 branch found by name. Use the Modrinth release KubeJS pins |
| Registrate (inside Create) | — | Create pins `MC1.21-1.3.0+67` | **MPL-2.0 confirmed on the 1.21.1 branch** ([LICENSE, 1.21.1/dev](https://raw.githubusercontent.com/tterrag1098/Registrate/1.21.1/dev/LICENSE)) |
| GeckoLib | MIT | 4.9.4 (2026-11-22) | Not adopted (ARCHITECTURE.md 2.13) |
| Sodium | PolyForm Shield 1.0.0 | **Updated 2026-11-28:** 0.8.13 is a **release** (2026-08-28) for NeoForge 1.21.1; an earlier note here said betas only (0.8.14-beta.2 is the newer beta). Source: `docs/tech/optional-mods-and-shaders.md` (Modrinth, read 2026-11-28) | **Owner accepted 2026-11-28:** Iris plus Sodium as the default stack after the owner's look test; not a classroom pack. CurseForge flags still unchecked |
| Better Advanced Tooltips | MIT | (pulled in by KubeJS) | New |

**Build and test tools** (not shipped):

| Tool | Licence (primary source) |
|---|---|
| ModDevGradle | LGPL-2.1 ([LICENSE](https://raw.githubusercontent.com/neoforged/ModDevGradle/main/LICENSE)) |
| JUnit 5 | EPL-2.0 ([LICENSE.md](https://raw.githubusercontent.com/junit-team/junit5/main/LICENSE.md)) |
| ArchUnit | Apache-2.0 ([LICENSE](https://raw.githubusercontent.com/TNG/ArchUnit/main/LICENSE)) |
| PIT (pitest) | Apache-2.0 ([LICENSE.txt](https://raw.githubusercontent.com/hcoles/pitest/master/LICENSE.txt)) |
| packwiz | MIT (re-confirmed); optional-mod field `option.optional` confirmed in [source](https://raw.githubusercontent.com/packwiz/packwiz/main/core/mod.go) |

## Redistribution rules that apply to how we ship

| Platform / channel | Rule | Source | Effect on us |
|---|---|---|---|
| **CurseForge** | Each author has a switch for third-party distribution. If it's off, third-party tools lose access to the files through the API. "The API will respect the toggle choice over the license type choice." CurseForge modpacks must be made in the CurseForge App format. | CurseForge support, 3rd Party API ([article](https://support.curseforge.com/support/solutions/articles/9000207877)); via search, not fetched | For each CurseForge-hosted mod, check the switch, not just the license. |
| **Modrinth** | A modpack may include a mod only if "the mod is licensed under terms that allow for redistribution"; the pack author is responsible for the license terms. | Modrinth, "modpack changes" ([news](https://modrinth.com/news/article/modpack-changes)); via search, not fetched | MIT, LGPL, MPL mods above are fine. ARR mods (e.g. FTB Library) are not, without permission. |
| **Our own server pack for classrooms** | We'd bundle jars for an offline school install. This *is* redistribution. | Follows each license | Ship only mods whose license allows redistribution of the **whole jar**: not Create (its jar holds ARR assets; round 3 correction). Include each license and notice in `pack/licenses/`. |

**Recommendation.** Publish on Modrinth (`.mrpack`) as the main channel and generate a CurseForge export from packwiz. Three rules: never ship ProbeJS (GPL-3.0, dev only); never modify Create's assets; and **never put Create's jar in an offline bundle** (its assets are All Rights Reserved; round 3 correction). The classroom offline bundle (in-house machines, KubeJS, Rhino, Better Advanced Tooltips, Mekanism if wanted) is possible because those are MIT, LGPL, or MPL.

## Version picture (for the Stage 1 version decision)

| Finding | Source | Status |
|---|---|---|
| NeoForge for 1.21.1 needs a Java 21 JDK | NeoForge docs, getting started (1.21.1), via search ([docs](https://docs.neoforged.net/docs/1.21.1/gettingstarted/)); Mekanism's 1.21.x build uses `java_version=21` | Verified (two sources) |
| NeoForge has moved on: a 26.1 release exists, on Java 25 | neoforged.net news ([26.1 release](https://neoforged.net/news/26.1release/)), via search, not fetched | Verified by search only |
| All key mods still ship 1.21.1 builds | Mekanism 10.7.19, Create 6.0.11, KubeJS 2101.7.2 (gradle.properties above); FTB Quests 1.21.1 build dated Jul 2026 on CurseForge (search) | Verified (primary for the first three) |
| The 1.21.1 MDK uses the ModDevGradle plugin | NeoForge MDK template ([neoforgemdks](https://github.com/neoforgemdks)), via search | Verified by search only |

**Read.** 1.21.1 is still the version with the widest mod support for our stack, but it is no longer the newest. Staying on it is the low-risk choice for Stage 1. The risk is that 1.21.1 builds stop getting fixes during our ~7 roadmap stages. Logged under Risks in the report (R-06).

## Our own licences (plan)

**Decision (user, 2026-11-26, on coordinator recommendation). A plan, not legal advice; unverified by any legal review.**

| What | Planned licence | Notes |
|---|---|---|
| Our code (companion mod, KubeJS scripts, build and test scripts) | **MIT** | Same as Mekanism and Create's code; compatible with linking LGPL KubeJS/NeoForge as separate jars **[unverified, not legal advice]** |
| Our non-code content: art, audio, story and journal text, keyframes, datapack content | **CC BY 4.0** | Teachers and players may adapt and share with credit (fits the user-generated-content hook, TEAM.md §4) |
| Educator materials (guide, worksheets, rubrics) | **CC BY 4.0** | |
| Third-party content | Their own licences, unchanged | **Never** retexture or redistribute All Rights Reserved assets (Create's assets, FTB Library). **Never** ship ProbeJS (GPL-3.0, dev only) |

- **No `LICENSE` file is added to the repository until release**, because this is a design exercise (user decision). Until then, `mod/` and `pack/` carry no licence header; the build plan adds the files at Stage 7.
- At release: a `LICENSE` (MIT) at the root and in the mod jar, a `LICENSE-CONTENT` (CC BY 4.0) covering `pack/assets/` and `pack/data/`, and `pack/licenses/` with every bundled third-party licence and notice.
- **Revisit if** a school or publisher needs different terms, or a legal review says the MIT/CC split doesn't work for a mixed jar.

## Still to do

1. ~~Modrinth~~ done (round 3). **CurseForge** distribution flags still unchecked (`curseforge.com` blocked); only matters if we publish a CurseForge export.
2. ~~Registrate and Rhino~~ done (round 3): both MPL-2.0.
3. ~~JUnit 5 and ArchUnit~~ done (round 3): EPL-2.0 and Apache-2.0.
4. Name a standalone oxygen/suit candidate, or record "build our own" (ROADMAP Stage 1).
5. Our own licences: **planned** (section above); add files at release.
6. Sodium for NeoForge (performance mod candidate, ARCHITECTURE.md 2.5.3): PolyForm Shield 1.0.0 ([LICENSE, 1.21.1/stable](https://raw.githubusercontent.com/CaffeineMC/sodium/1.21.1/stable/LICENSE.md)). Check whether it permits bundling in our offline classroom pack before adding it.
7. GeckoLib: MIT ([LICENSE, main](https://raw.githubusercontent.com/bernie-g/geckolib/main/LICENSE)). Not adopted (ARCHITECTURE.md 2.13); recorded in case the animation decision is revisited.
