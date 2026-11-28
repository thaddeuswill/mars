#!/usr/bin/env python3
"""Invented-machine art (Lead Artist, 2026-11-28): the shared 'made-up band' overlays and the water reclaimer.
Spec: docs/art/made-up-band-spec.md and docs/art/invented-machines-look.md. Writes textures and block/item-free
model JSON only (no blockstate, no registration). Run: python3 -I tools/art-preview/gen_invented_machines.py"""
import json
import os
import sys
from PIL import Image

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "..", "pack", "assets", "mars")
TEX = os.path.join(ROOT, "textures", "block")
MODELS = os.path.join(ROOT, "models", "block")


def hx(s):
    return tuple(int(s[i:i + 2], 16) for i in (1, 3, 5)) + (255,)


PURPLE, PURPLE_DK, INK = hx("#CC79A7"), hx("#8E3F6C"), hx("#2A2420")
A0, A1, A2, A3 = hx("#3B4148"), hx("#59616A"), hx("#7E8790"), hx("#A8B0B7")
ORANGE, SALT, ICE_D, ICE = hx("#E69F00"), hx("#E7E2D6"), hx("#9FB0BC"), hx("#C2CDD5")
SCREEN, INFO = hx("#1E2328"), hx("#56B4E9")
CLEAR = (0, 0, 0, 0)
PLATE_XY = (10, 9)  # standard plate position on a 16x16 front texture


def blank(c=CLEAR, w=16, h=16):
    im = Image.new("RGBA", (w, h))
    for x in range(w):
        for y in range(h):
            im.putpixel((x, y), c)
    return im


def rect(im, x0, y0, x1, y1, c):
    for x in range(x0, x1 + 1):
        for y in range(y0, y1 + 1):
            im.putpixel((x, y), c)


