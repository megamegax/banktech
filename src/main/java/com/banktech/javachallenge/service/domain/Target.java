package com.banktech.javachallenge.service.domain;

public class Target {

    private Long targetId;
    private Position position;

    public Target(Long targetId, Position position) {
        this.targetId = targetId;
        this.position = position;
    }

    public synchronized Long getTargetId() {
        return targetId;
    }

    public synchronized Position getPosition() {
        return position;
    }

}
