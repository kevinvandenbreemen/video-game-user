package com.vandenbreemen.videogameusr.tools.viewmodel

import androidx.compose.ui.geometry.Offset
import com.vandenbreemen.videogameusr.tools.model.LevelEditorModel
import com.vandenbreemen.videogameusr.tools.model.SpriteEditorModel
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

    val levelWidth: Int get() = levelEditorModel.levelWidth
    val levelHeight: Int get() = levelEditorModel.levelHeight

    val spriteWidth: Int get() = levelEditorModel.spriteWidth
    val spriteHeight: Int get() = levelEditorModel.spriteHeight

    fun selectSpriteIndex(index: Int) {
        levelEditorModel.selectSpriteIndex(index)
        _currentSelectedSpriteIndex.value = index
    }

    fun getSpriteEditorModel(): SpriteEditorModel {
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

}