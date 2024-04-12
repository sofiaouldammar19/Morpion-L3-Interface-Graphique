package controller;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.TicTacToeGame;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TicTacToeGameController {

    @FXML
    private GridPane gameBoard;

    private TicTacToeGame game;
    private Image xImage;
    private Image oImage;
    private boolean playingAgainstAI;


    public TicTacToeGameController() {
        game = new TicTacToeGame();
        xImage = new Image(TicTacToeGameController.class.getResourceAsStream("/images/x.png"), 50, 50, false, false);
        oImage = new Image(TicTacToeGameController.class.getResourceAsStream("/images/o.png"), 50, 50, false, false);
    }

    @FXML
    private void initialize() {
        updateBoard();
    }
    
    public void setPlayingAgainstAI(boolean playingAgainstAI) {
        this.playingAgainstAI = playingAgainstAI;
        if (playingAgainstAI) {
            System.out.println("Game mode set to: Human vs AI");
        } else {
            System.out.println("Game mode set to: Human vs Human");
        }
    }

    private void updateBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = (Button) gameBoard.getChildren().get(i * 3 + j);
                String mark = game.getMark(i, j);
                if ("X".equals(mark)) {
                    button.setGraphic(new ImageView(xImage));
                } else if ("O".equals(mark)) {
                    button.setGraphic(new ImageView(oImage));
                } else {
                    button.setGraphic(null); // Ensure it's null for spaces
                }
                button.setText(""); // Clear any text
            }
        }
    }


    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        Parent ret = FXMLLoader.load(getClass().getResource("/view/View.fxml"));
        Scene nvScene = new Scene(ret, 900, 700);
        Stage stageActuel = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActuel.setScene(nvScene);
        stageActuel.show();
    }

    @FXML
    private void handleButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        Integer row = GridPane.getRowIndex(clickedButton);
        Integer col = GridPane.getColumnIndex(clickedButton);
        if (row == null) row = 0;
        if (col == null) col = 0;

        if (game.playMove(row, col)) {
            String mark = game.getMark(row, col);
            if (!mark.equals("")) {
                clickedButton.setGraphic(new ImageView(mark.equals("X") ? xImage : oImage));
                clickedButton.setText(""); // Remove text
            }

            // Check for win or tie
            if (game.checkForWin()) {
                System.out.println("Player " + (game.isXTurn() ? "O" : "X") + " wins!");
                highlightWinningLine(game.getWinningLine());
            } else if (game.isBoardFull()) {
                System.out.println("The game is a tie!");
            }
        }
    }
    
    private void highlightWinningLine(List<int[]> winningLine) {
        if (winningLine != null) {
            // Disable all buttons to prevent further interaction
            gameBoard.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    node.setDisable(true);
                }
            });

            // Apply the fade transition to highlight the winning line
            for (int[] cell : winningLine) {
                int row = cell[0];
                int col = cell[1];
                Button button = (Button) gameBoard.getChildren().get(row * 3 + col);
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), button);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.1);
                fadeTransition.setCycleCount(4);
                fadeTransition.setAutoReverse(true);
                fadeTransition.play();
            }
        }
    }
}
