package com.banktech.javachallenge.service.domain.game;

import java.util.Map;

public class Scores {
    private Map<String, Integer> scores;

    public Scores() {
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }
}
