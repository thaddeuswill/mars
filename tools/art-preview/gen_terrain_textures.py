#!/usr/bin/env python3
"""Real 16x16 terrain textures for the W1/W2 blocks plus ripple_sand (Lead Artist, 2026-11-28).
Replaces the placeholders from gen_w2_placeholder_textures.py. Drawn from scratch, deterministic, palette ramps only.
Colour sources: docs/science/mars-surface-materials.md, docs/science/mars-terrain-reference.md.
Run: python3 -I tools/art-preview/gen_terrain_textures.py   (exits non-zero if a check fails)"""
import math
import os
import random
import sys
from PIL import Image

OUT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "..", "pack", "assets", "mars", "textures", "block")


def hx(s):
    return tuple(int(s[i:i + 2], 16) for i in (1, 3, 5))


def new(base):
    img = Image.new("RGBA", (16, 16))
    for x in range(16):
        for y in range(16):
            img.putpixel((x, y), base + (255,))
    return img


def px(img, x, y, c):
    img.putpixel((x % 16, y % 16), c + (255,))


def speckle(img, ramp, seed, weights):
    r = random.Random(seed)
    for x in range(16):
        for y in range(16):
            img.putpixel((x, y), r.choices(ramp, weights)[0] + (255,))


def voronoi(n, seed):
    """Wrapping voronoi: returns cell id and edge flag per pixel."""
    r = random.Random(seed)
    pts = [(r.uniform(0, 16), r.uniform(0, 16)) for _ in range(n)]
    ids, edge = {}, {}
    for x in range(16):
        for y in range(16):
            ds = []
            for i, (a, b) in enumerate(pts):
                dx = min(abs(x - a), 16 - abs(x - a))
                dy = min(abs(y - b), 16 - abs(y - b))
                ds.append((dx * dx + dy * dy, i))
            ds.sort()
            ids[(x, y)] = ds[0][1]
            edge[(x, y)] = (ds[1][0] ** 0.5 - ds[0][0] ** 0.5) < 0.9
    return ids, edge


def blob(img, cx, cy, w, h, cols):
    """cols = (light, mid, dark): top-left lit, bottom-right shaded."""
    for dx in range(w):
        for dy in range(h):
            if (w == 3 and h == 3) and dx + dy in (0, 4):
                continue
            c = cols[1]
            if dx + dy == 0 or (dx == 0 and dy == 0):
                c = cols[0]
            if dx == w - 1 and dy == h - 1 or (dy == h - 1 and w > 2 and dx > 0 and h > 1):
                c = cols[2]
            if dx == 0 and dy == 0:
                c = cols[0]
            px(img, cx + dx, cy + dy, c)


def basaltic_sand():
    ramp = [hx(s) for s in ("#2E2824", "#3E3530", "#4F443C", "#665A4F")]
    img = new(ramp[1])
    speckle(img, ramp, 11, [3, 8, 5, 1])
    return img


def ripple_sand():
    """Same dark sand, with soft wavy crests (period 8 px, diagonal tilt, wobble) at low contrast:
    one ramp step, so it reads as wind-shaped sand and not as black/brown stripes."""
    ramp = [hx(s) for s in ("#2E2824", "#3E3530", "#4F443C", "#665A4F")]
    r = random.Random(21)
    img = new(ramp[1])
    for x in range(16):
        for y in range(16):
            wob = 1.1 * math.sin(2 * math.pi * x / 16 * 2 + 0.7) + 0.5 * math.sin(2 * math.pi * x / 16 * 3)
            ph = ((y + 0.5 * x + wob) % 8) / 8.0  # 0..1 across one ripple
            if ph < 0.22:
                i = 2  # crest (lit)
            elif ph < 0.62:
                i = 1
            else:
                i = 0 if ph > 0.86 else 1  # thin trough shadow
            if r.random() < 0.22:
                i = max(0, min(3, i + r.choice((-1, 1))))
            img.putpixel((x, y), ramp[i] + (255,))
    return img


