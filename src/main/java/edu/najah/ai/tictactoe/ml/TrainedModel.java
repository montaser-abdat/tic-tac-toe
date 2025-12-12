package edu.najah.ai.tictactoe.ml;

import java.io.IOException;
import java.util.List;

/**
 * Runtime-trained ML model for Tic-Tac-Toe evaluation.
 * This is a logistic regression model that trains on startup from a CSV dataset.
 * The model predicts game outcomes based on board features.
 */
public class TrainedModel {
    
    private static double[] weights;
    private static double bias;
    private static boolean isTrained = false;
    
    // Dataset path in resources
    private static final String DATASET_PATH = "/dataset/tictactoe_dataset.csv";
    private static final int NUM_FEATURES = 6;
    
    /**
     * Initialize and train the model from the dataset.
     * This should be called once at application startup.
     */
    public static void initialize() {
        if (isTrained) {
            System.out.println("Model already trained.");
            return;
        }
        
        try {
            System.out.println("Loading dataset from: " + DATASET_PATH);
            List<DatasetLoader.DataLine> dataset = DatasetLoader.loadDataset(DATASET_PATH);
            
            if (dataset.isEmpty()) {
                throw new RuntimeException("Dataset is empty! Cannot train model.");
            }
            
            // Train the model
            LogisticRegressionTrainer trainer = new LogisticRegressionTrainer(NUM_FEATURES);
            trainer.train(dataset);
            
            // Get trained parameters
            weights = trainer.getWeights();
            bias = trainer.getBias();
            
            // Evaluate accuracy
            double accuracy = trainer.evaluateAccuracy(dataset);
            System.out.printf("Training accuracy: %.2f%%\n", accuracy * 100);
            
            isTrained = true;
            
        } catch (IOException e) {
            System.err.println("Error loading dataset: " + e.getMessage());
            e.printStackTrace();
            // Fall back to default weights if training fails
            initializeDefaultWeights();
        } catch (Exception e) {
            System.err.println("Error during training: " + e.getMessage());
            e.printStackTrace();
            initializeDefaultWeights();
        }
    }
    
    /**
     * Initialize with reasonable default weights if training fails.
     */
    private static void initializeDefaultWeights() {
        System.out.println("Using default weights (no training)");
        weights = new double[NUM_FEATURES];
        weights[0] = 1.0;   // f1_X_count
        weights[1] = -1.0;  // f2_O_count
        weights[2] = 2.0;   // f3_X_almost_win
        weights[3] = -2.0;  // f4_O_almost_win
        weights[4] = 0.5;   // f5_X_center
        weights[5] = 0.3;   // f6_X_corners
        bias = 0.0;
        isTrained = true;
    }
    
    /**
     * Computes the forward pass of the model.
     * Returns a score representing the evaluation of the board.
     * 
     * @param features extracted features from the board
     * @return evaluation score
     */
    public static double predict(double[] features) {
        if (!isTrained) {
            throw new IllegalStateException("Model not initialized! Call TrainedModel.initialize() first.");
        }
        
        if (features.length != NUM_FEATURES) {
            throw new IllegalArgumentException("Feature length mismatch. Expected " + NUM_FEATURES + ", got " + features.length);
        }
        
        double score = bias;
        for (int i = 0; i < features.length; i++) {
            score += weights[i] * features[i];
        }
        
        // Return raw score
        return score;
    }
    
    public static boolean isTrained() {
        return isTrained;
    }
}
