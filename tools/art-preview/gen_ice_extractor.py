#!/usr/bin/env python3
"""Ice extractor art (Lead Artist, 2026-11-28). Spec: docs/art/ice-extractor.md.
Real idea: the Rodriguez Well (NTRS 20205011353): a heated drill sleeve melts ice, a pump brings water to a tank.
Size, band, plate and every number are INVENTED. Writes new files only (textures, models ice_extractor,
_off, _idle, _running, item model). No blockstate, no registration.
Reuses the helpers of gen_invented_machines.py (palette, band, plate). Run: python3 -I tools/art-preview/gen_ice_extractor.py"""
import json
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from gen_invented_machines import (A0, A1, A2, A3, ICE, ICE_D, INFO, INK, ORANGE, PURPLE, PURPLE_DK, SALT, SCREEN,  # noqa: E402
                                   TEX, MODELS, alloy, band, blank, hx, plate, rect, save, _box, _el, _write_model)
from PIL import Image  # noqa: E402

HOT = hx("#FFD27A")          # melt glow (same warm tone as the oven coil: heat, not a new hue)
HEAT = hx("#B37400")         # heated sleeve base tone in the running frames
STEAM, STEAM_L = hx("#E4E8EA"), hx("#C2CDD5")
N = "ice_extractor_"


def make_textures():
    base = alloy()
    rect(base, 0, 0, 15, 0, A2)
    rect(base, 0, 13, 15, 13, A0)
    band(base)
    save(base, N + "base")

    # pump skid side (low box on the front of the plinth)
    side = alloy()
    rect(side, 0, 0, 15, 0, A0)
    rect(side, 0, 15, 15, 15, A0)
    for x in (2, 13):
        side.putpixel((x, 4), A3)
    save(side, N + "side")

    # front of the skid. Model crops uv [2,2,14,8]: 12 x 6 px. Lamp ring (hollow square) left, plate right.
    f = alloy()
    rect(f, 2, 2, 13, 7, A1)
    rect(f, 2, 2, 13, 2, A0)
    rect(f, 2, 7, 13, 7, A0)
    rect(f, 3, 2, 7, 6, SCREEN)   # lamp frame 5 x 5
    rect(f, 4, 3, 6, 5, INFO)     # hollow ring (8 px lit)
    f.putpixel((5, 4), SCREEN)
    plate(f, 9, 3)                # 4 x 4 plate at x9-12, y3-6; slash from (9,6) to (12,3)
    save(f, N + "front")

    # lamp states (shape: off = nothing lit; idle = hollow ring; running = solid), same language as the table
    off = f.copy()
    rect(off, 4, 3, 6, 5, SCREEN)
    save(off, N + "front_off")
    run = f.copy()
    rect(run, 4, 3, 6, 5, INFO)
    save(run, N + "front_running")

    leg = blank(A1)  # tripod leg: darker alloy, one lit edge, bolts
    rect(leg, 0, 0, 0, 15, A2)
    rect(leg, 15, 0, 15, 15, A0)
    for y in (3, 11):
        rect(leg, 0, y, 15, y, A0)
    save(leg, N + "leg")

    heater = alloy()  # heat-exchanger collar: alloy with orange fin lines (2 px, shape = horizontal fins)
    rect(heater, 0, 0, 15, 15, A1)
    for y in (3, 7, 11):
        rect(heater, 0, y, 15, y + 1, ORANGE)
        rect(heater, 0, y + 2, 15, y + 2, A0)
    rect(heater, 0, 0, 0, 15, A2)
    save(heater, N + "heater")

    sleeve = blank(A2)  # drill sleeve: pale alloy tube with ring seams every 4 px
    for y in range(0, 16, 4):
        rect(sleeve, 0, y, 15, y, A1)
    rect(sleeve, 0, 0, 0, 15, A3)
    rect(sleeve, 15, 0, 15, 15, A1)
    save(sleeve, N + "sleeve")

    # running: heated sleeve with a 2 px bright melt-glow ring that climbs (frame 0 low, frame 3 high)
    frames = []
    for top in (12, 8, 4, 0):
        fr = sleeve.copy()
        rect(fr, 0, top, 15, top + 1, HOT)
        if top + 2 <= 15:
            rect(fr, 0, top + 2, 15, top + 2, HEAT)
        if top - 1 >= 0:
            rect(fr, 0, top - 1, 15, top - 1, HEAT)
        frames.append(fr)
    s = Image.new("RGBA", (16, 64))
    for i, fr in enumerate(frames):
        s.paste(fr, (0, 16 * i))
    save(s, N + "sleeve_running")
    with open(os.path.join(TEX, N + "sleeve_running.png.mcmeta"), "w") as fh:
        json.dump({"animation": {"frametime": 10}}, fh, indent=1)

    top = alloy()  # pump box lid, with a small orange touch cap
    rect(top, 0, 0, 15, 0, A3)
    rect(top, 5, 5, 10, 10, A0)
    rect(top, 6, 6, 9, 9, A1)
    rect(top, 7, 7, 8, 8, ORANGE)
    save(top, N + "top")

    pump = alloy()  # pump box side: stacked plates, bolts
    rect(pump, 0, 0, 15, 0, A3)
    rect(pump, 0, 15, 15, 15, A0)
    rect(pump, 0, 7, 15, 7, A0)
    for x in (2, 13):
        for y in (3, 11):
            pump.putpixel((x, y), A3)
    save(pump, N + "pump")

    tank = blank(ICE_D)  # collection tank: ice-water tone with alloy hoops (rings = shape)
    for x in (0, 5, 10, 15):
        rect(tank, x, 0, x, 15, A1)
    rect(tank, 0, 0, 15, 0, A2)
    rect(tank, 0, 15, 15, 15, A0)
    for x in (2, 7, 12):
        tank.putpixel((x, 7), ICE)
    save(tank, N + "tank")

    tt = blank(A1)  # tank top with a vent stub socket
    rect(tt, 0, 0, 15, 0, A3)
    rect(tt, 4, 4, 11, 11, A0)
    rect(tt, 5, 5, 10, 10, ICE_D)
    save(tt, N + "tank_top")

    hose = blank(SALT)  # insulated hose: salt-crust, ribbed like the processor tubing
    for y in range(0, 16, 4):
        rect(hose, 0, y, 15, y, A3)
    rect(hose, 0, 0, 0, 15, hx("#C9C3B5"))
    save(hose, N + "hose")

    # steam puff (animated, cutout): rises from the tank vent, 4 frames, 4 x 6 px used (cols 6..9)
    shapes = [
        ["....", "....", "....", "....", ".oo.", ".##."],
        ["....", "....", ".oo.", ".##.", "####", ".##."],
        ["....", ".o..", ".##.", "####", ".##.", "..o."],
        ["o...", "..o.", "..#.", ".o#.", "....", "...."],
    ]
    pf = []
    for rows in shapes:
        im = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
        for y, r in enumerate(rows):
            for x, c in enumerate(r):
                if c != ".":
                    im.putpixel((6 + x, y), STEAM if c == "#" else STEAM_L)
        pf.append(im)
    s = Image.new("RGBA", (16, 64))
    for i, fr in enumerate(pf):
        s.paste(fr, (0, 16 * i))
    save(s, N + "steam")
    with open(os.path.join(TEX, N + "steam.png.mcmeta"), "w") as fh:
        json.dump({"animation": {"frametime": 10}}, fh, indent=1)


