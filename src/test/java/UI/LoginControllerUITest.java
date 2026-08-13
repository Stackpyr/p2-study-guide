/**
 * Tests basic login functionality using TestFX for the login-view components.
 *
 * @author: Jason Hamilton
 * @created: 8/12/2026
 * @since: 0.1.0
 */
package UI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Data.Account;
import Data.AccountRepository;
import Data.DatabaseManager;
import Service.AuthService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

class LoginControllerUITest extends ApplicationTest {

  private static final String USERNAME = "testfx-login@test.com";
  private static final String PASSWORD = "correct-horse-battery-staple";

  @Override
  public void start(Stage stage) {
    DatabaseManager.close(); // be sure to get a clean in-memory DB for this test
    AuthService.resetForTesting(); // ...and reset the AuthService singleton

    Account account = new Account(USERNAME);
    account.setPassword(PASSWORD);
    new AccountRepository().addAccount(account);

    stage.setScene(SceneFactory.load(SceneType.LOGIN));
    stage.show();
  }

  @Test
  void wrongPassword_showsErrorAndStaysOnLogin() {
    clickOn("#txtUsername").write(USERNAME);
    clickOn("#txtPassword").write("we know this is the wrong password");
    clickOn("#btnLogin");

    Label error = lookup("#lblErrorText").queryAs(Label.class);
    assertTrue(error.getText().startsWith("Login failed"));
    assertFalse(AuthService.getInstance().isLoggedIn());

    // make sure we're still on the login scene
    assertNotNull(lookup("#txtUsername").query());
  }

  @Test
  void blankUsername_showsErrorWithoutTouchingTheDatabase() {
    clickOn("#txtPassword").write(PASSWORD);
    clickOn("#btnLogin");

    Label error = lookup("#lblErrorText").queryAs(Label.class);
    assertTrue(error.getText().startsWith("Login failed"));
    assertFalse(AuthService.getInstance().isLoggedIn());
  }

  @Test
  void correctCredentials_navigatesToDashboard() {
    clickOn("#txtUsername").write(USERNAME);
    clickOn("#txtPassword").write(PASSWORD);
    clickOn("#btnLogin");

    // #takeQuizButton should exist after successful login, so verify
    Button takeQuiz = lookup("#takeQuizButton").queryAs(Button.class);
    assertNotNull(takeQuiz);
    assertTrue(AuthService.getInstance().isLoggedIn());
    assertEquals(USERNAME, AuthService.getInstance().getCurrentAccount().getUsername());
  }
}
