# Art Style Guide — Mars Terraforming

**Owner:** Lead Artist · **Status:** Draft 1 (2026-11-26), for team review · **Stability:** art, palettes, and sky/fog values are **fluid** (TEAM.md 8.1); the readability and accessibility rules in section 4 are meant to be **stable** once the team agrees to them.

**Depends on:** [DESIGN.md](../../DESIGN.md), [story bible](../design/story-bible.md), [balance sheet](../design/balance-sheet.md), [SME accuracy review](../science/accuracy-review.md), [TEAM.md](../../TEAM.md).
**Companion files:** [planetary-keyframes.md](planetary-keyframes.md) (how the world looks at every Index stage) · [asset-list.md](asset-list.md) (every asset and its status) · [palettes/palette-sheet-v1-placeholder.png](palettes/palette-sheet-v1-placeholder.png) (reference swatches, **placeholder**).

---

## 1. Direction in one paragraph

**Field-station realism, read at a glance.** The game looks like a working science outpost on a real planet, drawn with Minecraft's own grammar: 16×16 pixel blocks, flat light, clear shapes. Mars colors come from real imagery. Human technology is cool grey and safety orange, built from rectangles and bolts. Living things are rounded. Every change the player causes shows up in the world first (sky, fog, frost, water, ground, sound) and on the Planetary Monitor second. The planet starts quiet, dusty, and still, and ends loud, wet, and green. The mood moves from **alone and tense** to **careful wonder** to **earned relief** (story bible §2).

### 1.1 Where we stand on fidelity (TEAM.md §3)

| Type | Our target | What it means for art |
|---|---|---|
| **Visual fidelity** | **Moderate** | Minecraft blocks and pixel art, never photoreal. Colors, landforms, sun size, frost, and ice behavior are taken from real Mars data. We stylize shape and detail, not physics. |
| **Functional fidelity** | **High** | Every visual cue moves in the right **direction** and at roughly the right **place** in the Index. Example: the sky only turns blue when pressure is high and dust is low, because that is what makes a sky blue. We never paint an invisible gas a color (section 3.4). |
| **Temporal fidelity** | **Low** | Visual change is compressed into hours, like the simulation. Transitions are smoothed so they read as change, not flicker. |

### 1.2 Three words

| Word | Means | Test |
|---|---|---|
| **Grounded** | Real Mars first; every stylization is written down (keyframes §2.3) | Could the SME point to a source or a stated stylization for this? |
| **Legible** | A 12-year-old on a small laptop screen can tell what changed and what something is | Does it pass the silhouette, grayscale, and colorblind checks (section 4)? |
| **Hopeful** | The world gets warmer in color, sound, and life as the player succeeds | Does success *look* like success, without a pop-up saying so? |

### 1.3 How art serves the design pillars

| Pillar | Art's job |
|---|---|
| The planet is the quest log | The sky, ground, and water show where the player is in the game. A player who looks up should be able to guess the phase. |
| Every change is visible | Each of the six Index values has its own world cue (keyframes §1). Falling values show too. |
| Pressure, not grind | Hazards (storms, frost, collapse) are readable early, so players can plan instead of repeat. |
| Compact before wide | The dome interior is the most detailed space in Phase 1: worth staying in. |
| Your progress provokes the planet | Machines visibly attract the thawlings (heat shimmer, frills opening); the cause is on screen. |

---

## 2. Color

### 2.1 Color roles

Each family of things owns a color region, so players can sort the world at a glance. Hex values are the middle tones of the ramps in 2.2.

| Role | Color region | Mid tone | Why |
|---|---|---|---|
| **Planet (dead)** | Rust, tan, ochre, dark basalt | `#9A5B3C` regolith | Real Mars surface (keyframes §2) |
| **Ice and frost** | Near-white with cool grey shade | `#E4E8EA` | High luminance: reads on any terrain |
| **Water** | Silty brown-grey early, blue later | `#6E6250` → `#2C6CA6` | Water clears as the hydrosphere matures |
| **Human tech** | Cool alloy grey + safety orange accent | `#7E8790`, `#E69F00` | Cool against the warm planet; orange marks "touch this" |
| **Flora** | Dark biocrust → olive moss → green | `#43432F` → `#79A14B` | Luminance steps, not only hue (section 4.3) |
| **Thawlings (fauna)** | Cool grey-lavender with salt-white speckle | `#8E8FA6` | Belongs to neither the planet nor the tech; never red (section 6) |
| **UI signals** | Okabe–Ito colorblind-safe set | see 4.4 | Proven colorblind-safe set |

**Reserved colors.** Saturated red-orange (`#D55E00`, vermillion) is reserved for **failure and danger** in UI and hazard markings. It is never used on fauna, flora, or story props. Pure magenta `#FF00FF` is reserved for the **placeholder marker** (5.5).

### 2.2 Material ramps

Four tones per material, dark to light. Shadows shift slightly cooler and highlights slightly warmer than a straight darken/lighten; this keeps 16×16 textures lively without extra colors. All values are also on the reference sheet.

| Material | 1 (dark) | 2 | 3 (mid) | 4 (light) | Source / note |
|---|---|---|---|---|---|
| Regolith | `#5E3424` | `#7A4530` | `#9A5B3C` | `#B8764F` | Rust-brown iron-oxide soil (keyframes §2.1) |
| Bright dust | `#8E5E3E` | `#A86E48` | `#B97C52` | `#D09A6E` | Fine airborne/settled dust, lighter than soil |
| Basalt sand | `#2E2724` | `#3F3430` | `#4F4038` | `#685750` | Dark dune sand |
| Grey rock (drilled, fresh) | `#4A4644` | `#625C59` | `#7C7470` | `#9A928C` | Unoxidized rock under the dust is grey |
| Water ice | `#9FB0BC` | `#C2CDD5` | `#E4E8EA` | `#F7F9FA` | Clean ground ice |
| Dusty / layered ice | `#8C7C6E` | `#B3A496` | `#D2C6BA` | `#E8DFD5` | Polar-style layered ice with dust bands |
| Salt crust (perchlorate, brine residue) | `#B9B2A4` | `#D3CDBF` | `#E7E2D6` | `#F4F1EA` | **Stylized**: real perchlorate in soil is not visible; we show where brine dried |
| Soil (treated, organic) | `#3A2A20` | `#4A3528` | `#5A4232` | `#735743` | Darker and browner than regolith |
| Biocrust | `#2B2B1F` | `#363626` | `#43432F` | `#5A5A40` | First colonizers: dark crust (real on Earth deserts) |
| Moss | `#4E5E32` | `#657540` | `#7E9150` | `#98AA68` | Lighter than regolith on purpose (4.3) |
| Grass | `#4B6E2E` | `#5E853A` | `#79A14B` | `#93B967` | Pioneer grass |
| Alloy (dome, machines) | `#3B4148` | `#59616A` | `#7E8790` | `#A8B0B7` | Cool neutral |
| Thawling | `#4A4A5E` (edge) | `#7A7C96` | `#8E8FA6` | `#C9C6D2` | See section 6 |

**Accent:** safety orange `#E69F00` with a 45° stripe pattern; dome glass `#C8DCE4` at 35 % alpha.

### 2.3 Palette rules for textures

1. **Max 6 colors per 16×16 block face**, plus transparency. Most faces use one ramp (4 tones) plus one accent.
2. **No pure black (`#000000`) or pure white (`#FFFFFF`)** in world textures. Darkest is a ramp's tone 1; lightest is `#F7F9FA`.
3. **One light direction:** top-left, like vanilla convention, so our blocks sit with vanilla ones.
4. **No dithering gradients** across a face; use 2–3 px clusters. Noise reads as dirt at 16×16 and blurs at distance.
5. **Values over hues:** a texture must still read when converted to grayscale (section 4.3).

