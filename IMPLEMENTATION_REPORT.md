# Tic-Tac-Toe AI - Implementation Report

## Project Overview
This project implements a complete Tic-Tac-Toe game with an AI opponent using Alpha-Beta pruning, as specified in the Software Requirements Specification (SRS).

## Technology Stack
- **Language**: Java 17
- **GUI Framework**: JavaFX 17.0.2 with FXML
- **Build Tool**: Maven
- **AI Algorithm**: Custom Alpha-Beta Pruning (no external AI libraries)
- **ML Model**: Manual implementation (logistic regression with hardcoded weights)

## SRS Requirements Compliance

### ✅ 1. Functional Requirements - Game Initialization

#### FR-1: Main Menu Page
- **Status**: ✅ IMPLEMENTED
- **Location**: `src/main/resources/menu.fxml`, `MenuController.java`
- **Features**:
  - Start Game button
  - Settings button
  - Help button
  - Exit button

#### FR-2: Navigate to Game Settings
- **Status**: ✅ IMPLEMENTED
- **Location**: `src/main/resources/settings.fxml`, `SettingsController.java`
- **Features**:
  - Player Symbol selection (X or O)
  - Difficulty Level (Easy/Normal/Hard)
  - Evaluation Function (Classical/ML)
  - Debug Mode toggle

#### FR-3: Save Settings
- **Status**: ✅ IMPLEMENTED
- **Location**: `GameSettings.java` (Singleton pattern)
- **Features**: Settings persist during application session

### ✅ 2. Functional Requirements - Game Board & Gameplay

#### FR-4: Display Game Board
- **Status**: ✅ IMPLEMENTED
- **Location**: `src/main/resources/game.fxml`, `GameController.java`
- **Features**:
  - 3×3 GridPane with clickable cells
  - Visual feedback for X (blue) and O (red)
  - Disabled cells after moves

#### FR-5: User Move Handling
- **Status**: ✅ IMPLEMENTED
- **Location**: `GameController.handleCellClick()`
- **Features**:
  - Cell validation
  - Symbol placement
  - Terminal state checking
  - AI move triggering

