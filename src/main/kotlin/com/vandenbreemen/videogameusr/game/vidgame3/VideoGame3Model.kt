package com.vandenbreemen.com.vandenbreemen.videogameusr.game.vidgame3

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

class VideoGame3Model {

    private val requirements = GameDataRequirements(200, 150, 8, 8, 16*50)

    init {
        game3SpriteSheet(requirements)
    }



}