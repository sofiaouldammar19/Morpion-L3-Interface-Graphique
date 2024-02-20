package application.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import ai.Config;
import ai.MultiLayerPerceptron;
import ai.Coup; // Assuming Coup is a class you have defined for your game states

import java.util.HashMap;

public class LearningAIController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField textField;

    private Task<Void> trainingTask;

    @FXML
    private void onStartClicked() {
        if (trainingTask == null || trainingTask.isDone()) {
            trainingTask = createTrainingTask();
            progressBar.progressProperty().bind(trainingTask.progressProperty());
            textField.textProperty().bind(trainingTask.messageProperty());
            new Thread(trainingTask).start();
        }
    }

    private Task<Void> createTrainingTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Simulate loading data and training the model
                updateMessage("Loading data...");
                HashMap<Integer, Coup> coups = loadGames("./resources/dataset/Tic_tac_initial_results.csv");
                // Simulated training process
                final int epochs = 10000; // Total number of training epochs
                for (int i = 1; i <= epochs; i++) {
                    if (isCancelled()) {
                        updateMessage("Training cancelled.");
                        break;
                    }
                    // Simulate training and update progress
                    double error = simulateTrainingStep(i); // This should be replaced with actual training logic
                    updateProgress(i, epochs);
                    updateMessage(String.format("Error at step %d is %.5f", i, error));
                    Thread.sleep(10); // Simulate time taken for a training step
                }
                updateMessage("Training completed!");
                return null;
            }
        };
    }

    private double simulateTrainingStep(int step) {
        // Simulate an error rate decreasing as training progresses
        return 1.0 / step;
    }

    // Placeholder for your loadGames method
    private HashMap<Integer, Coup> loadGames(String fileName) {
        // Implement loading logic here
        return new HashMap<>();
    }

    @FXML
    private void onCancelClicked() {
        if (trainingTask != null) {
            trainingTask.cancel();
        }
    }
}
