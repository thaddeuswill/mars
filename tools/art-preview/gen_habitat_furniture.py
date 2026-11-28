"""Habitat furniture (2026-11-27): crew bunk, stowage crate, optional work bench. Owner: Lead Artist.
Reason: the starting dome places the vanilla red bed (red wool, wooden frame), the vanilla barrel (brown wood) and the
vanilla crafting table (wood). Owner direction: make our own bunk and crate (bench is optional, the crafting table stays
until machine recipes exist). Assets only; the Technical Lead builds the blocks (report 2026-11-27-artist-habitat-furniture.md).

References actually looked at: docs/art/references/habitat/iss_wpa_rack_peake.jpg (closed off-white rack doors with
latches and cut-outs, white padded cargo bag with a strap and a white label, red cross label), quest_airlock_hatch_hurley.jpg
(white wall panels, mesh grilles, webbing strap, blue brackets, yellow-green seal frame), chapea_sandbox_360.jpg (white
vaulted wall; no bunk or locker visible), the in-game shot out/20261127-233344/lamp_1_shelter_day.png (the red bed that
clashes). NO photo of a crew sleeping quarter or sleeping bag was viewed: the bunk is DESIGN CHOICE, not a copy.
V2 (2026-11-28) redone from the SME photos (audit section 9): iss_cq_cristoforetti_sleeping_bag (light green quilted bag, white zip,
white Velcro patches, navy items), iss_sleeping_bag_tethered_nespoli (olive bag, navy bands, white cords), chapea_crew_quarters_rooms
(low single bed, dark frame, light quilted cover), mplm_leonardo_stowage_racks (grey lockers with yellow/orange label tags and
cut-outs, white bags with tan straps and buckles), iss_cargo_transfer_bag_ohara (yellow label card, white Velcro tabs).
Optional extras (NOT registered, no code): models/block/stowage_bag.json + textures stowage_bag_cloth, habitat_wall_velcro.
Dim-light rule: style guide 5.5b; bag luminance is computed in luminance_report().

Usage: python3 -I tools/art-preview/gen_habitat_furniture.py
Writes pack/assets/mars/textures/block/habitat_bunk_*.png, stowage_crate_*.png, habitat_bench_*.png;
models/block/habitat_bunk_{foot,head}.json, stowage_crate{,_open}.json, habitat_bench.json;
models/item/{habitat_bunk,stowage_crate,habitat_bench}.json; blockstates/{habitat_bunk,stowage_crate,habitat_bench}.json.
Exits non-zero if a check fails (16x16, no pure black/white, opaque, at most 6 colours per texture, no placeholder marker).
All textures are original drawings, not copies of any game's art."""
import json, os, sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_habitat_restyle as R

Tex, A, MOD, fails = R.Tex, R.A, R.MOD, R.fails
H_DARK, H_SHADE, H_MID, H_LIGHT = R.H_DARK, R.H_SHADE, R.H_MID, R.H_LIGHT
AL1, AL2, AL3, AL4 = R.AL1, R.AL2, R.AL3, R.AL4
SALT1, SALT2, SALT3, SALT4 = R.SALT1, R.SALT2, R.SALT3, R.SALT4
# sleeping bag: muted blue-grey, the Okabe-Ito blue family desaturated so it stays quiet beside the warm-white hull
BAG1, BAG2, BAG3, BAG4 = "#7F9C69", "#98B37F", "#B0C896", "#CBDDB4"  # V2 light green quilt (CQ photo), lifted for the dim lamp
NAVY1, NAVY3 = "#33406A", "#566796"  # navy band (Harmony bag bands, CQ navy items), lifted a little
TAN, TAN_L = "#B59B6A", "#CDB98F"  # tan strap / Velcro square (MPLM straps, CQ walls)
YEL = "#F2CF3A"  # label card yellow (CTB card, MPLM tags); the card + text lines are the shape channel
# safety orange family (style guide 2.1 accent #E69F00; dark = paper-ink warning #8A5A00)
OR1, OR3, OR4 = "#8A5A00", "#E69F00", "#F4BF52"
BLUE = "#0072B2"  # Okabe-Ito blue, label band (ISS blue handrails and brackets, ref iss_wpa_rack_peake / quest hatch)
FACES = ("north", "south", "east", "west", "up", "down")


