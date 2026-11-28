# Phase 1 misconception items (draft, 2026-11-28)

**Owner:** Lead Instructional Designer · **Status:** draft, **SME check pending** (all science), not piloted · Source: `docs/team/reports/2026-11-28-instructional-designer-landing-text.md`

Both items are multiple choice, one best answer, for the item bank (LEARNING.md 3.3, misconception rows) and a candidate for the slice 8 mini test. Distractors are written from the misconception, not as nonsense. No NGSS code is claimed: no PE assesses these (same practice as LEARNING.md misconception rows, which tag the goal only). A1 context data: Mars about 0.6 kPa (SME Part A), Earth 101.3 kPa, Mars air 95.1 % CO2 and 0.145 % O2 (SME Part A, sourced). Banned words (CR-T1) avoided; the stems are for use in the pre/post test, which sits outside the game, so the CR-T1 scan applies only if they are ever shown in play.

## M-A1-O2: "Oxygen is easy on Mars" (goal A1)

**Misconception:** Mars air has oxygen in it, so a filter or a mask is enough.

| Band | Stem | Choices (key = B) |
|---|---|---|
| MS | Mars air is about 95 percent carbon dioxide. Less than 1 percent is oxygen. A Keeper needs air to breathe. What is true? | A. A mask that filters the air gives enough oxygen. B. The outside air has almost no oxygen, so oxygen has to be made or carried. C. The air is fine to breathe because it has some oxygen. D. The air is only a problem because it is cold. |
| HS | Mars air is 0.145 percent oxygen at about 0.6 kPa. Earth air is about 21 percent oxygen at about 101 kPa. Which statement is best supported? | A. Mars has about 1/7 of Earth's oxygen, so a booster mask is enough. B. Both the share and the push of oxygen are tiny on Mars (about 0.001 kPa against about 21 kPa), so lungs get almost none. C. Only the share matters, and 0.145 percent is enough for short trips. D. Mars has more CO2, so it also has more oxygen to filter out. |

Key notes: HS key B uses a **calc** (0.00145 x 0.6 is about 0.0009 kPa; Earth 0.21 x 101.3 is about 21 kPa); SME to confirm rounding. HS distractor A is the percent-only reading (a "21 % is always breathable" cousin, SME finding 1). Taught by: `journal.dome_stable.airmix`, Monitor Outside air line. Not revealed in any forecast.

## M-A1-V: "A vacuum sucks" (goal A1; context for C2 later)

**Misconception:** thin or empty space pulls air out of a cracked dome.

| Band | Stem | Choices (key = C) |
|---|---|---|
| MS | A dome with air inside gets a small crack. Air leaks out into the thin Mars air. What is the best reason? | A. Thin air outside sucks the dome air out. B. The crack makes new air that has to escape. C. Air pushes from where pressure is higher toward where it is lower. D. Mars wind blows the air out. |
| HS | The air in a dome is at 70 kPa and the air outside is at 0.6 kPa. Which describes the force on a patch over a crack? | A. About 0.6 kPa pulling out. B. About 70 kPa pushing out, with about 0.6 kPa pushing in, so the patch must hold the difference. C. About 70 kPa pulling in. D. No net force, because the outside has almost no air. |

