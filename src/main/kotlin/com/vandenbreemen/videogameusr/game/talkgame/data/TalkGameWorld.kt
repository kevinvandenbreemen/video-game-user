package com.vandenbreemen.videogameusr.game.talkgame.data

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.tools.gameEditor

/**
 * This is the static data for places in the talk game
 */
class TalkGameWorld {

    private lateinit var tileBasedGameWorld: TileBasedGameWorld

    fun load(requirements: GameDataRequirements) {
        tileBasedGameWorld = TileBasedGameWorld(requirements)
        //GameAssetsInteractor().loadAssetsFromClasspath("assets/games/talkgame/world.txt", requirements, tileBasedGameWorld)
    }

    fun edit(requirements: GameDataRequirements) {
        gameEditor(requirements, tileBasedGameWorld, 0, "requirements")
    }

}