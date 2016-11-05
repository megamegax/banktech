package com.banktech.javachallenge.service.domain.game;

import java.util.List;

public class GameResponse extends SimpleResponse {
    private List<Long> games;

    public GameResponse() {
    }

    public List<Long> getGames() {
        return games;
    }

    public void setGames(List<Long> games) {
        this.games = games;
    }
}
