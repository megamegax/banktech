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

    public double distance(Position position) {
        return Math.sqrt(Math.pow(position.x - x, 2) + Math.pow(position.y - y, 2));
    }

    @Override
    public String toString() {
        return "Position [x=" + x + ", y=" + y + "]";
    }

    public double angleTo(Position newDestination) {
        double x = newDestination.x - this.x;
        double y = newDestination.y - this.y;
        return Math.atan2(y, x) / Math.PI * 180;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (x == null) {
            if (other.x != null)
                return false;
        } else if (!x.equals(other.x))
            return false;
        if (y == null) {
            if (other.y != null)
                return false;
        } else if (!y.equals(other.y))
            return false;
        return true;
    }

    
}
