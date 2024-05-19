package com.vandenbreemen.videogameusr.tools.viewmodel

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.videogameusr.tools.model.GameDataEditorModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest

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

    private val _spriteBytesHashString: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Something to help you propagate updates to unrelated UI components faster without needing to break encapsulation
     */
    val spriteBytesHashString = _spriteBytesHashString.asStateFlow()

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

    private fun updateSpriteBytes(byteArray: ByteArray) {
        _spriteArray.value = byteArray
        _spriteCode.value = gameDataEditorModel.generateSpriteSourceCode()

        //  Update the hash
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(byteArray)
        _spriteBytesHashString.value =  hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun mirrorHorizontal() {
        gameDataEditorModel.mirrorHorizontal()
        updateSpriteBytes(gameDataEditorModel.getSpriteByteArray())
    }

    fun mirrorVertical() {
        gameDataEditorModel.mirrorVertical()
        updateSpriteBytes(gameDataEditorModel.getSpriteByteArray())
    }

    fun clearSprite() {
        gameDataEditorModel.clearSprite()
        updateSpriteBytes(gameDataEditorModel.getSpriteByteArray())
    }

    fun setPixel(x: Int, y: Int, color: Byte) {
        gameDataEditorModel.setPixel(x, y, color)
        updateSpriteBytes(gameDataEditorModel.getSpriteByteArray())
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