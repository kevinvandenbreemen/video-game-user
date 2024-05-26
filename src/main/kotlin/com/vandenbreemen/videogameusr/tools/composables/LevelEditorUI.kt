package com.vandenbreemen.videogameusr.tools.composables

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vandenbreemen.videogameusr.tools.viewmodel.LevelEditorViewModel
import com.vandenbreemen.videogameusr.view.Dimensions
import kotlinx.coroutines.launch
import kotlin.math.ceil

/**
 * Major Components<br/>
 * * Sprite Tile Selector [SpriteTileGrid]
 * * Level Editor [LevelEditorView]
 */
@Composable
fun LevelDesigner(levelEditorViewModel: LevelEditorViewModel) {
    Row(modifier = Modifier.fillMaxSize()) {

        val verticalScrollState = rememberScrollState()
        val horizontalScrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()

        val lastSelectedTileValue = levelEditorViewModel.lastSelectedTileStateFlow.collectAsState()

        val selectedSpriteIndex = remember { mutableStateOf( levelEditorViewModel.currentSelectedSpriteIndex ) }
        LaunchedEffect(selectedSpriteIndex.value) {
            levelEditorViewModel.selectSpriteIndex(selectedSpriteIndex.value)
        }

        Column(modifier = Modifier.weight(.12f)) {
            Card(modifier = Modifier.padding(Dimensions.padding).fillMaxSize(), elevation = Dimensions.elevation) {
                //  The tile selector
                SpriteTileGrid(levelEditorViewModel.getSpriteEditorModel(), "Tiles", highlightSelected = true, selectedSpriteIndex::value::set)
            }
        }

        val scrollAmount = 50f

        Column(modifier = Modifier.weight(.88f).clip(MaterialTheme.shapes.medium).fillMaxSize()) {
            //  The level editor
            Card(modifier = Modifier.padding(Dimensions.padding).fillMaxSize(), elevation = Dimensions.elevation){

                val buttonSectionHeightWgt = 0.08f

                Row {
                    Column(modifier=Modifier.weight(buttonSectionHeightWgt).fillMaxSize()) {
                        Spacer(modifier = Modifier.weight(0.5f))
                        LeftButton {
                            coroutineScope.launch { horizontalScrollState.scrollBy(-scrollAmount) }
                        }
                        Spacer(modifier = Modifier.weight(0.5f))
                    }

                    Column(modifier=Modifier.weight(1 - (2*buttonSectionHeightWgt)).fillMaxSize()) {
                        Row(modifier=Modifier.weight(buttonSectionHeightWgt), horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.weight(0.5f))
                            //  Up button
                            UpButton { coroutineScope.launch { verticalScrollState.scrollBy(-scrollAmount) } }
                            Spacer(modifier = Modifier.weight(0.5f))
                        }

                        Row(modifier=Modifier.weight(1 - (2*buttonSectionHeightWgt)).clip(MaterialTheme.shapes.medium).fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {

                            //  Put this inside a viewport
                            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(verticalScrollState).horizontalScroll(horizontalScrollState)) {
                                LevelEditorView(levelEditorViewModel)
                            }
                        }


                        Row(modifier=Modifier.weight(buttonSectionHeightWgt), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.weight(0.5f))
                            //  Down button
                            DownButton {
                                coroutineScope.launch { verticalScrollState.scrollBy(scrollAmount) }
                            }
                            Spacer(modifier = Modifier.weight(0.5f))
                            Card(elevation = Dimensions.elevation, modifier = Modifier.height(28.dp)) {
                                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(Dimensions.padding)
                                    ) {
                                    val coordStr = if (lastSelectedTileValue.value.first > -1) {
                                        "(${lastSelectedTileValue.value.first}, ${lastSelectedTileValue.value.second})"
                                    } else {
                                        "None"
                                    }
                                    Text(
                                        "COORD:  $coordStr",
                                        style = MaterialTheme.typography.caption.copy(fontSize = 8.sp)
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier=Modifier.weight(buttonSectionHeightWgt).fillMaxSize()) {
                        Spacer(modifier = Modifier.weight(0.5f))
                        RightButton {
                            coroutineScope.launch { horizontalScrollState.scrollBy(scrollAmount) }
                        }
                        Spacer(modifier = Modifier.weight(0.5f))
                    }
                }



            }
        }
    }
}

@Composable
private fun UpButton(onPress: ()->Unit) {
    buildScrollCircleButton("^", onPress)
}

@Composable
private fun DownButton(onPress: ()->Unit) {
    buildScrollCircleButton("v", onPress)
}

@Composable
private fun LeftButton(onPress: ()->Unit) {
    buildScrollCircleButton("<", onPress)
}

@Composable
private fun RightButton(onPress: ()->Unit) {
    buildScrollCircleButton(">", onPress)
}

@Composable
private fun buildScrollCircleButton(symbol: String, onPress: () -> Unit) {
    Card(shape = CircleShape, elevation = Dimensions.elevation, modifier = Modifier.padding(Dimensions.padding)) {
        Text(
            symbol,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(Dimensions.padding).clip(CircleShape).clickable {
                onPress()
            })
    }
}

private data class LevelGridRenderingInfo(val boxWidth: Float, val boxHeight: Float, val pixelWidth: Float, val pixelHeight: Float)

private fun getLevelGridRenderingInfo(size: Any, levelEditorViewModel: LevelEditorViewModel): LevelGridRenderingInfo {

    //  Handle regular Size
    if(size is IntSize) {
        val casnvasWidth = size.width
        val canvasHeight = size.height

        val boxWidth = ceil(casnvasWidth / levelEditorViewModel.levelWidth.toFloat())
        val boxHeight = ceil(canvasHeight / levelEditorViewModel.levelHeight.toFloat())

        val pixelWidth = (boxWidth / levelEditorViewModel.spriteWidth.toFloat())
        val pixelHeight = (boxHeight / levelEditorViewModel.spriteHeight.toFloat())

        return LevelGridRenderingInfo(boxWidth, boxHeight, pixelWidth, pixelHeight)
    }

    //  Handle Size
    if(size is Size) {
        val casnvasWidth = size.width
        val canvasHeight = size.height

        val boxWidth = ceil(casnvasWidth / levelEditorViewModel.levelWidth.toFloat())
        val boxHeight = ceil(canvasHeight / levelEditorViewModel.levelHeight.toFloat())

        val pixelWidth = (boxWidth / levelEditorViewModel.spriteWidth.toFloat())
        val pixelHeight = (boxHeight / levelEditorViewModel.spriteHeight.toFloat())

        return LevelGridRenderingInfo(boxWidth, boxHeight, pixelWidth, pixelHeight)
    }

    throw IllegalArgumentException("Unknown size type $size")

}

@Composable
fun LevelEditorView(levelEditorViewModel: LevelEditorViewModel) {

    val selectedSpriteIndex by levelEditorViewModel.currentSelectedSpriteIndexStateFlow.collectAsState()

    val scaleDP = 50.dp

    Column {

        for(y in 0 until levelEditorViewModel.levelHeight) {
            Row {
                for(x in 0 until levelEditorViewModel.levelWidth) {
                    val tileIndex = levelEditorViewModel.levelCoordinateToTileGrid.value[y][x]
                    val color = if(tileIndex == -1) {
                        Color.Black
                    } else {
                        val spritePixelColorGrid = levelEditorViewModel.getSpritePixelColorGridForSpriteIndex(tileIndex)
                        if(spritePixelColorGrid == null) {
                            Color.Black
                        } else {
                            spritePixelColorGrid[y % levelEditorViewModel.spriteHeight][x % levelEditorViewModel.spriteWidth]
                        }
                    }

                    Box(
                        modifier = Modifier.size(scaleDP, scaleDP).background(color).pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    levelEditorViewModel.setSpriteTileAt(x, y, selectedSpriteIndex)
                                },

                            )
                        }
                    ) {
                        //  Draw the sprite
                        Text("0")
                    }
                }
            }
        }
    }


}