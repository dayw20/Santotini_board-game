package com.yvette.santorini.backend.service;

import org.springframework.stereotype.Service;
import com.yvette.santorini.backend.model.*;
import com.yvette.santorini.backend.dto.*;
import com.yvette.santorini.backend.godpowers.GodFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing the Santorini game logic.
 */
@Service
public class GameService {
    
    private Game game;
    private Worker findWorkerAt(Cell cell) {
        for (Worker w : game.getBoard().getAllWorkers()) {
            if (w.getPosition() == cell) return w;
        }
        return null;
    }
    
    
    public GameService() {
        this.game = new Game();
    }
    
    /**
     * Start a new game with the given players.
     */
    public void startGame(String playerAName, String playerBName, String godA, String godB) {
        Player playerA = new Player(playerAName);
        Player playerB = new Player(playerBName);
        playerA.setGod(GodFactory.create(godA));
        playerB.setGod(GodFactory.create(godB));
        
        game = new Game();
        game.startGame(playerA, playerB);
        game.setPhase("placingWorker");
    }
    
    /**
     * Get the current state of the game.
     */
    public GameStateResponse getGameState() {
        GameStateResponse response = new GameStateResponse();
        
        if (game.getCurrPlayer() != null) {
            response.setCurrentPlayer(game.getCurrPlayer().getName());
        }
        
        if (game.getWinner() != null) {
            response.setWinner(game.getWinner().getName());
        }
        
        response.setGameOver(game.isGameOver());
        response.setPhase(game.getPhase());
        response.setBoard(getBoardState());

        List<String> playerNames = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            playerNames.add(player.getName());
        }
        response.setPlayers(playerNames);
        
