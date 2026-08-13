/**
 * Builds a Scene for a given SceneType
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

package UI;

import java.io.IOException;
import java.io.UncheckedIOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class SceneFactory {

  // prevent instantiation
  private SceneFactory() {
  }

  /**
   * Builds the Scene for the given SceneType
   *
   * @param type of scene to build
   * @return the Scene
   */
  public static Scene load(SceneType type) {
    FXMLLoader loader = new FXMLLoader(SceneFactory.class.getResource(type.getFxml()));
    try {
      Scene scene = new Scene(loader.load());
      scene.getStylesheets().add(SceneFactory.class.getResource("app.css").toExternalForm());
      return scene;
    } catch (IOException e) {
      // throw so that the caller knows there was a problem loading the fxml
      throw new UncheckedIOException("Error loading FXML for " + type + ": " + e.getMessage(), e);
    }
  }
}
