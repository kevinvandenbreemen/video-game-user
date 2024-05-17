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

}