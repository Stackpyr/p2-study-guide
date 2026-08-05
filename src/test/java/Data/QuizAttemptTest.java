package Data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class QuizAttemptTest {

    @Test
    void createsNewQuizAttempt() {
        QuizAttempt attempt = new QuizAttempt(5, 10);

        assertEquals(5, attempt.getAccountId());
        assertEquals(10, attempt.getTotalQuestions());
        assertEquals(0, attempt.getScore());
        assertNull(attempt.getCompletedAt());
    }

    @Test
    void updatesQuizAttemptResults() {
        QuizAttempt attempt = new QuizAttempt(5, 10);

        attempt.setScore(8);
        attempt.setCompletedAt("2026-08-03 12:00:00");

        assertEquals(8, attempt.getScore());
        assertEquals("2026-08-03 12:00:00", attempt.getCompletedAt());
    }
}