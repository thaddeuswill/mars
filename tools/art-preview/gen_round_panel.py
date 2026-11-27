"""PROTOTYPE (BACKLOG #25): a circular, real-scale solar array like InSight's (PIA22876 clean, PIA25286 dusty),
built from vanilla-compatible cuboids. STRAW-MAN. Run: python3 -I tools/art-preview/gen_round_panel.py

Scale: 1 block = 1 m = 16 px (1 px = 6.25 cm). PROVISIONAL dimensions until the SME's docs/science/dimensions.md:
array diameter about 2.2 m = 35 px, 12 segments, disc 1 px (6 cm) thick, 16 px (1 m) above the ground on a lander-deck mount (see DECK_Y).

Geometry: a "pixel-circle disc": one flat 1 px tall cuboid per run of texture rows with the same chord, no rotated
elements, no overlaps (so no z-fighting). The disc spans x and z from -9.5 to 25.5 px, inside Minecraft's allowed
element range [-16, 32]. One 48 x 48 texture covers the 3 x 3 block area (-16..32), so the texel density stays
16 px per block, matching every other asset.

Batch 3 (owner chose the round array, BACKLOG #28): writes the GAME's panel models solar_panel_round_dust0..4 and
solar_panel_round_partly_cleaned (block models and textures; the blockstate and item model are the Technical Lead's).
Diameter 2.2 m (dimensions.md §1, opened NASA sources) = 35.2 px; drawn as 35 px
and runs the numeric checks (palette only, superset dust rule, step size, structure, colour count)."""
import json, math, os, random, sys
from PIL import Image

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
A = os.path.join(ROOT, "pack", "assets", "mars"); TEX = os.path.join(A, "textures", "block")
P = {"1": "#3B4148", "2": "#59616A", "3": "#7E8790", "4": "#A8B0B7",
     "p": "#1C1A21", "q": "#26232C", "r": "#322D38", "s": "#4E4957",
     "v": "#5C4743", "x": "#89614C", "D": "#B97C52", "E": "#D09A6E", "S": "#7A4530", "m": "#FF00FF"}
def rgb(h): return tuple(int(h.lstrip("#")[j:j + 2], 16) for j in (0, 2, 4))

N, C, R = 48, 24.0, 17.5          # texture size, centre, radius (texture px; texture px i <-> model x = -16 + i)
SEGMENTS = 12
def polar(i, j):
    dx, dy = i + 0.5 - C, j + 0.5 - C
    return math.hypot(dx, dy), math.degrees(math.atan2(dy, dx)) % 360
