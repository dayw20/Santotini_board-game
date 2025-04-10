package com.yvette.santorini.backend.dto;

public class WorkerSelectionRequest {
    private String playerName;
    private int workerIndex;

    // Getters and Setters
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getWorkerIndex() { return workerIndex; }
    public void setWorkerIndex(int workerIndex) { this.workerIndex = workerIndex; }
}