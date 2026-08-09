package Service;

import static org.junit.jupiter.api.Assertions.*;

import Data.Account;
import Data.AccountRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for AuthService
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

class AuthServiceTest {

  private static final String TEST_USERNAME = "test@test.com";
  private static final String TEST_PASSWORD = "testpassword";
  private static Account TEST_ACCOUNT;

  @BeforeAll
  static void setUp() {
    TEST_ACCOUNT = new Account(TEST_USERNAME);
    TEST_ACCOUNT.setPassword(TEST_PASSWORD);
    new AccountRepository().addAccount(TEST_ACCOUNT);
  }

  @Test
  void getInstance() {
    assertNotNull(AuthService.getInstance());
  }

  @Test
  void login() {
    AuthService authService = AuthService.getInstance();

    // check successful login
    AuthResult result = authService.login(TEST_USERNAME, TEST_PASSWORD);
    assertTrue(result.getCode() == AuthResult.SUCCESS.getCode());
    assertTrue(authService.isLoggedIn()); // should be logged in

    // check failed login with bad user & password
    AuthResult result2 = authService.login("baduser", "badpassword");
    assertTrue(result2.getCode() == AuthResult.INCORRECT_USER_PASSWORD.getCode());
    assertTrue(!authService.isLoggedIn()); // should not be logged in

    // check failed login with good user & bad password
    AuthResult result3 = authService.login(TEST_USERNAME, "badpassword");
    assertTrue(result3.getCode() == AuthResult.INCORRECT_USER_PASSWORD.getCode());
    assertTrue(!authService.isLoggedIn()); // should not be logged in
  }

  /**
   * LLM GENERATED - Verifies that login() rejects a blank username with USERNAME_BLANK before
   * ever touching the database.
   */
  @Test
  void login_blankUsername_returnsUsernameBlank() {
    AuthResult result = AuthService.getInstance().login("", TEST_PASSWORD);
    assertTrue(result.getCode() == AuthResult.USERNAME_BLANK.getCode());
  }

  /**
   * LLM GENERATED - Verifies that login() rejects a blank password with PASSWORD_BLANK.
   */
  @Test
  void login_blankPassword_returnsPasswordBlank() {
    AuthResult result = AuthService.getInstance().login(TEST_USERNAME, "");
    assertTrue(result.getCode() == AuthResult.PASSWORD_BLANK.getCode());
  }

  /**
   * LLM GENERATED - Verifies that logging in with correct credentials for a suspended
   * (is_active = false) account returns ACCOUNT_NOT_ACTIVE and does not establish a session,
   * even though the password itself was correct.
   */
  @Test
  void login_inactiveAccount_returnsAccountNotActiveAndDoesNotLogIn() {
    AuthService authService = AuthService.getInstance();

    String username = "suspended@test.com";
    String password = "testpassword";
    Account account = new Account(username);
    account.setPassword(password);
    Account saved = new AccountRepository().addAccount(account);
    new AccountRepository().updateStatus(saved.getAccountId(), false); // suspend it

    AuthResult result = authService.login(username, password);
    assertTrue(result.getCode() == AuthResult.ACCOUNT_NOT_ACTIVE.getCode());
    assertTrue(!authService.isLoggedIn());
  }

  @Test
  void logout() {
    AuthService authService = AuthService.getInstance();

    assertTrue(!authService.isLoggedIn()); // not logged in yet
    // check successful login
    AuthResult result = authService.login(TEST_USERNAME, TEST_PASSWORD);
    assertTrue(result.getCode() == AuthResult.SUCCESS.getCode());
    assertTrue(authService.isLoggedIn());

    // now logout
    authService.logout();
    assertTrue(!authService.isLoggedIn());
  }

  /**
   * LLM GENERATED - Verifies isAdmin(): false when nobody is logged in, false for a logged-in
   * non-admin account, and true once logged in as an account with isAdmin = true.
   */
  @Test
  void isAdmin() {
    AuthService authService = AuthService.getInstance();
    assertFalse(authService.isAdmin()); // nobody logged in yet

    AuthResult result = authService.login(TEST_USERNAME, TEST_PASSWORD);
    assertTrue(result.getCode() == AuthResult.SUCCESS.getCode());
    assertFalse(authService.isAdmin()); // TEST_ACCOUNT is not an admin
    authService.logout();

    String adminUsername = "admin@test.com";
    String adminPassword = "adminpassword";
    Account adminAccount = new Account(adminUsername);
    adminAccount.setPassword(adminPassword);
    adminAccount.setIsAdmin(true);
    new AccountRepository().addAccount(adminAccount);

    AuthResult adminResult = authService.login(adminUsername, adminPassword);
    assertTrue(adminResult.getCode() == AuthResult.SUCCESS.getCode());
    assertTrue(authService.isAdmin());
    authService.logout();
  }

  @Test
  void getCurrentAccount() {
    AuthService authService = AuthService.getInstance();
    assertNull(authService.getCurrentAccount());

    assertTrue(!authService.isLoggedIn()); // not logged in yet
    // check successful login
    AuthResult result = authService.login(TEST_USERNAME, TEST_PASSWORD);
    assertTrue(result.getCode() == AuthResult.SUCCESS.getCode());
    assertEquals(TEST_USERNAME, authService.getCurrentAccount().getUsername());

    // logout
    authService.logout();
    assertNull(authService.getCurrentAccount());

    // swap account
    String testUsername2 = "test2@test.com";
    String testPassword2 = "testpassword";
    Account testAccount2 = new Account(testUsername2);
    testAccount2.setPassword(testPassword2);
    testAccount2 = new AccountRepository().addAccount(testAccount2);
    // check successful login
    AuthResult result2 = authService.login(testUsername2, testPassword2);
    assertTrue(result2.getCode() == AuthResult.SUCCESS.getCode());
    assertEquals(testAccount2, authService.getCurrentAccount());

    authService.logout();
  }
}
