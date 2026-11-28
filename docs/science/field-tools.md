# Field tools: the real hand and field tools behind the game's tools

**Owner:** Subject Matter Expert · **Date:** 2026-11-28 · **Serves:** `docs/design/world-and-blocks.md` section 0.2 item 5c (owner: "we wont need pickaxes; we need drills and stuff to get samples; we should do research on all the hand tools we need") · **Related:** `machine-references.md`, `mars-surface-materials.md`, `mars-terrain-reference.md`

**How to read it.** "Real tool" is what the Artist and Designer copy. The **Game category** is one of: `dig-loose`, `break-rock`, `core-sample`, `ice`, `scan`, `brush`, `carry-and-store`. Mapping categories to Minecraft tool types and tiers is the Game Designer's job; the wear ideas below are suggestions, not decisions.

**Source rule.** **VERIFIED** = I opened the page this session and quote a line. All NTRS items are abstracts from `https://ntrs.nasa.gov/api/citations/<id>` (id given). **NOT VERIFIED** = from memory or from a search-result summary only; do not print it in the journal until checked. Specifications I could not open (tool weights, dimensions, battery sizes) are left out rather than guessed.

---

## 1. Summary: what the game should copy

1. **Real planetary work is sampling, not mining.** Apollo crews used "nothing more advanced than traditional terrestrial field instruments: rock hammer, scoop, claw tool, and sample bags" (NTRS 20110007961, VERIFIED). Mars missions use a scoop for loose soil and a rotary-percussive drill for rock (below). That matches the owner's idea.
2. **Four jobs, four kinds of tool:** loose material (scoop, rake), hard rock (hammer, drill), ice (heated or low-power drill), and looking (lens, scanners). Plus keeping the sample clean and labelled.
3. **Real tools fail in teachable ways:** scoops jam, drills stall, ice stops scoops, a mole with no grip stalls. These are the best source of "wear and failure" ideas (section 3).
4. **Honest gap:** nearly all Mars hardware is **robotic**, driven from Earth over many days. The game's hand tools are closer to **Apollo and Artemis astronaut tools** (suited human, hand-held). Say this in the journal.

---

## 2. Tool table

Power: **none** = hand-powered; **batt** = battery hand tool; **big** = needs machine-scale power (not hand-held in reality).

### 2.1 Apollo and Artemis astronaut tools (hand-held, suited crew)