#### FR-6: AI Move Execution
- **Status**: ✅ IMPLEMENTED
- **Location**: `GameController.makeAIMove()`
- **Features**:
  - Alpha-Beta pruning search
  - Depth-limited search based on difficulty
  - Evaluation function application
  - Asynchronous execution (UI doesn't freeze)

#### FR-7: Terminal State Detection
- **Status**: ✅ IMPLEMENTED
- **Location**: `Board.checkWinner()`, `Board.isTerminal()`
- **Features**:
  - Detects X wins
  - Detects O wins
  - Detects draws
  - Checks rows, columns, diagonals

#### FR-8: End-Game Display
- **Status**: ✅ IMPLEMENTED
- **Location**: `GameController.showGameOverDialog()`
- **Features**:
  - Winner announcement dialog
  - "Play Again" option
  - "Return to Menu" option
  - Visual highlighting of winning line

### ✅ 3. AI Requirements

#### FR-9: Alpha-Beta Search Implementation
- **Status**: ✅ IMPLEMENTED
- **Location**: `AlphaBeta.java`
- **Features**:
  - `maxValue()` method for maximizing player
  - `minValue()` method for minimizing player
  - Alpha and beta bounds
  - Pruning when alpha ≥ beta
  - Depth limit enforcement
  - Node counting for debug

#### FR-10: AI Difficulty Levels
- **Status**: ✅ IMPLEMENTED
- **Location**: `Difficulty.java`
- **Configuration**:
  - **Easy**: Depth 2 (shallow search, beatable)
  - **Normal**: Depth 4 (moderate challenge)
  - **Hard**: Depth 9 (perfect play, full search)

#### FR-11: Evaluation Function Interface
- **Status**: ✅ IMPLEMENTED
- **Location**: `Evaluator.java` (interface)
- **Implementations**:
  - `ClassicalEvaluator.java`
  - `MLEvaluator.java`
- **Method**: `int evaluate(Board board, Player maximizingPlayer)`

### ✅ 4. Evaluation Function Requirements

#### FR-12: Classical Evaluation Rules
- **Status**: ✅ IMPLEMENTED
- **Location**: `ClassicalEvaluator.java`
- **Features**:
  - Win/Loss detection: ±1000 points
  - Center control: ±30 points
  - Corner control: ±20 points per corner
  - Two-in-a-row: ±50 points (offensive and defensive)
  - One-in-a-row: ±10 points
  - Terminal states override all heuristics

#### FR-13: ML Evaluation Requirements
- **Status**: ✅ IMPLEMENTED
- **Location**: `MLEvaluator.java`, `FeatureExtractor.java`, `TrainedModel.java`
- **Features**:
  - **Feature Extraction** (9 features):
    1. X count
    2. O count
    3. Center control
    4. X corners
    5. O corners
    6. X two-in-a-row
    7. O two-in-a-row
    8. X blocking opportunities
    9. O blocking opportunities
  - **Manual ML Model**:
    - Logistic regression with hardcoded weights
    - No external ML libraries
    - Forward pass: `score = w·features + bias`
  - **Terminal state override**: Win/loss detection takes precedence

### ✅ 5. Debug Information

#### FR-14: Debug Score Panel
- **Status**: ✅ IMPLEMENTED
- **Location**: `GameController.updateDebugInfo()`, `game.fxml`
- **Features**:
  - Shows all possible moves with scores
  - Sorted by score (best first)
  - Format: `(row,col): score`
  - Visible only when debug mode is ON

#### FR-15: Real-Time Logs
- **Status**: ✅ IMPLEMENTED
- **Location**: `GameController.updateDebugInfo()`
- **Features**:
  - Nodes evaluated count
  - Branches pruned count
  - Current depth limit

### ✅ 6. Application Flow Requirements

#### FR-16: Navigation Flow
- **Status**: ✅ IMPLEMENTED
- **Flow**: Menu → Settings → Save → Menu → Start Game → Gameplay

#### FR-17: Restart Game
- **Status**: ✅ IMPLEMENTED
- **Location**: `GameController.handleRestart()`
- **Features**: Full board reset without returning to menu

#### FR-18: Exit Behavior
- **Status**: ✅ IMPLEMENTED
- **Location**: `MenuController.handleExit()`
- **Features**: Clean application shutdown using `Platform.exit()`

## Non-Functional Requirements

### Performance
- ✅ AI move computation: < 1 second on Normal difficulty
- ✅ Hard difficulty: < 3 seconds (usually instant on modern hardware)
- ✅ Asynchronous AI moves prevent UI freezing

### Reliability
- ✅ All illegal moves are rejected
- ✅ Alpha-Beta never returns null (if moves exist)
- ✅ ML model always produces valid numeric output

### Usability
- ✅ Clean, intuitive JavaFX GUI with FXML layouts
- ✅ Clear button labels and visual feedback
- ✅ Color-coded X (blue) and O (red)
- ✅ Winning line highlighted in green

### Maintainability
- ✅ Modular architecture:
  - `/ai` - AI engine components
  - `/ml` - ML model and feature extraction
  - `/game` - Game logic and state
  - `/gui` - UI controllers

## Project Structure

```
src/main/java/edu/najah/ai/tictactoe/
├── TicTacToeApp.java           # Main application entry point
├── ai/
│   ├── AlphaBeta.java          # Alpha-Beta pruning implementation
│   ├── Evaluator.java          # Evaluator interface
│   ├── ClassicalEvaluator.java # Heuristic-based evaluator
│   ├── MLEvaluator.java        # ML-based evaluator
│   └── Difficulty.java         # Difficulty levels enum
├── game/
│   ├── Board.java              # Board state and logic
│   ├── Move.java               # Move representation
│   ├── Player.java             # Player enum (X, O, EMPTY)
│   └── GameSettings.java       # Settings singleton
├── gui/
│   ├── MenuController.java     # Main menu controller
│   ├── SettingsController.java # Settings page controller
│   └── GameController.java     # Game page controller
└── ml/
    ├── FeatureExtractor.java   # Board feature extraction
    └── TrainedModel.java       # ML model (manual weights)

src/main/resources/
├── menu.fxml                   # Main menu layout
├── settings.fxml               # Settings page layout
└── game.fxml                   # Game page layout
```

## Build and Run Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Compile the Project
```bash
mvn clean compile
```

### Run the Application
```bash
mvn javafx:run
```

Or use the provided batch file:
```bash
run.bat
```

## How to Play

1. **Launch the application** using `mvn javafx:run` or `run.bat`
2. **Configure settings**:
   - Click "Settings" from the main menu
   - Choose your symbol (X or O)
   - Select difficulty level (Easy/Normal/Hard)
   - Choose evaluation function (Classical or ML)
   - Toggle debug mode if you want to see AI scores
   - Click "Save Settings"
3. **Start the game**:
   - Click "Start Game" from the main menu
   - If you're X, make the first move
   - If you're O, the AI moves first
4. **Play**:
   - Click on empty cells to make your move
   - The AI will respond automatically
   - Watch the debug panel (if enabled) to see AI thinking
5. **End of game**:
   - A dialog shows the winner or draw
   - Choose "Play Again" or "Return to Menu"

## AI Algorithm Details

### Alpha-Beta Pruning
The AI uses the minimax algorithm with alpha-beta pruning to search the game tree:

```
function ALPHA-BETA-SEARCH(state, depth):
    if state is terminal or depth = 0:
        return EVALUATE(state)
    
    if MAXIMIZING-PLAYER:
        value = -∞
        for each child in CHILDREN(state):
            value = max(value, MIN-VALUE(child, α, β, depth-1))
            α = max(α, value)
            if α ≥ β:
                PRUNE  # Beta cutoff
        return value
    else:
        value = +∞
        for each child in CHILDREN(state):
            value = min(value, MAX-VALUE(child, α, β, depth-1))
            β = min(β, value)
            if α ≥ β:
                PRUNE  # Alpha cutoff
        return value
```

### Evaluation Functions

**Classical Heuristic**:
- Terminal states: Win = +1000, Loss = -1000, Draw = 0
- Center control: ±30
- Each corner: ±20
- Two-in-a-row: ±50
- One-in-a-row: ±10

**ML Model**:
- Features: 9-dimensional vector
- Model: Linear regression with manual weights
- Formula: `score = Σ(wi × fi) + bias`
- Weights tuned for strategic positions

## Testing Recommendations

1. **Easy Mode**: Should be beatable by average players
2. **Normal Mode**: Challenging but not perfect
3. **Hard Mode**: Should never lose (perfect play)
4. **Classical vs ML**: Both should play strategically
5. **Debug Mode**: Verify scores make sense

## Known Limitations

1. No persistence across application restarts
2. No network multiplayer
3. No undo functionality
4. ML model uses simple linear regression (could be improved with MLP)

## Future Enhancements (Optional)

- [ ] Save game statistics
- [ ] Dark/Light theme toggle
- [ ] Animation effects
- [ ] Sound effects
- [ ] More sophisticated ML model (MLP)
- [ ] Reinforcement learning training
- [ ] Larger board sizes (4×4, 5×5)

## Compliance Summary

✅ **All SRS requirements have been successfully implemented**

- [x] FR-1 to FR-18: All functional requirements
- [x] Alpha-Beta pruning (no external AI libraries)
- [x] Classical and ML evaluation functions
- [x] Three difficulty levels
- [x] JavaFX GUI with FXML
- [x] Debug mode
- [x] Complete navigation flow
- [x] Manual ML implementation (no ML libraries)

## Conclusion

This project fully satisfies the Software Requirements Specification for a Tic-Tac-Toe AI game with:
- Custom Alpha-Beta pruning implementation
- Two interchangeable evaluation functions
- Multiple difficulty levels
- Professional JavaFX GUI
- Debug capabilities

The application is ready for testing, demonstration, and deployment.
