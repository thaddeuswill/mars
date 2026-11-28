# Hydrosphere target, salt level and the seven gate items: science review

**Author:** Subject Matter Expert · **Date:** 2026-11-28 · **Status:** recommendation; every game number stays **INVENTED** · **Reviews:** `docs/design/phase3-hydrosphere-and-drift.md` section 2, `docs/design/gate-input-items.md` · **Goals:** A1, A2, B5, C1, C2, D2
**Marks.** **VERIFIED** = I opened the page or record this session (curl; HTML stripped with `python3 -I`; nothing run) and quote the line. **NOT VERIFIED** = from memory or only a search summary; do not put it in game text. **calc** = my arithmetic (scratch Python, not run in the game). **REAL / SIMPLIFIED / INVENTED** as in `mars-surface-materials.md`. Hosts used: ntrs.nasa.gov, science.nasa.gov, planetary.org, eutils.ncbi.nlm.nih.gov. The PubMed web pages returned a cookie wall; abstracts came through eutils.

## 1. The real water inventory

| Fact | Number | Source and quote | Mark |
|---|---|---|---|
| Near-surface water today, as a global layer (GEL = global equivalent layer, the depth if it were spread over the whole planet) | **about 34 m GEL**, in polar layered deposits, lobate debris aprons, ice-rich mantles and shallow ground ice (Carr and Head's estimate, as summarised) | NTRS 20160003145 (Mars water inventory, 2016): "their estimate of the present day near-surface inventory of H2O (approximately 34 m GEL, stored as ice in the polar layered deposits (PLD), lobate debris aprons, ice-rich latitude dependent mantles, and as shallow ground ice)" https://ntrs.nasa.gov/citations/20160003145 | **VERIFIED** (via a summary of Carr and Head, not their paper) |
| Total outgassed water over history | 7 to 1000 m GEL: wide, contested | Same record: "estimates of the total outgassed inventory of water on Mars have ranged from a global equivalent layer (GEL) approximately 7-1000 m deep" | **VERIFIED** |
| North residual cap | "possibly containing an amount of water equivalent to a global layer several tens of meters thick" (old, 1991 review) | NTRS 19910016760 https://ntrs.nasa.gov/citations/19910016760 | **VERIFIED** (dated) |
| Polar caps vs the rest of the surface ice | "Martian polar ice caps hold most of the exposed water ice on the surface of Mars" | NTRS 20040085623 https://ntrs.nasa.gov/citations/20040085623 | **VERIFIED** |
| Polar cap volume in km3, and a polar-only GEL | I could not open a source with a clean figure (a NASA mission PDF that a search returned gave a number with an obvious units error; I did not use it) | none | **NOT VERIFIED**: do not quote |
| SHARAD (the radar on Mars Reconnaissance Orbiter) sees inside both caps | "imaging the interiors of the north and south polar ice caps known, respectively, as Planum Boreum and Planum Australe" | Foss et al. 2017, PubMed 29400351 https://pubmed.ncbi.nlm.nih.gov/29400351/ (abstract via eutils) | **VERIFIED** |
| Utopia Planitia buried ice (SHARAD, published 2016, not 2023) | Water volume "about 14,300 cubic kilometers"; "about as much water as what is in Lake Superior"; deposit bottom about 10 to 170 m deep; "about halfway between the planet's equator and north pole" | NASA caption https://science.nasa.gov/resource/location-of-large-subsurface-water-ice-deposit-in-utopia-planitia-mars/ ; Stuurman (JPL), "50-80% water ice", over 600 radargrams: https://www.planetary.org/articles/1122-subsurface-water-ice-in-utopia-planitia-mars | **VERIFIED** |
| Same deposit as a global layer | 14,300 km3 / 144.8 million km2 (planet area, from memory) = **about 0.1 m (10 cm) GEL** | calc | calc; planet area NOT VERIFIED |
| Arcadia Planitia | Similar widespread subsurface ice, "tens of metres thick" | Planetary Society page above, citing Bramson et al. 2015: "a similar composition for the subsurface in Arcadia Planitia ... widespread areas where these deposits are tens of metres thick" | **VERIFIED** (second hand) |
| 2023 Utopia / Arcadia results, GRS (gamma-ray and neutron) hydrogen maps, Phoenix | The 2023 papers and the GRS maps were **not opened**. Phoenix: bright clumps "were probably water ice – the material had vaporized four days after the scoop" | Phoenix quote from `mars-surface-materials.md` row 16 | 2023 and GRS: **NOT VERIFIED**; Phoenix **VERIFIED** |
| Former ocean: size | Early Mars had "at least 137 meters" GEL, from the deuterium (heavy hydrogen) ratio in polar ice | NTRS 20150007120 (Villanueva et al., Science 2015): "early Mars (4.5 billion years ago) had a global equivalent water layer at least 137 meters deep" | **VERIFIED** (a lower bound, an inference) |
| Former ocean: surface fraction | I recall "about 19 percent of the surface" for that study and larger values (about a third) from shoreline work. **Not on any page I opened** | none | **NOT VERIFIED** |
| Former ocean: contested | Carr and Head argue the near-surface inventory at the end of the Noachian (about 24 m) and Hesperian (about 62 m) was "incompatible with the existence of a former ocean". A 2025 climate model holds a stable polar ocean "half in the ocean, half in the ice sheet" if water was also absorbed by the ground (the model's number was lost in the stripped text) | NTRS 20160003145; NTRS 20250010130 https://ntrs.nasa.gov/citations/20250010130 ; for: giant polygons below about -4000 m, NTRS 20130001894 | **VERIFIED** that it is debated. Journal must say "may have" |
| Liquid water today | "the Martian atmosphere is too thin for liquid water to exist for long on the surface"; water today is "water-ice just under the surface in the polar regions" and seasonal briny flows | https://science.nasa.gov/mars/facts/ | **VERIFIED** |

### 1.1 What "percent of surface with liquid water" can honestly mean
It is **not measured on Mars** (0 today) and not a real quantity anyone tracks. As a game number it means: **how much of the planet's area has standing liquid water, as a share of all area**. Earth's ocean is about 71 percent (common knowledge, not opened). Rule for the journal: show it as a game scale, never "Mars has X percent water".

### 1.2 Is the 30 percent Equilibrium target defensible? Yes, as game design, with a plain label
- It is the same order as the ocean sizes in the literature (about a fifth to a third; **NOT VERIFIED**), and well below Earth's 71 percent. Good: "a wet Mars, not a second Earth".
- **Ice budget check (calc):** water to cover 30 percent of the planet at a mean depth of 50 m = 15 m GEL; at 100 m = 30 m GEL. Today's near-surface ice is about 34 m GEL. So 30 percent is **possible only if the game melts roughly half to nearly all of the accessible ice**, and shallow lakes (tens of metres) are the only way it works. That is a good fact for the journal: "It would take most of Mars's ice."
- **Scale check:** one Lake-Superior-sized deposit (Utopia) is only about 0.1 m GEL (calc). One extractor "settling at 30 percent" is therefore a game compression, not a real rate. Keep it labelled.
- **Verdict:** keep 30 percent. Label: **"Game design, based on real ice. Mars holds about 34 m of near-surface water as ice; this target would melt much of it."** Do not call 30 percent the real ocean size.

### 1.3 Loss direction for the hydrosphere term
| Real cause | Plain version (grade 6-7) | Source | Mark |
|---|---|---|---|
| **Freezing** | Water that cools below 0 C turns back to ice; ground ice is where it came from | Mars Facts above; ground ice rows in `mars-surface-materials.md` | **VERIFIED** (state); loss link is reasoning |
| **Evaporation and sublimation** | Thin air lets water or ice turn to vapor and drift away (the Phoenix ice "vaporized four days after the scoop") | Phoenix quote; Mars Facts "too thin for liquid water to exist for long" | **VERIFIED** |
| **Infiltration (soaking into the ground)** | Porous rock and a cold underground layer take water in | NTRS 20250010130: "a significant amount of water has been absorbed by the ground ... mineral alteration, or ... a deep porous reservoir" | **VERIFIED** (model, stated as plausible) |
| **Boiling at low pressure** | Under very low pressure water boils away | Not opened. Basic physics; the triple-point value is in `accuracy-review.md` | NOT VERIFIED here |

**Recommendation:** the design's `restore` form toward 0 (a share of what exists is lost each sol) points the right way and covers all four causes. Keep one number in version 1. For version 2, make the loss **bigger when pressure is low** (the design already plans this) and **bigger when cold** (freezing). Label the rate **INVENTED**; the direction is real. Suggested journal line: **"Water is lost by freezing, by turning to vapor in thin air, and by soaking into the ground. The rate is a game number."**

### 1.4 Salt level side effect
Real hook, verified: Phoenix found perchlorate "at a concentration of ~0.5 wt% in northern plains soils"; its salts are "very soluble, deliquescent, and have low eutectic temperature (which decreases the temperature for stable liquids on Mars)" (NTRS 20190028297 https://ntrs.nasa.gov/citations/20190028297) **VERIFIED**. A 1 percent perchlorate analog killed tomato plants "in less than 2 weeks" (PubMed 41597718, Microorganisms 2026) **VERIFIED** (one study, a simulant). So **melted ice or washed soil carrying salts is honest**; it is a hazard for plants (B5) and people ("toxic to humans ... thyroid dysfunction", NTRS 20150023545 **VERIFIED**). Caveat: real salt sits in the **soil**, not in ice. Ground ice that is clean but mixed with soil would pick salts up when melted. Journal line: "Meltwater picks up salts from the soil. Plants do not like them." Do not say Mars ice itself is salty.

## 2. Perchlorate removal (resin pack, soil wash)

| Idea | Mark | Evidence |
|---|---|---|
| Perchlorate is in Mars soil and is toxic | **REAL** | Above; NTRS 20190028297 |
| **Wash with water**, then treat the brine | **REAL (principle), SIMPLIFIED** | "very soluble" (NTRS 20190028297) so washing moves it into water. Whether washing is the planned Mars method: **NOT VERIFIED** |
| **Ion-exchange resin removes perchlorate from water** | **REAL on Earth** | "Removal and recovery of perchlorate from fireworks production wastewater through ion exchange and biological reduction processes" (PubMed 40048978, title only); "perchlorate ... adsorption ... based on ion exchange reaction"; "Dissolved perchlorate (100mg/L) can be completely removed by acclimated anaerobic sludge in 15h" (PubMed 28341379, Bioresour Technol 2017) **VERIFIED** |
| ISS already uses ion-exchange resin for water | **REAL** | "Multifiltration Bed, which includes adsorbent media and ion exchange resin for the removal of dissolved organic and inorganic contaminants" (NTRS 20120015729) **VERIFIED** |
| Bioremediation of perchlorate for Mars | **REAL as a research idea** | "Although several perchlorate reducing bacteria have been identified and utilized in water treatment systems on Earth ... a perchlorate reducing strain for implementation on Mars" (NTRS 20150023545, a design plan, not a working system) **VERIFIED** |
| A "resin pack" as a Mars-ready, proven soil cleaner | **INVENTED** (the item), **SIMPLIFIED** (the idea) | No Mars-soil resin study opened |

**Verdict: SIMPLIFIED, not INVENTED.** The item tooltip "Game invention, not real" is too harsh and teaches that nobody removes perchlorate. Change to: **"Game-sized version of a real idea: ion-exchange resin catches perchlorate in water on Earth. Nobody has cleaned real Mars soil this way yet."** The existing journal line "Real fact: perchlorate in Phoenix soil, SME verified" is now sourced (NTRS 20190028297). Also correct `mars-materials-and-recipes.md` row 1.3 (it said the figure was not verified): ~0.5 wt% is verified; the older "0.4 to 0.6" is not.

## 3. Sorbent bed for CO2

| Claim | Mark | Evidence |
|---|---|---|
| Sorbent beds remove CO2 on the ISS, regenerable | **REAL** | "zeolite based carbon dioxide CO2 removal systems have historically been used successfully on Skylab and the International Space Station (ISS)" (NTRS 20250004920); "4 Bed Molecular Sieve (4BMS) of the Carbon Dioxide Removal Assembly (CDRA)" (NTRS 20150021467); amine beds: "pressure-swing regenerable beds" (NTRS 20060052563); "solid amine resin ... regenerative CO2-removal systems for manned spacecraft" (NTRS 19730016413) **VERIFIED** |
| Beds need upkeep | **REAL** | "As of 2014, the zeolite-based CDRA had the dubious honor of requiring more crew maintenance time than any other system on the ISS"; zeolite dust problem (NTRS 20250004920) **VERIFIED** |
| "Sorbent bed" as a name and role | **REAL, honest** | Same sources |
| **Porous regolith brick as the sorbent** | **INVENTED shortcut** | A fired brick is not a zeolite or amine. Real sorbents are zeolite minerals or amine resin. Regolith brick makes a fair stand-in for a "porous block" but the journal must not say brick captures CO2 |
| Bed "fills up" | **REAL** | Beds saturate, then are heated or put under vacuum to release the CO2 (regeneration) |

**Verdict: the bed is honest; the brick is a stand-in.** Tooltip: **"Sorbent bed. Porous pellets that hold CO2 until they fill. Real ones use zeolite or amine, and are heated to let the CO2 go."** Honest note for the journal: on the ISS the captured CO2 is **released again** (vented, or fed to a reactor); the game's scrubber locks it into rock, a **SIMPLIFIED** choice (carbonate rock is real, see `mars-conductor-and-carbonate-ores.md`). A bed removes CO2 only; it makes **no oxygen**, which matches the design.

## 4. Other gate items: real hardware analogues

| Item | Analogue | Mark and source |
|---|---|---|
| **Heater coil** | Phoenix TEGA: soils "individually heated to 1000 C in the DSC ovens" (NTRS 20090022803). Resistive wire as the heater: plain engineering | Ovens **VERIFIED**; "coil of iron wire" **NOT VERIFIED** (TEGA ovens were nickel, NTRS 20090010369 "TEGA ovens were fabricated from high purity Ni"). Say "heater element", not "iron coil", if real is quoted |
| **Filter cartridge** | ISS HEPA "Bacteria Filter Elements (BFEs)": "13 elements deployed on board", pre-flight life prediction "1 year" (NTRS 20030065941). Mars: MOXIE-type pumps need dust kept out; a scroll-pump test used "an inlet filter" and checked dust that "bypasses" it (NTRS 20210011639) | **VERIFIED** (ISS filters; dust risk). MOXIE filter details **NOT VERIFIED** |
| **Pump bearing** | ISS external ammonia coolant pump failed 2010; "root cause was postulated to be high speed wear of the bearings" (NTRS 20180004538; NTRS 20150004079) | **VERIFIED**: a strong, real "bearings wear out" story. (Hematite "blueberry" link is a nice extra: spherules are real, `mars-surface-materials.md` row 11) |
| **Signal board** | Mars orbiters carry radio relays: "relay-equipped orbiters ... telecommunications relay services to ... Spirit and Opportunity, and to ... Phoenix Lander" (NTRS 20150008935). A radio needs electronics | Relay **VERIFIED**; "circuit board" as the part: plain engineering, **NOT VERIFIED** as a Mars-specific fact. Game item stays **INVENTED** |
| **Activation cell** | None found. A transmitter does need power, but a one-use "activation cell" is a story device | **INVENTED**. Keep "Game invention, not real" |
| Seal kit (not a gate item) | Not reviewed | n/a |

Label summary for `gate-input-items.md`: heater coil, filter cartridge, pump bearing, sorbent bed = game-sized version of a real idea (as already labelled); **resin pack: change from "Game invention" to "game-sized version of a real idea"**; signal board and activation cell stay "Game invention".

## 5. Verdicts (six lines)
1. **Inventory:** about 34 m GEL of near-surface ice, mostly polar and mid-latitude (verified, summarised from Carr and Head); polar-only volume not verified.
2. **30 percent target:** defensible as game design; label "Game design, based on real ice"; it would melt much of Mars's ice (calc).
3. **Loss term:** direction real (freezing, vapor loss, soaking in; low pressure boosts it); rate INVENTED.
4. **Perchlorate resin and wash:** SIMPLIFIED (real on Earth, never done for Mars soil); change the tooltip.
5. **Sorbent bed:** real ISS hardware (zeolite and amine); "porous regolith brick" is an INVENTED stand-in.
6. **Other items:** heater, filter, bearing have real analogues; signal board and activation cell are INVENTED.
