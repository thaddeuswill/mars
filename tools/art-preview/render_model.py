"""Render a Minecraft block-model JSON with Blender (headless). Test tool.
Usage: blender -b -P render_model.py -- model.json texdir out.png [view]
Supports cuboid 'elements' with per-face textures (#name), nearest-neighbour pixels."""
import bpy, sys, json, os, math, mathutils
argv=sys.argv[sys.argv.index("--")+1:]
model_path,texdir,out=argv[0],argv[1],argv[2]
view=argv[3] if len(argv)>3 else "iso"
model=json.load(open(model_path))
bpy.ops.wm.read_factory_settings(use_empty=True)
sc=bpy.context.scene
imgs={}
def mat_for(name):
    if name in imgs: return imgs[name]
    m=bpy.data.materials.new(name); m.use_nodes=True
    nt=m.node_tree; bsdf=nt.nodes["Principled BSDF"]
    t=nt.nodes.new("ShaderNodeTexImage")
    t.image=bpy.data.images.load(os.path.join(texdir,model["textures"][name]+".png")); t.interpolation="Closest"
    nt.links.new(t.outputs["Color"],bsdf.inputs["Base Color"])
    bsdf.inputs["Roughness"].default_value=1.0
    imgs[name]=m; return m
S=1/16
faces_def={ # face -> corner indices on cuboid, order for uv
 "north":lambda a,b:[(b[0],a[1],a[2]),(a[0],a[1],a[2]),(a[0],b[1],a[2]),(b[0],b[1],a[2])],
 "south":lambda a,b:[(a[0],b[1],b[2]),(b[0],b[1],b[2]),(b[0],a[1],b[2]),(a[0],a[1],b[2])][::-1][::-1],
 "east": lambda a,b:[(b[0],a[1],b[2]),(b[0],a[1],a[2]),(b[0],b[1],a[2]),(b[0],b[1],b[2])],
 "west": lambda a,b:[(a[0],a[1],a[2]),(a[0],a[1],b[2]),(a[0],b[1],b[2]),(a[0],b[1],a[2])],
 "up":   lambda a,b:[(a[0],b[1],a[2]),(b[0],b[1],a[2]),(b[0],b[1],b[2]),(a[0],b[1],b[2])],
 "down": lambda a,b:[(a[0],a[1],b[2]),(b[0],a[1],b[2]),(b[0],a[1],a[2]),(a[0],a[1],a[2])],
}
# fix south ordering: corners seen from +z
faces_def["south"]=lambda a,b:[(a[0],a[1],b[2]),(b[0],a[1],b[2]),(b[0],b[1],b[2]),(a[0],b[1],b[2])]
def to_bl(p): # minecraft (x,y,z up=y) -> blender (x,-z,y), centred on block
    return ((p[0]-8)*S,-(p[2]-8)*S,p[1]*S)
for i,el in enumerate(model["elements"]):
    a,b=el["from"],el["to"]
    for fname,fd in el["faces"].items():
        mesh=bpy.data.meshes.new(f"e{i}{fname}")
        pts=[to_bl(p) for p in faces_def[fname](a,b)]
        mesh.from_pydata(pts,[],[(0,1,2,3)]); mesh.update()
        uv=mesh.uv_layers.new(); 
        for l,(u,v) in zip(mesh.loops,[(0,0),(1,0),(1,1),(0,1)]): uv.data[l.index].uv=(u,v)
        ob=bpy.data.objects.new(f"e{i}{fname}",mesh); sc.collection.objects.link(ob)
        ob.data.materials.append(mat_for(fd["texture"].lstrip("#")))
# ground (Mars regolith plane) and sky
w=bpy.data.worlds.new("w"); sc.world=w; w.use_nodes=True
bg=w.node_tree.nodes["Background"]; bg.inputs[0].default_value=(0.78,0.58,0.42,1); bg.inputs[1].default_value=0.9
sun=bpy.data.objects.new("sun",bpy.data.lights.new("sun","SUN")); sun.data.energy=4; sun.rotation_euler=(math.radians(50),0,math.radians(35)); sc.collection.objects.link(sun)
cam=bpy.data.objects.new("cam",bpy.data.cameras.new("cam")); cam.data.type="ORTHO"; cam.data.ortho_scale=1.7
sc.collection.objects.link(cam); sc.camera=cam
d=3
pos={"iso":(d,-d,d*0.9),"front":(0,-d*1.5,0.5),"top":(0,0,d*1.5)}[view]
cam.location=pos
cam.rotation_euler=(mathutils.Vector((0,0,0.45))-mathutils.Vector(pos)).to_track_quat("-Z","Y").to_euler()
sc.render.engine="CYCLES"; sc.cycles.samples=24; sc.cycles.device="CPU"; sc.cycles.use_denoising=False
sc.render.resolution_x=640; sc.render.resolution_y=640
sc.view_settings.view_transform="Standard"
sc.render.filepath=out; bpy.ops.render.render(write_still=True)
print("RENDERED",out)
