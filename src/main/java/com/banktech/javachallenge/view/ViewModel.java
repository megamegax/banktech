package com.banktech.javachallenge.view;

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
    }

    public ViewModel(Game game, List<ApiCall> calls, List<OwnSubmarine> ownSubmarines, List<Submarine> detectedSubmarines, World worldMap) {
        this.game = game;
        this.calls = calls;
        this.ownSubmarines = ownSubmarines;
        this.detectedSubmarines = detectedSubmarines;
        this.worldMap = worldMap;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<ApiCall> getCalls() {
        return calls;
    }

    public void setCalls(List<ApiCall> calls) {
        this.calls = calls;
    }

    public List<OwnSubmarine> getOwnSubmarines() {
        return ownSubmarines;
    }

    public void setOwnSubmarines(List<OwnSubmarine> ownSubmarines) {
        this.ownSubmarines = ownSubmarines;
    }

    public List<Submarine> getDetectedSubmarines() {
        return detectedSubmarines;
    }

    public void setDetectedSubmarines(List<Submarine> detectedSubmarines) {
        this.detectedSubmarines = detectedSubmarines;
    }

    public World getWorldMap() {
        return worldMap;
    }

    public void setWorldMap(World worldMap) {
        this.worldMap = worldMap;
    }

    public ViewModel cloneToNewTurn() {
        return new ViewModel(game, new ArrayList<>(), ownSubmarines, detectedSubmarines, worldMap);
    }
}
