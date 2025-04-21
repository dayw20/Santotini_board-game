package com.yvette.santorini.backend.godpowers.strategy.impl;

import com.yvette.santorini.backend.model.*;
import com.yvette.santorini.backend.godpowers.strategy.WinConditionStrategy;

public class PanWinConditionStrategy implements WinConditionStrategy {
    
    @Override
    public boolean checkWin(Game game, Player player, Worker worker, Cell from, Cell to) {
        // Standard win condition
        if (from.getLevel() == 2 && to.getLevel() == 3) {
            return true;
        }

        // Pan's additional win condition: move down 2 or more levels
        if (from.getLevel() - to.getLevel() >= 2) {
            return true;
        }

        return false;
    }
}
