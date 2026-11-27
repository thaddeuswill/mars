# Report: SME, storm figures check (2026-11-27)

Docs only. Desk check with sources I actually opened.

## 0. Handoffs received
Game Designer storm-length report, SME handoff (mark verified / not verified). Accepted. Mostly not completed: sources blocked (below).

## 1. Done
Access: shell fetch of `nssdc.gsfc.nasa.gov` works but only the fact sheet is there (no storm data). **Blocked by the agent proxy (HTTP 403, not bypassed):** `science.nasa.gov`, `www.nasa.gov`, `mars.nasa.gov`, `ntrs.nasa.gov`, `en.wikipedia.org`, `europepmc.org`. **WebFetch could not resolve** `www.jpl.nasa.gov` and `www.planetary.org` (need allowing in session network settings). **Open:** PubMed / NCBI (E-utilities, PMC XML). WebSearch gave titles only and itself said it could not confirm 97 %.

| Figure | Mark | Evidence |
|---|---|---|
| Regional storms last days to weeks; global months | **NOT VERIFIED** as a general statement. Two real cases found: InSight 2019 storm about 20 sols; 2018 global storm effects at Gale 55+ sols | Lorenz et al. 2020, PMID 32715027 (PMC7375148 full text): "grew dramatically over Sols 40-60 to a peak of 1.9 associated with a large dust storm" (tau 0.7 to 1.9). Viudez-Moreiras et al. 2019, PMID 31534881: "decreased by 90% between sols 2075 (opacity ~1) and 2085 (opacity ~8.5)"; "Between sols 2085 and 2130" the night inversion was absent. Sols 2075 to 2130 is 55 sols, about 1.9 Earth months [calculated; one sol is about 1.03 days]. Gale, not Opportunity's site |
| Opportunity -80 % (2007) | **NOT VERIFIED** | Not in any opened source |
| Opportunity -97 % (2018) | **NOT VERIFIED** | Not in any opened source. Search results confirm only that power fell, the rover went to minimal operations and fell silent on 10 June 2018 (not opened) |
| InSight about -35 % | **NOT VERIFIED** | The 425 to 275 Wh/sol figure in `dust-review.md` is 35 % by my arithmetic (150 / 425 = 35.3 %), but it is from a 2022 regional storm and the source was not opened. The opened Lorenz paper (2019 storm) gives tau, not a percent |
| 60 % cut is a fair game value | **Judgement, not a verified fact** | See below |

**Judgement (one line).** A 60 % cut at cap 1.0 is a fair game value: it sits between the claimed regional (-35 %) and global (-80 % to -97 %) figures, and it hurts without ending the game. It is fair only if those figures hold, so keep it tagged invented until the 2018 and 2007 numbers are opened. "Game storms are much shorter than real" **is honest** and now verified for the cases above: 2 sols against about 20 sols (regional) and 55+ sols (global at Gale), so 10 to 25 times shorter.

Edit: `docs/design/balance-sheet.md` storm table, Phase 1 storm row, marks only (no number changed). I did not touch the dust-review marks or the line-102 power row (outside the storm table); they still say "searched".

## 2. Decisions needed
- Well-structured, owner: allow `www.jpl.nasa.gov`, `science.nasa.gov`, `www.planetary.org` (and `ntrs.nasa.gov`) in the session network settings so I can open the three missing sources. Recommend yes; one short pass finishes it.

## 3. Handoffs
> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Needed:** until verified, journal text must not quote -80 %, -97 % or -35 %. It may say "real storms last weeks or longer; the game storm lasts 2 sols" (verified cases above). **Done when:** grep of journal text clean.

## 4. Risks
Unverified numbers could reach students as facts. Mitigated by the tags above.

## 5. Shared model updates
None. (Lesson: the sandbox allows PubMed/NCBI only; NASA pages need the network allow-list.)

## Addendum (same day): sources opened after network allow-list update

Access: `www.jpl.nasa.gov` still returned HTTP 403 for every page (not bypassed); `www.nasa.gov` 403. Opened instead: `www.planetary.org`, `science.nasa.gov` and `nssdc.gsfc.nasa.gov` (NASA press release text). Percentages are my arithmetic from the quoted watt-hours (Wh/sol).

| Figure | Mark | Quote and URL |
|---|---|---|
| Opportunity 2007, about -80 % | **VERIFIED** (-82 % by arithmetic: 128 / 700 = 18 % left) | NASA release 07-162, 20 Jul 2007: "Opportunity's solar panels had been producing about 700 watt hours of electricity per day" and "Opportunity's solar-panel output dropped even lower, to 128 watt hours." https://nssdc.gsfc.nasa.gov/planetary/text/mer_pr_20070720.txt. Same release: dust "has blocked 99 percent of direct sunlight". Use "about 80 %" |
| Opportunity 2018, about -97 % | **VERIFIED** (-96.6 %: 22 / 645 = 3.4 % left) | Planetary Society, 2018: "around 645 watt-hours ... on June 1st ... Opportunity was producing just 22 watt-hours of power." https://www.planetary.org/articles/06-mer-update-opportunity-dust-storm-sleep . Secondary report (journalist, quoting the team), not a NASA page. The 97 % wording was seen only in a search snippet, not opened |
| InSight about -35 % | **VERIFIED** (-36 %: 275 / 430 = 64 % left) | NASA Science, InSight blog, 10 Oct 2022: "a sharp decline last week from 430 watt-hours per sol to a low of 275 watt-hours per sol. The decline was caused by a regional dust storm". https://science.nasa.gov/blogs/insight/2022/10/10/nasa-insights-power-level-as-of-oct-8-2022/ . The 425 figure (-35 %) is from a JPL page I could not open. Caveat: panels were already dusty before the storm |
| Storm lengths | Specific cases **VERIFIED**; general rule **NOT VERIFIED** | 2007: "For nearly a month, a series of severe Martian summer dust storms" (release 07-162, URL above). 2018: "the thick cloud of dust that has blanketed the Red Planet for the last six weeks" (https://www.planetary.org/articles/08-mer-update-opportunity-sleeps); same page: 2007 storm "subsided in a matter of weeks" (https://www.planetary.org/articles/06-mer-update-opportunity-dust-storm-sleep). Jan 2022 regional storm: InSight "hunkered down for 11 days" (https://science.nasa.gov/resource/mars-report-dust-storms-on-mars/). Planetary Society: big storms "build for weeks, and become planet encircling events that last a long time, or not" |

Verdict on the game values: the real cuts (-36 % regional, -82 % and -97 % at the global events) bracket the 60 % storm cut, so it remains a fair game value; keep it labelled **invented** (it is a game number, not a measurement). Real storms last about 11 days to 6+ weeks, so a 2-sol game storm is still much shorter (honest note stands). Journal text may now quote the three power figures with the sources above.
