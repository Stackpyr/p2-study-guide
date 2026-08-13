/**
 * These tests verify that the SceneFactory produces the expected scenes for the various SceneTypes.
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;

class SceneFactoryTest {

  @BeforeAll
  static void bootToolkit() throws Exception {
    // Starts the JavaFX toolkit
    FxToolkit.registerPrimaryStage();
  }

  @BeforeEach
  void freshDbAndSession() {
    // be sure to get a clean in-memory DB for this test and reset the AuthService singleton
    DatabaseManager.close();
    AuthService.resetForTesting();
  }

  /**
   * Verifies that every SceneType produces a non-null scene with a non-null root.
   */
  @Test
  void everySceneTypeBuildsWithARoot() throws Exception {
    for (SceneType type : SceneType.values()) {
      Scene scene = FxToolkit.setupScene(() -> SceneFactory.load(type));
      assertNotNull(scene, "SceneFactory.load(" + type + ") returned a null scene");

      Parent root = scene.getRoot();
      assertNotNull(root, "SceneFactory.load(" + type + ") built a scene with no root");
    }
  }

  /**
   * Verifies that the dashboard scene hides the account manager button for non-admins.
   */
  @Test
  void dashboardScene_hidesAccountManagerButtonForNonAdmin() throws Exception {
    logInAs(false);

    Scene scene = FxToolkit.setupScene(() -> SceneFactory.load(SceneType.DASHBOARD));
    Button manageAccounts = (Button) scene.lookup("#manageAccountsButton");

    assertNotNull(manageAccounts);
    assertFalse(manageAccounts.isVisible());
    assertFalse(manageAccounts.isManaged());
  }

  /**
   * Verifies that the dashboard scene shows the account manager button for admins.
   */
  @Test
  void dashboardScene_showsAccountManagerButtonForAdmin() throws Exception {
    logInAs(true);

    Scene scene = FxToolkit.setupScene(() -> SceneFactory.load(SceneType.DASHBOARD));
    Button manageAccounts = (Button) scene.lookup("#manageAccountsButton");

    assertNotNull(manageAccounts);
    assertTrue(manageAccounts.isVisible());
  }

  /**
   * Creates a single test user and logs that user in to support our test cases.
   */
  private void logInAs(boolean isAdmin) {
    Account account = new Account("test@user.com");
    account.setPassword("testpassword");
    account.setIsAdmin(isAdmin);
    account = new AccountRepository().addAccount(account);

    var result = AuthService.getInstance().login(account.getUsername(), "testpassword");
    assertEquals(Service.AuthResult.SUCCESS.getCode(), result.getCode(),
        "test setup failed to log in: " + result.getMessage());
  }
}
