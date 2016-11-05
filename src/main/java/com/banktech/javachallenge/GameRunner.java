package com.banktech.javachallenge;

import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.game.*;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.domain.submarine.SubmarineResponse;
import com.banktech.javachallenge.view.ApiCall;
import com.banktech.javachallenge.view.GUIListener;
import com.banktech.javachallenge.view.ViewModel;
import com.banktech.javachallenge.world.ClientWorld;
import retrofit2.Response;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GameRunner {
    private List<ViewModel> turns = new ArrayList<>();
    private GUIListener listener;
    private long gameId;

    GameRunner(GUIListener listener) {
        this.listener = listener;
        turns.add(new ViewModel());
    }


    GameResponse listGames() throws IOException {
        String method = Api.gameService().listGames().request().method();
        String url = Api.gameService().listGames().request().url().url().toString();

        Response<GameResponse> response = Api.gameService().listGames().execute();
        handleConnectionErrors(response);
        if (response.body() != null) {
            GameResponse gameResponse = response.body();
            SimpleResponse simpleResponse = new SimpleResponse(gameResponse.getMessage(), gameResponse.getCode());
            ApiCall apiCall = new ApiCall(method, url, simpleResponse);
            refreshCallHistory(apiCall);
            return gameResponse;
        }
        return null;
    }

    private void handleConnectionErrors(Response response) throws IOException {
        if (response.errorBody() != null) {
            System.out.println(response.message());
            System.out.println(response.errorBody().string());
        }
    }

    private int getCurrentTurn() {
        return turns.size() - 1;
    }

    CreateGameResponse startGame() throws IOException {
        String method = Api.gameService().createGame().request().method();
        String url = Api.gameService().createGame().request().url().url().toString();

        Response<CreateGameResponse> response = Api.gameService().createGame().execute();
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

    void joinGame(Long gameId) throws IOException {
        String method = Api.gameService().joinGame(gameId).request().method();
        String url = Api.gameService().joinGame(gameId).request().url().url().toString();

        Response<SimpleResponse> response = Api.gameService().joinGame(gameId).execute();
        handleConnectionErrors(response);
        this.gameId = gameId;
        if (response.body() != null) {
            SimpleResponse simpleResponse = response.body();
            ApiCall apiCall = new ApiCall(method, url, simpleResponse);

            loadGameInfo(gameId);
            refreshCallHistory(apiCall);
        }
    }

    private Game loadGameInfo(Long gameId) throws IOException {
        String method = Api.gameService().gameInfo(gameId).request().method();
        String url = Api.gameService().gameInfo(gameId).request().url().url().toString();

        Response<GameInfoResponse> response = Api.gameService().gameInfo(gameId).execute();
        handleConnectionErrors(response);
        if (response.body() != null) {
            GameInfoResponse gameInfo = response.body();
            getCurrentViewModel().setGame(gameInfo.getGame());
            SimpleResponse simpleResponse = new SimpleResponse(gameInfo.getMessage(), gameInfo.getCode());
            ApiCall apiCall = new ApiCall(method, url, simpleResponse);
            refreshCallHistory(apiCall);

            getCurrentViewModel().setWorldMap(new ClientWorld(gameInfo.getGame()));
            return gameInfo.getGame();
        }
        return null;
    }


    public void printLogs() {
        System.out.println("------Turn: " + (getCurrentTurn() + 1) + "------");
        List<ApiCall> calls = getCurrentViewModel().getCalls();
        calls.forEach(apiCall -> System.out.println(apiCall.getMethod() + ":" + apiCall.getUrl() + " -> " + apiCall.getResponse()));
    }

    void play() throws IOException {
        Game game = loadGameInfo(gameId);
        boolean alreadyUsedTurn = false;
        while (!game.getStatus().equals(Status.ENDED)) {
            if (isNextTurn(game)) {
                incTurn();
                alreadyUsedTurn = false;
            }
            if (!alreadyUsedTurn) {
                if (getCurrentTurn() < 151) {
                    loadOwnSubmarines();
                }
                alreadyUsedTurn = true;
                printLogs();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game = loadGameInfo(gameId);
        }

    }

    private void loadOwnSubmarines() throws IOException {
        String method = Api.gameService().gameInfo(gameId).request().method();
        String url = Api.gameService().gameInfo(gameId).request().url().url().toString();
        Response<SubmarineResponse> response = Api.submarineService().listSubmarines(gameId).execute();
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

    private ViewModel getCurrentViewModel() {
        return turns.get(getCurrentTurn());
    }

    private void incTurn() {
        turns.add(getCurrentViewModel().cloneToNewTurn());
    }

    private boolean isNextTurn(Game game) {
        return getCurrentTurn() < game.getRound();
    }
}
