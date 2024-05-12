package com.vandenbreemen.videogameusr.model

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.videogameusr.model.game.LevelModel

class LevelRenderingInteractor(
    private val requirements: GameDataRequirements,
    private val runner: Runner, private val levelModel: LevelModel) {

    private var cameraLocation = Pair(0, 0)
    private val cameraWidthHeight = Pair(requirements.screenWidth, requirements.screenHeight)

    fun moveCameraRight(){
        cameraLocation = Pair(cameraLocation.first + 1, cameraLocation.second)
    }

    fun moveCameraLeft(){
        cameraLocation = Pair(cameraLocation.first - 1, cameraLocation.second)
    }

    fun moveCameraUp(){
        cameraLocation = Pair(cameraLocation.first, cameraLocation.second - 1)
    }

    fun moveCameraDown(){
        cameraLocation = Pair(cameraLocation.first, cameraLocation.second + 1)
    }

    fun drawFrameBasedOnCameraLocation() {
        val x = cameraLocation.first
        val y = cameraLocation.second
        val width = cameraWidthHeight.first / requirements.spriteWidth
        val height = cameraWidthHeight.second / requirements.spriteHeight

        for(i in 0 until width){
            for(j in 0 until height){
                val spriteTileId = levelModel.getSpriteTileAt(x + i, y + j)
                if(spriteTileId != LevelModel.NO_SPRITE){
                    runner.drawSpriteAt(spriteTileId, i * requirements.spriteWidth, j * requirements.spriteHeight)
                }
            }
        }
    }

}