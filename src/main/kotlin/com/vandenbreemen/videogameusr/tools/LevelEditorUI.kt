package com.vandenbreemen.videogameusr.tools

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import com.vandenbreemen.com.vandenbreemen.videogameusr.tools.SpriteTileGrid
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.VideoGameUserTheme
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.LevelModel
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
            SpriteTileGrid(levelEditorViewModel.getSpriteEditorModel(), 100, selectedSpriteIndex)
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

    //  Create an array of 100 rows of 100 ints each
    val rows = Array(100) { IntArray(100) }

    Column(modifier=Modifier.fillMaxSize(), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {



        val scale = remember { mutableStateOf(1f) }

        Row {
            Button(onClick = { scale.value *= 2 }) {
                //  Left arrow
                Text("Zoom In", style = MaterialTheme.typography.caption)
            }

            Button(onClick = { scale.value /= 2 }) {
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

                    val offset = remember { mutableStateOf(Offset.Zero) }


                    //  Show a grid of squares corresponding to the zoom etc
                    Canvas(modifier = Modifier.fillMaxSize().clipToBounds().pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                offset.value += dragAmount
                                change.consume()
                            }

                            detectTransformGestures { _, _, zoom, _ ->
                                scale.value *= zoom
                            }

                        }
                    ) {

                        translate(offset.value.x, offset.value.y) {
                            scale(scale.value) {
                                //  Draw the rows as boxes
                                for (row in 0 until 100) {
                                    for (col in 0 until 100) {
                                        drawRect(
                                            color = Color.Black,
                                            topLeft = Offset(col * 10f, row * 10f),
                                            size = Size(10f, 10f),
                                            style = Stroke(0.5f)
                                        )
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
    val levelEditorModel = LevelEditorModel(requirements, levelModel, SpriteEditorModel(requirements, 0, "kevin"))
    val levelEditorViewModel = LevelEditorViewModel(levelEditorModel)

    VideoGameUserTheme {
        LevelEditorView(levelEditorViewModel)
    }
}