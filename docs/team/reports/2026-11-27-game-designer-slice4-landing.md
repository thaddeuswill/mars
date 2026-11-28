# Game Designer report: Stage 3 slice 4 (Landing, Shift Briefing 1, journal), 2026-11-27

Type of work: design desk work, docs only, no Java or pack changes, no game run, no playtest (walkthroughs as a 12-year-old and a 17-year-old are in the spec, section 8). Spec: `docs/design/landing-spec.md`.

## 0. Handoffs received
| From | Handoff | Answer |
|---|---|---|
| Instructional Designer (monitor wording report) | (a) rose-sky label trigger; (b) Landing step 6 text; (c) Dome stable "On Earth" trigger and replacement sentence | Accepted, all three decided as **proposed** (spec L3, L4, L5). Item (c) of the ID's list (Next goal row for an unmet temperature condition) and "Useful now" are slice 4 rows but outside this request: **not done**, listed below |
| Technical Lead (slice 2 report) | Landing flow is slice 4; Monitor text straw-man | Accepted. The built grace (sol 1.2) is adopted and replaces dome-layout 5.8 (L2) |

## 1. Done
- `docs/design/landing-spec.md` (new): 9 decisions, the seal kit, the 8-step Landing state machine with trigger, completion, text, hint, skip rule, dome behaviour and events per step, BELL lines, Shift Briefing 1, the journal (format choice, 8 Phase 1 entries with texts, delivery rules), content-rule checks, who builds what, walkthroughs, 3 owner questions, tests.
- `docs/design/messages/phase-1.md`: changed lines marked proposed (steps 2, 5, 6, 7, briefing text, Dome stable Earth line, sky label trigger and verbatim SME text), with a pointer to the spec.
- `docs/design/ui-specs.md` 9.2: Landing step 6 row marked superseded.

### The three open items, as decisions (all proposed)
| Item | Decision |
|---|---|
| Step 6 text | "Place your two Ovens. Check Power on the Monitor." The battery has no on/off state in code (`BatteryBlock`), so "switch on" asked for a missing action. Detection: two Ovens, both powered. The battery stays in the hint and the outline |
| Rose-sky label | Unlock at the `mars:dome_stable` latch or the first Outgasser placement, whichever is first; silent unlock plus one shared chat pointer; repeat at Phase 2 begins; SME text verbatim |
| Dome stable "On Earth" | Trigger: the gate latch, once per planet, to every player, no Record page. Sentence: "On Earth, air pushes on you with more than 100 times the force of the air outside this dome. You never notice, because your body pushes back." "More than 100" instead of "about 100" because 101.3 / 0.636 is about 159 (calc). Second sentence pending SME; fallback given |

