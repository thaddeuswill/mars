"""Habitat restyle (2026-11-27): dome shell, hatch, tank, reclaimer and oxygen maker textures and the
model files for the new variant blocks. Owner: Lead Artist. Reference images: docs/art/references/habitat/
(NASA ISS hatch, NORS tank, MOXIE PIA24201, ISS rack, MDRS Hab). Colours are read BY EYE from those
photos, snapped to the style guide ramps where one exists (not measured).

Usage: python3 -I tools/art-preview/gen_habitat_restyle.py
Writes pack/assets/mars/textures/block/*.png and models/block/*.json (same file names as before for the
existing assets). Exits non-zero if a check fails (16x16, no pure black/white, opaque unless glass)."""
import json, math, os, sys
from PIL import Image

ROOT = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", ".."))
A = os.path.join(ROOT, "pack", "assets", "mars")
TEX = os.path.join(A, "textures", "block")
MOD = os.path.join(A, "models", "block")
fails = []


def rgb(h):
    h = h.lstrip("#")
    return tuple(int(h[i:i + 2], 16) for i in (0, 2, 4))


class Tex:
    def __init__(self, fill=None):
        self.p = {}
        if fill:
            for y in range(16):
                for x in range(16):
                    self.p[(x, y)] = (rgb(fill), 255)

    def set(self, x, y, c, a=255):
        if 0 <= x < 16 and 0 <= y < 16:
            self.p[(x, y)] = (rgb(c), a)

    def rect(self, x0, y0, x1, y1, c, a=255):
        for y in range(y0, y1 + 1):
            for x in range(x0, x1 + 1):
                self.set(x, y, c, a)

    def save(self, name):
        im = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
        for (x, y), (c, a) in self.p.items():
            im.putpixel((x, y), c + (a,))
        im.save(os.path.join(TEX, name + ".png"))
        TEXS[name] = im


TEXS = {}

# ---- palette (style guide 2.2 where a ramp exists; "hull" is new, read by eye from MDRS #12 / ISS #7) ----
H_DARK, H_SHADE, H_MID, H_LIGHT = "#7C786C", "#C4C0B2", "#DAD6C9", "#EDE9DD"  # painted hull, warm white
CREAM, CREAM_L = "#CDBF98", "#DDD2B0"  # MDRS cream trim band
AL1, AL2, AL3, AL4 = "#3B4148", "#59616A", "#7E8790", "#A8B0B7"  # alloy ramp
SALT1, SALT2, SALT3, SALT4 = "#B9B2A4", "#D3CDBF", "#E7E2D6", "#F4F1EA"  # salt crust ramp = tank wrap, tubes
GOLD1, GOLD2, GOLD3, GOLD4 = "#5C4320", "#7E5E2C", "#A3823F", "#C9AE6A"  # gold plating (style guide 2.2)
COPPER, COPPER_D = "#B5683F", "#7A4530"
SEAL = "#B9BA3E"  # yellow-green hatch seal frame (ref #11), by eye
GREEN, ORANGE, VERM, BLUE = "#009E73", "#E69F00", "#D55E00", "#0072B2"
GLASS = "#C8DCE4"


# ------------------------------------------------------------------ hull plate family
def plate(t, seam=True):
    """Tall vertical panel strip (MDRS: vertical seams only). Light from top-left."""
    t.rect(0, 0, 15, 15, H_MID)
    if seam:
        t.rect(0, 0, 0, 15, H_DARK)  # panel seam
        t.rect(1, 0, 1, 15, H_LIGHT)  # highlight beside the seam
        t.rect(15, 0, 15, 15, H_SHADE)  # shade before the next seam
    for (x, y) in ((3, 2), (3, 10), (12, 6), (12, 14)):  # bolt rows near both panel edges (ISS #7)
        t.set(x, y, H_DARK)
    for (x, y) in ((7, 4), (8, 4), (9, 12), (10, 12), (10, 13)):  # light wear, not dirt (dust is gradual)
        t.set(x, y, H_SHADE)


