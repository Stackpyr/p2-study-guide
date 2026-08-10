/**
 * Responsible for CRUD operations on the question table and hydrating the Question object
 *
 * @author Analiza Boehning
 * @version 0.1.1
 * @since 7/31/2026
 */

package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class QuestionRepository {
    private final Connection conn;
    private final String GET_BY_QID_CMD = "SELECT * FROM question WHERE question_id = ?";
    private final String INSERT_CMD = "INSERT INTO question (question_text, category, choice_a, choice_b, choice_c, choice_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_CMD = "UPDATE question SET question_text = ?, category = ?, choice_a = ?, choice_b= ?, choice_c = ?, choice_d = ?, correct_answer = ? WHERE question_id = ?";
    private final String DELETE_CMD = "DELETE FROM question WHERE question_id = ?";

    /**
     * Creates a QuestionRepository and database connection
     */
    public QuestionRepository() {
        conn = DatabaseManager.getConnection();
    }

    /**
     * Retrieves a question by ID
     * @param questionId ID of the question
     * @return Question object and question information - otherwise null if no question is found
     */
    public Question getQuestionById(int questionId) {
        try (PreparedStatement selectStmt = conn.prepareStatement(GET_BY_QID_CMD)) {
            selectStmt.setInt(1, questionId);

            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Add Question to the database
     * @param question Question to be added
     * @return added question - otherwise null
     */
    public Question addQuestion(Question question) {
        try (PreparedStatement addStmt = conn.prepareStatement(INSERT_CMD,
                Statement.RETURN_GENERATED_KEYS)) {
            addStmt.setString(1, question.getQuestionText());
            addStmt.setString(2, question.getCategory());
            addStmt.setString(3, question.getChoiceA());
            addStmt.setString(4, question.getChoiceB());
            addStmt.setString(5, question.getChoiceC());
            addStmt.setString(6, question.getChoiceD());
            addStmt.setString(7, question.getCorrectAnswer());

            addStmt.executeUpdate();

            try (ResultSet resultSet = addStmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                   int questionId = resultSet.getInt(1);
                   return getQuestionById(questionId);
                }
            }
        } catch(SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
    return null;
}

    /**
     * Update an existing question
     * @param question Question object that contains the update
     * @return updated Question object
     */
    public Question updateQuestion(Question question) {
        try (PreparedStatement updateStmt = conn.prepareStatement(UPDATE_CMD)){
            updateStmt.setString(1, question.getQuestionText());
            updateStmt.setString(2, question.getCategory());
            updateStmt.setString(3, question.getChoiceA());
            updateStmt.setString(4, question.getChoiceB());
            updateStmt.setString(5, question.getChoiceC());
            updateStmt.setString(6, question.getChoiceD());
            updateStmt.setString(7, question.getCorrectAnswer());
            updateStmt.setInt(8, question.getQuestionId());

            updateStmt.executeUpdate();

        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return question;
    }

    /**
     * Delete a question from the database
     * @param questionId the ID of the question to be deleted
     * @return true if the question was deleted - otherwise false
     */
    public boolean deleteQuestion(int questionId) {
        try (PreparedStatement deleteStmt = conn.prepareStatement(DELETE_CMD)) {
            deleteStmt.setInt(1, questionId);
            int rowsAffected = deleteStmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }


    /**
     * Maps a ResultSet row to a Question Object
     * @param resultSet result set row to map
     * @return Question object and mapped data
     */
    private Question mapRow(ResultSet resultSet) {
        try {
            return new Question(
                    resultSet.getInt("question_id"),
                    resultSet.getString("question_text"),
                    resultSet.getString("category"),
                    resultSet.getString("choice_a"),
                    resultSet.getString("choice_b"),
                    resultSet.getString("choice_c"),
                    resultSet.getString("choice_d"),
                    resultSet.getString("correct_answer")
            );
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
