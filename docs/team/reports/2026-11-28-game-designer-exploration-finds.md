# Game Designer report: exploration finds (rovers, minerals, "Signs of life?")

Date 2026-11-28 · Docs only · Design walkthrough, not a playtest · Output: `docs/design/exploration-finds.md` (status: proposed)

## 0. Handoffs received
Owner idea (BACKLOG): life on Mars, finding rovers, rare metals, things to help find life and make Mars habitable. Accepted. The SME review `docs/science/exploration-finds-review.md` did not exist when I finished, so every real-world fact is marked unverified.

## 1. Done
- `docs/design/exploration-finds.md`: principles E1 to E9; heritage sites (replica vs real hardware options, 9 sites, MVP three, journal entries at two depths, Hall as a timeline wall, seeded ring placement, BELL orbit-data hint, Artist method); 7 mineral nodes with mapping and an honest "rare metals" table; "Signs of life?" loop (5 steps, confidence ladder with no top, case 1 "fizz test", feedback, SOL-1 rule, ties to B3, C2, D1, Phase 3 biomass and gates); lever table (no new levers); CR-F9 and CR-F10 table; costs; stage placement; cut order; audit rows.
- Not changed: DESIGN.md, LEARNING.md, content rules (proposals only).

## 2. Decisions needed (owner)
1. Replicas by Hana (recommended) or real hardware on a compressed map? Ill-structured.
2. Case files always end at "not enough to say"? Recommend yes. Values call.
3. Build the MVP in Stage 4 Phase 2, funded by a cut-list swap; optional Stage 3 mini only if slices end early? Ill-structured.

## 3. Handoffs
> **To:** SME · **Problem type:** well-structured · **Problem:** verify all facts. **Context:** every item tagged unverified in `docs/design/exploration-finds.md`. **Needed:** confirm or correct, name the real life-detection confidence scale. **Done when:** review file exists and the doc is updated.

> **To:** Lead Instructional Designer · **Problem type:** ill-structured · **Problem:** the case loop is argument from evidence, not a listed goal. **Context:** section 4.4. **Your view:** B3 practice should not count in the forecast tally; C2 is a stretch. **Needed:** alignment call, add rule SOL-1, wording pass, recall check. **Done when:** alignment table row or a "not claimed" note.

> **To:** Technical Lead · **Problem type:** well-structured · **Problem:** feasibility. **Needed:** ring placement in the custom dimension (datapack-only?), per-player case store apart from `PlanetState`, which machine bakes iron oxide (open item G4). **Done when:** a short spike note.

> **To:** Lead Artist · MVP monument models, plaque, icons, node textures. · **To:** Producer · price the MVP, name the swap.

## 4. Risks
- Scope: MVP about 9 to 14 Technical Lead sessions (unverified) on top of a 40 to 66 session Stage 3; no budget row.
- Misconception: a student leaving with "life was found". Mitigated by the ladder, SOL-1, fiction note, recall check; untested.
- CR-F9: Phase 2 and 3 entries on dust, radiation, ice and flora can reveal F1, F2, F4a, F4b, F7, F8; held by `reveals`.
- Hall of rovers is a collectible-list risk (DESIGN Zeigarnik caution); designed as a timeline, no count.
- All facts from memory.

## 5. Shared model updates
- Proposed term: **heritage site** (replica monument of real hardware, labeled fiction); **case file** (a Signs of life? investigation).
- Proposed content rule SOL-1 (never says life was found).
- Decision candidates (not logged): no new Index levers from finds; finds are never on a gate's critical path.
