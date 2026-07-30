module org.example.otterdobetter {
  requires javafx.controls;
  requires javafx.fxml;

  opens org.example.otterdobetter to javafx.fxml;
  exports org.example.otterdobetter;
}