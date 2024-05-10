package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.ConfirmingButton
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.VideoGameUserTheme
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import kotlin.math.ceil

/**
 * Tool for editing sprites
 * Major Components:
 * 1.  Sprite Editor Application proper [spriteEditor]
 * 1.  Sprite Pixel Editor [SpritePixelEditor]
 * 2.  Color Picker [ColorPickerUI]
 * 3.  Grid of other tiles [SpriteTileGrid]
 *
 * @param model Model for the sprite editor
 */
@Composable
fun SpriteEditorUI(model: SpriteEditorModel) {

    val spriteArray = remember { mutableStateOf(model.getSpriteByteArray()) }
    val paintColorByte = remember { mutableStateOf(model.paintColor) }
    val spriteCode = remember { mutableStateOf(model.generateSpriteSourceCode()) }
    val isErasing = remember { mutableStateOf(false) }
    val isEyeDropping = remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.weight(0.2f)) {
            SpriteTileGrid(model)
        }
        Column(
            modifier = Modifier.weight(0.66f).fillMaxSize().background(
                Color.Gray
            )
        ) {

            Column(modifier = Modifier.weight(0.6f)) {
                SpritePixelEditor(model, isErasing, isEyeDropping, paintColorByte, spriteArray, spriteCode)
            }


            //  Divider line
            Divider(color = Color.Black, thickness = 2.dp, modifier = Modifier.padding(vertical = 5.dp))

            //  Bottom panel - color picker

            //  Determine the available colors -- for now 16 colors out of the 128 possible byte values evenly spaced
            
            Column(modifier = Modifier.weight(0.4f)) {
                Text("Color Picker", style = MaterialTheme.typography.h6)
                ColorPickerUI(paintColorByte, isErasing, isEyeDropping, model)
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
private fun SpriteTileGrid(model: SpriteEditorModel, width: Int = 100) {
    val range = model.getSpriteTileGridRange()
    val tilesPerRow = model.tilesPerRowOnSpriteTileGrid()

    val reqSpriteWidthToRowWidthRatio = (width / model.tilesPerRowOnSpriteTileGrid()) / model.spriteWidth

    val spriteWidthOnScreen = (model.spriteWidth * reqSpriteWidthToRowWidthRatio).dp
    val spriteHeightOnScreen = (model.spriteHeight * reqSpriteWidthToRowWidthRatio).dp

    Column(modifier = Modifier.border(1.dp, Color.Black).padding(5.dp).background(Color.Gray)) {
        Text("Nearest tiles in Assets", style = MaterialTheme.typography.h6)
        for(i in range.first..range.second step tilesPerRow){
            Row(modifier = Modifier.width(width.dp).height(spriteHeightOnScreen).padding(0.dp)) {
                for(j in i until i + tilesPerRow){
                    if(j > range.second){
                        break
                    }
                    val spriteArray = model.getSpriteTileGridArray(j)
                    Canvas(modifier = Modifier.size(width = spriteWidthOnScreen, height = spriteHeightOnScreen).padding(0.dp)) {
                        val width = size.width
                        val height = size.height

                        val pixelWidthInCanvas = ceil((width / model.spriteWidth).toDouble()).toFloat()
                        val pixelHeightInCanvas = ceil((height / model.spriteHeight).toDouble()).toFloat()

                        for (y in 0 until model.spriteHeight) {
                            for (x in 0 until model.spriteWidth) {
                                val left = x * pixelWidthInCanvas
                                val top = y * pixelHeightInCanvas

                                //  Draw grayscale color based on pixel byte value
                                val pixelColor = spriteArray[y * model.spriteWidth + x]

                                val color = model.getComposeColor(pixelColor)
                                drawRect(
                                    color,
                                    topLeft = Offset(left, top),
                                    size = Size(pixelWidthInCanvas, pixelHeightInCanvas)
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun SpritePixelEditor(
    model: SpriteEditorModel,
    isErasing: MutableState<Boolean>,
    isEyeDropping: MutableState<Boolean>,
    paintColorByte: MutableState<Byte>,
    spriteArray: MutableState<ByteArray>,
    spriteCode: MutableState<String>
) {
    val sizeWidthHeight = remember { mutableStateOf(Pair(0, 0)) }
    val tapState = remember { mutableStateOf(Offset.Zero) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Sprite Data Editor", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.width(10.dp))
        Button(onClick = {
            spriteArray.value = model.mirrorHorizontal()
            spriteCode.value = model.generateSpriteSourceCode()
        }) {
            Text("Flip Horiz", style = MaterialTheme.typography.button)
        }
        Spacer(modifier = Modifier.width(10.dp))
        ConfirmingButton("Clear Sprite", "This will erase all your work", {
            spriteArray.value = model.clearSprite()
            spriteCode.value = model.generateSpriteSourceCode()
        }, {})
    }

    //  Handle updates to the sprite here
    LaunchedEffect(tapState.value, sizeWidthHeight.value) {

        if (tapState.value == Offset.Zero || sizeWidthHeight.value == Pair(0, 0)) {
            return@LaunchedEffect
        }

        val width = sizeWidthHeight.value.first
        val height = sizeWidthHeight.value.second

        val pixelWidthInCanvas = width / model.spriteWidth
        val pixelHeightInCanvas = height / model.spriteHeight

        val x = (tapState.value.x / pixelWidthInCanvas).toInt()
        val y = (tapState.value.y / pixelHeightInCanvas).toInt()

        if (isErasing.value) {
            model.setPixel(x, y, 0)
        } else if (isEyeDropping.value) {
            model.getPixel(x, y)?.let {
                paintColorByte.value = it
                isEyeDropping.value = false
            }
        } else {
            model.setPixel(x, y, model.paintColor)
        }

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
                val pixelColor = spriteArray.value[y * model.spriteWidth + x]

                val color = model.getComposeColor(pixelColor)
                drawRect(
                    color,
                    topLeft = Offset(left, top),
                    size = Size(pixelWidthInCanvas, pixelHeightInCanvas)
                )
            }
        }
    }
}

@Composable
private fun ColorPickerUI(
    paintColorByte: MutableState<Byte>,
    isErasing: MutableState<Boolean>,
    isEyeDropping: MutableState<Boolean>,
    model: SpriteEditorModel
) {

    LaunchedEffect(paintColorByte.value) {  //  Force a redraw if the user picks a color
        model.paintColor = paintColorByte.value
    }

    val numColorChannelSteps = 4

    Column {
        Row(verticalAlignment = Alignment.CenterVertically){
            Column(modifier=Modifier.border(1.dp, Color.Black).padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Selected Color", style = MaterialTheme.typography.caption)
                //  Draw a box with the selected color
                val color = model.getComposeColor(paintColorByte.value)
                Box(modifier = Modifier.height(20.dp).width(60.dp).background(color))
            }


            Button(onClick = {
                paintColorByte.value = 0
                isErasing.value = false
            }, modifier = Modifier.width(70.dp).height(45.dp).padding(5.dp)) {
                Text("Reset Color",  style = TextStyle(color = Color.White, fontSize = 8.sp))
            }

            Button(onClick = {
                isErasing.value = !isErasing.value
                isEyeDropping.value = false
            }, modifier = Modifier.width(70.dp).height(45.dp).padding(5.dp)) {
                Text(if(isErasing.value) "Eraser ✏\uFE0F" else "Eraser",  style = TextStyle(color = Color.White, fontSize = 8.sp))
            }

            //  Eyedropper button
            Button(onClick = {
                isEyeDropping.value = !isEyeDropping.value
                isErasing.value = false
            }, modifier = Modifier.width(80.dp).height(55.dp).padding(5.dp)) {
                Text(if(isEyeDropping.value) "Eye Dropper 👁️" else "Eye Dropper",  style = TextStyle(color = Color.White, fontSize = 8.sp))
            }
        }

        Row {

            //  First the brightness column:
            Column(Modifier.weight(0.25f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Brightness")
                for (i in 0 until numColorChannelSteps) {

                    //  Grayscale color to signify a brightness
                    val colorByte = model.byteColorDataInteractor.getColorByte(i, i, i, i)
                    val color = model.getComposeColor(colorByte)
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = color, contentColor = Color.White),
                        onClick = {
                            paintColorByte.value = model.setBrightness(paintColorByte.value, i)
                            isErasing.value = false
                        }, modifier = Modifier.fillMaxWidth().background(color)
                    ) {
                        Text("$i", style = TextStyle(color = Color.White, fontSize = 8.sp))
                    }

                }
            }
            Column(Modifier.weight(0.25f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Red")
                for (i in 0 until numColorChannelSteps) {
                    val colorByte = model.byteColorDataInteractor.getColorByte(0, i, 0, 0)
                    val color = model.getComposeColor(colorByte)
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = color, contentColor = Color.White),
                        onClick = {
                            paintColorByte.value = model.setRed(paintColorByte.value, i)
                            isErasing.value = false
                        }, modifier = Modifier.fillMaxWidth().background(color)
                    ) {
                        Text("$i", style = TextStyle(color = Color.White, fontSize = 8.sp))
                    }
                }
            }
            Column(Modifier.weight(0.25f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Green")
                for (i in 0 until numColorChannelSteps) {
                    val colorByte = model.byteColorDataInteractor.getColorByte(0, 0, i, 0)
                    val color = model.getComposeColor(colorByte)
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = color, contentColor = Color.White),
                        onClick = {
                            paintColorByte.value = model.setGreen(paintColorByte.value, i)
                            isErasing.value = false
                        }, modifier = Modifier.fillMaxWidth().background(color)
                    ) {
                        Text("$i", style = TextStyle(color = Color.White, fontSize = 8.sp))
                    }
                }
            }
            Column(Modifier.weight(0.25f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Blue")
                for (i in 0 until numColorChannelSteps) {
                    val colorByte = model.byteColorDataInteractor.getColorByte(0, 0, 0, i)
                    val color = model.getComposeColor(colorByte)
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = color, contentColor = Color.White),
                        onClick = {
                            paintColorByte.value = model.setBlue(paintColorByte.value, i)
                            isErasing.value = false
                        }, modifier = Modifier.fillMaxWidth().background(color)
                    ) {
                        Text("$i", style = TextStyle(color = Color.White, fontSize = 8.sp))
                    }
                }
            }

        }
    }
}

fun spriteEditor(requirements: GameDataRequirements, spriteIndex: Int, requirementsVariableName: String = "requirement", maxWidth: Int = 1000) = application {

    //  Step 1:  Work out the height as a ratio of the width
    val height = (maxWidth * 0.9).toInt()

    val model = SpriteEditorModel(requirements, spriteIndex=spriteIndex, requirementsVariableName = requirementsVariableName)
    Window(
        onCloseRequest = {  },
        visible = true,
        title = "Sprite Editor - sprite $spriteIndex",
        state = WindowState(width = maxWidth.dp, height = height.dp)
    ) {

        VideoGameUserTheme {
            SpriteEditorUI(model)
        }

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