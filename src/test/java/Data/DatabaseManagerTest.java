package Data;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
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

}