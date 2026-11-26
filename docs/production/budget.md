# Budget: Effort, Scope Check, and Cut List

**Owner:** Producer · **Issued:** 2026-11-26 (Round 2) · **Revision 2:** 2026-11-26 (Round 3) · **Stage:** 1 (Pre-production) · **Stability:** flexible (re-estimated at every stage gate)
**Sources:** [ROADMAP.md](../../ROADMAP.md), [DESIGN.md](../../DESIGN.md) (Fun–learning matrix, Learning Mechanisms in Play), [LEARNING.md](../../LEARNING.md) §1 goal priorities (**decided: option T-C**, [consensus-round2.md](consensus-round2.md)), [ARCHITECTURE.md](../../ARCHITECTURE.md), [docs/art/asset-list.md](../art/asset-list.md), [docs/design/playtest-plan.md](../design/playtest-plan.md), role reports in `docs/team/reports/`.

This is the "Budget" document in the DOCUMENTATION.md map. For a design exercise, money isn't the constraint. Two things are: **team effort** and **the user's time** (the only human who plays builds and approves decisions).

---

## 1. How to read these numbers

- **Unit:** person-hours of focused work, as if a small human team built the pack (the design exercise's "as if" rule, TEAM.md Decision Log).
- **Accuracy:** Producer estimates, **±50%**. There is no historical data yet. No role has built anything. Treat them as order-of-magnitude.
- **Re-estimate after Stage 3.** The vertical slice is the first real measurement: effort per machine, per journal entry, per texture, per sound. Stage 4 numbers get replaced with measured rates before Stage 4 is committed (risk R-01).
- **Included:** design, build, content writing, art, audio, reviews, debates (TEAM.md §4), testing (qa-plan.md), and fixes.
- **Not included:** post-release support (see §5), LMS integration (post-release, opt-in), voice audio (deferred).

## 2. Effort budget by stage and role (hours)

**Revision 2 (Round 3).** Includes the owner's Round 3 decisions and the Round 2 role reports. Changes from revision 1 are in §2.3.

| Stage | Producer | Technical Lead | Lead Game Designer | Lead Instructional Designer | Lead Artist (incl. audio) | SME | **Total** | Share |
|---|---|---|---|---|---|---|---|---|
| 1 Pre-production (remaining) | 36 | 116 | 60 | 70 | 40 | 54 | **376** | 6% |
| 2 Prototype | 23 | 180 | 40 | 20 | 34 | 14 | **311** | 5% |
| 3 Vertical slice | 52 | 590 | 190 | 148 | 272 | 52 | **1,304** | 21% |
| 4 Production | 114 | 1,184 | 440 | 384 | 624 | 124 | **2,870** | 46% |
| 5 Alpha | 32 | 232 | 80 | 44 | 40 | 36 | **464** | 8% |
| 6 Beta | 42 | 160 | 120 | 130 | 60 | 30 | **542** | 9% |
| 7 Release | 62 | 80 | 40 | 60 | 60 | 10 | **312** | 5% |
| **Total** | **361** | **2,542** | **970** | **856** | **1,130** | **320** | **6,179** | 100% |
| Share | 6% | 41% | 16% | 14% | 18% | 5% | | |

**What this says**
- **The Technical Lead carries 41%** of all effort, almost all of it the companion mod (risk R-02). The in-house machine set is now about 21 machines (DESIGN.md), all ours to build.
- **Stage 4 is almost half the project** (46%). It is where cuts bite (section 4).
- **The Lead Artist's share rose to 18%:** audio production for the 99-row sound list, three original pixel fonts, and one original ending music piece.
- **Calendar view (arithmetic only):** at 60 team-hours a week, ~103 weeks; at 210 a week, ~29 weeks.

### 2.1 Where the big rows go

**Technical Lead, Stage 3 (590 h):** Mars dimension and worldgen 80 · oxygen, sealing, suits (custom) 120 · storms, radiation, wear, perchlorate dust-off 80 · Regolith Outgasser, Ovens, polar frost 24 · Planetary Monitor with next-gate display 40 · gates, holds, live conditions 30 · journal, forecasts (lock points), failure reports, Keeper's Record skeleton 80 · action log, pseudonymous IDs, CSV export 40 · story queue 30 · Landing onboarding hooks 24 · ambient-sound hook 12 · **`guidance` setting, three levels, logged and exported 30**.

**Technical Lead, Stage 4 (1,184 h):** Phase 2 machines, tubes, outposts 160 · fauna 180 · Phase 3 flora, seed kits, O2/CO2 machines, scrubbers 140 · teacher tools 100 · shrouds, lures, quiet zones 80 · ice rule (boiling-point comparison) and two-source chunk heat cache 60 · ground collapse and survey scan 60 · sky, fog, clouds 60 · beacon, ending cards, epilogue 50 · perchlorate meltwater and soil 40 · drift events 40 · Shift Board 40 · Breathable live check and gauge 30 · fading and hint escalation 30 · accessibility and performance hooks 40 · **Mekanism and Create hidden until Hydrosphere, hidden in the classroom preset 20** (was 40 for deeper hooks) · **orbital mirror route (no ground signature; F5 trigger) 12** · **ice–albedo capped at about 2 °C plus polar CO2 release 12** · **guidance, Phase 2–3 content 10** · **`server-setup.md` 16** · **mast CO2 gauge fitted at Phase 3 4**.

**Lead Artist, Stage 3 (272 h):** Phase 1 art ~170 · **three original pixel fonts (Cairn Display, Text, Mono) with glyph checklist 32** (the Artist's estimate: 3–5 days) · Phase 1 sound production ~70.

**Lead Artist, Stage 4 (624 h):** remaining art rows ~400 · sound production for the rest of the sound list ~160 · **one original ending music piece 32** · ending cards and beacon sequence ~32.

**Sound production (owner of the hours: Lead Artist).** The Artist's report says no one owns the production hours. This budget assigns them to the Lead Artist (who owns audio). 93 sounds to make (84 spec'd plus 9 needed; 3 are on the cut list) at about 2 h each, recorded or synthesized, including subtitles and the pre-filtered thin, mid, and full ambient beds: **~230 h** across Stages 3–4. That is a Producer estimate, not the Artist's.

### 2.2 Scope added in Round 2 (already inside the totals)

| Item | Stage | Hours | Requested by |
|---|---|---|---|
| Regolith Outgasser, Ovens, polar frost | 3 | 42 | Lead Game Designer (C-01 fix) |
| Perchlorate: Phase 1 dust-off / meltwater and soil | 3 / 4 | 24 / 64 | Lead Game Designer |
| Ground collapse with survey scan | 4 | 86 | Lead Game Designer |
| Shrouds, lures, quiet zones | 4 | 124 | Lead Game Designer (user decision) |
| Drift events | 4 | 64 | Lead Game Designer (M-16 fix) |
| Shift Board | 4 | 60 | Lead Game Designer |
| Ending cards | 4 | 58 | Lead Game Designer (user decision) |
| Weather mast | 3 | 26 | Lead Artist (user decision) |
| Audio, first estimate | 3–4 | 176 | User decision (Artist owns audio) |
| Landing onboarding | 3 | 68 | DESIGN.md gap |
| Simple in-house machines | 3–4 | 120 | DESIGN.md gap |
| Fading and worked examples | 3–4 | 70 | DESIGN.md gap |
| Next-gate goal display | 3 | 22 | DESIGN.md gap |
| Shift Briefings, 3 | 3–4 | 26 | DESIGN.md gap |
| Load test, 4 planets × 4 players | 3 | 24 | Technical Lead |
| Pause GameTests | 2 | 16 | Technical Lead |
| Full-team story review | before 3 | 24 | DESIGN.md Story review |
| Scheduled debates | all | 72 | TEAM.md §4 |
| Maintenance plan and user-generated content guide | 7 | 32 | Coordinator |
| Delayed post-test (now decided) | 6 | 14 | Lead Instructional Designer; owner decision Round 3 |
| Java-only note and paper reflection fallback | 4 | 6 | Lead Instructional Designer |
| **Total Round 2** | | **~1,220** | |

### 2.3 Scope changed in Round 3 (revision 1 → revision 2)

| Item | Stage | Change (h) | Source |
|---|---|---|---|
| `guidance` setting (gentle / standard / expert; four conditions from the Instructional Designer) | 3–5 | +76 (TL 48, GD 16, ID 12) | Owner decision |
| Orbital mirror route (no ground signature) | 4 | +20 (TL 12, GD 8); the mirror itself is now committed, not cuttable | Owner decision |
| Ice–albedo a+b (cap ~2 °C, polar CO2 release) | 4 | +22 (TL 12, GD 6, SME 4) | Owner decision |
| Mast CO2 gauge fitted at Phase 3 | 4 | +4 | Owner decision |
| Mekanism and Create optional and late | 4 | −20 | Owner decision |
| Basic CI for test layers L0–L2 at Stage 1 exit | 1 | +16 | Owner decision |
| Three external reviewers before Stage 3: SME review packs and folding in replies | 1, 3 | +40 (SME 36, P 4) | Owner decision |
| Stage 5 external review (kept; see note) | 5 | +16 (SME) | Owner decision, Round 2 |
| Offline LMS converter (never ships) | 5 | +28 (TL 24, ID 4) | Owner decision |
| `docs/tech/server-setup.md` | 4 | +16 | Technical Lead |
| Owner platform checklist at each stage exit (Producer prep) | 1–7 | +14 | Technical Lead |
| Stage 2 face-validity review (owner and SME) | 2 | +9 (SME 4, A 4, P 1) | Lead Artist; Chapter 9 |
| Three original pixel fonts | 3 | +32 | Lead Artist |
| Sound production re-estimate (99-row list) | 3–4 | +52 | Lead Artist; Producer estimate |
| One original ending music piece | 4 | +32 | Proposed default (Decision Log) |
| Perseverance recording license check (journal only, labeled REAL) | 4 | +2 | Proposed default (Decision Log) |
| **Net change** | | **+359 (≈ +6%)** | |

**Note on external reviews.** The owner decided one external review at Stage 5 (Round 2) and three targeted reviewers before Stage 3 (Round 3). The budget keeps both: the early three check the riskiest verdicts (ice rule, ice–albedo and polar frost, transfer key and distractors); the Stage 5 review checks the whole journal and educator guide. If the owner meant the early three to replace the Stage 5 review, remove 16 h.

## 3. The owner's time budget

The owner is the only human playtester and the only approver (risks R-17, R-27).

| Stage | What the owner does | Owner hours | Decision batches |
|---|---|---|---|
| 1 | Find three external reviewers (owner-actions.md); platform checklist | ~5 | 1 |
| 2 | Sessions P1, P2; **face-validity review** of concept sketches with the SME; platform checklist | ~4.5 | 1 |
| 3 | S1, S2, one re-run; learner survey as self and as Lens A and B; platform checklist | ~17.5 | 1–2 |
| 4 | Phase 2 and Phase 3 checks with debug shortcuts; two fauna runs; platform checklist | ~16 | 2 |
| 5 | **Full classroom-preset run plus standard spot checks** (decided; not the full 40 h); arrange the external review; platform checklist | ~21 | 1 |
| 6 | Tuning re-runs; unit walkthrough as the teacher; platform checklist | ~21 | 1 |
| 7 | Release smoke test; platform checklist | ~4 | 1 |
| **Total** | | **~89** | **7–8** |

## 4. Scope check: is the plan still achievable?

**Short answer (revision 2):** the **plan through Stage 3 is achievable** as written. **Stage 4 is at the edge, and slightly worse than in revision 1:** the total grew 6% while the cut list shrank (the mirror and the third-party decision left it). All cuts together now cover only about a 15% overrun of Stage 4. So if the Stage 3 measurement shows Stage 4 more than ~15% over, cuts alone aren't enough and the owner needs a scope call.

### 4.1 What changed since revision 1
- Owner decisions added committed scope: guidance setting, mirror route, ice–albedo loop, LMS converter, CI, external reviews.
- The Lead Artist's report put real numbers on fonts, sounds, and music.
- Two items left the cut list because the owner decided them (orbital mirrors, the Mekanism and Create depth). Ice-comet impacts stay on it.
- One decision saved hours: Mekanism and Create optional and late (−20 h).

### 4.2 Options

| Option | What | Effect on instructional value | Effect on market appeal | Schedule | Recommendation |
|---|---|---|---|---|---|
| A. Keep everything | 6,179 h | Full | Full | Highest risk of slip; no buffer | No |
| **B. Commit through Stage 3, then cut by T-C order** | Build Stage 3 as planned. Take cuts 1–3 now (no goal impact). Re-estimate Stage 4 from measured rates. Over budget by up to 10%: cuts 4–9. 10–15%: add cuts 10–11 (Core reductions). Over 15%: owner scope call (prepared now, see below) | Protects every Primary goal | Loses spectacle first, Core fauna content last | Decides with evidence | **Yes** |
| C. Cut deep now | Take all cuts before Stage 3 (~435 h) | Same as B at worst case | Loses more appeal than needed | Safest | No: cuts before evidence |

**Prepare now, decide after Stage 3 (ill-structured, owner):** if Stage 4 is more than 15% over, the next lever is release scope. Candidates the team should frame before the Stage 3 debrief: ship two of the four biome seed kits and add the rest in an update; or ship standard mode tuned for Phases 1–2 and classroom mode complete. Both touch appeal; neither may remove a Primary goal's teaching, feedback, or measure.

### 4.3 Cut list (ordered)

**Order rule (option T-C, decided by the owner):** two orders, one for each kind of item.
- **Learning content** (journal depth, forecasts, optional beats, reflection prompts) is ordered by **goal tier**: Can be deemphasized (A4, D1) first, down to each tier's minimum; then Secondary, down to one teaching, one feedback, one individual measure each. Primary goals are never reduced (LEARNING.md §1). The Instructional Designer will block any cut that drops forecasts below 3 per phase.
- **Features** (machines, mechanics, effects, art, media) are ordered by **fun–learning quadrant**: items with no unique goal and Moderation-quadrant items first; then Redesign-quadrant items; **Core-quadrant features last**, even when the goal they carry is deemphasized.
- Never cut a **Primary** goal's only teaching, feedback, or measure (DOCUMENTATION.md §3).
- **Any cut that takes a goal below its tier's minimum is a subset decision for the owner with the full team** (consensus brief §1.7), never one role's call.

| # | Cut | Ordered by | Saves (h) | Goals affected (tier) | Fun–learning quadrant | Appeal cost | Confirm with |
|---|---|---|---|---|---|---|---|
| 1 | Ice-comet impacts (the orbital mirror route stays: owner decision) | Quadrant | ~40 | None unique: A2 via extractors | Moderation | Low-medium: spectacle | Lead Game Designer |
| 2 | Trailer and cinematic ending → credits; ending cards and the ending music piece stay | Quadrant | ~60 | None | Moderation | Medium: marketing | Lead Artist |
| 3 | Phase music pieces stay unbuilt (only the ending piece is budgeted); Perseverance recording dropped if its license can't be verified | Quadrant | 0 in budget | None | Moderation | Low | Lead Artist |
| 4 | Clouds and heat shimmer (keep water, frost, flame, mast) | Quadrant | ~40 | None unique | Moderation | Low-medium; helps school hardware (R-08) | Lead Artist, Technical Lead |
| 5 | Secondary sounds made as simple synthesized versions, not recorded and layered | Quadrant | ~40 | None (every sound keeps its subtitle and visual twin) | Moderation | Low-medium | Lead Artist |
| 6 | Perchlorate soil stage (Phase 3) | Quadrant | ~25 | C1, C2 (Secondary) | Redesign (chore risk) | None or positive | Lead Game Designer, SME |
| 7 | Optional beats for deemphasized goals: beats 11 and 16 (D1) and forecast F7 (A4 repeat) become archive lines | Tier | ~30 | A4, D1: keep Report 2 question, final reflection, F2 | n/a | Low | Lead Instructional Designer, Lead Game Designer |
| 8 | Perchlorate meltwater stage (Phase 2); Phase 1 dust-off stays | Quadrant | ~40 | C2 (Secondary); D2 still carried by ground collapse and Earth links | Redesign | None | Lead Game Designer, SME |
| 9 | Merge two pairs of in-house machines (e.g. ice extractor with deep-crust extractor, which also fixes a naming drift; O2 generator with water reclaimer) | Quadrant | ~40 | None, if each merged machine keeps its "Effect on planet" line | n/a (infrastructure) | Low | Lead Game Designer, Technical Lead |
| 10 | Carbon reservoirs flow view → Monitor cause lines only | Quadrant (Primary guard) | ~20 | A5, B2 (Primary) keep cause lines and forecasts | Redesign | None | Lead Game Designer, Lead Instructional Designer |
| 11 | Lures (keep shrouds and quiet zones) | Quadrant (Core, weakest member) | ~40 | B2, B4, D1 still carried by shrouds and quiet zones | Core set | Low | Lead Game Designer |
| 12 | One later life stage per fauna family only | Quadrant (Core, extras only) | ~60 | B5 (Secondary) carried by the first stage | Core (extras only) | Low-medium | Lead Game Designer |
| 13 | Voice audio for BELL stays deferred (text-first) | Tier (media) | 0 in budget | Revisit after Stage 3 | n/a | Low | Producer, Lead Game Designer |
| | **Total** | | **~435** (15% of Stage 4) | | | | |

**Left the cut list by owner decision:** orbital mirrors (now a route choice, B4); Mekanism and Create depth (now optional and late, unlocked at Hydrosphere). **Deferred, not cut:** the "Veteran" hobbyist setting (revisit after Stage 3; 0 h in budget).

**Stop line: never cut** (each is a Primary goal's carrier or a must-pass check): Planetary Index and Monitor with cause lines and next-gate display (B1, C3); Regolith Outgasser, processors, emitters, and the mirror route (A3, B1, B4); the ice rule (A2); storms, polar frost, fauna by machine signature (B2); Phase 3 O2/CO2 balancing, drift events, Breathable gauge (A5, B4); the forecast system (at least 3 forecasts per phase); Earth links, Keeper's Reports, transfer task (D2); ground collapse; shrouds, quiet zones, ending cards; the `guidance` setting's gentle level (classroom default); accessibility (subtitles, fonts with checked glyphs); privacy.

## 5. After release (not in the totals)

| Item | Estimate | Note |
|---|---|---|
| Support window | ~8 h/month for 12 months (proposal) | Basis: the Technical Lead's estimate of about 1 day per NeoForge 21.1.x version bump plus teacher setup questions (**unverified**: bump frequency unknown), plus bug fixes and educator questions |
| Minecraft or NeoForge version port | 120–300 h per port (very rough) | 1.21.1 is ageing (R-06); a separate scope decision |
| LMS file export (opt-in) | ~60–100 h | Post-release; the Stage 5 offline converter reduces the unknowns; privacy review first (R-22) |
| "Veteran" setting | ~20–30 h | Deferred until after Stage 3; only if Lens B playtests ask for it |

## 6. Change log

| Date | Change |
|---|---|
| 2026-11-26 | First issue (Round 2): effort budget, user-time budget, scope check, cut list ordered by proposed goal tiers. |
| 2026-11-26 | Cut order rewritten to option T-C (owner decision): tier orders learning content, quadrant orders features; subset decisions go to the owner. Licensing plan noted (no budget effect). |
| 2026-11-26 | Revision 2 (Round 3): owner decisions and Round 2 reports (guidance, mirror route, ice–albedo, CI, external reviews, LMS converter, server-setup, platform checklists, face-validity review, fonts, sound production, ending music). Total 5,820 → 6,179 h; owner time 75 → 89 h; cut list re-ordered (13 items, ~435 h); scope verdict: Stage 4 buffer is now ~15%. |
