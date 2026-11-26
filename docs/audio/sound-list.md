# Sound List: Mars Terraforming

**Owner:** Lead Artist (audio) · **Status:** Draft 1 (2026-11-26) · **Stability:** fluid; updated whenever DESIGN.md, the story bible, or the asset list adds content.
**Direction:** [audio-direction.md](audio-direction.md) (section numbers below, "§", point there unless stated). **Replaces** the audio rows AU-01 to AU-12 in [asset-list.md](../art/asset-list.md) §9 (mapping at the end).

**SME round 2 applied** (flame bands, boiling-point puddles, surface radiation, sound claim accurate). Machine names follow DESIGN.md's in-house machine list.

**Original sounds only.** Every sound here is made by the team (recorded or synthesized). No sample libraries, nothing copied or resampled from vanilla Minecraft, other mods, or other games (§1.5). Vanilla actions keep their vanilla sounds and are not listed.

## Status key

| Status | Meaning |
|---|---|
| **Spec'd** | Direction, driving input, subtitle, and visual twin are written. Ready to make |
| **Needed** | Required by the design; direction is thin or depends on a design detail |
| **Blocked** | Waits on a decision (named in Notes) |
| **Placeholder** | A synthesized placeholder file exists (`_placeholder` in the name) |
| **Final** | Shipped sound |
| **Cut-list** | Depends on content that is first on the cut list, or optional scope |

**Columns.** *Event*: sound event name, namespace `mars:` omitted. *Kind*: loop or one-shot, number of variants, path (§2: Ext = exterior airborne, passes the pressure filter; Con = contact; Suit; Int = interior; UI = interface, non-positional). *Driving input*: what sets whether and how it plays (names match [planetary keyframes](../art/planetary-keyframes.md) §1.2a; the Technical Lead maps them to data keys). *Subtitle*: en_us text for `subtitles.mars.<event>`. *Visual twin*: what shows the same information without sound (rule 5). *Goals / purpose*: goal IDs, or the motivational purpose if none. **Stage**: ROADMAP stage that first needs it (2 prototype, 3 vertical slice, 4 production, 5 polish).

**Subtitles for loops:** a continuous bed (wind, hum, fan) shows its subtitle when it starts and when it changes band or state, not constantly (TL to confirm vanilla behavior for looping sounds).

**Current state: nothing is Final and no file exists.** `pack/assets/` is not set up yet (Technical Lead).

---

## 1. Ambience (AU-A)

