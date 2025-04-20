package com.yvette.santorini.backend.model;
import java.util.ArrayList;
import java.util.List;

import com.yvette.santorini.backend.godpowers.God;

/**
 * A player in the Santorini.
 * Each player has a name and controls two workers.
 */

public class Player {
    private String name;
    private List<Worker> workers;
    private God god;

    public God getGod() { return god; }
    
    public void setGod(God god) { this.god = god; }

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
     * Checks if this player owns the specified worker.
     * 
     * @param worker The worker to check
     * @return true if the player owns the worker, false otherwise
     */
    public boolean ownsWorker(Worker worker) {
        return workers.contains(worker);
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
