package UI;

import Data.Question;
import Data.QuestionRepository;
import Data.QuizAttempt;
import Data.QuizAttemptRepository;
import Service.AuthService;
import Service.QuizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.time.LocalDateTime;
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

    private final QuizAttemptRepository quizAttemptRepository =
            new QuizAttemptRepository();

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
     * Saves the completed quiz attempt.
     */
    private boolean saveQuizAttempt(int score) {
        if (!AuthService.getInstance().isLoggedIn()) {
            return false;
        }

        int accountId =
                AuthService.getInstance()
                        .getCurrentAccount()
                        .getAccountId();

        QuizAttempt attempt =
                new QuizAttempt(accountId, questions.size());

        attempt.setScore(score);
        attempt.setCompletedAt(
                LocalDateTime.now().toString()
        );

        return quizAttemptRepository.addQuizAttempt(attempt)
                != null;
    }

    /**
     * Grades the quiz when it is submitted.
     */
    @FXML
    protected void onSubmitClick() {
        saveCurrentAnswer();

        if (!quizService.isComplete(questions.size(), selectedAnswers)) {

            Alert warningAlert = new Alert(Alert.AlertType.WARNING);

            warningAlert.setTitle("Incomplete Quiz");
            warningAlert.setHeaderText("Please answer every question.");
            warningAlert.setContentText("Return to the quiz and finish " + "the unanswered questions.");

            warningAlert.showAndWait();
            statusLabel.setText("Quiz was not submitted.");
            return;
        }

        int score = quizService.calculateScore(
                questions,
                selectedAnswers
        );

        double percentage =
                quizService.calculatePercentage(
                        score,
                        questions.size()
                );

        if (!saveQuizAttempt(score)) {
            Alert errorAlert =
                    new Alert(Alert.AlertType.ERROR);

            errorAlert.setTitle("Save Error");
            errorAlert.setHeaderText("The quiz could not be saved."
            );
            errorAlert.setContentText("Please return to the dashboard " + "and try again."
            );

            errorAlert.showAndWait();
            statusLabel.setText("Quiz was not saved.");
            return;
        }

        boolean perfectScore =
                score == questions.size();

        Alert resultAlert =
                new Alert(Alert.AlertType.INFORMATION);

        if (perfectScore) {
            resultAlert.setTitle("Perfect Score!");
            resultAlert.setHeaderText("Congratulations!");
        } else {
            resultAlert.setTitle("Quiz Results");
            resultAlert.setHeaderText("Quiz complete.");
        }

        resultAlert.setContentText("Score: " + score + " out of " + questions.size() + "\nPercentage: " + String.format("%.1f", percentage) + "%"
        );

        resultAlert.showAndWait();

        statusLabel.setText("Quiz submitted.");
        submitButton.setDisable(true);
    }

    /**
     * Returns to the Dashboard.
     */
    @FXML
    protected void onBackClick(ActionEvent event) {
        swapScene(event, SceneType.DASHBOARD);
    }
}