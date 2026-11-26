# Report: Story Bible

- **Role:** Lead Game Designer
- **Date:** 2026-11-26
- **Output:** `docs/design/story-bible.md`
- **Note:** Ran before the team protocols (problem types, consensus, change levels, fantasy rules) existed. The coordinator applied them when filing.

## Done
Story bible covering purpose and rules, the core story elements, backstory, characters, plot arc, delivery, classroom mode, tone, and an originality check.
- **Premise:** the player is the **Keeper**, on a *planned* solo watch at Cairn Dome after the three-person First Shift left on schedule. A dust storm damages the dome.
- **Fauna ("thawlings"):** fictional dormant organisms found in deep ice, with hooks to real science: cryptobiosis, brines, life stages.
- **Cast:** Moreno, Basu, Kowalczyk, Mensah, the station system BELL, and the fauna. All reach the player through recorded messages, notes, traces, or behavior. There are no NPC questlines.
- **Beats:** 20 story beats tied to the gates. 13 are essential for classroom mode and 5 are optional.

## Decisions needed (classified by coordinator)
| # | Decision | Recommendation | Type |
|---|---|---|---|
| 1 | Non-lethal ways to deal with the fauna (deterrents, "quiet zones") | Yes | Ill-structured |
| 2 | Replace "mutated fauna variants" with "later life stage" (avoids a misconception about how evolution works) | Yes | Well-structured (accuracy fix) |
| 3 | Ending cards describing what happened to the fauna, with no ranking | Yes | Ill-structured |
| 4 | Recorded messages: text first, optional voice audio later | Text first | Well-structured (scope) |
| 5 | Characters address the player as "Keeper", not their username | Keeper | Well-structured |
| 6 | Approve the backstory premise (planned solo watch) | Approve | Ill-structured |

## Handoffs
- **Subject Matter Expert:** verify every [verify] claim in the bible; review the fauna fiction framework (section 3.4).
- **Lead Instructional Designer:**
  - Radiation prediction beat (closes the A4 gap).
  - Earth-link content for D2.
  - Reflection prompts.
  - Reading level and grade-band variants.
  - Colony Record framing.
- **Lead Artist:**
  - Fauna that look like animals, not monsters.
  - How BELL's log and messages look on screen.
  - Props left by the crew.
  - Sound change when the helmet comes off.
  - Beacon ending sequence.
- **Technical Lead:**
  - Message queue triggered by gates, plus a replayable message archive.
  - Delays measured in sols, scaled by classroom mode.
  - "Since last shift" recap and epilogue cards.
  - All of it as datapack and language files.
- **Producer:** clear the names against existing works; scope voice audio; schedule story writing.

## Risks
- The ethics goal (D1) is hollow if the fauna can only be fought.
- Fictional fauna could reinforce the idea that Mars has life.
- Names and premise not yet checked against existing works.
- Text load for weaker readers.
- Voice audio cost.
- Phase 2 has the fewest story beats per hour.
- Isolation themes for teens.
