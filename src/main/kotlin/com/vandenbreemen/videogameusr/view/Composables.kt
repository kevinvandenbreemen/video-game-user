package com.vandenbreemen.videogameusr.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
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
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vandenbreemen.com.vandenbreemen.videogameusr.controller.VideoGameController
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.videogameusr.view.render.CanvasRasterRender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * [GameConsole] is a composable that will render a game console with a screen and controls
 * [RasterDisplay] - The screen, where the game itself is rendered
 */
object MasterViewComposables {

}

@Composable
fun RasterDisplay(raster: DisplayRaster, canvasRasterRender: CanvasRasterRender) {

//  Render a white square
    Canvas(modifier=Modifier.fillMaxSize()) {
        canvasRasterRender.run {
            setCurrentDrawScope(this@Canvas)
            renderRaster(raster)
        }
    }

}

@Composable
fun GameConsole(framesPerSecond: Int = 1, controller: VideoGameController) {

    val raster = remember { mutableStateOf(controller.drawFrame()) }
    val controlsModel = remember { ControlsModel() }
    val delayTime = 1000 / framesPerSecond.toLong()

    val canvasRasterRender = CanvasRasterRender()

    Column(Modifier.fillMaxSize().background(MaterialTheme.colors.surface), horizontalAlignment = Alignment.CenterHorizontally) {

        //  The "screen"
        Card(modifier=Modifier.weight(0.8f).padding(Dimensions.padding), elevation = Dimensions.elevation) {
            Column(Modifier.fillMaxSize().clip(MaterialTheme.shapes.medium)) {
                RasterDisplay(raster.value, canvasRasterRender)
            }
        }

        //  The "controls"
        Card (modifier=Modifier.weight(0.2f).padding(Dimensions.padding).background(MaterialTheme.colors.surface).clip(MaterialTheme.shapes.medium),
            elevation = Dimensions.elevation
        ) {
            Column(Modifier.background(MaterialTheme.colors.surface).padding(Dimensions.padding).fillMaxSize()) {

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
        Card(modifier = Modifier.background(MaterialTheme.colors.surface).weight(0.2f).clip(CircleShape)
            , elevation = Dimensions.elevation
        ) {
            Column(
                Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Spacer(Modifier.weight(0.5f))

                    CircleButton("\uD83D\uDD3C\uFE0F") {
                        onUp()
                    }


                    Spacer(Modifier.weight(0.5f))
                }
                Row {

                    CircleButton("\u25C0\uFE0F") {
                        onLeft()
                    }
                    Spacer(Modifier.width(10.dp))

                    CircleButton("\u25B6\uFE0F") {
                        onRight()
                    }
                }
                Row {
                    Spacer(Modifier.weight(0.5f))

                    CircleButton("\uD83D\uDD3D\uFE0F") {
                        onDown()
                    }
                    Spacer(Modifier.weight(0.5f))
                }
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
fun CircleButton(text: String, onClick: ()->Unit) {
    Card(elevation = Dimensions.elevation, modifier = Modifier.size(25.dp).clip(CircleShape).background(MaterialTheme.colors.background).clickable {
        onClick()
    }) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text, style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.background, fontSize = 12.sp), modifier = Modifier.clip(CircleShape)
                .background(MaterialTheme.colors.background)
            )
        }

    }
}

@Preview
@Composable
fun PreviewCircleButton() {
    VideoGameUserTheme {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.secondary)) {
            CircleButton("\uD83D\uDD3C\uFE0F") {  }
        }


    }
}

@Composable
@Preview
fun PreviewRasterDisplay() {
    val raster = DisplayRaster(16, 16)
    raster.setPixel(8, 8, 100)
    raster.setPixel(9, 8, 100)
    RasterDisplay(raster, CanvasRasterRender())
}