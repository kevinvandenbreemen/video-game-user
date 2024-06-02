package com.vandenbreemen.videogameusr.tools.interactor

import com.vandenbreemen.viddisplayrast.data.ByteColorDataInteractor
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import kotlinx.coroutines.withContext
import java.io.File
import javax.imageio.ImageIO

class ImageImportInteractor() {

    private val ioDispatcher = CoreDependenciesHelper.getIODispatcher()
    private val byteColorDataInteractor = ByteColorDataInteractor()

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
                raster.setPixel(x, y, byteColorDataInteractor.convertIntColorToByte(pixel))
            }
        }

        return@withContext raster

    }

}