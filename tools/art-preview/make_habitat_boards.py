"""Side-by-side boards: reference crop | our Blender render | our flat texture(s). Owner: Lead Artist.
Usage: python3 -I tools/art-preview/make_habitat_boards.py   (needs Blender and Pillow)
Writes docs/art/renders/habitat-restyle/*.png. Blender renders use flat per-face shading (not Minecraft).
Items marked FLAT COMPOSITE are Pillow tilings of the real textures (no Blender), because the game models
use parents (cube_column, door) that render_model.py does not resolve."""
import json, os, subprocess, sys, tempfile
from PIL import Image, ImageDraw, ImageFont

HERE = os.path.dirname(os.path.abspath(__file__)); ROOT = os.path.abspath(os.path.join(HERE, "..", ".."))
A = os.path.join(ROOT, "pack", "assets", "mars"); TEX = os.path.join(A, "textures", "block")
REF = os.path.join(ROOT, "docs", "art", "references", "habitat")
OUT = os.path.join(ROOT, "docs", "art", "renders", "habitat-restyle"); os.makedirs(OUT, exist_ok=True)
SCR = tempfile.mkdtemp(prefix="habitat-boards-")
F = ImageFont.load_default()
H = 420  # tile height


def tex(n):
    return Image.open(os.path.join(TEX, n + ".png")).convert("RGBA")


def cube_model(faces):
    f = {k: {"texture": "#" + v} for k, v in faces.items()}
    return {"textures": {v: "mars:block/" + v for v in set(faces.values())},
            "elements": [{"from": [0, 0, 0], "to": [16, 16, 16], "faces": f}]}


def blender(model, view, name, extra=()):
    mp = os.path.join(SCR, name + ".json"); json.dump(model, open(mp, "w"))
    out = os.path.join(SCR, f"{name}_{view}.png")
    r = subprocess.run(["blender", "-b", "-P", os.path.join(HERE, "render_model.py"), "--", mp, TEX, out, view, "--size", "560", *extra],
                       capture_output=True, text=True)
    if "RENDERED" not in r.stdout:
        sys.exit("render failed " + name + r.stdout[-800:] + r.stderr[-800:])
    return Image.open(out).convert("RGB")


def fit(im, h=H):
    return im.resize((max(1, int(im.width * h / im.height)), h), Image.LANCZOS if im.width > 100 else Image.NEAREST)


def flat(ims, scale, bg=(120, 160, 200)):
    w = sum(i.width for i in ims) * scale; h = max(i.height for i in ims) * scale
    c = Image.new("RGBA", (w, h), bg + (255,)); x = 0
    for i in ims:
        c.alpha_composite(i.resize((i.width * scale, i.height * scale), Image.NEAREST), (x, 0)); x += i.width * scale
    return c.convert("RGB")


def stack(ims):
    c = Image.new("RGB", (max(i.width for i in ims), sum(i.height for i in ims)))
    y = 0
    for i in ims:
        c.paste(i, (0, y)); y += i.height
    return c


def shade(im, k):
    return Image.eval(im.convert("RGB"), lambda v: int(v * k))


def board(name, title, panels, notes):
    """panels: list of (label, PIL image)."""
    ims = [(l, fit(i)) for l, i in panels]
    W = sum(i.width for _, i in ims) + 10 * (len(ims) + 1)
    top = 34; bot = 18 * len(notes) + 10
    c = Image.new("RGB", (W, top + H + bot), (30, 34, 38)); d = ImageDraw.Draw(c)
    d.text((10, 10), title, fill=(232, 227, 216), font=F)
    x = 10
    for l, i in ims:
        c.paste(i, (x, top)); d.rectangle((x, top, x + i.width, top + 14), fill=(30, 34, 38)); d.text((x + 3, top + 2), l, fill=(240, 228, 120), font=F)
        x += i.width + 10
    for k, n in enumerate(notes):
        d.text((10, top + H + 6 + 18 * k), n, fill=(200, 205, 210), font=F)
    c.save(os.path.join(OUT, name + ".png")); print("BOARD", name)


def crop(fn, box):
    return Image.open(os.path.join(REF, fn)).convert("RGB").crop(box)


