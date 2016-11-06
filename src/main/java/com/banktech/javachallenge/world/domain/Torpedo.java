package com.banktech.javachallenge.world.domain;

import com.banktech.javachallenge.service.domain.Position;


public class Torpedo {
    private Position position;

    public Torpedo() {
    }

    public Torpedo(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
