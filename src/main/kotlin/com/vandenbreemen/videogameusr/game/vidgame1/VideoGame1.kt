package com.vandenbreemen.com.vandenbreemen.videogameusr.game.vidgame1

import com.vandenbreemen.com.vandenbreemen.videogameusr.tools.spriteEditor
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.viddisplayrast.view.swing.SwingRasterRender

fun main() {

    //  64 possible sprites in memory
    val requirements = GameDataRequirements(200, 150, 8, 8, 1024)

    //  Define the ship
    //  Basic array of 8x8 0s
    requirements.setData(0, byteArrayOf(
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 1, 0, 0, 0, 0, 0,
        120, 120, 120, 0, 0, 120, 120, 0,
        0, 120, 120, 120, 120, 0, 0, 120,
        1, 1, 120, 96, 120, 120, 120, 120,
        0, 1, 120, 96, 120, 120, 0, 0,
        1, 120, 120, 120, 120, 0, 0, 0,
        120, 120, 120, 0, 0, 0, 0, 0
    ))



    //  Define the bullet
    //  Basic array of 8x8 0s

    requirements.setData(1, byteArrayOf(
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 100, 0, 0, 0, 0, 0, 0,
        0, 0, 100, 0, 0, 100, 0, 0,
        0, 100, 0, 100, 100, 100, 100, 0,
        0, 100, 0, 100, 100, 100, 100, 0,
        0, 0, 100, 0, 0, 100, 0, 0,
        0, 100, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0
    ))




    //  Define the alien
    //  Basic array of 8x8 0s


    requirements.setData(2, byteArrayOf(
        120, 0, 0, 0, 0, 0, 0, 120,
        0, 120, 0, 100, 100, 0, 120, 0,
        0, 0, 100, 100, 100, 100, 0, 0,
        0, 100, 64, 96, 100, 64, 100, 0,
        100, 100, 100, 100, 100, 100, 100, 100,
        100, 0, 100, 100, 100, 0, 100, 100,
        0, 0, 100, 0, 0, 100, 0, 0,
        0, 100, 0, 0, 0, 0, 100, 0
    ))

    spriteEditor(requirements, 2, "requirements")

    //  Define the alien bullet
    //  Basic array of 8x8 0s
    requirements.setData(3, byteArrayOf(
        //  Just 0s
        1, 0, 0, 1, 1, 0, 0, 1,
        0, 1, 0, 1, 1, 0, 1, 0,
        0, 0, 0, 1, 1, 0, 0, 0,
        0, 1, 1, 0, 0, 1, 1, 0,
        0, 1, 1, 0, 0, 1, 1, 0,
        0, 0, 0, 1, 1, 0, 0, 0,
        0, 1, 0, 1, 1, 0, 1, 0,
        1, 0, 0, 1, 1, 0, 0, 1,
    ).also { // Multiply all values  by 100
        for(i in it.indices){
            it[i] = (it[i] * 200).toByte()
        }
    })

    //  Now set up a screen
    val runner = Runner(requirements)

    //  Test out the sprites
    runner.drawSpriteAt(0, 50, 60)
    runner.drawSpriteAt(1, 80, 60)
    runner.drawSpriteAt(2, 100, 60)
    runner.drawSpriteAt(3, 120, 60)

    //  Show the screen
    SwingRasterRender.showTestRenderWindow(runner.newFrame())


}