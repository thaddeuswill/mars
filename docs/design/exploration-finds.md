# Exploration Finds: rover sites, mineral nodes and "Signs of life?" (design proposal)

**Owner:** Lead Game Designer · **Status:** **proposed**, not built, not playtested (design walkthrough only) · **Written:** 2026-11-28, docs only · **For:** the owner (3 questions, section 8), Producer (budget fit), Lead Instructional Designer (alignment, wording), SME (every science line), Lead Artist (monument models), Technical Lead (feasibility)

**Owner request (BACKLOG, 2026-11-28):** "these things could be life on mars?, finding rovers, rare metals or things to help us find out life and making mars a habitable planet".

**Reads with:** [DESIGN.md](../../DESIGN.md) (Fantasy and Authenticity, Phase 3, gates), [LEARNING.md](../../LEARNING.md) (A1 to D2), [story-bible.md](story-bible.md) §3.4, §4.3, [stage3-plan.md](../tech/stage3-plan.md), [budget.md](../production/budget.md) §4.3, [landing-spec.md](landing-spec.md) §5 (journal entry format), [f1-storm-spec.md](f1-storm-spec.md), content rules CR-F9, CR-F10 in `docs/learning/content-rules.md`.

**Tags:** *invented* (game number), *design choice*, *unverified* (a science or history fact written from memory). **The SME's review `docs/science/exploration-finds-review.md` did not exist when I finished, so every real-world fact below is *unverified* until the SME confirms it.** I used no sources. Nothing here ships as fact before the SME and the Instructional Designer (ID) have checked it. No NGSS codes are claimed; the ID owns alignment.

---

## 0. Summary

| Question | Answer in one line |
|---|---|
| What is it? | Three optional kinds of discovery on the Mars map: **heritage sites** (full-size models of real rovers and landers, with a plaque and a journal entry), **mineral nodes** (real rock and soil types that feed existing machines), and **"Signs of life?" case files** (a sample-test-argue loop that never ends in "life found"). |
| Does it add a new Index lever? | **No.** Finds are inputs to existing levers, or knowledge. A new lever would break the Phase 3 mass balance (rule A) and the forecast keys (CR-F10). |
| Does it touch the gates? | **No gate reads a find.** Gates read Index values and machine states only. |
| Honest answer on "rare metals" | Mars has **no known ore of rare metals**. The game does not invent one. The real "rare" finds are iron-nickel meteorites and manganese-rich rock (section 3.3). |
| Honest answer on "life on Mars" | **No life has been found.** The case files teach how scientists argue about a possible sign. They end at "not enough to say", never at "life". |
| Minimum viable version | 3 heritage sites + 3 mineral nodes + 1 case file. About **9 to 14 Technical Lead sessions** and about 6 to 8 Artist sessions (*unverified*). **Not Stage 3**: Stage 3 is already 40 to 66 Technical Lead sessions. Land it in **Stage 4, Phase 2**, next to Basu's drill sites and the survey scan, which need the same placement and journal plumbing. |
| Biggest risk | Scope. Second biggest: a student leaves believing life was found. |

**Two Goals check (instructional value and market appeal).** Instructional: real hardware at true scale, real minerals and an argument-from-evidence loop carry A1, A2, C1, C2, D1 and D2 and give B3 a practice mode. Appeal: this is the Explorer player type's best content (DESIGN.md Bartle table lists only the survey scan and Basu's sites today) and the "rovers in the news" relevance hook (DESIGN.md, Relevance). Where they pull apart: a full list of rovers is a collectible checklist, which the Zeigarnik caution in DESIGN.md says to avoid. I chose learning-safe over appeal-max (section 2.4) and flag it.

---

## 1. Principles for every find (apply the six fantasy rules)

| # | Rule | Source |
|---|---|---|
| E1 | **Optional.** No find is on the critical path of a gate. The minimum path to every gate works with no find at all (starting stock covers the first iron, see 3.4). Exploring is the reward for finishing a job early, never a chore. | Pillar "pressure, not grind"; DESIGN.md §5 balance rules |
| E2 | **Every find has a real name and a job.** A find that teaches nothing and does nothing is cut (rule 5, seductive detail). | DESIGN.md fantasy rule 5 |
| E3 | **Find, then learn.** The world object comes first; the journal entry unlocks when the player reaches it. Never a pop-up. | landing-spec §5.4 |
| E4 | **Two depths.** MS text (80 words max, story bible 6.1) and an HS "read more". Same entry JSON as landing-spec §5.2 (`goal_ids`, `reveals`, `names`). | landing-spec §5 |
| E5 | **Fiction is labeled.** Anything invented says so in the entry (the "real vs. game" note). | DESIGN.md fantasy rule 3 |
| E6 | **No find reveals a forecast answer early.** Every entry declares `reveals`; text that could answer an open forecast is held (section 6.3). | CR-F9 |
| E7 | **No new Index lever, no new source ID** (section 4). | CR-F10, rule A |
| E8 | **Evidence is own-player.** Discovering is per planet (shared, like a gate), reading and testing are per player. | Group rules, Keeper's Record |
| E9 | **No score, no rank, no completion bar** (section 2.4). | DESIGN.md Motivation table |

---

## 2. Heritage sites: rovers and landers as monuments

### 2.1 Decision to make first: where is the real hardware? (ill-structured)

**Frame.** Real rovers sit thousands of kilometers apart on real Mars, each at its own landing site. Our planet (`mars:planet_1`) is one generic site with a dome. Placing real hardware near the dome says, without a word, "they landed next to each other". That is a negative-learning risk (fantasy rule 3). The owner wants the *feeling of finding a rover*. These two pull apart.

**Disciplines.** SME (what is true about each site and whether a compression is acceptable), Artist (scale and look), Technical Lead (placement cost), ID (negative-learning check), Producer (cost).

**One perspective (mine, Game Designer).** The find should feel like a discovery and cost no false belief.

