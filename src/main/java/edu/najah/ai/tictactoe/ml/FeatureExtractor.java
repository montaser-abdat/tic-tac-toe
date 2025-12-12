package edu.najah.ai.tictactoe.ml;

import edu.najah.ai.tictactoe.game.Board;
import edu.najah.ai.tictactoe.game.Player;

public class FeatureExtractor {
    
    /**
     * Extracts features from the board for ML evaluation.
     * The model is trained to predict X wins (+1) vs O wins (-1).
     * 
     * Features match the CSV dataset:
     * 0: f1_X_count - number of X marks on the board
     * 1: f2_O_count - number of O marks on the board
     * 2: f3_X_almost_win - number of lines where X has 2 marks and 1 empty cell
     * 3: f4_O_almost_win - number of lines where O has 2 marks and 1 empty cell
     * 4: f5_X_center - 1 if X controls center, 0 otherwise
     * 5: f6_X_corners - number of corners controlled by X
     * 
     * @param board the game board
     * @param forPlayer the player we're evaluating for (used to determine perspective)
     * @return feature vector of size 6
     */
    public static double[] extract(Board board, Player forPlayer) {
        double[] features = new double[6];
        
        int xCount = 0;
        int oCount = 0;
        
        // Count X and O marks
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getCell(i, j) == Player.X) {
                    xCount++;
                } else if (board.getCell(i, j) == Player.O) {
                    oCount++;
                }
            }
        }
        
        features[0] = xCount;    // f1_X_count
        features[1] = oCount;    // f2_O_count
        
        // Count almost-win situations (2 in a row with 1 empty)
        int[] almostWins = countAlmostWins(board);
        features[2] = almostWins[0];  // f3_X_almost_win
        features[3] = almostWins[1];  // f4_O_almost_win
        
        // X controls center
        Player center = board.getCell(1, 1);
        features[4] = (center == Player.X) ? 1 : 0;  // f5_X_center
        
        // X corner control
        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        int xCorners = 0;
        for (int[] corner : corners) {
            if (board.getCell(corner[0], corner[1]) == Player.X) {
                xCorners++;
            }
        }
        features[5] = xCorners;  // f6_X_corners
        
        return features;
    }
    
    /**
     * Count the number of "almost win" situations for each player.
     * An almost-win is a line with 2 marks of the same player and 1 empty cell.
     * 
     * @return array with [X_almost_wins, O_almost_wins]
     */
    private static int[] countAlmostWins(Board board) {
        int xAlmostWins = 0;
        int oAlmostWins = 0;
        
        // Check rows
        for (int i = 0; i < 3; i++) {
            int[] count = countLine(board.getCell(i, 0), board.getCell(i, 1), board.getCell(i, 2));
            xAlmostWins += count[0];
            oAlmostWins += count[1];
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            int[] count = countLine(board.getCell(0, j), board.getCell(1, j), board.getCell(2, j));
            xAlmostWins += count[0];
            oAlmostWins += count[1];
        }
        
        // Check diagonals
        int[] count1 = countLine(board.getCell(0, 0), board.getCell(1, 1), board.getCell(2, 2));
        int[] count2 = countLine(board.getCell(0, 2), board.getCell(1, 1), board.getCell(2, 0));
        xAlmostWins += count1[0] + count2[0];
        oAlmostWins += count1[1] + count2[1];
        
        return new int[]{xAlmostWins, oAlmostWins};
    }
    
    /**
     * Check if a line has an "almost win" situation.
     * Returns 1 for X if line has 2 X's and 1 empty, 1 for O if line has 2 O's and 1 empty.
     * 
     * @return array with [X_has_almost_win, O_has_almost_win]
     */
    private static int[] countLine(Player p1, Player p2, Player p3) {
        int xCount = 0;
        int oCount = 0;
        int emptyCount = 0;
        
        if (p1 == Player.X) xCount++;
        else if (p1 == Player.O) oCount++;
        else emptyCount++;
        
        if (p2 == Player.X) xCount++;
        else if (p2 == Player.O) oCount++;
        else emptyCount++;
        
        if (p3 == Player.X) xCount++;
        else if (p3 == Player.O) oCount++;
        else emptyCount++;
        
        int xAlmostWin = (xCount == 2 && emptyCount == 1) ? 1 : 0;
        int oAlmostWin = (oCount == 2 && emptyCount == 1) ? 1 : 0;
        
        return new int[]{xAlmostWin, oAlmostWin};
    }
}
