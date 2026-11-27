"""Oxygen maker v3 and water reclaimer v3 (2026-11-27). Owner: Lead Artist.
Reason (Technical Lead in-game check, out/20261127-233726): at 2 to 4 blocks the v2 maker read as a dark lattice with
amber windows (grooves were 1/3 of the face, gold only about 33 % of lamp light) and the reclaimer as a labelled box.
Measured in that shot: maker face texture mean luminance 93 -> 30 on screen (factor 0.33; per channel R 0.35, G 0.32,
B 0.27), hull wall 98. So v3 (a) makes the pockets 4 x 3 px with 1 px grooves of #5A3F12 (not #2A1D0A), pocket colours at the
top of the palette, (b) sets "shade": false on every maker element (no directional face shade, x1.25 on the north/south
faces, x1.67 on east/west), (c) adds a front tube loop, (d) makes the reclaimer one-sided (everything on north).
Usage: python3 -I tools/art-preview/gen_maker_reclaimer_v3.py   (imports v2 helpers; v2 main is not run)."""
import os, sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import gen_maker_reclaimer_v2 as V
R, Tex, el, uniform, fails = V.R, V.Tex, V.el, V.uniform, V.fails

GROOVE = "#70511A"                       # lighter than v2 #2A1D0A, still clearly the darkest thing on the face
P_HI, P_BASE, P_LO, P_EDGE = "#FFFBD8", "#FFF2B4", "#F8DC7C", "#F2CE5E"
RECESS = "#E0B84C"
SIL4, SIL3, SIL2 = V.SIL4, V.SIL3, V.SIL2
BAND = {(0, 1), (1, 0), (1, 1)}          # pockets carrying the highlight (col, row) on the 2 x 3 grid


def panels(t, x0=3, y0=4, band=True):
    """Region 10 x 12: pockets 4 x 3 px, 2 cols x 3 rows; groove 1 px between; right column and bottom row are lit frame."""
    t.rect(x0, y0, x0 + 9, y0 + 11, P_EDGE)
    for r in range(3):
        for c in range(2):
            px, py = x0 + 5 * c, y0 + 4 * r
            col = P_HI if (band and (c, r) in BAND) else P_BASE
            t.rect(px, py, px + 3, py + 2, col)
            t.rect(px, py + 2, px + 3, py + 2, P_LO)       # lower lip
            t.rect(px + 3, py, px + 3, py + 2, P_LO)        # right lip
            if (c, r) in BAND and band:
                t.set(px, py, "#FFFFEC")                    # specular glint
    for r in (0, 1):                                       # horizontal grooves at rows 3 and 7
        t.rect(x0, y0 + 3 + 4 * r, x0 + 8, y0 + 3 + 4 * r, GROOVE)
    t.rect(x0 + 4, y0, x0 + 4, y0 + 10, GROOVE)            # vertical groove
    t.rect(x0 + 9, y0, x0 + 9, y0 + 11, P_LO)             # lit right frame column
    t.rect(x0, y0 + 11, x0 + 9, y0 + 11, P_EDGE)           # bottom flange


def face():
    t = Tex(); t.rect(0, 0, 15, 15, GROOVE); panels(t); t.save("o2_generator_face")


def back():
    t = Tex(); t.rect(0, 0, 15, 15, GROOVE); panels(t, band=False)
    t.rect(4, 5, 7, 6, SIL3); t.rect(4, 5, 7, 5, SIL4); t.set(4, 6, SIL2)      # data connector (ref PIA24203)
    t.save("o2_generator_back")


def side():
    t = Tex(); t.rect(0, 0, 15, 15, GROOVE); panels(t, band=False)
    for y in (5, 12):
        t.set(3, y, "#FFFFEC")
    t.save("o2_generator_side")


def end():
    """East end: X ribs (ref PIA24201 left face): bright ribs in a gold recess, 1 px dark outline only."""
    t = Tex(); t.rect(0, 0, 15, 15, GROOVE)
    x0, y0, w, h = 3, 4, 10, 12
    t.rect(x0, y0, x0 + w - 1, y0 + h - 1, RECESS)
    t.rect(x0, y0, x0 + w - 1, y0, P_HI); t.rect(x0, y0, x0, y0 + h - 1, P_HI)
    t.rect(x0 + w - 1, y0, x0 + w - 1, y0 + h - 1, P_EDGE)
    for i in range(h):
        xa = x0 + 1 + (i * (w - 3)) // (h - 1)
        xb = x0 + w - 2 - (i * (w - 3)) // (h - 1)
        for xx in (xa, xb):
            t.set(xx, y0 + i, P_BASE); t.set(min(xx + 1, x0 + w - 2), y0 + i, P_HI if i < 6 else P_BASE)
            if xx - 1 > x0:
                t.set(xx - 1, y0 + i, GROOVE)                 # shadow side of the rib
    t.set(x0 + 1, y0 + 1, "#FFFFEC"); t.set(x0 + w - 2, y0 + 1, "#FFFFEC")
    t.save("o2_generator_end")


