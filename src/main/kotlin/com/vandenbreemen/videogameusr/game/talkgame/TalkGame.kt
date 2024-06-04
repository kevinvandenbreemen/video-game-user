package com.vandenbreemen.videogameusr.game.talkgame

fun main() {
    val model = TalkGameModel()

//    val runnerView = RunnerView(model.requirements)

    model.edit()

//    ProgramEntryPoints.playGameInWindow(TalkGameController(model, runnerView), 60, 800,
//        (model.requirements.screenHeight.toFloat() / model.requirements.screenWidth.toFloat()))
}