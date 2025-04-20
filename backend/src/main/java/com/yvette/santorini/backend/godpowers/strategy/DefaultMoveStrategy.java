package com.yvette.santorini.backend.godpowers.strategy;

import com.yvette.santorini.backend.model.*;

public class DefaultMoveStrategy implements MoveStrategy {
    public boolean isValidMove(Game game, Player player, Worker worker, Cell targetCell) {
        Cell current = worker.getPosition();
        if (current == null || targetCell == null) {
            throw new IllegalArgumentException("Either current or targetCell is null");
        }
        if (!game.getBoard().isAdjacent(current, targetCell)) {
            throw new IllegalArgumentException("Invalid move: target is not adjacent.");
        }
        if (targetCell.isOccupied() || targetCell.getOccupancy() == Occupancy.DOME) {
            throw new IllegalArgumentException("Invalid move: target is occupied or domed.");
        }
        if (!worker.canClimb(targetCell.getLevel())) {
            throw new IllegalArgumentException("Invalid move: height too high to climb.");
        }
        return true;
    }
    public void afterMove(Game game, Player player, Worker worker, Cell from, Cell to) {}
}