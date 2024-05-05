package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.viddisplayrast.view.TextRender

class SpriteEditorModel(private val requirements: GameDataRequirements, private val spriteIndex: Int) {

    private val runner = Runner(requirements)
    private val spriteByteArray = ByteArray(requirements.spriteWidth * requirements.spriteHeight)

    val spriteWidth = requirements.spriteWidth
    val spriteHeight = requirements.spriteHeight

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

        TextRender().renderRaster(raster)
    }

    fun setPixel(x: Int, y: Int, value: Byte){
        spriteByteArray[y * requirements.spriteWidth + x] = value
        requirements.setData(spriteIndex, spriteByteArray.clone())  //  Clone to avoid reference issues
        refreshSprite()
    }

    fun getSpriteByteArray(): ByteArray {
        return spriteByteArray
    }

}