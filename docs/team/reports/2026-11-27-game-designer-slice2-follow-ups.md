# Report: Game Designer, slice 2 follow-ups (docs only)

Role: Lead Game Designer · 2026-11-27 · Docs only; no Java or `pack/` edits. Nothing was run or playtested.

## 0. Handoffs received
- **Technical Lead: confirm "machines contribute while unloaded":** accepted, recorded in DESIGN.md.
- **Technical Lead: replace the stale arrival line:** questioned. A grep for "empty for now" in `mod/src` and `pack` finds nothing, so it appears already replaced (see `2026-11-27-technical-lead-arrival-copy.md`). Fallback text is in ui-specs §9.
- **Technical Lead: final Monitor wording:** accepted, ui-specs §9.

## 1. Done
- `DESIGN.md` (World State bullets, after "Physical thresholds never scale"): rule that machines keep contributing while their chunk is unloaded. Design reason, alternatives, trade-offs, classroom-mode note, revisit-if. (This edit was auto-committed as a WIP commit `c845536` by the harness; the final text is in the file.)
- `docs/design/ui-specs.md` new §9: key-by-key review of `monitor.*`, recommended wording, missing keys, one-line pause design.
- `docs/design/balance-sheet.md` (after "Why Dome stable unlocks the Outgasser"): power-order note.

## 2. Findings
**Wording (changes recommended):** "Pressure" to "Air pressure"; `no_planet` to "This Monitor only works on Mars. Travel there to read a planet."; `water.no_liquid` to "Too thin for liquid water" (fits the panel; read-more gives the 0.61 kPa triple-point sentence, SME to confirm); trend words stay and gain shapes. Other keys unchanged.

**Pause design (not built):** "Colony paused" HUD chip with two bars plus words; Monitor reads "Sol N (paused)" and opens read-only; planet-changing actions blocked with one log line ("The colony is paused. Your teacher will resume it."); movement, journal and Monitor stay available.

**Power vs. first-session flow:** no contradiction. Solar then battery then Outgasser matches DESIGN.md (battery in Landing step 6; Outgasser unlocks at Dome stable; Hana's note covers power for the night). Two flags: (1) the Outgasser is not in the first session (Landing 0.4 h, Dome stable about 3 h); (2) Outgasser power is extra on top of life support, so power is a hidden second pacing gate on the 8 h Phase 1 target. Panels per Outgasser, battery size and unpowered behaviour are unset (invented when set). Recommend output scaling with power satisfied (as slice 3 plans), with a visible Monitor cause line. Also: slice 1 ran the Outgasser unpowered and ungated, so slice 1 speeds are not the balance curve (already stated in stage2-plan R9).

## 3. Decisions needed
| Decision | Recommendation | Type |
|---|---|---|
| Pause blocking and HUD chip: build in slice 9 or earlier? | Slice 9 (classroom); standard play uses operator pause rarely | Well-structured |
| Unpowered Outgasser: scale with power or hard stop? | Scale (slice 3 plan); lets the player see partial power | Ill-structured (fun and learning; Technical Lead and ID input) |

## 4. Handoffs
> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** confirm "Below 0.61 kPa, ice turns straight into vapor." as grade 6-7 read-more text. **Context:** ui-specs §9. **Done when:** confirmed or reworded.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** apply the recommended `monitor.*` wording and add the three proposed keys when convenient; provide a Monitor power line in slice 3 so power shortfall is visible. **Context:** ui-specs §9, balance-sheet power note. **Done when:** keys updated and a power cause line appears.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** no goal IDs changed; FYI for alignment: B1 cause lines now rely on unattended machines showing failures on the Monitor.

## 5. Risks
- Hidden power pacing gate could break the 8 h Phase 1 target (balance-sheet note); measure in slice 3.
- Far-away machines fail silently; the Monitor must surface their upkeep (DESIGN.md revisit-if).
- The WIP auto-commit means DESIGN.md is already in history; my other two files are staged, not committed.

## 6. Shared model updates
- Decision (owner doc DESIGN.md, World State): machines contribute while their chunk is unloaded; classroom pause is the session clock, not unloading.
- Lesson: the harness may auto-commit a partial edit ("WIP"); check `git log` before assuming an edit is uncommitted.
