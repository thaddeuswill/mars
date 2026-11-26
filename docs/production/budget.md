# Budget: Effort, Scope Check, and Cut List

**Owner:** Producer · **Issued:** 2026-11-26 (Round 2) · **Stage:** 1 (Pre-production) · **Stability:** flexible (re-estimated at every stage gate)
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

| Stage | Producer | Technical Lead | Lead Game Designer | Lead Instructional Designer | Lead Artist (incl. audio) | SME | **Total** | Share |
|---|---|---|---|---|---|---|---|---|
| 1 Pre-production (remaining) | 30 | 100 | 60 | 70 | 40 | 30 | **330** | 6% |
| 2 Prototype | 20 | 180 | 40 | 20 | 30 | 10 | **300** | 5% |
| 3 Vertical slice | 50 | 560 | 180 | 140 | 220 | 40 | **1,190** | 20% |
| 4 Production | 110 | 1,150 | 420 | 380 | 560 | 120 | **2,740** | 47% |
| 5 Alpha | 30 | 200 | 80 | 40 | 40 | 20 | **410** | 7% |
| 6 Beta | 40 | 160 | 120 | 130 | 60 | 30 | **540** | 9% |
| 7 Release | 60 | 80 | 40 | 60 | 60 | 10 | **310** | 5% |
| **Total** | **340** | **2,430** | **940** | **840** | **1,010** | **260** | **5,820** | 100% |
| Share | 6% | 42% | 16% | 14% | 17% | 4% | | |

**What this says**
- **The Technical Lead carries 42%** of all effort, almost all of it the companion mod (risk R-02). It is the critical path in every build stage.
- **Stage 4 is almost half the project.** It is where cuts bite (section 4).
- **The Lead Artist's share rose** when audio moved to the role (user decision): about 175 h of audio work is now in the Artist column.
- **Calendar view (arithmetic only):** at 60 team-hours a week (six people part-time), ~97 weeks; at 210 a week (six full-time), ~28 weeks.

### 2.1 Where the big rows go

**Technical Lead, Stage 3 (560 h):** Mars dimension and worldgen 80 · oxygen, sealing, suits (custom, still open) 120 · storms, radiation, wear, perchlorate dust-off 80 · Regolith Outgasser, Ovens, polar frost 24 · Planetary Monitor with next-gate display 40 · gates, holds, live conditions 30 · journal, forecasts, failure reports, Keeper's Record skeleton 80 · action log, pseudonymous IDs, CSV export 40 · story queue 30 · Landing onboarding hooks 24 · ambient-sound hook 12.

**Technical Lead, Stage 4 (1,150 h):** Phase 2 machines, tubes, outposts 160 · fauna (mobs, life stages, signature, caps) 180 · Phase 3 flora, seed kits, O2/CO2 machines, scrubbers 140 · teacher tools (overview, rollback, set phase, per-planet pacing) 100 · shrouds, lures, quiet zones 80 · ice-to-water rule and chunk heat map 60 · ground collapse and survey scan 60 · sky, fog, clouds 60 · beacon, ending cards, epilogue 50 · perchlorate meltwater and soil 40 · drift events 40 · Shift Board 40 · KubeJS presets and third-party hooks 40 · accessibility and performance hooks 40 · Breathable live check and gauge 30 · fading and hint escalation 30.

**Lead Artist, Stage 4 (560 h):** the remaining ~120 rows of the asset list (machines, flora, fauna models and animation, props, UI) ~400 · audio sets AU-05 to AU-12 ~130 · ending cards and beacon sequence ~30.

### 2.2 Scope added since the consistency review (already inside the totals)

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
| Audio, 12 sets, incl. pressure-driven ambience (AU-01) | 3–4 | 176 | User decision (Artist owns audio); DESIGN.md gap |
| Landing onboarding (controls first) | 3 | 68 | DESIGN.md gap |
| Simple in-house machines for learning-critical work | 3–4 | 120 | DESIGN.md gap |
| Fading and worked examples | 3–4 | 70 | DESIGN.md gap |
| Next-gate goal display | 3 | 22 | DESIGN.md gap |
| Shift Briefings (advance organizers), 3 | 3–4 | 26 | DESIGN.md gap |
| Load test, 4 planets × 4 players | 3 | 24 | Technical Lead |
| Pause GameTests for third-party machines | 2 | 16 | Technical Lead |
| Full-team story review | before 3 | 24 | DESIGN.md Story review |
| Scheduled debates (perspective rounds, all stages) | all | 72 | TEAM.md §4 |
| Maintenance plan and user-generated content guide | 7 | 32 | Coordinator (Fanning) |
| Optional delayed post-test | 6 | 14 | Lead Instructional Designer |
| Java-only note and paper reflection fallback | 4 | 6 | Lead Instructional Designer |
| **Total added** | | **~1,220** | **about 21% of the budget** |

