package com.vandenbreemen.videogameusr.tools.interactor

import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import kotlinx.coroutines.withContext
import java.io.File
import javax.imageio.ImageIO

class ImageImportInteractor() {

    private val ioDispatcher = CoreDependenciesHelper.getIODispatcher()

    suspend fun importImageAsRaster(imagePath: String): DisplayRaster = withContext(ioDispatcher) {

        val bufferedImage = ImageIO.read(File(imagePath))

        //  Now read in pixels array
        val pixels = IntArray(bufferedImage.width * bufferedImage.height)
        bufferedImage.getRGB(0, 0, bufferedImage.width, bufferedImage.height, pixels, 0, bufferedImage.width)

        val raster = DisplayRaster(bufferedImage.width, bufferedImage.height)

        //  Now gotta populate the raster
        for (y in 0 until bufferedImage.height) {
            for (x in 0 until bufferedImage.width) {
                val pixel = pixels[y * bufferedImage.width + x]
                val red = (pixel shr 16) and 0xFF
                val green = (pixel shr 8) and 0xFF
                val blue = pixel and 0xFF
                val alpha = (pixel shr 24) and 0xFF

                val color = (alpha shl 24) or (red shl 16) or (green shl 8) or blue


                val byte = color.toByte()
                klog(KlogLevel.DEBUG, "r=$red, g=$green, b=$blue, a=$alpha, byte=$byte")

                raster.setPixel(x, y, color.toByte())
            }
        }

        return@withContext raster

    }

}