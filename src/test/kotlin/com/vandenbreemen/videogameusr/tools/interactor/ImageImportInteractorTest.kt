package com.vandenbreemen.videogameusr.tools.interactor

import com.vandenbreemen.videogameusr.log.KLogConfig
import com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.videogameusr.log.klogConfig
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

            //ComposeRasterRender.showTestRenderWindow(pixels)
        }
    }

    @Test
    fun `should import png from classpath`() {
        runBlocking {
            val interactor = ImageImportInteractor()
            val pixels = interactor.importImageAsRasterFromClasspath("/assets/test_image.png")

            //ComposeRasterRender.showTestRenderWindow(pixels)
        }
    }

}