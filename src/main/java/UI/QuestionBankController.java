package UI;

import Data.Question;
import Data.QuestionRepository;
import javafx.collections.FXCollections;
import Service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

import java.util.List;

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
    private Label noMatchesLabel;

    private final QuestionRepository questionRepository = new QuestionRepository();


    /**
     * Initializes the Question bank view
     */
    @FXML
    @Override
    protected void initialize() {
        super.initialize();

        questionColumn.setCellValueFactory(
                new PropertyValueFactory<>("questionText"));

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category"));

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
     * Logs user out of the application and returns to login screen
     * @param event login screen
     */
    @FXML
    protected void onLogoutClick(MouseEvent event) {
        AuthService.getInstance().logout();
        swapScene(event, SceneType.LOGIN);
    }
}
