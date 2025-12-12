package edu.najah.ai.tictactoe.gui;

import edu.najah.ai.tictactoe.ai.Difficulty;
import edu.najah.ai.tictactoe.game.GameSettings;
import edu.najah.ai.tictactoe.game.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {
    
    @FXML
    private RadioButton playerXRadio;
    
    @FXML
    private RadioButton playerORadio;
    
    @FXML
    private ToggleGroup playerGroup;
    
    @FXML
    private ComboBox<String> difficultyCombo;
    
    @FXML
    private ComboBox<String> evaluationCombo;
    
    @FXML
    private CheckBox debugModeCheck;
    
    private GameSettings settings;
    
    @FXML
    public void initialize() {
        settings = GameSettings.getInstance();
        
        // Populate difficulty combo box
        difficultyCombo.getItems().addAll("Easy", "Normal", "Hard");
        
        // Populate evaluation combo box
        evaluationCombo.getItems().addAll("Classical", "ML");
        
        // Load current settings
        loadSettings();
    }
    
    private void loadSettings() {
        // Set player selection
        if (settings.getHumanPlayer() == Player.X) {
            playerXRadio.setSelected(true);
        } else {
            playerORadio.setSelected(true);
        }
        
        // Set difficulty
        switch (settings.getDifficulty()) {
            case EASY:
                difficultyCombo.setValue("Easy");
                break;
            case NORMAL:
                difficultyCombo.setValue("Normal");
                break;
            case HARD:
                difficultyCombo.setValue("Hard");
                break;
        }
        
        // Set evaluation type
        if (settings.getEvaluationType() == GameSettings.EvaluationType.CLASSICAL) {
            evaluationCombo.setValue("Classical");
        } else {
            evaluationCombo.setValue("ML");
        }
        
        // Set debug mode
        debugModeCheck.setSelected(settings.isDebugMode());
    }
    
    @FXML
    private void handleSave() {
        // Save player selection
        if (playerXRadio.isSelected()) {
            settings.setHumanPlayer(Player.X);
        } else {
            settings.setHumanPlayer(Player.O);
        }
        
        // Save difficulty
        String difficulty = difficultyCombo.getValue();
        if (difficulty != null) {
            switch (difficulty) {
                case "Easy":
                    settings.setDifficulty(Difficulty.EASY);
                    break;
                case "Normal":
                    settings.setDifficulty(Difficulty.NORMAL);
                    break;
                case "Hard":
                    settings.setDifficulty(Difficulty.HARD);
                    break;
            }
        }
        
        // Save evaluation type
        String evaluation = evaluationCombo.getValue();
        if (evaluation != null) {
            if (evaluation.equals("Classical")) {
                settings.setEvaluationType(GameSettings.EvaluationType.CLASSICAL);
            } else {
                settings.setEvaluationType(GameSettings.EvaluationType.ML);
            }
        }
        
        // Save debug mode
        settings.setDebugMode(debugModeCheck.isSelected());
        
        // Show confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings Saved");
        alert.setHeaderText(null);
        alert.setContentText("Settings have been saved successfully!");
        alert.showAndWait();
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) difficultyCombo.getScene().getWindow();
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.setTitle("Tic-Tac-Toe - Main Menu");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