| Real tool | Does (one sentence) | Works on | Scale / power class | Game category | Needs power | Wear idea | Honest note | Source |
|---|---|---|---|---|---|---|---|---|
| **Geology (rock) hammer** | Knocks a chip off a rock or breaks a boulder open. | Rock | Hand tool, none | `break-rock` | No | Chips slowly; no repair needed | Apollo crews used it; it is a normal field-geology tool | NTRS 20110007961: "rock hammer, scoop, claw tool, and sample bags" (VERIFIED) |
| **Scoop** | Picks up a bag's worth of loose soil. | Regolith, sand | Hand tool, none | `dig-loose` | No | Fine dust clings (see Surveyor scoop, 3.1) | Apollo "bulk regolith" samples (~24% of mass) were scooped | NTRS 20110007961 and 20240009312 (VERIFIED) |
| **Claw tool** | Grabs small rocks and lifts them without bending over. | Loose rocks | Hand tool, none | `dig-loose` | No | None | Name only is verified. I could not open a spec | NTRS 20110007961 (VERIFIED, name); shape NOT VERIFIED |
| **Rake** | Combs the surface and collects small rock pieces from the soil. | Pebbles in regolith | Hand tool, none | `dig-loose` | No | Bent tines | Rake samples were ~4% of Apollo sample mass | NTRS 20240009312: "rake samples (~4%)" (VERIFIED) |
| **Tongs** | Pick up a rock without touching it. | Rocks | Hand tool, none | `carry-and-store` | No | None | Tongs and gnomon are standard Apollo tools, but I did not open a page naming them | NOT VERIFIED |
| **Gnomon and colour chart** | A stick with a scale and colour bar, set beside a sample so the photo shows its size, up direction and colour. | Any | Hand tool, none | `scan` (context) | No | None | A good idea for "label the find" | NOT VERIFIED (no page opened) |
| **Sample bags** | Hold a collected sample and its label. | Loose samples | Hand tool, none | `carry-and-store` | No | None | Named in the same line as the hammer | NTRS 20110007961 (VERIFIED) |
| **Drive tube** | A hollow tube hammered into soil so a core of soil (layers kept) comes out. | Soft regolith | Hand tool + hammer, none | `core-sample` | No | Thin walls dent; tubes are single-use | First design (2 cm wide, thick wall) recovered only ~50% of the soil and distorted the layers; the 4 cm thin-wall tube recovered nearly 100% | NTRS 19890008919 (VERIFIED) |
| **Apollo Lunar Surface Drill (ALSD)** | A battery-powered drill that drove hollow core stems deep into the ground to drill holes for heat-flow probes and pull a core. | Regolith, down to metres | Battery hand drill on a stand | `core-sample` (deep) | Yes (batt) | High resistance; slow stem pull-out | Built "for emplacement of lunar subsurface heat flow probes" (NTRS 19690002958, VERIFIED). Crews met "higher than expected resistances" with the drill and trenching tool (NTRS 20240002664, VERIFIED). Apollo 15 deep core: six 42.5 cm segments (NTRS 19730039311, VERIFIED); Apollo 16 deep core 220 cm (NTRS 19940011854, VERIFIED) | see left |
| **Astronaut Lunar Drill (ALD)**, Artemis | A rotary-percussive hand drill: deep regolith cores, surface rock cores, and strength tests, held on a stand. | Regolith and rock | Battery; "only require the crewmember to hold a single switch" | `core-sample` | Yes (batt) | Battery life; percussion engages only when needed to save power | "Extraction of deep cores... was a problem on Apollo"; the stand helps. Also carries a cone penetrometer and shear vane for soil strength | NTRS 20210013587 (VERIFIED) |
| **Artemis geology tool set** | A new family of sampling tools for suited crew, 8 first and 17 more proposed. | Mixed | Hand tools | all | Mixed | n/a | Shows hand tools are still being redesigned for gloves | NTRS 20210000242 (VERIFIED) |
| **Vacuum-sealable container** | A sealed box that keeps gas (volatiles) from escaping. | Samples | Hand carried | `carry-and-store` | No | Seal fails if dust is on it | Apollo containers' indium and Viton seals "were not reliable in the lunar environment" (NTRS 19890008919, VERIFIED) | NTRS 20210013587 (VERIFIED) |
| **Apollo Lunar Roving Vehicle sampling** | Crews drove the LRV to stations and sampled by hand from it. | n/a | n/a | n/a | n/a | n/a | I found no page that describes LRV sample tools. The rover was battery-powered (NTRS 19720015165, VERIFIED); sampler details NOT VERIFIED | NTRS 19720015165 |

### 2.2 ISS and EVA tools (not for sampling; for building and repair)

| Real tool | Does | Scale / power | Game category | Needs power | Wear idea | Honest note | Source |
|---|---|---|---|---|---|---|---|
| **Pistol Grip Tool (PGT)** | A battery, computer-controlled wrench that sets exact torque on a bolt. | "3/8-inch-drive hand-held tool"; also works as a plain ratchet if the battery dies | Not a sampling tool; maps to a **wrench / assembly tool** (outside the 7 categories) | Yes (batt) | Battery; torque limit | A training copy weighs 1.1 kg underwater and runs 6 h | NTRS 19960025621 and 20120006613 (VERIFIED) |
| **EVA tool kit** (wrenches, powered sockets, foot restraints) | Everyday tools for repair outside a spacecraft. | Hand tools | wrench (outside categories) | Some | n/a | "The Space Shuttle carries a standard toolkit in the cargo bay for emergencies." | NTRS 19880033923 (VERIFIED) |
| **Handrails and tethers** | Keep an astronaut from drifting away. | n/a | Not needed on Mars (gravity) | n/a | n/a | Torque and tether ideas do not carry over; skip | Not opened; NOT VERIFIED |

### 2.3 Mars landers and rovers (robotic)

