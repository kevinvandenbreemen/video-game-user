package com.vandenbreemen.com.vandenbreemen.videogameusr.game.vidgame3

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

class VideoGame3Model {

    private val requirements = GameDataRequirements(200, 150, 8, 8, (8*8)*100)

    init {
        game3SpriteSheet(requirements)
    }



}