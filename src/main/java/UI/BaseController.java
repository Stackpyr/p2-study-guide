/**
 * This base controller class provides common functionality for all controllers, including the
 * management of the custom title bar (since I don't care for the standard Windows one - don't know about Mac)
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

package UI;

import Service.AuthService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
   * Loads the given scene onto the current stage.
   * @param event the event that triggered this
   * @param type the type of scene to load
   */
  protected void swapScene(Event event, SceneType type) {
    Scene scene = SceneFactory.load(type);
    stageOf(event).setScene(scene);
  }

  /**
   * Handles the mouse press event on the title bar to initiate dragging of the window.
   * @param event the mouse press event
   */
  @FXML
  protected void onTitleBarPressed(MouseEvent event) {
    dragOffsetX = event.getSceneX();
    dragOffsetY = event.getSceneY();
  }

  /**
   * Handles the mouse drag event on the title bar to move the window.
   * @param event the mouse drag event
   */
  @FXML
  protected void onTitleBarDragged(MouseEvent event) {
    Stage stage = stageOf(event);
    stage.setX(event.getScreenX() - dragOffsetX);
    stage.setY(event.getScreenY() - dragOffsetY);
  }

  /**
   * Handles the minimize button click event to minimize the window.
   * @param event the button click event
   */
  @FXML
  protected void onMinimizeClick(Event event) {
    stageOf(event).setIconified(true);
  }

  /**
   * Handles the close button click event to close the window.
   * @param event the button click event
   */
  @FXML
  protected void onCloseClick(Event event) {
    stageOf(event).close();
  }

  /**
   * Retrieves the Stage (window) associated with the given event.
   * @param event the event from which to retrieve the stage
   * @return the Stage associated with the event
   */
  protected Stage stageOf(Event event) {
    return (Stage) ((Node) event.getSource()).getScene().getWindow();
  }
}
