"""Boards 10 (oxygen maker v2) and 11 (water reclaimer v2) plus the numeric contrast comparison. Owner: Lead Artist.
Usage: python3 -I tools/art-preview/make_maker_reclaimer_boards.py [OLD_TEX_DIR OLD_MAKER_JSON]   (needs Blender, Pillow)
Writes docs/art/renders/habitat-restyle/10_oxygen_maker_v2.png, 11_reclaimer_v2.png and prints the numbers.
Dim light = Minecraft face shading (done by render_model.py --shading mc) times (0.53, 0.48, 0.40), a factor measured
from the game screenshot tools/screenshots/out/20261127-232753/hab_11_machine_bay_wide_lamps.png (hull plate
(218,214,201)*0.8 appears as (92,82,64)). Blender is NOT Minecraft: no ambient occlusion, no smooth lighting."""
import json, os, sys
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import make_habitat_boards as B
from PIL import Image, ImageFilter

DIM = (0.53, 0.48, 0.40)
OLD_TEX = sys.argv[1] if len(sys.argv) > 1 else None
OLD_MAKER = sys.argv[2] if len(sys.argv) > 2 else None


def render(model, texdir, name, view="iso", transparent=False):
    mp = os.path.join(B.SCR, name + ".json"); json.dump(model, open(mp, "w"))
    out = os.path.join(B.SCR, f"{name}_{view}{'_t' if transparent else ''}.png")
    import subprocess
    extra = ["--transparent"] if transparent else []
    r = subprocess.run(["blender", "-b", "-P", os.path.join(B.HERE, "render_model.py"), "--", mp, texdir, out, view, "--size", "560", *extra], capture_output=True, text=True)
    if "RENDERED" not in r.stdout:
        sys.exit("render failed " + name + r.stdout[-500:])
    return Image.open(out).convert("RGBA")


def dim(im):
    r = Image.new("RGBA", im.size)
    px = im.load(); o = r.load()
    for y in range(im.height):
        for x in range(im.width):
            p = px[x, y]
            o[x, y] = (int(p[0] * DIM[0]), int(p[1] * DIM[1]), int(p[2] * DIM[2]), p[3])
    return r


def lum(p):
    return 0.2126 * p[0] + 0.7152 * p[1] + 0.0722 * p[2]


