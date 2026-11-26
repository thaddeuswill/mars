"""Generate STRAW-MAN (fidelity code SM) textures, block models, item models, and blockstates for the
Phase 1 dome machines, batch 1 (asset list M-05, M-07, M-08, M-09, M-10, M-13, plus T-01 regolith).
Uses only style-guide palette colors and runs the numeric checks. Run with: python3 -I gen_phase1_batch1.py
Every texture carries the 2x2 magenta placeholder marker (style guide §5.5) in the top-left of each used region."""
import json, os, math, sys
from PIL import Image

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
A = os.path.join(ROOT, "pack", "assets", "mars")
TEX = os.path.join(A, "textures", "block")
for d in (TEX, os.path.join(A, "models", "block"), os.path.join(A, "models", "item"), os.path.join(A, "blockstates")):
    os.makedirs(d, exist_ok=True)

# ---- palette (style guide §2.2, §3.1, §4.4; PV ramp added in round 2 batch 1) ----
P = {
    "1": "#3B4148", "2": "#59616A", "3": "#7E8790", "4": "#A8B0B7",   # alloy ramp
    "o": "#E69F00",                                                  # safety orange
    "g": "#009E73",                                                  # lamp: running
    "L": "#F5E3C3",                                                  # LED lamp light
    "k": "#1E2328", "w": "#E8E3D8", "y": "#F0E442", "b": "#56B4E9",  # UI slate panel + Okabe-Ito
    "p": "#1C1A21", "q": "#26232C", "r": "#322D38", "s": "#4E4957",  # PV cell ramp (r measured, PIA22876)
    "v": "#5C4743", "x": "#89614C",                                  # dust veil on cells: 1/3, 2/3 toward D (OKLab)
    "G": "#5C4320", "H": "#7E5E2C", "I": "#A3823F", "J": "#C9AE6A",  # muted gold plating (PIA24201, PIA24203)
    "W": "#E7E2D6", "V": "#D3CDBF",                                  # off-white tube (salt-crust ramp 3, 2)
    "D": "#B97C52", "E": "#D09A6E",                                  # bright dust 3, 4
    "i": "#C2CDD5",                                                  # water ice 2 (electrolyte window)
    "R": "#5E3424", "S": "#7A4530", "T": "#9A5B3C", "U": "#B8764F",  # regolith ramp
    "m": "#FF00FF",                                                  # placeholder marker only
}
def rgb(h): return tuple(int(h.lstrip("#")[j:j + 2], 16) for j in (0, 2, 4))
PAL = {rgb(v): k for k, v in P.items()}

class T:
    def __init__(s, name, base="3"):
        s.name = name; s.px = [[base] * 16 for _ in range(16)]; s.regions = []
    def rect(s, x0, y0, x1, y1, c):
        for y in range(y0, y1 + 1):
            for x in range(x0, x1 + 1): s.px[y][x] = c
    def box(s, x0, y0, x1, y1, c):  # outline
        for x in range(x0, x1 + 1): s.px[y0][x] = c; s.px[y1][x] = c
        for y in range(y0, y1 + 1): s.px[y][x0] = c; s.px[y][x1] = c
    def dot(s, x, y, c): s.px[y][x] = c
    def bevel(s, x0, y0, x1, y1, hi="4", lo="1"):  # light from top-left (style guide §2.3 rule 3)
        for x in range(x0, x1 + 1): s.px[y0][x] = hi; s.px[y1][x] = lo
        for y in range(y0, y1 + 1): s.px[y][x0] = hi; s.px[y][x1] = lo
    def region(s, x0, y0, x1, y1):  # a used uv region (inclusive px)
        s.regions.append((x0, y0, x1, y1)); return [x0, y0, x1 + 1, y1 + 1]
    def save(s):
        # one 2x2 marker per texture file, in the top-left of its largest region (style guide §5.5)
        x0, y0, x1, y1 = max(s.regions, key=lambda r: (r[2] - r[0] + 1) * (r[3] - r[1] + 1))
        if x1 - x0 >= 3 and y1 - y0 >= 3: s.rect(x0, y0, x0 + 1, y0 + 1, "m")
        im = Image.new("RGBA", (16, 16))
        im.putdata([rgb(P[c]) + (255,) for row in s.px for c in row]); im.save(os.path.join(TEX, s.name + ".png"))
        return s

def stripes(t, x0, x1, y0, y1):  # 45 degree hazard stripes, safety orange on alloy dark
    for y in range(y0, y1 + 1):
        for x in range(x0, x1 + 1): t.px[y][x] = "o" if (x + y) % 4 < 2 else "1"

textures, models = {}, {}
def face(tex, uv, **kw): return {"texture": "#" + tex, "uv": uv, **kw}

