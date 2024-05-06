package com.vandenbreemen.com.vandenbreemen.videogameusr.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vandenbreemen.viddisplayrast.data.DisplayRaster
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.viddisplayrast.game.Runner
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

@Composable
fun GameConsole(runner: Runner) {
    val raster = runner.newFrame()

    Column(Modifier.background(Color.Gray).fillMaxSize().padding(10.dp)) {

        //  The "screen"
        Column(Modifier.weight(0.6f).padding(5.dp)) {
            RasterDisplay(raster)
            //Text("The Screen", Modifier.padding(2.dp).background(Color.White))
        }

        //  The "controls"
        Column(Modifier.weight(.4f).background(Color.Green).padding(2.dp)) {

            Text("Controls", Modifier.padding(2.dp).background(Color.White))

            ControlDeck(
                onUp = {

                },
                onDown = {

                },
                onLeft = {

                },
                onRight = {

                },
                onA = {

                },
                onB = {

                }
            )

        }


    }
}

private fun ControlDeck(onUp: ()->Unit, onDown: ()->Unit, onLeft: ()->Unit, onRight: ()->Unit, onA: ()->Unit, onB: ()->Unit) {
    Row {
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
    GameConsole(runner)
}

@Composable
@Preview
fun PreviewRasterDisplay() {
    val raster = DisplayRaster(16, 16)
    raster.setPixel(8, 8, 100)
    raster.setPixel(9, 8, 100)
    RasterDisplay(raster)
}