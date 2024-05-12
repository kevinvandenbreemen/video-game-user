package com.vandenbreemen.videogameusr.game.vidgame3

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.game.LevelModel

class VideoGame3Model {

    val requirements = GameDataRequirements(200, 150, 8, 8, (8*8)*100)

    init {
        game3SpriteSheet(requirements)
    }

    val levelModel = LevelModel(requirements, 35, 40)

    fun setupLevel() {

        levelModel.setSpritesOnRow(0, listOf(
            2, 2, 2, 2, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6, 2, 2, 2, 2,
        ))
        levelModel.setSpritesOnRow(1, listOf(
            2, 2, 2, 2, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6, 2, 2, 2, 2,
        ))

    }

    fun playTurn() {}


}