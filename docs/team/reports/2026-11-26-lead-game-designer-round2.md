# Report: Lead Game Designer Round 2

- **Role:** Lead Game Designer
- **Date:** 2026-11-26
- **Outputs:** `DESIGN.md` (new sections: Flow and Task Design, Motivation and Player Types, Core Design Check, Content Lists, Content Inventory; Round 2 decision table), `docs/design/story-bible.md` (draft 3), `docs/design/messages/` (new: README, phase-1, phase-2, phase-3, failure-reports), `docs/design/ui-specs.md` (new), `docs/design/balance-sheet.md`, `docs/design/playtest-plan.md`
- **Perspective round:** Succeeds if a 12-year-old always knows what to do next and why the planet changed, and every learning moment feels like part of the colony job rather than homework.
- **Note:** Design and writing plus walkthroughs (a 12-year-old new to tech modpacks; an experienced 17-year-old). Nothing was playtested.

## Handoffs received
- **Instructional Designer:** story bible classroom details fixed (no threshold is multiplied; debriefs in periods 6, 14, 21 of the 23-period unit with 17 play periods); "Explain it" is now **"What caused it?"**; gauge, radiation warning, and consensus input done.
- **Subject Matter Expert (accepted, one partial push-back):** boiling-point ice rule; polar frost wording (with an optional reversible reservoir); carbon-neutral wording; invented-number labels; story and sound line fixes; mast instrument list; forecast distractor fixes and approved Earth links. **Ice–albedo: adopted a+b, no block** (capped at about 2 °C, invented; the speed-up comes from the real polar CO2 release). **Push-back:** the "about 1.1 °C" Earth-warming figure is left out of Mensah's text because the SME's own audit says it isn't sourced yet.
- **Technical Lead:** storm caps scale both gameplay and look; next-gate bar uses a log axis for pressure; `asserts` on story beats; banned-token lists (CR-H2); the "Don't write your name" note; Training Hall lobby; mast gas gauges fitted at Phase 3 (pending Artist and Instructional Designer).
- **Producer:** the Keeper's Report fix confirmed: one log line plus a side-panel entry, opened full screen only by the player's choice or at the next natural pause. Done: weather mast in DESIGN.md, Phase 2–3 curves, content inventory, "pseudonymous" in Group Play, in-house machine list, Stage 2a paper prototypes.
- **Owner decisions (T-C, I-C), Chapter 6, Chapter 9, PROCESS.md:** applied.

## Done (highlights)
- **Story bible:** §3.6 **Training Hall** lobby (on Earth, before the story timeline); §4.4 character-load check (Phase 1 had 5 essential voices; now BELL plus one crew voice per phase: Hana, then Basu, then Moreno; Mensah speaks only in report replies); `asserts` table; Landing, worked example, and Shift Briefings in the beat table.
- **Messages:** forecasts F1–F10 (F4a and F4b) and Earth links in the shared format, middle and high school versions; 11 failure types with hint ladders, positive closing lines, and banned tokens. Reading screen: essential story text written so far is 452 words, ten essential beats are still unwritten (budgeted ≤ 60 words each, about 1,050 words total against a 1,500 budget). The readability grade of about 3 is a screen, not proof.
- **UI specs:** message delivery under I-C; Monitor layout; progressive disclosure (2 → 4 → 6 planet rows); next-gate bar with a log axis, one bar per condition, a sol estimate, and a "Useful now" list of up to 3 builds; solar warning at T−1 sol with the forecast locking at T−0.25 sol; Air window in all four pressure states (zone label "Too thin to breathe" renamed **"Too little oxygen"**); weather mast accepted; quiet-zone marker accepted (the zone works without it); thawling defense accepted.
- **DESIGN.md:** sleep never skips Mars time and the sky follows simulation time; in-house machines for all learning-critical work (Mekanism and Create optional, unlocked at Hydrosphere); threshold-vs-curve table (found the hidden triple-point regime and an arithmetic error in balance §3, fixed to 1.8 kPa per sol, not 1.1); an orbital mirror route (warms without a ground signature, expensive); Content Lists (4 crops, 4 biome kits as a succession chain, 4 maintenance parts, 5 beacon component types); first experience to design in detail: the first dust storm.
- **Playtest plan:** separate functionality and instructional-effectiveness passes; one scenario per goal ID; flow and motivation checks; observation prompts; caution about non-representative testers; root-cause sheet.

