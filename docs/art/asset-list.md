# Asset List — Mars Terraforming

**Owner:** Lead Artist · **Status:** Draft 1 (2026-11-26) · **Stability:** fluid; updated whenever DESIGN.md, the story bible, or the balance sheet adds content.
**Built from:** [DESIGN.md](../../DESIGN.md) (current version with Regolith Outgasser, ice-to-water rule, perchlorate, ground collapse, shrouds, lures, quiet zones, Shift Board), [story bible](../design/story-bible.md) draft 2, [balance sheet](../design/balance-sheet.md), [ROADMAP.md](../../ROADMAP.md). Specs: [style guide](style-guide.md), [planetary keyframes](planetary-keyframes.md).

## Status key

| Status | Meaning |
|---|---|
| **Spec'd** | Look, colors, and size are written in docs/art. Ready to make |
| **Needed** | Required by the design; only named here, spec comes later |
| **Blocked** | Waits on a design or tech decision (named in Notes) |
| **Placeholder** | A placeholder file exists (with the magenta marker, style guide §5.5) |
| **Final** | Shipped art |
| **Cut-list** | Depends on content that is first on the cut list (risk R-01) |

**Stage** = the ROADMAP stage that first needs it: **2** prototype, **3** vertical slice (Phase 1), **4** production (Phases 2–3), **5+** polish. Sizes per style guide §5.1. Paths assume `pack/assets/mars/textures/` (TL to confirm).

**Current state: nothing is Final; the only file made is the palette reference sheet (placeholder).** No texture or model exists yet, because `pack/assets/` is not set up (Technical Lead).

---

## 1. Environment, sky, weather (data and textures)

| ID | Asset | Kind / size | Stage | Status | Notes |
|---|---|---|---|---|---|
| E-01 | Sky keyframes by pressure | Data (JSON) | **2** | Spec'd | Keyframes §3.1. Prototype exit needs this |
| E-02 | Fog keyframes by pressure | Data | **2** | Spec'd | Keyframes §3.1 |
| E-03 | Sunset halo and band keyframes | Data | 3 | Spec'd | Blue → orange flip |
| E-04 | Night sky colors, star density | Data | 3 | Spec'd | |
| E-05 | Storm overlay values | Data | 3 | Spec'd | Keyframes §3.7 |
| E-06 | Dust speck particle | 8×8, 2 frames | 3 | Spec'd | |
| E-07 | Sun | 32×32 | 3 | Spec'd | Drawn at 0.66× |
| E-08 | Phobos | 16×16 | 5 | Needed | Optional; rises in the west |
| E-09 | Deimos, Earth "evening star" | 2–4 px quads | 5 | Needed | Optional |
| E-10 | Clouds map (wisps → cumulus) | 256×256 | 4 | Blocked | Needs custom cloud renderer (TL cost) |
| E-11 | Rain particle | 8×8 | 4 | Needed | |
| E-12 | Radiation spike aurora glow | Data + overlay | 3 | Spec'd | SME to confirm (keyframes §3.7) |
| E-13 | Frost overlays (light, medium, heavy) | 16×16 ×3 | 3 | Spec'd | Keyframes §3.2 |
| E-14 | Dome glass frost rim variants | 16×16 ×5 | 3 | Spec'd | |
| E-15 | Heat shimmer | Shader or particle fallback 8×8 | 4 | Blocked | TL: shader cost on school hardware; fallback spec'd |
| E-16 | Vapor wisp particle | 8×8, 4 frames | 3 | Spec'd | Phase 1 sublimation and later water |
| E-17 | Water color keyframes | Data | 4 | Spec'd | Keyframes §3.3 |
| E-18 | Morning ground fog | Particle 16×16 | 4 | Needed | |
| E-19 | Spore / pollen particle | 8×8 | 4 | Spec'd | |
| E-20 | Bubble particle (algae, water plants) | 8×8 | 4 | Spec'd | O2 cue |
| E-21 | Smoke puff (fire fails, no O2) | 8×8 | 3 | Spec'd | |
| E-22 | Flame variants (struggling, small, normal, large) + sparks | 16×16 animated ×4, 8×8 | 4 | Spec'd | Own art, not vanilla fire; thresholds SME |
| E-23 | Beacon signal rings | Rendered effect | 4 | Spec'd | Style guide §10 |

