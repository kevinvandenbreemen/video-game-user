package com.vandenbreemen.videogameusr.model.game

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class TileBasedGameWorldTest {

    val requirements = GameDataRequirements(
        100, 100, 10, 10, 222
    )

    @Test
    fun `should prevent adding level with same name twice`() {
        val world = TileBasedGameWorld(requirements)
        world.addLevel("level1", 10, 10)

        assertThrows(IllegalArgumentException::class.java) {
            world.addLevel("level1", 10, 10)
        }
    }

    @Test
    fun `should prevent adding levels with blank name`() {
        val world = TileBasedGameWorld(requirements)

        assertThrows(IllegalArgumentException::class.java) {
            world.addLevel("", 10, 10)
        }

        assertThrows(IllegalArgumentException::class.java) {
            world.addLevel("        ", 10, 10)
        }
    }

    @Test
    fun `should prevent adding levels with invalid width or height`() {
        val world = TileBasedGameWorld(requirements)

        assertThrows(IllegalArgumentException::class.java) {
            world.addLevel("level1", 0, 10)
        }
        assertThrows(IllegalArgumentException::class.java) {
            world.addLevel("level1", 10, 0)
        }
        assertThrows(IllegalArgumentException::class.java) {
            world.addLevel("level1", -1, 10)
        }
        assertThrows(IllegalArgumentException::class.java) {
            world.addLevel("level1", 10, -1)
        }
    }

}