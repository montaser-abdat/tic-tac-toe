package edu.najah.ai.tictactoe.ai;

import edu.najah.ai.tictactoe.game.Board;
import edu.najah.ai.tictactoe.game.Player;

public interface Evaluator {
    /**
     * Evaluates the board state from the perspective of the maximizing player.
     * Positive scores favor X, negative scores favor O.
     * 
     * @param board the current board state
     * @param maximizingPlayer the player trying to maximize the score
     * @return evaluation score
     */
    int evaluate(Board board, Player maximizingPlayer);
}
