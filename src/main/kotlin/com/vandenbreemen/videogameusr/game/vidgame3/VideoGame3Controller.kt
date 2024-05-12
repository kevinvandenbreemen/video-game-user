package com.vandenbreemen.videogameusr.game.vidgame3

import androidx.compose.ui.graphics.Color
import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.LevelRenderingInteractor

class VideoGame3Controller(private val videoGame3Model: VideoGame3Model, runner: Runner): VideoGameController {

    private val model = videoGame3Model
    private val colorInteractor = CoreDependenciesHelper.getColorInteractor()
    private val levelRenderingInteractor = LevelRenderingInteractor(model.requirements, runner, model.levelModel)

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
        levelRenderingInteractor.drawCameraView()
    }

    override fun getComposeColor(value: Byte): Color {
        return colorInteractor.getComposeColor(value)
    }
}