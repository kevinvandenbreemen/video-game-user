package com.vandenbreemen.videogameusr.tools.model

import androidx.compose.ui.graphics.Color
import com.vandenbreemen.viddisplayrast.data.ByteColorDataInteractor
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.videogameusr.model.ColorInteractor
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.model.game.assetmgt.GameAssetsInteractor
import com.vandenbreemen.videogameusr.tools.interactor.CodeGenerationInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GameDataEditorModel(private val requirements: GameDataRequirements,
                          private val tileBasedGameWorld: TileBasedGameWorld,
                          private var spriteIndex: Int, private val requirementsVariableName: String,
                          private  val assetsDataFilePath: String = "generated/game.dat"
    ) {

    private val codeGenerationInteractor = CodeGenerationInteractor(requirements)
    private val gameAssetsInteractor = GameAssetsInteractor()

    private val runner = Runner(requirements)
    private val spriteByteArray = ByteArray(requirements.spriteWidth * requirements.spriteHeight)

    val spriteWidth = requirements.spriteWidth
    val spriteHeight = requirements.spriteHeight
    val currentSpriteIndex: Int
        get() = spriteIndex

    private val previouslySelectedSpriteIndices = mutableSetOf(spriteIndex)

    /**
     * Only allow rotating sprite tiles if they are square, otherwise we lose fidelity to some kind of weird algorithm
     */
    val canRotateSpriteTiles: Boolean
        get() = requirements.spriteWidth == requirements.spriteHeight

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

    //  Sprite tile grid stuff

    /**
     * Get the start and end index of sprites to show on the tile preview grid
     */
    fun getSpriteTileGridRange(): Pair<Int, Int> {
        return Pair(0, (requirements.maxBytes/(requirements.spriteWidth * requirements.spriteHeight)) - 1)
    }

    val tilesPerRowOnSpriteTileGrid = 4

    fun getSpriteTileGridArray(index: Int): ByteArray {
        return codeGenerationInteractor.getSpriteTileGridArray(index)
    }

    fun selectSpriteIndex(index: Int) {
        spriteIndex = index
        previouslySelectedSpriteIndices.add(index)
        refreshSprite()
    }

    //  Level editing stuff

    /**
     * Get a model suitable for editing a level
     */
    fun editLevel(name: String): LevelEditorModel {
        return LevelEditorModel(requirements, tileBasedGameWorld.getLevel(name), this, colorInteractor, name)
    }

    fun getLevelNames(): List<String> {
        return tileBasedGameWorld.getLevelNames()
    }

    fun addLevel(name: String, widthTiles: Int, heightTiles: Int) {
        tileBasedGameWorld.addLevel(name, widthTiles, heightTiles)
    }

    /**
     * Copy the contents of sprite at fromIndex to the current selected sprite
     */
    fun copySprite(fromIndex: Int) {
        val spriteDataAtOrigin = requirements.getSpriteData(fromIndex)
        requirements.setData(spriteIndex, spriteDataAtOrigin)
        refreshSprite()
    }

    fun dumpAssetsToFile(): String {

        CoroutineScope(CoreDependenciesHelper.getIODispatcher()).launch {
            gameAssetsInteractor.writeAssetsToFile(assetsDataFilePath, requirements, tileBasedGameWorld)
        }

        return assetsDataFilePath
    }

    fun mirrorVertical(): ByteArray {
        val mirrored = ByteArray(spriteByteArray.size)
        for(y in 0 until spriteHeight){
            for(x in 0 until spriteWidth){
                mirrored[y * spriteWidth + x] = spriteByteArray[(spriteHeight - y - 1) * spriteWidth + x]
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

    fun fill(value: Byte) {
        spriteByteArray.forEachIndexed { index, byte ->
            spriteByteArray[index] = value
        }
        requirements.setData(spriteIndex, spriteByteArray.clone())
        refreshSprite()
    }

    fun rotateClockwise() {
        val rotated = ByteArray(spriteByteArray.size)
        for(y in 0 until spriteHeight){
            for(x in 0 until spriteWidth){
                rotated[x * spriteHeight + (spriteHeight - y - 1)] = spriteByteArray[y * spriteWidth + x]
            }
        }

        //  Update the bytes in here
        this.spriteByteArray.forEachIndexed { index, _ ->
            this.spriteByteArray[index] = rotated[index]
        }

        requirements.setData(spriteIndex, spriteByteArray.clone())
        refreshSprite()
    }

}