package com.vandenbreemen.videogameusr.game.vidgame3

import androidx.compose.ui.graphics.Color
import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.view.render.LevelRenderingInteractor
import com.vandenbreemen.videogameusr.view.render.RunnerView

class VideoGame3Controller(private val videoGame3Model: VideoGame3Model, private val runner: RunnerView): VideoGameController {

    private val model = videoGame3Model
    private val colorInteractor = CoreDependenciesHelper.getColorInteractor()
    private val levelRenderingInteractor = LevelRenderingInteractor(model.requirements, runner, model.background).also {
        it.moveCameraTo(15, 30)
    }

    override fun moveRight() {
        model.moveRight()
        levelRenderingInteractor.moveCameraRight()
    }

    override fun moveLeft() {
        levelRenderingInteractor.moveCameraLeft()
        model.moveLeft()
    }

    override fun moveUp() {
        levelRenderingInteractor.moveCameraUp()
        model.moveUp()
    }

    override fun moveDown() {
        levelRenderingInteractor.moveCameraDown()
        model.moveDown()
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

    override fun drawFrame(): DisplayRaster {
        levelRenderingInteractor.drawCameraView(model.background)
        levelRenderingInteractor.drawCameraView(model.grassLand)
        levelRenderingInteractor.drawCameraView(model.foregroundCastle)

        levelRenderingInteractor.drawSinglePlayerCenter(model.getPlayerSpriteTiles())

        return runner.newFrame()
    }

    override fun getComposeColor(value: Byte): Color {
        return colorInteractor.getComposeColor(value)
    }
}