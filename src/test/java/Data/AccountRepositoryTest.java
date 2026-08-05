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
    Account account = new Account("testuser@test.com");
    account.setPassword("testpassword");
    account.setDisplayName("Test User");
    account.setIsActive(true);
    account.setIsAdmin(false);
    AccountRepository repo = new AccountRepository();
    Account result = repo.addAccount(account);
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

  /**
   * LLM GENERATED - Verifies that inserting a second account with a username that already
   * exists returns null instead of throwing, relying on the same UNIQUE constraint
   * RegisterController's duplicate-username check uses as a backstop.
   */
  @Test
  void addAccount_duplicateUsername_returnsNull() {
    // this is the same UNIQUE constraint RegisterController's duplicate-username check
    // relies on as a backstop
    AccountRepository repo = new AccountRepository();

    Account first = new Account("dup1@test.com", "dupuser");
    first.setPassword("testpassword");
    assertNotNull(repo.addAccount(first));

    Account second = new Account("dup2@test.com", "dupuser"); // same username, different email
    second.setPassword("testpassword");
    assertNull(repo.addAccount(second));
  }

  /**
   * LLM GENERATED - Verifies that an account saved via addAccount() can be re-fetched by its
   * generated accountId, and that the fetched copy matches what was saved.
   */
  @Test
  void getById() {
    AccountRepository repo = new AccountRepository();
    Account account = new Account("getbyid@test.com");
    account.setPassword("testpassword");
    Account saved = repo.addAccount(account);

    Account found = repo.getById(saved.getAccountId());
    assertNotNull(found);
    assertEquals(saved.getUsername(), found.getUsername());
    assertEquals(saved.getEmailAddress(), found.getEmailAddress());
  }

  /**
   * LLM GENERATED - Verifies that getById() returns null (rather than throwing) for an id that
   * doesn't exist.
   */
  @Test
  void getById_unknownId_returnsNull() {
    AccountRepository repo = new AccountRepository();
    assertNull(repo.getById(-1));
  }

  /**
   * LLM GENERATED - Verifies that an account saved via addAccount() can be re-fetched by
   * username.
   */
  @Test
  void getByUsername() {
    AccountRepository repo = new AccountRepository();
    Account account = new Account("getbyusername@test.com", "getbyusernameuser");
    account.setPassword("testpassword");
    repo.addAccount(account);

    Account found = repo.getByUsername("getbyusernameuser");
    assertNotNull(found);
    assertEquals("getbyusername@test.com", found.getEmailAddress());
  }

  /**
   * LLM GENERATED - Verifies that getByUsername() returns null (rather than throwing) for a
   * username that doesn't exist - this is the exact behavior AuthService.login() and
   * RegisterController's duplicate check both depend on.
   */
  @Test
  void getByUsername_unknownUsername_returnsNull() {
    AccountRepository repo = new AccountRepository();
    assertNull(repo.getByUsername("no-such-user-should-exist"));
  }

  @Test
  void updatePassword() {
    AccountRepository repo = new AccountRepository();
    Account account = new Account("updatepw@test.com");
    account.setPassword("originalpassword");
    Account saved = repo.addAccount(account);

    Account newPasswordHolder = new Account("newpwholder@test.com");
    newPasswordHolder.setPassword("newpassword");
    repo.updatePassword(saved.getAccountId(), newPasswordHolder.getPasswordHash(),
        newPasswordHolder.getPasswordSalt());

    Account updated = repo.getById(saved.getAccountId());
    assertEquals(newPasswordHolder.getPasswordHash(), updated.getPasswordHash());
    assertEquals(newPasswordHolder.getPasswordSalt(), updated.getPasswordSalt());
    assertTrue(updated.verifyPassword("newpassword"));
    assertFalse(updated.verifyPassword("originalpassword"));
  }

  @Test
  void updateAdmin() {
    AccountRepository repo = new AccountRepository();
    Account account = new Account("updateadmin@test.com");
    account.setPassword("testpassword");
    Account saved = repo.addAccount(account);
    assertFalse(saved.getIsAdmin()); // default

    repo.updateAdmin(saved.getAccountId(), true);
    assertTrue(repo.getById(saved.getAccountId()).getIsAdmin());

    repo.updateAdmin(saved.getAccountId(), false);
    assertFalse(repo.getById(saved.getAccountId()).getIsAdmin());
  }

  @Test
  void updateStatus() {
    AccountRepository repo = new AccountRepository();
    Account account = new Account("updatestatus@test.com");
    account.setPassword("testpassword");
    Account saved = repo.addAccount(account);
    assertTrue(saved.getIsActive()); // default

    repo.updateStatus(saved.getAccountId(), false);
    assertFalse(repo.getById(saved.getAccountId()).getIsActive());

    repo.updateStatus(saved.getAccountId(), true);
    assertTrue(repo.getById(saved.getAccountId()).getIsActive());
  }
}
