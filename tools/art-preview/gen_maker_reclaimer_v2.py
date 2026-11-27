"""Oxygen maker v2 and water reclaimer v2 (2026-11-27). Owner: Lead Artist.
Reason: in game (dim warm light, 2 to 4 blocks) the maker read as a brown crate and the reclaimer as a clean
closed cube (Technical Lead report 2026-11-27 hull-lights). Refs: docs/art/references/habitat/PIA24201.jpg,
PIA24203.jpg (MOXIE), iss_wpa_rack_peake.jpg, iss_wrs_rack_open_fincke.jpg (ISS water racks).
Colours are read BY EYE from those photos and then brightened until the textures survive the game's dim
warm light (see docs/art/habitat-restyle-notes? no: see report 2026-11-27-artist-maker-reclaimer-v2.md).

Usage: python3 -I tools/art-preview/gen_maker_reclaimer_v2.py
Writes (same names as before, plus new ones): textures o2_generator_{face,back,end,side,top,tube,base,kapton},
water_reclaimer{,_top,_side,_hose}.png (water_reclaimer_open.png is left as it was), models o2_generator.json and
water_reclaimer.json. Exits non-zero on a failed check. Re-running gen_habitat_restyle.py no longer touches these."""
import json, os, sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_habitat_restyle as R  # Tex, palette, paths (importing does not write anything)

Tex, MOD = R.Tex, R.MOD
fails = R.fails

# ---- MOXIE gold ramp v2: brighter than style guide 2.2 (GOLD1..4), see report. Grooves stay dark. ----
G0, G1, G2, G3, G4 = "#2A1D0A", "#9C7226", "#D8AC40", "#F0CE66", "#FFEBA6"
COPPER_L, COPPER, COPPER_D = "#E09A5C", "#C47840", "#8A5030"
KAP, KAP_L, KAP_D = "#D98A1E", "#F2B450", "#6B3A12"   # Kapton amber, by eye from PIA24203
TUBE, TUBE_S = "#FAF7EE", "#C9CFD2"                    # PTFE tube white, shade
SIL4, SIL3, SIL2 = R.AL4, R.AL3, R.AL2


# ------------------------------------------------------------------ oxygen maker textures
BAND = {(0, 2), (0, 3), (1, 1), (1, 2), (2, 0), (2, 1)}   # pocket cells that carry the specular band (col, row)


def pockets_3x4(t, x0, y0, band=True):
    """Region 10 wide x 12 high: grooves at cols 0,3,6,9 and rows 0,3,6,9 (1 px, G0); pockets 2 x 2 px = 3 x 4 grid."""
    t.rect(x0, y0, x0 + 9, y0 + 11, G0)
    for r in range(4):
        for c in range(3):
            px, py = x0 + 1 + 3 * c, y0 + 1 + 3 * r
            on = band and (c, r) in BAND
            tl, tr, bl, br = (G4, G3, G3, G2) if on else (G3, G2, G2, G1)
            t.set(px, py, tl); t.set(px + 1, py, tr); t.set(px, py + 1, bl); t.set(px + 1, py + 1, br)


def base_rows(t):
    """Rows 0..3 are unused by the body; row 15 is the last pocket row, nothing below it."""


def o2_generator_face():
    t = Tex(); t.rect(0, 0, 15, 15, G0)
    pockets_3x4(t, 3, 4)
    t.save("o2_generator_face")


def o2_generator_back():
    t = Tex(); t.rect(0, 0, 15, 15, G0)
    pockets_3x4(t, 3, 4, band=False)
    t.rect(3, 4, 12, 4, G0)
    # status lamp of the maker is drawn by the game (block light); here only a silver data connector, ref PIA24203
    t.rect(4, 5, 7, 6, SIL3); t.rect(4, 5, 7, 5, SIL4); t.set(4, 6, SIL2)
    t.save("o2_generator_back")


