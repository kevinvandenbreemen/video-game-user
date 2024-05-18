package com.vandenbreemen.videogameusr.tools

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
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.com.vandenbreemen.videogameusr.tools.SpriteTileGrid
import com.vandenbreemen.com.vandenbreemen.videogameusr.tools.ToolType
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.Dimensions
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.VideoGameUserTheme
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.tools.composables.LevelDesigner
import com.vandenbreemen.videogameusr.tools.model.GameDataEditorModel
import com.vandenbreemen.videogameusr.tools.viewmodel.GameDataEditorViewModel
import com.vandenbreemen.videogameusr.tools.viewmodel.LevelEditorViewModel
import com.vandenbreemen.videogameusr.tools.viewmodel.SpriteEditorViewModel
import com.vandenbreemen.videogameusr.view.common.ConfirmingButton
import com.vandenbreemen.videogameusr.view.common.InputtingButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.ceil

/**
 * Tool for editing sprites
 * Major Components:
 * 1.  Sprite Editor Application proper [gameEditor]
 * 1.  Sprite Pixel Editor [SpritePixelEditor]
 * 2.  Level Editor [LevelDesigner]
 * 2.  Color Picker [ColorPickerUI]
 * 3.  Grid of other tiles [SpriteTileGrid]
 * 4.  Tool select drawer [GameToolDrawerContent]
 * 5.  Previewer of Compose Components [PreviewOfComponentYourWorkingOn]
 *
 * @param model Model for the sprite editor
 */
