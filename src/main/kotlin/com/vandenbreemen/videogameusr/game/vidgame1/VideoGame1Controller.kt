package com.vandenbreemen.com.vandenbreemen.videogameusr.game.vidgame1

import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner

class VideoGame1Controller(private val runner: Runner, requirements: GameDataRequirements) : VideoGameController {

    private val model = VideoGame1Model(requirements.screenWidth, requirements.screenHeight, requirements.spriteWidth, requirements.spriteHeight)

    override fun drawFrame() {

        runner.drawSpriteAt(0, model.getPlayerLocation().first, model.getPlayerLocation().second)
        runner.drawSpriteAt(2, model.getEnemyLocation().first, model.getEnemyLocation().second)

    }

    override fun moveRight() {
        model.movePlayerRight()
    }

    override fun moveLeft() {
        model.movePlayerLeft()
    }

    override fun moveUp() {
        model.movePlayerUp()
    }


    override fun moveDown() {
        model.movePlayerDown()
    }

    override fun pressA() {
        //  For now do nothing
    }

    override fun pressB() {
        //  For now do nothing
    }

    /**
     * Have the game AI etc play its turn
     */
    override fun playTurn() {
        model.playGamesTurn()
    }

}