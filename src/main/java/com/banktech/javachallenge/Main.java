package com.banktech.javachallenge;

import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.game.GameResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        startUp(args);
        exampleApiCall();
        exampleApiCallAsync();
    }

    private static void exampleApiCall() {
        try {
            Response<GameResponse> response = Api.gameService().listGames().execute();
            GameResponse runningGames = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exampleApiCallAsync() {
        Api.gameService().listGames().enqueue(new Callback<GameResponse>() {
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {
                GameResponse runningGames = response.body();
            }

            public void onFailure(Call<GameResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private static void startUp(String[] args) {
        if (args.length == 2) {
            setUpApi(args[0], args[1]);
        } else {
            setUpApi();
        }
    }

    private static void setUpApi() {
        Api.initialize();
    }

    private static void setUpApi(String baseUrl, String port) {
        Integer portNumber = convertToInt(port);
        Api.initialize(baseUrl, portNumber);
    }

    private static Integer convertToInt(String port) {
        return Integer.parseInt(port);
    }
}
