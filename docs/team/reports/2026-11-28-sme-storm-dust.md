# Report: SME, storm and dust sources reopened (2026-11-28)

Role: Lead Subject Matter Expert. Task: owner request for the Stage 3 slice 5 SME inputs. Full review: [docs/science/storm-dust-review.md](../../science/storm-dust-review.md). Docs only; no Java, no pack, no balance-sheet edit.

**Perspective round (my lens).** Success is a student who knows that storms hurt power by dimming the sky first and coating panels later, that Mars wind is weak but gets stronger as the player thickens the air, and which game numbers are real and which are compressed.

## 0. Handoffs received

| Handoff | Answer |
|---|---|
| Open the "searched, not opened" dust sources (Lorenz 2021, NASA pages) | **Accepted, partly done.** 13 NASA pages and 4 Wikipedia pages opened. **Lorenz 2021 could not be opened** (HAL, NTRS, IP Paris, INTA, doi.org all refused by the proxy). Still search-summary |
| Real numbers for accumulation, cleaning, storm loss, duration, warning | **Accepted, done** (review §1 to §4) |
| F1 mechanism and misconceptions at two depths | **Accepted, done** (review §3.4, §5, §10) |
| Monitor cue from pressure or temperature | **Accepted, done.** Recommendation: haze and day-night spread, **no pressure cue** (review §6) |

## 1. Done

