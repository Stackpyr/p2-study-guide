package UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * Controls the Quiz Results scene.
 */
public class ResultController extends BaseController {

    @FXML
    private Label scoreLabel;

    @FXML
    private Label percentageLabel;

    @FXML
    private Label messageLabel;

    /**
     * Displays the final quiz results.
     */
    public void setResults(int score, int totalQuestions) {
        double percentage = 0.0;

        if (totalQuestions > 0) {
            percentage =
                    score * 100.0 / totalQuestions;
        }

        scoreLabel.setText("Score: " + score + " out of " + totalQuestions);

        percentageLabel.setText("Percentage: " + String.format("%.1f", percentage) + "%");

        if (score == totalQuestions && totalQuestions > 0) {

            messageLabel.setText("Perfect score! Great job!");

            Alert celebrationAlert =
                    new Alert(Alert.AlertType.INFORMATION);

            celebrationAlert.setTitle("Perfect Score!");
            celebrationAlert.setHeaderText("Congratulations!");
            celebrationAlert.setContentText("You answered every question correctly.");

            celebrationAlert.showAndWait();
        } else {
            messageLabel.setText("Quiz complete. Keep studying!");
        }
    }

    /**
     * Returns to the Dashboard.
     */
    @FXML
    protected void onBackClick(ActionEvent event) {
        swapScene(event, SceneType.DASHBOARD);
    }
}