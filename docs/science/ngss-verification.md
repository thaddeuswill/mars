# NGSS Verification

Owner: Lead Subject Matter Expert. Reviewed: 2026-11-26.
Scope: every performance expectation (PE) code in `LEARNING.md` section 6.1.

**How this was checked.** This session's network blocked nextgenscience.org, so I could not open the official PE pages directly. Each PE was checked by web search against:
- search-indexed copies of nextgenscience.org pages,
- the California Department of Education CA NGSS pages, and
- the NGSS evidence statements.

California adopted the NGSS PEs, so its pages carry the same text. The verification column says how strong each check was:
- **Verified:** the full PE sentence matched in an official or state source.
- **Partly verified:** the key wording matched in secondary summaries, and the full sentence comes from my knowledge of the standard.
- **Unverified:** not checked against any source.

Before the educator guide is published, someone with normal web access should open each nextgenscience.org page and confirm the text word for word. It takes about 30 minutes.

Mapping key: **Strong** (the PE directly asks for what the goal teaches), **Moderate** (there is overlap, but the game covers only part of the PE, or the PE is about Earth and the game is about Mars), **Weak** (thin connection), **Wrong** (does not fit).

**Note on Earth vs. Mars.** Many Earth and space science PEs are written about *Earth's* systems. A Mars game supports them as an analog. To count fully, it needs the D2 "on Earth" content and the debrief or transfer task. That is why several mappings below are "Moderate" rather than "Strong".

---

## Summary

| Code | Goal | Verdict | Action |
|---|---|---|---|
| MS-ESS1-3 | A1 | Moderate | Keep. **Change the A1 post-test from recall to data interpretation** (see note) |
| MS-PS1-4 | A2 | Strong | Keep. Cite its DCI PS1.A, which covers pressure |
| HS-ESS2-5 | A2 | **Weak** | Downgrade to "related". No HS PE covers phase changes under pressure well |
| MS-ESS3-5 | A3, D2 | Moderate (A3) / Strong (D2) | Keep. It is mainly about Earth evidence, so it depends on the D2 content |
| HS-ESS2-4 | A3, D2 | Strong | Keep |
| HS-PS4-4 | A4 | Moderate | Keep. Note that it covers EM radiation only, not particle radiation or magnetic shielding |
| MS-LS1-6 | A5 | Strong | Keep |
| HS-LS2-5 | A5 | Strong | Keep |
| HS-ESS2-6 | A5 | Moderate | Keep. Needs the Monitor to show carbon amounts per reservoir |
| MS-LS2-4 | B1/B2 | Moderate | Keep for B1. Feedback loops (B2) are not in this PE |
| HS-ESS2-2 | B1/B2 | Strong | Keep. Add an ice–albedo feedback to the game if possible |
| HS-ESS3-6 | B3, D2 | Moderate | Keep. The game is itself a computational representation |
| MS-LS2-5 | B4/B5 | **Weak** | Replace with MS-ETS1-2 (B4). Optionally add MS-ESS3-3 (B5), **unverified** |
| HS-LS2-6 | B4/B5 | Moderate | Keep for B4 (stability and change; "changing conditions may result in a new ecosystem") |
| HS-LS2-7 | B4/B5 | **Weak** | Downgrade. Use HS-ETS1-3 for B4/B5 instead |
| MS-ETS1-1 to 4 | C1/C2 | Strong | Keep |
| HS-ETS1-1 to 4 | C1/C2 | Strong (ETS1-3, ETS1-4), Moderate (ETS1-1, ETS1-2) | Keep. Also map HS-ETS1-3 to B4 |

---

## PE-by-PE review

### A1 Mars vs. Earth

**MS-ESS1-3.** "Analyze and interpret data to determine scale properties of objects in the solar system."
- Clarification (summary): the emphasis is on analyzing data from Earth-based instruments, space telescopes and spacecraft to find similarities and differences among solar system objects. Examples of scale properties include the sizes of layers (such as crust and atmosphere), surface features, and orbital radius. **Assessment boundary: assessment does not include recalling facts about the properties of planets and other solar system bodies.**
- Sources: https://www.nextgenscience.org/pe/ms-ess1-3-earths-place-universe ; https://www2.cde.ca.gov/cacs/id/web/29058 ; https://thewonderofscience.com/msess13
- Verification: **Verified.**
- Mapping: **Moderate.** Comparing Mars and Earth with Monitor data fits "analyze and interpret data" and "similarities and differences". Pressure, temperature and composition are a stretch of "scale properties", though "size of layers such as … atmosphere" partly covers them.
- **Conflict:** the A1 measure in LEARNING.md 0.6 is a written, no-notes post-test, which is recall. The PE's assessment boundary excludes recall. **Fix:** make the A1 item a data task. Give students a Mars/Earth data table and ask them to compare and explain.
- HS: "— (background knowledge)" is reasonable. No HS ESS1 PE fits well.

