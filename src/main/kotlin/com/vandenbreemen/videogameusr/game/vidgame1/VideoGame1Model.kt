package com.vandenbreemen.com.vandenbreemen.videogameusr.game.vidgame1

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * State information about the game
 */
class VideoGame1Model(private val screenWidth: Int, private val screenHeight: Int, private val spriteWidth: Int, private val spriteHeight: Int) {

    private val motionIncrement = 1

    private val enemyTurnDelayFractionOfSecond = 2  //  As this increases it makes the game harder!
    private val enemyTurnDelay: Long = 1000 / enemyTurnDelayFractionOfSecond.toLong()
    private var lastTurnTime: Long = System.currentTimeMillis()


    private var playerLocation: Pair<Int, Int> = Pair((screenWidth * 0.1).toInt(), screenHeight / 2)
    private var enemyLocation: Pair<Int, Int> = Pair((screenWidth * 0.9).toInt(), screenHeight / 2)

    /**
     * List of positions of bullets in flight
     */
    //  Concurrent mutable list of pairs of ints
    private val bulletsInFlight = ConcurrentLinkedQueue<Pair<Int, Int>>()
    fun getBulletsInFlight(): List<Pair<Int, Int>> {
        return bulletsInFlight.toList()
    }

    init {

        klog("screenWidth=$screenWidth, screenHeight=$screenHeight")
    }

    private fun isInBounds(location: Pair<Int, Int>): Boolean {

        //  Since locations will always have origin at top left
        return location.first in 0..<screenWidth-spriteWidth && location.second in 0..<screenHeight-spriteHeight
    }

    fun getPlayerLocation(): Pair<Int, Int> {
        return playerLocation
    }

    fun getEnemyLocation(): Pair<Int, Int> {
        return enemyLocation
    }

    fun movePlayerRight() {
        Pair(playerLocation.first + motionIncrement, playerLocation.second).also {
            if(isInBounds(it)) {
                playerLocation = it
            }
        }
    }

    fun movePlayerLeft() {
        Pair(playerLocation.first - motionIncrement, playerLocation.second).also {
            if(isInBounds(it)) {
                playerLocation = it
            }
        }
    }

    fun movePlayerUp() {
        Pair(playerLocation.first, playerLocation.second - motionIncrement).also {
            if(isInBounds(it)) {
                playerLocation = it
            }
        }
    }

    fun movePlayerDown() {
        Pair(playerLocation.first, playerLocation.second + motionIncrement).also {
            if(isInBounds(it)) {
                playerLocation = it
            }
        }
    }

    /**
     * Shoot your weapon
     */
    fun fireWeapon() {

        klog("Fire Weapon")
        bulletsInFlight.add(Pair(playerLocation.first + spriteWidth, playerLocation.second + spriteHeight / 2))

    }

    /**
     * Once the player has moved, the game will play its turn
     */
    fun playGamesTurn() {

        val currentTime = System.currentTimeMillis()
        if(currentTime - lastTurnTime >= enemyTurnDelay) {
            //  Move the enemy toward the player
            enemyLocation = Pair(
                if (enemyLocation.first > playerLocation.first) enemyLocation.first - motionIncrement else enemyLocation.first + motionIncrement,
                if (enemyLocation.second > playerLocation.second) enemyLocation.second - motionIncrement else enemyLocation.second + motionIncrement
            )
            lastTurnTime = currentTime
        }

        //  Move the bullets
        val newBullets = ArrayList(bulletsInFlight)
        val finalBulletList = ArrayList<Pair<Int, Int>>()
        newBullets.forEach { bulletLocation ->

            Pair(bulletLocation.first + motionIncrement, bulletLocation.second).also {
                if(isInBounds(it)) {
                    finalBulletList.add(it)
                }
            }

        }
        bulletsInFlight.apply {
            clear()
            addAll(finalBulletList)
        }

    }

}