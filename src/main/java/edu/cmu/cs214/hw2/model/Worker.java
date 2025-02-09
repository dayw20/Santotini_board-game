package edu.cmu.cs214.hw2.model;

import edu.cmu.cs214.hw2.player.Player;

/**
 * Represents a worker in the Santorini.
 * Workers can move around the board and build towers.
 */
public class Worker {
    private Cell position;
    private final Player owner;

    /**
     * Creates a new {@link Worker} instance owned by the specified player.
     * 
     * @param owner The player who controls this worker
     */
    public Worker(Player owner) {
        this.position = null;
        this.owner = owner;
    }

    /**
     * Checks if the worker can move to the target cell.
     * Movement rules:
     * - Target must be adjacent
     * - Target must not be occupied
     * - Level difference must not > 1
     * - Can move down any number of levels
     * 
     * @param targetCell The cell to check
     * @return true if the move is valid, false otherwise
     */
    public boolean canMove(Cell targetCell) {
        if (targetCell.isOccupied() || targetCell == null || position == null) {
            return false;
        }
        int dx = Math.abs(targetCell.getX() - position.getX());
        int dy = Math.abs(targetCell.getY() - position.getY());
        if (dx > 1 || dy > 1 || (dx == 0 && dy == 0)) {
            return false;
        }
        int dh = targetCell.getLevel() - position.getLevel();
        return dh <= 1;
    }

    /**
     * Moves the worker to the target cell if the can move.
     * 
     * @param targetCell The cell to move to
     */
    public void move(Cell targetCell) {
        if (canMove(targetCell)) {
            position.setOccupancy(Occupancy.EMPTY);
            targetCell.setOccupancy(Occupancy.WORKER);
            position = targetCell;
        }

    }

    /**
     * Checks if the worker can build on the target cell.
     * Building rules:
     * - Target must be adjacent to worker's position
     * - Target must not be occupied
     * - Can only build dome on level 3
     * 
     * @param targetCell The cell to check
     * @return true if building is possible, false otherwise
     */
    public boolean canBuild(Cell targetCell) {
        if (targetCell.isOccupied() || targetCell == null || position == null) {
            return false;
        }
        int dx = Math.abs(targetCell.getX() - position.getX());
        int dy = Math.abs(targetCell.getY() - position.getY());
        return dx <= 1 && dy <= 1 && (dx != 0 || dy != 0);
    }

    /**
     * Builds on the target cell if can build.
     * 
     * @param targetCell The cell to build on
     */
    public void build(Cell targetCell) {
        if (canBuild(targetCell)) {
            targetCell.increaseLevel();
        }
    }

    /**
     * Gets the current position of the worker.
     * 
     * @return The cell where the worker is on
     */
    public Cell getPosition() {
        return position;
    }

    /**
     * Sets the worker's position.
     * 
     * @param cell The new position
     */
    public void setPosition(Cell position) {
        this.position = position;
    }

    /**
     * Gets the owner of the worker.
     * 
     * @return the owner of this worker
     */
    public Player getOwner() {
        return owner;
    }
}
