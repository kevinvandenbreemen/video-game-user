package com.vandenbreemen.videogameusr.model

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.game.LevelModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class LevelModelTest {

    val requirements = GameDataRequirements(100, 100, 8, 8, 1024)

    @Test
    fun `should initialize all sprite tiles to the no sprite index`() {

            val levelModel = LevelModel(requirements, 1000, 100)

            for(x in 0 until 1000){
                for(y in 0 until 100){
                    assertEquals(LevelModel.NO_SPRITE, levelModel.getSpriteTileAt(x, y))
                }
            }
    }

    @Test
    fun `should register sprite index`() {

        val levelModel = LevelModel(requirements, 1000, 100)

        levelModel.setSpriteTileAt(10, 19, 5)
        assertEquals(5, levelModel.getSpriteTileAt(10, 19))


    }

    @Test
    fun `should prevent adding sprite of index out of bounds`() {

        val levelModel = LevelModel(requirements, 1000, 100)

        assertThrows(IllegalArgumentException::class.java) {
            levelModel.setSpriteTileAt(10, 19, 16)
        }
    }

    @Test
    fun `should prevent adding sprite of index out of bounds neg index`() {

        val levelModel = LevelModel(requirements, 1000, 100)

        assertThrows(IllegalArgumentException::class.java) {
            levelModel.setSpriteTileAt(10, 19, -2)
        }
    }

    @Test
    fun `should allow adding sprite index -1 to signify no sprite set at a tile`() {
        val levelModel = LevelModel(requirements, 1000, 100)
        levelModel.setSpriteTileAt(10, 19, -1)
    }

    @Test
    fun `should prevent accessing sprite tile from location out of bounds`() {
        val levelModel = LevelModel(requirements, 1000, 100)
        assertThrows(IllegalArgumentException::class.java) {
            levelModel.getSpriteTileAt(1000, 100)
        }
    }

}