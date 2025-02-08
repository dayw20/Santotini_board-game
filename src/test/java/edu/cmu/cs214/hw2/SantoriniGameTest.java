package edu.cmu.cs214.hw2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import edu.cmu.cs214.hw2.game.Game;
import edu.cmu.cs214.hw2.player.Player;
import edu.cmu.cs214.hw2.model.Worker;
import edu.cmu.cs214.hw2.model.Cell;
import edu.cmu.cs214.hw2.board.Board;
import edu.cmu.cs214.hw2.model.Occupancy;

public class SantoriniGameTest {
    private Game game;
    private Player playerA;
    private Player playerB;
    private Board board;

    @Before
    public void setUp() {
        game = new Game();
        playerA = new Player("Alice");
        playerB = new Player("Bob");
        game.startGame(playerA, playerB);
        board = game.getBoard();
    }

    @Test
    public void testGameInitialization() {
        assertNotNull("Game should be initialized", game);
        assertNotNull("Board should be initialized", board);
        assertEquals("Game should start with player A", playerA, game.getCurrPlayer());
        assertEquals("Each player should have 2 workers", 2, playerA.getWorkers().size());
        assertEquals("Each player should have 2 workers", 2, playerB.getWorkers().size());
    }

    @Test
    public void testWorkerPlacement() {
        Worker worker = playerA.getWorkers().get(0);
        Cell targetCell = board.getCell(0, 0);
        
        assertTrue("Should be able to place worker", board.placeWorker(worker, targetCell));
        assertEquals("Cell should be occupied by worker", Occupancy.WORKER, targetCell.getOccupancy());
        assertEquals("Worker position should be updated", targetCell, worker.getPosition());
    }

    @Test
    public void testInvalidWorkerPlacement() {
        Worker worker1 = playerA.getWorkers().get(0);
        Worker worker2 = playerA.getWorkers().get(1);
        Cell targetCell = board.getCell(0, 0);
        
        board.placeWorker(worker1, targetCell);
        assertFalse("Should not be able to place worker on occupied cell", 
                   board.placeWorker(worker2, targetCell));
    }

    @Test
    public void testWorkerMovement() {
        Worker worker = playerA.getWorkers().get(0);
        Cell startCell = board.getCell(1, 1);
        Cell targetCell = board.getCell(1, 2);
        
        board.placeWorker(worker, startCell);
        assertTrue("Worker should be able to move to adjacent cell", worker.canMove(targetCell));
        worker.move(targetCell);
        assertEquals("Worker should be in new position", targetCell, worker.getPosition());
        assertEquals("Old cell should be empty", Occupancy.EMPTY, startCell.getOccupancy());
        assertEquals("New cell should be occupied", Occupancy.WORKER, targetCell.getOccupancy());
    }

    @Test
    public void testInvalidWorkerMovement() {
        Worker worker = playerA.getWorkers().get(0);
        Cell startCell = board.getCell(0, 0);
        Cell targetCell = board.getCell(2, 2);
        
        board.placeWorker(worker, startCell);
        assertFalse("Worker should not be able to move diagonally more than one space", 
                   worker.canMove(targetCell));
    }

    @Test
    public void testBuildingTower() {
        Worker worker = playerA.getWorkers().get(0);
        Cell workerCell = board.getCell(1, 1);
        Cell buildCell = board.getCell(1, 2);
        
        board.placeWorker(worker, workerCell);
        assertTrue("Worker should be able to build in adjacent cell", worker.canBuild(buildCell));
        worker.build(buildCell);
        assertEquals("Building level should increase", 1, buildCell.getLevel());
    }

    @Test
    public void testWinCondition() {
        Worker worker = playerA.getWorkers().get(0);
        Cell levelThreeCell = board.getCell(0, 0);
        
        // Build up to level 3
        for (int i = 0; i < 3; i++) {
            levelThreeCell.increaseLevel();
        }
        
        // Place worker on level 3
        board.placeWorker(worker, levelThreeCell);
        
        assertNotNull("Game should have a winner", game.getWinner());
        assertEquals("PlayerA should win", playerA, game.getWinner());
        assertTrue("Game should be over", game.isGameOver());
    }

    @Test
    public void testTurnRotation() {
        assertEquals("Game should start with player A", playerA, game.getCurrPlayer());
        game.nextTurn();
        assertEquals("Should be player B's turn", playerB, game.getCurrPlayer());
        game.nextTurn();
        assertEquals("Should be back to player A's turn", playerA, game.getCurrPlayer());
    }

    @Test
    public void testBuildingDome() {
        Worker worker = playerA.getWorkers().get(0);
        Cell workerCell = board.getCell(1, 1);
        Cell buildCell = board.getCell(1, 2);
        
        board.placeWorker(worker, workerCell);
        
        // Build up to level 3
        for (int i = 0; i < 3; i++) {
            worker.build(buildCell);
        }
        
        // Build dome (4th level)
        worker.build(buildCell);
        assertEquals("Cell should have a dome", Occupancy.DOME, buildCell.getOccupancy());
        assertFalse("Should not be able to build on dome", worker.canBuild(buildCell));
    }
}