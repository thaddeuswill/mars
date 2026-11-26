# Game dev handoff: what worked, what broke, what to do differently

Lessons from building a Godot 4 first-person sim/management game with an AI agent doing most of the
code, one human owner making every product call, and no CI. Written to be dropped into another repo
and read by the next agent before it starts. Nothing here assumes the old project's code.

---

## 1. Working with the owner

- **Write every request down before building it.** Keep a `BACKLOG.md` with the owner's words quoted,
  a short "wanted" paragraph and acceptance criteria. Requests that arrive mid-task go there, not into
  the work in progress, unless they refine the exact thing being built. This stops half-finished
  features piling up.
- **Work in small iterations with a gate at each end.** What worked:
  1. Plan: write the goal, the items, and the decisions settled up front into the backlog's
     "Current iteration" section. Commit that first.
  2. Risk gate: list what the change could break and, for each, the check that will catch it. A risk
     with no check gets a new check.
  3. Build.
  4. Verify against a fixed definition of done (below).
  5. Report: what changed, in plain words, with what the screenshots show, then ask "Do you accept?"
  6. Retro: one log row per iteration ("Keep: ... Change: ...") in `PROCESS.md`.
- **Ask early, and ask once with everything.** When an item needs a decision only the owner can make
  (a real-world value, a price, a naming choice), ask before building, as one set of multiple-choice
  questions with a recommended option first. Meanwhile take the next item that needs nothing from
  them; don't sit blocked.
- **Never invent a number that's meant to be real.** If the game uses real-world values (specs,
  torques, prices from a catalog), keep them in one sourced document. When a value is missing, ship
  the feature without the number ("set the gap to the manufacturer's figure") and list the value as
  missing. Game-balance numbers you do invent: say so, give the reasoning (e.g. "the average of the
  five old values, so the economy stays the same on average") and flag them for a later balance pass.
- **"Accept" with a check still running is conditional.** Record the acceptance only after the check
  passes, and say so.
- **Report in the owner's terms.** Lead with what changed for the player. Say what a screenshot
  shows ("the arrows sat stacked in a corner"), not what a function does. Be explicit when something
  failed and what you did about it.

## 2. Definition of done (adapt the numbers)

- The script parse gate passes (see 4).
- The self-test passes N times across the variants that matter: plain several times (randomized
  content), each difficulty, the "everything at once" stress case, each tutorial step.
- A screenshot run at the **smallest supported window** (here 1280x720), and **you look at every
  screenshot that matters for the change**. Twice in this project the checks passed while the
  screenshot showed a real bug (dark text on a dark map; buttons stacked in a corner).
- Spec written (`docs/quick-specs/<name>-<date>.md`: why, what, acceptance), log row written, backlog
  updated, committed and pushed.

## 3. Godot 4 specifics that bit us

- **Never find or place a node by its `name` after a rebuild.** When you `queue_free()` a node and add
  a new one with the same name in the same frame, Godot renames the new one (`Arrow_left2`,
  `@Button@123`). Anything that later looks it up or switches on `name` silently fails. This broke four
  separate features. Tag nodes with `set_meta("kind", ...)` and read the meta; or keep direct
  references in an array.
- **Freed nodes linger until the end of the frame.** When you search children after a rebuild, skip
  `is_queued_for_deletion()` nodes.
- **A script that doesn't parse can hang a headless run forever** (no output, no exit), because the
  test harness lives in a script too. Two defences:
  - A parse gate before every run:
    `for f in scripts/*.gd; do godot --headless --path . --check-only --script "$f" 2>&1 | grep -c "SCRIPT ERROR"; done`
  - An autoload "boot guard" that fails the run with a clear message if the self-test hasn't started
    within N seconds, and a watchdog thread inside the self-test that fails it if frames stop for
    60 s or the run passes a hard time limit, printing the last step it reached.
- **Wrapped labels with no width lay out thousands of pixels tall.** Give every autowrapped Label a
  `custom_minimum_size.x` before it's added. If you estimate heights for pagination, base them on the
  same widths.
- **Drawn UI (`_draw`) has no nodes to inspect.** For anything a test must verify, have the draw code
  read from a pure data function (`quest_rows() -> Array`) and store the rect it drew
  (`last_rect`). Tests read those; the drawing stays a thin layer.
- **Headless still runs `_draw`** (so draw-time values like `last_rect` update), but wait a frame or two
  after changing state before reading them.
- **Static/cached scene definitions can't depend on runtime state.** When one thing needs two
  variants (e.g. a repair done two ways), add a second definition with its own id and map it back
  (`route_fault(scene) -> fault`) everywhere an id is read as the fault: completion, the "healthy"
  check, the manual page lookup.
- **`Input.parse_input_event` works headless** for testing key bindings end to end.
- **Check glyphs before using them** (arrows, curly quotes): `font.has_char(0x25B2)`.
- **Screenshots need a display:** `xvfb-run -a -s "-screen 0 1280x800x24" godot --path . --rendering-driver opengl3 --resolution 1280x720 -- --self-test --shot=prefix`.
  Take them at device scale 1 when only you look at them.
- **Parallel test runs fight over `user://`.** Give each its own: `XDG_DATA_HOME=/tmp/runN godot ...`
  (Linux). A small wrapper script per run (own folder, timeout, log, `rc=` line at the end) makes
  `xargs -P 2` batches easy.
- **Saves:** keep a `SAVE_VERSION` and a `_migrate()` step per bump; never overwrite a save from a
  newer build; keep a `.bak` and test the fallback by corrupting the main file deliberately. When you
  remove a concept (a "plane type"), keep the old field on saved records if anything derived from it
  (an id seeded from it) must stay stable; just stop showing it.

## 4. The self-test as the regression gate (no CI)

- **Play the real game through the real code**, headless, start to finish: travel, inspect, buy,
  repair, run up, sign off, fly out, get paid. Exit non-zero on any failure, and print the first
  failing check with an id.
- **Every content scene must be solvable by an automatic `solve()`**, and a test runs `solve()` on
  every scene. An unsolvable scene is a soft-lock for the player.
- **A check must say where a thing must be, not only where it mustn't be.** "Arrow not over the
  picture and on screen" passed with every arrow at (0, 0). "Left arrow left of the picture, centred
  on it" catches it.
- **Prove a new check can fail.** After writing one, run it against the old code (`git stash` the
  fix) and see it fail for the right reason.
- **Print what the check read**, not just true/false (`arrival "Arrived in Dallas. Walk around
  N6671W..."`). A check that passes because the thing it reads is empty or missing is worse than none.
- **Tests that change global state must put it back.** A test that fakes a delivery or fills the
  inventory and doesn't restore it leaks into later checks and screenshots (e.g. tutorial steps
  appearing already ticked). Save a tuple of everything touched at the start; restore at the end.