Key notes: the 70 kPa is an **invented** stand-in for dome pressure (the dome's actual pressure is not stated in the repo as far as read; use the value the SME or Game Designer confirms). Distractor D (HS) is the "no air, no force" reading. Taught by: proposed sentence in `journal.a1.pressure` (landing-spec §12 W6) and the revised hull line (W4). CR-F9: no forecast asks this.

## Use rules

- Same stems on pre and post, order shuffled; never shown in play (a quiz in play would break CR-T1).
- Score as a misconception item (LEARNING.md 3.3): record the chosen distractor, not only right or wrong, so the teacher sees which idea persists.

---

# Storm misconception items (candidates, added 2026-11-28)

**Status:** candidates, **SME check pending**, not piloted. Source: SME review `docs/science/storm-dust-review.md` §5.2 (the misconception table; numbers and tags there). Same use rules as above (outside the game, pre and post, distractor chosen is recorded). Six items; the SME table has a seventh row ("storms make everything colder") that is **held**: the game shows no storm temperature, and the numbers behind it are search-summary (SME §6, §14), so it needs the SME before an item is written.

**Pre-test rule (CR-F9 spirit).** Items S1 and S3 do not state that storms cut solar power, so they may sit on the **pre-test**. S2, S4, S5 and S6 carry the F1 answer (their keys say dust blocks sunlight or lowers panel power). Put them on the **post test and delayed post test only**; on the pre-test they would hand every player the F1 key before the game starts. Distractors are written from the misconception, not as nonsense. No NGSS code claimed (no PE assesses these; goal tag only, as above). "Today" in S1 matters: wind force rises with air thickness (SME §5.2 warning), so the items say "today's Mars air".

## S1: "A Mars storm can blow a base away" (goals A1, B1; pre or post)

**Misconception M-S1:** Mars storm wind is strong enough to knock over buildings (film). Source: NASA names it itself (SME N5).

| Band | Stem | Choices (key = B) |
|---|---|---|
| MS | In a Mars dust storm, the wind can reach 60 miles an hour. Today's Mars air is very thin. What is the best prediction for a base on the ground? | A. The wind knocks over the buildings, like a hurricane. B. The wind pushes very little, because thin air has little push. C. The wind pushes harder than on Earth, because the air is full of dust. D. There is no wind in a storm. |
| HS (key = C) | Wind pushes with a force that depends on air density and on speed squared. Today Mars's surface air is about 1/80 as dense as Earth's. A 97 km/h wind on Mars pushes about as hard as which wind on Earth? | A. About 97 km/h, since the speed is the same. B. About 1.2 km/h, since 97 divided by 80 is 1.2. C. About 11 km/h. D. Mars wind cannot be compared with Earth wind. |

Key notes: HS key is **calc** (97 times the square root of 1/80 is 10.8; SME §5.1, T = 210 K assumed). HS distractor B is the linear-scaling slip (density ratio applied to speed, not to speed squared); A is the speed-only reading. Taught by `journal.storm.wind`. SME to confirm the 11 km/h rounding.

## S2: "A Mars dust storm is like an Earth hurricane" (goals B1, A1; post only)

**Misconception M-S2:** a dust storm is a hurricane made of dust, with the same energy source and the same damage.

| Band | Stem | Choices (key = A) |
|---|---|---|
| MS | How is a Mars dust storm different from an Earth hurricane? | A. It is a huge cloud of fine dust in thin air. It mostly dims the sky instead of knocking things over. B. It brings heavy rain. C. It is the same thing, only red. D. It is weaker only because Mars is smaller. |
| HS (key = D) | What mainly powers a Mars dust storm, and what is its main effect on a lander? | A. Warm ocean water; heavy rain. B. Volcanoes; ash covering the panels. C. Strong wind from the poles; damage from wind force. D. Sunlight warming the dust in the air; less sunlight at the surface. |

Key notes: NASA (SME N6): "solar heating of the dust lofted into the atmosphere changes temperatures, which changes winds". Hurricane mechanism is general meteorology, **not opened** by the SME. Taught by `journal.storm.wind` and the F1 result.

## S3: "A storm is gone in a day" (goals C1, A1 weak; pre or post)

**Misconception M-S3:** Mars storms are short, because the game's storm is about a sol. A risk the game itself creates (SME §5.2).

| Band | Stem | Choices (key = C) |
|---|---|---|
| MS | In the game, a dust storm lasts about one sol. How long do real Mars dust storms last? | A. About one day, the same as in the game. B. A few hours. C. Days to months. D. About one year, every year. |
| HS (key = B) | A crew on Mars sees a regional dust storm start. Real regional storms last weeks, and global storms last months. Which plan fits best? | A. Cut loads for about a day and then expect full power. B. Plan power and stored reserves for weeks of low sunlight. C. Wait for the winds to stop, because then the sky clears. D. Do nothing, since panels recover at once when the sky is clear. |

Key notes: local about 3 days, regional weeks, global months (SME §3.3; durations **verified**, N1, N6, N11). The HS stem hands over the durations (this item tests planning, not recall); the MS stem tests recall and is a candidate for the delayed post test. Taught by `journal.f1.real_vs_game`.

## S4: "With no direct Sun, panels make nothing" (goal B1; post only)

**Misconception M-S4:** if the dust blocks direct sunlight, solar panels produce zero.

| Band | Stem | Choices (key = C) |
|---|---|---|
| MS | In 2018 a giant dust storm blocked almost all direct sunlight from the rover Opportunity. At the darkest point, what did its solar panels do? | A. Made no power at all. B. Made normal power, because panels run on heat. C. Made a little power, from light scattered across the sky. D. Made more power, because dust stores light. |
| HS (key = A) | At the darkest point in 2018 (optical depth about 10.5), direct sunlight was down to about 0.002 percent, yet Opportunity's panels made about 21 watt-hours in a sol. Which is the best explanation? | A. Scattered (diffuse) light from the whole sky still reached the panels. B. The panels were running on stored battery power. C. At that depth, none of the direct light is blocked. D. Panels make power from heat, not light. |

Key notes: 21 Wh and tau about 10.5 **verified** (NASA N8); 0.002 percent is **calc** (e^-10.8); "scattered light is what is left" is the SME's own reasoning (calc), not a measured diffuse fraction, so SME to confirm it can be keyed. Do not follow this item with a tau-to-percent chart (SME "Not one curve"). Taught by the F1 explanation (depth 2, both bands).

## S5: "Storms only matter because they cover the panels" (goals B1, C1; post only)

**Misconception M-S5:** the only harm of a storm is dust on the panels, so brushing during the storm fixes it (also matches game rule CR-F10: brushing moves net power, not the storm's own cut).

| Band | Stem | Choices (key = A) |
|---|---|---|
| MS | A dust storm can lower solar power in two ways. Which pair is right? | A. Dust in the air blocks sunlight, and dust settles on the panels. B. Wind breaks the panels, and dust blocks sunlight. C. The Sun gets weaker, and dust settles on the panels. D. Only dust on the panels matters. |
| HS (key = B) | During a storm, a Keeper brushes every panel clean. Power rises only a little. Which is the best explanation? | A. The brush did not work. B. Most of the loss is dust in the air, which a brush cannot remove. C. Dust on panels does not affect power. D. The panels are turned away from the Sun. |

Key notes: NASA says the same (SME N2: dust "reduces sunlight filtering through the atmosphere, and it can also accumulate on the panels"). The game shows the two as separate cause lines. Taught by `journal.storm.first` and `explain.f1.after_storm`.

## S6: "Storm dust falls and the sky is clear at once" (goals B1, C1; post only)

**Misconception M-S6:** when the sky looks clear, the dust is gone, and power is back to normal.

| Band | Stem | Choices (key = D) |
|---|---|---|
| MS | A big Mars dust storm ends and the sky looks clearer. What happens to the dust that was in the air? | A. It is gone right away. B. It rises into space. C. It turns into rain. D. It keeps falling out of the air for weeks. |
| HS (key = B) | After a regional storm in 2022, NASA said dust "will continue falling out of the atmosphere for weeks", and the InSight lander's power was back near its old level only several weeks later. What should a base planner expect after a storm? | A. Full power the moment the sky clears. B. Power that recovers over weeks, and some dust left on the panels to clean. C. Cleaning is pointless, because the dust returns. D. No change, because storm dust never reaches panels. |

Key notes: "for weeks" **verified** (NASA N1, N3, SME §5.2 and §8); "almost as much power as before the storm several weeks after" **verified** (N2). The game adds storm dust at storm end (a stated simplification, `journal.dust.storm_timing`); this item tests the real fact, not the game rule. Taught by `journal.dust.storm_timing` (changed from "days" to "weeks").

## Alignment and gaps

| Item | Goal | Taught by | Practiced in play | Measured |
|---|---|---|---|---|
| S1 | A1, B1 | `journal.storm.wind` (after F1) | Forecast F1 `m3` line | Pre and post item |
| S2 | B1, A1 | F1 result depth 2; `journal.storm.wind` | none | Post item |
| S3 | C1, A1 weak | `journal.f1.real_vs_game` | Planning for the first storm (C1) | Pre and post item |
| S4 | B1 | F1 result depth 2 (scattered light) | none | Post item |
| S5 | B1, C1 | `journal.storm.first` | `explain.f1.after_storm`; `panel.cleaned` before a warned storm | Post item |
| S6 | B1, C1 | `journal.dust.storm_timing` | `explain.f1.after_storm` (partly) | Post item |

Gap: S2 and S4 have no in-play practice, only a one-time explanation (single exposure, weak retention). They are honest candidates for the delayed post test only, to measure what survives. Recommend not adding game content for them.
