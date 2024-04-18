package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The Main class is the entry point for the JavaFX application.
 * It sets up the primary stage and loads the initial scene.
 * This class extends Application, making it the core of a JavaFX app.
 *
 * @author Sofia Ould Ammar
 */
public class Main extends Application {

	/**
     * Starts the primary stage for the application, loading the main view from an FXML file,
     * setting the application icon, and configuring scene properties.
     *
     * @param stage The primary stage for this application, onto which the scene is set.
     * @throws Exception if there is any issue loading the FXML file or setting the stage.
     */
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/View.fxml"));
		
		stage.setTitle("Frog Tic Tac Toe");
		
        Image logo = new Image(getClass().getResourceAsStream("/resources/images/logo.png"), 50, 50, true, true);		
        stage.getIcons().add(logo);
		
		Scene scene = new Scene(root, 900, 700);
		stage.setScene(scene);
		stage.setMinHeight(550);
		stage.setMinWidth(550);
		stage.show();
		
	}
	
	/**
     * The main method is the entry point of the application when launched.
     * It simply calls launch() to start the JavaFX application.
     *
     * @param args the command line arguments
     */
	public static void main(String[] args) {
		launch(args);
	}
}