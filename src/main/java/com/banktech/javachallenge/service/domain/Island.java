package com.banktech.javachallenge.service.domain;


public class Island {
    private Position position;

    public Island() {
    }

    public Island(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
