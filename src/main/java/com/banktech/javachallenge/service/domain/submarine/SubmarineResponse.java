package com.banktech.javachallenge.service.domain.submarine;

import com.banktech.javachallenge.service.domain.game.SimpleResponse;

import java.util.List;

public class SubmarineResponse extends SimpleResponse {
    private List<OwnSubmarine> submarines;

    public SubmarineResponse() {
    }

    public List<OwnSubmarine> getSubmarines() {
        return submarines;
    }

    public void setSubmarines(List<OwnSubmarine> submarines) {
        this.submarines = submarines;
    }

    @Override
    public String toString() {
        return "SubmarineResponse{" +
                "submarines=" + submarines +
                '}';
    }
}
