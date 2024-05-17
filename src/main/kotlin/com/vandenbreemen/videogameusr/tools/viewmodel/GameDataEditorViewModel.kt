package com.vandenbreemen.videogameusr.tools.viewmodel

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.com.vandenbreemen.videogameusr.tools.ToolType
import com.vandenbreemen.videogameusr.tools.model.GameDataEditorModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ToolParameters(val toolType: ToolType, val levelName: String? = null)

class GameDataEditorViewModel(private val gameDataEditorModel: GameDataEditorModel) {

    val _toolParameters: MutableStateFlow<ToolParameters?> = MutableStateFlow(ToolParameters(ToolType.SpriteEditor))
    val toolParameters = _toolParameters.asStateFlow()


    fun getLevelNames(): List<String> {
        return gameDataEditorModel.getLevelNames()
    }

    fun selectLevelForEdit(levelName: String) {
        _toolParameters.value = ToolParameters(ToolType.LevelEditor, levelName)
    }

    fun editSprites() {
        _toolParameters.value = ToolParameters(ToolType.SpriteEditor)
    }

    fun dumpAssetsToFile() {
        gameDataEditorModel.dumpAssetsToFile()
    }

    fun addLevel(levelName: String) {
        klog("Create level name=$levelName")
    }
}