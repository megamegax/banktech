package com.banktech.javachallenge.view

import com.banktech.javachallenge.service.domain.game.Game
import com.banktech.javachallenge.service.domain.submarine.Submarine
import com.banktech.javachallenge.service.domain.submarine.SubmarineResponse
import com.banktech.javachallenge.world.World
import java.util.*

data class ViewModel(var game: Game? = null,
                     var calls: MutableList<ApiCall> = ArrayList(),
                     var ownSubmarines: MutableList<SubmarineResponse> = ArrayList(),
                     var detectedSubmarines: MutableList<Submarine>? = ArrayList(),
                     var worldMap: World? = null)