        return response;
    }
    
    /**
     * Place a worker on the board.
     * 
     * @param request The request containing placement information
     * @return true if placement was successful, false otherwise
     * @throws IllegalArgumentException if invalid coordinates or player
     */
    public boolean placeWorker(PlaceWorkerRequest request) {
        Player player = findPlayerByName(request.getPlayerName());
        if (player == null) {
            throw new IllegalArgumentException("Player not found");
        }
        
        if (request.getWorkerIndex() < 0 || request.getWorkerIndex() >= player.getWorkers().size()) {
            throw new IllegalArgumentException("Invalid worker index");
        }
        
        Worker worker = player.getWorkers().get(request.getWorkerIndex());
        Cell cell = game.getBoard().getCell(request.getX(), request.getY());
        
        if (game.placeWorker(player, worker, cell)) {
            // Check if we should move to the next player or phase
            int totalWorkersPlaced = 0;
            for (Player p : game.getPlayers()) {
                for (Worker w : p.getWorkers()) {
                    if (w.getPosition() != null) {
                        totalWorkersPlaced++;
                    }
                }
            }
            
            // All workers have been placed
            if (totalWorkersPlaced == 4) {
                game.nextTurn();
                game.setPhase("selectingWorker");
            } else if (totalWorkersPlaced == 2) {
                // First player has placed both workers, switch to second player
                game.nextTurn();
            }
            
            return true;
        }
        
        return false;
    }

    /**
     * Select a worker for the current player.
     * 
     * @param request The request containing worker selection information
     * @return true if selection was successful, false otherwise
     * @throws IllegalArgumentException if invalid player or worker
     */
    public boolean selectWorker(WorkerSelectionRequest request) {
        Player player = findPlayerByName(request.getPlayerName());
        if (player == null || player != game.getCurrPlayer()) {
            throw new IllegalArgumentException("Not your turn or player not found");
        }
        
        if (request.getWorkerIndex() < 0 || request.getWorkerIndex() >= player.getWorkers().size()) {
            throw new IllegalArgumentException("Invalid worker index");
        }
        
        // Check if the phase is correct
        if (!"selectingWorker".equals(game.getPhase())) {
            throw new IllegalArgumentException("Cannot select worker at this phase");
        }
        
        // Change the game phase to moving
        game.setPhase("moving");
        
        return true;
    }
    
    /**
     * Move a worker on the board.
     * 
     * @param request The request containing move information
     * @return true if move was successful, false otherwise
     * @throws IllegalArgumentException if invalid coordinates or player
     */
    public void moveWorker(MoveRequest request) {
        Player player = findPlayerByName(request.getPlayerName());
        if (player == null || player != game.getCurrPlayer()) {
            throw new IllegalArgumentException("Not your turn or player not found");
        }
    
        if (request.getWorkerIndex() < 0 || request.getWorkerIndex() >= player.getWorkers().size()) {
            throw new IllegalArgumentException("Invalid worker index");
        }
    
        Worker worker = player.getWorkers().get(request.getWorkerIndex());
        Cell targetCell = game.getBoard().getCell(request.getTargetX(), request.getTargetY());
    
   
        if (!player.getGod().getMoveStrategy().isValidMove(game, player, worker, targetCell)) {
            throw new IllegalArgumentException("Invalid move");
        }
    
        Cell fromCell = worker.getPosition();
        if (targetCell.isOccupied()) {
            Worker opponent = findWorkerAt(targetCell);
            if (opponent != null && opponent.getOwner() != player) {
                int dx = targetCell.getX() - fromCell.getX();
                int dy = targetCell.getY() - fromCell.getY();
                int pushX = targetCell.getX() + dx;
                int pushY = targetCell.getY() + dy;
        
                if (!game.getBoard().isValidPosition(pushX, pushY)) {
                    throw new IllegalArgumentException("Invalid move: push off board");
                }
        
                Cell pushCell = game.getBoard().getCell(pushX, pushY);
                if (pushCell.isOccupied() || pushCell.getOccupancy() == Occupancy.DOME) {
                    throw new IllegalArgumentException("Invalid move: cannot push into occupied cell");
                }
        
                //  Push opponent first
                opponent.moveTo(pushCell);
            }
        }
        
        // Now move current worker
        worker.moveTo(targetCell);
        player.getGod().getMoveStrategy().afterMove(game, player, worker, fromCell, targetCell);
    
        if (player.getGod().getWinConditionStrategy().checkWin(game, player, worker, fromCell, targetCell)) {
            game.setPhase("end");
            game.setWinner(player);
        } else {
            game.setPhase("building");
        }
    }
    
    
    /**
     * Build with a worker on the board.
     * 
     * @param request The request containing build information
     * @return true if build was successful, false otherwise
     * @throws IllegalArgumentException if invalid coordinates or player
     */
    public boolean buildTower(BuildRequest request) {
        Player player = findPlayerByName(request.getPlayerName());
        if (player == null || player != game.getCurrPlayer()) {
            throw new IllegalArgumentException("Not your turn or player not found");
        }
        
        if (request.getWorkerIndex() < 0 || request.getWorkerIndex() >= player.getWorkers().size()) {
            throw new IllegalArgumentException("Invalid worker index");
        }
        
        Worker worker = player.getWorkers().get(request.getWorkerIndex());
        Cell targetCell = game.getBoard().getCell(request.getTargetX(), request.getTargetY());
        
        if (!player.getGod().getBuildStrategy().isValidBuild(game, player, worker, targetCell)) {
            return false;
        }

        worker.buildOn(targetCell);
        player.getGod().getBuildStrategy().afterBuild(game, player, worker, targetCell);

        return true;
    }
    
    /**
     * Convert the current board state to a list of CellState objects.
     */
    private List<CellState> getBoardState() {
        List<CellState> cellStates = new ArrayList<>();
        Board board = game.getBoard();
        
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Cell cell = board.getCell(x, y);
                CellState cellState = new CellState();
                
                cellState.setX(x);
                cellState.setY(y);
                cellState.setLevel(cell.getLevel());
                cellState.setHighlightType(cell.getHighlightType());  
                
                // Set occupancy
                switch (cell.getOccupancy()) {
                    case EMPTY:
                        cellState.setOccupancy("EMPTY");
                        break;
                    case WORKER:
                        cellState.setOccupancy("WORKER");
                        // Find which player and worker is on this cell
                        for (Player player : game.getPlayers()) {
                            for (int i = 0; i < player.getWorkers().size(); i++) {
                                Worker worker = player.getWorkers().get(i);
                                if (worker.getPosition() == cell) {
                                    cellState.setPlayer(player.getName());
                                    cellState.setWorkerIndex(i);
                                    break;
                                }
                            }
                        }
                        break;
                    case DOME:
                        cellState.setOccupancy("DOME");
                        break;
                }
                
                cellStates.add(cellState);
            }
        }
        
        return cellStates;
    }

    public Player findPlayerByName(String name) {
        for (Player player : game.getPlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }
    
    // Add a getter for the game object if needed
    public Game getGame() {
        return game;
    }

    /**
     * Handle skip/pass on second build phase (e.g., Demeter).
     */
    public void passSecondBuild() {
        game.getCurrPlayer().getGod().getBuildStrategy().resetTurnState();
        game.nextTurn();
        game.setPhase("selectingWorker");
    }
}