| ID | Sound | Event | Kind | Driving input | Subtitle | Visual twin | Goals / purpose | Stage | Status |
|---|---|---|---|---|---|---|---|---|---|
| AU-A01 | Exterior wind, thin (< 5 kPa), pre-filtered 1.5 kHz | `ambient.exterior.wind_thin` | Loop, stereo bed, Ext | **Pressure** (log10), band < 5 kPa | Faint, thin wind | Sky color, fog distance, windsock hanging | A1, B1 | **2** | Spec'd (§3.2), SME ✓ |
| AU-A02 | Exterior wind, mid (5–30 kPa), pre-filtered 5 kHz | `ambient.exterior.wind_mid` | Loop, stereo bed, Ext | Pressure, band 5–30 kPa | Muffled wind | Same, rose to lavender sky, windsock rising | A1, B1 | 3 | Spec'd, SME ✓ |
| AU-A03 | Exterior wind, full (≥ 30 kPa), unfiltered | `ambient.exterior.wind_full` | Loop, stereo bed, Ext | Pressure, band ≥ 30 kPa | Wind | Blue sky, windsock near level | A1, B1 | 4 | Spec'd, SME ✓ |
| AU-A14 | Exterior wind, full, heard through the helmet (pre-filtered 6 kHz) | `ambient.exterior.wind_full_helmet` | Loop, stereo bed, Ext | Pressure ≥ 30 kPa **and** helmet on; crossfades to AU-A03 at helmet-off | Wind, through helmet | Visor frame on screen | A5, A1 (helmet-off moment) | 4 | Spec'd (§3.5, §4) |
| AU-A04 | Dome hum (wavering, steady) | `interior.dome_hum` | Loop ×2 states, Int | **Life support state** (Dome stable gate) | Dome hum wavers / Dome systems hum | Lamp flicker vs. steady lamps; status lamps | C1, C2 | 3 | Spec'd (§8.1) |
| AU-A05 | Lamp buzz (before Dome stable) | `interior.lamp_buzz` | Loop, Int | Life support state | Lamps buzz | Lamp flicker | C2 | 3 | Spec'd |
| AU-A06 | Ice cracks near warm machines | `terrain.ice_crack` | One-shot ×4, Ext | **Local temperature** (heat map) + global temperature | Ice cracks | Frost-free ring, ice block states | A2, B1 | 4 | Spec'd (§3.6) |
| AU-A07 | Water drips | `water.drip` | One-shot ×3, Ext | **Hydrosphere** (%) + nearby water | Water drips | Puddles | A2 | 4 | Spec'd |
| AU-A08 | Water lapping | `water.lap` | Loop, Ext | Hydrosphere + pool blocks | Water laps | Pools, water color | A2 | 4 | Spec'd |
| AU-A09 | Rain | `weather.rain` | Loop, stereo bed, Ext | Hydrosphere (rain events ≥ 20 %) | Rain falls | Rain particles, clouds | A2, B2 | 4 | Spec'd |
| AU-A10 | Leaf and grass rustle | `ambient.rustle` | One-shot ×4, Ext | **Biomass** (BU) | Leaves rustle | Flora, spore particles | A5 | 4 | Spec'd |
| AU-A11 | Windsock flutter (weather mast) | `machine.weather_mast.flutter` | Loop, Ext | Pressure (level 0 → 1.0) | Windsock flutters | Windsock angle (keyframes §3.9) | A1 | 3 | Spec'd |
| AU-A12 | Balloon tether creak (weather mast) | `machine.weather_mast.creak` | One-shot ×2, Ext | Pressure (balloon up from 5 kPa) | Tether creaks | Balloon height | Mood; supports A1 | 4 | Spec'd |
| AU-A13 | Distant thawling calls at dusk | `fauna.thawling.distant` | One-shot ×3, Ext | **Fauna state** = settled, near quiet zones | Distant thawling calls | Nests, grazing thawlings | D1 | 4 | Spec'd (§8.5) |

## 2. Weather, hazards, terrain (AU-W)

| ID | Sound | Event | Kind | Driving input | Subtitle | Visual twin | Goals / purpose | Stage | Status |
|---|---|---|---|---|---|---|---|---|---|
| AU-W01 | Storm grain patter on visor and suit | `weather.storm.patter` | Loop, Con | **Storm strength** (0–1) | Dust patters on visor | Visor dust film, dust particles | C1 | 3 | Spec'd (§6.1) |
| AU-W02 | Storm airborne hiss | `weather.storm.hiss` | Loop, Ext | Storm strength × pressure filter | Dust storm hisses | Storm fog and sky | C1, B2 | 3 | Spec'd |
| AU-W03 | Storm on dome glass | `weather.storm.glass` | Loop, Int (from outside) | Storm strength | Dust taps on glass | Fog through glass | C1 | 3 | Spec'd |
| AU-W04 | Power drop | `interior.power_drop` | One-shot, Int | Solar output below demand | Power drops | Lamp flicker, Monitor power row, dusty panel texture | C1 | 3 | Spec'd |
| AU-W05 | Dosimeter clicks (slow) | `suit.dosimeter.click` | One-shot ×3, Suit, rate-driven | **Dose rate** = `mars:surface_radiation` (already pressure-attenuated) × local shielding | Dosimeter clicking | HUD trefoil pulse + dose number | A4 | 3 | Spec'd (§6.2) |
| AU-W06 | Dosimeter clicks (fast) | `suit.dosimeter.click_fast` | Same files, separate event for subtitle | Dose rate ≥ 0.4 | Dosimeter clicking fast | Same | A4 | 3 | Spec'd |
| AU-W07 | Dosimeter background ticks | `suit.dosimeter.background` | One-shot, Suit | Pressure < 5 kPa, outside, fading to 0 at 30 kPa | Dosimeter ticks | Dose number | A4 | 3 | Spec'd **[SME]** |
| AU-W08 | Ground creak (collapse warning) | `terrain.ground.creak` | One-shot ×3, Con | Ground-ice lens melt state (local) | Ground creaks | `regolith_cracked` texture, drip particles | B5, C2 | 4 | Spec'd (§8.4) |
| AU-W09 | Ground collapse | `terrain.ground.collapse` | One-shot ×2, Con + Ext | Collapse event | Ground collapses | Ground sinks, dust puffs | B5, C2 | 4 | Spec'd |
| AU-W10 | Flash puddle boils away | `water.puddle_boil` | One-shot 2–4 s, Ext | Local temp at or above the boiling point at current pressure (`mars:boiling_point`) | Puddle boils away | Boiling puddle (4 frames), dense wisps | A2 | 3 | Spec'd |
| AU-W11 | Puddle freezes at dusk | `water.puddle_freeze` | One-shot ×2, Ext | Local temperature crossing 0 °C | Puddle freezes | Frozen puddle texture | A2 | 4 | Spec'd |
| AU-W12 | Sublimating ice (rare: below the triple point, about the first sol) | — | **No sound (honest silence)** | — | — | Shrinking ice, vapor wisps | A2 | 3 | Spec'd (none) |
| AU-W13 | Igniter fails to light | `fire.igniter_fail` | One-shot ×2, Con | **O2 fraction** < 14 %, or pressure < 20 kPa (invented guard) | Igniter fails to light | Grey smoke puff, no flame | A5 | 3 | Spec'd (§8.3) |
| AU-W14 | Flame sputters | `fire.sputter` | Loop, Ext | O2 14–17 % | Flame sputters | Half-height flickering flame | A5 | 4 | Spec'd |
| AU-W15 | Fire crackles | `fire.crackle` | Loop, Ext | O2 17–23 % | Fire crackles | Normal flame | A5 | 4 | Spec'd |
| AU-W16 | Fire spreads fast, sparks | `fire.spread` | One-shot ×3, Ext | O2 > 23 % | Fire spreads fast | Large flame, sparks, HUD warning | A5, B4 | 4 | Spec'd |
| AU-W17 | Aurora | — | **No sound (honest silence)** | — | — | Faint glow | A4 | 3 | Spec'd (none) |