def hull_plate():
    t = Tex(); plate(t); t.save("hull_plate")


def hull_plate_base():
    """Wall course y1 (outside and inside): cream band at the foot, dark footing (MDRS #12)."""
    t = Tex(); plate(t)
    t.rect(0, 10, 15, 10, H_DARK)           # seam above the band
    t.rect(1, 11, 15, 14, CREAM)
    t.rect(1, 11, 15, 11, CREAM_L)           # highlight on the band top
    t.rect(0, 11, 0, 14, H_DARK)
    t.rect(0, 15, 15, 15, H_DARK)            # footing
    for (x, y) in ((3, 12), (12, 13)):
        t.set(x, y, H_DARK)
    t.save("hull_plate_base")


def hull_plate_rim():
    """Roof-edge course y4 (side faces): cream band at the top, then panel."""
    t = Tex(); plate(t)
    t.rect(1, 0, 15, 3, CREAM)
    t.rect(1, 0, 15, 0, CREAM_L)
    t.rect(0, 0, 0, 3, H_DARK)
    t.rect(0, 4, 15, 4, H_DARK)             # seam under the band
    for (x, y) in ((3, 2), (12, 2)):
        t.set(x, y, H_DARK)
    t.save("hull_plate_rim")


def hull_plate_rim_top():
    """Roof-edge course, top face: cream frame round plain roof panel; seen from above."""
    t = Tex(); t.rect(0, 0, 15, 15, CREAM)
    t.rect(2, 2, 13, 13, H_MID)
    t.rect(2, 2, 13, 2, H_LIGHT); t.rect(2, 2, 2, 13, H_LIGHT)
    t.rect(2, 13, 13, 13, H_SHADE); t.rect(13, 2, 13, 13, H_SHADE)
    t.rect(0, 0, 15, 0, CREAM_L); t.rect(0, 0, 0, 15, CREAM_L)
    for (x, y) in ((4, 4), (11, 4), (4, 11), (11, 11)):
        t.set(x, y, H_DARK)
    t.save("hull_plate_rim_top")


def hull_plate_cracked():
    """Same plate, a clear crack with a bright edge and a rust dust streak: a leak that shows in a wide
    shot by shape (crack) and a second channel (dust streak, bright lip). INVENTED (DESIGN.md audit)."""
    t = Tex(); plate(t)
    crack = [(7, 0), (7, 1), (6, 2), (6, 3), (7, 4), (8, 5), (8, 6), (7, 7), (6, 7), (5, 8), (6, 9), (7, 9),
             (8, 10), (9, 10), (9, 11), (10, 12), (10, 13), (11, 14), (11, 15)]
    branch = [(8, 5), (9, 5), (10, 4), (11, 4), (7, 9), (6, 10), (5, 11)]
    for (x, y) in crack + branch:
        t.set(x, y, "#26282B")
    for (x, y) in crack:  # bright lip on the right-hand edge (light from top-left would catch the far edge)
        if (x + 1, y) not in crack and (x + 1, y) not in branch:
            t.set(x + 1, y, H_LIGHT if y % 2 else "#F7F9FA")
    for (x, y) in ((9, 6), (9, 7), (10, 8), (10, 9), (11, 10), (11, 11), (12, 12), (12, 13), (12, 14)):  # dust streak: Mars blowing in
        t.set(x, y, "#9A5B3C")
    t.save("hull_plate_cracked")


def hull_glass():
    """Round ringed porthole in a hull plate (MDRS #12, hatch window #11). Opaque frame, translucent glass."""
    t = Tex(); plate(t)
    cx = cy = 7.5
    for y in range(16):
        for x in range(16):
            d = math.hypot(x - cx, y - cy)
            if d <= 4.7:
                t.set(x, y, GLASS, 89)           # 35 % alpha dome glass (style guide 2.2)
            elif d <= 5.6:
                t.set(x, y, AL4)                 # silver ring
            elif d <= 6.5:
                t.set(x, y, H_DARK)              # dark gasket
    for (x, y) in ((6, 5), (7, 6), (8, 7)):      # glint
        t.set(x, y, "#F7F9FA", 150)
    t.save("hull_glass")


