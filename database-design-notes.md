# Database Design — ER Modeling to PostgreSQL (CST 363)

## Design Process
Four phases: **Identify** (what exists in the domain) → **Read** (communicate the model visually) → **Decide** (what constraints must be true) → **Translate** (make it executable).
Pipeline: conceptual model → relational schema (logical design) → PostgreSQL DDL (physical language).

## ER Model Building Blocks
- **Entity**: one distinguishable thing. **Entity set**: collection of similar entities.
- **Relationship**: association among entities. **Relationship set**: collection of relationships of the same kind. A relationship set can involve the same entity set more than once (e.g. `prerequisite` uses `course` twice) — use **role names** to disambiguate.
- **Attribute**: property of an entity *or* relationship. Every attribute has a **domain** — the permitted values (not just a storage type; e.g. ZIP code is `text`, not `integer`, because leading zeros matter).
- Attribute kinds: **simple** (one value, e.g. `salary`), **composite** (made of parts, e.g. `address = street+city+state+zip`), **multivalued** (multiple values per entity, e.g. `phones`), **nullable** (may be unknown/N-A, e.g. `middle_name`).
- **Identifier**: attribute(s) that distinguish one entity from another (informal precursor to formal keys).
- **Weak entity set**: cannot be identified by its own attributes alone; depends on an **owner entity set**. Identified by **owner identifier + partial key** (e.g. `section` is weak: identified by `course_id` (owner) + `sec_id, semester, year` (partial key)).

