# Normalization — Normal Forms (CST 363)

## Cheat Sheet
| Form | Requires | Quick violation example | Fix |
|---|---|---|---|
| **1NF** | Atomic values; no repeating groups | `courses = "CS101, MATH200"` in one field (or `course1, course2, course3` columns) | One course per row |
| **2NF** | 1NF + no **partial dependency** (non-key attribute depends on only *part* of a composite key) | `(student_id, course_id, student_name, grade)` — `student_name` depends only on `student_id`, not the full key | Split: `Student(student_id, name)` + `Enrollment(student_id, course_id, grade)` |
| **3NF** | 2NF + no **transitive dependency** (non-key attribute depends on *another non-key attribute*, not directly on the key) | `(employee_id, employee_name, department_id, department_name)` — `department_name` depends on `department_id`, which depends on `employee_id` | Split: `Department(department_id, department_name)` + `Employee(employee_id, employee_name, department_id)` |
| **BCNF** | 3NF + every **determinant** (left side of any nontrivial FD) is a candidate key | Rare: a non-key attribute determines part of a composite candidate key | Split so every determinant becomes its own table's key |

Each row builds on the one above it — a table can't be in 2NF without already being in 1NF, can't be 3NF without 2NF, can't be BCNF without 3NF. Full walkthroughs with sample data are in the matching sections below.

## Definitions
Quick-reference glossary for every term used below. Full walkthroughs with sample data live in the matching sections further down the page.

- **Functional Dependency (FD)**: `X → Y` — if two rows agree on `X`, they must also agree on `Y`. Plain version: a "little rule" that says *if you know X, you always know Y*. Comes from **domain rules**, not merely patterns in the current data ("no duplicates so far" is not an FD).
- **Determinant**: the left side of a functional dependency. In `X → Y`, `X` is the determinant — whatever values it takes force a single value of `Y`. Plain version: the "knowing this" part of a little rule.
- **Nontrivial functional dependency**: an FD `X → Y` where `Y` is *not* already a subset of `X`. Excludes dependencies that are automatically true and say nothing new — e.g. `(student_id, course_id) → student_id` is trivially true for any relation, since `student_id` is already part of the left side.
- **Candidate key**: a minimal set of attributes that uniquely identifies a row (no smaller subset also does). A relation can have more than one candidate key; the chosen one becomes the primary key.
- **Atomic value**: a value treated as one indivisible unit — the application never needs to split it apart. Domain-dependent, not about length: `zip = "93940"` is atomic even though it's multi-character.
- **Multivalued attribute**: one *field* holding more than one value at once, e.g. `courses = "CS101, MATH200"`. A 1NF violation.
- **Repeating group**: several separate *columns* (`course1, course2, course3`) repeating the same kind of fact side by side, instead of one row per fact. A different-looking shape of the same 1NF violation as a multivalued attribute.
- **Functionally dependent / full functional dependency**: a non-key attribute's value is pinned down by the *entire* key — every column of a composite key is needed, not just part of it. This is the "good" case 2NF requires.
- **Partial dependency**: a non-key attribute depends on only *part* of a composite key, not the whole key — only possible when the key has more than one column. A 2NF violation.
- **Transitive dependency**: a non-key attribute depends on *another non-key attribute*, which in turn depends on the key — written `Key → A → B`, meaning `B` reaches the key only indirectly, through `A`. A 3NF violation.

