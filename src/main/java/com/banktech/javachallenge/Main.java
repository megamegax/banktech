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
                System.out.println("running games: "+runningGames);

                if (runningGames != null && runningGames.getGames().isEmpty()) {
                    System.out.println("No games are running...");
                    CreateGameResponse createGameResponse = gameRunner.startGame();
                    System.out.println("Starting new game: " + createGameResponse.getId());
                    gameRunner.joinGame(createGameResponse.getId());
                    System.out.println("Joined to game: " + createGameResponse.getId());
                } else {
                    System.out.println(runningGames);
                }

                gameRunner.printLogs();
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
