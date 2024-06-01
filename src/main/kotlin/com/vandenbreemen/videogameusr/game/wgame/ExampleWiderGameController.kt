package com.vandenbreemen.videogameusr.game.wgame

import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.videogameusr.controller.VideoGameController

class ExampleWiderGameController(private val model: ExampleWiderGameModel): VideoGameController {

    init {
        model.init()
    }

    override fun moveRight() {
        model.moveRight()
    }

    override fun moveLeft() {
        model.moveLeft()
    }

    override fun moveUp() {
        model.moveUp()
    }

    override fun moveDown() {
        model.moveDown()
    }

    override fun pressA() {
        
    }

    override fun pressB() {
        
    }

    override fun playTurn() {
        model.takeTurn()
    }

    override fun drawFrame(): DisplayRaster {
        return model.drawFrame()
    }
}