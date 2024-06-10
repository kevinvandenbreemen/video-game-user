package com.vandenbreemen.videogameusr.game.talkgame

fun main() {
    val model = TalkGameModel()

    //  Future project:  Game with some dialog in it to get used to building dialog boxes
//    val runnerView = RunnerView(model.requirements)

    model.edit()

//    ProgramEntryPoints.playGameInWindow(TalkGameController(model, runnerView), 60, 800,
//        (model.requirements.screenHeight.toFloat() / model.requirements.screenWidth.toFloat()))
}