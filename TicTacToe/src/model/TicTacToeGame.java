package model;

import java.util.Arrays;
import java.util.List;

public class TicTacToeGame {
    private final String[][] board = new String[3][3];
    private boolean isXTurn = true;

    public TicTacToeGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = " ";
            }
        }
    }

    public boolean isXTurn() {
        return isXTurn;
    }

    public String getMark(int row, int col) {
        return board[row][col];
    }

    public boolean playMove(int row, int col) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col].equals(" ")) {
            board[row][col] = isXTurn ? "X" : "O";
            isXTurn = !isXTurn;
            return true;
        }
        return false;
    }

    public boolean checkForWin() {
        // Check rows, columns, and diagonals for a win
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].equals(" ") && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return true;
            }
            if (!board[0][i].equals(" ") && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return true;
            }
        }
        if (!board[1][1].equals(" ") &&
            ((board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) ||
             (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])))) {
            return true;
        }
        return false;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public List<int[]> getWinningLine() {
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (!board[i][0].equals(" ") && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return Arrays.asList(new int[]{i, 0}, new int[]{i, 1}, new int[]{i, 2});
            }
            // Check columns
            if (!board[0][i].equals(" ") && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return Arrays.asList(new int[]{0, i}, new int[]{1, i}, new int[]{2, i});
            }
        }
        // Check diagonals
        if (!board[1][1].equals(" ") &&
            ((board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])))) {
            return Arrays.asList(new int[]{0, 0}, new int[]{1, 1}, new int[]{2, 2});
        }
        if (!board[1][1].equals(" ") &&
            (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]))) {
            return Arrays.asList(new int[]{0, 2}, new int[]{1, 1}, new int[]{2, 0});
        }
        return null; // No win found
    }

}
