package com.yvette.santorini.backend.godpowers.strategy.impl;

import com.yvette.santorini.backend.godpowers.strategy.DefaultBuildStrategy;
import com.yvette.santorini.backend.model.*;

public class AtlasBuildStrategy extends DefaultBuildStrategy {
    private boolean buildDome = false;

    public void enableDome() {
        this.buildDome = true;
    }

    @Override
    public void afterBuild(Game game, Player player, Worker worker, Cell target) {
        if (buildDome) {
            target.setOccupancy(Occupancy.DOME);
            game.nextTurn();
            game.setPhase("selectingWorker");
        } else {
            super.afterBuild(game, player, worker, target);  
        }
    
        buildDome = false;
    }

    @Override
    public boolean isValidBuild(Game game, Player player, Worker worker, Cell target) {
        if (!super.isValidBuild(game, player, worker, target)) return false;
        if (buildDome) {
            return target.getOccupancy() != Occupancy.DOME;
        }
        return true;
    }

    @Override
    public void performBuild(Game game, Player player, Worker worker, Cell targetCell, boolean buildDome) {
        if (buildDome) {
            targetCell.setOccupancy(Occupancy.DOME);  
        } else {
            worker.buildOn(targetCell);
        }
    }

    @Override
    public void resetTurnState() {
        buildDome = false;
    }

    public boolean isDomeMode() {
        return buildDome;
    }
}
