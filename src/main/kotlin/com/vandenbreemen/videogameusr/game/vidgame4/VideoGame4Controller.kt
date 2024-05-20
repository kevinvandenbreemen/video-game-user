package com.vandenbreemen.videogameusr.game.vidgame4

import androidx.compose.ui.graphics.Color
import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.view.render.LevelRenderingInteractor
import com.vandenbreemen.videogameusr.view.render.RunnerView

class VideoGame4Controller(private val model: VideoGame4Model,  private val runner: RunnerView): VideoGameController {

    private val colorInteractor = CoreDependenciesHelper.getColorInteractor()
    private val renderingInteractor = LevelRenderingInteractor(model.requirements, runner, model.level).also {
        //  Set the initial location in the game
        it.moveCameraTo(20, 20)
    }

    override fun moveRight() {
        renderingInteractor.moveCameraRight()
    }

    override fun moveLeft() {
        renderingInteractor.moveCameraLeft()
    }

    override fun moveUp() {
        renderingInteractor.moveCameraUp()
    }

    override fun moveDown() {
        renderingInteractor.moveCameraDown()
    }

    override fun pressA() {

    }

    override fun pressB() {

    }

    override fun playTurn() {
        //  1:  Need to animate the glowing tile!
    }

    override fun drawFrame(): DisplayRaster {
        model.allLevelsInRenderingOrder.forEach {
            renderingInteractor.drawCameraView(it)
        }

        return runner.newFrame()
    }

    override fun getComposeColor(value: Byte): Color {
        return colorInteractor.getComposeColor(value)
    }
}