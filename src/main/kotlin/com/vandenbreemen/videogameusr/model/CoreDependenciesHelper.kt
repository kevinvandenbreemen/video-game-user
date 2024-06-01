package com.vandenbreemen.videogameusr.model

import com.vandenbreemen.viddisplayrast.data.ByteColorDataInteractor
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.view.render.CanvasRasterRender
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Objects commonly used so you don't have to keep creating them
 */
object CoreDependenciesHelper {

    private val byteDataInteractor = ByteColorDataInteractor()
    fun getColorInteractor(): ColorInteractor {
        return ColorInteractor(byteDataInteractor)
    }

    fun getIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    fun getCanvasRasterRender(): CanvasRasterRender {
        return CanvasRasterRender(getColorInteractor())
    }

    /**
     * Provide for master game data requirements for the entire game!
     * See also https://snes.nesdev.org/wiki/Tiles
     */
    fun createRequirements(width: Int, height: Int, tileWidth: Int, tileHeight: Int, numTiles: Int): GameDataRequirements {
        return GameDataRequirements(width, height, tileWidth, tileHeight, numTiles * (tileWidth * tileHeight))
    }

    fun createSNESRequirements(numTiles: Int): GameDataRequirements {
        return createRequirements(256, 224, 8, 8, numTiles)
    }

}