package com.vandenbreemen.videogameusr.model

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

/**
 * Description of a "level" in the game
 */
class LevelModel(private val requirements: GameDataRequirements, val widthInTiles: Int, val heightInTiles: Int) {

    //  2-d array of sprite indexes
    private val levelData = Array(widthInTiles) { IntArray(heightInTiles) }

    /**
     * Specify sprite to go at the given tile
     */
    fun setSpriteTileAt(x: Int, y: Int, spriteIndex: Int){
        //  Verify the sprite index is good
        val maxSprites = requirements.maxBytes / (requirements.spriteWidth * requirements.spriteHeight)
        if(spriteIndex !in 0 until maxSprites){
            throw IllegalArgumentException("Invalid sprite index $spriteIndex, max sprites=$maxSprites")
        }

        levelData[x][y] = spriteIndex
    }

    /**
     * Get the sprite index at the given tile
     */
    fun getSpriteTileAt(x: Int, y: Int): Int {
        // Verify the location is in bounds
        if(x !in 0 until widthInTiles || y !in 0 until heightInTiles){
            throw IllegalArgumentException("Invalid location $x, $y, max x=$widthInTiles, max y=$heightInTiles")
        }

        return levelData[x][y]
    }

}