def o2_generator_end():
    """East end: X ribs (ref PIA24201 left face): bright ribs in a darker recess."""
    t = Tex(); t.rect(0, 0, 15, 15, G0)
    x0, y0, w, h = 3, 4, 10, 12
    t.rect(x0 + 1, y0 + 1, x0 + w - 2, y0 + h - 2, G1)
    t.rect(x0, y0, x0 + w - 1, y0, G3); t.rect(x0, y0, x0, y0 + h - 1, G3); t.rect(x0 + w - 1, y0, x0 + w - 1, y0 + h - 1, G2)
    for i in range(h):
        xa = x0 + 1 + (i * (w - 3)) // (h - 1)
        xb = x0 + w - 2 - (i * (w - 3)) // (h - 1)
        for xx in (xa, xb):
            t.set(xx, y0 + i, G4 if i < 6 else G3)
            t.set(min(xx + 1, x0 + w - 2), y0 + i, G2)
    t.set(x0 + 1, y0 + 1, SIL4); t.set(x0 + w - 2, y0 + 1, SIL4)
    t.save("o2_generator_end")


def o2_generator_side():
    """West side (tube loop sits in front of it): same 3 x 4 grid, no band, a silver bolt column."""
    t = Tex(); t.rect(0, 0, 15, 15, G0)
    pockets_3x4(t, 3, 4, band=False)
    for y in (5, 11):
        t.set(3, y, SIL4)
    t.save("o2_generator_side")


def o2_generator_top():
    """Top face of the gold body (a 1 px gold rim shows around the Kapton plate)."""
    t = Tex(); t.rect(0, 0, 15, 15, G2)
    t.rect(3, 3, 12, 12, G2)
    t.rect(3, 3, 12, 3, G4); t.rect(3, 3, 3, 12, G3); t.rect(12, 3, 12, 12, G1); t.rect(3, 12, 12, 12, G1)
    t.save("o2_generator_top")


def o2_generator_kapton():
    """Kapton-wrapped top plate (ref PIA24203): amber film, darker cable wraps, silver bolts."""
    t = Tex(); t.rect(0, 0, 15, 15, KAP)
    t.rect(0, 0, 15, 0, KAP_L); t.rect(0, 0, 0, 15, KAP_L)
    t.rect(15, 0, 15, 15, KAP_D); t.rect(0, 15, 15, 15, KAP_D)
    for i in range(3, 13):                        # diagonal cable wrap, dark with a light edge
        t.set(i, 15 - i, KAP_D); t.set(i, 14 - i, KAP_D)
        t.set(i - 1, 15 - i, KAP_L) if 0 < i - 1 < 15 else None
    for (x, y) in ((2, 2), (13, 2), (2, 13), (13, 13)):
        t.set(x, y, SIL4)
    t.save("o2_generator_kapton")


def o2_generator_base():
    """Copper base plate. Rows 0..1 = the 2 px side edge (uv used by the model), rows 2..15 = top."""
    t = Tex(); t.rect(0, 0, 15, 15, COPPER)
    t.rect(0, 0, 15, 0, COPPER_L); t.rect(0, 1, 15, 1, COPPER_D)
    t.rect(0, 2, 15, 15, COPPER); t.rect(0, 2, 15, 2, COPPER_L); t.rect(0, 2, 0, 15, COPPER_L)
    t.rect(15, 2, 15, 15, COPPER_D); t.rect(0, 15, 15, 15, COPPER_D)
    for (x, y) in ((2, 4), (13, 4), (2, 13), (13, 13)):
        t.set(x, y, SIL4)
    t.save("o2_generator_base")


def o2_generator_tube():
    """uv patches: white tube (0..2, 0..2), silver fitting (10..12, 2..5)."""
    t = Tex(); t.rect(0, 0, 15, 15, TUBE)
    t.rect(0, 0, 1, 0, TUBE); t.rect(0, 1, 1, 1, TUBE_S)
    t.rect(10, 2, 11, 5, SIL3); t.rect(10, 2, 11, 2, SIL4); t.rect(10, 4, 11, 4, SIL2)   # nut ring in the middle
    t.rect(10, 3, 11, 3, SIL4)
    t.save("o2_generator_tube")


# ------------------------------------------------------------------ oxygen maker model
def el(a, b, faces):
    return {"from": a, "to": b, "faces": faces}


