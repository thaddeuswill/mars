"""Hull light (2026-11-27): ceiling light panel block set 'hull_light'. Owner: Lead Artist.
Reference by eye (docs/art/references/habitat/): ISS lab light box (iss_wrs_rack_open_fincke.jpg, upper right
of the rack), CHAPEA dome wall/ceiling lamps (chapea_sandbox_360.jpg), flat LED/fluorescent panels in an
aluminium bezel. Not measured. PLACEHOLDER-free: no magenta marker (draft art, reference-checked).

Writes pack/assets/mars/: textures/block/hull_light.png (lit panel, the ceiling face),
hull_light_off.png (SPARE: dark panel for a future powered/blackout state, not used by the blockstate),
models/block/hull_light.json (full cube: panel on the DOWN face, hull plate elsewhere),
models/block/hull_light_off.json (spare), blockstates/hull_light.json, models/item/hull_light.json.
Usage: python3 -I tools/art-preview/gen_hull_light.py   (Pillow only). Exits non-zero if a check fails."""
import json, os, sys
from PIL import Image

ROOT = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", ".."))
A = os.path.join(ROOT, "pack", "assets", "mars")
TEX = os.path.join(A, "textures", "block")


def rgb(h):
    h = h.lstrip("#")
    return tuple(int(h[i:i + 2], 16) for i in (0, 2, 4))


# hull plate palette (gen_habitat_restyle.py) + alloy ramp (style guide 2.2)
H_DARK, AL3, AL4 = "#7C786C", "#7E8790", "#A8B0B7"
# lamp colour #F5E3C3 is the dome lamp colour of style guide section 8; the other two are one step either side
P_EDGE, P_MID, P_CORE = "#E6D2A8", "#F5E3C3", "#FBF1DA"
# off state: the same diffuser, unlit (greyed cream), for a later blackout state
O_EDGE, O_MID, O_CORE = "#8C877A", "#9A9586", "#A6A192"


def panel(edge, mid, core):
    im = Image.new("RGBA", (16, 16), rgb(H_DARK) + (255,))
    for y in range(16):
        for x in range(16):
            ring = min(x, y, 15 - x, 15 - y)
            if ring == 0:
                c = H_DARK          # panel seam, same as the hull plate seam
            elif ring == 1:
                c = AL4             # aluminium bezel, light edge
            elif ring == 2:
                c = AL3             # bezel lip
            elif ring == 3:
                c = edge            # diffuser edge (soft fall-off)
            elif ring <= 5:
                c = mid
            else:
                c = core            # flat bright core, 4 x 4
            im.putpixel((x, y), rgb(c) + (255,))
    for (x, y) in ((1, 1), (14, 1), (1, 14), (14, 14)):   # corner screws
        im.putpixel((x, y), rgb(H_DARK) + (255,))
    return im


def write(path, obj):
    with open(path, "w") as f:
        json.dump(obj, f, indent=1)


def model(panel_tex):
    p, h = "mars:block/" + panel_tex, "mars:block/hull_plate"
    return {"parent": "minecraft:block/cube",
            "textures": {"particle": h, "down": p, "up": h, "north": h, "south": h, "east": h, "west": h}}


if __name__ == "__main__":
    fails = []
    for name, cols in (("hull_light", (P_EDGE, P_MID, P_CORE)), ("hull_light_off", (O_EDGE, O_MID, O_CORE))):
        im = panel(*cols)
        im.save(os.path.join(TEX, name + ".png"))
        used = {im.getpixel((x, y))[:3] for x in range(16) for y in range(16)}
        if im.size != (16, 16) or len(used) > 6:
            fails.append(f"{name}: size or colour count {len(used)}")
        if (0, 0, 0) in used or (255, 255, 255) in used or (255, 0, 255) in used:
            fails.append(f"{name}: forbidden colour")
        print(name, len(used), "colours")
    write(os.path.join(A, "models", "block", "hull_light.json"), model("hull_light"))
    write(os.path.join(A, "models", "block", "hull_light_off.json"), model("hull_light_off"))
    write(os.path.join(A, "blockstates", "hull_light.json"), {"variants": {"": {"model": "mars:block/hull_light"}}})
    write(os.path.join(A, "models", "item", "hull_light.json"), {"parent": "mars:block/hull_light"})
    if fails:
        print("\n".join("FAIL " + x for x in fails)); sys.exit(1)
    print("OK")
