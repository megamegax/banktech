package com.banktech.javachallenge.world;

import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.game.Game;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.submarine.*;
import com.banktech.javachallenge.world.domain.Island;
import com.banktech.javachallenge.world.domain.Torpedo;
import retrofit2.Response;

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
     * @param selectedSubmarine {@link OwnSubmarine}
     * @param moveRequest       {@link MoveRequest}
     */
    @Override
    public SimpleResponse move(final OwnSubmarine selectedSubmarine, final MoveRequest moveRequest) throws IOException {
        //noinspection SuspiciousMethodCalls
        map.remove(selectedSubmarine);
        return delegateMovementToServer(moveRequest, selectedSubmarine);
    }

    private SimpleResponse delegateMovementToServer(MoveRequest moveRequest, final Entity selectedSubmarine) throws IOException {
        try {
            Response<SimpleResponse> response = Api.submarineService().move(gameId, selectedSubmarine.getId(), moveRequest).execute();
            return response.body();
        } catch (NullPointerException e) {
            return new SonarResponse("Timeout", 400);
        }
    }

    /**
     * Shoot with selected {@link OwnSubmarine}.
     *
     * @param selectedSubmarine {@link OwnSubmarine}
     * @param shootRequest      {@link ShootRequest}
     */
    @Override
    public SimpleResponse shoot(OwnSubmarine selectedSubmarine, ShootRequest shootRequest) throws IOException {
        return delegateShootToServer(shootRequest, selectedSubmarine);
    }

    @Override
    public SonarResponse sonar(OwnSubmarine selectedSubmarine) throws IOException {
        return delegateSonarToServer(selectedSubmarine);
    }

    private SonarResponse delegateSonarToServer(Entity selectedSubmarine) throws IOException {
        try {
            Response<SonarResponse> response = Api.submarineService().sonar(gameId, selectedSubmarine.getId()).execute();
            return response.body();
        } catch (NullPointerException e) {
            return new SonarResponse("Timeout", 400);
        }
    }

    @Override
    public SonarResponse extendedSonar(OwnSubmarine selectedSubmarine) throws IOException {
        return delegateExtendedSonarToServer(selectedSubmarine);
    }

    private SonarResponse delegateExtendedSonarToServer(Entity selectedSubmarine) throws IOException {
        try {
            Response<SonarResponse> response = Api.submarineService().extendSonar(gameId, selectedSubmarine.getId()).execute();
            return response.body();
        } catch (NullPointerException e) {
            return new SonarResponse("Timeout", 400);
        }
    }

    private SimpleResponse delegateShootToServer(ShootRequest shootRequest, Entity selectedSubmarine) throws IOException {
        try {
            Response<SimpleResponse> response = Api.submarineService().shoot(gameId, selectedSubmarine.getId(), shootRequest).execute();
            return response.body();
        } catch (NullPointerException e) {
            return new SimpleResponse("Timeout", 400);
        }
    }

    public Position size() {
        return size;
    }
}
