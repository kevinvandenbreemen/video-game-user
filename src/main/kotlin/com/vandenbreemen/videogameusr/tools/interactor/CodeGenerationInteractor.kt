package com.vandenbreemen.videogameusr.tools.interactor

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.game.LevelModel
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class CodeGenerationInteractor(private val requirements: GameDataRequirements) {

    private var fileToWriteTo = "generated/SpriteData.kt"
    private val tileDataFileToWrite = "generated/TileData.kt"
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
            generateSpriteSheet(codeBuilder)

            File(fileToWriteTo).writeText(codeBuilder.toString())
            klog("Wrote all sprites to $fileToWriteTo")
        }

    }

    private fun generateSpriteSheet(codeBuilder: StringBuilder) {
        for (index in 0 until (requirements.maxBytes / (requirements.spriteWidth * requirements.spriteHeight))) {
            codeBuilder.append(generateCodeForSpriteIndex(index, "requirements"))
            codeBuilder.append("\n\n")
        }
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

    /**
     * Writes every asset in the game (sprites, levels, and all) to a single file.  Note that this function should only be used
     * if your plan is to do most of the asset code writing using tools as it'll be pretty hard to find individual assets and change them!
     * @param  tileBasedGameWorldVariableName The name of the variable that holds the tile based game world
     * @param  levelWidth The width of each level
     * @param  levelHeight The height of each level
     */
    fun generateAssetSheet(
        tileBasedGameWorld: TileBasedGameWorld,
        requirementsVariableName: String, tileBasedGameWorldVariableName: String, levelWidth: Int, levelHeight: Int) {

        CoroutineScope(CoreDependenciesHelper.getIODispatcher()).launch {

            codeGenerationModel.setupCodeGeneratingDirectory()

            val codeBuilder = StringBuilder()
            codeBuilder.append("package com.vandenbreemen.videogameusr.generated\n\n")
            codeBuilder.append("import com.vandenbreemen.viddisplayrast.data.GameDataRequirements\n")
            codeBuilder.append("import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld\n\n")
            codeBuilder.append("fun generateAssets($requirementsVariableName: GameDataRequirements, $tileBasedGameWorldVariableName: TileBasedGameWorld) {\n\n")

            //  Do the sprites
            generateSpriteSheet(codeBuilder)

            codeBuilder.append("\n\n")

            //  Do the levels
            tileBasedGameWorld.getLevelNames().forEach { levelName ->
                val levelModel = tileBasedGameWorld.getLevel(levelName)
                codeBuilder.append("val levelModel = $tileBasedGameWorldVariableName.addLevel(\"$levelName\", $levelWidth, $levelHeight)\n")
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
                codeBuilder.append("}\n")
            }

            codeBuilder.append("}\n")

            File("generated/Assets.kt").writeText(codeBuilder.toString())
            klog("Wrote all assets to generated/Assets.kt")
        }

    }

}