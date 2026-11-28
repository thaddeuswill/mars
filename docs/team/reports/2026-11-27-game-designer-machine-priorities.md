# Game Designer report: machine priorities (2026-11-27)

Full detail: `docs/design/machine-priorities.md`. Method: document read-through only; no playtest, no build run.

## 0. Handoffs received
Owner approved the made-up band and keeping emitter, uplink and soil unit plainly fictional: accepted. Artist's priority column and order: accepted with changes (below).

## 1. Done
- `docs/design/machine-priorities.md`: class (G/L/N), gate and Index row for each of 11 machines, 6 machines the Artist list omits, gates with no machine path, 8 recipe conflicts, owner decisions.
- `docs/art/invented-machines-look.md`: priority column and section 3 work order only.
- **First batch: water reclaimer, atmospheric processor, super-greenhouse emitter**, after the band (2 h prerequisite).
- Changes to the Artist's order: extractor moves behind the processor and emitter (DESIGN unlocks it at Thaw); planter drops from G to L; dispenser cut to 4 kit items; uplink last (optional route).

## 2. Decisions needed
D1 redstone/copper source (ill-structured; recommend one rare ore, SME names analogue). D2 band on all machines (well-structured; recommend yes). D3 uplink required or optional (ill-structured; recommend optional). D4 first-kit carbonate source (ill-structured; recommend small mined vein).

## 3. Handoffs
> **To:** Subject Matter Expert · **Problem type:** ill-structured
> **Problem:** No Mars source for redstone or copper, but most machine recipes use them.
> **Context:** `docs/design/machine-priorities.md` R1, R3
> **Needed from them:** a real mineral to stand in; carbonate abundance
> **Done when:** the owner picks D1 and D4

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Gate files for Thaw, Hydrosphere, Phase 3 begins, Breathable, Equilibrium do not exist; the hydrosphere definition is open.
> **Done when:** gate JSON exists and the Designer supplies rates (R5)

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Add transit tubes; consider algae vat and meltwater treatment; cut dispenser.
> **Done when:** list updated

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Dome stable's real condition (is the reclaimer part of it?) and R6 (scrubber unlock) touch gate text.

## 4. Risks
- R1 blocks nearly every recipe if unresolved.
- No balance rows for any Phase 2 and 3 machine; Phase 2 is the longest phase (18 h).
- Artist estimates are unverified.

## 5. Shared model updates
Decision proposal: DESIGN.md gate table gets the scrubber at Phase 3 begins. Terms: G / L / N machine classes (`docs/design/machine-priorities.md`). Lesson: check recipe inputs against worldgen ores, not just the design table.
