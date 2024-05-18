package com.vandenbreemen.videogameusr.tools.viewmodel

import com.vandenbreemen.videogameusr.tools.model.GameDataEditorModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SpriteEditorViewModel(private val gameDataEditorModel: GameDataEditorModel) {

    private val _spriteArray = MutableStateFlow(gameDataEditorModel.getSpriteByteArray())
    val spriteArray = _spriteArray.asStateFlow()

    private val _paintColor: MutableStateFlow<Byte> = MutableStateFlow(0)
    val paintColor = _paintColor.asStateFlow()

    fun setPaintColorByte(it: Byte) {
        gameDataEditorModel.paintColor = it
        _paintColor.value = it
    }


}