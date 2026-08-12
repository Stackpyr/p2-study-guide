/**
 * Builds a Scene for a given SceneType
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

package UI;

import java.io.IOException;
import java.util.function.Consumer;
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
    return load(type, null);
  }

  /**
   * Builds a Scene and allows its controller to be set up
   *
   * @param type type of scene to build
   * @param onLoad setup to run after the scene loads
   * @return the Scene
   */
  private static <T> Scene load(
          SceneType type,
          Consumer<T> onLoad) {

    FXMLLoader loader =
            new FXMLLoader(SceneFactory.class.getResource(type.getFxml()));

    try {
      Scene scene = new Scene(loader.load());

      if (onLoad != null) {
        T controller = loader.getController();
        onLoad.accept(controller);
      }

      return scene;
    } catch (IOException e) {
      System.out.println("Error loading FXML: " + e.getMessage());
    }

    return null;
  }

  /**
   * Builds the Results scene and supplies the final score.
   */
  public static Scene loadResults(
          int score,
          int totalQuestions) {

    return load(
            SceneType.RESULT,
            (ResultController controller) ->
                    controller.setResults(score, totalQuestions)
    );
  }

}