/**
 * 
 */
package model;

import ai.Coup;
import ai.MultiLayerPerceptron;

public class Game {
    private Coup[] board;
    private int currentPlayer;
    private boolean gameEnded = false;
    private MultiLayerPerceptron net; // Assuming this is your AI model class

    public Game(MultiLayerPerceptron net) {
        this.net = net;
        this.board = new Coup[9]; // for a 3x3 Tic Tac Toe board
        this.currentPlayer = Coup.X; // X starts the game
        for (int i = 0; i < board.length; i++) {
            board[i] = new Coup(9, ""); // Initialize the board with empty coups
            board[i].in[i] = Coup.EMPTY; // Mark all positions as empty
        }
    }

    public void play() {
        while (!gameEnded) {
            if (isAITurn()) {
                int[] move = predictMove(); // Assume this method predicts the move based on the AI model
                makeMove(move[0], currentPlayer);
            } else {
                // Handle human player move, potentially through UI interaction
            }
            checkGameEnd();
            currentPlayer = currentPlayer == Coup.X ? Coup.O : Coup.X; // Switch turns
        }
    }

    private boolean isAITurn() {
        // Implement logic to determine if it's AI's turn
        // This could be based on the currentPlayer variable
        return true; // Placeholder
    }

    private int[] predictMove() {
        // Convert your board state to the format expected by your AI model
        // Call the model's prediction method
        // Convert the model's output back to a board index and return it
        return new int[]{0}; // Placeholder
    }

    private void makeMove(int position, int player) {
        // Update the board state with the player's move
        if (position >= 0 && position < board.length && board[position].cellAvailable(position)) {
            board[position].in[position] = player;
        }
    }

    private void checkGameEnd() {
        // Check for win or draw conditions
        // Update gameEnded accordingly
    }
}