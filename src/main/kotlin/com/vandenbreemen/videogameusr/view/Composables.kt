package com.vandenbreemen.com.vandenbreemen.videogameusr.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import kotlinx.coroutines.delay
import kotlin.math.ceil

@Composable
fun RasterDisplay(raster: DisplayRaster) {

//  Render a white square
    Canvas(modifier=Modifier.fillMaxSize()) {

        //  Display all the pixels:

        val width = size.width
        val height = size.height

        val pixelWidthInCanvas = ceil((width / raster.xDim).toDouble()).toFloat()
        val pixelHeightInCanvas = ceil((height / raster.yDim).toDouble()).toFloat()

        for (y in 0 until raster.yDim) {
            for (x in 0 until raster.xDim) {
                val left = x * pixelWidthInCanvas
                val top = y * pixelHeightInCanvas

                val pixelColor = raster.getPixel(
                    x,
                    y
                ).toInt()
                val color = Color(pixelColor, pixelColor, pixelColor)

                drawRect(color, topLeft = Offset(left, top), size = Size(pixelWidthInCanvas, pixelHeightInCanvas))
            }
        }
    }

}

//  Do a simple default implementation of VideoGameController that just shows toasts or something
class DummyVideoGameController : VideoGameController {
    override fun moveRight() {
        klog("Move Right")
    }

    override fun moveLeft() {
        klog("Move Left")
    }

    override fun moveUp() {
        klog("Move Up")
    }

    override fun moveDown() {
        klog("Move Down")
    }

    override fun pressA() {
        klog("Press A")
    }

    override fun pressB() {
        klog("Press B")
    }

    override fun playTurn() {
        klog("Play Turn")
    }

    override fun drawFrame() {
        klog("Draw Frame")
    }
}

@Composable
fun GameConsole(runner: Runner, framesPerSecond: Int = 60, controller: VideoGameController) {

    var raster = remember { mutableStateOf(runner.newFrame()) }

    Column(Modifier.background(Color.Gray).fillMaxSize().padding(10.dp)) {

        //  The "screen"
        Column(Modifier.weight(0.6f).padding(5.dp)) {
            RasterDisplay(raster.value)
            //Text("The Screen", Modifier.padding(2.dp).background(Color.White))
        }

        //  The "controls"
        Column(Modifier.weight(.4f).background(Color.Green).padding(2.dp)) {

            Text("Controls", Modifier.padding(2.dp).background(Color.White))

            ControlDeck(
                onUp = {
                    controller.moveUp()
                },
                onDown = {
                    controller.moveDown()
                },
                onLeft = {
                    controller.moveLeft()
                },
                onRight = {
                    controller.moveRight()
                },
                onA = {
                    controller.pressA()
                },
                onB = {
                    controller.pressB()
                }
            )

        }


    }

    //  Now start the game loop
    LaunchedEffect(Unit) {
        while(true) {
            delay(1000 / framesPerSecond.toLong())
            controller.playTurn()
            controller.drawFrame()
            raster.value = runner.newFrame()
        }

    }
}

@Composable
private fun ControlDeck(onUp: ()->Unit, onDown: ()->Unit, onLeft: ()->Unit, onRight: ()->Unit, onA: ()->Unit, onB: ()->Unit) {

    val focusRequester = remember { FocusRequester() }

    Row(modifier=Modifier
        .focusRequester(focusRequester)
        .onKeyEvent { keyEvent ->
            if(keyEvent.type != KeyEventType.KeyDown) { //  Don't handle the event if the user is just releasing the key
                return@onKeyEvent false
            }
        when(keyEvent.key) {
            Key.W -> {
                onUp()
                return@onKeyEvent true
            }
            Key.S -> {
                onDown()
                return@onKeyEvent true
            }
            Key.A -> {
                onLeft()
                return@onKeyEvent true
            }
            Key.D -> {
                onRight()
                return@onKeyEvent true
            }
            Key.Spacebar -> {
                onA()
                return@onKeyEvent true
            }
            Key.ShiftLeft -> {
                onB()
                return@onKeyEvent true
            }
        }
        false
    }) {
        //  Buttons
        Column(Modifier.weight(0.5f)) {
            Spacer(Modifier.weight(0.5f))
            Row {
                Spacer(Modifier.weight(0.5f))
                Button(onClick = {
                    onUp()
                }) {
                    Text("\uD83D\uDD3C\uFE0F")
                }
                Spacer(Modifier.weight(0.5f))
            }
            Row {
                Button(onClick = {
                    onLeft()
                }) {
                    Text("◀\uFE0F")
                }
                Spacer(Modifier.weight(0.5f))
                Button(onClick = {
                    onRight()
                }) {
                    Text("\u25B6\uFE0F")
                }
            }
            Row {
                Spacer(Modifier.weight(0.5f))
                Button(onClick = {
                    onDown()
                }) {
                    Text("\uD83D\uDD3D\uFE0F")
                }
                Spacer(Modifier.weight(0.5f))
            }
            Spacer(Modifier.weight(0.5f))
        }

        Spacer(Modifier.weight(0.3f))
        Text("Score: 0", Modifier.weight(0.2f).background(Color.White))
        Spacer(Modifier.weight(0.3f))

        Column(Modifier.weight(0.5f)) {
            Spacer(Modifier.weight(0.5f))
            Row {
                Button(onClick = {
                    onA()
                }) {
                    Text("A")
                }

                Spacer(Modifier.weight(0.2f))

                Button(onClick = {
                    onB()
                }) {
                    Text("B")
                }
            }
            Spacer(Modifier.weight(0.5f))
        }
    }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }
}

@Composable
@Preview
fun PreviewGameConsole() {

    val requirements = GameDataRequirements(100, 75, 8, 8, 1024)
    requirements.setData(0, byteArrayOf(
        0, 0, 100, 100, 100, 0, 0, 0,
        0, 0, 100, 127, 100, 127, 0, 0,
        0, 0, 100, 100, 100, 100, 0, 0,
        0, 100, 80, 80, 80, 100, 100, 100,
        0, 100, 100, 100, 80, 100, 0, 100,
        100, 100, 100, 100, 100, 100, 0, 0,
        100, 100, 100, 100, 100, 100, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0
    ))

    //  Draw pacman sprite
    requirements.setData(1, byteArrayOf(
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 1, 0, 0, 0, 0, 0,
        120, 120, 120, 0, 0, 120, 120, 0,
        0, 120, 120, 120, 120, 0, 0, 120,
        1, 1, 120, 96, 120, 120, 120, 120,
        0, 1, 120, 96, 120, 120, 0, 0,
        1, 120, 120, 120, 120, 0, 0, 0,
        120, 120, 120, 0, 0, 0, 0, 0
    ))

    val runner = Runner(requirements)
    runner.drawSpriteAt(0, 50, 60)
    //runner.drawSpriteAt(1, 100, 100)
    GameConsole(runner, 60, DummyVideoGameController())
}

@Composable
@Preview
fun PreviewRasterDisplay() {
    val raster = DisplayRaster(16, 16)
    raster.setPixel(8, 8, 100)
    raster.setPixel(9, 8, 100)
    RasterDisplay(raster)
}