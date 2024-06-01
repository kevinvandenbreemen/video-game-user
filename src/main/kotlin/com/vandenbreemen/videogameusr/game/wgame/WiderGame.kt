package com.vandenbreemen.videogameusr.game.wgame

import com.vandenbreemen.videogameusr.view.ProgramEntryPoints

//  More open concept game to allow me to experiment with creating more resources of different types using
//  this program
fun main() {

    val model = ExampleWiderGameModel()
    val controller = ExampleWiderGameController(model)

    ProgramEntryPoints.playGameInWindowSNES(controller, 60)

}