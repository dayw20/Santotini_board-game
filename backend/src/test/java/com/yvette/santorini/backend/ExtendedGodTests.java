package com.yvette.santorini.backend;

import static org.junit.jupiter.api.Assertions.*;

import com.yvette.santorini.backend.model.*;
import com.yvette.santorini.backend.godpowers.strategy.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExtendedGodTests {

    private Game game;
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("Atlas");
        game = new Game();
        game.startGame(player, new Player("Dummy"));
    }

    @Test
    public void testAtlasCanBuildDomeAtLevel1() {
        AtlasBuildStrategy strategy = new AtlasBuildStrategy();
        game.setPhase("building");
        Worker worker = player.getWorkers().get(0);
        Cell pos = game.getBoard().getCell(1, 1);
        Cell target = game.getBoard().getCell(1, 2);
        game.placeWorker(player, worker, pos);

        assertTrue(strategy.isValidBuild(game, player, worker, target));
        strategy.performBuild(game, player, worker, target, true); 

        assertEquals(Occupancy.DOME, target.getOccupancy());
        assertEquals(0, target.getLevel()); 
    }

    @Test
    public void testDemeterBuildSecondDifferentCellValid() {
        DemeterBuildStrategy strategy = new DemeterBuildStrategy();
        Worker worker = player.getWorkers().get(0);
        Cell pos = game.getBoard().getCell(2, 2);
        Cell first = game.getBoard().getCell(2, 3);
        Cell second = game.getBoard().getCell(3, 2);
        game.placeWorker(player, worker, pos);

        assertTrue(strategy.isValidBuild(game, player, worker, first));
        strategy.performBuild(game, player, worker, first, false);
        assertTrue(strategy.isValidBuild(game, player, worker, second));
        strategy.performBuild(game, player, worker, second, false);
    }
} 
