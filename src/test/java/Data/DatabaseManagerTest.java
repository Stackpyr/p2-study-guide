package Data;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;

class DatabaseManagerTest {

  @Test
  void getConnection() {
    assertNotNull(DatabaseManager.getConnection());
  }

  @Test
  void getInstance() {
    assertNotNull(DatabaseManager.getInstance());
  }

  @Test
  void returnsSameInstance() {
    DatabaseManager first = DatabaseManager.getInstance();
    DatabaseManager second = DatabaseManager.getInstance();
    assertSame(first, second);
  }

  @Test
  void returnsSameConnection() {
    Connection first = DatabaseManager.getConnection();
    Connection second = DatabaseManager.getConnection();
    assertSame(first, second);
  }

  @Test
  void getConnection_hasForeignKeysPragmaEnabled() throws SQLException {
    Connection conn = DatabaseManager.getConnection();
    try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("PRAGMA foreign_keys")) {
      assertTrue(rs.next());
      assertEquals(1, rs.getInt(1));
    }
  }

  @Test
  void getInstance_createsSchemaWithAccountTable() throws SQLException {
    // verify that the schema exists and tables were created (only check account)
    Connection conn = DatabaseManager.getConnection();
    try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name='account'")) {
      assertTrue(rs.next());
    }
  }


  @Test
  void close_calledTwice_succeeds() {
    DatabaseManager.getInstance();
    assertDoesNotThrow(DatabaseManager::close);
    assertDoesNotThrow(DatabaseManager::close);
    DatabaseManager.getInstance();
  }

  /**
   * LLM GENERATED - Verifies that calling getConnection() after close() transparently reopens
   * a new, usable Connection instead of returning the stale closed one.
   *
   * NOTE: since the in-memory SQLite URL ("jdbc:sqlite::memory:") is scoped to a single
   * Connection object, the reopened connection is a brand-new, empty in-memory database
   * (schema recreated, but any rows inserted before close() are gone). This test only checks
   * that the reopened connection is a different, open Connection - not that data survives -
   * because that data loss is expected behavior for this DB URL, not a bug to catch.
   */
  @Test
  void close_thenGetConnection_reopensWithNewConnection() throws SQLException {
    Connection before = DatabaseManager.getConnection();
    DatabaseManager.close();
    assertTrue(before.isClosed());

    Connection after = DatabaseManager.getConnection();
    assertNotSame(before, after);
    assertFalse(after.isClosed());
  }
}
