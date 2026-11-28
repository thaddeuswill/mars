#!/usr/bin/env python3
"""Running-state art (Lead Artist, 2026-11-28). Spec: docs/art/running-state-table.md.
Reads the EXISTING machine textures and derives NEW files only (never rewrites an existing texture):
  <machine>_front_off / _front_idle (lamp states), <part>_running (animated strips + .mcmeta),
  battery_front_charge0..2, and the model variants <machine>_off/_idle/_running.json (+ battery_charge0..2).
Nothing here is referenced by a blockstate yet: the Technical Lead adds the property (see the spec).
Frame counts and ticks are INVENTED. Run: python3 -I tools/art-preview/gen_running_states.py"""
import copy
import json
import os
import sys
from PIL import Image

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "..", "pack", "assets", "mars")
TEX = os.path.join(ROOT, "textures", "block")
MOD = os.path.join(ROOT, "models", "block")


def hx(s):
    return tuple(int(s[i:i + 2], 16) for i in (1, 3, 5)) + (255,)


SCREEN, INFO, GREEN = hx("#1E2328"), hx("#56B4E9"), hx("#009E73")
HOT, DIM = hx("#FFD27A"), hx("#B37400")      # oven coil: bright notch on a dim coil
LIT_SEG = hx("#D9DEE2")                      # processor canister lit segment
GLOW = hx("#F0E442")                         # emitter vent glow (Okabe-Ito yellow)
SURF = hx("#D5E0E8")                         # reclaimer water surface highlight
DUST, DUST_L = hx("#B8A890"), hx("#CDBFAA")  # outgasser puff
MESSAGES = []


def load(n):
    return Image.open(os.path.join(TEX, n + ".png")).convert("RGBA")


def save(im, n):
    im.save(os.path.join(TEX, n + ".png"))


def strip(frames, n, frametime):
    s = Image.new("RGBA", (16, 16 * len(frames)))
    for i, f in enumerate(frames):
        s.paste(f, (0, 16 * i))
    save(s, n)
    with open(os.path.join(TEX, n + ".png.mcmeta"), "w") as fh:
        json.dump({"animation": {"frametime": frametime}}, fh, indent=1)
    MESSAGES.append(f"{n}: {len(frames)} frames x {frametime} ticks")


def where(im, col):
    return [(x, y) for y in range(16) for x in range(16) if im.getpixel((x, y)) == col]


def paint(im, pts, col):
    for p in pts:
        im.putpixel(p, col)
    return im


def model(src, dst, swap, extra=None, render_type=None):
    m = json.load(open(os.path.join(MOD, src + ".json")))
    m = copy.deepcopy(m)
    for k, v in swap.items():
        assert k in m["textures"], (src, k)
        m["textures"][k] = "mars:block/" + v
    if extra:
        m["textures"].update(extra["textures"])
        m["elements"].append(extra["element"])
    if render_type:
        m["render_type"] = render_type
    with open(os.path.join(MOD, dst + ".json"), "w") as fh:
        json.dump(m, fh, indent=1)


def variants(mach, key, off_im, idle_im, extra_swap_running, extra=None, rt=None, idle_is_existing=False):
    save(off_im, f"{mach}_front_off")
    model(mach, f"{mach}_off", {key: f"{mach}_front_off"})
    if idle_is_existing:
        model(mach, f"{mach}_idle", {})
    else:
        save(idle_im, f"{mach}_front_idle")
        model(mach, f"{mach}_idle", {key: f"{mach}_front_idle"})
    model(mach, f"{mach}_running", extra_swap_running, extra, rt)


def lamp2x2(mach, key, pts, lit_col, running_front):
    """Green 2 x 2 lamp: off = dark, idle = one lit pixel, running = all four (the existing look)."""
    base = load(f"{mach}_front")
    assert all(base.getpixel(p) == lit_col for p in pts), mach
    off = paint(base.copy(), pts, SCREEN)
    idle = paint(off.copy(), [pts[0]], lit_col)
    return base, off, idle


