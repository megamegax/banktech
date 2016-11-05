package com.banktech.javachallenge.view

import com.banktech.javachallenge.service.domain.game.Game
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine
import com.banktech.javachallenge.service.domain.submarine.Submarine
import com.banktech.javachallenge.world.World
import java.util.*

data class ViewModel(var game: Game? = null,
                     var calls: MutableList<ApiCall> = ArrayList(),
                     var ownSubmarines: MutableList<OwnSubmarine> = ArrayList(),
                     var detectedSubmarines: MutableList<Submarine>? = ArrayList(),
                     var worldMap: World? = null) {
    fun cloneToNewTurn(): ViewModel {
        return ViewModel(game, ArrayList(), ownSubmarines, detectedSubmarines, worldMap)
    }
}