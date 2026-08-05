package Data;

/**
 * Represents one quiz attempt made by a user.
 */
public class QuizAttempt {

    private int attemptId;
    private int accountId;
    private int score;
    private int totalQuestions;
    private String completedAt;

    /**
     * Creates a new quiz attempt before it is saved.
     */
    public QuizAttempt(int accountId, int totalQuestions) {
        this.accountId = accountId;
        this.totalQuestions = totalQuestions;
        this.score = 0;
        this.completedAt = null;
    }

    /**
     * Creates a QuizAttempt from a database row.
     */
    QuizAttempt(
            int attemptId,
            int accountId,
            int score,
            int totalQuestions,
            String completedAt) {

        this.attemptId = attemptId;
        this.accountId = accountId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.completedAt = completedAt;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}