def oven():
    base = load("regolith_oven_front")
    lamp = [(11, 12), (12, 12), (11, 13), (12, 13)]
    _, off, idle = lamp2x2("regolith_oven", "front", lamp, GREEN, None)
    coil = [(x, y) for y in (7, 8) for x in range(6, 10)]
    assert all(base.getpixel(p) == hx("#E69F00") for p in coil)
    frames = []
    for k in (0, 1, 2, 1):  # notch ping-pongs across the 4 px coil
        f = base.copy()
        paint(f, coil, DIM)
        paint(f, [(x, y) for y in (7, 8) for x in (6 + k, 7 + k)], HOT)
        frames.append(f)
    strip(frames, "regolith_oven_front_running", 8)
    variants("regolith_oven", "front", off, idle, {"front": "regolith_oven_front_running"})


def outgasser():
    base = load("regolith_outgasser_front")
    lamp = [(11, 8), (12, 8), (11, 9), (12, 9)]
    _, off, idle = lamp2x2("regolith_outgasser", "front", lamp, GREEN, None)
    # explicit rows (top y0 .. bottom y7), each 4 chars for x 6..9
    f0 = ["....", "....", "....", "....", "....", "....", ".##.", ".##."]
    f1 = ["....", "....", "....", "....", ".##.", "####", "####", ".##."]
    f2 = ["....", "....", ".oo.", "oooo", "####", "####", ".##.", "...."]
    f3 = ["oo..", ".oo.", "oooo", ".oo.", "....", "....", "....", "...."]
    frames = []
    for rows in (f0, f1, f2, f3):
        f = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
        for y, r in enumerate(rows):
            for x, c in enumerate(r):
                if c != ".":
                    f.putpixel((6 + x, y), DUST if c in "#x" else DUST_L)
        frames.append(f)
    strip(frames, "regolith_outgasser_puff", 10)
    puff = {"from": [9, 16, 9], "to": [13, 24, 13],
            "faces": {d: {"texture": "#puff", "uv": [6, 0, 10, 8]} for d in ("north", "south", "east", "west")}}
    # running front = the existing front (lamp fully lit); the puff element is the moving part
    save(base, "regolith_outgasser_front_running")
    variants("regolith_outgasser", "front", off, idle, {"front": "regolith_outgasser_front_running"},
             {"textures": {"puff": "mars:block/regolith_outgasser_puff"}, "element": puff}, "minecraft:cutout")


def o2():
    base = load("o2_generator_face")
    assert base.getpixel((10, 6)) == GREEN and base.getpixel((11, 6)) == GREEN
    off = paint(base.copy(), [(10, 6), (11, 6)], hx("#3B4148"))
    frames = []
    for n in (1, 2, 3, 4):  # the output bar fills along the lamp strip x9..12, row 6
        f = off.copy()
        paint(f, [(9 + i, 6) for i in range(n)], GREEN)
        frames.append(f)
    strip(frames, "o2_generator_face_running", 10)
    save(off, "o2_generator_face_off")
    model("o2_generator", "o2_generator_off", {"front": "o2_generator_face_off"})
    model("o2_generator", "o2_generator_idle", {})
    model("o2_generator", "o2_generator_running", {"front": "o2_generator_face_running"})


def lamp3x3(mach, front_n, running_extra=None):
    base = load(front_n)
    lamp = where(base, INFO)
    assert len(lamp) == 8, (front_n, len(lamp))  # hollow ring (8 px), dark centre
    xs = [p[0] for p in lamp]
    ys = [p[1] for p in lamp]
    box = [(x, y) for x in range(min(xs), max(xs) + 1) for y in range(min(ys), max(ys) + 1)]
    off = paint(base.copy(), box, SCREEN)
    run = paint(base.copy(), box, INFO)
    return base, off, run


