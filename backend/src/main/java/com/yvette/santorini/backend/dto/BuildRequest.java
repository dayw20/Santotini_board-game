package com.yvette.santorini.backend.dto;

public class BuildRequest {
    private String playerName;
    private int workerIndex;
    private int targetX;
    private int targetY;
    private boolean buildDome;


    // Getters and Setters
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getWorkerIndex() { return workerIndex; }
    public void setWorkerIndex(int workerIndex) { this.workerIndex = workerIndex; }

    public int getTargetX() { return targetX; }
    public void setTargetX(int targetX) { this.targetX = targetX; }

    public int getTargetY() { return targetY; }
    public void setTargetY(int targetY) { this.targetY = targetY; }

    public boolean isBuildDome() { return buildDome; }
    public void setBuildDome(boolean buildDome) { this.buildDome = buildDome; }
}

