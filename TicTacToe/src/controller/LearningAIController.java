package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import ai.Config;
import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction; 


public class LearningAIController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField textField;

    private Task<Void> trainingTask;
    
    private Config config;
    
    private MultiLayerPerceptron net;
    
    public void setConfig(Config config) {
        this.config = config;
    }

    public void setConfigAndStart(Config config) {
        this.config = config;
        startTraining();
    }
    
    private void changeScene() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TicTacToeGameView.fxml")); // Assurez-vous que le chemin est correct
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) progressBar.getScene().getWindow(); // Obtient le stage actuel à partir d'un des composants
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    public void startTraining() {

        // Check if the config was successfully retrieved
        if (config == null) {
        	textField.setText("Configuration is not set.");
            return; // Exit the method if config is not available
        }
        
        System.out.println(config.toString());
        
        String modelFileName = String.format("model-%d-%.1f-%d.srl", config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
        String modelFilePath = "./resources/models/" + modelFileName;

        File modelFile = new File(modelFilePath);
        if (!modelFile.exists()) {
            try {
                modelFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                textField.setText("Failed to create model file.");
                return;
            }
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

                net = new MultiLayerPerceptron(layers, lr, new SigmoidalTransferFunction());
                
                double error = 0.0;

                for (int i = 0; i < epochs; i++) {

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

                 updateMessage("Learning completed!");

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
            save(modelFilePath);

            changeScene();
        });

        
        try {
            // Création d'un flux de sortie pour écrire dans le fichier
            FileOutputStream fileOut = new FileOutputStream(modelFilePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            // Sérialisation et écriture de l'objet 'net' (votre modèle entraîné) dans le fichier
            out.writeObject(net); // 'net' est votre instance de MultiLayerPerceptron

            // Fermeture des flux
            out.close();
            fileOut.close();

            System.out.printf("Le modèle sérialisé est sauvegardé dans %s%n", modelFilePath);
        } catch (IOException i) {
            i.printStackTrace();
        }
        
    }

    public boolean save(String path){
		try{
			FileOutputStream fout = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this);
			oos.close();
		}
		catch (Exception e) { 
			return false;
		}
		return true;
	}
}