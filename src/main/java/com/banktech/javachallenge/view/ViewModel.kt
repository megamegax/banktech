package com.banktech.javachallenge.view

import com.banktech.javachallenge.service.domain.game.Game
import com.banktech.javachallenge.service.domain.submarine.Submarine
import com.banktech.javachallenge.service.domain.submarine.SubmarineResponse

data class ViewModel(var game: Game, var calls: List<ApiCall>, var ownSubmarines: List<SubmarineResponse>, var detectedSubmarines: List<Submarine>)