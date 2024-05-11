package com.vandenbreemen.videogameusr.tools.model

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.LevelModel

class LevelEditorModel(private val requirements: GameDataRequirements,
                       private val levelModel: LevelModel,
                        private val spriteEditorModel: SpriteEditorModel
    ) {

    val spriteWidth = requirements.spriteWidth
    val spriteHeight = requirements.spriteHeight

    private var currentSelectedSpriteIndex: Int = 0
    val selectedSpriteIndex: Int get() = currentSelectedSpriteIndex

    private var zoomLevel = 1f
    val zoom get() = zoomLevel

    private var panXY = Pair(0f, 0f)
    val pan get() = panXY

    fun selectSpriteIndex(index: Int){
        currentSelectedSpriteIndex = index
    }

    fun getSpriteEditorModel(): SpriteEditorModel {
        return spriteEditorModel
    }

    fun zoomIn(){
        zoomLevel += 0.5f
    }

    fun zoomOut(){
        zoomLevel -= 0.5f
    }

    fun pan(x: Float, y: Float){
        panXY = Pair(panXY.first + x, panXY.second + y)
    }

}