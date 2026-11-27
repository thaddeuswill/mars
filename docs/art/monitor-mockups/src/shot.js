const {chromium}=require('/opt/node-tools/node_modules/playwright');
(async()=>{const b=await chromium.launch();const p=await b.newPage({viewport:{width:1280,height:720}});
for(const n of ['A','B','C']){await p.goto('file://'+process.cwd()+'/'+n+'.html');await p.evaluate(()=>document.fonts.ready);await p.waitForTimeout(300);
await p.screenshot({path:`mockup-${n}.png`});}
await b.close()})();
