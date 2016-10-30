package com.banktech.javachallenge.service;

import com.banktech.javachallenge.service.domain.SubmarineResponse;
import retrofit2.Call;
import retrofit2.http.GET;
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
     * @return
     */
    @GET("/game/{gameId}/submarine")
    Call<List<SubmarineResponse>> listSubmarines(@Path("gameId") int gameId);

}