def reclaimer():
    base, off, run = lamp3x3("water_reclaimer", "water_reclaimer_front")
    water = hx("#9FB0BC")
    pts = where(base, water)
    top = min(y for _, y in pts)
    bottom = max(y for _, y in pts)
    frames = []
    for rows in (3, 4, 5, 6):  # water level rises by one row per frame, then the bucket empties
        f = run.copy()
        cut = bottom - rows + 1
        for p in pts:
            if p[1] < cut:
                f.putpixel(p, hx("#3B4148"))
        # surface highlight on the top water pixel of each column
        for x in {p[0] for p in pts}:
            col = [p for p in pts if p[0] == x and p[1] >= cut]
            if col:
                f.putpixel(min(col, key=lambda q: q[1]), SURF)
        frames.append(f)
    strip(frames, "water_reclaimer_front_running", 20)
    variants("water_reclaimer", "front", off, base, {"front": "water_reclaimer_front_running"}, idle_is_existing=True)


def processor():
    base, off, run = lamp3x3("atmospheric_processor", "atmospheric_processor_front")
    can = load("atmospheric_processor_canister")
    segs = [(13, 14), (9, 11), (5, 7), (1, 3)]  # bottom to top, between the dark rings
    frames = []
    for a, b in segs:
        f = can.copy()
        paint(f, [(x, y) for y in range(a, b + 1) for x in range(1, 15)], LIT_SEG)
        frames.append(f)
    strip(frames, "atmospheric_processor_canister_running", 8)
    save(run, "atmospheric_processor_front_running")
    save(off, "atmospheric_processor_front_off")
    model("atmospheric_processor", "atmospheric_processor_off", {"front": "atmospheric_processor_front_off"})
    model("atmospheric_processor", "atmospheric_processor_idle", {})
    model("atmospheric_processor", "atmospheric_processor_running",
          {"front": "atmospheric_processor_front_running", "canister": "atmospheric_processor_canister_running"})


def emitter():
    base, off, run = lamp3x3("super_greenhouse_emitter", "super_greenhouse_emitter_front")
    flare = load("super_greenhouse_emitter_flare")
    frames = []
    for row in (2, 1, 0, None):  # glow rises through the 3 px flare lip, then a dark beat
        f = flare.copy()
        if row is not None:
            paint(f, [(x, row) for x in range(16)], GLOW)
        frames.append(f)
    strip(frames, "super_greenhouse_emitter_flare_running", 8)
    save(run, "super_greenhouse_emitter_front_running")
    save(off, "super_greenhouse_emitter_front_off")
    model("super_greenhouse_emitter", "super_greenhouse_emitter_off", {"front": "super_greenhouse_emitter_front_off"})
    model("super_greenhouse_emitter", "super_greenhouse_emitter_idle", {})
    model("super_greenhouse_emitter", "super_greenhouse_emitter_running",
          {"front": "super_greenhouse_emitter_front_running", "flare": "super_greenhouse_emitter_flare_running"})


def battery():
    base = load("battery_front")
    bars = [12, 10, 8]  # filled from the bottom
    pts = lambda r: [(x, r) for x in (5, 6, 7)]
    assert all(base.getpixel(p) == GREEN for r in bars for p in pts(r))
    for charge in (0, 1, 2):
        f = base.copy()
        for i, r in enumerate(bars):
            if i >= charge:
                paint(f, pts(r), SCREEN)
        if charge == 0:
            paint(f, [(10, 6), (10, 7)], SCREEN)
        save(f, f"battery_front_charge{charge}")
        model("battery", f"battery_charge{charge}", {"front": f"battery_front_charge{charge}"})
    model("battery", "battery_charge3", {})


def main():
    for fn in (oven, outgasser, o2, reclaimer, processor, emitter, battery):
        fn()
    print("\n".join(MESSAGES))


if __name__ == "__main__":
    main()
