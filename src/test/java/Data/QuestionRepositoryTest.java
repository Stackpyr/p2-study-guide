package Data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CRUD operations in QuestionRepository
 *
 * @author Analiza Boehning
 * @version 0.1.0
 * @since 8/10/2026
 */
class QuestionRepositoryTest {

    private QuestionRepository repository;

    @BeforeEach
    void setUp() {
        DatabaseManager.close();
        repository = new QuestionRepository();
    }

    @AfterEach
    void tearDown() {
        DatabaseManager.close();
    }

    /**
     * Verify that an existing question can be retrieved by the ID
     */
    @Test
    void getQuestionById() {
        Question question = repository.addQuestion(
                createTestQuestion("What is Java?")
        );

        Question result = repository.getQuestionById(question.getQuestionId());

        assertNotNull(result);
        assertEquals(question.getQuestionId(), result.getQuestionId());
        assertEquals("What is Java?", result.getQuestionText());
    }

    /**
     * All saved questions can be retrieved and returned
     */
    @Test
    void getAllQuestions() {
        repository.addQuestion(createTestQuestion("What is Java?"));
        repository.addQuestion(createTestQuestion("What is SQL?"));

        List<Question> questions = repository.getAllQuestions();

        assertEquals(2, questions.size());
        assertEquals("What is Java?", questions.get(0).getQuestionText());
        assertEquals("What is SQL?", questions.get(1).getQuestionText());
    }

    /**
     * Verify questions can be retrieved by their category
     */
    @Test
    void getQuestionByCategory() {
        Question testQ1 = createTestQuestion("What is Java?");
        testQ1.setCategory("Object-Oriented Programming");
        Question testQ2 = createTestQuestion("What SQL command adds a new row of data into a table?");
        testQ2.setCategory("Databases");

        repository.addQuestion(testQ1);
        repository.addQuestion(testQ2);

        List<Question> result = repository.getQuestionsByCategory("Object-Oriented Programming");

        assertEquals(1, result.size());
        assertEquals("What is Java?", result.get(0).getQuestionText());
        assertEquals("Object-Oriented Programming", result.get(0).getCategory());
    }

    /**
     * Verify all categories and corresponding questions are returned
     */
    @Test
    void getAllQuestionsByCategory() {
        Question testQ1 = createTestQuestion("What is Java?");
        testQ1.setCategory("Object-Oriented Programming");
        Question testQ2 = createTestQuestion("What SQL command adds a new row of data into a table?");
        testQ2.setCategory("Databases");

        repository.addQuestion(testQ1);
        repository.addQuestion(testQ2);

        List<String> result = repository.getAllCategories();

        assertEquals(2, result.size());
        assertTrue(result.contains("Object-Oriented Programming"));
        assertTrue(result.contains("Databases"));

    }

    /**
     * Verify keyword search returns only the questions containing that keyword
     */
    @Test
    void getQuestionsByKeyword() {
        repository.addQuestion(createTestQuestion("What is Java?"));
        repository.addQuestion(createTestQuestion("What SQL command adds a new row of data into a table?"));
        repository.addQuestion(createTestQuestion("What is the standard main entry point for a Java program?"));

        List<Question> result = repository.getQuestionsByKeyword("Java");

        assertEquals(2, result.size());
        assertEquals("What is Java?", result.get(0).getQuestionText());
        assertEquals("What is the standard main entry point for a Java program?", result.get(1).getQuestionText());

    }

    /**
     * A question can be added and receives an ID
     */
    @Test
    void addQuestion() {
        Question question = createTestQuestion("What is Java?");

        Question addedQuestion = repository.addQuestion(question);

        assertNotNull(addedQuestion);
        assertTrue(addedQuestion.getQuestionId() > 0);
        assertEquals("What is Java?", addedQuestion.getQuestionText());
        assertEquals("Programming", addedQuestion.getCategory());
        assertEquals("A", addedQuestion.getCorrectAnswer());
    }

    /**
     * Verifies a question can be updated
     */
    @Test
    void updateQuestion() {
        Question question = repository.addQuestion(
                createTestQuestion("What is Java?")
        );

        question.setQuestionText("What is object-oriented programming?");
        question.setCategory("Software Engineering");

        Question result = repository.updateQuestion(question);

        assertEquals("What is object-oriented programming?",
                result.getQuestionText());
        assertEquals("Software Engineering", result.getCategory());
    }

    /**
     * Verify a question can be deleted
     */
    @Test
    void deleteQuestion() {
        Question question = repository.addQuestion(
                createTestQuestion("What is Java?")
        );

        boolean deleted = repository.deleteQuestion(question.getQuestionId());

        assertTrue(deleted);
        assertNull(repository.getQuestionById(question.getQuestionId()));
    }

    /**
     * Creates a Question with test data.
     * @param questionText text for the test question
     * @return a Question populated with the data
     */
    private Question createTestQuestion(String questionText) {
        Question question = new Question(questionText);
        question.setCategory("Programming");
        question.setChoiceA("A");
        question.setChoiceB("B");
        question.setChoiceC("C");
        question.setChoiceD("D");
        question.setCorrectAnswer("A");
        return question;
    }
}