def uniform(tex, uv_side, uv_top):
    return {"north": {"texture": tex, "uv": uv_side}, "south": {"texture": tex, "uv": uv_side},
            "east": {"texture": tex, "uv": uv_side}, "west": {"texture": tex, "uv": uv_side},
            "up": {"texture": tex, "uv": uv_top}, "down": {"texture": tex, "uv": uv_top}}


def tube_box(a, b):
    return el(a, b, uniform("#tube", [0, 0, 2, 2], [0, 0, 2, 2]))


def fitting_box(a, b):
    return el(a, b, uniform("#tube", [10, 2, 12, 5], [10, 2, 12, 4]))


def maker_model():
    els = [
        # copper base plate 14 x 1 x 14 (hitbox in MarsMachines is 1..15 x 0..15 x 1..15)
        el([1, 0, 1], [15, 1, 15], {"north": {"texture": "#base", "uv": [1, 0, 15, 1]}, "south": {"texture": "#base", "uv": [1, 0, 15, 1]},
                                    "east": {"texture": "#base", "uv": [1, 0, 15, 1]}, "west": {"texture": "#base", "uv": [1, 0, 15, 1]},
                                    "up": {"texture": "#base", "uv": [1, 1, 15, 15]}, "down": {"texture": "#base", "uv": [1, 1, 15, 15]}}),
        # gold body 10 x 12 x 10 (v1 was 12 x 12 x 10): 3 x 4 pocket grid on north, south and west, X ribs on east
        el([3, 1, 3], [13, 13, 13], {"north": {"texture": "#front", "uv": [3, 4, 13, 16]}, "south": {"texture": "#back", "uv": [3, 4, 13, 16]},
                                     "east": {"texture": "#end", "uv": [3, 4, 13, 16]}, "west": {"texture": "#side", "uv": [3, 4, 13, 16]},
                                     "up": {"texture": "#top", "uv": [3, 3, 13, 13]}, "down": {"texture": "#top", "uv": [3, 3, 13, 13]}}),
        # Kapton plate on top, 8 x 1 x 8, and a silver connector on it
        el([4, 13, 4], [12, 14, 12], {"north": {"texture": "#kapton", "uv": [4, 14, 12, 15]}, "south": {"texture": "#kapton", "uv": [4, 14, 12, 15]},
                                      "east": {"texture": "#kapton", "uv": [4, 14, 12, 15]}, "west": {"texture": "#kapton", "uv": [4, 14, 12, 15]},
                                      "up": {"texture": "#kapton", "uv": [4, 4, 12, 12]}, "down": {"texture": "#kapton", "uv": [4, 4, 12, 12]}}),
        fitting_box([8, 14, 5], [11, 15, 8]),
        # west loop: 2 silver fittings, white tube up, across (arch), down
        fitting_box([1, 1, 4], [3, 4, 6]), fitting_box([1, 1, 10], [3, 4, 12]),
        tube_box([1, 4, 4], [3, 13, 6]), tube_box([1, 11, 6], [3, 13, 10]), tube_box([1, 4, 10], [3, 13, 12]),
        # east loop (smaller)
        fitting_box([13, 1, 5], [15, 4, 7]), fitting_box([13, 1, 9], [15, 4, 11]),
        tube_box([13, 4, 5], [15, 9, 7]), tube_box([13, 7, 7], [15, 9, 9]), tube_box([13, 4, 9], [15, 9, 11]),
    ]
    return {"parent": "block/block",
            "textures": {"particle": "mars:block/o2_generator_face", "front": "mars:block/o2_generator_face", "back": "mars:block/o2_generator_back",
                         "end": "mars:block/o2_generator_end", "side": "mars:block/o2_generator_side", "top": "mars:block/o2_generator_top",
                         "tube": "mars:block/o2_generator_tube", "base": "mars:block/o2_generator_base", "kapton": "mars:block/o2_generator_kapton"},
            "elements": els}


# ------------------------------------------------------------------ water reclaimer textures (ISS rack clutter)
H_DARK, H_SHADE, H_MID, H_LIGHT = R.H_DARK, R.H_SHADE, R.H_MID, R.H_LIGHT
OPEN_D, OPEN_L = "#2E2724", "#4A403A"
RED = "#C8322B"   # deeper red than Okabe-Ito vermillion to match the real label; the cross SHAPE carries the meaning, not the colour


