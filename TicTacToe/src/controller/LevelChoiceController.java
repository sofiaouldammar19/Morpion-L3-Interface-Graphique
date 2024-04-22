package controller;

import java.io.File;
import java.io.IOException;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import tools.Config;
import tools.ConfigFileLoader;

/**
 * The LevelChoiceController class handles level selection for a tic-tac-toe game,
 * enabling the user to choose between Easy, Medium, and Difficult levels.
 * It manages transitions between the level selection view and the game or learning views,
 * depending on whether a trained AI model exists for the chosen level.
 *
 */
public class LevelChoiceController {

    @FXML
    private Button easy;
    
    @FXML
    private Button medium;
    
    @FXML
    private Button difficult;
    
    private MediaPlayer clickMusic; // MediaPlayer for button click sound


    /**
     * Initializes the controller after the FXML elements have been loaded.
     * This setup includes preparing a media player for sound effects and applying
     * animations to buttons.
     */
    @FXML
    private void initialize() {
    	// Specify the path to your music file
        String musicFile = "src/resources/sounds/click.mp3";

        // Create a Media object for the specified file
        Media music = new Media(new File(musicFile).toURI().toString());

        // Create a MediaPlayer object and attach the Media object
        clickMusic = new MediaPlayer(music);
        
     // Setting up button animations
        buttonAnimation(easy);
        buttonAnimation(medium);
        buttonAnimation(difficult);
    }
    
    /**
     * Applies a scale transition animation to a button to enhance UI interactivity.
     * This animation enlarges the button when hovered and returns it to its original size when not.
     *
     * @param button the button to animate
     */
    private void buttonAnimation(Button button) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
        st.setFromX(1);
        st.setToX(1.1);
        st.setFromY(1);
        st.setToY(1.1);
        st.setAutoReverse(true);

        button.setOnMouseEntered(e -> st.playFromStart());
        button.setOnMouseExited(e -> st.stop());
    }

    /**
     * Handles actions performed when the 'Easy' level is selected.
     * This method sets up the game configuration for an easy level, checks if an AI model exists,
     * and transitions to the appropriate view based on model availability.
     *
     * @param event the action event triggered when the Easy button is clicked
     */
    @FXML
    private void handleEasyAction(ActionEvent event) {
        try {
        	// Play sound effect
            if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                clickMusic.stop();
                clickMusic.seek(clickMusic.getStartTime());
            }
            clickMusic.play();
        	
            ConfigFileLoader configFileLoader = new ConfigFileLoader();
            configFileLoader.loadConfigFile("./resources/config.txt");
            Config config = configFileLoader.get("F"); // Assume "F" is for Easy
            
            String modelFileName = String.format("easy-%d-%.1f-%d.srl", config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
            String modelFilePath = "./resources/models/" + modelFileName;
            
            File file = new File(modelFilePath);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file.exists() ? "/view/TicTacToeGameView.fxml" : "/view/LearningAIView.fxml"));
            Parent root = loader.load();
            
            if (!file.exists()) {
                // AI learning view
                LearningAIController learningAIController = loader.getController();
                learningAIController.setConfigAndStart(config);
            } else {
                // Game view against AI
                TicTacToeGameController gameController = loader.getController();
                gameController.setWithAI(true, modelFilePath);
            }
            
            Scene scene = new Scene(root, 900, 700);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles actions performed when the 'Medium' level is selected.
     * Similar to handleEasyAction, but configures the game for a medium difficulty level.
     *
     * @param event the action event triggered when the Medium button is clicked
     */
    @FXML
    private void handleMediumAction(ActionEvent event) {
    	try {
    		// Play sound effect
            if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                clickMusic.stop();
                clickMusic.seek(clickMusic.getStartTime());
            }
            clickMusic.play();
    		
            ConfigFileLoader configFileLoader = new ConfigFileLoader();
            configFileLoader.loadConfigFile("./resources/config.txt");
            Config config = configFileLoader.get("M"); // Assume "F" is for Easy
            
            String modelFileName = String.format("medium-%d-%.1f-%d.srl", config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
            String modelFilePath = "./resources/models/" + modelFileName;
            
            File file = new File(modelFilePath);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file.exists() ? "/view/TicTacToeGameView.fxml" : "/view/LearningAIView.fxml"));
            Parent root = loader.load();
            
            if (!file.exists()) {
                // AI learning view
                LearningAIController learningAIController = loader.getController();
                learningAIController.setConfigAndStart(config);
            } else {
                // Game view against AI
                TicTacToeGameController gameController = loader.getController();
                gameController.setWithAI(true, modelFilePath);
            }
            
            Scene scene = new Scene(root, 900, 700);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles actions performed when the 'Difficult' level is selected.
     * Similar to handleEasyAction, but configures the game for a difficult level.
     *
     * @param event the action event triggered when the Difficult button is clicked
     */
    @FXML
    private void handleDifficultAction(ActionEvent event) {
    	try {
    		// Play sound effect
            if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                clickMusic.stop();
                clickMusic.seek(clickMusic.getStartTime());
            }
            clickMusic.play();
    		
            ConfigFileLoader configFileLoader = new ConfigFileLoader();
            configFileLoader.loadConfigFile("./resources/config.txt");
            Config config = configFileLoader.get("D"); // Assume "F" is for Easy
            
            String modelFileName = String.format("hard-%d-%.1f-%d.srl", config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
            String modelFilePath = "./resources/models/" + modelFileName;
            
            File file = new File(modelFilePath);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file.exists() ? "/view/TicTacToeGameView.fxml" : "/view/LearningAIView.fxml"));
            Parent root = loader.load();
            
            if (!file.exists()) {
                // AI learning view
                LearningAIController learningAIController = loader.getController();
                learningAIController.setConfigAndStart(config);
            } else {
                // Game view against AI
                TicTacToeGameController gameController = loader.getController();
                gameController.setWithAI(true, modelFilePath);
            }
            
            Scene scene = new Scene(root, 900, 700);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    
    /**
     * Handles navigation back to the home view.
     * This method loads and displays the main menu scene when the home icon is clicked.
     *
     * @param event the mouse event triggered when the home icon is clicked
     */
    @FXML
    private void handleHome(MouseEvent event) throws IOException {
    	// Play sound effect
        if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            clickMusic.stop();
            clickMusic.seek(clickMusic.getStartTime());
        }
        clickMusic.play();
    	
        Parent ret = FXMLLoader.load(getClass().getResource("/view/View.fxml"));
        Scene nvScene = new Scene(ret, 900, 700);
        Stage stageActuel = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActuel.setScene(nvScene);
        stageActuel.show();
  }
}