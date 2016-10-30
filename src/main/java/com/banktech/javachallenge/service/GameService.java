package com.banktech.javachallenge.service;

import com.banktech.javachallenge.service.domain.game.CreateGameResponse;
import com.banktech.javachallenge.service.domain.game.GameResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Service for GameResponse manipulation
 */
public interface GameService {

    /**
     * Létrehoz egy új játékot a csapat számára. A létrehozott játék azonosítója a válasz "id" mezőjében található.
     * Egyszerre egy csapat csak egy játékot hozhat létre. Ha már létezik játék akkor annak az azonosítóját adja vissza.
     *
     * @return
     */
    @POST("/game")
    Call<CreateGameResponse> createGame();

    /**
     * GameResponse list
     * Lekéri a játékok listáját amibe a csapat csatlakozhat, és még nem csatlakozott.
     */
    @GET("/game")
    Call<GameResponse> listGames();


}
