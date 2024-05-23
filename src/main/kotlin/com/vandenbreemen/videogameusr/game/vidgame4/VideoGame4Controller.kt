package com.vandenbreemen.videogameusr.game.vidgame4

import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.videogameusr.view.render.LevelRenderingInteractor
import com.vandenbreemen.videogameusr.view.render.RunnerView

class VideoGame4Controller(private val model: VideoGame4Model,  private val runner: RunnerView): VideoGameController {

    init {
        runner.setCameraIncrement(model.cameraIncrementInGame)
    }

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
        model.takeTurn()
    }

    override fun drawFrame(): DisplayRaster {
        model.allLevelsInRenderingOrder.forEach {
            renderingInteractor.drawCameraView(it)
        }

        return runner.newFrame()
    }
}