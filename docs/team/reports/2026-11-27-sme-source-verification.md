# SME: source verification attempt (2026-11-27)

From my lens, success means no mark says "verified" for a page I did not open. Docs only.

## 0. Handoffs received
- Instructional Designer: confirm new sky wording keeps the hedge; update the quote in `round2-review.md`. **Accepted, done.**
- Owner task: verify pressure and body-fluid claims from opened sources. **Accepted; could not complete (network).**

## 1. Done
### 1.1 Source access (real output)
| Host | Tool | Result |
|---|---|---|
| nssdc.gsfc.nasa.gov (Mars fact sheet) | WebFetch | `getaddrinfo ENOTFOUND`: not allowed in this session |
| www.ncbi.nlm.nih.gov (PMC search, Bookshelf NBK430685) | WebFetch, 2 tries | `getaddrinfo ENOTFOUND`: not allowed in this session |
| Web search (NCBI-limited) | WebSearch | No NCBI page returned. Only an arXiv abstract ("In Outer Space without a Space Suit?", arXiv 0806.3792) about vacuum exposure, seen as a search summary, not opened |

I did not try to bypass the block. **Both hosts must be added to the session's allowed network domains**, then the fetch retried.

### 1.2 Verdicts
| Claim | Verdict | Why |
|---|---|---|
| Mars mean surface pressure about 0.636 kPa | **NOT VERIFIED** (this session) | Fact sheet not opened. The value in `docs/science/accuracy-review.md` [1] came from earlier work; I did not re-open it |
| "Air and fluids inside your body push back with the same pressure" (pressure balance; Armstrong limit about 6.3 kPa) | **NOT VERIFIED** | No physiology page opened. The search summary only restates that fluids boil at very low pressure and tissue holds the body together. The 6.3 kPa figure is from general knowledge, unchecked. The game text does not quote it |
| Sky wording "turns less tan and more blue. The dust is still there." | **Accurate; hedge intact** (reasoning, not a source check) | Rayleigh scattering of gas favors blue; the tan comes from suspended dust. More gas adds blue without removing dust, so "less tan and more blue" with "dust still there" is correct. The hedge ("Nobody has seen a thickened Mars sky... best guess, made stronger") is unchanged. Source status as in round 2: unopened now, so **not verified** as a citation |
| Round 2 sky-label quote | **Updated** | `docs/science/round2-review.md` line 216 now matches `pack/assets/mars/lang/en_us.json` |

No verdict changed, so the marks in the earlier report and `phase-1.md` section 9 `sme_note` stay as they are (NOT VERIFIED). Not edited.

Wording note for the Instructional Designer (optional, no change needed): the two-sentence "less tan and more blue. The dust is still there." is fine; do not add "pink" or "red" claims.

## 2. Decisions needed
1. Allow `nssdc.gsfc.nasa.gov` and `www.ncbi.nlm.nih.gov` in the session network settings (well-structured). Recommend yes, then rerun this task (about 10 minutes). Until then the marks stay NOT VERIFIED.

## 3. Handoffs
> **To:** Producer · **Problem type:** well-structured
> **Problem:** the two hosts the owner named do not resolve in this session.
> **Needed from them:** confirm the allowlist took effect in a new session.
> **Done when:** the Mars fact sheet and an NCBI physiology page open.

## 4. Risks
- The 0.636 kPa and body-pressure claims ship unverified. Low risk: the numbers are standard and the wording is general; "more than 100 times" holds even at 0.9 kPa (about 112 times).

## 5. Shared model updates
None. Lesson: an allowlist change may apply only to new sessions; test one host first.
