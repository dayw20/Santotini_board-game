package edu.cmu.cs214.hw2;

import edu.cmu.cs214.hw2.player.Player;
import edu.cmu.cs214.hw2.model.Worker;
import edu.cmu.cs214.hw2.game.Game;
import edu.cmu.cs214.hw2.model.Cell;


/**
 * Control the Santorini game.
 * Handle user input and game state.
 */
public class GameController {
    private Game game;
    private GameView view;

    /**
     * Creates a new GameController instance.
     */
    public GameController() {
        this.game = new Game();
        this.view = new GameView(game);
    }
    
    /**
     * Starts and runs a complete Santorini game.
     */
    public void playGame() {
        initializeGame();
        runGameLoop();
        endGame();
        view.closeScanner();
    }
    
    /**
     * Initializes the game: set up players and workers.
     */
    private void initializeGame() {
        view.displayWelcomeMessage();

        String nameA = view.getPlayerName("Enter name for Player A: ");
        String nameB = view.getPlayerName("Enter name for Player B: ");
        Player playerA = new Player(nameA);
        Player playerB = new Player(nameB);

        game.startGame(playerA, playerB);
        placeWorkersForPlayer(playerA);
        placeWorkersForPlayer(playerB);
    }

    /**
     * Places workers on the board.
     */
    private void placeWorkersForPlayer(Player player) {
        for (int i = 0; i < 2; i++) {
            boolean placed = false;
            view.displayBoard();
            while (!placed) {
                int[] coords = view.getWorkerPlacement(player.getName(), i + 1);
                Cell cell = game.getBoard().getCell(coords[0], coords[1]);
                if (game.placeWorker(player, player.getWorkers().get(i), cell)) {
                    placed = true;
                } else {
                    view.displayOccupiedPositionMessage();
                }
            }
        }
    }

    /**
     * Runs the main game loop.
     */
    private void runGameLoop() {
        while (!game.isGameOver()) {
            view.displayBoard();
            Player currentPlayer = game.getCurrPlayer();
            view.displayCurrentTurn(currentPlayer);

            Worker selectedWorker = currentPlayer.getWorkers().get(view.getWorkerSelection() - 1);
            executeMove(currentPlayer, selectedWorker);
            if (game.isGameOver()) {
                break;
            }

            executeBuild(currentPlayer, selectedWorker);
            if (!game.isGameOver()) {
                game.nextTurn();
            }
        }
    }

    /**
     * Executes a move action for a player and worker.
     * 
     * @param player The current player
     * @param worker The selected worker
     */
    private void executeMove(Player player, Worker worker) {
        boolean moved = false;
        while (!moved) {
            int[] moveCoords = view.getPosition("move");
            Cell targetCell = game.getBoard().getCell(moveCoords[0], moveCoords[1]);
            if (game.executeMove(player, worker, targetCell)) {
                moved = true;
            } else {
                view.displayInvalidMoveMessage();
            }
        }
    }

    /**
     * Executes a build action for a player and worker.
     * 
     * @param player The current player
     * @param worker The selected worker
     */
    private void executeBuild(Player player, Worker worker) {
        boolean built = false;
        while (!built) {
            int[] buildCoords = view.getPosition("build");
            Cell targetCell = game.getBoard().getCell(buildCoords[0], buildCoords[1]);
            if (game.executeBuild(player, worker, targetCell)) {
                built = true;
            } else {
                view.displayInvalidBuildMessage();
            }
        }
    }
    /**
     * Handles the end of the game.
     */
    private void endGame() {
        view.displayBoard();
        Player winner = game.getWinner();
        if (winner != null) {
            view.displayWinner(winner);
        }
    }
}
