package Service;

import Data.Question;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuizServiceTest {

    private final QuizService quizService = new QuizService();

    @Test
    void completeQuizReturnsTrue() {
        Map<Integer, String> answers = Map.of(
                0, "Answer A",
                1, "Answer B"
        );

        assertTrue(quizService.isComplete(2, answers));
    }

    @Test
    void incompleteQuizReturnsFalse() {
        Map<Integer, String> answers = Map.of(
                0, "Answer A"
        );

        assertFalse(quizService.isComplete(2, answers));
    }

    @Test
    void blankAnswerReturnsFalse() {
        Map<Integer, String> answers = Map.of(
                0, "Answer A",
                1, ""
        );

        assertFalse(quizService.isComplete(2, answers));
    }

    @Test
    void calculatesPerfectScore() {
        Question firstQuestion = createQuestion("Answer A");
        Question secondQuestion = createQuestion("Answer B");

        List<Question> questions = List.of(
                firstQuestion,
                secondQuestion
        );

        Map<Integer, String> answers = Map.of(
                0, "Answer A",
                1, "Answer B"
        );

        assertEquals(
                2,
                quizService.calculateScore(questions, answers)
        );
    }

    @Test
    void calculatesPartialScore() {
        Question firstQuestion = createQuestion("Answer A");
        Question secondQuestion = createQuestion("Answer B");

        List<Question> questions = List.of(
                firstQuestion,
                secondQuestion
        );

        Map<Integer, String> answers = Map.of(
                0, "Answer A",
                1, "Wrong Answer"
        );

        assertEquals(
                1,
                quizService.calculateScore(questions, answers)
        );
    }

    @Test
    void calculatesPercentage() {
        assertEquals(
                75.0,
                quizService.calculatePercentage(3, 4)
        );
    }

    @Test
    void zeroQuestionsReturnsZeroPercentage() {
        assertEquals(
                0.0,
                quizService.calculatePercentage(0, 0)
        );
    }

    private Question createQuestion(String correctAnswer) {
        Question question = new Question("Example question");
        question.setCorrectAnswer(correctAnswer);
        return question;
    }
}