def hull_glass_ribbed():
    """NEW variant for the annex roof: ribbed translucent panel (MDRS Greenhab #12, Ice Home dome #3)."""
    t = Tex()
    t.rect(0, 0, 15, 15, GLASS, 70)
    t.rect(0, 0, 0, 15, AL4); t.rect(0, 0, 15, 0, AL4)
    t.rect(8, 1, 8, 15, AL4, 200)                # thin mid rib
    for (x, y) in ((3, 3), (4, 4), (11, 10), (12, 11)):
        t.set(x, y, "#F7F9FA", 150)
    t.save("hull_glass_ribbed")


# ------------------------------------------------------------------ hatch (two tiles, 1 x 2 blocks)
def door_frame(t, top):
    """Yellow-green seal frame with rounded corners round a raw aluminium slab (all opaque)."""
    t.rect(0, 0, 15, 15, AL3)
    t.rect(0, 0, 0, 15, SEAL); t.rect(15, 0, 15, 15, SEAL)
    if top:
        t.rect(0, 0, 15, 0, SEAL)
        for (x, y) in ((1, 1), (2, 1), (1, 2), (14, 1), (13, 1), (14, 2)):
            t.set(x, y, SEAL)
        t.rect(3, 1, 12, 1, AL4); t.rect(1, 3, 1, 15, AL4)   # light edge, top-left
    else:
        t.rect(0, 15, 15, 15, SEAL)
        for (x, y) in ((1, 14), (2, 14), (1, 13), (14, 14), (13, 14), (14, 13)):
            t.set(x, y, SEAL)
        t.rect(1, 0, 1, 12, AL4)


def hull_door_top():
    t = Tex(); door_frame(t, True)
    cx, cy = 7.5, 8.5
    for y in range(16):
        for x in range(16):
            d = math.hypot(x - cx, y - cy)
            if d <= 2.9:
                t.set(x, y, "#9CC3D8")
            elif d <= 3.9:
                t.set(x, y, AL1)
            elif d <= 4.9:
                t.set(x, y, AL4)
    for (x, y) in ((6, 7), (7, 8)):
        t.set(x, y, "#D8E8EE")
    # indicator arc under the ring: green | yellow (seal colour) | vermillion, left to right (ref #11); shape + position
    # are the cue, colour is a second channel; static (decoration, not a live state).
    for y in range(16):
        for x in range(16):
            d = math.hypot(x - cx, y - cy)
            if 5.2 <= d <= 6.3 and y >= cy + 1.5:
                t.set(x, y, GREEN if x <= 5 else (SEAL if x <= 9 else VERM))
    t.save("hull_door_top")


def hull_door_bottom():
    t = Tex(); door_frame(t, False)
    # crank handle (ref #11: lever to the right of the window): pivot + lever + knob
    t.rect(11, 2, 12, 3, AL1)
    t.rect(7, 3, 10, 3, AL4)
    t.rect(6, 3, 6, 4, AL1)
    # "Opening hatch" placard (upper left) and "Closing hatch" placard (lower left): salt-white plates, dark text rows
    for (x0, y0) in ((3, 3), (3, 11)):
        t.rect(x0, y0, x0 + 3, y0 + 2, SALT3)
        t.rect(x0 + 1, y0 + 1, x0 + 2, y0 + 1, AL2)
    # mesh grille (ref #11: wall vent grilles): 5 x 4 checker
    for yy in range(9, 13):
        for xx in range(9, 14):
            t.set(xx, yy, AL1 if (xx + yy) % 2 == 0 else AL3)
    t.save("hull_door_bottom")


