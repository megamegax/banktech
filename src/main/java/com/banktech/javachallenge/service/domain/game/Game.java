package com.banktech.javachallenge.service.domain.game;


public class Game {
    private Long id;
    private Integer round;
    private Scores scores;
    private ConnectionStatus connectionStatus;
    private MapConfiguration mapConfiguration;
    private Status status;

    public Game() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Scores getScores() {
        return scores;
    }

    public void setScores(Scores scores) {
        this.scores = scores;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public MapConfiguration getMapConfiguration() {
        return mapConfiguration;
    }

    public void setMapConfiguration(MapConfiguration mapConfiguration) {
        this.mapConfiguration = mapConfiguration;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", round=" + round +
                ", scores=" + scores +
                ", connectionStatus=" + connectionStatus +
                ", mapConfiguration=" + mapConfiguration +
                ", status=" + status +
                '}';
    }
}
