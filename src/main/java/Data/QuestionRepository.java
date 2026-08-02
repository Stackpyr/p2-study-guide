/**
 * Responsible for CRUD operations on the question table and hydrating the Question object
 *
 * @author Analiza Boehning
 * @version 0.1.0
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
    // receive one question by its primary key
    private final String GET_BY_QID_CMD = "SELECT * FROM question WHERE question_id = ?";
    // Create
    private final String INSERT_CMD = "INSERT INTO question (account_id, question_text, choice_a, choice_b, choice_c, choice_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?, ?)";
    // Update
    private final String UPDATE_CMD = "UPDATE question SET question_text = ?, choice_a = ?, choice_b= ?, choice_c = ?, choice_d = ?, correct_answer = ? WHERE question_id = ?";
    // Delete
    private final String DELETE_CMD = "DELETE FROM question WHERE question_id = ?";

    /**
     * Constructor for QuestionRepository
     */
    public QuestionRepository() {
        conn = DatabaseManager.getConnection();
    }

    /**
     * Retrieves a question by ID
     *
     * @param questionId ID of the question
     * @return Question object and question information
     */
    public Question getQuestionById(int questionId) {
        try (PreparedStatement selectStmt = conn.prepareStatement(GET_BY_QID_CMD)) {
            selectStmt.setInt(1, questionId);

            try (ResultSet resultSet = selectStmt.executeQuery()) {
                resultSet.next(); // advance to the first row
                return mapRow(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Add Question
     */
    public Question addQuestion(Question question) {
        try (PreparedStatement addStmt = conn.prepareStatement(INSERT_CMD,
                Statement.RETURN_GENERATED_KEYS)) {
            addStmt.setInt(1, question.getAccountId());
            addStmt.setString(2, question.getQuestionText());
            addStmt.setString(3, question.getChoiceA());
            addStmt.setString(4, question.getChoiceB());
            addStmt.setString(5, question.getChoiceC());
            addStmt.setString(6, question.getChoiceD());
            addStmt.setString(7, question.getCorrectAnswer());
            addStmt.executeUpdate();

            try (ResultSet resultSet = addStmt.getGeneratedKeys()) {
                if (!resultSet.next()) {
                    System.out.println("Error: No such question ID");
                } else {
                    return question;
                }
            }
        }
    } catch(SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
    return null;
}

    /**
     * Update question
     */
    public Question updateQuestion(Question question) {
    return null;
    }


    /**
     * Maps a ResultSet row to a Question Object
     * @param resultSet result set row to map
     * @return Question Object and mapped data
     */
    private Question mapRow(ResultSet resultSet) {
        try {
            return new Question(
                    resultSet.getInt("account_id"),
                    resultSet.getInt("question_id"),
                    resultSet.getString("question_text"),
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
