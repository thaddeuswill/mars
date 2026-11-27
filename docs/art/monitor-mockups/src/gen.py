import json, sys
# ---- contrast ----
def lin(c):
    c/=255; return c/12.92 if c<=0.03928 else ((c+0.055)/1.055)**2.4
def L(h):
    h=h.lstrip('#'); r,g,b=[int(h[i:i+2],16) for i in (0,2,4)]
    return 0.2126*lin(r)+0.7152*lin(g)+0.0722*lin(b)
def cr(a,b):
    la,lb=L(a),L(b); 
    if la<lb: la,lb=lb,la
    return (la+0.05)/(lb+0.05)
def mix(fg,bg,a):
    f=fg.lstrip('#');b=bg.lstrip('#')
    return '#'+''.join('%02X'%round(int(f[i:i+2],16)*a+int(b[i:i+2],16)*(1-a)) for i in (0,2,4))
SKY='#B98A66'; WHITE='#FFFFFF'
DARK='#181210'; ALPHA=0.97
dark_over_sky=mix(DARK,SKY,ALPHA); dark_over_white=mix(DARK,WHITE,ALPHA)
D=dict(label='#9AA5B1',value='#FFFFFF',up='#7CD37C',down='#FF8A80',steady='#C8D0D8',cause='#E6D7A3',series='#8AD0E8',hint='#9AA5B1')
P=dict(ink='#1A1A1A',grey='#4A4A4A',blue='#1F5FA8',orange='#A84300',head='#FFFFFF')
PAPER='#F3EFE6'; HEADB='#2A1F1A'
pairs=[]
for k,v in D.items():
    pairs.append(('A/C dark',k,v,'over sky',cr(v,dark_over_sky)));pairs.append(('A/C dark',k,v,'over white',cr(v,dark_over_white)))
for k in ('ink','grey','blue','orange'):
    pairs.append(('B paper',k,P[k],'on paper',cr(P[k],PAPER)))
pairs.append(('B header','white',P['head'],'on header bar',cr(P['head'],HEADB)))
# non-text parts, 3:1 target
parts=[('B grid on paper','#8C8574',cr('#8C8574',PAPER)),('B blue series on paper',P['blue'],cr(P['blue'],PAPER)),
 ('A bar fill vs empty','#7CD37C vs #3A302A',cr('#7CD37C','#3A302A')),('A/C edge #9A6A4C vs backing',DARK,cr('#9A6A4C',dark_over_sky))]
json.dump(dict(pairs=pairs,parts=parts),open('contrast.json','w'))
for p in pairs: print('%-9s %-7s %s %-12s %.2f %s'%(p[0],p[1],p[2],p[3],p[4],'OK' if p[4]>=4.5 else 'FAIL'))
for p in parts: print(p[0],p[1],'%.2f'%p[2])

CSS=f"""
@font-face{{font-family:PX;src:url(PixelifySans-Medium.ttf)}}
@font-face{{font-family:PX;src:url(JBM-Bold.ttf);unicode-range:U+0030-0039;size-adjust:92%}}
*{{box-sizing:border-box;margin:0;padding:0}}
html,body{{width:1280px;height:720px;overflow:hidden;background:#000}}
#gui{{position:absolute;left:0;top:0;width:426.667px;height:240px;transform:scale(3);transform-origin:0 0;
 background:url(bg.png) 0 0/426.667px 240px;image-rendering:pixelated;font-family:PX,monospace;font-size:8px;line-height:10px;-webkit-font-smoothing:none}}
.abs{{position:absolute}}
.dark{{background:rgba(24,18,16,{ALPHA});border:1px solid #9A6A4C;color:{D['label']}}}
.lab{{color:{D['label']}}} .val{{color:#fff}} .up{{color:{D['up']}}} .dn{{color:{D['down']}}} .st{{color:{D['steady']}}} .ca{{color:{D['cause']}}}
.big{{font-size:12px;line-height:12px}} .r{{text-align:right}} .nw{{white-space:nowrap}}
.tri{{display:inline-block;width:0;height:0;border-left:3px solid transparent;border-right:3px solid transparent;border-bottom:5px solid currentColor;margin-right:2px;vertical-align:0}}
.flat{{display:inline-block;width:6px;height:2px;background:currentColor;margin-right:2px;vertical-align:2px}}
.bar{{position:absolute;background:#3A302A;border:1px solid #6B4A36}}
.bar i{{position:absolute;left:0;top:0;bottom:0;background:{D['up']}}}
.note{{position:absolute;left:0;bottom:0;font-size:6px;color:#fff;background:#000;padding:0 2px;line-height:8px}}
.light{{display:inline-block;width:6px;height:6px;vertical-align:-1px;margin-right:2px}}
.lg{{background:{D['up']};border-radius:3px}}
.paper{{background:{PAPER};border:1px solid #1A1A1A;color:{P['ink']}}}
.hdrB{{background:{HEADB};color:#fff}}
"""
def page(body): return f"<!doctype html><meta charset=utf-8><style>{CSS}</style><div id=gui>{body}</div>"

