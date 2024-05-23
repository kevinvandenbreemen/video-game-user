package com.vandenbreemen.videogameusr.view.render

import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.videogameusr.log.klog
import java.util.concurrent.Semaphore

/**
 * Runner that has a view of part of the actual rendered space
 */
class RunnerView(private val requirements: GameDataRequirements): Runner(requirements) {

    private var cameraViewStart = Pair(requirements.spriteWidth, requirements.spriteHeight)
    private var cameraViewEnd = Pair(requirements.screenWidth - requirements.spriteWidth, requirements.screenHeight - requirements.spriteHeight)

    private val renderSemaphore = Semaphore(1)

    private var cameraIncrement = 1

    /**
     * Gets the camera's offset from the center of the screen
     */
    fun getCameraOffset(): Pair<Int, Int> {
        return Pair(requirements.spriteWidth - cameraViewStart.first, requirements.spriteHeight - cameraViewStart.second)
    }

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
            if (cameraViewStart.first == 0 || cameraViewStart.first - cameraIncrement < 0) {

                cameraViewStart = Pair(requirements.spriteWidth, cameraViewStart.second)
                cameraViewEnd = Pair(requirements.screenWidth - requirements.spriteWidth, cameraViewEnd.second)

                onFailure()

                return
            }
            cameraViewStart = Pair(cameraViewStart.first - cameraIncrement, cameraViewStart.second)
            cameraViewEnd = Pair(cameraViewEnd.first - cameraIncrement, cameraViewEnd.second)
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
            if(cameraViewEnd.first == requirements.screenWidth - 1 || cameraViewEnd.first + cameraIncrement >= requirements.screenWidth - 1){

                cameraViewStart = Pair(requirements.spriteWidth, cameraViewStart.second)
                cameraViewEnd = Pair(requirements.screenWidth - requirements.spriteWidth, cameraViewEnd.second)

                onFailure()

                return
            }
            cameraViewStart = Pair(cameraViewStart.first + cameraIncrement, cameraViewStart.second)
            cameraViewEnd = Pair(cameraViewEnd.first + cameraIncrement, cameraViewEnd.second)
        } finally {
            renderSemaphore.release()
        }
    }

    fun setCameraIncrement(increment: Int){
        cameraIncrement = increment
    }

    /**
     * Move the camera up
     * @return True if the camera was moved, false if it was already at the edge
     */
    fun moveCameraUp(onFailure: ()->Unit) {
        try {
            renderSemaphore.acquire()
            if (cameraViewStart.second == 0 || cameraViewStart.second - cameraIncrement < 0) {
                cameraViewStart = Pair(cameraViewStart.first, requirements.spriteHeight)
                cameraViewEnd = Pair(cameraViewEnd.first, requirements.screenHeight - requirements.spriteHeight)

                onFailure()
                return
            }
            cameraViewStart = Pair(cameraViewStart.first, cameraViewStart.second - cameraIncrement)
            cameraViewEnd = Pair(cameraViewEnd.first, cameraViewEnd.second - cameraIncrement)
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
            if (cameraViewEnd.second == requirements.screenHeight - 1 || cameraViewEnd.second + cameraIncrement >= requirements.screenHeight - 1) {

                cameraViewStart = Pair(cameraViewStart.first, requirements.spriteHeight)
                cameraViewEnd = Pair(cameraViewEnd.first, requirements.screenHeight - requirements.spriteHeight)

                onFailure()

                return
            }
            cameraViewStart = Pair(cameraViewStart.first, cameraViewStart.second + cameraIncrement)
            cameraViewEnd = Pair(cameraViewEnd.first, cameraViewEnd.second + cameraIncrement)
        } finally {
            renderSemaphore.release()
        }
    }


}