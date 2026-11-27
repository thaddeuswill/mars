# Mars atmosphere: how it looks and sounds (science pass for the atmosphere plan)

**Owner:** Subject Matter Expert · **Date:** 2026-11-27 · **Status:** Draft 1 · **Stability:** fluid
**Backs:** [atmosphere-plan.md](../art/atmosphere-plan.md) (items AT-1 to AT-11), [planetary-keyframes.md](../art/planetary-keyframes.md), [artist sky review](../team/reports/2026-11-27-artist-sky-review.md).
**Reading time:** about 10 minutes. Start with section 1, then the claim tables.

## How to read this

- **Status:** *accurate* = matches the source. *simplification* = true in direction, the game bends the numbers. *wrong* = the plan says something the source contradicts. *not verifiable* = no allowed source I opened says it.
- **VERIFIED** = I opened the page or paper this session and the quoted line is in it. **NOT VERIFIED** = from memory, from my own arithmetic, or only seen in a search summary (I did not open it). Search summaries are never counted as verified.
- **DERIVED** = my arithmetic on a verified number. Check it before it goes to students.
- Sources are only the allowed hosts (NASA science, NTRS, PubMed). Source list at the end.

## 1. Bottom line (what the plan should change)

| # | Finding | Plan item |
|---|---|---|
| 1 | The tan daytime sky, blue sunset near the sun, the 0.66 sun size, thin water-ice clouds and long twilight are all **accurate** and now VERIFIED. | AT-4, AT-10, AT-11 |
| 2 | The **storm sun at 30 % alpha is a soft version**. In the 2018 global storm the sun became a "pinprick" (tau about 11). Fine as a game choice (storms shrink with phase), but label it a simplification. | AT-1 |
| 3 | **Wind streaks every 5 s at 0.6 kPa overstate real sand motion.** Real wind rarely reaches the speed that starts sand moving. Make streaks rarer, in bursts tied to gusts or dust devils. | AT-3 |
| 4 | **"Roar" is not honest at 0.6 kPa.** Rumble plus soft hiss is. Ruling in section 7. | AT-5 |
| 5 | **Dust devils are real and big** (25 m to 65 m wide, over 100 m tall) and harmless to a person. A 3-block devil is a toy, not a lie, but the journal should say real ones are about 10 to 20 times wider. | AT-6 |
| 6 | **Sound depends on CO2 as well as pressure.** The game keys sound only on pressure; that is a simplification, worth one journal line. | AT-5 |
| 7 | Phobos size (about 0.21 degrees) and Deimos as a point are **my arithmetic, not verified**. Orbit radii were not in any source I opened. | AT-4, AT-7 |
| 8 | Shadow fill, stars by pressure, breath fog, sun colour: **not verifiable** with the allowed sources. Do not put them in the journal as fact. | AT-8, AT-7, AT-9b |

## 2. Sky colour, haze, sunset, sun (AT-2, AT-4, AT-11)

