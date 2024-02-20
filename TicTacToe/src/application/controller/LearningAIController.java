package application.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import ai.Config;
import ai.MultiLayerPerceptron;
import ai.Coup;
import ai.Test;// Assuming Coup is a class you have defined for your game states

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
    			HashMap<Integer, Coup> mapTrain = loadCoupsFromFile("./resources/train_dev_test/train.txt");
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
    private HashMap<Integer, Coup> loadCoupsFromFile(String file) {
    	System.out.println("loadCoupsFromFile from "+file+" ...");
		HashMap<Integer, Coup> map = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file))));
			String s = "";
			while ((s = br.readLine()) != null) {
				String[] sIn = s.split("\t")[0].split(" ");
				String[] sOut = s.split("\t")[1].split(" ");

				double[] in = new double[sIn.length];
				double[] out = new double[sOut.length];

				for (int i = 0; i < sIn.length; i++) {
					in[i] = Double.parseDouble(sIn[i]);
				}

				for (int i = 0; i < sOut.length; i++) {
					out[i] = Double.parseDouble(sOut[i]);
				}

				Coup c = new Coup(9, "");
				c.in = in ;
				c.out = out ;
				map.put(map.size(), c);
			}
			br.close();
		} 
		catch (Exception e) {
			System.out.println("Test.loadCoupsFromFile()");
			e.printStackTrace();
			System.exit(-1);
		}
		return map ;
    }

    @FXML
    private void onCancelClicked() {
        if (trainingTask != null) {
            trainingTask.cancel();
        }
    }
}
