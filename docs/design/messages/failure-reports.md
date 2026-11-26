# Messages: Failure Reports and Banned Hint Tokens

**Owner:** Lead Game Designer · **Status:** draft: SME check and ID review pending · Rules: content rule **CR-H2** ([docs/learning/content-rules.md](../../learning/content-rules.md)): a hint never states the answer. Conventions: [README.md](README.md)

**Wording rules** (DESIGN.md, "Feedback wording: positive and fixable"): name the system, never the player; give the likely cause (the Monitor's largest cause, CR-H3); hint as a question or as "what holds"; close the loop with one positive line when the player's fix holds.

**Banned tokens.** A hint (levels 1–3) may not contain any token in the type's banned list: these name the exact block, recipe, setting, or place that fixes it. Matching is case-insensitive on the language-file text for every band, after argument substitution. The failure **title** and **cause** lines may name machines (that's the diagnosis); only the **hint** lines are checked. An automated test fails the build if a hint contains a banned token (PROCESS.md §3, content rules in tests).

| Failure type (`failure.<type>`) | Goals | Hint ladder (level 1 → 3; level 3 only after a repeat of the same cause) | Banned tokens in hints | Fixed (closing line) |
|---|---|---|---|---|
| `seal_breach` | C2, A1 | 1: Which blocks touch the outside? · 2: Cold finds the corners first. Check every outside edge. · 3: Seals hold where every outside edge is covered. Walk the wall | seal kit, sealant, place, glass block, airtight block, coordinates (x=, y=, z=), "patch at" | Hab {zone} holding. Seal good. |
| `filter_clog` | C1, C2 | 1: What comes in with you from outside? · 2: Dust builds up faster after trips outside. · 3: Filters last longer when dust stays out of the dome | filter cartridge, replace, dust-off, station, every N sols | Air filters clear. |
| `power_shortfall` | C1 | 1: Which machines run at night? · 2: Storms and nights cut solar. What carries the dome till morning? · 3: Life support first. What can wait? | battery, capacitor, solar panel, turn off, switch off, {machine name} off | Power holding through the night. |
| `machine_wear` (bearing, heater coil) | C2 | 1: When was this machine last serviced? · 2: Moving parts wear first. · 3: Hana's notes on this machine list its parts | bearing, heater coil, replace, part name, recipe | {machine} running smooth. |
| `perchlorate_exposure` | C1, C2, A1 | 1: What did you bring in on your suit? · 2: The salt in Mars dirt is the problem. Where does it get in? · 3: The airlock is where outside meets inside | dust-off, station, wash, airlock station, perchlorate filter | Exposure clearing. |
| `meltwater_contaminated` | C2, A1 | 1: What did the water pick up on the way? · 2: Mars dirt has salt in it, and salt dissolves. · 3: Water from the ground needs cleaning before the dome can use it | treatment, treat, filter, purifier, ion | Water clean. |
| `ground_collapse` | B5, C2 | 1: What was under this machine? · 2: Warm machines melt the ground below them. · 3: The survey scan shows what's underground | survey, scan, rock, piles, stilts, move, rebuild on | Outpost on firm ground. |
| `fauna_damage` | B2, B5 | 1: What draws them to this machine? · 2: Check the fauna page: which machine pulls hardest? · 3: Some answers lower the pull; some don't | shroud, lure, quiet zone, kill, fight, weapon, turret | Raids easing at {machine}. |
| `o2_overshoot_fire` | A5, B4 | 1: What's adding O2 fastest? · 2: Check the Air window: which edge did you cross? · 3: Fast-growing O2 sources need something to slow them | prune, cut, shut down, turn off, algae vat off, add CO2, scrubber | O2 back in the window. |
| `emitter_wear` | C2, B4 | 1: Which value started falling first? · 2: The temperature causes on the Monitor name it. · 3: Warming needs upkeep every sol | repair, replace, heater coil, part name | Emitter {n} back online. |
| `hold_lost` (hold counter reset) | B4, C3 | 1: Which value left the band? · 2: The hold pauses before it resets. How long was it out? · 3: Watch the time-to-edge line on the Air window | (any specific action verb + machine name) | Hold running again. |

**Format per entry** (shared §5.1 fields, filled for every type in the datapack): `id` `failure.<type>.<level>`; `type` failure report; `trigger` first event of the type (level 1), repeat of the same cause (levels 2–3, adaptive feedback); `delivery` BELL top line plus a Shift Board slip; Hana's taped tip attached on the first occurrence only; `hint_level` 1–3; `science_claims` per type (perchlorate, ground ice, fire: SME round 2 §1.4, §1.5, §6 accurate); `status` draft.

**Text budget:** title + cause + hint ≤ 35 words (story bible §6.1).
