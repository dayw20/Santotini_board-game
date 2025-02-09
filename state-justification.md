# Justification for handling state
Below, describe where you stored each of the following states and justify your answers with design principles/goals/heuristics/patterns. Discuss the alternatives and trade-offs you considered during your design process.

## Players
The Game class maintains a list of Player objects (`players: Player[]`). This design follows the Controller heuristic, as Game acts as a coordinator managing the overall game flow. It also supports low coupling since Player objects don't need to know about other players, they only focus on managing their own workers and actions.

## Current player
The Game class tracks the current player (`currPlayer: Player`). This aligns with the Controller heuristic as Game is responsible for managing turn sequence and game flow. Having Game manage the current player creates a single point of control for turn management, enhancing maintainability and reducing the risk of inconsistent game state.

## Worker locations
Worker locations are stored in two places with synchronized updates:

Each Worker object stores its own position (`position: Cell`)
The Board maintains a list of all workers (`allWorkers: List<Worker>`)

This dual storage approach was chosen because:

Having Worker store its own position follows the Information Expert heuristic since the Worker is the entity most closely associated with its location
Board's worker list enables efficient global queries(don't need to iterative over all cells to find worker) and position validation
The synchronized updates are encapsulated within `move()` methods to maintain consistency

## Towers
Tower information is stored within the Cell class through:

`level: int for tower height`

`occupancy: Occupancy enum (EMPTY, WORKER, DOME) for dome status`

This design was chosen over creating a separate Tower class to maintain a low representational gap - cells are the fundamental unit of the game board, and their properties (height, dome status) are intrinsic to the cell itself rather than separate entities.

## Winner
The Game class is responsible for tracking and determining the winner through its `checkWin()` and `isGameOver()` methods. This follows the Controller heuristic as Game has the global view necessary to evaluate victory conditions and manage game termination.

## Design goals/principles/heuristics considered
### **Low Representational Gap**

Cell structure closely mirrors the physical game board

Worker and Player relationships reflect actual game mechanics. Player control the move and build action of workers, while worker takes the responsibility of how mave and build are implemented.



### **Low Coupling**

Game class coordinates interactions, preventing direct coupling between players

Cell state management is localized

Worker operations are self-contained



### **High Cohesion**

Each class has clear, focused responsibilities:

Game manages game flow and logic

Board manages spatial relationships

Worker manages movement and building actions

Player manages worker control and strategy



### **Design Heuristics**

Controller: Game class coordinates overall flow

Information Expert: Classes manage their most relevant data

Creator: Player manages its workers, Board manages cells

## Alternatives considered and analysis of trade-offs
In designing the game state management, several key architectural decisions were made after careful consideration of alternatives. 

1. When considering the **overall game state management**, I initially explored the possibility of having Player objects track game state. However, I ultimately chose to centralize this responsibility in the Game class. While this decision slightly increased the complexity of the Game class, it significantly reduced coupling by preventing players from needing knowledge of game-wide state, resulting in a more maintainable design.

2. For **tower state management**, I considered creating a separate Tower class to represent building levels and dome status. After analysis, I decided to integrate these properties directly into the Cell class. This approach simplifies state management and reduces object overhead, though it makes tower modeling less explicit. The trade-off favored practical implementation and better alignment with the physical game board representation.

3. In **organizing the Cell state**, I faced a choice between directly storing all properties in the Cell class versus splitting them into distinct components. I chose to separate the state into an **Occupancy enum** and level properties. While this introduced some additional complexity in state representation, it provided clearer separation of concerns and simplified state management logic. The enum-based approach made the code more maintainable and easier to understand.

4. The **management of worker positions** presented another interesting design challenge. I considered having cells manage worker positions but ultimately decided to let workers manage their own positions while maintaining synchronized updates with the board. This decision required careful implementation of synchronization mechanisms but provided more intuitive position tracking and faster position queries.

5. Finally, I carefully considered **the distribution of action responsibilities between Player and Worker classes**. I decided that workers should execute the actual movement and building operations, while players control the strategic decisions about these actions. It resulted in a clearer separation of concerns that better reflects the natural flow of the game. Players make decisions about which workers to move and where to build, while workers handle the mechanical aspects of these actions.