## Why Normalize
Combining independent facts into one relation causes attributes to repeat across rows whenever the same real-world fact (e.g. a department's building/budget) applies to many rows (e.g. many instructors in that department). The problem isn't the foreign key — it's storing the same fact repeatedly. This leads to three anomalies:
- **Insert anomaly**: a fact can't be inserted without also inserting unrelated/redundant data (e.g. can't record a new department until an instructor is assigned to it, if the combined table's key is the instructor's ID).
- **Delete anomaly**: deleting one fact accidentally erases another fact that should survive (e.g. deleting the last instructor in a department loses the department's building/budget, since they were only stored on instructor rows).
- **Update anomaly**: the same fact is stored in multiple rows; updating one copy and missing another leaves the data inconsistent (e.g. a department's budget changes in one instructor's row but not another's).

**Fix**: separate facts according to what they describe — store each fact once, where its meaning belongs (e.g. split into `department(dept_name PK, building, budget)` and `instructor(ID PK, name, salary, dept_name FK)`).

## Functional Dependency (FD) — Walkthrough
See **Definitions** above for the core definition. Example: `student_id → name`.

```
student_id | name
-----------+-------
1001       | Alice
1002       | Bob
```
Every time `student_id = 1001` shows up, `name` is `Alice` — guaranteed by the domain, not just true in this snapshot. Note the dependency only runs one direction: `name → student_id` would NOT hold, because nothing stops two different students from being named "Alice."

## Dependency Types — Quick Preview
Partial and transitive dependency (defined in **Definitions** above) are the two specific patterns the normal forms are built around:

- **Partial dependency** preview: in `(student_id, course_id, student_name, grade)` keyed by `(student_id, course_id)`, `student_name` only needs `student_id` — the first half of the key — to be determined. Full walkthrough in the 2NF section below.
- **Transitive dependency** preview: in `(employee_id, employee_name, department_id, department_name)`, `department_name` depends on `department_id`, and `department_id` depends on `employee_id` — so `department_name` reaches the key only by riding along with `department_id`. Full walkthrough in the 3NF section below.

## Normalization Intuition
Every non-key attribute should depend on **"the key, the whole key, and nothing but the key."**
- *the whole key* → no **partial dependency** (this is what 2NF forbids).
- *nothing but the key* → no **transitive dependency** (this is what 3NF forbids).

## 1NF — First Normal Form
1NF rules out two different-looking shapes of the same underlying mistake: representing a one-to-many fact (one student, many courses) *sideways within a single row* instead of as separate rows. See **Definitions** above for *atomic value*, *multivalued attribute*, and *repeating group*.

**1. Multivalued attribute** — one *field* holds more than one value.

(Not to be confused with *composite* attributes like `address = street+city+state+zip` — that's about decomposing meaningful parts, a separate design question from "no multiple values of the same kind in one cell." A `full_name = "Alice Smith"` field is only non-atomic if something needs to query by last name alone — the moment `WHERE last_name = 'Smith'` is needed, it's secretly two values and should become two columns.)

Violates 1NF — `courses` crams multiple values into one field:
```
student_id | name  | courses
-----------+-------+--------------------
1001       | Alice | CS101, MATH200
1002       | Bob   | CS101
```

**2. Repeating group** — instead of one multivalued field, several separate *columns* repeat the same kind of fact side by side:
```
student_id | name  | course1 | course2 | course3
-----------+-------+---------+---------+--------
1001       | Alice | CS101   | MATH200 | NULL
1002       | Bob   | CS101   | NULL    | NULL
```
Structurally different from case 1 (three distinct columns, not one crowded field), but the same mistake — and it adds its own problems: a hard cap on how many courses one student can have, wasted `NULL`s, and a query like "who's taking CS101" needs `course1 OR course2 OR course3` instead of one clean filter.

**Fix for both**: one course per row (this also sets up the 2NF fix below, since it introduces the composite key `(student_id, course_id)`):
```
student_id | name  | course_id
-----------+-------+----------
1001       | Alice | CS101
1001       | Alice | MATH200
1002       | Bob   | CS101
```

## 2NF — Second Normal Form
- Relation is already in 1NF.
- Every non-key attribute is **fully** functionally dependent on the *entire* primary key — no **partial dependency** (see Definitions).

Violation example: `(student_id, course_id, student_name, grade)` with composite key `(student_id, course_id)` — both columns together are needed to identify a row (one student takes many courses, one course has many students), so `student_id` is the first key component and `course_id` is the second.
```
student_id | course_id | student_name | grade
-----------+-----------+--------------+------
1001       | CS101     | Alice        | A
1001       | MATH200   | Alice        | B+
1002       | CS101     | Bob          | B
```
Check each non-key column against the *whole* key:
- `grade` needs **both** `student_id` and `course_id` to pin down a value (Alice has two different grades for two different courses) — fully dependent on the whole key. Fine.
- `student_name` only needs `student_id` — every row with `student_id = 1001` says `Alice`, regardless of `course_id`. That's a **partial dependency** → violates 2NF.

Fix — pull `student_name` into its own table keyed by just `student_id`, since that's genuinely all it depends on:
```
Student(student_id, student_name)
Enrollment(student_id, course_id, grade)
```

## 3NF — Third Normal Form
- Relation is already in 2NF.
- Every non-key attribute depends **directly** on the key — no **transitive dependency** (see Definitions).

Violation example: `(employee_id, employee_name, department_id, department_name)` with key `employee_id`:
```
employee_id | employee_name | department_id | department_name
------------+----------------+----------------+----------------
2001        | Alice          | D10            | Accounting
2002        | Bob            | D20            | HR
2003        | Charlie        | D10            | Accounting
```
`employee_id → department_id` holds (each employee is in one department), and `department_id → department_name` holds (each department code has one name) — chain them together and `employee_id → department_name` holds too, but only *transitively*, through `department_id`. `department_name` doesn't really describe the employee at all; it describes the department. Notice `D10`/`Accounting` is repeated on rows 1 and 3 — the same redundancy problem 2NF was solving, just one hop further away from the key.

Fix — pull the transitively-dependent columns into their own table, keyed by the attribute they actually depend on:
```
Department(department_id, department_name)
Employee(employee_id, employee_name, department_id)
```
`department_id` stays in `Employee` as a foreign key — a foreign key is always considered directly dependent on the primary key (it identifies *which* department, which is a fact about the employee), so this doesn't reintroduce the problem.

## BCNF — Boyce-Codd Normal Form

**Plain version first.** Every table has little rules like "if you know X, you always know Y" (that's a functional dependency). The "X" part is called the *left side* (the formal word is *determinant*). A *key* is the smallest set of columns that can pick out exactly one row — a table can have more than one valid key.

**BCNF's whole rule**: for every little rule in the table, its left side must be a *full* key. No partial credit. If any rule's left side is only *part of* a key, the table fails BCNF, even if the design otherwise looks clean.

**Why this matters** — it's always about repeated/wasted data. Example: a school table of (student, class, teacher), where Finch teaches Art and nothing else:
```
student | class | teacher
--------+-------+--------
Alice   | Art   | Finch
Bob     | Art   | Finch
Carol   | Math  | Diaz
```
Two little rules hold here: "student + class tells you the teacher" and "teacher tells you the class" (each teacher only teaches one subject). That second rule's left side is just "teacher" — alone, teacher isn't a full key (you still need the student to pin down one row). So this table fails BCNF. And you can see why it matters: "Finch teaches Art" gets written down once per student, Alice's row and Bob's row both say it. Move Finch to Math and you have to catch and fix every row, or the table ends up contradicting itself.

**Formal version, for the exam**: a table is in BCNF when it has all of the following:
- a primary key
- no multivalued columns (1NF)
- every determinant of a nontrivial functional dependency is itself a candidate key (see Definitions) — the strict version of "the key, the whole key, and nothing but the key"
- no transitive dependency (3NF)

BCNF is the "smallest matryoshka doll" of the four (the Russian nesting dolls, each one opening to reveal a smaller doll inside): a relation in BCNF automatically satisfies 1NF, 2NF, and 3NF too — the same way the smallest doll is still "inside" every larger one wrapped around it. In practice, once every table in a design is in BCNF, the database is considered fully normalized.

### BCNF vs. 3NF — why they're not the same thing

**Plain version**: 3NF checks the same little rules, but it has one loophole. It says a rule's left side can be *less* than a full key, as long as the thing on the right side is at least *part of* some other key. BCNF has no such loophole — the left side must always be a full key, period. That loophole is the entire gap between the two forms.

**Worked example — 3NF holds, BCNF fails**: `(student, course, instructor)`, where a student takes a given course from exactly one instructor (`(student, course) → instructor`), a course can have several instructors across different sections, but each instructor teaches only one course (`instructor → course`).
```
student | course | instructor
--------+--------+-----------
Alice   | CS101  | Smith
Bob     | CS101  | Jones
Carol   | MATH200| Lee
```
Candidate keys here: `(student, course)` and `(student, instructor)` — both uniquely identify a row.
- **3NF check**: the little rule `instructor → course` has left side `instructor` alone, which isn't a full key. But its right side, `course`, is part of the other key `(student, course)`. That satisfies 3NF's loophole, so this relation **passes 3NF**.
- **BCNF check**: same rule, `instructor → course`. Left side `instructor` still isn't a full key. BCNF gives no credit for the right side being part of a key. So this relation **fails BCNF**.

Fix: split into `CourseInstructor(instructor, course)` and `Enrollment(student, instructor)` — now `instructor → course` lives in a table where `instructor` is the whole key.

**Containment only runs one way**: BCNF implies 3NF implies 2NF implies 1NF, never the reverse. So "passes 3NF, fails BCNF" is possible (shown above); "passes BCNF, fails 3NF" is never possible.

**Beyond BCNF**: 4NF (eliminates multivalued dependencies), 5NF (eliminates redundancy from join dependencies not caught by 4NF), and 6NF (largely theoretical) exist but are rarely needed outside specialized design problems.

## Worked Examples

**Movie data** — `(title, year, length, genre, studio_name, star_name)` with `(title, year)` identifying a movie and `star_name` identifying a star. `(title, year) → (length, genre, studio_name)` holds but `(title, year) → star_name` does *not* (a movie has many stars) — so the un-decomposed table isn't even functionally consistent per-row and repeats movie facts once per star. Normalize by splitting into:
```
Movie(title, year, length, genre, studio_name)
StarsIn(title, year, star_name)
```

**Classroom data** — `(student_id, name, course_id, room, time)`. Domain rules: `student_id` identifies a student; a student enrolls in at most one section of a given course; a student can't attend two classes at once; a room hosts at most one class at a given time. `student_id → name` holds regardless of course, so `name` is redundant on every enrollment row. Decompose into:
```
Student(student_id, name)
Enrollment(student_id, course_id, room, time)
```
(A fuller normalization could separate room/time scheduling data as well.)

## Denormalization and Trade-offs
- Normalization improves data integrity and reduces redundancy, but highly normalized schemas can require more joins, which can slow or complicate some queries.
- In read-heavy systems it can be worth intentionally **denormalizing** — to simplify queries, reduce join cost, or improve reporting/analytics performance.
- Trade-off: **normalization** favors consistency and maintainability; **denormalization** favors read performance and convenience.
- Guideline: **normalize first, denormalize only when there is a clear reason.**
