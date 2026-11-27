# SME: Dome stable "On Earth" line, plot goal label, sky label (2026-11-27)

Perspective: from my lens, success means no line teaches a wrong number or a wrong cause. Docs only.

## 0. Handoffs received
- Instructional Designer 4b and section 6: confirm "body pushes back" and the Phase 3 / Breathable plot label. **Accepted, with two corrections** (below).
- Owner task: also check the sky label text. Accepted.

## 1. Done
### 1.1 "On Earth" replacement sentence: corrected, not confirmed as written
Proposed: "Earth's air pushes on you with about 100 times more force than Mars's air. You never notice, because your body pushes back."
- **Number is off.** 101.3 kPa / about 0.636 kPa (Mars mean) is about 160; with the game's 0.6 kPa it is about 170. "About 100 times" understates by roughly 40 percent. Use "more than 100 times". Real Mars pressure varies by season and place (about 0.4 to 0.9 kPa; from memory of the NASA Mars Fact Sheet, **not opened this session**: the host was blocked).
- **"Body pushes back" is loosely right, but can mislead.** The accepted physics is pressure balance: air in lungs, sinuses and gut, and the body's fluids and tissue, are at the same pressure as the outside air, so there is no net squeeze. A student can read "pushes back" as muscle effort or as "100 kPa of gas inside me". Say what pushes back.
- **Replacement (both bands):** "Earth's air pushes on you with more than 100 times the force of Mars's air. You never notice, because the air and fluids inside your body push back with the same pressure."
- **Certainty:** well established (high) for the balance idea; the exact split between gas and fluids is explained differently in popular sources, so the line stays general. Also avoid "crushed" and avoid saying "ears feel nothing": ears pop when pressure changes (a good HS read-more).
- **Source and mark:** web search summary only (Science Focus, LiveScience, Science Questions with Surprising Answers, WTAMU). I could not open any page (wtamu.edu and nssdc.gsfc.nasa.gov blocked in this session). **Mark: NOT VERIFIED.** Needs a primary or textbook source (a physiology or physics text on pressure equalization; NASA Mars Fact Sheet for the number). Do not mark verified until opened.
- Also keep the "dome" link: the old second line "Your dome has to make up the difference" is fine for HS.

### 1.2 Plot label "Pressure goal: 30 kPa"
- **Phase 3 begins: honest.** Gate is pressure 30 kPa and temperature 0 C or more (`DESIGN.md` gate table, line 75). "Pressure goal" names only the pressure part, and the label says so; this is the right fix for the earlier risk that "Goal" implied pressure alone. Needs the Next goal row to show the unmet temperature (already handed to the Game Designer).
- **Breathable: the number is wrong.** Breathable's pressure condition is 75 kPa, not 30 (`DESIGN.md` line 76: O2 at least 16 kPa and pressure at least 75 kPa and CO2 at most 0.5 kPa, held 7 sols). Use `Pressure goal: 75 kPa` there, and the same key. Because Breathable is mostly about gas mix, consider showing O2 and CO2 rows instead of leaning on the pressure line. The 30 kPa value applies only to Phase 3 begins.
- Equilibrium pressure is 75 kPa too, with a band; label `Pressure goal` is fine only if the band is explained elsewhere.

### 1.3 Sky label
Text: "More air scatters more blue light, so the sky slowly loses its dusty tan. Nobody has seen a thickened Mars sky. These in-between colors are our best guess, made stronger so you can see them."
- **Accurate and honestly hedged.** Rayleigh scattering grows with the amount of gas in the path, and blue scatters most. Today's tan sky comes from suspended dust (qualitative, textbook-level; this is the line I required in round 2 section 6 note 8; sources there, **not re-opened now**).
- **One nuance, optional edit:** "loses its dusty tan" can read as if air removes the dust. The dust stays; the blue from the gas simply grows to dominate. Suggested optional wording: "...so the sky slowly turns less tan and more blue. The dust is still there; the air's blue just grows stronger." Current text is acceptable to ship (**verdict: accurate, minor nuance**).
- Mark: **not verified this session** (no source opened). Round 2 status stands.

## 2. Decisions needed
1. Adopt the corrected On Earth line (well-structured). Recommend yes.
2. Breathable plot label says 75 kPa, not 30 (well-structured). Recommend yes.
3. Optional sky wording tweak (well-structured). Recommend yes, low priority.

## 3. Handoffs
> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** apply the corrected On Earth line to `text.ms`/`text.hs`; make the goal value per gate (Phase 3 begins 30 kPa; Breathable 75 kPa).
> **Context:** `docs/design/messages/phase-1.md` section 9 `sme_note`; ui-specs 9.2.
> **Needed from them:** accept or change. **Done when:** text and gate data agree.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** approve the line for reading level (long second sentence).
> **Done when:** CR-T6 check passes.

## 4. Risks
- "Pushes back" misread as effort or hidden gas pressure. The corrected line addresses it.
- A reused 30 kPa label on Breathable would teach a wrong threshold.
- Sources not opened: every mark above stays unverified until a teammate with network access to NASA and a physics or physiology text opens them.

## 5. Shared model updates
None. Lesson for the log: check per-gate numbers when a label key is shared across gates.
