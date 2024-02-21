package controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import java.util.HashMap;

import ai.Config;
import ai.ConfigFileLoader;
import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction; 


public class LearningAIController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField textField;

    @FXML
    private Button startButton; 

    @FXML
    private Button cancelButton;

    private Task<Void> trainingTask;
    
    private ai.Config config;

    @FXML
    void onStartClicked(ActionEvent event) {
        startTraining();
    }
    
    private void startTraining() {
        String desiredLevel = "F"; 

        // Initialize ConfigFileLoader and load configurations
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        configFileLoader.loadConfigFile("./resources/config.txt");
        
        // Retrieve the specific Config object for the desired level
        Config config = configFileLoader.get(desiredLevel);
        System.out.println(config.toString());

        // Check if the config was successfully retrieved
        if (config == null) {
            textField.setText("Failed to load configuration for level: " + desiredLevel);
            return; // Exit the method if config is not available
        }

        HashMap<Integer, Coup> mapTrain = ai.Test.loadCoupsFromFile("./resources/train_dev_test/train.txt");
        int size = 9;
        int h = config.hiddenLayerSize;
        double lr = config.learningRate;
        int l = config.numberOfhiddenLayers;
        double epochs = 100000;
        boolean verbose = true;

        trainingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (verbose) updateMessage("START TRAINING ...");


                int[] layers = new int[l + 2];
                layers[0] = size;
                for (int i = 0; i < l; i++) {
                    layers[i + 1] = h;
                }
                layers[layers.length - 1] = size;

                MultiLayerPerceptron net = new MultiLayerPerceptron(layers, lr, new SigmoidalTransferFunction());
                double error = 0.0;

                for (int i = 0; i < epochs; i++) {
                    if (isCancelled()) {
                        updateMessage("Training cancelled.");
                        break;
                    }

                    Coup c = null;
                    while (c == null) {
                        c = mapTrain.get((int) (Math.round(Math.random() * mapTrain.size())));
                    }

                    error += net.backPropagate(c.in, c.out);

                    if (i % 1000 == 0 && verbose) { 
                        String message = "Error at step " + i + " is " + (error / (double) i);
                        updateMessage(message);
                        updateProgress(i, epochs);
                    }
                }

                if (!isCancelled()) updateMessage("Learning completed!");

                return null;
            }
        };

        progressBar.progressProperty().bind(trainingTask.progressProperty());
        textField.textProperty().bind(trainingTask.messageProperty());

        Thread thread = new Thread(trainingTask);
        thread.setDaemon(true);
        thread.start();

        trainingTask.setOnSucceeded(e -> {
            progressBar.progressProperty().unbind();
            textField.textProperty().unbind();
            progressBar.setProgress(0);
            textField.setText("Training complete!");
        });

        trainingTask.setOnCancelled(e -> {
            progressBar.progressProperty().unbind();
            textField.textProperty().unbind();
            progressBar.setProgress(0);
            textField.setText("Training cancelled.");
        });
    }
    
    @FXML
    void onCancelClicked(ActionEvent event) {
        if (trainingTask != null) {
            trainingTask.cancel();
        }
    }

}
