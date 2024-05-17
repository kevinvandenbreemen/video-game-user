package com.vandenbreemen.videogameusr.view.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.Dimensions
import com.vandenbreemen.com.vandenbreemen.videogameusr.view.VideoGameUserTheme

@Composable
fun InputtingButton(label: String, instruction: String, onInput: (String)->Unit) {

    Card(modifier = Modifier.padding(Dimensions.padding), elevation = 7.dp,
        shape = MaterialTheme.shapes.medium, border = BorderStroke(1.dp, MaterialTheme.colors.primary)
        ) {

        val text = remember { mutableStateOf("") }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(MaterialTheme.colors.secondary).border(1.dp, MaterialTheme.colors.primary)
                .padding(Dimensions.padding)
        ) {
            val isShowing = remember { mutableStateOf(false) }
            val text = remember { mutableStateOf("") }

            if (!isShowing.value) {
                Button(onClick = {
                    isShowing.value = true
                }) {
                    Text(label, style = MaterialTheme.typography.caption)
                }
            } else {
                TextField(value = text.value, onValueChange = {
                    text.value = it
                }, label = {
                    Text(instruction)
                })
                Spacer(modifier = Modifier.width(Dimensions.padding))
                Button(onClick = {
                    isShowing.value = false
                    onInput(text.value)
                }) {
                    Text("Enter", style = MaterialTheme.typography.caption)
                }
                Spacer(modifier = Modifier.width(Dimensions.padding))
                Button(onClick = {
                    isShowing.value = false
                }) {
                    Text("Cancel", style = MaterialTheme.typography.caption)
                }
            }
        }
    }
}

@Composable
@Preview
fun InputtingButtonPreview() {
    VideoGameUserTheme {
        Column(modifier=Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
            Text("Inputting Button", style = MaterialTheme.typography.h6)
            InputtingButton( "Edit", "Enter a new value", onInput = { println(it) })
        }

    }
}