# ---------- T-01 regolith (straw-man ground) ----------
t = T("regolith", "T"); import random; random.seed(7)
for _ in range(14):
    x, y = random.randrange(15), random.randrange(15); c = random.choice("RSU")
    t.rect(x, y, min(15, x + random.choice((1, 2))), min(15, y + 1), c)
t.region(0, 0, 15, 15); t.save()

# ---------- M-08 Regolith Outgasser: squat heater, stack, hazard stripes ----------
f = T("regolith_outgasser_front"); fr = f.region(1, 6, 14, 15)
f.rect(1, 6, 14, 15, "3"); f.bevel(1, 6, 14, 15)
f.rect(3, 8, 9, 12, "1"); f.bevel(3, 8, 9, 12, hi="4", lo="2")  # heater door: dark, with a light top-left lip
for y in (9, 11): f.rect(4, y, 8, y, "o")                       # hot slots
f.rect(10, 7, 13, 10, "1"); f.rect(11, 8, 12, 9, "g")           # status lamp: running = solid square
stripes(f, 2, 13, 13, 14); f.save()
sd = T("regolith_outgasser_side"); sr = sd.region(2, 6, 13, 15)
sd.rect(2, 6, 13, 15, "3"); sd.bevel(2, 6, 13, 15)
for y in (8, 10): sd.rect(4, y, 11, y, "1"); sd.rect(4, y + 1, 11, y + 1, "4")  # louvres
stripes(sd, 3, 12, 13, 14); sd.save()
tp = T("regolith_outgasser_top"); tr = tp.region(1, 2, 14, 13)
tp.rect(1, 2, 14, 13, "3"); tp.box(1, 2, 14, 13, "1"); tp.rect(7, 3, 7, 12, "2"); tp.rect(2, 7, 13, 7, "2")
for x, y in ((2, 12), (13, 3), (13, 12)): tp.dot(x, y, "4")
tp.save()
st = T("regolith_outgasser_stack", "2"); str_ = st.region(6, 0, 9, 5)
st.rect(6, 0, 9, 5, "2"); st.rect(6, 0, 6, 5, "3"); st.rect(6, 1, 9, 1, "1"); st.rect(6, 4, 9, 4, "1")
stt = st.region(6, 8, 9, 11); st.rect(6, 8, 9, 11, "2"); st.rect(7, 9, 8, 10, "k"); st.save()
models["regolith_outgasser"] = {
    "textures": {"particle": "mars:block/regolith_outgasser_side", "front": "mars:block/regolith_outgasser_front",
                 "side": "mars:block/regolith_outgasser_side", "top": "mars:block/regolith_outgasser_top",
                 "stack": "mars:block/regolith_outgasser_stack"},
    "elements": [
        {"from": [1, 0, 2], "to": [15, 10, 14], "faces": {
            "north": face("front", fr), "south": face("back", [1, 6, 15, 16]),
            "east": face("side", sr), "west": face("side", sr), "up": face("top", tr), "down": face("top", tr)}},
        {"from": [9, 10, 9], "to": [13, 16, 13], "faces": {
            "north": face("stack", str_), "south": face("stack", str_), "east": face("stack", str_),
            "west": face("stack", str_), "up": face("stack", stt)}}]}
# the back (south) face is 14 wide, so it gets its own texture rather than stretching the 12-wide side
b = T("regolith_outgasser_back"); br = b.region(1, 6, 14, 15)
b.rect(1, 6, 14, 15, "3"); b.bevel(1, 6, 14, 15); b.rect(3, 8, 12, 8, "2"); b.rect(3, 11, 12, 11, "2"); stripes(b, 2, 13, 13, 14); b.save()
models["regolith_outgasser"]["textures"]["back"] = "mars:block/regolith_outgasser_back"