def band(im, w=16):
    """2 px band on the bottom two rows, dashed 2 on / 2 off; row 14 purple, row 15 dark edge."""
    for x in range(w):
        if (x // 2) % 2 == 0:
            im.putpixel((x, 14), PURPLE)
            im.putpixel((x, 15), PURPLE_DK)


def plate(im, x0, y0):
    """4x4 purple square with a 1 px ink diagonal slash (bottom-left to top-right)."""
    rect(im, x0, y0, x0 + 3, y0 + 3, PURPLE)
    for i in range(4):
        im.putpixel((x0 + i, y0 + 3 - i), INK)


def save(im, name):
    im.save(os.path.join(TEX, name + ".png"))


# ---- reusable overlays -----------------------------------------------------------------------------------
def overlays():
    b = blank()
    band(b)
    save(b, "made_up_band")
    p = blank()
    plate(p, *PLATE_XY)
    save(p, "made_up_plate")


# ---- water reclaimer (ISS Water Recovery System idea; size and look invented) ------------------------------
def alloy(seed_dark=False):
    im = blank(A1)
    for x in range(16):
        for y in range(16):
            if (x * 7 + y * 3) % 11 == 0:
                im.putpixel((x, y), A2)
    return im


def reclaimer():
    # plinth/base side: alloy with the made-up band on rows 14-15 (model uses uv v 14..16 for the plinth)
    base = alloy()
    rect(base, 0, 0, 15, 0, A2)
    rect(base, 0, 13, 15, 13, A0)
    band(base)
    save(base, "water_reclaimer_base")

    # tank side: plain alloy with rivet rows
    side = alloy()
    rect(side, 0, 0, 15, 0, A0)
    rect(side, 0, 15, 15, 15, A0)
    for x in (2, 13):
        for y in (2, 13):
            side.putpixel((x, y), A3)
    save(side, "water_reclaimer_side")

    # front of the lower tank: window with water-level steps (shape, not colour), lamp, plate. Model crops uv [2,2,14,14]
    f = alloy()
    rect(f, 2, 2, 13, 13, A1)
    rect(f, 2, 2, 13, 2, A0)
    rect(f, 2, 13, 13, 13, A0)
    rect(f, 2, 2, 2, 13, A0)
    rect(f, 13, 2, 13, 13, A0)
    rect(f, 3, 3, 8, 11, SCREEN)  # window
    rect(f, 3, 3, 8, 3, A3)
    # water below a stepped surface: 3 px steps left to right (levels 5, 7, 9 from the top of the window)
    for x, top in ((4, 6), (5, 6), (6, 8), (7, 8), (8, 10)):
        rect(f, x, top, x, 11, ICE_D)
    for y in (6, 8, 10):  # tick marks on the window frame, every 2 px
        f.putpixel((3, y), A3)
    # status lamp (hollow square, 3.1 'standby') in a dark ring
    rect(f, 9, 2, 13, 6, SCREEN)
    rect(f, 10, 3, 12, 5, INFO)
    f.putpixel((11, 4), SCREEN)
    plate(f, *PLATE_XY)
    save(f, "water_reclaimer_front")

    # frame posts
    fr = alloy()
    rect(fr, 0, 0, 0, 15, A0)
    rect(fr, 15, 0, 15, 15, A0)
    save(fr, "water_reclaimer_frame")

    # upper filter canister: lighter alloy with ring lines
    c = blank(A2)
    for y in (0, 5, 10, 15):
        rect(c, 0, y, 15, y, A1)
    rect(c, 0, 0, 0, 15, A1)
    rect(c, 15, 0, 15, 15, A3)
    save(c, "water_reclaimer_canister")

    # top plate with a hatch
    t = alloy()
    rect(t, 0, 0, 15, 0, A3)
    rect(t, 4, 4, 11, 11, A0)
    rect(t, 5, 5, 10, 10, A1)
    rect(t, 7, 7, 8, 8, A3)
    save(t, "water_reclaimer_top")

    # clear tube: pale, ribbed (opaque on purpose; see spec)
    tb = blank(ICE)
    for y in range(0, 16, 4):
        rect(tb, 0, y, 15, y, A2)
    rect(tb, 0, 0, 0, 15, hx("#E4E8EA"))
    save(tb, "water_reclaimer_tube")

    # bucket spout: safety orange touch part
    sp = blank(ORANGE)
    rect(sp, 0, 15, 15, 15, hx("#A66F00"))
    save(sp, "water_reclaimer_spout")

    def el(frm, to, faces):
        return {"from": frm, "to": to, "faces": faces}

    def allf(tex, uv=None):
        d = {}
        for f in ("north", "south", "east", "west", "up", "down"):
            d[f] = {"texture": tex}
        return d

    def sides(tex, uv, front=None, fuv=None):
        d = {f: {"texture": tex, "uv": uv} for f in ("south", "east", "west")}
        d["north"] = {"texture": front or tex, "uv": fuv or uv}
        d["up"] = {"texture": "#top", "uv": [0, 0, 16, 16]}
        d["down"] = {"texture": "#top"}
        return d

    els = [
        # plinth y 0..2 (band rows via uv v 14..16)
        el([1, 0, 1], [15, 2, 15], {**sides("#base", [1, 14, 15, 16])}),
        # lower tank y 2..14, front window, 12 wide
        el([2, 2, 2], [14, 14, 14], sides("#side", [2, 2, 14, 14], "#front", [2, 2, 14, 14])),
        # four frame posts y 14..30
        el([2, 14, 2], [4, 30, 4], sides("#frame", [0, 0, 2, 16])),
        el([12, 14, 2], [14, 30, 4], sides("#frame", [0, 0, 2, 16])),
        el([2, 14, 12], [4, 30, 14], sides("#frame", [0, 0, 2, 16])),
        el([12, 14, 12], [14, 30, 14], sides("#frame", [0, 0, 2, 16])),
        # upper filter canister
        el([5, 15, 5], [11, 28, 11], sides("#canister", [0, 0, 6, 13])),
        # top plate
        el([2, 28, 2], [14, 30, 14], {**sides("#side", [2, 0, 14, 2])}),
        # clear tube loop on the east side, visible water path from tank to canister
        el([14, 4, 7], [16, 28, 9], sides("#tube", [0, 0, 2, 16])),
        # bucket spout on the front, orange
        el([6, 4, 0], [10, 6, 2], sides("#spout", [0, 0, 4, 2])),
    ]
    model = {
        "textures": {"particle": "mars:block/water_reclaimer_side", "base": "mars:block/water_reclaimer_base",
                     "side": "mars:block/water_reclaimer_side", "front": "mars:block/water_reclaimer_front",
                     "frame": "mars:block/water_reclaimer_frame", "canister": "mars:block/water_reclaimer_canister",
                     "top": "mars:block/water_reclaimer_top", "tube": "mars:block/water_reclaimer_tube",
                     "spout": "mars:block/water_reclaimer_spout"},
        "elements": els,
    }
    # the plinth and tank tops use #top: fine. Top plate side uses side texture rows 2..4 (rivet-free)
    model = {"parent": "block/block", **model}
    with open(os.path.join(MODELS, "water_reclaimer.json"), "w") as fh:
        json.dump(model, fh, indent=1)


# ---- shared model helpers ----------------------------------------------------------------------------------
def _el(frm, to, faces):
    return {"from": frm, "to": to, "faces": faces}


def _box(tex, uv, front=None, fuv=None, top="#top", topuv=None):
    d = {f: {"texture": tex, "uv": uv} for f in ("south", "east", "west")}
    d["north"] = {"texture": front or tex, "uv": fuv or uv}
    d["up"] = {"texture": top, "uv": topuv or [0, 0, 16, 16]}
    d["down"] = {"texture": top}
    return d


def _write_model(name, textures, els):
    model = {"parent": "block/block", "textures": textures, "elements": els}
    with open(os.path.join(MODELS, name + ".json"), "w") as fh:
        json.dump(model, fh, indent=1)


# ---- atmospheric processor (MOXIE gold box + Sabatier-style intake; size, band, plate INVENTED) -------------
G0, G1, G2, G3 = hx("#5C4320"), hx("#7E5E2C"), hx("#A3823F"), hx("#C9AE6A")


def foil(seed):
    """Gold-wrapped look: mid gold, soft horizontal creases, a few lit ridge pixels (no pure white)."""
    import random
    r = random.Random(seed)
    im = blank(G2)
    for y in (3, 8, 12):
        for x in range(16):
            if r.random() < .75:
                im.putpixel((x, y), G1)
    for _ in range(14):
        im.putpixel((r.randint(0, 15), r.randint(0, 15)), G3)
    for _ in range(6):
        im.putpixel((r.randint(0, 15), r.randint(0, 15)), G1)
    return im


def processor():
    base = alloy()
    rect(base, 0, 0, 15, 0, A2)
    rect(base, 0, 13, 15, 13, A0)
    band(base)
    save(base, "atmospheric_processor_base")

    side = foil(201)
    rect(side, 0, 0, 15, 0, G1)
    rect(side, 0, 15, 15, 15, G1)
    for x in (2, 13):
        for y in (2, 13):
            side.putpixel((x, y), A3)  # alloy fasteners on the gold
    save(side, "atmospheric_processor_side")

    f = foil(202)  # model crops uv [2,2,14,14]
    rect(f, 2, 2, 13, 2, G1)
    rect(f, 2, 13, 13, 13, G1)
    rect(f, 2, 2, 2, 13, G1)
    rect(f, 13, 2, 13, 13, G1)
    rect(f, 3, 4, 8, 11, A0)  # intake grille: frame, slots (shape = "air goes in here")
    for y in (5, 7, 9):
        rect(f, 4, y, 7, y, SCREEN)
        rect(f, 4, y + 1, 7, y + 1, A2)
    rect(f, 3, 4, 8, 4, ORANGE)  # orange touch/intake lip (style: orange = interact)
    rect(f, 9, 2, 13, 6, SCREEN)  # lamp ring, hollow square (standby)
    rect(f, 10, 3, 12, 5, INFO)
    f.putpixel((11, 4), SCREEN)
    plate(f, *PLATE_XY)
    save(f, "atmospheric_processor_front")

    top = foil(203)
    rect(top, 0, 0, 15, 0, G3)
    rect(top, 9, 3, 14, 8, A0)  # canister socket seen from above
    rect(top, 10, 4, 13, 7, A1)
    rect(top, 11, 5, 12, 6, A3)
    save(top, "atmospheric_processor_top")

    can = blank(A2)  # raised filter canister: alloy cylinder with ring lines
    for y in (0, 4, 8, 12, 15):
        rect(can, 0, y, 15, y, A1)
    rect(can, 0, 0, 0, 15, A1)
    rect(can, 15, 0, 15, 15, A3)
    save(can, "atmospheric_processor_canister")

    port = blank(A1)  # output ports: alloy
    rect(port, 0, 0, 15, 0, A2)
    rect(port, 0, 15, 15, 15, A0)
    rect(port, 0, 7, 15, 8, A0)
    save(port, "atmospheric_processor_port")

    tube = blank(SALT)  # tubing run, salt-crust colour, ribbed
    for y in range(0, 16, 4):
        rect(tube, 0, y, 15, y, A3)
    rect(tube, 0, 0, 0, 15, hx("#C9C3B5"))
    save(tube, "atmospheric_processor_tube")

    foot = blank(A1)  # lander-style feet
    rect(foot, 0, 0, 15, 0, A2)
    rect(foot, 0, 15, 15, 15, A0)
    save(foot, "atmospheric_processor_foot")

    t = {"particle": "mars:block/atmospheric_processor_side", "base": "mars:block/atmospheric_processor_base",
         "side": "mars:block/atmospheric_processor_side", "front": "mars:block/atmospheric_processor_front",
         "top": "mars:block/atmospheric_processor_top", "canister": "mars:block/atmospheric_processor_canister",
         "port": "mars:block/atmospheric_processor_port", "tube": "mars:block/atmospheric_processor_tube",
         "foot": "mars:block/atmospheric_processor_foot"}
    els = [
        # skirt y0..2 carries the made-up band (rows 14-15 via uv v 14..16)
        _el([2, 0, 2], [14, 2, 14], _box("#base", [2, 14, 14, 16], top="#foot")),
        # gold-wrapped body y2..14, intake grille + plate on the north (front) face
        _el([1, 2, 1], [15, 14, 15], _box("#side", [1, 2, 15, 14], "#front", [1, 2, 15, 14], top="#side")),
        # four lander feet below the body corners (y0..2), outside the skirt
        _el([1, 0, 1], [3, 2, 3], _box("#foot", [0, 0, 2, 2], top="#foot")),
        _el([13, 0, 1], [15, 2, 3], _box("#foot", [0, 0, 2, 2], top="#foot")),
        _el([1, 0, 13], [3, 2, 15], _box("#foot", [0, 0, 2, 2], top="#foot")),
        _el([13, 0, 13], [15, 2, 15], _box("#foot", [0, 0, 2, 2], top="#foot")),
        # top plate of the gold wrap (lid), raised 1 px
        _el([2, 14, 2], [14, 15, 14], _box("#top", [2, 0, 14, 1], top="#top", topuv=[2, 2, 14, 14])),
        # raised filter canister y15..22 on the top, east half, over the socket
        _el([9, 15, 3], [13, 23, 7], _box("#canister", [0, 0, 4, 8])),
        # two output ports on the east face (O2 high, CO low), alloy
        _el([15, 5, 3], [16, 9, 6], _box("#port", [0, 0, 3, 4])),
        _el([15, 5, 10], [16, 9, 13], _box("#port", [0, 0, 3, 4])),
        # tubing: ports joined by a salt-crust tube running along the east face
        _el([15, 6, 6], [16, 8, 10], _box("#tube", [0, 0, 4, 2])),
    ]
    _write_model("atmospheric_processor", t, els)


# ---- super-greenhouse gas emitter (NO real machine; plainly fictional; band, plate INVENTED) ---------------------
def emitter():
    base = alloy()
    rect(base, 0, 0, 15, 0, A2)
    rect(base, 0, 13, 15, 13, A0)
    band(base)
    save(base, "super_greenhouse_emitter_base")

    tank = alloy()  # horizontal tank side: ring seams every 5 px, rivet dots
    for x in (0, 5, 10, 15):
        rect(tank, x, 0, x, 15, A0)
    for y in (3, 12):
        for x in (2, 7, 12):
            tank.putpixel((x, y), A3)
    rect(tank, 0, 0, 15, 0, A2)
    save(tank, "super_greenhouse_emitter_tank")

    f = alloy()  # tank front: pipe box with lamp + plate (model crops uv [2,0,14,12])
    rect(f, 2, 0, 13, 11, A1)
    rect(f, 2, 0, 13, 0, A0)
    rect(f, 2, 11, 13, 11, A0)
    rect(f, 3, 2, 8, 9, A0)  # valve box
    rect(f, 4, 3, 7, 8, A1)
    rect(f, 5, 5, 6, 6, ORANGE)  # touch valve wheel
    rect(f, 9, 1, 13, 5, SCREEN)
    rect(f, 10, 2, 12, 4, INFO)
    f.putpixel((11, 3), SCREEN)
    save(f, "super_greenhouse_emitter_front")

    # plate sits on the plinth-skirt-free tank front at standard offset relative to the crop: bake it
    plate(f, 10, 6)
    save(f, "super_greenhouse_emitter_front")

    stack = alloy()  # stack: alloy above, 45 degree orange/alloy hazard stripe on the lower 6 rows
    for y in range(10, 16):
        for x in range(16):
            stack.putpixel((x, y), ORANGE if (x + y) % 8 < 4 else A1)
    rect(stack, 0, 9, 15, 9, A0)
    rect(stack, 0, 0, 0, 15, A0)
    rect(stack, 15, 0, 15, 15, A2)
    save(stack, "super_greenhouse_emitter_stack")

    flare = blank(A0)  # flared top: dark mouth, lit rim
    rect(flare, 0, 0, 15, 2, A2)
    rect(flare, 0, 0, 0, 15, A1)
    for y in range(4, 16, 4):
        rect(flare, 0, y, 15, y, A1)
    save(flare, "super_greenhouse_emitter_flare")

    top = alloy()
    rect(top, 0, 0, 15, 0, A3)
    rect(top, 3, 3, 12, 12, A0)
    rect(top, 4, 4, 11, 11, A1)
    save(top, "super_greenhouse_emitter_top")

    t = {"particle": "mars:block/super_greenhouse_emitter_tank", "base": "mars:block/super_greenhouse_emitter_base",
         "tank": "mars:block/super_greenhouse_emitter_tank", "front": "mars:block/super_greenhouse_emitter_front",
         "stack": "mars:block/super_greenhouse_emitter_stack", "flare": "mars:block/super_greenhouse_emitter_flare",
         "top": "mars:block/super_greenhouse_emitter_top"}
    els = [
        _el([1, 0, 1], [15, 2, 15], _box("#base", [1, 14, 15, 16], top="#top")),  # plinth + band
        # lower horizontal tank (full width), y2..8; front face carries lamp and plate
        _el([2, 2, 2], [14, 8, 14], _box("#tank", [2, 4, 14, 10], "#front", [2, 0, 14, 6], top="#top", topuv=[2, 2, 14, 14])),
        # upper horizontal tank, narrower, y8..13
        _el([3, 8, 3], [13, 13, 13], _box("#tank", [3, 4, 13, 9], top="#top", topuv=[3, 3, 13, 13])),
        # pipe gantry: two posts beside the stack
        _el([3, 13, 3], [4, 22, 4], _box("#tank", [0, 0, 1, 9], top="#top")),
        _el([12, 13, 3], [13, 22, 4], _box("#tank", [0, 0, 1, 9], top="#top")),
        # vent stack y13..29 with hazard stripe on its lower 6 px (uv rows 10..16)
        _el([6, 13, 6], [10, 29, 10], _box("#stack", [0, 0, 4, 16], top="#flare", topuv=[6, 6, 10, 10])),
        # flared top y29..32
        _el([5, 29, 5], [11, 32, 11], _box("#flare", [0, 0, 6, 3], top="#flare", topuv=[5, 5, 11, 11])),
    ]
    _write_model("super_greenhouse_emitter", t, els)


def check():
    errs = []
    names = ["made_up_band", "made_up_plate"] + ["atmospheric_processor_" + n for n in
             ("base", "side", "front", "top", "canister", "port", "tube", "foot")] + \
            ["super_greenhouse_emitter_" + n for n in ("base", "tank", "front", "stack", "flare", "top")] + ["water_reclaimer_" + n for n in
             ("base", "side", "front", "frame", "canister", "top", "tube", "spout")]
    for n in names:
        im = Image.open(os.path.join(TEX, n + ".png"))
        if im.size != (16, 16) or im.mode != "RGBA":
            errs.append(n + " size/mode")
        for x in range(16):
            for y in range(16):
                r, g, b, a = im.getpixel((x, y))
                if a and (max(r, g, b) < 8 or min(r, g, b) > 247):
                    errs.append(n + " pure black/white")
    # band purple must appear only in the base texture bottom rows and overlays
    base = Image.open(os.path.join(TEX, "water_reclaimer_base.png"))
    if base.getpixel((0, 14)) != PURPLE or base.getpixel((2, 14)) == PURPLE:
        errs.append("band dash pattern")
    fr = Image.open(os.path.join(TEX, "water_reclaimer_front.png"))
    if fr.getpixel((10, 12)) != INK or fr.getpixel((10, 9)) != PURPLE:
        errs.append("plate slash")
    for n in ("atmospheric_processor_base", "super_greenhouse_emitter_base"):
        b2 = Image.open(os.path.join(TEX, n + ".png"))
        if b2.getpixel((0, 14)) != PURPLE or b2.getpixel((2, 14)) == PURPLE or b2.getpixel((0, 15)) != PURPLE_DK:
            errs.append(n + " band")
    pf = Image.open(os.path.join(TEX, "atmospheric_processor_front.png"))
    if pf.getpixel((10, 12)) != INK or pf.getpixel((10, 9)) != PURPLE:
        errs.append("processor plate")
    ef = Image.open(os.path.join(TEX, "super_greenhouse_emitter_front.png"))
    if ef.getpixel((10, 9)) != INK or ef.getpixel((13, 6)) != INK or ef.getpixel((10, 6)) != PURPLE:
        errs.append("emitter plate")
    return errs


if __name__ == "__main__":
    overlays()
    reclaimer()
    processor()
    emitter()
    e = check()
    print("OK" if not e else "FAIL " + "; ".join(e))
    sys.exit(1 if e else 0)
