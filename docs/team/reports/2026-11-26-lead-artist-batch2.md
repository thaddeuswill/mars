# Report: Lead Artist, batch 2 (BACKLOG #21, #22)

- **Role:** Lead Artist
- **Date:** 2026-11-26
- **Outputs:** `pack/assets/mars/` (Oxygen maker `o2_generator`, solar panel `solar_panel_dust0`–`dust4` and `solar_panel_partly_cleaned`; batch 1's `solar_panel` and `solar_panel_dusty` retired), `tools/art-preview/` (generator, viewer with a dust-level slider), `docs/art/renders/phase1-batch2/` (`dust_progression.png`, `contact_sheet.png`), `docs/art/asset-list.md`, `docs/art/style-guide.md` §2.2 and new §2.4 (weathering), `docs/art/references/README.md` (PIA17759 and PIA18079 added)
- **Not checked:** how any of it looks inside Minecraft.

## What changed
1. **Oxygen maker** (id stays `o2_generator`; display name "Oxygen maker (CO2)"): a 12×12×10 gold-plated box. Front and back show a 3×3 grid of recessed panels with the status lamp in a dark ring framed by a light rib; the east end has the X rib pattern; two off-white tube loops run from fittings on top, over the edge and down each side to low silver fittings. New gold ramp `#5C4320`, `#7E5E2C`, `#A3823F`, `#C9AE6A` (median reading of the gold face in PIA24201 about `#7B5A2E`).
2. **Clean solar cells:** cell ramp now the measured `#322D38` (near-black, faint purple-grey); the light-blue highlight is gone; each sub-panel shows rows of 2 px cells with thin lighter seams and five dark cut-outs, in the square 2×2 layout. Style guide §2.2 corrected and cites PIA22876.
3. **Dust stages 0–4 plus "partly cleaned":** all six come from one fixed dust map, so each stage adds dust to the previous one; from stage 3 the seams turn into dark lines. "Partly cleaned" is patchy and different on each of the four sub-panels (after PIA17759/PIA18079; one sub-panel is left completely untouched, which is more extreme than the real photo and easy to soften).
4. **Viewer:** a "Dust level" slider (0–4) and a "Partly cleaned" button appear only when the solar panel is selected; the overview uses stage 0. **Bug found and fixed:** the viewer showed every texture washed out (textures weren't flagged sRGB in three.js); the viewer screenshots sent to the owner before this report were too light. Tested in headless Chrome with a local copy of three.js (the CDN is blocked in the sandbox).
5. **Docs:** style guide §2.2 (cell, dust-veil, and gold ramps) and new §2.4 (weathering, plus a per-panel stage bar spec: five slots, filled count = stage); asset list rows M-05, M-09, M-09a, M-35 (panel brush), M-36 (panel wiper), M-37 (Water electrolysis plant), U-37; storm storyboard (panels keep their stage during a storm and gain storm stages at clearing).

## Checks
All pass after fixes. The checks caught and the Artist fixed: the lamp ring didn't stand out on gold (1.7:1; now inside a light rib); "partly cleaned" had 7 colours on one face (limit 6); the gold box against the regolith was too close for colourblind players (0.021 at worst; the light gold edge rib now gives 0.22 or more); three of four dust steps were first too small to notice.
- Gold against the hazard accent: OKLab ΔE 0.120 or more in normal vision and all three colourblind simulations; light gold against the UI yellow: 0.142 or more.
- Dust steps on the cell area (normal / protan / deutan / tritan): 0→1 0.039 / 0.035 / 0.040 / 0.040; 1→2 0.149 / 0.134 / 0.152 / 0.151; 2→3 0.142 / 0.127 / 0.145 / 0.144; 3→4 0.050 / 0.050 / 0.050 / 0.049. **Caveat:** over the whole panel face, the steps are only 0.011–0.044 (cells are about 45% of the face), so 0→1 and 3→4 are subtle in iso views; the top view shows them clearly.
- Panel structure contrast (seams, cut-outs, frame against cells) stays at 2.4:1 or more at every stage.

## Coordinator's look at the result (screenshots from the viewer)
- **Oxygen maker:** reads as a gold box with recessed panels, white tube loops and silver fittings; a fair match to MOXIE. It reads a little like a waffle at a glance.
- **Clean panel (stage 0):** colour is a good match to InSight 2018.
- **Even veil (stage 4): does not match.** Real seams under dust are faint thin lines; ours are bold near-black lines (2 px on a 16 px texture, kept dark to satisfy the 2.4:1 structure rule), so it reads as a tan lattice with black seams, not an even veil. **Recommended fix:** at stages 3–4, make seams 1 px and a dark tan-brown (not near-black), keep the dark cut-outs, and relax the seam contrast rule for those two stages.

## What the Artist still can't judge
Whether the stage-1 specks survive at play distance (no Minecraft view); NASA colours are approximate; in PIA22876 the clean-array cut-outs look lighter, not dark (the task said dark; worth a second look); the gold box may read as a "waffle" or "chocolate bar".

## Handoffs
- **Game Designer:** confirm 30 / 50 / 80% cell coverage per stage against the output steps; whether "partly cleaned" should ever leave a sub-panel fully untouched; the stage-bar wording ("Dust 3 of 4 · heavy veil").
- **Technical Lead:** batch 1's `solar_panel` and `solar_panel_dusty` are retired (their assets remain in `mod/build` until a rebuild); the block needs a dust-stage property mapping to the six models.
- **Subject Matter Expert:** check the gold reading from PIA24201, the observation about lighter cut-outs in PIA22876, and the patchy cleaning against PIA18079.

## Shared model updates
- **Terms:** panel dust stages `dust0`–`dust4` plus "partly cleaned"; display name "Oxygen maker (CO2)" with id `o2_generator` until the rename.
- **Lesson:** a browser screenshot caught the viewer's washed-out colours, which the Blender renders could never show. Check every preview path with its own screenshot.
