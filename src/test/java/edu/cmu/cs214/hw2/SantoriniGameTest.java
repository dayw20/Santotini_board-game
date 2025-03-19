package edu.cmu.cs214.hw2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import edu.cmu.cs214.hw2.board.Board;
import edu.cmu.cs214.hw2.game.Game;
import edu.cmu.cs214.hw2.model.Cell;
import edu.cmu.cs214.hw2.model.Worker;
import edu.cmu.cs214.hw2.player.Player;

public class SantoriniGameTest {
    private Game game;
    private Player playerA;
    private Player playerB;
    private Worker workerA1;
    private Worker workerA2;
    private Worker workerB1;
    private Worker workerB2;
    private Board board;

    @Before
    public void setUp() {
        game = new Game();
        playerA = new Player("Alice");
        playerB = new Player("Bob");
        game.startGame(playerA, playerB);

        workerA1 = playerA.getWorkers().get(0);
        workerA2 = playerA.getWorkers().get(1);
        workerB1 = playerB.getWorkers().get(0);
        workerB2 = playerB.getWorkers().get(1);

        board = game.getBoard();
    }

    @Test
    public void testValidMove() {
        Cell startCell = board.getCell(1, 1);
        Cell validMove = board.getCell(1, 2);
        Cell invalidMove = board.getCell(4, 4);

        game.placeWorker(playerA, workerA1, startCell);
        
        // Valid move (adjacent, no height restrictions)
        assertTrue(game.executeMove(playerA, workerA1, validMove));

        // Invalid move (too far)
        assertFalse(game.executeMove(playerA, workerA1, invalidMove));
    }

    @Test
    public void testMoveHeightRestriction() {
        Cell startCell = board.getCell(1, 1);
        Cell tooHighCell = board.getCell(1, 2);
        
        game.placeWorker(playerA, workerA1, startCell);
        
        // Simulate building on the too-high cell
        tooHighCell.increaseLevel();
        tooHighCell.increaseLevel(); 
        tooHighCell.increaseLevel();  
        
        // Worker should NOT be able to move onto a Level 3 cell
        assertFalse(game.executeMove(playerA, workerA1, tooHighCell));
    }

    @Test
    public void testValidBuild() {
        Cell startCell = board.getCell(2, 2);
        Cell validBuild = board.getCell(2, 3);
        Cell invalidBuild = board.getCell(4, 4);

        game.placeWorker(playerA, workerA1, startCell);
        
        // Valid build (adjacent)
        assertTrue(game.executeBuild(playerA, workerA1, validBuild));

        // Invalid build (too far)
        assertFalse(game.executeBuild(playerA, workerA1, invalidBuild));
    }

    @Test
    public void testBuildOnDomeForbidden() {
        Cell startCell = board.getCell(3, 3);
        Cell domeCell = board.getCell(3, 4);
        
        game.placeWorker(playerA, workerA1, startCell);
        
        domeCell.increaseLevel();
        domeCell.increaseLevel();
        domeCell.increaseLevel();
        domeCell.increaseLevel();  
        assertFalse(game.executeBuild(playerA, workerA1, domeCell));
    }

    @Test
    public void testGameSequence() {
        Cell startCellA = board.getCell(0, 0);
        Cell moveCellA = board.getCell(0, 1);
        Cell buildCellA = board.getCell(0, 2);
        
        Cell startCellB = board.getCell(4, 4);
        Cell moveCellB = board.getCell(3, 4);
        Cell buildCellB = board.getCell(2, 4);

        game.placeWorker(playerA, workerA1, startCellA);
        game.placeWorker(playerB, workerB1, startCellB);

        // Player A move & build
        assertTrue(game.executeMove(playerA, workerA1, moveCellA));
        assertTrue(game.executeBuild(playerA, workerA1, buildCellA));

        // Player B move & build
        game.nextTurn();
        assertTrue(game.executeMove(playerB, workerB1, moveCellB));
        assertTrue(game.executeBuild(playerB, workerB1, buildCellB));
    }

    @Test
    public void testWinCondition() {
        // First, build a level 3 tower
        Cell level3Cell = board.getCell(1, 0);
        level3Cell.increaseLevel(); // Level 1
        level3Cell.increaseLevel(); // Level 2
        level3Cell.increaseLevel(); // Level 3
        
        // Place the worker on level 1 first
        Cell level1Cell = board.getCell(1, 1);
        level1Cell.increaseLevel(); // Level 1
        game.placeWorker(playerA, workerA1, level1Cell);
    
        Cell level2Cell = board.getCell(2, 0);
        level2Cell.increaseLevel(); // Level 1
        level2Cell.increaseLevel(); // Level 2
        assertTrue(game.executeMove(playerA, workerA1, level2Cell));
 
        assertTrue(game.executeMove(playerA, workerA1, level3Cell));
    
        // Check win condition
        assertTrue("Game should be over after reaching level 3", game.isGameOver());
        assertEquals("Player A should be winner", playerA, game.getWinner());
    }

