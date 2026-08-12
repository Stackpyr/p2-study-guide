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

/**
 *  Controller for Question Bank that shows a table of all questions with search and filter options.
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
                "Object-Oriented Programming",
                "Databases",
                "Software Engineering"
        );

        categoryChoiceBox.setValue("All Categories");
        categoryChoiceBox.setOnAction(event -> onCategorySelected());
        searchQuestionsField.setOnAction(event -> onSearchQuestions());

        loadQuestions();
    }

    /*
    * Load the saved questions and populate the table
     */
    private void loadQuestions() {
        questionTable.setItems(
                FXCollections.observableArrayList(
                        questionRepository.getAllQuestions()
                )
        );
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
     * Filters the table based on a category selected or will show all questions if
     * equal to All Categories.
     */
    @FXML
    public void onCategorySelected() {
        String category = categoryChoiceBox.getValue();

        if (category.equals("All Categories")) {
            loadQuestions();
        } else {
            questionTable.setItems(
                    FXCollections.observableArrayList(
                            questionRepository.getQuestionsByCategory(category)
                    )
            );
        }
    }

    /**
     * Filters the table based on a keyword searched by the user
     */
    @FXML
    public void onSearchQuestions() {
        String keyword = searchQuestionsField.getText().trim();

        if (keyword.isEmpty()) {
            loadQuestions();
        } else {
            questionTable.setItems(
                    FXCollections.observableArrayList(
                            questionRepository.getQuestionsByKeyword(keyword)
                    )
            );
        }
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
