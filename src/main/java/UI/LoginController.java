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
import Service.LinkedInOAuthProvider;
import Service.OAuthProvider;
import javafx.application.Platform;
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
  private Button linkedInLogin;
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

  /**
   * Attempts to log the user in with LinkedIn oauth
   *
   * @param event the mouse click event
   */
  @FXML
  protected void onLinkedInLoginClick(MouseEvent event) {
    signInWithProvider(event, new LinkedInOAuthProvider());
  }

  /**
   * Attempts to log the user in with the given OAuth provider.
   * This is done in a separate thread to avoid blocking the UI.
   *
   * @param event the mouse click event that triggered this
   * @param provider the provider to sign in with
   */
  private void signInWithProvider(MouseEvent event, OAuthProvider provider) {

    errorText.setText("");
    linkedInLogin.setDisable(true);

    new Thread(() -> {
      AuthResult result;
      String failureMessage = null;

      try {
        OAuthProvider.Profile profile = provider.authenticate();
        result = AuthService.getInstance().loginWithOAuth(profile);
      } catch (Exception e) {
        result = null;
        failureMessage = e.getMessage();
      }

      AuthResult finalResult = result;
      String finalFailureMessage = failureMessage;

      Platform.runLater(() -> {
        linkedInLogin.setDisable(false);
        if (finalResult != null && finalResult.getCode() == AuthResult.SUCCESS.getCode()) {
          swapScene(event, SceneType.DASHBOARD);
        } else {
          errorText.setTextFill(Color.RED);
          errorText.setText(provider.getProviderName() + " sign-in failed: "
              + (finalResult != null ? finalResult.getMessage() : finalFailureMessage));
        }
      });
    }, provider.getProviderName() + "-oauth").start();
  }

}
