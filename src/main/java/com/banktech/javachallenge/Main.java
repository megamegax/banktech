package com.banktech.javachallenge;

import java.io.IOException;

import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.game.CreateGameResponse;
import com.banktech.javachallenge.view.gui.jfx.TorpedoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 */
public class Main extends Application {

    public static void main(String[] args) {
        startUp(args);
        //  View view = new View();
        //  GameRunner gameRunner = new GameRunner(view);
        launch(args);


    }

    private static void initializeGame(GameRunner gameRunner) throws IOException {
        gameRunner.listGames(runningGames -> {
            if (runningGames != null && runningGames.getGames().isEmpty()) {
                System.out.println("No games are running...");
                CreateGameResponse createGameResponse = gameRunner.startGame();
                System.out.println("Starting new game: " + createGameResponse.getId());
                gameRunner.joinGame(createGameResponse.getId());
                System.out.println("Joined to game: " + createGameResponse.getId());
                gameRunner.play();
                System.out.println("Control added to GameRunner.");
            } else {
                System.out.println("Game already running: " + runningGames.getGames().get(0));
                gameRunner.joinGame(runningGames.getGames().get(0));
                System.out.println("Joined to game: " + runningGames.getGames().get(0));
                gameRunner.play();
                System.out.println("Control added to GameRunner.");
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

    public static String ourTeamName() {
        return "Nightmare Build";
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("Torpedo");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Torpedo.fxml"));
        Pane panel = loader.load();
        Scene scene = new Scene(panel);
        primaryStage.setScene(scene);

        TorpedoController torpedoController = loader.getController();
        primaryStage.show();
        GameRunner gameRunner = new GameRunner(torpedoController);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        new Thread(() -> {
            try {
                initializeGame(gameRunner);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