# ------------------------------------------------------------------ oxygen tank (cube_column: side + end)
def o2_tank():
    t = Tex()
    shade = [SALT1, SALT2, SALT2, SALT3, SALT3, SALT4, SALT4, SALT3, SALT3, SALT3, SALT3, SALT3, SALT2, SALT2, SALT1, SALT1]
    for x in range(16):
        t.rect(x, 0, x, 15, shade[x])            # cylinder shading across the width
    t.rect(0, 14, 15, 15, AL1)                   # dark base ring
    t.rect(0, 0, 15, 0, AL2)                     # dark top collar
    t.rect(0, 3, 15, 3, SALT1); t.rect(0, 12, 15, 12, SALT1)   # straps (ref #10: pale straps)
    t.rect(0, 4, 15, 4, SALT2); t.rect(0, 13, 15, 13, SALT2)
    # yellow-black hazard label: 6 x 4, diagonal stripes (shape carries the meaning)
    for y in range(6, 10):
        for x in range(2, 8):
            t.set(x, y, ORANGE if (x + y) % 3 else AL1)
    # green "oxygen" tag with a ring mark (no text in textures)
    t.rect(9, 6, 13, 9, GREEN)
    t.set(10, 7, SALT4); t.set(11, 7, SALT4); t.set(10, 8, SALT4); t.set(11, 8, SALT4)
    t.set(10, 7, GREEN); t.set(11, 8, GREEN)     # leaves a ring-like glyph, not a block
    t.save("o2_tank")


def o2_tank_top():
    t = Tex(); t.rect(0, 0, 15, 15, AL1)
    for y in range(16):
        for x in range(16):
            d = math.hypot(x - 7.5, y - 7.5)
            if d <= 7.2:
                t.set(x, y, AL2 if d > 6.2 else (SALT2 if d > 3 else SALT3))
    t.rect(7, 7, 8, 8, AL4); t.set(7, 7, AL1)    # valve stem
    t.rect(6, 6, 9, 6, AL4)
    t.save("o2_tank_top")


# ------------------------------------------------------------------ water reclaimer (cube_column)
def water_reclaimer():
    """ISS rack look (ref #8): off-white latched bay panels, blue handrail, dark cut-outs."""
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(0, 0, 15, 0, H_LIGHT); t.rect(2, 0, 2, 15, H_LIGHT)
    t.rect(0, 7, 15, 7, H_DARK)                  # seam between the two bays
    t.rect(0, 15, 15, 15, H_DARK)
    t.rect(15, 0, 15, 15, H_SHADE)
    t.rect(0, 0, 1, 15, BLUE)                    # blue handrail along the edge
    t.rect(0, 0, 0, 15, "#004C78")
    for (y) in (2, 10):                          # cut-out windows (dark slot) and latches
        t.rect(5, y, 10, y, H_DARK); t.rect(5, y + 1, 10, y + 1, H_SHADE)
    for (x, y) in ((13, 3), (13, 11)):
        t.rect(x, y, x, y + 1, AL3)              # latch
    t.rect(12, 5, 13, 5, ORANGE)                 # small tag
    t.save("water_reclaimer")


def water_reclaimer_top():
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(0, 0, 15, 0, H_LIGHT); t.rect(0, 0, 1, 15, BLUE)
    t.rect(15, 0, 15, 15, H_SHADE); t.rect(0, 15, 15, 15, H_DARK)
    t.rect(6, 6, 10, 9, H_SHADE); t.rect(6, 6, 10, 6, H_DARK)
    t.save("water_reclaimer_top")


