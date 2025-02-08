package edu.cmu.cs214.hw2.player;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs214.hw2.model.*;

public class Player {
    private String name;
    private List<Worker> workers;

    public Player(String name) {
        this.name = name;
        this.workers = new ArrayList<>();
        workers.add(new Worker(this));
        workers.add(new Worker(this));
    }

    public void moveWorker(Worker worker, Cell targetCell) {
        if (workers.contains(worker)) {
            worker.move(targetCell);
        }
    }

    public void buildWithWorker(Worker worker, Cell targetCell) {
        if (workers.contains(worker)) {
            worker.build(targetCell);
        }
    }

    public List<Worker> getWorkers() {
        return new ArrayList<>(workers);
    }
    
    public String getName() {
        return name;
    }
}
