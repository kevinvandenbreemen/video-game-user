package com.vandenbreemen.videogameusr.tools.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.vandenbreemen.videogameusr.tools.model.LevelEditorModel
import com.vandenbreemen.videogameusr.tools.model.SpriteEditorModel

class LevelEditorViewModel(private val levelEditorModel: LevelEditorModel) {

    val currentSelectedSpriteIndex = mutableStateOf(levelEditorModel.selectedSpriteIndex)
    fun selectSpriteIndex(index: Int) {
        levelEditorModel.selectSpriteIndex(index)
        currentSelectedSpriteIndex.value = index
    }

    fun getSpriteEditorModel(): SpriteEditorModel {
        return levelEditorModel.getSpriteEditorModel()
    }

}