# ------------------------------------------------------------------ bunk textures
def _hull_end(t, tag):
    """V2 plain alloy frame (CHAPEA low bed: dark frame, no drawers seen in any photo; Skylab: light tube frame).
    Rows 6..10 = the 5 px cap band (head/foot board), rows 11..15 = the 5 px shell side. Rows 0..5 unused (filled)."""
    t.rect(0, 0, 15, 15, AL3)
    t.rect(0, 6, 15, 6, AL4); t.rect(0, 10, 15, 10, AL2)
    t.rect(0, 6, 0, 10, AL2)
    if tag:                                              # head board: Velcro squares + a small pouch (CQ photo cue B5), no text
        t.rect(2, 7, 4, 8, TAN_L)
        t.rect(8, 7, 10, 9, YEL); t.rect(8, 7, 10, 7, AL4)
        t.set(13, 8, OR3)
    t.rect(0, 11, 15, 11, AL4)                           # shell side: light top edge, plain panel, dark toe-kick
    t.rect(15, 12, 15, 14, AL2)
    t.rect(0, 15, 15, 15, AL2)


def bunk_frame():
    t = Tex(); _hull_end(t, False); t.save("habitat_bunk_frame")


def bunk_end():
    t = Tex(); _hull_end(t, True); t.save("habitat_bunk_end")


def bunk_top():
    t = Tex(); t.rect(0, 0, 15, 15, AL3)
    t.rect(0, 0, 15, 0, AL4); t.rect(0, 0, 0, 15, AL4); t.rect(15, 0, 15, 15, AL2); t.rect(0, 15, 15, 15, AL2)
    t.rect(1, 8, 14, 8, AL2)
    t.save("habitat_bunk_top")


def bunk_bag():
    """V2 quilted light-green bag (CQ photo). Rows run across the bag (baffles). Top uses uv [1,0,15,14]; sides use [1,1,15,5].
    White zip down the middle, one white Velcro patch (as on the CQ bag)."""
    t = Tex(); t.rect(0, 0, 15, 15, BAG3)
    for y in range(16):
        if y % 4 == 0:
            t.rect(0, y, 15, y, BAG1)
        elif y % 4 == 1:
            t.rect(0, y, 15, y, BAG4)
    t.rect(1, 0, 1, 15, BAG2); t.rect(14, 0, 14, 15, BAG2)          # tucked edges
    t.rect(7, 0, 7, 15, SALT4)                                        # white zip tape
    for y in range(0, 16, 2):
        t.set(8, y, BAG1)                                             # zip teeth shadow
    t.rect(10, 6, 12, 7, SALT4)                                       # Velcro patch
    t.save("habitat_bunk_bag")


def bunk_strap():
    """V2 navy band with white stitching and a silver buckle (Harmony bag: navy bands; white cords). Top 14 x 2 (uv [1,0,15,2]);
    edge row 2; vertical side straps rows 4..8."""
    t = Tex(); t.rect(0, 0, 15, 15, NAVY3)
    t.rect(0, 3, 15, 3, NAVY1)
    for x in (3, 11):
        t.set(x, 1, SALT4)                                            # white stitches / cord ends
    t.rect(6, 0, 9, 0, AL4); t.rect(6, 1, 9, 1, AL3)                  # buckle
    t.rect(1, 4, 1, 8, NAVY1); t.rect(0, 4, 0, 8, SALT4)
    t.save("habitat_bunk_strap")


def bunk_pillow():
    """Top 10 x 6 (uv [0,0,10,6]); front/back band 10 x 2 (uv [0,6,10,8]); ends 6 x 2 (uv [10,6,16,8])."""
    t = Tex(); t.rect(0, 0, 15, 15, SALT3)
    t.rect(0, 0, 9, 5, SALT1); t.rect(1, 1, 8, 4, SALT3)
    t.rect(1, 1, 3, 1, SALT4); t.rect(1, 1, 1, 3, SALT4)               # light from top-left
    t.rect(4, 3, 7, 3, SALT2)                                          # stitch
    t.rect(0, 6, 9, 6, SALT4); t.rect(0, 7, 9, 7, SALT2)
    t.rect(10, 6, 15, 6, SALT3); t.rect(10, 7, 15, 7, SALT1)
    t.save("habitat_bunk_pillow")


