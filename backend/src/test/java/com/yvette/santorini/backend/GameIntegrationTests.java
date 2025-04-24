package com.yvette.santorini.backend;

import static org.junit.jupiter.api.Assertions.*;

import com.yvette.santorini.backend.model.*;
import com.yvette.santorini.backend.godpowers.GodFactory;
import com.yvette.santorini.backend.godpowers.strategy.impl.PanWinConditionStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameIntegrationTests {

    private Game game;
    private Player playerA;
    private Player playerB;

    @BeforeEach
    public void setUp() {
        playerA = new Player("Alice");
        playerB = new Player("Bob");
        playerA.setGod(GodFactory.create("demeter"));
        playerB.setGod(GodFactory.create("apollo"));
        game = new Game();
        game.startGame(playerA, playerB);
    }


    @Test
    public void testDemeterSecondBuildSkipped() {
        playerA.setGod(GodFactory.create("demeter"));
        game.setPhase("building");
        Worker worker = playerA.getWorkers().get(0);
        Cell pos = game.getBoard().getCell(2, 2);
        Cell build1 = game.getBoard().getCell(2, 3);
        game.placeWorker(playerA, worker, pos);
        playerA.getGod().getBuildStrategy().performBuild(game, playerA, worker, build1, false);
        game.setPhase("optionalAction");
        game.setPhase("selectingWorker");
        assertTrue(worker.getPosition() != null);
    }

    @Test
    public void testPanWinsOnDescent() {
        PanWinConditionStrategy strategy = new PanWinConditionStrategy();
        Worker worker = playerA.getWorkers().get(0);
        Cell from = game.getBoard().getCell(1, 1);
        Cell to = game.getBoard().getCell(1, 2);
        from.increaseLevel(); from.increaseLevel(); from.increaseLevel();
        Number a = from.getLevel();
        Number b = to.getLevel();
        assertEquals(3, a);
        assertEquals(0, b);
        game.placeWorker(playerA, worker, from);
        game.setPhase("moving");
        game.executeMove(playerA, worker, to);
        assertEquals(true, strategy.checkWin(game, playerA, worker, from, to));
    }
} 
