package com.vandenbreemen.videogameusr.view.render

import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.videogameusr.model.game.LevelModel

/**
 * Takes a different approach:  Rather than draw what's inside the view of the level at runtime, renders out the entire level as a byte array and
 * renders what's in the view of that at runtime as a subview.
 */
class LevelPrerenderInteractor(
    private val requirements: GameDataRequirements, private val levelWidthPixels: Int, private val levelHeightPixels: Int) {

    private var cameraPosition: Pair<Int, Int> = Pair(0, 0)

    /**
     * The pre-render of the raster
     */
    private val levelPrerenderRaster = DisplayRaster(levelWidthPixels, levelHeightPixels)

    /**
     * Write the contents of the level out to the prerender
     */
    fun renderLevel(level: LevelModel) {

        for(y in 0 until level.heightInTiles) {
            for(x in 0 until level.widthInTiles) {
                val spriteIndex = level.getSpriteTileAt(x, y)
                if (spriteIndex == LevelModel.NO_SPRITE) {
                    continue
                }

                level.getSpriteBytesAt(x, y)?.let { bytes ->
                    for (j in 0 until level.spriteHeight) {
                        for (i in 0 until level.spriteWidth) {
                            levelPrerenderRaster.setPixel(
                                x * level.spriteWidth + i,
                                y * level.spriteHeight + j,
                                bytes[j * level.spriteWidth + i]
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Writes the given raster at the origin.
     */
    fun renderRaster(displayRaster: DisplayRaster) {
        try {
            //levelPrerenderRaster.writeRaster(0, 0, displayRaster)

            for (y in 0 until displayRaster.yDim) {
                for (x in 0 until displayRaster.xDim) {
                    levelPrerenderRaster.setPixel(x, y, displayRaster.getPixel(x, y))
                }
            }

        } catch (e: Exception) {
            throw RuntimeException("Failed to render raster (displayRaster dimensions:  ${displayRaster.xDim} x ${displayRaster.yDim}) vs expected dim ${levelPrerenderRaster.xDim} x ${levelPrerenderRaster.yDim})", e)
        }
    }

    fun renderCameraView(): DisplayRaster {
        return levelPrerenderRaster.view(
            cameraPosition.first,
            cameraPosition.second,
            cameraPosition.first  + requirements.screenWidth,
            cameraPosition.second + requirements.screenHeight
        )
    }


    fun moveCameraRight(){
        //  First make sure we're not at the edge
        if(cameraPosition.first + requirements.screenWidth >= levelWidthPixels-1){
            klog(KlogLevel.DEBUG, "Camera already at right edge")
            return
        }

        klog(KlogLevel.DEBUG, "Moving camera right")

        cameraPosition = Pair(cameraPosition.first + 1, cameraPosition.second)
    }

    fun moveCameraLeft(){
        if(cameraPosition.first == 0){
            return
        }

        klog(KlogLevel.DEBUG, "Moving camera left")

        cameraPosition = Pair(cameraPosition.first - 1, cameraPosition.second)
    }

    fun moveCameraUp(){
        if(cameraPosition.second == 0){
            return
        }

        klog(KlogLevel.DEBUG, "Moving camera up")

        cameraPosition = Pair(cameraPosition.first, cameraPosition.second - 1)
    }

    fun moveCameraDown(){
        if(cameraPosition.second + requirements.screenHeight >= levelHeightPixels-1){
            return
        }

        klog(KlogLevel.DEBUG, "Moving camera down")

        cameraPosition = Pair(cameraPosition.first, cameraPosition.second + 1)
    }

    fun moveCameraTo(x: Int, y: Int){
        //  Make sure this makes sense
        if(x < 0 || y < 0 || x >= requirements.screenWidth || y >= requirements.screenHeight){
            throw IllegalArgumentException("Invalid camera position $x, $y")
        }
        if(x + requirements.screenWidth >= requirements.screenWidth || y + requirements.screenHeight >= requirements.screenHeight) {
            throw IllegalArgumentException("Invalid camera position (camera goes off edge) $x, $y")
        }

        cameraPosition = Pair(x, y)
    }

}