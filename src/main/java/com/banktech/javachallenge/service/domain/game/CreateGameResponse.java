package com.banktech.javachallenge.service.domain.game;


public class CreateGameResponse extends SimpleResponse {
    private Long id;

    public CreateGameResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
