package com.yvette.santorini.backend.godpowers.strategy;

import com.yvette.santorini.backend.model.*;

public interface MoveStrategy {
    boolean isValidMove(Game game, Player player, Worker worker, Cell targetCell);
    default void performMove(Game game, Player player, Worker worker, Cell from, Cell to) {
        worker.moveTo(to);
    }
    void afterMove(Game game, Player player, Worker worker, Cell from, Cell to);
}