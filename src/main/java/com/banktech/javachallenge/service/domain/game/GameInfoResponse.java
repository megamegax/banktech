package com.banktech.javachallenge.service.domain.game;

public class GameInfoResponse extends SimpleResponse {
    private Game game;

    public GameInfoResponse() {
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
