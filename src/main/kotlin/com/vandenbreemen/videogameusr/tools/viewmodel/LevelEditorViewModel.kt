package com.vandenbreemen.videogameusr.tools.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.videogameusr.tools.model.GameDataEditorModel
import com.vandenbreemen.videogameusr.tools.model.LevelEditorModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LevelEditorViewModel(private val levelEditorModel: LevelEditorModel) {

    private val _currentSelectedSpriteIndex = MutableStateFlow(levelEditorModel.selectedSpriteIndex)
    val currentSelectedSpriteIndexStateFlow = _currentSelectedSpriteIndex.asStateFlow()
    val currentSelectedSpriteIndex: Int get() = levelEditorModel.selectedSpriteIndex

    private val _scale = MutableStateFlow(levelEditorModel.zoom)
    val scale = _scale.asStateFlow()

    private val _pan = MutableStateFlow(Offset(levelEditorModel.pan.first, levelEditorModel.pan.second))
    val pan = _pan.asStateFlow()

    private val _levelCoordinateToTileGrid = MutableStateFlow<Array<Array<Int>>>(emptyArray())
    val levelCoordinateToTileGrid = _levelCoordinateToTileGrid.asStateFlow()

    val levelWidth: Int get() = levelEditorModel.levelWidth
    val levelHeight: Int get() = levelEditorModel.levelHeight

    val spriteWidth: Int get() = levelEditorModel.spriteWidth
    val spriteHeight: Int get() = levelEditorModel.spriteHeight

    val levelName: String get() = levelEditorModel.levelName

    private val lastSelectedTile = MutableStateFlow(Pair(-1, -1))
    val lastSelectedTileStateFlow = lastSelectedTile.asStateFlow()

    init {
        updateLevelCoordinateToTileGrid()
    }

    fun selectSpriteIndex(index: Int) {
        levelEditorModel.selectSpriteIndex(index)
        _currentSelectedSpriteIndex.value = index
    }

    fun getSpriteEditorModel(): GameDataEditorModel {
        return levelEditorModel.getSpriteEditorModel()
    }

    fun zoomIn(){
        levelEditorModel.zoomIn()
        _scale.value = levelEditorModel.zoom
    }

    fun zoomOut(){
        levelEditorModel.zoomOut()
        _scale.value = levelEditorModel.zoom
    }

    fun pan(x: Float, y: Float){
        levelEditorModel.pan(x, y)
        _pan.value = Offset(levelEditorModel.pan.first, levelEditorModel.pan.second)
    }

    fun getSpritePixelColorGridForSpriteIndex(spriteIndex: Int): Array<Array<Color>>? {
        return levelEditorModel.getSpritePixelColorGridForTileIndex(spriteIndex)
    }

    fun setSpriteTileAt(x: Int, y: Int, spriteIndex: Int){
        if(x < 0 || x >= levelWidth || y < 0 || y >= levelHeight){
            klog("Invalid tile location $x, $y, max x=$levelWidth, max y=$levelHeight - ignoring")
            return
        }
        lastSelectedTile.value = Pair(x, y)
        levelEditorModel.setSpriteTileAt(x, y, spriteIndex)
        updateLevelCoordinateToTileGrid()
    }

    private fun updateLevelCoordinateToTileGrid(){
        val grid = Array(levelHeight) { y ->
            Array(levelWidth) { x ->
                levelEditorModel.getSpriteIndexAt(x, y)
            }
        }
        _levelCoordinateToTileGrid.value = grid
    }

}