def water_reclaimer_open():
    """Spare texture for an 'opened for repair' state (ref #9); needs a blockstate property, so it is a handoff."""
    t = Tex(); t.rect(0, 0, 15, 15, AL2)
    t.rect(1, 1, 14, 14, "#2E2724")
    t.rect(0, 0, 1, 15, BLUE)
    for yy in (3, 6, 9, 12):
        for xx in (4, 7, 10, 13):
            t.set(xx, yy, AL4)                   # round silver studs
    t.rect(11, 1, 13, 2, "#4B6E2E")              # green circuit card
    t.save("water_reclaimer_open")


# ------------------------------------------------------------------ oxygen maker (MOXIE-like)
# Model regions (existing uv in o2_generator.json): face/back region x2..13, y4..15 (12 x 12);
# end/side region x3..12, y4..15 (10 x 12); top region x2..13, y3..12 (12 x 10).
def pockets(t, x0, y0, w, h, cw, ch):
    """Grid of pocketed panels: 1 px dark groove, light top-left lip, shaded bottom-right."""
    t.rect(x0, y0, x0 + w - 1, y0 + h - 1, GOLD1)
    for cy in range(0, h, ch):
        for cx in range(0, w, cw):
            px, py = x0 + cx + 1, y0 + cy + 1
            pw, ph = cw - 1, ch - 1
            if px + pw > x0 + w or py + ph > y0 + h:
                continue
            t.rect(px, py, px + pw - 1, py + ph - 1, GOLD3)
            t.rect(px, py, px + pw - 1, py, GOLD4)
            t.rect(px, py, px, py + ph - 1, GOLD4)
            t.rect(px + pw - 1, py + 1, px + pw - 1, py + ph - 1, GOLD2)
            t.rect(px + 1, py + ph - 1, px + pw - 1, py + ph - 1, GOLD2)


def copper_strip(t, x0, x1, y):
    t.rect(x0, y, x1, y, COPPER); t.rect(x0, y + 1, x1, y + 1, COPPER_D)


def o2_generator_face():
    t = Tex(); t.rect(0, 0, 15, 15, GOLD1)
    pockets(t, 2, 4, 12, 10, 4, 5)
    copper_strip(t, 2, 13, 14)
    t.set(2, 4, AL4); t.set(13, 4, AL4)          # bolts in the frame corners
    t.save("o2_generator_face")


def o2_generator_back():
    t = Tex(); t.rect(0, 0, 15, 15, GOLD1)
    pockets(t, 2, 4, 12, 10, 4, 5)
    t.rect(5, 5, 8, 6, AL3); t.rect(5, 5, 8, 5, AL4)   # silver connector (ref #2)
    copper_strip(t, 2, 13, 14)
    t.save("o2_generator_back")


def o2_generator_end():
    """East end: diagonal X ribs (ref #1 left face)."""
    t = Tex(); t.rect(0, 0, 15, 15, GOLD1)
    x0, y0, n = 3, 4, 10
    t.rect(x0, y0, x0 + n - 1, y0 + n - 1, GOLD2)
    t.rect(x0, y0, x0 + n - 1, y0, GOLD3); t.rect(x0, y0, x0, y0 + n - 1, GOLD3)
    t.rect(x0 + n - 1, y0, x0 + n - 1, y0 + n - 1, GOLD3)
    for i in range(n):
        for (xx, yy) in ((x0 + i, y0 + i), (x0 + n - 1 - i, y0 + i)):
            t.set(xx, yy, GOLD4)
            t.set(min(xx + 1, x0 + n - 2), yy, GOLD3)
    for (x, y) in ((x0 + 1, y0 + 1), (x0 + n - 2, y0 + 1)):
        t.set(x, y, AL4)
    copper_strip(t, 3, 12, 14)
    t.save("o2_generator_end")


def o2_generator_side():
    t = Tex(); t.rect(0, 0, 15, 15, GOLD1)
    pockets(t, 3, 4, 10, 10, 5, 5)
    copper_strip(t, 3, 12, 14)
    t.save("o2_generator_side")


