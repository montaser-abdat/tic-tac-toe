package edu.najah.ai.tictactoe.ai;

import edu.najah.ai.tictactoe.game.Board;
import edu.najah.ai.tictactoe.game.Player;
import edu.najah.ai.tictactoe.ml.FeatureExtractor;
import edu.najah.ai.tictactoe.ml.TrainedModel;

public class MLEvaluator implements Evaluator {
    
    private static final int WIN_SCORE = 1000;
    private static final int LOSS_SCORE = -1000;
    
    @Override
    public int evaluate(Board board, Player maximizingPlayer) {
        Player winner = board.checkWinner();
        
        // Terminal states override ML prediction
        if (winner == maximizingPlayer) {
            return WIN_SCORE;
        } else if (winner == maximizingPlayer.opponent()) {
            return LOSS_SCORE;
        } else if (board.isFull()) {
            return 0; // Draw
        }
        
        // Extract features and predict
        // The model is trained to predict X wins (+) vs O wins (-)
        double[] features = FeatureExtractor.extract(board, maximizingPlayer);
        double mlScore = TrainedModel.predict(features);
        
        // The model always predicts from X's perspective:
        // Positive score = X is winning, Negative score = O is winning
        // If the AI is playing as O, we need to negate the score
        if (maximizingPlayer == Player.O) {
            mlScore = -mlScore;
        }
        
        // Scale the score to a reasonable range for minimax
        // The raw ML score can be any value, so we scale it
        return (int) (mlScore * 10);
    }
}