### Other findings that change what other roles built or wrote
1. **Grace conflict.** `dome-layout.md` 5.8 says nothing starts "when Landing is done or skipped"; the built code uses the fixed sol 1.2 (`landingEndSol`). I adopted the built behaviour (L2) because the balance-sheet timing needs it; Q2 asks the owner.
2. **"Hab B is leaking" is false** of the simulation: Hab B is not a required volume, so no tank loses air. Step 5 now says "has a crack" (CR-H1).
3. **Suits, dust-off station, weather mast, airlock interlock do not exist**, so steps 2 and 7 have a "now" text and a "later" text switched by data flags.
4. **The Monitor has no "Outside air" line** (only the dome `Air` row). Step 3 asks the player to read it; the Technical Lead must add one or fold it into the pressure row.
5. `en_us.json` `life.blackout` says the player wakes "in your bunk"; code wakes at the shelter spawn. The journal says "in the shelter".
6. **Beat 6 (Hana's relay message) has no text** in `messages/phase-1.md`, but the briefing promises "the relay link comes back". At minimum one BELL log line is needed at the Dome stable latch.
7. The old briefing said "O2" and "no failures"; the gate allows short failures (balance sheet 2c decision 7). Rewritten.

## 2. Decisions needed (owner)
1. **Journal as the vanilla written book for Stage 3?** ill-structured. **Recommend yes** (cheap, per player, server-safe; entry JSON carries to the Stage 4 screen; no pictures or tabs until then).
2. **Landing grace stays at the fixed sol 1.2, or ends when everyone finishes?** well-structured once the sheet is read. **Recommend fixed** (what is built; ending early shifts the Dome stable timing table).
3. **Minimal suit before Landing, or step 2 as an air tank read-out?** ill-structured. **Recommend the read-out now**, suits with slice 6.

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** build the Landing state machine, seal kit, locker, tank read-out, Outside air line, intro screen and panel (or sidebar fallback), journal book and unlock hooks.
> **Context:** `docs/design/landing-spec.md` sections 2, 3, 5, 7.3; the table 3.11 lists seven items T1 to T7.
> **Needed from them:** a re-estimate, the panel choice (HUD layer or sidebar), whether written-book page jumps work on 1.21.1, and one Monitor line for Outside air.
> **Done when:** the tests in spec section 10 pass and each fails when its guard is removed.

> **To:** Codex (`mars/learning`) · **Problem type:** well-structured
> **Problem:** new event kinds `landing.step`, `landing.skipped`, `landing.done`, `briefing.opened`, `journal.opened`; the open hooks (Oven placement, Monitor open, gate latch); a player band setting; the journal entry JSON schema.
> **Context:** spec 3.9, 5.1, 7.1, 7.2. I read only `ActionEvent.java` names and `LearningEvents.java`.
> **Needed from them:** the kinds and the band setting, or a note that the builder sits elsewhere.
> **Done when:** a learning test emits each new kind with a valid subject.

> **To:** Instructional Designer · **Problem type:** well-structured
> **Problem:** grade 6 to 7 pass and CR-T4/T7/T8 on every key in spec 3.8 and 5.3; confirm the briefing storm sentence does not make F1 trivial; confirm "Your hull holds back that difference"; whether "kilopascals" needs more than the inline definition.
> **Done when:** keys final, key ids unchanged.

> **To:** SME · **Problem type:** well-structured
> **Problem:** confirm "your body pushes back" (fallback: "you grew up in it"); the 24 h 40 min sol; the molecule line in the HS pressure read-more; "more than 100 times" (159 by calc).
> **Done when:** each marked verified or replaced in spec 5.3.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** a resource-pack look for the journal book item, the seal kit icon, and (later) the soft outline for Landing targets.
> **Done when:** same item ids, 16 px.

> **To:** Producer · **Problem type:** well-structured
> **Problem:** "Useful now" lists and the Next goal row for an unmet temperature condition are slice 4 rows not covered here; Hana's beat 6 text is missing. I can write both as a short follow-up spec if asked.

## 4. Risks
- First new client UI of Stage 3 (intro screen, panel): not in any estimate yet.
- The Dome stable moment now bundles Outgasser unlock, two journal entries, the relay line and the goal change: one pointer line, but Lens A may still be overloaded.
- Fixed grace means up to 24 minutes of "Waiting: landing" for a fast player (mitigated by building during the grace; first thing to watch in S1).
- The vanilla book is plain; no pictures.
- Not verified: written-book page links on 1.21.1, walking time, reading time, all word counts beyond my script, the grade level.

## 5. Shared model updates
- **Proposed decisions (owning doc `docs/design/landing-spec.md`):** L1 to L9 (Landing per player, any order, nothing fails; grace is the clock sol 1.2; step 6 text; sky label trigger; Dome stable Earth trigger and sentence; vanilla-book journal; honest degrade of steps 2 and 7; Landing patch is not C2 evidence; "has a crack").
- **New term:** **Landing grace** = sol 0 to 1.2, nothing made, used or leaked.
- **Lesson:** a tutorial text that names an action the game does not have (switch on a battery) is caught only by reading the block code; check every step against the built blocks.
- **Lesson:** a message that claims "leaking" must match a real simulation state (CR-H1 applies to onboarding too).
- **Fiction audit row to add (DESIGN.md table):** seal kit, a stand-in sealant patch, no science claim.
