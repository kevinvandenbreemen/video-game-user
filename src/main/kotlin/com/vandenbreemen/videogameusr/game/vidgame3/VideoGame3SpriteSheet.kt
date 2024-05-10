package com.vandenbreemen.com.vandenbreemen.videogameusr.game.vidgame3

import com.vandenbreemen.com.vandenbreemen.videogameusr.tools.spriteEditor
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

/**
 * Draft up the sprites for the game
 */
fun game3SpriteSheet(requirements: GameDataRequirements) {


    //  Define the person

    requirements.setData(0, byteArrayOf(
        0, 0, 0, 0, 0, 0, 56, 56,
        0, 0, 0, 0, 56, 56, 56, 56,
        0, 0, 0, 56, 56, 56, 56, -66,
        0, 0, 0, 56, 56, 56, -66, -66,
        0, 0, 56, 56, 56, 56, -66, -66,
        0, 0, 56, 56, 56, 56, -66, -66,
        0, 0, 56, 56, 56, 56, -66, -66,
        0, 0, 56, 56, -66, -66, -66, -66
    ))

    requirements.setData(1, byteArrayOf(
        56, 56, 56, 56, 0, 0, 0, 0,
        -66, -66, -66, -66, 0, 0, 0, 0,
        -66, -66, -66, -66, -66, 0, 0, 0,
        -66, -66, -66, -66, -66, 0, 0, 0,
        -66, -66, -66, 2, 2, -66, 0, 0,
        -66, -66, -66, -66, -66, -66, -66, 0,
        -66, -66, -66, -66, -66, -66, -66, 0,
        -66, -66, -66, -66, -66, -66, 0, 0
    ))


    requirements.setData(3, byteArrayOf(
        0, 0, 56, 56, -66, -66, -66, -66,
        0, 0, 56, 56, -66, -66, -66, -66,
        0, 56, 56, 56, -66, -66, -66, -66,
        56, 56, 56, 0, 0, -66, -66, -66,
        0, 0, 0, 0, 0, -66, -66, -66,
        0, 0, 0, 0, 0, -117, -117, -117,
        0, 0, 0, -117, -117, -117, -117, -117,
        0, 0, -117, -117, -117, -117, -117, -117
    ))



    requirements.setData(4, byteArrayOf(
        -66, -66, -66, -66, -66, -66, 0, 0,
        -66, -66, -66, 0, 0, 0, 0, 0,
        -66, -66, -66, -66, -66, -66, 0, 0,
        -66, -66, -66, -66, -66, 0, 0, 0,
        -66, -66, -66, -66, 0, 0, 0, 0,
        -58, -58, -58, -58, -58, -58, 0, 0,
        -58, -58, -58, -58, -58, -58, 0, 0,
        -58, -58, -58, -58, -58, -58, -58, 0
    ))

    spriteEditor(requirements, 4, "requirements", 800)

}