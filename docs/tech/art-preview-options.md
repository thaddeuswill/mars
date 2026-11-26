# Art preview options: seeing what the game will look like

**Date:** 2026-11-26 · **Requested by:** owner (BACKLOG #19 follow-up: "everything it can show … find an open source project that you can mcp through agents") · **Status:** research only; nothing below except Blender has been run.
**Sources:** project pages read by web-fetch readers, plus search results. Items marked *unread* could not be opened (npm returned 403; some hosts were not allowed).

## What Blender (done) can and can't show
Shapes, proportions, and texture readability of block models, rendered by `tools/art-preview/`. It cannot show Minecraft's lighting, blocks in a world, animation, entity models, or the GUI.

## Options found

| Option | License | What it can show | MCP? | Fits our modded 1.21.1 game? | Status |
|---|---|---|---|---|---|
| **Blockbench headless MCP** ([jasonjgardner/blockbench-mcp-plugin](https://github.com/jasonjgardner/blockbench-mcp-plugin), v1.10.1) | GPL-3.0 (Blockbench itself is GPL-3.0) | Block models, entity models, textures, animations. Tools: `bbmodel_render` (one view to PNG), `bbmodel_contact_sheet` (several views), `bbmodel_sample_pose` (numbers, not an image), create/edit/validate, `bbmodel_export_java_block`, `bbmodel_export_modded_entity`. Particles are **not** rendered headlessly; Bezier keyframes are sampled as straight lines. | **Yes**, stdio: `claude mcp add bbmodel --transport stdio -- npx -y github:jasonjgardner/blockbench-mcp-plugin --root <folder>` (or `bunx`) | It authors and previews **models**, not the world. Java block export fits us; GeckoLib is only validated, not confirmed exportable. | **Not run.** README says rendering needs "Node 23.6 or newer with npm, and a GPU" and downloads about 130 MB on first render. This sandbox has Node 22.23.5, Bun installed, and no GPU (software GL only). |
| Other Blockbench MCPs ([enfp-dev-studio](https://github.com/enfp-dev-studio/blockbench-mcp) MIT; sosadly and "Stonecut" MIT per search, *unread*) | MIT | enfp: no screenshot tool mentioned; two commits, 34 stars. sosadly and Stonecut: *unread* (lobehub.com not allowed) | Yes (Blockbench desktop app plugin over a local socket) | Same as above | Not run; the desktop-plugin route needs the Blockbench desktop app |
| **Real client, headless** ([HeadlessMC](https://github.com/headlesshq/headlessmc) MIT; [MC-Runtime-Test](https://github.com/marketplace/actions/mc-runtime-test) action) | MIT (HeadlessMC) | The **real game**: world, lighting, entities, animation, GUI. | No MCP; a command-line launcher. HeadlessMC README: runs the client headless and "handles various mod loaders". MC-Runtime-Test lists NeoForge ✅ for 1.21–1.21.11 and uses Xvfb. | **Best fit.** HeadlessMC's page names no Minecraft version and says nothing on screenshots; MC-Runtime-Test only joins a world and quits (runs GameTests). Neither takes screenshots. | Not run. A screenshot needs a small piece of **our own** code (a dev-only client command calling Minecraft's own screenshot function) and a software-GL client run under Xvfb. Unverified whether this is fast enough here. |
| Mineflayer bot MCPs ([yuniko-software](https://github.com/yuniko-software/minecraft-mcp-server) Apache-2.0; [haksndot/haksnbot-tools](https://github.com/haksndot/haksnbot-tools) MIT) | Apache-2.0 / MIT | yuniko: no rendering, no screenshots. haksnbot-tools: `take_screenshot` (3D render via prismarine-viewer; needs OpenGL/Mesa) | Yes | **Doubtful.** These are vanilla-protocol bots; the pages say nothing about NeoForge, custom blocks, entities, or dimensions. | Not run |
| [prismarine-viewer](https://github.com/PrismarineJS/prismarine-viewer) | MIT | World view in a browser (three.js), supports up to 1.21.1 and 1.21.4 textures | No (a library) | **Doubtful:** uses the game version's own textures and models; nothing about mod assets | Not run |
| [minecraft-render-py](https://github.com/hexdoc-dev/minecraft-render-py) | MIT | Renders block models with three.js to PNG; needs `xvfb-run` | No | Reads models from the game jar; mod asset folders are listed as unfinished | Not run |

*Unread:* awesome-mineflayer-mcp, `@leftsquarebracket/minecraft-render`, `minecraft-renderer` (npm 403 or host not allowed), CraftGround (host not allowed).

## Recommendation (needs the owner's choice)
1. **Real in-game screenshots are the answer to "everything it can show."** Owner: Technical Lead, after the Stage 1 skeleton builds. Add a dev-only client command to our mod that calls Minecraft's screenshot function, and run the client under Xvfb with software GL (Mesa is present here; the Mojang hosts are now allowed). Use HeadlessMC or the dev run for launching. Agents can run it as a plain script, so no MCP is needed for this route.
2. **Blockbench headless MCP is the only MCP found that fits our art workflow** (render, contact sheet, validate, export Java block models). It is **unrun**, requires a newer Node than this sandbox has, and wants a GPU. Run it only after review (below).
3. **Mineflayer-based MCPs and prismarine-viewer: not recommended** for our content unless a page confirms modded NeoForge support.
4. Keep the Blender preview for fast shape checks.

## Safety notes for the Blockbench MCP
- `npx` or `bunx github:…` fetches and runs third-party code at launch. **The first attempt to run it was blocked by the safety classifier**, and was not retried.
- Headless server: `--root` limits file access; no authentication mentioned. Desktop plugin: **no authentication** and a `risky_eval` tool that runs any JavaScript a client sends; binds to localhost by default (README). Avoid the desktop plugin.
- GPL-3.0 applies to the tool, not to the models we make with it.
- Project-scoped MCP servers would live in a `.mcp.json` the owner must approve, and subagents would need the MCP tool names added to their tool lists: unverified.

## Decision needed from the owner
(a) Approve running the Blockbench headless server in a scratch folder, pinned to a specific commit; (b) first have its source reviewed (read-only: install scripts, network calls, file access), then decide; or (c) skip Blockbench and go straight to in-game screenshots.
