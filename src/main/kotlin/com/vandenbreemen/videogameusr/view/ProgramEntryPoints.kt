package com.vandenbreemen.videogameusr.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.videogameusr.log.klog


class ProgramEntryPoints {

    companion object {

        fun playGameInWindow(controller: VideoGameController, framesPerSecond: Int = 60, maxWidth: Int = 800, aspectRatio:Float = .75f) = application {

            val maxFPS = 60
            val fpsToUse = if(framesPerSecond > maxFPS) {
                klog(KlogLevel.DEBUG, "FPS requested is greater than max of $maxFPS.  Using max.", Throwable())
                maxFPS
            } else framesPerSecond

            //  Step 1:  Work out the height as a ratio of the width
            val height = (maxWidth * aspectRatio).toInt()

            Window(
                onCloseRequest = {  },
                visible = true,
                title = "Video Game Test",
                state = WindowState(width = maxWidth.dp, height = height.dp)
            ) {
                VideoGameUserTheme {
                    Column(modifier= Modifier.fillMaxSize()) {
                        GameConsole(fpsToUse, controller)
                    }

                }
            }
        }

        fun playGameInWindowSNES(controller: VideoGameController, framesPerSecond: Int, maxWidth: Int = 800) {
            val snesWidth = 256
            val snesHeight = 224
            val aspectRatio = snesHeight.toFloat() / snesWidth.toFloat()
            playGameInWindow(controller, framesPerSecond, maxWidth, aspectRatio)
        }
    }

}