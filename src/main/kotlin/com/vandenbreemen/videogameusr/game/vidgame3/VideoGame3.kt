package com.vandenbreemen.videogameusr.game.vidgame3

import com.vandenbreemen.com.vandenbreemen.videogameusr.tools.gameEditor
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.ProgramEntryPoints
import com.vandenbreemen.viddisplayrast.game.Runner

fun main() {

    val model = VideoGame3Model()
    model.setupLevel()

    gameEditor(model.requirements, 4, "requirements")

    val runner = Runner(model.requirements)
    val controller = VideoGame3Controller(model, runner)
    ProgramEntryPoints.playGameInWindow(runner, controller, 60, 800)

}