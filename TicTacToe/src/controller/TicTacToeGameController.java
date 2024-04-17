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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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

public class TicTacToeGameController {

    @FXML
    private GridPane gameBoard;
    
    @FXML 
    private ToggleButton btn;
    
    @FXML
    private ImageView toggleImageView;

    private Image image1;

    private TicTacToeGame game;
    private Image xImage;
    private Image oImage; 
    private boolean withAI;
    private MultiLayerPerceptron aiModel; 
    private MediaPlayer clickMusic; // MediaPlayer for button click sound
    private MediaPlayer winMusic; // MediaPlayer for win sound effect
    private MediaPlayer loseMusic; // For tie game sound effect
    private MediaPlayer musicPlayer; // MediaPlayer for the background music
    private RotateTransition rotateTransition; // Rotation transition for the music icon
    
    @FXML
    private void initialize() {
    	String clickSoundFile = "src/resources/sounds/bell_sound.mp3";
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

    public TicTacToeGameController() {
        game = new TicTacToeGame();
        xImage = new Image(TicTacToeGameController.class.getResourceAsStream("/resources/images/x.png"), 50, 50, true, true);
        oImage = new Image(TicTacToeGameController.class.getResourceAsStream("/resources/images/o.png"), 50, 50, true, true);
    }
    
    

    @FXML
    private void handleHome() {
        try {
        	if (musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                musicPlayer.stop();
            }
        	
        	if (winMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                winMusic.stop();  // Stop if already playing
            }
        	
        	if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                clickMusic.stop();
                clickMusic.seek(clickMusic.getStartTime());
            }
            clickMusic.play();

            Parent ret = FXMLLoader.load(getClass().getResource("/view/View.fxml"));
            Scene scene = new Scene(ret, 900, 700);
            Stage currentStage = (Stage) gameBoard.getScene().getWindow(); // This assumes gameBoard is part of the currently active scene
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
   
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
    
    @FXML
    private void handleRestart() {
    	if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            clickMusic.stop();
            clickMusic.seek(clickMusic.getStartTime());
        }
        clickMusic.play();
        
        game = new TicTacToeGame(); // Reset the game model
        updateBoard();
        gameBoard.getChildren().forEach(node -> node.setDisable(false)); // Re-enable buttons
    }
    
    @FXML
    private void handleHelp(MouseEvent event) {
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
        helpContent.setStyle("-fx-padding: 20; -fx-text-fill: #836953; -fx-font-size: 15px; -fx-font-family: Helvetica;");      

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
        Scene scene = new Scene(dialogVBox, 500, 400);
        scene.getStylesheets().add(getClass().getResource("/application/Main.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT); // Ensure the scene background is transparent
        stage.setScene(scene);
        stage.showAndWait();
    }

    
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
            handleRestart();
            stage.close();
        });

        // Create ImageView for home button
        ImageView homeBtn = new ImageView(new Image(getClass().getResourceAsStream("/resources/images/hoome.png"), 60, 60, true, true));
        homeBtn.setPickOnBounds(true);
        homeBtn.setOnMouseClicked(e -> {
            handleHome();
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
