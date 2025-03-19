package edu.cmu.cs214.hw2.board;

import edu.cmu.cs214.hw2.model.Cell;
import edu.cmu.cs214.hw2.model.Worker;
import edu.cmu.cs214.hw2.model.Occupancy;
import java.util.ArrayList;
import java.util.List;

/**
 * Controls the game board for Santorini.
 * The board is a 5x5 grid where each cell can contain Towers or workers.
 */
public class Board {
    private Cell[][] grid;
    private List<Worker> allWorkers;

    /**
     * Creates a new {@link Board} instance.
     * Initializes all cells as empty with no buildings.
     */
    public Board() {
        grid = new Cell[5][5];
        allWorkers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * Checks if the given coordinates are valid for the board.
     *
     * @param x The x-coordinate 
     * @param y The y-coordinate 
     * @return true if the coordinates are within range, false otherwise
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 5 && y >= 0 && y < 5;
    }

    /**
     * Gets the cell at the specified position.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The cell at the specified position
     * @throws IllegalArgumentException if coordinates are out of bounds
     */
    public Cell getCell(int x, int y) {
        if (!isValidPosition(x, y)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        return grid[x][y];
    }

    /**
     * Checks if two cells are adjacent to each other.
     *
     * @param cell1 The first cell
     * @param cell2 The second cell
     * @return true if the cells are adjacent, false otherwise
     */
    public boolean isAdjacent(Cell cell1, Cell cell2) {
        if (cell1 == null || cell2 == null) {
            return false;
        }
        
        int dx = Math.abs(cell1.getX() - cell2.getX());
        int dy = Math.abs(cell1.getY() - cell2.getY());
        
        return dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0);
    }


    /**
     * Gets all workers currently on the board.
     *
     * @return List of all workers
     */
    public List<Worker> getAllWorkers() {
        return allWorkers;
    }

    /**
     * Places a worker on the specified cell.
     *
     * @param worker The worker to place
     * @param targetCell The cell where the worker will be placed
     * @return true if the worker was successfully placed, false otherwise
     */
    public boolean placeWorker(Worker worker, Cell targetCell) {
        if (targetCell.isOccupied()) {
            return false;
        }
        Cell oldPosition = worker.getPosition();
        if (oldPosition != null) {
            oldPosition.setOccupancy(Occupancy.EMPTY);
        }

        worker.setPosition(targetCell);
        targetCell.setOccupancy(Occupancy.WORKER);

        if (!allWorkers.contains(worker)) {
            allWorkers.add(worker);
        }
        return true;
    }


}
