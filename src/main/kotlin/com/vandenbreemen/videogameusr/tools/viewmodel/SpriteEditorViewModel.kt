package com.vandenbreemen.videogameusr.tools.viewmodel

import com.vandenbreemen.videogameusr.tools.model.GameDataEditorModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SpriteEditorViewModel(private val gameDataEditorModel: GameDataEditorModel) {

    private val _spriteArray = MutableStateFlow(gameDataEditorModel.getSpriteByteArray())
    val spriteArray = _spriteArray.asStateFlow()

    private val _paintColor: MutableStateFlow<Byte> = MutableStateFlow(0)
    val paintColor = _paintColor.asStateFlow()

    private val _isErasing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isErasing = _isErasing.asStateFlow()

    private val _isEyeDropping: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEyeDropping = _isEyeDropping.asStateFlow()

    fun setPaintColorByte(it: Byte) {
        gameDataEditorModel.paintColor = it
        _paintColor.value = it
    }

    fun toggleErasing() {
        _isErasing.value = !_isErasing.value
    }

    fun setErasing(isErasing: Boolean) {
        _isErasing.value = isErasing
    }

    fun setEyeDropping(isEyeDropping: Boolean) {
        _isEyeDropping.value = isEyeDropping
    }

    fun toggleEyeDropping() {
        _isEyeDropping.value = !_isEyeDropping.value
    }

}