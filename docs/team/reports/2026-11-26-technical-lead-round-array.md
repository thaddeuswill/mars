# Technical Lead report: round solar array footprint and wiring (BACKLOG #28), 2026-11-26

Summary (full text was delivered by the agent hand-back; key facts kept here).

- Round array = 3 x 3 footprint: one centre block draws the model, eight invisible canopy cells (`SolarPanelCanopyBlock`) reserve the overhang. Blocked placement shows an action-bar message. Breaking any part breaks the whole array, drops one panel.
- Approach: vanilla JSON boxes (Artist's pixel disc). `neoforge:obj` and composite not tried in game. ARCHITECTURE.md section 2.18.
- Wired to batch 3 models (dust0..4, partly cleaned); 40 variants checked.
- Loot tables added for all six machines (before, broken machines dropped nothing).
- Bug found by screenshots: creative players fell before the shot; `view` now enables flying.
- Results: clean build; 46 unit tests pass; boot test 19/19 (8 footprint checks); six client screenshot runs rc=0. Final shots: tools/screenshots/out/20261126-235531/ and 20261126-235259/.
- Seen: partly cleaned clearly differs from stage 4 from above; arrays read as low tables at real scale; no light leaks under the overhang; ground under overhang is not shadowed; outline shows per-cell boxes.
- Not done / untested: inventory-only marker overlay; frustum culling with centre off-screen at a chunk border; jar from mods folder; dedicated server (EULA); Windows/macOS.
- Open handoffs: Artist + Game Designer choose (a) raise array toward ~1 m deck height (needs SME deck height), (b) dim ground under canopy (~1 day, unverified), (c) accept. Technical Lead leans (a)+(c). Artist: keep array bounds within -9..25 px. Producer: optional items (whole-array outline ~1 d, ground shadow ~1 d, chunk-border culling test ~0.5 d; all unverified).
- Lessons: check camera position before concluding from a screenshot; when another role regenerates assets in parallel, test against a scratch copy then rerun on the real files.
