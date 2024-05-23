package com.vandenbreemen.videogameusr.view.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.vandenbreemen.com.vandenbreemen.videogameusr.model.ColorInteractor
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.view.RasterRender
import kotlin.math.ceil

class CanvasRasterRender(private val colorInteractor: ColorInteractor): RasterRender<DrawScope> {

    private var currentScope: DrawScope? = null

    fun setCurrentDrawScope(scope: DrawScope) {
        currentScope = scope
    }

    override fun renderRaster(raster: DisplayRaster): DrawScope {
        currentScope?.let { scope ->

            val width = scope.size.width
            val height = scope.size.height

            val pixelWidthInCanvas = ceil((width / raster.xDim).toDouble()).toFloat()
            val pixelHeightInCanvas = ceil((height / raster.yDim).toDouble()).toFloat()

            for (y in 0 until raster.yDim) {
                for (x in 0 until raster.xDim) {

                    val left = x * pixelWidthInCanvas
                    val top = y * pixelHeightInCanvas

                    val color = colorInteractor.getComposeColor(raster.getPixel(x, y))
                    scope.drawRect(color, topLeft = Offset(left, top), size = Size(pixelWidthInCanvas, pixelHeightInCanvas))
                }
            }

            this.currentScope = null    //  Force reset this to prevent mem leaks

            return scope
        } ?: throw IllegalStateException("No draw scope set!")
    }
}