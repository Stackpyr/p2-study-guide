package UI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController {

  @FXML
  private Label messageText;

  @FXML
  protected void onLoginClick() {
    messageText.setText("Error: Invalid username or password.");
  }

  @FXML
  protected void onRegisterClick() {
    messageText.setText("Error: This is not yet supported.");
  }

}
