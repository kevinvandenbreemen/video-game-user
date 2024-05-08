package com.vandenbreemen.com.vandenbreemen.videogameusr.model

class ScreenInteractor(private val screenWidth: Int, private val screenHeight: Int, private val spriteWidth: Int, private val spriteHeight: Int) {

    fun isInBounds(location: Pair<Int, Int>): Boolean {

        //  Since locations will always have origin at top left
        return location.first in 0..<screenWidth-spriteWidth && location.second in 0..<screenHeight-spriteHeight
    }

}