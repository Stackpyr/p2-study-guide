/**
 * Provides a list of supported scenes and associated fxml files. Then, SceneFactory can build the
 * scenes based on this enum.
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

package UI;

public enum SceneType {
  LOGIN("login-view.fxml"),
  REGISTER("register-view.fxml"),
  DASHBOARD("dashboard-view.fxml"),
  QUIZ("quiz-view.fxml"),
  RESULT("result-view.fxml");
  ACCOUNT_ADMIN("account-admin-view.fxml");

  private final String fxml;

  SceneType(String fxml) {
    this.fxml = fxml;
  }

  String getFxml() {
    return fxml;
  }
}
