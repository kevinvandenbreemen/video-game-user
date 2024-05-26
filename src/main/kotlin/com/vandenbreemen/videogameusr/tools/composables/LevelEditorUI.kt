package com.vandenbreemen.videogameusr.tools.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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

        Column(modifier = Modifier.weight(.88f).clip(MaterialTheme.shapes.medium).fillMaxSize()) {
            //  The level editor
            Card(modifier = Modifier.padding(Dimensions.padding).fillMaxSize(), elevation = Dimensions.elevation){

                val buttonSectionHeightWgt = 0.08f

                Column {
                    //  Put this inside a viewport
                    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(verticalScrollState).horizontalScroll(horizontalScrollState)) {
                        LevelEditorView(levelEditorViewModel)
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

    val scaleDP = 50.dp

    Column {

        for(y in 0 until levelEditorViewModel.levelHeight) {
            Row {
                for(x in 0 until levelEditorViewModel.levelWidth) {
                    val tileIndex = tileGrid.value[y][x]
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