def make_models():
    t = {"particle": "mars:block/" + N + "side"}
    for k in ("base", "side", "front", "leg", "heater", "sleeve", "top", "pump", "tank", "tank_top", "hose"):
        t[k] = "mars:block/" + N + k
    els = [
        # plinth y0..2, made-up band via uv v 14..16
        _el([1, 0, 1], [15, 2, 15], _box("#base", [1, 14, 15, 16], top="#top", topuv=[1, 1, 15, 15])),
        # pump skid y2..8 on the front; lamp and plate on the north face
        _el([2, 2, 1], [14, 8, 4], _box("#side", [0, 2, 3, 8], "#front", [2, 2, 14, 8], top="#top", topuv=[2, 6, 14, 9])),
        # tripod: west and east legs (lower foot, upper brace), south leg
        _el([2, 2, 6], [4, 7, 8], _box("#leg", [0, 0, 2, 5], top="#leg")),
        _el([4, 7, 6], [6, 12, 8], _box("#leg", [0, 0, 2, 5], top="#leg")),
        _el([12, 2, 6], [14, 7, 8], _box("#leg", [0, 0, 2, 5], top="#leg")),
        _el([10, 7, 6], [12, 12, 8], _box("#leg", [0, 0, 2, 5], top="#leg")),
        _el([7, 2, 12], [9, 7, 14], _box("#leg", [0, 0, 2, 5], top="#leg")),
        _el([7, 7, 10], [9, 12, 12], _box("#leg", [0, 0, 2, 5], top="#leg")),
        # heater collar y10..16 with orange fins
        _el([5, 10, 5], [11, 16, 11], _box("#heater", [5, 0, 11, 6], top="#top", topuv=[5, 5, 11, 11])),
        # drill sleeve y16..32 (animated texture in the running model)
        _el([6, 16, 6], [10, 32, 10], _box("#sleeve", [6, 0, 10, 16], top="#top", topuv=[6, 6, 10, 10])),
        # pump box on the sleeve top y26..32, offset east (asymmetric top: unlike the centred emitter flare)
        _el([6, 26, 5], [14, 32, 11], _box("#pump", [6, 0, 14, 6], top="#top", topuv=[6, 5, 14, 11])),
        # collection tank (back east) y2..8
        _el([10, 2, 9], [15, 8, 15], _box("#tank", [0, 0, 6, 6], top="#tank_top", topuv=[0, 0, 5, 6])),
        # hose from the heater collar east, then down into the tank
        _el([11, 10, 9], [14, 12, 11], _box("#hose", [0, 0, 3, 2], top="#hose")),
        _el([12, 8, 9], [14, 12, 11], _box("#hose", [0, 0, 2, 4], top="#hose")),
        # vent stub on the tank top
        _el([13, 8, 13], [15, 10, 15], _box("#leg", [0, 0, 2, 2], top="#leg")),
    ]
    _write_model("ice_extractor", t, els)

    def variant(name, swap, extra=None, rt=None):
        m = json.load(open(os.path.join(MODELS, "ice_extractor.json")))
        for k, v in swap.items():
            assert k in m["textures"], k
            m["textures"][k] = "mars:block/" + v
        if extra:
            m["textures"].update(extra["textures"])
            m["elements"].append(extra["element"])
        if rt:
            m["render_type"] = rt
        with open(os.path.join(MODELS, name + ".json"), "w") as fh:
            json.dump(m, fh, indent=1)

    variant("ice_extractor_off", {"front": N + "front_off"})
    variant("ice_extractor_idle", {})
    puff = {"from": [12, 10, 12], "to": [16, 16, 16],
            "faces": {d: {"texture": "#steam", "uv": [6, 0, 10, 6]} for d in ("north", "south", "east", "west")}}
    variant("ice_extractor_running", {"front": N + "front_running", "sleeve": N + "sleeve_running"},
            {"textures": {"steam": "mars:block/" + N + "steam"}, "element": puff}, "minecraft:cutout")

    item = os.path.join(MODELS, "..", "item", "ice_extractor.json")
    with open(item, "w") as fh:
        json.dump({"parent": "mars:block/ice_extractor"}, fh, indent=1)


