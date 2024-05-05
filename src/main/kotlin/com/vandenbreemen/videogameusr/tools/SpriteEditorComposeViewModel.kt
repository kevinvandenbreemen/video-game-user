package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import androidx.compose.runtime.mutableStateOf

/**
 * View model to make it easier to work with compose states.
 */
class SpriteEditorComposeViewModel(private val model: SpriteEditorModel) {

    var spriteArray = mutableStateOf(model.getSpriteByteArray())
    fun setPixel(x: Int, y: Int, value: Byte){
        model.setPixel(x, y, value)
        spriteArray.value = model.getSpriteByteArray()
    }

}