# ------------------------------------------------------------------ crate textures
def crate_front():
    """V2 locker door (MPLM grey lockers: yellow/orange label tags, open cut-outs). Door region is uv [1,2,15,14] (14 x 12).
    Left half: yellow label card with text lines (no readable text). Right half: three vent cut-outs. Seam at x 7. Two grey latches."""
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(1, 2, 14, 13, H_MID)
    t.rect(1, 2, 14, 2, H_LIGHT); t.rect(1, 2, 1, 13, H_LIGHT); t.rect(14, 2, 14, 13, H_SHADE); t.rect(1, 13, 14, 13, H_SHADE)
    for (x, y) in ((2, 3), (13, 3), (2, 12), (13, 12)):
        t.set(x, y, H_DARK)                                            # bolts
    t.rect(7, 3, 7, 12, H_SHADE)                                       # door seam
    t.rect(3, 4, 6, 8, YEL)                # yellow label card
    t.rect(3, 6, 6, 6, H_DARK); t.rect(3, 8, 5, 8, H_DARK)             # stylised text lines (no real text)
    for y in (4, 6, 8):
        t.rect(9, y, 12, y, H_DARK)                                    # cut-out slots (dark), like the MPLM locker openings
    t.rect(5, 10, 6, 11, AL3); t.rect(8, 10, 9, 11, AL3); t.rect(5, 12, 9, 12, H_DARK)   # grey latches on a dark shadow line
    t.save("stowage_crate_front")


def crate_side():
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(0, 0, 15, 0, H_LIGHT); t.rect(0, 0, 0, 15, H_LIGHT); t.rect(15, 0, 15, 15, H_SHADE); t.rect(0, 15, 15, 15, H_SHADE)
    t.rect(4, 5, 11, 6, H_DARK); t.rect(4, 7, 11, 7, H_LIGHT)          # recessed grab handle slot with lit lower lip
    t.rect(1, 11, 14, 11, H_DARK)                                      # seam
    for (x, y) in ((2, 2), (13, 2), (2, 13), (13, 13)):
        t.set(x, y, H_DARK)
    t.save("stowage_crate_side")


def crate_top():
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(0, 0, 15, 0, H_LIGHT); t.rect(0, 0, 0, 15, H_LIGHT); t.rect(15, 0, 15, 15, H_SHADE); t.rect(0, 15, 15, 15, H_SHADE)
    for (x, y) in ((1, 1), (12, 1), (1, 12), (12, 12)):                # stacking corner fittings
        t.rect(x, y, x + 2, y + 2, AL3); t.rect(x, y, x + 2, y, AL4); t.rect(x, y, x, y + 2, AL4)
    t.rect(4, 7, 11, 8, H_DARK); t.rect(4, 9, 11, 9, H_LIGHT)          # lid seam
    t.save("stowage_crate_top")


def crate_skid():
    """V2: the skid is dropped. The same file is now a 1 px grey base rail (kick plate), sides use row 15 (uv [0,15,16,16]).
    Kept only because the model keeps the file name; it is a design choice, NOT an ISS cue (audit S7)."""
    t = Tex(); t.rect(0, 0, 15, 15, AL3)
    t.rect(0, 0, 15, 0, AL4); t.rect(0, 15, 15, 15, AL2)
    t.save("stowage_crate_skid")


def crate_inside():
    """Seen when the door is open, region uv [1,2,15,14]: dark cavity, a shelf, a white cargo bag with a strap."""
    t = Tex(); t.rect(0, 0, 15, 15, AL1)
    t.rect(1, 2, 14, 13, AL1); t.rect(1, 2, 14, 2, AL2); t.rect(1, 2, 1, 13, AL2)
    t.rect(1, 10, 14, 10, AL3); t.rect(1, 11, 14, 11, AL2)             # shelf
    t.rect(3, 5, 9, 9, H_LIGHT); t.rect(3, 5, 9, 5, H_MID); t.rect(9, 5, 9, 9, H_SHADE)   # white cargo bag
    t.rect(3, 7, 9, 7, AL3)                                            # strap
    t.rect(11, 6, 13, 9, AL2); t.rect(11, 6, 13, 6, AL3)               # second, smaller box
    t.save("stowage_crate_inside")


# ------------------------------------------------------------------ bench textures (optional)
def bench_top():
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(0, 0, 15, 0, H_LIGHT); t.rect(0, 0, 0, 15, H_LIGHT); t.rect(15, 0, 15, 15, H_SHADE); t.rect(0, 15, 15, 15, H_SHADE)
    t.rect(2, 2, 12, 12, AL2)                                          # work mat
    for k in (5, 9):
        t.rect(k, 2, k, 12, AL3); t.rect(2, k, 12, k, AL3)             # 3 x 3 grid of 3 px cells (the crafting grid)
    t.rect(13, 2, 13, 12, H_DARK)
    t.save("habitat_bench_top")


