# Instructional Designer: journal and guidance wording check (2026-11-27)

From my lens, success means a 12-year-old reads each line once and takes away the right idea.

## 0. Handoffs received
- Technical Lead: check invented titles, pointer line, guidance messages; confirm journal text may stay `.ms` until grade bands exist. **Accepted** (below).
- SME recommendations accepted by the owner (On Earth line, Breathable 75 kPa, sky tweak): **applied**.

## 1. Done
Reading screen: rough script, Flesch-Kincaid (CR-T6). A screen, not proof. Syllable counting is approximate.

| Text | Words | FK grade | Verdict |
|---|---|---|---|
| Titles "The air outside", "On Earth", "The sky" (invented) | 2-3 | under 2 | Pass. Plain, concrete, US spelling |
| `journal.pointer` "New journal entries: N. Press J." | 6 | 1.3 | Pass. Positive, tells what to do |
| `journal.dome_stable.airmix.ms` | 15 | 5.4 | Pass |
| `earth.gate.dome_stable.ms` (corrected) | 31 | 5.3 | Pass; second sentence long but one idea |
| `journal.sky.stylized.ms` (after tweak) | 43 | 2.9 | Pass; 43 words, under the invented 45 budget |
| `guidance.*` (11 keys) | 3-11 | 0.5-6.0 | Pass; highest is `guidance.unknown` 6.0 |

- No banned words (CR-T1, CR-T2) and no negative feedback phrases (CR-T5); the test scans pass. US spelling holds.
- Guidance: gentle and standard have identical effect text ("all help is on"). Fine now; they will need distinct text if they ever differ (**not changed**).
- `journal.dome_stable.airmix.ms` writes "95 %" with a space. Minor; US style is "95%". Not changed (the symbol rule CR-T7b allows %). Optional.
- Pointer line is one per gate latch, as built. `phase-1.md` row for `journal.sky.stylized` still says chat pointer "New journal entry: the sky." (older wording). Game Designer to align.
- Journal text may stay `.ms` until grade bands exist: **approved**. The `.hs` On Earth read-more is over the 45-word budget by design.

Edits (all minimal):
- Sky line: "loses its dusty tan" became "turns less tan and more blue. The dust is still there." Hedge ("Nobody has seen a thickened Mars sky... made stronger so you can see them") unchanged. Files: `pack/assets/mars/lang/en_us.json`, `docs/design/messages/phase-1.md` section 12 row. **Flag for SME confirmation** (SME owns accuracy).
- `docs/design/ui-specs.md` 9.2: Breathable label value is per gate (30 kPa Phase 3 begins, 75 kPa Breathable); sky wording note added.
- Not edited: older quotes of the sky text in `docs/science/round2-review.md` (SME) and `docs/art/planetary-keyframes.md` (Artist). They show the old wording.

Real output: `cd mod && ./gradlew spotlessApply test --rerun`: BUILD SUCCESSFUL, **199 tests, 0 failures** (summed from test-result XML). Boot test not run.

## 2. Decisions needed
None. Well-structured, optional: "95 %" to "95%".

## 3. Handoffs
> **To:** SME · **Problem type:** well-structured
> **Needed from them:** confirm the new sky wording keeps the required hedge; update the quotes in `docs/science/round2-review.md`. **Done when:** confirmed.

> **To:** Lead Artist · **Problem type:** well-structured
> **Needed from them:** update the quoted label in `docs/art/planetary-keyframes.md` line 128.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Needed from them:** align the pointer wording in `phase-1.md` (row `journal.sky.stylized`); the Breathable gate data (when built) must give 75 kPa to `monitor.plot.goal.partial`. **Done when:** text and data agree.

## 4. Risks
- Breathable gate is not built, so the per-gate value is untested; a test should compare label value to gate pressure condition when it exists.
- Reading screen is rough; the 45-word budget is invented.

## 5. Shared model updates
None.
