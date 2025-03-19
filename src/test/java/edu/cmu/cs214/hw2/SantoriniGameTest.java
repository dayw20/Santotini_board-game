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
    private Worker workerA1;
    private Worker workerA2;
    private Worker workerB1;
    private Worker workerB2;

    @Before
    public void setUp() {
        game = new Game();
        playerA = new Player("Alice");
        playerB = new Player("Bob");
        game.startGame(playerA, playerB);
        board = game.getBoard();
        
        // Initialize worker references for convenience
        workerA1 = playerA.getWorkers().get(0);
        workerA2 = playerA.getWorkers().get(1);
        workerB1 = playerB.getWorkers().get(0);
        workerB2 = playerB.getWorkers().get(1);
        
        // Place workers in initial positions for testing
        game.placeWorker(playerA, workerA1, board.getCell(0, 0));
        game.placeWorker(playerA, workerA2, board.getCell(0, 1));
        game.placeWorker(playerB, workerB1, board.getCell(4, 4));
        game.placeWorker(playerB, workerB2, board.getCell(4, 3));
    }

    @Test
    public void testGameInitialization() {
        assertNotNull("Game should be initialized", game);
        assertNotNull("Board should be initialized", board);
        assertEquals("Game should start with player A", playerA, game.getCurrPlayer());
        assertEquals("Each player should have 2 workers", 2, playerA.getWorkers().size());
        assertEquals("Each player should have 2 workers", 2, playerB.getWorkers().size());
        assertFalse("Game should not be over at start", game.isGameOver());
        assertNull("There should be no winner at start", game.getWinner());
    }

    @Test
    public void testWorkerPlacement() {
        Worker worker = playerA.getWorkers().get(0);
        Cell targetCell = board.getCell(1, 1);
        
        assertTrue("Should be able to place worker", game.placeWorker(playerA, worker, targetCell));
        assertEquals("Cell should be occupied by worker", Occupancy.WORKER, targetCell.getOccupancy());
        assertEquals("Worker position should be updated", targetCell, worker.getPosition());
    }

    @Test
    public void testInvalidWorkerPlacement() {
        Worker worker = playerA.getWorkers().get(0);
        Cell targetCell = board.getCell(0, 0); // Already occupied in setUp
        
        assertFalse("Should not be able to place worker on occupied cell", 
                   game.placeWorker(playerA, worker, targetCell));
    }

    // Test for valid move validation
    @Test
    public void testValidMoveChecking() {
        // Adjacent move on same level
        assertTrue("Should allow move to adjacent cell on same level", 
                  game.executeMove(playerA, workerA1, board.getCell(1, 0)));
        
        // Place worker on level 1 cell
        Cell levelOneCell = board.getCell(2, 0);
        levelOneCell.increaseLevel(); // Make it level 1
        
        // Reset worker position for next test
        game.placeWorker(playerA, workerA1, board.getCell(1, 0));
        
        // Test climbing up one level
        assertTrue("Should allow climbing up one level", 
                  game.executeMove(playerA, workerA1, levelOneCell));
                  
        // Reset worker position for next test
        Cell levelTwoCell = board.getCell(3, 0);
        levelTwoCell.increaseLevel(); // Level 1
        levelTwoCell.increaseLevel(); // Level 2
        game.placeWorker(playerA, workerA1, levelTwoCell);
        
        // Test moving down multiple levels
        assertTrue("Should allow moving down multiple levels", 
                  game.executeMove(playerA, workerA1, board.getCell(3, 1)));
    }

    // Test for invalid move validation
    @Test
    public void testInvalidMoveChecking() {
        // Test non-adjacent cell
        assertFalse("Should not allow move to non-adjacent cell", 
                   game.executeMove(playerA, workerA1, board.getCell(2, 2)));
        
        // Test move to occupied cell
        assertFalse("Should not allow move to occupied cell", 
                   game.executeMove(playerA, workerA1, board.getCell(0, 1)));
        
        // Test climbing more than one level
        Cell levelTwoCell = board.getCell(1, 0);
        levelTwoCell.increaseLevel(); // Level 1
        levelTwoCell.increaseLevel(); // Level 2
        assertFalse("Should not allow climbing more than one level", 
                   game.executeMove(playerA, workerA1, levelTwoCell));
        
        // Test move to dome
        Cell domeCell = board.getCell(1, 1);
        domeCell.increaseLevel(); // Level 1
        domeCell.increaseLevel(); // Level 2
        domeCell.increaseLevel(); // Level 3
        domeCell.increaseLevel(); // Dome
        assertFalse("Should not allow move to dome", 
                   game.executeMove(playerA, workerA1, domeCell));
        
        // Test move with wrong player's turn
        assertFalse("Should not allow move during other player's turn", 
                   game.executeMove(playerB, workerB1, board.getCell(3, 4)));
        
        // Test move with other player's worker
        assertFalse("Should not allow move with other player's worker", 
                   game.executeMove(playerA, workerB1, board.getCell(3, 4)));
    }

    // Test for valid build validation
    @Test
    public void testValidBuildChecking() {
        // Move worker to position for building
        game.executeMove(playerA, workerA1, board.getCell(1, 0));
        
        // Test building on adjacent empty cell
        assertTrue("Should allow building on adjacent empty cell", 
                  game.executeBuild(playerA, workerA1, board.getCell(2, 0)));
        assertEquals("Cell level should increase", 1, board.getCell(2, 0).getLevel());
        
        // Test building on an existing building
        assertTrue("Should allow building on existing building", 
                  game.executeBuild(playerA, workerA1, board.getCell(2, 0)));
        assertEquals("Cell level should increase to 2", 2, board.getCell(2, 0).getLevel());
        
        // Test building level 3
        assertTrue("Should allow building level 3", 
                  game.executeBuild(playerA, workerA1, board.getCell(2, 0)));
        assertEquals("Cell level should increase to 3", 3, board.getCell(2, 0).getLevel());
        
        // Test building dome on level 3
        assertTrue("Should allow building dome on level 3", 
                  game.executeBuild(playerA, workerA1, board.getCell(2, 0)));
        assertEquals("Cell should have dome", Occupancy.DOME, board.getCell(2, 0).getOccupancy());
    }

    // Test for invalid build validation
    @Test
    public void testInvalidBuildChecking() {
        // Test building on non-adjacent cell
        assertFalse("Should not allow building on non-adjacent cell", 
                   game.executeBuild(playerA, workerA1, board.getCell(2, 2)));
        
        // Test building on occupied cell
        assertFalse("Should not allow building on occupied cell", 
                   game.executeBuild(playerA, workerA1, board.getCell(0, 1)));
        
        // Test building on dome
        Cell domeCell = board.getCell(1, 0);
        domeCell.increaseLevel(); // Level 1
        domeCell.increaseLevel(); // Level 2
        domeCell.increaseLevel(); // Level 3
        domeCell.increaseLevel(); // Dome
        assertFalse("Should not allow building on dome", 
                   game.executeBuild(playerA, workerA1, domeCell));
        
        // Test build with wrong player's turn
        assertFalse("Should not allow build during other player's turn", 
                   game.executeBuild(playerB, workerB1, board.getCell(3, 4)));
        
        // Test build with other player's worker
        assertFalse("Should not allow build with other player's worker", 
                   game.executeBuild(playerA, workerB1, board.getCell(3, 4)));
    }

    @Test
    public void testWinCondition() {
        // Build a level 3 tower
        Cell targetCell = board.getCell(1, 0);
        targetCell.increaseLevel();
        targetCell.increaseLevel(); 
        targetCell.increaseLevel(); 
        
        // Move worker to level 3
        assertTrue(game.executeMove(playerA, workerA1, targetCell));
        
        // Game should be over with player A as winner
        assertTrue("Game should be over after reaching level 3", game.isGameOver());
        assertEquals("Player A should be winner", playerA, game.getWinner());
    }

    @Test
    public void testTurnRotation() {
        assertEquals("Game should start with player A", playerA, game.getCurrPlayer());
        
        // Execute a full turn for player A
        game.executeMove(playerA, workerA1, board.getCell(1, 0));
        game.executeBuild(playerA, workerA1, board.getCell(2, 0));
        game.nextTurn();
        
        assertEquals("Should be player B's turn", playerB, game.getCurrPlayer());
        
        // Execute a full turn for player B
        game.executeMove(playerB, workerB1, board.getCell(3, 4));
        game.executeBuild(playerB, workerB1, board.getCell(3, 3));
        game.nextTurn();
        
        assertEquals("Should be back to player A's turn", playerA, game.getCurrPlayer());
    }

    // Integration test for a complete game sequence
    @Test
    public void testGamePlaySequence() {
        // Turn 1: Player A moves and builds
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(1, 0)));
        assertTrue(game.executeBuild(playerA, workerA1, board.getCell(1, 1)));
        game.nextTurn();
        
        // Turn 1: Player B moves and builds
        assertTrue(game.executeMove(playerB, workerB1, board.getCell(3, 4)));
        assertTrue(game.executeBuild(playerB, workerB1, board.getCell(3, 3)));
        game.nextTurn();
        
        // Turn 2: Player A moves and builds (climbing to level 1)
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(1, 1)));
        assertTrue(game.executeBuild(playerA, workerA1, board.getCell(2, 1)));
        assertEquals("Worker should be on level 1", 1, workerA1.getPosition().getLevel());
        game.nextTurn();
        
        // Turn 2: Player B moves and builds
        assertTrue(game.executeMove(playerB, workerB1, board.getCell(3, 3)));
        assertTrue(game.executeBuild(playerB, workerB1, board.getCell(2, 3)));
        assertEquals("Worker should be on level 1", 1, workerB1.getPosition().getLevel());
        game.nextTurn();
        
        // Turn 3: Player A moves and builds (climbing to level 2)
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(2, 1)));
        assertTrue(game.executeBuild(playerA, workerA1, board.getCell(2, 2)));
        assertEquals("Worker should be on level 2", 2, workerA1.getPosition().getLevel());
        game.nextTurn();
        
        // Turn 3: Player B moves and builds
        assertTrue(game.executeMove(playerB, workerB1, board.getCell(2, 3)));
        assertTrue(game.executeBuild(playerB, workerB1, board.getCell(2, 2)));
        assertEquals("Worker should be on level 2", 2, workerB1.getPosition().getLevel());
        game.nextTurn();
        
        // Turn 4: Player A moves to level 3 and wins
        Cell level3Cell = board.getCell(2, 2);
        assertEquals("Cell should be level 3", 3, level3Cell.getLevel());
        assertTrue(game.executeMove(playerA, workerA1, level3Cell));
        
        // Check game state
        assertTrue("Game should be over", game.isGameOver());
        assertEquals("Player A should be winner", playerA, game.getWinner());
    }
    
    // Test active player validation
    @Test
    public void testActivePlayerValidation() {
        // It's player A's turn first
        assertTrue("Player A should be able to move their worker", 
                  game.executeMove(playerA, workerA1, board.getCell(1, 0)));
        
        // Try to have player B move
        assertFalse("Player B should not be able to move during player A's turn", 
                   game.executeMove(playerB, workerB1, board.getCell(3, 4)));
        
        // Complete player A's turn
        game.executeBuild(playerA, workerA1, board.getCell(1, 1));
        game.nextTurn();
        
        // Now it's player B's turn
        assertTrue("Player B should be able to move their worker", 
                  game.executeMove(playerB, workerB1, board.getCell(3, 4)));
        
        // Try to have player A move
        assertFalse("Player A should not be able to move during player B's turn", 
                   game.executeMove(playerA, workerA2, board.getCell(1, 1)));
    }
}