def stats(im, mask_fn, alpha=True):
    """mean luminance, P90-P10 (mean of top 10 % minus mean of bottom 10 %) over the object pixels, 0..255 scale."""
    L = []
    px = im.convert("RGBA").load()
    for y in range(im.height):
        for x in range(im.width):
            p = px[x, y]
            if (not alpha or p[3] > 200) and mask_fn(p):
                L.append(lum(p))
    L.sort(); n = len(L); k = max(1, n // 10)
    return {"n": n, "mean": sum(L) / n, "spread": sum(L[-k:]) / k - sum(L[:k]) / k}


def gold(p):
    return p[0] > 60 and (p[0] - p[2]) / (p[0] + 1) > 0.35 and p[1] > 0.55 * p[0]


def edge_density(im, w=128, thr=28):
    g = im.convert("L"); g = g.resize((w, int(g.height * w / g.width)), Image.LANCZOS)
    px = g.load(); n = e = 0
    for y in range(g.height - 1):
        for x in range(g.width - 1):
            n += 1
            if abs(px[x, y] - px[x + 1, y]) > thr or abs(px[x, y] - px[x, y + 1]) > thr:
                e += 1
    return e / n


def refcrop(fn, box):
    return B.crop(fn, box).convert("RGBA")


def tiny(im, h=150):
    return im.resize((int(im.width * h / im.height), h), Image.LANCZOS)


def main():
    A = B.A; T = B.TEX
    mk = json.load(open(os.path.join(A, "models", "block", "o2_generator.json")))
    rc = json.load(open(os.path.join(A, "models", "block", "water_reclaimer.json")))
    new_n = render(mk, T, "mk_new"); new_t = render(mk, T, "mk_new", transparent=True)
    comp = lambda opaque, tr: Image.composite(opaque, Image.new("RGBA", opaque.size, (0, 0, 0, 0)), tr.split()[3])
    out = {}
    ref = refcrop("PIA24201.jpg", (520, 100, 935, 700))              # the big gold face only
    ref_all = refcrop("PIA24201.jpg", (130, 90, 945, 900))
    out["ref_face"] = stats(ref, gold, alpha=False)
    out["new_normal"] = stats(new_t, gold); out["new_dim"] = stats(dim(new_t), gold)
    if OLD_TEX and OLD_MAKER:
        om = json.load(open(OLD_MAKER)); old_t = render(om, OLD_TEX, "mk_old", transparent=True); old_n = render(om, OLD_TEX, "mk_old")
        out["old_normal"] = stats(old_t, gold); out["old_dim"] = stats(dim(old_t), gold)
        old_dim = dim(old_n)
    new_dim = dim(new_n)
    front_n = render(mk, T, "mk_new", "front")
    row = [("REF: MOXIE, big gold face + tubes (PIA24201, NASA/JPL)", ref_all.convert("RGB")),
           ("NEW iso, normal light (Blender, mc shading)", new_n.convert("RGB")),
           ("NEW iso, DIM warm light (x0.53,0.48,0.40)", new_dim.convert("RGB")),
           ("NEW front, DIM", dim(front_n).convert("RGB"))]
    if OLD_TEX:
        row.insert(2, ("OLD v1 iso, DIM warm light", old_dim.convert("RGB")))
    st = out
    note = lambda k: f"{st[k]['mean']:.0f} mean, spread {st[k]['spread']:.0f}"
    notes = [f"Gold-pixel luminance (0..255, mean | top 10 % minus bottom 10 %): REF {note('ref_face')} | NEW normal {note('new_normal')} | NEW dim {note('new_dim')}"
             + (f" | OLD normal {note('old_normal')} | OLD dim {note('old_dim')}" if OLD_TEX else ""),
             "Reproduced: bright gold with a diagonal specular band, dark pocket grooves in a 3 x 4 grid, white tubes with silver fittings in two loops, copper base plate, Kapton amber top plate with a silver connector.",
             "Not reproduced: true mirror reflection (the band is painted), 4 x 4 bigger panels (3 x 4 fits 16 px), the many hex bolts, smooth tube curves (squared loops), the X-rib face is at the east end only.",
             "Scale: body 10 x 12 x 10 px = 0.62 x 0.75 x 0.62 m, about 3 x MOXIE. Hitbox unchanged (1..15, 0..15, 1..15). Blender iso, NOT Minecraft."]
    B.board("10_oxygen_maker_v2", "OXYGEN MAKER v2 vs MOXIE: reference | new (normal and dim light) | numbers below", row, notes)

    # ---- reclaimer
    f = lambda n: {"texture": "#" + n}
    old_rec = {"textures": {"s": "mars:block/water_reclaimer", "t": "mars:block/water_reclaimer_top"},
               "elements": [{"from": [0, 0, 0], "to": [16, 16, 16], "faces": {"north": f("s"), "south": f("s"), "east": f("s"), "west": f("s"), "up": f("t"), "down": f("t")}}]}
    rn = render(rc, T, "rc_new"); rt = render(rc, T, "rc_new", transparent=True)
    rows = [("REF: ISS water racks, closed (Peake)", refcrop("iss_wpa_rack_peake.jpg", (0, 380, 760, 666))),
            ("REF: rack open (Fincke)", refcrop("iss_wrs_rack_open_fincke.jpg", (0, 0, 1000, 700))),
            ("NEW iso, normal", rn), ("NEW iso, DIM", dim(rn)), ("NEW front, DIM", dim(render(rc, T, "rc_new", "front")))]
    ed = {"ref_peake": edge_density(refcrop("iss_wpa_rack_peake.jpg", (0, 380, 760, 666))),
          "ref_fincke": edge_density(refcrop("iss_wrs_rack_open_fincke.jpg", (0, 0, 1000, 700))),
          "new": edge_density(rt.crop(rt.getbbox()).convert("RGB") if rt.getbbox() else rt)}
    if OLD_TEX:
        # old textures: the v1 files are in OLD_TEX (water_reclaimer.png / _top)
        old_r = render(old_rec, OLD_TEX, "rc_old"); old_rt = render(old_rec, OLD_TEX, "rc_old", transparent=True)
        ed["old"] = edge_density(old_rt.crop(old_rt.getbbox()).convert("RGB"))
        rows.insert(2, ("OLD v1 iso, DIM (clean cube)", dim(old_r)))
    out["edge"] = ed
    s = lambda k: f"{ed[k]:.2f}"
    notes = [f"Edge density (share of pixels with a luminance step > 28 at 128 px width; higher = more clutter): REF closed {s('ref_peake')} | REF open {s('ref_fincke')} | NEW {s('new')}" + (f" | OLD {s('old')}" if OLD_TEX else ""),
             "Reproduced: label card with a red cross, two latch handles per face, dark open-panel strip with silver studs and a green card, hose loop standing off the face, vertical grey and green braided hoses with clamps, blue handrail edge on an overhanging top shroud.",
             "Not reproduced: the grid of many rack bays, hanging cables and bags, text, yellow tags in numbers, anything beyond the block (hitbox 1..15). Real racks are more cluttered than a 16 px block can show.",
             "The block has no facing (blockstate has one variant): north and south carry the label and strip, east and west the latches and hoses."]
    B.board("11_reclaimer_v2", "WATER RECLAIMER v2 vs ISS racks: references | new (normal and dim light) | numbers below", rows, notes)
    print(json.dumps(out, indent=1))


main()
