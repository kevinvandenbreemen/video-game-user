package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

@Composable
fun SpriteEditorUI(model: SpriteEditorModel) {

    //  Draw a compose UI with a top panel for the pixels and a bottom panel for the color selector
    //  The top panel will be a grid of pixels
    //  The bottom panel will be a grid of colors
    //  The user can click on a pixel to change its color

    //  Byte state
    val spriteByteArray = remember { model.getSpriteByteArray() }

    Column(modifier = androidx.compose.ui.Modifier.fillMaxSize().background(
        androidx.compose.ui.graphics.Color.Gray
    )) {

        Column(modifier = Modifier.weight(0.8f)) {
            Text("Sprite Data")
            //  Top panel
            Canvas(modifier = Modifier.fillMaxSize()) {
                //  Draw the sprite
                val width = size.width
                val height = size.height

                val pixelWidthInCanvas = width / model.spriteWidth
                val pixelHeightInCanvas = height / model.spriteHeight

                for (y in 0 until model.spriteHeight) {
                    for (x in 0 until model.spriteWidth) {
                        val left = x * pixelWidthInCanvas
                        val top = y * pixelHeightInCanvas
                        val color = if (spriteByteArray[y * model.spriteWidth + x] > 0) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black
                        drawRect(color, topLeft = Offset(left, top), size = Size(pixelWidthInCanvas, pixelHeightInCanvas))
                    }
                }
            }
        }



        //  Bottom panel
        Column(modifier = Modifier.weight(0.2f)) {
            Text("Color Picker")
            Row {
                //  Draw the color selector
                for (i in 0 until 16) {
                    Canvas(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val color = androidx.compose.ui.graphics.Color(i, i, i)
                        drawRect(color, topLeft = Offset(0f, 0f), size = Size(width, height))
                    }
                }
            }
        }
    }

}

fun spriteEditor(requirements: GameDataRequirements, maxWidth: Int = 800) = application {

    //  Step 1:  Work out the height as a ratio of the width
    val height = (maxWidth * 0.75).toInt()

    val model = SpriteEditorModel(requirements, 0)
    Window(
        onCloseRequest = {  },
        visible = true,
        title = "Raster Render Test",
        state = WindowState(width = maxWidth.dp, height = height.dp)
    ) {
        SpriteEditorUI(model)
    }

}

@Composable
@Preview
fun PreviewSpriteEditorUI() {

    val requirements = GameDataRequirements(200, 150, 8, 8, 1024)
    requirements.setData(0, byteArrayOf(
        //  Just 0s
        1, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 1, 0, 0, 0, 0, 0,
        0, 0, 1, 1, 0, 0, 0, 0,
        1, 1, 1, 1, 1, 0, 0, 0,
        1, 1, 1, 1, 1, 1, 1, 0,
        0, 1, 1, 0, 0, 1, 0, 0,
        1, 1, 0, 0, 0, 0, 0, 0,
        1, 0, 0, 0, 0, 0, 0, 0,
    ).also { // Multiply all values  by 100
        for(i in it.indices){
            it[i] = (it[i] * 200).toByte()
        }
    })

    val model = SpriteEditorModel(requirements, 0)

    SpriteEditorUI(model)

}