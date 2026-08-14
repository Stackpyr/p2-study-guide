PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS account (
                                       account_id       INTEGER PRIMARY KEY AUTOINCREMENT,
                                       username         TEXT NOT NULL UNIQUE,
                                       email            TEXT NOT NULL,
                                       password_hash    TEXT,
                                       password_salt    TEXT,
                                       display_name     TEXT,
                                       oauth_provider   TEXT,
                                       oauth_subject    TEXT,
                                       is_active        INTEGER NOT NULL DEFAULT 1 CHECK (is_active IN (0, 1)),
                                       is_admin         INTEGER NOT NULL DEFAULT 0 CHECK (is_admin IN (0, 1)),
                                       created_at       TEXT NOT NULL DEFAULT (CURRENT_TIMESTAMP),
                                       updated_at       TEXT NOT NULL DEFAULT (CURRENT_TIMESTAMP),
                                       CHECK (
                                         (password_hash IS NOT NULL AND password_salt IS NOT NULL)
                                         OR oauth_provider IS NOT NULL
                                       ),
                                       UNIQUE (oauth_provider, oauth_subject)
);

CREATE INDEX IF NOT EXISTS idx_account_username ON account (username);
CREATE INDEX IF NOT EXISTS idx_account_email ON account (email);

CREATE TABLE IF NOT EXISTS question (
                                        question_id    INTEGER PRIMARY KEY AUTOINCREMENT,
                                        question_text  TEXT NOT NULL,
                                        category       TEXT NOT NULL,
                                        choice_a       TEXT NOT NULL,
                                        choice_b       TEXT NOT NULL,
                                        choice_c       TEXT NOT NULL,
                                        choice_d       TEXT NOT NULL,
                                        correct_answer TEXT NOT NULL,
                                        created_at     TEXT NOT NULL DEFAULT (CURRENT_TIMESTAMP),
                                        updated_at     TEXT NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE IF NOT EXISTS quiz_attempt (
                                            attempt_id      INTEGER PRIMARY KEY AUTOINCREMENT,
                                            account_id      INTEGER NOT NULL REFERENCES account (account_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                                            score           INTEGER NOT NULL DEFAULT 0,
                                            total_questions INTEGER NOT NULL DEFAULT 0,
                                            completed_at    TEXT
);

CREATE INDEX IF NOT EXISTS idx_quiz_attempt_account_id ON quiz_attempt (account_id);

CREATE TABLE IF NOT EXISTS quiz_attempt_answer (
                                                   answer_id       INTEGER PRIMARY KEY AUTOINCREMENT,
                                                   attempt_id      INTEGER NOT NULL REFERENCES quiz_attempt (attempt_id) ON DELETE CASCADE ON UPDATE CASCADE,
                                                   question_id     INTEGER NOT NULL REFERENCES question (question_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                                                   selected_choice TEXT,
                                                   is_correct      INTEGER CHECK (is_correct IN (0, 1))
);

CREATE INDEX IF NOT EXISTS idx_quiz_attempt_answer_attempt_id ON quiz_attempt_answer (attempt_id);
CREATE INDEX IF NOT EXISTS idx_quiz_attempt_answer_question_id ON quiz_attempt_answer (question_id);