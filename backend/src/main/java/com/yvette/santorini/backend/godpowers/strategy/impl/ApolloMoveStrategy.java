package com.yvette.santorini.backend.godpowers.strategy.impl;

import com.yvette.santorini.backend.godpowers.strategy.DefaultMoveStrategy;
import com.yvette.santorini.backend.model.*;

public class ApolloMoveStrategy extends DefaultMoveStrategy {

    @Override
    public boolean isValidMove(Game game, Player player, Worker worker, Cell targetCell) {
        Cell current = worker.getPosition();
        if (current == null || targetCell == null) return false;
        if (!game.getBoard().isAdjacent(current, targetCell)) return false;
        if (targetCell.getOccupancy() == Occupancy.DOME) return false;

        Worker occupant = getWorkerAt(game, targetCell);
        return (occupant == null || occupant.getOwner() != player) && worker.canClimb(targetCell.getLevel());
    }

    @Override
    public void performMove(Game game, Player player, Worker worker, Cell from, Cell to) {
        Worker opponent = getWorkerAt(game, to);
        if (opponent != null && opponent.getOwner() != player) {
            // 1. Clear old positions
            from.setOccupancy(Occupancy.EMPTY);
            to.setOccupancy(Occupancy.EMPTY);

            // 2. Move opponent to 'from'
            opponent.setPosition(from);
            from.setOccupancy(Occupancy.WORKER);

            // 3. Move current player to 'to'
            worker.setPosition(to);
            to.setOccupancy(Occupancy.WORKER);
        } else {
            super.performMove(game, player, worker, from, to);
        }
    }



    private Worker getWorkerAt(Game game, Cell cell) {
        for (Worker w : game.getBoard().getAllWorkers()) {
            if (w.getPosition() == cell) return w;
        }
        return null;
    }
}
