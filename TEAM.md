# Team Shared Mental Model

A shared mental model (SMM) is what every team member knows the same way: the task, the team, the vocabulary, and how we work. It lets members explain and predict each other's work, coordinate without spelling everything out, and adapt as the task changes (Cannon-Bowers et al., 1993).

**Why this file exists.** The LGD team is made of AI agents that start every task with no memory of earlier ones. A human team builds its shared model by working together over time. Ours has to be **written down**, read at the start of every task, and updated after every task. This file is that model.

### The loop

```
  team interactions ──► communication and coordination ──► shared mental model
         ▲                                                          │
         └──────────── better communication, less spelled out ◄────┘
                                  │
                                  ▼
                          team performance
```

1. Every agent **reads this file first**, before its role file and the task.
2. Agents work and communicate through reports and handoffs.
3. Every report ends with **Shared model updates**: new terms, decisions, lessons, or expectations the team should share.
4. The coordinating session merges those updates here. The Producer reviews this file at each roadmap stage.
5. The next task starts from a richer model, so less needs to be explained.

---

## 1. Task Model: What We're Making

- **The game:** an educational Minecraft modpack. Players terraform a hostile Mars in three phases, Survival Desperation → Industrial Geo-Engineering → Biome Seeding & Equilibrium, ending when they can remove the helmet and build the colonization beacon.
- **Learners:** ages 12–18, in two grade bands, aligned to NGSS. Played in classrooms (classroom mode, ~11–12 hours) and at home (~40 hours).
- **Two goals:** instructional value **and** market appeal. Neither wins by default. See "Two Goals" in `DESIGN.md`.
- **Nature of the problem:** ill-structured. No single right design; most big decisions need several disciplines.
- **Design pillars:** the planet is the quest log; every change is visible; pressure, not grind; compact before wide; your progress provokes the planet.
- **Where we are:** see "Where We Are Now" in `ROADMAP.md`.

## 2. Team Model: Who Knows What

Knowing who to ask is part of the shared model.

| Role | Expert in | Ask them about | Needs from others | Tends to argue for |
|---|---|---|---|---|
| Producer | Scope, schedule, risk, release | "Can we afford this?" "Who owns this?" | Effort estimates, risks, status | Feasibility, shipping |
| Technical Lead | Code, tools, data, LMS | "Can we build this, and what does it cost?" | Clear specs from design and learning | Simplicity, performance, privacy |
| Lead Game Designer | Mechanics, balance, story, UI, playtests | "Is it fun? Does it fit the loop?" | Objectives, science limits, art direction, tech constraints | **Appeal** |
| Lead Instructional Designer | Learners, objectives, assessment, feedback | "Will they learn it? How will we know?" | Mechanics, science content, data | **Instructional value** |
| Lead Artist | Visual style, visible progress, readability | "How will players see it?" | Design specs, science imagery | **Appeal**, clarity |
| Subject Matter Expert | Mars and Earth science, learners as a population | "Is it true? What do students get wrong?" | What the game simplifies | **Accuracy**, instructional value |

Healthy disagreement is expected. These leanings are why ill-structured problems go to review by several roles.

## 3. Shared Vocabulary

Use these words the same way everywhere.

| Term | Meaning |
|---|---|
| **Planetary Index** | The single, global set of planet values (pressure, temperature, hydrosphere, O2, CO2, biomass). The one source of truth. |
| **Contribution** | A rate a machine, flora, or event adds to or removes from an Index value. |
| **Gate** | A world-state condition that unlocks the next content. |
| **Lock in** | Once a gate is reached it stays unlocked, even if values later fall. (Breathable air is the exception: a live check.) |
| **Gameplay phase** | Phase 1, 2, or 3 of play. Not the same as a **roadmap stage**. |
| **Roadmap stage** | Development stage 0–7 in `ROADMAP.md`. |
| **Goal ID** | Learning goal code A1–D2 in `LEARNING.md`. |
| **Grade band** | Middle school (6–8) or high school (9–12) depth setting. |
| **Classroom mode** | Config preset: faster pacing, teacher tools, world pause. |
| **Colony Record** | In-game page where learners see which goals they've shown. |
| **Planetary Monitor** | In-game block showing Index values, trends, and causes. |
| **Action log** | Local, anonymous record of player actions, for assessment. |
| **PE / SEP / CCC** | NGSS performance expectation / science and engineering practice / crosscutting concept. |
| **Well-structured / ill-structured** | Problem types; decide how to handle and route a problem. See the agent role files. |
| **Handoff** | A message from one role to another in the standard format (To, Problem type, Problem, Context, Your view, Needed from them, Done when). |

## 4. Process Model: How We Work

- **Who owns what:** see the table in `README.md`. Edit only your own files; hand off the rest.
- **Task flow:** user or Producer sets a task → role does it → report with handoffs → coordinating session merges, commits, and routes handoffs.
- **Well-structured problems:** solved by one role, reported.
- **Ill-structured problems:** framed, viewed from several disciplines, options with trade-offs for both goals, routed to the Producer for review or to the user for a values or scope call.
- **Where decisions live:** gameplay in `DESIGN.md`, learning in `LEARNING.md`, technical in `ARCHITECTURE.md`, schedule in `ROADMAP.md`. This file keeps a short log pointing to them.
- **Evidence beats argument:** playtests, prototypes, and sources settle disagreements where possible.

## 5. What Teammates Can Count On

Implicit expectations, so we don't have to restate them each time.

- Every claim of fact has a source or is marked **unverified**.
- Every report uses the standard format and names the problem type.
- No one changes another role's files.
- No one reopens a settled decision without new evidence; concerns are flagged, not acted on.
- Text for students is short and plain (about grade 6–7 at middle school depth).
- Nothing personal about players ever leaves the server's computer.
- No one commits or pushes except the coordinating session.

## 6. Decision Log

Short pointers. Details live in the owning document.

| Date | Decision | Where |
|---|---|---|
| 2026-11-26 | Global progress; singleplayer and small servers; ~40 hours | DESIGN.md: Decisions |
| 2026-11-26 | Custom Mars dimension; gates lock in; fauna scales with infrastructure plus a threshold config | DESIGN.md: Decisions |
| 2026-11-26 | Ages 12–18, two grade bands; NGSS; classroom mode; local anonymous logging | LEARNING.md: Decisions |
| 2026-11-26 | ISD / ADDIE as the learning design framework | LEARNING.md |
| 2026-11-26 | Systems in Java, content in data, glue in KubeJS; server authoritative | ARCHITECTURE.md |
| 2026-11-26 | Two goals: instructional value and market appeal, neither wins by default | DESIGN.md: Two Goals |

## 7. Team Learning Log

What we've learned about working together. Add an entry whenever a task teaches the team something about coordination, communication, or the problem.

| Date | Lesson | Source |
|---|---|---|
| 2026-11-26 | Agents start with no memory, so the shared model must be written down and read first. | Team setup |
| 2026-11-26 | Gameplay phases and roadmap stages were easy to confuse; the vocabulary now separates them. | Early docs review |
