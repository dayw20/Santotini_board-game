package com.yvette.santorini.backend.dto;

import java.util.List;

public class GameStateResponse {
    private String currentPlayer;
    private String winner;
    private String phase;
    private boolean gameOver;
    private List<CellState> board;
    private List<String> players;

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
    this.players = players;
}

    // Getters and Setters
    public String getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(String currentPlayer) { this.currentPlayer = currentPlayer; }

    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public List<CellState> getBoard() { return board; }
    public void setBoard(List<CellState> board) { this.board = board; }

    public String getPhase() { return phase; }
    public void setPhase(String phase) { this.phase = phase; }
}

