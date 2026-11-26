"""Render a Minecraft block-model JSON with Blender (headless). Preview tool, NOT Minecraft.

Usage:
  blender -b -P render_model.py -- <model.json> <texture_dir> <out.png> [view] [options]

  view     iso (default) | front | top | side | back
  options  --shading mc|lit   mc (default): flat, unlit faces with Minecraft's fixed per-face
                              brightness (up 1.0, north/south 0.8, east/west 0.6, down 0.5);
                              lit: Blender sun light (soft shadows, not like the game)
           --ground <tex>     ground plane (3x3 blocks) using <texture_dir>/<tex>.png, or a full .png path
           --sky <hex>        background color (default #D2AE86, the 0.6 kPa horizon, keyframes §3.1)
           --ref              add a 0.6 x 1.8 block grey box (player size) for scale
           --transparent      transparent background, no ground (for silhouette checks)
           --size <px>        output size (default 640)

Supports: cuboid elements; per-face textures (#name) with "uv" (explicit or Minecraft's default);
face "rotation" (0/90/180/270); element "rotation" (origin, axis, angle); parent models are NOT
resolved (put elements and textures in the model you render). Textures are 16x16 px, nearest-neighbour.
Front = the model's north face (Minecraft convention for facing=north)."""
import bpy, sys, json, os, math, mathutils

argv = sys.argv[sys.argv.index("--") + 1:]
model_path, texdir, out = argv[0], argv[1], argv[2]
rest = argv[3:]
view = "iso"
if rest and not rest[0].startswith("--"):
    view = rest.pop(0)
opts = {"shading": "mc", "ground": None, "sky": "#D2AE86", "ref": False, "transparent": False, "size": 640}
i = 0
while i < len(rest):
    k = rest[i].lstrip("-")
    if k in ("ref", "transparent"):
        opts[k] = True; i += 1
    else:
        opts[k] = rest[i + 1]; i += 2
model = json.load(open(model_path))

def srgb_to_lin(c):
    return c / 12.92 if c <= 0.04045 else ((c + 0.055) / 1.055) ** 2.4
def hexlin(h):
    h = h.lstrip("#"); return tuple(srgb_to_lin(int(h[j:j + 2], 16) / 255) for j in (0, 2, 4)) + (1.0,)

bpy.ops.wm.read_factory_settings(use_empty=True)
sc = bpy.context.scene
SHADE = {"up": 1.0, "down": 0.5, "north": 0.8, "south": 0.8, "east": 0.6, "west": 0.6}
mats = {}

def material(texname, face):
    key = (texname, face if opts["shading"] == "mc" else "")
    if key in mats: return mats[key]
    m = bpy.data.materials.new(f"{texname}_{face}"); m.use_nodes = True
    nt = m.node_tree; nt.nodes.clear()
    outn = nt.nodes.new("ShaderNodeOutputMaterial")
    t = nt.nodes.new("ShaderNodeTexImage")
    path = texname if texname.endswith(".png") else os.path.join(texdir, texname + ".png")
    t.image = bpy.data.images.load(path, check_existing=True); t.interpolation = "Closest"
    if opts["shading"] == "mc":
        em = nt.nodes.new("ShaderNodeEmission"); em.inputs["Strength"].default_value = SHADE[face]
        nt.links.new(t.outputs["Color"], em.inputs["Color"]); shader = em
    else:
        b = nt.nodes.new("ShaderNodeBsdfPrincipled"); b.inputs["Roughness"].default_value = 1.0
        nt.links.new(t.outputs["Color"], b.inputs["Base Color"]); shader = b
    # alpha: mix with transparent so cut-out pixels work
    tr = nt.nodes.new("ShaderNodeBsdfTransparent"); mix = nt.nodes.new("ShaderNodeMixShader")
    nt.links.new(t.outputs["Alpha"], mix.inputs[0]); nt.links.new(tr.outputs[0], mix.inputs[1])
    nt.links.new(shader.outputs[0], mix.inputs[2]); nt.links.new(mix.outputs[0], outn.inputs["Surface"])
    mats[key] = m; return m

# Corner order per face: bottom-left, bottom-right, top-right, top-left as seen from outside.
# For "up", the texture's top edge is north (-z); for "down", the texture's top edge is south (+z).
FACES = {
    "north": lambda a, b: [(b[0], a[1], a[2]), (a[0], a[1], a[2]), (a[0], b[1], a[2]), (b[0], b[1], a[2])],
    "south": lambda a, b: [(a[0], a[1], b[2]), (b[0], a[1], b[2]), (b[0], b[1], b[2]), (a[0], b[1], b[2])],
    "east":  lambda a, b: [(b[0], a[1], b[2]), (b[0], a[1], a[2]), (b[0], b[1], a[2]), (b[0], b[1], b[2])],
    "west":  lambda a, b: [(a[0], a[1], a[2]), (a[0], a[1], b[2]), (a[0], b[1], b[2]), (a[0], b[1], a[2])],
    "up":    lambda a, b: [(a[0], b[1], b[2]), (b[0], b[1], b[2]), (b[0], b[1], a[2]), (a[0], b[1], a[2])],
    "down":  lambda a, b: [(a[0], a[1], a[2]), (b[0], a[1], a[2]), (b[0], a[1], b[2]), (a[0], a[1], b[2])],
}
def default_uv(face, a, b):  # Minecraft's automatic UVs
    return {"north": [16 - b[0], 16 - b[1], 16 - a[0], 16 - a[1]], "south": [a[0], 16 - b[1], b[0], 16 - a[1]],
            "east": [16 - b[2], 16 - b[1], 16 - a[2], 16 - a[1]], "west": [a[2], 16 - b[1], b[2], 16 - a[1]],
            "up": [a[0], a[2], b[0], b[2]], "down": [a[0], 16 - b[2], b[0], 16 - a[2]]}[face]

