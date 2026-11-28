#!/usr/bin/env python3
"""Algae vat and carbon scrubber art (Lead Artist, 2026-11-28). Specs: docs/art/algae-vat.md, docs/art/carbon-scrubber.md.
Real ideas: ISS CDRA (twin adsorbent beds, air selector valves, NTRS 20130000838); Veggie / Advanced Plant Habitat
(lit sealed growth chamber) and the algae photobioreactor idea (glass tank, light, bubbled gas). Size, band, plate,
colours of the culture and every number are INVENTED. Writes new files only (textures, models <name>, _off, _idle,
_running, item model). No blockstate, no registration. Reuses gen_invented_machines.py helpers.
Run: python3 -I tools/art-preview/gen_vat_scrubber.py"""
import json
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from gen_invented_machines import (A0, A1, A2, A3, INFO, INK, ORANGE, PURPLE, PURPLE_DK, SCREEN, TEX,  # noqa: E402
                                   MODELS, alloy, band, blank, hx, plate, rect, save, _box, _el, _write_model)
from PIL import Image  # noqa: E402

# algae culture: dark amber-brown (redder than the processor gold), NOT green; bubbles pale
M0, M1, M2, M3 = hx("#3E2412"), hx("#5C3A1A"), hx("#7A4E22"), hx("#9A6A30")
BUB = hx("#D8E4EA")
LAMP, LAMP_DIM = hx("#F3EBC8"), hx("#8D8870")   # grow lamp lit / dim (warm white, not purple, not green)
# scrubber: cream-grey canisters
C0, C1, C2, C3 = hx("#8A877E"), hx("#AFAB9F"), hx("#CFCABD"), hx("#E4E0D2")
PALE, PALE_DIM = hx("#BFE3F7"), hx("#6F7F8B")   # cycling indicator lit / dim
CARB = hx("#D3CDBF")                           # carbonate fill (look brief)
V, S = "algae_vat_", "carbon_scrubber_"


def strip(frames, name, ft):
    s = Image.new("RGBA", (16, 16 * len(frames)))
    for i, f in enumerate(frames):
        s.paste(f, (0, 16 * i))
    save(s, name)
    with open(os.path.join(TEX, name + ".png.mcmeta"), "w") as fh:
        json.dump({"animation": {"frametime": ft}}, fh, indent=1)


def ring(im, fr_x, fr_y, state):
    """5 x 5 lamp frame at (fr_x, fr_y); ring 3 x 3 inside. state off / idle (hollow) / running (solid)."""
    rect(im, fr_x, fr_y, fr_x + 4, fr_y + 4, SCREEN)
    if state != "off":
        rect(im, fr_x + 1, fr_y + 1, fr_x + 3, fr_y + 3, INFO)
        if state == "idle":
            im.putpixel((fr_x + 2, fr_y + 2), SCREEN)


