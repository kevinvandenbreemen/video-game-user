package com.vandenbreemen.com.vandenbreemen.videogameusr.tools

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.VideoGameUserTheme

/**
 * Major Components
 */
@Composable
fun LevelDesigner() {
    Column {
        Text("Welcome to the Next Level")
    }
}

@Composable
@Preview
fun PreviewOfWhatYourWorkingOn() {
    VideoGameUserTheme {
        LevelDesigner()
    }
}