## 3. Suit and player (AU-S)

| ID | Sound | Event | Kind | Driving input | Subtitle | Visual twin | Goals / purpose | Stage | Status |
|---|---|---|---|---|---|---|---|---|---|
| AU-S01 | Suit fan bed | `suit.fan` | Loop, Suit | Helmet state | Suit fan hums | Visor frame | Mood: you're in a suit | 3 | Spec'd |
| AU-S02 | Breathing hard (exertion, low O2) | `suit.breath` | Loop ×2 rates, Suit | Sprinting; suit O2 below warning level | Breathing hard | O2 HUD number and icon | C1 | 3 | Spec'd. Unvoiced breath, not gendered |
| AU-S03 | Helmet seal opens | `suit.helmet.seal_open` | One-shot, Suit | Player removes helmet (breathable condition true) | Helmet seal opens | Visor lifts away | A5, A1 | 4 | Spec'd (§4) |
| AU-S04 | First breath | `suit.helmet.first_breath` | One-shot, Suit | First helmet removal per player | You breathe in | Clear view, no HUD frame | A5, A1; motivational peak | 4 | Spec'd. Unvoiced |
| AU-S05 | Helmet seals | `suit.helmet.seal_close` | One-shot, Suit | Helmet put on | Helmet seals | Visor returns | — (state feedback) | 4 | Spec'd |
| AU-S06 | Block sound sets for custom terrain (step, break, place, hit) | `block.<material>.*` | ~12 materials × 4 actions × 3–4 variants, Con | Player action | Vanilla-style ("Footsteps", "Block broken") | The block itself | Mood; schema | 3 | Needed (materials from asset list §2) |
| AU-S07 | Airlock pumps down | `machine.airlock.cycle_out` | One-shot, Int | **Pressure difference** dome vs. Index pressure (length 3 s → short sigh) | Airlock pumps down | Airlock door states, gauge needle | A1 | 3 | Spec'd (§8.2) |
| AU-S08 | Airlock fills | `machine.airlock.cycle_in` | One-shot, Int | Same | Airlock fills | Same | A1 | 3 | Spec'd |

## 4. Alarms (AU-L): life-threatening only (§5)

