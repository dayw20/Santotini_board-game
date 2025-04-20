package com.yvette.santorini.backend.godpowers;

import com.yvette.santorini.backend.godpowers.strategy.*;

public class God {
    private final MoveStrategy moveStrategy;
    private final BuildStrategy buildStrategy;
    private final WinConditionStrategy winConditionStrategy;

    public God(MoveStrategy move, BuildStrategy build, WinConditionStrategy win) {
        this.moveStrategy = move;
        this.buildStrategy = build;
        this.winConditionStrategy = win;
    }

    public MoveStrategy getMoveStrategy() { return moveStrategy; }
    public BuildStrategy getBuildStrategy() { return buildStrategy; }
    public WinConditionStrategy getWinConditionStrategy() { return winConditionStrategy; }
}
