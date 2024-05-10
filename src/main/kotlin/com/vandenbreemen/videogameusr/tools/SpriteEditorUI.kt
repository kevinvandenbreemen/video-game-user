package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.ConfirmingButton
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.VideoGameUserTheme
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.tools.LevelDesigner
import com.vandenbreemen.videogameusr.tools.model.SpriteEditorModel
import com.vandenbreemen.videogameusr.tools.viewmodel.LevelEditorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.ceil

/**
 * Tool for editing sprites
 * Major Components:
 * 1.  Sprite Editor Application proper [spriteEditor]
 * 1.  Sprite Pixel Editor [SpritePixelEditor]
 * 2.  Color Picker [ColorPickerUI]
 * 3.  Grid of other tiles [SpriteTileGrid]
 * 4.  Tool select drawer [GameToolDrawerContent]
 * 5.  Previewer of Compose Components [PreviewOfComponentYourWorkingOn]
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
    val spriteIndex = remember { mutableStateOf(model.currentSpriteIndex) }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.weight(0.1f)) {
            SpriteTileGrid(model, 100, spriteIndex)
        }
        Column(
            modifier = Modifier.weight(0.6f).fillMaxSize().background(
                Color.Gray
            )
        ) {

            Column(modifier = Modifier.weight(0.6f)) {
                SpritePixelEditor(model, isErasing, isEyeDropping, paintColorByte, spriteArray, spriteCode, spriteIndex)
            }


            //  Divider line
            Divider(color = Color.Black, thickness = 2.dp, modifier = Modifier.padding(vertical = 5.dp))

            //  Bottom panel - color picker

            //  Determine the available colors -- for now 16 colors out of the 128 possible byte values evenly spaced
            
            Column(modifier = Modifier.weight(0.4f)) {
                Text("Color Picker", style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold))
                ColorPickerUI(paintColorByte, isErasing, isEyeDropping, model)
            }
        }
        Column(modifier = Modifier.weight(0.3f).fillMaxSize().background(Color.Black)) {
            //  Show the sourcecode for creating the sprite
            LaunchedEffect(spriteIndex.value) {
                spriteCode.value = model.generateSpriteSourceCode()
            }
            val scrollState = rememberScrollState()
            Text("Source Code", style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

            TextField(value = spriteCode.value, onValueChange = {  }, readOnly = true,
                textStyle = TextStyle(fontSize = 8.sp, color = Color.Green, fontFamily = FontFamily.Monospace),
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState))

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
    spriteCode: MutableState<String>,
    spriteIndex: MutableState<Int>
) {
    val sizeWidthHeight = remember { mutableStateOf(Pair(0, 0)) }
    val tapState = remember { mutableStateOf(Offset.Zero) }


    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Sprite Data Editor", style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold))
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

    //  Handle picking a different sprite to edit here!
    LaunchedEffect(spriteIndex.value) {
        model.selectSpriteIndex(spriteIndex.value)
        spriteArray.value = model.getSpriteByteArray()
        spriteCode.value = model.generateSpriteSourceCode()
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
                Text("Reset Color",  style = MaterialTheme.typography.button)
            }

            Button(onClick = {
                isErasing.value = !isErasing.value
                isEyeDropping.value = false
            }, modifier = Modifier.width(70.dp).height(45.dp).padding(5.dp)) {
                Text(if(isErasing.value) "Eraser ✏\uFE0F" else "Eraser",  style = MaterialTheme.typography.button)
            }

            //  Eyedropper button
            Button(onClick = {
                isEyeDropping.value = !isEyeDropping.value
                isErasing.value = false
            }, modifier = Modifier.width(80.dp).height(55.dp).padding(5.dp)) {
                Text(if(isEyeDropping.value) "Eye Dropper 👁️" else "Eye Dropper",  style = MaterialTheme.typography.button)
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
                        Text("$i", style = MaterialTheme.typography.button)
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
                        Text("$i", style = MaterialTheme.typography.button)
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
                        Text("$i", style =MaterialTheme.typography.button)
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
                        Text("$i", style = MaterialTheme.typography.button)
                    }
                }
            }

        }
    }
}

fun spriteEditor(requirements: GameDataRequirements, spriteIndex: Int, requirementsVariableName: String = "requirement") = application {

    val maxWidth = 900

    //  Step 1:  Work out the height as a ratio of the width
    val height = (maxWidth * 0.90).toInt()

    val model = SpriteEditorModel(requirements, spriteIndex=spriteIndex, requirementsVariableName = requirementsVariableName)
    val selectedTool = remember { mutableStateOf(ToolType.SpriteEditor) }
    val coroutineScope = rememberCoroutineScope()

    Window(
        resizable = false,
        onCloseRequest = {  },
        visible = true,
        title = "Sprite Editor - sprite $spriteIndex",
        state = WindowState(width = maxWidth.dp, height = height.dp)
    ) {

        VideoGameUserTheme {

            //  Show this inside a UI with a hamburger menu on the top left
            val scaffoldState = rememberScaffoldState( rememberDrawerState(DrawerValue.Closed) )
            Scaffold(
                drawerGesturesEnabled = false,
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(title = { Text("Game Editor", style = MaterialTheme.typography.subtitle1) }, navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    })
                },
                drawerShape = object: Shape {
                    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
                        return Outline.Rectangle(Rect(0f, 0f, (size.width * 0.6f), size.height))
                    }
                },
                drawerContent = {
                    GameToolDrawerContent(coroutineScope, scaffoldState, selectedTool)
                }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    when(selectedTool.value){
                        ToolType.SpriteEditor -> {
                            SpriteEditorUI(model)
                        }
                        ToolType.LevelEditor -> {
                            LevelDesigner(LevelEditorViewModel(model.getLevelEditorModel()))
                        }
                    }

                }
            }


        }

    }

}

@Composable
private fun GameToolDrawerContent(
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    selectedTool: MutableState<ToolType>
) {
    Column(modifier = Modifier.background(MaterialTheme.colors.background).fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sprite Editor", style = MaterialTheme.typography.subtitle1, modifier = Modifier.clickable {
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                    selectedTool.value = ToolType.SpriteEditor
                }
            })
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Level Editor", style = MaterialTheme.typography.subtitle1, modifier = Modifier.clickable {
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                    selectedTool.value = ToolType.LevelEditor
                }
            })
        }
    }
}

@Composable
@Preview
fun PreviewOfComponentYourWorkingOn() {

    val scaffoldState = rememberScaffoldState( rememberDrawerState(DrawerValue.Closed) )

    VideoGameUserTheme {
        GameToolDrawerContent(rememberCoroutineScope(), scaffoldState, mutableStateOf(ToolType.SpriteEditor))
    }

}