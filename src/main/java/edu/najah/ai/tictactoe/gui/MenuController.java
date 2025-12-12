package edu.najah.ai.tictactoe.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    
    @FXML
    private Button startButton;
    
    @FXML
    private Button settingsButton;
    
    @FXML
    private Button helpButton;
    
    @FXML
    private Button exitButton;
    
    @FXML
    private void handleStartGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) startButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Tic-Tac-Toe - Game");
            
            // Initialize the game
            GameController controller = loader.getController();
            controller.initializeGame();
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load game screen: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/settings.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) settingsButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.setTitle("Tic-Tac-Toe - Settings");
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load settings screen: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Tic-Tac-Toe AI - Help");
        alert.setContentText(
            "How to Play:\n\n" +
            "1. Click 'Settings' to choose your symbol (X or O), difficulty level, and evaluation function.\n\n" +
            "2. Click 'Start Game' to play against the AI.\n\n" +
            "3. Click on an empty cell to make your move.\n\n" +
            "4. The AI uses Alpha-Beta pruning to find the best move.\n\n" +
            "5. Enable Debug Mode to see AI evaluation scores for each possible move.\n\n" +
            "Difficulty Levels:\n" +
            "- Easy: Limited search depth (easier to beat)\n" +
            "- Normal: Moderate search depth (challenging)\n" +
            "- Hard: Full search depth (perfect play)\n\n" +
            "Evaluation Functions:\n" +
            "- Classical: Heuristic-based evaluation\n" +
            "- ML: Machine learning-based evaluation"
        );
        alert.showAndWait();
    }
    
    @FXML
    private void handleExit() {
        Platform.exit();
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
