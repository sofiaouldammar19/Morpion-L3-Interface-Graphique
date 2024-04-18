package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.TicTacToeGame;
import tools.MultiLayerPerceptron;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Controller class for a Tic Tac Toe game application.
 * Handles user interactions and updates the game state accordingly.
 * This controller supports different game modes including Human vs. Human and Human vs. AI.
 *
 * @author Sofia Ould Ammar
 */
public class TicTacToeGameController {

	@FXML
    private GridPane gameBoard; // The grid layout for the Tic Tac Toe board

    @FXML 
    private ToggleButton btn; // A toggle button, if needed for UI (specific function not described)

    @FXML
    private ImageView toggleImageView; // ImageView for displaying current state toggles (e.g., music on/off)

    private Image image1; // Holds the image for the toggle state

    private TicTacToeGame game; // Game logic handler
    private Image xImage; // Image for the X player
    private Image oImage; // Image for the O player
    private boolean withAI; // Flag to check if the game is against AI
    private MultiLayerPerceptron aiModel; // AI model for AI player moves
    private MediaPlayer clickMusic; // MediaPlayer for button click sound effects
    private MediaPlayer winMusic; // MediaPlayer for win sound effects
    private MediaPlayer loseMusic; // MediaPlayer for tie game sound effects
    private MediaPlayer musicPlayer; // MediaPlayer for background music
    private RotateTransition rotateTransition; // Animation for the music toggle button
    
    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	String clickSoundFile = "src/resources/sounds/click.mp3";
        Media clickSound = new Media(new File(clickSoundFile).toURI().toString());
        clickMusic = new MediaPlayer(clickSound);
        
        String winSoundFile = "src/resources/sounds/win.mp3";  // Assuming you have this file
        Media winSound = new Media(new File(winSoundFile).toURI().toString());
        winMusic = new MediaPlayer(winSound);
        
        String loseSoundFile = "src/resources/sounds/lose.mp3"; // Path to your tie music
        Media loseSound = new Media(new File(loseSoundFile).toURI().toString());
        loseMusic = new MediaPlayer(loseSound);
        
        String musicFile = "src/resources/sounds/music.mp3"; // Path to your background music
        Media backgroundMusic = new Media(new File(musicFile).toURI().toString());
        musicPlayer = new MediaPlayer(backgroundMusic);
        
        image1 = new Image(getClass().getResourceAsStream("/resources/images/music.png"));       
        toggleImageView.setImage(image1); // Set initial image
        
        // Initialize rotation transition
        rotateTransition = new RotateTransition(Duration.seconds(2), toggleImageView);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
       
