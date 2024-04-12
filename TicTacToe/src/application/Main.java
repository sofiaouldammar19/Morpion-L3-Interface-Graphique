package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/View.fxml"));
		
		stage.setTitle("Tic Tac Toe");
		
        Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"), 50, 50, true, true);		
        stage.getIcons().add(logo);
		
		Scene scene = new Scene(root, 900, 700);
		stage.setScene(scene);
		stage.setMinHeight(550);
		stage.setMinWidth(550);
		stage.show();
		
	}
	public static void main(String[] args) {
		launch(args);
	}
	
}