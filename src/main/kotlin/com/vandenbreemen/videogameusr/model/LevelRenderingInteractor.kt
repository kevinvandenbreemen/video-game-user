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
        //  Ensure camera width doesn't go beyond level width
        if(cameraLocation.first + cameraWidthHeight.first / requirements.spriteWidth >= levelModel.widthInTiles){
            return
        }
        cameraLocation = Pair(cameraLocation.first + 1, cameraLocation.second)
    }

    fun moveCameraLeft(){
        //  Ensure camera width doesn't go beyond level width
        if(cameraLocation.first == 0){
            return
        }
        cameraLocation = Pair(cameraLocation.first - 1, cameraLocation.second)
    }

    fun moveCameraUp(){
        //  Ensure camera height doesn't go beyond level height
        if(cameraLocation.second == 0){
            return
        }
        cameraLocation = Pair(cameraLocation.first, cameraLocation.second - 1)
    }

    fun moveCameraDown(){
        //  Ensure camera height doesn't go beyond level height
        if(cameraLocation.second + cameraWidthHeight.second / requirements.spriteHeight >= levelModel.heightInTiles){
            return
        }
        cameraLocation = Pair(cameraLocation.first, cameraLocation.second + 1)
    }

    fun drawCameraView() {
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