crosshair_ok = ""  # real crosshair is in the backdrop shot

# ---------- A ----------
sols=list(range(6,17)); pv=[0.6+0.1*(s-6) for s in sols]
def spark(x,y,w,h,ymin,ymax,color,grid='#4A3C34'):
    pts=[(x+ (s-6)/10*(w), y+h-(p-ymin)/(ymax-ymin)*h) for s,p in zip(sols,pv)]
    d=' '.join(f"{a:.1f},{b:.1f}" for a,b in pts)
    dots=''.join(f'<rect x="{a-1:.1f}" y="{b-1:.1f}" width="2" height="2" fill="{color}"/>' for a,b in pts)
    return d,dots
d,dots=spark(24,2,150,26,0.5,1.7,D['series'])
A=f"""
<div class="abs dark" style="left:6px;top:6px;width:196px;height:128px">
 <div class="abs val nw" style="left:5px;top:3px">Planetary Monitor</div>
 <div class="abs lab nw r" style="right:5px;top:3px">Sol 16.0</div>
 <div class="abs" style="left:5px;right:5px;top:14px;height:1px;background:#6B4A36"></div>
 <div class="abs lab nw" style="left:5px;top:18px">Air pressure</div>
 <div class="abs val big nw" style="left:5px;top:28px">1.60 kPa</div>
 <div class="abs up nw r" style="right:5px;top:20px"><span class="tri"></span>Rising</div>
 <div class="abs up nw r" style="right:5px;top:30px">+0.10 kPa/sol</div>
 <!-- sparkline -->
 <div class="abs lab nw" style="left:5px;top:44px;font-size:6px;line-height:7px">kPa</div>
 <div class="abs lab nw" style="left:5px;top:52px;font-size:6px;line-height:7px">1.7</div>
 <div class="abs lab nw" style="left:5px;top:68px;font-size:6px;line-height:7px">0.5</div>
 <svg class="abs" style="left:5px;top:46px" width="186" height="30" shape-rendering="crispEdges">
  <g transform="translate(5,0)"><line x1="19" y1="2" x2="19" y2="28" stroke="{D['cause']}" stroke-dasharray="2 2"/>
  <line x1="19" y1="28" x2="169" y2="28" stroke="#6B4A36"/>
  <polyline points="{d}" fill="none" stroke="{D['series']}" stroke-width="1"/>{dots}</g>
 </svg>
 <div class="abs ca nw" style="left:30px;top:49px;font-size:6px;line-height:7px">Outgasser placed</div>
 <div class="abs lab nw" style="left:28px;top:76px;font-size:6px;line-height:7px">Sol 6</div>
 <div class="abs lab nw" style="left:92px;top:76px;font-size:6px;line-height:7px">Sol 11</div>
 <div class="abs lab nw r" style="right:6px;top:76px;font-size:6px;line-height:7px">Sol 16</div>
 <div class="abs lab nw" style="left:5px;top:84px">Temperature</div>
 <div class="abs val nw" style="left:78px;top:84px">-60 °C</div>
 <div class="abs st nw r" style="right:5px;top:84px"><span class="flat"></span>Steady</div>
 <div class="abs lab nw" style="left:5px;top:94px">Water boils at</div>
 <div class="abs val nw" style="left:78px;top:94px">14 °C</div>
 <div class="abs lab nw" style="left:5px;top:104px">Power</div>
 <div class="abs up nw" style="left:78px;top:104px"><span class="light lg"></span>Enough power</div>
 <div class="bar" style="right:5px;top:106px;width:44px;height:6px"><i style="width:50%"></i></div>
 <div class="abs lab nw r" style="right:5px;top:112px;font-size:6px;line-height:7px">5.0 / 10.0 stored</div>
 <div class="abs lab nw" style="left:5px;top:116px;font-size:6px;line-height:7px">Next goal</div>
 <div class="abs val nw" style="left:5px;top:116px;margin-left:34px;font-size:6px;line-height:7px">Phase 2 begins  1.60 / 5 kPa</div>
</div>
<div class="note">MOCKUP A: dark console + sparkline. Series is example data (invented history), Sol 6 to 16. Backdrop: real game shot.</div>
"""
# ---------- B ----------
def px(s): return 26+(s-6)/10*158
def py(pa): return 8+(1800-pa)/(1800-600)*34
ptsB=' '.join(f"{px(s):.1f},{py(p*1000):.1f}" for s,p in zip(sols,pv))
dotsB=''.join(f'<rect x="{px(s)-1.5:.1f}" y="{py(p*1000)-1.5:.1f}" width="3" height="3" fill="{P["blue"]}"/>' for s,p in zip(sols,pv))
gridB=''.join(f'<line x1="26" y1="{py(v):.1f}" x2="184" y2="{py(v):.1f}" stroke="#8C8574" stroke-dasharray="1 2"/>' for v in (600,1000,1400,1800))
ylab=''.join(f'<text x="23" y="{py(v)+2:.1f}" text-anchor="end" font-size="6" fill="{P["grey"]}" font-family="PX">{v}</text>' for v in (600,1000,1400,1800))
xlab=''.join(f'<text x="{px(s):.1f}" y="51" text-anchor="middle" font-size="6" fill="{P["grey"]}" font-family="PX">{s}</text>' for s in (6,8,10,12,14,16))
B=f"""
<div class="abs paper" style="left:6px;top:6px;width:194px;height:124px">
 <div class="abs hdrB nw" style="left:0;top:0;right:0;height:12px;padding:1px 5px">Planetary Monitor<span class="abs r" style="right:5px;top:1px">Sol 16.0</span></div>
 <div class="abs nw" style="left:5px;top:16px;color:{P['grey']}">Air pressure</div>
 <div class="abs nw big r" style="right:5px;top:14px;color:{P['blue']}">1600 Pa <span style="font-size:8px;color:{P['grey']}">(1.60 kPa)</span></div>
 <div class="abs nw r" style="right:5px;top:27px;color:{P['ink']}"><span class="tri" style="color:{P['blue']}"></span>Rising +100 Pa/sol</div>
 <svg class="abs" style="left:3px;top:36px" width="188" height="60" shape-rendering="crispEdges">
  {gridB}
  <line x1="26" y1="8" x2="26" y2="42" stroke="{P['ink']}"/><line x1="26" y1="42" x2="184" y2="42" stroke="{P['ink']}"/>
  <line x1="{px(6)+0.5:.1f}" y1="8" x2="{px(6)+0.5:.1f}" y2="42" stroke="{P['orange']}" stroke-dasharray="3 1"/>
  <polyline points="{ptsB}" fill="none" stroke="{P['blue']}" stroke-width="1"/>{dotsB}
  {ylab}{xlab}
  <text x="188" y="57" text-anchor="end" font-size="6" fill="{P['grey']}" font-family="PX">Sol (example history)</text>
  <text x="29" y="14" font-size="6" fill="{P['orange']}" font-family="PX">Outgasser placed</text>
  <text x="2" y="5" font-size="6" fill="{P['grey']}" font-family="PX">Pa</text>
 </svg>
 <div class="abs nw" style="left:5px;top:94px;color:{P['grey']}">Temperature</div><div class="abs nw" style="left:64px;top:94px">-60 °C</div>
 <div class="abs nw" style="left:106px;top:94px;color:{P['grey']}">Water boils at</div><div class="abs nw" style="left:166px;top:94px">14 °C</div>
 <div class="abs nw" style="left:5px;top:104px;color:{P['grey']}">Power</div><div class="abs nw" style="left:34px;top:104px;color:{P['ink']}">Enough</div>
 <div class="abs" style="left:74px;top:106px;width:40px;height:6px;border:1px solid {P['ink']};background:#fff"><i class="abs" style="left:0;top:0;bottom:0;width:50%;background:{P['blue']}"></i></div>
 <div class="abs nw" style="left:118px;top:104px;color:{P['grey']}">5.0 / 10.0</div>
 <div class="abs nw" style="left:5px;top:114px;color:{P['grey']}">Next goal</div><div class="abs nw" style="left:48px;top:114px">Phase 2 begins 1.60 / 5 kPa</div>
</div>
<div class="note">MOCKUP B: light science plot. Example history (invented), Sol 6 to 16. Pa as in REMS plots. Backdrop: real shot.</div>
"""
# ---------- C ----------
segs=''.join(f'<i class="abs" style="left:{i*9}px;top:0;width:8px;height:7px;background:{D["up"] if i<5 else "#3A302A"}"></i>' for i in range(10))
C=f"""
<div class="abs dark" style="left:6px;top:6px;width:414px;height:88px">
 <div class="abs val nw" style="left:6px;top:3px">Planetary Monitor</div>
 <div class="abs lab nw" style="left:150px;top:3px">Esc to close</div>
 <div class="abs lab nw r" style="right:6px;top:3px">Sol 16.0</div>
 <div class="abs" style="left:6px;right:6px;top:14px;height:1px;background:#6B4A36"></div>
 <!-- cells -->
 <div class="abs lab nw" style="left:6px;top:18px">Air pressure</div>
 <div class="abs val big nw" style="left:6px;top:28px">1.60 kPa</div>
 <div class="abs up nw" style="left:6px;top:41px"><span class="tri"></span>Rising +0.10 /sol</div>
 <div class="abs" style="left:92px;top:18px;width:1px;height:44px;background:#4A3C34"></div>
 <div class="abs lab nw" style="left:98px;top:18px">Temperature</div>
 <div class="abs val big nw" style="left:98px;top:28px">-60 °C</div>
 <div class="abs st nw" style="left:98px;top:41px"><span class="flat"></span>Steady</div>
 <div class="abs" style="left:168px;top:18px;width:1px;height:44px;background:#4A3C34"></div>
 <div class="abs lab nw" style="left:174px;top:18px">Water boils at</div>
 <div class="abs val big nw" style="left:174px;top:28px">14 °C</div>
 <div class="abs lab nw" style="left:174px;top:41px">needs 0.61+ kPa</div>
 <div class="abs" style="left:244px;top:18px;width:1px;height:44px;background:#4A3C34"></div>
 <div class="abs lab nw" style="left:250px;top:18px">Battery</div>
 <div class="abs val big nw" style="left:250px;top:28px">50%</div>
 <div class="abs nw" style="left:250px;top:43px;width:89px;height:7px">{segs}</div>
 <div class="abs lab nw" style="left:250px;top:52px;font-size:6px;line-height:7px">5.0 / 10.0 units</div>
 <div class="abs" style="left:346px;top:18px;width:1px;height:44px;background:#4A3C34"></div>
 <div class="abs nw" style="left:352px;top:18px;line-height:10px"><span class="light lg"></span><span class="up">Power ok</span></div>
 <div class="abs nw" style="left:352px;top:29px"><span class="light lg"></span><span class="up">Outgasser</span></div>
 <div class="abs nw" style="left:352px;top:40px"><span class="light lg"></span><span class="up">Dome</span></div>
 <div class="abs lab nw" style="left:250px;top:62px;display:none"></div>
 <div class="abs" style="left:6px;right:6px;top:64px;height:1px;background:#4A3C34"></div>
 <div class="abs lab nw" style="left:6px;top:69px">Next goal</div>
 <div class="abs val nw" style="left:50px;top:69px">Phase 2 begins</div>
 <div class="bar" style="left:128px;top:70px;width:200px;height:7px"><i style="width:32%;background:{D['cause']}"></i></div>
 <div class="abs val nw" style="left:334px;top:69px">1.60 / 5 kPa</div>
 <div class="abs lab nw r" style="right:6px;top:79px;font-size:6px;line-height:7px">Goals 1 / 2 · making +5.0, using -4.0 units/sol</div>
</div>
<div class="note" style="top:96px;bottom:auto">MOCKUP C: HUD strip. Status lights and battery bar are invented (no real source image). Covers the tutorial toast; backdrop: real game shot.</div>
"""
for n,b in (('A',A),('B',B),('C',C)): open(f'{n}.html','w').write(page(b))
