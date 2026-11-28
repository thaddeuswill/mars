# Subject Matter Expert: hydrosphere target, perchlorate, sorbent, gate items

Date 2026-11-28. Branch `claude/elegant-brown-anu1ci`. Full content: `docs/science/hydrosphere-and-gate-items.md`. Docs only; nothing under `mod/` or `pack/` touched.

## Handoffs received
Game Designer (`phase3-hydrosphere-and-drift.md` 2.5, `gate-input-items.md` section on open questions): source the water inventory, the loss direction, the 30 percent label, "ground-ice melting is a game-sized idea", resin and porous brick. All accepted and answered. Not done: polar-cap volume in km3, GRS maps and the 2023 Utopia/Arcadia papers were **not opened** (marked NOT VERIFIED). The Utopia SHARAD result I opened is from 2016.

## Done (what I actually ran)
- Opened by curl: NASA Mars Facts, NASA Utopia caption, Planetary Society Utopia article, ~25 NTRS records via the NTRS API, PubMed abstracts via eutils. HTML stripped with `python3 -I`; nothing executed. PubMed web pages and the PMC page did not open (cookie wall, empty).
- Wrote `docs/science/hydrosphere-and-gate-items.md` (inventory table, 30 percent check, loss direction, salt side effect, perchlorate, sorbent, item analogues).
- Key numbers: near-surface ice about 34 m global layer (verified, second hand); Utopia deposit 14,300 km3 = about 0.1 m global layer (calc); 30 percent of the surface at 50 to 100 m depth = 15 to 30 m (calc), so the target would melt roughly half to most of today's ice.

## Decisions needed (owner)
1. Keep 30 percent with the label "Game design, based on real ice"? Recommend yes. **Ill-structured** (fun vs. realism): lowering to about 20 percent would match a possible ocean fraction better but that figure is not verified.
2. Reword the resin pack from "Game invention, not real" to "game-sized version of a real idea"? Recommend yes. Well-structured.
3. Keep "porous regolith brick" as the sorbent recipe input, flagged as a stand-in? Recommend yes, with the tooltip wording in the science doc, not a rename. **Ill-structured** (recipe makeability vs. honesty).

## Handoffs
> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** Label and wording fixes from the science review.
> **Context:** `docs/science/hydrosphere-and-gate-items.md` sections 1.2 to 1.4, 2, 3, 4.
> **Needed from them:** Hydrosphere label "Game design, based on real ice"; loss-term note "freezing, vapor loss, soaking in; rate invented"; v2 loss rises when cold and when pressure is low; resin pack relabelled; sorbent tooltip; heater tooltip should say "heater element" if it quotes Phoenix; journal line that salts come from soil, not the ice itself.
> **Done when:** the design docs and tooltips carry these lines and the `phase3-hydrosphere-and-drift.md` 2.5 items are closed.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Item tooltips and the loss term's `source` text need the new wording; no code change to rates.
> **Context:** `pack/data/mars/parameters/hydrosphere` (not yet present), item lang for `resin_pack`, `sorbent_bed`, `heater_coil`.
> **Needed from them:** put the "invented rate, real direction" text in the loss-term `source` field; update lang strings when the Designer confirms.
> **Done when:** the sheet-vs-data test passes with the new strings.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Check content rules (CR-F9, CR-F10) against the new journal lines; the salt-level forecast must not imply Mars ice is salty.
> **Context:** science doc section 1.4.
> **Done when:** the lines are checked.

## Risks
- The 34 m figure is a summary of Carr and Head (2015), not their paper; the polar-only share and ocean surface fraction are unverified. Keep ocean-size claims out of text until opened.
- "Mars may have had an ocean" is debated; journal must say "may have".
- Mixing "removes CO2" with "locks it in rock": the ISS beds release CO2 again. Needs the SIMPLIFIED note or players may think scrubbers destroy CO2.

## Shared model updates
- Hydrosphere is labelled "Game design, based on real ice" (proposal, owner: Game Designer; documented in `docs/science/hydrosphere-and-gate-items.md`).
- Lesson: PubMed pages and PMC are cookie-walled for curl; use eutils (efetch abstracts) and the NTRS API (`/api/citations/search`, `/api/citations/<id>`) for verifiable quotes. Respect eutils' 3 requests per second limit.
- Corrects `mars-materials-and-recipes.md` row 1.3: the Phoenix perchlorate figure (~0.5 wt percent) is now verified (NTRS 20190028297).
