package com.vandenbreemen.videogameusr.game.wgame

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.tools.gameEditor

class ExampleWiderGameModel {

    //  SNES style
    private val requirements = GameDataRequirements(256, 224, 8, 8, 100*64 )

    private val world = TileBasedGameWorld(requirements)

    /**
     * Developer tools
     */
    fun edit() {
        gameEditor(requirements, world, 0, "requirements")
    }

}