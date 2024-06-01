package com.vandenbreemen.videogameusr.game.wgame.data

import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.model.game.assetmgt.GameAssetsInteractor
import com.vandenbreemen.videogameusr.tools.gameEditor

class Cave() {

    //  TODO    Create some kind of factory that makes these with the same dimensions and stuff
    private val requirements = CoreDependenciesHelper.createSNESRequirements(100)

    val caveWorld = TileBasedGameWorld(requirements)

    fun load() {
        GameAssetsInteractor().loadAssetsFromClasspath("/assets/games/wider/cave.dat", requirements, caveWorld)
    }

    fun edit() {
        gameEditor(requirements, caveWorld, 0, "requirements")
    }

}

fun main() {
    val cave = Cave()
    cave.load()
    cave.edit()
}