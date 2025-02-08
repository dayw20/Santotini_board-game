package edu.cmu.cs214.hw2.game;

import java.util.ArrayList;
import java.util.List;
import edu.cmu.cs214.hw2.board.Board;
import edu.cmu.cs214.hw2.model.Worker;
import edu.cmu.cs214.hw2.player.Player;

public class Game {
    private Board board;
    private List<Player> players;
    private Player currPlayer;

    public Game() {
        this.board = new Board();
        this.players = new ArrayList<>();
    }

    public void startGame(Player playerA, Player playerB) {
        players.clear();
        players.add(playerA);
        players.add(playerB);
        currPlayer = playerA;
    }    

    public void nextTurn() {
        currPlayer = (currPlayer == players.get(0)) ? players.get(1) : players.get(0);
    }

    public Player checkWin() {
        for (Player player : players) {
            for (Worker worker : player.getWorkers()) {
                if (worker.getPosition() != null && 
                    worker.getPosition().getLevel() == 3) {
                    return player;
                }
            }
        }
        return null;
    }

    public Player getWinner() {
        return checkWin();
    }

    public boolean isGameOver() {
        return checkWin() != null;
    }
    public Player getCurrPlayer() {
        return currPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

}
