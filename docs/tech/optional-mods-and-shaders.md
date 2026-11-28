# Optional Mods, Shaders and Performance: Research and Verdicts

**Owner:** Technical Lead · **Data date:** Modrinth API and raw GitHub read on **2026-11-28** · **Scope:** NeoForge, Minecraft 1.21.1 only · **Status:** research (docs only; nothing was downloaded, installed or run; no shader was seen on a screen)

**Owner direction applied.** Look for a realistic and moody Mars. If an existing mod does what we built **better**, use it; if we can do better, build it. **Update 2026-11-28 (owner):** this is a design exercise and the owner is the only player, so classroom hardware does not drive the verdicts. A shader loader and a performance mod are recommended as the **default look on a normal gaming PC**. A plain no-shader path stays only as a cheap safety net (and for the Linux screenshot harness, which runs on software GL).

## How this was read (and its limits)

| Source | Used for | Trust |
|---|---|---|
| `api.modrinth.com/v2` project and version endpoints (licence id, `client_side`/`server_side`, loaders, `game_versions`, version dates, dependencies) | Licence flag, 1.21.1 builds, dates | Platform metadata set by authors; treated as untrusted data. Licence ids were cross-checked against LICENSE text where the text was reachable |
| `raw.githubusercontent.com` (LICENSE files, Iris source, EMI source) | Licence text, how Iris picks a shader program set per dimension | Primary source |
| **Not reachable or not done** | CurseForge flags (host blocked earlier); any real shader run; GitHub API (blocked) | **Not verified** |

Mark meaning: **Verified** = read from the source named. **Not verified** = from memory, inference, or an estimate. Effort numbers are **estimates, not measured**.

---

## 1. Shaders

### 1.1 Loader: Iris on NeoForge 1.21.1 (verified)

| Item | Finding | Source | Mark |
|---|---|---|---|
| **Iris Shaders** | Licence LGPL-3.0-only; client required, server unsupported; loader `neoforge`; 14 builds for 1.21.1 | Modrinth `/project/iris` | Verified |
| Latest builds | `1.8.14-beta.1+1.21.1-neoforge` (**beta**-named, but flagged `release`, 2026-06-13; changelog "Updates to Sodium 0.8") and `1.8.12+1.21.1-neoforge` (2025-06-17) | Modrinth versions | Verified |
| Required dependency | Iris needs **Sodium**. 1.8.14-beta.1 pins Sodium `0.8.13-neoforge` (release, 2026-08-28); 1.8.12 pins Sodium `0.6.13-neoforge` (2025-04-04) | Modrinth version dependency ids resolved with `/version/<id>` | Verified |
| **Oculus** (older Forge port) | Last update 2024-12-24, **0 builds for 1.21.1**. Not an option | Modrinth | Verified |
| Alternative loader | None found for NeoForge 1.21.1. (OptiFine is not on NeoForge 1.21.1 in what we read; **not checked in depth**.) | n/a | Not verified |

**Verdict: ADOPT Iris + Sodium as the default look for the owner's PC.** Pin the matched pair: Iris 1.8.14-beta.1 with Sodium 0.8.13 (newest pair, both flagged `release`), falling back to Iris 1.8.12 with Sodium 0.6.13 if the newer pair misbehaves. Caveat: the newer Iris carries "beta" in its version string; judge it by playing.

### 1.2 Shader packs for 1.21.1 (verified metadata; look not seen)

All below list `iris` as loader and 1.21.1 builds on Modrinth. The look is **not seen** by me (no GPU, no eye).

