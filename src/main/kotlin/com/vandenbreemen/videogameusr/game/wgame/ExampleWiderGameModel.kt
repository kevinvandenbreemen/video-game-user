package com.vandenbreemen.videogameusr.game.wgame

import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.game.wgame.data.Cave
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.view.render.RunnerView

class ExampleWiderGameModel {

    //  SNES style
    //  TODO    Right now this isn't being used but it seems like it'd be used for the "overworld" outside of the cave maybe?
    private val _laterToBeUsedRequirements = GameDataRequirements(256, 224, 8, 8, 100*64 )
    private val _laterToBeUsedWorld = TileBasedGameWorld(_laterToBeUsedRequirements)

    private val cave = Cave()

    /**
     * This will change dependeing on where the player is
     */
    private lateinit var runnerView: RunnerView

    fun init() {
        cave.load()
        runnerView = RunnerView(cave.requirements)  //  TODO    Is there a way to make the Cave object handle making this?  Same for the rendering interactor below.....


    }

    fun takeTurn() {
        //  Do something
    }

    fun drawFrame(): DisplayRaster {
        return cave.render()
    }

    fun moveRight() {
        cave.levelPrerenderInteractor.moveCameraRight()
    }

    fun moveLeft() {
        cave.levelPrerenderInteractor.moveCameraLeft()
    }

    fun moveUp() {
        cave.levelPrerenderInteractor.moveCameraUp()
    }

    fun moveDown() {
        cave.levelPrerenderInteractor.moveCameraDown()
    }
}