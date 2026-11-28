# Game Designer report: exploration finds revision (owner decisions applied)

**Framing in two sentences.** "Signs of life?" case files now come in two labelled kinds: REAL EVIDENCE cases (Viking-style, Cheyava Falls-style) stay open at rung 3 and are scored on the quality of the player's reasoning, while FICTION cases (the thawling nests, the player's own biocrust) can reach a top rung 4 that says "life", because in the story the evidence really supports it. If you want a real-Mars "life" ending instead, rung 4 would unlock on real cases and the game would teach something science has not established (NASA says no life or confirmed sign has been found, SME review section C), so I advise against it; the revision would also make SOL-1 unenforceable and drop the kill switch.

## 0. Handoffs received
- Owner decisions (BACKLOG 2026-11-28): replicas accepted; MVP in Stage 4 Phase 2 accepted; "life" ending allowed. **Accepted** and applied.
- SME review `docs/science/exploration-finds-review.md` now exists. **Accepted**: used for the real-case wording, the "do not show life found outside fiction" rule, the CoLD scale caution (not verified, so not named) and the Cheyava Falls wording.
- ID's SOL-1 request: **revised as asked** (below). `docs/learning/content-rules.md` has no SOL-1 row yet (grep found none), so the ID adds it.

## 1. Done
Revised `/home/user/mars/docs/design/exploration-finds.md` (docs only):
- **Section 4 rewritten.** 4.0 two kinds side by side; 4.2 five-rung ladder (rung 4 only for fiction, shown locked on real cases as "No real Mars case has reached this"); 4.3 case 1 stays real and open; 4.4 new fiction case **F1 "Are the sleepers alive?"** (Phase 2; fading chemistry vs. holding signal, sterile and heated controls, repeat, positive control) and **F2 positive control** on the player's own biocrust (rung 4 is correct, then "would you call it Martian?", the contamination lesson); rung 4 needs: each non-life card ruled out, a repeat, a positive control; 4.6 feedback; 4.7 **SOL-1 revised** in four testable parts, the **labelling rule** (book icon + FICTION and dashed frame vs. rover-wheel icon + REAL EVIDENCE and solid frame, on board, journal page, sample label), four ways to tell the kinds apart, recall check "Has life been found on Mars?" after every case; 4.8 misconception table, rated medium to high, with a **kill switch** (`fiction_top_rung`, invented threshold 1 in 5).
- **SOL-1 revised:** "No text, icon, sound or log line claims that life was found on real Mars. Any claim that life exists is allowed only on a surface carrying the FICTION tag and only in a fiction case." Parts a to d: banned words on real text; `ending: life` only on `kind: fiction` (data validation); typed sample sources (E10); tag cannot be omitted.
- **CR-F9/F10 table (6.3)** gets rows for F1, F2, rung 4 text and CR-F11 items; F10: no new source ID, life ending is knowledge only.
- **Cost note (7.2):** F1 adds about 2 to 3.5 sessions over the earlier MVP. New MVP total about 11 to 17 TL, 6.5 to 9 Artist, 4 to 5 GD, 3 to 4.5 ID, 3.5 to 4.5 SME (all *unverified*; no rate measured). Cut order and "never cut" updated; audit rows added; section 8 reduced to one question.
- Summary, principles (E5, E10), misconception table, assumptions and handoffs updated. Replicas and Stage 4 marked decided.

Not verified: all real-world site facts remain *unverified* unless in the SME review; the F1 test logic ("chemistry fades, life holds") is a story rule the SME must check. Nothing was built or playtested; this is a design walkthrough (as a 12-year-old: the tag and dashed frame are visible, but a skimming reader may still miss the contrast line; as a 17-year-old: the locked rung is the hook).

## 2. Decisions needed
1. **Ship F1 (fiction case) in the Stage 4 MVP, or only F2 in Phase 3?** Ill-structured. **Recommend F1 in the MVP**: the pair real-open and fiction-answerable teaches by contrast; cost about 2 to 3.5 sessions.

## 3. Handoffs
> **To:** Lead Instructional Designer · **Problem type:** well-structured (rule text) with an ill-structured part (threshold)
> **Problem:** add SOL-1 revised (a to d) to the content rules; check the recall check, the misconception threshold and D1 alignment.
> **Context:** `docs/design/exploration-finds.md` 4.5, 4.7, 4.8.
> **Needed from them:** rule row, wording pass (grade 6 to 7), the real threshold for the kill switch, the Stage 6 real-reader check.
> **Done when:** SOL-1 is in `content-rules.md` with a test description.

> **To:** SME · **Problem type:** well-structured
> **Problem:** check rung wording ("No real Mars case has reached this"), the keyed recall answer, the F1 test logic, the contrast lines.
> **Context:** same file, 4.2 to 4.4, 4.7.
> **Done when:** each line marked verified or reworded.

> **To:** Lead Artist · **Problem type:** well-structured
> **Needed:** book and rover-wheel icons readable without colour; dashed and solid frames.
> **Done when:** icons pass a greyscale check.

> **To:** Technical Lead, Producer · **Problem type:** well-structured
> **Needed:** feasibility of case-data validation and the `fiction_top_rung` flag; re-price the MVP and the swap.

## 4. Risks
- Fiction-to-real transfer (main risk, medium to high, untested). Controls: labels, contrast lines, recall check, kill switch.
- Scope: MVP rose by about 2 to 3.5 sessions; Stage 3 is untouched.
- Labelling is the safety system: if it cannot be built everywhere, cut the fiction case, not the labels.
- The invented 1-in-5 threshold and every session count are guesses.

## 5. Shared model updates
- New terms: **REAL EVIDENCE case** and **FICTION case** (case-file kinds); **rung 4** (fiction-only top rung); **sample source tag**.
- Decision for the Decision Log (owner's, owner doc `docs/design/exploration-finds.md`): case files may end in "life" only in labelled fiction; real-evidence cases cap at rung 3. Rationale: real science unresolved. Alternatives: always-open (rejected by owner); real "life" ending (advised against). Trade-off: appeal and D1 payoff against transfer risk. Revisit if the recall check shows misconceptions above the threshold.
- Proposed content rule SOL-1 (revised, a to d) for `docs/learning/content-rules.md`.
