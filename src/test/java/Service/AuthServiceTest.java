package Service;

import static org.junit.jupiter.api.Assertions.*;

import Data.Account;
import Data.AccountRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * [Explanation]
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

  @Test
  void isAdmin() {
  }

  @Test
  void getCurrentAccount() {
    AuthService authService = AuthService.getInstance();
    assertNull(authService.getCurrentAccount());

    assertTrue(!authService.isLoggedIn()); // not logged in yet
    // check successful login
    AuthResult result = authService.login(TEST_USERNAME, TEST_PASSWORD);
    assertTrue(result.getCode() == AuthResult.SUCCESS.getCode());
    assertEquals(TEST_ACCOUNT, authService.getCurrentAccount());

    // logout
    authService.logout();
    assertNull(authService.getCurrentAccount());

    // swap account
    String testUsername2 = "test2@test.com";
    String testPassword2 = "testpassword";
    Account testAccount2 = new Account(testUsername2);
    testAccount2.setPassword(testPassword2);
    new AccountRepository().addAccount(testAccount2);
    // check successful login
    AuthResult result2 = authService.login(testUsername2, testPassword2);
    assertTrue(result2.getCode() == AuthResult.SUCCESS.getCode());
    assertEquals(testAccount2, authService.getCurrentAccount());
  }
}