### A2 Liquid water

**MS-PS1-4.** "Develop a model that predicts and describes changes in particle motion, temperature, and state of a pure substance when thermal energy is added or removed."
- Clarification (summary): qualitative molecular-level models of solids, liquids and gases. They show that adding or removing thermal energy changes the particles' kinetic energy until a change of state occurs. Examples of pure substances include water and carbon dioxide.
- Supporting DCI PS1.A: "The changes of state that occur with variations in temperature or pressure can be described and predicted using these models of matter."
- Sources: https://www2.cde.ca.gov/cacs/id/web/29099 ; https://thewonderofscience.com/msps14 ; https://snexplores.org/ngss/ms-ps1-4
- Verification: **Verified** (PE text and the DCI sentence).
- Mapping: **Strong**, via the DCI. The PE sentence itself names only thermal energy. **Recommendation:** in LEARNING.md, cite "MS-PS1-4 (DCI PS1.A: state changes with temperature *or pressure*)" so teachers can see why pressure belongs here.

**HS-ESS2-5.** "Plan and conduct an investigation of the properties of water and its effects on Earth materials and surface processes."
- Clarification (summary): mechanical and chemical investigations with water and solid materials, such as stream transport and deposition, erosion, frost wedging, chemical weathering and solubility.
- Sources: https://www2.cde.ca.gov/cacs/id/web/29173 ; https://learninglab.si.edu/standards/HS-ESS2-5/2047
- Verification: **Verified.**
- Mapping: **Weak.** The PE is about what water *does to rock and land*, not about when water can be liquid. It would only fit if the game showed water eroding regolith or frost wedging, which it currently does not.
- Better options: no HS PE targets phase behavior under pressure directly.
  - **HS-PS1-3** lists vapor pressure and boiling point among the bulk properties to investigate, but it is about the strength of forces between particles. Its fit is also weak, and its wording is **unverified** in this review.
  - **Recommendation:** list A2 at HS as "extends MS-PS1-4 (PS1.A)". Keep HS-ESS2-5 only as "related", or add a small erosion or frost-wedging visual (ice → water → regolith change) so the link is real. That is a Game Designer decision.

### A3 Greenhouse effect

**MS-ESS3-5.** "Ask questions to clarify evidence of the factors that have caused the rise in global temperatures over the past century."
- Clarification (summary): factors include human activities (fossil fuel combustion, cement production, agriculture) and natural processes (solar changes, volcanic activity). Evidence includes graphs and maps of temperature and of gas levels such as CO2 and methane. Emphasis is on the major role of human activities.
- Sources: https://nextgenscience.org/pe/ms-ess3-5-earth-and-human-activity ; https://www2.cde.ca.gov/cacs/id/web/29070
- Verification: **Verified.**
- Mapping: **Moderate for A3, Strong for D2.** The PE is about evidence for *Earth's recent* warming, and the SEP is *asking questions*. The game teaches the mechanism on Mars. It meets the PE only when the D2 debrief turns to Earth's temperature and CO2 data. **Recommendation:** add the SEP "Asking questions and defining problems" to section 6.2, and build the Phase 2 debrief around Earth CO2/temperature graphs.

**HS-ESS2-4.** "Use a model to describe how variations in the flow of energy into and out of Earth's systems result in changes in climate."
- Clarification (summary): causes of climate change differ by timescale, from volcanic eruptions and ocean circulation (1–10 years), through human activity and solar output, to orbital changes and long-term changes in atmospheric composition. Assessment boundary: climate change results are limited to surface temperatures, precipitation patterns, glacial ice volumes, sea levels and biosphere distribution.
- Sources: https://see.isbscience.org/ngss/hs-ess2-4/ ; https://learninglab.si.edu/standards/HS-ESS2-4/2046
- Verification: **Verified** (PE text). The clarification was **partly verified**, because the search copy was cut off at the longest timescales.
- Mapping: **Strong.** Greenhouse emitters and orbital mirrors change the energy flowing in and out, and surface temperature and ice respond. That is exactly a "model of energy flow → climate change".

### A4 Radiation

