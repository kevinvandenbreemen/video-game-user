package com.vandenbreemen.videogameusr.game.videogame2

import com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.videogameusr.model.ScreenInteractor

class VideoGame2Model(
    private val screenWidth: Int, private val screenHeight: Int, private val spriteWidth: Int, private val spriteHeight: Int,
) {

    private var playerGoingRight = false
    private var movementIncrement = 1   //  TODO    Consider accelerating the player
    private var playerLocation = Pair((25).toInt(), (screenHeight * 0.5).toInt())

    private val playerAnimationLoopFrameCount = 3
    private var playerAnimationFrameIndex: Int = 0
    private var playerMoving = false
    private val movingLeftFrames = listOf(VideoGame2SpriteAddresses.RUNNING_LEFT_FRAME_1, VideoGame2SpriteAddresses.RUNNING_LEFT_FRAME_2, VideoGame2SpriteAddresses.RUNNING_LEFT_FRAME_3)
    private val movingRightFrames = listOf(VideoGame2SpriteAddresses.RUNNING_RIGHT_FRAME_1, VideoGame2SpriteAddresses.RUNNING_RIGHT_FRAME_2, VideoGame2SpriteAddresses.RUNNING_RIGHT_FRAME_3)

    private val screenInteractor = ScreenInteractor(screenWidth, screenHeight, spriteWidth, spriteHeight)

    private val ground = mutableListOf<Pair<Int, Int>>()


    init {
        setup()
    }

    fun setup() {
        //  Set up the ground
        //  Go sprite width incremenets across the width of the screen
        val yLevel = (screenHeight * 0.5).toInt() + spriteHeight

        val holeRange = 8 until 10
        var groundPlotCounter = 0

        for (x in 0 until screenWidth step spriteWidth) {
            groundPlotCounter++
            if(groundPlotCounter in holeRange) {
                klog("Skipping hole at $x, $yLevel")
                continue
            }
            klog("Add ground sprite at $x, $yLevel")
            ground.add(Pair(x, yLevel))
        }
    }

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

    fun getGroundSpriteLocations() : List<Pair<Int, Int>>{
        return ground.toList()
    }
}