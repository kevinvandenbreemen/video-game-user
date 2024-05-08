package com.vandenbreemen.com.vandenbreemen.videogameusr.game.videogame2

import com.vandenbreemen.com.vandenbreemen.videogameusr.model.ScreenInteractor

class VideoGame2Model(
    private val screenWidth: Int, private val screenHeight: Int, private val spriteWidth: Int, private val spriteHeight: Int,
) {

    private var playerGoingRight = false
    private var movementIncrement = 1   //  TODO    Consider accelerating the player
    private var playerLocation = Pair((screenWidth *0.5).toInt(), (screenHeight * 0.5).toInt())

    private val screenInteractor = ScreenInteractor(screenWidth, screenHeight, spriteWidth, spriteHeight)

    fun movePlayerRight() {
        playerGoingRight = true
        val newPlayerLocation = Pair(playerLocation.first + movementIncrement, playerLocation.second)
        if(screenInteractor.isInBounds(newPlayerLocation)) {
            playerLocation = newPlayerLocation
        }

    }

    fun movePlayerLeft() {
        playerGoingRight = false
        val newPlayerLocation = Pair(playerLocation.first - movementIncrement, playerLocation.second)
        if(screenInteractor.isInBounds(newPlayerLocation)) {
            playerLocation = newPlayerLocation
        }
    }

    fun playGamesTurn() {
        //  TODO    Implement
    }

    /**
     *
     */
    fun jump() {
        //  TODO    Implement
    }

    fun getPlayerLocation(): Pair<Int, Int> {
        return playerLocation
    }

    fun getPlayerSpriteIndex(): Int {
        return if(playerGoingRight) VideoGame2SpriteAddresses.STANDING_STILL_RIGHT else VideoGame2SpriteAddresses.STANDING_STILL_LEFT
    }


}