# Chess-Terminal-UI

## **Utilities Module**
Location: `utilities/`

### **1. Pair.java**
A generic utility class that holds two related objects as a pair.  
- **Key Methods**: 
  - `getFirst()` and `getSecond()` to retrieve the pair's elements.
  - Supports custom implementations for managing pairs effectively.

### **2. ANSIColour.java**
A utility class for working with ANSI colour codes.  
- **Key Methods**:
  - Provides methods to add colour to console outputs for better visualization.
  - Includes predefined constants for common colours like red, green, blue, etc.

---

## **View Module**
Location: `view/`

### **1. TUI.java**
Implements a text-based user interface (TUI) for interacting with the application.  
- **Key Features**:
  - Handles user input/output via the console.
  - Manages prompts, responses, and interactions with the core game logic.

---

## **Model Module**
Location: `model/`

### Core Game Files:
#### **1. App.java**
The main entry point for the application.  
- **Responsibilities**:
  - Initializes the game environment.
  - Orchestrates interactions between different components (view, utilities, and model).

#### **2. Board.java**
Represents the game board as a grid of `Square` objects.  
- **Key Features**:
  - Provides methods to initialize and reset the board.
  - Manages the layout and positioning of pieces.

#### **3. Game.java**
Encapsulates the main game logic.  
- **Key Responsibilities**:
  - Manages turns, rules, and game flow.
  - Handles win/loss conditions and player interactions.

#### **4. Move.java**
Defines a move within the game.  
- **Key Features**:
  - Tracks the start and end positions of a move.
  - Validates the legality of moves based on game rules.

#### **5. Monitor.java**
Handles the game's state monitoring and control.  
- **Responsibilities**:
  - Observe changes in game status.
  - Coordinates responses to significant events (e.g., checkmate).

#### **6. Square.java**
Represents an individual square on the game board.  
- **Key Features**:
  - Stores information about its position and the piece it contains (if any).
  - Provides methods for setting and retrieving pieces.

#### **7. Side.java**
An enumeration defining the two sides of the game (e.g., `WHITE` and `BLACK`).  
- **Usage**:
  - Used by pieces and players to identify their allegiance.

#### **8. Player.java**
Represents a player in the game.  
- **Key Features**:
  - Stores player-related data, such as name and side.
  - Manages player actions and decision-making.

### Pieces Submodule:
Location: `model/pieces/`

#### **1. Piece.java**
An abstract base class for all game pieces.  
- **Key Features**:
  - Defines common attributes like position and side.
  - Provides a template for piece-specific behaviours (e.g., `isMoveValid()`).

#### **2. King.java**
Represents the king piece.  
- **Special Features**:
  - Implements movement logic (one square in any direction).
  - Tracks conditions like check and checkmate.

#### **3. Queen.java**
Represents the queen piece.  
- **Key Features**:
  - Combines the movement abilities of both the rook and bishop.

#### **4. Rook.java**
Represents the rook piece.  
- **Key Features**:
  - Moves in straight lines horizontally or vertically.

#### **5. Bishop.java**
Represents the bishop piece.  
- **Key Features**:
  - Moves diagonally across the board.

#### **6. Knight.java**
Represents the knight piece.  
- **Special Movement**:
  - Moves in an "L" shape (two squares in one direction and one square perpendicular).

#### **7. Pawn.java**
Represents the pawn piece.  
- **Unique Features**:
  - Moves forward one square, with an optional two-square move on its first turn.
  - Can capture diagonally and perform special moves like "en passant."

### Logs Submodule:
Location: `model/Logs/`

#### **1. LogServer.java**
Handles the server-side logging for the game.  
- **Responsibilities**:
  - Captures events and stores them for debugging or replay purposes.

#### **2. LogClientThread.java**
Manages client-side logging in a multi-threaded environment.  
- **Key Features**:
  - Allows simultaneous logging from multiple clients.
  - Ensures thread safety during log operations.

---

## How to Use

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>

	2.	Compile the Java files:

javac -d bin $(find . -name "*.java")


	3.	Run the application:

java -cp bin model.App

Contribution Guidelines

	•	Fork the repository.
	•	Create a new branch (git checkout -b feature-name).
	•	Commit your changes (git commit -m "Add feature-name").
	•	Push to your branch (git push origin feature-name).
	•	Open a pull request.

License

This project is licensed under the MIT License.


Acknowledgments

	•	Thanks to the contributors for their efforts in building and maintaining this project.
	•	This project follows modular and object-oriented design principles.
