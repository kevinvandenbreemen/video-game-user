package com.vandenbreemen.videogameusr.view.render

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner

/**
 * Runner that has a view of part of the actual rendered space
 */
class RunnerView(private val requirements: GameDataRequirements): Runner(requirements) {

    private var cameraViewStart = Pair(requirements.spriteWidth, requirements.spriteHeight)
    private var cameraViewEnd = Pair(requirements.screenWidth - requirements.spriteWidth, requirements.screenHeight - requirements.spriteHeight)

    override fun newFrame(): DisplayRaster {

        klog(KlogLevel.DEBUG) { "screen width/height = ${requirements.screenWidth}, ${requirements.screenHeight} - Displaying camera view from $cameraViewStart to $cameraViewEnd" }

        return super.newFrame().view(
            cameraViewStart.first,
            cameraViewStart.second,
            cameraViewEnd.first,
            cameraViewEnd.second
        )
    }
}