module TicTacToe {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;

    opens application to javafx.fxml;
    opens controller to javafx.fxml, javafx.media;

    exports application;
    exports controller;
}
