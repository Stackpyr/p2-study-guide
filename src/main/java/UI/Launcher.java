/**
 * This class is the entry point for the OtterDoBetter application.
 * It launches the JavaFX application by calling the Application.launch() method with the
 * OtterDoBetterApplication class as an argument.
 *
 * @author: Jason Hamilton
 * @created: 7/31/2026
 * @since: 0.1.0
 */

package UI;

import javafx.application.Application;

public class Launcher {

  /**
   * The main method is the entry point of the application.
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Application.launch(OtterDoBetterApplication.class, args);
  }
}
