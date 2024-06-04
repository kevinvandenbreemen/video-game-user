package com.vandenbreemen.videogameusr.game.talkgame

import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.videogameusr.view.render.LevelRenderingInteractor
import com.vandenbreemen.videogameusr.view.render.RunnerView

class TalkGameController(requirements: GameDataRequirements, private val model: TalkGameModel): VideoGameController {

    private val runnerView = RunnerView(requirements)
    private val renderingInteractor = LevelRenderingInteractor(requirements, runnerView, 30, 30).also {
        it.moveCameraTo(15, 15)
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

    }

    override fun drawFrame(): DisplayRaster {
        model.getLevelsForRender().forEach {
            renderingInteractor.drawCameraView(it)
        }

        return runnerView.newFrame()
    }
}