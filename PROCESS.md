# Process

How the team works day to day. Adapted from the lessons in `docs/team/reference/GAME_DEV_HANDOFF.md`, a handoff from an earlier game project (a Godot game, so the engine-specific parts don't apply here). Owner: Producer. The coordinator adds retro rows when it files a round; the Producer reviews the log at every stage gate.

**How this file fits the others.** An **iteration** here is one round of agent tasks, or one build-and-test loop once code exists. Iterations sit inside **roadmap stages** (ROADMAP.md, "How We Iterate"). A stage exits only when its last iteration meets the definition of done (section 2) and the stage's exit criteria. Testing detail lives in `docs/production/qa-plan.md`; this file keeps the rules short.

**Who commits.** Role agents never commit or push (TEAM.md §5). Wherever this file says "commit", the coordinating session does it.

## 1. Working with the owner (the user)

- **Write every request down before building it.** `BACKLOG.md` holds the owner's words quoted, a short "wanted" line, and acceptance criteria. **Requests that arrive mid-task go to the backlog first**, then get routed; they go straight into the work in progress only if they refine the exact thing being built. The coordinator adds the backlog row before relaying the request to a role, and the relay message names the row number. A role that receives an unlogged mid-task request adds it to its report under Handoffs so it isn't lost.
- **Small iterations with a gate at each end:**
  1. **Plan:** goal, items, and decisions settled up front go into the backlog's "Current iteration". Commit that first.
  2. **Risk gate:** list what the change could break and, for each, the check that will catch it. A risk with no check gets a new check. For documents, the change impact check (TEAM.md 8.2) lists what could break; the check is the named role's review, the alignment table, or a forecast re-check. Write the risk gate as a table in the report: *change · could break · check · result*.
  3. **Build.**
  4. **Verify** against the definition of done (section 2).
  5. **Report:** what changed for the player, in plain words, then ask "Do you accept?"
  6. **Retro:** one row per iteration in the log below ("Keep: … Change: …").
- **Ask early, and ask once with everything.** When an item needs a decision only the owner can make, ask before building, as one set of questions with a recommended option first. Meanwhile take the next item that needs nothing from them.
- **Never invent a number that's meant to be real.** Real-world values live in sourced documents (`docs/science/`). If a real value is missing, build the feature without the number and list the value as missing. Game-balance numbers we do invent: say so, give the reasoning, and flag them for a balance pass (`docs/design/balance-sheet.md`).
- **"Accept" with a check still running is conditional.** Record acceptance only after the check passes.
- **Report in the owner's terms.** Lead with what changed for the player. Say what a screenshot or walkthrough shows, not what a function does. Say plainly when something failed and what we did.

## 2. Definition of done

**For documents and designs (now):**
- Consistent with TEAM.md decisions and vocabulary; goal IDs listed (traceability); rationale recorded (DOCUMENTATION.md).
- Alignment table, balance sheet, and story checked if the change touches them (change impact check, TEAM.md 8.2).
- Science claims sourced or marked unverified.
- **Risk gate table** in the report (section 1, step 2), every row with a result.
- **The owner can look at it:** a short walkthrough, diagram, or mockup for anything the player will see (a design can hide a visual problem just as passing checks can).
- Report written, backlog updated; the coordinator commits.
- **Accept is conditional while a check runs:** if an SME review, alignment check, or consensus is still open, the item is "accepted, pending <check>" and is recorded as done only after the check passes.

**For builds (once code exists):**
- The build and a **parse/compile gate** pass (a broken script must fail fast, never hang a test run).
- The automated playthrough passes N times across the variants that matter: standard mode, classroom mode, group of 1 and group of 5, the "everything at once" stress case.
- A screenshot run at the **smallest supported window (1280×720)**, and a person **looks at every screenshot that matters for the change**. Passing checks can hide a real visual bug. (1280×720 at GUI scale 3 is about 426×240 GUI px, which matches the style guide's 420×236 screen budget.)
- The invariant tests in `docs/production/qa-plan.md` §4 pass.
- Risk gate table filled; accept is conditional while any check runs.
- Spec written, retro row added, backlog updated; the coordinator commits.

## 3. Regression gate

When the mod exists, the automated checks are the regression gate. They run locally on every build; once CI exists (ROADMAP Stage 1 item), CI runs the same set. Details per stage: `docs/production/qa-plan.md` §5.
- **Play the real game through the real code**, start to finish, headless: seal the dome, raise pressure, reach each gate, breathe, build the beacon. Exit non-zero on any failure and print the first failing check with an id.
- **Every gate and every story beat must be reachable by an automatic solver**, and a test runs it. An unreachable gate is a soft-lock.
- **A check says where a thing must be, not only where it mustn't be.**
- **Prove a new check can fail:** run it against the old code and see it fail for the right reason.
- **Print what the check read**, not just true/false.
- **Tests that change global state put it back.**
- **Determinism:** generated content (forecast wording variants, drift events) is seeded from a stable id so it reads the same after save and load. Test that.
- **Content rules live in tests:** a clue or hint only appears for a problem that really exists and never states the answer; story beats match gate conditions; every forecast has a correct answer under the current physics.
- **Readability checks:** text color against its real background, at the smallest window.
- **Saves:** keep a schema version and a migration step per change; never overwrite a save from a newer build; keep a backup copy and test the fallback by corrupting the main file on purpose.

## 4. UI rules that apply to us

- **One message at a time at the top.** Close or hide banners and pinned notes when a screen opens.
- **No modal pop-ups for progress.** Progress lives in a side panel (next-gate display, Keeper's Record). Keep the "just finished" note there until the player starts the next real action. *Open point:* TEAM.md 8.6 rule 3 allows a phase debrief (Keeper's Report) as a blocking, deferrable interruption. That conflicts with this rule; it is raised in `docs/production/consensus-round2.md` §2.
- **Hide HUD panels over every full-screen sheet**, and measure that they fit at 1280×720.
- **Values bigger than labels** on stat rows.
- **Mark the next step** clearly.
- **Every line over the 3D world needs a backing** (dark strip or outline). Icons drawn for a light paper sheet vanish on a dark backing, so make a HUD variant.
- **Only offer actions the player can do now**; on gentle settings, one quiet line saying what would unlock more, rather than greyed-out buttons.
- **Name things the way the domain does** (real instrument and machine names), not with game-descriptions.
- **Deliberate typography**, not the engine's default font. Check glyphs exist before using them.
- **Small text hygiene:** thousands separators and units everywhere (one helper, tested); true minus signs; curly quotes.
- **Wrapped text needs a width** before it's added to a screen.

## 5. Game-design habits

- **Real procedure is the content.** Steps that match how the real work is done (checking the Monitor before a build, sealing before pressurizing) are what players value.
- **Fair, legible rules.** Resource costs and who pays for what are one written rule, shown on the screen where it matters.
- **Choice between routes** that fix the same thing (expensive and quick vs. cheap and more steps) adds depth for little code.
- **Clues reward careful reading.** A clue only appears for a problem that really exists, and never states the answer.
- **Difficulty as information, not stats.** Gentle settings point at the problem and explain blocks; hard settings tell you nothing and let bad work come back.
- **A checkpoint beats permadeath** for a long game. Gates lock in; backups and rollback exist (ARCHITECTURE.md §7).

## 6. Agent workflow habits

- Read TEAM.md, this file, and the settled-decisions docs before touching anything, and follow them even when told "just do it".
- Grep every use of a thing before removing it. Then add a check that the old thing no longer appears in player-facing text.
- When editing with a script, assert each old string occurs exactly once; a silent no-op replace is a common way to "finish" a change that didn't happen.
- The coordinator commits the plan, then the build, then the verified result, each pushed. If a hook demands a commit mid-verification, commit and say plainly it isn't verified yet.
- Run long jobs in the background and get notified; don't poll. Don't start a second copy of a long run on the same files; if code changes mid-run, restart the whole set on the final code.

## 7. Retro log

One row per iteration.

| Iteration | Keep | Change |
|---|---|---|
| Round 1 (2026-11-26): design docs, story bible, science review, consistency review | Parallel agents each in their own files; filing each report with a Task History row | Protocols (problem types, consensus, change levels) arrived mid-run, so the first round didn't follow them. Define process before launching a round. |
| Round 2 (2026-11-26): roles cleared Round 1 handoffs; budget, QA plan, consensus, process files | The change impact check and handoff format; asking the owner once with a recommended option; cut list ordered by goal tier and fun–learning quadrant, so cuts follow agreed rules instead of a meeting (Producer) | One handoff message went to the wrong agent. Keep the role-to-agent list in the task summary and check the recipient before sending. Mid-task research from the owner should go in the backlog first, then be routed. The Producer's task received three mid-task inputs (two Chapter 9 relays and this process handoff); each was small, but together they widened the task. Log them in the backlog with numbers before relaying, and batch them per role. |
| Round 3 (in progress): carry out the owner's Round 3 decisions | Owner decisions batched as one Decision Log row with "interpretation" noted; the backlog numbered every mid-task relay (#15–#17), which made the Producer's report traceable | Backlog rows were appended out of number order and one item (R-19) was handed off as stale when it was already fixed: read the current file, not the last report, before handing off |
