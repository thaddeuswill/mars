# pack/

The modpack as text files (packwiz), plus our own content sources. Owner: Technical Lead. Layout and rules: [ARCHITECTURE.md §3.2](../ARCHITECTURE.md).

| Folder | What goes here | Owner of the content |
|---|---|---|
| `assets/mars/textures/` | Our textures, paths as in [style guide §5.3](../docs/art/style-guide.md) | Lead Artist |
| `assets/mars/sounds/`, `assets/mars/sounds.json` | Our sounds (Ogg Vorbis; mono for positional sounds); every sound needs a subtitle key | Lead Artist |
| `assets/mars/keyframes/` | Sky, fog, cloud, ambient-sound, and weather-mast keyframes (ARCHITECTURE.md 2.5.2) | Lead Artist |
| `assets/mars/lang/` | All player-facing text (`.ms` / `.hs` key variants) | Lead Game Designer, Lead Instructional Designer |
| `assets/mars/models/`, `blockstates/`, `particles/` | Block, item, and particle definitions | Lead Artist with Technical Lead |
| `data/mars/` | Datapack content: parameters, signals, gates, conditions, phases, hazards, Monitor pages, story, learning | Per document (DESIGN, LEARNING, story bible) |
| `mods/` | packwiz references to third-party mods | Technical Lead |
| `config/`, `defaultconfigs/` | Third-party configs; our server config presets | Technical Lead |
| `kubejs/server_scripts/` | Recipes and recipe gating, one file per integrated mod | Technical Lead |
| `licenses/` | Licence and notice of every bundled mod | Technical Lead |

`assets/` and `data/` are **built into the companion mod's jar** (build plan §4) and ignored by packwiz (`.packwizignore`). Placeholder art carries the 2×2 magenta marker (style guide §5.5). Never put Create's or any other mod's assets here.
