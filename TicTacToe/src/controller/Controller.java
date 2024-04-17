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

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


public class Controller implements Initializable {
	
	@FXML
    private MenuItem settingsMenuItem;
	
	@FXML 
	private MenuItem modelsMenuItem; 
	 
	@FXML
	private Button gameVsAIBtn;
	
	@FXML
	private Button gameVsHumanBtn;
	
    private MediaPlayer clickMusic; // MediaPlayer for button click sound

	 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String musicFile = "src/resources/sounds/bell_sound.mp3";

        // Create a Media object for the specified file
        Media music = new Media(new File(musicFile).toURI().toString());

        // Create a MediaPlayer object and attach the Media object
        clickMusic = new MediaPlayer(music);
        
        // Setting up button animations
        buttonAnimation(gameVsAIBtn);
        buttonAnimation(gameVsHumanBtn);
    }

    private void buttonAnimation(Button button) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
        st.setFromX(1);
        st.setToX(1.1);
        st.setFromY(1);
        st.setToY(1.1);
        st.setAutoReverse(true);

        button.setOnMouseEntered(e -> st.playFromStart());
        button.setOnMouseExited(e -> st.stop());
    }
    
    @FXML
    private void handleGameVsAI(ActionEvent event) { 
        try {   
        	// Play sound effect
            if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                clickMusic.stop();
                clickMusic.seek(clickMusic.getStartTime());
            }
            clickMusic.play();
            
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
    		// Play sound effect
            if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                clickMusic.stop();
                clickMusic.seek(clickMusic.getStartTime());
            }
            clickMusic.play();
                		
    		// Load the FXML for the LearningAIController scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameVsHumanView.fxml")); 
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
    public void settingsPopUp() {
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        configFileLoader.loadConfigFile("./resources/config.txt");

        Config levelF = configFileLoader.get("F");
        Config levelM = configFileLoader.get("M");
        Config levelD = configFileLoader.get("D");

        VBox vbox = new VBox(30); // Main layout with spacing
        vbox.setStyle("-fx-background-color: #cde4b3; -fx-background-radius: 50px;");
        vbox.setAlignment(Pos.CENTER);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Configuration");
        
        UnaryOperator<Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        // Easy settings HBox
        HBox easyBox = new HBox(10);
        easyBox.setAlignment(Pos.CENTER); 
        Label easyLabel = new Label("Easy:");
        TextField hiddenLayerSizeF = createNumericTextField(Integer.toString(levelF.hiddenLayerSize), filter);
        TextField learningRateF = createNumericTextField(Double.toString(levelF.learningRate), filter);
        TextField numberOfhiddenLayersF = createNumericTextField(Integer.toString(levelF.numberOfhiddenLayers),
                filter);
        easyBox.getChildren().addAll(easyLabel, hiddenLayerSizeF, learningRateF, numberOfhiddenLayersF);

        // Medium settings HBox
        HBox mediumBox = new HBox(10);
        mediumBox.setAlignment(Pos.CENTER); 
        Label mediumLabel = new Label("Medium:");
        TextField hiddenLayerSizeM = createNumericTextField(Integer.toString(levelM.hiddenLayerSize), filter);
        TextField learningRateM = createNumericTextField(Double.toString(levelM.learningRate), filter);
        TextField numberOfhiddenLayersM = createNumericTextField(Integer.toString(levelM.numberOfhiddenLayers), filter);
        mediumBox.getChildren().addAll(mediumLabel, hiddenLayerSizeM, learningRateM, numberOfhiddenLayersM);

        // Difficult settings HBox
        HBox difficultBox = new HBox(10);
        difficultBox.setAlignment(Pos.CENTER); 
        Label difficultLabel = new Label("Difficult:");
        TextField hiddenLayerSizeD = createNumericTextField(Integer.toString(levelD.hiddenLayerSize), filter);
        TextField learningRateD = createNumericTextField(Double.toString(levelD.learningRate), filter);
        TextField numberOfhiddenLayersD = createNumericTextField(Integer.toString(levelD.numberOfhiddenLayers), filter);
        difficultBox.getChildren().addAll(difficultLabel, hiddenLayerSizeD, learningRateD, numberOfhiddenLayersD);

        Button updateBtn = new Button("Update");
        updateBtn.setLayoutX(250);
        updateBtn.setLayoutY(300);
        updateBtn.setId("btn");
        buttonAnimation(updateBtn);

        updateBtn.setOnAction(event -> {
        	if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
        		clickMusic.stop();
        		clickMusic.seek(clickMusic.getStartTime());
            }
        	clickMusic.play();
        	
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

            stage.close();
        });
        
        // Layout for placing the button in the bottom right
        StackPane buttonLayout = new StackPane(updateBtn);
        buttonLayout.setAlignment(Pos.BOTTOM_RIGHT);  // Align button to the bottom right
        buttonLayout.setPadding(new Insets(0, 20, 20, 0));  // Padding to keep button off the edges

        vbox.getChildren().addAll(easyBox, mediumBox, difficultBox, buttonLayout);
        
        Scene scene = new Scene(vbox, 400, 350);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/application/Main.css").toExternalForm()); 
        stage.showAndWait();
    }

    private TextField createNumericTextField(String text, UnaryOperator<Change> filter) {
        TextField textField = new TextField(text);
        textField.setPrefWidth(75);
        textField.setTextFormatter(new TextFormatter<>(filter));
        return textField;
    }
     
    @FXML
    public void aboutPopUp() {
        Label aboutText = new Label("Frog Tic Tac Toe v1.0 \nDeveloped by Hammoudi Fatima & \nOuld Ammar Sofia"
        		+ "\nProject by the UE Professor Mr Morchid \nAll rights reserved.");
        aboutText.setStyle("-fx-padding: 20; -fx-text-alignment: center; -fx-text-fill: #836953; -fx-font-size: 14px; -fx-font-family: Helvetica;");
        		
        VBox layout = new VBox(10);
        layout.getChildren().addAll(aboutText);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #cde4b3; -fx-background-radius: 20px;");

        Scene scene = new Scene(layout, 350, 250);
        scene.getStylesheets().add(getClass().getResource("/application/Main.css").toExternalForm()); 
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        // Adding a close button for good UX
        Button closeBtn = new Button("Close");
        closeBtn.setId("btn");
        closeBtn.setOnAction(event -> {
	        if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
	    		clickMusic.stop();
	    		clickMusic.seek(clickMusic.getStartTime());
	        }
	    	clickMusic.play();
	        stage.close();
        });
        layout.getChildren().add(closeBtn);

        stage.showAndWait();
    } 
   
    @FXML
    public void modelPopUp(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Model Files");

            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(10));

            ListView<CheckBox> listView = new ListView<>();
            File dir = new File("./resources/models/");
            File[] files = dir.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isFile()) {
                        CheckBox checkBox = new CheckBox(file.getName());
                        listView.getItems().add(checkBox);
                    }
                }
            } else {
                Label emptyState = new Label("No models available.\n"
                		+ "Please start the Human vs AI game to train a model \nand see it listed here.");
                
                emptyState.setStyle("-fx-padding: 100 10 0 0; -fx-text-alignment: center; -fx-text-fill: #836953; -fx-font-size: 12;");
                vbox.getChildren().add(emptyState);  // Add the label to vbox if no files found
            }

            Button deleteBtn = new Button("Delete");
            Button selectAllBtn = new Button("Select All");
            deleteBtn.setId("btn");
            selectAllBtn.setId("btn");

            buttonAnimation(deleteBtn);
            buttonAnimation(selectAllBtn);

            deleteBtn.setOnAction(e -> {
            	if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            		clickMusic.stop();
            		clickMusic.seek(clickMusic.getStartTime());
                }
            	clickMusic.play();
            	
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

            selectAllBtn.setOnAction(e -> {
            	if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            		clickMusic.stop();
            		clickMusic.seek(clickMusic.getStartTime());
                }
            	clickMusic.play();
            	
                for (CheckBox checkBox : listView.getItems()) {
                    checkBox.setSelected(true);
                }
            });

            // Only add the listView and buttons if files were found
            if (!listView.getItems().isEmpty()) {
                HBox hboxButtons = new HBox(10); // Spacing between buttons
                hboxButtons.getChildren().addAll(deleteBtn, selectAllBtn);
                vbox.getChildren().addAll(listView, hboxButtons);
            }

            Scene scene = new Scene(vbox, 350, 300);
            scene.getRoot().setStyle("-fx-background-color: #b3cf99;");
            scene.getStylesheets().add(getClass().getResource("/application/Main.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}