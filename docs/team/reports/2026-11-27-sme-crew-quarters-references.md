# SME report: crew quarters and stowage references (2026-11-28)

## 0. Handoffs received
Artist report (habitat furniture), "To collect for a real bunk reference": accepted. All five items were collected; the CHAPEA photo does not show a bunk close up but does show the beds.

## 1. Done
- 10 real images added to `docs/art/references/habitat/` (CQ interior, tethered sleeping bag, Zvezda sleep station, CHAPEA rooms, Skylab bunk, Unity stowage, cargo transfer bag, MPLM racks, two suitport photos). Each was opened and looked at. README lists URL, credit and licence (all NASA, Commons tag PD-USGov-NASA).
- Rejected after looking: ISS "crew asleep" (too dark), CHAPEA plant and party-hat photos, a Mars Explorer rendering (not buried).
- `docs/art/habitat-reference-audit.md` section 9 appended: bunk cues B1-B9, stowage cues S1-S7 with [V]/[NV], comparison with boards 14 and 15, suitport and buried-habitat gaps.
- Method: Commons file pages read with `action=raw`; thumbnails from `upload.wikimedia.org` computed by md5 (960 px; 1000 px is not an allowed size, and 429 needed retries); NASA page `nap-time-iss` for the Nespoli photo. Not run: NTRS (403 in the proxy, ENOTFOUND in WebFetch).

## 2. Findings that matter
- **Bunk bag is blue in the Artist's board; no real photo shows a blue bag** (green, olive, tan). Straps: white or navy, not orange.
- **Stowage crate:** label tags are yellow or orange; the skid has no ISS source; add a soft strapped-bag variant.
- The grey stacks in the Unity photo are food canisters (NASA caption), not CTBs; I corrected my first reading.
- No buried habitat photo exists in what I could reach. CTB size and CQ size are [NV] (secondary web-search snippet only).
- I judged the boards from very wide, reduced images; fine pixel detail was not checked.

## 3. Decisions needed
1. Allow `ntrs.nasa.gov` for the proxy so dimensions can be verified (well-structured). Recommendation: yes, or accept the by-eye sizes.

## 4. Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Align bunk and crate with the references.
> **Context:** `docs/art/habitat-reference-audit.md` section 9.3.
> **Needed from them:** bunk bag to muted green or olive, white or navy strap; yellow or orange label card; drop or shrink skid; optional soft-bag variant; optional wall panel with Velcro squares.
> **Done when:** new boards 14 and 15 show the changes and the SME re-checks them.

## 5. Risks
- Hex colours and sizes are by eye ([NV]). The bunk's drawers have no source. Light at game scale is still unchecked.

## 6. Shared model updates
None.
