# Reference Audit: batch 1 placeholders against real hardware

**Date:** 2026-11-26 · **Owner of the findings:** Lead Artist (changes) · **Done by:** coordinator, for the owner · **Status:** findings and recommendations; no assets changed yet.
**Method:** found NASA's Photojournal pages through a browser (Chromium), downloaded the originals from NASA's image server, **looked at each photo**, measured colours from the true-colour ones, and compared with our renders from the browser viewer. Boards: `docs/art/renders/reference-audit/` (`board_o2.png`, `board_solar.png`, `board_dust.png`, `board_mast.png`). Photos and credits: `docs/art/references/`.

## What could and couldn't be checked
| Asset | Real counterpart found? | Audited? |
|---|---|---|
| O2 generator | **Yes:** MOXIE (Perseverance), 2 photos | Yes |
| Solar panel (clean) | **Yes:** InSight arrays (true colour); Opportunity arrays (false colour, layout only) | Yes |
| Solar panel (dusty) | **Yes:** InSight after about four years of dust | Yes |
| Planetary Monitor | A real weather station exists (REMS/MEDA on the rovers) but our block is an in-game console, not a copy | No |
| Weather mast (not built) | **Yes:** REMS boom on Curiosity's mast | Reference saved for when it is built |
| Regolith Outgasser | **No photo found.** Text sources describe heated-regolith reactors (a cylindrical reactor with an internal auger and a feed hopper) but no image | **No** |
| Regolith Oven | No real counterpart found | No |
| Battery | **Not looked up** (no search made) | No |

## Findings

| # | Asset | Finding | Evidence | Recommended change |
|---|---|---|---|---|
| 1 | **O2 generator** | **Wrong identity.** Ours is two grey gas tanks on a base. The real oxygen maker is one **gold-plated square box** with a grid of pocketed panels on the large faces, a diagonal X rib pattern on the end face, **white flexible tubes** looping over it, and **silver fittings**. | `board_o2.png`; PIA24201, PIA24203 | Rebuild as a gold-toned box with a pocket grid on its faces, white tube loops (thin elements), and silver fittings. Optionally keep one small tank as a side element. Needs a gold ramp in the palette and a fresh contrast and colourblind check. Ask the Game Designer which process the Phase 1 generator uses (MOXIE makes oxygen from Mars's CO2). |
| 2 | **Solar panel, clean: cell colour** | Darkness is right, hue is off. Real cells are **near black with a faint purple-grey cast**: median `#322d38`, luminance 0.028. Ours are blue (`#24324a`, `#2f4462`; luminance 0.03 and 0.06) and the light-blue highlight (`#4a6488`, luminance 0.12) has no real match. The style guide's cell ramp says "real cells are dark blue-black" with no source; the photo says purple-grey. | Measured from PIA22876. Note: the photo carries a colour cast, so treat the hue as approximate. | Shift the cell ramp toward a neutral purple-grey near-black; drop or darken the light-blue highlight; re-run the contrast and colourblind checks (frame against cells must still pass). Correct the sourcing line in style guide §2.2. |
| 3 | **Solar panel, clean: pattern** | Ours is a woven **lattice** inside four boxes. Real panels are **rows of small rectangular cells** with thin light seams and small dark rectangular cut-outs. | PIA22876 (round fan of 12 segments); PIA18080 (Opportunity: flat panels of small cells; layout only, false colour) | Redraw the cell texture as rows of small rectangles with thin lighter seams, plus a few dark cut-out squares. Keep the square 2×2 shape: a round 12-segment fan doesn't read at 16 px. |
| 4 | **Solar panel, dusty** | **Wrong pattern.** Real dust is an **even veil**: the whole array is almost the colour of the ground (measured `#d88f62` on the array vs `#c07a50` ground), the seams stay as dark lines, and the cut-outs stay dark. Ours has patchy streaks and a drift along one edge with dark cells showing between them. That looks like a **partly cleaned** panel, not a coated one. Dust **colour** is close (ours `#b97c52` and `#d09a6e`; the NASA image is strongly orange-processed, so this is approximate). | `board_dust.png`; PIA25286 | Make "dusty" the even veil, and keep today's streaks as a third state, "partly cleaned" (real arrays were cleaned in streaks by wind: Opportunity, 2014). **Trade-off for the owner:** the Artist tried an even veil and dropped it because it read as "a tan lattice". The real photo looks exactly like that. Accurate vs. easy to read at a glance. |
| 5 | **Weather mast** (not built) | Reference only: a **cream-white painted cylinder** mast with a **horizontal boom** carrying a cylindrical sensor head, a brown strap, dust-tan staining. | PIA19164 | Use as the shape and colour guide when the mast (asset M-30) is modelled, alongside the Subject Matter Expert's planned instruments. |
| 6 | **Outgasser** | Unaudited: no photo. Text sources suggest a **feed hopper on top** and a **cylindrical reactor body**; ours is a box with a stack and hazard stripes. | Search summaries only (NASA technical reports; not opened) | Low confidence. Consider a hopper funnel on top and a rounder body. Ask for a real photo of a regolith heater if you know one. |

## Things that held up
- Dust colour is in the right range.
- The dusty panel's low-edge drift is plausible as a settled drift, though it isn't what the real veil looks like.
- Cell darkness (luminance) matches the clean InSight arrays.

## Limits of this audit
- **Colours are approximate.** Each NASA image has its own white balance and processing. The Opportunity image I downloaded is **false colour**, so I used it only for layout; no colour was taken from it.
- **Our renders are not Minecraft** (no world light or shadow). Shape and texture comparisons are fair; brightness comparisons less so.
- **Only seven photos were looked at.** The battery was not researched, and the Outgasser has no photo.
- Power figures from search summaries (InSight about 5,000 to 500 Wh/sol; Opportunity wind cleaning raising output) were **not opened** at source and are not used in any finding.

## Handoffs
- **Lead Artist (batch 2):** findings 1–4, pending the owner's choices below.
- **Subject Matter Expert:** confirm the photo-based colour readings and the claim in style guide §2.2 about cell colour.
- **Game Designer:** which process the Phase 1 oxygen generator uses; whether "partly cleaned" belongs in the dust mechanic.

## Decisions needed (owner)
1. Rebuild the O2 generator as a MOXIE-style gold box (recommended: yes).
2. Dusty panel: (A) even veil, accurate; (B) keep streaks, readable; (C) both, as "dusty" and "partly cleaned" (recommended).
3. Adjust the clean cell colour and pattern to match the photos (recommended: yes).