| ID | Claim in the plan | Status | Backs | Source and quote |
|---|---|---|---|---|
| V1 | Daytime sky is tan to butterscotch from suspended dust | **accurate** | sky keys 0.6 kPa, AT-2 | VERIFIED. NASA, "10+ Things: Tour of Storms": "To the human eye, the sky would appear hazy and reddish or butterscotch colored because of all the dust suspended in the air." |
| V2 | Sunsets are blue near the sun (PIA19400, Curiosity sol 956) | **accurate** | `sunset_halo` at 0.6 kPa, AT-11 | VERIFIED. NASA "Sunset at Mars' Gale Crater (Curiosity)": "Dust in the Martian atmosphere has fine particles that permit blue light to penetrate the atmosphere more efficiently than longer-wavelength colors. That causes the blue colors ... to stay closer to Sun's part of the sky, compared to the wider scattering of yellow and red colors." Also NASA "What Do Sunrises and Sunsets Look Like on Mars?": "Fine dust makes the blue near the Sun's part of the sky much more prominent." |
| V3 | The cause is dust scattering | **accurate** | journal line | VERIFIED. Same pages. Simple version for students: tiny dust grains send blue light mostly forward, close to the sun's direction; red and yellow light spreads over the whole sky. |
| V4 | The blue is a **sunrise and sunset** effect; most of the sky stays yellow to orange | **accurate** | AT-11 | VERIFIED. "The effect is most pronounced near sunset, when light from the Sun passes through a longer path" (PIA19400 page). Perseverance sunset page: "The rest of the sky is yellow to orange." A midday blue aureole is therefore a **simplification**; keep any midday glow faint (aureole stays deferred). |
| V5 | Blue sunset flips to orange at 75 kPa | **game extrapolation, not observed** | AT-11, `sunset_halo` | NOT VERIFIED. Reason it is plausible: thicker clean air scatters like Earth's (textbook idea). The sources above only describe today's dusty Mars. Write "we expect", never "Mars did". |
| V6 | Far hills wash toward the sky colour (aerial perspective) | **not verifiable** | AT-2 | NOT VERIFIED. Dust haze is real (V1); the exact look of distant hills I did not find in an allowed source. Treat AT-2 as art tuning, not a science claim. |
| V7 | Sun is about 0.66 of Earth's apparent width | **accurate** | `sun_scale` 0.66 | VERIFIED. NASA sunrise page: "the Sun appears only about two-thirds the size we see when we watch sunsets here on Earth." |
| V8 | Sun disc is whitish (`#F4EBDD`) | **not verifiable** | AT-4 | NOT VERIFIED. No allowed page gave the disc colour. Keep it as an art choice; the vanilla yellow rim is the unsafe part, because it adds a colour claim nobody sourced. |
| V9 | Dust scatters light into shadows, so shadows are soft | **not verifiable** | AT-8 | NOT VERIFIED. Plausible, not in anything I opened. Vanilla has no cast shadows anyway. Do not teach it. |
| V10 | Horizon glow lasts long after sunset | **accurate** | AT-11 | See T1 in section 5. |

## 3. Storms and dust (AT-1, AT-3, AT-6)