| Option | What the player finds | Learning | Appeal | Cost | Risk |
|---|---|---|---|---|---|
| **A. Hana's heritage trail** (replicas) | Full-size replicas that the First Shift crew (Hana, who "builds things") put up from spare parts, each with her plaque: "Not the real one. The real one is [far away]." | A1 scale at 1 block = 1 m; true site named; no false belief; fits the story-bible "traces left by earlier crew" (§4.3) | Medium to high: a trail with a human voice; less wonder than "the real thing" | Same models as B | Low. A new fiction element: replicas are labeled [FICTION] (audit row) |
| B. Compressed map, real hardware | The real machines, as if they landed here, with a map note "Not to scale: on real Mars these are thousands of km apart" | Scale lesson is explicit but undone by the placement | High wonder | Same models | Medium: a correction plaque fights what the player sees |
| C. Real distances, reachable late | Hardware at true separations, reached by transit or vehicle in Phase 3 | Most honest | Low for most players: too far, too late | High (vehicles, travel time) | High scope |

**Recommendation: A.** It keeps "I found a rover" and adds a person, Hana, in the voice the story already has. It needs one fiction audit row (section 7). **Assumption:** the owner accepts replicas as "finding rovers" (question 1). If not, B is the fallback with a mandatory scale note, and the audit row changes.

The rest of this file is written for option A. Under B only the plaque text and the audit row change.

### 2.2 The sites

