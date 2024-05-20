package com.vandenbreemen.com.vandenbreemen.videogameusr.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.sp
import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.com.vandenbreemen.videogameusr.model.ColorInteractor
import com.vandenbreemen.viddisplayrast.data.ByteColorDataInteractor
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.ceil

/**
 * [GameConsole] is a composable that will render a game console with a screen and controls
 * [RasterDisplay] - The screen, where the game itself is rendered
 */
object MasterViewComposables {

}

@Composable
fun RasterDisplay(raster: DisplayRaster, controller: VideoGameController) {

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

                val color = controller.getComposeColor(raster.getPixel(
                    x,
                    y
                ))

                drawRect(color, topLeft = Offset(left, top), size = Size(pixelWidthInCanvas, pixelHeightInCanvas))
            }
        }
    }

}

//  Do a simple default implementation of VideoGameController that just shows toasts or something
class DummyVideoGameController : VideoGameController {

    private val colorInteractor = ColorInteractor(ByteColorDataInteractor())

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

    override fun drawFrame(): DisplayRaster {
        klog("Draw Frame")
        return DisplayRaster(16, 16)
    }

    override fun getComposeColor(value: Byte): Color {
        return colorInteractor.getComposeColor(value)
    }
}

@Composable
fun GameConsole(framesPerSecond: Int = 1, controller: VideoGameController) {

    val raster = remember { mutableStateOf(controller.drawFrame()) }
    val controlsModel = remember { ControlsModel() }
    val delayTime = 1000 / framesPerSecond.toLong()

    Column(Modifier.fillMaxSize().background(MaterialTheme.colors.surface), horizontalAlignment = Alignment.CenterHorizontally) {

        //  The "screen"
        Column(Modifier.weight(0.8f).fillMaxSize().clip(MaterialTheme.shapes.medium)) {
            RasterDisplay(raster.value, controller)
        }

        //  The "controls"
        Column(Modifier.weight(.2f).background(MaterialTheme.colors.onSurface).padding(Dimensions.padding)) {

            ControlDeck(
                controlsModel,
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

            delay(delayTime)

            //  Handle user input based on what keys are pressed
            if(controlsModel.isButtonPressed(Button.UP)) controller.moveUp()
            if(controlsModel.isButtonPressed(Button.DOWN)) controller.moveDown()
            if(controlsModel.isButtonPressed(Button.LEFT)) controller.moveLeft()
            if(controlsModel.isButtonPressed(Button.RIGHT)) controller.moveRight()
            if(controlsModel.isButtonPressed(Button.A)) controller.pressA()
            if(controlsModel.isButtonPressed(Button.B)) controller.pressB()

            launch(Dispatchers.Default) {  //  Only do this on the main thread so that it can happen only once
                controller.playTurn()
                raster.value = controller.drawFrame()
            }

        }

    }
}

@Composable
private fun ControlDeck(
    controlsModel: ControlsModel,
    onUp: ()->Unit, onDown: ()->Unit, onLeft: ()->Unit, onRight: ()->Unit, onA: ()->Unit, onB: ()->Unit) {

    val focusRequester = remember { FocusRequester() }
    val buttonStyle = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onSurface, fontSize = 10.sp)

    Row(modifier=Modifier.fillMaxSize()
        .focusRequester(focusRequester)
        .onKeyEvent { keyEvent ->

            when(keyEvent.type) {
                KeyEventType.KeyDown -> {
                    if (keyEvent.key == Key.W) controlsModel.pressButton(Button.UP)
                    if (keyEvent.key ==  Key.S) controlsModel.pressButton(Button.DOWN)
                    if (keyEvent.key == Key.A) controlsModel.pressButton(Button.LEFT)
                    if (keyEvent.key ==  Key.D) controlsModel.pressButton(Button.RIGHT)
                    if (keyEvent.key ==  Key.Spacebar) controlsModel.pressButton(Button.A)
                    if (keyEvent.key ==  Key.ShiftLeft) controlsModel.pressButton(Button.B)
                }
                KeyEventType.KeyUp -> {
                    if (keyEvent.key == Key.W) controlsModel.releaseButton(Button.UP)
                    if (keyEvent.key ==  Key.S) controlsModel.releaseButton(Button.DOWN)
                    if (keyEvent.key == Key.A) controlsModel.releaseButton(Button.LEFT)
                    if (keyEvent.key ==  Key.D) controlsModel.releaseButton(Button.RIGHT)
                    if (keyEvent.key ==  Key.Spacebar) controlsModel.releaseButton(Button.A)
                    if (keyEvent.key ==  Key.ShiftLeft) controlsModel.releaseButton(Button.B)
                }
            }

            //  If we got in here we handled it
            true
    }) {
        //  Buttons

        Column(Modifier.weight(0.3f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Row {
                Spacer(Modifier.weight(0.5f))
                Button(
                    modifier = Modifier.clip(CircleShape),
                    onClick = {
                    onUp()
                }) {
                    Text("\uD83D\uDD3C\uFE0F", style = buttonStyle, modifier = Modifier.clip(CircleShape))
                }
                Spacer(Modifier.weight(0.5f))
            }
            Row {
                Button(
                    modifier = Modifier.clip(CircleShape),
                    onClick = {
                    onLeft()
                }) {
                    Text("◀\uFE0F", style = buttonStyle)
                }
                Spacer(Modifier.weight(0.5f))
                Button(
                    modifier = Modifier.clip(CircleShape),
                    onClick = {
                    onRight()
                }) {
                    Text("\u25B6\uFE0F", style = buttonStyle)
                }
            }
            Row {
                Spacer(Modifier.weight(0.5f))
                Button(
                    modifier = Modifier.clip(CircleShape),
                    onClick = {
                    onDown()
                }) {
                    Text("\uD83D\uDD3D\uFE0F", style = buttonStyle)
                }
                Spacer(Modifier.weight(0.5f))
            }
        }

        Spacer(Modifier.weight(0.3f))

        Column(Modifier.weight(0.5f)) {
            Spacer(Modifier.weight(0.5f))
            Row {
                Button(onClick = {
                    onA()
                }) {
                    Text("A", style = buttonStyle)
                }

                Spacer(Modifier.weight(0.2f))

                Button(onClick = {
                    onB()
                }) {
                    Text("B", style = buttonStyle)
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
    GameConsole( 60, DummyVideoGameController())
}

@Composable
@Preview
fun PreviewRasterDisplay() {
    val raster = DisplayRaster(16, 16)
    raster.setPixel(8, 8, 100)
    raster.setPixel(9, 8, 100)
    RasterDisplay(raster, DummyVideoGameController())
}