package com.vandenbreemen.com.vandenbreemen.videogameusr.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import kotlin.math.ceil

@Composable
fun RasterDisplay(raster: DisplayRaster) {

    Column {

        //  Render a white square
        Canvas(modifier=Modifier.fillMaxSize()) {

            //  Display all the pixels:

            val width = size.width
            val height = size.height

            val pixelWidthInCanvas = ceil((width / raster.xDim).toDouble()).toFloat()
            val pixelHeightInCanvas = ceil((height / raster.yDim).toDouble()).toFloat()

            for (y in 0 until raster.yDim) {
                for (x in 0 until raster.xDim) {
                    val left = x * pixelWidthInCanvas
                    val top = y * pixelHeightInCanvas

                    val pixelColor = raster.getPixel(
                        x,
                        y
                    ).toInt()
                    val color = Color(pixelColor, pixelColor, pixelColor)

                    drawRect(color, topLeft = Offset(left, top), size = Size(pixelWidthInCanvas, pixelHeightInCanvas))
                }
            }
        }

    }

}

@Composable
@Preview
fun PreviewRasterDisplay() {
    val raster = DisplayRaster(16, 16)
    raster.setPixel(8, 8, 100)
    raster.setPixel(9, 8, 100)
    RasterDisplay(raster)
}