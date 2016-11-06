package com.banktech.javachallenge.world;

import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.game.Game;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.ShootRequest;
import com.banktech.javachallenge.service.domain.submarine.Submarine;
import com.banktech.javachallenge.world.domain.Island;
import com.banktech.javachallenge.world.domain.Torpedo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ClientWorld implements World {
    private Position size;
    private Map<Position, Object> map;
    private long gameId;

    public ClientWorld(final Game game) {
        size = new Position(game.getMapConfiguration().getWidth(), game.getMapConfiguration().getHeight());
        map = new HashMap<>();
        this.gameId = game.getId();
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
     * @param selectedSubmarine {@link Submarine}
     * @param moveRequest       {@link MoveRequest}
     */
    @Override
    public void move(final Submarine selectedSubmarine, final MoveRequest moveRequest) throws IOException {
        //noinspection SuspiciousMethodCalls
        map.remove(selectedSubmarine);
        delegateMovementToServer(moveRequest, selectedSubmarine);
    }

    private void delegateMovementToServer(MoveRequest moveRequest, final Submarine selectedSubmarine) throws IOException {
        Api.submarineService().move(gameId, selectedSubmarine.getId(), moveRequest).execute();
    }

    /**
     * Shoot with selected {@link Submarine}.
     *
     * @param selectedSubmarine {@link Submarine}
     * @param shootRequest      {@link ShootRequest}
     */
    @Override
    public void shoot(Submarine selectedSubmarine, ShootRequest shootRequest) throws IOException {
        delegateShootToServer(shootRequest, selectedSubmarine);
    }
    
    @Override
    public void sonar(Submarine selectedSubmarine) throws IOException {
        Api.submarineService().sonar(gameId, selectedSubmarine.getId()).execute();
    }
    
    private void delegateShootToServer(ShootRequest shootRequest, Submarine selectedSubmarine) throws IOException {
        Api.submarineService().shoot(gameId, selectedSubmarine.getId(), shootRequest).execute();
    }

    public Position size() {
        return size;
    }
}