@Composable
fun SpriteEditorUI(model: GameDataEditorModel, viewModel: SpriteEditorViewModel) {

    val spriteArray = remember { mutableStateOf(model.getSpriteByteArray()) }
    val paintColorByte = remember { mutableStateOf(model.paintColor) }
    val spriteCode = remember { mutableStateOf(model.generateSpriteSourceCode()) }
    val isErasing = remember { mutableStateOf(false) }
    val isEyeDropping = remember { mutableStateOf(false) }
    val spriteIndex = remember { mutableStateOf(model.currentSpriteIndex) }
    val isPickingSpriteToCopyFrom = remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.weight(0.1f)) {
            SpriteTileGrid(model, 100,
                title = if(isPickingSpriteToCopyFrom.value) "Select tile to Copy" else "All Tile Assets",
                onSelectSpriteIndex = {
                if(isPickingSpriteToCopyFrom.value) {
                    model.copySprite(it)
                    isPickingSpriteToCopyFrom.value = false
                    return@SpriteTileGrid
                }
                model.selectSpriteIndex(it)
                spriteIndex.value = it
            })
        }
        Column(
            modifier = Modifier.weight(0.6f).fillMaxSize().background(
                MaterialTheme.colors.surface
            )
        ) {

            Column(modifier = Modifier.weight(0.6f)) {
                SpritePixelEditor(viewModel, model, isEyeDropping, spriteArray, spriteCode, spriteIndex, isPickingSpriteToCopyFrom)
            }


            //  Divider line
            Divider(color = Color.Black, thickness = 2.dp, modifier = Modifier.padding(vertical = 5.dp))

            //  Bottom panel - color picker

            //  Determine the available colors -- for now 16 colors out of the 128 possible byte values evenly spaced
            
            Column(modifier = Modifier.weight(0.4f)) {
                Text("Color Picker", style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold))
                ColorPickerUI(viewModel, paintColorByte, isEyeDropping, model)
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
    viewModel: SpriteEditorViewModel,
    model: GameDataEditorModel,
    isEyeDropping: MutableState<Boolean>,
    spriteArray: MutableState<ByteArray>,
    spriteCode: MutableState<String>,
    spriteIndex: MutableState<Int>,
    isPickingSpriteToCopyFrom: MutableState<Boolean>
) {
    val sizeWidthHeight = remember { mutableStateOf(Pair(0, 0)) }
    val tapState = remember { mutableStateOf(Offset.Zero) }
    val isErasing = viewModel.isErasing.collectAsState()

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Sprite Data Editor", style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onSurface))
        Spacer(modifier = Modifier.width(Dimensions.padding))
        Button(onClick = {
            spriteArray.value = model.mirrorHorizontal()
            spriteCode.value = model.generateSpriteSourceCode()
        }) {
            Text("Flip Horiz", style = MaterialTheme.typography.button)
        }
        Spacer(modifier = Modifier.width(Dimensions.padding))
        Button(onClick = {
            spriteArray.value = model.mirrorVertical()
            spriteCode.value = model.generateSpriteSourceCode()
        }) {
            Text("Flip Vert", style = MaterialTheme.typography.button)
        }
        Spacer(modifier = Modifier.width(Dimensions.padding))
        ConfirmingButton("Clear Sprite", "This will erase all your work", {
            spriteArray.value = model.clearSprite()
            spriteCode.value = model.generateSpriteSourceCode()
        }, {})
        Spacer(modifier = Modifier.width(Dimensions.padding))
        ConfirmingButton("Copy to Current", "This will overwrite the current sprite", {
            isPickingSpriteToCopyFrom.value = true
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
                viewModel.setPaintColorByte(it)
                isEyeDropping.value = false
            }
        } else {
            model.setPixel(x, y, model.paintColor)
        }

        spriteArray.value = model.getSpriteByteArray()
        spriteCode.value = model.generateSpriteSourceCode()
    }

    //  Top panel
    Canvas(modifier = Modifier.fillMaxSize().background(Color.Black).pointerInput(Unit) {
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
    spriteEditorViewModel: SpriteEditorViewModel,
    paintColorByte: MutableState<Byte>,
    isEyeDropping: MutableState<Boolean>,
    model: GameDataEditorModel
) {

    val isErasing = spriteEditorViewModel.isErasing.collectAsState()

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
                spriteEditorViewModel.setErasing(false)
            }, modifier = Modifier.width(70.dp).height(45.dp).padding(5.dp)) {
                Text("Reset Color",  style = MaterialTheme.typography.button)
            }

            Button(onClick = {
                spriteEditorViewModel.toggleErasing()
                isEyeDropping.value = false
            }, modifier = Modifier.width(70.dp).height(45.dp).padding(5.dp)) {
                Text(if(isErasing.value) "Eraser ✏\uFE0F" else "Eraser",  style = MaterialTheme.typography.button)
            }

            //  Eyedropper button
            Button(onClick = {
                isEyeDropping.value = !isEyeDropping.value
                spriteEditorViewModel.setErasing(false)
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
                            spriteEditorViewModel.setErasing(false)
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
                            spriteEditorViewModel.setErasing(false)
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
                            spriteEditorViewModel.setErasing(false)
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
                            spriteEditorViewModel.setErasing(false)
                        }, modifier = Modifier.fillMaxWidth().background(color)
                    ) {
                        Text("$i", style = MaterialTheme.typography.button)
                    }
                }
            }

        }
    }
}

