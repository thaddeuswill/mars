#!/usr/bin/env python3
"""PLACEHOLDER 16x16 item icons for the gate input items: heater_coil, filter_cartridge, pump_bearing
(Technical Lead, 2026-11-28; docs/design/gate-input-items.md). Hand-scripted, deterministic, transparent
corners, ink outline, no green and no purple (purple is the made-up band). Shape carries the meaning:
a coiled wire, a ribbed filter drum, a ring with a hole. Never overwrites an existing PNG or model, so
the Lead Artist's real art stays; the Artist replaces these (same names).
Run: python3 -I tools/art-preview/gen_gate_item_textures.py"""
import json
import os
from PIL import Image

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "..", "pack", "assets", "mars")


def hx(s):
    return tuple(int(s[i:i + 2], 16) for i in (1, 3, 5)) + (255,)


INK, ALLOY, ALLOY_L, ALLOY_D = hx("#2A2420"), hx("#7E8790"), hx("#A8B0B7"), hx("#59616A")
ORANGE, ORANGE_D = hx("#E69F00"), hx("#B37400")
PAPER, PAPER_D = hx("#E7E2D6"), hx("#C9C2B2")


def canvas():
    return Image.new("RGBA", (16, 16), (0, 0, 0, 0))


def coil():
    im = canvas()
    p = im.putpixel
    # a wound coil: five turns of orange wire on an alloy spindle, two lead wires
    for x in range(3, 13):
        p((x, 4), INK)
        p((x, 12), INK)
    for y in range(5, 12):
        p((3, y), INK)
        p((12, y), INK)
    for x in range(4, 12):
        for y in range(5, 12):
            p((x, y), ALLOY_D)
    for y in (5, 7, 9, 11):
        for x in range(4, 12):
            p((x, y), ORANGE if (x + y) % 4 else ORANGE_D)
    p((2, 6), ORANGE_D)
    p((1, 6), ORANGE_D)
    p((13, 10), ORANGE_D)
    p((14, 10), ORANGE_D)
    return im


def cartridge():
    im = canvas()
    p = im.putpixel
    # a ribbed drum: alloy end caps, pale paper pleats between them
    for x in range(4, 12):
        p((x, 2), INK)
        p((x, 14), INK)
    for y in range(3, 14):
        p((3, y), INK)
        p((12, y), INK)
    for x in range(4, 12):
        for y in range(3, 14):
            p((x, y), PAPER if x % 2 == 0 else PAPER_D)
    for x in range(4, 12):
        for y in (3, 4, 12, 13):
            p((x, y), ALLOY if y in (4, 12) else ALLOY_L)
    return im


def bearing():
    im = canvas()
    p = im.putpixel
    # a ring with a hole and three ball marks
    cx = cy = 7.5
    for x in range(16):
        for y in range(16):
            d = ((x - cx) ** 2 + (y - cy) ** 2) ** 0.5
            if 5.6 < d <= 6.6:
                p((x, y), INK)
            elif 3.6 < d <= 5.6:
                p((x, y), ALLOY_L if x + y < 15 else ALLOY)
            elif 2.6 < d <= 3.6:
                p((x, y), INK)
    for xy in ((7, 4), (4, 9), (10, 9)):
        p(xy, ALLOY_D)
    return im


def sorbent_bed():
    im = canvas()
    p = im.putpixel
    # a flat canister of porous pellets: alloy frame, pale pellet dots, two small ports on top
    for x in range(2, 14):
        p((x, 4), INK)
        p((x, 13), INK)
    for y in range(5, 13):
        p((2, y), INK)
        p((13, y), INK)
    for x in range(3, 13):
        for y in range(5, 13):
            p((x, y), ALLOY_D)
    for x in range(3, 13):
        for y in range(5, 13):
            if (x * 3 + y * 5) % 4 == 0:
                p((x, y), PAPER)
            elif (x + y) % 3 == 0:
                p((x, y), PAPER_D)
    for x in (4, 11):
        p((x, 3), INK)
        p((x, 2), ALLOY_L)
    return im


def save(name, im):
    png = os.path.join(ROOT, "textures", "item", name + ".png")
    if os.path.exists(png):
        print("exists, left alone:", name)
    else:
        im.save(png)
        print("wrote", name)
    model = os.path.join(ROOT, "models", "item", name + ".json")
    if not os.path.exists(model):
        with open(model, "w") as f:
            json.dump({"parent": "minecraft:item/generated", "textures": {"layer0": "mars:item/" + name}}, f, indent=1)
            f.write("\n")


save("heater_coil", coil())
save("filter_cartridge", cartridge())
save("pump_bearing", bearing())
save("sorbent_bed", sorbent_bed())
