package com.vandenbreemen.videogameusr.tools.interactor

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class SpriteCodeGenerationInteractor(private val requirements: GameDataRequirements) {

    private var fileToWriteTo = "generated/SpriteData.kt"
    private val codeGenerationModel = CoreDependenciesHelper.getCodeGenerationModel()

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

    /**
     * Write out all sprites to a file
     */
    fun writeAllSpritesToFile() {

        CoroutineScope(CoreDependenciesHelper.getIODispatcher()).launch {

            codeGenerationModel.setupCodeGeneratingDirectory()

            val codeBuilder = StringBuilder()
            for(index in 0 until (requirements.maxBytes/(requirements.spriteWidth * requirements.spriteHeight))){
                codeBuilder.append(generateCodeForSpriteIndex(index, "requirements"))
                codeBuilder.append("\n\n")
            }

            File(fileToWriteTo).writeText(codeBuilder.toString())
            klog("Wrote all sprites to $fileToWriteTo")
        }

    }

}