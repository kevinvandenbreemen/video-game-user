package com.vandenbreemen.videogameusr.tools.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.Dimensions
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.VideoGameUserTheme
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.CoreDependenciesHelper
import com.vandenbreemen.videogameusr.model.game.LevelModel
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import com.vandenbreemen.videogameusr.tools.interactor.CodeGenerationInteractor
import com.vandenbreemen.videogameusr.tools.model.GameDataEditorModel
import com.vandenbreemen.videogameusr.tools.model.LevelEditorModel
import com.vandenbreemen.videogameusr.tools.viewmodel.LevelEditorViewModel
import kotlin.math.ceil

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

        Column(modifier = Modifier.weight(.12f)) {
            Card(modifier = Modifier.padding(Dimensions.padding).fillMaxSize(), elevation = Dimensions.elevation) {
                //  The tile selector
                SpriteTileGrid(levelEditorViewModel.getSpriteEditorModel(), "Tiles", selectedSpriteIndex::value::set)
            }
        }
        Column(modifier = Modifier.weight(.88f).clip(MaterialTheme.shapes.medium).fillMaxSize()) {
            //  The level editor
            Card(modifier = Modifier.padding(Dimensions.padding).fillMaxSize(), elevation = Dimensions.elevation){
                LevelEditorView(levelEditorViewModel)
            }
        }
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
    val tileIndexGridForLevel = levelEditorViewModel.levelCoordinateToTileGrid.collectAsState()


    //  Show a grid of squares corresponding to the zoom etc
    Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {

        //  Detect user tapping on a tile and set its sprite
        detectTapGestures {

            val gridRenderinInfo = getLevelGridRenderingInfo(size, levelEditorViewModel)

            val x = it.x
            val y = it.y

            val casnvasWidth = size.width
            val canvasHeight = size.height



            val boxWidth = gridRenderinInfo.boxWidth
            val boxHeight = gridRenderinInfo.boxHeight
            val col = (x / boxWidth).toInt()
            val row = (y / boxHeight).toInt()
            klog("tapped ($x, $y), Canvas width, height=($casnvasWidth, $canvasHeight), Level Width, Height = (${levelEditorViewModel.levelWidth}, ${levelEditorViewModel.levelHeight}), Box width,height =($boxWidth, $boxHeight) --> Tap on row=$row, col=$col")

            val index = selectedSpriteIndex
            levelEditorViewModel.setSpriteTileAt(col, row, index)
        }

    }
    ) {

        val gridRenderinInfo = getLevelGridRenderingInfo(size, levelEditorViewModel)

        val spriteWidth = levelEditorViewModel.spriteWidth.toFloat()
        val spriteHeight = levelEditorViewModel.spriteHeight.toFloat()

        val casnvasWidth = size.width
        val canvasHeight = size.height

        val boxWidth = gridRenderinInfo.boxWidth
        val boxHeight = gridRenderinInfo.boxHeight

        val pixelWidth = gridRenderinInfo.pixelWidth
        val pixelHeight = gridRenderinInfo.pixelHeight



        klog("canvas width=$casnvasWidth, Box width=$boxWidth, box height=$boxHeight")

        //  Draw the rows as boxes
        for (row in 0 until levelEditorViewModel.levelHeight) {
            for (col in 0 until levelEditorViewModel.levelWidth) {
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(col * boxWidth, row * boxHeight),
                    size = Size(boxWidth, boxHeight),
                    style = Stroke(0.1f)
                )

                //  Now grab the sprite from the level and draw it
                val index = tileIndexGridForLevel.value[row][col]
                val spriteColorGrid = levelEditorViewModel.getSpritePixelColorGridForSpriteIndex(index)
                if (spriteColorGrid != null) {
                    for (y in 0 until spriteHeight.toInt()) {
                        for (x in 0 until spriteWidth.toInt()) {
                            drawRect(
                                color = spriteColorGrid[y][x],
                                topLeft = Offset((col * boxWidth) + (x*pixelWidth), (row * boxHeight) + (y*pixelHeight)),
                                size = Size(pixelWidth, pixelHeight)
                            )
                        }
                    }
                }
            }
        }

    }

}

@Composable
@Preview
fun PreviewOfWhatYourWorkingOn() {

    val requirements = GameDataRequirements(8, 8, 8, 8, 1024)
    val levelModel = LevelModel(requirements, 1000, 100)
    val levelEditorModel = LevelEditorModel(requirements, levelModel, GameDataEditorModel(requirements,
        TileBasedGameWorld(requirements), 0, "requirements"),
        CoreDependenciesHelper.getColorInteractor(), CodeGenerationInteractor(requirements), "Level 1"
    )
    val levelEditorViewModel = LevelEditorViewModel(levelEditorModel)

    VideoGameUserTheme {
        LevelEditorView(levelEditorViewModel)
    }
}