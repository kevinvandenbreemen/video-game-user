package com.vandenbreemen.videogameusr.tools.viewmodel

import com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.videogameusr.log.klog
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

    private val _isCopyingFromAnotherTile = MutableStateFlow(false)
    val isCopyingFromAnotherTile = _isCopyingFromAnotherTile.asStateFlow()

    val canRotateSpriteTiles get() = gameDataEditorModel.canRotateSpriteTiles

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

    fun startCopyFromAnotherTile() {
        _isCopyingFromAnotherTile.value = true
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

        updateSpriteBytes(gameDataEditorModel.getSpriteByteArray())
    }

    fun tapSpriteIndex(index: Int) {
        if(_isCopyingFromAnotherTile.value) {
            copySprite(index)
        }
        else {
            setSpriteIndex(index)
        }
    }

    private fun updateSpriteBytes(byteArray: ByteArray) {
        _spriteArray.value = byteArray

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

    private fun setPixel(x: Int, y: Int, color: Byte) {
        gameDataEditorModel.setPixel(x, y, color)
        updateSpriteBytes(gameDataEditorModel.getSpriteByteArray())
    }

    fun tapPixel(x: Int, y: Int) {
        klog(KlogLevel.DEBUG, "vm Tapping pixel at $x, $y (paintColor=${_paintColor.value})")
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
        updateSpriteBytes(gameDataEditorModel.getSpriteByteArray())
        _isCopyingFromAnotherTile.value = false
    }

    fun fill() {
        gameDataEditorModel.fill(_paintColor.value)
        updateSpriteBytes(gameDataEditorModel.getSpriteByteArray())
    }

    fun rotateClockwise() {
        gameDataEditorModel.rotateClockwise()
        updateSpriteBytes(gameDataEditorModel.getSpriteByteArray())
    }

}