package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and retrieves quiz attempts from database
 */
public class QuizAttemptRepository {

    private final Connection connection;

    private static final String INSERT_SQL =
            "INSERT INTO quiz_attempt " + "(account_id, score, total_questions, completed_at) " + "VALUES (?, ?, ?, ?)";

    private static final String GET_BY_ID_SQL = "SELECT * FROM quiz_attempt WHERE attempt_id = ?";

    private static final String GET_BY_ACCOUNT_ID_SQL = "SELECT * FROM quiz_attempt " + "WHERE account_id = ? ORDER BY attempt_id";

    private static final String UPDATE_SQL = "UPDATE quiz_attempt " + "SET score = ?, total_questions = ?, completed_at = ? " + "WHERE attempt_id = ?";

    private static final String DELETE_SQL = "DELETE FROM quiz_attempt WHERE attempt_id = ?";

    /**
     * Connects repository to shared database
     */
    public QuizAttemptRepository() {
        connection = DatabaseManager.getConnection();
    }


    /**
     * Saves new quiz attempt
     */
    public QuizAttempt addQuizAttempt(QuizAttempt attempt) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, attempt.getAccountId());
            statement.setInt(2, attempt.getScore());
            statement.setInt(3, attempt.getTotalQuestions());
            statement.setString(4, attempt.getCompletedAt());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return getById(resultSet.getInt(1));
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error adding quiz attempt: " + exception.getMessage());
        }

        return null;
    }


    /**
     * Finds a quiz attempt by its ID
     */
    public QuizAttempt getById(int attemptId) {
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID_SQL)) {

            statement.setInt(1, attemptId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error finding quiz attempt: " + exception.getMessage());
        }

        return null;
    }


    /**
     * Finds all quiz attempts belonging to an account
     */
    public List<QuizAttempt> getByAccountId(int accountId) {
        List<QuizAttempt> attempts = new ArrayList<>();

        try (PreparedStatement statement =
                     connection.prepareStatement(GET_BY_ACCOUNT_ID_SQL)) {

            statement.setInt(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    attempts.add(mapRow(resultSet));
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error finding account attempts: " +
                            exception.getMessage());
        }

        return attempts;
    }


    /**
     * Updates saved quiz attempt
     */
    public QuizAttempt updateQuizAttempt(QuizAttempt attempt) {
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE_SQL)) {

            statement.setInt(1, attempt.getScore());
            statement.setInt(2, attempt.getTotalQuestions());
            statement.setString(3, attempt.getCompletedAt());
            statement.setInt(4, attempt.getAttemptId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return getById(attempt.getAttemptId());
            }
        } catch (SQLException exception) {
            System.out.println("Error updating quiz attempt: " + exception.getMessage());
        }

        return null;
    }


    /**
     * Deletes a quiz attempt by its ID
     */
    public boolean deleteQuizAttempt(int attemptId) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, attemptId);

            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.println("Error deleting quiz attempt: " + exception.getMessage());
        }

        return false;
    }


    /**
     * Converts a database row into a QuizAttempt object.
     */
    private QuizAttempt mapRow(ResultSet resultSet) throws SQLException {
        return new QuizAttempt(
                resultSet.getInt("attempt_id"),
                resultSet.getInt("account_id"),
                resultSet.getInt("score"),
                resultSet.getInt("total_questions"),
                resultSet.getString("completed_at")
        );
    }
}