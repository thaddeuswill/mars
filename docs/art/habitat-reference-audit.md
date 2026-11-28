# Habitat Reference Audit: dome, hull, hatch, tanks, reclaimer, oxygen maker

**Date:** 2026-11-27 · **Owner:** Subject Matter Expert (research) · **Consumers:** Lead Artist (textures, models), Lead Game Designer (dome look; layout is NOT changed here), Technical Lead (block shapes) · **Status:** findings and design cues. No assets changed.
**Method (same as `reference-audit.md`):** downloaded credited images from allowed hosts, **opened and looked at every image before describing it**, rejected the ones that did not show what the search said. Images: `docs/art/references/habitat/` (about 1000 px, internal reference only, not shipped). Compared against `tools/screenshots/out/20261127-223929/life_1_starting_dome_from_outside.png` (looked at: a flat grey box, 4 courses of identical plate texture, flat roof, one window group, one small glass patch on the roof, a small stub at the west end).
**Tags:** **[V]** = seen in an image I looked at. **[NV]** = not seen (from text, memory or inference); treat as unverified. "Estimate" = my reading of a photo, not a measurement. **Colours are described by eye, not measured** (images carry their own white balance; the earlier audit measured hex values only for true-colour PIA frames).

## 0. Access report (what did and did not open)

| Host | Result |
|---|---|
| `www.nasa.gov` (pages and `wp-content/uploads` images) | Opened with curl through the proxy. WebFetch could not resolve the host; curl worked. A few URLs I guessed returned 404 (MOXIE science page, ECLSS page); I used the real ones that search returned. |
| `commons.wikimedia.org` API and `upload.wikimedia.org` | Opened, but **rate-limited hard** (HTTP 429 "robot policy"). Worked with 8 to 15 s delays and retries. Several lookups failed and were retried. |
| `photojournal.jpl.nasa.gov` | Opened (PIA24201 caption read). Images for MOXIE were already in the repo. |
| `images-api.nasa.gov` (NASA Image Library search) | **Blocked (HTTP 403, not on the allowed list).** Not worked around. Result: I could not search the NASA library for a photo of the ISS Water Recovery System in flight; I used Commons copies of NASA photos instead. |
| `science.nasa.gov` MOXIE/instrument page | **Not opened by me.** MOXIE size and rate figures below come from a web-search summary of that page plus the PIA captions already in the repo; marked accordingly. |
| `www.jpl.nasa.gov` | Not tried (known to block bots). |
| **Not found** | A photo of a **regolith-covered or buried** habitat concept; a **suitport** photo; a clean exterior photo of the Quest **high-pressure gas tanks** (they sit outside the airlock). See section 4 and section 7. |

## 1. Images kept (13 files, all looked at)

All NASA images are US-government works (NASA media guidelines: generally not copyrighted; check before use outside this repo). The artist credits are kept because the concept art was made by contractors for NASA.