def hematite_ore():
    rock = [hx(s) for s in ("#3F3B3A", "#4F4A48", "#605A57")]
    img = new(rock[1])
    speckle(img, rock, 31, [3, 6, 3])
    red = [hx("#7A2E22"), hx("#9A3F2B"), hx("#B5543A")]
    grey = [hx("#7C8185"), hx("#9AA0A4"), hx("#5E6367")]
    r = random.Random(32)
    for _ in range(5):  # rust-red ore lumps
        x, y = r.randint(0, 15), r.randint(0, 15)
        w, h = r.choice(((3, 2), (2, 3), (3, 3), (2, 2)))
        for dx in range(w):
            for dy in range(h):
                if w * h > 4 and dx + dy == 0 and r.random() < .5:
                    continue
                px(img, x + dx, y + dy, red[1] if (dx + dy) % 2 == 0 else red[0])
        px(img, x, y, red[2])
    for _ in range(7):  # grey metallic hematite flecks
        px(img, r.randint(0, 15), r.randint(0, 15), r.choice(grey))
    return img


def dark_sand_ore():
    ramp = [hx(s) for s in ("#2F1D1A", "#3F2822", "#53332A", "#693E2F")]
    img = new(ramp[1])
    speckle(img, ramp, 41, [3, 6, 4, 1])
    ore = [hx("#A8683F"), hx("#C08450"), hx("#7C4B33")]
    r = random.Random(42)
    for _ in range(5):  # lighter ore grains in small clusters
        x, y = r.randint(0, 15), r.randint(0, 15)
        px(img, x, y, ore[1])
        px(img, x + 1, y, ore[2])
        if r.random() < .6:
            px(img, x, y + 1, ore[0])
    return img


def ground_ice():
    """Dusty water ice: neutral pale grey-white (not blue), dust specks and hairline cracks. No wet shine."""
    ramp = [hx(s) for s in ("#B9BFC2", "#CDD2D4", "#E4E8EA")]
    img = new(ramp[1])
    speckle(img, ramp, 51, [3, 6, 4])
    dust = [hx("#8A6F5C"), hx("#A58468")]
    r = random.Random(52)
    for _ in range(14):
        px(img, r.randint(0, 15), r.randint(0, 15), r.choice(dust))
    crack = hx("#8F979B")
    x, y = 3, 0
    for _ in range(16):
        px(img, x, y, crack)
        y += 1
        x += r.choice((-1, 0, 1, 1))
    return img


def duricrust():
    """Cemented tan crust: plates with lighter lifted edges and cracks (NTRS 20050203800). 2026-11-28: cooler and
    lower contrast (was ochre #8E6A44..#B98F5F, read as wood)."""
    ramp = [hx(s) for s in ("#8A7656", "#9A8664", "#A89473")]
    crack, lip = hx("#6A5A42"), hx("#B5A383")
    ids, edge = voronoi(7, 61)
    r = random.Random(62)
    tone = {i: r.choice((0, 1, 1, 2)) for i in range(7)}
    img = new(ramp[1])
    for x in range(16):
        for y in range(16):
            c = ramp[tone[ids[(x, y)]]]
            if r.random() < .15:
                c = ramp[(tone[ids[(x, y)]] + 1) % 3]
            img.putpixel((x, y), c + (255,))
    for (x, y), e in edge.items():
        if e:
            px(img, x, y, crack)
            if not edge[((x + 1) % 16, y)] and not edge[(x, (y - 1) % 16)] and r.random() < .5:
                pass
    for (x, y), e in edge.items():  # lip: a light pixel just right/above a crack
        if not e and edge[((x - 1) % 16, y)] and r.random() < .6:
            px(img, x, y, lip)
    return img


def vesicular_basalt():
    ramp = [hx(s) for s in ("#303133", "#3E3F42", "#4D4E52", "#5E5F63")]
    img = new(ramp[1])
    speckle(img, ramp, 71, [3, 6, 4, 1])
    hole, rim = hx("#1C1D1F"), hx("#6E6F73")
    r = random.Random(72)
    taken = []
    while len(taken) < 11:
        x, y = r.randint(0, 15), r.randint(0, 15)
        if any(abs(x - a) < 3 and abs(y - b) < 3 for a, b in taken):
            continue
        taken.append((x, y))
        big = len(taken) % 3 == 0
        cells = [(0, 0), (1, 0), (0, 1), (1, 1)] if big else [(0, 0)] + ([(1, 0)] if r.random() < .5 else [])
        for dx, dy in cells:
            px(img, x + dx, y + dy, hole)
        wx = max(dx for dx, _ in cells) + 1
        hy = max(dy for _, dy in cells) + 1
        for dx, _ in cells:
            px(img, x + dx, y + hy, rim)  # lit lower lip
    return img


