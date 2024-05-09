package com.vandenbreemen.com.vandenbreemen.videogameusr.view

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.game.Runner


class ComposeRasterRender {

    companion object {
        fun showTestRenderWindow(raster: DisplayRaster, maxWidth: Int = 800) = application {

            //  Step 1:  Work out the height as a ratio of the width
            val ratio  = raster.yDim.toFloat() / raster.xDim.toFloat()
            val height = (maxWidth * ratio).toInt()

            Window(
                onCloseRequest = {  },
                visible = true,
                title = "Raster Render Test",
                state = WindowState(width = maxWidth.dp, height = height.dp)
            ) {
                RasterDisplay(raster, DummyVideoGameController())
            }
        }

        fun playGameInWindow(runner: Runner, controller: VideoGameController, framesPerSecond: Int = 60, maxWidth: Int = 800) = application {

            val maxFPS = 60
            val fpsToUse = if(framesPerSecond > maxFPS) {
                klog(KlogLevel.DEBUG, "FPS requested is greater than max of $maxFPS.  Using max.", Throwable())
                maxFPS
            } else framesPerSecond

            //  Step 1:  Work out the height as a ratio of the width
            val height = (maxWidth * 0.75).toInt()

            Window(
                onCloseRequest = {  },
                visible = true,
                title = "Video Game Test",
                state = WindowState(width = maxWidth.dp, height = height.dp)
            ) {
                VideoGameUserTheme {
                    GameConsole(runner, fpsToUse, controller)
                }
            }
        }
    }

}