## 3. The user's time budget

The user is the only human playtester and the only approver (risks R-17, R-27). This is the tighter budget.

| Stage | Sessions (playtest-plan.md) | User play hours | Decision batches |
|---|---|---|---|
| 2 | P1, P2 | ~2 | 1 |
| 3 | S1 (standard Phase 1), S2 (classroom config), one re-run after fixes | ~15 | 1–2 |
| 4 | Phase 2 and Phase 3 checks with debug shortcuts; two fauna runs (lethal only, non-lethal) | ~15 | 2 |
| 5 | Full run in the classroom preset (~11.3 h) plus spot checks of standard mode | ~20 | 1 |
| 6 | Tuning re-runs; unit plan walkthrough playing the teacher | ~20 | 1 |
| 7 | Release smoke test | ~3 | 1 |
| **Total** | | **~75** | **7–8** |

A full 40-hour standard run in Stage 5 would raise the total to ~95 h. **Recommendation:** don't require it. Measure Phase 1 at standard pace in Stage 3 and check the other phases' curves with debug shortcuts; agents walk through the rest.

## 4. Scope check: is the plan still achievable?

**Short answer:** the **plan through Stage 3 is achievable** as written. **Stage 4 as written is at the edge**: it is 47% of the effort, carries most of this round's added scope, and depends on a companion mod whose size we haven't measured. That is the ill-structured part (it trades appeal and learning against cost), so section 4.2 gives options, not one answer.

### 4.1 What changed

- Scope grew ~21% this round. Every addition traces to a goal ID or a pillar, so none of it is padding. But it lands on the two most loaded columns (Technical Lead, Lead Artist) in the most loaded stage.
- Two blockers closed on paper (C-01, C-02). Several risks fell (R-07, R-13, R-14, R-24). See the risk register.
- New learning infrastructure (fading, worked examples, Shift Briefings, Landing) is cheap per item but spreads across four roles. It needs one owner per item (ROADMAP lists them).

### 4.2 Options

| Option | What | Effect on instructional value | Effect on market appeal | Schedule | Recommendation |
|---|---|---|---|---|---|
| A. Keep everything | 5,820 h | Full | Full | Highest risk of slip; no buffer | No |
| **B. Commit through Stage 3, then cut by T-C order** | Build Stage 3 as planned. Take cuts 1–2 now (no goal impact). Re-estimate Stage 4 from measured rates. If Stage 4 is over budget by more than 15%, take cuts 3–7 in order (no-unique-goal, Redesign, and tier items); over 30%, go to cuts 8–10 (Core reductions and the owner's third-party call) | Protects every Primary goal; Secondary goals keep one teaching, one feedback, one measure | Loses spectacle first (mirrors, comets, trailer, clouds), Core fauna content last | Keeps the release date honest; decides with evidence | **Yes** |
| C. Cut deep now | Take cuts 1–11 before Stage 3 (~505 h, ~9%) | Same as B at worst case | Loses more appeal than needed if Stage 3 shows we can afford it | Safest | No: cuts before evidence, and still doesn't remove the main uncertainty (the mod's size) |

**Note:** even all eleven cuts save only ~505 h, about 18% of Stage 4. If the Stage 3 measurement shows Stage 4 at double the estimate, cuts aren't enough. The next lever would be an ill-structured scope call for the user (for example, release with a smaller Phase 3 content set and add biomes in an update). That isn't proposed now.

### 4.3 Cut list (ordered)

**Order rule (option T-C, decided by the owner):** two orders, one for each kind of item.
- **Learning content** (journal depth, forecasts, optional beats, reflection prompts) is ordered by **goal tier**: Can be deemphasized (A4, D1) first, down to each tier's minimum; then Secondary, down to one teaching, one feedback, one individual measure each. Primary goals are never reduced (LEARNING.md §1).
- **Features** (machines, mechanics, effects, art, media) are ordered by **fun–learning quadrant**: items with no unique goal and Moderation-quadrant items first; then Redesign-quadrant items; **Core-quadrant features last**, even when the goal they carry is deemphasized (e.g. fauna options and ending cards carry D1 but are Core, so they are protected).
- Never cut a **Primary** goal's only teaching, feedback, or measure (DOCUMENTATION.md §3).
- **Any cut that takes a goal below its tier's minimum is a subset decision for the owner with the full team** (ACEDT "broad first, then drill down"; consensus brief §1.7), never one role's call.

