package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage wind) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/LearningAIView.fxml"));
		
		wind.setTitle("JavaFX");
		
		Scene scene = new Scene(root, 600, 400);
		wind.setScene(scene);
		wind.setMinHeight(550);
		wind.setMinWidth(550);
		wind.show();
		
	}
	public static void main(String[] args) {
		launch(args);
	}
	
}