**HS-PS4-4.** "Evaluate the validity and reliability of claims in published materials of the effects that different frequencies of electromagnetic radiation have when absorbed by matter."
- DCI (summary): longer wavelengths absorbed by matter mostly turn into heat. Shorter wavelengths (UV, X-rays, gamma rays) can ionize atoms and damage living cells. Assessment boundary: qualitative descriptions only.
- Sources: https://www.nextgenscience.org/pe/hs-ps4-4-waves-and-their-applications-technologies-information-transfer ; https://www2.cde.ca.gov/cacs/id/web/29161 ; evidence statement: https://www.nextgenscience.org/sites/default/files/evidence_statement/black_white/HS-PS4-4%20Evidence%20Statements%20June%202015%20asterisks.pdf
- Verification: **Verified.**
- Mapping: **Moderate.**
  - It fits the UV part of Mars surface radiation, and ionizing damage to cells.
  - It does **not** cover charged-particle radiation (solar particles, cosmic rays), and it does not cover magnetic-field shielding, which is half of goal A4.
  - Its practice is "evaluate claims in published materials". The game would need a "read more" page or a classroom task where students judge a claim, for example "a spacesuit blocks all radiation".
- MS: "— (introduce as a concept)" is acceptable. **MS-PS4-2** (waves are reflected, absorbed or transmitted through materials) could support shielding blocks. Its wording is **unverified** in this review.

### A5 CO2/O2 exchange

**MS-LS1-6.** "Construct a scientific explanation based on evidence for the role of photosynthesis in the cycling of matter and flow of energy into and out of organisms."
- Clarification (summary): emphasis on tracing the movement of matter and the flow of energy. Assessment does not include the biochemical mechanism of photosynthesis.
- Sources: https://www2.cde.ca.gov/cacs/id/web/29080 ; https://www.nextgenscience.org/topic-arrangement/msmatter-and-energy-organisms-and-ecosystems
- Verification: **Verified.**
- Mapping: **Strong.** Algae vats and flora turn CO2 into O2 using light.

**HS-LS2-5.** "Develop a model to illustrate the role of photosynthesis and cellular respiration in the cycling of carbon among the biosphere, atmosphere, hydrosphere, and geosphere."
- Assessment boundary: does not include the specific chemical steps of photosynthesis and respiration.
- Sources: https://www2.cde.ca.gov/cacs/id/web/29126 ; https://learninglab.si.edu/standards/HS-LS2-5/2020
- Verification: **Verified.** One secondary source says "oceans" instead of "hydrosphere"; the CDE text matches the wording above.
- Mapping: **Strong**, provided the game also models *respiration*: fauna and decomposition releasing CO2. If the game has only photosynthesis, the link is Moderate. **Handoff to the Game Designer:** fauna and decaying biomass should add a little CO2.

**HS-ESS2-6.** "Develop a quantitative model to describe the cycling of carbon among the hydrosphere, atmosphere, geosphere, and biosphere."
- Clarification (summary): emphasis on modeling the biogeochemical cycles that include carbon cycling through the ocean, atmosphere, soil and biosphere (including humans).
- Sources: https://www2.cde.ca.gov/cacs/id/web/29174 ; https://learninglab.si.edu/standards/HS-ESS2-6/2048
- Verification: **Verified.**
- Mapping: **Moderate.** The PE asks for a *quantitative* model across four reservoirs. The game tracks atmospheric CO2 and biomass only. It becomes Strong if the Monitor (HS depth) shows carbon stored in each reservoir (air, biomass, soil or carbonate, water) and the flows between them.

### B1 / B2 Systems and feedback

**MS-LS2-4.** "Construct an argument supported by empirical evidence that changes to physical or biological components of an ecosystem affect populations."
- Clarification (summary): emphasis on recognizing patterns in data, making warranted inferences about changes in populations, and evaluating empirical evidence that supports arguments about ecosystem change.
- Sources: https://www2.cde.ca.gov/cacs/id/web/29086 ; https://thewonderofscience.com/msls24
- Verification: **Verified.**
- Mapping: **Moderate for B1.** Temperature and water drive flora and fauna populations. **Weak for B2**, because feedback loops are not part of this PE.

**HS-ESS2-2.** "Analyze geoscience data to make the claim that one change to Earth's surface can create feedbacks that cause changes to other Earth systems."
- Clarification (summary): examples should include climate feedbacks. One example is how more greenhouse gas raises temperature, which melts glacial ice, which reduces the sunlight Earth's surface reflects. Another example involves the loss of wetlands and local humidity.
- Sources: https://nextgenscience.org/topic-arrangement/hsearths-systems ; https://www2.cde.ca.gov/cacs/id/web/29170 ; https://thewonderofscience.com/hsess22
- Verification: **Verified** (PE text). The clarification was **partly verified** (third-party copy).
- Mapping: **Strong** for B2. **Suggestion:** the clarification's own example is the ice–albedo feedback. If melting ice darkens the surface and speeds up warming in the game, the mapping is close to perfect, and it is real physics.

