# Report: Artist inspiration survey (2026-11-28)

## Handoffs received
Owner request (via coordinator): "what are some mods we are taking inspiration from; we need to be taking a lot of inspiration for every aspect." Accepted. Docs only; no downloads.

## Done
- `docs/art/inspiration-survey-look-and-sound.md`: eight aspects, 3 to 7 references each, with copy / avoid / where-it-applies, then "What we do now", "Gap", "Recommended next steal" per aspect, and a ranked top-8 table.
- What I actually ran: Modrinth API lookups (project JSON, search, 1.21.1 NeoForge version lists for Mekanism, Create, AmbientSounds, Sound Physics Remastered). JSON kept in the scratchpad, read as data only, never run.
- **Verified** = Modrinth metadata only (licence, type, latest build, date). **Not verified:** how any mod, pack or shader looks; every game, film and NASA statement is from memory. Stay True, John Smith Legacy and Sphax base packs were not found on Modrinth (only add-ons); no Mars terrain resource pack exists there by my search. IE and Thermal 1.21.1 builds not checked.
- Honest finding: so far our look came from real NASA hardware and the owner's accepted W1 terrain, not from named game or mod references. The survey fixes that.

## Decisions needed
None. Well-structured work; the only taste call (shader look vs our keyframes) is already in `docs/tech/optional-mods-and-shaders.md` section 5.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** top steal 1 is already specified.
> **Context:** `docs/art/atmosphere-plan.md` AT-2, AT-1, AT-3.
> **Needed from them:** build in that order; confirm the 110-degree wind convention and the GUI-layer quad.
> **Done when:** one screenshot trio shows haze, a storm tint and dust motes.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** add a "mission control rules" box (sol clock, unit and trend on every number, one rare alarm colour) to the Monitor UI spec.
> **Context:** survey section 4.
> **Done when:** `docs/design/ui-specs.md` carries it or the designer declines with a reason.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** source the Perseverance microphone facts and sample one white-balanced Mastcam frame for ground colour.
> **Context:** survey sections 1 and 5 (both from memory).
> **Done when:** marks change from "memory" to "verified".

## Risks
- Memory-based game and film notes may contain errors; flagged in section 10 of the survey.
- Inspiration drift: mod looks (Mekanism neon, Create steampunk) must not pull our real-hardware look away. Each row lists what we avoid.
- NASA media guidelines still unchecked before any real image or sound enters the repo.

## Shared model updates
None (no decisions made). Suggested lesson: when asked "what do we take inspiration from", record references in the repo with a verified/memory mark, not only in chat.
