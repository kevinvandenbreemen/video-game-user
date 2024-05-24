package com.vandenbreemen.videogameusr.game.vidgame4

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.DelayedSwitch
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.model.game.assetmgt.GameAssetsInteractor
import com.vandenbreemen.videogameusr.tools.gameEditor

class VideoGame4Model {

    //  100 possible sprite tiles
    //  SNES style
    val requirements = GameDataRequirements(256, 224, 8, 8, 6400 )

    val cameraIncrementInGame = 2

    private var blinkFrame = 0
    private val nextFrameSwitch = DelayedSwitch(100)
    private val blinkerFrames = listOf(
        listOf(12, 13, 16, 17),
        listOf(14, 15, 18, 19),
        listOf(20, 21, 24, 25),
        listOf(22, 23, 26, 27),
        listOf(20, 21, 24, 25),
        listOf(14, 15, 18, 19),
        listOf(12, 13, 16, 17),
    )

    private val world = TileBasedGameWorld(requirements)

    /**
     * The actual level that stores the majority of the objects in the game
     */
    val level by lazy {  world.getLevel("Main Background") }

    val allLevelsInRenderingOrder by lazy { listOf(
        world.getLevel("Floor Tiles"),
        world.getLevel("Main Background")
    ) }

    /**
     * Triggers the game editor for the game.  Use only for development purposes
     */
    fun edit() {
        gameEditor(requirements, world, 0, "requirements")
    }


    fun takeTurn() {

        //  Handle the blinking animation of the thing in the middle
        world.getLevel("Main Background").apply {

            //  top corner:  14, 22 -> 15, 23

            if(nextFrameSwitch.trigger()) {
                blinkFrame = (blinkFrame + 1) % blinkerFrames.size
                blinkerFrames[blinkFrame].let { tileSet->
                    setSpriteTileAt(14, 22, tileSet[0])
                    setSpriteTileAt(15, 22, tileSet[1])
                    setSpriteTileAt(14, 23, tileSet[2])
                    setSpriteTileAt(15, 23, tileSet[3])
                }

            }
        }
    }

    fun load() {
        GameAssetsInteractor().loadAssetsFromClasspath("/assets/games/game4.dat", requirements, world)
    }

}