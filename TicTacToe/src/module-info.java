module TicTacToe {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;

    opens application to javafx.fxml;

    exports application;
    exports controller;
    opens controller to javafx.fxml;
}
