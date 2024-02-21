package application.controller;
import ai.Config;
import ai.ConfigFileLoader;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;


public class Controller implements Initializable {
	
	@FXML
    private MenuItem settingsMenuItem;
	@FXML
    private ConfigFileLoader configFileLoader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configFileLoader = new ConfigFileLoader();
        configFileLoader.loadConfigFile("./resources/config.txt"); 

        settingsMenuItem.setOnAction(event -> handleSettingsAction());
    }
    @FXML
    private void handleSettingsAction() {
        StringBuilder contentBuilder = new StringBuilder();

        Collection<Config> configs = configFileLoader.getAllConfigs();
        
        if (!configs.isEmpty()) {
            for (Config config : configs) {
                String content = String.format("Level: %s\nHidden Layer Size: %d\nLearning Rate: %.2f\nNumber of Hidden Layers: %d\n\n",
                        config.level, config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers);
                contentBuilder.append(content);
            }
            
            showAlertWithConfigInfo(contentBuilder.toString());
        }
    }
    
    private void showAlertWithConfigInfo(String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Settings");
        alert.setHeaderText("Configuration Settings");
       
        alert.setContentText(content);
        alert.showAndWait();
    }

		
}