## 2. Terrain blocks

| ID | Asset | Kind / size | Stage | Status | Notes |
|---|---|---|---|---|---|
| T-01 | Regolith (3 variants) | 16×16 | **2** | Spec'd | Ramp in style guide §2.2 |
| T-02 | Dust layer (layered like snow, 8 heights) | 16×16 | 3 | Spec'd | Shows tracks |
| T-03 | Basalt sand | 16×16 | 3 | Spec'd | |
| T-04 | Grey rock / drilled rock | 16×16 | 3 | Spec'd | Safe to build on (ground collapse) |
| T-05 | Water ice + 3 shrink stages | 16×16 ×4 | 3 | Spec'd | Sublimation steps |
| T-06 | Dusty layered ice | 16×16 | 3 | Spec'd | Polar caps, crater ice |
| T-07 | Ice with sleeper cyst | 16×16 | 4 | Spec'd | Fiction trace (thawling dormant) |
| T-08 | CO2 frost (polar) | 16×16 | 4 | Needed | Polar frost sink (DESIGN.md hazards) |
| T-09 | Salt crust | 16×16 | 4 | Spec'd | Stylized perchlorate cue |
| T-10 | Cracked regolith (collapse warning) | 16×16 | 4 | Spec'd | Keyframes §3.8 |
| T-11 | Wet regolith | 16×16 | 4 | Spec'd | |
| T-12 | Treated soil | 16×16 | 4 | Spec'd | Cross-hatch tilled look |
| T-13 | Biocrust | 16×16 | 4 | Spec'd | |
| T-14 | Moss block | 16×16 | 4 | Spec'd | |
| T-15 | Mars grass (top, side) | 16×16 ×2 | 4 | Spec'd | |
| T-16 | Mars water (still, flowing) | 16×16 animated | 4 | Spec'd | Color from keyframes |
| T-17 | Puddle, boiling puddle, frozen puddle | 16×16 layers | 4 | Spec'd | Ice-to-water rule (keyframes §3.3) |
| T-18 | Carbonate deposit | 16×16 | 4 | Spec'd | Scrubber output |
| T-19 | Ice-lens survey outline | Overlay | 4 | Spec'd | Keyframes §3.8 |

## 3. Flora

| ID | Asset | Kind / size | Stage | Status | Notes |
|---|---|---|---|---|---|
| F-01 | Pioneer biocrust patch | 16×16 | 4 | Spec'd | |
| F-02 | Moss tufts | 16×16 cross | 4 | Spec'd | |
| F-03 | Pioneer grass | 16×16 cross | 4 | Spec'd | |
| F-04 | Shrub | Model 16×16 | 4 | Needed | |
| F-05 | Engineered tree: sapling, log, leaves | 16×16 ×4 | 4 | Needed | Original species design needed |
| F-06 | Water plant / algae mat | 16×16 | 4 | Needed | Bubble emitter |
| F-07 | Biome seed kits (one per biome type) | 16×16 items | 4 | Blocked | Number of biome types not designed yet |
| F-08 | Dome greenhouse crops | 16×16 stages | 3 | Needed | Phase 1 food; crop list not designed |

## 4. Machines and building blocks

Every machine: front face with status lamp (style guide §3.1) in states **off, running, worn, failed**; plus a **scraped** overlay (fauna) for machines thawlings target.