ROLE = [[None] * N for _ in range(N)]
SEG = 360 / SEGMENTS
# Round 2 (after looking at the first render beside PIA22876): the real array is a 12-sided polygon (each segment has a
# straight outer edge) and its cells run in rows PARALLEL to that edge. Round 1 used a true circle with concentric
# ring seams, which read as a target or rose window. Now: dodecagon outline, rows parallel to each segment's edge.
for j in range(N):
    for i in range(N):
        r, a = polar(i, j)
        k = int(a // SEG); th = (k + 0.5) * SEG                   # this segment's bisector
        d = r * math.cos(math.radians(a - th))                     # distance from centre along the bisector
        if d > R * math.cos(math.radians(SEG / 2)): continue       # outside the 12-gon
        if d > R * math.cos(math.radians(SEG / 2)) - 1: ROLE[j][i] = "rim"; continue
        if r < 1.6: ROLE[j][i] = "hub"; continue
        if d > 4 and (d - 4) % 4.4 < 0.6: ROLE[j][i] = "seam"; continue        # cell rows parallel to the outer edge
        ROLE[j][i] = "cell"
# Round 3: 12 radial seams drawn as continuous 1 px lines (Bresenham) from the hub to each polygon vertex; sampling
# by angle left them dotted and the segments stopped reading.
for k in range(SEGMENTS):
    ang = math.radians(k * SEG)
    x0, y0 = C - 0.5, C - 0.5
    x1, y1 = C - 0.5 + (R - 0.6) * math.cos(ang), C - 0.5 + (R - 0.6) * math.sin(ang)
    steps = int(max(abs(x1 - x0), abs(y1 - y0)) * 2) + 1
    for t in range(steps + 1):
        i = round(x0 + (x1 - x0) * t / steps); j = round(y0 + (y1 - y0) * t / steps)
        if ROLE[j][i] in ("cell", "seam"): ROLE[j][i] = "seam"
# small dark cut-outs: one in every other segment at mid radius (PIA22876 shows a few per array)
for k in range(SEGMENTS):   # one per segment, as in PIA25286
    ang = math.radians((k + 0.5) * 360 / SEGMENTS); i = int(C + 12.8 * math.cos(ang)); j = int(C + 12.8 * math.sin(ang))
    if ROLE[j][i] == "cell": ROLE[j][i] = "cut"

rnd = random.Random(2201)
U = [[rnd.random() for _ in range(N)] for _ in range(N)]
def rim_band(i, j): return polar(i, j)[0] > R - 3.0        # dust drifts first along the rim (flat disc: no low edge)
def level(stage, i, j):
    u, drift = U[j][i], rim_band(i, j)
    if stage == 0: return 0
    if stage == 1: return 2 if (drift and u < 0.5) else (1 if (drift or u < 0.30) else 0)
    if stage == 2: return 3 if (drift and u < 0.5) else (2 if (drift or u < 0.50) else 1)
    if stage == 3: return 3 if (drift or u < 0.72) else 2
    return 4 if u > 0.80 else 3          # a fairly even veil (round 3: 35 % highlights read as noise)
LEVEL = "rvxDE"
def stage_px(stage, cleaned=False):
    px = [[None] * N for _ in range(N)]
    for j in range(N):
        for i in range(N):
            ro = ROLE[j][i]
            if ro is None: continue
            if ro in ("rim", "hub", "rib"): px[j][i] = "3"
            elif ro == "cut": px[j][i] = "p"
            elif ro == "seam": px[j][i] = "s" if stage <= 2 else "S"   # softened veil seams: 1 px dark tan-brown
            else: px[j][i] = LEVEL[level(stage, i, j)]
    if cleaned:
        # Batch 3: must read as different from stage 4 in a WIDE shot (in-game screenshot: the speckled version looked
        # like stage 4 from a distance). Uneven cleaning by segment (SME dust review §3; PIA17759 -> PIA18079), now as
        # LARGE CONTIGUOUS clean wedges: dark clean cells next to light veil, a big lightness difference.
        # Per segment: 1.0 = cleaned out to the rim band, 0.55 = inner half cleaned, 0 = untouched veil.
        plan = (1.0, 1.0, 0.55, 0, 0, 1.0, 1.0, 0.55, 0, 0.55, 0, 0)
        for j in range(N):
            for i in range(N):
                if ROLE[j][i] != "cell": continue
                r, a = polar(i, j); k = int(a // SEG)
                if plan[k] and r < (R - 2.5) * plan[k]: px[j][i] = "r"
    return px
# Batch 3: these ARE the game's solar panel models, under the names the registered blockstate uses (the Technical
# Lead's blockstates/solar_panel.json maps dust_stage 0-4 -> solar_panel_round_dust0..4 and partly_cleaned ->
# solar_panel_round_partly_cleaned; the item model's parent is solar_panel_round_dust0).
STAGES = {f"solar_panel_round_dust{k}": stage_px(k) for k in range(5)}
STAGES["solar_panel_round_partly_cleaned"] = stage_px(4, cleaned=True)

# ---- geometry: merge rows with the same chord into one cuboid ----
runs = []
for j in range(N):
    xs = [i for i in range(N) if ROLE[j][i] is not None]
    if not xs: continue
    span = (min(xs), max(xs) + 1)
    if runs and runs[-1][0] == span and runs[-1][2] == j: runs[-1][2] = j + 1
    else: runs.append([span, j, j + 1])
# Raised array (2026-11-27, owner decision; SME docs/team/reports/2026-11-27-sme-deck-height.md): the array sits on a lander
# deck 16 px (1.0 m) above the base. InSight deck 0.83-1.08 m (13-17 px), NASA/JPL InSight press kit "Spacecraft" page (SME
# could not open it; from search summaries, NOT directly verified). DECK_Y = underside of the array. To use the
# Producer decision: top of the disc at 16 px, so DECK_Y = 15 (underside-at-16 alternative: DECK_Y = 16, canopy one block up).
DECK_Y = 15
Y0, Y1 = DECK_Y, DECK_Y + 1           # disc 1 px thick: y 15..16, inside the centre block height (Producer chose this reading 2026-11-27)
def uv(i0, j0, i1, j1): return [round(i0 / 3, 4), round(j0 / 3, 4), round(i1 / 3, 4), round(j1 / 3, 4)]
def model_for(tex):
    els = []
    for (i0, i1), j0, j1 in runs:
        f = [-16 + i0, Y0, -16 + j0]; t = [-16 + i1, Y1, -16 + j1]
        els.append({"from": f, "to": t, "faces": {
            "up": {"texture": "#cells", "uv": uv(i0, j0, i1, j1)},
            "down": {"texture": "#frame", "uv": [0, 0, 1, 1]},
            "north": {"texture": "#frame", "uv": [0, 0, 1, 1]}, "south": {"texture": "#frame", "uv": [0, 0, 1, 1]},
            "east": {"texture": "#frame", "uv": [0, 0, 1, 1]}, "west": {"texture": "#frame", "uv": [0, 0, 1, 1]}}})
    # lander deck mount (inside the centre block's footprint x/z 0..16 so the hitbox and neighbours stay unchanged):
    # base plate, three vertical legs at 120 degrees, and an octagonal deck plate 2 px thick under the array (y 14..16).
    # Deliberate stylization: real deck is 1.56 m (25 px); drawn 16 px (1 m) so it stays inside one block.
    fr = lambda a, b, c, d: {"north": {"texture": "#frame", "uv": [0, 0, 1, 1]}, "south": {"texture": "#frame", "uv": [0, 0, 1, 1]},
                             "east": {"texture": "#frame", "uv": [0, 0, 1, 1]}, "west": {"texture": "#frame", "uv": [0, 0, 1, 1]},
                             "up": {"texture": "#frame", "uv": [a, b, c, d]}, "down": {"texture": "#frame", "uv": [a, b, c, d]}}
    # octagon deck: centre box plus four side strips (no overlaps, no z-fighting)
    for (fx, fz, tx, tz) in ((3, 3, 13, 13), (1, 3, 3, 13), (13, 3, 15, 13), (3, 1, 13, 3), (3, 13, 13, 15)):
        els.append({"from": [fx, DECK_Y - 2, fz], "to": [tx, DECK_Y, tz], "faces": fr(fx, fz, tx, tz)})
    for (lx, lz) in ((7, 3), (3, 10), (11, 10)):                       # three legs, 2 x 2 px, y 1..14
        els.append({"from": [lx, 1, lz], "to": [lx + 2, DECK_Y - 2, lz + 2],
                    "faces": {k: {"texture": "#post", "uv": [lx % 14, 0, lx % 14 + 2, DECK_Y - 3]} for k in ("north", "south", "east", "west")}})
    els.append({"from": [2, 0, 2], "to": [14, 1, 14], "faces": {k: {"texture": "#post", "uv": [4, 0, 12, 1]} for k in ("north", "south", "east", "west")} | {"up": {"texture": "#frame", "uv": [2, 2, 14, 14]}}})
    return {"parent": "block/block", "textures": {"particle": "mars:block/" + tex, "cells": "mars:block/" + tex,
            "frame": "mars:block/solar_panel_frame", "post": "mars:block/solar_panel_post"}, "elements": els}
for name, px in STAGES.items():
    tex = name + "_top"
    im = Image.new("RGBA", (N, N), (0, 0, 0, 0))
    for j in range(N):
        for i in range(N):
            if px[j][i]: im.putpixel((i, j), rgb(P[px[j][i]]) + (255,))
    # placeholder marker (batch 3): ONE pixel at the top-left-most painted pixel (style guide §5.5)
    mi, mj = next((i, j) for j in range(N) for i in range(N) if ROLE[j][i])
    im.putpixel((mi, mj), rgb(P["m"]) + (255,))
    im.save(os.path.join(TEX, tex + ".png"))
    json.dump(model_for(tex), open(os.path.join(A, "models", "block", name + ".json"), "w"), indent=1)
    # blockstate and item model are the Technical Lead's (blockstates/solar_panel.json, models/item/solar_panel.json)

# ---- checks ----
fails = []
def lin(c): c /= 255; return c / 12.92 if c <= 0.04045 else ((c + 0.055) / 1.055) ** 2.4
def lum(h): r, g, b = [lin(v) for v in rgb(h)]; return 0.2126 * r + 0.7152 * g + 0.0722 * b
def cr(a, b): la, lb = sorted((lum(a), lum(b)), reverse=True); return (la + 0.05) / (lb + 0.05)
def ok(c):
    l = [lin(v) for v in c]
    L_ = (0.4122214708 * l[0] + 0.5363325363 * l[1] + 0.0514459929 * l[2]) ** (1 / 3)
    M_ = (0.2119034982 * l[0] + 0.6806995451 * l[1] + 0.1073969566 * l[2]) ** (1 / 3)
    S_ = (0.0883024619 * l[0] + 0.2817188376 * l[1] + 0.6299787005 * l[2]) ** (1 / 3)
    return (0.2104542553 * L_ + 0.7936177850 * M_ - 0.0040720468 * S_, 1.9779984951 * L_ - 2.4285922050 * M_ + 0.4505937099 * S_,
            0.0259040371 * L_ + 0.7827717662 * M_ - 0.8086757660 * S_)
pal = {rgb(v) for v in P.values()}
for name in STAGES:
    im = Image.open(os.path.join(TEX, name + "_top.png")).convert("RGBA")
    cols = {p[:3] for p in im.get_flattened_data() if p[3] == 255}
    if cols - pal: fails.append(f"{name}: non-palette colours")
    n = len(cols - {rgb(P["m"])})
    if n > 6: fails.append(f"{name}: {n} colours (> 6)")
order = {c: k for k, c in enumerate(LEVEL)}
names = [f"solar_panel_round_dust{k}" for k in range(5)]
for k in range(1, 5):
    a, b = STAGES[names[k - 1]], STAGES[names[k]]
    for j in range(N):
        for i in range(N):
            if ROLE[j][i] == "cell" and order[b[j][i]] < order[a[j][i]]: fails.append(f"dust{k} not a superset at {i},{j}")
def mean(px):
    v = [ok(rgb(P[px[j][i]])) for j in range(N) for i in range(N) if ROLE[j][i] == "cell"]
    return tuple(sum(x[t] for x in v) / len(v) for t in range(3))
print("Dust steps on the cell area (OKLab dE, normal vision; need >= 0.035):")
for k in range(1, 5):
    d = math.dist(mean(STAGES[names[k]]), mean(STAGES[names[k - 1]]))
    print(f"  dust{k-1} -> dust{k}: {d:.3f}  {'ok' if d >= 0.035 else 'TOO SMALL'}")
    if d < 0.035: fails.append(f"step {k} too small")
print("Structure (seam or cut-out vs dominant cell colour, need >= 2:1 somewhere):")
from collections import Counter
for name, px in STAGES.items():
    dom = Counter(px[j][i] for j in range(N) for i in range(N) if ROLE[j][i] == "cell").most_common(1)[0][0]
    seam = "s" if px is STAGES[names[0]] or name.endswith(("dust1", "dust2")) else "S"
    best = max(cr(P[seam], P[dom]), cr(P["p"], P[dom]), cr(P["3"], P[dom]))
    print(f"  {name:34s} cells {dom}  seam {seam}  {best:.1f}  {'ok' if best >= 2 else 'WEAK'}")
    if best < 2: fails.append(f"{name} structure weak")
print(f"Softened veil seam S {P['S']} on veil D: {cr(P['S'], P['D']):.1f}:1 (target 2:1); cut-out p on veil D: {cr(P['p'], P['D']):.1f}:1")
print(f"Geometry: disc {len(runs)} cuboids + deck/legs/base = {len(runs) + 9} elements per model; "
      f"x/z extent {-16 + min(r[0][0] for r in runs)} to {-16 + max(r[0][1] for r in runs)} px (allowed -16..32)")
# partly cleaned must differ from stage 4 at a distance: mean cell colour dE >= 0.10 and a large contiguous clean area
pc, d4 = STAGES["solar_panel_round_partly_cleaned"], STAGES[names[4]]
dpc = math.dist(mean(pc), mean(d4))
seen, best = set(), 0
for j in range(N):
    for i in range(N):
        if pc[j][i] == "r" and (i, j) not in seen:
            stack, n = [(i, j)], 0; seen.add((i, j))
            while stack:
                x, y = stack.pop(); n += 1
                for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                    q = (x + dx, y + dy)
                    if 0 <= q[0] < N and 0 <= q[1] < N and q not in seen and pc[q[1]][q[0]] in ("r", "s", "S", "p") and ROLE[q[1]][q[0]] in ("cell", "seam", "cut"):
                        seen.add(q); stack.append(q)
            best = max(best, n)
cells = sum(1 for j in range(N) for i in range(N) if ROLE[j][i] == "cell")
print(f"Partly cleaned vs stage 4: mean cell dE {dpc:.3f} (need >= 0.10); largest connected clean area {best} px ({best / cells:.0%} of cells; need >= 10 %)")
if dpc < 0.10 or best / cells < 0.10: fails.append("partly cleaned not distinct enough from stage 4")
print("FAILS:" if fails else "All checks passed.", *fails, sep="\n  ")
sys.exit(1 if fails else 0)
