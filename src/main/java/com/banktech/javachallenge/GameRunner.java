package com.banktech.javachallenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.game.CreateGameResponse;
import com.banktech.javachallenge.service.domain.game.Game;
import com.banktech.javachallenge.service.domain.game.GameInfoResponse;
import com.banktech.javachallenge.service.domain.game.GameResponse;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.game.Status;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.domain.submarine.SubmarineResponse;
import com.banktech.javachallenge.view.ApiCall;
import com.banktech.javachallenge.view.GUIListener;
import com.banktech.javachallenge.view.ViewModel;
import com.banktech.javachallenge.world.ClientWorld;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GameRunner {
    private List<ViewModel> turns = new ArrayList<>();
    private GUIListener listener;
    private long gameId;
    private int localRound;

    GameRunner(GUIListener listener) {
        this.listener = listener;
        turns.add(new ViewModel());
        localRound = 0;
    }


    void listGames(Consumer<GameResponse> consumer) {
        Call<GameResponse> gameResponseCall = Api.gameService().listGames();
        String method = gameResponseCall.request().method();
        String url = gameResponseCall.request().url().url().toString();
        gameResponseCall.enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {
                if (response.isSuccessful()) {
                    handleConnectionErrors(response);
                    if (response.body() != null) {
                        GameResponse gameResponse = response.body();
                        SimpleResponse simpleResponse = new SimpleResponse(gameResponse.getMessage(), gameResponse.getCode());
                        ApiCall apiCall = new ApiCall(method, url, simpleResponse);
                        refreshCallHistory(apiCall);

                        consumer.accept(gameResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<GameResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }


    private void handleConnectionErrors(Response response) {
        if (response != null) {
            if (response.errorBody() != null) {
                System.out.println(response.message());
                try {
                    System.out.println(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int getCurrentTurn() {
        return turns.size() - 1;
    }


    private int getCurrentRound() {
        if (turns.size() == 0) {
            return 1;
        } else {
            if (turns.get(turns.size() - 1).getGame() == null) {
                return turns.size();
            } else {
                return turns.get(turns.size() - 1).getGame().getRound();
            }
        }
    }

    CreateGameResponse startGame() {
        String method = Api.gameService().createGame().request().method();
        String url = Api.gameService().createGame().request().url().url().toString();

        Response<CreateGameResponse> response = null;
        try {
            response = Api.gameService().createGame().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handleConnectionErrors(response);

        if (response.body() != null) {
            CreateGameResponse createGameResponse = response.body();
            SimpleResponse simpleResponse = new SimpleResponse(createGameResponse.getMessage(), createGameResponse.getCode());
            ApiCall apiCall = new ApiCall(method, url, simpleResponse);
            refreshCallHistory(apiCall);
            return createGameResponse;
        }
        return null;
    }

    private void refreshCallHistory(ApiCall apiCall) {
        System.out.println(apiCall);
        getCurrentViewModel().getCalls().add(apiCall);
        listener.refresh(turns);
    }

    private void refreshGui() {
        listener.refresh(turns);
    }

    void joinGame(Long gameId) {
        String method = Api.gameService().joinGame(gameId).request().method();
        String url = Api.gameService().joinGame(gameId).request().url().url().toString();

        Response<SimpleResponse> response = null;
        try {
            response = Api.gameService().joinGame(gameId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handleConnectionErrors(response);
        this.gameId = gameId;
        if (response.body() != null) {
            SimpleResponse simpleResponse = response.body();
            ApiCall apiCall = new ApiCall(method, url, simpleResponse);
            refreshCallHistory(apiCall);
            loadGameInfo(gameId, game -> refreshGui());

        }
    }

    private void loadGameInfo(Long gameId, Consumer<Game> consumer) {
        String method = Api.gameService().gameInfo(gameId).request().method();
        String url = Api.gameService().gameInfo(gameId).request().url().url().toString();
        Api.gameService().gameInfo(gameId).enqueue(new Callback<GameInfoResponse>() {
            @Override
            public void onResponse(Call<GameInfoResponse> call, Response<GameInfoResponse> response) {
                if (response.isSuccessful()) {
                    handleConnectionErrors(response);
                    if (response.body() != null) {
                        GameInfoResponse gameInfo = response.body();
                        getCurrentViewModel().setGame(gameInfo.getGame());
                        SimpleResponse simpleResponse = new SimpleResponse(gameInfo.getMessage(), gameInfo.getCode());
                        ApiCall apiCall = new ApiCall(method, url, simpleResponse);
                        refreshCallHistory(apiCall);
                        consumer.accept(gameInfo.getGame());
                        getCurrentViewModel().setWorldMap(new ClientWorld(gameInfo.getGame()));
                    }
                }
            }

            @Override
            public void onFailure(Call<GameInfoResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }


    public void printLogs() {
        System.out.println("------Turn: " + (getCurrentRound() + 1) + "------");
        List<ApiCall> calls = getCurrentViewModel().getCalls();
        calls.forEach(apiCall -> System.out.println(apiCall.getMethod() + ":" + apiCall.getUrl() + " -> " + apiCall.getResponse()));
    }

    void play() {
        loadGameInfo(gameId, (game) -> {
            final boolean[] alreadyUsedTurn = {false};
            final boolean[] finish = {false};
            while (!finish[0]) {
                loadGameInfo(gameId, (newGame -> {
                    if (newGame.getStatus().equals(Status.ENDED)) {
                        System.out.println(newGame);
                        finish[0] = true;
                    }
                    if (isNextTurn(newGame)) {
                        incTurn();
                        alreadyUsedTurn[0] = false;
                    }
                    if (!alreadyUsedTurn[0]) {
                        if (getCurrentTurn() < 151) {
                            loadOwnSubmarines();
                        }
                        alreadyUsedTurn[0] = true;
                        try {
                            int maxSpeed = getCurrentViewModel().getGame().getMapConfiguration().getMaxSpeed();
                            int maxAccel = getCurrentViewModel().getGame().getMapConfiguration().getMaxAccelerationPerRound();
                            List<OwnSubmarine> ownSubmarines = getCurrentViewModel().getOwnSubmarines();
                            if (!ownSubmarines.isEmpty()) {
                                OwnSubmarine ownSubmarine = ownSubmarines.get(0);
                                int speedChange = 0;
                                if (ownSubmarine.getVelocity() < maxSpeed) {
                                    speedChange = Math.min(maxSpeed - ownSubmarine.getVelocity(), maxAccel);
                                }
                                getCurrentViewModel().getWorldMap().move(ownSubmarine, new MoveRequest((double)speedChange, 15.0));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        printLogs();
                    }
                }
                ));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Játék vége!");
        });


    }

    private void loadOwnSubmarines() {
        String method = Api.gameService().gameInfo(gameId).request().method();
        String url = Api.gameService().gameInfo(gameId).request().url().url().toString();
        Api.submarineService().listSubmarines(gameId).enqueue(new Callback<SubmarineResponse>() {
            @Override
            public void onResponse(Call<SubmarineResponse> call, Response<SubmarineResponse> response) {
                if (response.isSuccessful()) {
                    handleConnectionErrors(response);
                    if (response.body() != null) {
                        List<OwnSubmarine> submarines = response.body().getSubmarines();
                        SimpleResponse simpleResponse = new SimpleResponse(response.body().getMessage(), response.body().getCode());
                        ApiCall apiCall = new ApiCall(method, url, simpleResponse);
                        refreshCallHistory(apiCall);
                        submarines.forEach(ownSubmarine -> {
                            getCurrentViewModel().getWorldMap().replaceCell(ownSubmarine.getPosition(), ownSubmarine);
                        });

                        getCurrentViewModel().setOwnSubmarines(submarines);
                    }
                }
            }

            @Override
            public void onFailure(Call<SubmarineResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }

    private ViewModel getCurrentViewModel() {
        return turns.get(getCurrentTurn());
    }

    private void incTurn() {
        turns.add(getCurrentViewModel().cloneToNewTurn());
    }

    private boolean isNextTurn(Game game) {
        int lastLocalRound = localRound;
        int remoteRound = game.getRound();
        localRound = remoteRound;
        return lastLocalRound < remoteRound;
    }
}