## Consensus positions (TEAM.md §9)
- **Goal tiers T-C: Agree.** The quadrant rule protects Core fauna features. D1 stays deemphasized for learning resources; no block.
- **Interruption rules I-C:** agree with rules 1, 2, and 5–11; **rule 3 (blocking, deferrable debrief): can live with it** (in our UI the report is never modal); **rule 4 (caps): agree**. Two additions: the gate sequence (report → reply → briefing) counts as one pop-up; a forecast locks before any message that gives its answer away.

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| A `guidance` setting (gentle / standard / expert): difficulty shown as information; thresholds never change; gentle is the classroom default | Yes | Ill-structured |
| The orbital mirror route (warming with no fauna signature, expensive) | Yes: a real B4 route choice, cheaply | Ill-structured |

## Handoffs
- **Instructional Designer:** alignment check on F1, F2, F4a, F6, F7, F9 wording; F4a now fires below 5 kPa and F7 at 5 kPa or above; F2 and F7 lock at T−0.25 sol; F3, F5, F8 lock when the Monitor is opened; Mensah essential only in report replies; the `guidance` setting; "Useful now"; the Air window label; update `prompts-and-reflections.md` §1 (it still has the old F4a and F7); agree or object to mast gauges at Phase 3; read the "What caused it?" sub-line; the four-question proposal.
- **Lead Artist:** zone labels; mast instruments; quiet-zone ring; "Useful now" icon; HUD icon variants for dark backings; a pixel font with checked glyphs; assets F-07, F-08, I-01, I-07; a joint pass on the first dust storm.
- **Technical Lead:** no sleep skip, `/time set` has no effect, sky follows simulation time; hide Mekanism and Create until Hydrosphere; cost the in-house machine list and the orbital mirror; fit the Training Hall lobby.
- **Subject Matter Expert (ill-structured):** the Phase 3 CO2 budget is undefined. If gas added by Outgassers and processors is CO2, Phase 3 must remove tens of kPa to reach ≤ 0.5 kPa. One option: processors release mostly untracked filler gas plus some CO2, with a journal-labeled composition rule. Also verify the crop list, biome succession chain, mirror framing, the Earth-mean-temperature line (Training Hall), the greenhouse read-more, and source "about 1.1 °C".
- **Producer:** add `ui-specs.md` and `messages/` to the DOCUMENTATION.md map; decide whether the four Chapter 9 questions go into TEAM.md 8.2; schedule the Stage 2a paper prototypes.

## Risks
- Phase 3 CO2 is unmodeled: it could become a grind or a false climb.
- The Phase 2 story stays the thinnest per hour.
- The in-house machine set (about 20 machines) is large for scope.
- The Air window's "not thick enough yet" state lasts only about 3 sols.
- Moreno's apology is now optional, which may weaken the Phase 2 emotional beat.
- Ten essential story beats still need final text.
- The instructional designer's spec and these messages disagree until `prompts-and-reflections.md` §1 is updated.

## Shared model updates
- **Terms:** "What caused it?" (in-game name of the old "Explain it" check); Shift Briefing; Landing (first-shift checklist); Training Hall (class lobby); "Useful now" list; side panel vs. top line vs. full-screen sheet; `asserts` (a beat's world-claim condition); banned hint tokens; lead crew voice per phase.
- **Decisions:** sleep never skips Mars time; storm caps scale gameplay as well as look; ice–albedo capped at about 2 °C plus polar CO2 release; mast gas gauges fitted at Phase 3 (pending); Training Hall lobby; one crew voice per phase.
- **Lessons:** run the threshold-vs-curve table whenever a curve or rule changes; forecasts need an explicit **lock point** before any hint, warning, or worked example that reveals the answer; batching mid-task research into the backlog first would have cut rework.
