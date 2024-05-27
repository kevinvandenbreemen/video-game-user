package com.vandenbreemen.videogameusr.tools.composables

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vandenbreemen.videogameusr.tools.viewmodel.LevelEditorViewModel
import com.vandenbreemen.videogameusr.view.Dimensions

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
        val zoomLevel = levelEditorViewModel.scale.collectAsState()

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

        Column(modifier = Modifier.weight(.88f).clip(MaterialTheme.shapes.medium).fillMaxSize()) {
            //  The level editor
            Card(modifier = Modifier.padding(Dimensions.padding).fillMaxSize(), elevation = Dimensions.elevation){

                Column {

                    //  Title and controls
                    Row(modifier = Modifier.fillMaxWidth().padding(start = Dimensions.padding, end = Dimensions.padding, top = Dimensions.padding)
                        , verticalAlignment = Alignment.CenterVertically
                    ) {
                        //  Title
                        Text("Level Editor: ${levelEditorViewModel.levelName} (${levelEditorViewModel.levelWidth} x ${levelEditorViewModel.levelHeight})",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.weight(1f))
                        //  Controls
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            val zoomControlSize = 25.dp

                            if(zoomLevel.value != 1f) {
                                Text(
                                    "${zoomLevel.value}x",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier.padding(end = Dimensions.padding)
                                )
                            }

                            //  Plus and minus buttons
                            IconButton(onClick = {
                                levelEditorViewModel.zoomIn()
                            }, modifier = Modifier.size(zoomControlSize)) {
                                Text("+")
                            }

                            Spacer(modifier = Modifier.width((Dimensions.padding.value * 2).dp))

                            IconButton(onClick = {
                                levelEditorViewModel.zoomOut()
                            }, modifier = Modifier.size(zoomControlSize)) {
                                Text("-")
                            }
                        }
                    }

                    Card(elevation = Dimensions.elevation, modifier = Modifier.padding(Dimensions.padding)) {
                        //  Put this inside a viewport
                        Box(
                            modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Black).verticalScroll(verticalScrollState)
                                .horizontalScroll(horizontalScrollState)
                        ) {
                            LevelEditorView(levelEditorViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LevelEditorView(levelEditorViewModel: LevelEditorViewModel) {

    val selectedSpriteIndex by levelEditorViewModel.currentSelectedSpriteIndexStateFlow.collectAsState()
    val tileGrid = levelEditorViewModel.levelCoordinateToTileGrid.collectAsState()
    val zoomLevel = levelEditorViewModel.scale.collectAsState()

    val scaleDP = (50 * zoomLevel.value).dp

    Column {

        for(y in 0 until levelEditorViewModel.levelHeight) {
            Row {
                for(x in 0 until levelEditorViewModel.levelWidth) {
                    Box(
                        modifier = Modifier.size(scaleDP, scaleDP).pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    levelEditorViewModel.setSpriteTileAt(x, y, selectedSpriteIndex)
                                },

                            )
                        }
                    ) {
                        val tileIndex = tileGrid.value[y][x]
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            levelEditorViewModel.getSpritePixelColorGridForSpriteIndex(tileIndex)?.let { grid ->
                                val scale = scaleDP.value / levelEditorViewModel.spriteWidth
                                val scaleY = scaleDP.value / levelEditorViewModel.spriteHeight
                                grid.forEachIndexed { y, row ->
                                    row.forEachIndexed { x, color ->
                                        drawRect(color, topLeft = Offset(x * scale, y * scaleY), size = Size(scale, scaleY))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}