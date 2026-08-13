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
import javafx.scene.Scene;

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
    private QuizAttempt currentAttempt;

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
            questionTextLabel.setText("Add questions to the Question Bank first.");

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

        if (!startQuizAttempt()) {
            previousButton.setDisable(true);
            nextButton.setDisable(true);
            submitButton.setDisable(true);
            statusLabel.setText("Quiz attempt could not be started.");
            return;
        }

        showCurrentQuestion();
    }

    /**
     * Creates and reads the current quiz attempt.
     */
    private boolean startQuizAttempt() {
        if (!AuthService.getInstance().isLoggedIn()) {
            return false;
        }

        int accountId = AuthService.getInstance().getCurrentAccount().getAccountId();

        QuizAttempt newAttempt = new QuizAttempt(accountId, questions.size());

        QuizAttempt savedAttempt = quizAttemptRepository.addQuizAttempt(newAttempt);

        if (savedAttempt == null) {
            return false;
        }

        currentAttempt = quizAttemptRepository.getById(savedAttempt.getAttemptId());

        return currentAttempt != null;
    }

    /**
     * Displays the current question and answer choices.
     */
    private void showCurrentQuestion() {
        Question question = questions.get(currentQuestionIndex);

        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());

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
        nextButton.setDisable(currentQuestionIndex == questions.size() - 1);

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
                answerGroup.getSelectedToggle().getUserData().toString();

        selectedAnswers.put(currentQuestionIndex, selectedAnswer);
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
     * Updates the current attempt with its final score.
     */
    private QuizAttempt completeQuizAttempt(int score) {
        if (currentAttempt == null) {
            return null;
        }

        currentAttempt.setScore(score);
        currentAttempt.setCompletedAt(LocalDateTime.now().toString());

        return quizAttemptRepository.updateQuizAttempt(currentAttempt);
    }

    /**
     * Grades, saves, and displays the quiz results.
     */
    @FXML
    protected void onSubmitClick(ActionEvent event) {
        saveCurrentAnswer();

        if (!quizService.isComplete(questions.size(), selectedAnswers)) {

            Alert warningAlert =
                    new Alert(Alert.AlertType.WARNING);

            warningAlert.setTitle("Incomplete Quiz");
            warningAlert.setHeaderText("Please answer every question.");
            warningAlert.setContentText("Return to the quiz and finish " + "the unanswered questions.");

            warningAlert.showAndWait();
            statusLabel.setText("Quiz was not submitted.");
            return;
        }

        int score = quizService.calculateScore(questions, selectedAnswers);

        QuizAttempt completedAttempt = completeQuizAttempt(score);

        if (completedAttempt == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);

            errorAlert.setTitle("Save Error");
            errorAlert.setHeaderText("The quiz could not be saved.");
            errorAlert.setContentText("Please return to the dashboard and try again.");

            errorAlert.showAndWait();
            statusLabel.setText("Quiz was not saved.");
            return;
        }

        submitButton.setDisable(true);

        Scene resultScene = SceneFactory.loadResults(completedAttempt.getScore(), completedAttempt.getTotalQuestions());

        if (resultScene == null) {
            Alert errorAlert =
                    new Alert(Alert.AlertType.ERROR);

            errorAlert.setTitle("Scene Error");
            errorAlert.setHeaderText("The Results scene could not be opened.");
            errorAlert.setContentText("Your quiz was saved, but the Results scene could not be opened. Return to the Dashboard.");
            errorAlert.showAndWait();
            swapScene(event, SceneType.DASHBOARD);
            return;
        }

        stageOf(event).setScene(resultScene);
    }

    /**
     * Returns to the Dashboard.
     */
    @FXML
    protected void onBackClick(ActionEvent event) {
        if (currentAttempt != null) {
            boolean deleted = quizAttemptRepository.deleteQuizAttempt(currentAttempt.getAttemptId());

            if (!deleted) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);

                errorAlert.setTitle("Delete Error");
                errorAlert.setHeaderText("The unfinished quiz could not be removed.");
                errorAlert.setContentText("Please try again.");

                errorAlert.showAndWait();
                return;
            }

            currentAttempt = null;
        }


        swapScene(event, SceneType.DASHBOARD);
    }
}