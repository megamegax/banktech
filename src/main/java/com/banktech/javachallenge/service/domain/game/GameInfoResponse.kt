package com.banktech.javachallenge.service.domain.game

import com.banktech.javachallenge.service.domain.Position

/**
 *
 */
data class GameInfoResponse(val game: Game,
                            val message: String,
                            val code: Int)

data class MapConfiguration(val width: Int,
                            val height: Int,
                            val islandPositions: List<Position>,
                            val teamCount: Int,
                            val submarinesPerTeam: Int,
                            val torpedoDamage: Int,
                            val torpedoHitScore: Int,
                            val torpedoDestroyScore: Int,
                            val torpedoHitPenalty: Int,
                            val torpedoCooldown: Int,
                            val sonarRange: Int,
                            val extendedSonarRange: Int,
                            val extendedSonarRounds: Int,
                            val extendedSonarCooldown: Int,
                            val torpedoSpeed: Int,
                            val torpedoExplosionRadius: Int,
                            val roundLength: Int,
                            val islandSize: Int,
                            val submarineSize: Int,
                            val rounds: Int,
                            val maxSteeringPerRound: Int,
                            val maxAccelerationPerRound: Int,
                            val maxSpeed: Int,
                            val torpedoRange: Int,
                            val rateLimitedPenalty: Int)

data class Game(val id: Long,
                val round: Int,
                val scores: Scores,
                val connectionStatus: ConnectionStatus,
                val mapConfiguration: MapConfiguration,
                val status: Status)

data class Scores(val scores: Map<String, Int>)

data class ConnectionStatus(val connected: Map<String, Boolean>)


enum class Status{
    WAITING, RUNNING, ENDED
}