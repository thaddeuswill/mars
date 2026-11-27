# Report: Lead Game Designer, storm messages (2026-11-27)

Docs only. Desk check of documents; nothing run (no readability screen, no tests).

## 0. Handoffs received
Coordinator task: check F1 and F2 trigger text and the solar-event warnings against the new order (first storm sol 14 / classroom sol 4, after Dome stable and first Outgasser; warning 1 sol; length 2 sols / 0.5; cut 60 %; +1 dust stage at end), fix `phase-1.md`, add storm line wording to `ui-specs.md`. Accepted. Also closes the open risk in `2026-11-27-game-designer-storm-length.md` (order line vs curve).

## 1. Done
- `docs/design/messages/phase-1.md`:
  - Line 5: order confirmed; tokens `{lead}` (1 sol / quarter sol) and `{len}` (2 sols / half sol) so classroom reads correctly.
  - Shift Briefing 1 gave F1's answer 13 sols early ("Dust storms cut solar power"). Now "Dust storms will come. Plan your power." `science_claims` set to None.
  - F1: trigger now names sol 14 / sol 4, lead `{lead}`; prompt "in about {lead}"; lock row adds expiry rule and `reveals` list.
  - Solar events (section 3): added an ordering rule that the first solar event comes after F1's result (proposed, *invented*). F2 text is unchanged and fits: no storm or Dome stable mention, lead 1 sol in both modes (`ui-specs.md` section 4).
- `docs/design/messages/story-beats.md` beat 4: "half a sol to a full sol" replaced by "about {len}" (it contradicted the 2-sol storm); `asserts` gives classroom 0.2 to 0.3 sol.
- `docs/design/ui-specs.md` new section 10: storm line keys (row labels, values, HUD, log lines), all labels 24 characters or fewer, US spelling, positive wording, and the list of what must not reveal F1 (`reveals` declared per key).

## 2. Decisions needed
- Ill-structured, low stakes: state the storm length in the warning line ("should last about 2 sols")? Recommend yes (lets players size a battery plan, C1, and it does not touch F1). Revisit if testers skip planning.
- Well-structured: ordering rule "first solar event after F1's result" is invented; owner may drop it.

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Needed:** formatting helper for `{lead}` / `{len}` ("1 sol", "a quarter sol", minutes under 1 sol); lang keys from `ui-specs.md` section 10; `reveals` field on the storm keys (CR-F9). **Done when:** keys in `en_us.json`, CR-F9 test passes.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Needed:** readability screen (grade 6-7) on section 10 keys and the new Briefing 1 line; confirm the Briefing no longer pre-teaches F1 (alignment B3, C1). **Done when:** screen result recorded.

> **To:** Lead Artist · **Problem type:** well-structured
> **Needed:** storm HUD icon shape distinct from the sun-burst hexagon. **Done when:** icon in the style guide.

## 4. Risks
- Classroom lead 0.25 sol is about 5 min of play: F1 may expire unanswered. Playtest.
- Briefing 1 change loses the explicit "storms matter" threat; "Plan your power" keeps it. Check in playtest S1.
- `balance-sheet.md` lists the lead as 0.25 sol (classroom) while `ui-specs.md` section 4 says solar-event lead stays 1 sol in both modes: deliberate (different hazards), but unverified against code.

## 5. Shared model updates
- Storm messages use `{lead}` and `{len}` tokens, never fixed numbers.
- A forecast's answer must also not be pre-taught in Briefings (lesson: Briefing 1 spoiled F1).
