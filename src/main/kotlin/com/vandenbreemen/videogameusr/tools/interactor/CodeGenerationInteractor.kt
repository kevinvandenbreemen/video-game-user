package com.vandenbreemen.videogameusr.tools.interactor

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.game.LevelModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class CodeGenerationInteractor(private val requirements: GameDataRequirements) {

    private val tileDataFileToWrite = "generated/TileData.kt"
    private val codeGenerationModel = CoreDependenciesHelper.getCodeGenerationModel()

    fun getSpriteTileGridArray(index: Int): ByteArray {
        return requirements.getSpriteData(index)
    }

    fun writeLevelToFile(levelModel: LevelModel) {
        CoroutineScope(CoreDependenciesHelper.getIODispatcher()).launch {
            codeGenerationModel.setupCodeGeneratingDirectory()

            val codeBuilder = StringBuilder()

            codeBuilder.append("levelModel.apply {\n")
            for(row in 0 until levelModel.heightInTiles) {
                codeBuilder.append("setSpritesOnRow($row, listOf(")
                for (col in 0 until levelModel.widthInTiles) {

                    val spriteIndex = levelModel.getSpriteTileAt(col, row)
                    codeBuilder.append(spriteIndex)
                    if (col < levelModel.widthInTiles - 1) {
                        codeBuilder.append(", ")
                    }

                }
                codeBuilder.append("))\n")
            }
            codeBuilder.append("}")

            File(tileDataFileToWrite).writeText(codeBuilder.toString())
            klog("Wrote level data to $tileDataFileToWrite")
        }
    }

}