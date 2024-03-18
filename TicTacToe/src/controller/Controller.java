package controller;

import ai.Config;
import ai.ConfigFileLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    	Stage popupWindow=new Stage();
    	popupWindow.initModality(Modality.APPLICATION_MODAL);
    	popupWindow.setTitle("Configuration");
    	popupWindow.setHeight(600);
    	popupWindow.setWidth(600);
    	
    	
    	
    	Text facile = new Text();
    	facile.setText("F : \n");
    	facile.setLayoutX(20);
    	facile.setLayoutY(110);
    	
    	Text moyen = new Text();
    	moyen.setText("M : \n");
    	moyen.setLayoutX(20);
    	moyen.setLayoutY(170);
    	
    	Text difficile = new Text();
    	difficile.setText("D : \n");
    	difficile.setLayoutX(20);
    	difficile.setLayoutY(230);
    	
        pane.getChildren().add(facile);
        pane.getChildren().add(moyen);
        pane.getChildren().add(difficile);
    	
    	//Facile
    	TextField hiddenLayerSizeF = new TextField();
    	hiddenLayerSizeF.setText(Integer.toString(levelF.hiddenLayerSize));
        hiddenLayerSizeF.setLayoutX(105);
        hiddenLayerSizeF.setLayoutY(90);  
        hiddenLayerSizeF.setPrefWidth(75);
    	pane.getChildren().add(hiddenLayerSizeF);
    	
    	TextField learningRateF = new TextField();
    	learningRateF.setText(Double.toString(levelF.learningRate));
    	learningRateF.setLayoutX(200);
    	learningRateF.setLayoutY(90);  
        learningRateF.setPrefWidth(75);
    	pane.getChildren().add(learningRateF);
    	
    	
    	TextField numberOfhiddenLayersF = new TextField();
    	numberOfhiddenLayersF.setText(Integer.toString(levelF.numberOfhiddenLayers));
    	numberOfhiddenLayersF.setLayoutX(300);
    	numberOfhiddenLayersF.setLayoutY(90);  
    	numberOfhiddenLayersF.setPrefWidth(75);
    	pane.getChildren().add(numberOfhiddenLayersF);
    	
    	
    	//Moyen
    	TextField hiddenLayerSizeM = new TextField();
    	hiddenLayerSizeM.setText(Integer.toString(levelM.hiddenLayerSize));
        hiddenLayerSizeM.setLayoutX(105);
        hiddenLayerSizeM.setLayoutY(150);  
        hiddenLayerSizeM.setPrefWidth(75);
    	pane.getChildren().add(hiddenLayerSizeM);
    	
    	TextField learningRateM = new TextField();
    	learningRateM.setText(Double.toString(levelM.learningRate));
    	learningRateM.setLayoutX(200);
    	learningRateM.setLayoutY(150);  
    	learningRateM.setPrefWidth(75);
    	pane.getChildren().add(learningRateM);
    	
    	TextField numberOfhiddenLayersM = new TextField();
    	numberOfhiddenLayersM.setText(Integer.toString(levelM.numberOfhiddenLayers));
    	numberOfhiddenLayersM.setLayoutX(300);
    	numberOfhiddenLayersM.setLayoutY(150);  
    	numberOfhiddenLayersM.setPrefWidth(75);
    	pane.getChildren().add(numberOfhiddenLayersM);
    	
    	
    	//difficile
    	TextField hiddenLayerSizeD = new TextField();
    	hiddenLayerSizeD.setText(Integer.toString(levelD.hiddenLayerSize));
        hiddenLayerSizeD.setLayoutX(105);
        hiddenLayerSizeD.setLayoutY(210);  
        hiddenLayerSizeD.setPrefWidth(75);
    	pane.getChildren().add(hiddenLayerSizeD);
    	
    	TextField learningRateD = new TextField();
    	learningRateD.setText(Double.toString(levelD.learningRate));
    	learningRateD.setLayoutX(200);
    	learningRateD.setLayoutY(210);  
    	learningRateD.setPrefWidth(75);
    	pane.getChildren().add(learningRateD);
    	
    	
    	TextField numberOfhiddenLayersD = new TextField();
    	numberOfhiddenLayersD.setText(Integer.toString(levelD.numberOfhiddenLayers));
    	numberOfhiddenLayersD.setLayoutX(300);
    	numberOfhiddenLayersD.setLayoutY(210);  
    	numberOfhiddenLayersD.setPrefWidth(75);
    	pane.getChildren().add(numberOfhiddenLayersD);
    	
    	
    	Button modifier = new Button();
    	modifier.setText("Modifier");
    	modifier.setLayoutX(400);
    	modifier.setLayoutY(300);
    	pane.getChildren().add(modifier);
    	
    	
    	modifier.setOnAction(event -> {
    	    String configFilePath = "./resources/config.txt";

    	    // Supprime le fichier existant
    	    new File(configFilePath).delete();

    	    // Prépare le contenu à écrire en vérifiant directement si les champs de texte sont vides
    	    String ligneF = "F:" + (hiddenLayerSizeF.getText().isEmpty() ? "0" : hiddenLayerSizeF.getText()) + 
    	                    ":" + (learningRateF.getText().isEmpty() ? "0" : learningRateF.getText()) + 
    	                    ":" + (numberOfhiddenLayersF.getText().isEmpty() ? "0" : numberOfhiddenLayersF.getText()) + "\n";
    	    
    	    String ligneM = "M:" + (hiddenLayerSizeM.getText().isEmpty() ? "0" : hiddenLayerSizeM.getText()) + 
    	                    ":" + (learningRateM.getText().isEmpty() ? "0" : learningRateM.getText()) + 
    	                    ":" + (numberOfhiddenLayersM.getText().isEmpty() ? "0" : numberOfhiddenLayersM.getText()) + "\n";
    	    
    	    String ligneD = "D:" + (hiddenLayerSizeD.getText().isEmpty() ? "0" : hiddenLayerSizeD.getText()) + 
    	                    ":" + (learningRateD.getText().isEmpty() ? "0" : learningRateD.getText()) + 
    	                    ":" + (numberOfhiddenLayersD.getText().isEmpty() ? "0" : numberOfhiddenLayersD.getText()) + "\n";
    	    
    	    String ligne = ligneF + ligneM + ligneD;

    	    // Écrit le contenu dans le nouveau fichier
    	    try {
    	        Files.write(Paths.get(configFilePath), ligne.getBytes(), StandardOpenOption.CREATE);
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }

    	    popupWindow.close();
    	});
    	

    	Scene scene = new Scene(pane);
    	popupWindow.setScene(scene);
        popupWindow.showAndWait();
    	
    }
    
    
    @FXML
    private void handleGameVsAI(ActionEvent event) { 
        try {
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

            vbox.getChildren().addAll(listView, deleteButton);

            Scene scene = new Scene(vbox, 300, 400);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }		
}