@Composable
private fun prepareAlertDialogForErrors(viewModel: GameDataEditorViewModel) {
    val error = viewModel.errorMessage.collectAsState()
    key(error.value) {
        error.value?.let {
            AlertDialog(
                onDismissRequest = {
                    viewModel.onErrorDismissed()
                },
                title = { Text("Error") },
                text = { Text(it) },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.onErrorDismissed()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
private fun prepareAlertDialogForStatusMessages(viewModel: GameDataEditorViewModel) {
    val status = viewModel.message.collectAsState()
    key(status.value) {
        status.value?.let {
            AlertDialog(
                onDismissRequest = {
                    viewModel.onMessageDismissed()
                },
                title = { Text("Status") },
                text = {
                    Row {

                        Text(it)
                    }
                       },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.onMessageDismissed()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

fun gameEditor(requirements: GameDataRequirements,
               tileBasedGameWorld: TileBasedGameWorld,
               spriteIndex: Int, requirementsVariableName: String = "requirement") = application {

    val maxWidth = 1100

    //  Step 1:  Work out the height as a ratio of the width
    val height = (maxWidth * 0.80).toInt()

    val model = GameDataEditorModel(requirements, spriteIndex=spriteIndex, requirementsVariableName = requirementsVariableName, tileBasedGameWorld = tileBasedGameWorld)
    val viewModel = GameDataEditorViewModel(model)
    val coroutineScope = rememberCoroutineScope()

    val toolSelection = viewModel.toolParameters.collectAsState()

    Window(
        resizable = false,
        onCloseRequest = {  },
        visible = true,
        title = "Sprite Editor - sprite $spriteIndex",
        state = WindowState(width = maxWidth.dp, height = height.dp)
    ) {

        VideoGameUserTheme {

            prepareAlertDialogForErrors(viewModel)
            prepareAlertDialogForStatusMessages(viewModel)

            //  Show this inside a UI with a hamburger menu on the top left
            val scaffoldState = rememberScaffoldState( rememberDrawerState(DrawerValue.Closed) )
            Scaffold(
                drawerGesturesEnabled = false,
                scaffoldState = scaffoldState,
                topBar = {

                    val titleStr = "Game Editor"

                    TopAppBar(
                        backgroundColor = MaterialTheme.colors.primary,
                        title = { Text(titleStr, style = MaterialTheme.typography.subtitle1) }, navigationIcon = {
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
                    GameToolDrawerContent(coroutineScope, scaffoldState,
                        viewModel,
                        )
                }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    key(toolSelection.value) {
                        klog("UI - Tool selection changed to ${toolSelection.value}")

                        toolSelection.value?.let { tool ->

                            when (tool.toolType) {
                                ToolType.SpriteEditor -> {
                                    SpriteEditorUI(model, SpriteEditorViewModel(model))
                                }

                                ToolType.LevelEditor -> {

                                    val levelName =
                                        tool.levelName ?: throw IllegalStateException("No level selected")

                                    klog("UI - Editing level $levelName")
                                    LevelDesigner(
                                        LevelEditorViewModel(
                                            model.editLevel(levelName)
                                        )
                                    )
                                }
                            }
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
    gameDataEditorModel: GameDataEditorViewModel
) {
    Column(modifier = Modifier.background(MaterialTheme.colors.background).fillMaxSize()) {

        //  Sprite editor
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sprite Editor", style = MaterialTheme.typography.subtitle1, modifier = Modifier.clickable {
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                    gameDataEditorModel.editSprites()
                }
            })
        }

        //  Level Creator
        Column(modifier = Modifier.border(1.dp, MaterialTheme.colors.onBackground,
                shape = MaterialTheme.shapes.medium
            ).padding(Dimensions.borderPadding,
            )) {

            Text("Add Level", style = MaterialTheme.typography.subtitle1)
            InputtingButton( "Add Level", "Enter level name", onInput = {
                coroutineScope.launch {
                    gameDataEditorModel.addLevel(it)
                }
            })

        }

        //  Level editor
        Column {
            Text("Level Editor", style = MaterialTheme.typography.subtitle1)

            val availableLevelNames = gameDataEditorModel.levelNames.collectAsState()

            //  Buttons for each of the levels
            Column(modifier=Modifier.border(1.dp, Color.Black).padding(5.dp)) {
                for(levelName in availableLevelNames.value) {
                    Button(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                            gameDataEditorModel.selectLevelForEdit(levelName)
                        }
                    }) {
                        Text(levelName, style = MaterialTheme.typography.caption)
                    }
                }
            }

        }

        //  Code Dumper
        Row {
            Text("Dump Assets to File", style = MaterialTheme.typography.subtitle1.copy(color = Color.Green), modifier = Modifier.clickable {
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                    gameDataEditorModel.dumpAssetsToFile()
                }
            })
        }
    }
}

@Composable
@Preview
fun PreviewOfComponentYourWorkingOn() {

    val scaffoldState = rememberScaffoldState( rememberDrawerState(DrawerValue.Closed) )

    val requirements = GameDataRequirements(16, 16, 8, 8, 128,)
    val tileBasedGameWorld = TileBasedGameWorld(requirements)
    val model = GameDataEditorModel(requirements, tileBasedGameWorld,0, "requirements")
    val viewModel = GameDataEditorViewModel(model)

    VideoGameUserTheme {
        GameToolDrawerContent(rememberCoroutineScope(), scaffoldState,
            viewModel,
            )
    }

}