# Report: Game Designer, dome layout (G1) and life-support table (G2), 2026-11-27

Role: Lead Game Designer · Docs only. No Java, no pack. Nothing built, run in game or playtested. All numbers are **proposed**; invented numbers are labelled invented; arithmetic is **calc** (by hand and a throwaway script outside the game, not run in the game).

## Owner-facing summary

Stage 3 slices 1 and 2 were waiting on two inputs. Both now exist as proposals.

- **Dome (slice 1):** one shell, 23 x 16 blocks, for every group size. Shelter with 5 bunks and 5 air tanks, Ops with the Monitor and journal, a machine bay (5 oxygen maker bays, reclaimer, 3 batteries, 2 Oven bays), an annex (Hab B, the leak you patch in Landing), storage, suit room and a 3 x 3 airlock with the dust-off station. Sealing rule: a full block or nothing; a volume is sealed if the flood fill ends without reaching open sky or the cap. Panels go on an east apron in 3 x 3 pads, far from machinery.
- **Life support (slice 2):** one rule the player can learn: **one oxygen maker per player** (a player uses 7 air units a sol, a maker makes 9). Dome stable now means: dome sealed, air made at least as fast as used with 3 sols in the tank, water looked after, power covers life support with a night's battery, held 3 sols. Power priority: life support, then Ovens, then Outgassers.
- **Timing:** Dome stable arrives at about **3.1 h solo** (sol 9.2) and 3.4 to 3.7 h for a group in standard mode, about 0.8 h in the classroom. The old curve for pressure still holds (within 0.03 kPa). A fast player who builds a second maker right away gets there in about 2 h.

Files: `docs/design/dome-layout.md` (new), `docs/design/balance-sheet.md` (new section 2d, pointer line in section 2).

## 0. Handoffs received

From the Producer (`2026-11-27-producer-stage3-plan.md`, section 3): G1 dome spec and G2 life-support numbers. **Accepted and delivered** (G1 under the name `dome-layout.md`; G2 as section 2d, not 2b, as the coordinator asked). Also asked: Landing step 6 text, rose-sky trigger and "Useful now" content for slice 4. **Not done here** (out of this task); still open for the Game Designer before slice 4.

## 1. Done

- `/home/user/mars/docs/design/dome-layout.md`: scale rules, ASCII plan (23 x 16), rooms and purpose, positions of Oven, battery, panels, Monitor, bunks, journal, mast and airlock, the sealing rule (8 points), fit with the 3 x 3 array (pitch 4, apron of 5 pads), groups of 3 and 4 (same shell; fallback 23 x 14 for 3), worked blocks list (about 728 hull plates, 46 glass, 7 doors), open questions.
- `/home/user/mars/docs/design/balance-sheet.md` section 2d: use per player per sol, machines and tanks, recipe costs (standard and classroom), power tiers with a panel table for n = 1 to 5, the real Dome stable condition as four named states, the failure-state table (what the player sees), the timing arithmetic and the pressure-curve check, proposed JSON keys, open items. Pointer line added to section 2.
- Scaling rule applied: thresholds never scale; survival numbers are per-sol physical numbers and scale only with `group.size`; recipe cost, hold and the reserve requirement are the classroom levers.

## 2. Decisions needed (owner)

| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Dome stable moves from about 1 h to about 3.1 h solo (3.4 to 3.7 h for a group, 0.8 h classroom), as designed and warned in R-73. Accept as "prototype timing"? | Well-structured (the design says 3 h) | Yes. A fast player can still reach it in about 2 h by building a second maker. |
| 2 | When air runs out: **faint and wake in your bunk, nothing lost, no death** (proposed), or a real death and respawn? | Ill-structured (fear and stakes against ages 12 to 18 and C1) | Faint with a failure report. It teaches the cause without punishing a 12-year-old; revisit if no playtester ever runs low. |
| 3 | The classroom lowers the "air reserve needed" from 3 sols to 1 (like a hold). Is that acceptable under "physical thresholds never scale"? | Ill-structured (rule wording against classroom time) | Yes. It is a pacing requirement, not physics; no Index or breathing threshold changes. Alternative: keep 3 and start classroom tanks fuller, which hides the same choice. |

