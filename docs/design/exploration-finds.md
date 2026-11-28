# Exploration Finds: rover sites, mineral nodes and "Signs of life?" (design proposal)

**Owner:** Lead Game Designer · **Status:** **revised 2026-11-28 after the owner's answers** (heritage sites = Hana's replicas, accepted; MVP in Stage 4 Phase 2, accepted; "Signs of life?" case files **may end in "life"**, owner's choice); not built, not playtested (design walkthrough only) · **For:** the owner (1 question, section 8), Producer (budget fit), Lead Instructional Designer (alignment, wording), SME (every science line), Lead Artist (monument models), Technical Lead (feasibility)

**Owner request (BACKLOG, 2026-11-28):** "these things could be life on mars?, finding rovers, rare metals or things to help us find out life and making mars a habitable planet".

**Reads with:** [DESIGN.md](../../DESIGN.md) (Fantasy and Authenticity, Phase 3, gates), [LEARNING.md](../../LEARNING.md) (A1 to D2), [story-bible.md](story-bible.md) §3.4, §4.3, [stage3-plan.md](../tech/stage3-plan.md), [budget.md](../production/budget.md) §4.3, [landing-spec.md](landing-spec.md) §5 (journal entry format), [f1-storm-spec.md](f1-storm-spec.md), content rules CR-F9, CR-F10 in `docs/learning/content-rules.md`.

**Tags:** *invented* (game number), *design choice*, *unverified* (a science or history fact written from memory). **The SME's review `docs/science/exploration-finds-review.md` now exists; I used it in this revision for the life-detection material (section 4). Facts I did not find in it, and every site size and date, stay *unverified* until the SME confirms them.** I opened no other sources. Nothing here ships as fact before the SME and the Instructional Designer (ID) have checked it. No NGSS codes are claimed; the ID owns alignment.

---

## 0. Summary

| Question | Answer in one line |
|---|---|
| What is it? | Three optional kinds of discovery on the Mars map: **heritage sites** (full-size models of real rovers and landers, with a plaque and a journal entry), **mineral nodes** (real rock and soil types that feed existing machines), and **"Signs of life?" case files** (a sample-test-argue loop in two labelled kinds: REAL EVIDENCE cases stay open; FICTION cases may end in "life"). |
| Does it add a new Index lever? | **No.** Finds are inputs to existing levers, or knowledge. A new lever would break the Phase 3 mass balance (rule A) and the forecast keys (CR-F10). |
| Does it touch the gates? | **No gate reads a find.** Gates read Index values and machine states only. |
| Honest answer on "rare metals" | Mars has **no known ore of rare metals**. The game does not invent one. The real "rare" finds are iron-nickel meteorites and manganese-rich rock (section 3.3). |
| Honest answer on "life on Mars" | **No life has been found on real Mars, and no sign has been confirmed** (SME review C). Case files that mirror real evidence (Viking-style, Cheyava Falls-style) stay open and are scored on reasoning. A **labelled fiction case** (the thawlings; the player's own biocrust) can end in "life", because in the story the evidence supports it. |
| Minimum viable version | 3 heritage sites + 3 mineral nodes + **2 case files (case 1 real, F1 fiction)**. About **11 to 17 Technical Lead sessions** and about 6.5 to 9 Artist sessions (*unverified*; was 9 to 14 and 6 to 8 with one case, section 7.2). **Not Stage 3**: Stage 3 is already 40 to 66 Technical Lead sessions. Land it in **Stage 4, Phase 2**, next to Basu's drill sites and the survey scan, which need the same placement and journal plumbing. |
| Biggest risk | Scope. Second biggest: **a student carries the fiction "life" ending over to real Mars** (section 4.8). |

**Two Goals check (instructional value and market appeal).** Instructional: real hardware at true scale, real minerals and an argument-from-evidence loop carry A1, A2, C1, C2, D1 and D2 and give B3 a practice mode. Appeal: this is the Explorer player type's best content (DESIGN.md Bartle table lists only the survey scan and Basu's sites today) and the "rovers in the news" relevance hook (DESIGN.md, Relevance). Where they pull apart: a full list of rovers is a collectible checklist, which the Zeigarnik caution in DESIGN.md says to avoid. I chose learning-safe over appeal-max (section 2.4) and flag it. The owner's "life" ending adds appeal (a payoff) and a new instructional risk (transfer); sections 4.7 and 4.8 are the controls.

---

## 1. Principles for every find (apply the six fantasy rules)

