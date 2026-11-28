#!/usr/bin/env python3
"""PLACEHOLDER 16x16 item icons for the gate input items: heater_coil, filter_cartridge, pump_bearing, sorbent_bed, machine_core, resupply_request, resin_pack, signal_board, activation_cell
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


GOLD, GOLD_D = hx("#C9A227"), hx("#8F7414")
AMBER, AMBER_D = hx("#C98A2B"), hx("#8F5F18")
SLATE, SLATE_D, TRACE = hx("#3C5A7A"), hx("#2A4058"), hx("#E8C86A")


def machine_core():
    im = canvas()
    p = im.putpixel
    # a grey box with a gold band and a pipe stub on the right (orbit-sent tech, invented)
    for x in range(2, 12):
        p((x, 3), INK)
        p((x, 13), INK)
    for y in range(4, 13):
        p((2, y), INK)
        p((11, y), INK)
    for x in range(3, 11):
        for y in range(4, 13):
            p((x, y), ALLOY if y < 8 else ALLOY_D)
    for x in range(3, 11):
        p((x, 4), ALLOY_L)
        for y in (7, 8):
            p((x, y), GOLD if y == 7 else GOLD_D)
    for x in range(12, 15):
        p((x, 6), INK)
        p((x, 7), ALLOY_L)
        p((x, 8), ALLOY)
        p((x, 9), INK)
    p((11, 7), ALLOY)
    p((11, 8), ALLOY)
    for xy in ((4, 10), (6, 10), (8, 10)):
        p(xy, INK)
    return im


def resupply_request():
    im = canvas()
    p = im.putpixel
    # a small card with a flare: an orange dot with two arcs
    for x in range(3, 13):
        p((x, 2), INK)
        p((x, 13), INK)
    for y in range(3, 13):
        p((3, y), INK)
        p((12, y), INK)
    for x in range(4, 12):
        for y in range(3, 13):
            p((x, y), PAPER if y < 11 else PAPER_D)
    p((7, 9), ORANGE)
    p((8, 9), ORANGE)
    p((7, 8), ORANGE_D)
    p((8, 8), ORANGE_D)
    for xy in ((5, 6), (6, 5), (7, 5), (8, 5), (9, 5), (10, 6)):
        p(xy, ORANGE)
    for xy in ((6, 7), (9, 7)):
        p(xy, ORANGE_D)
    return im


def resin_pack():
    im = canvas()
    p = im.putpixel
    # a sealed packet of amber beads
    for x in range(4, 12):
        p((x, 2), INK)
        p((x, 14), INK)
    for y in range(3, 14):
        p((3, y), INK)
        p((12, y), INK)
    for x in range(4, 12):
        for y in range(3, 14):
            p((x, y), PAPER_D if y in (3, 4) else PAPER)
    for xy in ((5, 7), (7, 6), (9, 7), (6, 9), (8, 9), (10, 9), (5, 11), (7, 11), (9, 11)):
        p(xy, AMBER)
        p((xy[0] + 1, xy[1]), AMBER_D)
    return im


def signal_board():
    im = canvas()
    p = im.putpixel
    # a slate-blue circuit board with pale-gold traces and two chips (blue, not green)
    for x in range(1, 15):
        p((x, 3), INK)
        p((x, 12), INK)
    for y in range(4, 12):
        p((1, y), INK)
        p((14, y), INK)
    for x in range(2, 14):
        for y in range(4, 12):
            p((x, y), SLATE if (x + y) % 5 else SLATE_D)
    for x in range(3, 9):
        p((x, 6), TRACE)
    for y in range(6, 10):
        p((8, y), TRACE)
    for x in range(8, 13):
        p((x, 9), TRACE)
    for x in (10, 11):
        for y in (5, 6):
            p((x, y), INK)
    for x in (3, 4):
        for y in (9, 10):
            p((x, y), ALLOY_D)
    return im


def activation_cell():
    im = canvas()
    p = im.putpixel
    # an upright power cell: alloy body, orange charge band, a small top terminal
    for x in range(5, 11):
        p((x, 3), INK)
        p((x, 14), INK)
    for y in range(4, 14):
        p((4, y), INK)
        p((11, y), INK)
    for x in range(5, 11):
        for y in range(4, 14):
            p((x, y), ALLOY if x < 8 else ALLOY_D)
    for x in range(5, 11):
        for y in (7, 8, 9):
            p((x, y), ORANGE if y != 9 else ORANGE_D)
    p((7, 2), INK)
    p((8, 2), INK)
    p((7, 1), ALLOY_L)
    p((8, 1), ALLOY_L)
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
save("machine_core", machine_core())
save("resupply_request", resupply_request())
save("resin_pack", resin_pack())
save("signal_board", signal_board())
save("activation_cell", activation_cell())
