# Game Designer: journal and `guidance` minimum design, 2026-11-27

Type of work: desk design from the docs and a read of `mod/` (gate tracker, PlanetManager, SessionCommands, pack gate files). Nothing built, run, or playtested. Docs only.

## 0. Handoffs received
| From | Handoff | Answer |
|---|---|---|
| Instructional Designer (monitor-wording 4a, 4b, 5 decision 2) | Sky-label and Dome stable journal triggers; CR-T8 needs `guidance` and an open-forecast mark | Accepted. Triggers: both at gate `mars:dome_stable` (derived unlock, section 2 of the design). CR-T8 mechanism designed |
| Technical Lead (slice 4/8 reports, BACKLOG #49) | "CR-T8 and journal not built (no mechanism)" | Accepted: mechanism specified |

## 1. Done
- `docs/design/journal-and-guidance-minimum.md`: what the player sees, JSON shapes (`pack/data/mars/story/journal/*.json`, `pack/data/mars/monitor/lines.json`, forecast `asks_about`), storage (world SavedData of per-UUID `guidance` and `journal_seen`), triggers, who sets `guidance`, left-out list, 10 tests, two options, assumptions.
- Key finding: **journal unlock needs no new state.** Gates lock in, so an entry is unlocked iff its gate is latched. Late joiners work for free.
- Recommended Option A (screen + key + chat pointer + `/mars journal`, about 700 to 900 lines, **unverified**). Cheaper Option B (no screen, chat only, about 350 to 450 lines, **unverified**), which is also A's first commit. Guidance and CR-T8 part is identical in both and should be built first.

## 2. Decisions needed (owner)
1. Option A or B for the journal. Ill-structured. Recommend A (B first if credits are short).
2. May a non-operator set their own `guidance` in standard play (`/mars guidance`)? Recommend yes; classroom mode stays teacher-only, expert refused. Well-structured once chosen.
3. Journal key J (invented) and "Keeper's Journal" screen title: accept? Well-structured.
4. Ship a debug-only `/mars debug forecast open|close` so CR-T8 is provable before real forecasts exist? Recommend yes.

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** build the mechanism in the design doc, guidance and CR-T8 first.
> **Context:** `docs/design/journal-and-guidance-minimum.md` sections 4 to 7; `PlanetManager.announceGates`; `MonitorSender`.
> **Needed from them:** (a) confirm whether the Monitor payload can be filtered per recipient; (b) confirm the real ETA lang key names for `lines.json`; (c) add `mars.learning` to the pure ArchUnit rule; (d) size estimate to replace mine.
> **Done when:** tests 1 to 10 in the design pass and the CR-T8 scan fails on an undeclared estimate key.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** check that hiding without a "hidden" note, and per-player masking, satisfy CR-T8/CR-F9; confirm `reveals` on "Leveling off".
> **Done when:** CR-T8 text matches section 3.2 of the design.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** the "On Earth" sentence ("body pushes back") is still unconfirmed (IDer 4b); the journal will ship it.
> **Done when:** noted in `phase-1.md` section 9.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** journal screen straw-man in Monitor style (list + text pane, unread dot plus the word "new").
> **Done when:** a mockup or note exists; not blocking Option B.

## 4. Risks
- Per-recipient Monitor payload may be a larger change than assumed (unverified).
- Dome stable burst (chat line, unlock, journal pointer, sky entry): kept to one extra line.
- Players who pick expert lose the ETA but keep bar and percent; first playtest should check it is not read as a bug.
- A debug command could be mistaken for learning evidence; it logs no learning event.
- Journal text is `.ms` only until a grade band exists in code.

## 5. Shared model updates
- Decision (proposed, owning doc `docs/design/journal-and-guidance-minimum.md`): journal unlock is derived from latched gates; per-player state is only `guidance` and `journal_seen`.
- Proposed term: **Monitor line declaration** = an entry in `pack/data/mars/monitor/lines.json` giving a line's `reveals` and `hidden_under`.
- Lesson: when a rule says "never deliver X while Y", look for a derived-state design before adding storage; lock-in already gave us the late-joiner behavior.
