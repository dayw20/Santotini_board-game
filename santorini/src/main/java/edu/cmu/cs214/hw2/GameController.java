package edu.cmu.cs214.hw2;

import edu.cmu.cs214.hw2.player.Player;
import edu.cmu.cs214.hw2.model.Worker;
import edu.cmu.cs214.hw2.game.Game;
import edu.cmu.cs214.hw2.model.Cell;
import edu.cmu.cs214.hw2.model.Occupancy;

import java.util.Scanner;

public class GameController {
    private Game game;
    private Scanner scanner;

    public GameController() {
        this.game = new Game();
        this.scanner = new Scanner(System.in);
    }
    
    public void playGame() {
        initialGame();
        runGame();
        endGame();
    }
    
    private void initialGame() {
        System.out.println("Welcome to Santorini!");
        System.out.println();
        System.out.println("Build your way to victory in this strategic game of towers and workers.");
        System.out.println("Place your workers, move wisely, and construct towers to reach the top.");
        System.out.println("Be the first to climb to level 3 and claim your victory!");
        System.out.println();
        System.out.println("Will you outsmart your opponent and become the master builder?");
        System.out.println("Let the game begin!");

        System.out.print("Enter name for Player A: ");
        Player playerA = new Player(scanner.nextLine());
        System.out.print("Enter name for Player B: ");
        Player playerB = new Player(scanner.nextLine());

        game.startGame(playerA, playerB);
        setStartPlace(playerA);
        setStartPlace(playerB);
    }

    private void setStartPlace(Player player) {
        System.out.println("\n" + player.getName() + ", please place your workers:");
        for (int i = 0; i < 2; i++) {
            Worker worker = player.getWorkers().get(i);
            boolean placed = false;
            displayBoard();
            while (!placed) {
                System.out.printf("Enter position for worker %d (x y), e.g., '1 2': ", i + 1);
                try {
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    
                    Cell cell = game.getBoard().getCell(x, y);
                    if (game.getBoard().placeWorker(worker, cell)) {
                        placed = true;
                    } else {
                        System.out.println("Position occupied. Try again.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid coordinates. Must be between 0 and 4.");
                    scanner.nextLine(); 
                }
            }
        }
        scanner.nextLine();
    }    
    private void runGame() {
        while (!game.isGameOver()) {
            displayBoard();
            playTurn(game.getCurrPlayer());
            if (!game.isGameOver()) {
                game.nextTurn();
            }
        }
    }

    private void displayBoard() {
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
                if (cell.getOccupancy() == Occupancy.WORKER) {
                    Worker w = findWorkerAtPosition(cell);
                    if (w != null) {
                        char playerChar = (w.getOwner() == game.getPlayers().get(0)) ? 'A' : 'B';
                        int workerNum = w.getOwner().getWorkers().indexOf(w) + 1;
                        System.out.printf("[%c%d]", playerChar, workerNum);
                    }
                } else if (cell.getOccupancy() == Occupancy.DOME) {
                    System.out.print("[D ]");
                } else {
                    System.out.printf("[%d ]", cell.getLevel());
                }
            }
            System.out.println();
        }
    }

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

    private void playTurn(Player player) {
        System.out.println("Now is" + "\n" + player.getName() + "'s turn:");
        Worker worker = selectWorker(player);
        moveWorker(player, worker);
        buildTower(player, worker);
    }
    private Worker selectWorker(Player player) {
        displayBoard();
        while (true) {
            System.out.print("Select the worker you want to move in this turn (1 or 2): ");
            try {
                int workerNum = scanner.nextInt();
                if (workerNum == 1 || workerNum == 2) {
                    return player.getWorkers().get(workerNum - 1);
                }
                System.out.println("Please enter 1 or 2.");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                scanner.nextLine(); 
            }
        }
    }

    private void moveWorker(Player player, Worker worker) {
        boolean moved = false;
        displayBoard();
        while (! moved) {
            System.out.print("Enter the position(x y) you want to move to, the worker can only move to an adjacent unoccupied field: e.g., '1 2'");
            try {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                
                Cell targetCell = game.getBoard().getCell(x, y);
                if (worker.canMove(targetCell)) {
                    player.moveWorker(worker, targetCell);
                    moved = true;
                } else {
                    System.out.println("Invalid move. Worker can only move to adjacent cells and climb up max 1 level.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Coordinates must be between 0 and 4.");
                scanner.nextLine(); 
            }
        }
    } 

    private void buildTower(Player player, Worker worker) {
        boolean built = false;
        displayBoard();
        while (!built) {
            System.out.print("Enter the position(x y) you want to build, worker can only to build on adjacent unoccupied field or tower without dome: e.g., '1 2': ");
            try {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                
                Cell targetCell = game.getBoard().getCell(x, y);
                if (worker.canBuild(targetCell)) {
                    player.buildWithWorker(worker, targetCell);
                    built = true;
                } else {
                    System.out.println("Invalid build position. Must be adjacent and unoccupied.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid coordinates. Must be between 0 and 4.");
                scanner.nextLine(); 
            }
        }
    }

    private void endGame() {
        Player winner = game.getWinner();
        if (winner != null) {
            System.out.println("\nCongratulations " + winner.getName() + "! You've won!");
        }
    }
}
