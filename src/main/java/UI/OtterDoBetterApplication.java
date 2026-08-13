/**
 * This class is the main entry point of the application.
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OtterDoBetterApplication extends Application {

  // use a fixed window size for the whole application lifetime
  private static final double WINDOW_WIDTH = 900;
  private static final double WINDOW_HEIGHT = 600;

  @Override
  public void start(Stage stage) {
    Scene scene = SceneFactory.load(SceneType.LOGIN);
    // swapping out for a custom title bar
    stage.initStyle(StageStyle.UNDECORATED);
    stage.setTitle("Otter Do Better - Study Guide");
    stage.setWidth(WINDOW_WIDTH);
    stage.setHeight(WINDOW_HEIGHT);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
  }
}
