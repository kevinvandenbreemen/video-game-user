package com.vandenbreemen.com.vandenbreemen.videogameusr.model

import androidx.compose.ui.graphics.Color
import com.vandenbreemen.viddisplayrast.data.ByteColorDataInteractor

class ColorInteractor(private val byteColorDataInteractor: ByteColorDataInteractor = ByteColorDataInteractor()) {

    fun getComposeColor(colorByte: Byte): Color {

        if(colorByte.toInt() == 0) {
            return Color(0, 0, 0, 0,)
        }

        val redRaw = byteColorDataInteractor.getRed(colorByte)
        val greenRaw = byteColorDataInteractor.getGreen(colorByte)
        val blueRaw = byteColorDataInteractor.getBlue(colorByte)

        val red = redRaw.toFloat() / 255
        val green = greenRaw.toFloat() / 255
        val blue = blueRaw.toFloat() / 255

        return Color(red, green, blue)
    }

}