        updateBoard();
    }

    /**
     * Constructs an instance of the TicTacToeGameController. Initializes the game logic and image resources.
     */
    public TicTacToeGameController() {
        game = new TicTacToeGame();
        xImage = new Image(TicTacToeGameController.class.getResourceAsStream("/resources/images/x.png"), 50, 50, true, true);
        oImage = new Image(TicTacToeGameController.class.getResourceAsStream("/resources/images/o.png"), 50, 50, true, true);
    }

    /**
     * Handles action on the 'Home' button click. Prompts user for confirmation to exit.
     */
    @FXML
    private void handleHome() {
    	if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            clickMusic.stop();
            clickMusic.seek(clickMusic.getStartTime());
        }
        clickMusic.play();
        
    	// Create a new stage for the confirmation dialog
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

        // Message label
        Label confirmLabel = new Label("Are you sure you want to exit the game?");
        confirmLabel.setStyle("-fx-text-fill: #836953; -fx-font-size: 13px; -fx-font-family: Helvetica;");      

        // Creating buttons for the dialog
        Button yesButton = new Button("Yes");
        yesButton.setId("btn");
        Button noButton = new Button("No");
        noButton.setId("btn");

        // Setting up actions for buttons
        yesButton.setOnAction(e -> {
            proceedToHome();        	
            stage.close();
        });

        noButton.setOnAction(e ->{ 
        	if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                clickMusic.stop();
                clickMusic.seek(clickMusic.getStartTime());
            }
            clickMusic.play();
            
        	stage.close();
        	
        });

        // Layout for buttons
        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(yesButton, noButton);

        // Main layout for the dialog
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(confirmLabel, buttonLayout);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #cde4b3; -fx-background-radius: 20px;");


        // Set up the scene and stage
        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add(getClass().getResource("/application/Main.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT); // Ensure the scene background is transparent
        stage.setScene(scene);
        stage.showAndWait();
    }
   
    /**
     * Plays or stops the background music based on its current state.
     */
    @FXML
    private void handleMusic() {
        if (musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            musicPlayer.stop();
            rotateTransition.stop(); // Stop the rotation when music stops
        } else {
            musicPlayer.play();
            rotateTransition.play(); // Start the rotation when music plays
        }
    }

    /**
     * Resets the game. If playing against AI, prompts the user to change difficulty.
     */
    @FXML
    private void handleRestart() {
    	// Play sound effect
        if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            clickMusic.stop();
            clickMusic.seek(clickMusic.getStartTime());
        }
        clickMusic.play();
        
        if (withAI) {
            showDifficultyChangePopup();
        } else {
            restartGame();
        }
    }
    
    /**
     * Displays a help dialog with the game rules.
     */
    @FXML
    private void handleHelp(MouseEvent event) {
    	// Play sound effect
        if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            clickMusic.stop();
            clickMusic.seek(clickMusic.getStartTime());
        }
        clickMusic.play();
    	
        // Create the dialog stage which acts as the popup
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

        Label helpContent = new Label(
            "Rules:\n" +
            "- The game is played on a grid that's 3 squares by 3 squares.\n" +
            "- You are X, your friend (or the computer in this game) is O.\n" +
            "- Players take turns putting their marks in empty squares.\n" +
            "- The first player to get 3 of their marks in a row (up, down, across, or diagonally) is the winner.\n" +
            "- When all 9 squares are full, the game is over.\n\n" +
            "Click on an empty square to make your move."
        );
        
        helpContent.setWrapText(true);
        helpContent.setStyle("-fx-text-fill: #836953; -fx-font-size: 13px; -fx-font-family: Helvetica;");      

        // Create a button to close the dialog
        Button closeButton = new Button("Close");
        closeButton.setId("btn");
        closeButton.setOnAction(e -> {
        	if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
	    		clickMusic.stop();
	    		clickMusic.seek(clickMusic.getStartTime());
	        }
	    	clickMusic.play();
	    	
        	stage.close();
        });

        // Layout the popup components
        VBox dialogVBox = new VBox(20);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setPadding(new Insets(15));
        dialogVBox.setStyle("-fx-background-color: #cde4b3; -fx-background-radius: 20px;");        
        dialogVBox.getChildren().addAll(helpContent, closeButton);

        // Create a scene and show the stage
        Scene scene = new Scene(dialogVBox, 450, 350);
        scene.getStylesheets().add(getClass().getResource("/application/Main.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT); // Ensure the scene background is transparent
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    /**
     * Responds to button clicks on the game board. Determines the move based on the game mode.
     */
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
    
    /**
     * Displays a confirmation dialog to change the difficulty when playing against AI.
     * Offers the option to change the difficulty level or continue with the current settings.
     */
    private void showDifficultyChangePopup() {
        // Create a new stage for the confirmation dialog
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

        // Message label
        Label confirmLabel = new Label("Do you want to change the difficulty level?");
        confirmLabel.setWrapText(true);
        confirmLabel.setStyle("-fx-text-fill: #836953; -fx-font-size: 13px; -fx-font-family: Helvetica;"); 

        // Creating buttons for the dialog
        Button yesButton = new Button("Yes");
        yesButton.setId("btn");
        Button noButton = new Button("No");
        noButton.setId("btn");

        // Setting up actions for buttons
        yesButton.setOnAction(e -> {
            stage.close();
            try {
                if (musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    musicPlayer.stop();
                }

                if (winMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                    winMusic.stop();
                }

                if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                    clickMusic.stop();
                    clickMusic.seek(clickMusic.getStartTime());
                }
                clickMusic.play();

                Parent ret = FXMLLoader.load(getClass().getResource("/view/LevelChoiceView.fxml"));
                Scene scene = new Scene(ret, 900, 700);
                Stage currentStage = (Stage) gameBoard.getScene().getWindow();
                currentStage.setScene(scene);
                currentStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        noButton.setOnAction(e -> {
            stage.close();
            restartGame();
        });

        // Layout for buttons
        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(yesButton, noButton);

        // Main layout for the dialog
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(confirmLabel, buttonLayout);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #cde4b3; -fx-background-radius: 20px;");

        // Set up the scene and stage
        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add(getClass().getResource("/application/Main.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    /**
     * Resets the game to its initial state, re-enabling all buttons and clearing the board.
     */
    private void restartGame() {
        if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            clickMusic.stop();
            clickMusic.seek(clickMusic.getStartTime());
        }
        clickMusic.play();
        
        game = new TicTacToeGame(); // Reset the game model
        updateBoard();
        gameBoard.getChildren().forEach(node -> node.setDisable(false)); // Re-enable buttons
    }

    /**
     * Directly transitions the application to the home view.
     * This method is called after user confirmation to exit the game.
     */
    private void proceedToHome() {
        try {
            if (musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                musicPlayer.stop();
            }

            if (winMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                winMusic.stop();
            }

            if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                clickMusic.stop();
                clickMusic.seek(clickMusic.getStartTime());
            }
            clickMusic.play();

            Parent ret = FXMLLoader.load(getClass().getResource("/view/View.fxml"));
            Scene scene = new Scene(ret, 900, 700);
            Stage currentStage = (Stage) gameBoard.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Displays the end game popup with options to restart or go home.
     * This method is invoked when a game ends either by a win or a tie.
     *
     * @param message A message indicating the end game result, e.g., "Player X wins!" or "Game Over: It's a tie!"
     */
    private void showEndGamePopup(String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

        Label winnerLabel = new Label(message);
        winnerLabel.setAlignment(Pos.CENTER); // Ensure the label is centered
        winnerLabel.setStyle("-fx-padding: 20; -fx-text-alignment: center; -fx-text-fill: #836953; -fx-font-size: 17px; -fx-font-family: Helvetica;");

        // Create ImageView for restart button
        ImageView restartBtn = new ImageView(new Image(getClass().getResourceAsStream("/resources/images/draw.png"), 60, 60, true, true));
        restartBtn.setPickOnBounds(true);
        restartBtn.setOnMouseClicked(e -> {
        	restartGame();
            stage.close();
        });

        // Create ImageView for home button
        ImageView homeBtn = new ImageView(new Image(getClass().getResourceAsStream("/resources/images/hoome.png"), 60, 60, true, true));
        homeBtn.setPickOnBounds(true);
        homeBtn.setOnMouseClicked(e -> {
        	proceedToHome();
            stage.close();
        });

        // HBox for holding the image views (icons)
        HBox iconBox = new HBox(10); // Space between icons
        iconBox.setAlignment(Pos.CENTER); // Center the icons horizontally
        iconBox.getChildren().addAll(restartBtn, homeBtn);

        // VBox for the overall layout
        VBox layout = new VBox(10); // Space between label and HBox
        layout.setAlignment(Pos.CENTER); // Center the VBox content
        layout.getChildren().addAll(winnerLabel, iconBox); // Add label and icon HBox to VBox
        layout.setPadding(new Insets(10)); // Padding around the VBox
        layout.setStyle("-fx-background-color: #cde4b3; -fx-background-radius: 20px;");

        // Set the scene and stage
        Scene scene = new Scene(layout, 280, 180);
        scene.getStylesheets().add(getClass().getResource("/application/Main.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT); // Ensure the scene background is transparent
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Determines whether the game is played against AI and sets up the AI model if so.
     *
     * @param withAI Indicates whether the game is against AI.
     * @param modelFilePath The file path to the AI model, necessary if withAI is true.
     */
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
    
    /**
     * Updates the display of the game board based on the current state of the game.
     */
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
    
    /**
     * Handles a player move when playing against another human.
     *
     * @param row The row index where the player clicked.
     * @param col The column index where the player clicked.
     */
    private void playWithHuman(int row, int col) {
        if (game.playMove(row, col)) {
            updateBoard();
            checkGameState();
        }
    }
    
    /**
     * Handles a player move when playing against AI.
     *
     * @param row The row index where the player clicked.
     * @param col The column index where the player clicked.
     */
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
    
    /**
     * Performs an AI move based on the current state of the game board.
     * This method uses the AI model to predict the best move.
     */
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
    
    /**
     * Converts the current state of the game board into an input array for the AI model.
     *
     * @return An array representing the current game board, suitable for AI processing.
     */
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
 
    /**
     * Analyzes AI's output to determine the best move based on the highest score.
     * The AI model outputs a score for each possible move, and this method finds the move with the highest score
     * that is still available on the board.
     *
     * @param output An array of double values representing the score for each board position.
     * @return The index of the board position corresponding to the best move, or -1 if no valid move is found.
     */
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

    /**
     * Checks the current game state to determine if there is a winner or if the game has ended in a tie.
     */
    private void checkGameState() {
        if (game.checkForWin()) {
        	if (winMusic.getStatus() == MediaPlayer.Status.PLAYING) {
        		winMusic.stop();
            }
        	if (musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                musicPlayer.stop();
                rotateTransition.stop(); // Stop the rotation when music stops
            }
            winMusic.play();  // Play win sound
            
            highlightWinningLine(game.getWinningLine());
            showEndGamePopup("Player " + (game.isXTurn() ? "O" : "X") + " wins!");
        } else if (game.isBoardFull()) {
        	if (loseMusic.getStatus() == MediaPlayer.Status.PLAYING) {
        		loseMusic.stop();
            }
        	if (musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                musicPlayer.stop();
                rotateTransition.stop(); // Stop the rotation when music stops
            }
        	loseMusic.play();  // Play tie sound
            showEndGamePopup("Game Over: It's a tie!");
        }
    }
   
    /**
     * Applies a visual effect to highlight the winning line on the game board.
     *
     * @param winningLine A list of coordinate pairs indicating the winning line on the board.
     */
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