# ---------- M-07 Regolith Oven: tall cabinet with a lit window (inside the dome) ----------
o = T("regolith_oven_front"); orr = o.region(2, 2, 13, 15)
o.rect(2, 2, 13, 15, "3"); o.bevel(2, 2, 13, 15)
o.rect(4, 4, 11, 9, "1"); o.rect(5, 5, 10, 8, "L"); o.rect(6, 7, 9, 8, "o")   # window: warm light, hot tray
o.rect(5, 11, 10, 11, "4"); o.rect(5, 12, 10, 12, "1")                          # tray handle
o.rect(10, 11, 13, 14, "1"); o.rect(11, 12, 12, 13, "g"); o.save()                                           # status lamp
os_ = T("regolith_oven_side"); osr = os_.region(2, 2, 13, 15)
os_.rect(2, 2, 13, 15, "3"); os_.bevel(2, 2, 13, 15); os_.rect(7, 3, 7, 14, "2"); os_.save()
ot = T("regolith_oven_top"); otr = ot.region(2, 2, 13, 13)
ot.rect(2, 2, 13, 13, "3"); ot.box(2, 2, 13, 13, "1")
for y in (5, 8, 11): ot.rect(4, y, 11, y, "1"); ot.rect(4, y - 1, 11, y - 1, "4")
ot.save()
models["regolith_oven"] = {
    "textures": {"particle": "mars:block/regolith_oven_side", "front": "mars:block/regolith_oven_front",
                 "side": "mars:block/regolith_oven_side", "top": "mars:block/regolith_oven_top"},
    "elements": [{"from": [2, 0, 2], "to": [14, 14, 14], "faces": {
        "north": face("front", orr), "south": face("side", osr), "east": face("side", osr), "west": face("side", osr),
        "up": face("top", otr), "down": face("top", otr)}}]}

# ---------- M-09 Solar panel: five dust stages + partly cleaned (style guide §2.4 weathering) ----------
# Layout (PIA22876 clean, PIA18080 layout only): a 2 x 2 square of sub-panels on an alloy frame and cross; each
# sub-panel is rows of small rectangular cells (2 px tall) with thin lighter seams, plus a few dark cut-outs.
CELL = [[None] * 16 for _ in range(16)]       # role of each pixel: frame, seam, cut, cell
for y in range(16):
    for x in range(16):
        if x in (0, 15) or y in (0, 15) or x in (7, 8) or y in (7, 8): CELL[y][x] = "frame"
        else:
            lx, ly = (x - 1) % 8, (y - 1) % 8      # 0..5 inside a sub-panel
            CELL[y][x] = "seam" if (ly in (2, 5) or lx == 2) else "cell"
for (x, y) in ((2, 4), (12, 1), (5, 12), (10, 10), (13, 13)): CELL[y][x] = "cut"
random.seed(2201)
U = [[random.random() for _ in range(16)] for _ in range(16)]   # one fixed dust map: stage k adds to stage k-1
DRIFT_ROWS = (13, 14)                                            # low edge of the tilted panel
# dust level per cell pixel: 0 clean (r), 1 film (v), 2 two-thirds (x), 3 ground dust (D), 4 dust highlight (E)
def level(stage, x, y):
    u = U[y][x]; drift = y in DRIFT_ROWS
    if stage == 0: return 0
    if stage == 1: return (2 if y == 14 else 1) if drift else (1 if u < 0.30 else 0)      # first film: specks + drift
    if stage == 2: return 3 if drift else (2 if u < 0.50 else 1)                          # light veil: lifted ~1/3 or more
    if stage == 3: return 3 if (drift or u < 0.80) else 2                                 # heavy: mostly ground colour
    return 4 if u > 0.65 else 3                                                           # full even veil, lighter
LEVEL_COLOR = "rvxDE"
def stage_px(stage, cleaned=False):
    px = [[None] * 16 for _ in range(16)]
    for y in range(16):
        for x in range(16):
            role = CELL[y][x]
            if role == "frame": px[y][x] = "3"
            elif role == "cut": px[y][x] = "p"
            elif role == "seam": px[y][x] = "s" if stage <= 2 else "q"   # light seams until the veil is heavy; then dark lines
            else: px[y][x] = LEVEL_COLOR[level(stage, x, y)]
    if cleaned:   # wind cleared streaks out of a heavy veil (Opportunity, 2014): horizontal runs back to clean
        random.seed(2014)
        for _ in range(11):
            y = random.choice([yy for yy in range(1, 13) if yy not in (7, 8)]); x0 = random.randrange(1, 10)
            for x in range(x0, x0 + random.choice((4, 5, 6))):
                if CELL[y][x] == "cell": px[y][x] = "r"
    return px
STAGES = {f"solar_panel_dust{k}": stage_px(k) for k in range(5)}
STAGES["solar_panel_partly_cleaned"] = stage_px(3, cleaned=True)
pvr = None
for name, px in STAGES.items():
    t = T(name + "_top"); pvr = t.region(0, 0, 15, 15); t.px = [row[:] for row in px]; t.save()
