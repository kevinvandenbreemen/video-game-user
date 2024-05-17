package com.vandenbreemen.videogameusr.model.game.assetmgt

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.KLogConfig
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klogConfig
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.viddisplayrast.view.TextRender
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class GameAssetsInteractorTest {

    val testOutDir = "testOutput"

    private val gameDataRequirements = GameDataRequirements(24, 16, 8, 8, 6400)
    private val world = TileBasedGameWorld(gameDataRequirements)

    @BeforeEach
    fun setup() {
        klogConfig(KLogConfig(level = KlogLevel.DEBUG, logImmediate = true))
        File(testOutDir).mkdir()
    }

    @Test
    fun tearDown() {

    }

    @Test
    fun `should load sprites from file`() {

        val interactor = GameAssetsInteractor()
        interactor.loadAssetsFromClasspath("/assets/testgame.dat", gameDataRequirements, world)

        val runner = Runner(gameDataRequirements)
        runner.drawSpriteAt(0, 0, 0)

        val raster = runner.newFrame()

        val textDebug = TextRender()
        println(textDebug.renderRaster(raster))

        assertEquals(48, raster.getPixel(0, 0))
        assertEquals(-107, raster.getPixel(3, 0))
        assertEquals(-16, raster.getPixel(1,1))

    }

    @Test
    fun `should write sprites to file`() {

        val expectedSpriteData = byteArrayOf(
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, -14, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
        )

        gameDataRequirements.setData(0, expectedSpriteData)

        val interactor = GameAssetsInteractor()
        interactor.writeAssetsToFile("$testOutDir/testgame.dat", gameDataRequirements, world)

        val readInRequirements = GameDataRequirements(24, 16, 8, 8, 6400)
        interactor.loadAssetsFromFile("$testOutDir/testgame.dat", readInRequirements, world)

        val spriteData = readInRequirements.getSpriteData(0)
        assertArrayEquals(expectedSpriteData, spriteData)
    }

    @Test
    fun `should write level to file`() {
        val expectedSpriteData = byteArrayOf(
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, -14, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
        )

        val expectedSpriteData1 = byteArrayOf(
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, -14, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
        )

        gameDataRequirements.setData(0, expectedSpriteData)
        gameDataRequirements.setData(1, expectedSpriteData1)

        //  Now set up a level with a few of the sprites
        world.addLevel("Level 1", 100, 100).apply {
            setSpriteTileAt(0, 0, 0)
            setSpriteTileAt(1, 1, 1)
            setSpriteTileAt(1, 0, 1)
        }

        val interactor = GameAssetsInteractor()
        interactor.writeAssetsToFile("$testOutDir/testgame1.dat", gameDataRequirements, world)

        val readInRequirements = GameDataRequirements(24, 16, 8, 8, 6400)
        val newWorld = TileBasedGameWorld(readInRequirements)
        interactor.loadAssetsFromFile("$testOutDir/testgame1.dat", readInRequirements, newWorld)

        val spriteData = readInRequirements.getSpriteData(0)
        assertArrayEquals(expectedSpriteData, spriteData)

        assertEquals(1, newWorld.getLevel("Level 1").getSpriteTileAt(1, 1))
        assertEquals(0, newWorld.getLevel("Level 1").getSpriteTileAt(0, 0))
        assertEquals(1, newWorld.getLevel("Level 1").getSpriteTileAt(1, 0))
    }

    @Test
    fun `should store multiple levels to file`() {
        val expectedSpriteData = byteArrayOf(
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, -14, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
        )

        val expectedSpriteData1 = byteArrayOf(
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, -14, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 10, 0, 0, 10, 10, 11,11,
            0, 0, 0, 0, 0, 0, 0, 0,
        )

        gameDataRequirements.setData(0, expectedSpriteData)
        gameDataRequirements.setData(1, expectedSpriteData1)

        //  Now set up a level with a few of the sprites
        world.addLevel("Level 1", 100, 100).apply {
            setSpriteTileAt(0, 0, 0)
            setSpriteTileAt(1, 1, 1)
            setSpriteTileAt(1, 0, 1)
        }

        world.addLevel("Level 2", 100, 120).apply {
            setSpriteTileAt(0, 0, 1)
            setSpriteTileAt(8, 21, 0)
            setSpriteTileAt(2, 0, 1)
        }

        val interactor = GameAssetsInteractor()
        interactor.writeAssetsToFile("$testOutDir/testgame2.dat", gameDataRequirements, world)

        val readInRequirements = GameDataRequirements(24, 16, 8, 8, 6400)
        val newWorld = TileBasedGameWorld(readInRequirements)
        interactor.loadAssetsFromFile("$testOutDir/testgame2.dat", readInRequirements, newWorld)

        val spriteData = readInRequirements.getSpriteData(0)
        assertArrayEquals(expectedSpriteData, spriteData)

        assertEquals(1, newWorld.getLevel("Level 1").getSpriteTileAt(1, 1))
        assertEquals(0, newWorld.getLevel("Level 1").getSpriteTileAt(0, 0))
        assertEquals(1, newWorld.getLevel("Level 1").getSpriteTileAt(1, 0))
        assertEquals(-1, newWorld.getLevel("Level 1").getSpriteTileAt(19, 3))
        assertEquals(100, newWorld.getLevel("Level 1").widthInTiles)
        assertEquals(100, newWorld.getLevel("Level 1").heightInTiles)

        assertEquals(1, newWorld.getLevel("Level 2").getSpriteTileAt(0, 0))
        assertEquals(0, newWorld.getLevel("Level 2").getSpriteTileAt(8, 21))
        assertEquals(1, newWorld.getLevel("Level 2").getSpriteTileAt(2, 0))
        assertEquals(100, newWorld.getLevel("Level 2").widthInTiles)
        assertEquals(120, newWorld.getLevel("Level 2").heightInTiles)
    }

}