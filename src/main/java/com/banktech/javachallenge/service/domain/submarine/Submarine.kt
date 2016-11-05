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

data class OwnSubmarine(val type: String,
                        val id: Long,
                        val position: Position,
                        val owner: Owner,
                        val velocity: Int,
                        val angle: Double,
                        val hp: Int,
                        val sonarCooldown: Int,
                        val torpedoCooldown: Int,
                        val sonarExtended: Int)