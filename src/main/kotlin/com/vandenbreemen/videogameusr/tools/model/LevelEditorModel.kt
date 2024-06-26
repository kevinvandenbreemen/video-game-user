package com.vandenbreemen.videogameusr.tools.model

import androidx.compose.ui.graphics.Color
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.ColorInteractor
import com.vandenbreemen.videogameusr.model.game.LevelModel

class LevelEditorModel(private val requirements: GameDataRequirements,
                       private val levelModel: LevelModel,
                       private val gameDataEditorModel: GameDataEditorModel,
                       private val colorInteractor: ColorInteractor,
                       val levelName: String,
    ) {

    val spriteWidth = requirements.spriteWidth
    val spriteHeight = requirements.spriteHeight

    val levelWidth = levelModel.widthInTiles
    val levelHeight = levelModel.heightInTiles

    private var currentSelectedSpriteIndex: Int = 0
    val selectedSpriteIndex: Int get() = currentSelectedSpriteIndex

    private var zoomLevel = 1f
    val zoom get() = zoomLevel

    private var panXY = Pair(0f, 0f)
    val pan get() = panXY

    fun selectSpriteIndex(index: Int){
        currentSelectedSpriteIndex = index
    }

    fun getSpriteEditorModel(): GameDataEditorModel {
        return gameDataEditorModel
    }

    /**
     * Zoom in to a max of 2x
     */
    fun zoomIn(){
        if (zoomLevel >= 2f){
            return
        }
        zoomLevel += 0.5f
    }

    /**
     * Zoom out to a maximum of 0.5x
     */
    fun zoomOut(){
        if(zoomLevel <= 0.5f){
            return
        }
        zoomLevel -= 0.5f
    }

    fun pan(x: Float, y: Float){
        panXY = Pair(panXY.first + x, panXY.second + y)
    }

    fun getSpritePixelColorGridForTileIndex(tileIndex: Int): Array<Array<Color>>? {

        if(tileIndex == LevelModel.NO_SPRITE){
            return null
        }

        val spriteBytes = requirements.getSpriteData(tileIndex) ?: return null

        //  Now build grid based on spriteWidth and spriteHeight
        val grid = Array(spriteHeight) { y ->
            Array(spriteWidth) { x ->
                val byte = spriteBytes[y * spriteWidth + x]
                colorInteractor.getComposeColor(byte)
            }
        }

        return grid
    }

    fun setSpriteTileAt(x: Int, y: Int, spriteIndex: Int){
        levelModel.setSpriteTileAt(x, y, spriteIndex)
    }

    fun getSpriteIndexAt(x: Int, y: Int): Int {
        return levelModel.getSpriteTileAt(x, y)
    }

}