package edu.cmu.cs214.hw2.board;
import edu.cmu.cs214.hw2.model.*;


import java.util.ArrayList;
import java.util.List;

public class Board {
    private Cell[][] grid;
    private List<Worker> allWorkers;

    public Board() {
        grid = new Cell[5][5];
        allWorkers = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 5 && y >= 0 && y < 5;
    }

    public Cell getCell(int x, int y) {
        if (!isValidPosition(x, y)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        return grid[x][y];
    }

    public List<Worker> getAllWorkers() {
        return allWorkers;
    }

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
