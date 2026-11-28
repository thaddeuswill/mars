# Exploration finds: science review (rovers and landers, minerals, biosignatures, habitability)

**Date:** 2026-11-28 · **Owner:** Subject Matter Expert · **For:** Lead Game Designer (`docs/design/exploration-finds.md`), Lead Instructional Designer, Lead Artist, Producer
**Trigger:** BACKLOG owner idea (2026-11-28): finding rovers, rare metals, things that help find out about life, and things that help make Mars habitable.
**Scope:** science only. How these fit the phases, goals and scope is the Game Designer's call. Nothing here changes a goal ID, a gate or a number in the pack.

## 0. How to read this

### Tags

| Tag | Meaning |
|---|---|
| **opened-NASA** | I opened the NASA page and read the statement myself this session |
| **opened-wiki** | I read the Wikipedia article text (it quotes primary sources, but is not one) |
| **search** | Seen only in a web-search summary; I could not open the page |
| **memory** | General knowledge, not re-checked this session. **Treat as unverified.** Check before it reaches a student |
| **invented** | A game or design choice, not a fact |

### What opened and what did not

| Opened | Could not open (blocked, 403/000, or throttled) |
|---|---|
| science.nasa.gov mission pages: Perseverance, Curiosity, Spirit and Opportunity, InSight, Pathfinder, Phoenix, Viking 1; science.nasa.gov/mars/facts; www.nasa.gov news release "Mars Terraforming Not Possible Using Present-Day Technology" (2018) | mars.nasa.gov (no connection); www.jpl.nasa.gov (403 in earlier rounds, only seen via search); lasp.colorado.edu and nature.com (the Jakosky and Edwards paper itself); phys.org; svs.gsfc.nasa.gov; astrobiology.nasa.gov |
| en.wikipedia.org (text via API, throttled, so only part came through): Spirit, Opportunity, Phoenix, Viking 2, Mars 2, Mars 6, Beagle 2, Schiaparelli EDM, Mars Polar Lander, Mars Pathfinder, Ingenuity, Cheyava Falls, Sapphire Canyon, Water on Mars, Life on Mars, Methane on Mars, Terraforming of Mars, Perseverance, InSight | Wikipedia pages that returned HTTP 429 and gave no text: **Sojourner, Mars 3, Zhurong, Curiosity, Viking 1, Perchlorate, MOXIE, Hematite, Deep Space 2, Mars Climate Orbiter**. Facts for these come from NASA pages or search summaries (tagged) |
| `docs/art/references/` (8 NASA photos; checked README only) | upload.wikimedia.org (not needed; no image work in this task) |

The 2018 Jakosky and Edwards paper itself could **not** be opened. Its conclusions below come from NASA's own release about it (opened-NASA), which is the better source for a student-facing claim anyway.

---

## A. Real rovers and landers: "wreck / monument" discoveries

### A.1 Honest framing for the game (read first)

1. The game's planet is `mars:planet_1`, a game world, not the real Mars. Real hardware sits at real sites thousands of km apart. Putting several of them within walking distance is a **game compression**. Label every placed object a **replica** or **monument**, never "the real Spirit".
2. Placing real hardware as a "wreck" is a **fictional-placement element**. It must pass the six rules in `DESIGN.md` (Fantasy and Authenticity) and get a row in the audit table. My note for that audit: the hardware is real and its description is true; only its location in the game world is invented.
3. Do **not** mix these with the fictional dormant fauna. A learner who finds Spirit and then a Thawling may think both are "things people found on Mars". Keep the visual and journal language apart: hardware = "Real mission", fauna = "Fiction".
4. Crash sites (Beagle 2, Schiaparelli, Mars 2, Mars Polar Lander) are real failures. Treat the people behind them with respect: "what went wrong and what was learned", not jokes. Engineering lessons map to **C2 Design, test, improve** (diagnose and fix).

### A.2 Table of real missions

Status words: **dead** = ended, no signal; **lost** = never confirmed working on the surface; **crashed**; **retired** = completed or ended by choice.

