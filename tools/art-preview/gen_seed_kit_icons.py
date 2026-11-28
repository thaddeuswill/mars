#!/usr/bin/env python3
"""Seed kit item icons (Lead Artist, 2026-11-28). PLACEHOLDER-grade, final-style pixel art: a small seed packet, paper body,
ink outline, folded crimped top, one colour band plus one symbol per kit (shape carries the meaning, not only hue).
No green, no purple (purple = made-up machine band). Band colours are Okabe-Ito. Spec: docs/design/seed-kit-recipes.md.
Writes pack/assets/mars/textures/item/{biocrust,meadow,wetland,forest}_kit.png and repoints their item models.
Run: python3 -I tools/art-preview/gen_seed_kit_icons.py"""
import json, os
from PIL import Image
ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "..", "pack", "assets", "mars")
def hx(s): return tuple(int(s[i:i + 2], 16) for i in (1, 3, 5)) + (255,)
INK, PAPER, PAPER_D, FOLD = hx("#2A2420"), hx("#E7E2D6"), hx("#C9C2B2"), hx("#D3CDBF")
BANDS = {"biocrust_kit": hx("#D55E00"), "meadow_kit": hx("#F0E442"), "wetland_kit": hx("#56B4E9"), "forest_kit": hx("#0072B2")}
def packet(name):
    im = Image.new("RGBA", (16, 16), (0, 0, 0, 0)); p = im.putpixel
    for y in range(2, 15):
        for x in range(3, 13): p((x, y), PAPER)
    for y in range(2, 15): p((12, y), PAPER_D)          # shade right edge
    for x in range(3, 13): p((x, 14), PAPER_D)
    for x in range(3, 13):                              # crimped fold at the top
        p((x, 2), INK if x % 2 else FOLD); p((x, 3), FOLD)
    for y in range(2, 15): p((2, y), INK); p((13, y), INK)
    for x in range(3, 13): p((x, 1), INK); p((x, 15), INK)
    for x in range(3, 13):                              # colour band, 3 rows
        for y in (6, 7, 8): p((x, y), BANDS[name])
    for x in range(3, 13): p((x, 5), INK); p((x, 9), INK)
    sym = {  # 4-5 px ink symbol in the lower panel (rows 10..13), distinct silhouettes
        "biocrust_kit": [(5, 12), (7, 11), (8, 13), (10, 12), (6, 13), (10, 10)],     # scattered crumbs
        "meadow_kit": [(5, 13), (5, 12), (5, 11), (7, 13), (7, 12), (8, 11), (9, 13), (9, 12), (10, 10), (9, 11)],  # grass tufts
        "wetland_kit": [(7, 13), (7, 12), (7, 11), (7, 10), (6, 10), (8, 10), (10, 13), (10, 12), (10, 11)],        # reeds with a head
        "forest_kit": [(6, 12), (7, 12), (6, 11), (7, 11), (9, 13), (10, 13), (9, 12), (10, 12)],                     # two berries
    }[name]
    for xy in sym: p(xy, INK)
    im.save(os.path.join(ROOT, "textures", "item", name + ".png"))
    mp = os.path.join(ROOT, "models", "item", name + ".json")
    json.dump({"parent": "minecraft:item/generated", "textures": {"layer0": "mars:item/" + name}}, open(mp, "w"), indent=1)
for n in BANDS: packet(n)
# checks: distinct, no green, 16x16
ims = {n: Image.open(os.path.join(ROOT, "textures", "item", n + ".png")).convert("RGBA") for n in BANDS}
names = list(BANDS)
for i in range(4):
    for j in range(i + 1, 4): assert ims[names[i]].tobytes() != ims[names[j]].tobytes()
for n, im in ims.items():
    assert im.size == (16, 16)
    for r, g, b, a in im.getdata(): assert not (a and g > r + 20 and g > b + 20), (n, (r, g, b))
print("seed kit icons ok")