| ID | Asset | Kind / size | Stage | Status | Notes |
|---|---|---|---|---|---|
| M-01 | Dome frame | 16×16 | 3 | Spec'd | Alloy ramp |
| M-02 | Dome glass (+ frost states) | 16×16 translucent | 3 | Spec'd | |
| M-03 | Airlock door (closed, cycling, open) | Model | 3 | Needed | |
| M-04 | Dust-off station | Model | 3 | Needed | Perchlorate Phase 1 |
| M-05 | O2 generator | Model | 3 | Needed | |
| M-06 | Water recycler | Model | 3 | Needed | |
| M-07 | Regolith Oven (in starting dome) | Model | 3 | Needed | Balance sheet §2 |
| M-08 | **Regolith Outgasser** + vapor plume | Model + particles | **3** | Spec'd | Keyframes §3.8; first planet lever. A placeholder is needed for Stage 2 (prototype "one machine raises pressure") |
| M-09 | Solar panel (clean, dusty) | 16×16 ×2 | 3 | Spec'd | |
| M-10 | Battery | Model | 3 | Needed | |
| M-11 | LED lamp | 16×16 | 3 | Spec'd | Lamp color `#F5E3C3` |
| M-12 | Shielding block, radiation shelter walls | 16×16 | 3 | Spec'd | Regolith-bag look |
| M-13 | Planetary Monitor block | Model + screen | 3 | Spec'd (screen visuals) | Style guide §11; UI layout is LGD's |
| M-14 | Main dish (broken, repaired) | Model | 3 | Spec'd | |
| M-15 | Shift Board block (0–6 slips) | 16×16 ×7 | 4 | Spec'd | Style guide §11.3 |
| M-16 | Atmospheric processor | Model | 4 | Needed | |
| M-17 | Super-greenhouse emitter | Model | 4 | Spec'd (look) | Shimmer and vent glow; no colored plume |
| M-18 | Deep-crust water extractor | Model | 4 | Needed | |
| M-19 | Transit tubes (straight, corner, junction) | Models | 4 | Needed | |
| M-20 | Outpost shell blocks | 16×16 | 4 | Needed | |
| M-21 | Meltwater treatment unit | Model | 4 | Needed | Perchlorate Phase 2 |
| M-22 | Soil treatment block | Model | 4 | Needed | Perchlorate Phase 3 |
| M-23 | Algae vat | Model + bubbles | 4 | Needed | |
| M-24 | Carbon scrubber | Model | 4 | Needed | Produces T-18 |
| M-25 | **Insulation shroud** (wrapped machine variants) | Overlay / model per machine | 4 | Spec'd | Keyframes §3.8 |
| M-26 | **Lure station** | Model | 4 | Spec'd | |
| M-27 | **Quiet zone marker post** | Model | 4 | Spec'd | Sleeping-thawling pictogram |
| M-28 | Survey scanner (item + scan effect) | 16×16 + overlay | 4 | Spec'd (effect) | |
| M-29 | Building piles (collapse-safe foundation) | Model | 4 | Needed | DESIGN.md: "building on rock or on piles" |
| M-30 | Weather mast (windsock, balloon, thermometer, gas tubes) | Model | 3 | Spec'd | **Proposal**, needs LGD approval |
| M-31 | Orbital mirror controller + sky glint | Model + effect | 4 | Cut-list | |
| M-32 | Ice-comet impact (sky streak, crater) | Effect | 4 | Cut-list | |
| M-33 | Colonization beacon (mast, lamp ring) | Model | 4 | Spec'd | Style guide §10 |
| M-34 | Machine scraped overlay | 16×16 | 4 | Spec'd | |

## 5. Items and the player

