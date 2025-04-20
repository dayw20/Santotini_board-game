package com.yvette.santorini.backend.dto;

public class StartGameRequest {
    private String playerAName;
    private String playerBName;
    private String playerAGod;
    private String playerBGod;
    
    public String getPlayerAGod() { return playerAGod; }
    public void setPlayerAGod(String god) { this.playerAGod = god; }
    public String getPlayerBGod() { return playerBGod; }
    public void setPlayerBGod(String god) { this.playerBGod = god; }

    public String getPlayerAName() {
        return playerAName;
    }

    public void setPlayerAName(String playerAName) {
        this.playerAName = playerAName;
    }

    public String getPlayerBName() {
        return playerBName;
    }

    public void setPlayerBName(String playerBName) {
        this.playerBName = playerBName;
    }
}
