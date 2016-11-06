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
import com.banktech.javachallenge.logic.GameLogic;
import com.banktech.javachallenge.logic.SimpleGameLogic;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.domain.submarine.SubmarineResponse;
import com.banktech.javachallenge.view.domain.ApiCall;
import com.banktech.javachallenge.view.GUIListener;
import com.banktech.javachallenge.view.domain.ViewModel;
import com.banktech.javachallenge.service.world.ClientWorld;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRunner {
    private List<ViewModel> turns = new ArrayList<>();
    private GUIListener listener;
    private long gameId;
    private int localRound;
    private GameLogic gameLogic = new SimpleGameLogic();

    GameRunner(GUIListener listener) {
        this.listener = listener;
        turns.add(new ViewModel());
        localRound = 0;
    }

    void listGames(Consumer<GameResponse> consumer) {
        Call<GameResponse> gameResponseCall = Api.gameService().listGames();

        gameResponseCall.enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {
                if (response.isSuccessful()) {
                    handleConnectionErrors(response);
                    if (response.body() != null) {
                        GameResponse gameResponse = response.body();
                        SimpleResponse simpleResponse = new SimpleResponse(gameResponse.getMessage(), gameResponse.getCode());
                        ApiCall apiCall = new ApiCall(Api.LIST_GAMES, "-", simpleResponse);
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

    private int getLastTurnNumber() {
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
            ApiCall apiCall = new ApiCall(Api.START_GAME, gameId, simpleResponse);
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
            ApiCall apiCall = new ApiCall(Api.JOIN_GAME, gameId, simpleResponse);
            refreshCallHistory(apiCall);
            loadGameInfo(gameId, game -> refreshGui());

        }
    }

    private void loadGameInfo(Long gameId, Consumer<Game> consumer) {
        Api.gameService().gameInfo(gameId).enqueue(new Callback<GameInfoResponse>() {
            @Override
            public void onResponse(Call<GameInfoResponse> call, Response<GameInfoResponse> response) {
                if (response.isSuccessful()) {
                    handleConnectionErrors(response);
                    if (response.body() != null) {
                        GameInfoResponse gameInfo = response.body();
                        getCurrentViewModel().setGame(gameInfo.getGame());
                        SimpleResponse simpleResponse = new SimpleResponse(gameInfo.getMessage(), gameInfo.getCode());
                        ApiCall apiCall = new ApiCall(Api.LOAD_GAME_INFO, gameId, simpleResponse);
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
        calls.forEach(System.out::println);
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
                        try {
                            if (getLastTurnNumber() < 151) {
                                loadOwnSubmarines();
                            }
                            alreadyUsedTurn[0] = true;
                            ViewModel updatedViewModel = gameLogic.step(getCurrentViewModel());
                            turns.set(getLastTurnNumber(), updatedViewModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        printLogs();
                    }
                }));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Játék vége!");
        });

    }

    private void loadOwnSubmarines() throws IOException {
        Response<SubmarineResponse> response = null;
        try {
            response = Api.submarineService().listSubmarines(gameId).execute();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Possible Timeout");
        }
        if (response != null && response.isSuccessful()) {
            handleConnectionErrors(response);
            if (response.body() != null) {
                List<OwnSubmarine> submarines = response.body().getSubmarines();
                SimpleResponse simpleResponse = new SimpleResponse(response.body().getMessage(), response.body().getCode());
                ApiCall apiCall = new ApiCall(Api.LOAD_OWN_SUBMARINES, gameId, simpleResponse);
                refreshCallHistory(apiCall);
                submarines.forEach(ownSubmarine ->
                        getCurrentViewModel().getWorldMap().replaceCell(ownSubmarine.getPosition(), ownSubmarine)
                );
                getCurrentViewModel().setOwnSubmarines(submarines);
            }
        }
    }

    private ViewModel getCurrentViewModel() {
        return turns.get(getLastTurnNumber());
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