    @Test
    public void testTurnSwitch() {
        assertEquals(playerA, game.getCurrPlayer());
        game.nextTurn();
        assertEquals(playerB, game.getCurrPlayer());
        game.nextTurn();
        assertEquals(playerA, game.getCurrPlayer());
    }

    @Test
    public void testInvalidBuilds() {
        Cell startCell = board.getCell(2, 2);
        game.placeWorker(playerA, workerA1, startCell);

        // Building on the same position (invalid)
        assertFalse(game.executeBuild(playerA, workerA1, startCell));

        // Building out of bounds
        // Building out of bounds
        if (board.isValidPosition(5, 5)) {
            assertFalse(game.executeBuild(playerA, workerA1, board.getCell(5, 5)));
        } else {
            System.out.println("Skipping out-of-bounds build test.");
        }


        // Building on a dome
        Cell domeCell = board.getCell(2, 3);
        domeCell.increaseLevel();
        domeCell.increaseLevel();
        domeCell.increaseLevel();
        domeCell.increaseLevel();
        assertFalse(game.executeBuild(playerA, workerA1, domeCell));

        // Building too far away (invalid)
        assertFalse(game.executeBuild(playerA, workerA1, board.getCell(0, 0)));
    }


    @Test
    public void testInvalidMoves() {
        Cell startCell = board.getCell(1, 1);
        game.placeWorker(playerA, workerA1, startCell);

        // Occupied cell test
        game.placeWorker(playerB, workerB1, board.getCell(1, 2));
        assertFalse(game.executeMove(playerA, workerA1, board.getCell(1, 2))); 

        // Out of bounds test
        if (board.isValidPosition(5, 5)) {
            assertFalse(game.executeMove(playerA, workerA1, board.getCell(5, 5)));
        } else {
            System.out.println("Skipping out-of-bounds move test.");
        } 

        // Moving from a dome (invalid start position)
        board.getCell(1, 1).increaseLevel();
        board.getCell(1, 1).increaseLevel();
        board.getCell(1, 1).increaseLevel();
        board.getCell(1, 1).increaseLevel(); 
        assertFalse(game.executeMove(playerA, workerA1, board.getCell(1, 2))); // Can't move from dome
    }


    @Test
    public void testFullGameSimulation() {
        // Initial placements
        game.placeWorker(playerA, workerA1, board.getCell(0, 0));
        game.placeWorker(playerA, workerA2, board.getCell(0, 1));
        game.placeWorker(playerB, workerB1, board.getCell(4, 4));
        game.placeWorker(playerB, workerB2, board.getCell(4, 3));

        // Turn 1: Player A moves and builds
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(1, 0)));
        assertTrue(game.executeBuild(playerA, workerA1, board.getCell(2, 0)));

        // Turn 2: Player B moves and builds
        game.nextTurn();
        assertTrue(game.executeMove(playerB, workerB1, board.getCell(3, 4)));
        assertTrue(game.executeBuild(playerB, workerB1, board.getCell(2, 4)));

        // Turn 3: Player A moves and builds
        game.nextTurn();
        assertTrue(game.executeMove(playerA, workerA2, board.getCell(1, 1)));
        assertTrue(game.executeBuild(playerA, workerA2, board.getCell(2, 0)));

        // Turn 4: PlayerB
        game.nextTurn();
        assertFalse(game.executeMove(playerB, workerB1, board.getCell(4, 3))); 
        assertTrue(game.executeMove(playerB, workerB1, board.getCell(3, 3))); 
        assertTrue(game.executeBuild(playerB, workerB1, board.getCell(2, 3)));

        // Turn 5: Player A 
        game.nextTurn();
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(0, 0)));
        assertTrue(game.executeBuild(playerA, workerA1, board.getCell(1, 0)));

        // Turn 6: PlayerB
        game.nextTurn();
        assertTrue(game.executeMove(playerB, workerB1, board.getCell(3, 2))); 
        assertTrue(game.executeBuild(playerB, workerB1, board.getCell(3, 1)));

        // Turn 7: Player A 
        game.nextTurn();
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(1, 0)));
        assertTrue(game.executeBuild(playerA, workerA1, board.getCell(2, 0)));

        // Turn 8: PlayerB
        game.nextTurn();
        assertTrue(game.executeMove(playerB, workerB1, board.getCell(3, 1))); 
        assertTrue(game.executeBuild(playerB, workerB1, board.getCell(2, 1)));

        // Turn 9: Player A 
        game.nextTurn();
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(0, 0)));
        assertTrue(game.executeBuild(playerA, workerA1, board.getCell(1, 0)));

        // Turn 10: Player A keep moving until win
        game.nextTurn();
        game.nextTurn();
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(0, 1)));
        assertTrue(game.executeBuild(playerA, workerA1, board.getCell(0, 0)));
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(0, 0)));
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(1, 0)));
        assertTrue(game.executeMove(playerA, workerA1, board.getCell(2, 0)));

        // Check Win Condition
        assertTrue(game.isGameOver());
        assertEquals(playerA, game.getWinner());
    }

}