def bench_side():
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(0, 0, 15, 0, H_LIGHT); t.rect(0, 0, 0, 15, H_LIGHT); t.rect(15, 0, 15, 15, H_SHADE); t.rect(0, 15, 15, 15, H_SHADE)
    for y0 in (2, 9):
        t.rect(2, y0, 13, y0 + 5, H_SHADE); t.rect(2, y0, 13, y0, H_DARK); t.rect(2, y0, 2, y0 + 5, H_DARK)
        t.rect(6, y0 + 2, 9, y0 + 2, AL3)                              # drawer pull
    t.save("habitat_bench_side")


def bench_front():
    """Tool board: a perforated panel with a wrench and an orange-handled screwdriver on pegs."""
    t = Tex(); t.rect(0, 0, 15, 15, AL2)
    t.rect(0, 0, 15, 0, AL3); t.rect(0, 0, 0, 15, AL3); t.rect(15, 0, 15, 15, AL1); t.rect(0, 15, 15, 15, AL1)
    for y in range(2, 14, 3):
        for x in range(2, 14, 3):
            t.set(x, y, AL1)                                           # peg holes
    t.rect(3, 4, 3, 10, AL4); t.rect(4, 4, 4, 10, AL3); t.rect(2, 3, 5, 3, AL4)       # wrench: shaft and open jaw
    t.rect(8, 3, 8, 6, AL4); t.rect(8, 7, 9, 11, OR3)       # screwdriver
    t.save("habitat_bench_front")


# ------------------------------------------------------------------ optional variants (NOT registered, no code)
def bag_cloth():
    """OPTIONAL soft strapped stowage bag (MPLM photo: white bags, tan straps with buckles; CTB: yellow label card).
    One texture for every face. Straps at x 4..5 and 10..11 run over the top and down the sides; buckle at y 7..8; label card x 6..9."""
    t = Tex(); t.rect(0, 0, 15, 15, SALT3)
    t.rect(0, 15, 15, 15, SALT2); t.rect(15, 0, 15, 15, SALT2); t.rect(0, 0, 15, 0, SALT4); t.rect(0, 0, 0, 15, SALT4)
    for x0 in (3, 10):
        t.rect(x0, 0, x0 + 1, 15, TAN)
        t.rect(x0, 7, x0 + 1, 8, AL3)                                  # buckle
    t.rect(6, 3, 8, 5, YEL); t.rect(6, 4, 8, 4, SALT2)                 # yellow label card with a text line
    t.save("stowage_bag_cloth")


def wall_velcro():
    """OPTIONAL wall panel variant (CQ photo: off-white quilted wall, beige Velcro squares, a few personal items)."""
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    for k in (0, 8):
        t.rect(0, k, 15, k, H_SHADE); t.rect(k, 0, k, 15, H_SHADE)     # 8 px quilt grid
    t.rect(0, 1, 15, 1, H_LIGHT)
    for (x, y) in ((2, 3), (11, 3), (11, 11)):
        t.rect(x, y, x + 2, y + 2, TAN_L)                              # Velcro squares
    t.rect(3, 10, 5, 12, AL2); t.rect(3, 10, 5, 10, H_DARK)            # a tablet / pouch stuck on
    t.save("habitat_wall_velcro")


def bag_model():
    """Two stacked strapped bags, one block. Lower 16 x 7 x 14, upper 14 x 6 x 12. All faces use stowage_bag_cloth."""
    def box(a, b):
        return {"from": a, "to": b, "shade": False, "faces": {k: {"texture": "#cloth", "uv": [0, 0, 16, 16]} for k in FACES}}
    return {"parent": "minecraft:block/block", "textures": {"particle": "mars:block/stowage_bag_cloth", "cloth": "mars:block/stowage_bag_cloth"},
            "elements": [box([0, 0, 1], [16, 7, 15]), box([1, 7, 2], [15, 13, 14])]}


# ------------------------------------------------------------------ models
def el(a, b, faces):
    return {"from": list(a), "to": list(b), "faces": {k: v for k, v in faces.items()}}


