package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class OtterDoBetterApplication extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(OtterDoBetterApplication.class.getResource("add-question-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    // swapping out for a custom title bar
    stage.initStyle(StageStyle.UNDECORATED);
    stage.setTitle("Otter Do Better - Study Guide");
    stage.setScene(scene);
    stage.show();
  }
}