def o2_generator_top():
    t = Tex(); t.rect(0, 0, 15, 15, GOLD1)
    t.rect(2, 3, 13, 12, GOLD3)
    t.rect(2, 3, 13, 3, GOLD4); t.rect(2, 3, 2, 12, GOLD4)
    t.rect(13, 4, 13, 12, GOLD2); t.rect(3, 12, 13, 12, GOLD2)
    t.rect(8, 7, 11, 9, AL3); t.rect(8, 7, 11, 7, AL4)   # silver connector
    for (x, y) in ((4, 8), (5, 9), (4, 10)):             # dark Kapton-orange cable wrap dots (ref #2)
        t.set(x, y, COPPER_D)
    for (x, y) in ((3, 4), (12, 4), (3, 11), (12, 11)):
        t.set(x, y, AL4)
    t.save("o2_generator_top")


def o2_generator_tube():
    """Used only through two uv patches: (2,2) = tube pixel, (10..11, 2..3) = silver fitting."""
    t = Tex(); t.rect(0, 0, 15, 15, SALT3)
    t.set(2, 2, SALT3)
    t.rect(10, 2, 11, 3, AL4); t.set(11, 3, AL3); t.set(10, 3, AL3)
    t.save("o2_generator_tube")


# ------------------------------------------------------------------ models for existing and new blocks
def write(name, obj):
    with open(os.path.join(MOD, name + ".json"), "w") as f:
        json.dump(obj, f, indent=1)


def cube6(down, up, north, south, east, west, **extra):
    d = {"parent": "minecraft:block/cube",
         "textures": {"particle": north, "down": down, "up": up, "north": north, "south": south, "east": east, "west": west}}
    d.update(extra)
    return d


def box(x0, y0, z0, x1, y1, z1, tex, uvs=None):
    faces = {f: {"texture": tex} for f in ("north", "south", "east", "west", "up", "down")}
    return {"from": [x0, y0, z0], "to": [x1, y1, z1], "faces": faces}


def models():
    write("o2_tank", {"parent": "minecraft:block/cube_column", "textures": {"end": "mars:block/o2_tank_top", "side": "mars:block/o2_tank"}})
    write("water_reclaimer", {"parent": "minecraft:block/cube_column", "textures": {"end": "mars:block/water_reclaimer_top", "side": "mars:block/water_reclaimer"}})
    p, pb, pr, prt = "mars:block/hull_plate", "mars:block/hull_plate_base", "mars:block/hull_plate_rim", "mars:block/hull_plate_rim_top"
    write("hull_plate_base", cube6(p, p, pb, pb, pb, pb))
    write("hull_plate_rim", cube6(p, prt, pr, pr, pr, pr))
    write("hull_glass_ribbed", {"parent": "minecraft:block/cube_all", "render_type": "minecraft:translucent", "textures": {"all": "mars:block/hull_glass_ribbed"}})
    # roof vent: low louvred box (12 x 5 x 12 px), sits on the roof (y5), NOT a full cube
    write("roof_vent", {"parent": "block/block", "textures": {"particle": "mars:block/roof_vent_top", "top": "mars:block/roof_vent_top", "side": "mars:block/roof_vent_side"},
                        "elements": [
                            {"from": [2, 0, 2], "to": [14, 5, 14], "faces": {
                                "north": {"texture": "#side", "uv": [2, 2, 14, 7]}, "south": {"texture": "#side", "uv": [2, 2, 14, 7]},
                                "east": {"texture": "#side", "uv": [2, 2, 14, 7]}, "west": {"texture": "#side", "uv": [2, 2, 14, 7]},
                                "up": {"texture": "#top", "uv": [2, 2, 14, 14]}, "down": {"texture": "#top", "uv": [2, 2, 14, 14]}}},
                            {"from": [1, 0, 1], "to": [15, 1, 15], "faces": {f: {"texture": "#top"} for f in ("north", "south", "east", "west", "up", "down")}}]})
    # roof dish: mast + stepped bowl + feed horn (stepped, no rotated elements)
    t = "#d"
    els = []
    def el(a, b, uv=(0, 0, 16, 16)):
        els.append({"from": a, "to": b, "faces": {f: {"texture": t, "uv": list(uv)} for f in ("north", "south", "east", "west", "up", "down")}})
    el([5, 0, 5], [11, 1, 11])          # base plate
    el([7, 1, 7], [9, 8, 9])            # mast
    el([5, 8, 5], [11, 9, 11])          # bowl step 1
    el([3, 9, 3], [13, 10, 13])         # bowl step 2
    el([1, 10, 1], [15, 11, 15])        # bowl step 3 (rim)
    el([7, 11, 7], [9, 15, 9])          # feed horn
    el([6, 15, 6], [10, 16, 10])        # horn head
    write("roof_dish", {"parent": "block/block", "textures": {"particle": "mars:block/roof_dish", "d": "mars:block/roof_dish"}, "elements": els})


