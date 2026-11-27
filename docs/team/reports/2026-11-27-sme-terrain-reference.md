# SME report: Mars terrain reference (2026-11-27)

## Handoffs received
Task from the owner via the coordinating session: write a terrain reference so generated terrain looks like real Mars. Accepted.

## Done
- `docs/science/mars-terrain-reference.md`: scale rules, 15-row landform table (what to generate, INVENTED parameters, source with quote, Artist search term), rock-cover table derived from the Golombek rock-size formula, colour palette, "what generic hills get wrong", notes for the Technical Lead.
- Method: documents only; sources fetched by curl from science.nasa.gov, nssdc.gsfc.nasa.gov and ntrs.nasa.gov (HTML stripped with `python3 -I`, never run). Counts: 9 pages opened with a usable quote (Phoenix, Pathfinder, Mars Facts, Valles Marineris, Curiosity dune report, ripple photo, linear dune, polar layers, Jezero boulders, Perseverance highlights) plus 3 NTRS abstracts (Golombek rock formula, Barlow d/D, Pike transition 3 to 8 km).
- Not reachable: the PMC dune paper (curl failed) and two guessed NASA URLs (404). Not opened: any page with dune heights, crater rim heights, mesa or ridge sizes, polar deposit thickness, Phoenix or Pathfinder rock cover percentages. Those are marked NOT VERIFIED in the reference.

## Decisions needed
1. **Relief budget (ill-structured):** the design doc says hills 10 to 40 blocks; real Mars plains are flatter. I recommend base relief 3 to 6 blocks, with drama from craters, dunes, mesas and one canyon. Cost: less climbing fun; mitigate with mesas and crater walls. Needs Game Designer and owner.
2. **Compression of large landforms (ill-structured):** I propose about 25 times for large craters and about 100 times for the canyon, labelled "stylised". Owner to accept.
3. **Wind direction per planet (well-structured):** one fixed dune direction in planet data. Recommend yes.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** tune noise and features from the reference.
> **Needed from them:** biome list and crater feature with the parameters in section 1 and 5; keep numbers in datapack JSON.
> **Done when:** a screenshot of plain, crater, dune basin shows no Earth-like rolling hills.

> **To:** Lead Artist · **Problem type:** well-structured
> **Needed from them:** use the section 3 palette and the search terms (view only, no downloads) for textures.
> **Done when:** dust, dark sand, basalt, layered rock and polar ice textures exist.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Needed:** journal wording for "Shrunk to fit the game" notes; D2 links.

## Risks
- Many numbers are INVENTED or NOT VERIFIED; if shown as fact they teach false precision.
- Real rock density is low at block scale; terrain may look empty without pebbles and craters.
- Compression may let students think canyons are small.

## Shared model updates
- Lesson: small features at 1:1, large features compressed and labelled "stylised".
- Rule: a 5 to 40 percent rock cover range is verified (Golombek); everything else in the terrain table carries its own VERIFIED mark.
