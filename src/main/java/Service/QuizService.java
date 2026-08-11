package Service;

import Data.Question;
import java.util.List;
import java.util.Map;

/**
 * Calculates quiz scores and checks quiz completion.
 */
public class QuizService {

    /**
     * Checks whether every question has an answer.
     */
    public boolean isComplete(
            int totalQuestions,
            Map<Integer, String> answers) {

        if (totalQuestions <= 0 || answers == null) {
            return false;
        }

        if (answers.size() != totalQuestions) {
            return false;
        }

        for (String answer : answers.values()) {
            if (answer == null || answer.isBlank()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates the number of correct answers.
     */
    public int calculateScore(
            List<Question> questions,
            Map<Integer, String> answers) {

        if (questions == null || answers == null) {
            return 0;
        }

        int score = 0;

        for (int index = 0; index < questions.size(); index++) {
            Question question = questions.get(index);
            String selectedAnswer = answers.get(index);

            if (selectedAnswer != null
                    && selectedAnswer.equals(question.getCorrectAnswer())) {
                score++;
            }
        }

        return score;
    }

    /**
     * Calculates the final percentage.
     */
    public double calculatePercentage(
            int score,
            int totalQuestions) {

        if (totalQuestions <= 0) {
            return 0.0;
        }

        return score * 100.0 / totalQuestions;
    }
}