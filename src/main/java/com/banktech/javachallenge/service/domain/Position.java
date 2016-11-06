package com.banktech.javachallenge.service.domain;

import com.banktech.javachallenge.service.domain.game.MapConfiguration;

public class Position {
    private Double x;
    private Double y;

    public Position() {
    }

    public Position(Integer x, Integer y) {
        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
    }

    public Position(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Position copy() {
        return new Position(x, y);
    }

    public boolean outSide(MapConfiguration mapConfiguration) {
        return x - mapConfiguration.getSubmarineSize() < 0 || y - mapConfiguration.getSubmarineSize() < 0
                || x + mapConfiguration.getSubmarineSize() > mapConfiguration.getWidth()
                || y + mapConfiguration.getSubmarineSize() > mapConfiguration.getHeight();
    }

    public boolean island(MapConfiguration mapConfiguration) {
        for (Position island : mapConfiguration.getIslandPositions()) {
            if (island.distance(this) <= mapConfiguration.getIslandSize()) {
                return true;
            }
        }
        return false;
    }

    private double distance(Position position) {
        return Math.sqrt(Math.pow(position.x - x, 2) + Math.pow(position.y - y, 2));
    }

    @Override
    public String toString() {
        return "Position [x=" + x + ", y=" + y + "]";
    }

}
