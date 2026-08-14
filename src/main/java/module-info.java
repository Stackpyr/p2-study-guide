module org.example.otterdobetter {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires java.desktop;
  requires jdk.httpserver;
  requires static org.junit.jupiter.api;

  requires scribejava.core;
  requires scribejava.apis;

  opens UI to javafx.fxml, org.junit.platform.commons;
  opens Data to org.junit.platform.commons, javafx.base;
  opens Service to org.junit.platform.commons;

  exports UI;
}