| ID | Sound | Event | Kind | Driving input | Subtitle | Visual twin | Goals / purpose | Stage | Status |
|---|---|---|---|---|---|---|---|---|---|
| AU-L01 | **Air alarm** (low-high, 3 pulses) | `alarm.air` | One-shot pattern, repeats; UI | Suit O2 nearly out, suit breach, dome breach with player inside, unbreathable with helmet off | AIR ALARM | HUD octagon "AIR" at 2 Hz, O2 number, visor edge + word | Safety; C1, C2 | 3 | Spec'd. Thresholds: Lead Game Designer |
| AU-L02 | **Radiation alarm** (6 fast pulses) | `alarm.radiation` | Same | Harmful dose reached | RADIATION ALARM | Solid trefoil + "DOSE" + number | Safety; A4 | 3 | Spec'd. Threshold: Lead Game Designer |
| AU-L03 | **Fire alarm** (long falling tone) | `alarm.fire` | Same | Fire inside the player's sealed volume | FIRE ALARM | Flame icon + "FIRE", visible flames | Safety; A5, B4 | 4 | Spec'd |

## 5. Feedback and BELL's text-message cues (AU-U)

| ID | Sound | Event | Kind | Driving input | Subtitle | Visual twin | Goals / purpose | Stage | Status |
|---|---|---|---|---|---|---|---|---|---|
| AU-U01 | **Log chime** | `ui.log.chime` | One-shot, UI | Colony log line (Info), Shift Briefing; rate-limited (§7.3) | New colony log entry | Monitor tab flash + badge, block indicator lamp, HUD toast (style guide §13.1) | B1, C3 (salience) | 3 | Spec'd (§7.1) |
| AU-U02 | **Gate chime** | `ui.gate.reached`, `ui.milestone.reached` | One-shot ×1 (two events), UI | Gate reached; Monitor milestone; hold confirmed | Gate reached / Milestone reached | Gate diamond in log, next-gate strip fills, gauge notch fills | A1; flow (clear goals); motivational | 3 | Spec'd |
| AU-U03 | **Warning tone** | `ui.warning` | One-shot, UI | Warning severity (storm/solar coming, drift event, hold paused, low battery, creak near machine) | Warning logged | Triangle icon + "WARNING" | C1, C2, B4 | 3 | Spec'd |
| AU-U04 | **Failure tone** (and −3 dB repeat use) | `ui.failure` | One-shot, UI | Failure report | Failure report | Octagon card, Shift Board slip count | C2 | 3 | Spec'd |
| AU-U05 | **Message cue** (relay blip + printer) | `ui.message.received` | One-shot, UI | Recorded message delivered | Message received | Paper slip card, unread dot (filled shape) | Motivational; carries D1, D2 messages | 3 | Spec'd |
| AU-U06 | Backlog printing | `ui.message.backlog` | One-shot 2 s, UI | Relay restored (beat 6, Dome stable) | Messages printing | Archive bundle, slip stack | Motivational: not alone anymore | 3 | Spec'd |
| AU-U07 | Report sent to Earth | `ui.report.sent` | One-shot, UI | Keeper's Report submitted | Report sent to Earth | Report card stamped "Sent" | B, C, D (debrief) | 3 | Spec'd |
| AU-U08 | **Forecast cue** (neutral for right and wrong) | `ui.forecast.prompt`, `ui.forecast.result` | One-shot, UI | Forecast prompt at natural pause; forecast resolves | Forecast requested / Forecast result | Forecast card, tally in journal | B3 | 3 | Spec'd. Neutral result cue: ID to confirm |
| AU-U09 | Keeper's Record page stamped | `ui.record.stamp` | One-shot, UI | Record page filled from own evidence | Record page stamped | Stamped seal | All goals; motivational | 3 | Spec'd |
| AU-U10 | Hana's tip unpeeled | `ui.tip.tape` | One-shot, UI | Failure report with tip opened | Tape peels | Taped slip under report | C2; motivational (a person helped) | 3 | Spec'd |
| AU-U11 | Monitor switches on | `ui.monitor.open` | One-shot, Int | Monitor opened | Monitor switches on | Screen | C3 | 3 | Spec'd |
| AU-U12 | Page turns | `ui.journal.page` | One-shot ×3, UI | Journal page, epilogue card | Page turns | The page | Mood | 3 | Spec'd |
| AU-U13 | Note pinned / marked fixed | `ui.shiftboard.pin`, `ui.shiftboard.tick` | One-shot, Int | Shift Board claim / fix | Note pinned / Marked fixed | Slip status shapes | C2 (group) | 4 | Spec'd |
| AU-U14 | World paused / resumed | `ui.world.paused`, `ui.world.resumed` | One-shot, UI | Teacher pause | World paused / World resumed | Pause overlay | Classroom mode | 4 | Spec'd |
| AU-U15 | Main dish turns (relay restored) | `machine.dish.turn` | One-shot, Ext | Dish repaired | Dish turns | Dish state swap, relay icon on Monitor | Motivational | 3 | Spec'd |