| ID | Claim | Status | Backs | Source and quote |
|---|---|---|---|---|
| S1 | Dust storms are common; local ones often, regional sometimes, global rarely | **accurate** | storm cadence | VERIFIED. NASA "10 Things: Dust in the Wind": "Local dust storms occur frequently on Mars, and occasionally grow or merge to form regional systems ... A few of these events may become truly global storms." |
| S2 | A big storm dims the sky and shrinks the sun | **accurate (direction)** | AT-1 dim, sun fade | VERIFIED. NASA "Shades of Martian Darkness": "a darkening Martian sky blotting out the Sun ... The right shows the Sun so obscured by dust it looks like a pinprick. Each frame corresponds to a tau value ... 1, 3, 5, 7, 9, 11." (tau = opacity; the frames run up to 11, simulating Opportunity's June 2018 view, so almost no direct light.) |
| S3 | Game sun alpha 0.30 at full storm, dim 18 %, fog end 0.18 | **simplification** | AT-1 | The real global-storm peak is far darker than the game's. We soften it because the cap falls with phase and the player must still see to work. Journal note: "Real storms can hide the Sun almost completely." |
| S4 | Storm sky colour `#9A6A48`, fog `#A8744E` | **not verifiable** | AT-1 | NOT VERIFIED. I have no rover pixel samples for storm colour. Art choice. |
| S5 | Storms are more a **dust** event than a **wind** event | **accurate** | AT-1, "no violent wind" rule | VERIFIED (part). Perseverance, Jezero: "99% of wind speeds were below 9.95 ms-1" and "95% ... below 7.46 ms-1" (Guzewich et al., NTRS 20230001086). InSight, Elysium: 99% below 15.7 m/s. Even a Viking-era local storm was reported to need a "threshold wind speed for saltation ... 25-30 m/sec" (NTRS 19810060076, abstract). The DESIGN rule against flying debris stands. |
| S6 | Storm warning haze before onset | **not verifiable** | AT-1 haze | NOT VERIFIED. I found no source on pre-storm haze. It is a design cue (F1 rule from the Lead Instructional Designer), not a claim about Mars. Do not word it as "Mars storms are preceded by haze". |
| S7 | Storms shrink as pressure rises (caps 1.0, 0.8, 0.5, 0.25) | **game rule** | AT-1 | Not observed; our design promise. Say "in this game" in the journal. |
| S8 | Dust devils are real and common | **accurate** | AT-6 | VERIFIED. Murdoch et al. 2022 (PubMed 36513637): "Dust devils (convective vortices loaded with dust) are common at the surface of Mars." |
| S9 | Size: tens of metres wide, over 100 m tall | **accurate** | AT-6 size | VERIFIED. Same paper: "around 25 m large, at least 118 m tall ... travelling at approximately 5 m s-1." NASA Mars Report: the big one "is 210 feet" wide (about 65 m); MRO imaged one "12 miles high" (about 19 km). |
| S10 | A dust devil is harmless to a person | **accurate** | AT-6 | VERIFIED. NASA Mars Report: "The Martian atmosphere is so thin that it would feel like a gust of wind, though you'd get pretty dirty." |
| S11 | Dust devils leave dark tracks readable from orbit | **accurate** | AT-6 (optional ground tint) | VERIFIED. NASA Mars Report: "We've also captured the tracks these dust devils leave behind. These can tell us about the directions dust devils traveled." Also NTRS 20070032968 (MGS images of 65-72 N): "Dust devil tracks (DDT) and wind streaks (WS) were observed ... as surface evidence for winds." |
| S12 | How often a given spot sees one | **not verifiable** | AT-6 rate | NOT VERIFIED. A search summary quoted "around 1-4 vortex encounters per day" (Lorenz 2012) but I did not open it. Use "common" and pick the game rate by feel. |
| S13 | Game devil width (small, a few blocks) | **simplification** | AT-6 | Real widths (25 to 65 m) are 25 to 65 blocks at 1 block = 1 m. A smaller game devil is fine; do not claim it is true size. |

## 4. Wind, sand and dust motes (AT-3)

| ID | Claim | Status | Backs | Source and quote |
|---|---|---|---|---|
| W1 | Thin air needs **faster** wind to move sand | **accurate** | AT-3 rate direction | VERIFIED. NASA-Ames wind-tunnel paper (NTRS 19900005681): "to a first order approximation, the same dynamic pressure was required at Martian pressure to entrain or saltate particles as was required on Earth, although wind and particle speed are considerably higher at Martian pressure." |
| W2 | By how much | **DERIVED, not verified** | AT-3 numbers | Dynamic pressure is half of density times speed squared. Mars surface density is "~0.016 +/- 0.006 kg/m3" (NSSDC fact sheet, VERIFIED); Earth sea level is about 1.2 kg/m3 (my knowledge, NOT VERIFIED). Same push means speed about 9 times higher on Mars. A 20 m/s Mars wind pushes about like a 2 m/s breeze on Earth. Use this as a journal sentence only after the Earth density is sourced. |
| W3 | Real winds rarely reach the sand-start speed | **accurate** | AT-3 frequency | VERIFIED (numbers in S5). Once sand is moving it needs less: Guzewich et al.: "On Mars, impact threshold speeds are only about 10% of the fluid threshold (Kok, 2010; Bridges et al., 2012)." Sand motion starts rarely, in gusts, then keeps going. |
| W4 | Very fine grains behave differently at very low pressure | **accurate (caution)** | AT-3 | VERIFIED. NTRS 19770028156: "the assumption of universality of the A-B curve is in error at very low pressures for small particles." So W1 is a first-order rule, not exact. |
| W5 | Wind-blown grains were heard and recorded on Mars | **accurate** | AT-3 | VERIFIED (via a citing paper). Smith et al. (NTRS 20250010704): "The microphone has also recorded winds and saltation due to a passing dust devil (Murdoch et al., 2022)." |
| W6 | Plan rate: 1 streak per 5 s at 0.6 kPa | **simplification, too busy** | AT-3 | By W3 sand motion should be rare and bursty at 0.6 kPa. Recommend: streaks come in clusters of 2 to 4 over a few seconds, with long quiet gaps (tens of seconds), and more during a storm and near a dust devil. This keeps the "alive" feel and the right physics. |
| W7 | Dust motes (suspended dust) 4 per second, drifting slowly | **simplification** | AT-3 | Suspended dust is real (V1). Its drift speed is not measured here. Game value; label it so. |
| W8 | Wet ground does not blow | **not verifiable** | AT-3 hydrosphere term | NOT VERIFIED here. Common physics (moisture binds grains), not in an opened source. Keep as a game rule. |
| W9 | Wind direction fixed per planet (110 degrees) | **simplification** | AT-3 | Real winds change with time of day. Jezero: "westerly-northwesterly winds" at night and "easterly to southeasterly" by day (Guzewich et al., VERIFIED, NTRS 20230001086). A single direction is fine; the dune tie-in is a game rule. |
| W10 | Typical wind at the surface | **info** | AT-3, AT-5 | VERIFIED. NASA InSight page: seismometer and pressure sensor "picked up vibrations from 10-15 mph (16-24 kph) winds ... nearly all bass." Jezero 95 % below 7.5 m/s (S5). |

## 5. Twilight, clouds, stars, moons (AT-7, AT-10, AT-4)

| ID | Claim | Status | Backs | Source and quote |
|---|---|---|---|---|
| T1 | Twilight is long because high dust scatters light to the night side | **accurate** | AT-11 note, "no change" | VERIFIED. NASA sunrise page: "the twilight glow remains visible, but increasingly fainter, for up to two hours before sunrise or after sunset. The long Martian twilight (compared to Earth's) is caused by sunlight scattered around to the night side of the planet by abundant high altitude dust." |
| T2 | Game keeps vanilla dusk length | **simplification, fine** | plan "skip" | DERIVED: two hours is about 8 % of a 24.66 h sol (NSSDC: length of day 24.6597 h, VERIFIED), so about 1.6 game minutes of a 20 minute sol per side. Vanilla dusk is around that long (vanilla tick count from memory, NOT VERIFIED). So **skipping a custom curve is supported**. Journal: "Mars twilight can last about two hours." |
| T3 | Mars clouds today are thin, mostly water ice | **accurate** | AT-10, vanilla-cloud note | VERIFIED. NASA "Clouds Sailing Overhead on Mars": "Martian clouds are likely composed of crystals of water ice that condense onto dust grains in the cold Martian atmosphere. Cirrus wisps appear as ice crystals fall and evaporate in patterns known as 'fall streaks'". "Likely" is NASA's word; keep it. |
| T4 | Some high clouds are CO2 ice | **accurate** | AT-10 nuance | VERIFIED. NASA "Curiosity Views Twilight Clouds": "The clouds at the top of the frame ... are made of carbon dioxide ice. The plumes are an estimated 37 to 50 miles (60 to 80 kilometers) above the Martian surface". |
| T5 | Thin high clouds can glow after sunset | **accurate** | AT-10 | VERIFIED. NASA "Three-Frame Mosaic of Clouds": "likely water-ice clouds about 19 miles (31 kilometers) above the surface ... 'noctilucent' ... still illuminated by the Sun, even when it's night at Mars' surface." |
| T6 | Vanilla white blocky clouds at 0 hydrosphere | **simplification (visual only)** | clouds stay until E-10 | Real clouds exist but are thin and wispy, not puffy and white. Journal note until E-10: "The clouds in this game are placeholders; real Mars clouds are thin ice wisps." Do not use the cloud as a water-cycle signal yet. |
| T7 | Stars dense at 0.6 kPa, fewer as air thickens | **game extrapolation, not verifiable** | AT-7 | NOT VERIFIED. I opened nothing on Mars star visibility or on thick-air stars. Keep as art; no journal fact. |
| T8 | Phobos: lumpy, dark | **accurate** | AT-4 | VERIFIED. NASA Phobos page: "27 by 22 by 18 kilometers" (shape not round). NSSDC fact sheet lists geometric albedo "0.07" for Phobos. My column reading of the table (Phobos first) is NOT VERIFIED but fits the mass row. |
| T9 | Phobos crosses the sky about twice a sol, rising in the west | **DERIVED** | AT-4 | NASA: "It orbits Mars three times a day". Phobos is faster than the sky turns, so it rises in the west (my reasoning, from verified facts). "About twice a sol" for the **sky crossings seen from one spot** is my arithmetic, NOT VERIFIED. Plan already says do not claim the motion; agree. |
| T10 | Phobos looks about 0.21 degrees wide, about 0.6 of the sun | **NOT VERIFIED (arithmetic)** | AT-4 | 22 km over about 6,000 km gives 0.21 degrees. The 6,000 km distance is not in a page I opened. Sun check: the sun is about 0.35 degrees from Mars (two-thirds of Earth's 0.53, DERIVED from V7). Ratio about 0.6. Ship only with the label "about". |
| T11 | Phobos "cannot always be seen" | **accurate** | AT-4 | VERIFIED. NASA: "so close to the planet's surface that in some locations on Mars it cannot always be seen." Good honest caveat: not every player latitude sees it. |
| T12 | Deimos looks like a bright star-like point | **NOT VERIFIED** | AT-7 | NASA: "15 by 12 by 11 kilometers", "orbits Mars every 30 hours" (VERIFIED). The angular size and brightness are my arithmetic or memory. Keep as art. |

## 6. Sound (AT-5)

| ID | Claim | Status | Backs | Source and quote |
|---|---|---|---|---|
| A1 | Sound is quieter and muffled | **accurate** | A1 journal line, subtitles | VERIFIED. NASA "Sounds of Mars": "The sound level you'd hear would be automatically lower on Mars." |
| A2 | High pitches die out; low pitches carry | **accurate** | low-pass at 1.5 kHz | VERIFIED. NASA: "The atmosphere of Mars, made up of 96 percent carbon dioxide, would absorb a lot of higher-pitched sounds, so only lower-pitched sounds would travel long distances." Nature paper (Maurice et al. 2022, PubMed 35364602): "high-frequency waves are strongly attenuated with distance in CO2" and the measured attenuation above "2 kHz". The 1.5 kHz game value is an **invented cut-off** that sits just below where the measurement starts; label it so. |
| A3 | Speed of sound is slower | **accurate** | delay not used in game | VERIFIED. NASA: "around 540 mph (~240 meters per second), compared to about 760 mph (~340 meters per second) on Earth." Paper: two speed values "about 10 m s-1 apart below and above 240 Hz". Game does not delay sound; say so if asked. |
| A4 | Whistles and bells are nearly inaudible | **accurate** | "no whistle" rule | VERIFIED. NASA: "Some sounds ... like whistles, bells or bird songs, would almost be inaudible on Mars." Supports the no-howl rule. |
| A5 | The microphone has recorded Martian wind | **accurate** | AU-A01 | VERIFIED. NASA "Sounds of Mars": "This mic has also captured the sounds of Martian wind." InSight: winds "nearly all bass." |
| A6 | Thin-air wind: faint low rumble, no whistle at 0.6 kPa | **accurate** | AU-A01 | Supported by A2, A4, A5, W10. |
| A7 | Contact sound (own footsteps, grains on the visor) is audible | **accurate (reasoned)** | AU-A15, AU-W01 | Partly VERIFIED. Murdoch: "Acoustic signals of grain impacts recorded during the vortex encounter" (dust-devil paper). Sound through the suit and ground, not air: my reasoning, NOT VERIFIED. |
| A8 | Dust devil sound: faint rising hiss | **simplification** | AU-A16 | VERIFIED that it was recorded (title: "The sound of a Martian dust devil", PubMed 36513637). The paper's sound is mostly wind pressure change and grain hits; "rising hiss" is a design reading. Keep it faint. |
| A9 | Sound gets less muffled as pressure rises | **game extrapolation** | AU-A02, A03 | Not observed. The paper says the damping comes from "CO2 vibrational relaxation", so it depends on how much CO2 is in the air, not only on pressure. In this game CO2 is also the oxygen source, so it falls as O2 rises: one more reason the real answer is not just "pressure". The game keys on pressure only: **simplification**. |
| A10 | Wind-sound level tracks wind speed | **accurate** | AT-5 | VERIFIED (via Smith et al. abstract): "the sensitivity of the microphone signal level to wind speed, yielding a power law dependence." |

## 7. Ruling: is a storm "roar" honest? (AT-5, AU-W01 to W03)

**Ruling: not at 0.6 kPa. A deep rumble plus a faint hiss is honest. Avoid the word "roar" in the journal and subtitles.**

Why, in order:
1. Storm winds on Mars are not stronger than Earth winds; the 99th percentile at Jezero is about 10 m/s (S5).
2. At that speed the push (dynamic pressure) in thin air is tiny, so wind noise is quiet (W2, A10).
3. The thin CO2 air absorbs high pitches (A2) and whistles and bells nearly vanish (A4). What is left is bass: InSight's wind was "nearly all bass" (W10).
4. Grains hitting the suit are real and can be heard through contact (A7). That is the "hiss" and "patter".

A loud, broad "roar" appears honest only later, as a **game extrapolation**: at 30 kPa and above, with more air, wind noise is louder and wider. Mark it as expected, not observed. If the owner wants the word "roar", use it only for 30 kPa and above, never for the Phase 1 storm.

## 8. What changes as pressure rises (game extrapolation, not observed)

Mars has never had 5, 30 or 75 kPa in any record we have. Everything here is reasoning from physics and our game rule. Mark it in the journal as "what we expect".

| Effect | 0.6 kPa (real today, VERIFIED above) | 5 kPa | 30 kPa | 75 kPa | Basis |
|---|---|---|---|---|---|
| Daytime sky | tan, hazy | still hazy tan, a little paler | lavender-blue | pale blue | Thicker, cleaner air scatters blue; textbook idea, NOT VERIFIED. The game colours are art. |
| Sunset | blue near the sun | blue-ish, weaker | neutral-warm | orange-red | V5. The flip is the lesson; a dust-free sky is assumed. |
| Sun size | 0.66 of Earth's | same | same | same | Orbit distance, not air. Pressure does not change it. |
| Haze and visibility | dust only | dust plus a little air haze | more air haze | most | Thicker air column adds haze, NOT VERIFIED. |
| Storms | big in the real world | smaller (game cap 0.8) | smaller (0.5) | small (0.25) | Game rule S7. |
| Sand and dust motion | rare, needs a gust | easier: threshold wind falls as air thickens | much easier | Earth-like | W1 (first-order rule); W4 caution for fine grains. |
| Sound | quiet, bass only | a little more | less muffled | near Earth-like | A9: also depends on CO2 share. |
| Clouds | thin ice wisps | same, E-10 later | water cycle begins | rain at equilibrium | T3; later stages are game rules. |
| Twilight | up to two hours of glow (T1) | same | shorter as high dust settles? | Earth-like | **Unverified guess**: high dust may fall out as the planet changes; no source. Do not teach. |

## 9. Items for students and the journal (two depths)

- **Middle school:** "Mars sky is tan because dust hangs in the air. Near the sunset, the sky turns blue around the sun. The sun looks about two-thirds as wide as it does on Earth. Sounds are quiet and deep, because the air is so thin." All VERIFIED (V1, V2, V7, A1, A2).
- **High school adds:** "Fine dust sends blue light forward, close to the sun. Red and yellow spread across the sky. The CO2 air absorbs high pitches, so only bass travels far. Wind needs to blow about nine times faster than on Earth to move sand (derived; check before use)."
- **Honest notes the journal should show** (from the "Science accuracy notes" idea in LEARNING.md): storms in the game are weaker than the real global storm; the dust devils are smaller than real ones; clouds are placeholders; sound is keyed on pressure only; sunsets at 30 and 75 kPa are our expectation, not a record.

## 10. Open items and what would help

| Item | What would settle it |
|---|---|
| Sun disc colour, storm sky colour, daytime shadow softness | Rover colour pixel samples from a mission page (optional sampling task) |
| Earth air density 1.2 kg/m3 for W2 | One NASA Earth fact sheet line (NSSDC) |
| Phobos and Deimos distances for T10, T12 | NSSDC satellite fact sheet pages (two fetches) |
| Dust-devil rate per sol | Open the Lorenz or Newman et al. paper via NTRS |
| Breath fog on a visor (AT-9b) | Not verifiable here. **Recommend: defer.** Nothing I opened covers suit visors; it is a feel effect, not a science one |
| Whether storm "pre-haze" (S6) is real | Needs a source or the journal must call it a game cue |

## Sources opened (this session)

All fetched with curl from allowed hosts; HTML stripped with `python3 -I`; nothing run.

| Short name | Host and page | Used for |
|---|---|---|
| NASA sunrise page | science.nasa.gov/solar-system/planets/mars/what-does-a-sunrise-sunset-look-like-on-mars/ | V2, V4, V7, T1 |
| NASA sunset Gale | science.nasa.gov/resource/sunset-at-mars-gale-crater-curiosity/ (PIA19400) | V2, V3, V4 |
| NASA storms tour | science.nasa.gov/solar-system/10-things-tour-of-storms-across-the-solar-system/ | V1 |
| NASA dust in the wind | science.nasa.gov/solar-system/10-things-dust-in-the-wind-on-mars-and-well-beyond/ | S1 |
| NASA shades of darkness | science.nasa.gov/resource/shades-of-martian-darkness/ | S2 |
| NASA Mars Report dust devils | science.nasa.gov/resource/mars-report-perseverance-rover-captures-dust-devils-whirling-across-mars/ | S9 to S11 |
| NASA Sounds of Mars | science.nasa.gov/mission/mars-2020-perseverance/sounds-of-mars/ | A1 to A5 |
| NASA InSight wind | science.nasa.gov/resource/sounds-of-mars-nasas-insight-senses-martian-wind/ | W10, A5 |
| NASA clouds (three) | science.nasa.gov/resource/clouds-sailing-overhead-on-mars-unenhanced/, .../photojournal/curiositys-three-frame-mosaic-of-clouds/, .../photojournal/curiosity-views-twilight-clouds/ | T3 to T5 |
| NASA Phobos, Deimos | science.nasa.gov/mars/moons/phobos/, .../deimos/ | T8, T9, T11, T12 |
| NSSDC Mars fact sheet | nssdc.gsfc.nasa.gov/planetary/factsheet/marsfact.html | density, day length, albedo |
| Maurice 2022 (Nature) | PubMed 35364602, abstract via eutils | A2, A3, A9 |
| Murdoch 2022 (Nat Commun) | PubMed 36513637, abstract via eutils | S8, S9, A7, A8 |
| Guzewich et al., winds Part 2 | ntrs.nasa.gov/api/citations/20230001086 (PDF) | S5, W3, W9 |
| Smith et al., wind and microphone | ntrs.nasa.gov/api/citations/20250010704 (PDF) | W5, A10 |
| Saltation papers | NTRS 19900005681, 19770028156, 19810060076 (abstracts) | W1, W4, S5 |
| Dust devil tracks | NTRS 20070032968 (abstract) | S11 |

**Seen only in a search summary (NOT counted as verified):** dust-devil encounter rate 1 to 4 per day (Lorenz 2012); gust peaks over 10 m/s from the microphone; water-ice cloud cover turning the sky bluish. Two hosts I could not use: photojournal.jpl.nasa.gov and www.ncbi.nlm.nih.gov (not on the allowed list; the NASA science and PubMed mirrors carried the same text).
