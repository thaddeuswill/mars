# Technical Lead: inspiration survey (docs only)

Date 2026-11-28. Branch `claude/elegant-brown-anu1ci`.

## Handoffs received
Coordinator task from the owner: "what are some mods we are taking inspiration from; we need to be taking a lot of inspiration for every aspect of this". Accepted. Docs only; no jar downloaded or run.

## Done
- `docs/tech/inspiration-survey-systems.md`: nine systems, about 80 mods, each with a USE / STUDY / SKIP verdict, what we do now, gap, and a sized next steal; ranked top 8 at the end.
- Method (real): `api.modrinth.com` project and version queries for licence, loaders, 1.21.1 builds and dates for about 70 projects, 2026-11-28; Galacticraft 5 LICENSE and README and the first part of the Ad Astra Terrarium licence read from raw.githubusercontent.com. JSON stayed in the scratchpad; no jar fetched.
- Not done: how mods behave in game (UI conventions, Jade plugin API, Patchouli multiblocks) is **from memory, marked M**. Licence text unread for Immersive Engineering, Terralith and Stardust packs, GuideME, Alex's Caves. CurseForge flags unchecked (host blocked). Flux Networks and Thermal 1.21 are probably CurseForge-only; unverified.

## Decisions needed
1. Owner-installed (not bundled) add-ons: Jade, Sound Physics Remastered, Inventory Profiles Next? Recommendation: yes, Jade first. *Well-structured, owner taste.*
2. Which "next steal" goes into the backlog first? Recommendation: Jade plugin line (small), then planter as data (Botany Pots shape). *Ill-structured* (needs the Designer and SME: what planter teaches).

## Handoffs
> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** Greenhouse planter and soil treatment need a design before code (machine-priorities rows 2, 9).
> **Context:** survey section 6; Botany Pots keeps soil and crop rules in datapack JSON; Mystical Agriculture is the anti-pattern.
> **Needed from them:** which growth conditions (pressure, temperature, light, soil chemistry) a Phase 1 planter shows, and the failure the lesson C1 needs.
> **Done when:** a one-page planter spec exists.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** Check the claim "Thirst Was Taken-style purification maps to perchlorate removal" and what real Mars flight hardware calls it.
> **Needed from them:** a sourced one-line description.
> **Done when:** the Meltwater treatment tooltip has a sourced line.

## Risks
- Several UI claims are from memory; do not treat them as verified before the owner plays the mods.
- NC-SA, GPL and AGPL add-ons are fine for the owner's own PC but would block bundling if the pack is ever published.
- Over-borrowing UI patterns could pull the design toward a "tech tree" feel against "the planet is the quest log".

## Shared model updates
None.
