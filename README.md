# Tic-Tac-Toe AI with Alpha-Beta Pruning

## Project Overview

This is a complete implementation of a Tic-Tac-Toe game with AI opponent using:
- **Alpha-Beta Pruning** search algorithm (custom implementation)
- **Classical Heuristic Evaluation** function
- **Machine Learning-Based Evaluation** function (manual implementation)
- **JavaFX GUI** with FXML-based design
- **Multiple Difficulty Levels** (Easy, Normal, Hard)
- **Debug Mode** showing AI evaluation scores

## Features

### Core Features
✅ Fully functional Tic-Tac-Toe game  
✅ AI opponent using Alpha-Beta pruning (NO external AI libraries)  
✅ Two evaluation functions: Classical and ML  
✅ Three difficulty levels affecting search depth  
✅ JavaFX GUI with clean FXML design  
✅ Complete menu system (Main Menu → Settings → Game)  
✅ Debug mode showing move evaluations  

### Technical Implementation
- **Language**: Java 17
- **GUI Framework**: JavaFX with FXML
- **AI Algorithm**: Custom Alpha-Beta pruning with configurable depth
- **ML Model**: Manually implemented logistic regression (no external ML libraries)
- **Architecture**: Modular design with separation of concerns

## Project Structure

```
tic-tac-toe/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/edu/najah/ai/tictactoe/
│   │   │   ├── TicTacToeApp.java          # Main application entry point
│   │   │   ├── game/
│   │   │   │   ├── Board.java             # Game board logic
│   │   │   │   ├── Move.java              # Move representation
│   │   │   │   ├── Player.java            # Player enum (X, O, EMPTY)
│   │   │   │   └── GameSettings.java      # Settings manager
│   │   │   ├── ai/
│   │   │   │   ├── AlphaBeta.java         # Alpha-Beta pruning implementation
│   │   │   │   ├── Evaluator.java         # Evaluation interface
│   │   │   │   ├── ClassicalEvaluator.java # Heuristic-based evaluation
│   │   │   │   ├── MLEvaluator.java       # ML-based evaluation
│   │   │   │   └── Difficulty.java        # Difficulty levels
│   │   │   ├── ml/
│   │   │   │   ├── FeatureExtractor.java  # Extract features from board
│   │   │   │   └── TrainedModel.java      # ML model (manual implementation)
│   │   │   └── gui/
│   │   │       ├── MenuController.java    # Main menu controller
│   │   │       ├── SettingsController.java # Settings screen controller
│   │   │       └── GameController.java    # Game screen controller
│   │   └── resources/
│   │       ├── menu.fxml                  # Main menu UI
│   │       ├── settings.fxml              # Settings UI
│   │       └── game.fxml                  # Game UI
```

## How to Build and Run

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build the Project

```cmd
mvn clean compile
```

### Run the Application

```cmd
mvn javafx:run
```

Alternatively, you can package and run:

```cmd
mvn clean package
java --module-path "path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -jar target\tic-tac-toe-0.0.1-SNAPSHOT.jar
```

## How to Play

1. **Start the Application**: Run using `mvn javafx:run`

2. **Configure Settings**: 
   - Click "Settings" on the main menu
   - Choose your symbol (X or O)
   - Select difficulty level (Easy/Normal/Hard)
   - Choose evaluation function (Classical/ML)
   - Enable/disable Debug Mode
   - Click "Save Settings"

3. **Play the Game**:
   - Click "Start Game" from the main menu
   - Click on an empty cell to make your move
   - The AI will automatically respond
   - Game ends when someone wins or it's a draw

4. **Debug Mode**:
   - When enabled, the right panel shows:
     - AI evaluation scores for all possible moves
     - Number of nodes evaluated
     - Number of branches pruned
     - Current search depth

## AI Implementation Details

### Alpha-Beta Pruning
- Implements minimax with alpha-beta cutoffs
- Configurable depth limits based on difficulty:
  - **Easy**: Depth 2 (weak play)
  - **Normal**: Depth 4 (moderate play)
  - **Hard**: Depth 9 (perfect play)
- Prunes branches when α ≥ β

### Classical Evaluator
Considers:
- **Terminal states**: Win (+1000), Loss (-1000), Draw (0)
- **Center control**: +30 for occupying center
- **Corner control**: +20 per corner
- **Two-in-a-row**: +50 for potential winning lines
- **Blocking opportunities**: Detects and values defensive moves

### ML Evaluator
Features extracted:
1. X count on board
2. O count on board
3. Center control
4. Corner control for X and O
5. Two-in-a-row counts
6. Blocking opportunities

Uses a manually implemented logistic regression model with pre-trained weights.

## Acceptance Criteria Status

✅ Game can be played from start to finish against AI  
✅ Three difficulty levels produce different AI strength  
✅ Both evaluation functions (Classical & ML) work correctly  
✅ Full GUI flow: Menu → Settings → Game → End Screen → Restart/Menu  
✅ Alpha-Beta runs with pruning and variable depth  
✅ ML evaluation produces consistent numeric results  
✅ Debug mode shows evaluation scores for all candidate moves  
✅ No external AI libraries used (custom implementation)  
✅ ML model manually implemented (no TensorFlow, scikit-learn, etc.)  

## Extension Features (Implemented)

- Real-time status updates
- Visual highlighting of winning line
- Thread-based AI thinking (non-blocking UI)
- Clean, modern UI design
- Help dialog with instructions
- Settings persistence during session

## Testing

To test the application:

1. **Test Easy Difficulty**: AI should make suboptimal moves (beatable)
2. **Test Normal Difficulty**: AI should be challenging but beatable
3. **Test Hard Difficulty**: AI should never lose (perfect play)
4. **Test Classical vs ML**: Both should produce valid moves
5. **Test Debug Mode**: Verify scores are displayed correctly
6. **Test Both Players**: Try playing as X and as O

## Known Limitations

- Settings are not persisted across application restarts
- ML model uses pre-set weights (not actually trained on data)
- No animations or sound effects (as per SRS optional features)
- Single-player mode only (no human vs human)

## Credits

**Project**: Tic-Tac-Toe AI  
**Technology**: Java 17 + JavaFX  
**AI Techniques**: Alpha-Beta Pruning, Heuristic Evaluation, ML Integration  
**Implementation**: Custom (no external AI libraries)

---

**Enjoy playing against the AI!** 🎮