def spherule_sand():
    """Dark sand with grey hematite spheres. Neutral grey, NOT blue (Opportunity, Meridiani)."""
    ramp = [hx(s) for s in ("#2E2824", "#3E3530", "#4F443C")]
    img = new(ramp[1])
    speckle(img, ramp, 81, [3, 7, 3])
    ball = (hx("#B3B6B8"), hx("#8C8F91"), hx("#5D6062"))
    spots = [(1, 2, 3), (9, 0, 3), (6, 6, 2), (12, 7, 3), (2, 10, 2), (8, 11, 3), (13, 13, 2), (4, 14, 2)]
    for x, y, d in spots:
        if d == 3:
            for dx in range(3):
                for dy in range(3):
                    if (dx, dy) in ((0, 0), (2, 0), (0, 2), (2, 2)):
                        continue
                    px(img, x + dx, y + dy, ball[1])
            for dx, dy in ((1, 0), (0, 1)):
                px(img, x + dx, y + dy, ball[0])
            for dx, dy in ((1, 2), (2, 1)):
                px(img, x + dx, y + dy, ball[2])
        else:
            px(img, x, y, ball[0])
            px(img, x + 1, y, ball[1])
            px(img, x, y + 1, ball[1])
            px(img, x + 1, y + 1, ball[2])
    return img


def impact_breccia():
    matrix = [hx("#2F2A28"), hx("#3A3431")]
    frag = [hx(s) for s in ("#6F6459", "#857862", "#58545A", "#9A8872", "#4B4E52")]
    ids, edge = voronoi(9, 91)
    r = random.Random(92)
    tone = {i: r.choice(frag) for i in range(9)}
    img = new(matrix[0])
    for x in range(16):
        for y in range(16):
            if edge[(x, y)]:
                c = r.choice(matrix)
            else:
                c = tone[ids[(x, y)]]
                if r.random() < .12:
                    c = r.choice(frag)
            img.putpixel((x, y), c + (255,))
    return img


def gypsum_vein():
    ramp = [hx(s) for s in ("#4A4745", "#585451", "#67625D")]
    img = new(ramp[1])
    speckle(img, ramp, 101, [3, 6, 3])
    vein, soft = hx("#E8E4D8"), hx("#BDB8AC")
    r = random.Random(102)
    for start, slope in ((2, 1), (10, -1)):  # two branching, wrapping veins
        y = start
        for x in range(16):
            px(img, x, y, vein)
            if r.random() < .25:
                px(img, x, y + 1, soft)
            if r.random() < .45:
                y += slope
    for _ in range(3):  # tiny side branches
        x, y = r.randint(0, 15), r.randint(0, 15)
        px(img, x, y, soft)
    return img


