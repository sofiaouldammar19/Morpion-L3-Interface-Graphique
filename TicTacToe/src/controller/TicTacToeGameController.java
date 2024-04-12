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
import tools.MultiLayerPerceptron;
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
    private boolean withAI;
    private MultiLayerPerceptron aiModel;
    
    @FXML
    private void initialize() {
        updateBoard();
    }

    public TicTacToeGameController() {
        game = new TicTacToeGame();
        xImage = new Image(TicTacToeGameController.class.getResourceAsStream("/images/x.png"), 50, 50, false, false);
        oImage = new Image(TicTacToeGameController.class.getResourceAsStream("/images/o.png"), 50, 50, false, false);
    }
    
    public void setWithAI(boolean withAI, String modelFilePath) {
        this.withAI = withAI;
        System.out.print(withAI);
        if (withAI) {
            System.out.println("Game mode set to: Human vs AI");
            // Load the AI model
            aiModel = MultiLayerPerceptron.load(modelFilePath);
            if (aiModel == null) {
                System.err.println("Failed to load the AI model.");
                // Optionally revert to human vs. human mode or handle error
            }
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
        row = (row == null) ? 0 : row;
        col = (col == null) ? 0 : col;

        if (withAI) {
            playWithAI(row, col);
        } else {
            playWithHuman(row, col);
        }
    }
    
    private void playWithHuman(int row, int col) {
        if (game.playMove(row, col)) {
            updateBoard();
            checkGameState();
        }
    }
    
    private void playWithAI(int row, int col) {
        if (game.playMove(row, col)) {
            updateBoard();
            checkGameState();
            if (!game.isBoardFull() && !game.checkForWin()) {
                aiMove(); // Let AI make a move
                updateBoard();
                checkGameState();
            }
        }
    }
    
    private void aiMove() {
        double[] input = boardToInputArray(); // Convert current board to input for the AI
        double[] output = aiModel.forwardPropagation(input);
        int moveIndex = getBestMoveFromOutput(output); // Determine best move from AI output
        if (moveIndex == -1) {
            // Handle case where no valid move is found (should be rare)
            return;
        }
        int aiRow = moveIndex / 3;
        int aiCol = moveIndex % 3;
        if (game.playMove(aiRow, aiCol)) {
            updateBoard();
            checkGameState();
        }
    }
    
    private double[] boardToInputArray() {
        double[] input = new double[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String mark = game.getMark(i, j);
                if (mark.equals("X")) {
                    input[i * 3 + j] = 1.0;
                } else if (mark.equals("O")) {
                    input[i * 3 + j] = -1.0;
                } else {
                    input[i * 3 + j] = 0.0;
                }
            }
        }
        return input;
    }

    
    private int getBestMoveFromOutput(double[] output) {
        int bestMoveIndex = -1;
        double maxScore = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < output.length; i++) {
            // Ensure the position is available by checking the board
            int row = i / 3;
            int col = i % 3;
            if (game.getMark(row, col).equals(" ") && output[i] > maxScore) {
                maxScore = output[i];
                bestMoveIndex = i;
            }
        }
        return bestMoveIndex;
    }



    private void checkGameState() {
        if (game.checkForWin()) {
            highlightWinningLine(game.getWinningLine());
            System.out.println("Game Over: Player " + (game.isXTurn() ? "O" : "X") + " wins!");
        } else if (game.isBoardFull()) {
            System.out.println("Game Over: It's a tie!");
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
