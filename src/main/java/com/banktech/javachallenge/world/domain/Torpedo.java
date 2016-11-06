package com.banktech.javachallenge.world.domain;

import com.banktech.javachallenge.service.domain.Position;


public class Torpedo {
    private Position currentPosition;
    private Position startPosition;
    private Double speed;
    private Double angle;
    private Integer startRound;

    public Torpedo() {
    }

    public Torpedo(Position startPosition, Double speed, Double angle, Integer startRound) {
        this.startPosition = startPosition;
        this.speed = speed;
        this.angle = angle;
        this.startRound = startRound;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }
}
