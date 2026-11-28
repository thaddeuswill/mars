# Running-state table: one visible moving part per machine

**Owner:** Lead Artist · **Date:** 2026-11-28 · **Status:** art and model variants PREPARED and script/unit-checked; NOT referenced by any blockstate yet (needs the Technical Lead's property below); not seen in a real client (see report) · **Source:** `docs/art/inspiration-survey-look-and-sound.md` steal 5 (Create, Factorio: one visible moving part per machine, a running-state table) · **Generator:** `tools/art-preview/gen_running_states.py` (rerunnable; reads existing textures, writes new files only) · **Test:** `mod/src/test/java/mars/art/TextureSpecTest.java`

All frame counts and tick timings below are **INVENTED** (game feel, not science). 20 ticks = 1 s. Every animation is 4 frames, 8 to 20 ticks per frame, never interpolated (pixel steps), so nothing changes faster than 2.5 times a second (no flicker). Cost: six animated strips of 16 x 64 px; no new render passes except one cutout puff on the Outgasser.

## 1. State language (same on every machine)

| State | Meaning (from the powered state) | Lamp shape | Moving part |
|---|---|---|---|
| off | No power on the grid, or machine locked out | Lamp dark (`#1E2328`), no lit pixel | none |
| idle / starved | Powered but not working (low power share, nothing to do) | Hollow ring (3 x 3 blue machines), 1 lit pixel (2 x 2 green lamps), 2 px (oxygen maker) | none |
| running | Doing its job | Solid lit lamp (blue `#56B4E9` or green `#009E73`) | the ONE part in the table |

Shape carries the state, not hue alone: off = nothing lit, idle = a small or hollow lamp, running = a full lamp plus motion (motion is the colour-blind-safe signal). Solar panel and Monitor have no running table (see section 3).

## 2. Per-machine table

| Machine | Running: the one moving part | Frames x ticks (loop) | Texture file (new) | Off / idle look |
|---|---|---|---|---|
| Regolith Oven | Coil in the front window: a 2 px hot notch (`#FFD27A`) ping-pongs across a dim coil (`#B37400`) | 4 x 8 (32 ticks) | `regolith_oven_front_running` (animated) | off: lamp dark; idle: 1 lit green pixel (`regolith_oven_front_off/_idle`) |
| Regolith Outgasser | Dust puff rises from the stack: small, wider, widest and high, then a faint wisp (`#B8A890`, `#CDBFAA`), on a 4 x 8 px cutout element above the stack | 4 x 10 (40) | `regolith_outgasser_puff` (animated, cutout); front lamp `_front_running` is the existing lit lamp | as Oven |
| Oxygen maker | Output bar fills along the lamp strip, 1, 2, 3, 4 px (`#009E73`) | 4 x 10 (40) | `o2_generator_face_running` (animated; band and plate untouched) | off: lamp pixels grey; idle: the existing 2 px lamp |
| Water reclaimer | Water in the front window rises one row per frame (3, 4, 5, 6 rows) with a pale surface line (`#D5E0E8`), then the bucket "empties" and restarts | 4 x 20 (80, slow) | `water_reclaimer_front_running` (animated; lamp solid blue) | off: lamp dark; idle: the existing hollow ring |
| Atmospheric processor | A lit segment (`#D9DEE2`) climbs the canister between its dark rings, bottom to top (the "canister pulse") | 4 x 8 (32) | `atmospheric_processor_canister_running` (animated) + `_front_running` (solid lamp, static) | off: `_front_off`; idle: existing front |
| Super-greenhouse emitter | Glow line (`#F0E442`) climbs the 3 px flare lip, then one dark beat (vent glow) | 4 x 8 (32) | `super_greenhouse_emitter_flare_running` (animated) + `_front_running` | off: `_front_off`; idle: existing front |
| Battery | No motion. Charge shown by the count of lit bars: 0 to 3 (shape = count), bars fill from the bottom, status pixel dark at 0 | static, 4 levels | `battery_front_charge0..2` (level 3 = the existing `battery_front`) | n/a (no off/idle; level 0 is "empty") |

The made-up band (rows 14 and 15) and plate are never changed in any frame (checked by test for the oxygen maker and reclaimer; the other animated textures do not contain them).

## 3. No running table

| Block | Why |
|---|---|
| Solar panel (round array) | Real hardware; its state is the dust stage (already five models) and daylight. A moving part would break "real NASA hardware is the reference". |
| Planetary Monitor | The screen is the live part already; no machine state to add. |

## 4. What the Technical Lead must add (nothing in `mod/` was touched)

1. A block state property on the six machines: `machine_state` = `off | idle | running` (enum), derived from the existing powered/running logic that feeds the Monitor Power row ("needs power", "low power", "running"): off = "needs power", idle = "low power" or no job, running = "running". Update the state only when it changes (not every tick), so chunks re-render rarely.
2. Battery: an int property `charge` 0..3 from stored / capacity (quarters, rounded down; 3 = 75 percent or more... full bar only at 100 percent if preferred: Technical Lead picks and tells the Artist).
3. Blockstates: variants `facing=<d>,machine_state=<s>` using the model files below with the same `y` rotations as today. The existing `<name>.json` model equals the old look and stays valid (it is the fallback).
4. Outgasser puff model has `"render_type": "minecraft:cutout"` already in `regolith_outgasser_running.json`. The 8 px puff overhangs the top of the block (y 16 to 24, like the reclaimer overhang); outline and collision unchanged.
5. Optional later (audio, not built): Oven hum, processor pulse tick; see `docs/audio/`.

| Machine | Models (`pack/assets/mars/models/block/`) |
|---|---|
| Regolith Oven | `regolith_oven_off`, `_idle`, `_running` |
| Regolith Outgasser | `regolith_outgasser_off`, `_idle`, `_running` |
| Oxygen maker | `o2_generator_off`, `_idle` (= old model), `_running` |
| Water reclaimer | `water_reclaimer_off`, `_idle` (= old model), `_running` |
| Atmospheric processor | `atmospheric_processor_off`, `_idle` (= old model), `_running` |
| Super-greenhouse emitter | `super_greenhouse_emitter_off`, `_idle` (= old model), `_running` |
| Battery | `battery_charge0`, `_charge1`, `_charge2`, `_charge3` (= old model) |

## 5. Why nothing animates today without the property

An animated `.mcmeta` texture plays all the time. A machine that is switched off must not move, and the existing machine textures are all shared by every state, so animating them now would make a dead machine look busy and break the teaching signal ("is it working?"). No existing texture was changed (all `git diff` clean on them); every animated strip is a new `*_running` file.

## 6. Not verified

- Not seen in a real client (no blockstate uses the models yet; a temporary wiring run was not allowed in this session). Flat contact sheets of the frames were viewed.
- Colour-vision simulation not run on lamp states; the signal is shape and motion by design.
- Cutout puff rendering and the 8 px overhang: unverified.
- All timings are invented; owner look and the first in-game run may change them.