| # | File (in `habitat/`) | What it shows (checked by eye) | Source URL | Credit and licence |
|---|---|---|---|---|
| 1 | `PIA24201.jpg` | MOXIE engineering twin, gold box with 3 big face panels, X-rib end face, white tubes, silver fittings | photojournal.jpl.nasa.gov/catalog/PIA24201 | NASA/JPL-Caltech. NASA media, generally not copyrighted. |
| 2 | `PIA24203.jpg` | MOXIE top plate installed in Perseverance: gold plate, silver fittings, white tubes with cable ties, Kapton-wrapped cables | photojournal.jpl.nasa.gov/catalog/PIA24203 | NASA/JPL-Caltech. Same. |
| 3 | `ice_home_crew_arrives.jpg` | Mars Ice Home concept: translucent ribbed dome on a dark base, a **white box module with a round-cornered hatch, louvre vents and NASA logos**, ramp, two suited crew in orange, rover | nasa.gov/wp-content/uploads/2016/12/mih_fig-3_cloudsao-search_crewarrives.jpg (on the 3D-Printed Habitat Challenge page) | NASA, Clouds AO, SEArch (the credit NASA gives). Artist's rendering. NASA media. |
| 4 | `marsha_ai_spacefactory.jpg` | Tall dark ovoid habitat with fine horizontal print layers, slim lit windows, glowing dome cap; a suited person for scale; dust haze. (NASA's page alt text says "zopherus", but the file name says AI SpaceFactory and the shape matches MARSHA; **attribution is from the file name, not verified**) | nasa.gov/wp-content/uploads/2019/04/ai-spacefactory-mars-habitat-exterior-dust_storm_-_small.jpg | NASA 3D-Printed Habitat Challenge page. Artist's rendering; credit to the team (AI SpaceFactory) assumed, not stated in the caption I read. |
| 5 | `apis_cor_printed_wall_porthole.jpg` | A 3D-printed cylinder wall of **layered ridges** with a rough round **porthole opening** and a dark round window frame; robot arms around it | nasa.gov/wp-content/uploads/2019/02/search_apis_cor_level_2_2.jpg | NASA caption alt text: team SEArch+/Apis Cor structure being prepared for hydrostatic leak testing. This is a **test structure on Earth**, not a concept. NASA media. |
| 6 | `chapea_sandbox_360.jpg` | CHAPEA habitat interior: **white vaulted ceiling with rib lines**, red sand floor, printed red-brown wall with stepped top, small wall devices. 360-degree (stretched) view | nasa.gov/wp-content/uploads/2023/03/chapea-jsc2022e094560-highres.jpg (on NASA "Analogs and Mockups") | NASA JSC photo (file id jsc2022e094560). NASA media. |
| 7 | `beam_on_node3.jpg` | **Real hull skin**: Tranquility module (silver-white panels with bolt rows, rails) next to **BEAM** (white padded fabric with tan straps and round fittings), seen from outside | nasa.gov/wp-content/uploads/2023/03/beam-iss067e214089.jpg (NASA BEAM page) | NASA ISS photo iss067e214089. NASA media. |
| 8 | `iss_wpa_rack_peake.jpg` | **ISS water-system racks, closed**: rows of off-white rack panels with latches and cut-outs, silver hoses, blue handrails, a white "Condensate" bag, a red cross label | upload.wikimedia.org (File:ISS-47 Timothy Peake works on the Water Processor Assembly in the Destiny lab.jpg) | NASA/ESA, public domain (Commons licence field "Public domain"; original on NASA's Flickr). |
| 9 | `iss_wrs_rack_open_fincke.jpg` | WRS rack **swung out**: a dark brown-black panel with round silver studs, grey frame, blue handrails, green circuit card, white bag, green braided hose | upload.wikimedia.org (File:ISS-18 Michael Fincke performs a leak check on the Water Recovery System in the Destiny lab.jpg) | NASA, public domain (Commons field). |
| 10 | `nors_o2_tank_rubins.jpg` | A **Nitrogen/Oxygen Recharge System (NORS) tank** wrapped in white with straps, **yellow-black warning labels and a green "OXYGEN" label**, in the Quest area with an orange duct and a round hatch ring at left | upload.wikimedia.org (File:ISS-49 Kate Rubins works with an oxygen tank in the Quest airlock.jpg) | NASA, 2016-09-14, public domain (Commons field). |
| 11 | `quest_airlock_hatch_hurley.jpg` | **Airlock hatch**, closed: large grey aluminium door with **rounded corners**, a round **window with a ring and a coloured ring indicator**, a crank handle, white instruction placards ("Opening hatch", "Closing hatch"), a **yellow-green seal frame**, mesh vent grilles beside it | upload.wikimedia.org (File:ISS 63 Hurley in Quest airlock.jpg; NASA id iss063e033522) | NASA, 2020-06-26, public domain (Commons field). Which of the airlock's hatches this is, I did not verify. |
| 12 | `mdrs_hab_greenhab.jpg` | **Mars Desert Research Station Hab**: tall white cylinder of vertical panels, cream trim at top and bottom, round portholes, a rounded door with its own porthole, **steps**, dark buttress struts, a dish and wind mast on the roof, a **silver arched Greenhab** beside it | upload.wikimedia.org (File:Mars Desert Research Station.jpg) | **CC BY 2.5, McKay Salisbury (2004).** Credit required if ever reused. |
| 13 | `nasa_marshab_radiation_shelter_concept.jpg` | NASA concept (2002): a white panelled cylinder lander with **round portholes**, and a white **inflatable shelter** with ribs and ground struts. Commons description: "outpost designed to protect astronauts from radiation" | upload.wikimedia.org (File:19310main marshab.jpg; original nasa.gov/images/content/19310main_marshab.jpg) | Frassanito and Associates for NASA, 2002. Public domain (Commons field). |

**Rejected after looking:** two photos that the page listings made look relevant but were not: a Starliner crew photo (`jsc2022e085783-3`) and an Orion capsule water-recovery training photo (`jsc2022e045105orig`), both found on the CHAPEA/Analogs page; an Orion cabin interior (`jsc2022e044165`); a Penn State and SEArch+/Apis Cor tower concept (`p3l4_search_apis_cor`) was a good image but redundant (tall lattice tower; kept out to stay under 13 files); an ISS "brine processor" photo (`bpa-filter-repair`) shows a cylinder and a blue rail but no rack, so it can't support a reclaimer claim.

## 2. Facts with sources (real science vs our scale)

| Fact | Value | Source and status |
|---|---|---|
| MOXIE box size | about 23.9 x 23.9 x 30.9 cm, 17.1 kg on Earth | NASA Perseverance instrument page, **from a search summary; page not opened by me.** Repo `references/README.md` (earlier session) says "about 24 x 24 x 31 cm". |
| MOXIE output | designed for up to 10 g O2/h; NASA reports a best of about 12 g/h at 98 percent purity or better; total 122 g over the mission (last run 7 Aug 2023) | NASA MOXIE pages **via search summary; not opened.** |
| MOXIE inside | "gold-plated aluminum box"; the heart is the Solid Oxide Electrolysis unit (SOXE), which splits CO2 into CO and O2 | **[V] read in the PIA24201 caption** (opened) |
| MOXIE at our scale | 24 x 24 x 31 cm is **3.8 x 3.8 x 5 px**. A one-block maker (16 px = 1 m) is **about 3 times the real linear size** | calc. Honest note: "a crew-scale maker would be bigger than MOXIE; MOXIE makes about the oxygen a small dog breathes" (NASA's own comparison, via summary) |
| BEAM expanded | about 4.0 m long, 3.2 m diameter, 565 cubic feet (about 16 m3) habitable volume | NASA BEAM fact sheet **via search summary; not opened** |
| ISS water recovery | NASA material says about 90 percent (older) and about 98 percent (recent, with the brine processor); racks are described in Destiny and Tranquility/Node 3 in different documents | NASA ECLSS fact sheets and ICES papers **via search; not opened.** "Where the racks are" differs between sources, so do not put a module name on our reclaimer. |
| Ice Home | inflatable torus (inner-tube shape) inside a water-ice shell; ice shields from radiation and passes some light | NASA Langley feature **via search summary; not opened.** Dimensions **not obtained.** |
| 3D-Printed Habitat Challenge | MARSHA (AI SpaceFactory) won the 2019 final; a tall four-storey cylinder-like design | NASA challenge pages **via search summary.** Details of height **[NV]** |
| MDRS Hab size | **[NV]** (I recall a two-storey cylinder of about 8 m diameter; **not checked**) | do not use a number until a source is opened |
| CHAPEA | about 1,700 square feet (about 158 m2) printed habitat, four crew quarters, galley, medical station | NASA pages **via search summary** |
| Scale rule | 1 block = 1 m = 16 px; person 1.8 m = about 29 px | `AGENTS.md` section 2 |

## 3. What real habitats look like from outside

| Type | Real examples (seen) | What it looks like | Why designers choose it |
|---|---|---|---|
| **Dome** | Ice Home [V] (#3) | Rounded, ribbed shell, translucent; the **crew enters through a separate white box module with a hatch**, so the dome is not the door | Strong shape under pressure (round shapes hold air), ice shell for shielding (**[NV]** thickness) |
| **Upright cylinder** | MDRS Hab [V] (#12), MARSHA [V] (#4), printed test cylinder [V] (#5), NASA 2002 lander [V] (#13) | Cylinder with a **flat or shallow-dome cap**, round portholes, one door at the base with **steps**, roof full of antennas | Cylinders are easy to build and to seal; vertical stacking saves footprint |
| **Horizontal cylinder / inflatable** | ISS modules and BEAM [V] (#7), inflatable shelter [V] (#13) | Long tube; BEAM is **soft, white, padded with straps**, docked to a hard panelled module | Packs small, expands on site |
| **Buried or regolith-covered** | **No image obtained.** The NASA 2002 caption says the shelter is made of radiation-shielding materials [V caption, #13]; the Ice Home uses an ice shell [V]. Regolith berms or burying are widely proposed in the literature | Would look like a mound with an entry and a vent | Radiation: Mars has no global magnetic field and a thin atmosphere (see `docs/science/` radiation notes). **[NV in an image]** |

**What this implies for our 23 x 16 flat box (layout is NOT changed).** From the screenshot, three things make it read as "grey box" rather than "habitat": uniform plate on all four courses, a perfectly flat roof, and no sign of entry, plant or equipment outside. Every real example above has **a visible entry with a step or ramp, round portholes, an equipment skin on the roof (dish, mast, vents), and a base or top trim band**. None is a flat unbroken rectangle.

| Question | Real references suggest | Cost and limit |
|---|---|---|
| Roof shape | Domed or capped (Ice Home, MDRS, MARSHA); **or** a flat roof is fine if it carries equipment (MDRS roof dish and mast [V]; NASA 2002 lander flat top [V]) | A stepped low-pitch roof (stair or slab blocks above y4) does not move anything inside. Cheapest honest option: keep flat, add a **raised perimeter rim (cream trim band)**, roof panel seams, a vent and one dish. Needs the Lead Game Designer and Technical Lead to agree, because the interior ceiling stays at y4 |
| Entry | A separate airlock module sticking out with a ramp or steps (Ice Home box module [V], MDRS door with steps [V]) | Our airlock A is at x11, z15 on the south wall (dome-layout section 2). A **1 to 2 block outward stub** with steps is **outside the accepted footprint**, so it is a handoff, not something I changed |
| Scale vs people | A suited person is about 1.8 m = 29 px; in #3 the dome is roughly 4 to 5 times a person's height [V, visual estimate]; MDRS Hab is about 2 to 3 persons tall [estimate]. Our walls are 4 blocks (about 2.2 players) | Walls of 4 m are in the real MDRS range for one storey; it is the **flat plain wall** that looks wrong |
| Windows | Small **round portholes** on MDRS and the 2002 concept [V]; ISS hatch has a round window [V]. Our long square glass strip is not what real habitats use | Round portholes can be done in a 16 px texture (see 4.2) but cannot be round in 3D without custom models |
| Regolith shielding | Concept-level only [NV in image] | A **dirt/regolith berm against the lower walls** (ground blocks) is a placement change, not a layout change. Recommend the Game Designer decide; also teaches radiation shielding (goal link for the Lead Instructional Designer) |

## 4. Reference by block: what to borrow

Each cue: **[V]** seen, with the image number. Pixel notes assume a 16 x 16 texture.

### 4.1 Hull plate (and wall)

| Cue | Tag | How a 16 px block shows it |
|---|---|---|
| Real hull skin is **rectangular panels with a row of bolts around the edge, raised rails and strips**, off-white to silver, not one flat sheet (ISS Tranquility panels, #7) | [V] #7 | 1 px darker seam on two sides, **single-pixel bolts at 4 corners and mid-edge** (we already have bolts; make the panel read as **2 x 1 or 1 x 2 blocks** by letting seams run across a pair of blocks) |
| **Vertical panel seams and a cream trim band at the base and top rim** (MDRS, #12) | [V] #12 | Two special courses: bottom course and top course use a **trim texture** (lighter cream, 3 px band). Breaks the "4 identical rows" look |
| **White, slightly warm** paint on habitats; MOXIE is gold; ISS hull silver-grey | [V] #12, #1, #7 | Use off-white (not blue-grey) for the main hull; our screenshot plate reads blue-grey. Colour by eye only |
| **Soft rounded corners** on doors and hatches; panels on ISS cut-outs have rounded corners (#8, #11) | [V] #8, #11 | Round the 4 corners of the door and window textures (1 px transparent or dark corner). Plates themselves stay square |
| **Layered print ridges** if the hull is a printed regolith shell (#4, #5) | [V] #4 #5 | An alternative "printed wall" texture: 1 px horizontal lines every 2 px. A different look for Hab B annex |
| **Yellow-green seal frame** around a hatch (#11) | [V] #11 | A 1 px yellow-green outline on the airlock door frame blocks only |

### 4.2 Hatch and airlock door

| Cue | Tag | How |
|---|---|---|
| Hatch is a **big rounded-corner rectangle in raw grey aluminium** (not painted), roughly 1 m wide and 1.5 to 2 m tall [estimate from #11, person for scale; **no dimension source**] | [V] #11 | Door is 1 x 2 blocks already: use **grey-silver, brushed texture**, rounded top and bottom corners |
| **Round window** with a ring, set high (one quarter to one third of the door width), plus a **coloured sector ring** (green/red/yellow arcs) used as a pressure/latch indicator | [V] #11 | 6 px circle with a 1 px dark rim at upper door tile; **3 px of green and red arcs** below it; this doubles as our "sealed / not sealed" cue (design choice, not a real convention claim) |
| **Crank handle** and **white placards** ("Opening hatch" and "Closing hatch") beside the window | [V] #11 | A 4 px lever pixel on one side, a 5 x 3 white rectangle for the placard |
| Door has **steps** outside on MDRS (#12); Ice Home has a **ramp** (#3) | [V] | Outer door: put a one-block step or ramp (outside layout, handoff) |
| Airlock chamber interior: **white padded walls, handrails, control panel with switches and a ring of cables** (seen through the Quest hatch window in a photo I looked at but did not keep: `ISS-36 EVA-2 d`) | [V, not kept] | Interior airlock walls white; a small switch panel block next to the inner door |
| **Suitports** (suit mounted in the wall) | [NV] no image found | Do not draw one. If wanted, needs a new source |
| Cracked plate (breach) | [NV] no real reference | Use the same plate with a dark diagonal crack and 2 to 3 px of bright rim; mark "invented" per `DESIGN.md` Fantasy and Authenticity |

### 4.3 Oxygen maker (MOXIE-like)

All from #1 and #2 [V], plus the 2026-11-26 audit boards.

| Cue | Tag | Notes |
|---|---|---|
| **Gold box**, three big faces each a **grid of pocketed square panels (3 x 4)** separated by deep thin grooves; panels are slightly shiny | [V] #1 | Gold ramp of 4 to 5 tones. The grid reads at 16 px as 3 x 3 or 4 x 4 cells with 1 px dark grooves |
| One end face has a **diagonal X-rib pattern** | [V] #1 | Side texture: X pattern in 1 px lines, gold on dark gold |
| **Silver fittings** (hex nuts, short cylinders) at the base, **white translucent tubes** looping out | [V] #1, #2 | The block model gets **2 thin tubes** (1 px thick, white) looping on one side and 2 silver 2 px nubs low. Needs the thin-element approach from the earlier audit |
| **Top plate** with dark Kapton-orange cable wraps, cable ties, a lot of small bolts | [V] #2 | Top face: gold with 3 to 4 orange-brown dots and a silver connector rectangle |
| Mounted on a **copper-coloured base plate** | [V] #1 | 2 px copper strip at the bottom edge |
| Size | 3.8 x 3.8 x 5 px at real scale; our block is about 3 times too big. Fine at gameplay scale, say so in the journal | [calc] |
| Our current maker is "two grey tanks" | already flagged in `reference-audit.md` finding 1 | Rebuild as above |

### 4.4 Water reclaimer (ISS rack look)

| Cue | Tag | How |
|---|---|---|
| ISS racks are **flush off-white panel doors in a grid of rack bays**, each with latches and small cut-outs, **blue handrails** along the rack edges (#8) | [V] #8 | Reclaimer front: 2 stacked off-white panels with a 1 px seam, 2 latch pixels, a **blue 1 px rail on one edge** |
| Behind the panels: **silver and dark-grey hoses, braided green hose, orange tubing** (#8, #9) | [V] #8 #9 | One exposed hose loop on the side (silver 1 px), one green braided hose accent |
| Open rack back is **dark brown-black with a regular grid of round silver studs** (#9) | [V] #9 | Back or service-side texture; this is a good "opened for repair" state for goal C2 (failure and repair) |
| **Warning/label stickers** in red and yellow, and a **"Condensate" bag** (#8) | [V] #8 | One 3 x 5 px red-cross and a yellow tag. Names the water source honestly (cabin condensate and urine are real inputs; sources via search, **not opened**) |
| Real systems recycle about 90 to 98 percent of water | [NV, opened source needed] | Put the number in the journal only after the SME opens a NASA page; use "most of the water" until then |

### 4.5 Oxygen tank (wall tank) and fill point

| Cue | Tag | How |
|---|---|---|
| Real NORS tank is a **cylinder wrapped in white insulation with straps**, **yellow-black hazard labels** and a **green oxygen label** (#10) | [V] #10 | Tank: white-grey body, **a 2 px yellow-black diagonal stripe label**, a **green 3 x 2 px "O2" tag**. Add 2 pale straps (1 px) |
| Tank sits on a **dark base ring**, with silver valve parts at the bottom (#10) | [V] #10 | 2 px dark ring at the base, one silver nub |
| Gas-colour conventions on Earth (green = oxygen in the US) | [NV] I did not source it | Use the green only because the ISS photo's tag is green; do not teach a rule |
| Quest airlock's own high-pressure tanks (outside) | [NV] no image kept | Do not copy. |

### 4.6 Hab B annex (greenhouse) and glass

| Cue | Tag | How |
|---|---|---|
| MDRS **Greenhab**: a **silver arched tunnel** of ribbed translucent panels with a small white door at one end (#12) | [V] #12 | Annex roof as **arched rib texture** (translucent, light grey ribs every 2 blocks). The accepted layout says "glass roof"; ribs are a texture on the existing glass |
| Inflatable segments with **ribs and ground struts** (#13) | [V] #13 | Alternative "soft annex" look; a good link to BEAM (#7) |
| Dome windows **are ribbed grids** (Ice Home dome, #3) | [V] #3 | Glass texture with a light grid of 1 px ribs, so it does not look like vanilla glass |

## 5. What a Minecraft 16 px block can and cannot show

| Real feature | Can show | Cannot show (say so) |
|---|---|---|
| Panel seams, bolts, trim bands | Yes, textures | None |
| Round window and indicator ring | Yes, a 6 px circle | A truly round hole (needs a custom model) |
| Thin tubes and fittings (MOXIE) | Yes with a few 1 px model elements | Tube bends smoother than 1 px steps |
| Rounded corners of a door | Yes, corner pixels | 3D rounding |
| Domes | A stepped dome with stair blocks, about 3 steps | A smooth sphere |
| MOXIE real size (24 cm) | No (3.8 px) | Must be one block; note it |
| Gold shine | Yes, 4 to 5 tone ramp with highlight line | Specular reflection |

## 6. Misconceptions to avoid (SME check)

| Risk | Fix |
|---|---|
| "The oxygen maker is a tank of oxygen" | The real one is a gold box that **makes** oxygen from CO2; the tank only **stores** it. The art now separates the two (maker vs tank) |
| "The dome is a greenhouse" | Keep the annex as the greenhouse; the main shell looks like opaque hull |
| "Habitats are shiny clean metal" | MDRS paint, ISS panels have tape, stickers, hoses. Add a little wear (dust gradual, as decided) |
| "The reclaimer makes water from nothing" | It recycles waste water (condensate, urine, hygiene water). The "Condensate" label is a good honest cue |
| "The ice home is buried" | It is an **ice shell over an inflatable**, not regolith |

## 7. Gaps (what to find next)

1. **A real regolith-covered or buried habitat image** (none found; Commons category "Mars habitats" lists none that I could confirm). Candidates: NASA Langley/SEArch+ imagery, NASA "Mars Design Reference Architecture 5.0" figures (**not opened**).
2. **Suitport and rear-entry suit** photos (NASA Z-series suits and suitport tests): not found.
3. **High-pressure gas tanks outside Quest**: not found as a clear photo.
4. **A proper water-recovery rack photo through NASA's Image Library**: the API is blocked for this session, so the two Commons copies are the best here. The full-rack front view (WRS-1 / WRS-2 racks closed) is not among them.
5. MDRS and Ice Home dimensions (need an opened source).

## 8. Needed from the team (short list)

| To | Ask |
|---|---|
| Lead Artist | Use sections 4.1 to 4.6 as the brief for hull plate, door, tank, maker, reclaimer textures; keep the "no new 3D outside the 16 px rules" limit |
| Lead Game Designer | Decide on roof rim, airlock stub with steps and a regolith berm (outside accepted layout), see section 3 |
| Lead Instructional Designer | Radiation-shielding link (regolith berm / ice shell) for the Phase 1 story and goal check |

## 9. Addendum 2026-11-28: bunk, crew quarters and stowage (SME)

Ten new images in `docs/art/references/habitat/` (list and credits in that folder's README). All were opened and looked at. [V] = seen in a photo or stated in a NASA caption I read. [NV] = my estimate by eye, or a secondary source I could not open. Sizes by eye are scaled from a person and are rough. `ntrs.nasa.gov` is blocked in this session, so no NASA technical paper dimensions were opened.

### 9.1 Bunk and sleeping area cues

| # | Cue | Real thing (file) | Mark | Minecraft block idea (1 m, 16 px) |
|---|---|---|---|---|
| B1 | Sleeping bag is a **quilted zip bag** in a muted colour: light green (CQ), olive-grey with navy bands (Harmony), tan with white piping (Skylab). Never blue in the photos | `iss_cq_cristoforetti...`, `iss_sleeping_bag_tethered...`, `skylab_sleep...` | [V] | Bag top texture: 3 to 4 tones of one muted green or olive, 1 px zip line down the middle, 2 px quilt grid |
| B2 | The bag is **tied to the wall** (white cords to blue handrails; NASA caption: "strap the bag to the wall to prevent free floating") | `iss_sleeping_bag_tethered...`; caption of Commons file for CQ | [V] | A 1 px white cord and a small loop on the wall-side face. This is a microgravity cue; a bunk on Mars with gravity does not need it. Use only as flavour |
| B3 | The CQ is a **small closed compartment**: off-white quilted padded walls with beige Velcro squares, a cloth door with beige tie loops, a small light | `iss_cq_cristoforetti...` | [V] | Wall panel texture: off-white with a 4 px quilt grid and a few 3 x 3 px beige squares. Optional curtain block |
| B4 | Compartment width is about one body plus a hand: roughly 0.8 to 1 m wide, 2 m long | same photo | [NV] (by eye) | Matches a 1 x 2 block bunk |
| B5 | **Personal items stuck on the wall** with Velcro: tablet, pouch, small cards, nuts in a pouch | `iss_cq_cristoforetti...` | [V] | 2 to 3 small coloured pixel clusters on the wall panel; this is the "personal touches" cue |
| B6 | **CHAPEA** (a ground habitat, with gravity): single bed with **black frame and grey quilted duvet**, navy chair, white door with steel lever handle, **white linear LED strip** on the ceiling, **layered 3D-printed walls in terracotta/peach**, grey speckled floor | `chapea_crew_quarters_rooms` | [V] (NASA caption: 1,700 sq ft habitat, four individual living quarters) | The closest real match to our case. Bed: dark frame, light grey top, 1 px pillow. Wall: horizontal 2 px ridge lines in peach |
| B7 | Zvezda sleep station: **cream walls**, a door panel with silver hinge and black strap latches, blue stowage bags at the side | `zvezda_sleep_station_budarin` | [V] | Cream, not white, is a valid hull colour for the Russian side |
| B8 | Skylab bunk: **aluminium tube frame**, taut bag, mesh wall panel, light under a ceiling lip, a map on the wall | `skylab_sleep_compartment_bean` | [V] | 1 px light grey tube along the bed edge; a small map pixel patch on the wall |
| B9 | Colour summary by eye: bag green or olive or tan; walls off-white or cream; accents blue (handrails), white (cords), navy (bands) | all of the above | [V] by eye | Palette for bunk: off-white `#E6E2D6`, muted green `#8FA878`, olive `#6B7157`, navy `#2E3A55`. Hex values are my eye estimates, [NV] |

### 9.2 Stowage cues

| # | Cue | Real thing (file) | Mark | Minecraft block idea |
|---|---|---|---|---|
| S1 | **Soft cargo transfer bag (CTB)**: off-white woven cloth, a long zipper edge, white Velcro tabs, a **yellow label card** with a barcode, a printed black serial number | `iss_cargo_transfer_bag_ohara` | [V] (NASA caption says "cargo transfer bag") | A soft-looking pale beige block with a 1 px zip line, two white tabs and a 4 x 3 px yellow card |
| S2 | CTB size: one source says a single CTB is about 0.50 x 0.42 x 0.25 m, with half, double and triple sizes (JSC paper, found by web search, **not opened**). The bag in the photo looks larger (about 1 m by 0.6 m), so it is probably a multiple | same photo | [NV] | One block (1 m) is bigger than a single CTB; show as a stack of 2 to 3 bags, or a half-height slab |
| S3 | **Hard lockers and racks in the MPLM**: grey aluminium boxes in a grid, each with a small orange or yellow label tag and open cut-outs; soft white bags on top held by **tan straps with metal buckles** | `mplm_leonardo_stowage_racks` | [V] | Grey door, 3 x 2 px orange tag, two tan 1 px straps over a pale top with a 2 px silver buckle |
| S4 | **Stacked boxes with coloured tape bands** (green, yellow, red) and blue-white labels, held by criss-cross white cords against blue rails. NASA caption calls the grey stacks **food canisters** (Unity, not CTBs) | `iss_unity_stowage_food_canisters` | [V] caption | Good "supply stack" decoration: 1 px colour band round the box, 1 px diagonal cord |
| S5 | Rack-door texture seen in the Artist's reference (`iss_wpa_rack_peake.jpg`): off-white doors with latches and cut-outs | existing image | [V] | already used |
| S6 | Clear plastic pocket bags with paper labels stuck on walls | `iss_sleeping_bag_tethered...` | [V] | 1 px light-blue outline with a white 3 x 2 px card |
| S7 | Real racks have **no skids or wooden feet**. Skids belong to ground shipping | all stowage photos | [V] (none visible) | If kept, the skid is a design choice and should not be called ISS |

### 9.3 Compared with the Artist's boards (14 and 15)

I looked at the two boards at reduced size (the PNGs are very wide), so fine pixel detail is [NV].

| Item | Matches the real thing | Does not match | Change |
|---|---|---|---|
| Bunk shape (14) | Low 1 x 2 bunk, mattress about 0.56 m, like CHAPEA's low single beds (B6). Drawers under the bed: **not seen** in any photo | Real bunks have no "drawers with latches" in the photos; the real CQ is enclosed | Shape is acceptable. Either keep the drawers as "under-bed storage" (plausible but [NV]) or use a dark plain frame like CHAPEA. Optional: add a wall-side quilted off-white panel (B3) |
| Bunk colour (14) | Off-white frame, white pillow, strap and cord ideas are real (B2) | The bag is **blue**; no photo shows a blue bag. The strap is **orange**; real straps are white or navy | Change the bag to muted green or olive (B1). Use a white or navy strap. Keep a small orange accent only on a label |
| Bunk labels | Not shown | Real compartments carry small cards and Velcro items (B5) | Add 2 or 3 small pixel items on the head board |
| Crate shape (15) | Off-white door with a latch and a label card matches rack doors (S3, S5) | A cube with a skid looks like a shipping crate, not a rack door or soft bag (S7) | Drop the skid or lower it to a 1 px edge. Make it read as a locker: 2 by 2 door grid |
| Crate colour (15) | Off-white and grey are right | Blue label band: real tags are **yellow or orange** on white (S1, S3). Blue is for handrails | Make the label card yellow or orange; use blue only as a 1 px rail |
| Crate variety | One kind of box | Real stowage is a mix of hard lockers and soft strapped bags (S3) | Add a second variant: a soft pale bag with two tan straps and buckle (S3), for visual variety. This is the most "real" cue we have |

What to tell players honestly (journal note): "The bunk is a ground version. On the real ISS, crew sleep in small closed rooms and tie their sleeping bags to the wall so they do not float. On Mars there is gravity, so a bed works."

### 9.4 Suitport and buried habitat (earlier gaps)

| Gap | Result |
|---|---|
| Suitport | Two photos kept. Z-1 suit at a suitport (`suitport_z1_bresnik`): a **white rectangular frame, steel hull plate with a ring of bolts, red rubber seal**, a quilted white back pad as the suit's hatch [V]. Field photo: **white rover with rivet lines, an orange hand lever, copper-coloured handrail**; the suits are backed against it [V]. Use: a hull door variant with a red seal line and bolt ring [V] |
| Buried or regolith-covered habitat | **Still not found as a real photo.** Only concept art exists (already in the folder). Mark any berm cue as concept, not photo [NV] |

### 9.5 Remaining gaps
1. Exact CQ dimensions and CTB sizes from a NASA paper (NTRS blocked; ask the owner to allow `ntrs.nasa.gov`, or the Artist may use the by-eye sizes above).
2. A clear straight-on photo of a closed MPLM or ISS stowage locker door with a label card.
3. Real buried habitat imagery.
