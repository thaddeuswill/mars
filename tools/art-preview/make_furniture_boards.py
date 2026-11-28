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
    iso = render(pair, "bunk2", "iso"); side = render(pair, "bunk2", "side")
    shot = Image.open(SHOT).convert("RGB").crop((0, 250, 800, 560))
    cq = B.crop("iss_cq_cristoforetti_sleeping_bag.jpg", (280, 280, 720, 1000))
    nes = B.crop("iss_sleeping_bag_tethered_nespoli.jpg", (0, 0, 960, 640))
    chap = B.crop("chapea_crew_quarters_rooms.jpg", (150, 360, 400, 640))
    row = [("OLD v1 (blue bag, orange strap, drawers), in game shot", shot),
           ("REF: ISS CQ light-green quilted bag, white zip + Velcro (NASA)", cq),
           ("REF: Harmony olive bag, navy bands, white cords (NASA)", nes),
           ("REF: CHAPEA low bed, dark frame, light quilt (NASA)", chap),
           ("NEW v2 bunk pair, iso (head + foot)", iso), ("NEW v2 side", side), ("NEW v2 iso, DIM (measured light)", dim(iso)),
           ("NEW v2 at in-game size, DIM", game_like(dim(iso)))]
    notes = ["Reproduced: muted light-green quilted bag with white zip tape and a white Velcro patch (CQ photo), navy band with white stitches and silver buckle (Harmony bag), plain alloy frame, low 1 x 2 bed (CHAPEA), white pillow, Velcro squares and a yellow pouch on the head board (CQ wall).",
             "Not reproduced: the enclosed quilted compartment and cloth door, white tether cords (microgravity cue, left out), olive variant, CHAPEA's dark-black frame (lifted to grey alloy for the dim lamp), the real bag's shape (ours is a flat slab); drawers removed: no photo shows them.",
             "Hex values are by eye from photos (unverified). Blender iso, NOT Minecraft; not yet seen in game. Dim = factor (0.4375, 0.396, 0.3375) from board 12."]
    B.board("14_habitat_bunk_v2", "HABITAT BUNK v2 (habitat_bunk, 2 blocks): old v1 | real references | new renders", row, notes)

    closed, opened = load("stowage_crate"), load("stowage_crate_open")
    ci = render(closed, "crate2_c", "iso"); cf = render(closed, "crate2_c", "front"); oi = render(opened, "crate2_o", "iso")
    bag = render(load("stowage_bag"), "bag2", "iso")
    mplm = B.crop("mplm_leonardo_stowage_racks.jpg", (140, 160, 420, 440))
    mplm2 = B.crop("mplm_leonardo_stowage_racks.jpg", (520, 380, 900, 600))
    ctb = B.crop("iss_cargo_transfer_bag_ohara.jpg", (240, 380, 820, 860))
    row = [("REF: MPLM grey lockers, yellow/orange label tags, cut-outs (NASA)", mplm),
           ("REF: MPLM white bags, tan straps, buckles (NASA)", mplm2),
           ("REF: cargo transfer bag, yellow label card, Velcro tabs (NASA)", ctb),
           ("NEW v2 crate closed, iso", ci), ("NEW front", cf), ("NEW open (door 45 deg)", oi),
           ("NEW closed, DIM", dim(ci)), ("NEW closed in-game size, DIM", game_like(dim(ci))),
           ("OPTIONAL soft bag stack (not registered)", bag), ("OPTIONAL soft bag, DIM", dim(bag))]
    notes = ["Reproduced: off-white locker door with a yellow label card (text lines, no real text), three dark cut-out slots, two grey latches, bolts, stacking corner fittings; skid dropped (real racks have none), only a 1 px grey kick plate remains. Optional soft bags: pale cloth, two tan straps with grey buckles, yellow card.",
             "Not reproduced: grey metal colour (ours is warm white hull paint), a 2 x 2 door grid, barcodes and printed serials, strap criss-cross, the cluttered look of real stowage. Label card colour yellow by eye (unverified hex).",
             "Full-block footprint like the barrel. Open state swings the door outside the cell (needs noOcclusion). Blender iso, NOT Minecraft; not yet seen in game."]
    B.board("15_stowage_crate_v2", "STOWAGE CRATE v2 (stowage_crate): real stowage | new renders", row, notes)


main()
