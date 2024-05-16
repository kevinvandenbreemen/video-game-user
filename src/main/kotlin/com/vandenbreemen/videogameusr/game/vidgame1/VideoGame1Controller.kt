package com.vandenbreemen.com.vandenbreemen.videogameusr.game.vidgame1

import androidx.compose.ui.graphics.Color
import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.com.vandenbreemen.videogameusr.model.ColorInteractor
import com.vandenbreemen.viddisplayrast.data.ByteColorDataInteractor
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner

class VideoGame1Controller(private val runner: Runner, requirements: GameDataRequirements, private val colorInteractor: ColorInteractor = ColorInteractor(
    ByteColorDataInteractor()
)) : VideoGameController {

    private val model = VideoGame1Model(requirements.screenWidth, requirements.screenHeight, requirements.spriteWidth, requirements.spriteHeight)

    override fun drawFrame() {

        runner.drawSpriteAt(0, model.getPlayerLocation().first, model.getPlayerLocation().second)
        runner.drawSpriteAt(2, model.getEnemyLocation().first, model.getEnemyLocation().second)
        model.getBulletsInFlight().forEach {
            runner.drawSpriteAt(1, it.first, it.second)
        }

        model.getExplosions().forEach {
            val spriteIndex = it.value + 4
            runner.drawSpriteAt(spriteIndex, it.key.first, it.key.second)
        }

    }

    override fun getFrameForDisplay(): DisplayRaster {
        return runner.newFrame()
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
        model.fireWeapon()
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

    override fun getComposeColor(value: Byte): Color {
        return colorInteractor.getComposeColor(value)
    }
}