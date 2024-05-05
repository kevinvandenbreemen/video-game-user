package com.vandenbreemen.com.vandenbreemen.videogameusr.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.vandenbreemen.viddisplayrast.data.DisplayRaster

@Composable
fun RasterDisplay(raster: DisplayRaster) {


    Column {

        //  Render a white square
        Canvas(modifier=Modifier.fillMaxSize()) {

            //  Display all the pixels:
            val pixelWidthInCanvas = size.width / raster.xDim
            val pixelHeightInCanvas = size.height / raster.yDim

            for (y in 0 until raster.yDim) {
                for (x in 0 until raster.xDim) {
                    val left = x * pixelWidthInCanvas
                    val top = y * pixelHeightInCanvas
                    val color = if (raster.getPixel(
                            x,
                            y
                        ) > 0
                    ) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black
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
    raster.setPixel(8, 8, 1)
    RasterDisplay(raster)
}