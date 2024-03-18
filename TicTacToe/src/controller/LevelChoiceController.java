package controller;

import java.io.File;
import java.io.IOException;

import ai.Config;
import ai.ConfigFileLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
        // Initialization logic here, if necessary
    }

    // Handler for the Easy button
    @FXML
    private void handleEasyAction(ActionEvent event) {
        try {
            ConfigFileLoader configFileLoader = new ConfigFileLoader();
            configFileLoader.loadConfigFile("./resources/config.txt");
            Config config = configFileLoader.get("F"); // Assume "F" is for Easy
            
            String modelFileName = String.format("model-%d-%.1f-%d.srl", config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
            String modelFilePath = "./resources/models/" + modelFileName;
            
            File file = new File(modelFilePath);
            
            FXMLLoader loader;
            if (!file.exists()) {
                loader = new FXMLLoader(getClass().getResource("/view/LearningAIView.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/view/TicTacToeGameView.fxml"));
            }
            Parent root = loader.load();
            Stage stage = (Stage) easy.getScene().getWindow();
            Scene scene = new Scene(root, 900, 700);
            stage.setScene(scene);
            stage.show();

            if (!file.exists()) {
                LearningAIController learningAIController = loader.getController();
                learningAIController.setConfigAndStart(config); // Pass the config to LearningAIController
            }
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
            
            String modelFileName = String.format("model-%d-%.1f-%d.srl", config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
            String modelFilePath = "./resources/models/" + modelFileName;
            
            File file = new File(modelFilePath);
            
            FXMLLoader loader;
            if (!file.exists()) {
                loader = new FXMLLoader(getClass().getResource("/view/LearningAIView.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/view/TicTacToeGameView.fxml"));
            }
            Parent root = loader.load();
            Stage stage = (Stage) medium.getScene().getWindow();
            Scene scene = new Scene(root, 900, 700);
            stage.setScene(scene);
            stage.show();

            if (!file.exists()) {
                LearningAIController learningAIController = loader.getController();
                learningAIController.setConfigAndStart(config); // Pass the config to LearningAIController
            }
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
            
            String modelFileName = String.format("model-%d-%.1f-%d.srl", config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
            String modelFilePath = "./resources/models/" + modelFileName;
            
            File file = new File(modelFilePath);
            
            FXMLLoader loader;
            if (!file.exists()) {
                loader = new FXMLLoader(getClass().getResource("/view/LearningAIView.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/view/TicTacToeGameView.fxml"));
            }
            Parent root = loader.load();
            Stage stage = (Stage) difficult.getScene().getWindow();
            Scene scene = new Scene(root, 900, 700);
            stage.setScene(scene);
            stage.show();

            if (!file.exists()) {
                LearningAIController learningAIController = loader.getController();
                learningAIController.setConfigAndStart(config); // Pass the config to LearningAIController
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
}