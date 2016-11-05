package com.banktech.javachallenge.view

import com.banktech.javachallenge.service.domain.game.Game
import com.banktech.javachallenge.service.domain.submarine.Submarine
import com.banktech.javachallenge.service.domain.submarine.SubmarineResponse
import com.banktech.javachallenge.world.World

data class ViewModel(var game: Game, var calls: List<ApiCall>, var ownSubmarines: List<SubmarineResponse>, var detectedSubmarines: List<Submarine>, var worldMap: World)