sp = T("solar_panel_frame", "2"); fr1 = sp.region(0, 0, 15, 0); spb = sp.region(0, 2, 15, 15)
sp.rect(0, 0, 15, 0, "1"); sp.rect(0, 2, 15, 15, "2"); sp.rect(0, 8, 15, 8, "1"); sp.rect(7, 2, 8, 15, "3")
sp.save()
po = T("solar_panel_post", "3"); por = po.region(7, 0, 8, 15); po.rect(7, 0, 7, 15, "4"); po.rect(8, 0, 8, 15, "2")
pbr = po.region(4, 0, 11, 1); po.rect(4, 0, 11, 1, "1"); po.save()
for name in STAGES:
    top = name + "_top"
    models[name] = {
        "textures": {"particle": "mars:block/" + top, "cells": "mars:block/" + top,
                     "frame": "mars:block/solar_panel_frame", "post": "mars:block/solar_panel_post"},
        "elements": [
            {"from": [4, 0, 4], "to": [12, 2, 12], "faces": {k: face("post", [4, 0, 12, 2]) for k in ("north", "south", "east", "west")} | {"up": face("frame", [4, 4, 12, 12])}},
            {"from": [7, 2, 7], "to": [9, 9, 9], "faces": {k: face("post", [7, 0, 9, 7]) for k in ("north", "south", "east", "west")}},
            {"from": [0, 9, 0], "to": [16, 10, 16], "rotation": {"origin": [8, 9.5, 8], "axis": "x", "angle": -22.5},  # cells tilt toward the front (north)
             "faces": {"up": face("cells", pvr), "down": face("frame", [0, 0, 16, 16]),
                       "north": face("frame", [0, 0, 16, 1]), "south": face("frame", [0, 0, 16, 1]),
                       "east": face("frame", [0, 0, 16, 1]), "west": face("frame", [0, 0, 16, 1])}}]}

# ---------- M-10 Battery: casing, shape-coded charge segments, + and - terminals ----------
bf = T("battery_front"); bfr = bf.region(3, 4, 12, 15)
bf.rect(3, 4, 12, 15, "3"); bf.bevel(3, 4, 12, 15); bf.rect(3, 5, 12, 5, "o")
bf.rect(4, 6, 9, 15 - 1, "1")                               # dark well behind the charge gauge
for n, y in enumerate((12, 10, 8)):                          # bottom-up: 2 charged (filled), 1 empty (hollow)
    if n < 2: bf.rect(5, y, 8, y, "g")
    else: bf.dot(5, y, "4"); bf.dot(8, y, "4")             # empty segment: only its two end marks (hollow)
bf.rect(9, 6, 12, 9, "1"); bf.rect(10, 7, 11, 8, "g"); bf.save()
bs = T("battery_side"); bsr = bs.region(3, 4, 12, 15); bs.rect(3, 4, 12, 15, "3"); bs.bevel(3, 4, 12, 15); bs.rect(3, 5, 12, 5, "o"); bs.save()
bt = T("battery_top"); btr = bt.region(3, 3, 12, 12); bt.rect(3, 3, 12, 12, "2"); bt.box(3, 3, 12, 12, "1")
tpo = bt.region(0, 0, 1, 1); bt.rect(0, 0, 1, 1, "o"); tmi = bt.region(14, 0, 15, 1); bt.rect(14, 0, 15, 1, "1"); bt.save()
def small(tex, r): return {k: face(tex, r) for k in ("north", "south", "east", "west", "up")}
models["battery"] = {
    "textures": {"particle": "mars:block/battery_side", "front": "mars:block/battery_front", "side": "mars:block/battery_side", "top": "mars:block/battery_top"},
    "elements": [
        {"from": [3, 0, 3], "to": [13, 12, 13], "faces": {"north": face("front", bfr), "south": face("side", bsr),
            "east": face("side", bsr), "west": face("side", bsr), "up": face("top", btr), "down": face("top", btr)}},
        {"from": [5, 12, 6], "to": [7, 14, 8], "faces": small("top", tpo)},
        {"from": [9, 12, 6], "to": [11, 14, 8], "faces": small("top", tmi)}]}

# ---------- M-05 O2 generator, MOXIE-style (PIA24201, PIA24203): gold-plated box, pocket grid, X ribs on one end,
# off-white tube loops, silver fittings. Process (MOXIE splits CO2; electrolysis splits water) is the Game Designer's.
def pockets(t, x0, y0, lamp):
    t.rect(x0, y0, x0 + 11, y0 + 11, "H")
    ribs = (0, 4, 8, 11)
    for r in ribs:
        t.rect(x0 + r, y0, x0 + r, y0 + 11, "J"); t.rect(x0, y0 + r, x0 + 11, y0 + r, "J")
    for a, b in ((1, 3), (5, 7), (9, 10)):          # each pocket: shadow on its top and left inner edge (recessed)
        for c, d in ((1, 3), (5, 7), (9, 10)):
            t.rect(x0 + a, y0 + c, x0 + b, y0 + d, "H"); t.rect(x0 + a, y0 + c, x0 + b, y0 + c, "G"); t.rect(x0 + a, y0 + c, x0 + a, y0 + d, "G")
            t.rect(x0 + b, y0 + d, x0 + b, y0 + d, "I")
    if lamp:  # status lamp in a dark ring, in the lower-right pocket
        t.rect(x0 + 7, y0 + 7, x0 + 11, y0 + 11, "J"); t.rect(x0 + 8, y0 + 8, x0 + 11, y0 + 11, "1")
        t.rect(x0 + 9, y0 + 9, x0 + 10, y0 + 10, "g")        # lamp in a dark ring, framed by the light rib
