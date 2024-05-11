package com.vandenbreemen.videogameusr.tools.model

import androidx.compose.ui.graphics.Color
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.com.vandenbreemen.videogameusr.model.ColorInteractor
import com.vandenbreemen.com.vandenbreemen.videogameusr.tools.SpriteCodeGenerationInteractor
import com.vandenbreemen.viddisplayrast.data.ByteColorDataInteractor
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.game.LevelModel

class SpriteEditorModel(private val requirements: GameDataRequirements, private var spriteIndex: Int, private val requirementsVariableName: String) {

    private val spriteCodeGenerationInteractor = SpriteCodeGenerationInteractor(requirements)

    private val runner = Runner(requirements)
    private val spriteByteArray = ByteArray(requirements.spriteWidth * requirements.spriteHeight)

    val spriteWidth = requirements.spriteWidth
    val spriteHeight = requirements.spriteHeight
    val currentSpriteIndex: Int
        get() = spriteIndex

    private val previouslySelectedSpriteIndices = mutableSetOf(spriteIndex)

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

    /**
     * Mirrors the sprite horizontally, returning the result
     */
    fun mirrorHorizontal(): ByteArray {
        val mirrored = ByteArray(spriteByteArray.size)
        for(y in 0 until spriteHeight){
            for(x in 0 until spriteWidth){
                mirrored[y * spriteWidth + x] = spriteByteArray[y * spriteWidth + (spriteWidth - x - 1)]
            }
        }

        //  Update the bytes in here
        this.spriteByteArray.forEachIndexed { index, byte ->
            this.spriteByteArray[index] = mirrored[index]
        }

        requirements.setData(spriteIndex, spriteByteArray.clone())
        refreshSprite()

        return this.spriteByteArray
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

    fun clearSprite(): ByteArray {
        spriteByteArray.forEachIndexed { index, byte ->
            spriteByteArray[index] = 0
        }
        requirements.setData(spriteIndex, spriteByteArray.clone())
        refreshSprite()

        return spriteByteArray
    }

    fun getComposeColor(colorByte: Byte): Color {

        return colorInteractor.getComposeColor(colorByte)
    }

    /**
     * Build the source code required to generate the sprite and assign it to the requirements
     */
    fun generateSpriteSourceCode(): String {

        val codeBuilder = StringBuilder()
        previouslySelectedSpriteIndices.sorted().forEach { index ->
            codeBuilder.append(spriteCodeGenerationInteractor.generateCodeForSpriteIndex(index, requirementsVariableName))
            codeBuilder.append("\n\n")
        }

        //  Kick off a full sprite write
        spriteCodeGenerationInteractor.writeAllSpritesToFile()

        return codeBuilder.toString()
    }

    //  Sprite tile grid stuff

    /**
     * Get the start and end index of sprites to show on the tile preview grid
     */
    fun getSpriteTileGridRange(): Pair<Int, Int> {
        return Pair(0, (requirements.maxBytes/(requirements.spriteWidth * requirements.spriteHeight)) - 1)
    }

    val tilesPerRowOnSpriteTileGrid = 4

    fun getSpriteTileGridArray(index: Int): ByteArray {
        return spriteCodeGenerationInteractor.getSpriteTileGridArray(index)
    }

    fun selectSpriteIndex(index: Int) {
        spriteIndex = index
        previouslySelectedSpriteIndices.add(index)
        refreshSprite()
    }

    //  Level editing stuff

    //  TODO    Gotta make all this parameterizable
    private val currentLevelBeingEdited = LevelModel(requirements, 100, 100)
    private val levelEditorModel = LevelEditorModel(requirements, currentLevelBeingEdited, this,
        CoreDependenciesHelper.getColorInteractor()
        )

    /**
     * Get a model suitable for editing a level
     */
    fun getLevelEditorModel(): LevelEditorModel {
        return levelEditorModel
    }

    /**
     * Copy the contents of sprite at fromIndex to the current selected sprite
     */
    fun copySprite(fromIndex: Int) {
        val spriteDataAtOrigin = requirements.getSpriteData(fromIndex)
        requirements.setData(spriteIndex, spriteDataAtOrigin)
    }

}