| # | Rule | Source |
|---|---|---|
| E1 | **Optional.** No find is on the critical path of a gate. The minimum path to every gate works with no find at all (starting stock covers the first iron, see 3.4). Exploring is the reward for finishing a job early, never a chore. | Pillar "pressure, not grind"; DESIGN.md §5 balance rules |
| E2 | **Every find has a real name and a job.** A find that teaches nothing and does nothing is cut (rule 5, seductive detail). | DESIGN.md fantasy rule 5 |
| E3 | **Find, then learn.** The world object comes first; the journal entry unlocks when the player reaches it. Never a pop-up. | landing-spec §5.4 |
| E4 | **Two depths.** MS text (80 words max, story bible 6.1) and an HS "read more". Same entry JSON as landing-spec §5.2 (`goal_ids`, `reveals`, `names`). | landing-spec §5 |
| E5 | **Fiction is labeled.** Anything invented says so in the entry (the "real vs. game" note). Fiction case files carry the FICTION tag on every surface (section 4.7). | DESIGN.md fantasy rule 3; SOL-1 revised |
| E6 | **No find reveals a forecast answer early.** Every entry declares `reveals`; text that could answer an open forecast is held (section 6.3). | CR-F9 |
| E7 | **No new Index lever, no new source ID** (section 4). | CR-F10, rule A |
| E8 | **Evidence is own-player.** Discovering is per planet (shared, like a gate), reading and testing are per player. | Group rules, Keeper's Record |
| E9 | **No score, no rank, no completion bar** (section 2.4). Case-file feedback is on reasoning, not a score. | DESIGN.md Motivation table |
| E10 | **Real and fiction never share a sample.** A fiction sample cannot be tested in a real case and the reverse; the bench shows the source tag. | SOL-1c (section 4.7) |

---

## 2. Heritage sites: rovers and landers as monuments

### 2.1 Decision to make first: where is the real hardware? (ill-structured)

**Frame.** Real rovers sit thousands of kilometers apart on real Mars, each at its own landing site. Our planet (`mars:planet_1`) is one generic site with a dome. Placing real hardware near the dome says, without a word, "they landed next to each other". That is a negative-learning risk (fantasy rule 3). The owner wants the *feeling of finding a rover*. These two pull apart. **Decided by the owner 2026-11-28: option A, Hana's replicas.**

**Disciplines.** SME (what is true about each site and whether a compression is acceptable), Artist (scale and look), Technical Lead (placement cost), ID (negative-learning check), Producer (cost).

**One perspective (mine, Game Designer).** The find should feel like a discovery and cost no false belief.

| Option | What the player finds | Learning | Appeal | Cost | Risk |
|---|---|---|---|---|---|
| **A. Hana's heritage trail** (replicas) | Full-size replicas that the First Shift crew (Hana, who "builds things") put up from spare parts, each with her plaque: "Not the real one. The real one is [far away]." | A1 scale at 1 block = 1 m; true site named; no false belief; fits the story-bible "traces left by earlier crew" (§4.3) | Medium to high: a trail with a human voice; less wonder than "the real thing" | Same models as B | Low. A new fiction element: replicas are labeled [FICTION] (audit row) |
| B. Compressed map, real hardware | The real machines, as if they landed here, with a map note "Not to scale: on real Mars these are thousands of km apart" | Scale lesson is explicit but undone by the placement | High wonder | Same models | Medium: a correction plaque fights what the player sees |
| C. Real distances, reachable late | Hardware at true separations, reached by transit or vehicle in Phase 3 | Most honest | Low for most players: too far, too late | High (vehicles, travel time) | High scope |

**Recommendation (accepted): A.** It keeps "I found a rover" and adds a person, Hana, in the voice the story already has. It needs one fiction audit row (section 7). The owner accepted replicas as "finding rovers". B stays the documented fallback only if playtests say replicas feel fake.

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

## 4. "Signs of life?" case files: two kinds, one honest ladder

### 4.0 Owner decision and framing (2026-11-28)

The owner chose to **allow a "life" ending**. The honesty constraint stays: the real science is unresolved, and the SME review says "No life has been found on Mars. No sign of life has been confirmed" and "Any 'life found' outcome: do not show" outside a marked fiction layer (`docs/science/exploration-finds-review.md` section C). The two are reconciled by **splitting case files into two kinds**, always labelled.