def f(tex, uv):
    return {"texture": "#" + tex, "uv": list(uv)}


def bunk_part(head):
    """Authored for facing=north: the head board is at the north end (z 0..2) of the HEAD block; the FOOT block has the foot
    board at its south end (z 14..16). In BedBlock the head block is the neighbour in the FACING direction of the foot block.
    Heights (px): shell 0..5, bag 5..9 (top = 9 px, same as vanilla's mattress so the sleeping pose matches), boards 5..10, pillow 9..11."""
    shell = el([0, 0, 0], [16, 5, 16], {
        ("north" if head else "south"): f("end", [0, 11, 16, 16]),
        "east": f("frame", [0, 11, 16, 16]), "west": f("frame", [0, 11, 16, 16]),
        "up": f("top", [0, 0, 16, 16]), "down": f("frame", [0, 11, 16, 16])})
    z0, z1 = (2, 16) if head else (0, 14)
    bag = el([1, 5, z0], [15, 9, z1], {
        "up": f("bag", [1, 0, 15, 14]), "east": f("bag", [1, 1, 15, 5]), "west": f("bag", [1, 1, 15, 5])})
    bag["shade"] = False                                       # style guide 5.5b
    if head:
        board = el([0, 5, 0], [16, 10, 2], {
            "north": f("end", [0, 6, 16, 11]), "south": f("frame", [0, 6, 16, 11]),
            "east": f("frame", [0, 6, 2, 11]), "west": f("frame", [0, 6, 2, 11]), "up": f("top", [0, 0, 16, 2])})
        pillow = el([3, 9, 2], [13, 11, 8], {
            "up": f("pillow", [0, 0, 10, 6]), "north": f("pillow", [0, 6, 10, 8]), "south": f("pillow", [0, 6, 10, 8]),
            "east": f("pillow", [10, 6, 16, 8]), "west": f("pillow", [10, 6, 16, 8])})
        els = [shell, bag, board, pillow]
    else:
        board = el([0, 5, 14], [16, 10, 16], {
            "south": f("end", [0, 6, 16, 11]), "north": f("frame", [0, 6, 16, 11]),
            "east": f("frame", [14, 6, 16, 11]), "west": f("frame", [14, 6, 16, 11]), "up": f("top", [0, 14, 16, 16])})
        strap = el([1, 9, 6], [15, 10, 8], {
            "up": f("strap", [1, 0, 15, 2]), "north": f("strap", [1, 2, 15, 3]), "south": f("strap", [1, 2, 15, 3]),
            "east": f("strap", [0, 2, 2, 3]), "west": f("strap", [0, 2, 2, 3])})
        wl = el([0, 5, 6], [1, 10, 8], {"west": f("strap", [0, 4, 2, 9]), "north": f("strap", [0, 4, 1, 9]),
                                       "south": f("strap", [0, 4, 1, 9]), "up": f("strap", [0, 2, 1, 4])})
        er = el([15, 5, 6], [16, 10, 8], {"east": f("strap", [0, 4, 2, 9]), "north": f("strap", [0, 4, 1, 9]),
                                         "south": f("strap", [0, 4, 1, 9]), "up": f("strap", [0, 2, 1, 4])})
        els = [shell, bag, board, strap, wl, er]
    tex = {"particle": "mars:block/habitat_bunk_frame", "frame": "mars:block/habitat_bunk_frame", "end": "mars:block/habitat_bunk_end",
           "top": "mars:block/habitat_bunk_top", "bag": "mars:block/habitat_bunk_bag"}
    tex["pillow" if head else "strap"] = "mars:block/habitat_bunk_" + ("pillow" if head else "strap")
    return {"parent": "minecraft:block/block", "textures": tex, "elements": els}


def bunk_item():
    """Both parts in one model for the inventory: head part at z 0..16, foot part shifted to z 16..32. Display is UNVERIFIED in game."""
    h, ft = bunk_part(True), bunk_part(False)
    els = list(h["elements"])
    for e in ft["elements"]:
        e2 = json.loads(json.dumps(e)); e2["from"][2] += 16; e2["to"][2] += 16
        els.append(e2)
    tex = dict(h["textures"]); tex.update(ft["textures"])
    # bunk shows its long axis left to right: rotate the whole thing 90 degrees in the GUI so it fits the slot
    disp = {"gui": {"rotation": [30, 135, 0], "translation": [0, 0, 0], "scale": [0.32, 0.32, 0.32]},
            "ground": {"rotation": [0, 0, 0], "translation": [0, 2, 0], "scale": [0.2, 0.2, 0.2]},
            "fixed": {"rotation": [0, 90, 0], "translation": [0, 0, 0], "scale": [0.4, 0.4, 0.4]},
            "thirdperson_righthand": {"rotation": [0, 0, 0], "translation": [0, 3, 1], "scale": [0.25, 0.25, 0.25]},
            "firstperson_righthand": {"rotation": [0, 135, 0], "translation": [0, 0, 0], "scale": [0.25, 0.25, 0.25]}}
    return {"parent": "minecraft:block/block", "textures": tex, "elements": els, "display": disp}


