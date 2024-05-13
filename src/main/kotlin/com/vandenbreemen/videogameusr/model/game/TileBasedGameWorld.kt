package com.vandenbreemen.videogameusr.model.game

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

/**
 * Model that contains all things pertinent to a given game.  Provides facilities for sprites, levels, etc
 */
class TileBasedGameWorld(val requirements: GameDataRequirements) {

    private val levels: MutableMap<String, LevelModel> = mutableMapOf()

    /**
     * Add a level to the game
     */
    fun addLevel(name: String, widthInTiles: Int, heightInTiles: Int): LevelModel {
        val level = LevelModel(requirements, widthInTiles, heightInTiles)
        levels[name] = level
        return level
    }

    fun getLevel(name: String): LevelModel {
        return levels[name] ?: throw IllegalArgumentException("No level found with name $name")
    }

    fun getLevelNames(): List<String> {
        return levels.keys.toList()
    }

}