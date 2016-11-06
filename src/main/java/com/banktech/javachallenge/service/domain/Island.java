package com.banktech.javachallenge.service.domain;

import com.banktech.javachallenge.service.domain.Position;


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
