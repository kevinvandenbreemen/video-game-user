package com.vandenbreemen.videogameusr.tools.viewmodel

import com.vandenbreemen.videogameusr.tools.model.GameDataEditorModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameDataEditorViewModel(private val gameDataEditorModel: GameDataEditorModel) {


    val _levelName: MutableStateFlow<String?> = if(gameDataEditorModel.getLevelNames().isNotEmpty()) MutableStateFlow(gameDataEditorModel.getLevelNames()[0]) else MutableStateFlow(null)
    val levelName = _levelName.asStateFlow()


    fun getLevelNames(): List<String> {
        return gameDataEditorModel.getLevelNames()
    }

    fun selectLevelForEdit(levelName: String) {
        _levelName.value = levelName
    }
}