package com.banktech.javachallenge.service.domain.game;

import java.util.Map;


public class ConnectionStatus {

    Map<String, Boolean> connected;

    public ConnectionStatus() {
    }

    public Map<String, Boolean> getConnected() {
        return connected;
    }

    public void setConnected(Map<String, Boolean> connected) {
        this.connected = connected;
    }

    @Override
    public String toString() {
        return "ConnectionStatus{" +
                "connected=" + connected +
                '}';
    }
}