---

## 3. Shape language

| Family | Shapes | Avoid |
|---|---|---|
| Human tech | Rectangles, chamfered corners, bolts at corners, visible panels and seams, status lamp on a front face | Organic curves, glowing "sci-fi" runes |
| Flora | Rounded clumps, soft blades, radial leaves | Spikes |
| Thawlings | Domed back, round body, stubby legs, soft frills | Teeth, claws, spikes, glowing eyes, hunched "stalker" posture |
| Hazards | Diagonals, cracks, jagged outlines, stripes | Using color alone to say "danger" |
| Story props | Hand-made: tape, paper, scratched tallies, labels | Pristine, mass-produced look |

### 3.1 Status lamps: shape-coded

Every machine has one status lamp (2×2 or 3×3 px on the front face). State is shown by **shape and blink pattern**, with color as a third channel.

| State | Shape | Blink | Color | Text (Monitor/tooltip) |
|---|---|---|---|---|
| Running | Solid square | Steady | `#009E73` | "Running" |
| Idle / no input | Hollow square | Steady | `#A9B0B8` | "Idle: no power" (cause named) |
| Worn (needs parts) | Triangle | Slow pulse, 1 per 2 s | `#E69F00` | "Worn: 30 %" |
| Failed | X / cross | Fast pulse, 2 per s (max; see 4.6) | `#D55E00` | "Failed: seal" |

### 3.2 Silhouette test
Every mob, machine, and prop must be identifiable as a **solid black silhouette at 32×32 screen pixels**. Thawling life stages must be told apart by silhouette alone.

---

## 4. Readability and accessibility (must pass)

These rules apply to everything visual. They are the art side of the ROADMAP accessibility pass and LEARNING.md's "color is never the only signal".

### 4.1 Color is never the only signal
Every meaningful visual state carries **at least two** of these channels: color, **shape**, **pattern/texture**, **motion**, **sound** (with subtitle), **text or number**. Examples:

| Signal | Channels |
|---|---|
| Sky changing with pressure | Sky color + fog distance + sunset halo position and color + windsock angle + exterior sound level |
| Biomass rising | Block texture changes (blades, tufts) + luminance step + particles + rustle sound |
| Machine state | Lamp shape + blink + color + tooltip text |
| O2 high enough to burn | Flame **size** and flicker + fire spread + HUD text |
| Journal fact type | Badge shape + label word + color (section 7.4) |

### 4.2 Colorblind safety
- Check every palette and keyframe with a simulation of **deuteranopia, protanopia, and tritanopia** (Machado, Oliveira & Fernandes 2009 matrices; any equivalent tool is fine).
- **Minimum differences** (OKLab ΔE, a perceptual color distance; about 0.02 is a just-noticeable difference):
  - A **step change the player is meant to notice** (one keyframe to the next): ΔE ≥ 0.035 in normal vision and in at least two of the three simulations. Where a step falls below that in one simulation, a non-color channel must change at the same time (the keyframe tables say which).
  - An **object against its usual background** (mob on ground, machine on terrain): ΔE ≥ 0.10 in all simulations, **or** a 1 px darker edge ramp around the silhouette.
- **Mars is a red world, and red–green colorblind players are the largest group** (roughly 1 in 12 boys of Northern European descent is commonly cited; **unverified** here, SME to source). Green on rust is exactly the pair they confuse. So flora must change **luminance and texture**, not just hue (4.3).

**Check run for this draft** (my script, Machado matrices at full severity; scratch only, not shipped):

| Pair | Normal ΔE | Worst simulated ΔE | Result |
|---|---|---|---|
| Regolith `#9A5B3C` vs. moss (first draft `#5E7440`) | 0.114 | **0.019** (deuteranopia) | **Failed**: moss was retuned |
| Regolith vs. moss (final `#7E9150`) | 0.146 | 0.087 | Passes, with texture change |
| Regolith vs. biocrust `#43432F` | 0.180 | 0.127 | Pass |
| Thawling `#8E8FA6` vs. regolith | 0.168 | 0.154 | Pass |
| Thawling `#8E8FA6` vs. grass `#79A14B` | 0.155 | **0.033** | Fails alone → 1 px edge ramp `#4A4A5E` required on all thawling textures |
| Sky zenith 0.6 → 5 kPa (first draft) | 0.021 | 0.009 | **Failed**: Phase 2 sky shift was invisible; retuned (keyframes §3.1) |

### 4.3 Grayscale test
Convert any texture or screen to grayscale. If two things the player must tell apart look the same, fix the **values**, not the hues. The ground-conversion chain (regolith → biocrust → soil → moss → grass) alternates dark and light on purpose: OKLab lightness 0.54 → 0.38 → 0.40 → 0.63 → 0.66.

### 4.4 UI colors and contrast
UI uses the **Okabe–Ito palette** (Okabe & Ito, 2008, *Color Universal Design*), chosen to stay distinct for common color vision types.

| Use | Hex | Contrast on panel `#1E2328` | Allowed for |
|---|---|---|---|
| Body text | `#E8E3D8` | 12.4 : 1 | All text |
| Secondary text, sol stamps | `#A9B0B8` | 7.2 : 1 | All text |
| Orange (warning) | `#E69F00` | 7.0 : 1 | Text and icons |
| Sky blue (info) | `#56B4E9` | 6.9 : 1 | Text and icons |
| Yellow (gate / milestone) | `#F0E442` | 12.0 : 1 | Text and icons |
| Bluish green (OK / real) | `#009E73` | 4.6 : 1 | Text (just passes) and icons |
| Reddish purple (fiction) | `#CC79A7` | 5.2 : 1 | Text and icons |
| Vermillion (failure) | `#D55E00` | 4.1 : 1 | **Icons and fills only**, not text |
| Blue | `#0072B2` | 3.1 : 1 | **Icons and fills only**, not text |
| Paper card text on paper | `#2A2420` on `#EDE4D3` | 12.1 : 1 | Recorded messages, notes |

Targets follow WCAG 2.1: **4.5 : 1 for text**, **3 : 1 for icons and graphical parts** (success criteria 1.4.3 and 1.4.11). Meaning is always in the text word too ("WARNING:", "FAILED:"), never in text color alone.

**The table above is for the slate panel only.** The same colors fail on paper and over the open world; see 4.4a.

### 4.4a Two backgrounds: paper sheets and the open world (numeric check)

