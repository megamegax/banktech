package com.banktech.javachallenge.view.domain;

import com.banktech.javachallenge.service.domain.game.Game;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.domain.submarine.Submarine;
import com.banktech.javachallenge.world.World;

import java.util.ArrayList;
import java.util.List;


public class ViewModel {
    private Game game;
    private List<ApiCall> calls;
    private List<OwnSubmarine> ownSubmarines;
    private List<Submarine> detectedSubmarines;
    private World worldMap;

    public ViewModel() {
        calls = new ArrayList<>();
        ownSubmarines = new ArrayList<>();
        detectedSubmarines = new ArrayList<>();
    }

    public ViewModel(Game game, List<ApiCall> calls, List<OwnSubmarine> ownSubmarines, List<Submarine> detectedSubmarines, World worldMap) {
        this.game = game;
        this.calls = calls;
        this.ownSubmarines = ownSubmarines;
        this.detectedSubmarines = detectedSubmarines;
        this.worldMap = worldMap;
    }

    public synchronized Game getGame() {
        return game;
    }

    public synchronized void setGame(Game game) {
        this.game = game;
    }

    public synchronized List<ApiCall> getCalls() {
        return calls;
    }

    public synchronized void setCalls(List<ApiCall> calls) {
        this.calls = calls;
    }

    public synchronized List<OwnSubmarine> getOwnSubmarines() {
        return ownSubmarines;
    }

    public synchronized void setOwnSubmarines(List<OwnSubmarine> ownSubmarines) {
        this.ownSubmarines = ownSubmarines;
    }

    public synchronized List<Submarine> getDetectedSubmarines() {
        return detectedSubmarines;
    }

    public synchronized void setDetectedSubmarines(List<Submarine> detectedSubmarines) {
        this.detectedSubmarines = detectedSubmarines;
    }

    public synchronized World getWorldMap() {
        return worldMap;
    }

    public synchronized void setWorldMap(World worldMap) {
        this.worldMap = worldMap;
    }

    public synchronized ViewModel cloneToNewTurn() {
        return new ViewModel(game, new ArrayList<>(), ownSubmarines, detectedSubmarines, worldMap);
    }
}
