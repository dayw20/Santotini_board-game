package edu.cmu.cs214.hw2.player;

import java.util.ArrayList;
import java.util.List;
import edu.cmu.cs214.hw2.model.Worker;
import edu.cmu.cs214.hw2.model.Cell;

/**
 * A player in the Santorini.
 * Each player has a name and controls two workers.
 */

public class Player {
    private String name;
    private List<Worker> workers;

    /**
     * Creates a new {@link Player} instance with the input name.
     * Initializes two workers for the player.
     * 
     * @param name The player's name
     */
    public Player(String name) {
        this.name = name;
        this.workers = new ArrayList<>();
        workers.add(new Worker(this));
        workers.add(new Worker(this));
    }

    /**
     * Moves a worker to a target cell.
     * 
     * @param worker The worker to move
     * @param targetCell The cell to move to
     */
    public void moveWorker(Worker worker, Cell targetCell) {
        if (workers.contains(worker)) {
            worker.move(targetCell);
        }
    }

    /**
     * Makes a worker to build on a target cell.
     * 
     * @param worker The worker that will build
     * @param targetCell The cell to build on
     */
    public void buildWithWorker(Worker worker, Cell targetCell) {
        if (workers.contains(worker)) {
            worker.build(targetCell);
        }
    }

    /**
     * Gets all workers controlled by this player.
     * 
     * @return List of the player's workers
     */
    public List<Worker> getWorkers() {
        return new ArrayList<>(workers);
    }
    
    /**
     * Gets the player's name.
     * 
     * @return The player's name
     */
    public String getName() {
        return name;
    }
}