def top():
    t = Tex(); t.rect(0, 0, 15, 15, P_BASE)
    t.rect(3, 3, 12, 3, P_HI); t.rect(3, 3, 3, 12, P_HI); t.rect(12, 3, 12, 12, P_LO); t.rect(3, 12, 12, 12, P_LO)
    t.save("o2_generator_top")


def tube():
    """Same uv patches as v2: white tube (0..2, 0..2), silver fitting (10..12, 2..5)."""
    V.o2_generator_tube()


def maker_model():
    m = V.maker_model()
    base = m["elements"][:4]                                  # copper base, gold body, Kapton plate, connector fitting
    west = m["elements"][4:]
    # front tube loop standing 2 px off the north face (z 1..3), bottom-right (as the front-right loop in PIA24201)
    front = [V.fitting_box([9, 1, 1], [11, 4, 3]), V.fitting_box([13, 1, 1], [15, 4, 3]),
             V.tube_box([9, 4, 1], [11, 8, 3]), V.tube_box([13, 4, 1], [15, 8, 3]), V.tube_box([9, 7, 1], [15, 9, 3])]
    els = base + west + front
    for e in els:
        e["shade"] = False
    m["elements"] = els
    return m


# ------------------------------------------------------------------ reclaimer: everything on the north face
H_MID, H_LIGHT, H_SHADE, H_DARK = V.H_MID, V.H_LIGHT, V.H_SHADE, V.H_DARK
RED = V.RED


def rec_front():
    """North face, model uv region x2..14, y3..16. Card 8 x 8 with a 6 x 6 red cross (arms 2 px), strip with latches below."""
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(2, 3, 13, 3, H_LIGHT); t.rect(2, 3, 2, 15, H_LIGHT); t.rect(13, 3, 13, 15, H_SHADE); t.rect(2, 15, 13, 15, H_DARK)
    t.rect(3, 4, 10, 11, "#F4F1EA"); t.rect(3, 4, 10, 4, H_DARK); t.rect(3, 11, 10, 11, H_DARK); t.rect(3, 4, 3, 11, H_DARK); t.rect(10, 4, 10, 11, H_DARK)
    t.rect(6, 5, 7, 10, RED); t.rect(4, 7, 9, 8, RED)                    # the cross (shape, not colour, carries the meaning)
    t.rect(11, 4, 11, 9, R.AL4); t.rect(12, 4, 12, 9, R.AL3); t.rect(11, 10, 12, 10, H_DARK)   # latch handle right of the card
    t.rect(3, 12, 12, 14, V.OPEN_D); t.rect(3, 12, 12, 12, V.OPEN_L)    # open strip
    for x in (5, 8, 10):
        t.set(x, 13, R.AL4)
    t.rect(3, 13, 3, 14, R.AL4); t.set(11, 14, "#4B6E2E"); t.set(12, 14, "#7FA656")
    t.save("water_reclaimer")


def rec_plain():
    """South, east, west: plain panel, one seam, four corner bolts. Same uv region."""
    t = Tex(); t.rect(0, 0, 15, 15, H_MID)
    t.rect(2, 3, 13, 3, H_LIGHT); t.rect(2, 3, 2, 15, H_LIGHT); t.rect(13, 3, 13, 15, H_SHADE); t.rect(2, 15, 13, 15, H_DARK)
    t.rect(2, 9, 13, 9, H_DARK)
    for (x, y) in ((4, 5), (11, 5), (4, 12), (11, 12)):
        t.set(x, y, R.AL4)
    t.save("water_reclaimer_side")


def rec_model():
    m = V.rec_model()
    side = {"north": {"texture": "#front", "uv": [2, 3, 14, 16]}, "south": {"texture": "#side", "uv": [2, 3, 14, 16]},
            "east": {"texture": "#side", "uv": [2, 3, 14, 16]}, "west": {"texture": "#side", "uv": [2, 3, 14, 16]},
            "up": {"texture": "#top", "uv": [2, 2, 14, 14]}, "down": {"texture": "#top", "uv": [2, 2, 14, 14]}}
    cap = m["elements"][1]

    def hose(a, b):
        return el(a, b, {k: {"texture": "#hose", "uv": [0, 0, 2, 2]} for k in ("north", "south", "east", "west", "up", "down")})
    els = [el([2, 0, 2], [14, 13, 14], side), cap,
           # hose loop on the face side only: a ring right of the card, 1 px standoff, plus a drop to the strip
           hose([11, 2, 1], [12, 9, 2]), hose([13, 2, 1], [14, 9, 2]), hose([11, 8, 1], [14, 9, 2]), hose([11, 2, 1], [14, 3, 2]),
           hose([12, 0, 1], [13, 2, 2])]
    m["elements"] = els
    return m


if __name__ == "__main__":
    for f in (face, back, side, end, top, rec_front, rec_plain):
        f()
    mm, rm = maker_model(), rec_model()
    V.check_model(mm, "o2_generator"); V.check_model(rm, "water_reclaimer")
    R.write("o2_generator", mm); R.write("water_reclaimer", rm)
    R.checks()
    if fails:
        print("\n".join("FAIL " + x for x in fails)); sys.exit(1)
    print("OK")
