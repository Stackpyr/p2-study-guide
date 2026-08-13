<!--
CST 338 Project 2 — README template.
Copy this file into the ROOT of your team's repository as README.md and keep it current.
This README is your project dashboard: it is the first thing the instructor reads when
grading, and a working, up-to-date README is part of your integration score.

GitHub Issues are your LIVE tracker — every slice task, enhancement, and scope decision is
an Issue: assigned to its owner, labeled (slice-1, testing, enhancement, will-not-do,
extra-credit), and closed by a PR via "Closes #N". The tables below link into those Issues
and PRs. Replace every <placeholder> and delete this comment before you submit.
-->

# Study Guide

Study Guide is a JavaFX application that allows users to create an account, navigate through trivia questions by category, take quizzes, and receive scores that are saved and tracked.

CST 338 Project 2 — Team **Team 1**.

## Team & Slice Ownership
| Slice | Owner | GitHub username | Issues | Branch(es) | PR(s) | Enhancement chosen               | Status      |
|-------|-------|-----------------|--------|------------|-------|----------------------------------|-------------|
| 1 — Accounts | Jason Hamilton | Stackpyr | | | | ScribeJava for Social OAUTH      | In-Progress |
| 2 — Question Bank | Analiza Boehning | aboehning-cs |#6, #7, #8 |analiza/question-bank, analiza/question-bank-scene, analiza/question-database |#15, #20 | Advanced Query (Search & Filter) | In-Progress  |
| 3 — Quiz Engine | Sawyer Phillips | sawyerphillips | #9, #10, #11 | sawyer/quiz-attempt-data, sawyer/quiz-scene, sawyer/quiz-scoring, sawyer/result-scene-alerts | #17, #18, #22, #23 | Alerts and Notifications | In-Progress |

_Status values: planned · in-progress · complete_

## WILL NOT DO (declared scope cuts)
_Slices and beyond-scope items we are consciously NOT building. Move an item to a tracked
Issue if the team later decides to attempt it for extra credit._

- Slice 4 — Leaderboard & History: not building (team size).
- Slide 5 - Study Mode & Tagging: not building (team size).

## Code Review Log
| PR | Author | Human reviewer(s)                 | AI review (link)                                                                        | Outcome                                                                                                     |
|----|--------|-----------------------------------|-----------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| [#12](https://github.com/Stackpyr/p2-study-guide/pull/12) | Jason Hamilton | Analiza Boehning, Sawyer Phillips | —                                                                                       | Merged                                                                                                      |
| [#13](https://github.com/Stackpyr/p2-study-guide/pull/13) | Jason Hamilton | Sawyer Phillips, Analiza Boehning | —                                                                                       | Merged                                                                                                      |
| [#14](https://github.com/Stackpyr/p2-study-guide/pull/14) | Jason Hamilton | Analiza Boehning, Sawyer Phillips | —                                                                                       | Merged                                                                                                      |
| [#15](https://github.com/Stackpyr/p2-study-guide/pull/15) | Analiza Boehning | Jason Hamilton, Sawyer Phillips   | —                                                                                       | Merged                                                                                                      |
| [#16](https://github.com/Stackpyr/p2-study-guide/pull/16) | Jason Hamilton | Sawyer Phillips, Analiza Boehning | —                                                                                       | Merged                                                                                                      |
| [#17](https://github.com/Stackpyr/p2-study-guide/pull/17) | Sawyer Phillips | Jason Hamilton, Analiza Boehning  | —                                                                                       | Merged                                                                                                      |
| [#18](https://github.com/Stackpyr/p2-study-guide/pull/18) | Sawyer Phillips | Jason Hamilton, Analiza Boehning  | —                                                                                       | Merged                                                                                                      |
| [#19](https://github.com/Stackpyr/p2-study-guide/pull/19) | Sawyer Phillips | Jason Hamilton, Analiza Boehning  | —                                                                                       | Merged                                                                                                      |
| [#20](https://github.com/Stackpyr/p2-study-guide/pull/20) | Analiza Boehning | Jason Hamilton, Sawyer Phillips   | —                                                                                       | Merged                                                                                                      |
| #6 | Analiza Boehning | Jason Hamilton                    |                                                                                         | Merged                                                                                                      |
| [#22](https://github.com/Stackpyr/p2-study-guide/pull/22) | Sawyer Phillips | Jason Hamilton, Analiza Boehning | —                                                                                       | Merged                                                                                                      |
| [#23](https://github.com/Stackpyr/p2-study-guide/pull/23) | Sawyer Phillips | Waiting for Jason Hamilton and Analiza Boehning | [AI review](https://github.com/Stackpyr/p2-study-guide/pull/23#issuecomment-5256267752) | Open |

## AI Usage Log
- **AI-drafted tests:** [TESTING.md](TESTING.md) — per Jason Hamilton (Slice 1: Accounts). Covers `Account`, `AccountRepository`, `AuthService`, and `DatabaseManager` unit tests; AI-authored methods are tagged `LLM GENERATED` in each test file.
- **AI code reviews:** [Analiza - AI REVIEW/ADJUDICATION] (https://github.com/Stackpyr/p2-study-guide/pull/27#issuecomment-5276019597) 

- **AI code reviews:** - [AI review](https://github.com/Stackpyr/p2-study-guide/pull/23#issuecomment-5256267752), [adjudication](https://github.com/Stackpyr/p2-study-guide/pull/23#issuecomment-5256602846), and [fixing commit](https://github.com/Stackpyr/p2-study-guide/commit/7b6661c1f97e90ed6ee1fef3dd1eed5d94cd980a).

## Extra Credit Log
| Item | Who | Evidence (Issue/PR) |
|------|-----|---------------------|
| | | |

## Build & Run
```
./gradlew run        # launch the app
./gradlew test       # run the test suite
```
Requirements: JDK <version>, JavaFX <version>. Any setup notes go here.