| Real tool | Does | Works on | Scale / power | Game category | Needs power | Wear idea | Honest note | Source |
|---|---|---|---|---|---|---|---|---|
| **Viking surface sampler arm** (1976) | A boom with a scoop that dug soil and reached it to the lander's instruments. | Dust and sand | A 3-axis boom that "positions a soil sampler and provides digging forces" | `dig-loose` | Yes | Jam: a latch pin "failed to fall clear" and jammed it on the first use | Fixed by changing the command sequence | NTRS 19730014590 and 19770027873 (VERIFIED) |
| **Phoenix robotic arm and scoop** | Digs trenches, scrapes icy soil, and delivers samples to the ovens (TEGA) and wet cell (MECA). | Soil and ice-rich soil | "operated for over 150 sols"; the "Icy Soil Acquisition Device" is a separate powered mechanism for ice | `dig-loose`, `ice` | Yes | Ice-cemented ground stops a plain scoop | Phoenix arm length (about 2.4 m) and "rasp" are from a search summary: NOT VERIFIED. The follow-on Icebreaker concept needs a drill for "the ice-cemented Mars subsurface that stymied the Phoenix scoop" | NTRS 20150014801, 20080023069, 20240006097 (VERIFIED) |
| **MSL (Curiosity) scoop and CHIMRA** | Scoops loose soil; CHIMRA (Collection and Handling for In-situ Martian Rock Analysis) processes samples and delivers them. | Sand | Arm-mounted | `dig-loose` | Yes | Grit in the handling parts (idea) | Only the name and the role "sample processing" are verified | NTRS 20150007475 (VERIFIED) |
| **MSL (Curiosity) drill** | A rotary-percussive drill that powders rock for the lab instruments. | Clay to basalt, "depths up to 50 mm" | Arm-mounted | `core-sample` (powder) | Yes | The feed mechanism stalled in 2016; drilling stopped for 521 sols until a new method (feed extended) worked | Great real failure story | NTRS 20120013241, 20100021931, 20230005798 (VERIFIED) |
| **Perseverance coring drill, bits and sample tubes** | A rotary-percussive drill that cuts a rock core into a titanium tube; bits are swapped between coring, regolith and abrasion. | Rock, regolith | Holes "1 inch (27 millimeters)"; a bit carousel holds bits | `core-sample`, `carry-and-store` | Yes | Bits wear and are changed from a carousel; the chuck must not jam | A "suite of interchangeable bits: coring bits, regolith bits and an abrader" | https://science.nasa.gov/mission/mars-2020-perseverance/rover-components/ ("Drill bits ... 1 inch (27 millimeters)") (VERIFIED); NTRS 20220000868 and 20230006955 (VERIFIED) |
| **Abrasion bit and RAT (Rock Abrasion Tool)** | Grinds a clean flat patch on a rock so dust and weathered skin are gone. | Rock surface | Drill bit; RAT was on the MER rovers | `brush` (surface prep) | Yes | Bit wear | Perseverance's abrader is a bit on the corer; the MER RAT is named in an Opportunity analysis | https://science.nasa.gov/... (above) (VERIFIED); NTRS 20060009018 (VERIFIED) |
| **Gas Dust Removal Tool (gDRT)** | Blows compressed gas to sweep abrasion dust off before the instruments look. | Dust on rock | Gas puff | `brush` | Yes (gas) | Valve fails when too cold (-20 C rating risk) | Real equivalent of a brush | NTRS 20210012228 (VERIFIED) |
| **Mars Hand Lens Imager (MAHLI)** | A close-up camera on Curiosity's arm that works like a hand lens. | Rock and soil grains | "2.1 cm to infinity; at 2.1 cm... 14 microns/pixel" | `scan` | Yes | Dust on lens | Name and role match the geologist's hand lens | NTRS 20150007463 (VERIFIED) |
| **InSight HP3 "mole"** | A self-hammering spike meant to bury itself 3 to 5 m and measure heat flow. | Regolith | Target 3-5 m; "progress stopped after only 35 cm" | `core-sample` (deep probe) | Yes | Stalled: "lack of friction in an unexpectedly thick cohesive duricrust" | The team used the arm's **scoop** to push on it; it reached about 37 cm. The ~20 cm duricrust layer sat under 1 cm of dust. Resistance 0.3-0.7 MPa | NCBI PMC9734249 (Spohn et al. 2022), NTRS 20220003780 (VERIFIED) |

### 2.4 Terrestrial planetary field kit (on Earth and in analog sites)

| Real tool | Does | Game category | Power | Status |
|---|---|---|---|---|
| **Rock hammer and chisel** | Breaks a fresh surface; the chisel splits layers. | `break-rock` | No | Hammer VERIFIED (above). Chisel NOT VERIFIED (no page opened) |
| **Hand lens** (10x) | Shows grains and crystals. | `scan` | No | Role confirmed by MAHLI's name; the lens itself NOT VERIFIED |
| **Brunton compass** | Measures the tilt (dip) and direction of rock layers. | `scan` | No | NOT VERIFIED (an NTRS search found nothing). Optional |
| **Trowel** | Digs a small pit. | `dig-loose` | No | NOT VERIFIED; same job as the scoop |
| **Soil auger** | A hand screw that drills a soil core. | `core-sample` | No | NOT VERIFIED. NTRS has a lunar "rotary stepped auger" design (19900014261: "cuttings slide up the helix", VERIFIED) |

