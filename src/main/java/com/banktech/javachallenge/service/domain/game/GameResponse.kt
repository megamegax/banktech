package com.banktech.javachallenge.service.domain.game

import java.util.*

/**
 *
 */
data class GameResponse(val games: List<Long> = ArrayList(), val message: String, val code: Int)