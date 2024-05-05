package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner

class SpriteEditorModel(private val requirements: GameDataRequirements, private val spriteIndex: Int) {

    private val runner = Runner(requirements)
    private val spriteByteArray = ByteArray(requirements.spriteWidth * requirements.spriteHeight)

    val spriteWidth = requirements.spriteWidth
    val spriteHeight = requirements.spriteHeight

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
        spriteByteArray[y * requirements.spriteWidth + x] = value
        requirements.setData(spriteIndex, spriteByteArray.clone())  //  Clone to avoid reference issues
        refreshSprite()
    }

    fun getSpriteByteArray(): ByteArray {
        return spriteByteArray
    }

    /**
     * Build the source code required to generate the sprite and assign it to the requirements
     */
    fun generateSpriteSourceCode(): String {

        //  TODO    This should probably get made into a pluggable interactor since the library is written in java....  For now
        //  we'll just generate the code in kotlin but someday it might be necessary to write a java code generator....
        val stringBld = StringBuilder("""
requirement.setData($spriteIndex, byteArrayOf(""")
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