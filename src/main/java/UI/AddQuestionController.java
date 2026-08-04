package UI;

import Data.Question;
import Data.QuestionRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * [Brief one-sentence description of what this class does.]
 *
 * @author Analiza Boehning
 * @version 0.1.0
 * @since 8/3/2026
 */
public class AddQuestionController {
    // Question text area
    @FXML
    private TextArea questionTextArea;

    // All choice text fields
    @FXML
    private TextField choiceA;
    @FXML
    private TextField choiceB;
    @FXML
    private TextField choiceC;
    @FXML
    private TextField choiceD;

    // Category Dropdown
    @FXML
    private ComboBox<String> categoryChoiceBox;

    // Radio buttons for choosing  correct answer
    @FXML
    private RadioButton radioA;
    @FXML
    private RadioButton radioB;
    @FXML
    private RadioButton radioC;
    @FXML
    private RadioButton radioD;
    @FXML
    private ToggleGroup correctAnswer;

    @FXML
    public void initialize() {

    }

    @FXML
    protected void onSaveClick(ActionEvent event) {
        // Check if fields are blank
        if (questionTextArea.getText().isBlank()) {
            System.out.println("Missing required fields");
            return;
        }

        // Check if choices are blank
        if (choiceA.getText().isBlank()
                || choiceB.getText().isBlank()
                || choiceC.getText().isBlank()
                || choiceD.getText().isBlank()) {
            System.out.println("Missing required fields");
            return;
        }

        // Check if a correct answer has been selected
        if (correctAnswer.getSelectedToggle() == null) {
            System.out.println("Please select a correct answer");
            return;
        }

        // Check if a category has been selected
        if (categoryChoiceBox.getValue() == null) {
            System.out.println("Please select category");
            return;
        }

        String A = choiceA.getText();
        String B = choiceB.getText();
        String C = choiceC.getText();
        String D = choiceD.getText();

        String correctAnswer = "";

        if (radioA.isSelected()) {
            correctAnswer = A;
        } else if (radioB.isSelected()) {
            correctAnswer = B;
        } else if (radioC.isSelected()) {
            correctAnswer = C;
        } else if (radioD.isSelected()) {
            correctAnswer = D;
        }


        QuestionRepository questionRepository = new QuestionRepository();

        Question newQuestion = new Question(
                // fix Question.java first.
        );




        questionRepository.addQuestion(newQuestion);
        System.out.printf("Added question: %s", newQuestion);
    }

    @FXML
    protected void onCancelClick(ActionEvent event) {
        System.out.println("Cancelled by user");
    }

    // TODO user can navigate back to the question scene.
}
