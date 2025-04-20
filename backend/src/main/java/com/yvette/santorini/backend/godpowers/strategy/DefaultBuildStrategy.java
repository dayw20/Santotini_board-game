package com.yvette.santorini.backend.godpowers.strategy;

import com.yvette.santorini.backend.model.*;

public class DefaultBuildStrategy implements BuildStrategy {
    public boolean isValidBuild(Game game, Player player, Worker worker, Cell targetCell) {
        if (worker.getPosition() == null || targetCell == null) return false;
        if (!game.getBoard().isAdjacent(worker.getPosition(), targetCell)) return false;
        if (targetCell.isOccupied() || targetCell.getOccupancy() == Occupancy.DOME) return false;
        return true;
    }
    public void afterBuild(Game game, Player player, Worker worker, Cell target) {
        game.nextTurn();
        game.setPhase("selectingWorker");         // move to next player's turn by default
    }
}