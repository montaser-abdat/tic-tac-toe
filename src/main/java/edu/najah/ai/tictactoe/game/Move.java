package edu.najah.ai.tictactoe.game;

public class Move {
    private final int row;
    private final int col;
    private int score;
    
    public Move(int row, int col) {
        this.row = row;
        this.col = col;
        this.score = 0;
    }
    
    public Move(int row, int col, int score) {
        this.row = row;
        this.col = col;
        this.score = score;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    @Override
    public String toString() {
        return "(" + row + "," + col + "): " + score;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return row == move.row && col == move.col;
    }
    
    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}