### 2.5 Digging, hauling and building (machine scale)

| Real tool | Does | Scale / power | Game category | Honest note | Source |
|---|---|---|---|---|---|
| **RASSOR excavator** | A light robot with two counter-rotating bucket drums that dig and carry regolith. | "<100 kg"; RASSOR 2.0: 66 kg, 4 W per kg/h, 2.7 t per day; 35 trips of 20 kg a day in the first plan | `dig-loose` (machine, not hand) | Far bigger than a hand tool. Use it as a machine upgrade, not a held item | NTRS 20150022134, 20210011366 (VERIFIED) |
| **Bucket drum** | A hollow cylinder with scoops that fills as it turns one way and empties the other way. | Part of RASSOR; IPEx is "a 30kg-class robotic excavator" | `dig-loose` | A nice model for a "regolith scooper" block | NTRS 20210025846 (VERIFIED) |
| **Microwave sintering** | Heats loose regolith until the grains fuse into a hard pad or brick. | Prototype stage | `build` (not a tool) | "Microwave energy was selected since it is the only method to volumetrically heat the regolith." Lunar tests; Mars use NOT VERIFIED | NTRS 20210020601 (VERIFIED) |
| **Cable-and-drill ideas** | Cable-hung drills for deep holes. | n/a | n/a | The ice drill below is cable-hung | NOT VERIFIED beyond the ice drill |

### 2.6 Ice

| Real tool | Does | Scale / power | Game category | Needs power | Wear idea | Honest note | Source |
|---|---|---|---|---|---|---|---|
| **Low-power ice drill (NASA JSC and Baker-Hughes)** | A down-hole drill that cuts ice cores with no drilling fluid. | "low mass (approx.20kg)... very low power levels (approx.60 watts electrical)"; "attached to a cable" | `ice` | Yes (about 60 W) | Cutting teeth wear; drill must avoid melting the core | Arctic tests. It is a down-hole unit, not hand-held | NTRS 20040085649 (VERIFIED) |
| **Icebreaker drill concept** | A drill to reach ice 1 to 2 m down and bring samples up. | "drilling up to 1 m"; "samples down to 2m depth" | `ice` | Yes | Ice sticks to the bit | A mission concept, never flown | NTRS 20190001259, 20240006097 (VERIFIED) |
| **Rodwell (Rodriguez Well)** | Melts a cavity in ice and pumps the hot water up a shaft. | JSC lab tests at Mars pressure | `ice` (machine) | Yes (heat, pump) | Pump and heater scale | Already the basis of the game's ice extractor (`machine-references.md`) | NTRS 20205011353 (VERIFIED) |
| **Ice auger, thermal head** | Hand-cranked or heated screw for shallow ice. | n/a | `ice` | Optional | Ice jams the flutes | Terrestrial tool; no Mars page opened | NOT VERIFIED |

### 2.7 Detectors and scanners

| Real tool | Does | Scale | Game category | Needs power | Honest note | Source |
|---|---|---|---|---|---|---|
| **Handheld XRF** | Fires X-rays at a rock and reads which elements glow back. | A reading takes "approx. 60 seconds" | `scan` | Yes (batt) | Works well on Si, Ca, Ti, K; is "unable to detect" some other elements (the abstract is cut off; which ones is not verified). Sample prep changes the result | NTRS 20120001846, 20170002326 (VERIFIED) |
| **Ground-penetrating radar (GPR)** | Sends radio pulses down and reads the echoes to map layers and buried ice. | Mars GPR: up to 50 m deep at 0.5 m resolution; CRUX GPR: 5 m deep at 10 cm | `scan` | Yes | Rover-mounted; real units are not hand-sized | NTRS 20090018991 (VERIFIED) |
| **Neutron spectrometer** | Counts neutrons leaking from the soil; fewer fast ones means hydrogen, so likely ice. | Resource Prospector: finds "0.5 wt%" or more water-equivalent hydrogen, down to about 1 m | `scan` | Yes | Rover-mounted; used for **finding** ice, not for digging it | NTRS 20170010372 (VERIFIED) |
| **Hand lens or microscope** | See grains up close. | MAHLI at 14 microns/pixel | `scan` | No or camera | See MAHLI above | NTRS 20150007463 (VERIFIED) |

