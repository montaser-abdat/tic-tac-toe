package edu.najah.ai.tictactoe.game;

import edu.najah.ai.tictactoe.ai.Difficulty;

/**
 * Manages game settings selected by the user.
 */
public class GameSettings {
    private static GameSettings instance;
    
    private Player humanPlayer;
    private Player aiPlayer;
    private Difficulty difficulty;
    private EvaluationType evaluationType;
    private boolean debugMode;
    
    public enum EvaluationType {
        CLASSICAL, ML
    }
    
    private GameSettings() {
        // Default settings
        this.humanPlayer = Player.X;
        this.aiPlayer = Player.O;
        this.difficulty = Difficulty.NORMAL;
        this.evaluationType = EvaluationType.CLASSICAL;
        this.debugMode = false;
    }
    
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }
    
    public Player getHumanPlayer() {
        return humanPlayer;
    }
    
    public void setHumanPlayer(Player humanPlayer) {
        this.humanPlayer = humanPlayer;
        this.aiPlayer = humanPlayer.opponent();
    }
    
    public Player getAiPlayer() {
        return aiPlayer;
    }
    
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    public EvaluationType getEvaluationType() {
        return evaluationType;
    }
    
    public void setEvaluationType(EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
    }
    
    public boolean isDebugMode() {
        return debugMode;
    }
    
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
