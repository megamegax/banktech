package com.banktech.javachallenge.service.domain;

public class ProjectedPosition {

    private Position position;

    private double round;

    public ProjectedPosition(Position position, double round) {
        this.position = position;
        this.round = round;
    }

    public synchronized Position getPosition() {
        return position;
    }

    public synchronized double getRound() {
        return round;
    }

    @Override
    public String toString() {
        return "ProjectedPosition [position=" + position + ", round=" + round + "]";
    }

}
