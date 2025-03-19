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
     * Checks if the worker can physically climb from current position to target level.
     * Workers can climb at most 1 level up but can go down any number of levels.
     * 
     * @param targetLevel The level to climb to
     * @return true if the climb is physically possible
     */
    public boolean canClimb(int targetLevel) {
        if (position == null) return false;
        
        int currentLevel = position.getLevel();
        int levelDiff = targetLevel - currentLevel;
        
        return levelDiff <= 1;
    }

    /**
     * Moves the worker to the specified cell.
     * This method assumes all validations have been done.
     * 
     * @param targetCell The cell to move to
     */
    public void moveTo(Cell targetCell) {
        if (position != null) {
            position.setOccupancy(Occupancy.EMPTY);
        }
        position = targetCell;
        targetCell.setOccupancy(Occupancy.WORKER);
    }

    /**
     * Builds on the specified cell.
     * This method assumes all validations have been done.
     * 
     * @param targetCell The cell to build on
     */
    public void buildOn(Cell targetCell) {
        targetCell.increaseLevel();
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