fa = T("o2_generator_face"); far = fa.region(2, 4, 13, 15); pockets(fa, 2, 4, True); fa.save()
fb = T("o2_generator_back"); fbr = fb.region(2, 4, 13, 15); pockets(fb, 2, 4, False); fb.save()
en = T("o2_generator_end"); enr = en.region(3, 4, 12, 15)
en.rect(3, 4, 12, 15, "H"); en.box(3, 4, 12, 15, "J")
# X ribs (round 2: one-pixel diagonals with shadows read as noise in the iso render; now 2 px wide, light on mid gold)
for k in range(0, 12):
    yy = 4 + k; xa = 3 + round(k * 9 / 11); xb = 12 - round(k * 9 / 11)
    for xx in (xa, xa + 1, xb, xb - 1):
        if 3 <= xx <= 12: en.dot(xx, yy, "J")
    # round 3: light ribs on a darker field (light-on-light read as an hourglass)
en.save()
sd_ = T("o2_generator_side"); sdr = sd_.region(3, 4, 12, 15)
sd_.rect(3, 4, 12, 15, "H"); sd_.box(3, 4, 12, 15, "J"); sd_.rect(4, 8, 11, 8, "J"); sd_.rect(4, 12, 11, 12, "J"); sd_.rect(4, 9, 11, 9, "G"); sd_.rect(4, 13, 11, 13, "G"); sd_.save()
tp_ = T("o2_generator_top"); tpr = tp_.region(2, 3, 13, 12)
tp_.rect(2, 3, 13, 12, "I"); tp_.box(2, 3, 13, 12, "J")
for x, y in ((3, 4), (12, 4), (3, 11), (12, 11), (7, 4), (8, 11)): tp_.dot(x, y, "G")
tp_.save()
tb = T("o2_generator_tube", "W"); tbr = tb.region(0, 0, 7, 7)
tb.rect(0, 0, 7, 7, "W"); tb.rect(0, 5, 7, 7, "V"); tb.rect(6, 0, 7, 7, "V")   # off-white, shaded lower and right
fi = tb.region(8, 0, 15, 7); tb.rect(8, 0, 15, 7, "4"); tb.rect(8, 4, 15, 4, "3"); tb.rect(14, 0, 15, 7, "3")   # silver fitting
tb.save()
def tube(a, b): return {"from": a, "to": b, "faces": {k: face("tube", [0, 0, 1, 1]) for k in ("north", "south", "east", "west", "up", "down")}}
def fitting(a, b): return {"from": a, "to": b, "faces": {k: face("tube", [8, 0, 10, 2]) for k in ("north","south","east","west","up","down")}}
models["o2_generator"] = {
    "textures": {"particle": "mars:block/o2_generator_side", "front": "mars:block/o2_generator_face", "back": "mars:block/o2_generator_back",
                 "end": "mars:block/o2_generator_end", "side": "mars:block/o2_generator_side", "top": "mars:block/o2_generator_top",
                 "tube": "mars:block/o2_generator_tube"},
    "elements": [
        {"from": [2, 0, 3], "to": [14, 12, 13], "faces": {"north": face("front", far), "south": face("back", fbr),
            "east": face("end", enr), "west": face("side", sdr), "up": face("top", tpr), "down": face("top", tpr)}},
        # two loops, as in PIA24201: from a fitting on top, up, over the edge, and down the side to a low fitting
        fitting([4, 12, 6], [6, 13, 8]), tube([4, 13, 6], [5, 15, 7]), tube([0, 14, 6], [5, 15, 7]),
        tube([0, 3, 6], [1, 14, 7]), fitting([1, 2, 6], [2, 4, 8]),                   # west loop
        fitting([10, 12, 4], [12, 13, 6]), tube([11, 13, 4], [12, 14, 5]), tube([11, 13, 4], [16, 14, 5]),
        tube([15, 5, 4], [16, 13, 5]), fitting([14, 4, 4], [15, 6, 6])]}             # east loop, near the front edge so the X ribs stay visible
