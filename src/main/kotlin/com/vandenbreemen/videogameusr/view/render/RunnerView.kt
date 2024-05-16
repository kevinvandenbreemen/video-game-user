package com.vandenbreemen.videogameusr.view.render

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import java.util.concurrent.Semaphore

/**
 * Runner that has a view of part of the actual rendered space
 */
class RunnerView(private val requirements: GameDataRequirements): Runner(requirements) {

    private var cameraViewStart = Pair(requirements.spriteWidth, requirements.spriteHeight)
    private var cameraViewEnd = Pair(requirements.screenWidth - requirements.spriteWidth, requirements.screenHeight - requirements.spriteHeight)

    private val renderSemaphore = Semaphore(1)

    override fun newFrame(): DisplayRaster {

        try {
            renderSemaphore.acquire()
            klog(KlogLevel.DEBUG) { "screen width/height = ${requirements.screenWidth}, ${requirements.screenHeight} - Displaying camera view from $cameraViewStart to $cameraViewEnd" }

            return super.newFrame().view(
                cameraViewStart.first,
                cameraViewStart.second,
                cameraViewEnd.first,
                cameraViewEnd.second
            )
        } finally {
            renderSemaphore.release()
        }
    }

    /**
     * Move the camera left
     * @return True if the camera was moved, false if it was already at the edge
     */
    fun moveCameraLeft(onFailure: ()->Unit) {
        try {
            renderSemaphore.acquire()
            if (cameraViewStart.first == 0) {
                return
            }
            cameraViewStart = Pair(cameraViewStart.first - 1, cameraViewStart.second)
            cameraViewEnd = Pair(cameraViewEnd.first - 1, cameraViewEnd.second)
        } finally {
            renderSemaphore.release()
        }
    }

    /**
     * Move the camera right
     * @return True if the camera was moved, false if it was already at the edge
     */
    fun moveCameraRight(onFailure: ()->Unit) {
        try {
            renderSemaphore.acquire()
            if(cameraViewEnd.first == requirements.screenWidth - 1){
                return
            }
            cameraViewStart = Pair(cameraViewStart.first + 1, cameraViewStart.second)
            cameraViewEnd = Pair(cameraViewEnd.first + 1, cameraViewEnd.second)
        } finally {
            renderSemaphore.release()
        }
    }

    /**
     * Move the camera up
     * @return True if the camera was moved, false if it was already at the edge
     */
    fun moveCameraUp(onFailure: ()->Unit) {
        try {
            renderSemaphore.acquire()
            klog(KlogLevel.DEBUG) { "Runner - Moving camera up" }
            if (cameraViewStart.second == 0) {
                cameraViewStart = Pair(cameraViewStart.first, requirements.spriteHeight)
                cameraViewEnd = Pair(cameraViewEnd.first, requirements.screenHeight - requirements.spriteHeight)

                klog(KlogLevel.DEBUG) { "Runner - Camera already at top - recentering" }
                onFailure()
                return
            }
            cameraViewStart = Pair(cameraViewStart.first, cameraViewStart.second - 1)
            cameraViewEnd = Pair(cameraViewEnd.first, cameraViewEnd.second - 1)
        } finally {
            renderSemaphore.release()
        }
    }

    /**
     * Move the camera down
     * @return True if the camera was moved, false if it was already at the edge
     */
    fun moveCameraDown(onFailure: ()->Unit) {
        try {
            renderSemaphore.acquire()
            if (cameraViewEnd.second == requirements.screenHeight - 1) {
                return
            }
            cameraViewStart = Pair(cameraViewStart.first, cameraViewStart.second + 1)
            cameraViewEnd = Pair(cameraViewEnd.first, cameraViewEnd.second + 1)
        } finally {
            renderSemaphore.release()
        }
    }


}