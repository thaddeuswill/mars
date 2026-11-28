# Game Designer: orbital resupply (owner 5b)

## 0. Handoffs received
Task from the coordinating session (owner 5b): simplify recipe inputs with an orbital resupply. Accepted. One pushback: the ISS is not named as sender (it cannot reach Mars); a "Relay Orbiter" is, with game-design labels.

## 1. Done
- `docs/design/recipe-inputs-ruling.md` rewritten as v2 (old plan kept in Appendix A, marked superseded). Drop per gate, one new item (`mars:machine_core`), fallback `mars:resupply_request` (4 iron + 1 coil, 3-sol pass timer), exact recipe changes, Artist and journal effects. Basalt fibre/board and tag files dropped.
- `docs/design/gate-input-items.md`: status note; gates 4 to 7 items marked drop-only.
- Nothing run (docs only). All counts INVENTED.

## 2. Decisions needed
A accept orbital resupply with core and request item (ill-structured; yes). B Relay Orbiter only, no ISS (yes). C fuel: coal in drops (ill-structured; yes now). D request cost and timer (tune). E drop-only gate parts (yes).

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured · **Problem:** build core, request item, cache lists, recipe edits · **Context:** `recipe-inputs-ruling.md` section 7 (11 rows) · **Needed:** implement; confirm `GateCaches` accepts vanilla ids and a refill list; `CacheLedger` timestamp · **Done when:** boot test latches each gate, request cools down, Index rows stay flat.

> **To:** Artist · **Problem type:** well-structured · **Needed:** icons for machine core, resupply request, resin pack, sorbent bed, signal board, activation cell; "Sent from orbit" corner mark · **Done when:** icons readable in greyscale.

> **To:** Instructional Designer · **Problem type:** well-structured · **Needed:** CR-F9 scan and alignment (A1, B4) of the supply note, journal entry and tooltips in section 8 · **Done when:** wording approved.

> **To:** SME · **Problem type:** well-structured · **Needed:** confirm Relay Orbiter wording (real orbiters relay for rovers; supply role invented) and ISS cargo mention.

## 4. Risks
Drops may read as free progress if core counts are too generous; counts wait on Index rate rows (balance sheet R5). Fuel is unresolved. Drop-only parts lose a crafting step.

## 5. Shared model updates
Terms: orbital resupply, machine core, resupply request. Proposed Decision Log entry is in ruling section 11 (TEAM.md and DESIGN.md not edited).
