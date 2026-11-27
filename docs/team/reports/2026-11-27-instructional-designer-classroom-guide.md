# Instructional Designer report: classroom pace in the educator guide, and the group-size range

## Handoffs received
Technical Lead, slice 9 (`2026-11-27-technical-lead-slice9-pace.md`): educator-guide wording for `/mars pace classroom`, creative bypass off, teacher-only guidance. Accepted. Done in `docs/learning/educator-guide.md` section 2.4.

## Done
- `docs/learning/educator-guide.md`: new section 2.4 "Choosing the pace (classroom mode)": the commands, what changes, what does not (thresholds), how to check, what to do if you forget, and a 7-item pre-class checklist. Section 2.3 step 1 now points to it. Marked untested: the chat text of the command (only run through the test dispatcher), and the checklist (no real class). The "Classroom pace" Monitor row is marked pending.
- Docs only. Nothing under `mod/` or `pack/` changed. Nothing was run.

## Opinion: classroom group size default 4, range 1 to 5 (ill-structured, one perspective)
**Frame.** The Decision Log (TEAM.md, "groups of 3 to 5 share one planet") and balance-sheet 5.1 ("teacher sets 3 to 5") describe classroom groups. The built range is 1 to 5 for both modes, default 4 for classroom. The range and default are the Technical Lead's choices. Competing goals: fit the settled design, versus not blocking a real teacher.

**Learning view.**
- Default 4 is right. The classroom time model (balance sheet 5.3) is built on a group of 4 (11.3 h, about 17 periods), so the default should be the size the numbers were tuned for.
- A classroom group of 1 or 2 breaks the design's reasons for groups: shared Shift Board, rotating posts, and discussion of forecasts. A pair at x4 with a scale of 0.74 would also run faster than the model was tuned for. Pairs and solos are not a classroom case we designed.
- A teacher's own dry run is the one real use of size 1. Standard pace plus `/mars index advance` covers it.
- Group size is per world, not per planet (Tech Lead risk). Real classes split unevenly (25 students: 5 groups of 5; 14 students: 3, 3, 4, 4). Sizes 3 to 5 cover that; the balance sheet says groups of 3 to 5 need no retuning.

**Options.**

| Option | Fits settled design | Teacher friction | Notes |
|---|---|---|---|
| A. Classroom 3 to 5, default 4; standard keeps 1 to 5 | Yes | Low; refusal message should point to standard pace for pairs | My recommendation |
| B. Keep 1 to 5 in both | Partly | None | Lets a class run a size the model never covered; no warning |
| C. 1 to 5 plus a warning below 3 | Partly | Low | More code and text for a case we did not design |

**Recommendation.** Option A. Keep default 4. Standard pace stays 1 to 5 (solo players and home groups). This is the Technical Lead's code and the Game Designer's number, so it needs their agreement; I did not edit the Decision Log or any code.

**Assumptions.** Groups of 1 to 2 in a classroom are rare; the 4-person throughput (divide building by 2.5) is the balance sheet's own assumption, not measured.

**Would make it better defined.** A first-slice playtest of a classroom group of 3 and one of 5, plus the owner's view on small clubs (pairs).

## Decisions needed
1. Ill-structured (owner, with Game Designer and Technical Lead): classroom range 3 to 5, default 4 (option A)? Recommend yes.
2. Well-structured (owner): should the Monitor "Classroom pace" row be built before any teacher-facing playtest? Recommend yes; a forgotten command is silent today.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** If option A is approved, `/mars pace classroom` should refuse group sizes 1 and 2 with a message pointing to `/mars pace standard`.
> **Context:** `pack/data/mars/pace.json` range 1 to 5; guide section 2.4.
> **Needed from them:** the range check for classroom only, and the Monitor "Classroom pace" row.
> **Done when:** the command and the guide text match.

## Risks
- A forgotten `/mars pace classroom` is silent until the Monitor row exists (R: wrong pace for a whole class period).
- The guide says the planet's group size is shared across planets; a mixed-size class gets one rate scale.
- Existing guide §2.2 still says teacher tools are buttons; they are planned, not built. Section 2.4 says so, but §2.2 text is unchanged.

## Shared model updates
None. (Pending owner answer on the classroom group range; then the Decision Log and balance-sheet 5.1 would need one line.)
