package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

class SpriteCodeGenerationInteractor(private val requirements: GameDataRequirements) {

    fun generateCodeForSpriteIndex(spriteIndex: Int, requirementsVariableName: String): String {

        val byteArrayToCode = getSpriteTileGridArray(spriteIndex)

        val stringBld = StringBuilder("""
$requirementsVariableName.setData($spriteIndex, byteArrayOf(""")
        byteArrayToCode.forEachIndexed { index, byte ->
            if(index % requirements.spriteWidth == 0){
                stringBld.append("\n       ")
            }
            stringBld.append(byte)
            if(index < byteArrayToCode.size - 1){
                stringBld.append(", ")
            }
        }
        stringBld.append("\n))")

        return stringBld.toString()
    }

    fun getSpriteTileGridArray(index: Int): ByteArray {
        return requirements.getSpriteData(index)
    }

}