| | **Real-evidence case** (tag: REAL EVIDENCE) | **Fiction case** (tag: FICTION) |
|---|---|---|
| Based on | Viking-style and Cheyava Falls-style evidence: what real instruments really saw | The game's own story: the thawlings and the player's own biocrust |
| Sample source | Real-style sites: heritage replicas, perchlorate and clay nodes | Thawling nests and ice (Basu's sites); the player's own biocrust |
| Top rung reachable | **Rung 3** ("Consistent with life, but other causes are still open") | **Rung 4** ("Life is the best explanation here"), only with the evidence in 4.4 |
| What is scored | The **quality of the reasoning** (controls, alternatives, claim strength matched to evidence) | The same reasoning score, **and** whether the claim is supported by evidence the player gathered |
| Ending text | Open: "No life has been found on Mars, and no sign of life has been confirmed. Scientists keep testing." Never "we found life", never "no life exists" | A story ending. In the story, the sleepers are alive, and the board says it is the story |
| Why this is true to the game | The real question is open, so the game cannot close it | In this world the evidence really does support life: the player seeded the biocrust, and the thawlings are game entities that move, feed and grow |

**Problem type (ill-structured).** Fun, learning and honesty pull apart: a "life!" payoff is the most wanted ending, and a student who leaves thinking real Mars has life would hold a false belief.

**Disciplines.** SME (real cases, what each test can and cannot show, the real confidence scale), ID (alignment, wording, the recall check), Technical Lead (board, per-player store, data validation), Artist (the Fiction tag and frame), Producer (cost).

**My view (Game Designer).** Let the player feel a real "yes" once the evidence earns it, so the **top rung means something**, and make sure the fiction label and the real/fiction contrast do the teaching: *the same method gives an open answer on real Mars and a firm answer in the story, because the story has the evidence and real Mars does not yet.*

### 4.1 The loop (five steps, about 12 to 15 minutes per case; invented)

| Step | The player does | System shows |
|---|---|---|
| 1. **Find** | Reaches a site with an odd result (a BELL log line, for example "Gas release in the soil sample"). Not a quest: a log line and a case file in the journal | Case card opens when a **sample** of the right type is placed in the lab bench. The bench reads the sample's **source tag** (REAL-BASED or FICTION) from the sample item and shows it |
| 2. **Collect** | Takes sample tubes with the kit (3.5); the label records site, depth, source tag and sol | Evidence slots on the board: *What I saw*, *What I measured*, *What the controls show* |
| 3. **Ask what else could do this** | Looks at **explanation cards**: Living things · Chemistry in the soil · Geology (rock, water, heat) · A mistake (instrument or contamination) | All cards start unmarked: none is ruled in or out. Cards look equal (SME rule: do not rank "life" above "chemistry") |
| 4. **Test** | Picks up to four tests (fiction cases: up to five) at the bench. Each costs about 0.1 sol of bench time and power (*invented*), no consumables. **Prediction first** (practice, 4.5) | Each result marks which cards it still allows, with a plain-language reason |
| 5. **Claim** | Chooses a rung on the ladder and a reason | Feedback on the **reasoning** (4.6) |

### 4.2 The confidence ladder (top rung only for fiction cases)

The wording is a simplified teaching device. The real scale the SME found (the CoLD "Confidence of Life Detection" scale, seven steps, Cheyava Falls at step one) is **not verified in a NASA source**, so the game says "a scale of confidence" and does not copy its steps (SME review C.2).

| Rung | Wording (draft; ID pass pending) | Real-evidence case | Fiction case |
|---|---|---|---|
| 0 | "Not enough to say." | Fair early | Fair early |
| 1 | "Something unexplained. More tests needed." | Fair | Fair |
| 2 | "Non-living causes explain this as well or better." | Fair | Fair if chemistry is not yet ruled out |
| 3 | "Consistent with life, but other causes are still open." | Fair, and **the top** | Fair if some cause is still open |
| **4** | **"Life is the best explanation, and the other causes were tested and ruled out. In this story."** | **Shown greyed with the line "No real Mars case has reached this."** Cannot be picked | Pickable only after the evidence in 4.4 |

Why show the locked rung on a real case: the player learns that the bar exists and how high it is. The line "No real Mars case has reached this" is the SME-safe version of "the question is unresolved" (CoLD wording itself held until sourced). **Both** statements are *unverified* until the SME confirms the exact wording.

### 4.3 Case file 1: "The fizz test" (REAL EVIDENCE; MVP; Phase 2, after the Thaw gate)

Based on the 1976 Viking life-detection experiments, **as a story of reasoning**. SME-checked facts available now: Viking's biology tests gave results some scientists called weak positives; the organic-molecule test found none; perchlorate, found by Phoenix, led scientists to revisit the Viking data (NASA, SME review C). Anything beyond that is *unverified* (for example that heating destroys organics when perchlorate is present: SME marks it memory).

| Beat | What happens | The lesson |
|---|---|---|
| Start | A perchlorate-patch sample (or Viking replica sample). The player adds water with nutrients at the lab bench (inside the dome, normal air pressure). Gas comes off. "It fizzes!" | The pull toward "life" |
| Test A (control) | Heat a second sample first, then repeat. **No fizz.** | Consistent with "heat killed it" and with "heat destroyed a chemical". A control must be able to tell two ideas apart |
| Test B | Search for carbon-based molecules. Real Viking found none | A missing result makes life less likely, not impossible |
| Test C | Add perchlorate to sterile test soil from the dome stores. It also fizzes | Chemistry can make the same signal |
| Claim | **Rung 2 and rung 3 are both fair.** Rung 4 is locked. Rung 0 or 1 is fair early | Argue from evidence; scientists changed their minds as tests arrived |

Closing line (REAL EVIDENCE tag, always shown): "No life has been found on Mars, and no sign of life has been confirmed. Scientists keep testing the question." The board never says "Viking found life" or "Viking found no life" (the SME review lists both as misconceptions).

### 4.4 Fiction cases: where "life" is a true ending

Both fiction cases carry the FICTION tag everywhere (4.7) and use only fiction samples.

**Case F1: "Are the sleepers alive?" (FICTION; MVP; Phase 2, after the Thaw gate and Basu's first notes).**

Sample: the crust and ice at a thawling nest (Basu's "Group S"). The player has seen the animals move, but the **nest material** is the question: is it alive, or is it ice and salt?

| Test | Result in the story | What it rules in or out |
|---|---|---|
| Warm the sample with water (prediction first: "Will it change?") | It stirs and gives off a little gas | Chemistry or life (both allow it) |
| Heated control (sterilised first) | Nothing | Heat killed it, or heat destroyed a chemical (allows both) |
| **Repeat over three bench cycles** | Gas output **holds steady and grows**. Reagent-driven chemistry (the perchlorate fizz from case 1) **fades** as the reagent runs out | **Rules out simple chemistry**: the same method that left case 1 open now separates the two ideas |
| Sterile-soil control with the same water | Nothing | Rules out a mistake: contamination from the dome |
| **Positive control**: run the same test on a patch of the player's own biocrust (Phase 2 has no biocrust yet, so the bench has a **reference sample of known-living culture supplied by Basu's field kit**, labelled FICTION; in Phase 3 it can be the player's own biocrust) | Same signature | Shows what "alive" looks like on this instrument |

**Rung 4 is pickable only if** (data, per case): at least one test removed each non-life card, the signal was **repeated** on a second sample, and the positive control was run. If the player claims rung 4 without all three, the feedback says which is missing (4.6). If the player claims rung 3 when everything is ruled out, feedback says "your evidence supports more than you claimed" (both are shown honestly, no penalty to progress).

Ending (FICTION tag): "In this story, the sleepers are alive. You showed it: the signal repeated, the controls were quiet, and a living patch gave the same signature." Then the mandatory contrast line: "Real Mars has not given us a case like this yet. That is why scientists keep testing." The thawlings' ancestry stays Basu's invented theory (story bible 3.4; DESIGN option B).

**Case F2 = case 4: "Positive control" (FICTION; Phase 3; full version).** After the first biocrust grows, the player runs the same instrument on **their own** biocrust. A clear positive appears: **rung 4 is the correct answer** because the player knows the patch is alive: they seeded it. The case then asks one more thing: "If this signal turned up on an unseeded patch, would you call it Martian?" The answer the board teaches: **no, not without ruling out that you brought it**. This is the contamination lesson and the heart of why real Mars cases stay open. It opens only after F8 resolves (CR-F9, 6.3).

**Learning value of the pair.** F1 teaches the instrument that can say "yes" (a positive and a negative control, repeats, a fading reaction versus a lasting one). Case 1 shows what happens when those are missing. The player does not need to be told real life is not found: **they have played why it is hard**.

### 4.5 Where it ties to goals (honest alignment, ID decides)

| Goal | How the loop serves it | Strength and caveat |
|---|---|---|
| **B3 Prediction** | "What will the control do?" asked before each test | **Practice only, not scored.** B3's measure counts forecast accuracy (at least 3 per phase). Case predictions often have no single keyed answer. **Recommendation: not counted as forecasts**; the ID may opt in for keyed ones ("the heated sample will not fizz") |
| **C2 Design, test, improve** | Choosing a control, noticing it cannot separate two ideas, adding a better test | A **stretch** of C2's current definition. **ID to confirm** if C2's measure may use it. I do not claim coverage until then |
| **D1 Ethics** | A fiction case where life is real makes the question "should we terraform around them?" concrete, and real uncertainty explains why it matters | Supports D1 more strongly than the open-ended version alone. The thawlings stay fiction (4.7) |
| A2, A5 | Case 1 uses water and gas behavior; F2 uses the player's own biocrust | Light |
| Not in A1 to D2 | "Argue from evidence" is not a listed goal | **Proposal to the ID:** optional goal extension, or not in the alignment table. No goal ID changes here |

### 4.6 Feedback (shared format: what happened, why, what to try)

Feedback rewards reasoning, not a verdict. Drafts (ID pass pending):

| Situation | Message (draft) |
|---|---|
| Real case, rung 3 while chemistry is open | **What happened:** your claim says life is still possible. **Why:** your tests did not separate life from soil chemistry. **Try:** a test that gives different results for each. (Fair: rung 3 is the top for this case.) |
| Real case, rung 2 after Test C | **What happened:** you say non-living causes fit well. **Why:** perchlorate in sterile soil gave the same fizz. **Note:** that does not prove there is no life. Rung 2 and rung 3 can both be fair. |
| Fiction case, rung 4 without the repeat | **What happened:** you claimed life. **Why:** the signal was seen once. **Try:** repeat it on a second sample. |
| Fiction case, rung 4 without the positive control | **What happened:** you claimed life. **Why:** you have not shown what a living sample looks like on this instrument. **Try:** run the control. |
| Fiction case, rung 3 with everything ruled out | **What happened:** you stayed cautious. **Why:** your tests ruled out the other causes. **Note:** your evidence supports a stronger claim. |
| No control run (any case) | **What happened:** you made a claim with no control. **Try:** run the heated sample and compare. |
| Player skipped tests | No penalty. The board says which explanations are still open. |

### 4.7 Content rule SOL-1 (revised), the labelling rule, and how the player tells the kinds apart

**SOL-1 (revised; proposed to the ID for `docs/learning/content-rules.md`).** *No text, icon, sound or log line claims that life was found on real Mars. Any statement that life exists, "life detected", "organism found" or "alien" is allowed only on a surface that carries the FICTION tag and only in a fiction case.* Four parts:

| Part | Rule | Test (CI) |
|---|---|---|
| SOL-1a | Real-evidence text uses "possible sign", "potential", "needs more evidence". Never "proof", "found life", "life confirmed" (SME: show real Cheyava Falls wording exactly, "potential biosignature, not confirmed") | Banned-word list on every message with `kind: real`; fail on a match |
| SOL-1b | A **life ending** is data: `ending: life` is allowed only on `kind: fiction` cases | Case-data validation: `kind: real` has `max_rung: 3` and no `ending: life` |
| SOL-1c | Sample source is typed: a fiction sample (thawling nest, own biocrust) cannot be tested in a real case, and a real-based sample cannot reach rung 4. Fridge B and the thawlings never appear as a "real" sample | Case-data validation on `sample_source` |
| SOL-1d | Every surface that shows a fiction case also shows the FICTION tag (below) | Message JSON has `fiction: true`; the renderer cannot omit the tag; test enumerates the keys |

**The labelling rule.** Two tags, both **shape + word** (never colour alone):

| Where | FICTION case | REAL EVIDENCE case |
|---|---|---|
| Case board header | A **book icon** and the word **FICTION**, always visible, dashed frame | A **rover-wheel icon** and the words **REAL EVIDENCE**, solid frame |
| Case card in the journal (written-book page) | First line of the page: "[FICTION] In this story ..." | First line: "[REAL EVIDENCE] Based on ..." |
| Sample tube label at the bench | "FICTION sample" | "Real-based sample" |
| Rung 4 | Present, labelled "In this story" | Locked and labelled "No real Mars case has reached this" |
| Closing line | The contrast line (4.4) | "No life has been found on Mars, and no sign of life has been confirmed..." |
| After the case | Recall check (below) | Recall check (below) |

**The player can tell the kind of case four ways, so the tag is never the only signal:** (1) the tag and icon; (2) the **source** of the sample (a replica or real-style node = real; a nest or the player's own biocrust = fiction); (3) the **frame** (dashed against solid); (4) the **rung ladder** (a locked top rung means real).

**Recall check (one question, not scored, logged for the ID).** After **every** case: **"Has life been found on Mars?"** Choices: *Yes* / *No, not so far* / *No one knows, because no test is possible*. Keyed: **"No, not so far."** (The second choice is the NASA-aligned answer; the SME confirms the wording.) After a **fiction** case the answer card adds: "The case you just played was fiction, and its ending is not real Mars news." If the player answers Yes after a fiction case, the next journal page repeats the contrast line (an extra reminder, nothing else changes).

### 4.8 Misconception risk and mitigations (the main new risk)

The revision adds one risk, **fiction-to-real transfer**: the game gives a satisfying "life!" and a student remembers it as science. Risk rating: **medium to high** until a real-reader check says otherwise. *This is a design walkthrough; no student has seen it.*

| Misconception | Why the design invites it | Mitigation |
|---|---|---|
| "The game says life is real on Mars" | Same bench, ladder and tone for both kinds | Tag on every surface, dashed frame, "In this story" on rung 4, contrast line, recall check after every case |
| "Real scientists found what I found" | The player used the same instrument in both | The fiction ending says "Real Mars has not given us a case like this yet" |
| "Real Mars life is easy to detect" | Fiction samples give a clean signal | F1's controls are the same hard ones as case 1; the fiction case says clean results are what the game story allows (one line) |
| "Viking proved no life" or "Viking found life" | A real case that ends open can be read either way | The board never states either; closure line is the SME wording |
| "The thawlings are microbes found on Mars" | The sample is a thawling nest; Basu's theory links them to microbes | Fiction tag; fauna note in the journal ("The thawlings are made up for this game", story bible 2.3) |
| "The top rung is easy in real life" | Rung 4 is reachable in fiction | The positive-control case (F2) ends with "would you call it Martian?" and the contamination lesson |
| "Mars is dead" | Open real endings could read as no | Closing line says "not found, not ruled out": unresolved |
| "Biocrust positive means we found Martian life" | The player's own patch gives a positive | Case F2 states the patch is Earth-origin; "You brought it" is the point |

**Kill switch.** The top rung is controlled by one data flag (`fiction_top_rung`). If the Stage 6 real-reader check or the recall-check data show misconception rates above a threshold (**invented: more than 1 in 5 keyed wrong after a fiction case**), the fix is to set the flag off: fiction cases then cap at rung 3 like real ones, and nothing else changes. The ID sets the real threshold.

### 4.9 Ties to Phase 3 biomass, and why gates are not affected

| Item | Rule |
|---|---|
| Biomass | Counts **seeded flora only** (DESIGN section 1). A case, a claim or a "positive" test **never changes biomass**, the Index, or fauna. A fiction "life" ending is knowledge, not an Index input |
| Gates | No gate reads a find or a case. A case result is stored apart from `PlanetState` (a small per-player store, like Keeper's Record evidence) |
| The "habitable" link | A case file has a **habitability check** with four lines the Monitor already shows: liquid water, temperature, radiation, pressure. These **read** the Index; they change nothing. This keeps **habitable** (a place that could support life) apart from **inhabited** (life is there), a real distinction, and now shows both in the same board: real cases can be habitable and not inhabited, the fiction case inhabited |
| Fauna | A fiction "life" ending does **not** change thawling behavior or any D1 mechanic. The D1 choice (lure, quiet zones, defense) is unchanged |

### 4.10 More cases (full version; each is data plus one SME check)

| Case | Kind | Phase | Based on (*unverified*, SME checks) | Lesson |
|---|---|---|---|---|
| 1 The fizz test | **REAL EVIDENCE** | 2 | Viking experiments | Controls; chemistry vs. biology |
| F1 Are the sleepers alive? | **FICTION** | 2 | The story's thawling nests | What a good test that can say "yes" looks like |
| 2 Spots on a rock | **REAL EVIDENCE** | 3 | Cheyava Falls: NASA's wording "potential biosignature", Nature, Sept 2025 | Non-living chemical reactions can make patterns; the sample is waiting for Earth labs. Real wording exactly as NASA's |
| 3 A gas in the air | **REAL EVIDENCE** | 2 or 3 | Methane seen in the air by Curiosity, rising and falling by season | Gas could come from rock or from life; not settled |
| F2 Positive control | **FICTION** | 3 | The player's own biocrust | What a clear positive looks like; "would you call it Martian?" |

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
| "Life was found on real Mars" | Real cases cap at rung 3 with the top rung shown locked; SOL-1 revised; closure line; recall check after every case; FICTION tag (4.2, 4.7) | ID, SME |
| "The fiction ending is real science" | New, main risk: tag on every surface, "In this story" on rung 4, contrast line, case F2 contamination lesson, kill switch (4.8) | Game Designer, ID, SME |
| "The rovers are near each other, or near our dome" | Option A replicas with the real site named on the plaque; a one-line scale note in the Hall | Game Designer, SME |
| "Those rovers are still working" | Each entry says *ended* or *still working*; dates *unverified* | SME |
| "Opportunity said 'my battery is low'" | Never quoted as a rover message (misattribution) | SME |
| "Mars is full of gold or rare metals" | Honest line in 3.3 | SME |
| "Iron comes out of dirt with just heat" | Entry names the missing step | SME |
| "Perchlorate is free oxygen" | Listed as hazard only; no oxygen recipe | SME |
| "Olivine would fix Earth's climate quickly" | D2 note: real on Earth but slow and huge-scale (*unverified*) | SME |
| "The thawlings are evidence of real life" | Fiction tag, fauna note, SOL-1c sample typing (4.7) | Game Designer |

### 6.3 CR-F9 and CR-F10: do the finds reveal forecast answers?

Every find entry and every case line is a message with a `reveals` field. **A held entry stays hidden, including journal copies** (CR-F9 2026-11-28 addition), until the named forecast has resolved or expired for that player.

| Find or text | Could reveal | Rule |
|---|---|---|
| Sojourner HS line on dust and power; Opportunity; InSight; Spirit | **F1** (storms cut solar power) and the F1 and FI-1 mechanism | `reveals: forecast.f1`. Those entries are Phase 2 sites by design. The Phase 1 site (Sojourner) keeps the dust line **only** in the held HS text |
| Curiosity (surface radiation) | **F2** and **F7** | `reveals: forecast.f2, forecast.f7`; Phase 3 site, held until both resolve |
| Phoenix; ice sublimating in dry air | **F4a** | `reveals: forecast.f4a` |
| Clay or sulfate "formed in liquid water when the air was thicker" | **F4b** (the HS reading hints that thicker air keeps water) | `reveals: forecast.f4b`, HS text only |
| Case 1 (real; a liquid at normal pressure) | F4a "why" | Case 1 opens after the Thaw, so F4a and F4b are long resolved |
| Case F1 "Are the sleepers alive?" (fiction; warm sample with water, gas output) | F4a and F4b "why" (liquid water needs thick air) | Opens after the Thaw and Basu's first notes; same timing as case 1, so F4a and F4b are resolved. Entry keeps `reveals: forecast.f4a, forecast.f4b` so a debug or skipped path cannot show it early |
| Case F2 "Positive control" (fiction) and the biocrust test | **F8** (flora: O2 up, CO2 down) | `reveals: forecast.f8`; opens after F8 resolves |
| Rung 4 text, the contrast line and the recall-check answer card | None (no forecast names life) | Still carry `fiction: true` or `kind` fields, which SOL-1 reads (4.7). Not a CR-F9 case |
| Case F1 and F2 `failure.*`-style hints, bench diagrams | Any mechanism a forecast asks about | CR-F11: declare `reveals`; held like text. F1 and F2 have no failure reports |
| Olivine and scrubbers | **F8, FI-4** (net CO2) | Entry says what scrubbers do, never what net CO2 will do; scrubber stays its own source ID |

**CR-F10.** Finds and cases create **no new source ID** and change no contribution formula. The only candidate risk is a stronger scrubber (olivine), which changes a **rate in data**; the solver test (clean panels mid-storm, scrub mid-forecast) is **not run** here and must run when the numbers exist. The fiction "life" ending is knowledge only: it adds no Index contribution, so no `resolve_source` is needed for it.

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
| Stage 4, Phase 3 build | Olivine, Perseverance, case F2 | Needs the biocrust |
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
| **Case F1 "Are the sleepers alive?" (fiction) + labelling** | 1.5 to 2.5 (FICTION tag in board and book page, `kind` and `sample_source` in case data, rung 4 gating, CI checks for SOL-1a to d, source tag on sample items) | 0.5 to 1 (book and wheel icons, dashed frame, nest sample) | 1 | 1 to 1.5 (SOL-1 wording, recall check, contrast lines) | 0.5 to 1 | Reuses case 1's engine and board. **This is the added cost of the owner's "life" ending**: about 2 to 3.5 sessions in total over the earlier MVP |
| **MVP total (case 1 + F1)** | **about 11 to 17** | **about 6.5 to 9** | **about 4 to 5** | **about 3 to 4.5** | **about 3.5 to 4.5** | Against Stage 3's 40 to 66 TL sessions. Earlier total with case 1 only: 9 to 14 / 6 to 8 / 3 to 4 / 2 to 3 / 3 to 3.5 |
| Full version extra (6 more sites, 3 more nodes, cases 2, 3 and F2, HS depth, Hall polish) | about 8 to 12 | about 12 to 16 | about 6 | about 3 | about 4 | The Artist is the larger line |

**Funding.** There is no budget row. Stage 4's cut list totals about 435 h of buffer (budget §4.3). The fiction case adds about 2 to 3.5 sessions, so the swap is slightly larger than in the first draft (the Producer re-prices). **Recommendation:** fund the MVP by *swapping* an item of similar size (for example cut #9, merging two pairs of machines, or cut #1 if the owner prefers), not by growing Stage 4. The Producer prices it. The Instructional Designer blocks nothing here because no Primary goal's only teaching or measure moves.

### 7.3 What to cut first (ordered; stop when the budget is met)

| # | Cut | Learning cost | Appeal cost |
|---|---|---|---|
| 1 | Sites beyond the MVP three (and the Lost missions wall) | None unique | Low to medium |
| 2 | Carbonate, clay and sulfate nodes (keep iron oxide, perchlorate, olivine) | A2 loses one node | Low |
| 3 | Survey compass item (already cut), HS read-more depth | HS depth only | Low |
| 4 | Cases 2 and 3 | None unique | Medium: the loop gets thinner |
| 5 | Case F2 (positive control, fiction) | **B3 and A5 link lost, and the contamination lesson**; the Phase 3 life ending goes with it. Cut last of the cases | Low to medium |
| 6 | Hall page → plain journal entries | D2 timeline lost | Low |
| 7 | Nickel-iron drill-bit dependency | Rare-find hook lost | Low to medium |
| **Never cut** | SOL-1 (revised) and its four CI checks, the locked top rung on real cases, the FICTION tag and sample typing (E10), the recall check, the `reveals` fields, rule E1. **If the fiction case F1 is cut, the real-case rules stay as they are; if the labelling cannot be built, cut the fiction case, not the labels** | | |

If only one thing survives, it is the **pair, case 1 plus F1**: each teaches by contrast with the other (a test that cannot decide, then a test that can). If the budget forces one, keep **case 1** (the one that serves the real-evidence learning goal) and ship F2 in Phase 3 as the only life ending.

### 7.4 Fantasy and authenticity audit rows (to add to the DESIGN.md table)

| Element | Real or fantasy | Purpose | Activity matches skill? | Negative-learning risk | Status |
|---|---|---|---|---|---|
| Hana's replica heritage trail | **Fiction** (replicas by a crew member); the machines are real | Motivation, scale (A1), history (D2) | Walking, reading | Low with "replica" and the real site on the plaque | Proposed |
| Mineral nodes (iron oxide, olivine, perchlorate, sulfate, clay) | Real minerals; **placement and machine use simplified** | Resource planning (C1); links to A2, A5 | Choosing where to dig, planning a trip | Medium: "easy to get metal from dirt"; entries state the missing step | Proposed |
| Iron-nickel meteorite node | Real kind of find; rarity is invented | A "rare find" without inventing a metal | Spotting a dense rock | Low | Proposed |
| "Signs of life?" real-evidence cases (1, 2, 3) | Real kind of reasoning; simplified retellings of Viking and Cheyava Falls style evidence; capped at rung 3 | Argument from evidence, D1 | Testing, comparing | Medium: "Viking found / disproved life"; SOL-1a | Proposed |
| **Fiction case files** (F1 "Are the sleepers alive?", F2 "Positive control") | **Fiction**: the thawlings and the player's own biocrust; the instrument and bench are game inventions | Show what evidence that can say "yes" looks like; D1 | Testing, repeating, using a positive control | **Medium to high** (fiction-to-real transfer); FICTION tag, SOL-1, recall check, kill switch | Proposed |
| Confidence ladder with rung 4 "In this story" | Simplified teaching device; the top rung is fiction-only | Honest uncertainty; the bar for "life" | Choosing a rung with a reason | Medium (see above) | Proposed |
| FICTION and REAL EVIDENCE tags | Game device | Keep the two kinds apart | Reading a tag | Low | Proposed |

---

## 8. Questions for the owner (at most 1)

Decided and no longer open: replicas (accepted), Stage 4 Phase 2 MVP via a cut-list swap (accepted), "life" ending allowed (owner's choice, built as sections 4.0 to 4.8).

1. **Ship the fiction case F1 in the Stage 4 MVP, or only F2 in Phase 3?** (ill-structured: appeal and contrast teaching against scope.) **Recommend F1 in the MVP**: the pair "case 1 (open) next to F1 (answerable)" teaches by contrast and gives the owner's "life" ending in Phase 2. *Cost:* about 2 to 3.5 more Technical Lead and related sessions (7.2). If you say Phase 3 only, F1 moves to the full version and the MVP is as first priced.

## 9. Assumptions, and what would make this better defined

| Assumption | Check |
|---|---|
| Ring placement in a custom dimension can be datapack-only | Technical Lead, a small spike |
| Starting stock covers the minimum iron path (E1) | Balance sheet 2d and the Technical Lead |
| Players will read a plaque | Playtest observation |
| Classroom default off is acceptable | Teacher feedback, Stage 6 |
| The ladder reads as fair to ages 12 to 18, and rung 4 "In this story" is read as fiction | Playtest, recall check, Stage 6 real-reader check |
| Fiction ending does not raise misconception rates above the threshold (invented: 1 in 5) | Recall-check data; kill switch `fiction_top_rung` |
| Chemistry fades while life-like signal holds (case F1 tests) is a fair story rule | SME check; it mirrors how a real labeled-release style test is argued (*unverified*) |
| Every real-world fact | **SME review: not done.** All *unverified* |

**Would make it better defined:** the SME review; one paper prototype of case 1 with three readers aged 12 to 16 (cards only; I have run none); a Technical Lead placement spike; a Producer price.

## 10. Handoffs

| To | Ask |
|---|---|
| SME | Check the rung wording ("No real Mars case has reached this"), the contrast lines, the F1 test logic, and whether "No, not so far" is the right keyed recall answer. Verify every *unverified* fact: rover and lander dates, sizes, endings, Viking and later results, the real life-detection scale, hematite, olivine and carbonation, sulfate water, meteorites, manganese, the Opportunity "last message" origin, and the 2025 rock-spot claim. Confirm what is safe to call "potential" |
| Instructional Designer | Alignment of case files (B3 practice not counted, C2 stretch, "argument from evidence", D1 now stronger); **add SOL-1 revised (a to d) to `docs/learning/content-rules.md`**; grade 6 to 7 pass; the recall check and the misconception threshold; the Stage 6 real-reader check |
| Lead Artist | FICTION (book) and REAL EVIDENCE (rover wheel) icons that work without colour, dashed and solid case frames; monument models (MVP three), plaque, sample tube and bench icons, node textures, lighting for dust weathering |
| Technical Lead | Feasibility: ring placement, the board screen, per-player case store apart from `PlanetState`, case-data validation (`kind`, `sample_source`, `max_rung`, `ending`), the `fiction_top_rung` flag, which machine bakes iron oxide and sulfate (open item G4) |
| Producer | Price the MVP and name the swap |
