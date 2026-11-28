# Report: Game Designer, F1 and first dust storm spec (Stage 3 slice 5 inputs)

**Date:** 2026-11-28 · **Mode:** documents only (no Java, no pack). Nothing was run or playtested; this is design judgment from the documents.

## 0. Handoffs received
Task came from the coordinating session ("continue with slice 5 inputs"). Accepted in full. Questioned one point: the balance-sheet curve's 2-sol storm (see Risks).

## 1. Done
- **`/home/user/mars/docs/design/f1-storm-spec.md`** (new): (1) F1: card, shuffled choices with a misconception per distractor, HS "Why?", lock point and what the player has seen by then, group-hidden rule, `resolve_source: storm.solar_cut` and `outcome_check`, result at two depths, `journal.storm.first` (exists only after resolve), "What caused it?" item `explain.f1.after_storm`; (2) the first storm: onset sol 14 standard / 4.5 classroom, 1-sol warning (log, HUD, Monitor, haze), 0.75-sol first storm (classroom 0.5), visibility through a keyframe overlay with a fog-only fallback, solar cut table by phase (calc), storm-end +2/+1 stages in two steps, brush and wiper, scaled / chosen / never-scaled table; (3) dust timing by pace, the Monitor lines with the height budget and what drops; (4) 21 acceptance criteria; (5) ID and SME lists; (6) three owner questions.
- **`/home/user/mars/docs/design/prototypes/first-dust-storm.md`**: two stale cause-line strings "Dust on panels −60 %" corrected to "Storm: less sunlight −60 %" (panel dust has its own line).

## 2. Decisions needed (owner)
1. **Classroom storm numbers** (ill-structured): first storm sol 4.5 and 0.5 sol long, or plain ÷4 (3.5 and 0.75). Recommend 4.5 / 0.5.
2. **Ship F1 pending external review** (well-structured): recommend yes.
3. **A "how much?" follow-up to F1** (ill-structured): recommend not now; decide from playtest accuracy.

Decisions I made within my lane (for the log): the dust line, its BELL line and `journal.dust.panels` are held until the Landing is done or skipped (the starting panel is at stage 1 from tick 0); F1 choice order is shuffled per player; the first storm schedule is fixed with no rubber-banding; the first storm is 0.75 sol; the after-storm "What caused it?" item replaces an in-storm one (the F1 result would answer that one).

## 3. Handoffs

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Build slice 5 to `docs/design/f1-storm-spec.md`.
> **Context:** §2.5 formulas, §4 acceptance criteria AC-1 to AC-21. New data: `data/mars/hazards/dust_storm.json` (caps, schedule, lengths) and an F1 forecast JSON with `resolve_source`. The overlay needs a `mars:storm_haze` and a `mars:storm_strength` input in the keyframe loader (slice 6 accepts only pressure); fog-only fallback is allowed. Brush recipe suggestion Fe 2, W 1.
> **Needed from them:** build, or amend with a reason; measure the Monitor height with both new lines (my +20 px is an estimate, not measured); confirm tier 2 and 3 get nothing while tier 1 uses the battery.
> **Done when:** AC list passes or deviations are in the report.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Pass over the texts in spec §5.1 (F1 card and results, `journal.storm.first`, `explain.f1.after_storm`, log and Monitor keys), and confirm `journal.dust.panels` lets no one infer "storms cut power" before the lock.
> **Needed from them:** edits or approval; CR-T7 check of the "−" and "%" forms; I3 distractor review or the "pending external review" mark.
> **Done when:** strings final with reading-level mark.

> **To:** Lead Subject Matter Expert · **Problem type:** well-structured
> **Problem:** Check the facts in spec §5.2, especially: is a hazy sky a real storm precursor (unverified), "blocks" vs "blocks and scatters", wind-cleaning wording.
> **Done when:** each marked verified, searched or unverified; haze cue kept as real or relabelled as a game cue.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Storm overlay keyframes (haze, storm, 18 % fog, 30 % sun) driven by `storm_strength` and `storm_haze`, dust-stage textures during the ramp-out, AU-W stubs.
> **Done when:** keyframe file and textures match spec §2.4, or the fog-only fallback is accepted.

## 4. Risks
- **Balance sheet §2 curve assumes a 2-sol storm** (sols 14 to 15); every other source says 0.5 to 1 sol. Spec uses 0.75. Calc effect on the curve is small (about 0.14 kPa with 3 Outgassers). The §2 note should be corrected; I did not edit the balance sheet in this task because tests compare it to JSON.
- **Starting panel is at stage 1**, so the "first time a panel reaches stage 1" trigger fires at tick 0 (inside the Landing). Fixed by the Landing-hold rule above; needs the ID to agree.
- **F1 will probably be answered right by most players** (the haze and the word "storm"). Accepted as a first-win forecast; log accuracy.
- **Monitor height:** +20 px worst case is an estimate. The plot goes first; at 236 px high the panel may exceed target.
- **No power-history plot exists**, so the art storyboard's "graph shows the dip with a callout" cannot be built; only an optional event marker on the pressure plot.
- **Overlay loader is more than JSON** (slice 6 accepts only pressure); fallback is fog only.
- Wiper's bearing waits for slice 6 maintenance parts.
- No external reviewer yet (I3, R-51); all distractors are SME round-2 text, pending review.

## 5. Shared model updates
- **Decision (owner: DESIGN/balance sheet via f1-storm-spec.md):** first storm is a fixed schedule: onset sol 14 standard, 4.5 classroom (chosen, not scaled); 1-sol warning in both; length 0.75 sol (classroom 0.5). Pending owner Q1.
- **Decision:** forecast choice order is shuffled per player; anonymous tally after resolve only.
- **Decision:** F1 does not need the "look first" card (nothing on the Monitor reveals the answer before onset).
- **Lesson:** a pre-placed machine in a starting state (panel at stage 1) can fire a "first time" trigger at tick 0; every "first time" reveal needs an explicit "after Landing" guard.
- **Lesson:** when a forecast's in-event cause is named in its result, the "What caused it?" item should ask about the *next* cause, or it tests recall.
