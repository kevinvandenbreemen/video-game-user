package com.vandenbreemen.videogameusr.game.wgame.data

import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.model.game.assetmgt.GameAssetsInteractor
import com.vandenbreemen.videogameusr.tools.gameEditor
import com.vandenbreemen.videogameusr.view.render.LevelRenderingInteractor

class Cave() {

    //  TODO    Create some kind of factory that makes these with the same dimensions and stuff
    val requirements = CoreDependenciesHelper.createSNESRequirements(100)

    private val caveWorld = TileBasedGameWorld(requirements)

    val widthInTiles: Int get() = caveWorld.getLevel(caveWorld.getLevelNames()[0]).widthInTiles
    val heightInTiles: Int get() = caveWorld.getLevel(caveWorld.getLevelNames()[0]).heightInTiles

    fun load() {
        GameAssetsInteractor().loadAssetsFromClasspath("/assets/games/wider/cave.dat", requirements, caveWorld)
    }

    fun edit() {
        gameEditor(requirements, caveWorld, 0, "requirements")
    }

    /**
     * Render this place given the rendering interactor
     */
    fun render(renderingInteractor: LevelRenderingInteractor) {
        val level = caveWorld.getLevel("Cave")
        renderingInteractor.drawCameraView(level)
    }

}

fun main() {
    val cave = Cave()
    cave.load()
    cave.edit()
}