| Pack | Licence (Modrinth id; text read where noted) | Latest 1.21.1 build | May we put it in our pack? | Verdict |
|---|---|---|---|---|
| **Complementary Shaders: Reimagined** | `LicenseRef-Custom` → "Complementary License Agreement 1.8" ([License.txt](https://raw.githubusercontent.com/ComplementaryDevelopment/ComplementaryReimagined/main/License.txt), first ~2500 characters read). Free for normal use. §1.2: may be added to a modpack if credited on the pack page when enabled by default, and the modpack author takes responsibility for problems | r5.10, 2026-11-18, 16 builds for 1.21.1 | Yes, **unmodified**, with credit. Rest of the licence (modification rules) **not read** | **Primary candidate.** Most-downloaded realistic pack with a moody, atmospheric look; also the sister **Unbound** (more stylised) |
| **Photon Shaders** | Custom ([LICENSE](https://raw.githubusercontent.com/sixthsurge/photon/main/LICENSE), read in full): use free; "may not redistribute on platforms that provide monetary compensation to creators (e.g. Modrinth, CurseForge) without explicit written permission"; "may redistribute as part of a (commercial) Modpack" | v1.4, 2026-10-19, 8 builds | Contradictory-looking terms: **do not bundle**; the owner installs it by hand | **Alternative to try** (physically based sky and fog; look not seen) |
| **BSL Shaders** | All Rights Reserved (Modrinth id) | 10.1.8, 2026-09-21 | **No** (ARR). Owner may install by hand | Try by hand if wanted |
| **Solas Shader** | All Rights Reserved (Modrinth id) | 3.9, 2026-11-02 | **No** (ARR). By hand only | Try by hand if wanted |
| MakeUp Ultra Fast | LGPL-3.0-or-later | 9.6a, 2026-11-21 | Yes | **Light fallback** for weak GPUs; not the moody look |
| Miniature Shader | MIT | 2.19, 2026-07-29 | Yes | Stylised, small (44 KB); not realistic |
| Rethinking Voxels | `LicenseRef-Complementary-Agreement` | r0.1-beta9 (**beta**) | n/a | Heavy, beta; skip |
| Sildur's Vibrant, Kappa, Nostalgia | All Rights Reserved | 2025-2026 | No | Not considered |

Other "moody" hits in the Modrinth search (Spooklementary, NOISE Shaders, Sarp, Alpha Piscium GPL-3.0) were not evaluated beyond licence. **Not verified:** which pack looks best on a butterscotch Mars; only the owner can judge (section 4).

### 1.3 How a shader pack meets our sky, fog and dimension

What we built (read in `mod/src/main/java/mars/client/sky/`): the dimension type has `"effects": "mars:mars"`; `MarsSkyEffects extends DimensionSpecialEffects` overrides `renderSky` (gradient dome, 0.66-size sun, moon, stars); `SkyClient` listens to `ViewportEvent.ComputeFogColor` and `RenderFog` (the latter cancelled to apply our fog distances); colours come from `pack/assets/mars/keyframes/sky.json` keyed on pressure. Biomes carry static `sky_color` 11041626 (#A87B5A), `fog_color` 12884595 (read in `pack/data/mars/worldgen/biome/*.json`).

| Question | What I read | Consequence | Mark |
|---|---|---|---|
| Which shader programs run on `mars:planet_1`? | Iris `getCurrentDimension()` returns the level's own id (`mars:planet_1`) unless the shader pack's `dimension.properties` maps it, or the dimension type's effects are the End or Nether ones ([Iris.java](https://raw.githubusercontent.com/IrisShaders/Iris/1.21.1/common/src/main/java/net/irisshaders/iris/Iris.java), lines 595-625). `ShaderPack.getProgramSet` returns the pack's **base (overworld-style) programs** when the id is not mapped ([ShaderPack.java](https://raw.githubusercontent.com/IrisShaders/Iris/1.21.1/common/src/main/java/net/irisshaders/iris/shaderpack/ShaderPack.java), lines 556-585) | `mars:*` planets get the pack's **Earth overworld look** (blue sky logic, Earth water, rain, daylight curve). We cannot add a `mars` folder without editing and redistributing the pack | Verified (source) |
| Is our `renderSky` still drawn? | Iris hooks `LevelRenderer.renderSky` with phase markers (SKY, SUN, MOON, STARS) and forces a flag argument false ([MixinLevelRenderer.java](https://raw.githubusercontent.com/IrisShaders/Iris/1.21.1/common/src/main/java/net/irisshaders/iris/mixin/MixinLevelRenderer.java), lines 160-205). It also decides sky-disc drawing from `effects().skyType()` and `hasSkyLight()` ([IrisRenderingPipeline.java](https://raw.githubusercontent.com/IrisShaders/Iris/1.21.1/common/src/main/java/net/irisshaders/iris/pipeline/IrisRenderingPipeline.java), line 1004-1006). Our dome uses `SkyType.NORMAL`, so Iris treats it as a normal sky | Our geometry may still be drawn but **most packs recolour or replace the sky in their own shaders**, so the keyframe colours may not show | Unknown: needs a real run |
| Fog colour | Our `ComputeFogColor` handler changes the same colour vanilla uses; Iris passes the fog colour to packs as a uniform | Likely survives as an input; **many packs apply their own fog colour and density** on top | Inference; not verified |
| Fog distance | We set near/far planes through `RenderFog`. Packs such as Complementary commonly compute their own fog from render distance | The pressure-driven "air thickens" cue (fog start/end) may be **weakened or lost** under a shader | Not verified |
| Sky colour uniform | Packs read `skyColor`, which vanilla derives from the biome `sky_color` and time, not from our `DimensionSpecialEffects` | Keep the biome `sky_color` and `fog_color` at a Mars baseline (they are #A87B5A and `12884595` today). They are static JSON, so they cannot follow pressure | Inference; not verified |
| Clouds | We planned our own cloud mesh (ARCHITECTURE 2.5.3 option A). Packs draw their own clouds | Our density-by-hydrosphere clouds may be hidden or doubled | Not verified |
| Particles (heat shimmer, dust motes, frost wisps) | Vanilla particle pipeline; Iris draws particles in its own pass | Expected to show; blending may differ | Not verified |
| Dust storm dim | Done with fog and colours (atmosphere-plan 5.2), not a lightmap | Shader lighting can ignore it | Not verified |

**What we must change (small, data-first) if the owner wants shaders as the default look:**

| # | Change | Cost (estimate, unverified) |
|---|---|---|
| S-1 | **Owner test first** (section 4): load Complementary Reimagined on `mars:planet_1` and record what the sky, fog, sun, stars and storm look like at 0.6, 5, 30 and 75 kPa (`/mars index` or the pressure debug command). Decide from pictures | 30-60 min of the owner's time |
| S-2 | Ship a **shader options file** (`shaderpacks/<pack>.zip.txt`) as a preset: turn off or reduce Earth features that clash (rain, aurora, blue-sky tint; names depend on the pack, **not verified**). It is user config, not a modified pack, so it does not touch the pack's licence | 0.5 day |
| S-3 | If the pack ignores our sky: choose between (a) accept the pack's sky with the preset, and mark the sky-pressure cue as **shader-off only**; or (b) make the cue not depend on the sky (it already has fog, sun and sound channels, atmosphere-plan 1) | A decision, not code |
| S-4 | Optionally detect a running shader pack on the client (an Iris API call; the method name `IrisApi#isShaderPackInUse` is **not verified**: my fetch of that file path returned 404) and add a one-line note to the Monitor or log. Needs a soft dependency, so no compile-time link | 0.5 day |
| S-5 | Write a dimension map for our own fork of a pack | **Not recommended**: needs a modified pack, so licence terms apply (Complementary's modification clauses not read; Photon and the ARR packs forbid it) |

The artist's colour-step checks (OKLab) were done against vanilla rendering. Under a shader they no longer hold. That is the real cost of making shaders the default: the pressure-to-sky readout becomes pack-dependent.

### 1.4 Safety net and packaging

- **Default on, with a fallback.** Ship Iris and Sodium as normal mods. Ship Complementary Reimagined as a pack entry with the packwiz `[option]` block (`optional = true`, `default = true`, plus a `description`; fields confirmed in [packwiz `core/mod.go`](https://raw.githubusercontent.com/packwiz/packwiz/main/core/mod.go), lines 43-47). The no-shader path is "pick *(none)* in Iris": no code, no cost. Mark: field names **verified**; how packwiz handles a Modrinth `shader` project type and the `shaderpacks/` folder **not verified**.
- Auto-selecting the pack needs `config/iris.properties` (`shaderPack=...`): **not verified**; the owner can also pick it in the menu.
- The screenshot harness runs on software GL under Xvfb. Shaders are **not expected to run there**. Keep the no-shader path as the harness default and keep pressure-step pictures taken without a shader.
- Weak-GPU risk is the owner's own PC only; MakeUp Ultra Fast is the light fallback if frame rate is poor.

---

## 2. Performance mods (verified metadata)

| Mod | Licence | NeoForge 1.21.1 build | Notes | Verdict |
|---|---|---|---|---|
| **Sodium** | PolyForm Shield 1.0.0 (text read: [LICENSE.md](https://raw.githubusercontent.com/CaffeineMC/sodium/1.21.1/stable/LICENSE.md)). Distribution allowed with a copy of the terms and any `Required Notice`; noncompete clause applies only to products that compete with Sodium | **`0.8.13` release (2026-08-28)**; 0.8.14-beta.2 (2026-11-25). **Correction:** `mod-licenses.md` (round 3) said "only betas"; a release build now exists | Required by Iris. Client-only. Replaces the chunk renderer, not `DimensionSpecialEffects`; our sky, cloud and particle code compatibility is **to test** | **ADOPT** (default) |
| Embeddium | LGPL-3.0-only | `1.0.15` (2025-01-24); no update for 22 months | Stale; Iris on NeoForge pairs with Sodium now | **SKIP** |
| Sodium Extra | LGPL-3.0-only | 0.9.5 release (2026-10-25), 0.9.6-beta.1 | Adds cloud/fog/animation toggles. Its fog and cloud options could override ours: test | OPTIONAL |
| ImmediatelyFast | LGPL-3.0-or-later | 1.6.15 (2026-10-13) | Faster immediate-mode drawing (HUD, our Monitor and journal screens) | OPTIONAL |
| ModernFix | LGPL-3.0-only | 5.27.27 (2026-10-24) | Load time and memory. Client and server | OPTIONAL |
| FerriteCore | MIT | 7.0.3 (2026-01-25) | Memory | OPTIONAL |
| Lithium | LGPL-3.0-only | 0.15.4 (2026-06-27) | Server tick optimisations. Changes game logic caches; not needed for us | SKIP for now |
| Dynamic FPS | MIT | 3.11.4 | Lower FPS when window is unfocused | SKIP |

**Recommendation:** Sodium (+ Iris) as default; ImmediatelyFast, ModernFix, FerriteCore only if the owner sees a problem (the owner's choice; no need to bundle on spec). Each adds a thing to break on update.

**CurseForge distribution flags: still not checked.** Modrinth is the main channel.

---

## 3. Mods that do what we built: better or not

Rule from the owner: **if a mod clearly does it better and can be tied to our Index and data, adopt it; otherwise keep ours.**

| Area | Candidate (licence, 1.21.1 NeoForge, source: Modrinth unless noted) | Ours that it would replace | What we would lose | Effort saved vs. risk (**estimates, unverified**) | Verdict |
|---|---|---|---|---|---|
| **Terrain / worldgen** | Terralith (Stardust Labs License; 2.6.2; server required); Tectonic (MIT; 3.0.28; reshapes **overworld** height); Biomes O' Plenty (ARR; 21.1.0.14 beta); Regions Unexplored (MIT; 0.6.3) | `mars:planet` chunk generator (W1: noise terrain, craters, dunes with one fixed wind, buttes, canyon, ripples, ores tied to data, pad) | These are **Earth biome and terrain** mods: forests, oceans, rivers, snow. They hook the overworld biome source; they would not generate craters or dunes and have no Mars data. We would lose the dome pad, the terrain-to-ore tie, and control over the look | Saves 0 days (our generator exists); big mismatch | **SKIP.** Honest: they are better Earth terrain, not Mars terrain |
| **Distant view** | Distant Horizons (LGPL-3.0; 3.3.3 release 2026-09-29; 28 MB; client and server optional) | Nothing; we have vanilla render distance and fog | Possible LOD generation cost for our custom generator; fog design is built for vanilla range. Shader support depends on the pack | Gives long vistas of canyons and buttes cheaply: could be a strong "moody Mars" piece | **OPTIONAL.** Owner tries it after shaders |
| **Sky / fog** | Fog Overrides (LGPL-3.0; last update 2025-05); Nuit (MIT); Smooth Skies (LGPL-3.0); others in the search | `MarsSkyEffects` and the fog events keyed on pressure keyframes | They set static or per-biome looks; none reads the Planetary Index | Saves a day we already spent | **SKIP** |
| **Weather / dust storm** | Improved Weather (CC-BY-4.0 **datapack**: adds sandstorm); Weather Storms & Tornadoes (ARR); Project Atmosphere (ARR); Particle Rain (MIT) | `mars:solar_spike` and S3-1 storm: seeded schedule on the power grid, forecasting, panel dust | Earth weather tied to vanilla rain and biomes (desert sandstorm). No tie to the power grid, to the pressure-scaled storm cap, or to our forecast. Classroom determinism does not matter to the owner now, but the learning rule (a storm cuts solar by a known share) does | Saves 0; adds an Earth look | **SKIP** |
| **Particles / ambience** | Particular (LGPL-3.0); Hear the Wind (AGPL-3.0); Grim & Bleak (MIT) | Heat shimmer, dust motes, frost wisps (data-driven, shown by meaning) | Meaning (shimmer = hot machine) must look the same for everyone | none | **SKIP** |
| **Sound ambience** | AmbientSounds (LGPL-3.0; 6.3.11; 85 MB jar; required dependency `OsZiaDHq`, not resolved); Dynamic Surroundings Rebirth (MIT; 5,974 downloads); MAtmos (ARR); Sound Physics Remastered (GPL-3.0; 1.5.2 **beta**) | Our wind and ambience tied to pressure (ARCHITECTURE 2.14) | AmbientSounds is a rule-based engine for biome/time/weather sounds, driven by resource-pack JSON; **possibly** usable with a Mars sound set but not tied to Index values (not verified). GPL on Sound Physics blocks bundling with an MIT mod without review | Maybe 1-2 days if it could read our pressure (**unverified**; I did not read its engine) | **SKIP** for now; revisit if the owner finds our ambience thin |
| **Recipe viewer** | **EMI** (MIT; 1.1.24 `release`, 2026-05-13; 13 builds; client required, server optional). **JEI** (MIT; releases up to 19.64.0.455, 2026-10-16; betas to 19.76.1.482 2026-11-26; 248 builds). REI (MIT) not preferred | None (we have no viewer; `ARCHITECTURE.md` 2.15 says "hidden" currently means journal text never names a thing early) | Control: a viewer shows every recipe at once, which breaks "the planet is the quest log" unless locked recipes are hidden | EMI: `EmiRegistry.removeRecipes(Predicate)` and `removeEmiStacks(Predicate)` exist in the 1.21 branch, which builds for Minecraft 1.21.1 and mod version 1.1.24 ([source](https://raw.githubusercontent.com/emilyploszaj/emi/1.21/xplat/src/main/java/dev/emi/emi/api/EmiRegistry.java), [gradle.properties](https://raw.githubusercontent.com/emilyploszaj/emi/1.21/gradle.properties)). JEI's hide API: **not verified** (I could not find the file path). Hooking gate sync to a viewer plugin: about 1-2 days (**unverified**) | **ADOPT EMI** (one stable release, MIT, hiding API read). JEI is an acceptable swap. Build the hiding plugin as its own slice, tied to latched gates (`unlocks` lists already exist in gate JSON) |
| **Suit / oxygen** (ROADMAP "Oxygen/suit mod") | Cosmonautics: Deep Air (MIT; bridge for Create: Deep Seas, not a suit mod); Simple Dive Gear (CC0; water and lava air timer); Creating Space (MIT; Create rockets to other planets); Ad Astra (Terrarium licence; already out); Bottled Air (ARR) | A suit and helmet whose oxygen follows the `breathable_air` live condition and the Breathable hold gate | None names a Mars suit tied to an atmosphere value. Simple Dive Gear's timer is for water; not Index-driven | We would still write the Index link; saves a small item and timer (under 1 day, unverified) | **SKIP: build our own.** This **closes** the ROADMAP open item with "no candidate fits" |

**Summary of the rule:** none of the Earth-biome, weather, sky or sound mods can read the Planetary Index; the ones that could (a rule engine like AmbientSounds) would need glue code as large as our own. Only the recipe viewer is something we do not have, and it is a better answer than anything we could build.

---

## 4. What only the owner can test

| # | Test | Why I cannot |
|---|---|---|
| T-1 | **Shader look on a real GPU.** Install Iris + Sodium, load Complementary Reimagined (and optionally Photon, BSL, Solas by hand), walk `mars:planet_1` at 0.6, 5, 30 and 75 kPa and in a storm. Note sky colour, fog colour and distance, sun size, stars, clouds, heat shimmer, dust motes | No GPU, no eye; software GL |
| T-2 | Frame rate with and without shaders on the owner's PC (render distance 12) | No measurement possible here |
| T-3 | Does Sodium 0.8.13 break any of our client code (sky dome, fog, Monitor and journal screens, particles)? Boot the real client with Sodium alone first, then with Iris | Dev environment has not run it |
| T-4 | Windows and macOS: Iris and Sodium behaviour; macOS OpenGL is old, shader packs can fail there | Only Linux seen |
| T-5 | Distant Horizons with our generator (optional) | Not run |
| T-6 | **CurseForge** "allow distribution" flags for any mod we put in a CurseForge export | Host blocked; still unchecked |
| T-7 | Pick the recipe viewer by trying EMI (and JEI) for a few minutes | Taste |

## 5. Decisions for the owner

1. **Default shader stack?** Recommendation: Iris + Sodium + Complementary Reimagined, **only after T-1 shows the Mars sky survives**; otherwise ship Iris and Sodium without a default pack and keep our sky as the look. *Ill-structured* (look versus the pressure-sky learning cue).
2. Accept **EMI** as the recipe viewer and queue a "hide until gate" slice. *Well-structured.*
3. Close the ROADMAP "Oxygen/suit mod" item as **build our own**. *Well-structured.*
4. Try Distant Horizons after shaders? *Well-structured, owner taste.*

## Revisit if

Iris or Sodium drops 1.21.1 fixes; Complementary changes licence terms; a mod appears that reads a data-driven atmosphere value (then re-check sound and sky rows).
