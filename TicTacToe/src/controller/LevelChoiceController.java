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

public class LevelChoiceController {

    @FXML
    private Button easy;
    
    @FXML
    private Button medium;
    
    @FXML
    private Button difficult;
    
    private MediaPlayer clickMusic; // MediaPlayer for button click sound


    // Initializes the controller class. This method is automatically called
    // after the FXML file has been loaded.
    @FXML
    private void initialize() {
    	// Specify the path to your music file
        String musicFile = "src/resources/sounds/bell_sound.mp3";

        // Create a Media object for the specified file
        Media music = new Media(new File(musicFile).toURI().toString());

        // Create a MediaPlayer object and attach the Media object
        clickMusic = new MediaPlayer(music);
        
     // Setting up button animations
        buttonAnimation(easy);
        buttonAnimation(medium);
        buttonAnimation(difficult);
    }
    
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

    // Handler for the Easy button
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
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file.exists() ? "/view/GameVsAIView.fxml" : "/view/LearningAIView.fxml"));
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

    // Handler for the Medium button
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
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file.exists() ? "/view/GameVsAIView.fxml" : "/view/LearningAIView.fxml"));
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

    // Handler for the Difficult button
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
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file.exists() ? "/view/GameVsAIView.fxml" : "/view/LearningAIView.fxml"));
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
    
    @FXML
    private void handleHome(MouseEvent event) throws IOException {
	  Parent ret = FXMLLoader.load(getClass().getResource("/view/View.fxml"));
      Scene nvScene = new Scene(ret, 900, 700);
      Stage stageActuel = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stageActuel.setScene(nvScene);
      stageActuel.show();
  }
}