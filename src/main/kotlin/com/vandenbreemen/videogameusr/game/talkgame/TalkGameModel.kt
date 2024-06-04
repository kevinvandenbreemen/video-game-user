package com.vandenbreemen.videogameusr.game.talkgame

import com.vandenbreemen.videogameusr.game.talkgame.data.TalkGameWorld
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.game.LevelModel

class TalkGameModel {

    private val requirements = CoreDependenciesHelper.createSNESRequirements(100)
    private val world = TalkGameWorld().also { it.load(requirements) }

    fun edit() {
        world.edit(requirements)
    }

    fun drawFrame() {

    }

    fun getLevelsForRender(): List<LevelModel> {
        return emptyList()
    }

}