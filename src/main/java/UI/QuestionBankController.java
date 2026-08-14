package UI;

import Data.Question;
import Data.QuestionRepository;
import javafx.collections.FXCollections;
import Service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Optional;

/**
 *  Controller for Question Bank that shows a table of all questions with search and filter options based on user selection/input.
 *
 * @author Analiza Boehning
 * @version 0.1.1
 * @since 8/4/2026
 */

public class QuestionBankController extends BaseController {

    @FXML
    private TextField searchQuestionsField;

    @FXML
    private ComboBox<String> categoryChoiceBox;

    @FXML
    private TableView<Question> questionTable;

    @FXML
    private TableColumn<Question, String> questionColumn;

    @FXML
    private TableColumn<Question, String> categoryColumn;

    @FXML
    private TableColumn<Question, Void> actionsColumn;

    @FXML
    private Label noMatchesLabel;

    @FXML
    private Label statusLabel;

    private final QuestionRepository questionRepository = new QuestionRepository();


    /**
     * Initializes the Question bank view
     */
    @FXML
    @Override
    protected void initialize() {
        super.initialize();

        // stretch columns to always exactly fill the table's width, instead of the default
        // policy which can leave a gap or let columns run past the visible area
        questionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        questionColumn.setCellValueFactory(
                new PropertyValueFactory<>("questionText"));

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category"));

        actionsColumn.setCellFactory(col -> new ActionsCell());

        categoryChoiceBox.getItems().addAll(
                "All Categories",
                "Object-Oriented Programming",
                "Databases",
                "Software Engineering"
        );

        categoryChoiceBox.setValue("All Categories");
        categoryChoiceBox.setOnAction(event -> filterQuestions());
        searchQuestionsField.setOnAction(event -> filterQuestions());

        loadQuestions();
    }

    /*
    * Load the saved questions and populate the table
     */
    private void loadQuestions() {
        List<Question> questions = questionRepository.getAllQuestions();

        questionTable.setItems(
                FXCollections.observableArrayList(
                        questionRepository.getAllQuestions()
                )
        );
        noMatchesMessageHandler(questions);
    }

    /**
     * swaps to the Add Question scene so the user can add a question
     * @param event add question scene
     */
    @FXML
    public void onAddQuestionClicked(ActionEvent event) {
       swapScene(event, SceneType.ADD_QUESTION);
    }

    /**
     * Update the table based on the keyword and category selected
     */
    private void filterQuestions() {
        String keyword = searchQuestionsField.getText().trim();
        String category = categoryChoiceBox.getValue();

        // Check for if a keyword is entered
        boolean keywordEntered = !keyword.isEmpty();

        // Check for if a category is selected excluding all categories
        boolean categorySelected = category != null && !category.equals("All Categories");

        if (keywordEntered && categorySelected) { // if both filters are being used
          List<Question> questions = questionRepository.getQuestionsByCategoryAndKeyword(keyword, category);

          questionTable.setItems(FXCollections.observableArrayList(questions));

            noMatchesMessageHandler(questions);

        } else if (keywordEntered) { // if only a keyword was entered
            List<Question> questions = questionRepository.getQuestionsByKeyword(keyword);

            questionTable.setItems(FXCollections.observableArrayList(questions));

            noMatchesMessageHandler(questions);
        } else if (categorySelected) { // if only the category is selected
            List<Question> questions = questionRepository.getQuestionsByCategory(category);

            questionTable.setItems(FXCollections.observableArrayList(questions));

            noMatchesMessageHandler(questions);
        } else {
            loadQuestions();
//            noMatchesMessageHandler(questionRepository.getAllQuestions());
        }
    }

    /**
     * Display or hide the 'No Matches" message based on whether the filtered question list is empty
     * @param questions questions currently displayed in the table
     */
    private void noMatchesMessageHandler(List<Question> questions) {
        boolean noMatchesFound = questions.isEmpty();

        noMatchesLabel.setVisible(noMatchesFound);
    }

    /**
     * Confirms with the user, then deletes the question from the database and removes it from
     * the table without needing a full reload.
     * @param question the question whose delete button was clicked
     */
    private void delete(Question question) {
        Alert confirm = new Alert(AlertType.CONFIRMATION,
                "Delete this question? This can't be undone.", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.YES) {
            return;
        }

        boolean deleted = questionRepository.deleteQuestion(question.getQuestionId());
        if (!deleted) {
            setStatus("Couldn't delete that question. Please try again.", true);
            return;
        }

        questionTable.getItems().remove(question);
        noMatchesMessageHandler(questionTable.getItems());
        setStatus("Question has been deleted.", false);
    }

    /**
     * Updates the status message shown below the search/filter row.
     * @param message the message to display
     * @param isError true to style the message as an error
     */
    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: #b40b0b;" : "-fx-text-fill: #0b7a1f;");
    }

    /**
     * Logs user out of the application and returns to login screen
     * @param event login screen
     */
    @FXML
    protected void onLogoutClick(MouseEvent event) {
        AuthService.getInstance().logout();
        swapScene(event, SceneType.LOGIN);
    }

    /**
     * Returns to the Dashboard scene without logging out
     * @param event dashboard scene
     */
    @FXML
    protected void onBackClick(ActionEvent event) {
        swapScene(event, SceneType.DASHBOARD);
    }

    /**
     * Draws the Delete button for each row in the Actions column
     */
    private class ActionsCell extends TableCell<Question, Void> {
        private final Button deleteButton = new Button("🗑"); // wastebasket

        ActionsCell() {
            deleteButton.setStyle(
                    "-fx-font-size: 13px; -fx-padding: 2 6 2 6; -fx-min-width: 28px; -fx-cursor: hand;");
            deleteButton.setTooltip(new Tooltip("Delete"));
            deleteButton.setOnAction(e -> delete(getRowQuestion()));
        }

        /**
         * Looks up the question for the given row
         * @return the question for this row
         */
        private Question getRowQuestion() {
            return getTableView().getItems().get(getIndex());
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : deleteButton);
        }
    }
}
