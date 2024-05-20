package com.vandenbreemen.videogameusr.game.vidgame4

import com.vandenbreemen.com.vandenbreemen.videogameusr.view.ProgramEntryPoints
import com.vandenbreemen.videogameusr.view.render.RunnerView

fun main() {

    val model = VideoGame4Model()
    model.load()

    val runnerView = RunnerView(model.requirements)

    //model.edit()

    ProgramEntryPoints.playGameInWindow(VideoGame4Controller(model, runnerView), 60, 800)

}