package com.banktech.javachallenge;

import com.banktech.javachallenge.logic.ExtendedGameLogic;
import com.banktech.javachallenge.logic.GameLogic;
import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.game.*;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.domain.submarine.SubmarineResponse;
import com.banktech.javachallenge.service.world.ClientWorld;
import com.banktech.javachallenge.view.GUIListener;
import com.banktech.javachallenge.view.domain.ApiCall;
import com.banktech.javachallenge.view.domain.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GameRunner {
    private List<ViewModel> turns = new ArrayList<>();
    private GUIListener listener;
    private long gameId;
    private int localRound;
    private GameLogic firstGameLogic = new ExtendedGameLogic();
    private GameLogic extendedGameLogic = new ExtendedGameLogic();

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
        } catch (Exception e) {
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
        getCurrentViewModel().getCalls().add(apiCall);
    }

    private void refreshGui() {
        listener.refresh(turns);
    }

    void joinGame(Long gameId) {
        Response<SimpleResponse> response = null;
        try {
            response = Api.gameService().joinGame(gameId).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        handleConnectionErrors(response);
        this.gameId = gameId;
        if (response.body() != null) {
            SimpleResponse simpleResponse = response.body();
            ApiCall apiCall = new ApiCall(Api.JOIN_GAME, gameId, simpleResponse);
            refreshCallHistory(apiCall);
            loadGameInfo(gameId);
            refreshGui();
        }
    }

    private Game loadGameInfo(Long gameId) {
        try {
            Response<GameInfoResponse> response = Api.gameService().gameInfo(gameId).execute();
            if (response.isSuccessful()) {
                handleConnectionErrors(response);
                if (response.body() != null) {
                    GameInfoResponse gameInfo = response.body();
                    getCurrentViewModel().setGame(gameInfo.getGame());
                    SimpleResponse simpleResponse = new SimpleResponse(gameInfo.getMessage(), gameInfo.getCode());
                    ApiCall apiCall = new ApiCall(Api.LOAD_GAME_INFO, gameId, simpleResponse);
                    refreshCallHistory(apiCall);
                    getCurrentViewModel().setWorldMap(new ClientWorld(gameInfo.getGame()));
                    return gameInfo.getGame();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    private void printLogs() {
        System.out.println("------Turn: " + (getCurrentRound() + 1) + "------");
      /*  List<ApiCall> calls = getCurrentViewModel().getCalls();
        calls.forEach(System.out::println);*/
    }

    void play() {
        boolean alreadyUsedTurn = false;
        Game game = loadGameInfo(gameId);
        if (game != null) {
            System.out.println("Waiting for players to join...");
            while (game.getStatus().equals(Status.WAITING)) {
                System.out.println("waiting");
            }
            while (!game.getStatus().equals(Status.ENDED)) {
                if (isNextTurn(game)) {
                    incTurn();
                    alreadyUsedTurn = false;
                }
                if (!alreadyUsedTurn) {
                    try {
                        if (getLastTurnNumber() < game.getMapConfiguration().getRounds()) {
                            loadOwnSubmarines();
                        }
                        alreadyUsedTurn = true;

                        //  turns.set(getLastTurnNumber(), firstGameLogic.sonar(getCurrentViewModel(), getCurrentViewModel().getOwnSubmarines().get(0).getId()));
                        //    turns.set(getLastTurnNumber(), firstGameLogic.step(getCurrentViewModel(), getCurrentViewModel().getOwnSubmarines().get(0).getId(), );
                        //     turns.set(getLastTurnNumber(), extendedGameLogic.sonar(getCurrentViewModel(), getCurrentViewModel().getOwnSubmarines().get(1).getId()));
                        //    turns.set(getLastTurnNumber(), extendedGameLogic.step(getCurrentViewModel(), getCurrentViewModel().getOwnSubmarines().get(1).getId(), ));



                    /*    getCurrentViewModel().getOwnSubmarines().forEach(submarine ->
                                turns.set(getLastTurnNumber(), extendedGameLogic.sonar(getCurrentViewModel(), submarine.getId())));
*/
                        List<Position> fallbackPositions = new ArrayList<>(2);
                        fallbackPositions.add(new Position(game.getMapConfiguration().getWidth() - 100, game.getMapConfiguration().getHeight() / 2));
                        fallbackPositions.add(new Position(100, game.getMapConfiguration().getHeight() / 2));
                        int index = 0;
                        for (OwnSubmarine submarine : getCurrentViewModel().getOwnSubmarines()) {
                            ViewModel model = extendedGameLogic.step(getCurrentViewModel(), submarine.getId(), fallbackPositions.get(index));
                            turns.set(getLastTurnNumber(), model);
                            index++;
                            refreshGui();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    printLogs();
                }
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                game = loadGameInfo(gameId);
            }


        }
        System.out.println("Játék vége!");
        getCurrentViewModel().getCalls().add(new ApiCall("End of Game", "-", new SimpleResponse()));

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
                if (submarines != null)
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
