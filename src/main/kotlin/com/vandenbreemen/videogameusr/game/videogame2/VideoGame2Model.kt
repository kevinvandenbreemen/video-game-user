package com.vandenbreemen.com.vandenbreemen.videogameusr.game.videogame2

import com.vandenbreemen.com.vandenbreemen.videogameusr.model.ScreenInteractor

class VideoGame2Model(
    private val screenWidth: Int, private val screenHeight: Int, private val spriteWidth: Int, private val spriteHeight: Int,
) {

    private var playerGoingRight = false
    private var movementIncrement = 1   //  TODO    Consider accelerating the player
    private var playerLocation = Pair((screenWidth *0.5).toInt(), (screenHeight * 0.5).toInt())

    private val playerAnimationLoopFrameCount = 3
    private var playerAnimationFrameIndex: Int = 0
    private var playerMoving = false
    private val movingLeftFrames = listOf(VideoGame2SpriteAddresses.RUNNING_LEFT_FRAME_1, VideoGame2SpriteAddresses.RUNNING_LEFT_FRAME_2, VideoGame2SpriteAddresses.RUNNING_LEFT_FRAME_3)
    private val movingRightFrames = listOf(VideoGame2SpriteAddresses.RUNNING_RIGHT_FRAME_1, VideoGame2SpriteAddresses.RUNNING_RIGHT_FRAME_2, VideoGame2SpriteAddresses.RUNNING_RIGHT_FRAME_3)

    private val screenInteractor = ScreenInteractor(screenWidth, screenHeight, spriteWidth, spriteHeight)

    fun movePlayerRight() {

        if(playerGoingRight) {
            playerAnimationFrameIndex = (playerAnimationFrameIndex + 1) % playerAnimationLoopFrameCount
            playerMoving = true
        } else {
            playerAnimationFrameIndex = 0
            playerMoving = false
        }

        playerGoingRight = true
        val newPlayerLocation = Pair(playerLocation.first + movementIncrement, playerLocation.second)
        if(screenInteractor.isInBounds(newPlayerLocation)) {
            playerLocation = newPlayerLocation
        }

    }

    fun movePlayerLeft() {

        if(!playerGoingRight) {
            playerAnimationFrameIndex = (playerAnimationFrameIndex + 1) % playerAnimationLoopFrameCount
            playerMoving = true
        } else {
            playerAnimationFrameIndex = 0
            playerMoving = false
        }

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
        if(playerMoving) {
            return if (playerGoingRight) {
                movingRightFrames[playerAnimationFrameIndex]
            } else {
                movingLeftFrames[playerAnimationFrameIndex]
            }
        }
        return if(playerGoingRight) VideoGame2SpriteAddresses.STANDING_STILL_RIGHT else VideoGame2SpriteAddresses.STANDING_STILL_LEFT
    }


}