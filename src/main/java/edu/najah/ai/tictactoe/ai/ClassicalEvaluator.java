package edu.najah.ai.tictactoe.ai;

import edu.najah.ai.tictactoe.game.Board;
import edu.najah.ai.tictactoe.game.Player;

public class ClassicalEvaluator implements Evaluator {
    
    private static final int WIN_SCORE = 1000;
    private static final int LOSS_SCORE = -1000;
    private static final int CENTER_SCORE = 30;
    private static final int CORNER_SCORE = 20;
    private static final int TWO_IN_ROW_SCORE = 50;
    
    @Override
    public int evaluate(Board board, Player maximizingPlayer) {
        Player winner = board.checkWinner();
        
        // Terminal states
        if (winner == maximizingPlayer) {
            return WIN_SCORE;
        } else if (winner == maximizingPlayer.opponent()) {
            return LOSS_SCORE;
        } else if (board.isFull()) {
            return 0; // Draw
        }
        
        // Non-terminal evaluation
        int score = 0;
        
        // Center control
        if (board.getCell(1, 1) == maximizingPlayer) {
            score += CENTER_SCORE;
        } else if (board.getCell(1, 1) == maximizingPlayer.opponent()) {
            score -= CENTER_SCORE;
        }
        
        // Corner control
        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        for (int[] corner : corners) {
            if (board.getCell(corner[0], corner[1]) == maximizingPlayer) {
                score += CORNER_SCORE;
            } else if (board.getCell(corner[0], corner[1]) == maximizingPlayer.opponent()) {
                score -= CORNER_SCORE;
            }
        }
        
        // Two in a row/column/diagonal (potential wins)
        score += evaluateLines(board, maximizingPlayer);
        
        return score;
    }
    
    private int evaluateLines(Board board, Player player) {
        int score = 0;
        
        // Check all rows
        for (int i = 0; i < 3; i++) {
            score += evaluateLine(
                board.getCell(i, 0),
                board.getCell(i, 1),
                board.getCell(i, 2),
                player
            );
        }
        
        // Check all columns
        for (int j = 0; j < 3; j++) {
            score += evaluateLine(
                board.getCell(0, j),
                board.getCell(1, j),
                board.getCell(2, j),
                player
            );
        }
        
        // Check diagonals
        score += evaluateLine(
            board.getCell(0, 0),
            board.getCell(1, 1),
            board.getCell(2, 2),
            player
        );
        
        score += evaluateLine(
            board.getCell(0, 2),
            board.getCell(1, 1),
            board.getCell(2, 0),
            player
        );
        
        return score;
    }
    
    private int evaluateLine(Player p1, Player p2, Player p3, Player player) {
        int playerCount = 0;
        int opponentCount = 0;
        int emptyCount = 0;
        
        Player opponent = player.opponent();
        
        if (p1 == player) playerCount++;
        else if (p1 == opponent) opponentCount++;
        else emptyCount++;
        
        if (p2 == player) playerCount++;
        else if (p2 == opponent) opponentCount++;
        else emptyCount++;
        
        if (p3 == player) playerCount++;
        else if (p3 == opponent) opponentCount++;
        else emptyCount++;
        
        // Two of mine and one empty = good
        if (playerCount == 2 && emptyCount == 1) {
            return TWO_IN_ROW_SCORE;
        }
        
        // Two of opponent and one empty = bad (need to block)
        if (opponentCount == 2 && emptyCount == 1) {
            return -TWO_IN_ROW_SCORE;
        }
        
        // One of mine and two empty = slight advantage
        if (playerCount == 1 && emptyCount == 2) {
            return 10;
        }
        
        // One of opponent and two empty = slight disadvantage
        if (opponentCount == 1 && emptyCount == 2) {
            return -10;
        }
        
        return 0;
    }
}
