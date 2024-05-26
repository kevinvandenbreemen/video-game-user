package com.vandenbreemen.videogameusr.view.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vandenbreemen.videogameusr.view.Dimensions
import com.vandenbreemen.videogameusr.view.VideoGameUserTheme

/**
 * A button that allows for inputting a value as well as any additional details you might like
 * @param label The label for the button
 * @param detailsRowComposable Any additional details you'd like to show
 */
@Composable
fun InputtingButton(label: String, instruction: String, detailsRowComposable: @Composable (()-> Unit)? = null,  onInput: (String)->Unit) {

    Card(elevation = 7.dp,
        shape = MaterialTheme.shapes.medium, border = BorderStroke(1.dp, MaterialTheme.colors.primary)
        ) {

        val text = remember { mutableStateOf("") }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(MaterialTheme.colors.secondary).border(1.dp, MaterialTheme.colors.primary)
                .padding(Dimensions.padding)
        ) {
            val isShowing = remember { mutableStateOf(false) }
            val focusRequester = remember { FocusRequester() }

            if (!isShowing.value) {
                Text(label, style = MaterialTheme.typography.caption, modifier = Modifier.clickable {
                    isShowing.value = true
                })
            } else {
                TextField(value = text.value, onValueChange = {
                    text.value = it
                }, label = {
                    Text(instruction)
                }, modifier = Modifier.focusRequester(focusRequester))

                // Request focus when TextField becomes visible
                LaunchedEffect(isShowing.value) {
                    focusRequester.requestFocus()
                }

                Spacer(modifier = Modifier.width(Dimensions.padding))

                //  Additional details for customization here:
                detailsRowComposable?.let { additionalDetails->
                    additionalDetails()
                    Spacer(modifier = Modifier.width(Dimensions.padding))
                }

                Button(
                    enabled = text.value.isNotBlank(),
                    onClick = {
                    isShowing.value = false
                    onInput(text.value)
                        text.value = ""
                }) {
                    Text("Enter", style = MaterialTheme.typography.caption.copy(color = Color.Green))
                }
                Spacer(modifier = Modifier.width(Dimensions.padding))
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    onClick = {
                    text.value = ""
                    isShowing.value = false
                }) {
                    Text("Cancel", style = MaterialTheme.typography.caption.copy(color=MaterialTheme.colors.onSecondary))
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
            InputtingButton( "Edit", "Enter a new value",
                { TextField(value = "", onValueChange = { println(it) })},
                onInput = { println(it) })
        }

    }
}