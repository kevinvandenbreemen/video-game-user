package com.vandenbreemen.com.vandenbreemen.videogameusr.game.vidgame1

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.com.vandenbreemen.videogameusr.model.DelayedSwitch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * State information about the game
 */
class VideoGame1Model(private val screenWidth: Int, private val screenHeight: Int, private val spriteWidth: Int, private val spriteHeight: Int, private val enemyHitPoints: Int = 3) {

    private val motionIncrement = 1

    private val enemyTurnDelayFractionOfSecond = 4  //  As this increases it makes the game harder!
    private val enemyTurnDelay: Long = 1000 / enemyTurnDelayFractionOfSecond.toLong()
    private var lastTurnTime: Long = System.currentTimeMillis()


    private var playerLocation: Pair<Int, Int> = Pair((screenWidth * 0.1).toInt(), screenHeight / 2)
    private var enemyLocation: Pair<Int, Int> = Pair((screenWidth * 0.9).toInt(), screenHeight / 2)

    private val gunFiringSwitch = DelayedSwitch(1000L)

    /**
     * Mapping from location to explosion frame
     */
    private val explosionLocations = ConcurrentHashMap<Pair<Int, Int>, Int>()
    fun getExplosions(): Map<Pair<Int, Int>, Int> {
        return explosionLocations.toMap()
    }

    /**
     *
     */
    private var enemyDamage = 0

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

        if(!gunFiringSwitch.trigger()) {
            return
        }

        klog("Firing weapon!")

        bulletsInFlight.add(Pair(playerLocation.first + spriteWidth, playerLocation.second + spriteHeight / 2))

    }


    private fun isHitboxIntersecting(location1: Pair<Int, Int>, location2: Pair<Int, Int>): Boolean {
        return location1.first < location2.first + spriteWidth && location1.first + spriteWidth > location2.first && location1.second < location2.second + spriteHeight && location1.second + spriteHeight > location2.second
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
        val newBullets = bulletsInFlight.toList()
        val finalBulletList = ArrayList<Pair<Int, Int>>()
        newBullets.forEach { bulletLocation ->

            Pair(bulletLocation.first + motionIncrement, bulletLocation.second).also {
                if(isInBounds(it)) {

                    //  Check if it hit the alien
                    if(isHitboxIntersecting(it, enemyLocation)) {
                        klog("Hit the alien!")
                        enemyDamage++
                    }
                    else {
                        finalBulletList.add(it)
                    }
                }
            }

        }
        bulletsInFlight.apply {
            clear()
            addAll(finalBulletList)
        }

        //  Now figure out if the alien has died
        if(enemyDamage >= enemyHitPoints) {
            klog("Alien has died!")
            explosionLocations[enemyLocation] = -1   //  Start a new explosion at this location
            enemyDamage = 0
            enemyLocation = Pair((screenWidth * 0.9).toInt(), screenHeight / 2)
        }

        //  Finally compute the explosion frames
        explosionLocations.forEach { (location, frame) ->
            if(frame < 3) {
                explosionLocations[location] = frame + 1
            }
            else {
                explosionLocations.remove(location)
            }
        }

    }

}