The list below interleaves the two orders by hours saved and risk. Column "Ordered by" says which rule places each item.

| # | Cut | Ordered by | Saves (h) | Goals affected (tier) | Fun–learning quadrant | Appeal cost | Confirm with |
|---|---|---|---|---|---|---|---|
| 1 | Orbital mirrors and ice-comet impacts | Quadrant | ~120 | None unique: A3 via emitters, A2 via extractors | Moderation (high fun, low added learning) | Medium: spectacle | Lead Game Designer |
| 2 | Trailer and cinematic ending → credits; ending cards stay | Quadrant | ~60 | None | Moderation | Medium: marketing | Lead Artist |
| 3 | Clouds and heat shimmer (keep water, frost, flame, mast) | Quadrant | ~40 | None unique; hydrosphere stays visible through water | Moderation | Low-medium; helps school hardware (R-08) | Lead Artist, Technical Lead |
| 4 | Perchlorate soil stage (Phase 3) | Quadrant | ~25 | C1, C2 (Secondary) | Redesign (chore risk) | None or positive | Lead Game Designer, SME |
| 5 | Optional beats for deemphasized goals: beats 11 and 16 (D1) and forecast F7 (A4 repeat) become archive lines | Tier | ~30 | A4, D1 (Can be deemphasized): keep Report 2 question, final reflection, F2 | n/a (content depth) | Low | Lead Instructional Designer, Lead Game Designer |
| 6 | Perchlorate meltwater stage (Phase 2); Phase 1 dust-off stays | Quadrant | ~40 | C2 (Secondary); D2 still carried by ground collapse and Earth links | Redesign | None | Lead Game Designer, SME |
| 7 | Carbon reservoirs flow view → Monitor cause lines only | Quadrant (Primary guard) | ~20 | A5, B2 (Primary) keep cause lines and forecasts | Redesign | None | Lead Game Designer, Lead Instructional Designer |
| 8 | Lures (keep shrouds and quiet zones) | Quadrant (Core, weakest member) | ~40 | B2, B4, D1 still carried by shrouds and quiet zones | Core set; lures are its weakest member (Lead Game Designer's own cut order) | Low | Lead Game Designer |
| 9 | One later life stage per fauna family only | Quadrant (Core, extras only) | ~60 | B5 (Secondary) carried by the first stage | Core (only extra variants cut) | Low-medium | Lead Game Designer |
| 10 | Third-party mods (Mekanism, Create) reduced to optional, minimal recipe hooks | Quadrant (owner call) | ~40–100 | None: learning-critical machines are in-house (DESIGN.md) | n/a | **Medium-high for tech-pack fans.** Ill-structured; user call | Lead Game Designer, Technical Lead, user |
| 11 | Voice audio for BELL stays deferred (already text-first) | Tier (media) | 0 in budget | Narration research favors voice; revisit after Stage 3 | n/a | Low | Producer, Lead Game Designer |

**Stop line: never cut** (each is a Primary goal's carrier or a must-pass check): Planetary Index and Monitor with cause lines and next-gate display (B1, C3); Regolith Outgasser, processors, emitters (A3, B1); ice-to-water rule (A2); storms, polar frost, fauna by machine signature (B2); Phase 3 O2/CO2 balancing, drift events, Breathable gauge (A5, B4); the forecast system (Primary infrastructure, LEARNING.md §1 B3 note); Earth links, Keeper's Reports, transfer task (D2); ground collapse (Core; B5 and the permafrost D2 link); shrouds, quiet zones, and ending cards (Core; without them D1 is hollow); accessibility; privacy.

## 5. After release (not in the totals)

| Item | Estimate | Note |
|---|---|---|
| Support window | ~8 h/month for 12 months (proposal) | Bug fixes, mod updates, educator questions. See ROADMAP "After Release" |
| Minecraft or NeoForge version port | 120–300 h per port (very rough) | 1.21.1 is ageing (R-06); a port is a separate scope decision |
| LMS file export (opt-in) | ~60–100 h | Post-release; needs a privacy review first (R-22) |

## 6. Change log

| Date | Change |
|---|---|
| 2026-11-26 | First issue (Round 2): effort budget, user-time budget, scope check, cut list ordered by proposed goal tiers. |
| 2026-11-26 | Cut order rewritten to option T-C (owner decision): tier orders learning content, quadrant orders features; subset decisions go to the owner. Licensing plan noted (no budget effect). |
