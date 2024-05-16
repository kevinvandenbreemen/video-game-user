package com.vandenbreemen.videogameusr.view.render

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.viddisplayrast.view.TextRender
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RunnerViewTest {

    //  Simple game data screen
    private val requirements = GameDataRequirements(10, 8, 3, 3, 1000)
    private val runnerView = RunnerView(requirements)

    @BeforeEach
    fun setup() {
        requirements.setData(0, byteArrayOf(
            10, 0, 0,
            0, 1, 0,
            0, 0, 20
        ))

        requirements.setData(1, byteArrayOf(
            0, 16, 0,
            17, 88, 17,
            0, 16, 0
        ))
    }

    @Test
    fun `should draw a frame with correct dimensions`() {

        //  First set up some sprites
        runnerView.drawSpriteAt(0, 2, 2, 0)
        runnerView.drawSpriteAt(1, 5, 5, 0)
        runnerView.drawSpriteAt(0, 7, 5, 0)

        val runner = Runner(requirements)
        runner.drawSpriteAt(0, 2, 2, 0)
        runner.drawSpriteAt(1, 5, 5, 0)
        runner.drawSpriteAt(0, 7, 5, 0)

        //  Printout of what it looks like
        val fullScreenRaster = runner.newFrame()
        val viewRaster = runnerView.newFrame()

        println("Full screen raster")
        println(TextRender().renderRaster(fullScreenRaster))

        println("View raster")
        println(TextRender().renderRaster(viewRaster))
        assertEquals(5, viewRaster.xDim)
        assertEquals(3, viewRaster.yDim)

    }

}