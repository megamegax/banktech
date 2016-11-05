package com.banktech.javachallenge.service;

import com.banktech.javachallenge.service.domain.game.CreateGameResponse;
import com.banktech.javachallenge.service.domain.game.GameInfoResponse;
import com.banktech.javachallenge.service.domain.game.GameResponse;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Service for GameResponse manipulation
 */
public interface GameService {

    /**
     * Létrehoz egy új játékot a csapat számára. A létrehozott játék azonosítója a válasz "id" mezőjében található.
     * Egyszerre egy csapat csak egy játékot hozhat létre. Ha már létezik játék akkor annak az azonosítóját adja vissza.
     *
     * @return CreateGameResponse
     */
    @POST("game")
    Call<CreateGameResponse> createGame();

    /**
     * GameResponse list
     * Lekéri a játékok listáját amibe a csapat csatlakozhat, és még nem csatlakozott.
     * return GameResponse
     */
    @GET("game")
    Call<GameResponse> listGames();

    /**
     * Csatlakozik a megadott azonosítójú játékhoz
     *
     * @param gameId gameId
     *               <p>
     *               ErrorCodes:
     *               1 - Nincs a csapat meghívva
     *               2 - Folyamatban lévő játék
     *               3 - Nem létező gameId
     *               </p>
     * @return SimpleResponse
     */
    @POST("game/{gameId}")
    Call<SimpleResponse> joinGame(@Path("gameId") Integer gameId);

    /**
     * Lekéri a megadott azonosítójú játék adatait.
     *
     * @param gameId gameId
     *               <p>
     *               id - A játék azonosítója
     *               round - Az aktuális kör száma
     *               scores - A játékban résztvevő csapatok pontszáma
     *               connectionStatus - A játékba meghívott csapatok csatlakozási státusza
     *               mapConfiguration - A játék konfigurációs paraméterei
     *               width - A pálya szélessége
     *               height - A pálya magassága
     *               islandPositions - A pályán található szigetek pozíciója (középpont)
     *               teamCount - A játékban részt vevő játékos csapatok száma
     *               submarinesPerTeam - Egy csapatnak hány tengeralattjárója van
     *               torpedoDamage - A torpedó robbanásakor a hatósugárban minden hajó ennyi életerő pontot veszít
     *               torpedoHitScore - A torpedó robbanásának sugarában lévő nem saját hajó esetén ennyi pontot szerez a csapat hajónként
     *               torpedoDestroyScore - A torpedoHitScore-n felül ennyi pontot kap a csapat a hajó megsemmisítéséért.
     *               torpedoHitPenalty - A torpedó robbanásának sugarában lévő saját hajó esetén ennyi pontot veszít a csapat hajónként
     *               torpedoCooldown - Torpedó indítása után ennyi kört kell várni a hajónak az újabb torpedó indításáig
     *               sonarRange - A szonár hatósugara
     *               extendedSonarRange - Az aktív szonár hatósugara
     *               extendedSonarRounds - Az aktív szonár ennyi körig marad bekapcsolva
     *               extendedSonarCooldown - Az aktív szonár bekapcsolása után ennyi kört kell várni a hajónak mielőtt újra aktiválhatja
     *               torpedoSpeed - A torpedók sebessége
     *               torpedoExplosionRadius - A torpedók robbanásának hatósugara
     *               roundLength - Egy kör hossza millisecundumban megadva
     *               islandSize - A szigetek méretének sugara
     *               submarineSize - A hajók méretének sugara
     *               rounds - A játék hossza körökben megadva
     *               maxSteeringPerRound - A hajó egy körben ennyivel módosíthatja az irányát (pozitív vagy negatív irányba)
     *               maxAccelerationPerRound - A hajó egy körben ennyivel módosíthatja a sebességét (pozitív vagy negatív irányba)
     *               maxSpeed - A hajó maximum ilyen sebességgel mozoghat
     *               torpedoRange - A kilőtt torpedó ennyi kör után semmisül meg ha nem ütközik semmivel
     *               rateLimitedPenalty - Egy körben hajónként kettőnél több azonos tipusú kérés esetén ennyi pontot veszít a csapat.*
     *               status - A játék státusza (WAITING, RUNNING, ENDED)
     *               </p>
     *               <p>Error codes:
     *               3 - Nem létező gameId</p>
     * @return GameInfoResponse
     */
    @GET("game/{gameId}")
    Call<GameInfoResponse> gameInfo(@Path("gameId") Integer gameId);

}
