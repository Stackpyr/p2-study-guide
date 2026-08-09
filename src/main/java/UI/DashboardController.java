/**
 * Placeholder controller for the post-login landing scene. Doesn't do much yet. It's here so
 * onLoginClick can send the user somewhere and everyone else can build their scenes.
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

package UI;

import Service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class DashboardController extends BaseController {

  @FXML
  public HBox titleBar;
  @FXML
  public Button logout;
  @FXML
  private Button takeQuizButton;
  @FXML
  private Button manageAccountsButton;
  @FXML
  private Label statusText;

  @Override
  @FXML
  protected void initialize() {
    super.initialize(); // still need the title bar's sessionLabel set

    // everyone can take a quiz, but only admins get the Account Manager button -
    // hide it entirely (not just disable it) so a non-admin doesn't even know it's there
    boolean isAdmin = AuthService.getInstance().isAdmin();
    manageAccountsButton.setVisible(isAdmin);
    manageAccountsButton.setManaged(isAdmin);
  }

  @FXML
  protected void onTakeQuizClick(MouseEvent event) {
    // TODO: once Question Bank / Quiz Engine has a scene, swap to it instead
    // swapScene(event, SceneType.QUIZ);
    statusText.setText("Quiz taking isn't built yet - check back soon!");
  }

  @FXML
  protected void onManageAccountsClick(MouseEvent event) {
    // Only allow admins
    if (!AuthService.getInstance().isAdmin()) {
      return;
    }
    // TODO: once Account Manager has a scene, swap to it instead
    // swapScene(event, SceneType.ACCOUNT_MANAGER);
    statusText.setText("Account Manager isn't built yet - check back soon!");
  }

  @FXML
  protected void onLogoutClick(MouseEvent event) {
    AuthService.getInstance().logout();
    swapScene(event, SceneType.LOGIN);
  }

  @FXML
  protected void onQuizClick(MouseEvent event) {
    swapScene(event, SceneType.QUIZ);
  }

}
