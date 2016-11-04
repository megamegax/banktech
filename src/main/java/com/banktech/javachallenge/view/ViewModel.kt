package com.banktech.javachallenge.view

import com.banktech.javachallenge.service.domain.game.Game

data class ViewModel(var game: Game, var calls: List<ApiCall>)