def sulfide_vein():
    """INVENTED conductor ore. Grey-brown rock first; rust-orange stains and a few dull gold flecks. Never green,
    never bright copper (SME mars-conductor-and-carbonate-ores.md section 3)."""
    rock = [hx(s) for s in ("#403A36", "#4E4743", "#5E5650")]
    img = new(rock[1])
    speckle(img, rock, 111, [3, 6, 3])
    rust = [hx("#7C4A30"), hx("#94593A")]
    gold = [hx("#9C8340"), hx("#B79B52")]
    r = random.Random(112)
    for _ in range(4):  # rust stains, 2-3 px diagonal runs
        x, y = r.randint(0, 15), r.randint(0, 15)
        for i in range(r.choice((2, 3))):
            px(img, x + i, y + i // 2, rust[i % 2])
    for _ in range(5):  # small dull gold flecks (1 px, one with a shade neighbour)
        x, y = r.randint(0, 15), r.randint(0, 15)
        px(img, x, y, gold[1])
        if r.random() < .5:
            px(img, x + 1, y, gold[0])
    return img


def magnetic_ore():
    """INVENTED conductor ore. Dark metallic stone, red-brown mineral flecks (not bright red: not redstone),
    a few cool-grey metallic glints."""
    rock = [hx(s) for s in ("#2A2A2C", "#363638", "#444446")]
    img = new(rock[1])
    speckle(img, rock, 121, [3, 6, 3])
    fleck = [hx("#6E3328"), hx("#8A4332")]
    glint = [hx("#7A7D82"), hx("#9A9DA2")]
    r = random.Random(122)
    for _ in range(6):  # red-brown flecks, 1 to 2 px
        x, y = r.randint(0, 15), r.randint(0, 15)
        px(img, x, y, fleck[1])
        if r.random() < .6:
            px(img, x + 1, y, fleck[0])
        if r.random() < .25:
            px(img, x, y + 1, fleck[0])
    for _ in range(7):  # metallic glints
        px(img, r.randint(0, 15), r.randint(0, 15), r.choice(glint))
    return img


def carbonate_rock():
    """REAL idea at game size (Jezero, Nili Fossae). Pale tan-grey rock, faint blocky veins."""
    ramp = [hx(s) for s in ("#AFA898", "#BFB8A8", "#CFC9BA")]
    img = new(ramp[1])
    speckle(img, ramp, 131, [3, 6, 3])
    vein, shade = hx("#E2DDD0"), hx("#948D7E")
    r = random.Random(132)
    for start in (4, 11):
        y = start
        for x in range(16):
            px(img, x, y, vein if r.random() < .8 else ramp[2])
            if r.random() < .15:
                px(img, x, y + 1, shade)
            if r.random() < .3:
                y += r.choice((-1, 1))
    for _ in range(5):
        px(img, r.randint(0, 15), r.randint(0, 15), shade)
    return img


def carbonate_item():
    """Item sprite: a small pale chunk on a transparent background (the only texture here with clear pixels)."""
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    rows = {  # y: (x0, x1)
        5: (6, 9), 6: (5, 10), 7: (4, 11), 8: (4, 11), 9: (4, 11), 10: (5, 11), 11: (6, 10), 12: (7, 9)}
    light, mid, dark, edge = hx("#DDD8CA"), hx("#C4BDAD"), hx("#A59E8E"), hx("#6E675A")
    for y, (a, b) in rows.items():
        for x in range(a, b + 1):
            c = mid
            if y <= 7 and x <= a + 2:
                c = light
            if y >= 10 or x >= b - 1:
                c = dark
            if x in (a, b) or y in (5, 12):
                c = edge
            px(img, x, y, c)
    px(img, 7, 7, hx("#E8E4D8"))
    px(img, 8, 8, dark)
    px(img, 6, 9, dark)
    return img


TEXTURES = {
    "basaltic_sand": basaltic_sand, "ripple_sand": ripple_sand, "hematite_ore": hematite_ore,
    "dark_sand_ore": dark_sand_ore, "ground_ice": ground_ice, "duricrust": duricrust,
    "vesicular_basalt": vesicular_basalt, "spherule_sand": spherule_sand,
    "impact_breccia": impact_breccia, "gypsum_vein": gypsum_vein,
    "sulfide_vein": sulfide_vein, "magnetic_ore": magnetic_ore, "carbonate_rock": carbonate_rock,
}


def check(name, img):
    errs = []
    if img.size != (16, 16) or img.mode != "RGBA":
        errs.append("size/mode")
    cols = set()
    for x in range(16):
        for y in range(16):
            r_, g, b, a = img.getpixel((x, y))
            cols.add((r_, g, b))
            if a != 255:
                errs.append("alpha")
            if g > max(r_, b) + 2:
                errs.append("greenish %s" % ((r_, g, b),))
            if max(r_, g, b) < 8 or min(r_, g, b) > 247:
                errs.append("pure black/white")
            # no liquid-water look: blue must never dominate by more than 12 (ice is neutral)
            if b > r_ + 12:
                errs.append("bluish %s" % ((r_, g, b),))
    if len(cols) > 16:
        errs.append("too many colours %d" % len(cols))
    return errs, len(cols)


def main():
    bad = 0
    for name, fn in TEXTURES.items():
        img = fn()
        errs, n = check(name, img)
        img.save(os.path.join(OUT, name + ".png"))
        print("%-18s %2d colours %s" % (name, n, "OK" if not errs else "FAIL " + "; ".join(sorted(set(errs)))))
        bad += bool(errs)
    item = carbonate_item()
    errs = []
    solid = 0
    for x in range(16):
        for y in range(16):
            r_, g, b, a = item.getpixel((x, y))
            if a == 0:
                continue
            solid += 1
            if a != 255 or g > max(r_, b) + 2 or b > r_ + 12 or max(r_, g, b) < 8 or min(r_, g, b) > 247:
                errs.append("bad pixel %s" % ((r_, g, b, a),))
    if not 40 <= solid <= 80:
        errs.append("chunk size %d px" % solid)
    item.save(os.path.join(OUT, "..", "item", "carbonate.png"))
    print("%-18s %2d solid px %s" % ("item/carbonate", solid, "OK" if not errs else "FAIL " + "; ".join(sorted(set(errs)))))
    bad += bool(errs)
    sys.exit(1 if bad else 0)


if __name__ == "__main__":
    main()
