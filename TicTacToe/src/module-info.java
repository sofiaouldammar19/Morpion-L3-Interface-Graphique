module TicTacToe {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;

    opens application to javafx.fxml;

    exports application;
    exports application.controller;
    opens application.controller to javafx.fxml;
}
