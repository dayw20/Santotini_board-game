package edu.cmu.cs214.hw2.model;
/**
 * Represents the occupancy status of a cell on the game board.
 */
public enum Occupancy {
    /**
     * Represents an empty cell that can be moved to or built upon.
     */
    EMPTY,

    /**
     * Represents a cell occupied by a worker.
     */
    WORKER,

    /**
     * Represents a cell with a dome (Level 3).
     * No further building or movement is possible on this cell.
     */
    DOME
}