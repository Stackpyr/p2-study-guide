# Study Guide

Study Guide is a JavaFX application that allows users to create an account, navigate through trivia questions by category, take quizzes, and receive scores that are saved and tracked.

CST 338 Project 2 — Team **Team 1**.

## Team & Slice Ownership
| Slice | Owner | GitHub username | Issues | Branch(es) | PR(s) | Enhancement chosen              | Status   |
|-------|-------|-----------------|--------|------------|-------|---------------------------------|----------|
| 1 — Accounts | Jason Hamilton | Stackpyr | #3, #4, #5, #29, #31 | jason/register-account, jason/ai-review-fixes, jason/admin-accnt-mgmt, jason/social-oauth | #13, #21, #26, #30 | ScribeJava for Social OAUTH | Complete |
| 2 — Question Bank | Analiza Boehning | aboehning-cs |#6, #7, #8 |analiza/question-bank, analiza/question-bank-scene, analiza/question-database, analiza/question-bank-category |#15, #20, #27 | Advanced Query (Search & Filter) | In-Progress |
| 3 — Quiz Engine | Sawyer Phillips | sawyerphillips | #9, #10, #11 | sawyer/quiz-attempt-data, sawyer/quiz-scene, sawyer/quiz-scoring, sawyer/result-scene-alerts, sawyer/quiz-crud-ui, sawyer/quiz-testfx | #17, #18, #22, #23, #28, #32 | Alerts and Notifications | In-Progress |

_Status values: planned · in-progress · complete_

## WILL NOT DO (declared scope cuts)
_Slices and beyond-scope items we are consciously NOT building. Move an item to a tracked
Issue if the team later decides to attempt it for extra credit._

- Slice 4 — Leaderboard & History: not building (team size).
- Slice 5 — Study Mode & Tagging: not building (team size).

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
| [#22](https://github.com/Stackpyr/p2-study-guide/pull/22) | Sawyer Phillips | Jason Hamilton, Analiza Boehning | —                                                                                       | Merged                                                                                                      |
| [#23](https://github.com/Stackpyr/p2-study-guide/pull/23) | Sawyer Phillips | Jason Hamilton | [AI review](https://github.com/Stackpyr/p2-study-guide/pull/23#issuecomment-5256267752), [adjudication](https://github.com/Stackpyr/p2-study-guide/pull/23#issuecomment-5256602846) | Merged |
| [#26](https://github.com/Stackpyr/p2-study-guide/pull/26) | Jason Hamilton | Analiza Boehning, Sawyer Phillips | [AI review of #21](https://github.com/Stackpyr/p2-study-guide/pull/21) (remediated in #26) | Merged |
| [#27](https://github.com/Stackpyr/p2-study-guide/pull/27) | Analiza Boehning | Jason Hamilton (changes requested), Sawyer Phillips (comment) | [AI review](https://github.com/Stackpyr/p2-study-guide/pull/27#issuecomment-5276019597) | Merged |
| [#28](https://github.com/Stackpyr/p2-study-guide/pull/28) | Sawyer Phillips | Jason Hamilton, Analiza Boehning | —                                                                                       | Merged                                                                                                      |
| [#30](https://github.com/Stackpyr/p2-study-guide/pull/30) | Jason Hamilton | Analiza Boehning, Sawyer Phillips | —                                                                                       | Merged                                                                                                      |
| [#32](https://github.com/Stackpyr/p2-study-guide/pull/32) | Sawyer Phillips | Jason Hamilton, Analiza Boehning | —                                                                                       | Merged   

## AI Usage Log
- **AI-drafted tests:** [TESTING.md](TESTING.md) — Jason Hamilton (Slice 1: Accounts). Covers `Account`, `AccountRepository`, `AuthService`, and `DatabaseManager` unit tests; AI-authored methods are tagged `LLM GENERATED` in each test file.
- **AI-drafted tests:** [TESTING.md](TESTING.md) — Sawyer Phillips (Slice 3: Quiz Engine). Covers `QuizAttemptRepository`, `QuizService`, and `QuizViewTest`; the tests were reviewed and adjusted for the project, and all 55 tests pass.
- **AI code reviews (Slice 1: Accounts):** AI code review of [PR #21](https://github.com/Stackpyr/p2-study-guide/pull/21), remediated in [PR #26](https://github.com/Stackpyr/p2-study-guide/pull/26) and tracked in [Issue #29](https://github.com/Stackpyr/p2-study-guide/issues/29) (Claude Code)
- **AI code reviews (Slice 2: Question Bank):** [PR #27](https://github.com/Stackpyr/p2-study-guide/pull/27) — [AI review](https://github.com/Stackpyr/p2-study-guide/pull/27#issuecomment-5276019597) (GitHub Copilot)
- **AI code reviews (Slice 3: Quiz Engine):** [PR #23](https://github.com/Stackpyr/p2-study-guide/pull/23) — [AI review](https://github.com/Stackpyr/p2-study-guide/pull/23#issuecomment-5256267752) (OpenAI Codex, flagged a possible duplicate quiz-attempt save in `QuizController.onSubmitClick()`), [adjudication](https://github.com/Stackpyr/p2-study-guide/pull/23#issuecomment-5256602846), and [fixing commit](https://github.com/Stackpyr/p2-study-guide/commit/7b6661c1f97e90ed6ee1fef3dd1eed5d94cd980a).

## Extra Credit Log
| Item                                                                                                                                                                                                                                                                                                           | Who | Evidence (Issue/PR) |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----|---------------------|
| (Accounts slice): TableView populated with live data for the Account Administration screen. It loads all accounts and renders them in a TableView backed by a FilteredList/SortedList that live-updates as the admin searches and after every account change | Jason Hamilton | [Issue #31](https://github.com/Stackpyr/p2-study-guide/issues/31) [PR #30](https://github.com/Stackpyr/p2-study-guide/pull/30)

## Build & Run
```
./mvnw javafx:run     # launch the app
./mvnw test           # run the test suite
```
Requirements: JDK 21, JavaFX 21.0.6 (resolved automatically by Maven per-OS via the profiles in `pom.xml`).

#Social sign-in (LinkedIn) requires the client id and secret to be defined in `src/main/resources/oauth.properties` file
These are not defined since this is a public repo.
