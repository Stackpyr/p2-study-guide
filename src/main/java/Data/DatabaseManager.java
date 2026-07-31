/**
 * Based off of Dr C's DatabaseManager slide Singleton for the database connection and creating the
 * database schema
 *
 * @author: Jason Hamilton
 * @created: 7/30/2026
 * @since: 0.1.0
 */
package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

  private static final String DB_URL = "jdbc:sqlite:otterdobetter.sqlite";
  private static final String DB_SCHEMA_SCRIPT = "/create_database.sql";
  private static DatabaseManager instance; // singleton
  private static Connection connection;

  /**
   * Private constructor to enforce a singleton pattern
   */
  private DatabaseManager() {
    try {
      connection = DriverManager.getConnection(DB_URL);
      try (Statement stmt = connection.createStatement()) {
        // to enforce foreign key constraints
        // probably overkill for this assignment :)
        stmt.execute("PRAGMA foreign_keys = ON");
      }
      createSchema();
    } catch (SQLException e) {
      System.err.println("Error connecting to database: " + e.getMessage());
    }
  }

  public static Connection getConnection() {
    return connection;
  }

  /**
   * Singleton pattern
   * @return instance of DatabaseManager
   */
  public static DatabaseManager getInstance() {
    if (instance == null) {
      instance = new DatabaseManager();
    }
    return instance;
  }

  /**
   * Closes a connection to the database
   */
  public void close() {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    instance = null;
  }

  /**
   * Creates the database schema
   */
  private void createSchema() {
    // load file
    System.out.println("Creating schema...");
    String createSchemaSql = getClass().getResource(DB_SCHEMA_SCRIPT).getFile();
    try (Statement createSchemaStmt = connection.createStatement()) {
      for (String sql : createSchemaSql.split(";")) {
        if (!sql.trim().isEmpty()) {
          createSchemaStmt.execute(sql.trim());
        }
      }
    } catch (SQLException e) {
      System.err.println("Failed to create the database schema: " + e.getMessage());
    }
  }
}
