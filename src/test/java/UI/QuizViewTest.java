package UI;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuizViewTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        Scene quizScene = SceneFactory.load(SceneType.QUIZ);

        assertNotNull(quizScene);

        stage.setScene(quizScene);
        stage.show();
    }

    @Test
    void backButtonReturnsToDashboard() {
        clickOn("Back to Dashboard");

        assertTrue(lookup("Dashboard").tryQuery().isPresent());
    }
}