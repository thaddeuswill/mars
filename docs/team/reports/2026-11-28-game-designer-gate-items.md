# Gate input items: report (Lead Game Designer, 2026-11-28)

Deliverable: `docs/design/gate-input-items.md`. Docs only; nothing under `mod/` or `pack/` changed. Design walkthrough only (no real players, nothing run).

## Handoffs received
Owner task: one new input item per gate, hold-to-explain cards, planet report page, 3 build slices. Accepted. One change to earlier work: the heater coil recipe moves from "iron + coal" to "2 iron ingots" because Mars has no coal (the earlier doc had flagged this for the SME).

## Done
- Gate-to-item table, item detail (obtain, recipe, unlocks, label, journal), never-blocked rules, 3 build slices, 9 hold-to-explain cards (24 words or fewer), planet report page template and per-gate side effects. All amounts INVENTED.

## Decisions needed (owner)
1. Where the cache arrives: inventory (like the Ovens) or a lander crate. Well-structured. Recommend inventory.
2. Heater coil recipe without coal (2 iron). Well-structured. Recommend yes.
3. Hydrosphere report side effect: salt level or wildlife signature. Ill-structured. Recommend salt level (B5), fauna on the Monitor.
4. Oxygen maker and the made-up band (carried from seed-kit doc section 4). Well-structured.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** build slices A to C in section 5 (items, caches, recipe edits, snapshot at latch).
> **Needed:** `cache` field in gate JSON; recipe edits for Outgasser, processor, emitter; hematite spherule item check. **Done when:** boot checks in section 5 pass.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** confirm or replace two real ideas: ion-exchange resin for perchlorate, porous brick as CO2 sorbent (both unverified).

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** alignment check of goals cited; CR-F9 scan of the 9 cards and 7 tooltips; wording of report page and supply notes.

> **To:** Lead Artist · **Problem type:** well-structured · **Problem:** 7 item icons, card panel look, report page look.

## Risks
- Items are soft gates (recipes are open; the machine lock does the gating); a player may feel the reward is small. Playtest.
- Report page needs new stored data (snapshots, collapse counter, perchlorate level, repair counts): schema bump.
- Slice C depends on the unbuilt conversion mechanism.

## Shared model updates
Proposal only: "Gate cache" = one-time item delivery at a latch; every gate has one new input item the next machine needs (rationale: Create-style recipe-chain gating, alternative: unlock list only, revisit if playtest shows players ignore items). Not edited in TEAM.md or DESIGN.md.
