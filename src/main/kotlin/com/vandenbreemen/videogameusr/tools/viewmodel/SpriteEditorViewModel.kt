package com.vandenbreemen.videogameusr.tools.viewmodel

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
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

    private val _spriteIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val spriteIndex = _spriteIndex.asStateFlow()

    private val _spriteCode: MutableStateFlow<String> = MutableStateFlow("")
    val spriteCode = _spriteCode.asStateFlow()

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

    fun setSpriteIndex(index: Int) {
        gameDataEditorModel.selectSpriteIndex(index)
        _spriteIndex.value = index
        _spriteArray.value = gameDataEditorModel.getSpriteByteArray()
        _spriteCode.value = gameDataEditorModel.generateSpriteSourceCode()
    }

    fun mirrorHorizontal() {
        gameDataEditorModel.mirrorHorizontal()
        _spriteArray.value = gameDataEditorModel.getSpriteByteArray()
        _spriteCode.value = gameDataEditorModel.generateSpriteSourceCode()
    }

    fun mirrorVertical() {
        gameDataEditorModel.mirrorVertical()
        _spriteArray.value = gameDataEditorModel.getSpriteByteArray()
        _spriteCode.value = gameDataEditorModel.generateSpriteSourceCode()
    }

    fun clearSprite() {
        gameDataEditorModel.clearSprite()
        _spriteArray.value = gameDataEditorModel.getSpriteByteArray()
        _spriteCode.value = gameDataEditorModel.generateSpriteSourceCode()
    }

    fun setPixel(x: Int, y: Int, color: Byte) {
        gameDataEditorModel.setPixel(x, y, color)
        _spriteArray.value = gameDataEditorModel.getSpriteByteArray()
        _spriteCode.value = gameDataEditorModel.generateSpriteSourceCode()
    }

    fun tapPixel(x: Int, y: Int) {
        klog("vm Tapping pixel at $x, $y (paintColor=${_paintColor.value})")
        if(_isErasing.value) {
            setPixel(x, y, 0)
        }
        else if(_isEyeDropping.value) {
            gameDataEditorModel.getPixel(x, y)?.let {
                setPaintColorByte(it)
            }
            setEyeDropping(false)
        }
        else {
            setPixel(x, y, _paintColor.value)
        }
    }

    fun copySprite(from: Int) {
        gameDataEditorModel.copySprite(from)
        _spriteArray.value = gameDataEditorModel.getSpriteByteArray()
        _spriteCode.value = gameDataEditorModel.generateSpriteSourceCode()
    }

    fun fill() {
        gameDataEditorModel.fill(_paintColor.value)
        _spriteArray.value = gameDataEditorModel.getSpriteByteArray()
        _spriteCode.value = gameDataEditorModel.generateSpriteSourceCode()
    }

}