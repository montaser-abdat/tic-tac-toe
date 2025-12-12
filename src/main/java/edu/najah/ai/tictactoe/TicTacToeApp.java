package edu.najah.ai.tictactoe;

import edu.najah.ai.tictactoe.ml.TrainedModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TicTacToeApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize and train the ML model at startup
        System.out.println("=== Initializing ML Model ===");
        TrainedModel.initialize();
        System.out.println("=== ML Model Ready ===\n");
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Tic-Tac-Toe AI");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
