package Data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Question model and its setters, getters, etc.
 *
 * @author Analiza Boehning
 * @version 0.1.0
 * @since 8/10/2026
 */
class QuestionTest {

    /**
     * Verify that the expected representation of a Question is returned.
     */
    @Test
    void testToString() {
        Question question = new Question(
                42,
                "What is Java?",
                "Object-Oriented Programming",
                "A programming language",
                "A database",
                "A coffee brand",
                "A computer",
                "A programming language"
        );

        assertEquals(
                "Question{questionId=42, questionText='What is Java?', category='Object-Oriented Programming'}",
                question.toString()
        );
    }

    /**
     * Comparing Questions with the same and different IDs
     */
    @Test
    void testEquals() {
        Question questionTest1 = new Question(
                27,
                "What is Java?",
                "Object-Oriented Programming",
                "A programming language",
                "A database",
                "A coffee brand",
                "A computer",
                "A programming language"
        );

        Question questionTest2 = new Question(
                27,
                "What is Java?",
                "Object-Oriented Programming",
                "A programming language",
                "A database",
                "A coffee brand",
                "A computer",
                "A programming language"
        );

        Question questionTest3 = new Question(
                2,
                "What is Java?",
                "Object-Oriented Programming",
                "A programming language",
                "A database",
                "A coffee brand",
                "A computer",
                "A programming language"
        );

        assertEquals(questionTest1, questionTest2);
        assertNotEquals(questionTest1, questionTest3);
    }

    /**
     * Verify equal Question objects produce the same hash code
     */
    @Test
    void testHashCode() {
        Question questionTest1 = new Question(
                27,
                "What is Java?",
                "Object-Oriented Programming",
                "A programming language",
                "A database",
                "A coffee brand",
                "A computer",
                "A programming language"
        );

        Question questionTest2 = new Question(
                27,
                "What is Java?",
                "Object-Oriented Programming",
                "A programming language",
                "A database",
                "A coffee brand",
                "A computer",
                "A programming language"
        );

        assertEquals(questionTest1, questionTest2);
        assertEquals(questionTest1.hashCode(), questionTest2.hashCode());
    }

    /**
     * Verify the question ID is returned
     */
    @Test
    void getQuestionId() {
        Question question = new Question(
                27,
                "What is Java?",
                "Object-Oriented Programming",
                "A programming language",
                "A database",
                "A coffee brand",
                "A computer",
                "A programming language"
        );

        assertEquals(27, question.getQuestionId());
    }

    /**
     * Verify the question text is returned
     */
    @Test
    void getQuestionText() {
        Question question = new Question("What is Java?");

        assertEquals("What is Java?",question.getQuestionText());
    }

    /**
     * Verify the question category is returned
     */
    @Test
    void getCategory() {
        Question question = new Question("What is Java?");
        question.setCategory("Object-Oriented Programming");

        assertEquals("Object-Oriented Programming",question.getCategory());
    }

    /**
     * Verify the question category is updated
     */
    @Test
    void setCategory() {
        Question question = new Question("What is Java?");
        question.setCategory("Object-Oriented Programming");

        assertEquals("Object-Oriented Programming",question.getCategory());
    }

    /**
     * Verify the question text is updated
     */
    @Test
    void setQuestionText() {
        Question question = new Question("What is Java?");
        question.setQuestionText("What is object-oriented programming?");

        assertEquals("What is object-oriented programming?",question.getQuestionText());
    }

    /**
     * Verify the Choice A is updated
     */
    @Test
    void setChoiceA() {
        Question question = new Question("What is Java?");
        question.setChoiceA("A programming language");

        assertEquals("A programming language",question.getChoiceA());
    }

    /**
     * Verify the Choice B is updated
     */
    @Test
    void setChoiceB() {
        Question question = new Question("What is Java?");
        question.setChoiceB("A database");

        assertEquals("A database",question.getChoiceB());
    }

    /**
     * Verify the Choice C is updated
     */
    @Test
    void setChoiceC() {
        Question question = new Question("What is Java?");
        question.setChoiceC("A coffee brand");

        assertEquals("A coffee brand",question.getChoiceC());
    }

    /**
     * Verify the Choice D is updated
     */
    @Test
    void setChoiceD() {
        Question question = new Question("What is Java?");
        question.setChoiceD("A computer");

        assertEquals("A computer",question.getChoiceD());
    }

    /**
     * Verify the correct answer is updated
     */
    @Test
    void setCorrectAnswer() {
        Question question = new Question("What is Java?");
        question.setCorrectAnswer("A programming language");

        assertEquals("A programming language",question.getCorrectAnswer());
    }

    /**
     * Verify you can retrieve choice A
     */
    @Test
    void getChoiceA() {
        Question question = new Question("What is Java?");
        question.setChoiceA("A programming language");

        assertEquals("A programming language",question.getChoiceA());
    }

    /**
     * Verify you can retrieve choice B
     */
    @Test
    void getChoiceB() {
        Question question = new Question("What is Java?");
        question.setChoiceB("A database");

        assertEquals("A database",question.getChoiceB());
    }

    /**
     * Verify you can retrieve choice C
     */
    @Test
    void getChoiceC() {
        Question question = new Question("What is Java?");
        question.setChoiceC("A coffee brand");

        assertEquals("A coffee brand",question.getChoiceC());
    }

    /**
     * Verify you can retrieve choice D
     */
    @Test
    void getChoiceD() {
        Question question = new Question("What is Java?");
        question.setChoiceD("A computer");

        assertEquals("A computer",question.getChoiceD());
    }

    /**
     * Verify you can retrieve correct asnwer
     */
    @Test
    void getCorrectAnswer() {
        Question question = new Question("What is Java?");
        question.setCorrectAnswer("A programming language");

        assertEquals("A programming language",question.getCorrectAnswer());
    }
}