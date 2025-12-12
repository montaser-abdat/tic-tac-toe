package edu.najah.ai.tictactoe.game;

public enum Player {
    X, O, EMPTY;
    
    public Player opponent() {
        return this == X ? O : (this == O ? X : EMPTY);
    }
    
    @Override
    public String toString() {
        return this == EMPTY ? "" : name();
    }
}
