# Report: Lead Game Designer, world and blocks plan (2026-11-27)

Deliverable: `docs/design/world-and-blocks.md`. Walkthrough and document review only; nothing built or run. Docs only (no edits under `mod/` or `pack/`).

## Handoffs received
Owner question (via coordinator): terrain generation and blocks, keeping mining, gardening, building. Accepted. No other handoffs.

## Done
- `docs/design/world-and-blocks.md`: verbs per phase, target terrain, data-only versus code table, three worldgen steps (W1 to W3), block/item list with gate/learning/nice-to-have marks, Index and science ties, scope with a recommended slice, an alternative and a cut list, two walkthroughs.
- Key design rule proposed: **blocks never change Index numbers; they change what you can build, where, and at what cost.** Ground ice must sit under an ice extractor, so terrain supports the Hydrosphere quest without rebalancing.
- Starting facts checked: `planet_1.json` is a flat generator; one biome `regolith_flats` with no features; `structure/empty.nbt` only; ice rule exists as data.

## Decisions needed (owner)
1. W1 shaped world as the next world slice (recommended), or W1-lite plus greenhouse. Ill-structured.
2. Resources affect build cost and placement only, not Index rates. Recommend yes. Well-structured.
3. New terrain applies to new worlds only. Recommend yes. Well-structured.
4. New ore and ground-ice blocks, not retextured vanilla. Recommend yes. Well-structured.
5. Remove diamonds, netherite, enchanting from the Mars dimension. Recommend yes. Ill-structured.

## Handoffs

> **To:** Lead Subject Matter Expert · **Problem type:** well-structured
> **Problem:** The world plan makes real-Mars claims I could not verify.
> **Context:** `docs/design/world-and-blocks.md` sections 2.2, 3.1, 4 (all marked **SME check**: perchlorate levels and washing, ground-ice depth and latitude, hematite, sulfate, carbonate abundance, meteorite iron, dunes, canyon, polar cap).
> **Needed from them:** source and verified/not-verified mark per claim; what to label "stylised" in the journal.
> **Done when:** each row has a mark before any journal or sign text uses it.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Check feasibility of the data-versus-code split and two unknowns.
> **Context:** section 2.3 and section 4 of the plan. How is the dome placed today? Do boot tests and the screenshot harness assume flat ground? Hydrosphere is pure simulation: what does the world need to show for it?
> **Needed from them:** feasibility and effort check (my numbers are unverified estimates); a flat dome pad approach; a definition of Hydrosphere in-world before W3.
> **Done when:** answers filed in `ARCHITECTURE.md` or a report; effort numbers replaced.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** New learning hooks need an alignment check.
> **Context:** survey scanner (A4, B5), perchlorate washing in the greenhouse (C1), carbonate as scrubber output (A5), ground collapse (B5).
> **Needed from them:** confirm goal IDs; wording for "no diamonds here" line.
> **Done when:** alignment table rows added.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** About 14 new block textures and 6 items; terrain palette.
> **Context:** section 3. Cut list in section 5 names what to reuse first.
> **Needed from them:** art cost per item; palette for dust, basalt, hematite, ground ice.
> **Done when:** costs recorded and the first slice list agreed.

> **To:** Producer · **Problem type:** well-structured
> **Problem:** Slot W1 against Stage 3 slice S3-1 (storm) and budget.
> **Context:** section 5 estimates (6 to 8 dev-days for W1). All unverified.
> **Needed from them:** ordering versus S3-1; budget line.
> **Done when:** BACKLOG item claimed or deferred.

## Risks
- Effort numbers are guesses; no measured rate (R-68).
- Terrain change can break boot tests, screenshot scripts and dome placement (unknown, handed to Technical Lead).
- Resource placement distances (near 64 blocks, far 150 to 400) are invented; they could make Phase 1 a hunt or too easy. Tune in first playtest.
- Real-Mars claims unverified (R-51: no standing external SME).
- Existing worlds keep flat chunks.
- Index independence from terrain could feel "fake" to Lens B players; revisit if playtests ask for richer ice meaning faster water.

## Shared model updates
- Proposed term and rule: **"blocks set cost and place, machines set rates."** Owning document would be `docs/design/world-and-blocks.md` (and DESIGN.md if accepted). Not a Decision Log edit; owner to accept.
- New doc exists: `docs/design/world-and-blocks.md` (world generation steps W1 to W3).
- Lesson: textures are global in a modpack, so "retextured vanilla" is risky for blocks that also exist elsewhere; prefer new blocks.
