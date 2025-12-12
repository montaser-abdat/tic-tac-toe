package edu.najah.ai.tictactoe.ai;

public enum Difficulty {
    EASY(1),     // Very shallow search - makes obvious mistakes
    NORMAL(3),   // Moderate search - decent but beatable
    HARD(9);     // Full depth for 3x3 board - plays perfectly
    
    private final int depth;
    
    Difficulty(int depth) {
        this.depth = depth;
    }
    
    public int getDepth() {
        return depth;
    }
}