- **Check readability, not just size:** text colour against its actual background (the same row style
  can sit on a light sheet and a dark map).
- **Determinism helps:** seed generated content from a stable id (`hash("logbook|" + registration)`)
  so the same thing reads the same after save/load, and test that it does.
- **Content rules belong in tests.** e.g. for generated logbook "clues": every clue entry matches a
  real fault on that plane; none names its fault or part outright; timestamps fall monotonically.

## 5. UI and UX lessons (game HUD and paper-style sheets)

- **One message at a time at the top.** Message band, banners, pinned notes and cards stacked up into
  four text layers. Clear the band and banners when a sheet, cockpit or pause opens.
- **No modal pop-ups for progress.** A side-panel quest tracker (title, objectives with
  done/in progress/locked, the current steps ticking, the reward shown) replaced a reward card the
  player had to dismiss. Keep the "just finished" note on the panel until the player starts the next
  real action, so it's actually seen.
- **Hide HUD panels over every sheet, scene, cockpit and results card**, and measure that they fit at
  the smallest window above the bottom HUD row.
- **Values bigger than labels** on stat rows ("$3,395" large, "Total" small).
- **Mark the next step** (a larger "Next:" row, an accent bar), drop repeated prefixes like "To do:".
- **Every line over the 3D world needs a backing** (a dark strip or outline), and **icons drawn for a
  light paper sheet vanish on a dark backing**: make a HUD variant of the icon.
- **Small text hygiene that players notice:** thousands separators on all money (one helper, used
  everywhere, tested by a regex for `\$[0-9]{4,}`); no full stop after `?` or `!` when joining
  sentences; curly quotes around quoted speech; true minus signs on money.
- **Tool-gating:** offer only actions the player can do now; on the easiest difficulty, add one quiet
  line saying what would unlock more, rather than greyed-out buttons.
- **Navigation in a static scene:** labelled arrows in the margins ("REAR CASE ▶") beat a row of
  tabs for "walk round the object". Shrink the picture to make margin room; never lay controls over
  clickable art.
- **Names that look real:** identify things the way the domain does (a plane by its tail number,
  "N591UZ, Piper J-3"), not by game descriptions ("Rag-wing taildragger").
- **Typography:** pick deliberate faces (a display face for UI, a monospace for changing figures, a
  serif for long reading) and never fall back to the engine's default font.

## 6. Game design lessons from this project

- **Real-world procedure is the content.** Players liked steps that match how the job is really done
  (logbook research, timing a magneto, the order of removal). Paraphrase manuals in their imperative
  style; never copy them.
- **Money must be fair and legible.** Who pays for what (the owner pays for the job and their own
  fuel; mistakes come out of your pocket) needs to be one written rule, shown on an itemised results
  card. A flat fee beat a clock the player never understood.
- **Choice between routes** (buy the full assembly, or the cheap piece parts and more steps) adds depth
  for little code if both routes fix the same thing and the shop sells both.
- **Clues in documents** (a missing oil change, work signed by someone unqualified, a follow-up never
  logged) reward careful reading; the rule is that a clue only appears for a problem that really
  exists, and never states the answer.
- **Difficulty as information, not stats:** Easy points at the work and explains blocks; Hard tells
  you nothing and lets bad work come back.
- **A checkpoint ("start over from the last job") beats permadeath** for a long career.

## 7. Agent workflow habits worth keeping

- Read the repo's own process and settled-decisions docs before touching code, and follow them even
  when the owner says "just do it": they wrote the process for a reason.
- Grep for every use of a thing before removing it (here: every `job.type` read), then add a test that
  the old thing no longer appears in player-facing text.
- Make the edit with a script that asserts each `old` string occurs exactly once; a silent no-op
  replace is a common way to "finish" a change that didn't happen.
- Commit the plan, then the build, then the verified result, each pushed, so a lost session loses
  little. If a hook demands commits mid-verification, commit and say plainly it isn't verified yet.
- Keep long runs in the background and get notified; don't poll. Never start a second copy of a
  long run while one is going on the same files; if you change code mid-run, stop and restart the
  whole set on the final code.