### 2.8 Containers and contamination control

| Real practice | What it does | Game category | Honest note | Source |
|---|---|---|---|---|
| **Sealed tubes** | Perseverance seals each core in a titanium tube and leaves it on the surface. | `carry-and-store` | "hermetically sealed" tubes; deposited for up to 10 years | NTRS 20210007841 (VERIFIED); https://science.nasa.gov/mission/mars-2020-perseverance/rover-components/ ("stores the sample cores in tubes") (VERIFIED) |
| **Contamination control** | Keeps Earth organic and mineral dust out of a sample. | `carry-and-store` | "assuring low levels of terrestrial contamination is of the utmost concern" | NTRS 20210012052 (VERIFIED) |
| **Cold and special storage** | Some Apollo samples were "sealed on the lunar surface, frozen... or stored under Helium" and studied 50 years later. | `carry-and-store` | A good link to **ice samples must stay cold** | NTRS 20205005336 (VERIFIED) |
| **Labels and catalogue** | Each sample is numbered, weighed and photographed; bags are "documented." | `carry-and-store` | Matches "sample, label, report" | NTRS 20240009312 (VERIFIED) |
| **Sample sorting** | Soil sieved into <1, 1-2, 2-4, 4-10 mm fractions. | `carry-and-store` | A fun "sieve" tool | NTRS 20240009312 (VERIFIED) |

---

## 3. Wear, jam and dust ideas (from real failures)

| Real event | Teaches | Game idea (suggestion for the Game Designer) |
|---|---|---|
| Viking 1 arm jammed because a latch pin did not fall clear; fixed by a changed command order (NTRS 19770027873) | Tools can jam; a fix is a different procedure, not new parts | A scoop "jams" once; the fix is a short action, not a new item |
| Curiosity drill feed stalled in 2016; 521 sols until drilling resumed a new way (NTRS 20230005798) | A mechanism wears and you adapt | A drill with a "feed wear" meter; a repair or a slower mode |
| InSight mole stalled in duricrust, then the scoop pushed on it (PMC9734249) | Crust is hard and friction matters; one tool can help another | A **duricrust** layer: scoop fails, hammer or drill needed (see section 4) |
| Phoenix scoop could not cut ice-cemented ground (NTRS 20240006097) | Ice-rich ground needs a drill | Ground ice cannot be scooped; needs the ice drill |
| Apollo "higher than expected resistances" for the drill and trenching tool (NTRS 20240002664) | Real regolith resists more than lab sand | Drill slows with depth |
| Surveyor 3 scoop: soil stuck to its surfaces (NTRS 19720019095, adhesion measured) | Dust sticks | Dust load on tools; the brush clears it |
| Apollo seals "were not reliable" (NTRS 19890008919) | A bad seal ruins the sample | Samples spoil if the container is unsealed or warm |
| gDRT valve below its rated -20 C (NTRS 20210012228) | Cold breaks gas and rubber | Cold-night tool slowdown |

---

## 4. Link to the blocks (`world-and-blocks.md` section 3.1)

| Block | Real behaviour | Suggested tool category |
|---|---|---|
| Regolith, sand | Loose; scoop, rake or trowel | `dig-loose` |
| **Duricrust** (the hard crust under 1 cm dust; real Mars, HP3: ~20 cm, 0.3-0.7 MPa) | Plain scoop fails; hammer or drill | `break-rock` or `core-sample` |
| Basalt, hematite ore, gypsum, carbonate | Hard rock; the real route is a rock hammer or a core drill | `break-rock`, `core-sample` |
| Ground ice | Ice-cemented soil stopped Phoenix's scoop | `ice` |
| Meteorite iron | Hammer chips; hand lens or XRF identifies | `break-rock`, `scan` |

Not found in the sources: a Mars-specific hardness scale for these rocks. Mohs hardness values I know from textbooks (gypsum soft, basalt hard) are NOT VERIFIED here.

---

## 5. Recommended short list: 12 hand tools

Grouped by job. Each has the real reference. Source status as above.

