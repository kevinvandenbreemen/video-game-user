package com.vandenbreemen.com.vandenbreemen.videogameusr.game.videogame2

import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.game.Runner

class VideoGame2Controller(private val model: VideoGame2Model, private val runner: Runner): VideoGameController {

    override fun moveRight() {
        model.movePlayerRight()
    }

    override fun moveLeft() {
        model.movePlayerLeft()
    }

    override fun moveUp() {
        TODO("Not yet implemented")
    }

    override fun moveDown() {
        TODO("Not yet implemented")
    }

    override fun pressA() {
        model.jump()
    }

    override fun pressB() {
        TODO("Not yet implemented")
    }

    override fun playTurn() {
        model.playGamesTurn()
    }

    override fun drawFrame(): DisplayRaster {
        val playerLocation = model.getPlayerLocation()
        val spriteIndex = model.getPlayerSpriteIndex()
        runner.drawSpriteAt(spriteIndex, playerLocation.first, playerLocation.second)

        model.getGroundSpriteLocations().forEach { (x, y) ->
            runner.drawSpriteAt(VideoGame2SpriteAddresses.GROUND_SPRITE_1, x, y)
        }

        return runner.newFrame()
    }
}