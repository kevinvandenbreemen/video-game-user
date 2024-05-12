package com.vandenbreemen.videogameusr.model.game

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

/**
 * Model that contains all things pertinent to a given game.  Provides facilities for sprites, levels, etc
 */
class TileBasedGameWorld(val requirements: GameDataRequirements) {

    private val levels: MutableList<LevelModel> = mutableListOf()

    /**
     * Add a level to the game
     */
    fun addLevel(widthInTiles: Int, heightInTiles: Int): LevelModel {
        val level = LevelModel(requirements, widthInTiles, heightInTiles)
        levels.add(level)
        return level
    }

    fun getLevel(index: Int): LevelModel {
        return levels[index]
    }

}