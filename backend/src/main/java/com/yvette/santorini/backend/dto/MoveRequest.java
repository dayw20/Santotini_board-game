package com.yvette.santorini.backend.dto;

public class MoveRequest {
    private String playerName;
    private int workerIndex;
    private int targetX;
    private int targetY;

    // Getters and Setters
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getWorkerIndex() { return workerIndex; }
    public void setWorkerIndex(int workerIndex) { this.workerIndex = workerIndex; }

    public int getTargetX() { return targetX; }
    public void setTargetX(int targetX) { this.targetX = targetX; }

    public int getTargetY() { return targetY; }
    public void setTargetY(int targetY) { this.targetY = targetY; }
}