def rotate(p, rot):
    if not rot: return p
    o = mathutils.Vector(rot["origin"]); v = mathutils.Vector(p) - o
    axis = {"x": (1, 0, 0), "y": (0, 1, 0), "z": (0, 0, 1)}[rot["axis"]]
    v.rotate(mathutils.Matrix.Rotation(math.radians(rot["angle"]), 3, axis))
    return tuple(v + o)

S = 1 / 16
def to_bl(p):  # Minecraft (x east, y up, z south) -> Blender (x, -z, y), block centred on origin
    return ((p[0] - 8) * S, -(p[2] - 8) * S, p[1] * S)

def add_quad(name, pts, uvs, mat):
    mesh = bpy.data.meshes.new(name)
    mesh.from_pydata([to_bl(p) for p in pts], [], [(0, 1, 2, 3)]); mesh.update()
    uvl = mesh.uv_layers.new()
    for loop, uv in zip(mesh.loops, uvs): uvl.data[loop.index].uv = uv
    ob = bpy.data.objects.new(name, mesh); sc.collection.objects.link(ob); ob.data.materials.append(mat)

for i, el in enumerate(model["elements"]):
    a, b = el["from"], el["to"]
    for fname, fd in el["faces"].items():
        u1, v1, u2, v2 = fd.get("uv", default_uv(fname, a, b))
        uvs = [(u1, v2), (u2, v2), (u2, v1), (u1, v1)]
        r = (fd.get("rotation", 0) // 90) % 4
        uvs = uvs[r:] + uvs[:r]
        uvs = [(u / 16, 1 - v / 16) for u, v in uvs]
        pts = [rotate(p, el.get("rotation")) for p in FACES[fname](a, b)]
        tex = model["textures"][fd["texture"].lstrip("#")]
        tex = tex.split("/")[-1]
        add_quad(f"e{i}{fname}", pts, uvs, material(tex, fname))

if opts["ground"] and not opts["transparent"]:
    for gx in range(-1, 2):
        for gz in range(-1, 2):
            a = (gx * 16, -0.01, gz * 16); b = (gx * 16 + 16, -0.01, gz * 16 + 16)
            add_quad(f"g{gx}{gz}", FACES["up"](a, b), [(0, 0), (1, 0), (1, 1), (0, 1)], material(opts["ground"], "up"))
if opts["ref"]:
    a, b = (19.2, 0, 4.8), (28.8, 28.8, 11.2)  # 0.6 x 1.8 x 0.4 blocks, one block east
    m = bpy.data.materials.new("ref"); m.use_nodes = True; nt = m.node_tree; nt.nodes.clear()
    o = nt.nodes.new("ShaderNodeOutputMaterial"); e = nt.nodes.new("ShaderNodeEmission")
    e.inputs["Color"].default_value = hexlin("#A9B0B8"); nt.links.new(e.outputs[0], o.inputs["Surface"])
    for f in FACES:
        add_quad("ref" + f, FACES[f](a, b), [(0, 0), (1, 0), (1, 1), (0, 1)], m)

w = bpy.data.worlds.new("w"); sc.world = w; w.use_nodes = True
bg = w.node_tree.nodes["Background"]; bg.inputs[0].default_value = hexlin(opts["sky"]); bg.inputs[1].default_value = 1.0
sc.render.film_transparent = opts["transparent"]
if opts["shading"] == "lit":
    sun = bpy.data.objects.new("sun", bpy.data.lights.new("sun", "SUN")); sun.data.energy = 4
    sun.rotation_euler = (math.radians(50), 0, math.radians(-35)); sc.collection.objects.link(sun)

cam = bpy.data.objects.new("cam", bpy.data.cameras.new("cam")); cam.data.type = "ORTHO"
wide = opts["ref"] or opts["ground"]
cam.data.ortho_scale = 2.6 if opts["ref"] else (2.0 if wide else 1.7)
sc.collection.objects.link(cam); sc.camera = cam
d = 4
# Blender +y = Minecraft north. iso looks at the north (front) and east faces from above.
pos = {"iso": (d * 0.8, d, d * 0.8), "front": (0, d, 0.5), "back": (0, -d, 0.5),
       "side": (d, 0, 0.5), "top": (0, 0.001, d)}[view]
target = mathutils.Vector((0.25 if opts["ref"] else 0, 0, 0.5 if view != "top" else 0))
cam.location = mathutils.Vector(pos) + mathutils.Vector((target.x, 0, 0))
cam.rotation_euler = (target - cam.location).to_track_quat("-Z", "Y").to_euler()
if view == "top":
    cam.location = (target.x, 0, d); cam.rotation_euler = (0, 0, 0)  # north at the top of the image
sc.render.engine = "CYCLES"; sc.cycles.samples = 16 if opts["shading"] == "mc" else 32
sc.cycles.device = "CPU"; sc.cycles.use_denoising = False
sc.render.resolution_x = sc.render.resolution_y = int(opts["size"])
sc.view_settings.view_transform = "Standard"; sc.view_settings.look = "None"
sc.render.filepath = out; bpy.ops.render.render(write_still=True)
print("RENDERED", out)
