package com.banktech.javachallenge;

import com.banktech.javachallenge.service.*;
import com.banktech.javachallenge.service.domain.game.CreateGameResponse;
import com.banktech.javachallenge.service.domain.game.GameResponse;
import com.banktech.javachallenge.view.GUIListener;
import com.banktech.javachallenge.view.ViewModel;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        startUp(args);
        GUIListener guiListener = new GUIListener() {
            @Override
            public void refresh(List<ViewModel> turns) {
                //refresh gui
            }
        };
        GameRunner gameRunner = new GameRunner(guiListener);
        new Thread(() -> {
            try {
                GameResponse runningGames = gameRunner.listGames();
                if (runningGames.getGames().isEmpty()) {
                    CreateGameResponse createGameResponse = gameRunner.startGame();
                    gameRunner.joinGame(createGameResponse.getId());
                }else{
                    System.out.println(runningGames);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

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
