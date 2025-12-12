# ML Training System - Tic-Tac-Toe AI

## Overview

The ML evaluation system uses **runtime training** with a Logistic Regression model. The model trains automatically when the application starts, learning from a CSV dataset included in the resources folder.

## Dataset Structure

**Location:** `src/main/resources/dataset/training_data.csv`

**Columns:**
- `f1_X_count`: Number of X marks on the board (0-9)
- `f2_O_count`: Number of O marks on the board (0-9)
- `f3_X_almost_win`: Number of lines where X has 2 marks and 1 empty cell (0-8)
- `f4_O_almost_win`: Number of lines where O has 2 marks and 1 empty cell (0-8)
- `f5_X_center`: 1 if X controls the center, 0 otherwise
- `f6_X_corners`: Number of corners controlled by X (0-4)
- `label`: Game outcome (+1 if X wins, -1 if O wins)

## How It Works

### 1. Training Phase (Runtime)
When the application starts:
1. The `TrainedModel.initialize()` method is called
2. The CSV dataset is loaded from resources
3. A Logistic Regression model trains using gradient descent
4. Training accuracy is displayed in the console
5. The trained weights are stored in memory

### 2. Player-Agnostic Design
The model is trained to predict outcomes from X's perspective:
- **Positive scores** indicate X is winning
- **Negative scores** indicate O is winning

When the AI plays as O:
- The `MLEvaluator` automatically negates the score
- This ensures the evaluation is always correct regardless of which player the AI controls

### 3. Feature Extraction
The `FeatureExtractor` analyzes the board state and creates a 6-dimensional feature vector that matches the training data format.

### 4. Prediction
During gameplay:
1. Features are extracted from the current board
2. The trained model computes a score
3. The score is adjusted based on which player the AI is playing
4. The Alpha-Beta algorithm uses this score to select the best move

## Training Parameters

- **Algorithm:** Gradient Descent
- **Learning Rate:** 0.01
- **Max Iterations:** 1000
- **Convergence Threshold:** 0.0001
- **Loss Function:** Mean Squared Error (MSE)

## Adding Your Own Dataset

To use your own training data:

1. Replace `src/main/resources/dataset/training_data.csv` with your CSV file
2. Ensure it has the same column structure
3. Make sure labels are +1 (X wins) or -1 (O wins)
4. The model will automatically retrain on next application startup

## Fallback Behavior

If the dataset fails to load:
- The system falls back to default hand-tuned weights
- A warning message is displayed in the console
- The game continues to work normally

## Console Output

When starting the application, you'll see:
```
=== Initializing ML Model ===
Loading dataset from: /dataset/training_data.csv
Loaded 2013 training examples from /dataset/training_data.csv
Starting training with 2013 examples...
Iteration 0: Loss = 0.523412
Iteration 100: Loss = 0.234567
...
Converged at iteration 456
Training complete!
Final weights:
  w[0] = 1.2345
  w[1] = -1.3456
  ...
  bias = 0.1234
Training accuracy: 87.34%
=== ML Model Ready ===
```

## Implementation Details

### Key Classes

- **TrainedModel:** Main interface for the ML model
- **DatasetLoader:** Loads and parses CSV training data
- **LogisticRegressionTrainer:** Implements gradient descent training
- **FeatureExtractor:** Converts board state to feature vector
- **MLEvaluator:** Uses the trained model for board evaluation

### Thread Safety

The model trains once at startup before the GUI loads. After training, it's read-only and thread-safe for concurrent predictions.