### B3 Prediction from models

**HS-ESS3-6.** "Use a computational representation to illustrate the relationships among Earth systems and how those relationships are being modified due to human activity."
- Clarification (summary): Earth systems to consider include the hydrosphere, atmosphere, cryosphere, geosphere and/or biosphere. Example: higher atmospheric CO2 increases photosynthetic biomass on land and ocean acidification. Assessment boundary: does not include running computational representations; it is limited to using the published results of scientific computational models.
- Sources: https://www2.cde.ca.gov/cacs/id/web/29181 ; https://learninglab.si.edu/standards/HS-ESS3-6/2056
- Verification: **Verified.**
- Mapping: **Moderate.** The Planetary Index is a computational representation, and the prediction prompts use it. But the PE is about *Earth* systems changed by *human activity*. The game matches the structure, by analogy. Note that the assessment boundary means students are not expected to build or run models. Using the game's outputs is within the spirit of the PE.

### B4 / B5 Trade-offs and consequences

**MS-LS2-5.** "Evaluate competing design solutions for maintaining biodiversity and ecosystem services."
- Clarification (summary): examples of ecosystem services include water purification, nutrient recycling and prevention of soil erosion. The supporting DCIs are LS2.C, LS4.D and ETS1.B.
- Sources: https://nextgenscience.org/pe/ms-ls2-5-ecosystems-interactions-energy-and-dynamics ; https://www2.cde.ca.gov/cacs/id/web/29087
- Verification: **Verified.**
- Mapping: **Weak.** The game does not ask players to compare designs for keeping biodiversity or ecosystem services.
- Better options:
  - **MS-ETS1-2** (below) for B4 trade-offs.
  - For B5 (unintended consequences), **MS-ESS3-3** ("Apply scientific principles to design a method for monitoring and minimizing a human impact on the environment") would fit if fauna blowback is framed as an impact to monitor and minimize. Its wording is **unverified** in this review.

**HS-LS2-6.** "Evaluate claims, evidence, and reasoning that the complex interactions in ecosystems maintain relatively consistent numbers and types of organisms in stable conditions, but changing conditions may result in a new ecosystem."
- Sources: https://nextgenscience.org/pe/hs-ls2-6-ecosystems-interactions-energy-and-dynamics ; https://www2.cde.ca.gov/cacs/id/web/29127 ; https://mynasadata.larc.nasa.gov/hs-ls2-6
- Verification: **Verified.**
- Mapping: **Moderate for B4.** "Stable conditions" maps to the equilibrium band, and "changing conditions → a new ecosystem" is what terraforming is. To meet the PE's practice (evaluating claims, evidence and reasoning), the Phase 3 debrief needs a claim-evaluation prompt.

**HS-LS2-7.** "Design, evaluate, and refine a solution for reducing the impacts of human activities on the environment and biodiversity."
- Sources: https://www2.cde.ca.gov/cacs/id/web/29128 ; https://snexplores.org/ngss/hs-ls2-7
- Verification: **Verified.**
- Mapping: **Weak.** The game's core activity is *increasing* human impact on a planet. Only the "design and refine a solution to reduce fauna blowback" angle fits. The CA item specifications do include "identify a likely unintended consequence", which supports B5 a little.
- Better option: **HS-ETS1-3**, which explicitly covers trade-offs and environmental impacts.

### C1 / C2 Engineering design

**MS-ETS1-1.** "Define the criteria and constraints of a design problem with sufficient precision to ensure a successful solution, taking into account relevant scientific principles and potential impacts on people and the natural environment that may limit possible solutions." **Verified.**

**MS-ETS1-2.** "Evaluate competing design solutions using a systematic process to determine how well they meet the criteria and constraints of the problem." **Verified.**

**MS-ETS1-3.** "Analyze data from tests to determine similarities and differences among several design solutions to identify the best characteristics of each that can be combined into a new solution to better meet the criteria for success." **Partly verified.**

**MS-ETS1-4.** "Develop a model to generate data for iterative testing and modification of a proposed object, tool, or process such that an optimal design can be achieved." **Partly verified** (evidence statement wording matched).