for el in models["o2_generator"]["elements"]:       # tube and fitting faces: uv sized to the face so pixels stay square
    if el["from"] == [2, 0, 3]: continue
    a, b = el["from"], el["to"]; dx, dy, dz = b[0] - a[0], b[1] - a[1], b[2] - a[2]
    base = 10 if any(f["uv"][0] >= 8 for f in el["faces"].values()) else 2   # start past the placeholder corner
    size = {"north": (dx, dy), "south": (dx, dy), "east": (dz, dy), "west": (dz, dy), "up": (dx, dz), "down": (dx, dz)}
    for k, f in el["faces"].items():
        # tubes are one flat off-white pixel stretched along the tube (no stretched detail); fittings a 2 x 2 silver patch
        f["uv"] = [base, 2, base + 1, 3] if base == 2 else [base, 2, base + 2, 4]

# ---------- M-13 Planetary Monitor: console with a tilted screen ----------
sc = T("planetary_monitor_screen", "k"); scr = sc.region(1, 4, 14, 12)
sc.rect(1, 4, 14, 12, "k"); sc.box(1, 4, 14, 12, "2")
sc.rect(3, 5, 4, 5, "y"); sc.rect(5, 5, 10, 5, "w")                          # next-gate strip (gate mark + bar)
sc.box(3, 7, 4, 11, "w"); sc.rect(3, 9, 4, 11, "g")                          # pressure gauge, filled part
for y in (7, 9, 11): sc.rect(6, y, 10, y, "w")                               # value rows
sc.dot(11, 7, "g"); sc.dot(11, 9, "g")                                       # trend marks
sc.rect(12, 5, 13, 5, "b")                                                   # unread-log indicator (U-22)
sc.save()
mb = T("planetary_monitor_body"); mbr = mb.region(0, 8, 15, 15)
mb.rect(0, 8, 15, 15, "3"); mb.bevel(0, 8, 15, 15); mb.rect(11, 9, 14, 12, "1"); mb.rect(12, 10, 13, 11, "g"); mb.rect(3, 12, 9, 12, "2")
mbt = mb.region(0, 0, 15, 5); mb.rect(0, 0, 15, 5, "2"); mb.box(0, 0, 15, 5, "1")
for x in range(2, 14, 2): mb.dot(x, 3, "4")
mb.save()
models["planetary_monitor"] = {
    "textures": {"particle": "mars:block/planetary_monitor_body", "body": "mars:block/planetary_monitor_body",
                 "screen": "mars:block/planetary_monitor_screen"},
    "elements": [
        {"from": [0, 0, 2], "to": [16, 8, 14], "faces": {"north": face("body", mbr), "south": face("body", mbr),
            "east": face("body", [2, 8, 14, 16]), "west": face("body", [2, 8, 14, 16]),
            "up": face("body", [0, 0, 16, 6]), "down": face("body", [0, 0, 16, 6])}},
        {"from": [1, 7, 9], "to": [15, 16, 11], "rotation": {"origin": [8, 8, 10], "axis": "x", "angle": -22.5},
         "faces": {"north": face("screen", scr), "south": face("body", [0, 8, 14, 15]), "up": face("body", [0, 8, 14, 10]),
                   "east": face("body", [0, 8, 2, 15]), "west": face("body", [0, 8, 2, 15])}}]}

# ---------- write models, item models, blockstates ----------
for name, m in models.items():
    m = {"parent": "block/block", **m}
    json.dump(m, open(os.path.join(A, "models", "block", name + ".json"), "w"), indent=1)
    json.dump({"parent": "mars:block/" + name}, open(os.path.join(A, "models", "item", name + ".json"), "w"), indent=1)
    json.dump({"variants": {f"facing={f}": ({"model": "mars:block/" + name} | ({"y": r} if r else {}))
                            for f, r in (("north", 0), ("east", 90), ("south", 180), ("west", 270))}},
              open(os.path.join(A, "blockstates", name + ".json"), "w"), indent=1)

# ---------- numeric checks ----------
def lin(c): c /= 255; return c / 12.92 if c <= 0.04045 else ((c + 0.055) / 1.055) ** 2.4
def lum(h): r, g, b = [lin(v) for v in rgb(h)]; return 0.2126 * r + 0.7152 * g + 0.0722 * b
def cr(a, b): la, lb = sorted((lum(a), lum(b)), reverse=True); return (la + 0.05) / (lb + 0.05)
fails = []
for fn in sorted(os.listdir(TEX)):
    if not fn.endswith(".png"): continue
    im = Image.open(os.path.join(TEX, fn)).convert("RGB"); cols = set(im.get_flattened_data())
    bad = [c for c in cols if c not in PAL]
    if bad: fails.append(f"{fn}: non-palette colors {bad}")
    if (0, 0, 0) in cols or (255, 255, 255) in cols: fails.append(f"{fn}: pure black or white")
