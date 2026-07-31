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

    public QuestionRepository() {
        conn = DatabaseManager.getConnection();
    }


}
