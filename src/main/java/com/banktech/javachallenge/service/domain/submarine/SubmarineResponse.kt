package com.banktech.javachallenge.service.domain.submarine

import com.banktech.javachallenge.service.domain.Position

/**
 *
 */
data class SubmarineResponse(val type: String,
                             val id: Long,
                             val position: Position,
                             val owner: Owner,
                             val velocity: Int,
                             val angle: Double,
                             val hp: Int,
                             val sonarCooldown: Int,
                             val torpedoCooldown: Int,
                             val sonarExtended: Int)

fun SubmarineResponse.toSubmarine(): Submarine {
    return Submarine(type, id, position, owner, velocity, angle)
}