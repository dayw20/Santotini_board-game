package com.yvette.santorini.backend;

import static org.junit.jupiter.api.Assertions.*;

import com.yvette.santorini.backend.model.*;
import com.yvette.santorini.backend.godpowers.strategy.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GodCardStrategyTests {

    private Game game;
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("Apollo");
        game = new Game();
        game.startGame(player, new Player("Dummy"));
    }

    @Test
    public void testApolloCanSwapWithOpponent() {
        ApolloMoveStrategy strategy = new ApolloMoveStrategy();
        Cell start = game.getBoard().getCell(1, 1);
        Cell target = game.getBoard().getCell(1, 2);
        Worker apolloWorker = player.getWorkers().get(0);
        Worker enemyWorker = new Player("Enemy").getWorkers().get(0);
        game.placeWorker(player, apolloWorker, start);
        game.placeWorker(new Player("Enemy"), enemyWorker, target);
        assertTrue(strategy.isValidMove(game, player, apolloWorker, target));
        strategy.performMove(game, player, apolloWorker, start, target);
        assertEquals(target, apolloWorker.getPosition());
    }

    @Test
    public void testHephaestusDoubleBuild() {
        HephaestusBuildStrategy strategy = new HephaestusBuildStrategy();
        game.setPhase("building");
        Worker worker = player.getWorkers().get(0);
        Cell pos = game.getBoard().getCell(2, 2);
        Cell target = game.getBoard().getCell(2, 3);
        game.placeWorker(player, worker, pos);
        strategy.performBuild(game, player, worker, target, false);
        assertTrue(strategy.isValidBuild(game, player, worker, target));
    }

    @Test
    public void testPanWinByDescending() {
        PanWinConditionStrategy strategy = new PanWinConditionStrategy();
        Cell from = game.getBoard().getCell(1, 1);
        Cell to = game.getBoard().getCell(1, 2);
        Worker worker = player.getWorkers().get(0);
        from.increaseLevel(); from.increaseLevel(); from.increaseLevel();
        game.placeWorker(player, worker, from);
        assertTrue(strategy.checkWin(game, player, worker, from, to));
    }
} 
