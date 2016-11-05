package com.banktech.javachallenge.world;

import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.ShootRequest;
import com.banktech.javachallenge.service.domain.submarine.Submarine;
import com.banktech.javachallenge.service.domain.submarine.SubmarineResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ClientWorld implements World {
    private Position size;
    private Map<Position, Object> map;
    private long gameId;

    public ClientWorld(final long gameId, final int width, final int height) {
        size = new Position(width, height);
        map = new HashMap<>();
        this.gameId = gameId;
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

    /**
     * Moves selected {@link Submarine} on the Map.
     *
     * @param selectedSubmarine {@link Submarine}
     * @param moveRequest       {@link MoveRequest}
     */
    @Override
    public void move(final Submarine selectedSubmarine, final MoveRequest moveRequest) {
        //noinspection SuspiciousMethodCalls
        map.remove(selectedSubmarine);
        delegateMovementToServer(moveRequest, selectedSubmarine);
    }

    private void delegateMovementToServer(MoveRequest moveRequest, final Submarine selectedSubmarine) {
        Api.submarineService().move(gameId, selectedSubmarine.getId(), moveRequest).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Api.submarineService().listSubmarines(gameId).enqueue(new Callback<List<SubmarineResponse>>() {
                    @Override
                    public void onResponse(Call<List<SubmarineResponse>> call, Response<List<SubmarineResponse>> response) {
                        if (response.body() != null) {
                            List<SubmarineResponse> submarineResponses = response.body();
                            Optional<SubmarineResponse> newSubmarine = submarineResponses.stream().filter(submarineResponse -> submarineResponse.getId() == selectedSubmarine.getId()).findFirst();
                            if (newSubmarine.isPresent()) {
                                Position newPosition = newSubmarine.get().getPosition();
                                selectedSubmarine.setPosition(newPosition);
                                map.put(newPosition, selectedSubmarine);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SubmarineResponse>> call, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    /**
     * Shoot with selected {@link Submarine}.
     *
     * @param selectedSubmarine {@link Submarine}
     * @param shootRequest      {@link ShootRequest}
     */
    @Override
    public void shoot(Submarine selectedSubmarine, ShootRequest shootRequest) {
        delegateShootToServer(shootRequest, selectedSubmarine);
    }

    private void delegateShootToServer(ShootRequest shootRequest, Submarine selectedSubmarine) {
        Api.submarineService().shoot(gameId, selectedSubmarine.getId(), shootRequest).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable throwable) {

            }
        });
    }

    public Position size() {
        return size;
    }
}
