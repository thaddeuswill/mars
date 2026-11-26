# Messages: Phase 3 (Biome Seeding & Equilibrium) and Ending

**Owner:** Lead Game Designer · **Status of every entry:** draft: SME check and ID review pending · Conventions: [README.md](README.md)

Order of first play: Shift Briefing 3 → first flora (F8, flora Earth link) → O2 enters the Air window (F9) → emitters turned down or failing (F10, any time in the equilibrium push) → Equilibrium (Keeper's Report 3, Mensah's reply) → beacon (Mensah's last line).

---

## 1. Shift Briefing 3 (advance organizer)

| Field | Value |
|---|---|
| `id` | `briefing.shift3` |
| `type` | Shift Briefing |
| `trigger` | Keeper's Report 2 sent or deferred (Phase 3 begins) |
| `delivery` | BELL briefing card after the report screen (one sequence); copy in the journal |
| `text.ms` | **Shift Briefing.** This shift: hold the balance. Plants add O2 and take in CO2. Keep O2 inside the Air window: too little and you can't breathe, too much and fires start. Holding is harder than climbing. Notice what you give up to hold the balance. |
| `text.hs` | Same as MS |
| `goal_ids` | A5, B4 |
| `instructional_purpose` | Advance organizer: the new job is a band, not a number; names the trade-off before it bites |
| `motivational_purpose` | A new kind of challenge; the end is in sight |
| `hint_level` | — |
| `science_claims` | Photosynthesis; fire risk above the band (see F9) |
| `fiction_note` | None |
| `status` | draft |

---

## 2. F8: First large seeding

| Field | Value |
|---|---|
| `id` | `forecast.f8.flora_gases` |
| `type` | forecast |
| `trigger` | Player places the first biome seed kit |
| `lock` | 1 sol after the kit is placed; Monitor open shows the card first (`forecast.look_first`, phase-1.md §11) |
| `delivery` | BELL forecast card at the seed kit screen; result at the next natural pause after 2 sols |
| `prompt.ms` | You seed a large field of plants. What will happen to O2 and CO2? |
| `prompt.hs` | Same as MS |
| `choices` | O2 rises, CO2 falls · Both rise · Both fall |
| `why.hs` | Why? — Growing plants take in CO2 and give off O2 · Plants make CO2 only at night · The soil releases O2 |
| `result.ms` | O2 is rising and CO2 is falling. Growing plants take in CO2 and give off O2. That's photosynthesis. Watch it slow down as the field grows up. |
| `result.hs` | O2 up, CO2 down. Plants also use some O2 and release CO2 all the time, day and night, but while they grow they take in more CO2 than they release. As the field matures, the two nearly balance. |
| `goal_ids` | A5, B3 |
| `instructional_purpose` | Photosynthesis as gas exchange; HS corrects "plants respire only at night"; foreshadows maturity slowdown (balance sheet §7) |
| `motivational_purpose` | Green on the ground; a visible new phase |
| `hint_level` | — |
| `science_claims` | Photosynthesis takes in CO2 and releases O2; plants respire continuously; mature ecosystems are near carbon-neutral **[SME: verify wording, balance sheet §7]** |
| `fiction_note` | Filling Mars's air with oxygen from plants could take up to about 100,000 years. The game speeds it up. |
| `status` | draft |

**Earth link at first flora** (journal "On Earth" line):
| Field | Value |
|---|---|
| `id` | `earth.gate.first_flora` |
| `type` | Earth link (journal line) |
| `trigger` | F8 result shown |
| `delivery` | Last line of the flora journal page |
| `text.ms` | About half of the oxygen made on Earth comes from tiny ocean plants and algae. |
| `text.hs` | Same as MS. Read more: the rest comes from plants on land. Ocean life also uses about as much O2 as it makes. |
| `goal_ids` | A5, D2 |
| `instructional_purpose` | Transfer: Earth's O2 source |
| `motivational_purpose` | Connects the player's fields to Earth's forests and oceans |
| `hint_level` | — |
| `science_claims` | "Roughly half" of Earth's O2 from ocean photosynthesis (NOAA, round 2 [S25]) |
| `fiction_note` | None |
| `status` | draft |

---

## 3. F9: O2 toward the top of the window

Fired early, when O2 first enters the Air window (ID's suggestion, LEARNING.md §4.7), so the player meets the top edge before a fire.

| Field | Value |
|---|---|
| `id` | `forecast.f9.o2_fire` |
| `type` | forecast |
| `trigger` | O2 fraction first enters the Air window with a rising trend |
| `lock` | O2 reaches 23 %, or 0.5 sol, whichever first |
| `delivery` | BELL forecast card at the next natural pause; correctness by rule; result at the next natural pause after lock |
| `prompt.ms` | O2 keeps rising. If it goes past the top of the Air window, what happens? |
| `prompt.hs` | Same as MS |
| `choices` | Fires start more easily · The air gets too thin · Plants stop growing |
| `why.hs` | Why? — More O2 makes things burn more easily · O2 is poisonous at any level · More O2 smothers flames |
| `result.ms` | Above 23 % oxygen, fires start and spread more easily. That's why the Air window has a top and a bottom. Too little: you can't breathe. Too much: fire. |
| `result.hs` | Above about 23 % O2, things catch fire and burn faster. The bottom edge depends on pressure: you need about 16 kPa of oxygen push. Raise pressure and that edge drops, so the window widens. |
| `goal_ids` | A5, B4, B3 |
| `instructional_purpose` | Both edges of the band; HS links the lower edge to partial pressure and the pressure trade-off |
| `motivational_purpose` | Warning before loss; the player gets to prevent the first fire |
| `hint_level` | — |
| `science_claims` | Above about 23 % O2 almost everything burns too easily; below about 15 % most things won't keep burning (SME round 2 §6 note 11, sourced); breathable at ≥ 16 kPa O2 partial pressure (user decision on SME recommendation) |
| `fiction_note` | None |
| `status` | draft |

---

## 4. F10: Emitters turned down

| Field | Value |
|---|---|
| `id` | `forecast.f10.emitter_tradeoff` |
| `type` | forecast |
| `trigger` | In Phase 3: total emitter output drops by 25 % or more for 0.1 sol (player turned them down, or upkeep failed) |
| `lock` | Monitor temperature trend turns down, or 0.5 sol |
| `delivery` | BELL forecast card at the next natural pause; result after 2 sols |
| `prompt.ms` | Your emitters are running at less power. What will temperature do over the next few sols? |
| `prompt.hs` | Same as MS |
| `choices` | Fall slowly · Rise · Stay the same |
| `why.hs` | What's the trade-off? — Less power used, but the planet cools · No trade-off · Fewer thawlings, nothing else |
| `result.ms` | Temperature is falling slowly. With less heat-trapping gas, more heat escapes to space. You saved power, but cold can freeze water and slow plants. Every choice here costs something. |
| `result.hs` | Temperature is falling slowly. Real factory gases break down slowly in Mars's strong sunlight, so they must be made all the time. Power saved; water and plants at risk. Holding a band costs something every sol. |
| `goal_ids` | B4, A3, B3 |
| `instructional_purpose` | The cost of holding the band; greenhouse warming needs upkeep in the game model |
| `motivational_purpose` | Real decision with no free option |
| `hint_level` | — |
| `science_claims` | Super-greenhouse gases last a long time but break down in sunlight on Mars and would need topping up (SME round 2 §3.1, [S24]); "falls slowly" is accurate as a game rule |
| `fiction_note` | None |
| `status` | draft |

---

## 5. Earth link: Mensah's reply to Keeper's Report 3 (Equilibrium)

| Field | Value |
|---|---|
| `id` | `earth.report3.reply` |
| `type` | recorded message (Earth desk) |
| `trigger` | Keeper's Report 3 sent (or gate + 1 sol if deferred), delay 0.5 sol × `story.delayScale` |
| `delivery` | Recorded message, text first |
| `text.ms` | Keeper, {sols_held} sols in band. On Earth, carbon moves between air, plants, soil, ocean, and rock. Balancing loops kept it steady for thousands of years. We're now adding carbon faster than they can remove it. You know what holding a band takes. |
| `text.hs` | Same as MS. Read more: one balancing loop: more CO2 can speed plant growth, and growing plants take up more CO2. The ocean also absorbs CO2, which makes seawater more acidic. |
| `goal_ids` | A5, B2, D2 |
| `instructional_purpose` | Earth's carbon cycle and balancing loops (HS-ESS2-6, HS-ESS3-6 context); human activity shifting it |
| `motivational_purpose` | Respect for what the player achieved; the Mars job mirrors Earth's |
| `hint_level` | — |
| `science_claims` | Carbon cycle reservoirs; balancing loops (CO2 fertilization, ocean uptake); emissions outpace natural uptake; ocean acidification **[SME: verify all]** |
| `fiction_note` | None |
| `status` | draft |

---

## 6. Earth link: Mensah's line at the beacon (ending step 3)

| Field | Value |
|---|---|
| `id` | `earth.ending.mensah` |
| `type` | recorded message (ending reply, one line) |
| `trigger` | Beacon lit, replies sequence |
| `delivery` | Ending sequence, after Moreno, Basu, Hana |
| `text.ms` | We already change a planet, Keeper. Ours. You showed what it takes to do it with a plan. |
| `text.hs` | Same as MS |
| `goal_ids` | D2, D1 |
| `instructional_purpose` | Final transfer frame before the final reflection question |
| `motivational_purpose` | Pride and a quiet question to carry out of the game |
| `hint_level` | — |
| `science_claims` | Humans are changing Earth's climate (see `earth.report2.reply`) |
| `fiction_note` | None |
| `status` | draft (tone check: ID) |
