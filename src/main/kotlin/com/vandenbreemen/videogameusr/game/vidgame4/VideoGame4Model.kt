package com.vandenbreemen.videogameusr.game.vidgame4

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.model.game.assetmgt.GameAssetsInteractor
import com.vandenbreemen.videogameusr.tools.gameEditor

class VideoGame4Model {

    //  100 possible sprite tiles
    //  SNES style
    private val requirements = GameDataRequirements(256, 224, 8, 8, 6400 )

    private val world = TileBasedGameWorld(requirements)

    /**
     * Triggers the game editor for the game.  Use only for development purposes
     */
    fun edit() {
        gameEditor(requirements, world, 0, "requirements")
    }

    fun load() {
        GameAssetsInteractor().loadAssetsFromClasspath("/assets/games/game4.dat", requirements, world)
    }

}