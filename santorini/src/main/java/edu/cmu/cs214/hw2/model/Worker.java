package edu.cmu.cs214.hw2.model;
import edu.cmu.cs214.hw2.player.*;

public class Worker {
    private Cell position;
    private final Player owner;

    public Worker(Player owner) {
        this.position = null;
        this.owner = owner;
    }


    public boolean canMove(Cell targetCell) {
        if (targetCell.isOccupied() || targetCell == null || position == null) {
            return false;
        }
        int dx = Math.abs(targetCell.getX() - position.getX());
        int dy = Math.abs(targetCell.getY() - position.getY());
        if (dx > 1 || dy > 1 || (dx == 0 && dy == 0)) {
            return false;
        }
        int dh = targetCell.getLevel() - position.getLevel();
        return dh <= 1;
    }

    public void move(Cell targetCell) {
        if (canMove(targetCell)) {
            position.setOccupancy(Occupancy.EMPTY);
            targetCell.setOccupancy(Occupancy.WORKER);
            position = targetCell;
        }

    }

    public boolean canBuild(Cell targetCell) {
        if (targetCell.isOccupied() || targetCell == null || position == null) {
            return false;
        }
        int dx = Math.abs(targetCell.getX() - position.getX());
        int dy = Math.abs(targetCell.getY() - position.getY());
        return dx <= 1 && dy <= 1 && (dx != 0 || dy != 0);
    }

    public void build(Cell targetCell) {
        if (canBuild(targetCell)) {
            targetCell.increaseLevel();
        }
    }


    public Cell getPosition() {
        return position;
    }

    public void setPosition(Cell position) {
        this.position = position;
    }

    public Player getOwner() {
        return owner;
    }
}
