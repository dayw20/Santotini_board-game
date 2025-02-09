package edu.cmu.cs214.hw2.model;

/**
 * A cell on the Santorini game board.
 * Each cell has position(x, y), a Tower level, and occupancy status.
 */
public class Cell {
    private int x;
    private int y;
    private int level;
    private Occupancy occupancy;

    /**
     * Creates a new {@link Cell} instance.
     *
     * @param x and @param y indicates the position in the board grid of this cell.
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.level = 0;
        this.occupancy = Occupancy.EMPTY;
    }

    /**
     * Checks if the cell is currently occupied (by a worker or dome).
     *
     * @return true if the cell is occupied, false otherwise
     */
    public boolean isOccupied() {
        return occupancy != Occupancy.EMPTY;
    }

    /**
     * Increases the tower level of this cell by 1.
     * If the cell's level is 3, a dome will be added instead.
     */
    public void increaseLevel() {
        if (level == 3) {
            occupancy = Occupancy.DOME;
        } else { level ++; }
    }
    
    public int getLevel() {
        return level;
    }

    /**
    * Gets the x-coordinate of this cell.
     *
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of this cell.
     *
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the current occupancy status of this cell.
     *
     * @return The occupancy status
     */
    public Occupancy getOccupancy() {
        return occupancy;
    }

    /**
     * Sets the occupancy status of this cell.
     *
     * @param occupancy The new occupancy status
     */
    public void setOccupancy(Occupancy occupancy) {
        this.occupancy = occupancy;
    }

}
