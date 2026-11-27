# Report: SME, atmosphere visuals and sound (2026-11-27)

Docs only. Main file: [docs/science/mars-atmosphere-visuals.md](../science/mars-atmosphere-visuals.md). No edits under `mod/` or `pack/`. I ran no build or client. I opened and quoted 17 sources (NASA science pages, NSSDC fact sheet, NTRS abstracts and PDFs, two PubMed abstracts).

## 0. Handoffs received

| From | Handoff (artist report section 3) | Answer |
|---|---|---|
| Lead Artist | Blue sunset (PIA19400) | **Accepted, VERIFIED** (V2 to V4). Blue is a sunrise/sunset effect; midday glow is a simplification |
| Lead Artist | Long twilight and its length | **Accepted, VERIFIED**: up to two hours (T1). Skipping a custom dusk curve is supported (T2) |
| Lead Artist | Wind-driven sand, rate direction | **Direction confirmed, rate pushed back**: real sand motion is rarer and burstier than 1 streak per 5 s (W3, W6) |
| Lead Artist | Dust devils as real | **Accepted, VERIFIED**: 25 to 65 m wide, over 100 m tall, harmless (S8 to S11) |
| Lead Artist | Phobos 0.21 degrees, Deimos as a point | **Questioned**: arithmetic only; orbit distances not in an opened source (T10, T12) |
| Lead Artist | Roar versus rumble and hiss | **Ruled**: not honest at 0.6 kPa; rumble plus faint hiss is (section 7) |
| Lead Artist | Breath fog on a visor | **Not verifiable; recommend defer** (section 10) |

## 1. Done

- The science pass: 50 claims in tables, each with status, plan item (AT-1 to AT-11), source and quote (VERIFIED) or NOT VERIFIED. Pressure progression table (section 8) marked game extrapolation. Journal-ready lines at two depths (section 9).
- Findings: tan sky, blue sunset, 0.66 sun, thin ice clouds, long twilight, real dust devils and the muffled low sound are accurate. Storm sun fade is a gentle simplification. Wind streaks are too frequent. "Roar" fails at 0.6 kPa.

## 2. Decisions needed

| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Use "roar" for the storm? | ill-structured (feel vs honesty) | No at Phase 1; allow it from 30 kPa as an expectation |
| 2 | Make wind streaks rarer and bursty (W6)? | ill-structured (alive feel vs physics) | Yes: clusters with long gaps, more in storms |
| 3 | Drop breath fog (AT-9b) for now? | well-structured | Yes, defer |

## 3. Handoffs

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** apply the corrections to the plan.
> **Context:** `docs/science/mars-atmosphere-visuals.md` S3, W6, T6, T10, V8.
> **Needed from them:** label storm sun fade a simplification; change AT-3 streak rate to bursts; replace the vanilla sun yellow rim; word Phobos size as "about"; mark midday blue aureole as a simplification.
> **Done when:** the plan cites the claim IDs.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** journal lines and honest notes (section 9) need alignment to goals.
> **Needed from them:** pick the lines; add the "what we expect" wording for 5, 30, 75 kPa.
> **Done when:** the lines are in the journal content file with goal IDs.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** sound is keyed on pressure only; the paper says damping depends on CO2.
> **Needed from them:** note it in the audio data; no change required now.

## 4. Risks

- The "nine times faster" sand-wind figure rests on an unsourced Earth density; do not publish it yet.
- Phobos and Deimos numbers are my arithmetic.
- Three claims came only from search summaries and are not used as verified.
- The 1.5 kHz low-pass is an invented value near the measured band; students may read it as measured.

## 5. Shared model updates

None.