def water_reclaimer():
    """North/south face: model uv region x2..14, y3..16 (12 x 13 px). Label with red cross, latch handles, open-panel strip."""
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(2, 3, 13, 15, H_MID)
    t.rect(2, 3, 13, 3, H_LIGHT); t.rect(2, 3, 2, 15, H_LIGHT); t.rect(13, 3, 13, 15, H_SHADE); t.rect(2, 15, 13, 15, H_DARK)
    t.rect(2, 9, 13, 9, H_DARK)                              # seam between the two bays
    # label panel (white card, dark border) with a red cross and two grey text lines
    t.rect(3, 4, 8, 8, "#F4F1EA"); t.rect(3, 4, 8, 4, H_DARK); t.rect(3, 8, 8, 8, H_DARK); t.rect(3, 4, 3, 8, H_DARK); t.rect(8, 4, 8, 8, H_DARK)
    for (x, y) in ((5, 5), (4, 6), (5, 6), (6, 6), (5, 7)):
        t.set(x, y, RED)
    t.set(7, 5, R.AL3); t.set(7, 7, R.AL3)
    # latch handles: two silver bars with a dark shadow underneath
    for x in (11, 12):
        pass
    t.rect(11, 4, 11, 6, R.AL4); t.rect(12, 4, 12, 6, R.AL3); t.rect(11, 7, 12, 7, H_DARK)
    # open-panel strip: dark, round silver studs, light lip (ref iss_wrs_rack_open_fincke)
    t.rect(3, 11, 12, 14, OPEN_D); t.rect(3, 11, 12, 11, OPEN_L)
    for x in (4, 7, 10):
        t.set(x, 12, R.AL4); t.set(x, 14, R.AL4)
    t.set(11, 13, "#4B6E2E")                                 # green circuit card corner
    t.save("water_reclaimer")


def water_reclaimer_side():
    """East/west face: closed rack bays with vertical latch handles and a yellow tag."""
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(2, 3, 13, 3, H_LIGHT); t.rect(2, 3, 2, 15, H_LIGHT); t.rect(13, 3, 13, 15, H_SHADE); t.rect(2, 15, 13, 15, H_DARK)
    t.rect(2, 9, 13, 9, H_DARK)
    for y in (5, 11):                                         # recessed cut-outs with latches
        t.rect(4, y, 9, y + 1, H_DARK); t.rect(4, y + 2, 9, y + 2, H_SHADE)
        t.rect(11, y, 11, y + 2, R.AL4); t.rect(12, y, 12, y + 2, R.AL3)
    t.rect(4, 4, 6, 4, R.ORANGE)                              # tag
    t.save("water_reclaimer_side")


def water_reclaimer_top():
    """uv: rows 0..1 = blue handrail band for the cap edge, row 2 = dark seam; top face uses region 1..15 x 1..15."""
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(0, 0, 15, 0, R.BLUE); t.rect(0, 1, 15, 1, "#004C78")
    t.rect(0, 2, 15, 15, H_MID); t.rect(0, 2, 15, 2, H_LIGHT)
    t.rect(15, 2, 15, 15, H_SHADE); t.rect(0, 15, 15, 15, H_DARK)
    t.rect(5, 6, 10, 9, H_SHADE); t.rect(5, 6, 10, 6, H_DARK)
    t.rect(11, 11, 12, 12, R.AL3); t.set(11, 11, R.AL4)        # vent cap
    t.save("water_reclaimer_top")


def water_reclaimer_hose():
    """Element patches: grey hose (0..2, 0..2), green braided hose (4..6, 0..2), dark hose clamp (8..9, 0..1)."""
    t = Tex(); t.rect(0, 0, 15, 15, R.AL3)
    t.rect(0, 0, 2, 0, R.AL4); t.rect(0, 1, 2, 1, R.AL3); t.rect(0, 2, 2, 2, R.AL2)
    t.rect(4, 0, 6, 2, "#4B6E2E"); t.rect(4, 0, 6, 0, "#7FA656"); t.set(5, 1, "#6A8F45"); t.rect(4, 2, 6, 2, "#33501F")
    t.rect(8, 0, 9, 1, R.AL1)
    t.save("water_reclaimer_hose")


