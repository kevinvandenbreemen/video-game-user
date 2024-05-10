package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.vandenbreemen.videogameusr.tools.model.SpriteEditorModel
import kotlin.math.ceil

object WhereToFindEverythingInCommonToolsUI {

}

@Composable
fun SpriteTileGrid(model: SpriteEditorModel, width: Int = 100, spriteIndex: MutableState<Int>) {
    val range = model.getSpriteTileGridRange()
    val tilesPerRow = model.tilesPerRowOnSpriteTileGrid

    val reqSpriteWidthToRowWidthRatio = (width / model.tilesPerRowOnSpriteTileGrid) / model.spriteWidth

    val spriteWidthOnScreen = (model.spriteWidth * reqSpriteWidthToRowWidthRatio).dp
    val spriteHeightOnScreen = (model.spriteHeight * reqSpriteWidthToRowWidthRatio).dp

    Column(Modifier.padding(5.dp)) {
        Text("All Tile Assets", style = MaterialTheme.typography.subtitle2)
        //  Scroll this
        LazyColumn(modifier = Modifier.border(1.dp, Color.Black).background(Color.Gray)) {
            items((range.first..range.second step tilesPerRow).toList()) { i ->
                Row(modifier = Modifier.width(width.dp).height(spriteHeightOnScreen).padding(0.dp)) {
                    for(j in i until i + tilesPerRow){
                        if(j > range.second){
                            break
                        }
                        val spriteArray = model.getSpriteTileGridArray(j)
                        Canvas(modifier = Modifier.size(width = spriteWidthOnScreen, height = spriteHeightOnScreen).padding(0.dp)
                            .pointerInput(Unit) {
                                detectTapGestures { offset ->
                                    model.selectSpriteIndex(j)
                                    spriteIndex.value = j
                                }
                            }) {


                            //  Detect user tapping on this


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
}