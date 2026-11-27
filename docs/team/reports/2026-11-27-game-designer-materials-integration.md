# Game Designer report: materials integration (2026-11-27)

## Handoffs received
- SME world-and-blocks verdicts (4 edits) and SME materials/recipes handoff ("pick recipes, amounts; flag any that skip a gate"). Accepted, all. Owner direction: vanilla stays, Mars adds, invented blocks/chemistry allowed if labelled.

## Done
All in `docs/design/world-and-blocks.md` (docs only; nothing under `mod/` or `pack/`). New section 0.1 lists each change and where it landed.
1. Glass ore is "sand", dark red-brown (no silica/quartz).
2. Perchlorate seams are invented; the scanner reads a soil percentage.
3. North cap: water ice is the main polar block; CO2 frost is a thin secondary layer.
4. "Stylised" notes for canyon and caves; depth note on every survey scan.
5. Washing step labelled *game design*.
6. Invented labels: ore amounts and distances, 64 and 150 to 400 rules, collapse, extractor on ice only, scanner (section 3.5).
7. Dome at a stated high-northern, Phoenix-like "latitude"; exact latitude marked not verified.
8. SME spine loop adopted for Phases 1 and 2 (section 3.6); aluminium cut; basalt fibre kept for hull panels only.
9. "Game invention, not real" / "Game-sized version of a real idea" on every invented machine.
10. New section 3.6: vanilla-first recipe table (resources, machines, farming), with Index/build task, status mark and gate check; a "vanilla kept" table shows crafting, furnace, tools, glass, bricks, farmland, bone meal still work. Amounts marked invented.
Not run: no game, no tests.

## Decisions needed
1. Hand-melted ground ice gives dome water but does not move the Hydrosphere row (machines do). Recommend yes; keeps the balance sheet valid. *Well-structured.*
2. Keep basalt fibre (hull panels only) or cut it too? Recommend keep; one furnace step. *Ill-structured (scope vs. fun), low stakes.*
3. Carry over earlier open items: W1 vs W1-lite first slice; new terrain only in new worlds. *Ill-structured / well-structured.*

## Handoffs
> **To:** SME · **Problem type:** well-structured
> **Needed:** source or "game design" confirmation for washing; carbonate abundance; Phoenix latitude. **Done when:** marks in section 2.2 and 3.6 updated.

> **To:** Technical Lead · **Problem type:** well-structured
> **Needed:** recipe shapes and KubeJS wiring from section 3.6; dome pad on noisy ground (still open).  **Done when:** recipes load and boot test passes.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Needed:** alignment check of the journal labels and the "diamonds not on Mars" joke line; goal IDs unchanged. **Done when:** labels in journal content.

## Risks
- Recipe amounts are all invented; a long chain (canister, electrolyser, Sabatier) may feel like grind (pillar 3). Tune in first playtest.
- Shallow ice near the dome may still teach "ice is everywhere"; the latitude line and scan note must ship together.
- Items still NOT VERIFIED (washing, carbonate, lava tubes) must not appear as fact.

## Shared model updates
- Term: "spine chain" (SME) = CO2 canister + ice, electrolysis, Sabatier; it is the crafting spine of Phases 1 and 2.
- Decision (proposed, owning doc `docs/design/world-and-blocks.md`): dome at high-northern Phoenix-like latitude; north cap is water ice; aluminium cut.
