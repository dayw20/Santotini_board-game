package com.yvette.santorini.backend.godpowers.strategy;

import com.yvette.santorini.backend.model.*;

public class DefaultBuildStrategy implements BuildStrategy {
    public boolean isValidBuild(Game game, Player player, Worker worker, Cell targetCell) {
        if (worker.getPosition() == null || targetCell == null) {
            throw new IllegalArgumentException("Invalid build: Either current or targetCell is null");
        }
        if (!game.getBoard().isAdjacent(worker.getPosition(), targetCell)) {
            throw new IllegalArgumentException("Invalid build: target is not adjacent.");
        }
        if (targetCell.isOccupied() || targetCell.getOccupancy() == Occupancy.DOME) {
            throw new IllegalArgumentException("Invalid build: target is occupied or domed.");
        }
        return true;
    }
    public void afterBuild(Game game, Player player, Worker worker, Cell target) {
        game.nextTurn();
        game.setPhase("selectingWorker");        
    }
    
    public void performBuild(Game game, Player player, Worker worker, Cell targetCell, boolean buildDome) {
        // Default: ignore buildDome flag, build level
        worker.buildOn(targetCell);
    }
}