- Sources: https://www.hempsteadschools.org/cms/lib/NY01920790/Centricity/Domain/206/MS-ETS1%2004.09.14.pdf (copy of the NGSS MS-ETS1 page) ; https://www.usd417.net/vimages/shared/vnews/stories/4a395f6f6c1fb/MS%20ETS%20Evidence%20Statements%20June%202015%20asterisks.pdf (NGSS evidence statements copy)
- Mapping: **Strong.** C1 (limited O2, water and power is a design problem with constraints) fits ETS1-1. C2 (breach → diagnose → rebuild) fits ETS1-3 and ETS1-4. LEARNING.md's summary "define problems, evaluate, test, and improve solutions" is fair.

**HS-ETS1-1.** "Analyze a major global challenge to specify qualitative and quantitative criteria and constraints for solutions that account for societal needs and wants." **Partly verified.**

**HS-ETS1-2.** "Design a solution to a complex real-world problem by breaking it down into smaller, more manageable problems that can be solved through engineering." **Partly verified.**

**HS-ETS1-3.** "Evaluate a solution to a complex real-world problem based on prioritized criteria and trade-offs that account for a range of constraints, including cost, safety, reliability, and aesthetics, as well as possible social, cultural, and environmental impacts." **Partly verified.**

**HS-ETS1-4.** "Use a computer simulation to model the impact of proposed solutions to a complex real-world problem with numerous criteria and constraints on interactions within and between systems relevant to the problem." **Verified** (key phrase). The ending is **partly verified**.

- Sources: https://www.nextgenscience.org/topic-arrangement/hsengineering-design ; https://nextgenscience.org/node/956 (HS-ETS1-4) ; https://www.cde.ca.gov/Ci/pl/documents/cangsshsengdesign-dci.pdf
- Mapping:
  - HS-ETS1-4: **Strong**. The game is a computer simulation of interacting systems.
  - HS-ETS1-3: **Strong**, and it is also the best HS code for **B4/B5**.
  - HS-ETS1-1: **Moderate**. Terraforming Mars is not a "major global challenge" for society today. Pair it with an Earth challenge in the transfer task.
  - HS-ETS1-2: **Moderate**. The phases do break a huge problem into smaller ones.

### D2 Link to Earth
MS-ESS3-5 (**Strong**), HS-ESS2-4 (**Strong**), HS-ESS3-6 (**Moderate**): see above. All three depend on the D2 journal and debrief content that LEARNING.md lists as a gap.

### D1 Ethics
No PE is listed, which is correct. NGSS PEs don't assess values. The SEP "Engaging in argument from evidence" covers the reasoning part.

---

## SEP and CCC names (sections 6.2, 6.3)

All six listed SEPs and all five listed CCCs are real NGSS practice and concept names. This is from the NRC *Framework for K-12 Science Education* (2012). The list was **not re-fetched** in this review.
- Missing but relevant: the SEP **"Asking questions and defining problems"**. It is the practice of MS-ESS3-5 and the ETS1-1 PEs.
- Missing CCC: **"Patterns"** (Monitor trends).
- The official CCC name is "Energy and matter: flows, cycles, and conservation". The short form is fine.

---

## Proposed replacement for LEARNING.md 6.1 (for the Instructional Designer)

| Goal | Middle school (6–8) | High school (9–12) |
|---|---|---|
| A1 Mars vs. Earth | MS-ESS1-3 (assessed with data, not recall) | — (background knowledge) |
| A2 Liquid water | MS-PS1-4 (DCI PS1.A: state changes with temperature or pressure) | Extends MS-PS1-4. Related: HS-ESS2-5 (only if water–regolith effects are shown in game) |
| A3 Greenhouse effect | MS-ESS3-5 (via the Earth debrief) | HS-ESS2-4 |
| A4 Radiation | — (introduce as a concept) | HS-PS4-4 (EM radiation only; magnetic shielding is beyond this PE) |
| A5 CO2/O2 exchange | MS-LS1-6 | HS-LS2-5; HS-ESS2-6 (needs carbon reservoirs on the Monitor) |
| B1 / B2 Systems and feedback | MS-LS2-4 (B1) | HS-ESS2-2 |
| B3 Prediction from models | — (practice, see SEPs) | HS-ESS3-6 |
| B4 / B5 Trade-offs and consequences | MS-ETS1-2 | HS-ETS1-3; HS-LS2-6 (stability and change) |
| C1 / C2 Engineering design | MS-ETS1-1 to MS-ETS1-4 | HS-ETS1-1 to HS-ETS1-4 |
| D2 Link to Earth | MS-ESS3-5 | HS-ESS2-4, HS-ESS3-6 |
