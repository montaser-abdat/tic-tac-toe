package edu.najah.ai.tictactoe.ai;

import edu.najah.ai.tictactoe.game.Board;
import edu.najah.ai.tictactoe.game.Move;
import edu.najah.ai.tictactoe.game.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Alpha-Beta pruning implementation for Tic-Tac-Toe AI.
 */
public class AlphaBeta {
    
    private Evaluator evaluator;
    private int nodesEvaluated;
    private int pruneCount;
    private List<Move> lastMoveScores; // For debug mode
    
    public AlphaBeta(Evaluator evaluator) {
        this.evaluator = evaluator;
        this.nodesEvaluated = 0;
        this.pruneCount = 0;
        this.lastMoveScores = new ArrayList<>();
    }
    
    /**
     * Finds the best move using Alpha-Beta pruning.
     * 
     * @param board current board state
     * @param player the AI player
     * @param difficulty difficulty level
     * @return the best move
     */
    public Move findBestMove(Board board, Player player, Difficulty difficulty) {
        nodesEvaluated = 0;
        pruneCount = 0;
        lastMoveScores.clear();
        
        List<Move> availableMoves = board.getAvailableMoves();
        
        if (availableMoves.isEmpty()) {
            return null;
        }
        
        // Add randomness for easier difficulties to make them play suboptimally
        if (difficulty == Difficulty.EASY) {
            // 60% chance to make a random move
            if (Math.random() < 0.60) {
                Move randomMove = availableMoves.get((int)(Math.random() * availableMoves.size()));
                randomMove.setScore(0);
                return randomMove;
            }
        } else if (difficulty == Difficulty.NORMAL) {
            // 30% chance to make a random move
            if (Math.random() < 0.30) {
                Move randomMove = availableMoves.get((int)(Math.random() * availableMoves.size()));
                randomMove.setScore(0);
                return randomMove;
            }
        }
        
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        
        // Evaluate all possible moves
        for (Move move : availableMoves) {
            board.makeMove(move.getRow(), move.getCol(), player);
            
            int score = alphabeta(board, player, alpha, beta, 0, difficulty.getDepth(), false);
            
            board.undoMove(move.getRow(), move.getCol());
            
            move.setScore(score);
            lastMoveScores.add(new Move(move.getRow(), move.getCol(), score));
            
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
            
            alpha = Math.max(alpha, bestScore);
        }
        
        if (bestMove != null) {
            bestMove.setScore(bestScore);
        }
        
        return bestMove;
    }
    
    /**
     * Alpha-Beta pruning algorithm.
     * 
     * @param board current board state
     * @param maximizingPlayer the AI player (used for evaluation)
     * @param alpha alpha value for pruning
     * @param beta beta value for pruning
     * @param depth current depth in the search tree
     * @param maxDepth maximum depth to search
     * @param maximizingPlayerTurn true if it's the maximizing player's turn
     * @return the heuristic value of the node
     */
    private int alphabeta(Board board, Player maximizingPlayer, int alpha, int beta, int depth, int maxDepth, boolean maximizingPlayerTurn) {
        nodesEvaluated++;
        
        // Terminal test or depth limit
        if (depth >= maxDepth || board.isTerminal()) {
            return evaluator.evaluate(board, maximizingPlayer);
        }
        
        List<Move> moves = board.getAvailableMoves();
        
        if (maximizingPlayerTurn) {
            // Maximizing player
            int v = Integer.MIN_VALUE;
            
            for (Move move : moves) {
                board.makeMove(move.getRow(), move.getCol(), maximizingPlayer);
                
                v = Math.max(v, alphabeta(board, maximizingPlayer, alpha, beta, depth + 1, maxDepth, false));
                
                board.undoMove(move.getRow(), move.getCol());
                
                alpha = Math.max(alpha, v);
                
                if (beta <= alpha) {
                    pruneCount++;
                    break; // Beta cut-off
                }
            }
            
            return v;
        } else {
            // Minimizing player
            int v = Integer.MAX_VALUE;
            Player minimizingPlayer = maximizingPlayer.opponent();
            
            for (Move move : moves) {
                board.makeMove(move.getRow(), move.getCol(), minimizingPlayer);
                
                v = Math.min(v, alphabeta(board, maximizingPlayer, alpha, beta, depth + 1, maxDepth, true));
                
                board.undoMove(move.getRow(), move.getCol());
                
                beta = Math.min(beta, v);
                
                if (beta <= alpha) {
                    pruneCount++;
                    break; // Alpha cut-off
                }
            }
            
            return v;
        }
    }
    
    public int getNodesEvaluated() {
        return nodesEvaluated;
    }
    
    public int getPruneCount() {
        return pruneCount;
    }
    
    public List<Move> getLastMoveScores() {
        return new ArrayList<>(lastMoveScores);
    }
    
    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }
}
