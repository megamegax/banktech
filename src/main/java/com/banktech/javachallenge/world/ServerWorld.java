package com.banktech.javachallenge.world;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.ShootRequest;
import com.banktech.javachallenge.service.domain.submarine.SonarResponse;
import com.banktech.javachallenge.service.domain.submarine.Submarine;
import com.banktech.javachallenge.world.domain.Island;
import com.banktech.javachallenge.world.domain.Torpedo;

import java.io.IOException;
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
     * @return {@link Submarine} or {@link Island} or {@link Torpedo} or Null if nothing is there.
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
     * Moves selected {@link Submarine} on the Map.
     *
     * @param submarine   {@link Submarine}
     * @param moveRequest {@link MoveRequest}
     */
    @Override
    public SimpleResponse move(final Submarine submarine, final MoveRequest moveRequest) {
        Submarine selectedSubmarine = (Submarine) cellAt(submarine.getPosition());
        map.remove(submarine);
        Position newPosition = calculateMovement(selectedSubmarine, moveRequest);
        selectedSubmarine.setPosition(newPosition);
        map.put(newPosition, selectedSubmarine);
        return new SimpleResponse();
    }

    /**
     * Shoot with selected {@link Submarine}.
     *
     * @param submarine    {@link Submarine}
     * @param shootRequest {@link ShootRequest}
     */
    @Override
    public SimpleResponse shoot(Submarine submarine, ShootRequest shootRequest) {
        return new SimpleResponse();
    }

    /**
     * Calculates the new position of the {@link Submarine}.
     *
     * @param submarine   {@link Submarine}
     * @param moveRequest {@link MoveRequest}
     * @return new {@link Position} of {@link Submarine}
     */
    private Position calculateMovement(final Submarine submarine, MoveRequest moveRequest) {
        return submarine.getPosition();
    }

    public Position size() {
        return size;
    }

    @Override
    public SonarResponse sonar(Submarine selectedSubmarine) throws IOException {
        throw new UnsupportedOperationException();
    }
}
