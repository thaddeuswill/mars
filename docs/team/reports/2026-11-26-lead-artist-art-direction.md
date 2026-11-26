# Report: First Art Direction

- **Role:** Lead Artist
- **Date:** 2026-11-26
- **Outputs:** `docs/art/style-guide.md`, `docs/art/planetary-keyframes.md`, `docs/art/asset-list.md` (144 rows), `docs/art/palettes/palette-sheet-v1-placeholder.png`
- **Note:** NASA and reference sites were blocked. Mars colors are estimates; citations aren't opened yet and are marked [SME].

## Handoffs received (all accepted)
- **M-17:** each of the six planet values has a main visible cue. Pressure drives sky, fog, and sunset. Temperature drives frost. Hydrosphere drives water and clouds. O2 drives flame. CO2 shows only through its effects. Biomass drives ground conversion.
- **Story bible:** thawlings read as animals, BELL's log and messages, crew props, helmet-off (with audio direction), beacon ending.
- **Game Designer:** all 10 new cues covered. 75 kPa is the top keyframe.
- **Technical Lead:** every keyframe names its driving input.

## Done (highlights)
- Direction: "field-station realism, read at a glance"; moderate visual and high functional fidelity.
- Accessibility:
  - Colorblind checks with minimum perceptual differences.
  - Okabe–Ito UI palette.
  - Every screen fits 420×236 GUI px.
  - No more than 3 flashes per second.
- Numeric checks caught two problems before anything was built:
  - The early sky step was invisible, so Phase 2 now uses a stylized rose-tan sky.
  - Moss on regolith couldn't be told apart under deuteranopia; it's now retuned.
- **Air window:** the Monitor's O2 display, zoomed to 15–27% with three checks, so the narrow breathable range is readable.

## Decisions needed
| Decision | Recommendation | Type |
|---|---|---|
| Rose/mauve Phase 2 sky (stylized for visibility) | Keep it, labeled stylized, unless the SME objects | Well-structured |
| Weather mast prop, so O2 and CO2 have an honest world cue | Adopt | Well-structured |

## Handoffs
- **SME:**
  - Verify every [SME] tag: sky path, flame bands, outgasser vapor, and the other items marked.
  - Pixel-sample calibrated rover images for the start colors.
- **Technical Lead:**
  - Map input names to data keys; expose storm strength.
  - Cost of clouds and heat shimmer.
  - Set up `pack/assets/`.
  - Animation approach.
  - Screen-size assumption.
- **Game Designer:**
  - Approve the weather mast, quiet-zone marker, Air window, and thawling defense behavior.
  - Send the lists of crops, biomes, parts, and beacon components.
- **Instructional Designer:** alignment and readability check of the Air window (A1, A5, B4, C3).
- **Producer:** no one owns audio. Assign an owner or scope audio down.

## Risks
- Real Mars colors unverified.
- Red-green colorblind players may miss biomass greening (mitigated with lightness and texture changes).
- Create and Mekanism machines keep their own look, since Create's art is All Rights Reserved.
- Clouds and shimmer may be costly on school hardware.
- Large art volume.
