/**
 * [Explanation]
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */


package UI;

import Service.AuthService;
import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * This base controller class provides common functionality for all controllers, including the
 * management of the custom title bar (since I don't care for the standard Windows one - don't know about Mac)
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

public class BaseController {
  // tracks where inside the title bar the mouse was pressed
  private double dragOffsetX;
  private double dragOffsetY;

  // prints the status of the session (user logged in or not) to the title bar
  @FXML
  protected Label sessionLabel;

  /**
   * FXMLLoader calls this automatically after injecting @FXML fields, since it matches this
   * exact no-arg signature - no need to implement Initializable for this to run.
   */
  @FXML
  protected void initialize() {
    refreshSessionLabel();
  }

  /**
   * Updates the "who's logged in" label in the title bar to match AuthService's current state.
   * Call this again after anything that changes who's logged in.
   */
  protected void refreshSessionLabel() {
    if (sessionLabel == null) {
      return; // no session, so nothing to do
    }
    if (AuthService.getInstance().isLoggedIn()) {
      sessionLabel.setText("Logged in as " + AuthService.getInstance().getCurrentAccount().getDisplayName());
    } else {
      sessionLabel.setText("Not logged in");
    }
  }

  /**
   * Loads another FXML scene onto the current stage and returns its controller, so the caller
   * can pass data into the new scene (e.g. a status message) before the user sees it.
   */
  protected <T> T swapScene(Event event, String fxml) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
    Scene scene = null;
    try {
      scene = new Scene(loader.load());
    } catch (IOException e) {
      System.out.println("Error loading FXML: " + e.getMessage());
    }
    stageOf(event).setScene(scene);
    return loader.getController();
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

  protected Stage stageOf(Event event) {
    return (Stage) ((Node) event.getSource()).getScene().getWindow();
  }
}
