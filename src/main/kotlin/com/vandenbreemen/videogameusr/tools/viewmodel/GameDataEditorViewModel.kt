package com.vandenbreemen.videogameusr.tools.viewmodel

import com.vandenbreemen.videogameusr.tools.ToolType
import com.vandenbreemen.videogameusr.tools.model.GameDataEditorModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ToolParameters(val toolType: ToolType, val levelName: String? = null)

class GameDataEditorViewModel(private val gameDataEditorModel: GameDataEditorModel) {

    val _toolParameters: MutableStateFlow<ToolParameters?> = MutableStateFlow(ToolParameters(ToolType.SpriteEditor))
    val toolParameters = _toolParameters.asStateFlow()

    private val _levelNames = MutableStateFlow(gameDataEditorModel.getLevelNames())
    val levelNames = _levelNames.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

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
        val path = gameDataEditorModel.dumpAssetsToFile()
        _message.value = "Assets dumped to $path"
    }

    fun addLevel(levelName: String, widthTiles: Int, heightTiles: Int) {
        try {
            gameDataEditorModel.addLevel(
                levelName,
                widthTiles,
                heightTiles
            )
            _levelNames.value = gameDataEditorModel.getLevelNames()
        } catch (e: IllegalArgumentException) {
            _errorMessage.value = e.message
        }
    }

    fun onErrorDismissed() {
        _errorMessage.value = null
    }

    fun onMessageDismissed() {
        _message.value = null
    }
}