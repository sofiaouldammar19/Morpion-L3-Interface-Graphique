package controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tools.Config;
import tools.ConfigFileLoader;

public class LevelChoiceController {

    @FXML
    private Button easy;
    
    @FXML
    private Button medium;
    
    @FXML
    private Button difficult;
    


    // Initializes the controller class. This method is automatically called
    // after the FXML file has been loaded.
    @FXML
    private void initialize() {
        // Initialization logic hoere, if necessary
    }

    // Handler for the Easy button
    @FXML
    private void handleEasyAction(ActionEvent event) {
        try {
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

    // Handler for the Medium button
    @FXML
    private void handleMediumAction(ActionEvent event) {
    	try {
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

    // Handler for the Difficult button
    @FXML
    private void handleDifficultAction(ActionEvent event) {
    	try {
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
}