package edu.cmu.cs214.hw2;

import java.util.Scanner;  
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
    private Scanner scanner;


    /**
     * Creates a new GameController instance.
     */
    public GameController() {
        this.game = new Game();
        this.scanner = new Scanner(System.in);
        this.view = new GameView(game);
    }
    
    /**
     * Starts and runs a complete Santorini game.
     */
    public void playGame() {
        initializeGame();
        runGameLoop();
        endGame();
    }
    
    /**
     * Initializes the game: set up players and workers.
     */
    private void initializeGame() {
        view.displayWelcomeMessage();

        System.out.print("Enter name for Player A: ");
        Player playerA = new Player(scanner.nextLine());
        System.out.print("Enter name for Player B: ");
        Player playerB = new Player(scanner.nextLine());

        game.startGame(playerA, playerB);
        placeWorkersForPlayer(playerA);
        placeWorkersForPlayer(playerB);
    }

    /**
     * Places the workers in start position for a player.
     * 
     * @param player The player whose workers need to be placed
     */
    private void placeWorkersForPlayer(Player player) {
        System.out.println("\n" + player.getName() + ", please place your workers:");
        for (int i = 0; i < 2; i++) {
            Worker worker = player.getWorkers().get(i);
            boolean placed = false;
            view.displayBoard();
            while (!placed) {
                System.out.printf("Enter position for worker %d (x y), e.g., '1 2': ", i + 1);
                try {
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    
                    Cell cell = game.getBoard().getCell(x, y);
                    if (game.placeWorker(player, worker, cell)) {
                        placed = true;
                    } else {
                        view.displayOccupiedPositionMessage();
                    }
                } catch (IllegalArgumentException e) {
                    view.displayInvalidCoordinatesMessage();
                    scanner.nextLine(); 
                }
            }
        }
        scanner.nextLine(); 
    }    

    /**
     * Runs the main game loop.
     */
    private void runGameLoop() {
        while (!game.isGameOver()) {
            view.displayBoard();
            Player currentPlayer = game.getCurrPlayer();
            view.displayCurrentTurn(currentPlayer);
            
            Worker selectedWorker = selectWorkerForPlayer(currentPlayer);
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
     * Makes the player select a worker.
     * 
     * @param player The current player
     * @return The selected worker
     */
    private Worker selectWorkerForPlayer(Player player) {
        view.displayBoard();
        while (true) {
            view.displayWorkerSelectionPrompt();
            try {
                int workerNum = scanner.nextInt();
                if (workerNum == 1 || workerNum == 2) {
                    return player.getWorkers().get(workerNum - 1);
                }
                System.out.println("Please enter 1 or 2.");
            } catch (Exception e) {
                view.displayInvalidInputMessage();
                scanner.nextLine(); 
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
        view.displayBoard();
        while (!moved) {
            view.displayMovePrompt();
            try {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                
                Cell targetCell = game.getBoard().getCell(x, y);
                if (game.executeMove(player, worker, targetCell)) {
                    moved = true;
                } else {
                    view.displayInvalidMoveMessage();
                }
            } catch (IllegalArgumentException e) {
                view.displayInvalidCoordinatesMessage();
                scanner.nextLine(); 
            } catch (Exception e) {
                view.displayInvalidInputMessage();
                scanner.nextLine(); 
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
        view.displayBoard();
        while (!built) {
            view.displayBuildPrompt();
            try {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                
                Cell targetCell = game.getBoard().getCell(x, y);
                if (game.executeBuild(player, worker, targetCell)) {
                    built = true;
                } else {
                    view.displayInvalidBuildMessage();
                }
            } catch (IllegalArgumentException e) {
                view.displayInvalidCoordinatesMessage();
                scanner.nextLine(); 
            } catch (Exception e) {
                view.displayInvalidInputMessage();
                scanner.nextLine(); 
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
