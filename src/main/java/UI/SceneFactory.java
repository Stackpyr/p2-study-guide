/**
 * Builds a Scene for a given SceneType
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

package UI;

import java.io.IOException;
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
      return new Scene(loader.load());
    } catch (IOException e) {System.out.println("Error loading FXML: " + e.getMessage());}
    return null;
  }

  /**
   * Builds the Results scene and supplies the final score.
   */
  public static Scene loadResults(
          int score,
          int totalQuestions) {

    FXMLLoader loader =
            new FXMLLoader(SceneFactory.class.getResource(SceneType.RESULT.getFxml()));

    try {
      Scene scene = new Scene(loader.load());

      ResultController controller =
              loader.getController();

      controller.setResults(score, totalQuestions);

      return scene;
    } catch (IOException exception) {
      System.out.println("Error loading Results FXML: " + exception.getMessage());
    }

    return null;
  }

}
