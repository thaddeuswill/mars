# Lead Artist: running states (2026-11-28)

## Handoffs received
Task from the coordinator: running-state table (inspiration survey steal 5), data-only animation, `TextureSpecTest`, build, screenshot. Accepted. Constraint kept: no `mod/src/main`, no `pack/assets/mars/lang`, no blockstate edits.

## Done
- `docs/art/running-state-table.md`: three states (off, idle/starved, running) and the ONE moving part per machine, with frame counts and ticks (INVENTED); battery shows charge by bar count; solar panel and Monitor explicitly excluded (real hardware / already live).
- `tools/art-preview/gen_running_states.py` (new, rerunnable, writes new files only): 6 animated strips + `.png.mcmeta` (Oven coil notch, Outgasser dust puff on a cutout element, oxygen-maker output bar, reclaimer rising water, processor canister segment, emitter vent glow), `*_front_off/_idle/_running`, `battery_front_charge0..2`, and models `*_off/_idle/_running`, `battery_charge0..3` in `pack/assets/mars/models/block/`. `git status` shows no modified existing texture or model.
- Why no live animation now: an `.mcmeta` texture animates in every state, and all current machine textures are shared by every state, so animating them would make a dead machine look busy. All animation is in new files for the property below.
- `mod/src/test/java/mars/art/TextureSpecTest.java`: 3 new tests (valid mcmeta and frame heights, 2 to 8 frames, frametime 8..40, no interpolation, frames really differ; band and plate rows identical in every frame; every `_off/_idle/_running/_charge` model references existing textures). TextureSpecTest: 10 tests pass.
- Docs: `docs/art/w1-placeholder-textures.md` (status), `BACKLOG.md` #49 (my sentence), `HANDOFF.md` section 00 (my paragraph).
- Real output: `cd mod && ./gradlew spotlessApply build runGameTestServer --offline`: BUILD SUCCESSFUL, `MARS-SELFTEST RESULT PASS passed=139 failed=0`. (A first run failed only because another agent's Gradle run was writing the same build directory; rerun green. Unit total now 344 including other agents' uncommitted tests.)
- Seen: flat 8x contact sheet of all animated frames (looked at). Not a real-client view.

## Decisions needed
1. Battery `charge` thresholds (quarters vs full only at 100 percent). Well-structured; recommend quarters, rounded down. Technical Lead decides, tells Artist.
2. Allow a temporary local blockstate swap to take a real-client screenshot of the animation? (Ill-structured only in that it touches shared pack files while the Technical Lead works; recommend yes, in a scratch branch or after the property lands.)

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Wire the prepared running-state art.
> **Context:** `docs/art/running-state-table.md` section 4. Enum property `machine_state` off/idle/running on Oven, Outgasser, oxygen maker, reclaimer, processor, emitter (from the same logic as the Monitor Power row); int `charge` 0..3 on the battery; blockstate variants to `*_off/_idle/_running` and `battery_charge0..3`; update only on change.
> **Needed from them:** the property, blockstates, and a GameTest that each state maps to an existing model.
> **Done when:** a real-client shot shows the Oven coil notch moving between two shots 8+ ticks apart; the Artist then retunes timing.

> **To:** Lead Game Designer / Technical Lead (audio) · **Problem type:** well-structured
> **Needed:** none yet; audio cues per running state are listed as optional in the table, owner `docs/audio/`.

## Risks
- Unverified in 3D: puff overhang (y 16 to 24) and cutout render; animated-texture cost is small (6 x 4 frames).
- Fast look: reclaimer reset (bucket empties) may read as a glitch; revisit after first run.
- Lamp colours not run through colour-vision simulation (shape and motion carry state).
- Screenshot step was blocked by the permission classifier (temporary blockstate rewrite plus client run); I did not work around it.

## Shared model updates
- Term: **running-state table** (`docs/art/running-state-table.md`): off / idle / running per machine, shape and motion not hue.
- Lesson: an `.mcmeta` animation is state-blind; state-dependent looks need block state properties, so art is prepared as separate files first.
