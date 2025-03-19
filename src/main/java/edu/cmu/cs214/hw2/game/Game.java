package edu.cmu.cs214.hw2.game;

import java.util.ArrayList;
import java.util.List;
import edu.cmu.cs214.hw2.board.Board;
import edu.cmu.cs214.hw2.model.Cell;
import edu.cmu.cs214.hw2.model.Worker;
import edu.cmu.cs214.hw2.player.Player;

public class Game {
    private Board board;
    private List<Player> players;
    private Player currPlayer;
    private boolean gameOver;
    private Player winner;

    public Game() {
        this.board = new Board();
        this.players = new ArrayList<>();
        this.gameOver = false;
        this.winner = null;
    }

    public void startGame(Player playerA, Player playerB) {
        players.clear();
        players.add(playerA);
        players.add(playerB);
        currPlayer = playerA;
        gameOver = false;
        winner = null;
    }    

    /**
     * Places a worker on the board during game setup.
     * 
     * @param player The player placing the worker
     * @param worker The worker to place
     * @param cell The cell to place the worker on
     * @return true if placement was successful, false otherwise
     */
    public boolean placeWorker(Player player, Worker worker, Cell cell) {
        if (!player.ownsWorker(worker)) {
            return false;
        }
        if (cell.isOccupied()) {
            return false;
        }
        
        return board.placeWorker(worker, cell);
    }

    /**
     * Checks if a move is valid according to game rules.
     * 
     * @param worker The worker to move
     * @param targetCell The destination cell
     * @return true if the move is valid, false otherwise
     */
    private boolean isValidMove(Worker worker, Cell targetCell) {
        Cell currentPosition = worker.getPosition();
        
        // Worker must have a position
        if (currentPosition == null) {
            return false;
        }
        
        // Target cell must be valid and unoccupied
        if (targetCell == null || targetCell.isOccupied()) {
            return false;
        }
        
        // Target cell must be adjacent
        if (!board.isAdjacent(currentPosition, targetCell)) {
            return false;
        }
        
        // Worker must be able to climb to target level
        if (!worker.canClimb(targetCell.getLevel())) {
            return false;
        }
        
        return true;
    }

     /**
     * Validates and executes a move action.
     * 
     * @param player The player making the move
     * @param worker The worker to move
     * @param targetCell The destination cell
     * @return true if the move was successful, false otherwise
     */
    public boolean executeMove(Player player, Worker worker, Cell targetCell) {
        // Check turn order
        if (player != currPlayer) {
            return false;
        }
        // Check worker ownership
        if (!player.ownsWorker(worker)) {
            return false;
        }
        // Check move validity
        if (!isValidMove(worker, targetCell)) {
            return false;
        }
        
        // Execute the move
        worker.moveTo(targetCell);
        
        // Check win condition after move
        checkWinCondition();
        
        return true;
    }

    /**
     * Checks if a build is valid according to game rules.
     * 
     * @param worker The worker doing the building
     * @param targetCell The cell to build on
     * @return true if the build is valid, false otherwise
     */
    private boolean isValidBuild(Worker worker, Cell targetCell) {
        Cell workerPosition = worker.getPosition();
        
        // Worker must have a position
        if (workerPosition == null) {
            return false;
        }
        
        // Target cell must be valid and unoccupied
        if (targetCell == null || targetCell.isOccupied()) {
            return false;
        }
        
        // Target cell must be adjacent to worker
        if (!board.isAdjacent(workerPosition, targetCell)) {
            return false;
        }
        
        return true;
    }

    /**
     * Validates and executes a build action.
     * 
     * @param player The player building
     * @param worker The worker doing the building
     * @param targetCell The cell to build on
     * @return true if the build was successful, false otherwise
     */
    public boolean executeBuild(Player player, Worker worker, Cell targetCell) {
        // Check turn order
        if (player != currPlayer) {
            return false;
        }
        
        // Check worker ownership
        if (!player.ownsWorker(worker)) {
            return false;
        }
        
        // Check build validity
        if (!isValidBuild(worker, targetCell)) {
            return false;
        }
        
        // Execute the build
        worker.buildOn(targetCell);
        
        return true;
    }

    /**
     * Checks if a player has won the game after a move.
     * A player wins when their worker reaches level 3.
     */
    private void checkWinCondition() {
        for (Worker worker : board.getAllWorkers()) {
            Cell position = worker.getPosition();
            if (position != null && position.getLevel() == 3) {
                gameOver = true;
                winner = worker.getOwner();
                return;
            }
        }
    }

    /**
     * Advances to the next player's turn.
     */
    public void nextTurn() {
        currPlayer = (currPlayer == players.get(0)) ? players.get(1) : players.get(0);
    }

    /**
     * Gets the current winner, if any.
     * 
     * @return The winning player, or null if game is ongoing
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Checks if the game is over.
     * 
     * @return true if game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Gets the current player whose turn it is.
     * 
     * @return The current player
     */
    public Player getCurrPlayer() {
        return currPlayer;
    }

    /**
     * Gets the game board.
     * 
     * @return The game board
     */
    public Board getBoard() {
        return board;
    }

     /**
     * Gets all players in the game.
     * 
     * @return List of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

}
