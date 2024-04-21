package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import tools.Config;
import tools.Coup;
import tools.MultiLayerPerceptron;
import tools.SigmoidalTransferFunction;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javafx.util.Duration;

/**
 * The LearningAIController class manages the AI training process for a tic-tac-toe game.
 * It allows setting configuration parameters for the AI model, initiates the training process,
 * and handles the user interface during training, including progress updates and animations.
 *
 */
public class LearningAIController {

    @FXML
    private ProgressIndicator progressIndicator;
    
    @FXML
    private ImageView imageView;


    private Task<Void> trainingTask;
    
    private Config config;
    
    private MultiLayerPerceptron net;
    
    /**
     * Sets the configuration for the AI model.
     * This configuration includes parameters like the number of hidden layers and learning rate.
     *
     * @param config the configuration settings for the AI model
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * Sets the configuration and starts the AI training process.
     * This method initializes training immediately after setting the configuration.
     *
     * @param config the configuration settings for the AI model
     */
    public void setConfigAndStart(Config config) {
        this.config = config;
        startTraining();
    }
    
    /**
     * Changes the scene of the JavaFX application to display the tic-tac-toe game interface.
     * This method is called after AI training completes, and it sets up the game for playing against the AI.
     *
     * @param modelFilePath the file path to the trained model which will be used in the game
     */
    private void changeScene(String modelFilePath) {
    	Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameVsAIView.fxml")); 
                Parent root = loader.load();
                Scene scene = new Scene(root, 900, 700);
                Stage stage = (Stage) progressIndicator.getScene().getWindow();
                
             // Retrieve the game controller and set AI mode
                TicTacToeGameController gameController = loader.getController();
                gameController.setWithAI(true, modelFilePath);  // Set the AI model
                
                stage.setScene(scene);                
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    /**
     * Starts the AI training process.
     * This method prepares and executes the training task, updating the UI with progress and messages.
     */
    public void startTraining() {
    	startImageAnimation();

        // Check if the config was successfully retrieved
        if (config == null) {
        	System.out.println("Configuration is not set.");
            return; // Exit the method if config is not available
        }
        
        System.out.println(config.toString());
        
        String modelFileName = "";
        if (config.level.equals("F")) {
            modelFileName = String.format("easy-%d-%.1f-%d.srl", config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
        } else if (config.level.equals("M")) {
            modelFileName = String.format("medium-%d-%.1f-%d.srl", config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
        } else {
            modelFileName = String.format("hard-%d-%.1f-%d.srl", config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
        }
        String modelFilePath = "./resources/models/" + modelFileName;
        
        File modelFile = new File(modelFilePath);
        if (!modelFile.exists()) {
            try {
                modelFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to create model file.");
                return;
            }
        }

        HashMap<Integer, Coup> mapTrain = tools.Test.loadCoupsFromFile("./resources/train_dev_test/train.txt");
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

                    error = net.backPropagate(c.in, c.out);

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

        progressIndicator.progressProperty().bind(trainingTask.progressProperty()); 

        Thread thread = new Thread(trainingTask);
        thread.setDaemon(true);
        thread.start();

        trainingTask.setOnSucceeded(e -> {
            progressIndicator.progressProperty().unbind(); 
            progressIndicator.setProgress(0);
            System.out.println("Training complete!");
            if (net.save(modelFilePath)) { // Ensure the model is saved before transitioning
                System.out.printf("The training model is saved in %s%n", modelFilePath);
                changeScene(modelFilePath);  // Pass the model file path to the game scene
            } else {
                System.out.println("Failed to save the model. Check permissions or path.");
            }
        });
    }

    /**
     * Initiates an animation in the imageView to display during the AI training process.
     * This animation cycles through a set of images to indicate ongoing activity.
     */
	private void startImageAnimation() {
		String[] paths = {"/resources/images/gr10.png", "/resources/images/gr11.png", "/resources/images/gr12.png"};
	    Image[] images = new Image[paths.length];

	    for (int i = 0; i < paths.length; i++) {
	        URL url = getClass().getResource(paths[i]);
	        if (url == null) {
	            System.out.println("Cannot find image: " + paths[i]);
	            continue;
	        }
	        images[i] = new Image(url.toString());
	    }
		    
	    final int[] imageIndex = {0}; 

	    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
	        imageView.setImage(images[imageIndex[0]]);
	        imageView.setFitWidth(200); 
	        imageView.setFitHeight(200);
	        imageIndex[0] = (imageIndex[0] + 1) % images.length;
	    }));

	    timeline.setCycleCount(Timeline.INDEFINITE);
	    timeline.play();
	}
}
