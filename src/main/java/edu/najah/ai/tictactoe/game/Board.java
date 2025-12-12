package edu.najah.ai.tictactoe.game;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Player[][] grid;
    private static final int SIZE = 3;
    
    public Board() {
        grid = new Player[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = Player.EMPTY;
            }
        }
    }
    
    public Board(Board other) {
        grid = new Player[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = other.grid[i][j];
            }
        }
    }
    
    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && grid[row][col] == Player.EMPTY;
    }
    
    public void makeMove(int row, int col, Player player) {
        if (isValidMove(row, col)) {
            grid[row][col] = player;
        }
    }
    
    public void undoMove(int row, int col) {
        grid[row][col] = Player.EMPTY;
    }
    
    public Player getCell(int row, int col) {
        return grid[row][col];
    }
    
    public List<Move> getAvailableMoves() {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == Player.EMPTY) {
                    moves.add(new Move(i, j));
                }
            }
        }
        return moves;
    }
    
    public Player checkWinner() {
        // Check rows
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][0] != Player.EMPTY && 
                grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2]) {
                return grid[i][0];
            }
        }
        
        // Check columns
        for (int j = 0; j < SIZE; j++) {
            if (grid[0][j] != Player.EMPTY && 
                grid[0][j] == grid[1][j] && grid[1][j] == grid[2][j]) {
                return grid[0][j];
            }
        }
        
        // Check diagonals
        if (grid[0][0] != Player.EMPTY && 
            grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) {
            return grid[0][0];
        }
        
        if (grid[0][2] != Player.EMPTY && 
            grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]) {
            return grid[0][2];
        }
        
        return Player.EMPTY;
    }
    
    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == Player.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isTerminal() {
        return checkWinner() != Player.EMPTY || isFull();
    }
    
    public int getSize() {
        return SIZE;
    }
    
    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = Player.EMPTY;
            }
        }
    }
}
