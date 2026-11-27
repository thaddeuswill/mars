# Report: SME, life-support science check (G2), 2026-11-27

Role: Subject Matter Expert · Docs only. Nothing built or run. Full review: `/home/user/mars/docs/science/life-support-review.md`.

**Honest limit.** `science.nasa.gov`, `www.nasa.gov` and `en.wikipedia.org` did not resolve from this session, so I opened no page today. Numbers marked "search-summary" come from search-result summaries of NASA pages; open the links in the review before calling them verified. MOXIE 300 W and 10 g/h come from an earlier SME session that did open NASA's page (`dimensions.md`).

## 0. Handoffs received

From the Game Designer (`2026-11-27-game-designer-dome-and-life-support.md`, section 3): mark each life-support number verified, invented or unverified; journal wording for the power compression and the faint state. **Accepted and done** in the review file. I did **not** edit `balance-sheet.md` (Designer's file, so "tags are in the table" needs the Designer to paste them; tags are in review section 1).

## 1. Done

- `/home/user/mars/docs/science/life-support-review.md`: tag table, reasoning for all seven items, journal wording at two depths, faint-state wording, sources and what I could not open.

Findings in short:

| # | Result |
|---|---|
| 1 | 0.84 kg per Earth day (NASA planning, search-summary) = 0.86 kg per sol (x 24.6 / 24). Game's 0.7 is **19 % low**. 0.68 came from a rounded "about 1 tonne for 4 for a year", so it is a low estimate, not a rival measurement |
| 2 | MOXIE 300 W, 10 g/h design, 12 g/h best, 122 g in 16 runs. "5 to 6 g/h" was the original **goal** (12 is twice the goal). 9 units a sol = 3 x 12 g/h x 24.6 h: consistent. The power discount is about 22x; it is also **below the physical minimum** (about 5 kWh per kg of O2, calc), which the sheet did not say. Acceptable **if** the journal says "discount for gameplay, real oxygen needs a lot of power" |
| 3 | Water 3 L is inside 2.4 to 3.5 L (NASA). ISS recovery is 93 to 94 % before the brine processor, about 98 % with it; the game's 90 % is below that, fine |
| 4 | Oven 0.5 L per sol needs about 25 kg of soil at 2 % water (Curiosity), about 2.8 kWh to heat (calc, unverified constants) against the game's 1.0: plausible. Teach "a few percent, locked in minerals", not "wet soil" |
| 5 | Real suit 7 h certified, 8 h nominal. Game suit is a time compression of about 25x, and is **3x more generous relative to daily air need** than real. Keep; say "17 minutes of play" |
| 6 | Rename "Faint" to "Blackout"; mark it a game rule; say real hypoxia is dangerous and poorly sensed; no medical details |
| 7 | Game leak = a crack 3 to 5 times worse than the worst ISS leak. The game ignores the air already in the room (about 190 kg of O2, ten times the tanks) and real large holes empty a room in minutes. CO2 removal is also left out. Fine if the journal says so |

## 2. Decisions needed (owner)

| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Change oxygen use from 7 to 8 units (and the numbers below), or keep 7 and label it "a little under NASA's 0.84 kg a day"? | Well-structured | Change to 8: a 19 % gap is easy for a teacher or student to catch and cheap to fix. Timing is unchanged (net still +2) |
| 2 | Accept the oxygen-power discount (below the physical minimum) as a labelled simplification? | Ill-structured (realism against Phase 1 pacing; goals C1, C2) | Yes, with the journal text in review section 3. The alternative (real power) needs about 4 panels per player and swamps Phase 1 |

## 3. Numbers to change if decision 1 is "change" (all in `balance-sheet.md` 2d; Designer to apply)

| Item | Now | Proposed | Why |
|---|---|---|---|
| Oxygen use per player per sol | 7 units | **8** | 0.8 kg, 7 % under 0.86 |
| Oxygen maker output | 9 units | **10** | keeps net +2, one maker per player |
| Start fill O2 | 11 per player | **14** | keeps (reserve 24 minus 14) / 2 = 5 sols to Dome stable |
| Reserve needed (3 sols x use) | 21 per player | 24 | follows from use |
| Leak with a breach | 10 | 10 (no change) | still equals a full group's surplus |
| Wall tank 200: time for 5 players | 5.7 sols | 5.0 sols | follows from use |
| "Tank empties in" column (2d.7) | 4.4 / 11.0 / 4.4 / 6.9 | about 4.7 / 10.5 / 4.7 / 7.3 (calc, recheck) | follows |
| Table tags | see review section 1 | | water 3 L: verified range; recovery 90 %: below ISS; Oven 0.5: invented, plausible; suit: invented, time compression; MOXIE 300 W: verified |
| Unchanged | water 3, 90 %, Oven 0.5, suit 6 and 10, maker power 1.0, tanks, leak | | |

Also to fix in the sheet: change "SME to confirm" notes to the tags; the line "if MOXIE draws about 300 W (unverified)" becomes **verified**; add "real minimum about 5 kWh per kg" to the compression note.

## 4. Handoffs

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** apply the tags and (if accepted) the numbers in section 3 to `balance-sheet.md` 2d; rename "Faint" to "Blackout" in 2d.6; check the Oven water is not also counted in the hydrosphere index.
> **Context:** `docs/science/life-support-review.md`.
> **Needed from them:** updated table and wording.
> **Done when:** 2d tags match the review and the arithmetic in 2d.7 is rechecked.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** reading-level pass for the journal drafts and the Blackout text; add the new misconception "oxygen is easy to make on Mars" (transfer risk from the power discount) to the item pool.
> **Context:** review sections 3 and 4.
> **Needed from them:** content-rules check (CR rules), fade rule.
> **Done when:** strings accepted.

> **To:** Producer / owner · **Problem type:** well-structured
> **Problem:** allow `science.nasa.gov`, `www.nasa.gov`, `en.wikipedia.org` in the session network, or have a person open the links in review section 5, so the "search-summary" tags can become "verified".
> **Done when:** the nine links are opened and the tags updated.

## 5. Risks

- Several numbers are only search-summary tagged this session; a wrong reading is possible. Highest-impact ones: 0.84 kg (drives decision 1), ISS water recovery, suit hours.
- Transfer error: students may leave believing oxygen on Mars is cheap. Mitigation: journal text plus the Monitor showing the maker as the biggest load.
- The air already in the room is a bigger reserve than the tanks in real life. If a playtester asks "why not breathe the room's air", the answer is a game simplification; have the journal line ready.
- Calc constants (enthalpies, air density, flow coefficient) are from memory.

## 6. Shared model updates

- **Term:** "game rule" label for anything the game does that real life does not (blackout, free suit refill, power discount). Use it in journal text.
- **Lesson:** per-day figures from NASA must be converted to per-sol (x 1.0275) before they are compared with game numbers; "about 1 tonne for 4 for a year" is a rounded estimate and reads low against NASA's 0.84 kg planning value.
- **Lesson:** a "compression" can cross a physical limit (here, oxygen power below the thermodynamic floor). Check the floor, not only the real machine.
- **Lesson:** the network allow-list may block NASA hosts; note it in the report and tag "search-summary".
