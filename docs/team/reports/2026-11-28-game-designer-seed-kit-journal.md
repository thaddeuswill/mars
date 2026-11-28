# Game Designer report: seed kit recipes, journal lines, machine-priority items (2026-11-28)

Docs only. Nothing under `mod/` or `pack/` changed. Method: design walkthrough (no real players), and a rough Flesch-Kincaid check on every new text (all about grade 1 to 6 by a quick script; a screen, not proof, CR-T6).

## 0. Handoffs received
| From | Handoff | Answer |
|---|---|---|
| Technical Lead (conductor ores report) | Confirm starter chest amounts and one carbonate per kit; write recipe against `#mars:seed_kit_carbonate` | Accepted. One carbonate per kit confirmed. Copper 4 confirmed; **redstone 8 -> 10 proposed** (seed-kit-recipes.md section 3) |
| Technical Lead | Review three ore tooltips, write compost label | Tooltips kept as the SME wrote them (checked for grade and positive wording); compost label written |
| SME | Pick option, rarity, first-kit count, update recipe table and audit table | Option kept (two invented ores); kit count set; recipe table rows updated; audit table rows for `sulfide_vein`, `magnetic_ore`, `carbonate_rock` are below (DESIGN.md not edited) |

## 1. Done
- `docs/design/seed-kit-recipes.md` (new): four kit recipes with sources, Index row and gate served, placement chain; loop check against compost (no deadlock if the first planter needs no bone meal); starter chest count by machine; R8 resolution; kit tooltips.
- `docs/design/messages/phase-1.md` section 12: nine rows appended (band note, two wordings, reclaimer tooltip, compost label, invented-ore journal note, three SME ore labels). All declare `reveals: none`.
- `docs/design/world-and-blocks.md` 3.6: three rows edited (planter bed bone meal now optional; grow lamp uses 1 redstone, no glowstone; kit row points to the new file).
- Findings: (a) the soil unit and kits both need bone meal, so the first planter must not; (b) **no seed source on Mars**: wheat seeds only come from wheat, so the chest needs a seed packet; (c) the redstone lamp and vanilla compass hide redstone and glowstone costs; (d) kit tooltips must not name gases (F8 fires when the first kit is placed).

## 2. Decisions needed (owner)
| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Chest: 4 copper and **10** redstone (was 8), plus a seed packet (6 wheat seeds, 2 sugar cane, 2 sweet berries, 2 potatoes) | Well-structured | Yes. 10 covers the gate path (7) and greenhouse lamps (3); without seeds the greenhouse cannot start |
| 2 | R8: band on all machines with wording A or B, retrofit the oxygen maker art (or give it a third wording and no band) | Well-structured (D2 said "all") | Band on A and B; retrofit oxygen maker |
| 3 | Kit count: 24 standard, 12 classroom (INVENTED) | Ill-structured (grind vs. weight of the Phase 3 loop) | Start there; tune in the first Phase 3 run |

## 3. Proposals for DESIGN.md and the Decision Log (not edited)
**Scrubber unlock (R6).** Gate table, row "Phase 3 begins": Unlocks becomes "Biome seed kits, soil treatment, **carbon scrubber**, storms weaken". Also the line 750 Phase 3 machine list is already correct. Rationale: Breathable's CO2 check needs the scrubber, and rule A makes "scrub or keep O2 source" a decision at the moment kits unlock. Alternatives: unlock at Hydrosphere (too early; rule A has no meaning before biomass); unlock at Breathable (too late; the last gate needs it). Trade-offs: learning (B4 meets the trade-off together with seeding) up; appeal: one more thing at the gate moment; cost: none. Revisit if the Phase 3 solver run finds scrubbing never used. Goals: A5, B4 (alignment check by the Lead Instructional Designer).

**Audit table additions (DESIGN.md Fantasy and Authenticity audit).**
| Element | Label | Real basis |
|---|---|---|
| `sulfide_vein` (raw copper) | INVENTED | Iron sulfides exist on Mars; copper amount NOT VERIFIED |
| `magnetic_ore` (redstone) | INVENTED | Magnetized southern crust (VERIFIED); pyrrhotite a 1989 hypothesis |
| `carbonate_rock` | REAL, SIMPLIFIED | Mg-rich carbonate in Jezero and Nili Fossae (VERIFIED abstracts); amount NOT VERIFIED |
| Starter chest and seed packet | INVENTED ("from the lander") | n/a |
| Four seed kits | INVENTED | Crop stand-ins; succession order accurate (SME round 3) |

## 4. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Starter chest: redstone 8 -> 10 and add a seed packet; confirm `mars:washed_regolith` exists; kit recipes (shapeless, tag `#mars:seed_kit_carbonate`) once the owner agrees.
> **Context:** `docs/design/seed-kit-recipes.md` sections 1 to 3; `pack/data/mars/terrain/starter_chest.json`.
> **Needed from them:** JSON change, recipe locked until Phase 3 begins, and a scan that no kit text names O2 or CO2 (CR-F9, F8).
> **Done when:** a new world's chest holds 4 copper, 10 redstone and the seed packet; kit recipes use the tag.

> **To:** Lead Artist · **Problem type:** well-structured (R8 flag is ill-structured for the owner)
> **Problem:** Add **transit tubes** (segment, junction, outpost airlock; unlock at Phase 2 begins) and the **algae vat** (unlocks at Hydrosphere) to the machine list. Proposed order: band, reclaimer, processor, emitter, **tubes** (plain blocks, low cost: reuse hull panel and glass look), extractor, scanner, planter, kit items, soil unit, scrubber, **algae vat**, beacon, uplink. Also decide the oxygen maker band (seed-kit-recipes.md section 4).
> **Context:** `docs/design/machine-priorities.md` section 2; `docs/art/made-up-band-spec.md`.
> **Needed from them:** accept order; SME names a real reference for each (vat: algae photobioreactor is the likely idea, **unverified**) in `machine-references.md`.
> **Done when:** both rows are in `invented-machines-look.md`.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Review the nine new journal and tooltip rows (phase-1.md section 12), the scrubber unlock proposal, and the kit tooltips' F8 guard.
> **Done when:** wording agreed or edited.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** Confirm "space stations recycle their water" (reclaimer tooltip) against NTRS 20170008948; vanilla composter chances and wheat yield are from memory (unverified).
> **Done when:** tooltip line marked verified, or reworded.

## 5. Risks
- Redstone and copper density is unmeasured; a short chest plus rare ore could stall Phase 1. Mitigation: raise the chest first.
- No seed source is a hard blocker for the greenhouse and kits until the seed packet exists.
- Bone meal competes with crop growth; a player can starve their own kits.
- Kit counts, bone meal and plant yields are INVENTED or from memory.
- Oxygen maker art may need a band retrofit (R8).

## 6. Shared model updates
- Decision proposed (owner): chest 4 copper and 10 redstone plus seed packet; band on wording A and B.
- Lesson: a recipe chain through a vanilla item (bone meal) needs a no-input first step, and every vanilla item must be checked for where it comes from on a planet with no grass, animals or glowstone.
- Lesson: item tooltips are visible before a forecast fires; they need a `reveals` check like messages (CR-F9).
- New term for the audit table: "seed packet".
