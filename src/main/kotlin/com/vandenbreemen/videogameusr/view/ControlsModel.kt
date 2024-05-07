package com.vandenbreemen.com.vandenbreemen.videogameusr.view

import java.util.concurrent.ConcurrentHashMap

enum class Button {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    A,
    B
}

class ControlsModel {

    private val buttonStates = ConcurrentHashMap<Button, Boolean>()
    fun pressButton(button: Button) {
        buttonStates[button] = true
    }

    fun releaseButton(button: Button) {
        buttonStates[button] = false
    }

    fun isButtonPressed(button: Button): Boolean {
        return buttonStates[button] ?: false
    }

}