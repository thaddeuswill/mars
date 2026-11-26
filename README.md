# Mars Terraforming Modpack

A total-conversion Minecraft modpack about turning a dead Mars into a living world, one atmospheric kilopascal at a time. Built for Minecraft: Java Edition with NeoForge 1.21.1 (it does not run in Minecraft Education). An educational design exercise for ages 12–18, aligned to NGSS.

## Start here
- [TEAM.md](TEAM.md): team shared mental model (read first)
- [DOCUMENTATION.md](DOCUMENTATION.md): document map, traceability, and shared formats
- [ROADMAP.md](ROADMAP.md): development stages, iteration model, and "Where We Are Now"
- [PROCESS.md](PROCESS.md): how we iterate day to day: definition of done, regression gate, UI and workflow rules, retro log
- [BACKLOG.md](BACKLOG.md): every owner request, the current iteration, and open questions for the owner

## Core documents
- [DESIGN.md](DESIGN.md): gameplay design and systems
- [LEARNING.md](LEARNING.md): learning goals, methods, assessment, and classroom mode
- [ARCHITECTURE.md](ARCHITECTURE.md): software architecture, algorithms, and coding practices

## Supporting documents

| Area | Documents |
|---|---|
| Design | [Balance sheet](docs/design/balance-sheet.md) · [Story bible](docs/design/story-bible.md) · [Playtest plan](docs/design/playtest-plan.md) · [UI specs](docs/design/ui-specs.md) · [Messages](docs/design/messages/README.md) |
| Learning | [Prompts and reflections](docs/learning/prompts-and-reflections.md) · [Rubrics](docs/learning/rubrics.md) · [NGSS map example](docs/learning/ngss_map.example.json) |
| Science | [Accuracy review](docs/science/accuracy-review.md) · [NGSS verification](docs/science/ngss-verification.md) |
| Technical | [Mod licenses](docs/tech/mod-licenses.md) · [School deployment](docs/tech/school-deployment.md) |
| Art and audio | [Style guide](docs/art/style-guide.md) · [Planetary keyframes](docs/art/planetary-keyframes.md) · [Asset list](docs/art/asset-list.md) · [Palettes](docs/art/palettes/) · `docs/audio/` (planned) |
| Production | [Budget](docs/production/budget.md) · [Risk register](docs/production/risk-register.md) · [QA plan](docs/production/qa-plan.md) · [Consistency review](docs/production/consistency-review.md) · [Consensus brief, Round 2](docs/production/consensus-round2.md) |
| Team | [Task reports](docs/team/reports/) · [Reference: game dev handoff](docs/team/reference/GAME_DEV_HANDOFF.md) |

## Team Agents

Claude Code agents for each Learning Game Design (LGD) role live in `.claude/agents/`:

| Agent | Role | Owns |
|---|---|---|
| `producer` | Producer / Project Manager | `ROADMAP.md`, `README.md`, `DOCUMENTATION.md`, `PROCESS.md`, `BACKLOG.md`, `docs/production/` |
| `technical-lead` | Technical Lead | `ARCHITECTURE.md`, `docs/tech/`, `mod/`, `pack/` |
| `lead-game-designer` | Lead Game Designer (incl. story) | `DESIGN.md`, `docs/design/` |
| `lead-instructional-designer` | Lead Instructional Designer | `LEARNING.md`, `docs/learning/` |
| `lead-artist` | Lead Artist (incl. audio) | `docs/art/`, `docs/audio/`, `pack/assets/` |
| `subject-matter-expert` | Subject Matter Expert (internal) | `docs/science/` |

`TEAM.md` is merged by the coordinating session; `docs/team/reports/` is filed by it.