- `docs/science/storm-dust-review.md` (new). Highlights:
  - **Hosts that opened:** science.nasa.gov (all pages tried), www.nasa.gov (feature pages), en.wikipedia.org, commons.wikimedia.org; photojournal.jpl.nasa.gov redirects to science.nasa.gov. **Blocked or unreachable:** www.jpl.nasa.gov (403), ntrs.nasa.gov, hal, arxiv, adsabs, doi.org, planetary.org, space.com, phys.org, mars.nasa.gov, robotics.nasa.gov. upload.wikimedia.org: redirect only, not needed.
  - **Storm loss (all verified or calc from opened):** InSight regional storm 425 to 275 Wh/sol (-35 %); Opportunity 2018: -28 %, -47 %, -80 %, -97 % at tau 1.0, 2.1, 3.0, about 10.8 (calc from the Wikipedia energy table that cites NASA; NASA's own pages give 21 Wh at tau 10.5 and 10.8); 2007: 128 Wh against about 700 (about -82 %). **The game's -60 % at cap 1.0 equals about tau 2.4** (calc): a strong regional storm, milder than the real global worst. **Keep it.**
  - **Warning:** real is about 1 sol (a lander reading its own rising haze; tau 1.0 to 2.1 in one sol) up to about 12 sols (MRO spotted the Oct 2022 storm 21 Sep; haze at InSight up about 40 % by 3 Oct; storm 3,500 km away). "At least 1 sol" is an honest, conservative minimum. MRO as an early-warning system is NASA's own wording, so an in-story orbiter source is real.
  - **Duration:** local about 3 days (Opportunity sol 631 storm), regional days to weeks, global about 6 weeks (2007) to months (2018: near peak about 3 weeks, tau below 1.5 over Opportunity by early Sept). Game 0.5 to 1 sol is compressed 3 to 100 times.
  - **Cleaning frequency:** at least 7 named Opportunity events in 5,111 sols (about 0.14 % per sol, lower bound) and Spirit about 3 in 2,000 sols (0.15 %). Game 1 % per sol is about 7 times real. Fine and labelled.
  - **F1 mechanism:** NASA's own line (dust reduces sunlight filtering through the air, and it can also accumulate on panels), NASA's anchor (tau 3 leaves about 5 % of direct sunlight), and my calc that the rover kept more power than the direct beam alone allows, so scattered (diffuse) light carries the rest. Marked as my reasoning, not a measured fraction.
  - **Misconceptions:** blown away (The Martian; NASA names it), "like a hurricane", "gone in a day", "no direct Sun means no power", "storms only coat the panels", "dust falls at once", "storms only cool". Each has a grade 6 to 8 and a high school correction.
  - **Pressure and temperature:** day-night swing narrows (Viking about 50 to about 10 C; REMS 2018 about 35 K at ground and 56 K in air, search-summary); nights warmer. Pressure tide grows by about 40 Pa (search-summary), invisible next to kPa, and mean-pressure change is missing. **No pressure cue.**

## 2. Decisions needed (owner)

| # | Decision | Type | My recommendation |
|---|---|---|---|
| 1 | Open Lorenz 2021 (HAL PDF hal-03369920) or accept the no-number journal fallback | well-structured | A human with access opens it once; until then ship the fallback text in `journal-dust.md` |
| 2 | The thin-air argument stops being true as pressure rises: a 97 km/h wind feels like about 11 km/h on Earth today, about 31 km/h at 5 kPa, about 77 km/h at 30 kPa, about 120 km/h at 75 kPa (calc, 210 K assumed). Keep storms weak by game design rather than by physics? | ill-structured (design vs honesty vs fun) | Yes. Keep storms weak (the storm cap is already invented), but stop citing "thin air" for Phase 3 and equilibrium. Optional bonus: a Monitor "feels like on Earth" wind line that rises with pressure, which teaches the reason (B1, B5) |

## 3. Handoffs

> **To:** Lead Game Designer · **Problem type:** well-structured, with one ill-structured choice
> **Problem:** Ten balance-sheet and DESIGN edits (tag upgrades, two "thin air" lines, optional storm length and F1 warning).
> **Context:** `docs/science/storm-dust-review.md` §9 (table of numbers to change), §5, §6.
> **Your view:** No penalty number must change. Optional: Phase 1 storm 1 to 2 sols and F1 warning 2 sols (battery lasts about 4.3 sols). Monitor cues: haze row, day-night spread, wind "feels like".
> **Needed from them:** apply §9 items 1 to 8 and 10; decide item 9 and the cues; add the orbiter line to the authenticity audit if BELL's warning source is an orbiter.
> **Done when:** the sheet and DESIGN.md cite the review and no longer say "thin air" for later phases.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Journal wording corrections and one new entry.
> **Context:** `docs/learning/journal-dust.md` §3 ("days" must be "weeks"), §6 (F1 real-vs-game; the 97 % now has opened sources); review §10 (F1 explanation at two depths, `journal.storm.wind`).
> **Needed from them:** apply wording, goal IDs (B1, C1, A1), and word counts; keep "believed" for what ended Opportunity.
> **Done when:** journal-dust.md carries the new text and tags.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Storm values in data; optional pure "feels like" function.
> **Context:** review §5 and §11. Formula: 97 km/h × sqrt(P / (188.9 × 210 × 1.2)); expected 10.9, 31.4, 77.0, 121.8 km/h at 600, 5,000, 30,000, 75,000 Pa.
> **Needed from them:** keep storm cut, cap, duration, warning lead and dust-devil chance in datapack JSON; add the function and test only if the Designer approves the cue.
> **Done when:** values are in JSON.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Storm light is diffuse.
> **Context:** `docs/art/references/PIA25286.jpg` shows soft-edged shadows under a hazy sky.
> **Needed from them:** optional: soft shadows and lower contrast in storm keyframes.
> **Done when:** the storm keyframe note says so.

## 4. Risks

| Risk | Mitigation |
|---|---|
| Lorenz 2021 unopened, so "about 15 times faster than real" and 0.2 %/sol stay search-summary | No-number fallback text; a human opens the PDF |
| Day-by-day tau and power table comes from Wikipedia (citing NASA); only the 10 June point is on NASA's own pages | Keep the table in the review, put only "about 97 percent" in game text |
| NASA pages disagree on storm season, first-detection date (30 May / 31 May / 1 Jun), tau 10.5 vs 10.8 and 21 vs 22 Wh | Recorded in the review; no season in the game; use "more than 10" and "about 97 percent" |
| "Thin air" wording in DESIGN lines 139 and 229 becomes false at 30 to 75 kPa | Decision 2 |
| Storm duration and warning are compressed | Journal compression note (review §10) |
| Local-storm power loss has no figure | Not used; marked missing |

## 5. Shared model updates

- **Lesson:** a physics justification in a design rule ("thin air, so no wind damage") can be true in Phase 1 and false by Phase 3 when the player changes the planet. Check every "because Mars is X" reason against the Planetary Index range the player will reach.
- **Clarified fact:** NASA (JPL, 2022) says drained batteries are *believed* to have ended Opportunity; we say "believed", never "the storm destroyed it".
- **Clarified fact:** the game's worst storm (-60 %) equals about tau 2.4, a strong regional storm; the real worst is -80 % to -97 %.
- **Tool note:** from this session, science.nasa.gov, www.nasa.gov and en.wikipedia.org open by curl through the proxy; www.jpl.nasa.gov (403), NTRS, HAL, arXiv and most publisher or news hosts do not.
