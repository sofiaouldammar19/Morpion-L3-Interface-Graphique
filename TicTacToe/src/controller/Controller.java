package controller;

import tools.Config;
import tools.ConfigFileLoader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
import javafx.scene.layout.BorderPane;
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

/**
 * The Controller class is responsible for handling user interactions with the
 * GUI.
 * It contains references to various UI components and implements the
 * Initializable interface,
 * which means it performs certain actions upon the initialization of the GUI.
 *
 */
public class Controller implements Initializable {

    // MenuItem for accessing the settings of the application
    @FXML
    private MenuItem settingsMenuItem;

    // MenuItem for accessing the models of the application
    @FXML
    private MenuItem modelsMenuItem;

    // Button for starting a game against the AI
    @FXML
    private Button gameVsAIBtn;

    // Button for starting a game against a human player
    @FXML
    private Button gameVsHumanBtn;

    // MediaPlayer object for playing sound effects
    private MediaPlayer clickMusic;

    /**
     * This method is called when the Controller class is initialized.
     * 
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String musicFile = "src/resources/sounds/click.mp3";

        // Create a Media object for the specified file
        Media music = new Media(new File(musicFile).toURI().toString());

        // Create a MediaPlayer object and attach the Media object
        clickMusic = new MediaPlayer(music);

        // Setting up button animations
        buttonAnimation(gameVsAIBtn);
        buttonAnimation(gameVsHumanBtn);
    }

    /**
     * Handles the action to start a game against the AI. This method triggers
     * the scene change to the AI level choice view.
     *
     * @param event The event that triggered the method.
     */
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

    /**
     * Handles the action to start a game against another human player. This method
     * triggers the scene change to the human vs. human game view.
     *
     * @param event The event that triggered the method.
     */
    @FXML
    private void handleGameVsHuman(ActionEvent event) {
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

    /**
     * Displays a configuration pop up for setting preferences. The settings include
     * options for different difficulty levels which affect gameplay against the AI.
     */
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
        easyLabel.setStyle(
                "-fx-padding: 20; -fx-text-alignment: center; -fx-text-fill: #836953; -fx-font-size: 12px; -fx-font-family: Helvetica;");
        TextField hiddenLayerSizeF = createNumericTextField(Integer.toString(levelF.hiddenLayerSize), filter);
        TextField learningRateF = createNumericTextField(Double.toString(levelF.learningRate), filter);
        TextField numberOfhiddenLayersF = createNumericTextField(Integer.toString(levelF.numberOfhiddenLayers),
                filter);
        easyBox.getChildren().addAll(easyLabel, hiddenLayerSizeF, learningRateF, numberOfhiddenLayersF);

        // Medium settings HBox
        HBox mediumBox = new HBox(10);
        mediumBox.setAlignment(Pos.CENTER);
        Label mediumLabel = new Label("Medium:");
        mediumLabel.setStyle(
                "-fx-text-alignment: center; -fx-text-fill: #836953; -fx-font-size: 12px; -fx-font-family: Helvetica;");
        TextField hiddenLayerSizeM = createNumericTextField(Integer.toString(levelM.hiddenLayerSize), filter);
        TextField learningRateM = createNumericTextField(Double.toString(levelM.learningRate), filter);
        TextField numberOfhiddenLayersM = createNumericTextField(Integer.toString(levelM.numberOfhiddenLayers), filter);
        mediumBox.getChildren().addAll(mediumLabel, hiddenLayerSizeM, learningRateM, numberOfhiddenLayersM);

        // Difficult settings HBox
        HBox difficultBox = new HBox(10);
        difficultBox.setAlignment(Pos.CENTER);
        Label difficultLabel = new Label("Difficult:");
        difficultLabel.setStyle(
                "-fx-text-alignment: center; -fx-text-fill: #836953; -fx-font-size: 12px; -fx-font-family: Helvetica;");
        TextField hiddenLayerSizeD = createNumericTextField(Integer.toString(levelD.hiddenLayerSize), filter);
        TextField learningRateD = createNumericTextField(Double.toString(levelD.learningRate), filter);
        TextField numberOfhiddenLayersD = createNumericTextField(Integer.toString(levelD.numberOfhiddenLayers), filter);
        difficultBox.getChildren().addAll(difficultLabel, hiddenLayerSizeD, learningRateD, numberOfhiddenLayersD);

        Button updateBtn = new Button("Update");
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
        buttonLayout.setAlignment(Pos.BOTTOM_RIGHT); // Align button to the bottom right
        buttonLayout.setPadding(new Insets(0, 20, 20, 0)); // Padding to keep button off the edges

        vbox.getChildren().addAll(easyBox, mediumBox, difficultBox, buttonLayout);

        Scene scene = new Scene(vbox, 350, 320);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/application/Main.css").toExternalForm());
        stage.showAndWait();
    }

