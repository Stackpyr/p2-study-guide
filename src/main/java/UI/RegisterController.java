package UI;

import Data.Account;
import Data.AccountRepository;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class RegisterController extends BaseController {

  @FXML
  private Label errorText;
  @FXML
  private TextField displayName;
  @FXML
  private TextField username;
  @FXML
  private TextField email;
  @FXML
  private TextField password;
  @FXML
  private TextField confirmPassword;

  @FXML
  protected void onCreateAccountClick(MouseEvent event) throws IOException {
    // This should all go into a service on its own, but doing this for simplicity
    if (displayName == null || displayName.getText().isEmpty() ||
        username == null || username.getText().isEmpty() ||
        email == null || email.getText().isEmpty() ||
        password == null || password.getText().isEmpty() ||
        confirmPassword == null || confirmPassword.getText().isEmpty()) {

      errorText.setText("Error: All fields are required");
      return;
    }

    if (!password.getText().equals(confirmPassword.getText())) {
      errorText.setText("Error: Passwords do not match");
      return;
    }

    // password complexity - just require at least 4 characters to make this easy
    if (password.getText().length() < 4) {
      errorText.setText("Error: Password must be at least 4 characters long");
      return;
    }

    Account account = new Account(email.getText(), username.getText());
    account.setDisplayName(displayName.getText());
    account.setPassword(password.getText());
    account.setIsActive(true);
    account.setIsAdmin(false);

    AccountRepository repo = new AccountRepository();
    // check to see if the username is already taken
    if (repo.getByUsername(username.getText()).getAccountId() > 0) {
      errorText.setText("Error: Username already exists. Try again");
      return;
    }

    account = repo.addAccount(account);
    if (account == null) {
      errorText.setText("Error: Could not create account. Please try again.");
      return;
    }

    LoginController loginController = swapScene(event, "login-view.fxml");
    loginController.showStatus("Account created! Please log in.");
  }

  @FXML
  protected void onBackToLoginClick(MouseEvent event) throws IOException {
    swapScene(event, "login-view.fxml");
  }

}
