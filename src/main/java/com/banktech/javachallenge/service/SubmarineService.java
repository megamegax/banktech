package com.banktech.javachallenge.service;

import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.ShootRequest;
import com.banktech.javachallenge.service.domain.submarine.SonarResponse;
import com.banktech.javachallenge.service.domain.submarine.SubmarineResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

/**
 *
 */
public interface SubmarineService {


    /**
     * Lekéri a csapathoz tartozó tengeralattjárók státuszát.
     * Ahol:
     * type - Az észlelt objektum típusa (hajó).
     * id - A észlelt objektum azonosítója.
     * position - Helyzete.
     * x - X tengelyen lévő koordinátája.
     * y - Y tengelyen lévő koordinátája.
     * owner - Tulajdonos.
     * name - A tulajdonos csapat neve.
     * velocity - Mozgásának sebessége.
     * angle - Mozgásának iránya (fokban).
     * hp - A hajó aktuális életereje.
     * sonarCooldown - Az aktív szonár újra aktiválásig hátralévő körök számát jelzi.
     * torpedoCooldown - A torpedó újra használhatóságáig hátralévő körök számát jelzi.
     * sonarExtended - Az aktív szonár használatából hátralévő körök számát jelzi.
     *
     * @return SubmarineResponse {@link SubmarineResponse}
     */
    @GET("game/{gameId}/submarine")
    Call<SubmarineResponse> listSubmarines(@Path("gameId") Long gameId);

    /**
     * A sebesség és az elfordulás változásának mértékét megadva csökkenthetjük-növelhetjük
     * a megadott azonosítójú tengeralattjáró sebességét, és/vagy változtathatunk a tengeralattjáró mozgásának irányán.
     * Sebességet, szám érték megadásával növelhetünk-csökkenthetünk:
     * speed: 5.0 (5-el növeljük a sebességet, tehát, az aktuális sebességünkhöz viszonyítva, ennyivel fogunk gyorsabban
     * haladni a következő körtől)
     * Elfordulás mértékén, szám megadásával változtathatunk ahol a megadott szám, az elfordulás fokában értendő:
     * turn: -15.0 (a következő körben hajónk iránya, az aktuális irányunkhoz képest -15 (fokkal) változik).
     * Keleti irány a 0 fok, Északi 90, Nyugati 180, Déli 270.
     * A sebesség és az elfordulás maximális mértéke a Game info-ból érhető el, ahol a:
     * maxSteeringPerRound - A hajó egy körben ennyivel módosíthatja az irányát (pozitív vagy negatív irányba)
     * maxAccelerationPerRound - A hajó egy körben ennyivel módosíthatja a sebességét (pozitív vagy negatív irányba)
     *
     * @param gameId      Long
     * @param submarineId Long
     * @param moveRequest {@link MoveRequest}
     *                    <p>
     *                    ErrorCodes:
     *                    3 - Nem létező gameId
     *                    4 - Nincs a csapatnak jogosultsága a megadott tengeralattjárót kezelni
     *                    9 - A játék nincs folyamatban
     *                    10 - A megadott hajó már mozgott ebben a körben
     *                    11 - Túl nagy gyorsulás
     *                    12 - Túl nagy kanyarodás
     *                    </p>
     * @return SimpleResponse {@link SimpleResponse}
     */
    @POST("game/{gameId}/submarine/{submarineId}/move")
    Call<SimpleResponse> move(@Path("gameId") Long gameId, @Path("submarineId") Long submarineId, @Body MoveRequest moveRequest);

    /**
     * A megadott irány felé indít egy torpedót. A megadott irány fok-ban értendő, ahol a:
     * Keleti irány a 0 fok, Északi 90, Nyugati 180, Déli 270.
     * Irányt, szám érték megadásával állíthatunk:
     * angle: 90.0 (Körök végi kiértékeléskor, a megadott irányba torpedót indít)
     *
     * @param gameId       Long
     * @param submarineId  Long
     * @param shootRequest {@link ShootRequest}
     *                     <p>
     *                     ErrorCodes:
     *                     3 - Nem létező gameId
     *                     4 - Nincs a csapatnak jogosultsága a megadott tengeralattjárót kezelni
     *                     7 - A torpedó cooldownon van
     *                     </p>
     * @return SimpleResponse {@link SimpleResponse}
     */
    @POST("game/{gameId}/submarine/{submarineId}/shoot")
    Call<SimpleResponse> shoot(@Path("gameId") Long gameId, @Path("submarineId") Long submarineId, @Body ShootRequest shootRequest);

    /**
     * Lekéri a tengeralattjáró szonárja által észlelt objektumokat ahol:
     * type - Az észlelt objektum típusa (hajó,torpedó)
     * id - A észlelt objektum azonosítója
     * position - Helyzete
     * x - X tengelyen lévő koordinátája
     * y - Y tengelyen lévő koordinátája
     * owner - Tulajdonos
     * name - A tulajdonos csapat neve
     * velocity - Mozgásának sebessége
     * angle - Mozgásának iránya (fokban)
     * roundsMoved - Megtett körök száma (torpedóknál)
     *
     * @param gameId      Long
     * @param submarineId Long
     *                    <p>
     *                    ErrorCodes:
     *                    3 - Nem létező gameId
     *                    4 - Nincs a csapatnak jogosultsága a megadott tengeralattjárót kezelni
     *                    </p>
     * @return SonarResponse {@link SonarResponse}
     */
    @GET("game/{gameId}/submarine/{submarineId}/sonar")
    Call<SonarResponse> sonar(@Path("gameId") Long gameId, @Path("submarineId") Long submarineId);

    /**
     * Bekapcsolja a megadott azonosítójú tengeralattjáró aktív (nagyobb hatótávolságú) szonárját.
     * Ezt X körönként Y kör erejéig tehetjük meg.
     * Lásd: Ha X=20, Y=10: A szonárt újra aktiválni ugyanezen a hajón, legközelebb 20 kör
     * elteltével tudjuk, és a szonár hatótávolsága az aktív szonár hatótávolságáig bővül 10 kör erejéig.
     * Ezek mértéke a Game info {@link com.banktech.javachallenge.service.domain.game.GameInfoResponse}-ból érhető el, ahol az:
     * extendedSonarRange - Az aktív szonár hatósugarát jelzi.
     * extendedSonarRounds - Az aktív szonár ennyi körig marad bekapcsolva.
     * extendedSonarCooldown - Az aktív szonár újra aktiválásig hátralévő körök számát jelzi.
     *
     * @param gameId      Long
     * @param submarineId Long
     *                    <p>
     *                    ErrorCodes:
     *                    3 - Nem létező gameId
     *                    4 - Nincs a csapatnak jogosultsága a megadott tengeralattjárót kezelni
     *                    8 - Újratöltődés előtti hívás
     *                    </p>     *
     * @return SimpleResponse {@link SimpleResponse}
     */
    @POST("game/{gameId}/submarine/{submarineId}/sonar")
    Call<SonarResponse> extendSonar(@Path("gameId") Long gameId, @Path("submarineId") Long submarineId);


}
