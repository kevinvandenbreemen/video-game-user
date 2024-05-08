package com.vandenbreemen.com.vandenbreemen.videogameusr.game.videogame2

import com.vandenbreemen.com.vandenbreemen.videogameusr.tools.spriteEditor
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

fun main() {

    val requirement = GameDataRequirements(200, 150, 16, 16, 1024)

    //  First sprite:  The player, standing still
    //  16x16 array of bytes

    requirement.setData(0, byteArrayOf(
        0, 0, 0, 0, 0, 0, 0, -75, -75, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 121, -75, -75, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 121, 121, -75, -75, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 121, 121, 121, -75, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 25, 121, 121, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, -71, -71, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 32, 0, 32, 32, 32, -96, 0, 32, 0, 0, 0, 0,
        0, 0, 0, 0, 32, 0, 0, 32, 32, 0, 0, 32, 0, 0, 0, 0,
        0, 0, 0, 0, 32, 0, 32, 32, 32, 35, 0, 32, 0, 0, 0, 0,
        0, 0, 0, 0, 32, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 3, 0, 0, 3, 3, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 63, 63, 0, 0, -65, 63, 63, 0, 0, 0, 0
    ))

    spriteEditor(requirement, 0, "requirement", 800)

}