    /**
     * Displays an informational popup about the game, including version and authorship.
     */
    @FXML
    public void aboutPopUp() {
        Label aboutText = new Label("Frog Tic Tac Toe v1.0 \nDeveloped by Hammoudi Fatima & \nOuld Ammar Sofia"
                + "\nProject by the UE Professor Mr Morchid \nAll rights reserved.");
        aboutText.setStyle(
                "-fx-padding: 20; -fx-text-alignment: center; -fx-text-fill: #836953; -fx-font-size: 14px; -fx-font-family: Helvetica;");

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
        buttonAnimation(closeBtn);
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

    /**
     * Displays a popup allowing the user to manage AI model files. This includes
     * options to delete selected models or select all models.
     *
     * @param event The event that triggered the method.
     */
    @FXML
    public void modelPopUp(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT); // Set the stage style to transparent

            BorderPane borderPane = new BorderPane();
            borderPane.setPadding(new Insets(10));
            borderPane.setStyle("-fx-background-color: #cde4b3; -fx-background-radius: 20px;"); 

            VBox vbox = new VBox(10);
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
                emptyState.setStyle("-fx-padding: 50 10 0 0; -fx-text-alignment: center; -fx-text-fill: #836953; -fx-font-size: 12px;");
                emptyState.setWrapText(true);

                Button closeButton = new Button("Close");
                closeButton.setId("btn");
                buttonAnimation(closeButton);
                
                closeButton.setOnAction(e ->{
                    if (clickMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                        clickMusic.stop();
                        clickMusic.seek(clickMusic.getStartTime());
                    }
                    clickMusic.play();
                    
                    stage.close();
                });

                vbox.getChildren().add(emptyState); // Add the label to vbox if no files found
                borderPane.setBottom(closeButton); // Add the close button to the bottom of the border pane
                BorderPane.setAlignment(closeButton, Pos.BOTTOM_RIGHT); // Align the close button to the right
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
                
                listView.getItems().removeIf(checkBox -> {
                    if (checkBox.isSelected()) {
                        File fileToDelete = new File(dir, checkBox.getText());
                        fileToDelete.delete();
                        return true;
                    }
                    return false;
                });
                stage.close();
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
            
            if (!listView.getItems().isEmpty()) {
                HBox hboxButtons = new HBox(10); // Spacing between buttons
                hboxButtons.getChildren().addAll(deleteBtn, selectAllBtn);
                vbox.getChildren().addAll(listView, hboxButtons);
            }

            borderPane.setCenter(vbox); // Add the vbox to the center of the border pane

            Scene scene = new Scene(borderPane, 350, 300);
            scene.getStylesheets().add(getClass().getResource("/application/Main.css").toExternalForm());            
            scene.setFill(Color.TRANSPARENT); // Set the scene fill to transparent
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Animates a button to slightly increase in size when hovered over.
     *
     * @param button The button to animate.
     */
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

    /**
     * Creates a TextField that only accepts numeric input.
     *
     * @param text The initial text for the TextField.
     * @param filter A filter that ensures only numeric input is accepted.
     * @return TextField The newly created TextField with the specified properties.
     */
    private TextField createNumericTextField(String text, UnaryOperator<Change> filter) {
        TextField textField = new TextField(text);
        textField.setPrefWidth(75);
        textField.setTextFormatter(new TextFormatter<>(filter));
        return textField;
    }
}