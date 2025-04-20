package com.yvette.santorini.backend.godpowers.strategy;

import com.yvette.santorini.backend.model.*;

public interface MoveStrategy {
    boolean isValidMove(Game game, Player player, Worker worker, Cell targetCell);
    void afterMove(Game game, Player player, Worker worker, Cell from, Cell to);
}