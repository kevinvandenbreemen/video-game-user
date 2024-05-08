package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import androidx.compose.ui.graphics.Color
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.com.vandenbreemen.videogameusr.model.ColorInteractor
import com.vandenbreemen.viddisplayrast.data.ByteColorDataInteractor
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner

class SpriteEditorModel(private val requirements: GameDataRequirements, private val spriteIndex: Int, private val requirementsVariableName: String) {

    private val runner = Runner(requirements)
    private val spriteByteArray = ByteArray(requirements.spriteWidth * requirements.spriteHeight)

    val spriteWidth = requirements.spriteWidth
    val spriteHeight = requirements.spriteHeight

    /**
     * Computation of color values etc
     */
    val byteColorDataInteractor = ByteColorDataInteractor()
    private val colorInteractor = ColorInteractor(byteColorDataInteractor)

    /**
     * Color of the paint brush for coloring pixels
     */
    var paintColor: Byte = 0


    init {

        //  Generate the
        runner.drawSpriteAt(spriteIndex, 0, 0)
        refreshSprite()
    }

    private fun refreshSprite() {
        runner.drawSpriteAt(spriteIndex, 0, 0)
        val raster = runner.newFrame()

        //  Start at 0, 0 and iterate over all the pixels the sprite occupies
        for(y in 0 until requirements.spriteHeight){
            for(x in 0 until requirements.spriteWidth){
                spriteByteArray[y * requirements.spriteWidth + x] = raster.getPixel(x, y)
            }
        }
    }

    fun setPixel(x: Int, y: Int, value: Byte){

        val index = y * requirements.spriteWidth + x
        if(index >= spriteByteArray.size){
            klog("Attempted to set pixel value out of bounds -- $x, $y.  This is probably a UI bug so ignoring the attempt at bailing")
            return
        }

        spriteByteArray[y * requirements.spriteWidth + x] = value
        requirements.setData(spriteIndex, spriteByteArray.clone())  //  Clone to avoid reference issues
        refreshSprite()
    }

    fun getPixel(x: Int, y: Int): Byte? {
        val index = y * requirements.spriteWidth + x
        if(index >= spriteByteArray.size){
            klog("Attempted to get pixel value from location that was out of bounds -- $x, $y.  This is probably a UI bug so ignoring the attempt at bailing")
            return null
        }
        return spriteByteArray[y * requirements.spriteWidth + x]
    }

    fun getSpriteByteArray(): ByteArray {
        return spriteByteArray
    }

    fun setBrightness(colorByte: Byte, brightness: Int): Byte {
        val red = byteColorDataInteractor.getRedChannel(colorByte)
        val green = byteColorDataInteractor.getGreenChannel(colorByte)
        val blue = byteColorDataInteractor.getBlueChannel(colorByte)
        return byteColorDataInteractor.getColorByte(brightness, red, green, blue)
    }

    fun setRed(colorByte: Byte, red: Int): Byte {
        val brightness = byteColorDataInteractor.getBrightness(colorByte)
        val green = byteColorDataInteractor.getGreenChannel(colorByte)
        val blue = byteColorDataInteractor.getBlueChannel(colorByte)
        return  byteColorDataInteractor.getColorByte(brightness, red, green, blue)
    }

    fun setGreen(colorByte: Byte, green: Int): Byte {
        val brightness = byteColorDataInteractor.getBrightness(colorByte)
        val red = byteColorDataInteractor.getRedChannel(colorByte)
        val blue = byteColorDataInteractor.getBlueChannel(colorByte)
        return  byteColorDataInteractor.getColorByte(brightness, red, green, blue)
    }

    fun setBlue(colorByte: Byte, blue: Int): Byte {
        val brightness = byteColorDataInteractor.getBrightness(colorByte)
        val red = byteColorDataInteractor.getRedChannel(colorByte)
        val green = byteColorDataInteractor.getGreenChannel(colorByte)
        return  byteColorDataInteractor.getColorByte(brightness, red, green, blue)
    }

    fun getComposeColor(): Color {
        return getComposeColor(paintColor)
    }

    fun getComposeColor(colorByte: Byte): Color {

        return colorInteractor.getComposeColor(colorByte)
    }

    /**
     * Build the source code required to generate the sprite and assign it to the requirements
     */
    fun generateSpriteSourceCode(): String {

        val stringBld = StringBuilder("""
$requirementsVariableName.setData($spriteIndex, byteArrayOf(""")
        spriteByteArray.forEachIndexed { index, byte ->
            if(index % requirements.spriteWidth == 0){
                stringBld.append("\n       ")
            }
            stringBld.append(byte)
            if(index < spriteByteArray.size - 1){
                stringBld.append(", ")
            }
        }
        stringBld.append("\n))")

        return stringBld.toString()

    }


}