"""Boards 12 (oxygen maker v3) and 13 (water reclaimer v3). Owner: Lead Artist. Needs Blender and Pillow.
Usage: python3 -I tools/art-preview/make_maker_reclaimer_boards_v3.py
Dim light = the factor measured on the real in-game maker face (tools/screenshots/out/20261127-233726/mk_2_maker_2m_day.png):
v2 face texture mean rgb (114,92,41) appears as (40,29,11) on a north face (shade 0.8): per channel (0.35,0.317,0.27), so
the light-only factor is that divided by 0.8 = (0.4375,0.396,0.3375). Hull wall in the same shot: luminance 98.
Blender is NOT Minecraft: no ambient occlusion, no smooth lighting. render_model.py honours "shade": false."""
import json, os, subprocess, sys
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import make_habitat_boards as B
from PIL import Image

DIM = (0.4375, 0.396, 0.3375)
SHOT = os.path.join(B.ROOT, "tools", "screenshots", "out", "20261127-233726")
L = lambda p: 0.2126 * p[0] + 0.7152 * p[1] + 0.0722 * p[2]


def render(model, name, view="iso"):
    mp = os.path.join(B.SCR, name + ".json"); json.dump(model, open(mp, "w"))
    out = os.path.join(B.SCR, f"{name}_{view}.png")
    r = subprocess.run(["blender", "-b", "-P", os.path.join(B.HERE, "render_model.py"), "--", mp, B.TEX, out, view, "--size", "560"], capture_output=True, text=True)
    if "RENDERED" not in r.stdout:
        sys.exit("render failed " + name + r.stdout[-400:])
    return Image.open(out).convert("RGB")


def dim(im):
    o = Image.new("RGB", im.size); a = im.load(); b = o.load()
    for y in range(im.height):
        for x in range(im.width):
            p = a[x, y]; b[x, y] = tuple(int(p[i] * DIM[i]) for i in range(3))
    return o


def game_like(im, h=150):
    """Shrink to the on-screen size of a block at 2 m (about 150 px of 720) and back up without smoothing."""
    s = im.resize((int(im.width * h / im.height), h), Image.LANCZOS)
    return s.resize((s.width * 3, s.height * 3), Image.NEAREST)


def tex_lum(name, box, k):
    t = Image.open(os.path.join(B.TEX, name + ".png")).convert("RGB").crop(box)
    px = list(t.get_flattened_data() if hasattr(t, "get_flattened_data") else t.getdata())
    m = [sum(p[i] for p in px) / len(px) for i in range(3)]
    return L([m[i] * DIM[i] * k for i in range(3)])


def main():
    A = B.A
    mk = json.load(open(os.path.join(A, "models", "block", "o2_generator.json")))
    rc = json.load(open(os.path.join(A, "models", "block", "water_reclaimer.json")))
    ingame = Image.open(os.path.join(SHOT, "mk_2_maker_2m_day.png")).convert("RGB").crop((520, 350, 780, 610))
    moxie = B.crop("PIA24201.jpg", (130, 90, 945, 900))
    iso = render(mk, "mk3"); front = render(mk, "mk3", "front")
    # numbers: on-screen luminance at 2 m. v2: measured. v3: texture mean x light factor x (1.0 shade-free / 0.8 already in v2 measure)
    v2 = 30.2
    v3_front = tex_lum("o2_generator_face", (3, 4, 13, 16), 1.0)
    wall = 98.5
    row = [("REF: MOXIE (PIA24201, NASA/JPL)", moxie), ("IN GAME v2, 2 m (real shot mk_2)", ingame),
           ("v3 iso, DIM (measured factor)", dim(iso)), ("v3 front, DIM", dim(front)), ("v3 front, DIM, at in-game size", game_like(dim(front)))]
    notes = [f"On-screen face luminance at 2 m (0..255): v2 MEASURED {v2:.0f} | v3 ESTIMATED {v3_front:.0f} (texture mean x measured light factor, shade false) | hull wall MEASURED {wall:.0f} | v3 / wall = {v3_front / wall:.2f}",
             "Reproduced: big pale-gold pockets (4 x 3 px) with thin 1 px seams (#70511A), bright highlight band, copper base, amber Kapton plate, white tube loops incl. a front loop standing off the face, X-rib east end.",
             "Not reproduced: mirror reflection (painted highlight), 4 x 4 panel count (2 x 3 fits 10 x 12 px), many hex bolts, smooth tube curves, wall-equal brightness (estimate is below the wall).",
             "Body 10 x 12 x 10 px, hitbox unchanged. Elements use shade:false (no per-face darkening). Blender iso, NOT Minecraft; v3 is NOT yet seen in game."]
    B.board("12_oxygen_maker_v3", "OXYGEN MAKER v3 vs MOXIE: reference | in game v2 | v3 renders in measured dim light", row, notes)

    # reclaimer: front (north) and back
    rn = render(rc, "rc3"); rfront = render(rc, "rc3", "front"); rback = render(rc, "rc3", "back")
    ing = Image.open(os.path.join(SHOT, "mk_4_reclaimer_2m_day.png")).convert("RGB").crop((500, 330, 780, 610))
    rows = [("REF: rack open (Fincke, NASA)", B.crop("iss_wrs_rack_open_fincke.jpg", (0, 0, 1000, 700))),
            ("REF: closed racks (Peake, NASA)", B.crop("iss_wpa_rack_peake.jpg", (0, 380, 760, 666))),
            ("IN GAME v2, 2 m (real shot mk_4)", ing), ("v3 iso, DIM", dim(rn)),
            ("v3 FRONT (north) DIM", dim(rfront)), ("v3 BACK (south): plain", dim(rback)), ("v3 front at in-game size", game_like(dim(rfront)))]
    notes = ["One-sided: label card 8 x 8 px with a 6 x 6 red cross (arms 2 px), latch handle, open strip with studs, hose ring: north face only. South, east, west: plain panel, one seam, four bolts.",
             "Blockstate has facing=north|east|south|west (y 0/90/180/270), the front is the model's north face, like oxygen_maker.json. The dome places it facing south.",
             "Not reproduced: rack clutter (cables, bags, many labels, panels at different depths); still a clean unit. The cross must be read by shape, not colour.",
             "Blender iso, NOT Minecraft; v3 is NOT yet seen in game."]
    B.board("13_reclaimer_v3", "WATER RECLAIMER v3 vs ISS racks: references | in game v2 | v3 front and back (dim)", rows, notes)
    print(json.dumps({"v3_front_lum_est": v3_front, "v2_measured": v2, "wall": wall, "ratio": v3_front / wall}))


main()