def crate_model(open_):
    sk = el([0, 0, 0], [16, 1, 16], {k: f("skid", [0, 15, 16, 16]) for k in ("north", "south", "east", "west")})
    sk["faces"]["down"] = f("skid", [0, 0, 16, 16])
    sk["faces"]["up"] = f("skid", [0, 0, 16, 16])
    body = el([0, 1, 1], [16, 16, 16], {
        "north": f("side", [0, 0, 16, 15]), "south": f("side", [0, 0, 16, 15]),
        "east": f("side", [0, 0, 16, 15]), "west": f("side", [0, 0, 16, 15]), "up": f("top", [0, 0, 16, 16])})
    door = el([1, 2, 0], [15, 14, 1], {k: f("front", [1, 2, 15, 14]) for k in ("north",)})
    door["faces"].update({k: f("side", [0, 0, 14, 1]) for k in ("east", "west", "up", "down", "south")})
    tex = {"particle": "mars:block/stowage_crate_side", "side": "mars:block/stowage_crate_side", "top": "mars:block/stowage_crate_top",
           "front": "mars:block/stowage_crate_front", "skid": "mars:block/stowage_crate_skid"}
    door["shade"] = False                                      # style guide 5.5b: bright face in dim light
    els = [sk, body]
    if open_:
        cav = el([1, 2, 0.9], [15, 14, 1], {"north": f("inside", [1, 2, 15, 14])})
        door["rotation"] = {"origin": [1, 8, 0.5], "axis": "y", "angle": 45}
        els += [cav, door]
        tex["inside"] = "mars:block/stowage_crate_inside"
    else:
        els.append(door)
    return {"parent": "minecraft:block/block", "textures": tex, "elements": els}


def bench_model():
    return {"parent": "minecraft:block/cube", "textures": {
        "particle": "mars:block/habitat_bench_side", "down": "mars:block/habitat_bench_side", "up": "mars:block/habitat_bench_top",
        "north": "mars:block/habitat_bench_front", "south": "mars:block/habitat_bench_front",
        "east": "mars:block/habitat_bench_side", "west": "mars:block/habitat_bench_side"}}


def jwrite(path, obj):
    with open(path, "w") as fh:
        json.dump(obj, fh, indent=1)


def blockstates():
    rot = {"north": 0, "east": 90, "south": 180, "west": 270}
    bunk = {}
    for fc, y in rot.items():                                   # vanilla BedBlock state order: FACING, OCCUPIED, PART (alphabetical)
        for occ in ("false", "true"):
            for part in ("foot", "head"):
                v = {"model": "mars:block/habitat_bunk_" + part}
                if y:
                    v["y"] = y
                bunk[f"facing={fc},occupied={occ},part={part}"] = v
    jwrite(os.path.join(A, "blockstates", "habitat_bunk.json"), {"variants": bunk})
    # crate: authored with the FRONT on the model's north face. facing=up/down tip it (x 270 / x 90), like BarrelBlock's 6 facings.
    crate = {}
    for fc, (x, y) in {"north": (0, 0), "east": (0, 90), "south": (0, 180), "west": (0, 270), "up": (270, 0), "down": (90, 0)}.items():
        for op in ("false", "true"):
            v = {"model": "mars:block/stowage_crate" + ("_open" if op == "true" else "")}
            if x:
                v["x"] = x
            if y:
                v["y"] = y
            crate[f"facing={fc},open={op}"] = v
    jwrite(os.path.join(A, "blockstates", "stowage_crate.json"), {"variants": crate})
    jwrite(os.path.join(A, "blockstates", "habitat_bench.json"), {"variants": {"": {"model": "mars:block/habitat_bench"}}})


