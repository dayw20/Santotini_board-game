package com.yvette.santorini.backend.godpowers.strategy.impl;

import com.yvette.santorini.backend.godpowers.strategy.DefaultBuildStrategy;
import com.yvette.santorini.backend.model.*;

public class HephaestusBuildStrategy extends DefaultBuildStrategy {
    private Cell firstBuild = null;

    @Override
    public boolean isValidBuild(Game game, Player player, Worker worker, Cell targetCell) {
        if (!super.isValidBuild(game, player, worker, targetCell)) return false;

        // First build: anything valid
        if (firstBuild == null) return true;

        // Second build: must be same cell and no dome
        if (firstBuild != targetCell) return false;
        if (targetCell.getLevel() >= 3) return false; // Level 3 becomes dome → not allowed

        return true;
    }

    @Override
    public void afterBuild(Game game, Player player, Worker worker, Cell target) {
        if (firstBuild == null) {
            firstBuild = target;
            firstBuild.setHighlightType("FIRST_BUILD");  
            game.setPhase("optionalAction");
        } else {
            firstBuild.setHighlightType("NONE");       
            game.nextTurn();
            game.setPhase("selectingWorker");
        }
        
    }

    @Override
    public boolean allowsSecondBuild() {
        return true;
    }

    @Override
    public boolean wantsToBuildAgain() {
        return firstBuild != null;
    }

    @Override
    public void resetTurnState() {
        if (firstBuild != null) {
            firstBuild.setHighlightType("NONE");
        }
        firstBuild = null;
        
    }
}
