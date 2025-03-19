package edu.cmu.cs214.hw2;

import java.util.Scanner;  
import edu.cmu.cs214.hw2.game.Game;
import edu.cmu.cs214.hw2.model.Cell;
import edu.cmu.cs214.hw2.model.Occupancy;
import edu.cmu.cs214.hw2.model.Worker;
import edu.cmu.cs214.hw2.player.Player;

/**
 * Handles the display and input for the Santorini game.
 */
public class GameView {
    private Game game;
    private Scanner scanner;

    /**
     * Creates a new {@link GameView} instance.
     * 
     * @param game The game to display
     */
    public GameView(Game game) {
        this.game = game;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads an integer input safely.
     * @return The valid integer input
     */
    private int getValidInteger() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer.");
            scanner.next(); 
        }
        return scanner.nextInt();
    }

    /**
     * Reads a player's name from input.
     * @param prompt Message to display before input
     * @return The player's name
     */
    public String getPlayerName(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads worker placement coordinates.
     * @param playerName The name of the player placing the worker
     * @param workerNum The worker number (1 or 2)
     * @return An integer array {x, y} representing coordinates
     */
    public int[] getWorkerPlacement(String playerName, int workerNum) {
        while (true) {
            System.out.printf("\n%s, enter position for worker %d (x y), e.g., '1 2': ", playerName, workerNum);
            int x = getValidInteger();
            int y = getValidInteger();
            
            if (x >= 0 && x < 5 && y >= 0 && y < 5) {
                return new int[]{x, y};
            }
            System.out.println("Invalid coordinates. Must be between 0 and 4.");
        }
    }

    /**
     * Reads a worker selection (1 or 2).
     * @return The worker number (1 or 2)
     */
    public int getWorkerSelection() {
        while (true) {
            while (true) {
                System.out.print("Select the worker you want to move (1 or 2): ");
                int workerNum = getValidInteger();
                if (workerNum == 1 || workerNum == 2) {
                    return workerNum;
                }
                System.out.println("Please enter 1 or 2.");
            }
        }
    }

    /**
     * Reads a move/build position.
     * @param actionType "move" or "build"
     * @return An integer array {x, y} representing coordinates
     */
    public int[] getPosition(String actionType) {
        while (true) {
            System.out.printf("Enter the position (x y) where you want to %s: ", actionType);
            int x = getValidInteger();
            int y = getValidInteger();
            
            if (x >= 0 && x < 5 && y >= 0 && y < 5) {
                return new int[]{x, y};
            }
            System.out.println("Invalid coordinates. Must be between 0 and 4.");
        }
    }

    /**
     * Closes the scanner when the game ends.
     */
    public void closeScanner() {
        scanner.close();
    }

    /**
     * Displays the welcome message and game instructions.
     */
    public void displayWelcomeMessage() {
        System.out.println("Welcome to Santorini!");
        System.out.println();
        System.out.println("Build your way to victory in this strategic game of towers and workers.");
        System.out.println("Place your workers, move wisely, and construct towers to reach the top.");
        System.out.println("Be the first to climb to level 3 and claim your victory!");
        System.out.println();
        System.out.println("Will you outsmart your opponent and become the master builder?");
        System.out.println("Let the game begin!");
    }
    
    /**
     * Displays the current state of the game board.
     */
    public void displayBoard() {
        System.out.println("\nCurrent board state⬇:");
        System.out.println("[D] - Dome");
        System.out.println("[An] - Player A's worker n (n=1,2)");
        System.out.println("[Bn] - Player B's worker n (n=1,2)");
        System.out.println("[n] - Building level n (0-3)");
        System.out.println();
        
        System.out.println("    0   1   2   3   4  ");
        for (int y = 0; y < 5; y++) {
            System.out.print(y + " ");
            for (int x = 0; x < 5; x++) {
                Cell cell = game.getBoard().getCell(x, y);
                displayCell(cell);
            }
            System.out.println();
        }
    }

    /**
     * Displays a single cell on the board.
     * 
     * @param cell The cell to display
     */
    private void displayCell(Cell cell) {
        if (cell.getOccupancy() == Occupancy.WORKER) {
            Worker worker = findWorkerAtPosition(cell);
            if (worker != null) {
                char playerChar = (worker.getOwner() == game.getPlayers().get(0)) ? 'A' : 'B';
                int workerNum = worker.getOwner().getWorkers().indexOf(worker) + 1;
                System.out.printf("[%c%d]", playerChar, workerNum);
            } else {
                System.out.print("[??]"); // Should never happen
            }
        } else if (cell.getOccupancy() == Occupancy.DOME) {
            System.out.print("[D ]");
        } else {
            System.out.printf("[%d ]", cell.getLevel());
        }
    }

    /**
     * Finds the worker at a given cell position.
     * 
     * @param cell The cell to check
     * @return The worker at the position, or null if no worker found
     */
    private Worker findWorkerAtPosition(Cell cell) {
        for (Player player : game.getPlayers()) {
            for (Worker worker : player.getWorkers()) {
                if (worker.getPosition() == cell) {
                    return worker;
                }
            }
        }
        return null;
    }

    /**
     * Displays whose turn it is.
     * 
     * @param player The current player
     */
    public void displayCurrentTurn(Player player) {
        System.out.println("\nNow is " + player.getName() + "'s turn:");
    }

    /**
     * Displays a message for worker selection.
     */
    public void displayWorkerSelectionPrompt() {
        System.out.print("Select the worker you want to move in this turn (1 or 2): ");
    }

    /**
     * Displays a message for move action.
     */
    public void displayMovePrompt() {
        System.out.print("Enter the position(x y) you want to move to, the worker can only move to an adjacent unoccupied field: e.g., '1 2': ");
    }

    /**
     * Displays a message for build action.
     */
    public void displayBuildPrompt() {
        System.out.print("Enter the position(x y) you want to build, worker can only to build on adjacent unoccupied field or tower without dome: e.g., '1 2': ");
    }

    /**
     * Displays a message for worker placement.
     * 
     * @param playerName The player's name
     * @param workerNum The worker number
     */
    public void displayWorkerPlacementPrompt(String playerName, int workerNum) {
        System.out.println("\n" + playerName + ", please place your workers:");
        System.out.printf("Enter position for worker %d (x y), e.g., '1 2': ", workerNum);
    }

    /**
     * Displays an error message for invalid move.
     */
    public void displayInvalidMoveMessage() {
        System.out.println("Invalid move. Worker can only move to adjacent cells and climb up max 1 level.");
    }

    /**
     * Displays an error message for invalid build.
     */
    public void displayInvalidBuildMessage() {
        System.out.println("Invalid build position. Must be adjacent and unoccupied.");
    }

    /**
     * Displays an error message for occupied position.
     */
    public void displayOccupiedPositionMessage() {
        System.out.println("Position occupied. Try again.");
    }

    /**
     * Displays an error message for invalid coordinates.
     */
    public void displayInvalidCoordinatesMessage() {
        System.out.println("Invalid coordinates. Must be between 0 and 4.");
    }

    /**
     * Displays an error message for invalid input.
     */
    public void displayInvalidInputMessage() {
        System.out.println("Invalid input. Please try again.");
    }

    /**
     * Displays the winner announcement.
     * 
     * @param winner The winning player
     */
    public void displayWinner(Player winner) {
        System.out.println("\nCongratulations " + winner.getName() + "! You've won!");
    }
}