def check():
    errs = []
    names = [N + n for n in ("base", "side", "front", "front_off", "front_running", "leg", "heater", "sleeve", "top",
                             "pump", "tank", "tank_top", "hose")]
    for n in names:
        im = Image.open(os.path.join(TEX, n + ".png"))
        if im.size != (16, 16) or im.mode != "RGBA":
            errs.append(n + " size/mode")
        for x in range(16):
            for y in range(16):
                r, g, b, a = im.getpixel((x, y))
                if a and (max(r, g, b) < 8 or min(r, g, b) > 247):
                    errs.append(n + " pure black/white")
                if a and g > r + 20 and g > b + 20:
                    errs.append(n + " green at %d,%d" % (x, y))
    for n in ("sleeve_running", "steam"):
        im = Image.open(os.path.join(TEX, N + n + ".png"))
        if im.size != (16, 64):
            errs.append(n + " strip size")
    b = Image.open(os.path.join(TEX, N + "base.png"))
    if b.getpixel((0, 14)) != PURPLE or b.getpixel((2, 14)) == PURPLE or b.getpixel((0, 15)) != PURPLE_DK:
        errs.append("band")
    f = Image.open(os.path.join(TEX, N + "front.png"))
    if f.getpixel((9, 6)) != INK or f.getpixel((12, 3)) != INK or f.getpixel((10, 4)) != PURPLE:
        errs.append("plate")
    # model uv ranges
    for mn in ("ice_extractor", "ice_extractor_off", "ice_extractor_idle", "ice_extractor_running"):
        m = json.load(open(os.path.join(MODELS, mn + ".json")))
        for el in m["elements"]:
            for c in range(3):
                if not (0 <= el["from"][c] < el["to"][c] <= 32 if c == 1 else 0 <= el["from"][c] < el["to"][c] <= 16):
                    errs.append(mn + " element bounds " + str(el["from"]))
            for face in el["faces"].values():
                for v in face.get("uv", []):
                    if not 0 <= v <= 16:
                        errs.append(mn + " uv " + str(face["uv"]))
    return errs


if __name__ == "__main__":
    make_textures()
    make_models()
    e = check()
    print("OK" if not e else "FAIL " + "; ".join(e))
    sys.exit(1 if e else 0)
