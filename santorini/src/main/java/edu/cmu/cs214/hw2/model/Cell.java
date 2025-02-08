package edu.cmu.cs214.hw2.model;

public class Cell {
    private int x;
    private int y;
    private int level;
    private Occupancy occupancy;

    /**
     * Creates a new {@link Cell} instance.
     *
     * @param x and @param y indicates the position in the board grid of this cell.
     * @param level The
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.level = 0;
        this.occupancy = Occupancy.EMPTY;
    }

    public boolean isOccupied() {
        return occupancy != Occupancy.EMPTY;
    }

    public void increaseLevel() {
        if (level == 3) {
            occupancy = Occupancy.DOME;
        } else { level ++; }
    }
    
    public int getLevel() {
        return level;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Occupancy getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Occupancy occupancy) {
        this.occupancy = occupancy;
    }

}
