package Data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountRepositoryTest {

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void addAccount() {
    AccountDao account = new AccountDao("testuser@test.com");
    account.setPassword("testpassword");
    account.setDisplayName("Test User");
    account.setIsActive(true);
    account.setIsAdmin(false);
    AccountRepository repo = new AccountRepository();
    AccountDao result = repo.addAccount(account);
    assertNotNull(result);
    assertEquals(account.getUsername(), result.getUsername());
    assertEquals(account.getEmailAddress(), result.getEmailAddress());
    assertEquals(account.getDisplayName(), result.getDisplayName());
    assertEquals(account.getIsActive(), result.getIsActive());
    assertEquals(account.getIsAdmin(), result.getIsAdmin());
    assertEquals(account.getPasswordHash(), result.getPasswordHash());
    assertEquals(account.getPasswordSalt(), result.getPasswordSalt());
    assertTrue(() -> result.getAccountId() > 0);
  }

  @Test
  void getById() {
  }

  @Test
  void getByUsername() {
  }

  @Test
  void updatePassword() {
  }

  @Test
  void updateAdmin() {
  }

  @Test
  void updateStatus() {
  }
}