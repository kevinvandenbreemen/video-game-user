package com.vandenbreemen.videogameusr.model.game

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

/**
 * Model that contains all things pertinent to a given game.  Provides facilities for sprites, levels, etc
 */
class GameDataModel(private val requirements: GameDataRequirements) {

    private val levels: MutableList<LevelModel> = mutableListOf()

}