# ======================================================================= algae vat
def vat_textures():
    base = alloy()
    rect(base, 0, 8, 15, 8, A0)
    band(base)
    save(base, V + "base")

    def front(state):
        f = alloy()
        rect(f, 0, 8, 15, 8, A0)
        ring(f, 3, 9, state)           # lamp frame x3-7, y9-13 (rows 8..13 = panel, 14..15 = band)
        plate(f, 10, 9)                # standard plate position x10-13, y9-12
        band(f)
        return f
    for st, n in (("idle", "front"), ("off", "front_off"), ("running", "front_running")):
        save(front(st), V + n)

    # tank glass (north face crop uv [2,2,14,14]): alloy frame, amber culture, pale glint, surface line
    def glass(bubbles=()):
        g = blank(A1)
        rect(g, 2, 2, 13, 13, A0)
        rect(g, 3, 3, 12, 12, M1)
        rect(g, 3, 3, 12, 3, M2)         # surface line (lighter) = liquid level
        rect(g, 3, 4, 12, 4, M2)
        rect(g, 3, 11, 12, 12, M0)       # dense culture settles low
        for y in (5, 6, 7):
            g.putpixel((4, y), A3)       # glass glint (grey-white, not a bubble: static column)
        for x, y in bubbles:
            g.putpixel((x, y), BUB)
        return g
    save(glass(), V + "glass")
    cols = ((6, (11, 6), 2), (9, (9, 4), 3), (11, (10, 5), 2))   # (x, start ys, rise px per frame)
    frames = []
    for i in range(4):
        pts = []
        for x, ys, k in cols:
            for y0 in ys:
                pts.append((x, 5 + (y0 - 5 - k * i) % 8))   # stays in rows 5..12, wraps like a bubble stream
        frames.append(glass(pts))
    strip(frames, V + "glass_running", 8)

    tank_side = blank(A1)  # alloy posts with a narrow culture window
    rect(tank_side, 2, 2, 13, 13, A0)
    rect(tank_side, 4, 3, 11, 12, M1)
    rect(tank_side, 4, 3, 11, 4, M2)
    rect(tank_side, 4, 11, 11, 12, M0)
    for y in (5, 6):
        tank_side.putpixel((5, y), A3)
    save(tank_side, V + "tank_side")

    def hood(state):
        h = blank(A1)
        rect(h, 0, 6, 15, 6, A3)
        rect(h, 0, 9, 15, 9, A0)
        rect(h, 2, 7, 13, 8, SCREEN)           # lamp strip window 12 x 2
        if state == "running":
            rect(h, 2, 7, 13, 8, LAMP)
        elif state == "idle":
            for x in range(2, 14, 2):          # dashed = dim: every other 2 px lit
                if (x // 2) % 2 == 0:
                    rect(h, x, 7, x + 1, 8, LAMP_DIM)
        return h
    for st, n in (("idle", "hood"), ("off", "hood_off"), ("running", "hood_running")):
        save(hood(st), V + n)

    hs = blank(A1)
    rect(hs, 0, 6, 15, 6, A3)
    rect(hs, 0, 9, 15, 9, A0)
    for x in (2, 13):
        hs.putpixel((x, 7), A3)
    save(hs, V + "hood_side")

    top = alloy()
    rect(top, 0, 0, 15, 0, A3)
    rect(top, 3, 6, 12, 7, A0)   # vent slots
    rect(top, 3, 9, 12, 10, A0)
    save(top, V + "top")

    pipe = blank(A2)   # air line (bubble feed): ribbed alloy
    for y in range(0, 16, 4):
        rect(pipe, 0, y, 15, y, A1)
    rect(pipe, 0, 0, 0, 15, A3)
    rect(pipe, 15, 0, 15, 15, A1)
    save(pipe, V + "pipe")


def vat_models():
    t = {"particle": "mars:block/" + V + "base"}
    for k in ("base", "front", "glass", "tank_side", "hood", "hood_side", "top", "pipe"):
        t[k] = "mars:block/" + V + k
    side = lambda tx, uv: {d: {"texture": tx, "uv": uv} for d in ("south", "east", "west")}
    plinth = _el([1, 0, 1], [15, 8, 15], _box("#base", [1, 8, 15, 16], "#front", [1, 8, 15, 16], top="#top", topuv=[1, 1, 15, 15]))
    f = {"north": {"texture": "#glass", "uv": [2, 2, 14, 14]},
         "south": {"texture": "#tank_side", "uv": [2, 2, 14, 14]},
         "east": {"texture": "#tank_side", "uv": [3, 2, 13, 14]},
         "west": {"texture": "#tank_side", "uv": [3, 2, 13, 14]},
         "up": {"texture": "#top", "uv": [2, 3, 14, 13]}, "down": {"texture": "#top"}}
    tank = _el([2, 8, 3], [14, 20, 13], f)
    hf = {"north": {"texture": "#hood", "uv": [1, 6, 15, 10]},
          "south": {"texture": "#hood_side", "uv": [1, 6, 15, 10]},
          "east": {"texture": "#hood_side", "uv": [2, 6, 14, 10]},
          "west": {"texture": "#hood_side", "uv": [2, 6, 14, 10]},
          "up": {"texture": "#top", "uv": [1, 2, 15, 14]}, "down": {"texture": "#top"}}
    hood = _el([1, 20, 2], [15, 24, 14], hf)
    els = [plinth, tank, hood,
           _el([11, 24, 10], [13, 26, 12], _box("#pipe", [0, 0, 2, 2], top="#pipe")),
           _el([5, 26, 10], [13, 28, 12], _box("#pipe", [0, 0, 8, 2], top="#pipe"))]
    _write_model("algae_vat", t, els)
    variants("algae_vat", {"front": V + "front_off", "hood": V + "hood_off"}, {},
             {"front": V + "front_running", "hood": V + "hood_running", "glass": V + "glass_running"})


def variants(name, off, idle, running):
    base = json.load(open(os.path.join(MODELS, name + ".json")))
    for suffix, swap in (("_off", off), ("_idle", idle), ("_running", running)):
        m = json.loads(json.dumps(base))
        for k, v in swap.items():
            assert k in m["textures"], k
            m["textures"][k] = "mars:block/" + v
        with open(os.path.join(MODELS, name + suffix + ".json"), "w") as fh:
            json.dump(m, fh, indent=1)
    with open(os.path.join(MODELS, "..", "item", name + ".json"), "w") as fh:
        json.dump({"parent": "mars:block/" + name}, fh, indent=1)


# ======================================================================= carbon scrubber
def scrub_textures():
    base = alloy()
    rect(base, 0, 12, 15, 12, A0)
    band(base)
    save(base, S + "base")

    ACT = {"off": (SCREEN, SCREEN), "idle": (PALE_DIM, PALE_DIM)}

    def front(state, left=None, right=None, bar=False):
        f = alloy()
        rect(f, 0, 0, 15, 0, A0)
        rect(f, 0, 9, 15, 9, A0)
        a, b = ACT.get(state, (left, right))
        rect(f, 3, 1, 6, 2, a)           # window A (bed A)
        rect(f, 9, 1, 12, 2, b)          # window B (bed B)
        rect(f, 7, 1, 8, 2, PALE if bar else SCREEN)   # valve bar between the windows
        ring(f, 3, 4, state)             # lamp frame x3-7, y4-8
        plate(f, 9, 4)                   # plate x9-12, y4-7 (slash (9,7) to (12,4))
        return f
    save(front("idle"), S + "front")
    save(front("off"), S + "front_off")
    frames = [front("running", PALE, SCREEN), front("running", PALE, SCREEN, True),
              front("running", SCREEN, PALE), front("running", SCREEN, PALE, True)]
    strip(frames, S + "front_running", 20)

    def bed(lit):
        b = blank(C2)
        for x in range(16):
            for y in range(16):
                if (x * 5 + y * 3) % 13 == 0:
                    b.putpixel((x, y), C1)
        for x in (3, 8, 13):            # vertical ribs (reads round)
            rect(b, x, 0, x, 15, C3 if x == 3 else C1)
        rect(b, 0, 0, 15, 0, C0)
        rect(b, 0, 15, 15, 15, C0)
        rect(b, 0, 7, 15, 7, C0)        # seam
        rect(b, 0, 1, 15, 1, C0)        # indicator ring: dark outline rows 1 and 4, ring rows 2-3
        rect(b, 0, 4, 15, 4, C0)
        rect(b, 0, 2, 15, 3, PALE if lit else SCREEN)
        for y in (10, 12):
            rect(b, 0, y, 15, y, C1)
        return b
    save(bed(False), S + "bed")
    strip([bed(True), bed(True), bed(False), bed(False)], S + "bed_a_running", 20)
    strip([bed(False), bed(False), bed(True), bed(True)], S + "bed_b_running", 20)

    cap = blank(A2)
    rect(cap, 0, 0, 15, 0, A3)
    rect(cap, 0, 15, 15, 15, A0)
    for x in (2, 13):
        cap.putpixel((x, 7), A3)
    save(cap, S + "cap")

    pipe = blank(A2)
    for y in range(0, 16, 4):
        rect(pipe, 0, y, 15, y, A1)
    rect(pipe, 0, 0, 0, 15, A3)
    rect(pipe, 15, 0, 15, 15, A1)
    save(pipe, S + "pipe")

    vs = alloy()
    rect(vs, 0, 0, 15, 0, A3)
    rect(vs, 0, 15, 15, 15, A0)
    for x in (2, 13):
        for y in (3, 11):
            vs.putpixel((x, y), A3)
    save(vs, S + "valve_side")

    top = alloy()
    rect(top, 0, 0, 15, 0, A3)
    rect(top, 4, 5, 11, 10, A0)
    rect(top, 5, 6, 10, 9, A1)
    save(top, S + "top")

    def chute(full):
        c = blank(A1)
        rect(c, 0, 0, 15, 0, A2)
        rect(c, 3, 3, 12, 12, A0)                 # mouth
        if full:
            rect(c, 4, 6, 11, 12, CARB)           # carbonate fill (look brief) rises to the lip
            for x in (5, 8, 10):
                c.putpixel((x, 5), CARB)
        return c
    save(chute(False), S + "chute")
    save(chute(True), S + "chute_running")


def cyl(x0, x1, z0, z1, y0, y1, tex):
    """Octagon-ish cylinder: two crossed boxes (6 x 4 and 4 x 6 px). x1-x0 must be 6, z1-z0 4."""
    def box(fr, to, uw_ns, uw_ew):
        faces = {}
        for d in ("north", "south"):
            faces[d] = {"texture": tex, "uv": [5, 0, 5 + uw_ns, 16]}
        for d in ("east", "west"):
            faces[d] = {"texture": tex, "uv": [0, 0, uw_ew, 16]}
        faces["up"] = {"texture": "#cap", "uv": [0, 0, 8, 8]}
        faces["down"] = {"texture": "#cap", "uv": [0, 0, 8, 8]}
        return _el(fr, to, faces)
    return [box([x0, y0, z0], [x1, y1, z1], 6, 4), box([x0 + 1, y0, z0 - 1], [x1 - 1, y1, z1 + 1], 4, 6)]


def scrub_models():
    t = {"particle": "mars:block/" + S + "base"}
    for k in ("base", "front", "valve_side", "top", "cap", "pipe", "chute"):
        t[k] = "mars:block/" + S + k
    t["bed_a"] = t["bed_b"] = "mars:block/" + S + "bed"
    plinth = _el([1, 0, 1], [15, 4, 15], _box("#base", [1, 12, 15, 16], top="#top", topuv=[1, 1, 15, 15]))
    valve = _el([2, 4, 2], [14, 14, 7],
                {"north": {"texture": "#front", "uv": [2, 0, 14, 10]},
                 "south": {"texture": "#valve_side", "uv": [2, 0, 14, 10]},
                 "east": {"texture": "#valve_side", "uv": [2, 0, 7, 10]},
                 "west": {"texture": "#valve_side", "uv": [2, 0, 7, 10]},
                 "up": {"texture": "#top", "uv": [2, 2, 14, 7]}, "down": {"texture": "#top"}})
    els = [plinth, valve]
    els += cyl(1, 7, 8, 12, 4, 20, "#bed_a")
    els += cyl(9, 15, 8, 12, 4, 20, "#bed_b")
    els += [_el([2, 20, 8], [6, 22, 12], _box("#cap", [0, 0, 4, 2], top="#cap")),
            _el([10, 20, 8], [14, 22, 12], _box("#cap", [0, 0, 4, 2], top="#cap")),
            _el([3, 22, 9], [13, 24, 11], _box("#pipe", [0, 0, 10, 2], top="#pipe")),      # CO2 inlet header
            _el([7, 6, 9], [9, 10, 11], _box("#pipe", [0, 0, 2, 4], top="#pipe")),          # manifold between beds
            _el([15, 4, 8], [16, 8, 12],
                {"east": {"texture": "#chute", "uv": [3, 3, 13, 13]}, "north": {"texture": "#cap", "uv": [0, 0, 1, 4]},
                 "south": {"texture": "#cap", "uv": [0, 0, 1, 4]}, "up": {"texture": "#cap", "uv": [0, 0, 1, 4]},
                 "down": {"texture": "#cap", "uv": [0, 0, 1, 4]}})]                         # carbonate chute
    _write_model("carbon_scrubber", t, els)
    variants("carbon_scrubber", {"front": S + "front_off"}, {},
             {"front": S + "front_running", "bed_a": S + "bed_a_running", "bed_b": S + "bed_b_running",
              "chute": S + "chute_running"})


# ======================================================================= checks
def check():
    errs = []
    static = {V: ("base", "front", "front_off", "front_running", "glass", "tank_side", "hood", "hood_off", "hood_running",
                  "hood_side", "top", "pipe"),
              S: ("base", "front", "front_off", "bed", "cap", "pipe", "valve_side", "top", "chute", "chute_running")}
    for pre, names in static.items():
        for n in names:
            im = Image.open(os.path.join(TEX, pre + n + ".png"))
            if im.size != (16, 16) or im.mode != "RGBA":
                errs.append(pre + n + " size/mode")
            for x in range(16):
                for y in range(16):
                    r, g, b, a = im.getpixel((x, y))
                    if a != 255:
                        errs.append(pre + n + " not opaque")
                    if max(r, g, b) < 8 or min(r, g, b) > 247:
                        errs.append(pre + n + " pure black/white")
                    if g > r + 20 and g > b + 20:
                        errs.append(pre + n + " green at %d,%d" % (x, y))
    for n in (V + "glass_running", S + "front_running", S + "bed_a_running", S + "bed_b_running"):
        im = Image.open(os.path.join(TEX, n + ".png"))
        if im.size != (16, 64):
            errs.append(n + " strip size")
        for k in range(4):   # no green in frames either
            for x in range(16):
                for y in range(16):
                    r, g, b, a = im.getpixel((x, 16 * k + y))
                    if g > r + 20 and g > b + 20:
                        errs.append(n + " green")
    for pre in (V, S):
        b = Image.open(os.path.join(TEX, pre + "base.png"))
        if b.getpixel((0, 14)) != PURPLE or b.getpixel((2, 14)) == PURPLE or b.getpixel((0, 15)) != PURPLE_DK:
            errs.append(pre + "band")
    vf = Image.open(os.path.join(TEX, V + "front.png"))
    if vf.getpixel((10, 9)) != PURPLE or vf.getpixel((10, 12)) != INK or vf.getpixel((13, 9)) != INK:
        errs.append("vat plate")
    sf = Image.open(os.path.join(TEX, S + "front.png"))
    if sf.getpixel((10, 4)) != PURPLE or sf.getpixel((9, 7)) != INK or sf.getpixel((12, 4)) != INK:
        errs.append("scrubber plate")
    for name, maxy in (("algae_vat", 28), ("carbon_scrubber", 24)):
        for suf in ("", "_off", "_idle", "_running"):
            m = json.load(open(os.path.join(MODELS, name + suf + ".json")))
            top = 0
            for el in m["elements"]:
                top = max(top, el["to"][1])
                for c in (0, 2):
                    if not 0 <= el["from"][c] < el["to"][c] <= 16:
                        errs.append(name + suf + " bounds " + str(el["from"]))
                for face in el["faces"].values():
                    for v in face.get("uv", []):
                        if not 0 <= v <= 16:
                            errs.append(name + suf + " uv " + str(face["uv"]))
            if top != maxy:
                errs.append(name + suf + " height %s" % top)
    return errs


if __name__ == "__main__":
    vat_textures()
    vat_models()
    scrub_textures()
    scrub_models()
    e = check()
    print("OK" if not e else "FAIL " + "; ".join(sorted(set(e))))
    sys.exit(1 if e else 0)
