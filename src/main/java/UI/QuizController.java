package UI;

import Data.Question;
import Data.QuestionRepository;
import Service.QuizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls the Quiz scene.
 */
public class QuizController extends BaseController {

    @FXML
    private ToggleGroup answerGroup;

    @FXML
    private Label questionNumberLabel;

    @FXML
    private Label questionTextLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private RadioButton answerA;

    @FXML
    private RadioButton answerB;

    @FXML
    private RadioButton answerC;

    @FXML
    private RadioButton answerD;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button submitButton;

    private final QuestionRepository questionRepository =
            new QuestionRepository();

    private final QuizService quizService =
            new QuizService();

    private final Map<Integer, String> selectedAnswers =
            new HashMap<>();

    private List<Question> questions = List.of();
    private int currentQuestionIndex = 0;

    /**
     * Loads the quiz questions when the scene opens.
     */
    @Override
    @FXML
    protected void initialize() {
        super.initialize();

        questions = questionRepository.getAllQuestions();

        if (questions.isEmpty()) {
            questionNumberLabel.setText("No questions available");
            questionTextLabel.setText(
                    "Add questions to the Question Bank first."
            );

            answerA.setVisible(false);
            answerB.setVisible(false);
            answerC.setVisible(false);
            answerD.setVisible(false);

            previousButton.setDisable(true);
            nextButton.setDisable(true);
            submitButton.setDisable(true);

            statusLabel.setText("No quiz questions were found.");
            return;
        }

        showCurrentQuestion();
    }

    /**
     * Displays the current question and answer choices.
     */
    private void showCurrentQuestion() {
        Question question = questions.get(currentQuestionIndex);

        questionNumberLabel.setText(
                "Question " + (currentQuestionIndex + 1)
                        + " of " + questions.size()
        );

        questionTextLabel.setText(question.getQuestionText());

        answerA.setText(question.getChoiceA());
        answerB.setText(question.getChoiceB());
        answerC.setText(question.getChoiceC());
        answerD.setText(question.getChoiceD());

        answerA.setUserData(question.getChoiceA());
        answerB.setUserData(question.getChoiceB());
        answerC.setUserData(question.getChoiceC());
        answerD.setUserData(question.getChoiceD());

        restoreSavedAnswer();

        previousButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(
                currentQuestionIndex == questions.size() - 1
        );

        statusLabel.setText("Select one answer.");
    }

    /**
     * Saves the answer selected for the current question.
     */
    private void saveCurrentAnswer() {
        if (answerGroup.getSelectedToggle() == null) {
            return;
        }

        String selectedAnswer =
                answerGroup.getSelectedToggle()
                        .getUserData()
                        .toString();

        selectedAnswers.put(
                currentQuestionIndex,
                selectedAnswer
        );
    }

    /**
     * Restores an answer when returning to a question.
     */
    private void restoreSavedAnswer() {
        String savedAnswer =
                selectedAnswers.get(currentQuestionIndex);

        answerGroup.selectToggle(null);

        if (savedAnswer == null) {
            return;
        }

        RadioButton[] answerButtons = {
                answerA,
                answerB,
                answerC,
                answerD
        };

        for (RadioButton answerButton : answerButtons) {
            if (savedAnswer.equals(answerButton.getUserData())) {
                answerGroup.selectToggle(answerButton);
                return;
            }
        }
    }

    /**
     * Handles the Previous button.
     */
    @FXML
    protected void onPreviousClick() {
        saveCurrentAnswer();

        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showCurrentQuestion();
        }
    }

    /**
     * Handles the Next button.
     */
    @FXML
    protected void onNextClick() {
        saveCurrentAnswer();

        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            showCurrentQuestion();
        }
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

    /**
     * Returns to the Dashboard.
     */
    @FXML
    protected void onBackClick(ActionEvent event) {
        swapScene(event, SceneType.DASHBOARD);
    }
}