# Game Designer report: recipe inputs ruling, SME wording, sheet row

Date 2026-11-28 · Branch `claude/elegant-brown-anu1ci` · Docs only (nothing under `mod/` or `pack/`). Full ruling: `docs/design/recipe-inputs-ruling.md`. Not run: the unit tests (docs only; I read the test and checked the row prefix by hand).

## Handoffs received
- Technical Lead slice A, decisions 1 to 3 and the sheet row: **accepted**, answered below.
- SME hydrosphere handoff (resin, sorbent, 30 percent label, loss direction, heater wording): **accepted**, applied. Heater tooltip and the "salt comes from soil" journal line are noted in `gate-input-items.md`; the `heater_coil` tooltip itself is in `pack/` (Technical Lead).

## Done
- `docs/design/recipe-inputs-ruling.md` (new): lander salvage plus Mars path, exact caches, tags, new items, label text, row name, brick stand-in, soil ladder.
- `docs/design/gate-input-items.md`: resin pack row and 3.4 relabelled "game-sized version of a real idea" (SIMPLIFIED); sorbent bed row and 3.5 say real beds are regenerable and the game locks CO2 into rock; brick is an INVENTED stand-in; open-questions row closed.
- `docs/design/phase3-hydrosphere-and-drift.md`: 30 percent label "Game design, based on real ice" with the 34 m and 15 to 30 m check; loss note "freezing, vapor loss, soaking in; rate invented"; v2 loss larger when cold and at low pressure; 2.5 marked answered.
- `docs/design/balance-sheet.md`: extractor row now "built, slice A" (+0.30 %/sol, 2 units/sol, at or above -20 C); loss row notes SME verification and the label.

## Decisions needed (owner)
See ruling section 7: (A) accept the rule and counts, well-structured, recommend yes; (B) basalt board in the `planks` tag, ill-structured, recommend yes; (C) furnace fuel, ill-structured, recommend 8 lander coal now.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** implement the ruling.
> **Context:** `docs/design/recipe-inputs-ruling.md` section 8 (starter chest, gate caches, two tag files, basalt fibre and board recipes, boot test).
> **Needed from them:** the six items; also confirm `GateCaches` takes vanilla ids; run `IceExtractorTest.dataMatchesTheBalanceSheet` once. It finds the row by the prefix `| Ice extractor`, so my rename to "(built, slice A)" should not break it, and the numbers are unchanged. If you changed the prefix, update the string with it.
> **Done when:** a survival player with mined basalt and iron can craft furnace, hopper and table; tests and boot checks pass.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** confirm "basalt fibre" as a real material and the tooltip wording.
> **Done when:** marked verified or reworded.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** wording check of the basalt board and fibre tooltips, the lander journal note, and the row-name pair "Surface water (the Hydrosphere)".
> **Done when:** confirmed against CR rules.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** icons for basalt fibre and basalt board (grey-green board, clearly not wood).

## Risks
- Fuel is unsolved; without item C a stuck player can still use lander parts, but cannot smelt more.
- Boards in the `planks` tag may leak into wood tools. Cheap to reverse.
- All counts are INVENTED and untested; first playtest should note where players run out of hoppers.

## Shared model updates
Proposed (not edited): terms *lander salvage* and *Mars path*; Decision Log entry on adding Mars materials to vanilla tags instead of editing recipes. Text in ruling section 9.
