package com.banktech.javachallenge.service.domain.submarine;

import com.banktech.javachallenge.service.domain.game.SimpleResponse;

import java.util.List;

public class SonarResponse extends SimpleResponse {
    private List<Submarine> entities;

    public SonarResponse() {
    }

    public SonarResponse(String message, Integer code) {
        super(message, code);
    }

    public List<Submarine> getEntities() {
        return entities;
    }

    public void setEntities(List<Submarine> entities) {
        this.entities = entities;
    }
}
