# Technical Lead: Monitor UI fixes from the UI skills review (BACKLOG #36), 2026-11-27

## 0. Handoffs received
`docs/team/reports/2026-11-27-ui-skills-monitor-review.md`, findings 1 to 8 and 6. Accepted. Finding 9 (live update, pause) not touched. Finding 5 (rename "Pressure" to "Air pressure", "Too thin for liquid water") not done: wording owner is the Instructional Designer, keys unchanged.

## 1. Done
- Reason on the stalled machine line (finding 1): Pressure cause line shows "needs power" (red) or "low power" (amber) instead of the rate; the dusty panel shows "Solar Panel x1 / dusty, makes less". `MonitorView.CauseLine.reasonKey`, `PlanetSim.machineDrawsPower`, `PlanetSim.reducedCount`.
- Size (finding 2): panel top-left (6 px margin), 250 wide, one row per line (label, value, trend). Power is one summary line "Making +5.0, using -4.0 units/sol" plus a 36 px battery bar and "5.0 / 10.0". Next goal and Goals reached are one goal line ("Goals 1 / 2" on its detail line; "Goals reached 2 / 2" when done). Measured from the PNGs: worst case (short on power plus dusty panel) about 131 GUI px = 55% of 240; powered about 118; both goals reached about 97. Was 173 (72%). Measured by eye from pixel rows /3, plus or minus 3 px.
- Hierarchy and state colours (findings 3, 4): only Pressure, the goal row and a Power problem row use value scale 1.25; all else 1.0. Power: Enough green, Short amber, None red, always with the words. Trend shapes drawn with fills (up triangle, flat bar, down triangle), no font glyph needed. "Falling" has no screenshot.
- "Esc to close" in the header (finding 6; header instead of a footer to save 11 px). The "Nothing is changing yet" empty-state line is not built.
- Style class `mod/src/main/java/mars/client/MonitorStyle.java` (finding 8). EDGE colour contrast fix not done (decoration only).
- Row/CauseLine records gained fields; the payload codec carries them (boot check `monitor.payload_round_trip` equal=true). Lang: new keys marked straw-man in `docs/design/ui-specs.md` section 9.
- Tests: 3 new in `MonitorViewTest` (reasons, short plus dusty plus battery bar, emphasis), `GateTest` goal assertions updated. Real output: `spotlessCheck build test --rerun --offline` BUILD SUCCESSFUL, 96 tests 0 failures (was 93); `runGameTestServer` `RESULT PASS passed=52 failed=0`.
- Screenshots (real client, 1280x720, GUI scale 3): `tools/screenshots/out/20261127-114741/` (slice3: unpowered, powered, short and dusty) and `20261127-114819/` (slice4: holding, reached, both goals). I looked at powered, short, both-goals images after the last change; I did not re-open unpowered and holding after the final layout edit (they were checked in the first pass only).

## 2. Decisions needed
1. Centre of the screen (ill-structured). The panel is 250 GUI px wide on a 426 px screen, so its right edge is past the horizontal centre and, in the tall states, its bottom (131 px) is below the crosshair height (120). The hotbar is clear. Clearing the centre needs a width of about 205 px, which means moving the trend onto the cause line or dropping the unit. Recommendation: accept as is for the prototype, since the Monitor is a screen you open on purpose and close with Esc; revisit after the owner looks.

## 3. Handoffs
> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** wording check of the new straw-man keys and the dropped trend word.
> **Context:** `docs/design/ui-specs.md` section 9, `pack/assets/mars/lang/en_us.json`.
> **Needed from them:** reading-level check of "needs power", "low power", "dusty, makes less", "Short on power", and "Air pressure" rename.
> **Done when:** keys confirmed or replaced.

## 4. Risks
- Not verified: other GUI scales (2, 4), other languages (long names), keyboard or gamepad, live update while open, the Monitor while paused, the Falling state, real players. Dusty plus all rows could pass 132 px if a second cause line appears.

## 5. Shared model updates
None.
