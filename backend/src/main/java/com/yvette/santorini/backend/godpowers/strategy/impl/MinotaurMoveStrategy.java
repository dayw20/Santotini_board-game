package com.yvette.santorini.backend.godpowers.strategy.impl;

import com.yvette.santorini.backend.godpowers.strategy.DefaultMoveStrategy;
import com.yvette.santorini.backend.model.*;

public class MinotaurMoveStrategy extends DefaultMoveStrategy {

    @Override
    public boolean isValidMove(Game game, Player player, Worker worker, Cell targetCell) {
        Cell current = worker.getPosition();
        if (current == null || targetCell == null) return false;
        if (!game.getBoard().isAdjacent(current, targetCell)) return false;
        if (targetCell.getOccupancy() == Occupancy.DOME) return false;

        // Normal move: target cell is empty
        if (!targetCell.isOccupied()) {
            return worker.canClimb(targetCell.getLevel());
        }

        // Minotaur push: target occupied by opponent
        Worker other = getWorkerAt(game, targetCell);
        if (other == null || other.getOwner() == player) return false;

        // Calculate push direction
        int dx = targetCell.getX() - current.getX();
        int dy = targetCell.getY() - current.getY();
        int pushX = targetCell.getX() + dx;
        int pushY = targetCell.getY() + dy;

        if (!game.getBoard().isValidPosition(pushX, pushY)) return false;

        Cell pushCell = game.getBoard().getCell(pushX, pushY);
        if (pushCell.isOccupied() || pushCell.getOccupancy() == Occupancy.DOME) return false;

        // Must still follow climb rule
        return worker.canClimb(targetCell.getLevel());
    }

    @Override
    public void performMove(Game game, Player player, Worker worker, Cell from, Cell to) {
        Worker opponent = getWorkerAt(game, to);
        if (opponent != null && opponent.getOwner() != player) {
            int dx = to.getX() - from.getX();
            int dy = to.getY() - from.getY();
            int pushX = to.getX() + dx;
            int pushY = to.getY() + dy;
            Cell pushCell = game.getBoard().getCell(pushX, pushY);
            opponent.moveTo(pushCell);
        }
        worker.moveTo(to); // Move player into the old opponent’s position
    }

    @Override
    public void afterMove(Game game, Player player, Worker worker, Cell from, Cell to) {
        Worker opponent = getWorkerAt(game, to);
        if (opponent != null && opponent.getOwner() != player) {
            int dx = to.getX() - from.getX();
            int dy = to.getY() - from.getY();
            int pushX = to.getX() + dx;
            int pushY = to.getY() + dy;
            Cell pushCell = game.getBoard().getCell(pushX, pushY);

            opponent.moveTo(pushCell);  
        }
    }



    private Worker getWorkerAt(Game game, Cell cell) {
        for (Worker w : game.getBoard().getAllWorkers()) {
            if (w.getPosition() == cell) return w;
        }
        return null;
    }
}
