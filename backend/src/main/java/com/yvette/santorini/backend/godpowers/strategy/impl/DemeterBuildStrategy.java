package com.yvette.santorini.backend.godpowers.strategy.impl;

import com.yvette.santorini.backend.godpowers.strategy.DefaultBuildStrategy;
import com.yvette.santorini.backend.model.*;

public class DemeterBuildStrategy extends DefaultBuildStrategy {
    private Cell firstBuild = null;

    @Override
    public boolean isValidBuild(Game game, Player player, Worker worker, Cell targetCell) {
        if (!super.isValidBuild(game, player, worker, targetCell)) return false;

        // Can't build on the same cell twice
        if (firstBuild != null && firstBuild == targetCell) return false;

        return true;
    }

    @Override
    public void afterBuild(Game game, Player player, Worker worker, Cell target) {
        if (firstBuild == null) {
            firstBuild = target;
            game.setPhase("optionalAction");
        } else {
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
        firstBuild = null;
    }

}