The same row style (a severity icon, a sigil, a badge) appears on **light paper** (journal, message slips, Hana's notes, `#EDE4D3`) and on **dark panels or strips over the 3D world** (HUD toasts, log, look-at readouts). Icons drawn for one vanish on the other (PROCESS.md §4). Checked with WCAG 2.1 contrast, my script, 2026-11-26 (scratch only):

**On paper `#EDE4D3`, the Okabe–Ito colors fail:** yellow 1.0 : 1, body text 1.0, sky blue 1.8, orange 1.8, purple 2.4, green 2.7. So paper uses a darker **paper-ink set**, same hue family, and the meaning stays in shape and word:

| Role | HUD / slate color | Paper-ink color | Contrast on paper | Allowed on paper for |
|---|---|---|---|---|
| Text | `#E8E3D8` | `#2A2420` | 12.1 : 1 | All text |
| Warning (orange) | `#E69F00` | `#8A5A00` | 4.7 : 1 | Text and icons |
| Gate / milestone (yellow) | `#F0E442` | `#6B6200` | 4.9 : 1 | Text and icons |
| Info (sky blue) | `#56B4E9` | `#1F6A99` | 4.6 : 1 | Text and icons |
| OK / real (green) | `#009E73` | `#006B4E` | 5.2 : 1 | Text and icons |
| Fiction (purple) | `#CC79A7` | `#8E3F6C` | 5.4 : 1 | Text and icons |
| Failure (vermillion) | `#D55E00` | `#A34500` | 4.9 : 1 | Text and icons |
| Blue | `#0072B2` | `#0072B2` | 4.1 : 1 | Icons only |

**Over the 3D world, every line of text needs a backing** (PROCESS.md §4). Worst case is text in front of the sun disc `#FFF6E0` or a bright cloud `#F4F4F2`:

| Backing `#1E2328` at | Body | Secondary | Orange | Sky blue | Yellow | Green | Purple | Vermillion | Result |
|---|---|---|---|---|---|---|---|---|---|
| 60 % | 3.5 | 2.0 | 2.0 | 1.9 | 3.4 | 1.3 | 1.5 | 1.2 | Fails |
| 80 % | 6.6 | 3.9 | 3.8 | 3.7 | 6.4 | 2.5 | 2.8 | 2.2 | Colored text fails |
| **90 % (rule)** | **9.3** | **5.4** | **5.3** | **5.1** | **9.0** | 3.5 | 3.9 | 3.1 | **Pass** (green, purple, vermillion as icons only) |

**Rules**
- Every HUD text line sits on a `#1E2328` strip at **≥ 90 % opacity**, with 2 GUI px padding. No text floats on the sky.
- **Outline instead of a strip** is allowed only for 1–2 word in-world labels (e.g. a look-at title): 1 GUI px `#1E2328` outline (14.7 : 1 against the brightest sky).
- **Every icon family has two variants:** `_hud` (1 px `#E8E3D8` light outer edge, Okabe–Ito fill, for slate and strips) and `_paper` (1 px `#2A2420` ink edge, paper-ink fill). Families: Index parameter icons (U-01), speaker sigils (U-07), Real/Simplified/Fiction badges (U-10), severity icons (U-15), status shapes (U-16), gate diamond, trend arrows, helmet icons, colony post icons. Asset list row U-31.
- **Check at the smallest window:** 1280×720 at GUI scale 3 (427×240 GUI px) and GUI scale 2. Every screen's text color is checked against **the background it actually sits on** in that layout, not against a swatch. This joins the numeric checks above and goes into the screenshot regression check (PROCESS.md §3; handoff to the Technical Lead).

### 4.5 Small classroom screens
Assumption (**unverified**, Technical Lead to confirm with school hardware data): many school laptops run **1366×768** or **1280×720**. Minecraft limits the GUI scale so the scaled screen stays at least 320×240; at 1280×720 the largest scale is 3, giving a **427×240** GUI-pixel canvas.

| Rule | Value |
|---|---|
| Every custom screen fits in | **420×236 GUI px** (fits 1280×720 at GUI scale 3, with margin) |
| Body text readable at | GUI scale 2 on a 1366×768 screen (vanilla font, 9 px line height → 18 screen px) |
| Fonts | Deliberate faces per role (section 4.8). The vanilla font is used only where vanilla draws the text (chat, vanilla tooltips, subtitles) and as a documented glyph fallback for translations |
| Minimum icon size | 9×9 GUI px for icons carrying meaning; 16×16 preferred |
| Text in textures | **None** that carries meaning (it can't be translated or read at distance). Signs and labels in the world are decoration; the real words are in tooltips and the journal. |
| HUD toasts | Max 3 lines, top-right, never over the crosshair or hotbar; stay ≥ 6 s and can be reopened from the journal (no time-pressured reading, LEARNING.md) |
| Helmet visor overlay | Max 4 % of screen height per edge; can be turned off in accessibility settings |

### 4.6 Motion, flashing, and comfort
- **No more than 3 flashes per second** anywhere (WCAG 2.3.1). Applies to storms, radiation spikes, failure lamps, the beacon, and fire.
- No full-screen flashes. Radiation spikes and lightning-like events use a slow fade (≥ 0.5 s).
- Camera shake (ground collapse) is optional and off by default for classroom mode.
- Sky and fog transitions are smoothed (keyframes §1.3), never snapped.

### 4.7 Sound cues need subtitles
Every gameplay sound has a subtitle string (vanilla subtitle system), and every sound that carries meaning has a visual twin. Full rules: [audio direction](../audio/audio-direction.md) §1; every sound and its subtitle and twin: [sound list](../audio/sound-list.md). The Lead Artist owns audio (user decision, 2026-11-26).

### 4.8 Typography

The process rule (PROCESS.md §4): pick deliberate faces, never fall back to the default font, and check glyphs before using them.

**What Minecraft 1.21.1 lets a resource pack do** (from the vanilla font system; **TL to confirm details for 1.21.1**): a font is a JSON file in `assets/<namespace>/font/` listing **providers**: `bitmap` (a PNG glyph sheet we draw), `ttf` (a TrueType/OpenType file, with size, oversample, and shift settings), `space` (fixed-width gaps), `unihex`, and `reference` (chain another font). Any text component can name a font (`"font": "mars:mono"`), and our own screens can draw with any font. **Limits:** vanilla UI (chat, vanilla tooltips, subtitles) keeps `minecraft:default` unless we override it for the whole game, which we won't; TTF text may look soft next to 16×16 pixel art at small sizes (**unverified**; straw-man render test needed).

| Role | Face | Used for | Source and license | Glyph set we need |
|---|---|---|---|---|
| **Display** | **Cairn Display** (original bitmap, by us), cap height 7 px | Screen titles, gate names, "Next:" headers, ending titles | Original; project asset license (open decision in TEAM.md) | A–Z, 0–9, basic punctuation, Latin-1 accents; uppercase only |
| **Numbers** | **Cairn Mono** (original bitmap, by us), every glyph 6 px advance (tabular, so changing values don't jitter) | Every changing number: Monitor values, gauges, look-at readouts, sol counters, HUD O2 and dose | Original; project asset license | 0–9, `+` `−` (U+2212, true minus), `.` `,` `%` `°` `/` `:` `×` `≥` `≤`, arrows `↑` `→` `↓` (U+2191–2193), `▲` `▼`, thin space (U+2009), unit letters for kPa, °C, %, BU, sol, kHz |
| **Reading** | **Atkinson Hyperlegible** via the `ttf` provider | Journal, message slips, colony log, failure reports, HUD lines, Monitor labels | Braille Institute; **SIL Open Font License 1.1** (to verify against the license file before shipping). Chosen because it was designed for low-vision legibility, with clearly different I / l / 1 and O / 0 | Latin, curly quotes `“ ” ‘ ’`, en dash, ellipsis, `°`, `−`, `·`. The font file ends with a `reference` to `minecraft:default` **only** so translations into other scripts still render (a documented glyph fallback, not the design default) |

- **Alternatives considered:** the vanilla font for all body text (my first draft; rejected under the new process rule, and its arrows and symbols come from a different-looking fallback); fonts made to imitate Minecraft's (rejected: imitation); IBM Plex Mono (OFL) as a TTF number face (good backup if the bitmap mono slips).
- **If the TTF render test fails** at 1280×720 (GUI scale 2 and 3): draw an original bitmap reading face, "Cairn Text", Latin only. Effort estimate ~3–5 days (Producer to weigh).
- **Glyph check:** before any string uses a symbol, check the face it's drawn in has that glyph. Proposed test (Technical Lead): for every language key, find its font, and assert every code point exists in that font's providers. Check marks and crosses are **icons**, not font glyphs.
- **Text hygiene:** units on every value; thousands separators (`1,500 BU`) from one helper; true minus sign on negative values (`−60 °C`); curly quotes in English text; no full stop after `?` or `!`.
- **Values bigger than labels:** on stat rows the number is drawn at 2× (Cairn Mono at scale 2, 14 GUI px tall) and the label at 1× in the reading face.
- **Sizes:** body text line 9 GUI px; at GUI scale 2 that is 18 screen px, at scale 3, 27. Never smaller.
- **Wrapped text has a set width** before it's placed on a screen (PROCESS.md §4). Widths per panel are in the UI spec (Lead Game Designer); the art default is 200 GUI px for log lines and 180 for paper slips.

### 4.9 Names in labels: name things the way the domain does

Labels use **real instrument and machine names** (PROCESS.md §4): "Dosimeter", "Pressure gauge", "Gas analyzer", "Thermometer", "Windsock", "Tethered balloon", "Airlock", "Carbon scrubber", "Electrolysis O2 generator". Plain-language help sits **next to** the real name at middle school depth ("Dosimeter: counts radiation"), never instead of it. Air check labels follow this too (§11.2). Final wording is the Lead Game Designer's and Lead Instructional Designer's; this is the visual rule (name first, larger; help second, secondary color).

---

## 5. Technical specs for assets

The Technical Lead sets up `pack/assets/` and may move assets into the companion mod's resources. Paths below assume the mod namespace `mars` (ARCHITECTURE.md naming: `mars:atmospheric_processor`). **The TL owns the final folder layout; these are proposals.**

### 5.1 Sizes

| Asset type | Size | Notes |
|---|---|---|
| Block faces | **16×16** | Vanilla-consistent. No higher-resolution blocks. |
| Items | **16×16** | |
| Animated block/item textures | 16×16 frames, stacked vertically, `.png.mcmeta` with `frametime` | Max **16 frames**; machines 4–8 frames; water/fluids 16–32 frames allowed |
| Entities (thawlings) | **64×64** texture sheet (juvenile may use 64×32) | Box-model style at the same 1 px = 1/16 block scale as vanilla mobs |
| Armor (EVA suit) layers | **64×32** per layer, 2 layers | Vanilla armor layout |
| Particles | **8×8** (dust, vapor, spores, bubbles), 16×16 max | 1–4 frames |
| Sky bodies | Sun **32×32**, Phobos 16×16, Deimos and Earth drawn as small quads (2–4 px) | Sun drawn at 0.66× vanilla quad size (keyframes §3.1) |
| Clouds | **256×256 greyscale density map** (`environment/clouds_density.png`), tileable; brighter = appears at lower coverage | TL option A (ARCHITECTURE.md §2.5.3): mesh rebuilt at 5 coverage steps (5 / 8 / 20 / 35 / 45 %). Concept generated (asset E-10) |
| GUI sprites | **Single sprites with nine-slice scaling** in `textures/gui/sprites/`, declared in `.png.mcmeta` (vanilla since 1.20.2 per TL, **unverified**; check with one panel in Stage 2). Fallback: 256×256 atlas per screen family | Panels: 9×9 px source sprites with a 3 px border (slate panel, paper card, backing strip). Two variants where needed: `_hud` and `_paper` (4.4a) |
| HUD icons | 9×9 and 16×16 | Index parameter icons need both sizes |
| Epilogue / ending cards | **192×108** GUI px illustration | Pixel illustrations, 16:9 |
| Pack icon / logo | 128×128 pack icon; title logo 256×64 | |

### 5.2 Format
- PNG, 8-bit RGBA, sRGB. No color profile tricks, no premultiplied alpha.
- Opaque blocks: alpha 0 or 255 only. Translucent blocks (glass, ice, water) may use partial alpha.
- No texture larger than needed; power-of-two sizes for sheets.

### 5.3 Naming and layout (proposal)

```
pack/assets/mars/textures/
  block/        regolith.png, regolith_dusty_1.png, water_ice.png, atmospheric_processor_front_on.png
  item/         filter_cartridge.png, biome_seed_kit_tundra.png
  entity/       thawling/thawling_juvenile.png, thawling_adult.png
  models/armor/ eva_suit_t1_layer_1.png, eva_suit_t1_layer_2.png
  particle/     dust_speck.png, vapor_wisp_0.png … _3.png
  environment/  sun.png, phobos.png, earth_star.png, clouds_density.png
  gui/sprites/  panel_slate.png, panel_paper.png, strip_hud.png (+ .png.mcmeta nine-slice)
  font/         cairn_display.png, cairn_mono.png (bitmap sheets; font JSON in assets/mars/font/)
  misc/         visor_overlay.png
```

- `snake_case`, English, ASCII. Same ID as the block/item where possible.
- State suffixes in this order: `_<face>` (`_front`, `_side`, `_top`), then `_<state>` (`_on`, `_off`, `_worn`, `_failed`, `_open`, `_locked`), then `_<n>` for variants or frames.
- Life stages and tiers: `_t1`, `_t2`; `thawling_juvenile`, `thawling_adult` (never "mutant").

### 5.4 Models
- Block and item models: vanilla JSON model format, made in Blockbench or by hand.
- Entity models and animations: **vanilla Java box models and vanilla keyframe animations**, exported from Blockbench's "Modded Entity" format (TL decision, ARCHITECTURE.md §2.13; export for 1.21.1 **unverified**, test one animation in Stage 2). GeckoLib is the fallback only if missing blends make the rear-up → shove → curl → roll sequence look broken. Because vanilla doesn't blend, each transition the thawlings need gets a **short dedicated animation** (≤ 0.4 s): idle → bask, bask → idle, rear-up → curl. Art delivers Blockbench sources plus exported textures.
- Keep machine models within one block where possible; multi-block machines get a clear front face with the status lamp.

### 5.5 Placeholders
- Placeholder textures carry a **2×2 `#FF00FF` marker in the top-left corner** so nobody mistakes them for final art in screenshots or playtests.
- File names stay the final IDs (so swapping art never breaks code).
- The asset list marks status **Placeholder** until final art lands.

### 5.6 Ownership, licensing, originality
- **All our textures and models are original.** Don't copy, trace, or recolor vanilla Minecraft textures, or any other game's or mod's art. Vanilla textures are not ours to redistribute.
- **Third-party mod art stays as it is.** Create's assets are All Rights Reserved: we must not copy, edit, or re-texture them (docs/tech/mod-licenses.md). Mekanism and Create machines will look like themselves. Our own machines should sit near them without clashing (cool greys, clean panels) but must not imitate them.
- Our own art license is a user decision (TEAM.md §4, still open).

---

## 6. The thawlings: animals, not monsters

**Fiction status:** invented for the game, option B framing (DESIGN.md, "The fauna: decision"). Their look must support what the story says about them: animals with a life cycle, drawn to warmth, not hunting the player (story bible §3.4, §8.1).

### 6.1 Design goals

| Goal | Visual answer |
|---|---|
| Read as an **animal**, not a monster | Rounded, domed body; small calm eyes set wide; no teeth or claws shown; animal behaviors animated (sniffing, basking, grooming, curling up) |
| Read as **from the ice and brine** | Cool grey-lavender skin with salt-white speckles; frost catches in the back ridges |
| Show **why** they come to machines | Back frills **fan open toward heat sources** (basking display). A player can see they want warmth, not the player. This supports B2 (machine signature drives them) |
| Show **life stages** by silhouette | Each stage has a different outline (6.2). Never described or drawn as "mutated" |
| Never look like an existing game's mob | Not segmented and worm-like, not insect-like, no glowing eyes. Checked against common Minecraft mobs (silverfish, endermite) and avoided: those are long and segmented; thawlings are round and domed |
| Stay readable on every ground | 1 px darker edge ramp `#4A4A5E`, belly lighter than back (4.2 check) |

### 6.2 Body plan and life stages

**Body plan (original):** a plump, domed body like a small seal pup crossed with a pill bug; six short stubby legs; a broad, flat, slightly upturned **scraper plate** on the snout (for scraping ice and frost); two small dark eyes; a double row of soft **heat frills** along the back that lie flat when cold and fan open when warm.

| Stage | When | Size (w × h × l, blocks) | Silhouette | Key poses |
|---|---|---|---|---|
| **Dormant** ("sleeper") | Before Thaw; in ice and Fridge B | ~0.3 curled, seen **inside** ice blocks | A dark, curled oval inside translucent ice (block variant `water_ice_sleeper`) | None (static) |
| **Stirring** (juvenile) | Thaw gate | 0.5 × 0.35 × 0.7 | Small dome, frills short, big head relative to body | Sniff, waddle, curl into ball when startled, bask at warm machine |
| **Later life stage** (adult) | Hydrosphere gate | 0.9 × 0.7 × 1.3 | Longer, higher dome; frills tall; scraper plate broad | Scrape machine casing, bask, shove-rear (defensive), curl-and-roll (flee) |
| **Settled** | Phase 3, quiet zone or stable biome | Same as adult | Same model; frills relaxed and half-open | Graze biocrust and moss, dig a nest bowl, groom, sleep in groups |

### 6.3 Colors
| Part | Hex |
|---|---|
| Back (adult) | `#7A7C96` |
| Back (juvenile), sides | `#8E8FA6` |
| Belly, speckles (salt) | `#C9C6D2`, `#EFEDF2` |
| Edge ramp (outline) | `#4A4A5E` |
| Frills | `#6F7A9C`; when fanned open near heat, the inner frill face shows `#B98A9A` **and** the frills change shape (open), so the state never relies on color |
| Eyes | `#1C1B22` with one `#C9C6D2` highlight pixel |

### 6.4 Animation and sound direction
- **Gait:** a soft six-leg wave walk; slow (0.6× player walk speed for adults).
- **Toward machines:** they approach, frills fan open, they press against the warm side and scrape. The "damage" is scraping and nesting, shown as scratch marks and frost-nest debris on the machine (overlay texture `machine_scraped`), not biting.
- **Toward the player:** they ignore the player unless attacked. If hit: rear up, frills flared, a short shove, then curl and roll away. **No attack lunge with open mouth.**
- **Deterrents** (DESIGN.md): near an insulation shroud they sniff, frills stay flat, they lose interest (a visible "nothing warm here"). At a lure station they cluster and bask. In a quiet zone they slow down, graze, and nest.
- **Sounds (direction for audio):** soft clicks, chirrs, a low hum when basking; a hiss when defending. No roars, screeches, or growls.
- **Death (lethal defense):** no gore, no blood. The body curls and fades into a small salt-crust puff, like vanilla mobs' smoke, recolored.

### 6.5 World traces of the fauna
- **Tracks:** six-dot paired prints in dust layer blocks (decal `thawling_tracks`), fading after 1 sol.
- **Nests:** shallow scraped bowl (block `thawling_nest`) lined with frost and salt crust; one per settled group.
- **Scrape marks** on machines (overlay).
- **Fridge B samples:** sleeper cysts visible through frosted jars.

---

## 7. Story presentation (BELL, messages, journal)

All story text is delivered by in-world media (story bible §6.1). The look of each medium tells the player who is speaking before they read a word.

### 7.1 BELL's colony log
BELL is "a careful instrument": calm, exact, never a personality (story bible §4.1). Its look is a plain instrument readout.

| Element | Spec |
|---|---|
| Panel | Dark slate `#1E2328`, 1 px border `#59616A`, square corners |
| Text | `#E8E3D8`, reading face (4.8), numbers in Cairn Mono, ≤ 20 words per line (story bible). Over the world: on a ≥ 90 % strip (4.4a) |
| Sol stamp | Prefix `Sol 41` in `#A9B0B8` |
| Severity | An icon **and** a word: Info (circle "i", `#56B4E9`), Gate/milestone (diamond, `#F0E442`), Warning (triangle "!", `#E69F00`), Failure (octagon "×", `#D55E00` fill, white glyph). The text stays `#E8E3D8`; meaning is in icon shape + word |
| BELL mark | A plain bracket glyph `[ ]` in `#A9B0B8`. No face, no eye, no glow. Never sinister: no red eye, no scanning line |
| HUD toast | Max 3 lines, top-right, 6 s, reopenable |
| "Since last shift" recap | Same panel, header "Since last shift" with a clock-arrow icon, 2–3 lines |

**Failure reports** use the same panel with a heavier top border in the severity color, the octagon icon, and the fixed layout **What failed · Likely cause · Hint**. Hana's taped tip, when attached, appears below as a small paper slip (7.2 style) with a strip of tape graphic, so the player sees a *person* added it.

### 7.2 Recorded messages
Messages cross space with a delay (story bible §4.1), so they look like **printed transmission slips**, not live chat.

| Element | Spec |
|---|---|
| Card | Paper `#EDE4D3`, text `#2A2420` (12.1 : 1), soft 1 px shadow; 9-slice so it fits 4.5 |
| Header strip | Speaker **sigil** + name + where they are + "sent 14 min ago" with a relay-wave icon (delay shown, not hidden) |
| Body | Transcript, always shown (text first, decided). "Read more" button for the high school band |
| Audio | No play button unless audio exists (voice is not planned) |
| Faces | **No portraits at launch.** Sigils carry identity. Avoids likeness and diversity-of-depiction risk at this stage; can be revisited |

**Speaker sigils** (shape is the identity; color is a second channel). The colors below are the `_hud` variants. **On the paper slip, use the paper-ink set (4.4a)**: five of the six HUD colors fall below 3 : 1 on paper.

| Speaker | Sigil | Color |
|---|---|---|
| Cmdr. Moreno | Double chevron | `#0072B2` |
| Dr. Basu | Hexagon cell | `#009E73` |
| Hana Kowalczyk | Wrench | `#E69F00` |
| Dr. Mensah (Earth desk) | Globe with grid lines | `#56B4E9` |
| BELL | Bracket `[ ]` | `#A9B0B8` |
| The Keeper (own reports) | Key | `#F0E442` |

**Message archive** in the journal: a list of slips with sigil, sender, sol, and a read/unread dot that is also hollow/filled (shape, not just color).

### 7.3 Journal and Keeper's Record
- The journal looks like a field notebook: grid-paper pages `#EDE4D3` with faint grid lines `#D9CFBC`, tabs on the side, each tab with an icon.
- The Keeper's Record header reads *"What the colonists will need to know"* (story bible §6.4). Filled entries get a stamped seal (shape) in paper-ink green `#006B4E` (the HUD green `#009E73` is only 2.7 : 1 on paper); unfilled are dashed outlines in `#2A2420`.
- Gate entries follow the story bible order; the **"On Earth"** line gets a small globe icon.

### 7.4 Real / Simplified / Fiction badges
Supports DESIGN.md fantasy rule 3 and the story bible's fiction flags. Every journal fact block that needs it carries one badge.

| Badge | Shape | Word | Color |
|---|---|---|---|
| Real | Solid circle with a check | "REAL" | `#009E73` |
| Simplified | Half-filled circle with a tilde | "SIMPLIFIED" | `#E69F00` |
| Fiction | Dashed-outline circle with a star | "MADE UP FOR THIS GAME" | `#CC79A7` |

Badges appear on journal paper, so they use the `_paper` variants (paper-ink colors, 4.4a); the colors in the table are the `_hud` variants used on the Monitor.

(Wording is the Lead Game Designer's and Lead Instructional Designer's call; this is the visual system.)

---

## 8. Crew props and the dome

The crew are never seen; their traces tell the story (story bible §4.3). Props should look **used and hand-made**.

| Prop | Look |
|---|---|
| Hana's taped notes | Small paper slip decal with a tape strip, slightly rotated; on machines' sides. Text appears in a tooltip/journal, not in the texture |
| Tally wall | Wall block with scratched tallies in groups of five; 5 variants (1–4 marks, and the struck-through 5) so walls can show any count |
| Dead tomato plant | Pot block with a wilted, brown-grey plant and a note slip. In Phase 3 a "living" variant exists if the player grows one (optional) |
| Lab Fridge B | Tall white-grey lab fridge, frost on the door edges; **locked** state shows a padlock shape and a closed latch; **unlocked** shows the latch open and the padlock hanging. Opening reveals frosted sample jars with sleeper cysts |
| Basu's drill sites | A survey stake with a faded flag (fabric `#CC79A7`, the "fiction" hue is *not* used here; use `#C9A227` mustard so it reads on rust), a drill collar in the ground, and a sealed sample canister |
| Supply caches | Cargo crates in alloy grey with stenciled panel patterns and a paper label slip (the packers' jokes live in the tooltip) |
| Handover note | Paper slip on the hab table |
| Main dish | Large dish on a mast: **broken** (bent, tilted, cable hanging) and **repaired** states |
| Shift Board | Cork-and-metal board block with pinned slips; slip count = open failure reports (shape count, not color) |
| Bunks (5) | Compact bunks with personal touches per bunk (blanket colors chosen from the Okabe–Ito set) |
| Shelter | Thick, rounded walls, regolith-bag texture outside: reads as "this protects" |

**The dome** is the Phase 1 home: warm interior light (`#F5E3C3` lamp color, LED lamps, no open flames, since there is no O2 to burn outside and no reason to waste it inside), grow lights `#E8C8F0`-tinted in the greenhouse corner, frost on the glass at night (keyframes §3.2).

---

## 9. The helmet and the helmet-off moment

### 9.1 Visor (helmet on)
- A thin visor frame overlay: alloy `#59616A` at 60 % opacity, corner brackets, max 4 % of screen height per edge (4.5). No full-face tint, so sky colors stay true and readable.
- During dust storms, a light dust film builds on the visor edges (overlay, not the center).
- HUD readouts (O2, suit integrity, radiation dose) sit in the visor frame with icons + numbers.

### 9.2 Helmet off (Breathable gate, story beat 17)
The emotional peak of the game. It should feel like a breath.

| Time | Visual | Sound (direction note for audio) |
|---|---|---|
| 0.0 s | Player removes helmet (action or prompt) | Suit seal release: a soft click and a short hiss |
| 0.0–1.5 s | Visor frame lifts upward out of view; the frame's corner brackets go last | Suit fan and breathing loop fade out |
| 1.5–4.5 s | No color change to the world (the world was already this color; the point is that *you* changed) | Exterior sound opens: full-frequency wind, water, flora rustle, distant thawling chirrs fade in to full volume |
| 4.5 s → | Clear view, no HUD frame | Ambient stays open; no music sting, no chime (let the world be heard). First breath at 2.0 s (audio direction §4) |
| ≥ 10 s | Moreno's message slip arrives (moved from 4.5 s so it doesn't land on the moment) | Message cue at −3 dB |

- No fade to black, no camera cut, no flash.
- Because helmet-off is a **live condition**, if the air becomes unbreathable the visor returns with a warning (triangle + word + sound) and the sound world closes back in.

### 9.3 Sound across the whole game (direction note)
Grounding: microphones on the Perseverance rover recorded Mars sound as much quieter than on Earth, with high pitches fading faster with distance (Maurice et al., 2022, *Nature*; not opened this session, **details unverified**). We use that direction, stylized:

| Pressure | Exterior world sound | Inside helmet |
|---|---|---|
| 0.6 kPa | Very quiet, muffled; high frequencies cut | Suit fan, breathing, own footsteps through the suit |
| 5 kPa | Still muffled; wind starts to be audible | Same |
| 30 kPa | Clearer; mid frequencies return | Same, quieter |
| ≥ 75 kPa | Full, Earth-like | (helmet off possible) |

Numbers (gain and low-pass values) are in keyframes §3.1 and [audio direction](../audio/audio-direction.md) §3, marked **[SME]** while the SME checks them. Audio is owned by the Lead Artist (user decision, 2026-11-26).

---

## 10. The beacon ending

| Step (story bible §6.5) | Visual |
|---|---|
| Build | The beacon is a transmitter mast (3×3 base, mast about 7 blocks; final size is the Game Designer's) topped with a ring of 8 landing-guide lamps. Alloy and safety orange; not a glowing pillar, and **not a vertical light beam** (keeps it clearly different from the vanilla beacon) |
| Light it | Lamps come on one by one around the ring (8 steps over 2 s). Then **signal rings**: thin expanding circles rise from the mast into the sky, `#F4F1EA` with a `#56B4E9` edge, one every 2 s (well under 3 flashes/s) |
| "Signal sent" | The sky directly over the beacon brightens 15 % within a 48-block radius for the rest of the sequence. BELL's log line appears |
| The wait | Rings stop; the landing lamps stay on, steady. The world is at its equilibrium look (keyframes §4.8), ideally at sunset so the **warm, Earth-like sunset** is visible: the opposite of the blue Mars sunset at the start (keyframes §3.1). That contrast is the visual summary of the whole game |
| Replies | Recorded-message slips, one per speaker |
| Epilogue cards | 192×108 pixel illustrations, one per outcome (asset list §7). Same calm palette for every outcome; **no** darker or "sad" palette for any fauna outcome (no ranking, story bible rule 7) |
| Keeper's Record | Opens with all seals stamped |

---

## 11. Planetary Monitor and group UI visuals

The Lead Game Designer owns the UI specs (screens, flows, wording). This section gives the **visual system** those specs should use. Proposals, open to the UI spec.

### 11.1 Pressure gauge with milestones (Armstrong line, Everest pressure)
- A vertical bar on a **log scale from 0.5 to 110 kPa**, so Phase 1's climb from 0.6 to 5 kPa takes real space on the bar (on a linear scale it would be a few pixels).
- Notches, each with a **pictogram, a short label, and a number**. Unreached notches are hollow outlines; reached ones fill in, with a BELL log line the first time.

| Notch | kPa | Pictogram | Label (wording: LGD) |
|---|---|---|---|
| Triple point | 0.61 | Droplet split by a line | "Water can't stay liquid below this" |
| Phase 2 gate | 5 | Gate diamond | "Phase 2" |
| **Armstrong line** | 6.3 | Body outline with a drop | "Armstrong line" |
| Phase 3 gate | 30 | Gate diamond | "Phase 3" |
| **Everest pressure** | 33.7 | Mountain peak | "Top of Everest" |
| Target | 75 | Flag | "Breathable target" |
| Earth sea level | 101.3 | Globe | "Earth" |

- The current value is a horizontal pointer with the number beside it; the trend is an arrow shape (up, flat, down), never only a color.

### 11.2 The O2 partial-pressure display ("Air window")
**The problem:** at 75 kPa, the breathable and fire-safe range is only **21.5–23 % O2** (balance sheet §1). On a normal 0–100 % gauge that window is about 1.5 % of the bar: invisible on a small screen and meaningless to a 12-year-old. The window's lower edge also **moves with pressure** (16 kPa ÷ pressure), which is the B4 trade-off.

**Visual answer, two levels:**

**Level 1 (default, middle school band): three checks and a zoomed O2 gauge.**
- **Air check row:** three small gauges side by side, each with a target notch and a check / cross shape: **"Thick enough"** (pressure ≥ 75 kPa) · **"Oxygen push"** (O2 partial pressure ≥ 16 kPa) · **"Low CO2"** (≤ 0.5 kPa). When all three show a check, a helmet-off icon lights (shape changes from closed helmet to open helmet). "Oxygen push" follows the story bible's middle school wording, "how hard the oxygen pushes". Under the naming rule (4.9) each check shows the **real name first** ("Pressure", "O2 partial pressure", "CO2 partial pressure") with the plain words as the help line at middle school depth; wording is for the Lead Game Designer and Lead Instructional Designer.
- **Zoomed O2 gauge:** the O2 bar shows only **15 % to 27 %**, so the window fills about an eighth of the bar instead of a sliver. Zones, each with a pattern **and** a pictogram:

| Zone | Range | Pattern | Pictogram |
|---|---|---|---|
| Too thin to breathe | below 16 kPa ÷ pressure (21.5 % at 75 kPa) | Sparse dots | Closed helmet |
| **Breathe here** | from the lower edge to 23 % | Clear, with a bold outline | Open helmet |
| Fire risk | above 23 % | Diagonal hatch | Flame |

- **The lower edge slides.** When pressure goes up, the "too thin" zone shrinks and the "breathe here" zone **widens on screen**. A player who pushes pressure higher literally sees the window open up. This is the B4 lesson made visible, and it is the main reason for this design.
- A ghost notch shows where the edge would be at the next 5 kPa of pressure (optional, high school band).

**Level 2 (high school band, "read more"): the Air window chart.** A small 2D chart: pressure across (60–100 kPa), O2 % up (15–27 %). The breathable, fire-safe region is the bold-outlined shape; the current state is a dot with a short trail of the last few sols. Fire-risk area hatched, too-thin area dotted. Fits 120×80 GUI px.

**Not used:** a single "breathability %" number. It hides the two causes the player has to balance.

**For the Lead Instructional Designer:** this display supports A1, A5, B4, C3; a 12-year-old readability check in the first walkthrough is needed (handoff in the report).

### 11.3 Shift Board
- **Block:** a cork-and-metal board with pinned paper slips; the number of slips on the texture follows the number of open failure reports (0–6 slip variants), so the board shows "lots to fix" from across the room.
- **Screen:** one row per open failure: failure icon (octagon), machine name and place, claimer's in-game name (or "Open"), and a **status shape**: hollow circle = open, half-filled = claimed, check = fixed. Text says the status too.
- Same panel style as BELL's log (§7.1), with a paper-slip look for each row so it reads as "people's notes".

### 11.4 Unranked ending cards
- Every card uses the **same frame, same palette range, same size** (192×108 illustration plus a text line). No card is darker, sadder, gold-framed, starred, or scored.
- Illustrations show places and animals calmly: thawlings nesting by a lake; a frosty deep-ice cave with thawling tracks; Fridge B's frosted jars with Basu's notebook; outposts on sunken ground beside a new one on rock; per-Keeper post card with post icons.
- Numbers (O2 margin, biomes, sols held) are plain text on the card, not bars or medals.

---

## 12. Sources and verification

Network access to NASA and reference sites was blocked in this session, so **no source below was opened**. Citations are from the artist's knowledge and must be checked by the SME before any of it appears as fact in game text. Colors in this guide are not used as facts in game text, so they are safe to prototype with.

| # | Source | Used for |
|---|---|---|
| 1 | Machado, Oliveira & Fernandes (2009), "A physiologically-based model for simulation of color vision deficiency", *IEEE TVCG* 15(6) | Colorblind simulation |
| 2 | Okabe & Ito (2008), *Color Universal Design* (web publication) | UI palette |
| 3 | W3C, WCAG 2.1, success criteria 1.4.3, 1.4.11, 2.3.1 | Contrast and flashing limits |
| 4 | Ottosson (2020), "A perceptual color space for image processing" (OKLab) | Color distances, interpolation |
| 5 | Maurice et al. (2022), "In situ recording of Mars soundscape", *Nature* | Sound direction |
| 6 | Real Mars color sources | See [planetary-keyframes.md](planetary-keyframes.md) §2 |
| 7 | Braille Institute, Atkinson Hyperlegible (SIL Open Font License 1.1; **license file to verify**) | Reading face (4.8) |
| 8 | Minecraft Java font JSON providers (bitmap, ttf, space, unihex, reference); **not opened this session** | What a resource-pack font can do (4.8) |

---

## 13. Learning mechanisms in play: visual specs

Answers DESIGN.md "Learning Mechanisms in Play" rows that name the Lead Artist, and the "Feedback types and timing" table. The Lead Game Designer owns screen layout and wording; these are the visual system and a **proposed** layout. Applies PROCESS.md §4 (one message at a time; no modal pop-ups for progress; values bigger than labels; mark the next step; backing under every line over the world).

### 13.1 Salient new-log highlight (Feedback types: "Feedback must be salient")
Goals: feedback salience for all goals; mainly **B1, C2, C3** (the log names causes and failures).

| Element | Spec |
|---|---|
| **Monitor "Log" tab** | When an unread entry arrives, the tab **flashes at 1 Hz** (0.5 s on, 0.5 s off) for **5 cycles**, then stays highlighted. Hard limit 3 flashes per second (4.6); we use 1 |
| Highlight (steady) | Tab gets a **badge**: the severity shape of the most serious unread entry (circle Info, diamond Gate, triangle Warning, octagon Failure; `_hud` variants) plus an unread count in Cairn Mono (1–9, then "9+"). Shape and number carry the meaning, color is the third channel |
| Re-flash | A new entry restarts the 5 cycles only if 30 s have passed since the last run, so a busy period doesn't blink constantly |
| **Monitor block in the world** | A 2×2 px indicator lamp on the block face, top right. Blinks at 1 Hz for up to 60 s while the viewing player has unread entries, then glows steady. Drawn per player on the client from that player's own read state (TL to confirm) |
| Sound twin | Log chime AU-U01 (rate-limited, audio direction §7.3). Subtitle "New colony log entry" |
| HUD toast | Existing (§7.1), **one at a time** at the top; others queue. Hidden while any full-screen sheet is open |
| Reduced motion | Accessibility setting "Reduce flashing": no flash; steady badge and lamp only |
| Cleared | When the player opens the Log tab, the badge clears for the entries shown |

### 13.2 Next-gate goal display (Flow: clear goals)
DESIGN.md: "Next: pressure 5 kPa, 62%". Goals: **A1, B3, C3** (reading progress data); motivational: clear goals.

| Element | Spec |
|---|---|
| Place | Top strip of the Monitor's Overview page, full width **404 × 38 GUI px** (layout in 13.4). A one-row compact version can be pinned to the HUD side (opt-in; on a 90 % strip; hidden over sheets). No pop-up ever announces progress |
| Header | Gate diamond icon + "Next:" in Cairn Display + gate name. This is the **marked next step**: a 2 px `#F0E442` accent bar down the left edge |
| Condition rows (1–3) | Per condition: parameter icon (U-01 `_hud`) · real name ("Pressure") · **value / target** in Cairn Mono at 2× ("3.1 / 5 kPa") · progress bar 80 × 5 px · percentage · trend arrow shape (↑ → ↓ as icons). Values are bigger than labels |
| Met condition | Bar full with a bold outline + check icon + word "met". Unmet: hollow bar. Neutral fill `#E8E3D8`; met fill `#009E73` with the check (never color alone) |
| Holds (Dome stable, Breathable, Equilibrium) | A row of **sol pips**: filled circle = sol held, hollow = to go, circle with two bars = hold paused. Count follows the active preset (3 / 7 / 14 standard; 1 / 2 / 3 classroom); pips 5 px wide, so 14 fit in 70 px |
| Machine-state gate (Dome stable) | "Life support steady" + pips (no Index bar) |
| Falling values | The bar shows the **current** value, so it falls; trend arrow down + word "falling". Gates already reached stay reached (lock-in): they move to a "Reached" list, not shown in the strip |
| After Equilibrium | "Next: build the beacon" (wording: Lead Game Designer) |
| "Just finished" | When a gate is reached, the strip shows "Reached: Phase 2 begins" with the gate diamond until the player's next real action (build, use a machine, leave the dome), then switches to the next gate (PROCESS.md §4) |

**Open question (Lead Game Designer and Lead Instructional Designer):** how is the percentage computed? My recommendation: **linear in the value's own unit, from the previous gate's threshold (or the start value) to this gate's threshold**, because "62 % of the way" in kPa is what a 12-year-old expects, and the real numbers sit beside it. The pressure **gauge** stays on a log scale (§11.1); the two are labeled differently ("62 %" vs. the gauge's tick labels), so they don't contradict each other.

### 13.3 Progressive disclosure on the Monitor (Cognitive load: one idea at a time)
DESIGN.md: Phase 1 shows pressure, O2, power, temperature; hydrosphere at the Thaw; O2/CO2 balance and biomass in Phase 3. Goals: **A1–A5, C3**.

| Element | Spec |
|---|---|
| **Fixed slots** | Every value has a fixed slot that never moves between phases, so players' spatial memory holds. Two panels: **Planet** (pressure, temperature, hydrosphere, O2, CO2, biomass) and **Dome** (O2 supply, power, water) |
| Planet vs. Dome | Different frames so the two are never confused: Planet panel has a rust frame `#9A5B3C` and a small planet icon; Dome panel a slate frame `#59616A` and a dome icon. Phase 1's "O2" on the Dome panel is the dome's supply, not the planet's 0.1 % (question for the Lead Game Designer, report) |
| Not yet in play | Slot drawn as a faint dashed outline `#3A4048`, **no label, no icon**: the layout doesn't jump later, and there is nothing to read now. Not a greyed-out row with a name (PROCESS.md §4: only offer what the player can use now) |
| Reveal | At the gate: the slot frame fades in (1 s), then the value; a "NEW" tag (diamond + word) stays for 1 sol of play. No separate sound; the gate chime already played |
| "Read more" (depth by band) | A book icon + "More" button. Expanded rows indent under their parent with a 1 px `#59616A` rule on the left. Middle school: partial pressures are here; high school: shown by default |
| **Cause breakdown** (fading) | Disclosure triangle (▶ closed / ▼ open, drawn as icons) + "Causes (3)". Open by default in Phases 1–2, **closed by default in Phase 3** (DESIGN.md Fading). Remembered per player |
| **Cause lines on the graph** (Multimedia: text next to what it explains) | The value's trend graph (160 × 60 GUI px) carries up to **3 callouts**, each tied by a 1 px leader line to the point where that cause started: machine icon + ≤ 4 words + signed rate in Cairn Mono ("Outgassers ×4 +0.40 kPa/sol"). No separate legend |

### 13.4 Proposed Monitor Overview layout (fits 420 × 236 GUI px)
Straw-man wireframe: [palettes/monitor-overview-wireframe-strawman.png](palettes/monitor-overview-wireframe-strawman.png) (generated by script; boxes and labels only; **placeholder**).

| Box | x, y, w, h (GUI px) |
|---|---|
| Tabs (Overview, Log, Fauna from Thaw, Air from Phase 3) | 8, 4, 404, 12 |
| Next-gate strip | 8, 20, 404, 38 |
| Planet panel (2 × 3 slots, each 130 × 54) | 8, 62, 268, 170 |
| Dome panel (3 slots, each 128 × 54) | 280, 62, 132, 170 |

Each slot: icon 16 × 16 at top left, real name in the reading face, value in Cairn Mono at 2×, trend arrow, mini bar 60 × 4. The Air window (§11.2) and pressure gauge (§11.1) open from their slots as detail views at the same 420 × 236 size.

### 13.5 Journal "what happened" pages (Multimedia: pair words with visuals)
Goals: whichever goal the entry teaches (gate entries: A1–A5, B1, D2).
- Page: one **diagram panel 120 × 72 GUI px** above ≤ 60 words (DESIGN.md).
- Diagram style: 1 px `#2A2420` lines on paper `#EDE4D3`; at most three paper-ink accents (4.4a); labels written **on** the diagram, next to the part they name; cause arrows are solid, "not this" arrows dashed. Same icons as the Monitor (`_paper` variants).
- If a Monitor graph is the visual, it is redrawn in paper style, not screenshotted.
- Asset row U-25 (one diagram per gate entry and per forecast explanation; count from the Lead Instructional Designer).

### 13.6 Shift Briefing card (Advance organizers)
- BELL panel (§7.1) with a clipboard icon and header "Shift briefing", 2–3 lines, delivered at a natural pause at phase start.
- The 1–2 parameter icons the briefing names are shown large on the card, and the **same slots on the Monitor get a "watch" ring** (2 px `#F0E442` outline + eye icon) for the first sol of the phase. That links the organizer to the place the player will look.

### 13.7 Landing checklist (controls first, in story)
- BELL's "first shift checklist": 5–7 items, each a hollow box that becomes a check icon, on a 90 % strip at the **top left** (toasts are top right), max 120 × 80 GUI px.
- Key bindings drawn as key-cap boxes with the bound key's name (read from the player's settings, never hard-coded).
- Hides over any full-screen sheet. "Skip" for experienced players (DESIGN.md).

### 13.8 Worked example: Hana's notes on the first Outgasser
- Her taped-note style (§7.2 paper slip with tape), attached to the side of the Outgasser's machine screen: numbered steps, each with a small pictogram, ≤ 12 words per step. Paper-ink colors only.
- "Skip notes" link for experienced players. After the first Outgasser, the note shrinks to a folded corner the player can reopen (fading).

### 13.9 Process and outcome feedback, and fading (Feedback types table)
| Feedback | Visual |
|---|---|
| Process (new situation) | Full failure report: What failed · Likely cause · Hint (§7.1), with Hana's slip the first time |
| Repeat of the same failure (fading) | **Compact report:** one line on the log panel: octagon + machine name + "again: likely cause" + a "details" disclosure. Failure tone at −3 dB (audio direction §7.2) |
| Outcome (familiar situation) | Trend arrows, next-gate bars, sol pips. No card |