| Mission | Landed (where) | Years active | Status and cause | Size / mass | What it found or showed | Tags |
|---|---|---|---|---|---|---|
| **Mars 2 lander** (USSR) | Crashed, Nov 1971 | none | **Crashed.** First human-made object to reach the Mars surface; landing system failed | Lander 1,210 kg (whole lander, launch mass) | A global dust storm hid the surface when it arrived; the landers had to be released without a plan change | opened-wiki (Mars 2). Exact crash date: memory |
| **Mars 3 lander** (USSR) | Dec 1971, soft landing | seconds | **Lost.** NASA's Viking page: first survivable landing, "contact was lost seconds after it touched down" | Same design as Mars 2 (1,210 kg) | First soft landing; no useful data. About 14.5 s of signal is **search**-only and its cause is debated: do not state a cause | opened-NASA (Viking 1 page); 14.5 s: search/unverified |
| **Mars 6 lander** (USSR) | Mar 1974 | minutes | **Lost** during descent; carried a barometer, thermometer and mass spectrometer | n/a | Returned some data on the way down; the landing failed | opened-wiki (Mars 6; confirms payload). "Data on the way down": memory |
| **Viking 1** (NASA) | 20 Jul 1976, Chryse Planitia | to 11 Nov 1982 | **Dead.** Ended when a faulty command interrupted communications; later renamed Thomas Mutch Memorial Station | Lander 978 kg; whole spacecraft 3,527 kg | First fully successful Mars landing. Three biology tests gave puzzling results; the organics test found **no** organic compounds at the detection limit | opened-NASA (Viking 1) |
| **Viking 2** (NASA) | 3 Sep 1976, Utopia Planitia | to 12 Apr 1980 | **Dead.** Batteries failed after 1,281 sols | Same design as Viking 1 | Weather, photos, soil tests. Same biology puzzle | opened-wiki (Viking 2) |
| **Mars Polar Lander** (NASA) | Dec 1999, near the south pole | none | **Lost.** Most likely cause: engines shut off too early, so it hit the ground fast | 290 kg | Was meant to look for ice and climate records at the south pole. Carried a CD with a million children's names | opened-wiki (Mars Polar Lander) |
| **Mars Pathfinder and Sojourner** (NASA) | 4 Jul 1997, Ares Vallis | about 3 months | **Dead** (base station last session about 27 Sep 1997; end date of the rover itself differs by source) | Sojourner about 10.5 kg, 65 cm long, 48 cm wide | First rover on Mars. Landed in a dry flood channel with rounded boulders from an ancient flood; dust is magnetic (maghemite, an iron oxide) | opened-NASA (Pathfinder; dust and flood text); size and last contact: **search**; "first rover on Mars": memory |
| **Beagle 2** (UK, ESA Mars Express) | Dec 2003, Isidis Planitia | none | **Lost, then found.** MRO images (announced Jan 2015) showed it landed but two of four solar panels did not open, blocking the antenna | Lander 33.2 kg at touchdown | A biology-search lander (rock samples to a mass spectrometer). Shows that "silent" can mean "landed but could not call home" | opened-wiki (Beagle 2); image dates: search |
| **Phoenix** (NASA) | 25 May 2008, Vastitas Borealis (Arctic plain) | to 2 Nov 2008 | **Dead.** Solar power ran out in the northern winter; lander declared dead after no contact up to May 2010 | Whole spacecraft 670 kg at launch (the lander alone is much lighter: **unverified**) | Dug to **water ice** just under the soil; found **perchlorate** salt in the soil; first photo of one spacecraft in the air taken by another (MRO) | opened-NASA (Phoenix); dead date: opened-wiki |
| **Spirit** (NASA) | Jan 2004, Gusev Crater | to 22 Mar 2010 | **Dead.** Stuck in soft sand May 2009; could not tilt its panels to the winter Sun | Planned for 90 sols, lasted about 20 times that; drove 7.7 km | Found signs of past water. NASA says Gusev was "a possible former lake". Opal-like silica at Home Plate: **memory** | opened-NASA (MER, 7.7 km, Gusev); stuck and end: opened-wiki. Mass about 185 kg: memory |
| **Opportunity** (NASA) | 25 Jan 2004, Meridiani Planum | to 10 Jun 2018 (5,111 sols) | **Dead.** A planet-wide dust storm cut sunlight; last contact 10 Jun 2018; mission declared complete 13 Feb 2019 after more than 1,000 signals | Drove 45.16 km (28.06 mi), the off-Earth driving record until Perseverance passed it | Minerals that form in water. **Heat Shield Rock**: the first meteorite identified on another planet, mostly iron and nickel. Planned for 90 sols, ran 57 times as long | opened-NASA (MER: dust storm, distance, 2018); opened-wiki (5,111 sols, Heat Shield Rock, dates); iron-nickel: search |
| **Curiosity** (NASA) | Aug 2012, Gale Crater | **active** | **Working.** Not a wreck. NASA says it reached 1 km of climb up Mount Sharp on 26 Aug 2026 | 899 kg on Earth, 337 kg weight on Mars. Car-sized, 2.1 m arm: NASA says "7 foot-long arm" | Found chemical and mineral evidence of past habitable environments. Found the **largest organic molecules seen on Mars** (in a rock called Cumberland); a 2026 study says non-biological sources considered could not fully account for them, but that is **not** a life claim. Found iron-nickel meteorites (Egg Rock; the big "Lebanon", 2 m) | opened-NASA (Curiosity); meteorites: search |
| **Perseverance** (NASA) | 18 Feb 2021, Jezero Crater | **active** | **Working.** Odometer 45.22 km on 11 Oct 2026, now the driving record holder | Car-sized (NASA). Mass: not read this session, **unverified** | Collected more than two dozen rock samples; found a delta, lake and river history; MOXIE made oxygen (Section C, D); potential biosignature in Cheyava Falls (Section C) | opened-NASA (Perseverance). Landing day: memory |
| **Ingenuity** (NASA helicopter) | Landed attached to Perseverance, Feb 2021 | flew 19 Apr 2021 to 2024 | **Retired.** 72 flights, first powered, controlled flight on another planet; planned for five | n/a | Flight is possible in air about 1% as thick as Earth's | opened-wiki (Ingenuity); end cause: memory (rotor damage in a landing, Jan 2024) |
| **InSight** (NASA) | 26 Nov 2018, Elysium Planitia | to Dec 2022 | **Dead.** Dust on its panels. NASA photographed it later "to study dust movement" | 360 kg; array 2.2 m across (see `dimensions.md`) | First seismometer on Mars: marsquakes, the size of the core (NASA says Mars's core radius is 1,500 to 2,100 km) | opened-NASA (InSight; Mars facts); end date: memory |
| **Schiaparelli EDM** (ESA) | 19 Oct 2016, Meridiani Planum | none | **Crashed.** Signal lost about one minute before touchdown; MRO imaged the crash site on 21 Oct 2016 (NASA, via Wikipedia) | n/a | A test of landing technology, not a science lander | opened-wiki (Schiaparelli EDM). Site name: memory |
| **Zhurong** (China, Tianwen-1) | May 2021, Utopia Planitia | to May 2022 | **Silent.** Went into planned winter hibernation May 2022 and has not woken. China's chief designer says dust may be blocking power; MRO images showed it had not moved | n/a | China's first Mars rover | search only (Space.com, Live Science, etc.). Treat as **unverified** until a primary source is read |

Missions I did **not** cover and left out on purpose: Mars 6/7 orbiters, Deep Space 2 microprobes, Mars Climate Orbiter (it never reached the surface, so it is not a surface discovery). Deep Space 2 and the Mars Climate Orbiter unit mix-up are good engineering stories (C2, C3), but I could not open a source for either this session.

### A.3 What each find can teach (for the Game Designer)

Each row gives a grade 6 to 8 sentence (MS) and a high school sentence (HS), and the learning goal it can serve. **Goal IDs are from `LEARNING.md`; no new goal is proposed.**

| Find | MS sentence | HS sentence | Serves | Show? |
|---|---|---|---|---|
| Sojourner and Pathfinder | "This was the first rover on Mars. It landed in a dry flood channel, and its rocks show a huge flood once ran here." | "The rounded boulders and dry channel are evidence that large amounts of liquid water once flowed at the surface, which today's thin, cold air cannot support." | A2 | Safe |
| Viking 1 and 2 | "Viking scooped soil to test for life. The tests puzzled scientists, and the organic-chemicals test found none." | "Viking's biology tests gave results some scientists called weak positives, but the organic-molecule instrument found none, so the claim was not accepted. Perchlorate, found 32 years later by Phoenix, made scientists revisit the Viking data (NASA)." | B3 (claims and evidence), A1 | Safe (see C for wording) |
| Spirit | "Spirit got stuck in soft sand. It could not tilt its panels toward the Sun, so it ran out of power in winter." | "Spirit's loss shows how a solar-powered machine depends on sun angle and season. It is a real power-budget failure." | C1, C2 | Safe |
| Opportunity | "Opportunity lasted 14 years instead of 3 months. A giant dust storm blocked its sunlight in 2018 and it went silent." | "Dust storms cut solar input; Opportunity's end is the real version of the game's storm and panel-dust loop." | C1, B1 (cause and effect) | Safe. **Strongest link to existing game systems** (storm, dust, panel power; see `journal-dust.md`, `storm-dust-review.md`) |
| Heat Shield Rock | "This is a rock from space. It is mostly iron and nickel." | "An iron-nickel meteorite is a sample of a metal asteroid core, and shows that metal-rich material reaches Mars from space." | A1, (Section B) | Safe |
| Phoenix | "Phoenix dug up water ice just under the dirt, close to the north pole." | "Phoenix confirmed shallow ground ice and found perchlorate salt; its panels failed as polar winter came." | A2, A1 | Safe |
| Mars Polar Lander | "Its engines probably switched off too soon, and it hit the ground." | "Post-mortem analysis found the likeliest cause was early engine shutdown, an example of diagnosing a failure from limited data." | C2 | Safe; **keep short and factual** |
| Beagle 2 | "Beagle 2 was lost for 11 years. Then a photo from orbit showed it had landed, but its panels did not open." | "An orbiter image showed that 'no signal' hid a successful landing with a blocked antenna: the same symptom can have different causes." | C2, B1 | Safe |
| Mars 2, 3, 6, Schiaparelli | "These landers failed during landing. Landing on Mars is very hard because the air is thin." | "Thin air gives little braking, so landers need parachutes and rockets together. Several early failures came at this stage." (cause of each: do not state beyond sources) | A1, C2 | Safe; no invented causes |
| Zhurong | "Zhurong went to sleep for winter and has not woken up." | "Dust on its panels may have cut its power too far to restart; China has said so, but this is not confirmed." | C1 | **Needs a label**: unverified, dust cause is a statement by the mission, not a finding |
| Curiosity, Perseverance (working) | "These rovers are still working. They are the 'scientists' of the story." | n/a | n/a | Safe as a **living** mission. Do not place as a wreck |
| Ingenuity | "A helicopter flew on Mars even though the air is very thin." | "Flight needed fast-spinning rotors because Mars's air is about 1% as dense as Earth's at the surface." (pressure 0.6 kPa is in `accuracy-review.md`; density ratio is **calculated by me from pressure and temperature, not read from a source**) | A1 | Safe |

### A.4 Cautions for placement

- Real rovers weigh 10 kg to 1,000 kg. A 1 block = 1 m scale model is easy (Sojourner about 0.65 x 0.5 m; InSight array 2.2 m, see `dimensions.md`). Check mass and size from NASA before the Artist builds anything. The numbers not opened this session are marked above.
- **Do not put a non-working rover in a state that suggests the player killed it** ("you broke Spirit"). Sets a wrong cause-and-effect message.
- **Do not let a find unlock gameplay by "salvaging" parts without a label.** In the real world these are historical objects with protected status (planetary-protection and heritage, **unverified**; I did not research it). Say "replica".
- Opportunity, Spirit and InSight are all **power-and-dust deaths**. They could carry the dust-panel lesson, which links to documents already in the repo.

---

## B. Minerals and resources: what Mars really has, and are "rare metals" honest?

### B.1 Resources the game can safely feature

| Item | What is real | Tag | MS sentence | HS sentence | Show? |
|---|---|---|---|---|---|
| **Iron oxides** (rust, including hematite) | NASA: Mars looks reddish because of oxidization of iron in the rocks, soil and dust. Cheyava Falls had reddish bands "indicative of hematite" | opened-NASA (Mars facts); opened-wiki (Cheyava Falls) | "The red color of Mars is rust: iron in the dust has reacted with oxygen." | "Iron oxide gives Mars its color; hematite can form in water, which is why rovers look for it." Whether it formed in water at a given site must be checked: **memory** (Opportunity's hematite "blueberries") | Safe |
| **Basalt** (volcanic rock) | Jezero's crater floor is volcanic rock that had interacted with water (NASA text: "two different types of volcanic rock") | opened-NASA (Perseverance) | "Much of Mars's ground is dark volcanic rock." | "Basalt is rich in iron and magnesium; NASA lists the crust as iron, magnesium, aluminum, calcium and potassium (truncated in the page text I read)." | Safe |
| **Olivine** | "Millimetre-size crystals of olivine" in Cheyava Falls veins; olivine-rich horizons | opened-wiki (Cheyava Falls) | "A green mineral from volcanoes, found on Mars." | "Olivine is a magnesium-iron silicate; it weathers fast in water, so finding it means water was scarce or brief there: **memory**, check." | Safe |
| **Sulfates** (like gypsum, calcium sulfate) | White calcium sulfate veins in Cheyava Falls | opened-wiki | "White mineral veins form when water leaves salts behind." | "Calcium sulfate veins record water that moved through cracks." Sulfate-rich layers at Meridiani: **memory** | Safe |
| **Clays** | Curiosity found "chemical and mineral evidence of past habitable environments" (NASA does not name clays in the text I read). Clay minerals as a water signature: **memory** | opened-NASA (partial); clays: memory | "Clays form when rock sits in water for a long time." | "Clay minerals need liquid water over long periods, so orbiters map them to choose landing sites." | Needs a check before use |
| **Silica** | Opal-like silica found by Spirit: **memory**, not verified this session | memory | "Silica is the main part of sand and glass." | "Silica-rich deposits can form in hot water, which is a possible habitat." | **Do not show until verified** |
| **Water ice** | Phoenix dug to ice; "in the mid-latitudes, surface ice is present in impact craters, steep scarps and gullies"; ice is visible at the north cap and under the CO2 cap at the south pole; Utopia Planitia holds a volume equal to Lake Superior (radar); all from Wikipedia's text of primary studies. Ice-rich ground at the Phoenix site is confirmed by NASA | opened-NASA (Phoenix); opened-wiki (Water on Mars) | "Mars has a lot of ice: at the poles and buried under the dirt in the middle latitudes." | "Total ice would cover Mars with a layer metres thick if melted (a figure I saw as a range: 6% to 27% of Earth's present ocean in a source; do not use until checked)." | Safe: ice, with sources; skip the percentage |
| **CO2 ice** | South polar cap has a permanent CO2 ice layer over water ice (opened-wiki). NASA 2018: vaporizing the polar CO2 ice "would only contribute enough CO2 to double the Martian pressure to 1.2 percent of Earth's" | opened-NASA (terraforming release) | "Part of Mars's ice is frozen CO2 (dry ice)." | "Even if all of it were released, pressure would only double, from 0.6 % to about 1.2 % of Earth's." | Safe |
| **Perchlorates** | Phoenix found perchlorate in the soil: a few tenths of a percent (JPL via search); NASA calls it "a possible energy source for microbes and a potentially valuable future resource for human explorers". It strongly attracts water; high doses can disturb thyroid hormone in people (search). A Phoenix scientist says it is wrong to call it simply toxic (search). Perchlorate is used in chemical oxygen generators (opened-wiki, Terraforming of Mars). UV light makes it more harmful to bacteria (opened-wiki, Life on Mars) | opened-NASA, search, opened-wiki | "Mars soil has a salt called perchlorate. It is bad to breathe or eat, but it can also be a resource." | "Perchlorate lowers the freezing point of water (so it may keep brines liquid at low temperature) and can release oxygen when heated, but it is hazardous to humans and microbes." | **Needs a label.** Already used in the Phase 1 "perchlorate dust" hazard and Phase 2 meltwater; those are consistent |
| **Dust** | Pathfinder: airborne dust is magnetic, probably maghemite (iron oxide) | opened-NASA | "The dust sticks to magnets." | "Iron oxide in the dust (maghemite) is the magnetic part." | Safe |

### B.2 "Rare metals": is it honest?

**Short answer: partly. The idea is honest only if the game says "unknown".**

| Question | What is known | Tag |
|---|---|---|
| Does Mars have iron, magnesium, aluminium? | Yes. NASA says the crust is made of "iron, magnesium, aluminum, calcium, and potassium" (the page text I read is cut after that list). Mars's core: iron, nickel and sulfur | opened-NASA (Mars facts) |
| Titanium? | Basalt rocks contain titanium oxides in some minerals. I **did not verify** a source this session | memory |
| Nickel? | Iron-nickel meteorites exist on the surface (Heat Shield Rock, Egg Rock, Lebanon) and the core contains nickel | search; opened-NASA (core) |
| Platinum-group metals (platinum, iridium, osmium, etc.)? | They appear in tiny amounts in **Martian meteorites**: platinum averages about 6.2 parts per billion in the SNC meteorite group (NASA abstract via search). That is a geochemical trace, not an ore. **No ore deposit of any rare metal has been found on Mars.** Whether concentrated deposits exist is unknown | search (Jones et al. 2003 summary) |
| Gold, rare earth elements? | I found nothing this session. **Unverified**, do not mention | n/a |
| Can a rover find a metal "vein" the way a player digs ore in Minecraft? | Ore needs a process that concentrates metals (hot fluids, magma layers). Mars had volcanoes and water, so such processes may have occurred, but **no rover has reported any**. A player mining diamonds-style is the game's invention | memory (reasoning); no source |

**Verdict.**

- **Safe:** iron, nickel, aluminium, magnesium, silicon as "common elements in Martian rock", with the real source (rover and meteorite results).
- **Needs a label:** any metal the game treats as scarce or precious. The label must say: "Invented. No rare-metal ore has been found on Mars." Use a game name that is not a real element if possible (for example a colour name), so students do not learn "Mars has platinum mines".
- **Do not show:** gold, platinum, rare earths, "alien metal". They would teach a false geology and mislead about space resource economics.
- **Better idea for scarcity:** make the scarce item something real and limited: **water ice**, **power**, **perchlorate-free clean water**, **nitrogen** (Mars has very little: see `accuracy-review.md` finding 5). These are real shortages and fit "pressure, not grind".

### B.3 Misconceptions (B)

| Misconception | Honest correction |
|---|---|
| "Mars is red because of blood or fire / it is a hot desert" | Rust (iron oxide) in dust and rocks; the surface is very cold |
| "Mars soil is like fertile Earth soil" | It has salts (perchlorate) that are hazardous, and little of the organic matter that makes Earth soil fertile |
| "Mars has rich ore deposits waiting" | Unknown. No ore found. Space-mining claims are speculation |
| "All the Martian ice is at the poles" | Ice is also buried at mid-latitudes (Phoenix, radar studies) |
| "Perchlorate is simply poison" | Hazardous to people, but also a possible oxygen and water-attracting resource. NASA calls it potentially valuable |
| "Meteorites on Mars prove life" | They are metal rocks from space. No link to life |

---

## C. Biosignatures and the search for life: what to teach and what not to claim

### C.1 The honesty line (non-negotiable)

> **No life has been found on Mars. No sign of life has been confirmed.**

Everything below must carry that sentence nearby. This also matters because the game contains **fictional** dormant fauna (see `DESIGN.md`, Finding 7 in `accuracy-review.md`). A learner will meet fiction *and* real science about "signs of life" in the same game. The labels have to stay far apart.

### C.2 What rovers look for

| Idea | What is real | Tag | MS | HS |
|---|---|---|---|---|
| **Habitability** | Curiosity's goal: "Did Mars ever have the right environmental conditions to support small life forms called microbes?" It found chemical and mineral evidence of past habitable environments | opened-NASA | "Habitable means a place where life could live, not a place where life lived." | "Habitable environments need liquid water, energy, and the right elements; 'habitable' is not evidence of life." |
| **Organics** | Curiosity found the largest organic molecules on Mars (decane, undecane, dodecane in Cumberland). A 2026 *Astrobiology* study said the non-biological sources considered could not fully account for the abundance; it hypothesizes fragments of fatty acids. NASA's own text keeps this a hypothesis | opened-NASA (Curiosity page) | "Organic molecules are carbon building blocks of life. They also form without life." | "Organic does not mean 'living'. Carbon compounds form in meteorites and chemistry. The study ruled out some non-biological sources but not all possible ones." |
| **Methane** | Measurements range from 60 ppbv to under the detection limit; "no scientific consensus on whether these observations genuinely corroborate the existence of methane on Mars". Methane can come from microbes or geology | opened-wiki (Methane on Mars) | "Some scientists saw a gas that, on Earth, is made by living things and by rocks. They do not agree it is there." | "Methane is destroyed quickly in Mars's air, so a source must exist, but instruments disagree on its presence. The source could be geological." |
| **Sample caching** | Perseverance collects core samples "for pickup by a future mission". More than two dozen collected | opened-NASA | "The rover stores rock samples in tubes to bring home later." | "Return is needed because lab instruments on Earth are far more sensitive than a rover's." **Status:** Mars Sample Return is **not funded**: Congress's 2026 budget left it out (search, not primary). Say "no return mission is currently funded" and mark **unverified** until a primary source is opened |
| **Instruments** | SHERLOC (organics and minerals with a UV laser), PIXL (X-ray chemistry), SuperCam (laser and camera, rock chemistry from a distance), MOXIE (oxygen). Perseverance team named SHERLOC and PIXL as the two instruments that studied Cheyava Falls | opened-wiki (Cheyava Falls: PIXL and SHERLOC); previous round opened Perseverance instruments page (see `dimensions.md`) | "Rovers have cameras and lasers that read what rocks are made of." | "Spectrometers identify minerals and organics by how they absorb or scatter light." SuperCam's exact method: memory |
| **Jezero delta** | Perseverance found a "habitable, stable lake, delta, and river system in Jezero Crater's history"; deltas bury and keep organic material quickly | opened-NASA (Perseverance); preservation: memory | "Jezero was a lake long ago. Mud from the river settled there." | "River deltas bury sediments fast, which can preserve organic matter, so they are good places to look for ancient life signs." |
| **Cheyava Falls** | Found July 2024. Rock about 0.98 x 0.61 m with white calcium sulfate veins, reddish hematite bands, olivine crystals, and mm-size "leopard spots" (off-white splotches ringed with black). The spots contain iron and phosphate. The sample is "Sapphire Canyon". **Sept 2025**: Nature published the peer-reviewed results. NASA's wording: "potential biosignatures — clues that suggest past life may have been present, but that require more data or further study before any conclusions about the absence or presence of life" | opened-NASA (Perseverance page), opened-wiki (Cheyava Falls) | "The rock has spots that look like ones made by tiny life on Earth. They may also be made by plain chemistry. We need more tests." | "The spots show iron phosphate and iron sulfide minerals (consistent with vivianite and greigite) formed in low-temperature reactions with organic carbon. On Earth microbes can drive these reactions, so it is a candidate biosignature, but non-biological reactions are not ruled out and confirmation needs laboratory study on Earth." (mineral names: opened-wiki; not checked in the Nature paper itself) |
| **CoLD scale** | A seven-step Confidence of Life Detection scale; Cheyava Falls was placed at Step One, "possible signal" (Wikipedia) | opened-wiki. **Not verified in a NASA source this session** | "Scientists use steps. 'We saw something' is step one; 'life confirmed' is step seven." | "CoLD asks for ruling out non-life explanations at every step before claiming life." Use the idea, but say "a scale of confidence" until the NASA source is opened |
| **Viking (the cautionary tale)** | Viking tests could be read as weak positives; the organics test found none (NASA). Perchlorate, found by Phoenix in 2008, led scientists to "revisit the data from the Viking landers" (NASA) | opened-NASA (Viking 1, Phoenix) | "Viking's tests puzzled scientists, and later discoveries changed how to read them." | "Perchlorate can destroy organics when heated, so Viking's heating method may have hidden them: **memory**, flag as unverified. The honest point is that new evidence reopened old data." |

### C.3 Teaching the scientific method without claiming life

This is the main gift of this content. The real story is a claims-evidence-alternatives chain, and it maps well to the learning goals the game already has.

| Idea | How a game could show it, in one line | Serves |
|---|---|---|
| **Claim, evidence, alternatives** | Each "possible sign" card has three boxes: "What we saw", "Could life explain it?", "Could non-life explain it?" The player cannot write "life found" | B3 (predict and check); a possible new data-reasoning idea for the Instructional Designer |
| **Confidence steps** | A ladder (see CoLD): "Seen" → "Measured" → "Repeated" → "Other explanations ruled out" → "Confirmed". Every real find stops at step 1 or 2; the top step stays **empty on purpose** | B3, C3 (use data to decide) |
| **Same data, different readings** | Viking: same results, then a new discovery (perchlorate) changed the reading | B3, B5 |
| **Contamination and false positives** | Phoenix scientists checked that perchlorate was not from the spacecraft (search) | C3 |
| **Samples are cached, not decided** | The game sample tubes "wait for a lab". No player can resolve them in the game | honesty |

**Rules for the game:**
1. A "sign of life" item must never turn into a living organism or a story reveal. The game's fiction (Thawlings) is separate and labelled "fiction".
2. Never use the word "proof" or "found life". Use "possible sign" and "needs more evidence".
3. Do not allow the player to "solve" the question. The honest ending is "samples are waiting".
4. Show alternatives as equal-looking options. Do not rank "life" above "chemistry".
5. The Cheyava Falls rock is **safe to show** with the NASA wording quoted exactly.

**Risk to flag:** a mystery rock in a game invites players to guess "alien life". That is fine as curiosity, but a game that rewards "life!" answers teaches the wrong habit. Reward good **questions** and **tests** instead.

### C.4 Misconceptions (C)

| Misconception | Honest correction |
|---|---|
| "NASA found life on Mars" (most common after the 2024 headlines) | NASA found a **potential** biosignature: a clue that needs more study. No life confirmed |
| "Organic = alive" | Organic means carbon-based; it also forms in meteorites and by chemistry |
| "Methane means life" | Methane can come from rocks; the Mars measurements are also disputed |
| "Habitable means inhabited" | It means life *could* live there |
| "The rock samples are on Earth" | They are still on Mars; no return mission is funded (search; verify) |
| "Viking proved there is no life" | Viking was inconclusive; later findings changed how to read it. Mars is not proven lifeless, and not proven to hold life |
| "Mars is dead / was always dead" | Jezero was a lake and river system; Mars was once wetter. Whether life ever existed is unknown |
| "Rovers can look for aliens like in films" | They look for chemistry and minerals; they cannot see microbes |

### C.5 Safe / label table (C)

| Item | Tag |
|---|---|
| Habitability, organics, methane debate, sample caching, delta preservation | **Safe**, with the honesty line |
| Cheyava Falls "leopard spots" | **Safe with exact NASA wording**. Needs a label: "potential biosignature, not confirmed" |
| CoLD scale | **Needs a label** and a NASA source before using the name |
| "Mars Sample Return is cancelled/unfunded" | **Needs a check** before text is written; status changes |
| Any game find that implies life | **Do not show**, unless in a clearly marked fiction layer |

---

## D. What would help make Mars habitable: real research versus speculation

### D.1 The honest headline

Terraforming Mars is **not possible with present-day technology**. NASA's 2018 release about the Jakosky and Edwards study (Nature Astronomy, **opened-NASA**; the paper itself not opened) says:

- Mars does not hold enough CO2 that could practically be put back into the air to warm it.
- Pressure today is about 0.6% of Earth's. Researchers estimate a CO2 pressure similar to Earth's total pressure is needed to allow stable liquid water.
- Vaporizing the polar CO2 ice would only **double** pressure, to **1.2% of Earth's** (NASA, release).
- CO2 on dust in the soil: **up to 4%** of the needed pressure. Carbon in minerals: **less than 5%** of the required pressure, depending on how extensive deposits buried near the surface are. Deeper deposits are unknown and need **above 300 °C** to release CO2 (NASA release).
- Water vapor cannot warm Mars on its own. Fluorine-based greenhouse gases were proposed, but NASA's release says they are short-lived (the page text I read is cut; it gives that reason only partly).
- Most of the ancient atmosphere has been lost to space, stripped by solar wind and radiation (MAVEN and Mars Express, NASA).

**Important consequence for the game.** `DESIGN.md` and `accuracy-review.md` already record that the game's CO2 pool is a simplification: the game gives the player far more CO2 and a 75 kPa target than the real Mars inventory allows. This review does not reopen that. It means the "things that help make Mars habitable" **must not suggest that a real, known method exists.** The existing journal note (`LEARNING.md` "Science accuracy notes") covers this; any new find content should link to it. (I did not re-read the note this session.)

### D.2 Ideas, graded

| Idea | Status | MS | HS | Tag |
|---|---|---|---|---|
| **Release CO2 from polar caps and soil** | Studied. Not enough (see D.1) | "Melting the poles would not make Mars Earth-like; there is not enough gas." | "Releasing all accessible CO2 would raise pressure to roughly 7% of Earth's (NASA Scientific Visualization Studio, via search) or less; far short of what is needed." The 7% figure is search-only; the 1.2% figure for the polar cap alone is NASA | **Safe** (exact numbers from the NASA release) |
| **Greenhouse gases made in factories** | Proposed; NASA release calls them short-lived (partial text). Needs production at huge scale | "People have suggested making strong greenhouse gases in factories." | "Fluorinated gases trap heat strongly but break down in sunlight; keeping them in place would need constant production." | **Safe** (it is in the game as "emitters"). Label "proposed, never tested at planet scale" |
| **Orbital mirrors / dust on the ice caps** | Spreading dust to absorb sunlight is named by NASA as a way to vaporize the polar ice. Mirrors: **speculative**; I did not find a source this session | "Dark dust on ice would soak up sunlight and melt it." | "Lower albedo raises absorbed energy, the same reason soot on Earth's glaciers speeds melting." | Dust: **safe** (NASA). Mirrors: **needs a label: speculative, no source opened** |
| **Importing gases by redirecting asteroids** | Wikipedia: ammonia-rich asteroids (40 to 400 billion tonnes) as a nitrogen source; speculative | "One idea is to bring nitrogen from space." | "Mars has very little nitrogen, which a breathable mix needs." | **Needs a label: speculative** |
| **Magnetic field replacement** | NASA terraforming release and wiki: no global field; atmosphere stripped by solar wind. A shield at a Mars-Sun point is **speculative** (memory) | "Mars lost its air partly because it has no magnetic field to shield it." (the loss is real; the "partly" is a simplification, see `round3-review.md`) | "Atmospheric loss is driven by solar wind and radiation (MAVEN data)." | Safe for the problem; **do not show** any shield as a solution |
| **Perchlorate cleaning / bioremediation** | Perchlorate-reducing bacteria exist on Earth (**memory**); using them on Mars is research-stage speculation | "The soil has salt that is harmful. Some Earth microbes can eat it." | "Perchlorate-reducing microorganisms are used on Earth to clean water; using them in Martian soil is unproven." | **Needs a label**; source not opened this session, so mark **unverified** |
| **Engineered microbes / photosynthesis** | Speculative. Wiki: "toxic soil, low light levels, no magnetic field" are hazards | "A plant or microbe needs water, light, warmth and clean soil." | "Photosynthesis could add O2, but 75 kPa total air also needs gases that Mars lacks." | **Needs a label**. Ties to the game's Phase 3 rule A (CO2 is the oxygen source), already an accepted simplification |
| **Closed greenhouses / domes** | Real approach to near-term living. Not terraforming | "People could grow food inside sealed buildings." | "A sealed dome avoids changing the whole planet." | **Safe**. Mention this as the real alternative to terraforming: it is a valid "compact before wide" lesson |
| **In-situ resource use: MOXIE** | Real: made **122 g** of oxygen over **16 runs**, at least **98%** pure, peak **12 g per hour** (NASA/JPL completion release, via search; NASA Perseverance page confirms that it "successfully produces oxygen from carbon dioxide ... to fill lungs and fuel rockets") | "A box on Perseverance made oxygen from Mars's air. A small dog breathes about that much in 10 hours." | "MOXIE electrolysed CO2 (about 95% of the air) to oxygen, a proof of concept for future crews and rocket fuel." | **Safe.** Excellent anchor for the game's Oxygen Maker. Note 122 g is a demonstration; planet-scale O2 is a different thing |
| **Ice mining** | Real: shallow ice at Phoenix, mid-latitude ice (see B.1) | "Digging up ice gives water for drinking and fuel." | "Ice at mid-latitudes can be mined for water; this is a leading resource target for crews." | Safe (already in game as ice extractors) |

### D.3 Terraforming misconceptions (D)

| Misconception | Honest correction | Risk |
|---|---|---|
| "We could terraform Mars if we wanted" | NASA-sponsored study: not with present-day technology | The game's central fantasy is the biggest instance of this, so the journal note must stay visible |
| "Melting the polar ice would make a thick atmosphere" | Would double pressure to 1.2% of Earth's | High |
| "Mars lost its air because of nothing/ a collision" | The data (MAVEN, Mars Express) say solar wind and radiation stripped it over billions of years; the contribution of the missing magnetic field is a simplification | Medium |
| "More greenhouse gas = more life" | Warm and thick air is necessary, not sufficient; need nutrients, clean soil, and more | Medium |
| "MOXIE solves oxygen for Mars" | It made 122 g in total, a demonstration | High (scale) |
| "Plants would just grow in Mars soil" | Perchlorate, no nitrogen, no organic matter, low light | Medium |
| "Terraforming is clearly good / clearly bad" | Contested science and ethics; **do not present opinions as facts**. This is a D1 theme, handled by the existing ethics design | Ill-structured, stays with D1 owner |

---

## E. Safe / label / do-not-show summary

| Content | Status |
|---|---|
| Real rovers and landers as replica monuments with true text (Sojourner, Viking, Spirit, Opportunity, Phoenix, InSight, Beagle 2, Mars Polar Lander, Schiaparelli, Mars 2/3/6 with no invented cause) | **Safe** with "replica" label |
| Zhurong | **Needs a label** (dust cause unconfirmed) and a primary source |
| Curiosity and Perseverance as wrecks | **Do not show** as dead: they are active |
| Hardware salvage that gives a gameplay reward | **Needs design review** (heritage framing) |
| Iron oxide, basalt, olivine, sulfate, water ice, CO2 ice, perchlorate | **Safe** (perchlorate needs "hazard and resource" label) |
| Clays, silica | **Check source first** |
| Platinum, gold, rare earths, "alien metal" | **Do not show** as real |
| Invented scarce metal | **Allowed only as clearly invented**; row in the fantasy audit table |
| Cheyava Falls | **Safe** with exact NASA wording ("potential biosignature") |
| Any "life found" outcome | **Do not show** |
| CoLD scale by name | **Needs source check** |
| Polar CO2, dust on ice, greenhouse emitters, ice mining, domes, MOXIE | **Safe** |
| Mirrors, asteroids, magnetic shield, perchlorate-eating microbes, engineered microbes | **Label as speculative** |
| "Terraforming works" implied as real | **Do not show**; link to the existing accuracy note |

---

## F. Ill-structured problem: how much exploration content, and how does it connect to the game's goals?

**Frame.** The owner wants explorable discoveries. The science is rich, but the game already has a tight loop (life support, Index, gates, 3 phases) and fixed learning goals. Exploration content can add **appeal** (discovery, mystery, collectibles) and **instructional value** (C2 diagnose, B3 claims, A2 water), but it can also add scope, break the "pressure, not grind" pillar, and blur real science with the game's fiction. It is ill-structured: unclear scope, competing goals, several valid paths.

**Disciplines needed.** Game Designer (loop fit, fun, reward design); Instructional Designer (which goal each find serves, assessment, whether "claims and evidence" becomes a new goal, which would be a goal change); Artist (build cost of replicas; real hardware is the reference); Technical Lead (structure placement, data-driven finds, journal entries); Producer (scope and schedule); SME (accuracy).

**SME view (one perspective).**

| Option | What | Pros | Cons |
|---|---|---|---|
| **1. Hardware monuments only** | A few replica sites (for example Opportunity, Spirit, Phoenix, Viking, Beagle 2) with journal text | Cheap; true; ties to dust, storm, power and water systems | Low mystery; fewer links to habitability |
| **2. Monuments + "Possible sign" cards** | Add 2 or 3 investigations modelled on Cheyava Falls and Viking, using the claim, evidence and alternatives structure; no life outcome | Strongest science-method teaching; high curiosity | New content type; risk of "life found" misreading if written carelessly; needs a goals check (is it B3?) |
| **3. Full exploration layer** | Option 2 plus resource finds (ice, perchlorate, invented metal) as gameplay | Most appeal | Large scope; invented metals bring misconception risk; may break "pressure, not grind" |

**SME recommendation:** Option 2, in this order: (a) hardware monuments that match systems the game already has (Opportunity and InSight for dust/power, Phoenix for ice and perchlorate, Beagle 2 for diagnosis, Viking for claims); (b) one "possible sign" investigation modelled on Cheyava Falls, with the honesty line; (c) skip invented rare metals in the first pass. If a scarce item is wanted, use ice or nitrogen.

**Assumptions I made.** (1) No new learning goal is added; finds serve A1, A2, B1, B3, C1, C2, C3. (2) Replicas are acceptable under the fantasy rules. (3) The owner wants "signs of life" as science investigation, not as a plot reveal. (4) The existing fiction (Thawlings) stays labelled fiction.

**What would make it better defined.** A decision on whether "claims and evidence" becomes a learning objective (a goal change for the Instructional Designer); a paper prototype of one "possible sign" card tested against the 12 to 18 misconception list; the owner's answer on rare metals (real, invented, or none).

**Route.** To the Game Designer (for `docs/design/exploration-finds.md`), then the Producer for a cross-role review. The "life" framing and the invented-metal question go to the owner.

---

## G. Sources (and what each supported)

| Source | Used for | Tag |
|---|---|---|
| science.nasa.gov/mission/mars-2020-perseverance/ | Perseverance goals, Cheyava Falls (Sept 2025 Nature, "potential biosignatures"), odometer 45.22 km, MOXIE, samples | opened-NASA |
| science.nasa.gov/mission/msl-curiosity/ | Curiosity mass, goal, organics (Cumberland), 2026 study | opened-NASA |
| science.nasa.gov/mission/mars-exploration-rovers-spirit-and-opportunity/ | Spirit 7.7 km, Opportunity 28.06 mi, 2018 storm, Gusev, Meridiani | opened-NASA |
| science.nasa.gov/mission/mars-pathfinder/ | Pathfinder landing, flood channel, magnetic dust | opened-NASA |
| science.nasa.gov/mission/phoenix/ | Phoenix landing, ice, perchlorate | opened-NASA |
| science.nasa.gov/mission/viking-1/ | Viking 1 dates, 978 kg lander, biology results, Mars 3 note | opened-NASA |
| science.nasa.gov/mission/insight/ | InSight landing site | opened-NASA |
| science.nasa.gov/mars/facts/ | Colour from iron oxide, crust and core composition | opened-NASA |
| nasa.gov/news-release/mars-terraforming-not-possible-using-present-day-technology/ | All terraforming numbers in D.1 | opened-NASA |
| en.wikipedia.org (articles listed in section 0) | Mission dates, Beagle 2, Schiaparelli, Mars Polar Lander, Methane on Mars, Cheyava Falls details, perchlorate in Life on Mars | opened-wiki (secondary) |
| JPL/NASA MOXIE completion release (Sep 2023), via search | 122 g, 16 runs, 98%, 12 g/h | search |
| JPL Beagle 2 release (Jan 2015), via search | Beagle 2 found by HiRISE | search |
| Jones, Neal and Ely (2003), NASA abstract, via search | Platinum about 6.2 ppb in SNC meteorites | search |
| Space.com, Live Science etc., via search | Zhurong hibernation, Mars Sample Return funding | search (not primary) |
| Jakosky and Edwards (2018), *Nature Astronomy* 2, 634 to 639, doi 10.1038/s41550-018-0529-6 | Source of NASA's release | **Not opened** (blocked). Citation from search |

**Unverified items to resolve before any student-facing text:** Mars 3 transmit time; Zhurong cause; platinum-group "trace" figure; all `memory` entries (silica at Home Plate, clays, Viking heating explanation, Heat Shield Rock details beyond the search summary, MER mass, Perseverance mass, perchlorate-reducing bacteria, mirrors); the CoLD scale in a NASA source; Mars Sample Return status; the "7%" pressure figure (NASA SVS, via search only).