## 6. Machines (AU-M)

Each "set" = start, running loop, worn, failed (wind-down), repaired, unless noted. Exterior machines pass the pressure filter (§8.2). At most 8 loops audible at once.

| ID | Sound | Event (prefix) | Kind | Driving input | Subtitle (main) | Visual twin | Goals / purpose | Stage | Status |
|---|---|---|---|---|---|---|---|---|---|
| AU-M01 | **Regolith Outgasser** drone (`run_thin` pre-filtered 1.5 kHz, `run_full`) + set | `machine.outgasser.*` | Set, Ext | Machine state; drone variants crossfaded by pressure | Outgasser drones | Status lamp, plume | B1, C3 | 3 | Spec'd (§8.2). **Placeholder wanted for Stage 2** |
| AU-M02 | Outgasser chuff (synced to puffs) | `machine.outgasser.chuff` | One-shot ×4, Ext | **Puff rate and size** (visual; size from temperature) | Outgasser chuffs | Vapor puffs | B1 | 3 | Spec'd |
| AU-M03 | Outgasser contact thump | `machine.outgasser.thump` | One-shot ×2, Con | Player within 3 blocks | Outgasser thumps | Plume, lamp | A1 (audible even in thin air) | 3 | Spec'd |
| AU-M04 | Electrolysis O2 generator set + filter whine | `machine.o2_generator.*` | Set + whine loop, Int | Machine state; **filter wear** (whine pitch) | Filter whines | Worn lamp, Monitor O2 row | C2, C1 | 3 | Spec'd |
| AU-M05 | Water reclaimer set | `machine.water_reclaimer.*` | Set, Int | Machine state | Pump gurgles | Status lamp | C1 | 3 | Spec'd |
| AU-M06 | Regolith Oven set | `machine.regolith_oven.*` | Set, Int | Machine state | Oven ticks | Status lamp | C1 | 3 | Needed |
| AU-M07 | Battery hum | `machine.battery.hum` | Loop, Int | Charging/discharging | Battery hums | Charge bar | Mood | 3 | Needed |
| AU-M08 | Dust-off station jets | `machine.dust_off.blast` | One-shot ×2, Int | Player uses station | Air jets blast dust | Dust particles off suit; exposure icon clears | C1, C2 (perchlorate) | 3 | Spec'd |
| AU-M09 | Atmospheric processor set | `machine.atmo_processor.*` | Set, Ext | Machine state | Processor thumps | Status lamp | B1, B4 | 4 | Needed |
| AU-M10 | Super-greenhouse emitter set (vent hiss, no puff) | `machine.emitter.*` | Set, Ext | Machine state | Emitter vents hiss | Heat shimmer, vent glow | A3 | 4 | Spec'd |
| AU-M11 | Ice extractor (deep-crust water extractor) set (drill thump) | `machine.extractor.*` | Set, Ext | Machine state | Drill thumps | Status lamp | A2 | 4 | Needed |
| AU-M12 | Algae vat bubbling | `machine.algae_vat.*` | Set, rate-driven, Int/Ext | **Vat output** | Algae vat bubbles | Bubble particles | A5 | 4 | Spec'd |
| AU-M13 | Carbon scrubber set + carbonate crumble | `machine.scrubber.*` | Set, Ext | Machine state; scrubber output | Scrubber whirs | Carbonate deposit grows | A5 | 4 | Needed |
| AU-M14 | Meltwater treatment set | `machine.meltwater_treatment.*` | Set, Int | Machine state | Filter pump runs | Status lamp | C2, A2 | 4 | Needed |
| AU-M15 | Soil treatment set | `machine.soil_treatment.*` | Set, Ext | Machine state | Soil churns | Treated soil texture | C2, A5 | 4 | Needed |
| AU-M16 | Shrouded machine (mix rule, no new file) | — | −6 dB, low-pass 2 kHz on the machine's sounds | Shroud on | Same as machine, plus "(muffled)" not needed | Quilted shroud, no shimmer | B4, B2 | 4 | Spec'd |
| AU-M17 | Lure station | `machine.lure_station.run` | Loop, Ext | Machine state | Warm water laps | Steam, clustered thawlings | B4, D1 | 4 | Spec'd |
| AU-M18 | Survey scanner sweep + ice-lens ping | `item.survey_scanner.*` | One-shot ×2, UI | Scan used; lenses found | Scanner sweeps / Ice found below | Ice-lens outline overlay | C2, B5 | 4 | Spec'd |
| AU-M19 | Metal scraping (thawling damage) | `machine.scraped` | One-shot ×3, Ext | Fauna behavior = scrape | Metal scraping | Scrape overlay, worn lamp | B5, B2 | 4 | Spec'd |
| AU-M20 | Transit tube air rush | `machine.transit_tube.*` | Loop, Int | Player in tube | Air rushes | Tube | Mood | 4 | Needed |
| AU-M21 | Orbital mirror controller, sky glint | `machine.orbital_mirror.*` | Set | Machine state | Mirror motors whir | Sky glint | A3 | 4 | Cut-list |
| AU-M22 | Ice-comet impact (distant boom, filtered) | `event.comet_impact` | One-shot, Ext | Event | Distant impact | Sky streak, crater | A2 | 4 | Cut-list |

