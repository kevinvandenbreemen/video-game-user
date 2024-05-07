package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import kotlin.math.ceil

@Composable
fun SpriteEditorUI(model: SpriteEditorModel) {

    val spriteArray = remember { mutableStateOf(model.getSpriteByteArray()) }
    val paintColorByte = remember { mutableStateOf(model.paintColor) }
    val spriteCode = remember { mutableStateOf(model.generateSpriteSourceCode()) }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(0.66f).fillMaxSize().background(
                Color.Gray
            )
        ) {

            Column(modifier = Modifier.weight(0.8f)) {

                val sizeWidthHeight = remember { mutableStateOf(Pair(0, 0)) }
                val tapState = remember { mutableStateOf(Offset.Zero) }

                Text("Sprite Data Editor")

                //  Handle updates to the sprite here
                LaunchedEffect(tapState.value, sizeWidthHeight.value) {

                    if(tapState.value == Offset.Zero || sizeWidthHeight.value == Pair(0, 0)){
                        return@LaunchedEffect
                    }

                    val width = sizeWidthHeight.value.first
                    val height = sizeWidthHeight.value.second

                    val pixelWidthInCanvas = width / model.spriteWidth
                    val pixelHeightInCanvas = height / model.spriteHeight

                    val x = (tapState.value.x / pixelWidthInCanvas).toInt()
                    val y = (tapState.value.y / pixelHeightInCanvas).toInt()

                    model.setPixel(x, y, model.paintColor)
                    spriteArray.value = model.getSpriteByteArray()
                    spriteCode.value = model.generateSpriteSourceCode()
                }

                //  Top panel
                Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                    detectTapGestures { offset ->
                        tapState.value = offset
                        sizeWidthHeight.value = Pair(size.width, size.height)
                    }
                }) {

                    //  Draw the sprite
                    val width = size.width
                    val height = size.height

                    val pixelWidthInCanvas = ceil((width / model.spriteWidth).toDouble()).toFloat()
                    val pixelHeightInCanvas = ceil((height / model.spriteHeight).toDouble()).toFloat()

                    for (y in 0 until model.spriteHeight) {
                        for (x in 0 until model.spriteWidth) {
                            val left = x * pixelWidthInCanvas
                            val top = y * pixelHeightInCanvas

                            //  Draw grayscale color based on pixel byte value
                            val pixelColor = spriteArray.value[y * model.spriteWidth + x].toInt()
                            val color = Color(pixelColor, pixelColor, pixelColor)
                            drawRect(
                                color,
                                topLeft = Offset(left, top),
                                size = Size(pixelWidthInCanvas, pixelHeightInCanvas)
                            )
                        }
                    }
                }
            }


            //  Bottom panel - color picker

            //  Determine the available colors -- for now 16 colors out of the 128 possible byte values evenly spaced
            
            Column(modifier = Modifier.weight(0.2f)) {
                Text("Color Picker")
                ColorPickerUI(paintColorByte, model)
            }
        }
        Column(modifier = Modifier.weight(0.33f).fillMaxSize().background(Color.Black)) {
            //  Show the sourcecode for creating the sprite
            Text("Source Code", style = TextStyle(fontSize = 14.sp, color = Color.Green
                , fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center))

            TextField(value = spriteCode.value, onValueChange = {  }, readOnly = true,
                textStyle = TextStyle(fontSize = 8.sp, color = Color.Green, fontFamily = FontFamily.Monospace),
                modifier = Modifier.fillMaxSize())

        }
    }
}

@Composable
private fun ColorPickerUI(
    paintColorByte: MutableState<Byte>,
    model: SpriteEditorModel
) {
    Row {

        val colorCount = 16
        val colorStep = 128 / colorCount

        LaunchedEffect(paintColorByte.value) {  //  Force a redraw if the user picks a color
            model.paintColor = paintColorByte.value
        }

        //  Draw the color selector
        Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectTapGestures { offset ->
                val x = offset.x
                val colorIndex = (x / size.width * colorCount).toInt()
                paintColorByte.value = (colorIndex * colorStep).toByte()
            }

        }) {

            val width = size.width / colorCount

            for (i in 0 until colorCount) {
                val colorVal = i * colorStep

                val color = Color(colorVal, colorVal, colorVal)
                drawRect(
                    color,
                    topLeft = Offset(i * width, 0f),
                    size = Size(width, size.height)
                )

                //  If the color is selected draw a small circle in the middle of it
                if (colorVal == paintColorByte.value.toInt()) {
                    drawCircle(
                        Color.Green,
                        center = Offset(i * width + width / 2, size.height / 2),
                        radius = 5f
                    )
                }
            }
        }
    }
}

fun spriteEditor(requirements: GameDataRequirements, spriteIndex: Int, requirementsVariableName: String = "requirement", maxWidth: Int = 800) = application {

    //  Step 1:  Work out the height as a ratio of the width
    val height = (maxWidth * 0.75).toInt()

    val model = SpriteEditorModel(requirements, spriteIndex=spriteIndex, requirementsVariableName = requirementsVariableName)
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

    val model = SpriteEditorModel(requirements, 0, "requirementInPreview")

    SpriteEditorUI(model)

}