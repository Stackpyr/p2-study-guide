/**
 * Placeholder controller for the post-login landing scene. Doesn't do much yet. It's here so
 * onLoginClick can send the user somewhere and everyone else can build their scenes.
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

package UI;

import Service.AuthService;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class DashboardController extends BaseController {

  @FXML
  protected void onLogoutClick(MouseEvent event) throws IOException {
    AuthService.getInstance().logout();
    swapScene(event, "login-view.fxml");
  }

  @FXML
  protected void onQuizClick(MouseEvent event) {
    swapScene(event, "quiz-view.fxml");
  }

}
