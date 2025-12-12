package edu.najah.ai.tictactoe.gui;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import edu.najah.ai.tictactoe.ai.AlphaBeta;
import edu.najah.ai.tictactoe.ai.ClassicalEvaluator;
import edu.najah.ai.tictactoe.ai.Evaluator;
import edu.najah.ai.tictactoe.ai.MLEvaluator;
import edu.najah.ai.tictactoe.game.Board;
import edu.najah.ai.tictactoe.game.GameSettings;
import edu.najah.ai.tictactoe.game.Move;
import edu.najah.ai.tictactoe.game.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameController {
    
    @FXML
    private GridPane boardGrid;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Label infoLabel;
    
    @FXML
    private VBox debugPanel;
    
    @FXML
    private VBox debugScoresBox;
    
    @FXML
    private Label debugStatsLabel;
    
    private Board board;
    private GameSettings settings;
    private AlphaBeta aiEngine;
    private Button[][] cellButtons;
    private boolean gameOver;
    private Player currentPlayer;
    
    @FXML
    public void initialize() {
        settings = GameSettings.getInstance();
        cellButtons = new Button[3][3];
        
        // Create the board grid
        createBoardUI();
    }
    
    public void initializeGame() {
        board = new Board();
        gameOver = false;
        
        // Setup AI engine with selected evaluator
        Evaluator evaluator;
        if (settings.getEvaluationType() == GameSettings.EvaluationType.CLASSICAL) {
            evaluator = new ClassicalEvaluator();
        } else {
            evaluator = new MLEvaluator();
        }
        aiEngine = new AlphaBeta(evaluator);
        
        // Show/hide debug panel
        if (settings.isDebugMode()) {
            debugPanel.setVisible(true);
            debugPanel.setManaged(true);
        } else {
            debugPanel.setVisible(false);
            debugPanel.setManaged(false);
        }
        
        // Update info label
        updateInfoLabel();
        
        // Determine starting player
        currentPlayer = Player.X; // X always starts
        
        // If AI is X, make AI move first
        if (settings.getAiPlayer() == Player.X) {
            statusLabel.setText("AI is thinking...");
            // Use a separate thread to avoid blocking UI
            new Thread(() -> {
                try {
                    Thread.sleep(500); // Small delay for better UX
                    Platform.runLater(this::makeAIMove);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            statusLabel.setText("Your turn!");
        }
    }
    
    private void createBoardUI() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setPrefSize(120, 120);
                button.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; " +
                              "-fx-background-color: white; " +
                              "-fx-border-color: #bdc3c7; " +
                              "-fx-border-width: 2;");
                
                final int row = i;
                final int col = j;
                button.setOnAction(e -> handleCellClick(row, col));
                
                cellButtons[i][j] = button;
                boardGrid.add(button, j, i);
            }
        }
    }
    
    private void handleCellClick(int row, int col) {
        if (gameOver) {
            return;
        }
        
        if (currentPlayer != settings.getHumanPlayer()) {
            return; // Not human's turn
        }
        
        if (!board.isValidMove(row, col)) {
            return; // Invalid move
        }
        
        // Make human move
        makeMove(row, col, settings.getHumanPlayer());
        
        // Check if game is over
        if (checkGameOver()) {
            return;
        }
        
        // AI's turn
        currentPlayer = settings.getAiPlayer();
        statusLabel.setText("AI is thinking...");
        
        // Make AI move in a separate thread
        new Thread(() -> {
            try {
                Thread.sleep(300); // Small delay for better UX
                Platform.runLater(this::makeAIMove);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private void makeMove(int row, int col, Player player) {
        board.makeMove(row, col, player);
        updateCell(row, col, player);
    }
    
    private void makeAIMove() {
        if (gameOver) {
            return;
        }
        
        Move bestMove = aiEngine.findBestMove(board, settings.getAiPlayer(), settings.getDifficulty());
        
        if (bestMove != null) {
            makeMove(bestMove.getRow(), bestMove.getCol(), settings.getAiPlayer());
            
            // Update debug info if enabled
            if (settings.isDebugMode()) {
                updateDebugInfo();
            }
            
            // Check if game is over
            if (checkGameOver()) {
                return;
            }
            
            // Human's turn
            currentPlayer = settings.getHumanPlayer();
            statusLabel.setText("Your turn!");
        }
    }
    
    private void updateCell(int row, int col, Player player) {
        Button button = cellButtons[row][col];
        button.setText(player.toString());
        
        if (player == Player.X) {
            button.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; " +
                          "-fx-background-color: #e8f4f8; " +
                          "-fx-text-fill: #3498db; " +
                          "-fx-border-color: #bdc3c7; " +
                          "-fx-border-width: 2;");
        } else {
            button.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; " +
                          "-fx-background-color: #fef5e7; " +
                          "-fx-text-fill: #e74c3c; " +
                          "-fx-border-color: #bdc3c7; " +
                          "-fx-border-width: 2;");
        }
        
        button.setDisable(true);
    }
    
    private boolean checkGameOver() {
        Player winner = board.checkWinner();
        
        if (winner != Player.EMPTY) {
            gameOver = true;
            String winnerText = (winner == settings.getHumanPlayer()) ? "You win!" : "AI wins!";
            statusLabel.setText(winnerText);
            highlightWinner(winner);
            showGameOverDialog(winnerText);
            return true;
        } else if (board.isFull()) {
            gameOver = true;
            statusLabel.setText("It's a draw!");
            showGameOverDialog("It's a draw!");
            return true;
        }
        
        return false;
    }
    
    private void highlightWinner(Player winner) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board.getCell(i, 0) == winner && 
                board.getCell(i, 1) == winner && 
                board.getCell(i, 2) == winner) {
                highlightCells(i, 0, i, 1, i, 2);
                return;
            }
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board.getCell(0, j) == winner && 
                board.getCell(1, j) == winner && 
                board.getCell(2, j) == winner) {
                highlightCells(0, j, 1, j, 2, j);
                return;
            }
        }
        
        // Check diagonals
        if (board.getCell(0, 0) == winner && 
            board.getCell(1, 1) == winner && 
            board.getCell(2, 2) == winner) {
            highlightCells(0, 0, 1, 1, 2, 2);
            return;
        }
        
        if (board.getCell(0, 2) == winner && 
            board.getCell(1, 1) == winner && 
            board.getCell(2, 0) == winner) {
            highlightCells(0, 2, 1, 1, 2, 0);
        }
    }
    
    private void highlightCells(int r1, int c1, int r2, int c2, int r3, int c3) {
        String highlightStyle = "-fx-font-size: 36px; -fx-font-weight: bold; " +
                               "-fx-background-color: #2ecc71; " +
                               "-fx-text-fill: white; " +
                               "-fx-border-color: #27ae60; " +
                               "-fx-border-width: 3;";
        
        cellButtons[r1][c1].setStyle(highlightStyle);
        cellButtons[r2][c2].setStyle(highlightStyle);
        cellButtons[r3][c3].setStyle(highlightStyle);
    }
    
    private void showGameOverDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(message);
        alert.setContentText("What would you like to do?");
        
        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType returnToMenu = new ButtonType("Return to Menu");
        
        alert.getButtonTypes().setAll(playAgain, returnToMenu);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == playAgain) {
            handleRestart();
        } else {
            handleBackToMenu();
        }
    }
    
    private void updateDebugInfo() {
        debugScoresBox.getChildren().clear();
        
        List<Move> moveScores = aiEngine.getLastMoveScores();
        
        // Sort by score (descending)
        moveScores.sort((m1, m2) -> Integer.compare(m2.getScore(), m1.getScore()));
        
        for (Move move : moveScores) {
            Label label = new Label(move.toString());
            label.setStyle("-fx-font-size: 12px; -fx-padding: 3;");
            debugScoresBox.getChildren().add(label);
        }
        
        // Update stats
        String stats = String.format(
            "Nodes evaluated: %d\nBranches pruned: %d\nDepth limit: %d",
            aiEngine.getNodesEvaluated(),
            aiEngine.getPruneCount(),
            settings.getDifficulty().getDepth()
        );
        debugStatsLabel.setText(stats);
    }
    
    private void updateInfoLabel() {
        String info = String.format(
            "You: %s | AI: %s | Difficulty: %s | Evaluator: %s",
            settings.getHumanPlayer(),
            settings.getAiPlayer(),
            settings.getDifficulty(),
            settings.getEvaluationType()
        );
        infoLabel.setText(info);
    }
    
    @FXML
    private void handleRestart() {
        // Reset board
        board.reset();
        gameOver = false;
        
        // Clear UI
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cellButtons[i][j].setText("");
                cellButtons[i][j].setStyle("-fx-font-size: 36px; -fx-font-weight: bold; " +
                                          "-fx-background-color: white; " +
                                          "-fx-border-color: #bdc3c7; " +
                                          "-fx-border-width: 2;");
                cellButtons[i][j].setDisable(false);
            }
        }
        
        // Clear debug info
        if (settings.isDebugMode()) {
            debugScoresBox.getChildren().clear();
            debugStatsLabel.setText("");
        }
        
        // Reset current player
        currentPlayer = Player.X;
        
        // If AI is X, make AI move first
        if (settings.getAiPlayer() == Player.X) {
            statusLabel.setText("AI is thinking...");
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    Platform.runLater(this::makeAIMove);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            statusLabel.setText("Your turn!");
        }
    }
    
    @FXML
    private void handleBackToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) boardGrid.getScene().getWindow();
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.setTitle("Tic-Tac-Toe - Main Menu");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