def main():
    # ---- 1 hull plate and courses
    courses = ["hull_plate_rim", "hull_plate", "hull_plate", "hull_plate_base"]
    wall = Image.new("RGBA", (12 * 16, 4 * 16), (0, 0, 0, 255))
    for r, tn in enumerate(courses):
        for c in range(12):
            wall.alpha_composite(tex(tn), (c * 16, r * 16))
    wall = shade(wall, 0.8).resize((12 * 16 * 3, 4 * 16 * 3), Image.NEAREST)
    shot = Image.open(os.path.join(ROOT, "tools/screenshots/out/20261127-223929/life_2_cracked_plate_in_hab_b_wall.png")).convert("RGB").crop((0, 320, 900, 620))
    board("01_hull_plate_courses", "HULL PLATE + COURSES: MDRS Hab (ref #12, CC BY 2.5 McKay Salisbury) | old in-game wall (screenshot 20261127-223929) | new wall: rim, plain, plain, base",
          [("REF: MDRS Hab wall, cream trim top and base, vertical seams", crop("mdrs_hab_greenhab.jpg", (30, 110, 610, 660))),
           ("OLD in game: 4 identical rows", shot),
           ("NEW (flat composite, needs the 3 course blocks)", wall)],
          ["Reproduced: off-white warm paint, vertical panel seams (no horizontal grid), cream trim at base and at roof rim, bolts, 1 px light wear.",
           "Not reproduced: roof dish/mast, steps, the curved cylinder, the dark buttress struts, stickers and lettering (no text in textures), real paint weathering.",
           "Needs new blocks hull_plate_base / hull_plate_rim (handoff). With only hull_plate the wall is the plain-seam version (rows 2 and 3)."])

    # ---- 2 cracked
    two = flat([tex("hull_plate"), tex("hull_plate_cracked"), tex("hull_plate")], 12)
    board("02_cracked_plate", "CRACKED PLATE (no real reference exists; invented, DESIGN.md audit)",
          [("REF: none (SME audit 4.2: no real breach reference)", Image.new("RGB", (200, 200), (60, 66, 72))),
           ("NEW: plate | cracked | plate (flat)", two)],
          ["Reproduced: same plate family so the leak reads as damage to a wall, not a different block; crack with bright lip plus a rust dust streak (two channels).",
           "Not reproduced: any real-world look (there is none to copy); this is fiction and goes on the fiction audit table as before."])

    # ---- 3 glass
    g = tex("hull_glass"); gr = tex("hull_glass_ribbed")
    bank = Image.new("RGBA", (5 * 16, 2 * 16), (210, 174, 134, 255))
    for r in range(2):
        for c in range(5):
            bank.alpha_composite(g, (c * 16, r * 16))
    # sky behind the glass: composite glass over horizon colour (alpha is real)
    ribbed = Image.new("RGBA", (4 * 16, 2 * 16), (210, 174, 134, 255))
    for r in range(2):
        for c in range(4):
            ribbed.alpha_composite(gr, (c * 16, r * 16))
    wp = Image.new("RGBA", (5 * 16, 2 * 16), (0, 0, 0, 255))
    for r in range(2):
        for c in range(5):
            wp.alpha_composite(tex("hull_plate"), (c * 16, r * 16))
    panels = [("REF: MDRS round portholes (ref #12)", crop("mdrs_hab_greenhab.jpg", (400, 150, 640, 330))),
              ("NEW hull_glass: 5 x 2 window bank over Mars sky (flat)", bank.resize((bank.width * 5, bank.height * 5), Image.NEAREST)),
              ("NEW hull_glass_ribbed (annex roof, new block)", ribbed.resize((ribbed.width * 5, ribbed.height * 5), Image.NEAREST))]
    board("03_hull_glass", "HULL GLASS: round ringed porthole (wall) + ribbed panel (annex roof)", [(l, i.convert("RGB")) for l, i in panels],
          ["Reproduced: round porthole with ring and dark gasket in a painted panel (MDRS); translucent glass at 35 % alpha.",
           "Not reproduced: a truly round hole (3D), the recessed square housing around the MDRS porthole, the Ice Home's translucent ribbed dome shell.",
           "Side effect: the roof glass of Hab B would read as porthole plates; hull_glass_ribbed is the proper annex roof (handoff)."])

    # ---- 4 door (Blender: two slabs)
    door = {"textures": {"t": "mars:block/hull_door_top", "b": "mars:block/hull_door_bottom"},
            "elements": [{"from": [0, 16, 13], "to": [16, 32, 16], "faces": {f: {"texture": "#t"} for f in ("north", "south", "east", "west", "up", "down")}},
                         {"from": [0, 0, 13], "to": [16, 16, 16], "faces": {f: {"texture": "#b"} for f in ("north", "south", "east", "west", "up", "down")}}]}
    dr = stack([flat([tex("hull_door_top")], 12), flat([tex("hull_door_bottom")], 12)])  # Blender framing clipped the 2-block model, so a flat stack is used
    board("04_hull_door", "HULL DOOR (hatch): ISS Quest airlock hatch (ref #11, NASA) | our door, flat front | same stack",
          [("REF: Quest hatch, rounded corners, ringed window, indicator arc, crank, placards, seal frame, grilles", crop("quest_airlock_hatch_hurley.jpg", (60, 0, 740, 600))),
           ("NEW door, 1 x 2 blocks, flat front (FLAT COMPOSITE)", dr),
           ("NEW door seen small (2 x scale)", stack([flat([tex("hull_door_top")], 4), flat([tex("hull_door_bottom")], 4)]))],
          ["Reproduced: rounded corners (3 px cut), yellow-green seal frame, raw aluminium slab, ringed window with a green / yellow / red arc, crank, two placards, mesh grille.",
           "Not reproduced: the hinges and latch hardware, hatch size (ours is 1 m wide, real one about 1 m by 1.5 m is an estimate), real text, brushed finish, the blue rail and wall grilles beside it.",
           "Window is 7 px (a third of the width) and the arc is decoration, not a live sealed/unsealed state."])

    # ---- 5 tank
    tank = cube_model({"north": "o2_tank", "south": "o2_tank", "east": "o2_tank", "west": "o2_tank", "up": "o2_tank_top", "down": "o2_tank_top"})
    tr = blender(tank, "iso", "tank", ["--ref"])
    board("05_o2_tank", "OXYGEN TANK: NORS tank on ISS (ref #10, NASA) | our tank (Blender iso, player-size box) | flat side + top",
          [("REF: white-wrapped tank, straps, yellow-black labels, green oxygen tag", crop("nors_o2_tank_rubins.jpg", (500, 120, 830, 540))),
           ("NEW o2_tank block (Blender)", tr),
           ("side + top (flat)", flat([tex("o2_tank"), tex("o2_tank_top")], 10))],
          ["Reproduced: white wrap with cylinder shading, two straps, yellow-black diagonal hazard label (safety orange #E69F00 used for the yellow), green tag with a ring mark, dark base ring.",
           "Not reproduced: a round silhouette (the block must stay a full cube for culling and collision, so the corners are square), cables, the gold-grey valve block, real label text.",
           "The block is one metre tall; a real NORS tank is smaller (size not sourced). The tag carries no text on purpose."])

    # ---- 6 reclaimer
    rec = cube_model({"north": "water_reclaimer", "south": "water_reclaimer", "east": "water_reclaimer", "west": "water_reclaimer", "up": "water_reclaimer_top", "down": "water_reclaimer_top"})
    rr = blender(rec, "iso", "reclaimer", ["--ref"])
    board("06_water_reclaimer", "WATER RECLAIMER: ISS water racks (ref #8 closed, ref #9 open) | our block (Blender iso) | closed + spare open texture (flat)",
          [("REF: closed rack panels, latches, blue rails (ref #8)", crop("iss_wpa_rack_peake.jpg", (0, 380, 760, 666))),
           ("REF: rack swung open, dark panel with silver studs (ref #9)", crop("iss_wrs_rack_open_fincke.jpg", (0, 0, 1000, 700)) if os.path.exists(os.path.join(REF, "iss_wrs_rack_open_fincke.jpg")) else Image.new("RGB", (10, 10))),
           ("NEW reclaimer (Blender)", rr),
           ("closed side | top | open (spare)", flat([tex("water_reclaimer"), tex("water_reclaimer_top"), tex("water_reclaimer_open")], 8))],
          ["Reproduced: off-white bay panels with a seam between two bays, latch pixels, dark cut-out slots, blue handrail along one edge, small orange tag.",
           "Not reproduced: hoses and braided green hose (a cube side cannot carry them legibly at 16 px), the red-cross label and the Condensate bag, the grid of rack bays.",
           "The open (dark, studded) texture exists but no state uses it: the block has no state property (handoff, C2 repair)."])

    # ---- 7 maker
    mk = json.load(open(os.path.join(A, "models", "block", "o2_generator.json")))
    iso = blender(mk, "iso", "maker_iso", ["--ref"]); back = blender(mk, "back", "maker_back")
    board("07_oxygen_maker", "OXYGEN MAKER: MOXIE engineering model (ref PIA24201, NASA/JPL-Caltech) | our maker iso | our maker, other side",
          [("REF: gold box, pocketed panels, X-ribs, white tubes, silver fittings, copper base", crop("PIA24201.jpg", (0, 0, 1000, 932))),
           ("NEW (Blender iso, player-size box)", iso), ("NEW back/east (Blender)", back)],
          ["Reproduced: gold ramp with pocketed panel grid and dark grooves, X-rib end, off-white tubes with silver fittings, copper strip at the base, silver connector and cable dots on the top.",
           "Not reproduced: the large 4 x 4 face grid (3 x 2 here), the glossy reflection, tube bends smoother than 1 px steps, the base plate, the many hex bolts.",
           "Scale: our box is 0.75 m wide, about 3 times MOXIE (24 cm). One block by owner choice (style guide 5.5a)."])

    # ---- 8 elevation of the dome from outside, new vs old
    col = 14
    ele = Image.new("RGBA", (col * 16, 4 * 16), (0, 0, 0, 255))
    rows = ["hull_plate_rim", "hull_plate", "hull_plate", "hull_plate_base"]
    for r, tn in enumerate(rows):
        for c in range(col):
            ele.alpha_composite(tex(tn), (c * 16, r * 16))
    sky = Image.new("RGBA", (16, 16), (210, 174, 134, 255))
    for c in range(3, 8):                       # window bank at y2..y3 (rows 2 and 1 from the top), as the '=' cells
        for r in (1, 2):
            tile = Image.new("RGBA", (16, 16), (60, 66, 72, 255)); tile.alpha_composite(tex("hull_glass"))
            ele.alpha_composite(tile, (c * 16, r * 16))
    for r, tn in ((2, "hull_door_top"), (3, "hull_door_bottom")):  # door at y2 (upper) and y1 (lower), as in the game
        ele.alpha_composite(tex(tn), (11 * 16, r * 16))
    ele = shade(ele, 0.8).resize((col * 16 * 4, 4 * 16 * 4), Image.NEAREST)
    board("08_dome_elevation", "DOME FROM OUTSIDE (flat composite, wall elevation): MDRS Hab | old in-game screenshot | new courses + porthole bank + hatch",
          [("REF: MDRS Hab with dish on the roof (ref #12)", crop("mdrs_hab_greenhab.jpg", (0, 0, 1000, 750))),
           ("OLD in game (life_1)", Image.open(os.path.join(ROOT, "tools/screenshots/out/20261127-223929/life_1_starting_dome_from_outside.png")).convert("RGB").crop((380, 190, 880, 400))),
           ("NEW elevation (rim, plain, plain, base; windows y2..y3)", ele)],
          ["This is a composite of the real textures, NOT an in-game render. It shows what the wall will look like once the rim and base blocks and the window/door textures are in.",
           "Door at y1..y2 and windows at y2..y3, as in DomeTemplate. The door column has no base-course block under it in the game either (a door replaces the plate).",
           "Missing in the composite: the roof dish and vent (models exist, roof_dish.json and roof_vent.json), the entry steps (layout change, not made)."])


