#!/usr/bin/env python3
"""Placeholder 16x16 textures for sulfide_vein, magnetic_ore, carbonate_rock, the carbonate item and
(only if absent) ripple_sand. ADD-ONLY: never overwrites an existing file (the Lead Artist edits
textures at the same time). Drawn from scratch, deterministic. Run: python3 -I gen_conductor_placeholder_textures.py"""
import os
import random
from PIL import Image

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "..", "pack", "assets", "mars", "textures")


def noisy(base, spread, seed):
    r = random.Random(seed)
    img = Image.new("RGBA", (16, 16))
    for x in range(16):
        for y in range(16):
            d = r.randint(-spread, spread)
            img.putpixel((x, y), tuple(max(0, min(255, c + d)) for c in base) + (255,))
    return img


def flecks(img, colors, n, seed):
    r = random.Random(seed)
    for _ in range(n):
        x, y = r.randint(0, 15), r.randint(0, 15)
        img.putpixel((x, y), r.choice(colors) + (255,))
        if r.random() < 0.5 and x < 15:
            img.putpixel((x + 1, y), r.choice(colors) + (255,))


def save(img, kind, name):
    path = os.path.join(ROOT, kind, name + ".png")
    if os.path.exists(path):
        print("exists, kept:", path)
        return
    img.save(path)
    print("wrote", path)


# sulfide_vein: grey-brown rock, rust and dull gold flecks (not green, no bright copper)
img = noisy((98, 88, 80), 8, 11)
flecks(img, [(150, 72, 40), (166, 120, 52), (140, 100, 48)], 20, 12)
save(img, "block", "sulfide_vein")

# magnetic_ore: dark grey rock with metallic speckle
img = noisy((58, 58, 62), 6, 13)
flecks(img, [(150, 156, 164), (110, 116, 124), (176, 180, 186)], 18, 14)
save(img, "block", "magnetic_ore")

# carbonate_rock: pale warm-grey rock with fine light bands
img = noisy((176, 166, 152), 7, 15)
for y in (3, 8, 12):
    for x in range(16):
        c = img.getpixel((x, y))
        img.putpixel((x, y), tuple(min(255, v + 16) for v in c[:3]) + (255,))
save(img, "block", "carbonate_rock")

# carbonate (item): a pale chunk
item = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
r = random.Random(16)
for x in range(3, 13):
    for y in range(4, 13):
        if (x - 8) ** 2 / 25.0 + (y - 8) ** 2 / 20.0 <= 1.0:
            d = r.randint(-8, 8)
            item.putpixel((x, y), (190 + d, 180 + d, 164 + d, 255))
save(item, "item", "carbonate")

# ripple_sand: a muted tan stripe sand (the Lead Artist is making the real one)
img = noisy((150, 118, 88), 6, 17)
save(img, "block", "ripple_sand")
