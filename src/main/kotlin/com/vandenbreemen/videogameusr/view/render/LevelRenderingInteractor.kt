package com.vandenbreemen.videogameusr.view.render

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.game.LevelModel

class LevelRenderingInteractor(
    private val requirements: GameDataRequirements,
    private val runner: RunnerView,

    //  TODO    This is silly.
    private val typicalLevelModel: LevelModel) {

    private var cameraLocationToNearestTile = Pair(0, 0)
    private val cameraWidthHeight = Pair(requirements.screenWidth, requirements.screenHeight)

    fun moveCameraRight(){
        //  Ensure camera width doesn't go beyond level width
        if(cameraLocationToNearestTile.first + cameraWidthHeight.first / requirements.spriteWidth >= typicalLevelModel.widthInTiles){
            return
        }
        cameraLocationToNearestTile = Pair(cameraLocationToNearestTile.first + 1, cameraLocationToNearestTile.second)
    }

    fun moveCameraLeft(){
        //  Ensure camera width doesn't go beyond level width
        if(cameraLocationToNearestTile.first == 0){
            return
        }
        cameraLocationToNearestTile = Pair(cameraLocationToNearestTile.first - 1, cameraLocationToNearestTile.second)
    }

    fun moveCameraUp(){
        //  Ensure camera height doesn't go beyond level height
        if(cameraLocationToNearestTile.second == 0){
            return
        }
        cameraLocationToNearestTile = Pair(cameraLocationToNearestTile.first, cameraLocationToNearestTile.second - 1)
    }

    fun moveCameraDown(){
        //  Ensure camera height doesn't go beyond level height
        if(cameraLocationToNearestTile.second + cameraWidthHeight.second / requirements.spriteHeight >= typicalLevelModel.heightInTiles){
            return
        }
        cameraLocationToNearestTile = Pair(cameraLocationToNearestTile.first, cameraLocationToNearestTile.second + 1)
    }

    fun moveCameraTo(x: Int, y: Int){
        cameraLocationToNearestTile = Pair(x, y)
    }

    //  Get the camera location
    fun getCameraLocation(): Pair<Int, Int> {
        return cameraLocationToNearestTile
    }

    fun drawCameraView(levelModel: LevelModel) {
        val x = cameraLocationToNearestTile.first
        val y = cameraLocationToNearestTile.second
        val width = cameraWidthHeight.first / requirements.spriteWidth
        val height = cameraWidthHeight.second / requirements.spriteHeight

        for(i in 0 until width){
            for(j in 0 until height){

                if(x + i < 0 || y + j < 0 || x + i >= levelModel.widthInTiles || y + j >= levelModel.heightInTiles){
                    break
                }

                val spriteTileId = levelModel.getSpriteTileAt(x + i, y + j)
                if(spriteTileId != LevelModel.NO_SPRITE){
                    runner.drawSpriteAt(spriteTileId, i * requirements.spriteWidth, j * requirements.spriteHeight, 0)
                }
            }
        }
    }

    //  Draw the player at the center.  Function takes a 2d array of sprite tiles
    fun drawSinglePlayerCenter(playerSpriteTiles: Array<Array<Int>>) {
        val x = cameraWidthHeight.first / requirements.spriteWidth / 2
        val y = cameraWidthHeight.second / requirements.spriteHeight / 2

        for(i in playerSpriteTiles.indices){
            for(j in playerSpriteTiles[i].indices){
                runner.drawSpriteAt(playerSpriteTiles[i][j],
                    x * requirements.spriteWidth + j * requirements.spriteWidth,
                    y * requirements.spriteHeight + i * requirements.spriteHeight,
                    0
                    )
            }
        }
    }

}