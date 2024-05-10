package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Row {
        Column(modifier = Modifier.weight(.2f)) {
            //  The tile selector
            SpriteTileGrid(levelEditorViewModel.getSpriteEditorModel(), 100, levelEditorViewModel.currentSelectedSpriteIndex)
        }
        Column(modifier = Modifier.weight(.8f)) {
            //  The level editor
            LevelEditorView(levelEditorViewModel)
        }
    }
}

@Composable
fun LevelEditorView(levelEditorViewModel: LevelEditorViewModel) {
    Column {
        Text("Level Editor")
        Text("Selected Sprite Index: ${levelEditorViewModel.currentSelectedSpriteIndex.value}")
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
        LevelDesigner(levelEditorViewModel)
    }
}