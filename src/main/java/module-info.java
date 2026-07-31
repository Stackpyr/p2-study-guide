module org.example.otterdobetter {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;

  opens org.example.otterdobetter to javafx.fxml;
  exports org.example.otterdobetter;
}