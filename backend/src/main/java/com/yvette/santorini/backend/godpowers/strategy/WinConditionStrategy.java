package com.yvette.santorini.backend.godpowers.strategy;

import com.yvette.santorini.backend.model.*;

public interface WinConditionStrategy {
    boolean checkWin(Game game, Player player, Worker worker, Cell from, Cell to);
}