def rec_model():
    side = {"north": {"texture": "#front", "uv": [2, 3, 14, 16]}, "south": {"texture": "#front", "uv": [2, 3, 14, 16]},
            "east": {"texture": "#side", "uv": [2, 3, 14, 16]}, "west": {"texture": "#side", "uv": [2, 3, 14, 16]},
            "up": {"texture": "#top", "uv": [2, 2, 14, 14]}, "down": {"texture": "#top", "uv": [2, 2, 14, 14]}}
    cap_edge = {k: {"texture": "#top", "uv": [1, 0, 15, 2]} for k in ("north", "south", "east", "west")}
    cap_edge.update({"up": {"texture": "#top", "uv": [1, 1, 15, 15]}, "down": {"texture": "#top", "uv": [1, 1, 15, 15]}})

    def hose(a, b, patch=(0, 0, 2, 2)):
        u = list(patch)
        return el(a, b, {k: {"texture": "#hose", "uv": u} for k in ("north", "south", "east", "west", "up", "down")})

    els = [
        el([2, 0, 2], [14, 13, 14], side),          # rack body 12 x 13 x 12 (hitbox 1..15 so hoses and cap stay inside it)
        el([1, 13, 1], [15, 15, 15], cap_edge),      # overhanging top shroud with the blue handrail edge
        # hose loop on the north face (right of the label): 4 x 6 px ring, 1 px standoff
        hose([10, 3, 1], [11, 9, 2]), hose([13, 3, 1], [14, 9, 2]), hose([10, 8, 1], [14, 9, 2]), hose([10, 3, 1], [14, 4, 2]),
        # and the same ring on the south face, so the block reads from both ends
        hose([5, 3, 14], [6, 9, 15]), hose([2, 3, 14], [3, 9, 15]), hose([2, 8, 14], [6, 9, 15]), hose([2, 3, 14], [6, 4, 15]),
        # vertical hose runs on the sides: grey on east, green braided on west
        hose([14, 1, 5], [15, 12, 6]), hose([1, 1, 9], [2, 12, 10], (4, 0, 6, 2)),
        # hose clamps
        hose([14, 5, 5], [15, 6, 6], (8, 0, 9, 1)), hose([14, 9, 5], [15, 10, 6], (8, 0, 9, 1)),
        hose([1, 5, 9], [2, 6, 10], (8, 0, 9, 1)), hose([1, 9, 9], [2, 10, 10], (8, 0, 9, 1)),
    ]
    return {"parent": "block/block",
            "textures": {"particle": "mars:block/water_reclaimer", "front": "mars:block/water_reclaimer", "side": "mars:block/water_reclaimer_side",
                         "top": "mars:block/water_reclaimer_top", "hose": "mars:block/water_reclaimer_hose"},
            "elements": els}


def check_model(m, name):
    for e in m["elements"]:
        for i in range(3):
            if not (1 <= e["from"][i] and e["to"][i] <= 15 or (i == 1 and e["from"][i] >= 0 and e["to"][i] <= 15)):
                fails.append(f"{name}: element {e['from']}-{e['to']} outside the 1..15 hitbox")
            if e["from"][i] >= e["to"][i]:
                fails.append(f"{name}: degenerate element {e['from']}-{e['to']}")


if __name__ == "__main__":
    for f in (o2_generator_face, o2_generator_back, o2_generator_end, o2_generator_side, o2_generator_top, o2_generator_kapton,
              o2_generator_base, o2_generator_tube, water_reclaimer, water_reclaimer_side, water_reclaimer_top, water_reclaimer_hose):
        f()
    mm, rm = maker_model(), rec_model()
    check_model(mm, "o2_generator"); check_model(rm, "water_reclaimer")
    R.write("o2_generator", mm); R.write("water_reclaimer", rm)
    R.checks()
    if fails:
        print("\n".join("FAIL " + x for x in fails)); sys.exit(1)
    print("OK")
