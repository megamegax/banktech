package com.banktech.javachallenge.service.domain;

import com.banktech.javachallenge.service.domain.submarine.Entity;


public class Torpedo extends Entity {
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

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

}
