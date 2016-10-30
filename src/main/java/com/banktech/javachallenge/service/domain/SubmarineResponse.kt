package com.banktech.javachallenge.service.domain

/**
 *
 */
data class SubmarineResponse(val type: String,
                             val id: Int,
                             val position: Position,
                             val owner: Owner,
                             val velocity:Int,
                             val angle:Int,
                             val hp:Int,
                             val sonarCooldown:Int,
                             val torpedoCooldown:Int,
                             val sonarExtended:Int)


data class Position(var x: Int, var y: Int)

data class Owner(val name: String)