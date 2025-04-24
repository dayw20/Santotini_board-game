package com.yvette.santorini.backend;

import static org.junit.jupiter.api.Assertions.*;

import com.yvette.santorini.backend.model.*;
import com.yvette.santorini.backend.godpowers.strategy.impl.DemeterBuildStrategy;
import com.yvette.santorini.backend.godpowers.strategy.impl.MinotaurMoveStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTests {

    private Game game;
    private Player playerA;
    private Player playerB;

    @BeforeEach
    public void setUp() {
        playerA = new Player("Alice");
        playerB = new Player("Bob");
        game = new Game();
        game.startGame(playerA, playerB);
    }

    @Test
    public void testPlaceWorkerValid() {
        Cell cell = game.getBoard().getCell(0, 0);
        Worker worker = playerA.getWorkers().get(0);
        boolean result = game.placeWorker(playerA, worker, cell);
        assertTrue(result);
        assertEquals(cell, worker.getPosition());
        assertEquals(Occupancy.WORKER, cell.getOccupancy());
    }

    @Test
    public void testPlaceWorkerOnOccupiedCell() {
        Cell cell = game.getBoard().getCell(0, 0);
        Worker worker1 = playerA.getWorkers().get(0);
        Worker worker2 = playerA.getWorkers().get(1);
        assertTrue(game.placeWorker(playerA, worker1, cell));
        assertFalse(game.placeWorker(playerA, worker2, cell));
    }

    @Test
    public void testValidMove() {
        Cell start = game.getBoard().getCell(1, 1);
        Cell target = game.getBoard().getCell(1, 2);
        Worker worker = playerA.getWorkers().get(0);
        game.placeWorker(playerA, worker, start);
        boolean result = game.executeMove(playerA, worker, target);
        assertTrue(result);
        assertEquals(target, worker.getPosition());
        assertEquals(Occupancy.WORKER, target.getOccupancy());
        assertEquals(Occupancy.EMPTY, start.getOccupancy());
    }

    @Test
    public void testMoveToNonAdjacentCell() {
        Cell start = game.getBoard().getCell(0, 0);
        Cell far = game.getBoard().getCell(4, 4);
        Worker worker = playerA.getWorkers().get(0);
        game.placeWorker(playerA, worker, start);
        boolean result = game.executeMove(playerA, worker, far);
        assertFalse(result);
    }

    @Test
    public void testMoveTooHigh() {
        Cell start = game.getBoard().getCell(2, 2);
        Cell target = game.getBoard().getCell(2, 3);
        Worker worker = playerA.getWorkers().get(0);
        game.placeWorker(playerA, worker, start);
        for (int i = 0; i < 3; i++) target.increaseLevel();
        boolean result = game.executeMove(playerA, worker, target);
        assertFalse(result);
    }

    @Test
    public void testDemeterCannotBuildTwiceOnSameCell() {
        DemeterBuildStrategy strategy = new DemeterBuildStrategy();
        game.setPhase("building");
        Worker worker = playerA.getWorkers().get(0);
        Cell pos = game.getBoard().getCell(2, 2);
        Cell target = game.getBoard().getCell(2, 3);
        game.placeWorker(playerA, worker, pos);
        strategy.performBuild(game, playerA, worker, target, false);
		strategy.afterBuild(game, playerA, worker, target);
        assertFalse(strategy.isValidBuild(game, playerA, worker, target));
    }

    @Test
    public void testMinotaurPush() {
        MinotaurMoveStrategy strategy = new MinotaurMoveStrategy();
        Cell aPos = game.getBoard().getCell(2, 2);
        Cell bPos = game.getBoard().getCell(2, 3);
        Cell behind = game.getBoard().getCell(2, 4);
        Worker wa = playerA.getWorkers().get(0);
        Worker wb = playerB.getWorkers().get(0);
        game.placeWorker(playerA, wa, aPos);
        game.placeWorker(playerB, wb, bPos);
        assertTrue(strategy.isValidMove(game, playerA, wa, bPos));
        strategy.performMove(game, playerA, wa, aPos, bPos);
        assertEquals(bPos, wa.getPosition());
        assertEquals(behind, wb.getPosition());
    }
} 
