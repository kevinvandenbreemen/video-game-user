package com.vandenbreemen

import com.vandenbreemen.com.vandenbreemen.videogameusr.view.ComposeRasterRender
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner

fun main() {
    println("Hello World!")

    val requirement =
        GameDataRequirements(200, 150, 8, 8, 1024)
    requirement.setData(
        0, byteArrayOf(
            0, 0, 100, 100, 100, 0, 0, 0,
            0, 0, 100, 127, 100, 127, 0, 0,
            0, 0, 100, 100, 100, 100, 0, 0,
            0, 100, 80, 80, 80, 100, 100, 100,
            0, 100, 100, 100, 80, 100, 0, 100,
            100, 100, 100, 100, 100, 100, 0, 0,
            100, 100, 100, 100, 100, 100, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
        )
    )

    val runner = Runner(requirement)
    runner.newFrame()
    //  Draw the first sprite asset at coord 200, 100 on the screen
    runner.drawSpriteAt(0, 50, 60)


    ComposeRasterRender.showTestRenderWindow(runner.newFrame())
}