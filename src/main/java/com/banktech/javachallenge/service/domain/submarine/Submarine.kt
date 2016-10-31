package com.banktech.javachallenge.service.domain.submarine

import com.banktech.javachallenge.service.domain.Coordinate

/**
 *
 */
data class Submarine(val type: String,
                     val id: Int,
                     val position: Coordinate,
                     val owner: Owner,
                     val velocity: Int,
                     val angle: Double)