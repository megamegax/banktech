package com.banktech.javachallenge.world;

import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.game.Game;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.ShootRequest;
import com.banktech.javachallenge.service.domain.submarine.SonarResponse;
import com.banktech.javachallenge.service.domain.submarine.Submarine;
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
    public SimpleResponse move(final Submarine selectedSubmarine, final MoveRequest moveRequest) throws IOException {
        //noinspection SuspiciousMethodCalls
        map.remove(selectedSubmarine);
        return delegateMovementToServer(moveRequest, selectedSubmarine);
    }

    private SimpleResponse delegateMovementToServer(MoveRequest moveRequest, final Submarine selectedSubmarine) throws IOException {
        try {
            Response<SimpleResponse> response = Api.submarineService().move(gameId, selectedSubmarine.getId(), moveRequest).execute();
            return response.body();
        } catch (NullPointerException e) {
            return new SonarResponse("Timeout", 400);
        }
    }

    /**
     * Shoot with selected {@link Submarine}.
     *
     * @param selectedSubmarine {@link Submarine}
     * @param shootRequest      {@link ShootRequest}
     */
    @Override
    public SimpleResponse shoot(Submarine selectedSubmarine, ShootRequest shootRequest) throws IOException {
        return delegateShootToServer(shootRequest, selectedSubmarine);
    }

    @Override
    public SonarResponse sonar(Submarine selectedSubmarine) throws IOException {
        return delegateSonarToServer(selectedSubmarine);
    }

    private SonarResponse delegateSonarToServer(Submarine selectedSubmarine) throws IOException {
        try {
            Response<SonarResponse> response = Api.submarineService().sonar(gameId, selectedSubmarine.getId()).execute();
            return response.body();
        } catch (NullPointerException e) {
            return new SonarResponse("Timeout", 400);
        }
    }
    
    @Override
    public SonarResponse extendedSonar(Submarine selectedSubmarine) throws IOException {
        return delegateExtendedSonarToServer(selectedSubmarine);
    }

    private SonarResponse delegateExtendedSonarToServer(Submarine selectedSubmarine) throws IOException {
        try {
            Response<SonarResponse> response = Api.submarineService().extendSonar(gameId, selectedSubmarine.getId()).execute();
            return response.body();
        } catch (NullPointerException e) {
            return new SonarResponse("Timeout", 400);
        }
    }
        
    private SimpleResponse delegateShootToServer(ShootRequest shootRequest, Submarine selectedSubmarine) throws IOException {
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
