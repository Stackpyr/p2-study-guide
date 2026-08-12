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

    @FXML
    public void onAddQuestionClicked(ActionEvent event) {
       swapScene(event, SceneType.ADD_QUESTION);
    }

    @FXML
    protected void onLogoutClick(MouseEvent event) {
        AuthService.getInstance().logout();
        swapScene(event, SceneType.LOGIN);
    }
}