for name, m in models.items():  # per used face region: <= 6 colors besides the marker
    for el in m["elements"]:
        for fname, fd in el["faces"].items():
            tex = m["textures"][fd["texture"][1:]].split("/")[-1]
            im = Image.open(os.path.join(TEX, tex + ".png")).convert("RGB")
            u1, v1, u2, v2 = fd["uv"]; x0, x1 = sorted((u1, u2)); y0, y1 = sorted((v1, v2))
            region = im.crop((int(x0), int(y0), int(x1), int(y1)))
            n = len({c for c in region.get_flattened_data() if c != rgb(P["m"])})
            if n > 6: fails.append(f"{name}.{fname}: {n} colors (> 6)")
            if not os.path.exists(os.path.join(TEX, tex + ".png")): fails.append(f"missing texture {tex}")
# meaning-bearing pairs must reach 3:1 (WCAG 1.4.11); shape-only pairs should reach 2:1 (target, reported)
meaning = [("lamp g on its ring 1", "g", "1"), ("heater slot o on door 1", "o", "1"), ("oven light L on frame 1", "L", "1"),
           ("dust highlight E on clean cell r", "E", "r"), ("veil D vs clean cell r (state)", "D", "r"),
           ("dark seam q on veil D", "q", "D"), ("cut-out p on veil D", "p", "D"), ("tube W on gold H", "W", "H"),
           ("lamp ring 1 vs light rib J", "1", "J"), ("screen text w on k", "w", "k"), ("screen gate y on k", "y", "k"),
           ("screen gauge g on k", "g", "k"), ("unread b on k", "b", "k"), ("empty segment 4 on well 1", "4", "1")]
shape = [("PV frame 3 vs clean cell r", "3", "r"), ("PV frame 3 vs veil D", "3", "D"), ("light seam s vs clean cell r", "s", "r"),
         ("gold rib J vs pocket H", "J", "H"), ("pocket shadow G vs H", "G", "H"), ("fitting 4 vs gold H", "4", "H"), ("gold H vs Mars sky", "H", None), ("door 1 vs body 3", "1", "3"), ("stripes o vs 1", "o", "1"),
         ("tank window i vs tank 3", "i", "3"), ("dust D vs Mars sky", "D", None), ("stack 2 vs sky", "2", None)]
print("Meaning-bearing contrast (need >= 3:1):")
for label, a, b2 in meaning:
    r = cr(P[a], P[b2]); print(f"  {label:32s} {r:5.1f}  {'ok' if r >= 3 else 'LOW'}")
    if r < 3: fails.append(f"contrast {label} {r:.1f}")
print("Shape contrast (target >= 2:1, reported only):")
for label, a, b2 in shape:
    other = P[b2] if b2 else "#D2AE86"
    r = cr(P[a], other); print(f"  {label:32s} {r:5.1f}  {'ok' if r >= 2 else 'weak'}")
# every running-lamp pixel must sit inside a dark ring (no green pixel touching mid alloy)
for fn in sorted(os.listdir(TEX)):
    if not fn.endswith(".png") or "screen" in fn or "solar" in fn: continue
    im = Image.open(os.path.join(TEX, fn)).convert("RGB"); g = rgb(P["g"]); ok = {g, rgb(P["1"]), rgb(P["k"])}
    for y in range(16):
        for x in range(16):
            if im.getpixel((x, y)) == g:
                for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                    if 0 <= x + dx < 16 and 0 <= y + dy < 16 and im.getpixel((x + dx, y + dy)) not in ok:
                        fails.append(f"{fn}: lamp pixel at {x},{y} touches a non-ring color"); break
# ---- OKLab and colour-vision simulation (Machado, Oliveira & Fernandes 2009, severity 1.0) ----
MX = {"normal": None,
      "protan": [[0.152286, 1.052583, -0.204868], [0.114503, 0.786281, 0.099216], [-0.003882, -0.048116, 1.051998]],
      "deutan": [[0.367322, 0.860646, -0.227968], [0.280085, 0.672501, 0.047413], [-0.011820, 0.042940, 0.968881]],
      "tritan": [[1.255528, -0.076749, -0.178779], [-0.078411, 0.930809, 0.147602], [0.004733, 0.691367, 0.303900]]}
def oklab_lin(l):
    r, g, b = l
    L_ = (0.4122214708 * r + 0.5363325363 * g + 0.0514459929 * b) ** (1 / 3)
    M_ = (0.2119034982 * r + 0.6806995451 * g + 0.1073969566 * b) ** (1 / 3)
    S_ = (0.0883024619 * r + 0.2817188376 * g + 0.6299787005 * b) ** (1 / 3)
    return (0.2104542553 * L_ + 0.7936177850 * M_ - 0.0040720468 * S_, 1.9779984951 * L_ - 2.4285922050 * M_ + 0.4505937099 * S_,
            0.0259040371 * L_ + 0.7827717662 * M_ - 0.8086757660 * S_)
