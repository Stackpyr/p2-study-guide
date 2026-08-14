package UI;

import Data.Question;
import Data.QuestionRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
/**
 * [Brief one-sentence description of what this class does.]
 *
 * @author Analiza Boehning
 * @version 0.1.1
 * @since 8/3/2026
 */
public class AddQuestionController extends BaseController {
    // Fixed category choices - can add more later but for now consolidating the choices to three
    private static final List<String> CATEGORIES = List.of(
            "Object-Oriented Programming",
            "Databases",
            "Software Engineering"
    );

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
        super.initialize();
        categoryChoiceBox.getItems().addAll(CATEGORIES);
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

        String category = categoryChoiceBox.getValue();

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

        Question newQuestion = new Question(questionTextArea.getText(), category);

        newQuestion.setChoiceA(choiceA.getText());
        newQuestion.setChoiceB(choiceB.getText());
        newQuestion.setChoiceC(choiceC.getText());
        newQuestion.setChoiceD(choiceD.getText());
        newQuestion.setCorrectAnswer(correctAnswer);

        Question savedQuestion = questionRepository.addQuestion(newQuestion);
        System.out.printf("Added question: %s%n", savedQuestion);

        // After saving, the scene should swap back to the Question bank scene.
        swapScene(event, SceneType.QUESTION_BANK);
    }

    @FXML
    protected void onCancelClick(ActionEvent event) {
        System.out.println("Cancelled by user");
        swapScene(event, SceneType.QUESTION_BANK);
    }

}