| # | Game tool | Category | Power | Real reference |
|---|---|---|---|---|
| **A. Dig and collect (loose)** | | | | |
| 1 | **Sampling scoop** (trowel head) | `dig-loose` | No | Apollo scoop; Phoenix/Curiosity scoop (NTRS 20110007961, 20150014801) |
| 2 | **Regolith rake** | `dig-loose` | No | Apollo rake samples (NTRS 20240009312) |
| **B. Break rock** | | | | |
| 3 | **Geology hammer and chisel** | `break-rock` | No | Apollo rock hammer (NTRS 20110007961); chisel NOT VERIFIED |
| **C. Core** | | | | |
| 4 | **Drive tube** | `core-sample` | No | Apollo drive tubes (NTRS 19890008919) |
| 5 | **Core drill** (battery; bits: coring, regolith, abrader) | `core-sample` | Yes | Perseverance drill bits and 27 mm hole (science.nasa.gov rover-components); Curiosity drill (NTRS 20120013241) |
| 6 | **Deep-core drill on a stand** (upgrade) | `core-sample` | Yes | Apollo ALSD (NTRS 19690002958), Artemis ALD (NTRS 20210013587) |
| **D. Ice** | | | | |
| 7 | **Ice drill** (low power, cable-hung) | `ice` | Yes | JSC/Baker-Hughes 20 kg, 60 W drill (NTRS 20040085649); Icebreaker (NTRS 20240006097) |
| **E. Clean** | | | | |
| 8 | **Dust brush and gas puff** | `brush` | Puff needs gas | Perseverance abrader + gDRT (rover-components page, NTRS 20210012228); a hand brush itself NOT VERIFIED |
| **F. Look and scan** | | | | |
| 9 | **Hand lens** | `scan` | No | MAHLI (NTRS 20150007463) |
| 10 | **Handheld XRF** | `scan` | Yes | NTRS 20120001846, 20170002326 |
| 11 | **Ground scanner** (radar and neutron: shows buried ice and layers) | `scan` | Yes | NTRS 20090018991 (GPR), 20170010372 (neutron) |
| **G. Carry and store** | | | | |
| 12 | **Sample tubes, bags and labels** (sealed, cold option) | `carry-and-store` | No | Mars 2020 tubes (NTRS 20210007841), Apollo bags and catalogue (NTRS 20240009312), cold storage (NTRS 20205005336) |

**Cut first if scope is tight:** 2 (rake), 6 (deep drill) and 11 (ground scanner: already in `machine-references.md` as "survey scanner").
**Not to add:** handrails, tethers (no Mars use); a pistol-grip wrench is only needed if the Designer adds an "assemble machine" step.

## 6. Honest notes the journal should show

- **Mars hardware is robotic.** "Perseverance, Curiosity, Phoenix and InSight dug and drilled by robot arm. The hand tools in this game are closer to Apollo and the planned Artemis astronaut tools." (VERIFIED basis, sections 2.1 and 2.3)
- **Real drills take hours to days per hole** and need careful power; the game compresses this. (General; no timing page opened, so no number is given.)
- **Machines like RASSOR and Rodwell are machines, not hand tools.**
- **The "mole" story is real:** a spike that needed a scoop to push it into Mars's hard crust.
- **Gypsum, carbonate, hematite sampling behaviour is not covered by any tool page here;** rock hardness values are textbook-level and NOT VERIFIED in this file.

## 7. Source list (all opened)

NTRS API pages (`https://ntrs.nasa.gov/api/citations/<id>`): 19690002958, 19730014590, 19730039311, 19770027873, 19880033923, 19890008919, 19900014261, 19940011854, 19720015165, 19720019095, 19960025621, 20040085649, 20060009018, 20080023069, 20090011852, 20090018991, 20100021931, 20110007961, 20120001846, 20120006613, 20120013241, 20150007463, 20150007475, 20150014801, 20150022134, 20170002326, 20170010372, 20190001259, 20190033743, 20205005336, 20205011353, 20210000242, 20210007841, 20210011366, 20210012052, 20210012228, 20210013587, 20210020601, 20210025846, 20220000868, 20220003780, 20230005798, 20230006955, 20240002664, 20240006097, 20240009312.
Other: https://science.nasa.gov/mission/mars-2020-perseverance/rover-components/ ; NCBI PMC9734249 via eutils (Spohn et al. 2022, InSight HP3 mole).
Not opened (search summary only): Phoenix arm length and rasp (jpl.nasa.gov and a Springer PDF were returned by search, not fetched; they are outside the allowed host list).
