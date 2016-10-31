package com.banktech.javachallenge.service.domain.submarine

import com.banktech.javachallenge.service.domain.game.Coordinate

/**
 *
 */
data class SubmarineResponse(val type: String,
                             val id: Int,
                             val position: Coordinate,
                             val owner: Owner,
                             val velocity: Int,
                             val angle: Double,
                             val hp: Int,
                             val sonarCooldown: Int,
                             val torpedoCooldown: Int,
                             val sonarExtended: Int)


data class Owner(val name: String)