def board09():
    """09 hull light: real lamp crops | flat texture | ceiling seen from below (Blender) | lit-room mock-up (Pillow)."""
    els = []
    for gx in range(3):
        for gy in range(3):
            tx = "hull_light" if (gx, gy) == (1, 1) else "hull_plate"
            els.append({"from": [gx * 16 - 16, 0, gy * 16 - 16], "to": [gx * 16, 16, gy * 16],
                        "faces": {"down": {"texture": "#" + tx}, "up": {"texture": "#hull_plate"}}})
    m = {"textures": {"hull_light": "mars:block/hull_light", "hull_plate": "mars:block/hull_plate"}, "elements": els}
    up = blender(m, "bottom", "ceiling", ["--transparent", "--size", "700"])
    flat_t = flat([tex("hull_light"), tex("hull_light_off")], 12)
    row = Image.new("RGBA", (6 * 16, 3 * 16), (0, 0, 0, 255))
    for r in range(3):
        for c in range(6):
            lamp = (c, r) in ((1, 1), (4, 1))
            t = tex("hull_light" if lamp else "hull_plate")
            if not lamp:
                t = shade(t, 0.7).convert("RGBA")  # plates in room light; lamps stay at full brightness
            row.alpha_composite(t, (c * 16, r * 16))
    row = row.resize((6 * 16 * 5, 3 * 16 * 5), Image.NEAREST)
    board("09_hull_light", "HULL LIGHT: ISS lab light box (ref #8/#9 upper right) | CHAPEA lamps (ref chapea_sandbox_360) | our ceiling panel",
          [("REF: ISS rack-open photo, lit light box (blurred, top right)", crop("iss_wrs_rack_open_fincke.jpg", (660, 140, 900, 280))),
           ("REF: CHAPEA dome: even white shell, small wall lamps", crop("chapea_sandbox_360.jpg", (0, 120, 240, 260))),
           ("NEW lit | off (spare), flat 12x", flat_t),
           ("NEW ceiling from below (Blender): 1 light in 3x3 plates", up.convert("RGB")),
           ("NEW two lights in a plate ceiling (flat, 70 % shade)", row.convert("RGB"))],
          ["Reproduced: a flat cream-white diffuser in an aluminium bezel with a dark seam and corner screws, same seam and palette as hull_plate; reads as a lit rectangle in the ceiling.",
           "Not reproduced: the references show NO flat ceiling panel: ISS light is a blurred lit box edge, CHAPEA lights are small round spot lamps on an evenly white shell. The panel is a stylised stand-in.",
           "Not reproduced: real emission (the texture is only bright; the block light level does the work), LED strips, wiring, the lamp body depth (full cube, flush ceiling). Blender uses flat shading, not Minecraft lighting."])


if __name__ == "__main__":
    main()
    board09()
