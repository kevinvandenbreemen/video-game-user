package com.vandenbreemen.com.vandenbreemen.videogameusr.controller

import com.vandenbreemen.viddisplayrast.data.DisplayRaster

/**
 * This is where the actual game logic should be implemented.  Please note that implementations of this interface
 * should NEVER call the Runner.newFrame() method!
 */
interface VideoGameController {
    fun moveRight()
    fun moveLeft()
    fun moveUp()
    fun moveDown()
    fun pressA()
    fun pressB()

    /**
     * Have the game AI etc play its turn.  Note that this method should NOT attempt to draw the frame.  That is the
     * responsibility of the drawFrame method.
     */
    fun playTurn()

    /**
     * Draw a frame in the raster.
     */
    fun drawFrame(): DisplayRaster

    fun getComposeColor(value: Byte): androidx.compose.ui.graphics.Color
}