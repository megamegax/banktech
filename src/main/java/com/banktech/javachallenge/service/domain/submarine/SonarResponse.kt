package com.banktech.javachallenge.service.domain.submarine

import com.banktech.javachallenge.service.domain.game.Coordinate

/**
 *
 */
data class SonarResponse(val entities: List<Submarine>, val message: String, val code: Int)


data class Submarine(val type: String,
                     val id: Int,
                     val position: Coordinate,
                     val owner: Owner,
                     val velocity: Int,
                     val angle: Double)