Sizes are **unverified** (from memory); the SME confirms them, and the Artist builds from NASA reference imagery (each image's license checked per the art reference audit practice). 1 block = 1 m = 16 px.

| Site | Real machine and year (*unverified*) | Real size (*unverified*) | Model footprint | What the player learns (goal) | Phase | MVP |
|---|---|---|---|---|---|---|
| **Pathfinder + Sojourner** | Lander and the first wheeled rover on Mars, 1997 | Rover about 65 cm long, about 10 kg (microwave-oven size) | 1 x 1 rover model (under 1 block) beside a 2 x 2 lander | **Scale**: it is smaller than the player's crate; first wheels; how much one small solar-powered robot could do (A1, D2: Earth link "engineering under tight limits"). | 1 | **Yes** |
| **Viking 1 lander** | Landed 1976; ran life-detection experiments | About 2 m class lander (*unverified*) | 3 x 3 | **Case file 1 starts here** ("The fizz test", section 4): the first life experiments (D1, C2). | 2 | Full |
| **Opportunity** | 2004 to 2018; found hematite "blueberries" and sulfates; ended after the 2018 dust storm | About 1.6 m long, about 185 kg, golf-cart size | 2 x 3 | Endurance, and why dust and sunlight matter to a solar machine (C1). Pairs with the **iron oxide** node (hematite) and the **sulfate** node. | 2 | **Yes** |
| **Spirit** | 2004 to 2010; stuck in soft sand | Same class as Opportunity | 2 x 3 | A repair-and-fail story: a rover lost to terrain and a cold winter (C2). | 2 | Full |
| **Phoenix** | 2008 polar lander; dug ice and found perchlorate | Static lander, about 2 m | 3 x 3 | Ice under the soil, and the perchlorate that the dome's dust-off station already guards against (A2, C1). Pairs with ice lenses. | 2 | Full |
| **InSight** | 2018 to 2022; listened for marsquakes; solar panels dusted over | Static lander with two round arrays | 3 x 3 + array (the round-array model already exists, ARCHITECTURE §2.18) | Dust on panels is a real end of mission (C1, C2); what seismology says about the inside of a planet. **Reuses the round solar array model.** | 2 or 3 | Full |
| **Curiosity** | 2012 to now; Gale Crater; found clay and organic molecules | About 3 m long, about 900 kg, small-SUV size | 3 x 3 x 2 | Scale again (versus Sojourner), clay minerals, radiation measured at the surface (A4). | 3 | Full |
| **Perseverance + Ingenuity** | 2021 to now; collects sample tubes for return to Earth; Ingenuity was the first powered flight on another world | About 3 m long, about 1 tonne; Ingenuity about 1.8 kg | 3 x 3 x 2 + a 1 x 1 helicopter | **Sample tubes**: the same practice the player's own "sample kit" copies (case files). Test of an idea on a small scale first (C2). | 3 | **Yes** |
| Zhurong (2021) | China's first Mars rover | About 240 kg class | 2 x 3 | International effort (D2). | 3 | Full (optional) |
| **Lost missions wall** | Missions that failed on arrival (e.g. Mars Polar Lander 1999, Beagle 2) | n/a | 1 sign wall, no model | Failure is normal; engineers diagnose (C2). Cause statements **unverified**. | 2 or 3 | Cut first |

**MVP three:** Pathfinder + Sojourner (Phase 1, scale), Opportunity (Phase 2, endurance and dust, pairs with minerals), Perseverance + Ingenuity (Phase 3, samples and the search for signs of life). One per phase, so each phase has one find that fits its theme.

### 2.3 What the player finds, learns, and does

**At the site (Phase 1 example, Pathfinder + Sojourner).**

| Step | What happens | Notes |
|---|---|---|
| 1. Hint | BELL logs a bearing band (2.5) | No marker on the map |
| 2. Walk | 120 to 200 blocks from the dome on foot, inside tier 1 suit air | About 35 to 60 s each way at walking pace (*calc*, *unverified* speed). Tier 1 suit is 17 minutes (balance sheet 2d). Trip planning is C1 practice |
| 3. Arrive | Replica stands on a small gravel pad; a **plaque block** is the discovery trigger (right-click; range check) | One interaction, no puzzle |
| 4. Read | The journal entry unlocks silently with the usual one-line pointer (landing-spec §5.4) | MS text always; HS "read more" below it |
| 5. Optional | Take a **sample** (3.5) or a "site note" book page. The plaque is never a quest | |

**Plaque voice (Hana).** Short, dry, fond, and honest: *"I built this from spare plate and a bad attitude. The real one is not here. It is far away, and it is smaller than you think."* (~~draft~~ voice sample, ID pass pending). It always says "replica" and names the real site.

**Journal entries, two depths (drafts; word caps and grade 6 to 7 pass belong to the ID; every fact *unverified* until the SME).**

`find.pathfinder` (Phase 1, goals A1, D2, `reveals`: none for the MS text)
> **Sojourner.** The first rover to drive on Mars was about as big as a microwave oven. It arrived in 1997 on a lander called Pathfinder. This is a replica Hana built at full size. The real one is far from here.
> *Read more (HS)* **`reveals: forecast.f1`, held until F1 resolves:** "Small solar panels power a rover like this. Fine dust settling on panels slowly cuts the power they make." (This sentence names the dust effect on power, which is F1's answer area; see 6.3.)

`find.opportunity` (Phase 2; C1, D2; `reveals: forecast.f1`, so it never appears in Phase 1)
> **Opportunity.** A rover meant to work for 90 sols worked for years. In 2018 a giant dust storm blocked the sunlight it needed, and it never woke up. This is a replica. The real one is far away.
> *Read more (HS):* "A rover's solar panels make less power when dust covers them or sunlight is blocked. Planners plan power around that." (The "90 sols" and the year are *unverified*. **Do not** present the popular line "my battery is low and it's getting dark" as a rover quote: it was a journalist's poetic translation of the data (*unverified*), a misattribution risk, section 6.2.)

`find.perseverance` (Phase 3; B3 practice link, D1, C2; `reveals: none` after case 1 is open)
> **Perseverance.** This rover collects rock samples in sealed tubes and leaves them in a pile, so a later mission can carry them to Earth labs. Ingenuity, the little helicopter beside it, was the first aircraft to fly on another world. The real ones are far away.
> *Read more (HS):* "Sealing a tube keeps the sample from being contaminated. Scientists name each tube and write what they saw, so another team can check the claim. Your sample kit works the same way."

### 2.4 The "Hall of rovers" progress list

**Tension (ill-structured).** The owner idea is a progress list. DESIGN.md (Motivation) says visible unfinished tasks must each tie to a learning goal and warns against "get every quest" compulsion. A list that says "3 of 9 rovers" is exactly that. The player types table says Explorers should be served richly. Learning wants a record; appeal wants collecting.

| Option | Description | Learning | Appeal | Risk |
|---|---|---|---|---|
| **A. Timeline wall (recommended)** | A page in the Colony Record: a time line of missions, a dot appears when found. **No count, no silhouettes of unfound ones, no completion state, no reward.** Each dot opens the entry. | Time order teaches how exploration built on itself (D2) | Medium: Explorers can see their trail | None of the compulsion risks |
| B. Silhouette checklist | Gray outlines for every site, filled when found | Same | Higher for completionists | Zeigarnik compulsion; invites a complete-all goal |
| C. No list | Entries live in the journal only | Same | Low | Loses the "wall of history" feel |

**Recommendation: A, and revisit if** playtesters ask for a count (the Colony Record page can add one line later; removing it later is harder). Per-planet discovery is **shared** (a find is a world fact, like a gate). Reading and sampling are per player, own evidence only (E8). The Hall fills no Keeper's Record page (reading is not evidence, CR-R2).

### 2.5 Placement and the compass hint

| Question | Recommendation | Why |
|---|---|---|
| Fixed coordinates or procedural? | **Seeded, in rings around the dome.** Each find has `ring_min`, `ring_max` (blocks) and a `phase` in `pack/data/mars/finds/*.json`. The bearing is derived from the planet seed, so it is fixed per planet and **different on each group planet**. | Same planet gives the same place every time (hints work, saves are stable). Different planets stop a class copying one answer. A worksheet cannot say "walk 200 east", which is a fair cost |
| Rings (*invented*, *unverified* walk times) | Phase 1: 120 to 200. Phase 2: 250 to 600. Phase 3: 500 to 1,200 | Phase 1 is on foot inside suit air. Later phases have airlocks, outposts, transit tubes (DESIGN Phase 2) |
| Mechanism | Prefer **datapack structure placement** (a ring-style structure set, like vanilla stronghold placement) so no Java is needed for placement | **Unverified:** the Technical Lead must confirm a custom dimension can use ring placement. Fallback: Java places once at first `/mars go`, as the starter dome does |
| Min spacing | At least 150 blocks between finds; no find within 100 blocks of a dome or outpost | Avoids a find "inside" a base |
| The hint | **BELL, in the colony log, at gates, by orbit data:** "Old orbiter images show a man-made shape about 200 meters to the north-east." Bearing and a distance band, never exact coordinates. One hint per find, delivered at its phase gate (Phase 2 begins and so on), not on a timer. | BELL "never guesses" (story bible 4.1). Real orbiters really did image the landers (*unverified*), so the hint is true |
| Mineral hint | The **survey scan** (asset M-28, T-19, already designed for ice lenses) also shows nodes within about 64 blocks | Reuses an existing item. The *Survey* colony post owns it |
| Compass item | **Cut first.** A "survey compass" is an easy add later | Another item to learn |

Colour is never the only signal: the plaque has a shape icon and a name; a node under the scan has an icon and a label (UI rule, ages 12 to 18).

### 2.6 How the model is made (Artist)

| Item | Approach | Reuse |
|---|---|---|
| Rover and lander models | Static block-model JSON at 1 block = 1 m = 16 px, built by the existing generator tools in `tools/art-preview/`, from NASA reference images; gradual dust weathering as in the art rules | Same pipeline as the habitat and the round solar array (3 x 3 footprint, raised model, shape code matches) |
| Collision and shape | One multi-block placement for 3 x 3 monuments, as the round array does | ARCHITECTURE §2.18 |
| Plaque | One block, same for every site, with the site name and the Hana line | One texture, text from lang |
| Gravel pad | Decorative; reuses existing terrain blocks | No new texture |
| Ingenuity | One small model on a post | One model |
| Reference check | Artist records each image source and license in the reference audit | `docs/art/reference-audit.md` practice |

**Cost.** One monument is one Artist session; 3 for the MVP, about 9 for the full list (*unverified*, no Artist rate has been measured for models of this size). The InSight monument reuses the array.

---

## 3. Mineral and resource finds

### 3.1 The nodes

All names are real; all science is *unverified* until the SME confirms it. A "node" is a patch of worldgen blocks with a different look and a loot table; **no machine is new**.

| Node | Real basis (*unverified*) | In the game | What it gives or unlocks | Goals | Phase | MVP |
|---|---|---|---|---|---|---|
| **Iron oxide** (hematite and rusty dust) | Mars's red color comes from iron-oxide minerals in the dust. Gray hematite spheres ("blueberries") were found by Opportunity | Red-orange patches, plus a rarer gray variant | **Iron plate** (the "Fe" in the recipe stand-ins, balance sheet 2d): bake in a powered heater, with an honest note that real smelting needs a chemical helper, not just heat | A1 (why Mars is red; gray is also iron), C1 | 1 | **Yes** |
| **Perchlorate patch** | Perchlorate salts are in Mars soil (found by Phoenix); toxic; they also lower the freezing point of water | White-gray crust patches; **hazard-labeled** | **Nothing to build.** It is a hazard (the dust-off station and filters already exist) and the **sample** for case 1. No oxygen from it (see 4.3) | C1, C2, A2 | 1 or 2 | **Yes** |
| **Olivine** | Green mineral rich in magnesium and iron, mapped from orbit in places; on Earth it reacts with CO2 and water to form carbonate rock | Green sand and rock | **Carbon scrubber cartridge**: scrubbers already "lock CO2 into carbonate rock" (balance sheet §4). The cartridge needs olivine **to exist** (a recipe input), not to add a new effect | A5, B4, D2 (Earth carbon capture) | 3 | **Yes** |
| **Ice and ground ice** | Real ice lies under the soil at high latitudes (Phoenix dug it) | **Already designed:** ice lenses, survey scan, ice extractor (DESIGN Phase 2). Not a new node | Water (existing) | A2 | 1 to 2 | Existing |
| **Hydrated sulfate** (gypsum and similar) | Sulfate salts hold water in their crystals and formed in water long ago | White-yellow veins | **Water by baking**: a heater releases water from the mineral. Same idea as the Outgasser, at small scale; journal note that real soil holds only a little water (about 2 % by mass, *unverified*) | A2, C1 | 2 | Full |
| **Clay** | Clay minerals form when rock sits in liquid water for a long time; found by orbiters and Curiosity | Pale gray-green layered ground | **Better soil**: cuts the soil-treatment step before biocrust (Phase 3). Also the best place to take a sample for case files | A2, A5, B5 | 3 | Full |
| **Carbonate rock** | Found in places on Mars; shows CO2 once reacted with water | Pale layers | Teaches where scrubbed CO2 goes (A5), no mechanic | A5 | 3 | Cut first |

### 3.2 Mapping to what each one unlocks (summary)

| Unlocks | Node | How it is wired | New code |
|---|---|---|---|
| Recipe input (iron) | Iron oxide | Loot table drops a raw item; a recipe turns it into iron plate | Block, loot, recipe (data) |
| Recipe input (scrubber cartridge) | Olivine | Recipe input | Block, loot, recipe (data) |
| Water | Hydrated sulfate, ice | Existing water inputs; sulfate is a new loot item | Block, loot |
| Soil quality | Clay | A **cost modifier** on the soil-treatment recipe (a data number) | One recipe variant |
| A sample for a case | Perchlorate, clay, any node | Sample item (3.5) | Item |
| Hazard | Perchlorate | Existing exposure effect | None |

**Open item G4 (balance sheet 2d): the Phase 1 raw-resource list does not exist.** Iron oxide and sulfate are the natural answer, but I did not decide it: which machine bakes them, and the counts, are for the Technical Lead and the balance sheet. All counts are not set in this file.

### 3.3 "Rare metals": what is real, what is not (honest treatment)

| Idea | Real status (*unverified*) | Decision |
|---|---|---|
| Gold, platinum, rare earths on Mars | **No known ore.** The surface is mostly basalt-type rock. Not finding them is not proof they are absent | **Not in the game.** No invented "Mars gold" |
| Invented rare ore ("Martian alloy") | Fiction | **Avoid.** If the owner insists: name it plainly as a game item, with an audit row (fantasy rule 5 says cut it, since it teaches nothing) |
| **Iron-nickel meteorites** | Real: rovers found metallic meteorites on the surface (e.g. Opportunity) | **Yes, as the one "rare find."** A small, hard-to-spot node: dense dark rocks. Gives **nickel-iron**, the input for a hard **core drill bit** (needed for core samples in case files). The drill-bit dependency is a *design choice* |
| Manganese-rich rock | Real: Curiosity found manganese-rich rock at Gale (*unverified*), a hint about past water and oxygen | Optional fact in the clay entry; no node |
| Titanium, aluminium, silicon | In common rock and soil | Not nodes: silicon is what "G" (glass) and "B" (regolith brick) already come from |

**The honest line the journal carries:** "No one has found rich ore on Mars. Most of what is there is rock and dust. The first Mars builders would use the soil itself."

### 3.4 Not on the critical path (rule E1)

Starting stock in the dome (the Ops locker and crate, landing-spec) covers the iron for the minimum path to Dome stable and Phase 2, so nodes only make the path **faster or richer**. The Technical Lead and the balance sheet confirm the numbers; if the minimum path needs an off-dome trip, E1 is broken and I will rework it.

### 3.5 The sample kit (shared with the case files)

| Item | What it is | Real basis (*unverified*) |
|---|---|---|
| Sample tube | One stackable item per sample; label records **site, depth, node type, sol** | Perseverance sample tubes |
| Core drill bit | Needs nickel-iron; required for samples at depth | Rover drill bits |
| Clean-hands rule | **Not built.** Contamination (Earth microbes) appears only as a case-file alternative explanation, not as a mechanic | Scope cut |

---

## 4. "Signs of life?" investigation loop

### 4.1 Frame (ill-structured)

**Problem.** Show a real kind of reasoning: a strange result might mean life, and proof is hard. The risk is the other direction from "no content": a game that lets a player *find life* would teach a falsehood. Also the story already has **fictional** dormant organisms (the thawlings, [FICTION]). The two must not blur.

**Disciplines.** SME (real cases, what each test can and cannot say), ID (what goal this serves and how to give feedback on reasoning, not on an answer), Technical Lead (a board screen and per-player state), Artist (icons, a lab-bench look).

**My view (Game Designer).** Make the argument the game. The player should feel the pull of "that could be life!", then feel what a good test does to that feeling.

### 4.2 The loop (five steps, about 12 to 15 minutes per case; invented)

| Step | The player does | System shows |
|---|---|---|
| 1. **Find** | Reaches a site with an odd result (a BELL log line: "Gas release in the soil sample"). Not a quest: a line in the colony log and a case file in the journal | Case card opens when a **sample** of the right type is placed in the lab bench |
| 2. **Collect** | Takes sample tubes with the kit (3.5); the label records site and depth | Evidence slots on the board: *What I saw*, *What I measured*, *What the controls show* |
| 3. **Ask what else could do this** | Looks at four **explanation cards**: Living things · Chemistry in the soil · Geology (rock and water, heat) · A mistake (instrument or contamination) | The cards start unmarked: none is ruled in or out |
| 4. **Test** | Picks up to four tests at the lab bench. Each costs about 0.1 sol of bench time and power (*invented*), not consumables. A **prediction first**: "What will the heated sample do?" (practice, see 4.4) | Each result marks which cards it still allows, with a plain-language reason |
| 5. **Claim** | Chooses a **confidence word** on the ladder below and writes (or taps) the reason | Feedback on the *reasoning* (4.5). Never a verdict about life |

**The ladder (the player can never go above rung 3).**

| Rung | Wording (draft) |
|---|---|
| 0 | "Not enough to say." |
| 1 | "Something unexplained. More tests needed." |
| 2 | "Non-living causes explain this as well or better." |
| 3 | "Consistent with life, but not enough to say. Other causes are still open." |
| (does not exist) | "Life confirmed." There is no such option anywhere in the game. |

The ladder is a simplified idea in the spirit of real "how sure are we" scales used by life-detection researchers (*unverified*; the SME names the real scale; I do not copy its levels).

### 4.3 Case file 1: "The fizz test" (MVP; Phase 2, after the Thaw gate)

Based on the 1976 Viking life-detection experiments, **as a story of reasoning** (all details *unverified*, SME to confirm before any text is written):

| Beat | What happens | The lesson |
|---|---|---|
| Start | Viking replica (or any perchlorate patch sample). The player adds water with nutrients to a soil sample at the lab bench (inside the dome, **normal air pressure**, so liquid water is fine). Gas comes off. "It fizzes!" | The pull toward "life" |
| Test A (control) | Heat a second sample first, then repeat. **No fizz.** | Looks like "heating killed the living things". But the heat could also destroy a **chemical**. The result is consistent with both. The player learns that **a control must be able to tell two ideas apart** |
| Test B | Search the sample for **carbon-based molecules** (organics). Real Viking found none; later rovers did find some (*unverified*) | No organics makes life less likely, not impossible; the player sees that **a missing test result is not proof** |
| Test C | Add **perchlorate** (the soil salt from the perchlorate patch) to sterile test soil from the dome stores. It fizzes too | **Chemistry can make the same signal.** The player sees an alternative explanation reproduced |
| Claim | Rung 2 or 3 are both fair (the real debate is not closed in the story the game tells; *unverified*). Rung 0 or 1 is fair early. A claim at the wrong strength gets feedback (4.5) | Argue from evidence; how scientists changed their minds as new tests arrived |

Real-world closure line (always shown at the end, **never "we found life"**): "No spacecraft has found life on Mars. The question is still open, and scientists keep testing it."

### 4.4 Where it ties to goals (honest alignment, ID decides)

| Goal | How the loop serves it | Strength and caveat |
|---|---|---|
| **B3 Prediction** | "What will the control do?" asked before each test | **Practice only, not scored.** B3's measure counts forecast accuracy per phase (≥ 3 per phase, +20 pp). A case prediction has no single keyed answer when the world is ambiguous, and counting it would add noise to the B3 tally. **Recommendation: do not count case predictions as forecasts.** The ID may opt in for the ones with a keyed answer (for example, "the heated sample will not fizz"). |
| **C2 Design, test, improve** | Choosing a control, noticing it cannot separate two ideas, adding a better test. This is a **stretch** of C2's current definition (diagnose and fix a failing system) | Secondary. **ID to confirm** whether C2's measure may use it; until then it supports C2 only informally, and I do not claim coverage |
| **D1 Ethics** | If life might exist, terraforming may harm it. The real uncertainty is why the question matters, and the story's thawlings are the fiction that dramatizes it | Supports D1 without confirming life. The thawlings stay fiction (4.6) |
| A2, A5 | Case 1 uses water and gas behavior; case 4 uses the player's own biocrust | Light |
| Not in A1 to D2 | "Argue from evidence" as a skill is not a listed goal. **Proposal to the ID:** treat it as an optional goal extension or leave it out of the alignment table. Not decided here | Needs an ID decision, no goal ID changes in this file |

### 4.5 Feedback (shared format: what happened, why, what to try)

Feedback rewards reasoning, not a conclusion. Drafts (ID pass pending):

| Situation | Message (draft) |
|---|---|
| Claim says rung 3, but chemistry is still open | **What happened:** your claim says life is still possible. **Why:** your tests did not separate life from soil chemistry. **Try:** a test that gives different results for each. |
| Claim says rung 2 after Test C | **What happened:** you say non-living causes fit well. **Why:** perchlorate in sterile soil made the same fizz. **Note:** that does not prove there is no life. Rung 3 and rung 2 can both be fair. |
| No control run | **What happened:** you made a claim with no control. **Try:** run the heated sample, then compare. |
| Player skipped tests | No penalty. The board says which explanations are still open. |

### 4.6 Never confirm life, and the fiction line

| Rule | Spec |
|---|---|
| **SOL-1 (proposed content rule, ID to add)** | No text, icon, sound or log line says life was found on Mars. Outside text labeled [FICTION] it never says "alien", "organism found", or "life detected". CI check on the key set |
| The ladder has no top | Rung 3 is the maximum (4.2) |
| Samples come only from real-style sites | Never from Fridge B or the thawlings. Fridge B stays fiction |
| A fiction note on the case board | "In this game's story, hidden animals wake up as Mars warms. That is made up. This case file is about how real evidence works." (DESIGN fauna rules require the journal to say so every time the fauna appear) |
| After a case | A one-question recall check, not scored: "Has life been found on Mars?" Choices: Yes / No, not so far / No one knows, because no test is possible. Keyed: "No, not so far." Logged for the ID. Misconceptions show up in data (6.2) |

### 4.7 Ties to Phase 3 biomass, and why gates are not affected

| Item | Rule |
|---|---|
| Biomass | Counts **seeded flora only** (DESIGN §1). A case, a claim, or a "positive" test **never changes biomass**, the Index, or fauna |
| **Case 4, "Positive control" (Phase 3, full version)** | After the first biocrust grows, the player runs the **same instrument** on their own biocrust. A clear positive appears. The player sees what a real biological positive looks like against the ambiguous Mars soil: this is the idea of a positive control. It is the strongest A5 and B3 link in the set. It **opens only after F8 resolves** (it shows CO2 and O2 changes; CR-F9) |
| Gates | No gate reads a find or a case. Gates read Index values and machine states. A case result is stored apart from `PlanetState` (a small per-player store, like Keeper's Record evidence) |
| The "habitable" link | A case file has a **habitability check** with four lines the Monitor already shows: liquid water, temperature, radiation, pressure. These **read** the Index; they change nothing. As the player terraforms, a site that failed the check can pass. That is "your progress provokes the planet", and it separates **habitable** (a place that could support life) from **inhabited** (life is there), a real distinction |

### 4.8 More cases (full version; each is data plus one SME check)

| Case | Phase | Based on (*unverified*) | Lesson |
|---|---|---|---|
| 1 The fizz test | 2 | Viking experiments | Controls; chemistry vs. biology |
| 2 Spots on a rock | 3 | A rock with patterned spots that Perseverance studied; NASA called it a *potential* sign in 2025 (*unverified*) | Non-living chemical reactions can make patterns. The sample is waiting for Earth labs |
| 3 A gas in the air | 2 or 3 | Methane seen in the air by Curiosity, rising and falling by season (*unverified*) | Gas could come from rock or from life; **what we do not know is shown honestly** |
| 4 Positive control | 3 | The player's own biocrust | What a clear positive looks like |

---

## 5. How finds help make Mars habitable (existing levers only)

| Lever (Index) | Which finds feed it | How (existing machine or recipe) | New? |
|---|---|---|---|
| Pressure | Hydrated sulfate (a little gas and water on baking) | Existing Outgasser idea, small scale | No |
| Temperature | None | Emitters and mirrors stay as designed. "A find that warms the planet" would be a new lever and would invite F5 and F10 confusion | No |
| Hydrosphere | Ice lenses, sulfate | Existing ice extractors and bake | No |
| O2 | Olivine (indirectly, by keeping scrubbers available) | Rule A: **CO2 is the oxygen source.** Perchlorate is **not** an oxygen source in the game (it would break the Phase 3 mass balance and teach "oxygen from nothing") | No |
| CO2 | Olivine feeds **carbon scrubbers** (lock CO2 into rock, no O2) | An input to an existing machine; no new source ID, so CR-F10 keys are unchanged | No |
| Biomass | Clay (cheaper soil step) | A cost modifier on an existing recipe | No |
| Knowledge | Everything | Journal, case files | n/a |

**New levers: none justified.** Two reasons. (1) The Index is six parameters with attributed sources (CR-F10 forecasts resolve on a named contribution); a seventh source from the map would put net-change forecasts at risk. (2) Rule A is the only version of Phase 3 where plants' O2 is physically true. **Revisit if** playtests show finds feel disconnected from the planet: the safer response is a **cost modifier** (a data number), not a new lever.

---

## 6. Content rules, misconceptions and risks

### 6.1 Fit to the Phase 3 gates

No gate, hold, or drift event reads a find. Finds change only **recipe inputs and costs** in existing machines. Phase 3 balancing (CO2 to O2, scrubbers, the O2 window of 21.3 to 23 percent at 75 kPa) is unchanged. If olivine lowers scrubber cost, the balance sheet needs a new line, and the §3a curves must be re-run (**not done here**).

### 6.2 Misconception risks

| Risk | Mitigation | Owner |
|---|---|---|
| "Life was found on Mars" | Ladder has no top; SOL-1; closure line; recall check (4.6); fiction note | ID, SME |
| "The rovers are near each other, or near our dome" | Option A replicas with the real site named on the plaque; a one-line scale note in the Hall | Game Designer, SME |
| "Those rovers are still working" | Each entry says *ended* or *still working*; dates *unverified* | SME |
| "Opportunity said 'my battery is low'" | Never quoted as a rover message (misattribution) | SME |
| "Mars is full of gold or rare metals" | Honest line in 3.3 | SME |
| "Iron comes out of dirt with just heat" | Entry names the missing step | SME |
| "Perchlorate is free oxygen" | Listed as hazard only; no oxygen recipe | SME |
| "Olivine would fix Earth's climate quickly" | D2 note: real on Earth but slow and huge-scale (*unverified*) | SME |
| "The thawlings are evidence of real life" | 4.6 fiction note and sample rule | Game Designer |

### 6.3 CR-F9 and CR-F10: do the finds reveal forecast answers?

Every find entry and every case line is a message with a `reveals` field. **A held entry stays hidden, including journal copies** (CR-F9 2026-11-28 addition), until the named forecast has resolved or expired for that player.

| Find or text | Could reveal | Rule |
|---|---|---|
| Sojourner HS line on dust and power; Opportunity; InSight; Spirit | **F1** (storms cut solar power) and the F1 and FI-1 mechanism | `reveals: forecast.f1`. Those entries are Phase 2 sites by design. The Phase 1 site (Sojourner) keeps the dust line **only** in the held HS text |
| Curiosity (surface radiation) | **F2** and **F7** | `reveals: forecast.f2, forecast.f7`; Phase 3 site, held until both resolve |
| Phoenix; ice sublimating in dry air | **F4a** | `reveals: forecast.f4a` |
| Clay or sulfate "formed in liquid water when the air was thicker" | **F4b** (the HS reading hints that thicker air keeps water) | `reveals: forecast.f4b`, HS text only |
| Case 1 (a liquid at normal pressure) | F4a "why" | Case 1 opens after the Thaw, so F4a and F4b are long resolved |
| Case 4 and the biocrust test | **F8** (flora: O2 up, CO2 down) | `reveals: forecast.f8`; opens after F8 resolves |
| Olivine and scrubbers | **F8, FI-4** (net CO2) | Entry says what scrubbers do, never what net CO2 will do; scrubber stays its own source ID |

**CR-F10.** Finds create **no new source ID** and change no contribution formula. The only candidate risk is a stronger scrubber (olivine), which changes a **rate in data**; the solver test (clean panels mid-storm, scrub mid-forecast) is **not run** here and must run when the numbers exist.

### 6.4 Other risks

| Risk | Detail | Mitigation |
|---|---|---|
| **Scope creep against the budget** | The MVP adds 9 to 14 Technical Lead sessions (about 15 to 30 percent of the Stage 3 Technical Lead plan) and has no budget row | Not Stage 3; Stage 4 with a funding swap (section 7) |
| Content cost per case | Each case needs SME review | Cases are data-driven (JSON), one reviewed spec each |
| "Hall" becomes a checklist | Players demand counts | Option A; revisit on evidence |
| Classroom time | A 15-minute case is about 2 percent of an 11 to 12 hour classroom run | **Classroom default: off for monuments, case 1 optional** (a teacher toggle). A teacher may switch on |
| Fiction layered on fiction | Hana's replicas plus thawlings plus Basu's sites | Phase 1 has Hana as lead voice; Phase 2 is Basu's; replicas need no second voice. Character load check (story bible 4.4) still passes: Hana only |
| Distance and suit O2 | Phase 2 and 3 trips are longer | Rings are *invented*; the Technical Lead measures walk times |
| Science drift | All facts here are from memory | SME review before any text, model, or plaque |
| Art licenses | NASA imagery is usually reusable, but each image needs checking | Art reference audit |

---

## 7. Where it fits, cost, and what to cut first

### 7.1 Placement in the stages

| Stage | What | Why there |
|---|---|---|
| **Stage 3 (now)** | **Nothing built.** Two cheap rules only: (1) the journal entry list stays data-driven (not hard-coded) so finds can add entries; (2) CR-F9's `reveals` field is already on every entry (it is). | Stage 3 is 40 to 66 Technical Lead sessions (stage3-plan §3), "5 to 8 times Stage 2", and an exit criterion is a clean spawn to Phase 2 run. Adding finds puts the exit at risk |
| Stage 3 optional | If slices 0 to 8 finish with spare time: **one owner-playable mini**: Pathfinder site + one iron oxide patch. About 2 to 3 Technical Lead sessions and 1 to 2 Artist sessions (*unverified*) | Gives the owner something to react to |
| **Stage 4, Phase 2 build** | The **foundation** (3 to 5 Technical Lead sessions) built **with** Basu's drill sites, the survey scan and ground-ice lenses, which need the same structure placement, sealed-cache interaction and journal unlock. Then the MVP content. | Shared plumbing is the cheapest place |
| Stage 4, Phase 3 build | Olivine, Perseverance, case 4 | Needs the biocrust |
| Stage 5 (alpha) | Full version of what survives the cut; balance run; SOL-1 check | |
| Stage 6 (beta) | Playtest the case loop and the recall check with real readers, including a misconception check | The only way to learn whether students leave with the right belief |

### 7.2 Cost (rough, *unverified*; sessions as in stage3-plan)

A **session** is the Technical Lead slice-sized unit: measured thin slices 5 to 7 took about 8 to 35 minutes of agent time (stage3-plan §3, budget §6.5); bigger slices are "about one session each" and not measured. Artist, Game Designer, ID and SME rates are **not measured**; I use "one session = one spec or one asset". **No hours are converted and no budget row is changed.**

| Work | Technical Lead | Artist | Game Designer | ID | SME | Note |
|---|---|---|---|---|---|---|
| Foundation (`finds/*.json`, placement, discovery trigger and event, journal unlock, Hall page, log events) | 3 to 5 | 0 | 1 (data spec) | 0.5 | 0.5 | Reuses starter-dome placement and the slice 4 journal |
| 3 heritage sites (MVP) | 0.5 | 3 to 4 (models, plaque, pad) | 1 (3 entries and plaques) | 1 | 1 | |
| 3 mineral nodes (MVP) | 2 to 3 (worldgen, loot, 2 recipes) | 2 (textures) | 0.5 | 0.5 | 1 | Blocks as variants of existing ones |
| Sample kit (tube and drill bit) | 1 | 0.5 | 0 | 0 | 0 | |
| Case file 1 + board screen | 4 to 6 (screen, per-player store, pure-Java test engine, tests) | 1 (icons, bench) | 1 | 1 to 1.5 | 1 | A screen the size of the Monitor is the biggest single item |
| **MVP total** | **about 9 to 14** | **about 6 to 8** | **about 3 to 4** | **about 2 to 3** | **about 3 to 3.5** | Against Stage 3's 40 to 66 TL sessions |
| Full version extra (6 more sites, 3 more nodes, 3 more cases, HS depth, Hall polish) | about 8 to 12 | about 12 to 16 | about 6 | about 3 | about 4 | The Artist is the larger line |

**Funding.** There is no budget row. Stage 4's cut list totals about 435 h of buffer (budget §4.3). **Recommendation:** fund the MVP by *swapping* an item of similar size (for example cut #9, merging two pairs of machines, or cut #1 if the owner prefers), not by growing Stage 4. The Producer prices it. The Instructional Designer blocks nothing here because no Primary goal's only teaching or measure moves.

### 7.3 What to cut first (ordered; stop when the budget is met)

| # | Cut | Learning cost | Appeal cost |
|---|---|---|---|
| 1 | Sites beyond the MVP three (and the Lost missions wall) | None unique | Low to medium |
| 2 | Carbonate, clay and sulfate nodes (keep iron oxide, perchlorate, olivine) | A2 loses one node | Low |
| 3 | Survey compass item (already cut), HS read-more depth | HS depth only | Low |
| 4 | Cases 2 and 3 | None unique | Medium: the loop gets thinner |
| 5 | Case 4 (positive control) | **B3 and A5 link lost**; cut last of the cases | Low |
| 6 | Hall page → plain journal entries | D2 timeline lost | Low |
| 7 | Nickel-iron drill-bit dependency | Rare-find hook lost | Low to medium |
| **Never cut** | SOL-1, the ladder with no top, the `reveals` fields, the fiction labels, rule E1 | | |

If only one thing survives, it is **case 1**: it is the only find with a learning loop of its own (B3 practice, C2 stretch, D1).

### 7.4 Fantasy and authenticity audit rows (to add to the DESIGN.md table)

| Element | Real or fantasy | Purpose | Activity matches skill? | Negative-learning risk | Status |
|---|---|---|---|---|---|
| Hana's replica heritage trail | **Fiction** (replicas by a crew member); the machines are real | Motivation, scale (A1), history (D2) | Walking, reading | Low with "replica" and the real site on the plaque | Proposed |
| Mineral nodes (iron oxide, olivine, perchlorate, sulfate, clay) | Real minerals; **placement and machine use simplified** | Resource planning (C1); links to A2, A5 | Choosing where to dig, planning a trip | Medium: "easy to get metal from dirt"; entries state the missing step | Proposed |
| Iron-nickel meteorite node | Real kind of find; rarity is invented | A "rare find" without inventing a metal | Spotting a dense rock | Low | Proposed |
| "Signs of life?" case files | Real kind of reasoning; the cases are simplified retellings | Argument from evidence, D1 | Testing, comparing | **High if mishandled** ("life found"); SOL-1 | Proposed |
| Confidence ladder | Simplified teaching device | Honest uncertainty | Choosing a rung with a reason | Low | Proposed |

---

## 8. Questions for the owner (at most 3)

1. **Replicas or real hardware?** (ill-structured: wonder against a false "they landed next to each other" belief.) **Recommend A: Hana's replicas**, with the real site named on every plaque. The fallback is the compressed map with a scale note. *Cost of A:* a little less "I found the real thing".
2. **Is "Signs of life?" allowed to end at "not enough to say" every time?** (values call: the owner's wording was "things to help us find out life".) **Recommend yes, always.** The game never finds life, because no one has; the thawlings remain fiction. The alternative, letting a late case "find a sign", would teach a falsehood and clash with the fauna fiction rule.
3. **Stage and money: build the MVP in Stage 4 Phase 2 and fund it by a cut-list swap?** (ill-structured: scope against budget.) **Recommend yes, and an optional Stage 3 mini** (one Pathfinder site + one iron patch) only if slices 0 to 8 end early. Not before.

---

## 9. Assumptions, and what would make this better defined

| Assumption | Check |
|---|---|
| Ring placement in a custom dimension can be datapack-only | Technical Lead, a small spike |
| Starting stock covers the minimum iron path (E1) | Balance sheet 2d and the Technical Lead |
| Players will read a plaque | Playtest observation |
| Classroom default off is acceptable | Teacher feedback, Stage 6 |
| The ladder reads as fair to ages 12 to 18 | Playtest, recall check |
| Every real-world fact | **SME review: not done.** All *unverified* |

**Would make it better defined:** the SME review; one paper prototype of case 1 with three readers aged 12 to 16 (cards only; I have run none); a Technical Lead placement spike; a Producer price.

## 10. Handoffs

| To | Ask |
|---|---|
| SME | Verify every *unverified* fact: rover and lander dates, sizes, endings, Viking and later results, the real life-detection scale, hematite, olivine and carbonation, sulfate water, meteorites, manganese, the Opportunity "last message" origin, and the 2025 rock-spot claim. Confirm what is safe to call "potential" |
| Instructional Designer | Alignment of case files (B3 practice not counted, C2 stretch, "argument from evidence"); add SOL-1; grade 6 to 7 pass; the recall check |
| Lead Artist | Monument models (MVP three), plaque, sample tube and bench icons, node textures, lighting for dust weathering |
| Technical Lead | Feasibility: ring placement, the board screen, per-player case store apart from `PlanetState`, which machine bakes iron oxide and sulfate (open item G4) |
| Producer | Price the MVP and name the swap |
