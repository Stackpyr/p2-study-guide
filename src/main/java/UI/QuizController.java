package UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;


/**
 * Controls the basic Quiz scene.
 */
public class QuizController extends BaseController {

    @FXML
    private ToggleGroup answerGroup;

    @FXML
    private Label statusLabel;

    /**
     * Handles the Previous button.
     */
    @FXML
    protected void onPreviousClick() {
        statusLabel.setText("This is the first question.");
    }


    /**
     * Handles the Next button.
     */
    @FXML
    protected void onNextClick() {
        statusLabel.setText("This is the last question.");
    }


    /**
     * Handles the Submit Quiz button.
     */
    @FXML
    protected void onSubmitClick() {
        if (answerGroup.getSelectedToggle() == null) {
            statusLabel.setText("Please select an answer.");
        } else {
            statusLabel.setText("Your answer was submitted.");
        }
    }


    /** Returns to the Dashboard. */
    @FXML
    protected void onBackClick(ActionEvent event) {
        swapScene(event, SceneType.DASHBOARD);
    }
}