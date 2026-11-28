# Cairn Dome resource progression

**Status** · Design proposal for the hand-made map. Resource yields, cache inventories, recipe integration, and implementation timing need a playtest design pass. The existing Landing checklist, Dome stable and 5 kPa Phase 2 gate remain the route’s story anchors.

## What lands with the crew

NASA has no flown crewed Mars manifest. Its reference studies explore pre-deploying power, mobility equipment, and an ascent vehicle before the crew. For Cairn, that suggests a functioning pressurized base and nearby cargo racks as the starting point. NASA life support separates pressure control, carbon dioxide removal, oxygen generation, water recovery, and other habitat functions. MOXIE demonstrated oxygen production on a rover. Perseverance’s coring drill demonstrates sample collection. Crew-scale extraction at Cairn is a game design assumption.

- **Shelter and Ops.** Pressurized rooms, airlock, thermal control, CO₂ scrubbers, air and water storage, packaged food, sanitation, medical kit, and local controls. Emergency reserves support repair work after the storm.
- **Engineering.** Existing generation and battery reserve, relay hardware, a repair bench, starter Ovens, hand tools, and spare seals, filters, cables, valves, and precision parts. Keep the critical seal kit in the Ops locker.
- **Airlock locker.** Pressure suits and refills when the suit system is available, dust control, radio, route markers, hand scoop, sample containers, and a service wrench. Reserve the hand core drill and labeled tubes for the survey route.
- **Pre-landed cargo.** An accessible rack of marked spares near Cairn and a more distant cache for bulk expansion equipment. A return vehicle is plausible background infrastructure. It does not need a recipe or unlock for this route.

[NASA SAC21 Mars architecture](https://ntrs.nasa.gov/citations/20210026448) · [NASA ECLSS](https://www.nasa.gov/reference/environmental-control-and-life-support-systems-eclss/) · [NASA MOXIE](https://science.nasa.gov/mission/mars-2020-perseverance/science-instruments/) · [Perseverance coring drill](https://science.nasa.gov/resource/sample-tube-in-perseverances-coring-drill/)

## The route from first repair to first water delivery

| When | Where and how the player gets it | What the player does with it |
|---|---|---|
| Landing | Find the seal kit in the Ops locker. Read the tank by the bunk. Reuse the working power, reserve supplies, and starter Ovens. | Patch Hab B and check the Monitor. Keep the existing BELL checklist and its skip path. |
| First shift | Follow a short, visible route from the airlock to a marked shallow regolith patch. Scoop a batch with the supplied tool. A visibly damaged cargo rack on the same route holds metal stock and serviceable parts recoverable with the wrench. | Check a sample batch at the Ops Oven. Compare a baseline sample with a richer one before choosing what to process. Use the near route to teach taking supplies out and bringing useful material home. |
| Dome stable | Hana’s relay message provides the existing Outgasser plan and points to the marked service cargo. Use the rack for early iron, copper, and glass. Use authored local feedstock and processing for the brick portion once that path exists. | Assemble and power the first Outgasser in Ops. The worked example takes the player to the Monitor to verify the pressure effect. |
| Pressure climb | Make additional parts available from a second accessible cargo bin or a reserved replenishment route. Use the scoop site for repeatable bulk input. | Respond to low power or frost by increasing generation or throughput. A short hand-core practice stop can fill the wait. The existing 5 kPa gate still opens Phase 2. |
| Phase 2 survey | Follow Basu’s existing drill markers to two hand-authored exposures. Use a hand drill to take labeled cores at chosen depths. Carry them to a sample bench at Cairn or an established outpost. | Compare the accessible lower-yield prospect with a richer prospect that costs more travel, power, or servicing. Keep the existing timing of Basu’s essential fauna message. |
| Phase 2 extraction | Bring a powered extractor, connection kit, and power reserve to the selected site. Use the validated deposit and return usable water to Cairn. | Verify real delivery, then decide whether the installation earns a shelter, refill point, and regular maintenance. One successful field sample is evidence for a decision. Sustained delivery is the payoff. |

[Landing spec](landing-spec.md) · [Story bible](story-bible.md) · [Life-support spec](../tech/life-support-spec.md)

## Resource and tool rules for the prototype

- **Budget cumulative parts.** Count the seal kit and replacement path, first Outgasser, and enough additional generation or extraction capacity to reach 5 kPa. Budget for shared play and both pacing presets. Price the first Outgasser and reaching 5 kPa as separate milestones. The existing solo curve assumes four Outgassers by sol 16 and five total panels, so funding one machine alone does not establish that the route is viable. Record what survives a lost tool, an accidental repair spend, or a replayed checklist. Essential cargo is one grant per planet, with a recovery route for critical losses.
- **Use the current recipe sheet as a constraint.** The Outgasser costs 10 iron, 6 copper, 2 glass, and 6 bricks in standard, or 8, 5, 2, and 5 in classroom. The stand-in items and approved quantities in the recipe data are the prototype reference. The Ops assembly interaction should accept counted stacks and use the correct preset. Verify the inputs, the gate, and delivery space before consuming anything. The current Oven is not a generic kiln, so specify a real processing action before requiring locally made bricks.
- **Hand-author readable sites.** Start with the shallow patch and cargo rack near Cairn. Place the two Basu prospect exposures farther out behind Phase 2. Use distinct layers, visible approach markers, and a clear way home. Track site, sampled layer, and tool for core results. Give samples a use at the bench, and let a characterized deposit be extracted in batches.
- **Use samples for decisions.** A core reports the yield and layer. The route adds an independent service cost. Surveying is distinct from extracting, and supplying water is distinct from placing the extractor. The playtest asks where the player would build and why.
- **Keep exploration finds separate.** This work covers essential resource logistics and a survey-to-extraction path. The approved Stage 4 exploration-finds MVP covers optional replicas, mineral finds, and the Fizz Test. Reuse authored sites and sample handling where practical while keeping optional finds off the critical path. Real-evidence life cases remain open-ended, per the exploration proposal.

[Recipe cost data](../../pack/data/mars/recipe_costs.json) · [Optional exploration finds](exploration-finds.md)

## Comparison with Subnautica

The useful comparison is the trip loop. A safe starting shelter gives the player a reason to venture out, and what they bring back changes the next trip. Cairn’s authored landmarks can give the same clarity through cargo manifests, exposed layers, and crew notes. In this game the drill answers a location question, and the outpost has to deliver something the colony can use. The Monitor then makes the effect of that build visible.

## First playtest

Paper-prototype one salvage trip and one Outgasser assembly before expanding the map. Write a manifest showing the source, tool, output, recovery path, and cumulative cost for each required part. Check that a fresh Survival group can find the materials and reach the first powered Outgasser with existing gate order, no operator commands, and no post-5 kPa supplies. Then run a separate two-site survey walkthrough and see whether players can explain their siting choice and identify the first delivery. The authoring decision for the near route can go to the Game Designer, material counts and feasibility to the Technical Lead, and the field instructions and evidence prompts to the Instructional Designer.
