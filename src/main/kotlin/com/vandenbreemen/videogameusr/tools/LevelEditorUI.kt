package com.vandenbreemen.videogameusr.tools

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.com.vandenbreemen.videogameusr.tools.SpriteTileGrid
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.VideoGameUserTheme
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.game.LevelModel
import com.vandenbreemen.videogameusr.tools.model.LevelEditorModel
import com.vandenbreemen.videogameusr.tools.model.SpriteEditorModel
import com.vandenbreemen.videogameusr.tools.viewmodel.LevelEditorViewModel

/**
 * Major Components<br/>
 * * Sprite Tile Selector [SpriteTileGrid]
 * * Level Editor [LevelEditorView]
 */
@Composable
fun LevelDesigner(levelEditorViewModel: LevelEditorViewModel) {
    Row(modifier = Modifier.fillMaxSize()) {

        val selectedSpriteIndex = remember { mutableStateOf( levelEditorViewModel.currentSelectedSpriteIndex ) }
        LaunchedEffect(selectedSpriteIndex.value) {
            levelEditorViewModel.selectSpriteIndex(selectedSpriteIndex.value)
        }

        Column(modifier = Modifier.weight(.2f)) {
            //  The tile selector
            SpriteTileGrid(levelEditorViewModel.getSpriteEditorModel(), 100, "All Tile Assets", selectedSpriteIndex::value::set)
        }
        Column(modifier = Modifier.weight(.8f)) {
            //  The level editor
            LevelEditorView(levelEditorViewModel)
        }
    }
}

@Composable
fun LevelEditorView(levelEditorViewModel: LevelEditorViewModel) {

    val selectedSpriteIndex by levelEditorViewModel.currentSelectedSpriteIndexStateFlow.collectAsState()

    Column(modifier=Modifier.fillMaxSize(), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {



        val scale = levelEditorViewModel.scale.collectAsState()
        val pan = levelEditorViewModel.pan.collectAsState()

        Row {
            Button(onClick = {
                levelEditorViewModel.zoomIn()
            }) {
                //  Left arrow
                Text("Zoom In", style = MaterialTheme.typography.caption)
            }

            Button(onClick = {
                levelEditorViewModel.zoomOut()
            }) {
                //  Left arrow
                Text("Zoom Out", style = MaterialTheme.typography.caption)
            }
        }
        Column(modifier=Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(0.1f)){
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { levelEditorViewModel.selectSpriteIndex(0) }) {
                    //  Up arrow
                    Text("Up", style = MaterialTheme.typography.caption)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth().weight(0.8f),
                //  center
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
                ){
                Button(
                    modifier = Modifier.weight(0.1f),
                    onClick = { levelEditorViewModel.selectSpriteIndex(0) }) {
                    //  Left arrow
                    Text("Left", style = MaterialTheme.typography.caption)
                }
                Column(modifier = Modifier.weight(0.8f)) {


                    //  Show a grid of squares corresponding to the zoom etc
                    Canvas(modifier = Modifier.fillMaxSize().clipToBounds().pointerInput(Unit) {

                        //  Detect user tapping on a tile and set its sprite
                        detectTapGestures {
                            val spriteWidth = levelEditorViewModel.spriteWidth.toFloat()
                            val spriteHeight = levelEditorViewModel.spriteHeight.toFloat()

                            val x = (it.x / scale.value + pan.value.x) / spriteWidth
                            val y = (it.y / scale.value + pan.value.y) / spriteHeight


                            klog("Setting sprite at $x, $y to $selectedSpriteIndex")
                            levelEditorViewModel.setSpriteTileAt(x.toInt(), y.toInt(), selectedSpriteIndex)

                        }

                        }
                    ) {

                        val spriteWidth = levelEditorViewModel.spriteWidth.toFloat()
                        val spriteHeight = levelEditorViewModel.spriteHeight.toFloat()

                        translate(pan.value.x, pan.value.y) {
                            scale(scale.value) {
                                //  Draw the rows as boxes
                                for (row in 0 until levelEditorViewModel.levelHeight) {
                                    for (col in 0 until levelEditorViewModel.levelWidth) {
                                        drawRect(
                                            color = Color.Black,
                                            topLeft = Offset(col * spriteWidth, row * spriteHeight),
                                            size = Size(spriteWidth, spriteHeight),
                                            style = Stroke(0.5f)
                                        )

                                        //  Now grab the sprite from the level and draw it
                                        val spriteColorGrid = levelEditorViewModel.getSpritePixelColorGrid(col, row)
                                        if (spriteColorGrid != null) {
                                            for (y in 0 until spriteHeight.toInt()) {
                                                for (x in 0 until spriteWidth.toInt()) {
                                                    drawRect(
                                                        color = spriteColorGrid[y][x],
                                                        topLeft = Offset(col * spriteWidth + x, row * spriteHeight + y),
                                                        size = Size(1f, 1f)
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
                Button(onClick = { levelEditorViewModel.selectSpriteIndex(0) }) {
                    //  Right arrow
                    Text("Right", style = MaterialTheme.typography.caption)
                }
            }
            Row(modifier = Modifier.fillMaxWidth().weight(0.1f)){
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { levelEditorViewModel.selectSpriteIndex(0) }) {
                    //  Down arrow
                    Text("Down", style = MaterialTheme.typography.caption)
                }
                Spacer(modifier = Modifier.weight(1f))

            }
        }
    }

}

@Composable
@Preview
fun PreviewOfWhatYourWorkingOn() {

    val requirements = GameDataRequirements(8, 8, 8, 8, 1024)
    val levelModel = LevelModel(requirements, 1000, 100)
    val levelEditorModel = LevelEditorModel(requirements, levelModel, SpriteEditorModel(requirements, 0, "kevin"),
        CoreDependenciesHelper.getColorInteractor()
        )
    val levelEditorViewModel = LevelEditorViewModel(levelEditorModel)

    VideoGameUserTheme {
        LevelEditorView(levelEditorViewModel)
    }
}