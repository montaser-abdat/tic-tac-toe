package edu.najah.ai.tictactoe.ml;

import java.util.List;
import java.util.Random;

/**
 * Logistic Regression trainer for Tic-Tac-Toe evaluation.
 * Trains a model to predict game outcomes based on board features.
 */
public class LogisticRegressionTrainer {
    
    private double[] weights;
    private double bias;
    private final int numFeatures;
    
    // Hyperparameters
    private static final double LEARNING_RATE = 0.01;
    private static final int MAX_ITERATIONS = 1000;
    private static final double CONVERGENCE_THRESHOLD = 0.0001;
    
    public LogisticRegressionTrainer(int numFeatures) {
        this.numFeatures = numFeatures;
        this.weights = new double[numFeatures];
        this.bias = 0.0;
        
        // Initialize weights with small random values
        Random random = new Random(42); // Fixed seed for reproducibility
        for (int i = 0; i < numFeatures; i++) {
            weights[i] = random.nextGaussian() * 0.01;
        }
    }
    
    /**
     * Train the model using gradient descent.
     */
    public void train(List<DatasetLoader.DataLine> dataset) {
        System.out.println("Starting training with " + dataset.size() + " examples...");
        
        double prevLoss = Double.MAX_VALUE;
        
        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            double totalLoss = 0.0;
            double[] weightGradients = new double[numFeatures];
            double biasGradient = 0.0;
            
            // Compute gradients over entire dataset
            for (DatasetLoader.DataLine line : dataset) {
                double prediction = predict(line.features);
                double error = prediction - line.label;
                
                // Accumulate gradients
                for (int i = 0; i < numFeatures; i++) {
                    weightGradients[i] += error * line.features[i];
                }
                biasGradient += error;
                
                // Compute loss (MSE)
                totalLoss += error * error;
            }
            
            // Average the gradients and loss
            int n = dataset.size();
            totalLoss /= n;
            for (int i = 0; i < numFeatures; i++) {
                weightGradients[i] /= n;
            }
            biasGradient /= n;
            
            // Update weights
            for (int i = 0; i < numFeatures; i++) {
                weights[i] -= LEARNING_RATE * weightGradients[i];
            }
            bias -= LEARNING_RATE * biasGradient;
            
            // Log progress
            if (iteration % 100 == 0) {
                System.out.printf("Iteration %d: Loss = %.6f\n", iteration, totalLoss);
            }
            
            // Check convergence
            if (Math.abs(prevLoss - totalLoss) < CONVERGENCE_THRESHOLD) {
                System.out.println("Converged at iteration " + iteration);
                break;
            }
            prevLoss = totalLoss;
        }
        
        System.out.println("Training complete!");
        System.out.println("Final weights: ");
        for (int i = 0; i < numFeatures; i++) {
            System.out.printf("  w[%d] = %.4f\n", i, weights[i]);
        }
        System.out.printf("  bias = %.4f\n", bias);
    }
    
    /**
     * Predict using current weights (returns raw score, not sigmoid).
     */
    private double predict(double[] features) {
        double score = bias;
        for (int i = 0; i < numFeatures; i++) {
            score += weights[i] * features[i];
        }
        return score;
    }
    
    public double[] getWeights() {
        return weights;
    }
    
    public double getBias() {
        return bias;
    }
    
    /**
     * Evaluate model accuracy on dataset.
     */
    public double evaluateAccuracy(List<DatasetLoader.DataLine> dataset) {
        int correct = 0;
        for (DatasetLoader.DataLine line : dataset) {
            double prediction = predict(line.features);
            // Classify: positive score -> X wins (+1), negative -> O wins (-1)
            double predictedLabel = prediction >= 0 ? 1.0 : -1.0;
            if (predictedLabel == line.label) {
                correct++;
            }
        }
        return (double) correct / dataset.size();
    }
}
