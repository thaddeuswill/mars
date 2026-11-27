# Report: SME, machine references (2026-11-27)

## Handoffs received
Owner rule 5a (`docs/design/world-and-blocks.md` section 0.2): accepted. Each invented machine now names a real machine to copy.

## Done
- `docs/science/machine-references.md`: 16 machines (13 requested plus oxygen maker, solar array, battery), each with real machine, plain sentence, what to copy, what not to claim, source (VERIFIED with quote, or NOT VERIFIED), image note. Sources opened via `curl` from NTRS API and `science.nasa.gov` only; text read, never run.
- Ran: ~25 NTRS searches and 20 abstract fetches. Did not read full papers. Did not download images.

## Decisions needed
1. **Super-greenhouse emitter and mirror uplink have no real machine** (ill-structured, Game Designer and owner). Recommendation: keep them, label plainly fictional, generic chemical-plant look.
2. **Soil treatment unit:** real cousin only measures soil. Recommendation: keep, label "made up" (well-structured).

## Handoffs

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Machine models need real references per rule 5a.
> **Context:** `docs/science/machine-references.md` "Image note" column. Repo already holds MOXIE, InSight, Opportunity images.
> **Needed from them:** Collect (not commit unless the owner agrees) NASA references for Rodwell, CDRA, WRS, Veggie/APH, TEGA, SHARAD; update `docs/art/reference-audit.md`; model silhouettes from these.
> **Done when:** each machine in the table has a named reference image in the art audit.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** Add the "Real machine" line to each machine's journal/tooltip, and decide the two no-reference machines.
> **Needed from them:** Pick keep-fictional or replace; update the `world-and-blocks.md` block table.
> **Done when:** every machine row cites a real machine or is flagged fictional.

## Risks
- Quotes are from abstracts; details (pillows, battery) beyond them are NOT VERIFIED.
- `www.nasa.gov` ISS pages not fetched (outside allowed hosts), so ISS hardware images are unlocated.

## Shared model updates
None.
