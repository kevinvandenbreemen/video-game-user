package com.vandenbreemen.videogameusr.tools.interactor

import com.vandenbreemen.viddisplayrast.view.TextRender
import com.vandenbreemen.videogameusr.log.KLogConfig
import com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.videogameusr.log.klogConfig
import com.vandenbreemen.videogameusr.view.render.ComposeRasterRender
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ImageImportInteractorTest {

    @BeforeEach
    fun setup() {
        klogConfig(KLogConfig(KlogLevel.DEBUG, true))
    }

    @Test
    fun `should import a png image`() {
        runBlocking {
            val interactor = ImageImportInteractor()
            val pixels = interactor.importImageAsRaster("src/test/resources/assets/test_image.png")
            println(TextRender().renderRaster(pixels))

            ComposeRasterRender.showTestRenderWindow(pixels)
            Thread.sleep(11000)
        }
    }

}