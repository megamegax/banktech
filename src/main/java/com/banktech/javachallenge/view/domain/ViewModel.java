package com.banktech.javachallenge.view.domain;

import com.banktech.javachallenge.service.domain.game.Game;
import com.banktech.javachallenge.service.domain.submarine.Entity;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.world.World;
import com.banktech.javachallenge.world.domain.Torpedo;

import java.util.ArrayList;
import java.util.List;

public class ViewModel {
    private Game game;
    private List<ApiCall> calls;
    private List<OwnSubmarine> ownSubmarines;
    private List<Entity> detectedSubmarines;
    private List<Torpedo> shootedTorpedos;
    private List<Entity> detectedTorpedos;
    private World worldMap;

    public ViewModel() {
        calls = new ArrayList<>();
        ownSubmarines = new ArrayList<>();
        detectedSubmarines = new ArrayList<>();
        shootedTorpedos = new ArrayList<>();
        detectedTorpedos = new ArrayList<>();
    }

    public ViewModel(Game game, List<ApiCall> calls, List<OwnSubmarine> ownSubmarines, List<Entity> detectedSubmarines,List<Entity>detectedTorpedos, List<Torpedo> shootedTorpedos, World worldMap) {
        this.game = game;
        this.calls = calls;
        this.ownSubmarines = ownSubmarines;
        this.detectedSubmarines = detectedSubmarines;
        this.shootedTorpedos = shootedTorpedos;
        this.detectedTorpedos = detectedTorpedos;
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

    public synchronized List<Entity> getDetectedSubmarines() {
        return detectedSubmarines;
    }

    public synchronized void setDetectedSubmarines(List<Entity> detectedSubmarines) {
        this.detectedSubmarines = detectedSubmarines;
    }

    public synchronized World getWorldMap() {
        return worldMap;
    }

    public synchronized void setWorldMap(World worldMap) {
        this.worldMap = worldMap;
    }

    public synchronized ViewModel cloneToNewTurn() {
        return new ViewModel(game, new ArrayList<>(), ownSubmarines, detectedSubmarines,detectedTorpedos, shootedTorpedos, worldMap);
    }

    public synchronized void setShootedTorpedos(List<Torpedo> shootedTorpedos){
        this.shootedTorpedos = shootedTorpedos;
    }
    public synchronized List<Torpedo> getShootedTorpedos() {
        return shootedTorpedos;
    }

    public List<Entity> getDetectedTorpedos() {
        return detectedTorpedos;
    }

    public void setDetectedTorpedos(List<Entity> detectedTorpedos) {
        this.detectedTorpedos = detectedTorpedos;
    }
}
