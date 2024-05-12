package com.vandenbreemen.videogameusr.game.vidgame3

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld

class VideoGame3Model {

    val requirements = GameDataRequirements(200, 150, 8, 8, (8*8)*100)
    val tileBasedGameWorld = TileBasedGameWorld(requirements)

    init {
        game3SpriteSheet(requirements)
    }

    val levelModel = tileBasedGameWorld.addLevel(35, 40)

    fun setupLevel() {

        levelModel.setSpritesOnRow(0, listOf(
            2, 2, 2, 2, 2, 10, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 7, 6,  6, 6, 6, 6,  6, 6, 6, 6, 2, 2, 2, 2,
        ))
        levelModel.setSpritesOnRow(1, listOf(
            2, 2, 2, 2, 2, 10, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6,  7, 6, 6, 6,  6, 6, 6, 6, 2, 2, 2, 2,
        ))

        levelModel.setSpritesOnRow(1, listOf(
            2, 2, 2, 2, 2, 10, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 7, 6, 7, 6, 6,  6, 6, 6, 6,  6, 6, 6, 11, 2, 2, 2, 2,
        ))


        levelModel.setSpritesOnRow(28, listOf(
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 6, 6, 6, 6, 7, 6, 61,  6, 7, 6, 6, 7,  6, 6, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        )   )

        levelModel.setSpritesOnRow(29, listOf(
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 6, 6, 6, 6, 7, 6, 61, 6, 6, 7, 6, 6, 7,  6, 6, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        )   )

        levelModel.setSpritesOnRow(30, listOf(
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 6, 6, 6, 6, 7, 6, 6, 6, 6, 7, 6, 6, 7,  6, 6, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        )   )

        levelModel.setSpritesOnRow(39, listOf(
            2, 2, 2, 2, 2, 10, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 7, 6, 6, 7,  6, 6, 6, 6,  6, 6, 6, 6, 2, 2, 2, 2,
        ))


    }

    fun playTurn() {}


}