def checks():
    R.checks()
    for name, im in R.TEXS.items():
        if name.startswith(("habitat_bunk", "stowage_crate", "habitat_bench", "stowage_bag", "habitat_wall")):
            cols = {im.getpixel((x, y))[:3] for y in range(16) for x in range(16)}
            if len(cols) > 6:
                fails.append(f"{name}: {len(cols)} colours (max 6 per texture)")
    for n in ("habitat_bunk_foot", "habitat_bunk_head", "stowage_crate", "stowage_crate_open", "habitat_bench"):
        m = json.load(open(os.path.join(MOD, n + ".json")))
        for e in m.get("elements", []):
            for k, fd in e["faces"].items():
                t = fd["texture"].lstrip("#")
                if t not in m["textures"]:
                    fails.append(f"{n}: texture variable {t} missing")
                elif not os.path.exists(os.path.join(R.TEX, m["textures"][t].split("/")[-1] + ".png")):
                    fails.append(f"{n}: texture file {m['textures'][t]} missing")
    # hitbox rule for the bunk: nothing taller than 11 px, collision shape is the 9 px mattress box (handoff)
    for n in ("habitat_bunk_foot", "habitat_bunk_head"):
        m = json.load(open(os.path.join(MOD, n + ".json")))
        top = max(e["to"][1] for e in m["elements"])
        if top > 11:
            fails.append(f"{n}: taller than 11 px")


def lum(c):
    return 0.2126 * c[0] + 0.7152 * c[1] + 0.0722 * c[2]


def luminance_report(dim=(0.4375, 0.396, 0.3375)):
    """Mean luminance (Rec. 709, 8-bit) of the surface the player sees, dimmed by the measured lamp factor (board 12 / style 5.5b),
    against the hull wall (mean of hull_plate). Surfaces: bag top uv [1,0,15,14]; crate door uv [1,2,15,14]; wall = hull_plate."""
    from PIL import Image
    def mean(name, box):
        im = R.TEXS[name] if name in R.TEXS else Image.open(os.path.join(R.TEX, name + ".png")).convert("RGBA")
        px = [im.getpixel((x, y))[:3] for y in range(box[1], box[3]) for x in range(box[0], box[2])]
        n = len(px); m = tuple(sum(p[i] for p in px) / n for i in range(3))
        return lum(m), lum(tuple(m[i] * dim[i] for i in range(3)))
    wall = mean("hull_plate", (0, 0, 16, 16))
    out = {"wall": wall}
    for k, (n, b) in {"bag top": ("habitat_bunk_bag", (1, 0, 15, 14)), "crate door": ("stowage_crate_front", (1, 2, 15, 14)),
                      "soft bag": ("stowage_bag_cloth", (0, 0, 16, 16)), "OLD blue bag": (None, None)}.items():
        if n:
            out[k] = mean(n, b)
    for k, v in out.items():
        print(f"LUM {k}: texture {v[0]:.1f}, dim {v[1]:.1f}, ratio to wall (dim) {v[1] / out['wall'][1]:.2f}")
    return out


if __name__ == "__main__":
    for fn in (bunk_frame, bunk_end, bunk_top, bunk_bag, bunk_strap, bunk_pillow,
               crate_front, crate_side, crate_top, crate_skid, crate_inside, bench_top, bench_side, bench_front,
               bag_cloth, wall_velcro):
        fn()
    jwrite(os.path.join(MOD, "habitat_bunk_foot.json"), bunk_part(False))
    jwrite(os.path.join(MOD, "habitat_bunk_head.json"), bunk_part(True))
    jwrite(os.path.join(MOD, "stowage_crate.json"), crate_model(False))
    jwrite(os.path.join(MOD, "stowage_crate_open.json"), crate_model(True))
    jwrite(os.path.join(MOD, "habitat_bench.json"), bench_model())
    jwrite(os.path.join(A, "models", "item", "habitat_bunk.json"), bunk_item())
    jwrite(os.path.join(A, "models", "item", "stowage_crate.json"), {"parent": "mars:block/stowage_crate"})
    jwrite(os.path.join(A, "models", "item", "habitat_bench.json"), {"parent": "mars:block/habitat_bench"})
    jwrite(os.path.join(MOD, "stowage_bag.json"), bag_model())
    blockstates()
    checks()
    if fails:
        print("\n".join("FAIL " + x for x in fails)); sys.exit(1)
    luminance_report()
    print("OK")
