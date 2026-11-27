# Can Schools Run It? Java + NeoForge vs. Minecraft Education

**Owner:** Technical Lead · **Checked:** 2026-11-26 · **Answers:** risk R-09, consistency review m-11, ROADMAP Stage 4 "Classroom deployment"

> **Context.** The project is a design exercise: no real students will use it (TEAM.md Decision Log, 2026-11-26). We still design as if schools would, so the deployment facts below matter for the design. They are not a launch plan.

## Short answer

- **Minecraft Education cannot run this pack.** It is a separate product built on Bedrock, and it doesn't support community mods. NeoForge mods are Java Edition only.
- **Java Edition with NeoForge can run in a school**, but only where the school (or a club) has Java Edition accounts, lets students install a launcher, and lets them join a server. Many schools that use Minecraft already have *Minecraft Education* licenses, which **don't carry over** to Java Edition.
- So the realistic classroom audiences are: schools that run Java Edition on purpose (computer labs, STEM programs), clubs and after-school groups, and home play. A typical school that only has Minecraft Education is **out of reach** without a port to Bedrock add-ons, which our custom dimension and Java mod can't support.

## Findings

| # | Question | Finding | Source | Status |
|---|---|---|---|---|
| 1 | Does Minecraft Education run community mods? | FAQ: "Community created mods are not currently supported in Minecraft Education." | Minecraft Education support FAQ ([article](https://edusupport.minecraft.net/hc/en-us/articles/360047117692)) | Verified via search quote; page not fetched |
| 2 | What is Minecraft Education built on? | The Bedrock engine; it "normally runs about one full version behind" Bedrock. Uses Bedrock add-ons, not Java mods. | Minecraft Education support pages, via search | Verified via search; page not fetched |
| 3 | Do Education licenses work for Java Edition? | No: "Since each of these Minecraft editions use distinct licensing systems, it's not possible to login to a different edition with Minecraft Education licenses." | Minecraft Education support ([article](https://edusupport.minecraft.net/hc/en-us/articles/360061371532)) | Verified via search quote |
| 4 | How do schools buy Minecraft Education? | Through Microsoft volume licensing (EES) or the Microsoft 365 admin center. Each user needs a Microsoft 365 school account. | Microsoft Learn, "school get minecraft" ([page](https://learn.microsoft.com/en-us/education/windows/school-get-minecraft)); Minecraft Education licensing ([page](https://education.minecraft.net/licensing)) | Verified via search |
| 5 | Is there a school volume license for **Java Edition**? | No official source found. A community answer on the Education forum says bulk purchase of Java may not be possible, though single copies can be bought. | Education community forum ([post](https://edusupport.minecraft.net/hc/en-us/community/posts/4403866194580-Where-can-I-buy-35-copies-of-Java-Minecraft-of-teaching-in-UK)) | **Unverified** (forum only) |
| 6 | Can student (child) Microsoft accounts join Java multiplayer? | Only if the family organizer enables multiplayer in the Microsoft/Xbox privacy settings. Blocked accounts see greyed-out Multiplayer buttons or "Failed to login: null". Several reports say the setting can take time to apply. | Minecraft help, Microsoft account privacy settings ([guide](https://hopper.minecraft.net/help/guides/xbox-privacy/)); Microsoft Q&A threads (user reports) | Verified (official guide, via search); fixes are anecdotal |
| 7 | Does Minecraft Education now have dedicated servers? | Yes: a 2026 update added dedicated servers run on-premises or in the cloud by school IT. | Minecraft Education blog, "Copper, Collaborate, and Compete" ([post](https://education.minecraft.net/blog/copper-collaborate-and-compete)) | Verified via search |
| 8 | Java Edition hardware needs | Mojang raised the Java Edition requirements: a 64-bit system, 8 GB RAM with a discrete GPU or 12 GB with integrated graphics, a 4-core CPU, and a Vulkan 1.3 GPU with 2 GB VRAM for 1080p at 30 FPS. | minecraft.net, "Java Edition system requirements" ([article](https://www.minecraft.net/article/minecraft-java-edition-system-requirements)); numbers from a localized copy via search | **Partly verified.** These are for current Java releases. Whether they apply to 1.21.1 (which predates the Vulkan change) is **unverified**. If they don't, staying on 1.21.1 may help weak lab PCs. |
| 9 | Java runtime | The game launcher bundles its own Java for players. Developers and server hosts need Java 21 for 1.21.1. | NeoForge docs (1.21.1), via search | Verified (server side) |

## Round 3 recheck (2026-11-26)

The help-centre pages block plain page requests (a browser check), but the help centre's public article API answered. Read directly:

| # | Finding | Source (read 2026-11-26) | Status now |
|---|---|---|---|
| 1 | "Mods are modifications to Minecraft: Java Edition … **Community created mods are not currently supported in Minecraft Education.**" | "FAQ: Game Features", article 360047117692, updated 2026-11-26 ([API](https://edusupport.minecraft.net/api/v2/help_center/en-us/articles/360047117692.json)) | **Verified** |
| 1b | Same FAQ: "Minecraft Education is not compatible with other versions of Minecraft such as Java or Bedrock" | Same | **Verified** |
| 3 | "Can I use my Minecraft Education licenses to access a Minecraft Bedrock or Java server? Since each of these Minecraft editions use distinct licensing systems, it's not possible to login to a different edition with Minecraft Education licenses." | "FAQ: Availability, Pricing, and Licensing", article 360047119092, updated 2026-11-26 (found through the help centre's search API) | **Verified** (the quote now lives in this article, not 360061371532) |
| 7 | Dedicated servers | "How To Set Up A Multiplayer Game" (360047555531, updated 2026-11-14) still says Minecraft Education "is not currently compatible with server software", while another article links a "Dedicated Server FAQ" | **Conflicting official pages**; doesn't affect us (we can't run in Minecraft Education either way) |

Conclusion unchanged and now verified: **this pack can't run in Minecraft Education.**

## What a school would need (draft IT requirements sheet)

For the educator guide (Lead Instructional Designer owns that document; this is the technical input).

| Need | Detail | Why |
|---|---|---|
| Java Edition account per student | Microsoft account with Java Edition; child accounts need multiplayer enabled by the account organizer | Required to launch and join (finding 6) |
| Launcher that installs modpacks | A third-party launcher that reads a Modrinth pack (`.mrpack`), or a pre-built instance folder the teacher copies | Students can't install NeoForge by hand in a lesson |
| Install rights | IT must allow the launcher and Java to run (often blocked on managed lab PCs) | Common school restriction (**unverified** how common) |
| One server machine | Runs the class server (see "Hosting options" in ARCHITECTURE.md §6). Needs Java 21 and enough RAM; numbers to measure in Stage 3 | All group planets run here |
| Local network | Students reach the server on the school LAN. Default Java server port is TCP 25565 (**unverified** in this check; standard Minecraft server default). No internet traffic is needed for play once accounts have signed in. | School firewalls often block game ports (**unverified**) |
| Storage | World, backups, and the local data folder on the server machine | Backups and the action log (ARCHITECTURE.md §7) |

## Undoing a bad session (slice 8)

Two tools, for different damage (ARCHITECTURE.md 7.4a):

| Damage | Tool | How | Covers |
|---|---|---|---|
| Wrong numbers or a goal latched by mistake | In-game checkpoint | `/mars checkpoint list`, then `/mars checkpoint restore session-N` (one is saved at every `/mars resume`); `restore start` for the start values | Planet numbers, goals, history, battery. **Not** blocks, items, inventories, other mods |
| Griefing, destroyed machines, a crash | World-folder backup | With the server **stopped**: `tools/world-backup.sh backup <world> <backups>` before class; after a bad session `tools/world-backup.sh restore <world> <backup file>` (the current world is moved aside, never deleted) | Everything in the world folder; all or nothing (no per-planet restore yet) |

Cost: a checkpoint is a few KB. A world backup is one archive of the world folder per class (size not yet measured on a real class world). The script needs a shell (Linux, macOS, or Git Bash on Windows; not tested on Windows or macOS) and does nothing automatically: schedule it from your start script. Student learning data is stored outside the world and is never touched by either tool.

## Consequences for the design

1. **Club and home play are first-class audiences**, not a fallback. Singleplayer must work fully (one planet, no teacher tools needed).
2. **The class server is one program on one machine** (ARCHITECTURE.md §6, recommended option). Teacher setup time is the Scorecard Feasibility target (under 30 minutes).
3. **No Bedrock port.** It would mean rewriting the companion mod as Bedrock add-ons with far less control over the sky, dimension, and simulation. If the user ever wants Minecraft Education schools, that is a separate product decision.
4. **Educator guide** must say up front: "Requires Minecraft: Java Edition. Does not run in Minecraft Education."

## Still to verify

- Read findings 1–3 and 8 directly on the official pages (hosts blocked this session).
- Whether the new Java hardware requirements apply to 1.21.1.
- Whether the Java Edition EULA or usage guidelines limit school servers or a downloadable classroom bundle (risk R-26, Producer).
