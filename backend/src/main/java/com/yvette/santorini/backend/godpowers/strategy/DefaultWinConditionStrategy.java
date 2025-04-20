package com.yvette.santorini.backend.godpowers.strategy;

import com.yvette.santorini.backend.model.*;


public class DefaultWinConditionStrategy implements WinConditionStrategy {
    public boolean checkWin(Game game, Player player, Worker worker, Cell from, Cell to) {
        return from.getLevel() == 2 && to.getLevel() == 3;
    }
}