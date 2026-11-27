# Lead Artist report: placeholder Mars sun texture (2026-11-27)

## 0. Handoffs received
Sky review handoff (2): "ask me for a 32x32 sun texture". Owner said yes. Accepted and done.

## 1. Done
- `pack/assets/mars/textures/environment/sun.png`: 32x32 RGBA, **placeholder**. Disc centred at (15.5, 15.5): opaque core radius 7.5 px, colour `#F4EBDD` (the keyframe disc colour); soft edge 3.5 px wide fading to alpha 0, in 8 alpha steps (pixel-art, no smooth gradient), edge colour shifting slightly cool to `#E4EAF2`. No yellow rim; no pixel is yellower than the core. 1 px `#FF00FF` marker at (13, 8), the top-left-most opaque disc pixel (style guide 5.5). Everything else fully transparent.
- Generator: scratchpad script only (not committed); parameters are the ones above.

## 2. Verified vs not
Verified: file is 32x32 RGBA; centre pixel is (244,235,221,255); corners transparent; I viewed an 8x upscale composited on the 0.6 kPa zenith colour `#A87B5A` (round disc, soft edge, marker top-left, no yellow rim).
Not verified: how it looks in game (not run; draw code is additive, see 3), and the real client sun size.

## 3. Can the code use it already? No.
`mod/src/main/java/mars/client/sky/MarsSkyEffects.java` line 39-40 draws the **vanilla** texture:
`SUN = ResourceLocation.withDefaultNamespace("textures/environment/sun.png")`; it is bound at line 158 (`RenderSystem.setShaderTexture(0, SUN)`). The Moon (line 41-42) stays vanilla. I did not edit Java.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** The sky draws the vanilla sun texture (yellow rim `#FFFFAA`), not ours.
> **Context:** `MarsSkyEffects.java` lines 39-40. Our texture is at `pack/assets/mars/textures/environment/sun.png`.
> **Needed from them:** (1) change lines 39-40 to `ResourceLocation.fromNamespaceAndPath("mars", "textures/environment/sun.png")` (1.21.1 API; use whatever factory the file already uses for mod ids). (2) Add a unit/boot check asserting that resource exists (e.g. in the pack-content test, `pack/assets/mars/textures/environment/sun.png` is present and 32x32). (3) Check the blend: line 145-149 uses SRC_ALPHA, ONE (additive), so the soft alpha edge fades correctly, but the magenta marker will add to the sky and look pale pink-white; that is fine for a placeholder. (4) Screenshot at 0.6 and 75 kPa and send me the images.
> **Done when:** the in-game sun has no yellow rim and the check passes.

## 4. Decisions needed
| # | Question | Recommendation | Type |
|---|---|---|---|
| 1 | The visible disc is about 22 of 32 px (vanilla's is a smaller part of its quad, I did not measure), so at `sun_scale` 0.66 the sun may look larger than intended (about 11 degrees was the quad, not the disc). Retune `sun_scale` after the first screenshot. | Wait for the screenshot, then adjust; I will propose the value. | well-structured |

## 5. Risks
Low. Placeholder only; colour is a family match, not sampled from a real image (the sun in real Mars images is a small, pale-blue-white disc; **unverified** here).

## 6. Shared model updates
None. Update `docs/art/asset-list.md` once the code switch lands (no sun row exists yet; I did not add one).