| ID | Asset | Kind / size | Stage | Status | Notes |
|---|---|---|---|---|---|
| I-01 | Maintenance parts (filter, seal, gasket, bearing) | 16×16 ×4 | 3 | Needed | Final list from LGD |
| I-02 | Scrap | 16×16 | 3 | Needed | |
| I-03 | O2 canister, water canister | 16×16 ×2 | 3 | Needed | |
| I-04 | Ice chunk, regolith sample | 16×16 ×2 | 3 | Needed | |
| I-05 | Sealed sample canister (Basu) | 16×16 | 4 | Needed | |
| I-06 | Notes (taped note, handover note, field notes, Fridge B note) | 16×16 ×4 | 3 | Spec'd | Text in tooltip/journal, not texture |
| I-07 | Beacon components | 16×16 | 4 | Blocked | Component list not designed |
| P-01 | EVA suit tier 1 (4 items + 2 armor layers) | 16×16, 64×32 | 3 | Needed | |
| P-02 | EVA suit tier 2 | same | 3 | Needed | Visibly sturdier silhouette than tier 1 |
| P-03 | Visor overlay + dust film | Screen overlay | 3 | Spec'd | Style guide §9.1 |
| P-04 | HUD icons: O2, suit integrity, radiation dose | 9×9 + 16×16 | 3 | Needed | Shape-distinct |

## 6. Fauna (thawlings, fiction, option B)

| ID | Asset | Kind / size | Stage | Status | Notes |
|---|---|---|---|---|---|
| A-01 | Thawling juvenile ("stirring") | Model + 64×32 or 64×64 | 4 | Spec'd | Style guide §6 |
| A-02 | Thawling **later life stage** (adult) | Model + 64×64 | 4 | Spec'd | Animal, not monster; never named "mutant" |
| A-03 | Animations: walk, sniff, bask (frills open), scrape, curl, roll-flee, rear-shove, graze, nest, groom, sleep | Animation set | 4 | Spec'd | Library is TL's call |
| A-04 | Nest block | 16×16 | 4 | Spec'd | |
| A-05 | Tracks decal | 16×16 | 4 | Spec'd | |
| A-06 | Death puff (salt crust) | Particle 8×8 | 4 | Spec'd | No gore |
| A-07 | Concept sheet (turnaround, stage line-up, scale vs. player) | Image | 4 | Needed | Before modeling; for LGD and SME review |

## 7. Story props and structures

| ID | Asset | Kind / size | Stage | Status | Notes |
|---|---|---|---|---|---|
| S-01 | Tally wall (5 variants) | 16×16 | 3 | Spec'd | |
| S-02 | Dead tomato plant pot (+ living variant) | Model | 3 | Spec'd | |
| S-03 | Lab Fridge B (locked, unlocked, open, jars) | Model | 3 | Spec'd | Unlocks at Thaw |
| S-04 | Drill site: stake + flag, collar, canister | Models | 4 | Spec'd | |
| S-05 | Supply crates + label slips | 16×16 | 3 | Spec'd | |
| S-06 | Bunks (5) | Model | 3 | Spec'd | Dome sized for five |
| S-07 | Cairn Dome starting structure | Structure (NBT) | 3 | Needed | Layout is LGD's; art dresses it |
| S-08 | Outpost structures | Structure | 4 | Needed | |
| S-09 | Cargo lander remains | Structure | 4 | Needed | Optional trace |

## 8. UI and story presentation