## 7. Fauna: thawlings (AU-F), fiction, animal not monster (§8.5)

All in the **neutral** category. No roars, growls, screeches, or screams.

| ID | Sound | Event | Kind | Driving input | Subtitle | Visual twin | Goals / purpose | Stage | Status |
|---|---|---|---|---|---|---|---|---|---|
| AU-F01 | Juvenile idle clicks | `fauna.thawling.juvenile.idle` | One-shot ×4, Ext | Fauna state = stirring; every 8–20 s | Thawling clicks | Frills, tracks | D1, B5 | 4 | Spec'd |
| AU-F02 | Juvenile sniff | `fauna.thawling.juvenile.sniff` | One-shot ×3 | Behavior = sniff | Thawling sniffs | Sniff animation | D1 | 4 | Spec'd |
| AU-F03 | Juvenile basking hum | `fauna.thawling.juvenile.bask` | Loop | Behavior = bask near heat | Thawling hums warmly | Frills fanned open toward heat | **B2** (why they come to machines) | 4 | Spec'd |
| AU-F04 | Curl and roll (plates clack) | `fauna.thawling.curl` | One-shot ×2 | Behavior = startled / flee | Thawling curls up | Curl animation | D1 | 4 | Spec'd |
| AU-F05 | Juvenile defensive hiss | `fauna.thawling.juvenile.hiss` | One-shot ×2 | Hit by player | Thawling hisses | Rear-up, frills flared | D1 | 4 | Spec'd |
| AU-F06 | Adult idle clicks (lower, slower) | `fauna.thawling.adult.idle` | One-shot ×4 | Fauna state = later life stage | Large thawling clicks | Adult silhouette | B5, D1 | 4 | Spec'd |
| AU-F07 | Adult sniff | `fauna.thawling.adult.sniff` | One-shot ×3 | Behavior = sniff | Large thawling sniffs | Sniff animation | D1 | 4 | Spec'd |
| AU-F08 | Adult basking hum | `fauna.thawling.adult.bask` | Loop | Behavior = bask | Large thawling hums | Tall frills fanned | B2 | 4 | Spec'd |
| AU-F09 | Adult hiss and shove | `fauna.thawling.adult.hiss` | One-shot ×2 | Hit by player | Large thawling hisses | Rear-up, shove | D1 | 4 | Spec'd |
| AU-F10 | Settled chirr and grooming | `fauna.thawling.settled` | One-shot ×4 | Fauna state = settled | Thawlings chirr | Grazing, grooming, nests | **D1** | 4 | Spec'd |
| AU-F11 | Nest digging | `fauna.thawling.dig` | One-shot ×3 | Behavior = nest | Thawling digs | Nest bowl block | D1 | 4 | Spec'd |
| AU-F12 | Loses interest (shrouded machine) | `fauna.thawling.snuffle` | One-shot ×2 | Sniffed a shrouded machine | Thawling loses interest | Frills flat, wanders off | **B4, B2** | 4 | Spec'd |
| AU-F13 | Death: sigh and salt crumble | `fauna.thawling.death` | One-shot ×2 | Killed (lethal defense) | Thawling dies | Curl and salt-crust puff, no gore | D1 | 4 | Spec'd |

