package UI;

import Service.AuthResult;
import Service.AuthService;
import java.io.IOException;
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

  @FXML
  protected void onLoginClick(MouseEvent event) throws IOException {
    AuthResult result = AuthService.getInstance().login(username.getText(), password.getText());
    if (result.getCode() == AuthResult.SUCCESS.getCode()) {
      swapScene(event, "dashboard-view.fxml");
    } else {
      errorText.setTextFill(Color.RED);
      errorText.setText("Login failed: " + result.getMessage());
    }
  }

  @FXML
  protected void onRegisterClick(MouseEvent event) throws IOException {
    swapScene(event, "register-view.fxml");
  }

  /**
   * Lets another controller (e.g. RegisterController, after a successful sign-up) show a
   * message on this scene once it's loaded, instead of the default red error styling.
   */
  protected void showStatus(String message) {
    errorText.setTextFill(Color.GREEN);
    errorText.setText(message);
  }

}
