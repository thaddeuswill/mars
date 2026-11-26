"""Build the single-file Mars Asset Viewer (docs/art/viewer/index.html).

Embeds the block models and textures from pack/assets/mars/ as JSON and data URIs, so the page needs only
the three.js script. Usage:
  python3 tools/art-preview/build_viewer.py [--three-src URL] [--out PATH] [--fragment]
--fragment writes just the page content (no <html>/<body> wrapper), which is the form the Artifact tool wants.
"""
import base64, glob, json, os, sys

root = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
assets = os.path.join(root, "pack", "assets", "mars")
args = sys.argv[1:]
def opt(name, default):
    return args[args.index(name) + 1] if name in args else default
three = opt("--three-src", "https://cdn.jsdelivr.net/npm/three@0.158.0/build/three.min.js")
out = opt("--out", os.path.join(root, "docs", "art", "viewer", "index.html"))
fragment = "--fragment" in args

models = {}
for f in sorted(glob.glob(os.path.join(assets, "models", "block", "*.json"))):
    models[os.path.splitext(os.path.basename(f))[0]] = json.load(open(f))
textures = {}
for f in sorted(glob.glob(os.path.join(assets, "textures", "block", "*.png"))):
    textures[os.path.splitext(os.path.basename(f))[0]] = "data:image/png;base64," + base64.b64encode(open(f, "rb").read()).decode()
# the shipped regolith texture carries a magenta placeholder corner; the ground uses the corner-free preview copy
ground = os.path.join(root, "docs", "art", "renders", "phase1-batch1", "_ground_preview.png")
if os.path.exists(ground):
    textures["ground_regolith"] = "data:image/png;base64," + base64.b64encode(open(ground, "rb").read()).decode()
data = json.dumps({"models": models, "textures": textures}, separators=(",", ":"))

tpl = open(os.path.join(os.path.dirname(os.path.abspath(__file__)), "viewer_template.html")).read()
assert tpl.count("__THREE_SRC__") == 1 and tpl.count("/*__DATA__*/") == 1
page = tpl.replace("__THREE_SRC__", three).replace("/*__DATA__*/", data)
if not fragment:
    # title and meta first, then the rest, wrapped in a minimal document
    page = "<!doctype html>\n<html lang=\"en\"><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head><body>\n" + page + "\n</body></html>\n"
os.makedirs(os.path.dirname(out), exist_ok=True)
open(out, "w").write(page)
print(f"wrote {out}: {len(models)} models, {len(textures)} textures, {len(page)//1024} KB")
