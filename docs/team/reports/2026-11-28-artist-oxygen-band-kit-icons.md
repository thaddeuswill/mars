# Lead Artist: oxygen maker band, seed kit icons, processor port check

**Date:** 2026-11-28 · **Branch:** claude/elegant-brown-anu1ci

## Handoffs received
- Game Designer: oxygen maker gets the made-up band, wording "Game-sized version of a real idea". **Accepted.** Art done; the tooltip wording is lang (not mine, Technical Lead).
- Technical Lead: four seed kit items reuse the carbonate texture; processor east ports and lamp standby not yet seen. **Accepted**, all three done.

## Done
- **Oxygen maker** (`pack/assets/mars/textures/block/o2_generator_{face,back,end,side}.png`): band on rows 14-15 (2 on / 2 off, global x), plate x10..13 y9..12 on the front. Status lamp moved from the lower-right to the upper-right panel (it sat on the band rows). Regenerated with `tools/art-preview/gen_phase1_batch1.py` (palette keys P, Q, N added; band colours exempt from its 6-colour region budget). Only those four PNGs changed; every other texture is byte-identical (checked with `git status`). Tube and top untouched.
- **Seed kit icons** (`pack/assets/mars/textures/item/{biocrust,meadow,wetland,forest}_kit.png`): 16 x 16 packet, ink outline, crimped top, colour band (`#D55E00`, `#F0E442`, `#56B4E9`, `#0072B2`) plus an ink symbol (crumbs, grass tufts, reed with head, two berries). No green, no purple. Generator `tools/art-preview/gen_seed_kit_icons.py` (new, rerunnable, self-checks); the four item model JSONs now point at their own icon.
- **Processor ports**: the real-client close-up showed the dark alloy ports sinking into the shaded east side. Retoned lighter in `gen_invented_machines.py` (`atmospheric_processor_port.png`); the second close-up reads clearly (two ports, pale tube between).
- **Emitter and reclaimer lamp standby** (hollow blue square in a dark ring): read as a hollow square in the real client at the normal view distance. No change needed.
- **TextureSpecTest** (test file only): oxygen maker band and plate, kit icons (exist, 16 x 16, clear corner, 0/255 alpha, no green, distinct, model path).
- Docs: `docs/art/w1-placeholder-textures.md`, BACKLOG #49 and HANDOFF section 00 (my lines).
- Screenshots cited (committed with `git add -f`): `tools/screenshots/out/20261128-oxygen-band/` (`band_oxygen_front`, `band_oxygen_end_east_v2`, `band_processor_east_ports_v2`, `band_emitter_front`, `band_reclaimer_front`). Script `tools/screenshots/oxygen_band_closeups.shots` (and `oxygen_band_ports.shots`). Untracked screenshot folders deleted.

## What I saw
Real client, generated pad (top block y 80), no "Unknown block" in client.log (0 matches). The oxygen maker front shows the purple dashes at the base, the slashed plate and the green lamp in its dark ring; the east end and back show the dashes. Seen in flat shade the dashes look dark purple, which is dim but legible. The magenta placeholder-marker pixels (older, deliberate) still show at region corners. The camera in the harness sits farther than the `view` numbers suggest, so close-ups are crops.

## Not verified
- Kit icons were seen only in a flat enlarged preview, not in an inventory or hand (no inventory shot script). Meadow and wetland symbols are similar in shape; the hue band separates them (colour-vision simulation not run: **unverified**).
- Band colorblind simulation not run (shape carries meaning).
- Full build is not green here, for reasons outside my files: `./gradlew test` ends 312 tests, 2 failed (`GateTest.monitorShowsNextGoalThenGoalsReached`, `MonitorGoalViewTest.pressureGoalShowsLogProgressAnEstimateAndGoalsCount`, "1 / 2" vs "1 / 6"). Cause: other agents' uncommitted new files in `pack/data/mars/gates/` (breathable, equilibrium, phase_3_begins, thaw) and edits to `pack/data/mars/machines/*.json`. My new tests pass. `runGameTestServer`: `MARS-SELFTEST RESULT PASS passed=138 failed=0`.

## Decisions needed
None (ill-/well-structured: n/a). Optional: an inventory/hand screenshot script for the kit icons (well-structured, small).

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Oxygen maker tooltip must say "Game-sized version of a real idea" and the journal should explain the purple dashes.
> **Needed from them:** lang key (I did not touch lang). **Done when:** tooltip shows the wording.

> **To:** Producer / whoever owns the gate and rate work · **Problem type:** well-structured
> **Problem:** Uncommitted gate JSON and machine rate edits break two unit tests in my working tree.
> **Done when:** the owner of those files commits them with matching test updates.

## Risks
Band colour is dim on shaded faces; if learners miss it, the tooltip wording still carries the message. Kit icons are 16 px and the symbols are 2 to 4 px.

## Shared model updates
None.
