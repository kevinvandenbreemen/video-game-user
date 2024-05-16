package com.vandenbreemen.videogameusr.game.vidgame3

import com.vandenbreemen.com.vandenbreemen.videogameusr.view.ProgramEntryPoints
import com.vandenbreemen.videogameusr.view.render.RunnerView

fun main() {

    val model = VideoGame3Model()
    model.setupLevel()

    //gameEditor(model.requirements, model.tileBasedGameWorld, 4, "requirements")

    val runner = RunnerView(model.requirements)
    val controller = VideoGame3Controller(model, runner)
    ProgramEntryPoints.playGameInWindow(controller, 60, 800)

}