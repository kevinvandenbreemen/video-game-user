package com.vandenbreemen.videogameusr.game.wgame

import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.game.wgame.data.Cave
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.view.render.LevelRenderingInteractor
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
    private lateinit var levelRenderingInteractor: LevelRenderingInteractor

    fun init() {
        cave.load()
        runnerView = RunnerView(cave.requirements)  //  TODO    Is there a way to make the Cave object handle making this?  Same for the rendering interactor below.....
        levelRenderingInteractor = LevelRenderingInteractor(cave.requirements, runnerView, cave.widthInTiles, cave.heightInTiles)

        levelRenderingInteractor.moveCameraTo(20, 20)
    }

    fun takeTurn() {
        //  Do something
    }

    fun drawFrame(): DisplayRaster {
        cave.render(levelRenderingInteractor)
        return runnerView.newFrame()
    }

    fun moveRight() {
        levelRenderingInteractor.moveCameraRight()
    }

    fun moveLeft() {
        levelRenderingInteractor.moveCameraLeft()
    }

    fun moveUp() {
        levelRenderingInteractor.moveCameraUp()
    }

    fun moveDown() {
        levelRenderingInteractor.moveCameraDown()
    }
}