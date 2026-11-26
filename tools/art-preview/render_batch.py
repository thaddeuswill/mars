"""Render a batch of block models in several views and compose a labeled contact sheet.
Usage: python3 -I render_batch.py <out_dir> <model_id> [<model_id> ...]
Each model is rendered iso (with ground and a player-size reference box), front, top, and as a
32x32 black silhouette (style guide §3.2). Needs Blender (see README) and Pillow."""
import os, sys, subprocess
from PIL import Image, ImageDraw, ImageFont
HERE = os.path.dirname(os.path.abspath(__file__)); ROOT = os.path.abspath(os.path.join(HERE, "..", ".."))
A = os.path.join(ROOT, "pack", "assets", "mars"); TEX = os.path.join(A, "textures", "block")
out, ids = sys.argv[1], sys.argv[2:]; os.makedirs(out, exist_ok=True)
# render-only ground: the regolith straw-man with its placeholder marker painted out (not shipped)
g = Image.open(os.path.join(TEX, "regolith.png")).convert("RGB")
g = Image.eval(g, lambda v: v)
px = g.load()
for yy in range(16):
    for xx in range(16):
        if px[xx, yy] == (255, 0, 255): px[xx, yy] = (154, 91, 60)
GROUND = os.path.join(out, "_ground_preview.png"); g.save(GROUND)
VIEWS = [("iso", ["--ground", GROUND, "--ref"]), ("front", []), ("top", [])]
def render(mid, view, extra, path):
    cmd = ["blender", "-b", "-P", os.path.join(HERE, "render_model.py"), "--",
           os.path.join(A, "models", "block", mid + ".json"), TEX, path, view, "--size", "480"] + extra
    r = subprocess.run(cmd, capture_output=True, text=True)
    if "RENDERED" not in r.stdout: sys.exit(f"render failed: {mid} {view}\n{r.stdout[-2000:]}\n{r.stderr[-2000:]}")
F = ImageFont.load_default()
rows = []
for mid in ids:
    tiles = []
    for view, extra in VIEWS:
        p = os.path.join(out, f"{mid}_{view}.png"); render(mid, view, extra, p); tiles.append(Image.open(p).convert("RGB"))
    # silhouette: FRONT view (the profile; iso views of boxes all look like hexagons), fixed scale:
    # the 1 x 1 block square (480 / 1.7 px at ortho scale 1.7) becomes 32 x 32 px (style guide §3.2)
    sp = os.path.join(out, f"{mid}_silhouette_src.png"); render(mid, "front", ["--transparent"], sp)
    a = Image.open(sp).getchannel("A").point(lambda v: 255 if v > 128 else 0)
    blk = 480 / 1.7; c = 240
    sq = a.crop((int(c - blk / 2), int(c - blk / 2), int(c + blk / 2), int(c + blk / 2)))
    small = sq.resize((32, 32), Image.BOX).point(lambda v: 255 if v > 127 else 0)
    sil = Image.new("RGB", (32, 32), (242, 242, 242)); sil.paste((20, 20, 20), mask=small)
    sil.save(os.path.join(out, f"{mid}_silhouette32.png")); os.remove(sp)
    tiles.append(sil.resize((480, 480), Image.NEAREST))
    rows.append((mid, tiles))
T = 240; lab = 26
sheet = Image.new("RGB", (4 * T, len(rows) * (T + lab) + 40), (16, 19, 22)); d = ImageDraw.Draw(sheet)
for r, (mid, tiles) in enumerate(rows):
    y = r * (T + lab)
    d.text((6, y + 7), f"{mid}   (iso with player-size box | front | top | front silhouette at 32x32 px)", fill=(232, 227, 216), font=F)
    for c, im in enumerate(tiles): sheet.paste(im.resize((T, T), Image.LANCZOS if c < 3 else Image.NEAREST), (c * T, y + lab))
d.text((6, len(rows) * (T + lab) + 12), "STRAW-MAN (SM) placeholders, magenta corner = placeholder. Blender preview, NOT Minecraft: flat per-face shading like the game, but no world light, AO, or GUI.", fill=(232, 227, 216), font=F)
sheet.save(os.path.join(out, "contact_sheet.png")); print("SHEET", os.path.join(out, "contact_sheet.png"))
