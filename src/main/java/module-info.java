module org.example.otterdobetter {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires java.desktop;
  requires static org.junit.jupiter.api;

  opens org.example.otterdobetter to javafx.fxml;
  opens Data to org.junit.platform.commons;
  exports org.example.otterdobetter;
  exports UI;
  opens UI to javafx.fxml;
}