| ID | Asset | Kind / size | Stage | Status | Notes |
|---|---|---|---|---|---|
| U-01 | Index parameter icons ×6 (gauge, thermometer, drop, O2 ring, CO2 molecule, leaf) | 9×9 + 16×16 | **2** | Spec'd (shapes) | Shape-distinct |
| U-02 | Monitor screen: pressure gauge with milestones | GUI | 3 | Spec'd | Style guide §11.1 |
| U-03 | Monitor: **Air window** (air checks, zoomed O2 gauge, HS chart) | GUI | 4 | Spec'd | Style guide §11.2 |
| U-04 | Monitor: fauna page, carbon flow view, cause breakdown | GUI | 4 | Needed | Waits on LGD UI spec |
| U-05 | Colony log panel, toast, recap | GUI | 3 | Spec'd | Style guide §7.1 |
| U-06 | Failure report card + Hana's slip | GUI | 3 | Spec'd | |
| U-07 | Recorded message card + 6 sigils | GUI + icons | 3 | Spec'd | Style guide §7.2 |
| U-08 | Message archive | GUI | 3 | Spec'd | |
| U-09 | Journal (field notebook), tabs | GUI | 3 | Spec'd | |
| U-10 | Real / Simplified / Fiction badges | 16×16 ×3 | 3 | Spec'd | Style guide §7.4 |
| U-11 | Keeper's Record (seals) | GUI | 3 | Spec'd | |
| U-12 | Forecast prompt ("Log your forecast") | GUI | 3 | Needed | |
| U-13 | Keeper's Report (debrief) | GUI | 3 | Needed | |
| U-14 | Shift Board screen | GUI | 4 | Spec'd | Style guide §11.3 |
| U-15 | Severity icons (info, gate, warning, failure) | 9×9 | 3 | Spec'd | |
| U-16 | Machine status lamp shapes | 2×2 / 3×3 px set | 3 | Spec'd | |
| U-17 | Teacher tools screens (class overview, pause, set phase, export) | GUI | 4 | Needed | Teacher-facing language allowed |
| U-18 | Opening card ("This story is fiction…") | GUI | 3 | Needed | |
| U-19 | Epilogue / ending cards (fauna ×3, ground, per-Keeper post, numbers) | 192×108 ×6+ | 4 | Spec'd | Unranked; style guide §11.4 |
| U-20 | Title logo, pack icon | 256×64, 128×128 | 5 | Needed | Names not yet cleared (story bible §9) |
| U-21 | Palette reference sheet | PNG | 1 | **Placeholder** | `docs/art/palettes/palette-sheet-v1-placeholder.png` |

## 9. Audio (direction only; no owner yet)

The team has no audio role. Art gives direction (style guide §9.2–9.3, §6.4); the **Producer** needs to assign production. Every sound needs a subtitle string.

| ID | Sound | Stage | Status |
|---|---|---|---|
| AU-01 | Exterior ambience with pressure-based muffling | 2–3 | Spec'd (direction) |
| AU-02 | Suit fan, breathing, seal release (helmet-off) | 3 | Spec'd (direction) |
| AU-03 | Storm, dust hiss (no howling gale) | 3 | Needed |
| AU-04 | Radiation dosimeter clicks | 3 | Needed |
| AU-05 | Machine loops ×N, failure and repair | 3–4 | Needed |
| AU-06 | Fizz (fire fails), flame, fire spread | 3–4 | Needed |
| AU-07 | Ice crack, sublimation hiss, drip, water, rain | 3–4 | Needed |
| AU-08 | Ground creak and collapse | 4 | Needed |
| AU-09 | Thawling clicks, chirrs, basking hum, defensive hiss | 4 | Spec'd (direction) |
| AU-10 | Flora rustle | 4 | Needed |
| AU-11 | Message received, log entry, gate reached | 3 | Needed |
| AU-12 | Beacon lamps and signal pulse | 4 | Needed |

## Counts

| Group | Items | Spec'd | Needed | Blocked | Cut-list | Placeholder |
|---|---|---|---|---|---|---|
| Environment | 23 | 17 | 4 | 2 | 0 | 0 |
| Terrain | 19 | 18 | 1 | 0 | 0 | 0 |
| Flora | 8 | 3 | 4 | 1 | 0 | 0 |
| Machines | 34 | 17 | 15 | 0 | 2 | 0 |
| Items and player | 11 | 2 | 8 | 1 | 0 | 0 |
| Fauna | 7 | 6 | 1 | 0 | 0 | 0 |
| Props and structures | 9 | 6 | 3 | 0 | 0 | 0 |
| UI | 21 | 14 | 6 | 0 | 0 | 1 |
| Audio (direction) | 12 | 3 | 9 | 0 | 0 | 0 |

(Environment Blocked = E-10, E-15. Counts are by row; several rows are sets of textures.)

## Stage 2 prototype minimum
E-01, E-02, T-01, M-08 (placeholder outgasser or processor), U-01. Everything else can wait. Placeholders for these can be generated by script and marked with the magenta corner.
