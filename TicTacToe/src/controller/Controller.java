package controller;

import tools.Config;
import tools.ConfigFileLoader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Controller implements Initializable {
	
	@FXML
    private MenuItem settingsMenuItem;
	
	@FXML 
	private MenuItem modelsMenuItem; 
	 
	@FXML
	private Button gameVsAIBtn;
	
	@FXML
	private Button gameVsHumanBtn;
	 
	 @FXML
	 private Slider playPauseSlider;
	 
	 private MediaPlayer mediaPlayer;
	 
	 public static boolean playingAgainstAI = false;

	 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	settingsMenuItem.setOnAction(event -> showSettingsPopup());
    }

    public void showSettingsPopup() {
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        configFileLoader.loadConfigFile("./resources/config.txt");

        Config levelF = configFileLoader.get("F");
        Config levelM = configFileLoader.get("M");
        Config levelD = configFileLoader.get("D");

        Pane pane = new Pane();
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Configuration");
        popupWindow.setHeight(600);
        popupWindow.setWidth(600);

        Text facile = new Text();
        facile.setText("Easy : \n");
        facile.setLayoutX(20);
        facile.setLayoutY(110);

        Text moyen = new Text();
        moyen.setText("Medium : \n");
        moyen.setLayoutX(20);
        moyen.setLayoutY(170);

        Text difficile = new Text();
        difficile.setText("Difficult : \n");
        difficile.setLayoutX(20);
        difficile.setLayoutY(230);

        pane.getChildren().addAll(facile, moyen, difficile);

        UnaryOperator<Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        // Facile
        TextField hiddenLayerSizeF = createNumericTextField(105, 90, Integer.toString(levelF.hiddenLayerSize), filter);
        TextField learningRateF = createNumericTextField(200, 90, Double.toString(levelF.learningRate), filter);
        TextField numberOfhiddenLayersF = createNumericTextField(300, 90, Integer.toString(levelF.numberOfhiddenLayers),
                filter);

        // Moyen
        TextField hiddenLayerSizeM = createNumericTextField(105, 150, Integer.toString(levelM.hiddenLayerSize), filter);
        TextField learningRateM = createNumericTextField(200, 150, Double.toString(levelM.learningRate), filter);
        TextField numberOfhiddenLayersM = createNumericTextField(300, 150,
                Integer.toString(levelM.numberOfhiddenLayers), filter);

        // Difficile
        TextField hiddenLayerSizeD = createNumericTextField(105, 210, Integer.toString(levelD.hiddenLayerSize), filter);
        TextField learningRateD = createNumericTextField(200, 210, Double.toString(levelD.learningRate), filter);
        TextField numberOfhiddenLayersD = createNumericTextField(300, 210,
                Integer.toString(levelD.numberOfhiddenLayers), filter);

        Button modifier = new Button("Modifier");
        modifier.setLayoutX(400);
        modifier.setLayoutY(300);

        modifier.setOnAction(event -> {
            String configFilePath = "./resources/config.txt";

            // Supprime le fichier existant
            new File(configFilePath).delete();

            // le contenu à écrire
            String ligneF = String.format("F:%s:%s:%s\n", hiddenLayerSizeF.getText(), learningRateF.getText(),
                    numberOfhiddenLayersF.getText());
            String ligneM = String.format("M:%s:%s:%s\n", hiddenLayerSizeM.getText(), learningRateM.getText(),
                    numberOfhiddenLayersM.getText());
            String ligneD = String.format("D:%s:%s:%s\n", hiddenLayerSizeD.getText(), learningRateD.getText(),
                    numberOfhiddenLayersD.getText());

            // Écrit le contenu dans le nouveau fichier
            try {
                Files.write(Paths.get(configFilePath), (ligneF + ligneM + ligneD).getBytes(),
                        StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }

            popupWindow.close();
        });

        pane.getChildren().addAll(hiddenLayerSizeF, learningRateF, numberOfhiddenLayersF, hiddenLayerSizeM,
                learningRateM, numberOfhiddenLayersM, hiddenLayerSizeD, learningRateD, numberOfhiddenLayersD, modifier);

        Scene scene = new Scene(pane);
        popupWindow.setScene(scene);
        popupWindow.showAndWait();
    }

    private TextField createNumericTextField(int layoutX, int layoutY, String text, UnaryOperator<Change> filter) {
        TextField textField = new TextField(text);
        textField.setLayoutX(layoutX);
        textField.setLayoutY(layoutY);
        textField.setPrefWidth(75);
        textField.setTextFormatter(new TextFormatter<>(filter));
        return textField;
    }
    
    
    @FXML
    private void handleGameVsAI(ActionEvent event) { 
        try {
        	// set to true when playing against the AI
        	Controller.playingAgainstAI = true;
        	System.out.println(Controller.playingAgainstAI);
        	
            // Load the FXML for the LearningAIController scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LevelChoiceView.fxml")); 
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) gameVsAIBtn.getScene().getWindow(); 

            // Create a new scene with the loaded root node
            Scene scene = new Scene(root, 900, 700);

            // Set the new scene on the current stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleGameVsHuman(ActionEvent event)
    {
    	try {
    		System.out.println("Game mode set to: Human vs Human");
    		// Set to false when playing against another human
    		Controller.playingAgainstAI = false;
    		
    		// Load the FXML for the LearningAIController scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TicTacToeGameView.fxml")); 
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) gameVsHumanBtn.getScene().getWindow(); 

            // Create a new scene with the loaded root node
            Scene scene = new Scene(root, 900, 700);

            // Set the new scene on the current stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    @FXML
    private void handleModelsMenuItem(ActionEvent event) {
    	try {
            Stage stage = new Stage();
            stage.setTitle("Model Files");

            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(10));

            ListView<CheckBox> listView = new ListView<>();
            File dir = new File("./resources/models/");
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        CheckBox checkBox = new CheckBox(file.getName());
                        listView.getItems().add(checkBox);
                    }
                }
            }

            Button deleteButton = new Button("Delete");
            Button selectAllButton = new Button("Select All");

            deleteButton.setOnAction(e -> {
                List<CheckBox> selectedItems = new ArrayList<>();
                for (CheckBox checkBox : listView.getItems()) {
                    if (checkBox.isSelected()) {
                        selectedItems.add(checkBox);
                        File fileToDelete = new File(dir, checkBox.getText());
                        fileToDelete.delete();
                    }
                }
                listView.getItems().removeAll(selectedItems);
            });

            selectAllButton.setOnAction(e -> {
                for (CheckBox checkBox : listView.getItems()) {
                    checkBox.setSelected(true);
                }
            });

            vbox.getChildren().addAll(listView, deleteButton, selectAllButton);

            Scene scene = new Scene(vbox, 300, 400);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}