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


def check():
    errs = []
    names = ["made_up_band", "made_up_plate"] + ["water_reclaimer_" + n for n in
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
    return errs


if __name__ == "__main__":
    overlays()
    reclaimer()
    e = check()
    print("OK" if not e else "FAIL " + "; ".join(e))
    sys.exit(1 if e else 0)
