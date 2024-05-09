package com.vandenbreemen.com.vandenbreemen.videogameusr.game.controller

import com.vandenbreemen.com.vandenbreemen.videogameusr.game.vidgame1.VideoGame1Controller
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.ProgramEntryPoints
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner

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
        -125, 0, 0, 0, 0, 0, 0, -125,
        0, 120, 0, 0, 0, 0, 120, 0,
        0, 0, 100, -16, -16, 100, 0, 0,
        0, 100, 64, -52, -52, 64, 100, 0,
        100, 100, 12, 12, 12, 100, 100, 100,
        12, 0, 12, 12, 12, 0, 12, 12,
        0, 0, 12, 0, 0, 12, 0, 0,
        0, 12, 0, 0, 0, 0, 12, 0
    ))
    //spriteEditor(requirements, 2, "requirements", 800)



    //  Define the alien bullet
    //  Basic array of 8x8 0s
    requirements.setData(3, byteArrayOf(
        1, 0, 0, 1, 1, 0, 0, 1,
        0, 1, 0, 1, 1, 120, 1, 0,
        0, 0, 0, 1, 1, 120, 0, 0,
        0, 1, 120, 120, 120, 1, 48, 48,
        0, 1, 120, 120, 120, 1, 48, 48,
        0, 0, 0, 1, 1, 120, 0, 0,
        0, 1, 0, 1, 1, 120, 1, 0,
        1, 0, 0, 1, 1, 0, 0, 1
    ))

    //  Define an explosion
    //  Basic array of 8x8 0s

    requirements.setData(4, byteArrayOf(
        0, 0, 0, 24, 0, 0, 0, 0,
        0, 40, 0, 0, 0, 72, 16, 0,
        0, 0, 120, 72, 120, 0, 104, 0,
        0, 0, 0, 120, 0, 0, 0, 0,
        40, 0, 112, 120, 96, 0, 112, 0,
        0, 0, 0, 0, 112, 0, 0, 0,
        0, 0, 40, 72, 0, 96, 0, 0,
        0, 72, 0, 0, 104, 0, 0, 72
    ))

    //  Explosion 2

    requirements.setData(5, byteArrayOf(
        0, 0, 0, 24, 0, 0, 0, 0,
        0, 40, 0, 0, 0, 72, 32, 0,
        32, 0, 120, 72, 120, 0, 32, 0,
        0, 32, 0, 48, 48, 0, 0, 0,
        40, 0, 64, 48, 48, 0, 112, 0,
        0, 0, 0, 48, 112, 0, 0, 0,
        0, 0, 40, 72, 0, 96, 32, 0,
        0, 72, 0, 0, 104, 0, 0, 32
    ))

    //  Explosion frame 3

    requirements.setData(6, byteArrayOf(
        0, 0, 64, 24, 0, 0, 0, 80,
        64, 40, 0, 0, 64, 72, 32, 0,
        32, 0, 0, 40, 0, 0, 0, 0,
        0, 32, 0, 16, 16, 0, 0, 0,
        40, 0, 0, 16, 16, 0, 112, 0,
        64, 0, 0, 16, 0, 0, 0, 80,
        0, 0, 40, 0, 0, 0, 32, 80,
        80, 72, 0, 0, 0, 0, 80, 32
    ))

    //spriteEditor(requirements, 6, "requirements", 800)


    //  Now set up a screen
    val runner = Runner(requirements)

    //  Test out the sprites
//    runner.drawSpriteAt(0, 50, 60)
//    runner.drawSpriteAt(1, 80, 60)
//    runner.drawSpriteAt(2, 100, 60)
//    runner.drawSpriteAt(3, 120, 60)

    //  Show the screen
    //SwingRasterRender.showTestRenderWindow(runner.newFrame())
    //ComposeRasterRender.showTestRenderWindow(runner.newFrame())

    //  Now set up a game
    val controller = VideoGame1Controller(runner, requirements)
    ProgramEntryPoints.playGameInWindow(runner, controller, 120, 800)
    //  Draw the screen



}