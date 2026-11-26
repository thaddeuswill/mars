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
    "p": "#1B2433", "q": "#24324A", "r": "#2F4462", "s": "#4A6488",  # photovoltaic cell ramp
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
        for (x0, y0, x1, y1) in s.regions:
            if x1 - x0 >= 3 and y1 - y0 >= 3:
                s.rect(x0, y0, x0 + 1, y0 + 1, "m")
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

# ---------- M-09 Solar panel (clean and dusty): tilted panel on a post ----------
def pv(name, dusty):
    t = T(name, "r"); reg = t.region(0, 0, 15, 15)
    t.box(0, 0, 15, 15, "3"); t.rect(7, 1, 8, 14, "3"); t.rect(1, 7, 14, 8, "3")
    for (cx, cy) in ((1, 1), (9, 1), (1, 9), (9, 9)):
        for k in (2, 4): t.rect(cx + k, cy, cx + k, cy + 5, "q"); t.rect(cx, cy + k, cx + 5, cy + k, "q")
        t.dot(cx, cy, "s"); t.dot(cx + 1, cy, "s"); t.dot(cx, cy + 1, "s")
    if dusty:
        random.seed(3)
        for _ in range(26):
            x, y = random.randrange(1, 15), random.randrange(1, 15); t.rect(x, y, min(14, x + 1), y, "E")
        t.rect(1, 13, 14, 14, "E"); t.rect(1, 14, 14, 14, "D")                     # dust collects at the low edge
    return t.save(), reg
_, pvr = pv("solar_panel_top", False); pv("solar_panel_dusty_top", True)
sp = T("solar_panel_frame", "2"); fr1 = sp.region(0, 0, 15, 0); spb = sp.region(0, 2, 15, 15)
sp.rect(0, 0, 15, 0, "1"); sp.rect(0, 2, 15, 15, "2"); sp.rect(0, 8, 15, 8, "1"); sp.rect(7, 2, 8, 15, "3")
sp.save()
po = T("solar_panel_post", "3"); por = po.region(7, 0, 8, 15); po.rect(7, 0, 7, 15, "4"); po.rect(8, 0, 8, 15, "2")
pbr = po.region(4, 0, 11, 1); po.rect(4, 0, 11, 1, "1"); po.save()
for name, top in (("solar_panel", "solar_panel_top"), ("solar_panel_dusty", "solar_panel_dusty_top")):
    models[name] = {
        "textures": {"particle": "mars:block/" + top, "cells": "mars:block/" + top,
                     "frame": "mars:block/solar_panel_frame", "post": "mars:block/solar_panel_post"},
        "elements": [
            {"from": [4, 0, 4], "to": [12, 2, 12], "faces": {k: face("post", [4, 0, 12, 2]) for k in ("north", "south", "east", "west")} | {"up": face("frame", [4, 4, 12, 12])}},
            {"from": [7, 2, 7], "to": [9, 9, 9], "faces": {k: face("post", [7, 0, 9, 7]) for k in ("north", "south", "east", "west")}},
            {"from": [0, 9, 0], "to": [16, 10, 16], "rotation": {"origin": [8, 9.5, 8], "axis": "x", "angle": 22.5},
             "faces": {"up": face("cells", pvr), "down": face("frame", [0, 0, 16, 16]),
                       "north": face("frame", [0, 0, 16, 1]), "south": face("frame", [0, 0, 16, 1]),
                       "east": face("frame", [0, 0, 16, 1]), "west": face("frame", [0, 0, 16, 1])}}]}

# ---------- M-10 Battery: casing, shape-coded charge segments, + and - terminals ----------
bf = T("battery_front"); bfr = bf.region(3, 4, 12, 15)
bf.rect(3, 4, 12, 15, "3"); bf.bevel(3, 4, 12, 15); bf.rect(3, 5, 12, 5, "o")
bf.rect(4, 6, 9, 15 - 1, "1")                               # dark well behind the charge gauge
for n, y in enumerate((12, 10, 8)):                          # bottom-up: 2 charged (filled), 1 empty (hollow)
    if n < 2: bf.rect(5, y, 8, y, "g")
    else: bf.box(5, y, 8, y, "4")
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

# ---------- M-05 O2 generator (electrolysis): base with panel, twin tanks ----------
tk = T("o2_generator_tank"); tkr = tk.region(0, 0, 4, 10)
for x, c in zip(range(5), "23443"): tk.rect(x, 0, x, 10, c)
tk.rect(0, 2, 4, 2, "o"); tk.rect(2, 4, 2, 9, "i"); tk.dot(2, 5, "2"); tk.dot(2, 8, "2")   # electrolyte window + bubbles
tkt = tk.region(8, 0, 12, 4); tk.rect(8, 0, 12, 4, "3"); tk.box(8, 0, 12, 4, "1"); tk.dot(10, 2, "4"); tk.save()
ob = T("o2_generator_base"); obr = ob.region(1, 12, 14, 15)
ob.rect(1, 12, 14, 15, "2"); ob.bevel(1, 12, 14, 15, hi="3", lo="1"); ob.rect(10, 12, 13, 15, "1"); ob.rect(11, 13, 12, 14, "g"); ob.rect(3, 13, 8, 13, "4")
obt = ob.region(1, 1, 14, 10); ob.rect(1, 1, 14, 10, "2"); ob.box(1, 1, 14, 10, "1"); ob.save()
obs = T("o2_generator_base_side"); obsr = obs.region(1, 12, 14, 15); obs.rect(1, 12, 14, 15, "2"); obs.bevel(1, 12, 14, 15, hi="3", lo="1"); obs.save()
models["o2_generator"] = {
    "textures": {"particle": "mars:block/o2_generator_base", "base": "mars:block/o2_generator_base",
                 "bside": "mars:block/o2_generator_base_side", "tank": "mars:block/o2_generator_tank"},
    "elements": [
        {"from": [1, 0, 3], "to": [15, 4, 13], "faces": {"north": face("base", obr), "south": face("bside", obsr),
            "east": face("bside", [3, 12, 13, 16]), "west": face("bside", [3, 12, 13, 16]),
            "up": face("base", [1, 1, 15, 11]), "down": face("base", [1, 1, 15, 11])}},
        {"from": [2, 4, 5], "to": [7, 15, 10], "faces": {k: face("tank", tkr) for k in ("north", "south", "east", "west")} | {"up": face("tank", tkt)}},
        {"from": [9, 4, 5], "to": [14, 15, 10], "faces": {k: face("tank", tkr) for k in ("north", "south", "east", "west")} | {"up": face("tank", tkt)}},
        {"from": [7, 12, 7], "to": [9, 13, 8], "faces": {k: face("tank", [8, 0, 10, 1]) for k in ("north", "south", "up", "down")}}]}

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
        {"from": [1, 8, 9], "to": [15, 17, 11], "rotation": {"origin": [8, 8, 10], "axis": "x", "angle": -22.5},
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
           ("dust E on cell r (dusty state)", "E", "r"), ("screen text w on k", "w", "k"), ("screen gate y on k", "y", "k"),
           ("screen gauge g on k", "g", "k"), ("unread b on k", "b", "k"), ("empty segment 4 on well 1", "4", "1")]
shape = [("PV frame 3 vs cell r", "3", "r"), ("door 1 vs body 3", "1", "3"), ("stripes o vs 1", "o", "1"),
         ("tank window i vs tank 3", "i", "3"), ("stack 2 vs sky", "2", None)]
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
print("FAILS:" if fails else "All checks passed.", *fails, sep="\n  ")
sys.exit(1 if fails else 0)