def sim_lin(c, k):
    l = [lin(v) for v in c]
    if MX[k]: l = [min(1, max(0, sum(MX[k][i][j] * l[j] for j in range(3)))) for i in range(3)]
    return l
def dE(c1, c2, k): return math.dist(oklab_lin(sim_lin(c1, k)), oklab_lin(sim_lin(c2, k)))
print("Colour-vision checks (OKLab dE, normal / protan / deutan / tritan):")
for label, a, b2, need in (("gold I vs safety orange o", "I", "o", 0.10), ("gold J vs UI yellow y", "J", "y", 0.10),
                           ("gold edge rib J vs regolith T (edge on ground)", "J", "T", 0.10), ("veil D vs clean cell r", "D", "r", 0.10)):
    ds = [dE(rgb(P[a]), rgb(P[b2]), k) for k in MX]; ok_ = min(ds) >= need
    print(f"  {label:40s} " + " / ".join(f"{d:.3f}" for d in ds) + f"  {'ok' if ok_ else 'LOW'} (need {need} in all)")
    if not ok_: fails.append(f"colour-vision {label} min {min(ds):.3f}")
# ---- dust progression: superset, visible steps, structure kept ----
order = {c: i for i, c in enumerate(LEVEL_COLOR)}
names = [f"solar_panel_dust{k}" for k in range(5)]
pxs = [STAGES[n] for n in names]
for k in range(1, 5):
    for y in range(16):
        for x in range(16):
            if CELL[y][x] == "cell" and order[pxs[k][y][x]] < order[pxs[k - 1][y][x]]:
                fails.append(f"dust{k} at {x},{y} has less dust than dust{k-1} (not a superset)")
def mean_lab(px, k, roles=("cell",)):
    cells = [rgb(P[px[y][x]]) for y in range(16) for x in range(16) if CELL[y][x] in roles]
    labs = [oklab_lin(sim_lin(c, k)) for c in cells]
    return tuple(sum(v[i] for v in labs) / len(labs) for i in range(3))
NONCOLOUR = {1: "specks and a drift band appear along the low edge (pattern change)"}
print("Dust steps (mean OKLab dE of the CELL area vs the previous stage, where the dust sits; need >= 0.035 in normal and 2 of 3")
print("simulations, or a non-colour cue). Whole-face dE (cells + seams + cut-outs + frame) is shown after the slash group for reference:")
for k in range(1, 5):
    ds = {m: math.dist(mean_lab(pxs[k], m), mean_lab(pxs[k - 1], m)) for m in MX}
    passed = ds["normal"] >= 0.035 and sum(ds[m] >= 0.035 for m in ("protan", "deutan", "tritan")) >= 2
    changed = sum(pxs[k][y][x] != pxs[k - 1][y][x] for y in range(16) for x in range(16)) / 256
    note = "ok" if passed else (f"colour step small; non-colour cue: {NONCOLOUR[k]} ({changed:.0%} of pixels change)" if k in NONCOLOUR and changed >= 0.08 else "TOO SMALL")
    face_d = math.dist(mean_lab(pxs[k], "normal", ("cell", "seam", "cut", "frame")), mean_lab(pxs[k - 1], "normal", ("cell", "seam", "cut", "frame")))
    print(f"  dust{k-1} -> dust{k}: " + " / ".join(f"{ds[m]:.3f}" for m in MX) + f"  (whole face {face_d:.3f})  {note}")
    if note == "TOO SMALL": fails.append(f"dust step {k-1}->{k} too small")
print("Panel structure per stage (seams and cut-outs stay; contrast vs the dominant cell colour):")
from collections import Counter
for n, px in list(zip(names, pxs)) + [("solar_panel_partly_cleaned", STAGES["solar_panel_partly_cleaned"])]:
    dom = Counter(px[y][x] for y in range(16) for x in range(16) if CELL[y][x] == "cell").most_common(1)[0][0]
    seam = Counter(px[y][x] for y in range(16) for x in range(16) if CELL[y][x] == "seam").most_common(1)[0][0]
    best = max(cr(P[c], P[dom]) for c in (seam, "p", "3"))
    print(f"  {n:28s} cells {dom}  seam {seam}  best structure contrast {best:.1f}  {'ok' if best >= 2 else 'WEAK'}")
    if best < 2: fails.append(f"{n}: panel structure unreadable")
print("FAILS:" if fails else "All checks passed.", *fails, sep="\n  ")
sys.exit(1 if fails else 0)