def roof_vent_textures():
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(0, 0, 15, 0, H_LIGHT); t.rect(15, 0, 15, 15, H_SHADE)
    for y in range(3, 13, 2):                    # louvres
        t.rect(2, y, 13, y, AL1); t.rect(2, y + 1, 13, y + 1, AL3)
    t.save("roof_vent_side")
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(2, 2, 13, 13, AL2)
    for y in range(3, 13, 2):
        t.rect(3, y, 12, y, AL1)
    t.rect(0, 0, 15, 0, H_LIGHT); t.rect(0, 0, 0, 15, H_LIGHT)
    t.save("roof_vent_top")


def roof_dish_texture():
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(0, 0, 15, 3, H_LIGHT); t.rect(0, 12, 15, 15, AL3)
    t.rect(0, 8, 15, 9, H_SHADE)
    t.save("roof_dish")


def blockstate_and_item(bid, model=None):
    with open(os.path.join(A, "blockstates", bid + ".json"), "w") as f:
        json.dump({"variants": {"": {"model": "mars:block/" + (model or bid)}}}, f, indent=1)
    with open(os.path.join(A, "models", "item", bid + ".json"), "w") as f:
        json.dump({"parent": "mars:block/" + (model or bid)}, f, indent=1)


def checks():
    for name, im in TEXS.items():
        if im.size != (16, 16):
            fails.append(f"{name}: not 16x16")
        cols = set()
        for y in range(16):
            for x in range(16):
                r, g, b, a = im.getpixel((x, y))
                if a == 0:
                    continue
                if (r, g, b) in ((0, 0, 0), (255, 255, 255)):
                    fails.append(f"{name}: pure black/white at {x},{y}")
                if (r, g, b) == (255, 0, 255):
                    fails.append(f"{name}: placeholder marker left")
                cols.add((r, g, b))
        glass = name in ("hull_glass", "hull_glass_ribbed")
        if not glass and any(im.getpixel((x, y))[3] != 255 for x in range(16) for y in range(16)):
            fails.append(f"{name}: has alpha but is not glass")
        print(f"{name}: {len(cols)} colours")


if __name__ == "__main__":
    for f in (hull_plate, hull_plate_base, hull_plate_rim, hull_plate_rim_top, hull_plate_cracked, hull_glass, hull_glass_ribbed,
              hull_door_top, hull_door_bottom, o2_tank, o2_tank_top, water_reclaimer, water_reclaimer_top, water_reclaimer_open,
              o2_generator_face, o2_generator_back, o2_generator_end, o2_generator_side, o2_generator_top, o2_generator_tube,
              roof_vent_textures, roof_dish_texture):
        f()
    models()
    for bid in ("hull_plate_base", "hull_plate_rim", "hull_glass_ribbed", "roof_vent", "roof_dish"):
        blockstate_and_item(bid)
    checks()
    if fails:
        print("\n".join("FAIL " + x for x in fails)); sys.exit(1)
    print("OK")
