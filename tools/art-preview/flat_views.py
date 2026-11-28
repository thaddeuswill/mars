#!/usr/bin/env python3
"""Flat orthographic views (front = north face, side = east face, top) of block models, textures sampled 1:1.
Not a 3D render: no lighting, no perspective. Usage: python3 -I flat_views.py out.png model [model ...]"""
import json, os, sys
from PIL import Image
ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "..", "pack", "assets", "mars")

def tex(ref, t):
    ref = t[ref[1:]] if ref.startswith("#") else ref
    p = os.path.join(ROOT, "textures", ref.split(":")[1] + ".png")
    return Image.open(p).convert("RGBA")

def view(m, face, scale=8):
    W = H = 32
    im = Image.new("RGBA", (W, H), (60, 80, 100, 255))
    els = m["elements"]
    # depth key: north view -> far (high z) first; east view -> far = low x first... paint far to near
    key = {"north": lambda e: -e["from"][2], "east": lambda e: e["to"][0], "up": lambda e: e["to"][1]}[face]
    for e in sorted(els, key=lambda e: key(e)):
        f = e["faces"].get(face)
        if not f:
            continue
        t = tex(f["texture"], m["textures"])
        if t.height > 16:
            t = t.crop((0, 0, 16, 16))
        u = f.get("uv", [0, 0, 16, 16])
        fr, to = e["from"], e["to"]
        if face == "north":
            xs, ys, w, h = fr[0], fr[1], to[0] - fr[0], to[1] - fr[1]
        elif face == "east":
            xs, ys, w, h = 16 - to[2], fr[1], to[2] - fr[2], to[1] - fr[1]
        else:
            xs, ys, w, h = fr[0], fr[2], to[0] - fr[0], to[2] - fr[2]
        for i in range(w):
            for j in range(h):
                a = t.getpixel((min(15, int(u[0] + (u[2] - u[0]) * (i + .5) / w)), min(15, int(u[1] + (u[3] - u[1]) * ((h - 1 - j if face != 'up' else j) + .5) / h))))
                if a[3] == 0:
                    continue
                X = xs + i
                Y = (H - 1 - ys - j) if face != "up" else (ys + j)
                if 0 <= X < W and 0 <= Y < H:
                    im.putpixel((X, Y), a)
    return im.resize((W * scale, H * scale), Image.NEAREST)

out, names = sys.argv[1], sys.argv[2:]
rows = []
for n in names:
    m = json.load(open(os.path.join(ROOT, "models", "block", n + ".json")))
    rows.append([view(m, f) for f in ("north", "east", "up")])
sheet = Image.new("RGBA", (3 * 264, len(rows) * 264), (30, 30, 30, 255))
for r, row in enumerate(rows):
    for c, v in enumerate(row):
        sheet.paste(v, (c * 264 + 4, r * 264 + 4))
sheet.save(out)
