package com.vandenbreemen.com.vandenbreemen.videogameusr.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TestDisplay() {

    Column(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Gray)) {
        Column {
            Text("Hello from Compose Bitch")
        }
    }


}

@Composable
@Preview
fun PreviewTestComposable() {
    TestDisplay()
}