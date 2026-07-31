package UI;

import Service.AuthResult;
import Service.AuthService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {

  public Button login;
  public Button register;
  @FXML
  private Label errorText;
  @FXML
  private TextField username;
  @FXML
  private TextField password;

  // tracks where inside the title bar the mouse was pressed, so dragging
  // moves the window by the same offset instead of snapping its corner
  // to the cursor
  private double dragOffsetX;
  private double dragOffsetY;

  @FXML
  protected void onLoginClick() {
    AuthResult result = AuthService.getInstance().login(username.getText(), password.getText());
    if (result.getCode() == AuthResult.SUCCESS.getCode()) {
      errorText.setText("Login successful!");
    } else {
      errorText.setText("Login failed: " + result.getMessage());
    }

  }

  @FXML
  protected void onRegisterClick() {
    errorText.setText("Error: This is not yet supported.");
  }

  @FXML
  protected void onTitleBarPressed(MouseEvent event) {
    dragOffsetX = event.getSceneX();
    dragOffsetY = event.getSceneY();
  }

  @FXML
  protected void onTitleBarDragged(MouseEvent event) {
    Stage stage = stageOf(event);
    stage.setX(event.getScreenX() - dragOffsetX);
    stage.setY(event.getScreenY() - dragOffsetY);
  }

  @FXML
  protected void onMinimizeClick(Event event) {
    stageOf(event).setIconified(true);
  }

  @FXML
  protected void onCloseClick(Event event) {
    stageOf(event).close();
  }

  private Stage stageOf(Event event) {
    return (Stage) ((Node) event.getSource()).getScene().getWindow();
  }

}
