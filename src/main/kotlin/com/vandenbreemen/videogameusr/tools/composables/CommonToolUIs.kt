package com.vandenbreemen.videogameusr.tools.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.vandenbreemen.videogameusr.tools.model.GameDataEditorModel
import kotlin.math.ceil

object WhereToFindEverythingInCommonToolsUI {

}

@Composable
fun SpriteTileGrid(model: GameDataEditorModel,
                   title:String = "All Tile Assets",
                   onSelectSpriteIndex: (Int)-> Unit) {
    val range = model.getSpriteTileGridRange()
    val tilesPerRow = model.tilesPerRowOnSpriteTileGrid

    val width = 100
    val reqSpriteWidthToRowWidthRatio = (width / model.tilesPerRowOnSpriteTileGrid) / model.spriteWidth

    val spriteWidthOnScreen = (model.spriteWidth * reqSpriteWidthToRowWidthRatio).dp
    val spriteHeightOnScreen = (model.spriteHeight * reqSpriteWidthToRowWidthRatio).dp

    Column(Modifier.clip(MaterialTheme.shapes.small).fillMaxSize().padding(5.dp)) {
        Text(title, style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.onSurface))
        //  Scroll this
        LazyColumn(modifier = Modifier.border(1.dp, MaterialTheme.colors.onBackground).background(Color.Black)) {
            items((range.first..range.second step tilesPerRow).toList()) { i ->
                Row(modifier = Modifier.width(width.dp).height(spriteHeightOnScreen).padding(0.dp)) {
                    for(j in i until i + tilesPerRow){
                        if(j > range.second){
                            break
                        }
                        val spriteArray = model.getSpriteTileGridArray(j)
                        Canvas(modifier = Modifier.size(width = spriteWidthOnScreen, height = spriteHeightOnScreen).padding(0.dp)
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    onSelectSpriteIndex(j)
                                }
                            }) {


                            //  Detect user tapping on this


                            val sizeWidth = size.width
                            val height = size.height

                            val pixelWidthInCanvas = ceil((sizeWidth / model.spriteWidth).toDouble()).toFloat()
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
}