/**
 * This class handles the login click and register click events
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

package UI;

import Service.AuthResult;
import Service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class LoginController extends BaseController {

  public Button login;
  public Button register;
  @FXML
  private Label errorText;
  @FXML
  private TextField username;
  @FXML
  private TextField password;

  /**
   * Attempts to log the user in
   * @param event the mouse click event
   */
  @FXML
  protected void onLoginClick(MouseEvent event) {
    AuthResult result = AuthService.getInstance().login(username.getText(), password.getText());
    if (result.getCode() == AuthResult.SUCCESS.getCode()) {
      swapScene(event, SceneType.DASHBOARD);
    } else {
      errorText.setTextFill(Color.RED);
      errorText.setText("Login failed: " + result.getMessage());
    }
  }

  /**
   * Swaps to the register scene
   * @param event the mouse click event
   */
  @FXML
  protected void onRegisterClick(MouseEvent event) {
    swapScene(event, SceneType.REGISTER);
  }

}
