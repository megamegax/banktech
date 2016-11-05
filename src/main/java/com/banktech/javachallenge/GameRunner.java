package com.banktech.javachallenge;

import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.game.CreateGameResponse;
import com.banktech.javachallenge.service.domain.game.GameResponse;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.view.ApiCall;
import com.banktech.javachallenge.view.GUIListener;
import com.banktech.javachallenge.view.ViewModel;
import retrofit2.Response;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GameRunner {
    private List<ViewModel> turns = new ArrayList<>();
    private GUIListener listener;

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
        turns.get(getCurrentTurn()).getCalls().add(apiCall);
        listener.refresh(turns);
    }

    void joinGame(Long gameId) throws IOException {
        String method = Api.gameService().joinGame(gameId).request().method();
        String url = Api.gameService().joinGame(gameId).request().url().url().toString();

        Response<SimpleResponse> response = Api.gameService().joinGame(gameId).execute();
        handleConnectionErrors(response);
        if (response.body() != null) {
            SimpleResponse simpleResponse = response.body();
            ApiCall apiCall = new ApiCall(method, url, simpleResponse);
            refreshCallHistory(apiCall);
        }
    }


    public void printLogs() {
        List<ApiCall> calls = turns.get(getCurrentTurn()).getCalls();
        calls.forEach(apiCall -> System.out.println(apiCall.getMethod() + ":" + apiCall.getUrl() + " -> " + apiCall.getResponse()));
    }
}
