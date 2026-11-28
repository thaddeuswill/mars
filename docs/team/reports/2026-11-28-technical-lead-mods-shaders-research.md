# Technical Lead: optional mods, shaders and performance research (2026-11-28)

Main document: [docs/tech/optional-mods-and-shaders.md](../../tech/optional-mods-and-shaders.md). Docs only. Nothing downloaded or run; Modrinth API and raw GitHub read on 2026-11-28.

## Handoffs received
- Owner: realistic, moody Mars; use a mod if clearly better and tied to our Index, else keep ours. **Accepted.**
- Owner update: not a classroom; shaders and a performance mod may be the **default** look on a normal PC, with a plain no-shader fallback. **Accepted and applied** (classroom hardware does not drive verdicts).

## Done
- `docs/tech/optional-mods-and-shaders.md`: shader loader, 4 packs with licences, how Iris picks programs for `mars:planet_1`, what must change, performance mods, 8 mod-area verdicts, owner-only test list.
- Key facts read: Iris 1.8.14-beta.1 needs Sodium 0.8.13 (release 2026-08-28); Iris 1.8.12 needs Sodium 0.6.13. Iris source: an unmapped dimension gets the pack's base (Earth overworld) programs. Photon's licence forbids redistribution on monetised platforms. BSL and Solas are All Rights Reserved. EMI 1.1.24 (MIT) has `removeRecipes` and `removeEmiStacks`.
- **Correction** to `docs/tech/mod-licenses.md` round 3: Sodium now has a 0.8.13 `release` build for NeoForge 1.21.1 (not only betas). I did not edit that file (owned elsewhere in this task); the new doc supersedes the line.

## Verdicts
Shaders: ADOPT Iris+Sodium as default look, Complementary Reimagined first, after the owner's test. Performance: ADOPT Sodium; Embeddium SKIP; others OPTIONAL. Terrain, sky/fog, weather, particles, sound, suit/oxygen: SKIP (ours). Recipe viewer: ADOPT EMI (JEI acceptable). Distant Horizons: OPTIONAL.

## Decisions needed
1. Default shader stack, only if the Mars sky survives T-1 (ill-structured: look vs. pressure-sky cue). Recommend yes after test.
2. Accept EMI and queue a hide-until-gate slice (well-structured).
3. Close ROADMAP "Oxygen/suit mod" as build our own (well-structured).
4. Try Distant Horizons (owner taste).

## Handoffs
> **To:** Lead Artist · **Problem type:** ill-structured
> **Problem:** Our sky colour steps (OKLab recheck) assume vanilla rendering; a shader pack may replace them.
> **Your view:** one cue among several (fog, sun, sound), so accept shader-off as the measured baseline.
> **Needed from them:** after the owner's T-1 pictures, say whether the look is acceptable under shaders.
> **Done when:** a keep / retune call is recorded.

## Risks
- Under a shader, sky colour and fog distance (our pressure cues) may be ignored. Not verified; needs a real run.
- Iris 1.8.14-beta.1 is beta-named. Sodium 0.8.x not yet tested against our sky, fog or screen code.
- Bundling shader packs: only Complementary (credit, unmodified) is clearly allowed; Photon, BSL, Solas by hand.
- CurseForge flags still unchecked.

## Shared model updates
- Lesson: Iris maps unknown dimensions to the pack's Earth overworld programs; a custom planet has no shader look of its own unless the pack's `dimension.properties` names it.
- Proposed decision (owner to accept): shaders may be the default look for the owner; shader-off stays the measured baseline.
