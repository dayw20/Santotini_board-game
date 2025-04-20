package com.yvette.santorini.backend.dto;

public class CellState {
    private int x;
    private int y;
    private int level;
    private String occupancy; // "EMPTY", "WORKER", "DOME"
    private String player; // if worker, which player
    private Integer workerIndex; // if worker, worker 0 or 1
    private String highlightType = "NONE";



    // Getters and Setters
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getOccupancy() { return occupancy; }
    public void setOccupancy(String occupancy) { this.occupancy = occupancy; }

    public String getPlayer() { return player; }
    public void setPlayer(String player) { this.player = player; }

    public Integer getWorkerIndex() { return workerIndex; }
    public void setWorkerIndex(Integer workerIndex) { this.workerIndex = workerIndex; }

    public String getHighlightType() {
        return highlightType;
    }
    
    public void setHighlightType(String highlightType) {
        this.highlightType = highlightType;
    }
    
}
