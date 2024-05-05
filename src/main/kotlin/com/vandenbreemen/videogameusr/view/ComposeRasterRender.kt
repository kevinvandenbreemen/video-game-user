package com.vandenbreemen.com.vandenbreemen.videogameusr.view

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.vandenbreemen.viddisplayrast.data.DisplayRaster


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
                RasterDisplay(raster)
            }
        }
    }

}