package com.vandenbreemen.videogameusr.game.vidgame3

import androidx.compose.ui.graphics.Color
import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.LevelRenderingInteractor

class VideoGame3Controller(private val videoGame3Model: VideoGame3Model, private val runner: Runner): VideoGameController {

    private val model = videoGame3Model
    private val colorInteractor = CoreDependenciesHelper.getColorInteractor()
    private val levelRenderingInteractor = LevelRenderingInteractor(model.requirements, runner, model.background).also {
        it.moveCameraTo(15, 30)
    }

    override fun moveRight() {
        levelRenderingInteractor.moveCameraRight()
    }

    override fun moveLeft() {
        levelRenderingInteractor.moveCameraLeft()
    }

    override fun moveUp() {
        levelRenderingInteractor.moveCameraUp()
    }

    override fun moveDown() {
        levelRenderingInteractor.moveCameraDown()
    }

    override fun pressA() {
        TODO("Not yet implemented")
    }

    override fun pressB() {
        TODO("Not yet implemented")
    }

    override fun playTurn() {
        model.playTurn()
    }

    override fun drawFrame() {
        levelRenderingInteractor.drawCameraView(model.background)
        levelRenderingInteractor.drawCameraView(model.grassLand)
        levelRenderingInteractor.drawCameraView(model.foregroundCastle)

        levelRenderingInteractor.drawSinglePlayerCenter(getPlayerSpriteTiles())
    }

    private fun getPlayerSpriteTiles(): Array<Array<Int>> {
        //  2d array of sprite tiles
        return arrayOf(
            arrayOf(0, 1),
            arrayOf(4, 5),
            arrayOf(8, 9),
            arrayOf(12, 13)
        )
    }

    override fun getComposeColor(value: Byte): Color {
        return colorInteractor.getComposeColor(value)
    }
}