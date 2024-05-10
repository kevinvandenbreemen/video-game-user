package com.vandenbreemen.videogameusr.tools.viewmodel

import com.vandenbreemen.videogameusr.tools.model.LevelEditorModel
import com.vandenbreemen.videogameusr.tools.model.SpriteEditorModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LevelEditorViewModel(private val levelEditorModel: LevelEditorModel) {

    private val _currentSelectedSpriteIndex = MutableStateFlow(levelEditorModel.selectedSpriteIndex)
    val currentSelectedSpriteIndexStateFlow = _currentSelectedSpriteIndex.asStateFlow()
    val currentSelectedSpriteIndex: Int get() = levelEditorModel.selectedSpriteIndex

    fun selectSpriteIndex(index: Int) {
        levelEditorModel.selectSpriteIndex(index)
        _currentSelectedSpriteIndex.value = index
    }

    fun getSpriteEditorModel(): SpriteEditorModel {
        return levelEditorModel.getSpriteEditorModel()
    }

}