package com.vandenbreemen.videogameusr.game.wgame.data

import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.model.game.assetmgt.GameAssetsInteractor
import com.vandenbreemen.videogameusr.tools.gameEditor
import com.vandenbreemen.videogameusr.view.render.LevelPrerenderInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Cave() {

    //  TODO    Create some kind of factory that makes these with the same dimensions and stuff
    val requirements = CoreDependenciesHelper.createSNESRequirements(100)

    private val caveWorld = TileBasedGameWorld(requirements)

    val widthInTiles: Int get() = caveWorld.getLevel(caveWorld.getLevelNames()[0]).widthInTiles
    val heightInTiles: Int get() = caveWorld.getLevel(caveWorld.getLevelNames()[0]).heightInTiles

    val levelPrerenderInteractor: LevelPrerenderInteractor by lazy {
        LevelPrerenderInteractor(requirements, widthInTiles * requirements.spriteWidth, heightInTiles * requirements.spriteHeight)
    }


    fun load() {
        GameAssetsInteractor().loadAssetsFromClasspath("/assets/games/wider/cave.dat", requirements, caveWorld)
        CoroutineScope(CoreDependenciesHelper.getIODispatcher()).launch {
            CoreDependenciesHelper.getImageImportInteractor().importImageAsRasterFromClasspath("/assets/games/wider/cave_bg.png").let {

                klog(KlogLevel.DEBUG,"Loaded background raster:  ${it.xDim} x ${it.yDim}")

                levelPrerenderInteractor.renderRaster(it)

                levelPrerenderInteractor.renderLevel(caveWorld.getLevel(caveWorld.getLevelNames()[0]))
            }
        }
    }

    fun edit() {
        gameEditor(requirements, caveWorld, 0, "requirements")
    }

    /**
     * Render this place given the rendering interactor
     */
    fun render(): DisplayRaster {
        return levelPrerenderInteractor.renderCameraView()
    }

}

fun main() {
    val cave = Cave()
    cave.load()
    cave.edit()
}