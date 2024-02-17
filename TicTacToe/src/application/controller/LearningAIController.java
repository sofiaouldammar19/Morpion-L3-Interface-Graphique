package application.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

/**
 * 
 */
public class LearningAIController {
	
	@FXML
    private ProgressBar progressBar;
	
	@FXML
	private TextField textField;

    private Task<Void> task;

    public void initialize() {
        task = new Task<Void>() {
            @Override
            protected Void call() {
                final int max = 100000000;
                for (int i = 1; i <= max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    updateProgress(i, max);
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
    }

    @FXML
    private void onStartClicked() {
        textField.setText("Démarrage en cours...");
        new Thread(task).start();
    }


    @FXML
    private void onCancelClicked() {
        task.cancel();
    }
}
