/**
 * Basic tests to ensure proper functionality for the Question Bank UI
 *
 * @author Analiza Boehning
 * @version 0.1.0
 * @since 8/13/2026
 */

package UI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Data.DatabaseManager;
import Service.AuthService;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javax.swing.text.TabExpander;

public class QuestionBankControllerUITest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        DatabaseManager.close();
        AuthService.resetForTesting();

        stage.setScene(SceneFactory.load(SceneType.QUESTION_BANK));
        stage.show();
    }

    /**
     * Verify the Question Bank view loads with all the main elements
     */
    @Test
    void testQuestionBankScene() {
        TextField searchQuestionsField = lookup("#searchQuestionsField").queryAs(TextField.class);
        ComboBox<?> categoryChoiceBox = lookup("#categoryChoiceBox").queryAs(ComboBox.class);
        TableView<?> questionTable = lookup("#questionTable").queryAs(TableView.class);
        Label noMatchesLabel = lookup("#noMatchesLabel").queryAs(Label.class);

        assertNotNull(searchQuestionsField);
        assertNotNull(categoryChoiceBox);
        assertNotNull(questionTable);
        assertNotNull(noMatchesLabel);
    }

    /**
     * Verify category filter contains all the category options
     */
    @Test
    void categoryChoiceBoxTest() {
        ComboBox<?> categoryChoiceBox = lookup("#categoryChoiceBox").queryAs(ComboBox.class);

        assertNotNull(categoryChoiceBox);

        assertEquals("All Categories", categoryChoiceBox.getItems().get(0));
        assertTrue(categoryChoiceBox.getItems().contains("Object-Oriented Programming"));
        assertTrue(categoryChoiceBox.getItems().contains("Databases"));
        assertTrue(categoryChoiceBox.getItems().contains("Software Engineering"));

    }

    /**
     * Verify the table contains columns
     */
    @Test
    void questionTableTest() {
        TableView<?> questionTable = lookup("#questionTable").queryAs(TableView.class);
        assertNotNull(questionTable);
        assertEquals(2, questionTable.getColumns().size());

        TableColumn<?, ?> questionColumn = questionTable.getColumns().get(0);

        TableColumn<?, ?> categoryColumn = questionTable.getColumns().get(1);

        assertEquals("Question", questionColumn.getText());
        assertEquals("Category", categoryColumn.getText());
    }

    /**
     * Verify "No Matches Found" is hidden
     */
    @Test
    void noMatchesLabelTest() {
        Label noMatchesLabel = lookup("#noMatchesLabel").queryAs(Label.class);

        assertNotNull(noMatchesLabel);
        assertTrue(noMatchesLabel.isVisible());
    }
}
