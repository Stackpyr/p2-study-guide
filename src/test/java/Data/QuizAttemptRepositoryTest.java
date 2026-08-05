package Data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuizAttemptRepositoryTest {

    private QuizAttemptRepository repository;
    private int accountId;


    @BeforeEach
    void setUp() {
        DatabaseManager.close();

        Account account = new Account(
                "quiz-" + UUID.randomUUID() + "@test.com");

        account.setPassword("testPassword123");
        account.setDisplayName("Quiz Tester");

        AccountRepository accountRepository = new AccountRepository();
        Account savedAccount = accountRepository.addAccount(account);

        assertNotNull(savedAccount);

        accountId = savedAccount.getAccountId();
        repository = new QuizAttemptRepository();
    }


    @AfterEach
    void tearDown() {
        DatabaseManager.close();
    }


    @Test
    void addsAndReadsQuizAttempt() {
        QuizAttempt attempt = new QuizAttempt(accountId, 10);

        QuizAttempt savedAttempt =
                repository.addQuizAttempt(attempt);

        assertNotNull(savedAttempt);
        assertTrue(savedAttempt.getAttemptId() > 0);
        assertEquals(accountId, savedAttempt.getAccountId());
        assertEquals(10, savedAttempt.getTotalQuestions());
        assertEquals(0, savedAttempt.getScore());

        QuizAttempt foundAttempt =
                repository.getById(savedAttempt.getAttemptId());

        assertNotNull(foundAttempt);
        assertEquals(
                savedAttempt.getAttemptId(),
                foundAttempt.getAttemptId());
    }


    @Test
    void findsQuizAttemptsByAccountId() {
        repository.addQuizAttempt(
                new QuizAttempt(accountId, 10));

        repository.addQuizAttempt(
                new QuizAttempt(accountId, 5));

        List<QuizAttempt> attempts =
                repository.getByAccountId(accountId);

        assertEquals(2, attempts.size());

        for (QuizAttempt attempt : attempts) {
            assertEquals(accountId, attempt.getAccountId());
        }
    }


    @Test
    void updatesQuizAttempt() {
        QuizAttempt savedAttempt =
                repository.addQuizAttempt(
                        new QuizAttempt(accountId, 10));

        assertNotNull(savedAttempt);

        savedAttempt.setScore(8);
        savedAttempt.setCompletedAt(
                "2026-08-03 12:00:00");

        QuizAttempt updatedAttempt =
                repository.updateQuizAttempt(savedAttempt);

        assertNotNull(updatedAttempt);
        assertEquals(8, updatedAttempt.getScore());
        assertEquals(
                "2026-08-03 12:00:00",
                updatedAttempt.getCompletedAt());
    }


    @Test
    void deletesQuizAttempt() {
        QuizAttempt savedAttempt =
                repository.addQuizAttempt(
                        new QuizAttempt(accountId, 10));

        assertNotNull(savedAttempt);

        boolean deleted = repository.deleteQuizAttempt(
                savedAttempt.getAttemptId());

        assertTrue(deleted);
        assertNull(
                repository.getById(
                        savedAttempt.getAttemptId()));
    }
}