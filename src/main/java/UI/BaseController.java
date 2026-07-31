/**
 * [Explanation]
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */


package UI;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
  // tracks where inside the title bar the mouse was pressed, so dragging
  // moves the window by the same offset instead of snapping its corner
  // to the cursor
  private double dragOffsetX;
  private double dragOffsetY;

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
