package com.banktech.javachallenge.service.domain.submarine

import com.banktech.javachallenge.service.domain.Position

/**
 *
 */
data class Submarine(val type: String,
                     val id: Long,
                     var position: Position,
                     val owner: Owner,
                     val velocity: Int,
                     val angle: Double)