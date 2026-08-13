/**
 * Controller for the Account Administration scene. Lets an admin search accounts, suspend or
 * reactivate them, reset a user's password, grant/revoke admin access, and delete accounts
 * (blocked when the account has quiz history).
 *
 * @author: Jason Hamilton
 * @created: 8/8/2026
 * @since: 0.1.0
 */

package UI;

import Data.Account;
import Data.AccountRepository;
import Data.QuizAttempt;
import Data.QuizAttemptRepository;
import Service.AuthService;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AccountAdminController extends BaseController {

  // Allowable characters for the newly generated password (all characters - upper and lower case)
  private static final int ASCII_PRINTABLE_START = 33;
  private static final int ASCII_PRINTABLE_END = 126;
  private static final int GENERATED_PASSWORD_LENGTH = 12;

  private final AccountRepository accountRepository = new AccountRepository();
  private final QuizAttemptRepository quizAttemptRepository = new QuizAttemptRepository();
  private final ObservableList<Account> allAccounts = FXCollections.observableArrayList();
  private final SecureRandom random = new SecureRandom();

  @FXML
  private TextField searchField;
  @FXML
  private TableView<Account> accountsTable;
  @FXML
  private TableColumn<Account, String> usernameColumn;
  @FXML
  private TableColumn<Account, String> emailColumn;
  @FXML
  private TableColumn<Account, String> displayNameColumn;
  @FXML
  private TableColumn<Account, Boolean> activeColumn;
  @FXML
  private TableColumn<Account, Boolean> adminColumn;
  @FXML
  private TableColumn<Account, Void> actionsColumn;
  @FXML
  private Label statusLabel;

  /**
   * Initializes the scene and wires up the table columns.
   */
  @Override
  @FXML
  protected void initialize() {
    super.initialize();

    usernameColumn.setCellValueFactory(cellData ->
        new SimpleStringProperty(cellData.getValue().getUsername()));
    emailColumn.setCellValueFactory(cellData ->
        new SimpleStringProperty(cellData.getValue().getEmailAddress()));
    displayNameColumn.setCellValueFactory(cellData ->
        new SimpleStringProperty(cellData.getValue().getDisplayName()));
    activeColumn.setCellValueFactory(cellData ->
        new SimpleBooleanProperty(cellData.getValue().getIsActive()));
    adminColumn.setCellValueFactory(cellData ->
        new SimpleBooleanProperty(cellData.getValue().getIsAdmin()));

    activeColumn.setCellFactory(col -> statusBadgeCell("Active", "Suspended"));
    adminColumn.setCellFactory(col -> statusBadgeCell("Admin", "User"));
    actionsColumn.setCellFactory(col -> new ActionsCell());

    FilteredList<Account> filteredAccounts = new FilteredList<>(allAccounts, account -> true);
    searchField.textProperty().addListener((obs, oldText, newText) ->
        filteredAccounts.setPredicate(account -> matchesSearch(account, newText)));

    SortedList<Account> sortedAccounts = new SortedList<>(filteredAccounts);
    sortedAccounts.comparatorProperty().bind(accountsTable.comparatorProperty());
    accountsTable.setItems(sortedAccounts);

    refreshAccounts(); // populate the grid as soon as the scene renders
  }

  /**
   * Reloads the account list from the database.
   */
  private void refreshAccounts() {
    List<Account> accounts = accountRepository.getAllAccounts();
    allAccounts.setAll(accounts);
  }

  /**
   * Case insensitive match against username, email, or display name.
   *
   * @param account the account to test
   * @param query the search text
   * @return true if the account should be shown for the given search
   */
  private boolean matchesSearch(Account account, String query) {
    if (query == null || query.isBlank()) {
      return true;
    }
    String lowerQuery = query.trim().toLowerCase();
    return containsIgnoreCase(account.getUsername(), lowerQuery)
        || containsIgnoreCase(account.getEmailAddress(), lowerQuery)
        || containsIgnoreCase(account.getDisplayName(), lowerQuery);
  }

  /**
   * Searches the text for a given searchText string, ignoring case.
   *
   * @param text the string to search
   * @param searchText the text to search for
   * @return true if there's a match
   */
  private boolean containsIgnoreCase(String text, String searchText) {
    return text != null && text.toLowerCase().contains(searchText);
  }

  /**
   * Builds a badge cell for the Active/Admin boolean columns.
   *
   * @param whenTrue text to show when the column's value is true
   * @param whenFalse text to show when the column's value is false
   * @return a table cell that renders one of the two labels based on the row's value
   */
  private TableCell<Account, Boolean> statusBadgeCell(String whenTrue, String whenFalse) {
    return new TableCell<>() {
      @Override
      protected void updateItem(Boolean value, boolean empty) {
        super.updateItem(value, empty);
        setText(empty || value == null ? null : (value ? whenTrue : whenFalse));
      }
    };
  }

  /**
   * Updates the status label at the bottom of the scene.
   *
   * @param message the text to display
   * @param isError true to style the message as an error (red); false for success (green)
   */
  private void setStatus(String message, boolean isError) {
    statusLabel.setText(message);
    statusLabel.setStyle(isError ? "-fx-text-fill: #b40b0b;" : "-fx-text-fill: #0b7a1f;");
  }

  /**
   * Checks whether the given row belongs to the admin who's currently logged in (if so, it may
   * block certain actions since you can't modify your own account)
   *
   * @param account the row's account
   * @return true if account is the currently logged-in admin's own account
   */
  private boolean isCurrentAdmin(Account account) {
    Account current = AuthService.getInstance().getCurrentAccount();
    return current != null && current.getAccountId() == account.getAccountId();
  }

  /**
   * Generates a new random password from the ASCII range.
   *
   * @return a newly generated password
   */
  private String generatePassword() {
    int range = ASCII_PRINTABLE_END - ASCII_PRINTABLE_START + 1;
    StringBuilder sb = new StringBuilder(GENERATED_PASSWORD_LENGTH);
    for (int i = 0; i < GENERATED_PASSWORD_LENGTH; i++) {
      sb.append((char) (ASCII_PRINTABLE_START + random.nextInt(range)));
    }
    return sb.toString();
  }

  /**
   * Toggles an account's active status between suspended and reactivated.
   *
   * @param account the account whose status button was clicked
   */
  private void suspendReactivate(Account account) {
    if (isCurrentAdmin(account)) {
      setStatus("You can't suspend your own account.", true);
      return;
    }
    boolean newStatus = !account.getIsActive();
    accountRepository.updateStatus(account.getAccountId(), newStatus);
    setStatus((newStatus ? "Reactivated " : "Suspended ") + account.getUsername() + ".", false);
    refreshAccounts();
  }

  /**
   * Toggles an account's admin flag.
   *
   * @param account the account whose admin button was clicked
   */
  private void grantRevokeAdmin(Account account) {
    if (isCurrentAdmin(account)) {
      setStatus("You can't change your own admin access.", true);
      return;
    }
    boolean newAdmin = !account.getIsAdmin();
    accountRepository.updateAdmin(account.getAccountId(), newAdmin);
    setStatus((newAdmin ? "Granted admin access to " : "Revoked admin access from ")
        + account.getUsername() + ".", false);
    refreshAccounts();
  }

  /**
   * Lets the admin set a new password for an account - either typed in directly or generated -
   * then saves it. Note: there is no email functionality in this application yet (or likely ever), so the admin
   * has to relay the password to the user some other way.
   *
   * @param account the account whose password is being reset
   */
  private void resetPassword(Account account) {
    Optional<String> chosen = promptForPassword(account.getUsername());
    if (chosen.isEmpty()) {
      return; // cancelled
    }
    String newPassword = chosen.get();
    if (newPassword.isBlank()) {
      setStatus("Password can't be blank.", true);
      return;
    }

    // Reuse Account's own hashing logic to make it easy and consistent
    Account acct = new Account(account.getEmailAddress(), account.getUsername());
    acct.setPassword(newPassword);
    accountRepository.updatePassword(account.getAccountId(), acct.getPasswordHash(),
        acct.getPasswordSalt());

    setStatus("Password reset for " + account.getUsername(), false);
  }

  /**
   * Shows a dialog to manage the user's password (generate a new one or type one in)
   *
   * @param username the account the password is being set for
   * @return the password the admin chose
   */
  private Optional<String> promptForPassword(String username) {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Password Reset"); // not visible
    dialog.initStyle(StageStyle.UNDECORATED);
    if (statusLabel.getScene() != null) {
      dialog.initOwner(statusLabel.getScene().getWindow());
    }

    // Custom title bar
    Label titleLabel = new Label("Password Reset");
    titleLabel.setTextFill(Color.WHITE);
    Button closeButton = new Button("X");
    closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
    closeButton.setOnAction(e -> {
      dialog.setResult(null);
      dialog.hide();
    });
    HBox titleBar = new HBox(titleLabel, new Region(), closeButton);
    HBox.setHgrow(titleBar.getChildren().get(1), Priority.ALWAYS); // spacer pushes X to the right
    titleBar.setAlignment(Pos.CENTER_LEFT);
    titleBar.setPrefHeight(32.0);
    titleBar.setPadding(new Insets(0, 10, 0, 10));
    titleBar.setStyle("-fx-background-color: #0b49b4;");

    // dragging the title bar moves the dialog's window
    double[] dragOffset = new double[2];
    titleBar.setOnMousePressed(e -> {
      dragOffset[0] = e.getSceneX();
      dragOffset[1] = e.getSceneY();
    });
    titleBar.setOnMouseDragged(e -> {
      Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
      stage.setX(e.getScreenX() - dragOffset[0]);
      stage.setY(e.getScreenY() - dragOffset[1]);
    });

    TextField passwordField = new TextField();
    passwordField.setPromptText("Type a password, or click Generate");
    passwordField.setStyle("-fx-font-family: 'Consolas', 'Menlo', monospace; -fx-font-size: 16px;");
    passwordField.setPrefColumnCount(GENERATED_PASSWORD_LENGTH);

    Button generateButton = new Button("Generate");
    generateButton.setOnAction(e -> {
      passwordField.setText(generatePassword());
      passwordField.requestFocus();
      passwordField.selectAll();
    });

    Button copyButton = new Button("Copy");
    copyButton.setOnAction(e -> {
      ClipboardContent clipboardContent = new ClipboardContent();
      clipboardContent.putString(passwordField.getText());
      Clipboard.getSystemClipboard().setContent(clipboardContent);
    });

    HBox passwordRow = new HBox(8.0, passwordField, generateButton, copyButton);
    passwordRow.setAlignment(Pos.CENTER_LEFT);

    VBox body = new VBox(10.0,
        new Label("New password for " + username + ":"),
        passwordRow);
    body.setPadding(new Insets(20.0));

    BorderPane content = new BorderPane();
    content.setTop(titleBar);
    content.setCenter(body);
    dialog.getDialogPane().setContent(content);

    ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
    dialog.setResultConverter(
        buttonType -> buttonType == saveButtonType ? passwordField.getText() : null);

    return dialog.showAndWait();
  }

  /**
   * Deletes an account unless it has quiz history which displays an error
   *
   * @param account the account whose delete button was clicked
   */
  private void delete(Account account) {
    if (isCurrentAdmin(account)) {
      setStatus("You can't delete your own account.", true);
      return;
    }

    List<QuizAttempt> attempts = quizAttemptRepository.getByAccountId(account.getAccountId());
    if (attempts != null && !attempts.isEmpty()) {
      setStatus(account.getUsername() + " has quiz history and can't be deleted.", true);
      return;
    }

    Alert confirm = new Alert(AlertType.CONFIRMATION,
        "Delete " + account.getUsername() + "? This can't be undone.", ButtonType.YES,
        ButtonType.NO);
    Optional<ButtonType> result = confirm.showAndWait();
    if (result.isEmpty() || result.get() != ButtonType.YES) {
      return;
    }

    accountRepository.deleteAccount(account.getAccountId());
    setStatus("Deleted " + account.getUsername() + ".", false);
    refreshAccounts();
  }

  /**
   * "Back to Dashboard" button click
   *
   * @param event the button click event
   */
  @FXML
  protected void onBackClick(ActionEvent event) {
    swapScene(event, SceneType.DASHBOARD);
  }

  /**
   * Logs the current admin out and returns to the login scene
   *
   * @param event the mouse click event
   */
  @FXML
  protected void onLogoutClick(MouseEvent event) {
    AuthService.getInstance().logout();
    swapScene(event, SceneType.LOGIN);
  }

  /**
   * Draws the Suspend/Reactivate, Reset Password, Grant/Revoke Admin, and Delete buttons
   */
  private class ActionsCell extends TableCell<Account, Void> {
    private static final String ICON_STYLE =
        "-fx-font-size: 13px; -fx-padding: 2 6 2 6; -fx-min-width: 28px; -fx-cursor: hand;";

    private final Button statusButton = iconButton("⏸"); // pause; becomes play (▶) when suspended
    private final Button resetPasswordButton = iconButton("🔑"); // key
    private final Button adminButton = iconButton("👑"); // crown
    private final Button deleteButton = iconButton("🗑"); // wastebasket
    private final HBox container = new HBox(4.0, statusButton, resetPasswordButton, adminButton,
        deleteButton);

    /**
     * Wires up each button to their respective handlers
     */
    ActionsCell() {
      statusButton.setOnAction(e -> suspendReactivate(getRowAccount()));
      resetPasswordButton.setOnAction(e -> resetPassword(getRowAccount()));
      adminButton.setOnAction(e -> grantRevokeAdmin(getRowAccount()));
      deleteButton.setOnAction(e -> delete(getRowAccount()));

      resetPasswordButton.setTooltip(new Tooltip("Reset Password"));
      deleteButton.setTooltip(new Tooltip("Delete"));
    }

    /**
     * Builds the icon buttons for the action column
     *
     * @param icon the glyph to display on the button
     * @return a small icon button
     */
    private Button iconButton(String icon) {
      Button button = new Button(icon);
      button.setStyle(ICON_STYLE);
      return button;
    }

    /**
     * Looks up the account for the given row
     *
     * @return the account for this row
     */
    private Account getRowAccount() {
      return getTableView().getItems().get(getIndex());
    }

    /**
     * Refreshes the row's buttons to match the current account state
     *
     * @param item the item for this cell
     * @param empty true if this cell no longer links to a row
     */
    @Override
    protected void updateItem(Void item, boolean empty) {
      super.updateItem(item, empty);
      if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
        setGraphic(null);
        return;
      }
      Account account = getRowAccount();

      boolean active = account.getIsActive();
      statusButton.setText(active ? "⏸" : "▶"); // inactive:active
      statusButton.setTooltip(new Tooltip(active ? "Suspend" : "Reactivate"));

      boolean admin = account.getIsAdmin();
      adminButton.setStyle(ICON_STYLE + (admin ? " -fx-background-color: #ffe28a;" : ""));
      adminButton.setTooltip(new Tooltip(admin ? "Revoke Admin" : "Grant Admin"));

      setGraphic(container);
    }
  }
}
