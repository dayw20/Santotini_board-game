package com.yvette.santorini.backend.godpowers.strategy;

import com.yvette.santorini.backend.model.*;

public interface BuildStrategy {
    boolean isValidBuild(Game game, Player player, Worker worker, Cell targetCell);
    void afterBuild(Game game, Player player, Worker worker, Cell target);
    void performBuild(Game game, Player player, Worker worker, Cell targetCell, boolean buildDome);
    default void resetTurnState() {}
    default boolean allowsSecondBuild() { return false; }
    default boolean wantsToBuildAgain() { return false; }
}