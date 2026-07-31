package UI;

import Service.AuthResult;
import Service.AuthService;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class RegisterController extends BaseController {

  public Button login;
  public Button register;
  @FXML
  private Label errorText;
  @FXML
  private TextField username;
  @FXML
  private TextField password;

  @FXML
  protected void onLoginClick() {
    AuthResult result = AuthService.getInstance().login(username.getText(), password.getText());
    if (result.getCode() == AuthResult.SUCCESS.getCode()) {
      errorText.setText("Login successful!");
      //TODO: This needs to transition to the landing scene
    } else {
      errorText.setText("Login failed: " + result.getMessage());
    }
  }

  @FXML
  protected void onCreateAccountClick() {
    errorText.setText("Error: This is not yet supported.");
  }

  @FXML
  protected void onBackToLoginClick(MouseEvent event) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
    Scene loginScene = null;
    try {
      loginScene = new Scene(loader.load());
    } catch (IOException e) {
      System.out.println("Error loading login scene: " + e.getMessage());
    }
    stageOf(event).setScene(loginScene);
  }

}
