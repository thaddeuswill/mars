#!/usr/bin/env python3
"""Placeholder 16x16 textures for the W2 Tier A surface blocks. Drawn from scratch (no vanilla pixels),
deterministic. The Lead Artist replaces the PNGs (same names). Run: python3 -I gen_w2_placeholder_textures.py"""
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
    img.save(os.path.join(OUT, name + ".png"))


# duricrust: pale tan-brown plates with dark crack lines
img = noisy((176, 138, 102), 9, 1)
for x in range(16):
    for y in range(16):
        if (x + (y // 5) * 3) % 8 == 0 or y % 5 == 0 and (x // 3) % 2 == 0:
            img.putpixel((x, y), (122, 94, 70))
save(img, "duricrust")

# vesicular basalt: dark grey-brown with round gas holes
img = noisy((74, 68, 64), 8, 2)
r = random.Random(3)
for _ in range(9):
    cx, cy = r.randint(1, 14), r.randint(1, 14)
    for dx, dy in ((0, 0), (1, 0), (0, 1), (1, 1)):
        x, y = min(15, cx + dx), min(15, cy + dy)
        img.putpixel((x, y), (28, 26, 26))
save(img, "vesicular_basalt")

# spherule sand: dark sand with grey-silver dots (grey, not blue)
img = noisy((76, 63, 56), 7, 4)
r = random.Random(5)
for _ in range(14):
    x, y = r.randint(0, 15), r.randint(0, 15)
    img.putpixel((x, y), (154, 160, 164))
    if r.random() < 0.4 and x < 15:
        img.putpixel((x + 1, y), (120, 126, 130))
save(img, "spherule_sand")

# impact breccia: dark matrix with angular paler fragments
img = noisy((66, 58, 54), 6, 6)
r = random.Random(7)
for _ in range(7):
    x0, y0 = r.randint(0, 12), r.randint(0, 12)
    w, h = r.randint(2, 4), r.randint(2, 3)
    col = r.choice([(118, 104, 94), (140, 120, 100), (92, 88, 90)])
    for x in range(x0, min(16, x0 + w)):
        for y in range(y0, min(16, y0 + h)):
            if (x - x0) + (y - y0) < w + h - 2:
                img.putpixel((x, y), col)
save(img, "impact_breccia")

# gypsum vein: grey rock with thin bright white veins
img = noisy((110, 106, 100), 7, 8)
r = random.Random(9)
for start in (2, 9):
    for x in range(16):
        y = (start + x // 2 + (r.randint(0, 1))) % 16
        img.putpixel((x, y), (232, 228, 218))
        if r.random() < 0.3:
            img.putpixel((x, (y + 1) % 16), (206, 202, 194))
save(img, "gypsum_vein")
