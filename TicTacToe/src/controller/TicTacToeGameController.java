package controller;

import java.io.IOException;
import java.util.List;

import javafx.collections.ObservableList;
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

public class TicTacToeGameController {

    @FXML
    private GridPane gameBoard;

    private TicTacToeGame game;

    public TicTacToeGameController() {
        game = new TicTacToeGame();
    }

    @FXML
    private void initialize() {
        updateBoard();
    }

    private void updateBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = (Button) gameBoard.getChildren().get(i * 3 + j);
                button.setText(game.getMark(i, j));
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
    private void handleButtonClick(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        Integer row = GridPane.getRowIndex(clickedButton);
        Integer col = GridPane.getColumnIndex(clickedButton);
        if (row == null) row = 0;
        if (col == null) col = 0;

        if (game.playMove(row, col)) {
            clickedButton.setText(game.getMark(row, col));
            if (game.checkForWin()) {
                System.out.println("Player " + (game.isXTurn() ? "O" : "X") + " wins!");
                highlightWin();
            } else if (game.isBoardFull()) {
                System.out.println("The game is a tie!");
            }
        }
    }

    private void highlightWin() {
        List<int[]> winningLine = game.getWinningLine();
        if (winningLine != null) {
            for (int[] cell : winningLine) {
                int row = cell[0];
                int col = cell[1];
                Button button = (Button) getNodeByRowColumnIndex(row, col, gameBoard);
                if (button != null) {
                    button.setStyle("-fx-background-color: #00ff00;"); // Set the button background to green
                }
            }
        }
    }

    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

}
