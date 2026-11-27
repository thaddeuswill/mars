# Report: Artist, invented machines look brief (2026-11-27)

## Handoffs received
None. Task from the coordinating session; docs only.

## Done
- `docs/art/invented-machines-look.md`: look brief for 11 invented machines (reference, size in blocks and px, cues, palette, dust behavior, priority), one shared "made-up band" cue, a work order and an unverified effort estimate (about 51 h total).
- Nothing under `mod/` or `pack/`; no binary art; no images downloaded. Image references are named as NASA library search terms.
- Sizes: the SME doc has no real sizes for these machines, so all are marked invented.

## Decisions needed (owner)
1. **Approve the made-up band** (dashed 2 px Fiction-purple `#CC79A7` band on side bases plus a 4 x 4 px slashed plate). Recommendation: yes; matches the existing Fiction badge. Ill-structured (also touches Designer/ID wording).
2. **Which of the two no-reference machines (emitter, mirror uplink) stay.** Recommendation: keep both, plainly fictional, as the SME advised. Ill-structured.
3. Confirm gate-critical set with the Game Designer (well-structured).

## Handoffs
> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** My priority column follows the block list "Why" column, not confirmed gate data.
> **Needed from them:** list which invented machines gate progress; whether survey scanner and mirror uplink stay.
> **Done when:** the priority column is corrected in `docs/art/invented-machines-look.md`.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Dust weathering for machines: one 4-frame overlay texture per machine vs separate models.
> **Needed from them:** confirm the overlay approach is feasible, and whether the Monitor/look-at card needs a dust readout for machines.
> **Done when:** reply in the next Technical Lead report.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Tooltip and journal wording must match the band ("Game invention, not real" vs "Game-sized version of a real idea") per machine.
> **Needed from them:** per-machine wording check against goal IDs.
> **Done when:** wording table agreed.

## Risks
- Effort estimate is unverified.
- Planter LED pink `#D9A0C8` sits near Fiction purple; needs a colorblind simulation check before final.
- The SME marks the soil unit, emitter and mirror uplink references as NOT VERIFIED, so the real-reference cues for those are generic.
- Dust overlay on 10 machines could multiply asset count if done as models.

## Shared model updates
- New convention (proposed, pending owner): the **made-up band** is the in-world mark for invented machines; Fiction purple `#CC79A7` stays reserved for "fiction" in world and UI.
