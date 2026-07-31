module org.example.otterdobetter {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires java.desktop;
  requires static org.junit.jupiter.api;

  opens UI to javafx.fxml;
  opens Data to org.junit.platform.commons;
  opens Service to org.junit.platform.commons;
  exports UI;
}