## 8. Ending (AU-E)

| ID | Sound | Event | Kind | Driving input | Subtitle | Visual twin | Goals / purpose | Stage | Status |
|---|---|---|---|---|---|---|---|---|---|
| AU-E01 | Beacon lamp lights (8 rising tones) | `ending.beacon.lamp` | One-shot ×8 pitches, Ext | Ending step: light | Beacon lamp lights | Lamps on one by one | Motivational: earned relief | 4 | Spec'd (§13) |
| AU-E02 | Signal pulse (every 2 s) | `ending.beacon.pulse` | One-shot, Ext | Ending step: rings | Signal pulses | Signal rings | Motivational | 4 | Spec'd |
| AU-E03 | "Signal sent", replies, cards, Record | Reuses AU-U02, AU-U05, AU-U12, AU-U09 | — | Ending steps | (as listed) | (as listed) | D1, D2 | 4 | Spec'd |

## 9. Music and voice (AU-X): decisions pending

| ID | Sound | Kind | Stage | Status | Notes |
|---|---|---|---|---|---|
| AU-X01 | Ending piece (original) | Stereo, 2–3 min | 5 | **Blocked** | User decision on music scope (audio direction §12). Subtitle "Music plays" |
| AU-X02 | Phase pieces ×3 (sparse, original) | Stereo | 5 | **Cut-list** | Same decision |
| AU-X03 | Real Mars recording in the journal (Perseverance) | Journal media | 4 | **Blocked** | Not original: needs user approval and a license check (report). Would be labeled REAL |
| AU-X04 | BELL voice lines | Voice | 5+ | **Blocked** | Revisit after Stage 3 playtest (DESIGN.md Multimedia). Captions always on; feedback cues wait for voice (§1.2 rule 3) |

---

## Counts

| Group | Rows | Spec'd | Needed | Blocked | Cut-list | Placeholder | Final |
|---|---|---|---|---|---|---|---|
| Ambience (A) | 14 | 14 | 0 | 0 | 0 | 0 | 0 |
| Weather, hazards, terrain (W) | 17 | 17 | 0 | 0 | 0 | 0 | 0 |
| Suit and player (S) | 8 | 7 | 1 | 0 | 0 | 0 | 0 |
| Alarms (L) | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| Feedback and UI (U) | 15 | 15 | 0 | 0 | 0 | 0 | 0 |
| Machines (M) | 22 | 12 | 8 | 0 | 2 | 0 | 0 |
| Fauna (F) | 13 | 13 | 0 | 0 | 0 | 0 | 0 |
| Ending (E) | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| Music and voice (X) | 4 | 0 | 0 | 3 | 1 | 0 | 0 |
| **Total** | **99** | **84** | **9** | **3** | **3** | **0** | **0** |

Rows AU-W12 and AU-W17 are deliberate silences (no file). AU-M16 and AU-E03 make no new file. Several rows are sets (variants, machine states).

## Stage 2 prototype minimum
AU-A01 (thin wind with the pressure filter) and AU-M01 (Outgasser drone), as synthesized placeholders, so the prototype can test whether players notice the outside getting louder as pressure rises. Everything else can wait for Stage 3.

## Mapping from the old asset-list audio rows

| Old (asset list §9) | Now |
|---|---|
| AU-01 Exterior ambience with pressure muffling | AU-A01 to AU-A03 |
| AU-02 Suit fan, breathing, seal release | AU-S01 to AU-S05 |
| AU-03 Storm, dust hiss | AU-W01 to AU-W04 |
| AU-04 Dosimeter clicks | AU-W05 to AU-W07 |
| AU-05 Machine loops, failure and repair | AU-M01 to AU-M22 |
| AU-06 Fizz, flame, fire spread | AU-W13 to AU-W16 ("fizz" became igniter clicks: honest) |
| AU-07 Ice crack, sublimation hiss, drip, water, rain | AU-A06 to AU-A09, AU-W10, AU-W11; **sublimation hiss dropped** (AU-W12, honest silence) |
| AU-08 Ground creak and collapse | AU-W08, AU-W09 |
| AU-09 Thawling sounds | AU-F01 to AU-F13, AU-A13 |
| AU-10 Flora rustle | AU-A10 |
| AU-11 Message received, log entry, gate reached | AU-U01 to AU-U15 |
| AU-12 Beacon lamps and signal pulse | AU-E01 to AU-E03 |
