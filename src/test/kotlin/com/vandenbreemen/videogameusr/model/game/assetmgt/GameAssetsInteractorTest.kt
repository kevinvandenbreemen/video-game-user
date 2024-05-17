package com.vandenbreemen.videogameusr.model.game.assetmgt

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.KLogConfig
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klogConfig
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.viddisplayrast.view.TextRender
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameAssetsInteractorTest {

    private val gameDataRequirements = GameDataRequirements(24, 16, 8, 8, 6400)
    private val world = TileBasedGameWorld(gameDataRequirements)

    @BeforeEach
    fun setup() {
        klogConfig(KLogConfig(level = KlogLevel.DEBUG, logImmediate = true))
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

}