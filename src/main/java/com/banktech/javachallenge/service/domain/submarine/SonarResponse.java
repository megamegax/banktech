package com.banktech.javachallenge.service.domain.submarine;

import com.banktech.javachallenge.service.domain.game.SimpleResponse;

import java.util.ArrayList;
import java.util.List;

public class SonarResponse extends SimpleResponse {
    private List<Entity> entities;

    public SonarResponse() {
        entities = new ArrayList<>();
    }

    public SonarResponse(String message, Integer code) {
        super(message, code);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        if (entities != null) {
            this.entities = entities;
            System.out.println("SONARRESPONSE ENTITY NULL!!!!!!!");
        }

    }
}
