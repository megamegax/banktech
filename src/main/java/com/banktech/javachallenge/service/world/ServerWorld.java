package com.banktech.javachallenge.service.world;

import com.banktech.javachallenge.service.domain.Island;
import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.Torpedo;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.domain.submarine.ShootRequest;
import com.banktech.javachallenge.service.domain.submarine.SonarResponse;

import java.util.HashMap;
import java.util.Map;


public class ServerWorld implements World {
    private Position size;
    private Map<Position, Object> map;

    public ServerWorld(final int width, final int height) {
        size = new Position(width, height);
        map = new HashMap<>();
    }

    /**
     * Gives back the Cell on the given {@link Position}.
     *
     * @param position {@link Position}
     * @return {@link OwnSubmarine} or {@link Island} or {@link Torpedo} or Null if nothing is there.
     */
    @Override
    public Object cellAt(final Position position) {
        return map.get(position);
    }

    @Override
    public void replaceCell(Position position, Object object) {
        if (map.get(position) != null) {
            map.replace(position, object);
        } else {
            map.put(position, object);
        }
    }

    /**
     * Moves selected {@link OwnSubmarine} on the Map.
     *
     * @param submarine   {@link OwnSubmarine}
     * @param moveRequest {@link MoveRequest}
     */
    @Override
    public SimpleResponse move(final OwnSubmarine submarine, final MoveRequest moveRequest) {
        OwnSubmarine selectedSubmarine = (OwnSubmarine) cellAt(submarine.getPosition());
        map.remove(submarine);
        Position newPosition = calculateMovement(selectedSubmarine, moveRequest);
        selectedSubmarine.setPosition(newPosition);
        map.put(newPosition, selectedSubmarine);
        return new SimpleResponse();
    }

    /**
     * Shoot with selected {@link OwnSubmarine}.
     *
     * @param submarine    {@link OwnSubmarine}
     * @param shootRequest {@link ShootRequest}
     */
    @Override
    public SimpleResponse shoot(OwnSubmarine submarine, ShootRequest shootRequest) {
        return new SimpleResponse();
    }

    /**
     * Calculates the new position of the {@link OwnSubmarine}.
     *
     * @param submarine   {@link OwnSubmarine}
     * @param moveRequest {@link MoveRequest}
     * @return new {@link Position} of {@link OwnSubmarine}
     */
    private Position calculateMovement(final OwnSubmarine submarine, MoveRequest moveRequest) {
        return submarine.getPosition();
    }

    public Position size() {
        return size;
    }

    @Override
    public SonarResponse sonar(OwnSubmarine selectedSubmarine)  {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public SonarResponse extendedSonar(OwnSubmarine selectedSubmarine) {
        throw new UnsupportedOperationException();
    }
    
}
