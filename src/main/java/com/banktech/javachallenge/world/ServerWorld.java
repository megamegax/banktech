package com.banktech.javachallenge.world;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.Submarine;

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
    public Object cellAt(final Position position) {
        return map.get(position);
    }

    /**
     * Moves selected {@link Submarine} on the Map.
     *
     * @param submarine   {@link Submarine}
     * @param moveRequest {@link MoveRequest}
     */
    public void move(final Submarine submarine, final MoveRequest moveRequest) {
        Submarine selectedSubmarine = (Submarine) cellAt(submarine.getPosition());
        map.remove(submarine);
        Position newPosition = calculateMovement(selectedSubmarine, moveRequest);
        selectedSubmarine.setPosition(newPosition);
        map.put(newPosition, selectedSubmarine);
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
}
