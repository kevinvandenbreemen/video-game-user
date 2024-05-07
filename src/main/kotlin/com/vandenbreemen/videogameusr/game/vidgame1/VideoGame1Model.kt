package com.vandenbreemen.com.vandenbreemen.videogameusr.game.vidgame1

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog

/**
 * State information about the game
 */
class VideoGame1Model(screenWidth: Int, screenHeight: Int) {

    private val motionIncrement = 1

    private var playerLocation: Pair<Int, Int> = Pair((screenWidth * 0.1).toInt(), screenHeight / 2)
    private var enemyLocation: Pair<Int, Int> = Pair((screenWidth * 0.9).toInt(), screenHeight / 2)

    init {

        klog("screenWidth=$screenWidth, screenHeight=$screenHeight")
    }

    fun getPlayerLocation(): Pair<Int, Int> {
        return playerLocation
    }

    fun getEnemyLocation(): Pair<Int, Int> {
        return enemyLocation
    }

    fun movePlayerRight() {
        playerLocation = Pair(playerLocation.first + motionIncrement, playerLocation.second)
    }

    fun movePlayerLeft() {
        playerLocation = Pair(playerLocation.first - motionIncrement, playerLocation.second)
    }

    fun movePlayerUp() {
        playerLocation = Pair(playerLocation.first, playerLocation.second - motionIncrement)
    }

    fun movePlayerDown() {
        playerLocation = Pair(playerLocation.first, playerLocation.second + motionIncrement)
    }

    /**
     * Once the player has moved, the game will play its turn
     */
    fun playGamesTurn() {

        klog("Taking players turn - enemy location is $enemyLocation, player location is $playerLocation")

        //  Move the enemy toward the player
        enemyLocation = Pair(
            if (enemyLocation.first > playerLocation.first) enemyLocation.first - motionIncrement else enemyLocation.first + motionIncrement,
            if (enemyLocation.second > playerLocation.second) enemyLocation.second - motionIncrement else enemyLocation.second + motionIncrement
        )
        klog("Updated enemy location to $enemyLocation")

    }

}