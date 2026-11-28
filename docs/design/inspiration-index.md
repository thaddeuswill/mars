# Inspiration index (2026-11-28)

One page over the three surveys. Owner direction: take a lot of inspiration for every aspect; if a mod does it better, use it; if not, build our own (docs/design/world-and-blocks.md 0.2, 5a). Only Modrinth metadata was read; features of mods and every game or film are from memory and marked so in the surveys. Details, "what we copy" and "what we avoid": see the three files below.

| Survey | File |
|---|---|
| Mechanics: progression, terraforming games, life support, machines, farming, exploration, story, science games | `docs/design/inspiration-survey-mechanics.md` |
| Look, UI and sound | `docs/art/inspiration-survey-look-and-sound.md` |
| Systems and mods (use / study / skip) | `docs/tech/inspiration-survey-systems.md` |

## Where we take inspiration, by aspect

| Aspect | Main references (from the surveys) | Use, study or ours |
|---|---|---|
| Real science and hardware | MOXIE, ISS water and CO2 systems, Phoenix TEGA, Rodwell, InSight arrays, SHARAD, SHERLOC, Veggie | Verified by the SME (`docs/science/machine-references.md`) |
| Progression and gates | Create progression packs, GregTech-style tiers, Factorio and Kerbal tech trees | Study: one unseen input item per gate |
| Terraforming and planet state | Per Aspera, Surviving Mars, Terraforming Mars, Planet Crafter | Study: planet report page after each gate, staged ground ladder |
| Life support and survival | Oxygen Not Included, Subnautica, Tough As Nails, Ad Astra, Galacticraft | Study: suit HUD with shape-coded gauges. No mod fits (Galacticraft-Legacy is Forge only and last updated 2023; Galacticraft 5 pre-alpha, Fabric only) |
| Machines, power, UI | Mekanism, Create, Immersive Engineering, Thermal, Factorio | Study: energy bar, status lamp, moving part, hold-to-explain card; multiblock processor later |
| Farming and ecology | Farmer's Delight, Botany Pots, Planet Crafter; Mystical Agriculture is the anti-pattern | Study Botany Pots (soil and crop rules as datapack JSON) |
| Worldgen | Terralith, Tectonic (Earth mods: not a fit) | Ours (`mars:planet` generator) |
| Atmosphere and weather | Complementary, BSL, Photon; Weather2; Blade Runner 2049 colour | Iris + Sodium + Complementary after the owner's look test; study Weather2 fronts |
| Sound | Perseverance recordings; Sound Physics Remastered, AmbientSounds, Presence Footsteps | Study; Sound Physics Remastered in the owner's optional stack |
| Journal, logs, in-game docs | Outer Wilds ship log, Obra Dinn, Modonomicon (MIT), Patchouli (CC-BY-NC-SA) | Ours; study Modonomicon for gate-unlocked pages |
| Overlays and QoL | Jade (CC-BY-NC-SA, owner installs, not bundled), WTHIT, AppleSkin, Mouse Tweaks, Distant Horizons | Optional stack; our own Jade plugin lines |
| Recipe viewer | EMI | Use, with hide-until-gate from gate `unlocks` |

## Combined short list of what to build next (coordinator's pick, effort unverified)
1. Machine feel: one visible moving part per machine plus starved-machine alerts on the Monitor (Artist 5, Game Designer 8). Small.
2. Staged ground-state ladder: raw regolith, washed soil, rich soil, biocrust (Game Designer 2). It also supplies the missing `washed_regolith` item. Medium.
3. One new unseen input item per gate, so the new gates unlock something (Game Designer 4). Small.
4. Mission-control rules for the Monitor: sol clock, unit and trend on every number, one rare alarm colour (Artist 4). Small.
5. First sounds: footsteps and block sounds by surface (Artist 2). Small.
6. EMI with hide-until-gate (Technical Lead 1). Medium.
7. Suit HUD with shape-coded oxygen and warmth gauges (Game Designer 5, Technical Lead 5). Small to medium.
8. Planet report page after each gate (Game Designer 6). Medium.

## Licence notes that matter if the pack is ever published
Patchouli, Jade and WTHIT are CC-BY-NC-SA: they cannot be bundled in a published pack; the owner can install them privately. Create's assets are all-rights-reserved (see `docs/tech/mod-licenses.md`). Licence text is still unread for Immersive Engineering, Terralith and some others. CurseForge flags are unchecked.