## Crow's Foot Notation (our notation)
- Entity boxes: attributes + domains, `PK` marks the chosen identifier.
- Relationship lines: name + optionality/cardinality glyphs at each end.
- Glyph reference (read symbol nearest the far entity):
  - `一‖—` one and only one
  - `一○—` zero or one
  - `一‖<` one or many
  - `一○<` zero or many
  - First marker = **minimum** (optionality: circle=0, bar=1). Second marker = **maximum** (cardinality: bar=1, crow's foot=many).
- Weak-entity relationship line uses a double bar (`‖‖`) at the owner end (identifying relationship) and no separate table is created for it.
- **Modeling rule**: conceptual ER diagrams carry **no foreign-key attributes**. Conceptual ERD = what relationships exist; relational schema = where the FKs go.
- Notations vary (Chen: entities=rectangles/relationships=diamonds/attributes=ovals; Hybrid: attributes move into entity boxes, relationships stay diamonds; Crow's Foot: boxes + cardinality/optionality glyphs on the lines) — same underlying model, notation ≠ semantics.

### Worked Example: Reading an ER Diagram (Music Library)
Practice reading (not redesigning) a diagram by answering, in order: entity sets → attributes/domains → identifiers → relationship sets → each relationship's optionality/cardinality → relationship attributes.

Entities: `user(user_id PK, name)`, `artist(artist_id PK, name)`, `album(album_id PK, title, release_date)`, `genre(genre_id PK, name)`, `playlist(playlist_id PK, name)`, `track(track_id PK, title, duration)`, plus an **associative entity** `playlist_track` with no PK of its own marked yet.

Relationship sets and how to decode them — for each, the cardinality (1:1 / 1:N / M:N) and participation (total = must participate, drawn as a bar; partial = optional, drawn as a circle) on **each** side:
- `creates` (user–playlist): user end = one-and-only-one (**total** on the playlist side — every playlist requires a creator), playlist end = zero-or-many (**partial** on the user side — a user may create none) → **1:N**.
- `performs` (artist–track): both ends = zero-or-many → **partial participation on both sides** → **M:N**. An artist performs zero or many tracks; a track can credit zero or many artists.
- `contains` (album–track): album end = one-and-only-one (**total** on the track side — every track belongs to exactly one album), track end = zero-or-many (**partial** on the album side — an album may contain zero tracks) → **1:N**.
- `classifies` (genre–track): genre end = one-and-only-one (**total** on the track side — every track has exactly one genre), track end = zero-or-many (**partial** on the genre side) → **1:N** (this model gives each track a single genre, not multiple).
- `contains` (playlist–playlist_track) + `appears_in` (track–playlist_track): playlist and track are each one-and-only-one *from playlist_track's side* (**total** participation — a playlist_track row can't exist without both), while playlist_track is zero-or-many from each of theirs (**partial** on the playlist and track sides — either can exist with zero associated rows). Two 1:N relationships into an associative entity are how an **M:N between playlist and track** gets modeled conceptually, ahead of being translated into a junction table later.

Relationship attribute: `added_date` belongs to `playlist_track` — it describes *when a track was added to a playlist*, not a property of either entity alone. (Watch for a relationship-set name reused on two different relationships, like `contains` here — rename one if you draw this yourself, to avoid ambiguity.)

## Constraints: Cardinality & Participation
Constraints are **prescriptive** (what must be true), not merely descriptive of today's data — e.g. "every student currently has an advisor" doesn't answer "must every student have an advisor?".

**Mapping cardinality** — how many relationships of this type can one entity participate in:
| Cardinality | Meaning |
|---|---|
| 1:1 | at most one on either side |
| 1:N | one on one side, many on the other |
| M:N | many on both sides |

**Participation** — must every entity in this set participate in this relationship?
- **Total participation**: every entity must participate → drawn as the minimum-marker bar (`‖`) at that end.
- **Partial participation**: participation is optional → drawn as a circle (`○`) at that end.
- Example: if every student must have exactly one advisor, that's **total participation on the student side** of `advisor`.

## Keys (formalized)
Running example: `employee(emp_id, ssn, email, badge_number, first_name, last_name, dept_id)`, where `emp_id`, `ssn`, `email`, and `badge_number` are each independently unique.
```
emp_id | ssn         | email           | badge_number | first_name | last_name | dept_id
-------+-------------+-----------------+--------------+------------+-----------+--------
501    | 111-22-3333 | a@co.com        | B-9001       | Alice      | Smith     | D10
502    | 444-55-6666 | b@co.com        | B-9002       | Bob        | Jones     | D20
503    | 777-88-9999 | c@co.com        | B-9003       | Alice      | Smith     | D10
```
(Note rows 1 and 3: two different employees, both named Alice Smith — this is exactly why `{first_name, last_name}` can't be a key, further down.)

- **Superkey**: any attribute set that uniquely identifies an entity — extra attributes allowed. `{emp_id}`, `{ssn}`, `{emp_id, email}` (redundant but still unique), and the whole row are all superkeys.
- **Candidate key**: a *minimal* superkey — remove any attribute and it stops being unique. `{emp_id}`, `{ssn}`, `{email}`, `{badge_number}` are each candidate keys. `{emp_id, email}` is **not** a candidate key — it's not minimal, since `{emp_id}` alone is already a superkey.
- **Primary key**: the one candidate key chosen as the main identifier (e.g. `emp_id`), used for FK references, indexes, etc.
- **Alternate key**: a candidate key *not* chosen as primary (e.g. `ssn`, `email`, `badge_number` once `emp_id` is picked) — still enforced with `UNIQUE`.
- **Composite key**: a key spanning more than one column. Describes the *shape* of a key, not a separate category — a composite key can be a candidate key, primary key, etc. Example: `section`'s primary key `(course_id, sec_id, semester, year)` — no single column identifies a section.
- **Foreign key**: not an identifying key for its *own* table — a reference to *another* table's primary/unique key, implementing a relationship (e.g. `employee.dept_id REFERENCES department (dept_id)`).
- **Partial key**: a weak entity set's own distinguishing attribute(s), unique only *within* its owner (e.g. `section`'s `sec_id, semester, year` — only unique within one `course_id`). Owner key + partial key = the full composite primary key. See **Weak entity set** above.
- **Natural vs. surrogate key**: see the dedicated subsection under *Translating ER → PostgreSQL* below — natural keys (`ssn`, `(contributor_id, candidate_id, contribution_date)`) come from the domain; surrogate keys (`contribution_id GENERATED ALWAYS AS IDENTITY`) are invented purely for the database.

**Keys come from domain rules, not from the current data snapshot.** `{first_name, last_name}` might look unique in today's employee table, but it is *not* a candidate key, because nothing in the domain guarantees two employees can never share a name — a second "John Smith" could be hired tomorrow. A key is a promise the domain enforces, not an observation about the data you currently have. (Same principle as a functional dependency in the normalization material: "no duplicates seen so far" is not the same as "a key.")

- A **relationship set's** key depends on its cardinality:
| Mapping cardinality | Minimal relationship-set key |
|---|---|
| M:N | both participating entity keys |
| 1:N / N:1 | key from the "many" side |
| 1:1 | either side's key |
- Relationship attributes belong to the association itself, not either entity alone (e.g. `takes.grade` describes the fact a student took a section, not the student or the section).

## Redundant Attributes
**Rule**: don't store the same fact twice in the conceptual model. Example: `dept_name` appearing both as `department`'s identifier *and* as an attribute of `instructor` is redundant once an `inst_dept` relationship captures which department an instructor belongs to — the copy on `instructor` is derivable and should be removed. This pays off at translation time: the relationship becomes a foreign key instead of a duplicated column.

## Modeling Method Checklist
Identify: entity sets, relationship sets, attributes/domains. Read: draw the model. Decide: mapping cardinalities, participation constraints, entity-set keys, relationship-set keys, remove redundant attributes. (A guideline, not a law — iterate as the domain becomes clearer.)

---

## Translating ER → PostgreSQL

**Domain → PostgreSQL type**: `integer`→`INTEGER` (`BIGINT` if the identifier range may exceed INTEGER); `text`→`TEXT` (`VARCHAR(n)` only when length is a real constraint); `date`→`DATE`; `money`→`NUMERIC(12,2)` (not PostgreSQL's `MONEY` type).

**Naming conventions**: singular table names (`student`, `course`); snake_case (`student_id`); explicit identifiers (`student_id`, not just `id`).

**Attribute translation**:
| Conceptual attribute | Relational translation |
|---|---|
| simple | one column |
| composite | component columns |
| multivalued | separate table (+ FK back to owner) |
| nullable | column may be NULL |

```sql
-- multivalued example: instructor.phones is multivalued
CREATE TABLE instructor_phone (
  instructor_id INTEGER NOT NULL REFERENCES instructor (instructor_id),
  phone_number  TEXT    NOT NULL,
  PRIMARY KEY (instructor_id, phone_number)
);
```

**Rule 1 — strong entity set → relation.** Entity-set name → table name, simple attributes → columns, chosen identifier → `PRIMARY KEY`.
```sql
CREATE TABLE student (
  student_id   INTEGER PRIMARY KEY,
  name         TEXT    NOT NULL,
  total_credits INTEGER NOT NULL
);
```

**Rule 2 — weak entity set → relation keyed by owner key + partial key** (composite `PRIMARY KEY` + `FOREIGN KEY`). No separate table is created for the identifying relationship itself.
```sql
CREATE TABLE course (
  course_id TEXT PRIMARY KEY,
  title     TEXT NOT NULL,
  credits   INTEGER NOT NULL
);
CREATE TABLE section (
  course_id TEXT    NOT NULL,
  sec_id    INTEGER NOT NULL,
  semester  TEXT    NOT NULL,
  year      INTEGER NOT NULL,
  PRIMARY KEY (course_id, sec_id, semester, year),
  FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE
);
```
`course_id` plays a double role — part of the primary key *and* a foreign key — that double role is the fingerprint of a weak entity. `ON DELETE CASCADE` is a deletion *policy*, not the invariant itself; `ON DELETE RESTRICT` preserves the same "no section without a course" invariant by refusing the delete instead.

**Rule 3 — M:N relationship set → junction (associative) relation.** Steps: create a new table; include the keys of both participating entity sets as FKs; use their combination as the PK; put relationship attributes in the new table.
```sql
CREATE TABLE takes (
  student_id INTEGER NOT NULL REFERENCES student (student_id),
  course_id  TEXT    NOT NULL,
  sec_id     INTEGER NOT NULL,
  semester   TEXT    NOT NULL,
  year       INTEGER NOT NULL,
  grade      TEXT,   -- nullable: unknown before the final grade is assigned
  PRIMARY KEY (student_id, course_id, sec_id, semester, year),
  FOREIGN KEY (course_id, sec_id, semester, year)
    REFERENCES section (course_id, sec_id, semester, year)
);
```

**Rule 4 — 1:N relationship set → foreign key on the "N"-side relation.**
```sql
CREATE TABLE instructor (
  instructor_id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  salary NUMERIC(12,2) NOT NULL
);
CREATE TABLE student (
  student_id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  total_credits INTEGER NOT NULL,
  advisor_id INTEGER REFERENCES instructor (instructor_id),  -- nullable: advising is optional here
  advisor_start_date DATE  -- relationship attribute travels with the FK; nullable if advisor is optional
);
```
Total participation on the N side → mandatory FK → `NOT NULL`. Partial participation → nullable FK.

**Rule 5 — 1:1 relationship set → FK on one side + `UNIQUE`.** Choose one side to hold the FK (prefer the side with total participation); add `UNIQUE` so no two rows point at the same row; add `NOT NULL` if participation is mandatory on the FK side.
```sql
CREATE TABLE office (
  office_id TEXT PRIMARY KEY,
  building TEXT NOT NULL,
  room TEXT NOT NULL
);
CREATE TABLE instructor (
  instructor_id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  office_id TEXT UNIQUE REFERENCES office (office_id)          -- optional FK side
  -- office_id TEXT NOT NULL UNIQUE REFERENCES office (office_id)  -- mandatory FK side
);
```
Caveat: if participation is total on **both** sides of a 1:1 relationship, a single simple FK column can't fully enforce that in ordinary SQL table declarations — that needs a more advanced constraint strategy.

**Natural vs. surrogate keys**: a **natural key** comes from the domain (e.g. `(contributor_id, candidate_id, contribution_date)` — valid only if the domain rules out repeats). A **surrogate key** is introduced by the database design itself (e.g. `contribution_id BIGINT GENERATED ALWAYS AS IDENTITY`). This is a design decision from domain rules, not a convenience — "no duplicates seen so far" is not the same as "a key."
```sql
CREATE TABLE contribution (
  contribution_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  contributor_id   TEXT NOT NULL REFERENCES contributor (contributor_id),
  candidate_id     TEXT NOT NULL REFERENCES candidate (candidate_id),
  contribution_date DATE NOT NULL,
  amount           NUMERIC(12,2) NOT NULL
);
```

## SQL Constraints in Depth
Constraints are the rules the database itself enforces so application code can't quietly violate the design. A translated schema isn't finished until these are added, beyond just getting the columns and types right.

- **`NOT NULL`**: a value is required; the column can't be left empty. A `PRIMARY KEY` already implies `NOT NULL` automatically, so you don't need to write both on the same column.
- **`PRIMARY KEY`**: prevents duplicate key values *and* NULL key values. For a composite key, define it as a separate table-level clause rather than inline on one column:
```sql
CREATE TABLE enrollment (
  student_id INT NOT NULL,
  course_id  INT NOT NULL,
  grade      TEXT,
  CONSTRAINT pk_enrollment PRIMARY KEY (student_id, course_id)
);
```
- **`FOREIGN KEY` / referential integrity**: the table holding the FK is the **child**; the table it references is the **parent**. The constraint blocks inserting a child row whose FK value doesn't exist in the parent — and by default also blocks deleting/updating a parent row that still has matching child rows.
- **Referential actions** — what happens to child rows when a parent row is deleted or its key changes, set via `ON DELETE` / `ON UPDATE`:
  - `RESTRICT` / `NO ACTION` (the default): refuse the delete/update if matching child rows exist.
  - `CASCADE`: automatically delete (or update the FK value of) the matching child rows along with the parent.
  - `SET NULL`: the child row survives, but its FK column is set to NULL — useful when the relationship is optional and the child data is still worth keeping after the parent disappears.
```sql
CREATE TABLE student (
  student_id INT PRIMARY KEY,
  name TEXT NOT NULL,
  advisor_id INT REFERENCES instructor (instructor_id)
    ON DELETE SET NULL      -- losing an advisor shouldn't delete the student
    ON UPDATE CASCADE
);
CREATE TABLE section (
  course_id TEXT NOT NULL REFERENCES course (course_id)
    ON DELETE CASCADE       -- no section should outlive its course
    ON UPDATE CASCADE,
  sec_id INT NOT NULL,
  PRIMARY KEY (course_id, sec_id)
);
```
  A **mandatory** relationship (total participation on the referencing side) normally pairs the FK with `NOT NULL`, since a NULL FK can't reference anything.
- **`UNIQUE`**: forbids duplicate values in a *non-key* column, or combination of columns, where the domain says duplicates shouldn't happen — e.g. a `username` column, or a composite `UNIQUE (title, author)` on a `book` table so the same title/author pair can't be inserted twice.
- **`DEFAULT`**: supplies a value automatically when none is given on insert (e.g. `created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP`). It doesn't replace `NOT NULL` — an explicit `NULL` can still be inserted unless `NOT NULL` is also applied. In PostgreSQL, use `TIMESTAMP WITH TIME ZONE` (not plain `TIMESTAMP`) if you want `CURRENT_TIMESTAMP` to store UTC-aware data.
- **`CHECK`**: validates a condition beyond type/uniqueness — ranges, enumerated value lists, or cross-column rules within one row.
```sql
CREATE TABLE person (
  person_id INT PRIMARY KEY,
  age INT CHECK (age BETWEEN 0 AND 120),
  state TEXT CHECK (state IN ('CA','NY','TX'))  -- shortened list
);
```
- **Naming constraints**: not required, but good practice for `PRIMARY KEY` and `FOREIGN KEY` especially — a named constraint (`CONSTRAINT fk_student_advisor FOREIGN KEY ...`) gives clearer error messages and survives a restructure/migration more predictably than a database-generated name. `NOT NULL` and `DEFAULT` are usually left inline/unnamed since they're rarely referenced on their own.

## Capstone Map
| Conceptual | Relational | PostgreSQL |
|---|---|---|
| entity set | relation | `CREATE TABLE` |
| simple / composite / multivalued attribute | attribute(s) | typed column(s) / component columns / table + FK back |
| derived attribute | usually not stored | compute in query or view |
| relationship attribute | attribute of the relationship's relation | junction / FK-bearing table column |
| weak entity set | relation keyed by owner key + partial key | composite `PRIMARY KEY` + `FOREIGN KEY` |
| M:N relationship set | junction relation | composite PK + two FKs |
| 1:N relationship set | FK on the N-side relation | `REFERENCES` |
| 1:1 relationship set | FK on one side | `REFERENCES` + `UNIQUE` |
| total / partial participation (FK side) | mandatory / optional reference | `NOT NULL` / nullable |
| chosen identifier | primary key | `PRIMARY KEY`; surrogate may use `GENERATED ALWAYS AS IDENTITY` |
