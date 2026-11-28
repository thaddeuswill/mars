# SME report: field tools research (2026-11-28)

## Handoffs received
Owner request (BACKLOG, `world-and-blocks.md` 0.2 item 5c): "meaning we wont need pickaxes; we need drills and stuff to get samples; we should do research on all the hand tools we need". Accepted.

## Done
- `docs/science/field-tools.md`: tool tables (Apollo/Artemis, ISS/EVA, Mars landers and rovers, terrestrial kit, ISRU, ice, scanners, containers), failure and wear ideas, link to the block list, and a 12-tool recommended list.
- Sources: 46 NTRS abstracts, the Perseverance rover-components page on science.nasa.gov, and the InSight HP3 paper via NCBI PMC (all opened and quoted: VERIFIED). Phoenix arm length and rasp came only from search summaries: NOT VERIFIED.
- Not verified (marked in the file): Apollo tongs, gnomon, LRV sampler details, rock chisel, Brunton compass, trowel, soil auger, ice auger, hand brush, cable-and-drill ideas, Mohs values.
- Not run: no game or code checked. No files under `mod/` or `pack/` touched.

## Decisions needed (owner)
1. Do you want the 12-tool list as the target, or the shorter 9 (cut rake, deep drill, ground scanner)? Recommend 12, cut the three if scope is tight. Ill-structured (scope vs. learning).
2. Should powered drills run on the existing power grid (battery tool) or on a held-battery item? Recommend held-battery for tools, grid for machines. Ill-structured (Game Designer and Tech Lead).

## Handoffs
> **To:** Game Designer · **Problem type:** ill-structured
> **Problem:** Map the 7 categories (`dig-loose`, `break-rock`, `core-sample`, `ice`, `scan`, `brush`, `carry-and-store`) to Minecraft tool functions and tiers.
> **Context:** `docs/science/field-tools.md` sections 2, 4, 5. Real jam and wear events in section 3. Duricrust is a good first "scoop fails" gate.
> **Your view:** Hand tools are Apollo/Artemis-like; Mars rover drills are robotic. Keep that honest in the journal.
> **Needed from them:** a tier table, and which wear events to use.
> **Done when:** the tool list and tiers are in `DESIGN.md` or `world-and-blocks.md`.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Reference images for the 12 tools.
> **Context:** Real references per tool in `field-tools.md` section 5 (Perseverance corer and bit carousel, Apollo drive tubes and scoop, MAHLI, handheld XRF, sample tubes). Do not commit NASA images; link only.
> **Needed from them:** an item sprite sheet plan.
> **Done when:** each tool has a reference note in `docs/art/references/README.md`.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Sampling is the unit of discovery; check goal IDs for "sample, label, report" and for the honest notes (section 6).

## Risks
- Rover tools are robotic and far from hand-sized; a game that copies them as hand tools needs the honest note.
- Several classic Apollo tool names are unverified; do not print them in the journal until checked.

## Shared model updates
- Proposed vocabulary: the seven tool categories above.
- Lesson: NTRS API search works for tool history; short queries work, long ones return nothing.
