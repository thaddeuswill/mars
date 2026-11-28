"""Boards 14 (habitat bunk) and 15 (stowage crate). Owner: Lead Artist. Needs Blender and Pillow.
Usage: python3 -I tools/art-preview/make_furniture_boards.py
Blender is NOT Minecraft: no ambient occlusion, no smooth light. Dim = the measured in-game light factor used in board 12.
The bunk pair render is head part at z 0..16 and foot part at z 16..32 (the item model)."""
import json, os, subprocess, sys
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import make_habitat_boards as B
from PIL import Image

DIM = (0.4375, 0.396, 0.3375)
SHOT = os.path.join(B.ROOT, "tools", "screenshots", "out", "20261127-233344", "lamp_1_shelter_day.png")


def render(model, name, view="iso", extra=()):
    mp = os.path.join(B.SCR, name + ".json"); json.dump(model, open(mp, "w"))
    out = os.path.join(B.SCR, f"{name}_{view}.png")
    r = subprocess.run(["blender", "-b", "-P", os.path.join(B.HERE, "render_model.py"), "--", mp, B.TEX, out, view, "--size", "560", *extra],
                       capture_output=True, text=True)
    if "RENDERED" not in r.stdout:
        sys.exit("render failed " + name + r.stdout[-500:] + r.stderr[-500:])
    return Image.open(out).convert("RGB")


def dim(im):
    o = Image.new("RGB", im.size); a = im.load(); b = o.load()
    for y in range(im.height):
        for x in range(im.width):
            p = a[x, y]; b[x, y] = tuple(int(p[i] * DIM[i]) for i in range(3))
    return o


def game_like(im, h=150):
    s = im.resize((int(im.width * h / im.height), h), Image.LANCZOS)
    return s.resize((s.width * 3, s.height * 3), Image.NEAREST)


def load(n):
    return json.load(open(os.path.join(B.A, "models", "block", n + ".json")))


def main():
    item = json.load(open(os.path.join(B.A, "models", "item", "habitat_bunk.json")))
    pair = {"textures": item["textures"], "elements": item["elements"]}
    iso = render(pair, "bunk", "iso"); side = render(pair, "bunk", "side"); top = render(pair, "bunk", "top")
    shot = Image.open(SHOT).convert("RGB").crop((0, 250, 800, 560))
    peake = B.crop("iss_wpa_rack_peake.jpg", (380, 330, 800, 520))
    hatch = B.crop("quest_airlock_hatch_hurley.jpg", (700, 0, 1000, 400))
    row = [("OLD in game: vanilla red bed (shot 20261127-233344)", shot),
           ("REF: ISS white padded bag with strap (Peake, NASA); NOT a bunk", peake),
           ("REF: Quest wall: webbing strap, mesh grille (NASA)", hatch),
           ("NEW bunk pair, iso (head + foot)", iso), ("NEW side", side), ("NEW iso, DIM (measured light)", dim(iso)),
           ("NEW at in-game size, DIM", game_like(dim(iso)))]
    notes = ["Reproduced: off-white hull-paint shell with drawers and latches (rack-door language of the ISS photo), a padded bag with a strap and a white pillow, orange webbing as the 'touch this' accent, a blank name-tag slot on each end.",
             "Not reproduced: a real crew-sleeping-quarter or sleeping-bag photo was NOT viewed (none in the repo), so the shape is a design choice; real ISS bags are tethered upright to a wall, not lying on a shelf; no privacy curtain, no reading light.",
             "Size: 1 x 2 blocks, 0.56 m mattress top (9 px, same as the vanilla bed so the sleeping pose matches), boards 0.63 m, pillow 0.69 m. Blender iso, NOT Minecraft; not yet seen in game."]
    B.board("14_habitat_bunk", "HABITAT BUNK (habitat_bunk, 2 blocks): old red bed | references | new renders", row, notes)

    closed, opened = load("stowage_crate"), load("stowage_crate_open")
    ci = render(closed, "crate_c", "iso"); cf = render(closed, "crate_c", "front"); oi = render(opened, "crate_o", "iso")
    rack = B.crop("iss_wpa_rack_peake.jpg", (0, 380, 760, 666))
    row = [("REF: closed rack doors, latches, cut-outs, labels (Peake, NASA)", rack),
           ("NEW crate closed, iso", ci), ("NEW crate front", cf), ("NEW crate open (door 45 deg)", oi),
           ("NEW closed, DIM", dim(ci)), ("NEW closed at in-game size, DIM", game_like(dim(ci)))]
    notes = ["Reproduced: off-white panel door with bolts, a latch bar with an orange pull, a label card with a blue band and stylised text lines (the label cards and blue brackets of the ISS photos), a dark alloy skid with fork pockets, stacking corner fittings on the lid.",
             "Not reproduced: rack clutter, real label text, the 'open rack' dark interior with studs (ref #9); the inside is a plain shelf with a white cargo bag. No red cross (that cue belongs to the Water Reclaimer).",
             "Full-block footprint like the barrel it replaces (door at z 0..1). Open state swings the door outside the cell (needs noOcclusion). Blender iso, NOT Minecraft; not yet seen in game."]
    B.board("15_stowage_crate", "STOWAGE CRATE (stowage_crate): ISS rack doors | new renders", row, notes)


main()
