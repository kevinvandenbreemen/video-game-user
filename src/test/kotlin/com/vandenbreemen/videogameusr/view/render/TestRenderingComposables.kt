package com.vandenbreemen.videogameusr.view.render

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper


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


@Composable
fun RasterDisplay(raster: DisplayRaster) {

    val colorInteractor = CoreDependenciesHelper.getColorInteractor()

    Column {

        //  Render a white square
        Canvas(modifier= Modifier.fillMaxSize()) {

            //  Display all the pixels:

            val width = size.width
            val height = size.height

            //  Black background
            drawRect(Color.Black, topLeft = Offset(0f, 0f), size = Size(width, height))

            val pixelWidthInCanvas = width / raster.xDim
            val pixelHeightInCanvas = height / raster.yDim

            for (y in 0 until raster.yDim) {
                for (x in 0 until raster.xDim) {
                    val left = x * pixelWidthInCanvas
                    val top = y * pixelHeightInCanvas
                    val color = colorInteractor.getComposeColor(raster.getPixel(x, y))
                    drawRect(color, topLeft = Offset(left, top), size = Size(pixelWidthInCanvas, pixelHeightInCanvas))
                }
            }
        }

    }

}