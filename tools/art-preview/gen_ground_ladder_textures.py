#!/usr/bin/env python3
"""Placeholder 16x16 textures for the ground ladder blocks: washed_regolith, rich_soil, biocrust.
Drawn from scratch (no vanilla pixels), deterministic, same conventions as gen_terrain_textures.py
(small noise on a base colour, opaque, no green or blue cast on Mars ground, biocrust is a dark
grey-brown crust with paler patches: the stand-in for a living crust, not a lawn).
Never overwrites an existing PNG, so the Lead Artist's real art stays. The Artist replaces these
(same names). Run: python3 -I gen_ground_ladder_textures.py"""
import os
import random
from PIL import Image

OUT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "..", "pack", "assets", "mars", "textures", "block")


def noisy(base, spread, seed):
    r = random.Random(seed)
    img = Image.new("RGB", (16, 16))
    for x in range(16):
        for y in range(16):
            d = r.randint(-spread, spread)
            img.putpixel((x, y), tuple(max(0, min(255, c + d)) for c in base))
    return img


def save(img, name):
    path = os.path.join(OUT, name + ".png")
    if os.path.exists(path):
        print("exists, left alone:", name)
        return
    img.save(path)
    print("wrote", name)


# washed regolith: the red dust rinsed paler and greyer, with a few darker grains
img = noisy((158, 118, 92), 8, 11)
r = random.Random(12)
for _ in range(10):
    img.putpixel((r.randint(0, 15), r.randint(0, 15)), (104, 82, 68))
save(img, "washed_regolith")

# rich soil: dark red-brown with small darker crumbs (mixed-in organic matter)
img = noisy((86, 58, 44), 7, 13)
r = random.Random(14)
for _ in range(22):
    x, y = r.randint(0, 14), r.randint(0, 14)
    for dx, dy in ((0, 0), (1, 0)):
        img.putpixel((x + dx, y + dy), (50, 34, 28))
for _ in range(8):
    img.putpixel((r.randint(0, 15), r.randint(0, 15)), (122, 88, 66))
save(img, "rich_soil")

# biocrust: dark grey-brown crust, paler flaky patches, no green
img = noisy((70, 62, 54), 6, 15)
r = random.Random(16)
for _ in range(6):
    x0, y0 = r.randint(0, 12), r.randint(0, 12)
    w, h = r.randint(2, 4), r.randint(2, 3)
    for x in range(x0, min(16, x0 + w)):
        for y in range(y0, min(16, y0 + h)):
            img.putpixel((x, y), (106, 94, 80))
save(img, "biocrust")
