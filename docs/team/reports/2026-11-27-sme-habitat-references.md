# SME report: habitat reference images and audit (2026-11-27)

**Task:** collect real reference imagery and facts so the starting dome and life-support blocks look like real hardware and real Mars habitat concepts (owner request: "make sure what we build looks like the real reference"). **What I did:** a document and image review. I downloaded images, opened and looked at each, and wrote cues. I did not run the game.

## 0. Handoffs received
Task came from the coordinating session with no role handoffs. Accepted as written, including "do not change the owner-accepted layout".

## 1. Done
- `docs/art/habitat-reference-audit.md`: per-reference findings, facts with sources and [V]/[NV] marks, outside-look comparison (dome, cylinder, inflatable, buried), cues for hull plate, hatch and airlock, oxygen maker, reclaimer, tank, Hab B annex, what 16 px can and cannot show, misconceptions, gaps.
- `docs/art/references/habitat/` (13 images + README): Mars Ice Home, MARSHA, SEArch+/Apis Cor printed wall, CHAPEA interior, BEAM on Node 3, ISS water-rack photos (2), NORS oxygen tank, airlock hatch, MDRS Hab, NASA 2002 radiation-shelter concept, MOXIE (2, copied from the earlier set).
- Key findings for the artist: the screenshot's box lacks every common real cue (trim bands, round portholes, entry with step/ramp, roof equipment); the hatch is grey aluminium with rounded corners, round ringed window, crank and placards; MOXIE is a gold pocket-panel box with X-ribs, white tubes, silver fittings; ISS racks are off-white latched panels with blue handrails, and the open back is dark with silver studs; the NORS tank is white-wrapped with yellow-black and green labels.

## 2. Decisions needed
1. **Roof rim, airlock stub with steps, regolith berm.** Outside the accepted layout (23 x 16 footprint) but additive. Recommend: rim, vent and dish on the roof (no layout change) now; stub and berm after the Game Designer looks. **Ill-structured** (looks vs layout vs radiation teaching).
2. **Use of MDRS image (CC BY 2.5).** Fine as internal reference; if any art is derived or shown, credit McKay Salisbury. **Well-structured.**

## 3. Handoffs

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** hull plate, door, tank, oxygen maker, reclaimer, annex glass look generic.
> **Context:** `docs/art/habitat-reference-audit.md` section 4 (cues tagged [V] with image numbers).
> **Needed from them:** redo textures/models to the cues; keep MOXIE gold box + X-rib + tubes; off-white hull with trim courses; hatch with round window and indicator arcs. Check colours by eye against the images (colours in the audit are not measured).
> **Done when:** new screenshot of the dome from outside and the Ops/machine bay shows trim bands, portholes and the new maker.

> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** a flat 23 x 16 box does not look like any real habitat; entry, roof and shielding are unrepresented.
> **Context:** audit section 3; `docs/design/dome-layout.md`.
> **Your view (SME):** real habitats show an entry with steps or ramp, round portholes, roof equipment and shielding (ice, regolith). Options: (A) texture and roof-rim changes only; (B) A plus a 1 to 2 block airlock stub with steps; (C) B plus a regolith berm on lower walls (also teaches radiation shielding). Recommend A now, B and C as a later look at the layout.
> **Needed from them:** pick, or ask the Technical Lead for cost.
> **Done when:** decision recorded in `dome-layout.md`.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** a regolith berm or ice shell could carry a radiation-shielding lesson.
> **Needed from them:** alignment check for any goal ID before it is used.

## 4. Risks
- Several numbers (MOXIE size and output, BEAM size, ISS recovery rate, CHAPEA area) are **from search summaries of NASA pages I did not open**; marked in the audit. Do not put them in journal text until opened.
- Colours are by eye only.
- No image of a buried/regolith-covered habitat, a suitport, or the Quest gas tanks was found.
- Wikimedia rate-limits; `images-api.nasa.gov` is blocked for this session, so NASA Image Library was not searched.
- Image #4 (MARSHA) attribution comes from the file name; NASA's page alt text says "zopherus".

## 5. Shared model updates
- Lesson: WebFetch cannot resolve NASA/Commons hosts in this session; `curl` through the proxy works (add 8 to 15 s delays for Wikimedia, and compute `upload.wikimedia.org` thumb paths from the md5 of the file name to avoid the API).
- New reference set: `docs/art/references/habitat/` and `docs/art/habitat-reference-audit.md`.