## 3. Handoffs

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** check the life-support numbers.
> **Context:** `docs/design/balance-sheet.md` section 2d.8 and 2d.1 to 2d.2. 0.7 kg O2 per person per day (secondary source), water 3 L and 90 % recovery (unverified), maker power compressed about 22 times against MOXIE (about 300 W is unverified), Oven water 0.5 per sol, suit duration, fainting wording.
> **Needed from them:** each number marked verified, invented or unverified, and journal wording for the power compression and the faint state.
> **Done when:** tags are in the table.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** reading level and fading for the failure states; is "reserve in sols" too much for Landing?
> **Context:** `docs/design/balance-sheet.md` section 2d.6 (sketch wording), 2d.5.
> **Needed from them:** wording pass (CR rules), fade rule for first Low and Unsealed reports, event names for the log; also the Landing step order (dome-layout §3, step 5 walks back through the shelter).
> **Done when:** strings and fade rules agreed, or "reserve" moved to read-more.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** slice 1 and 2 inputs.
> **Context:** `docs/design/dome-layout.md` (structure template, flood-fill cap proposal 2,000, partial blocks not airtight); balance-sheet 2d.5 (gate `all_of`, `priority`, JSON key list, tests compare data to table).
> **Needed from them:** accept or amend; a Phase 1 raw-resource list for recipe costs (G4, with the Artist); say what happens to existing worlds when `lifeSupportOk()` is retired.
> **Done when:** slice 1 and 2 estimates reference these pages.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** straw-man hull and glass, oxygen maker, reclaimer, airlock models at one-block scale.
> **Context:** dome-layout §8.
> **Needed from them:** straw-man or "ship on SM".
> **Done when:** slice 1 and 2 have models.

> **To:** Codex (CX-C) · **Problem type:** well-structured
> **Problem:** once accepted, a test that compares the 2d numbers to the JSON.
> **Context:** balance-sheet 2d.5 key list.
> **Done when:** test fails on a changed JSON.

## 4. Risks

- **Reserve rule is the timing.** About 5 of the 6 sols to the condition come from refilling the tank. If playtesters feel they are only waiting, the lever is the start fill (11 per player), not the hold. Mitigation: the second-maker shortcut and Ovens, panels and the annex give work during the wait (watch for boredom, DESIGN.md flow table).
- **Standard groups of 4 land at 3.67 h**, the top of the 25 % tolerance. The assumed build schedule is invented (0.6 and 0.4 sol per maker).
- **Hidden second gate:** power for four Outgassers needs 5 panels solo (about 20 units), which the 8-hour Phase 1 target depends on (already flagged in section 2; now a measured prototype item).
- **Power compression** (22 times cheaper than the real ratio) is a teaching simplification the SME must accept; if not, the panel table changes.
- **Dome size** (about 700 blocks of air, 728 hull plates) is large for straw-man work; the shell is one template to limit cost.
- Number sources: 0.7 kg O2 is from a secondary source; water 3 L, 90 % recovery and MOXIE power are **unverified**.

## 5. Contradictions found

1. DESIGN.md (group play) says use scales with "real player count"; balance-sheet 5.1 says group size is config, "not read from who is online". Section 2d follows 5.1. Propose a one-word fix in DESIGN.md ("configured group size"): not edited here.
2. Stage 3 plan names the dome file `dome-spec.md` and puts G2 in section 2b; the files are `dome-layout.md` and 2d.
3. Balance sheet section 2 listed "Water for the dome" as an Oven output while 2c decision 6 deferred it; 2d now activates it (0.5 per sol each).
4. Classroom Phase 1 check (section 5.3) assumes Dome stable on sol 3; 2d gives sol 2.5. To update when 2d is accepted.

## 6. Shared model updates

- **Term:** "one oxygen maker per player" (the rule the Monitor teaches); "reserve" = stored O2 in sols of use.
- **Decision proposed (owner Qs 1 to 3; owning documents `docs/design/dome-layout.md`, balance-sheet 2d):** one shell for all groups; Dome stable = sealed + air + water + power; life support is tier 1; survival numbers scale only with `group.size`, never with pace.
- **Lesson:** a hold alone does not set the timing of a gate; the thing the player must refill or build before the hold starts does. Tune that first.
