package com.vandenbreemen.videogameusr.view.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.vandenbreemen.videogameusr.view.VideoGameUserTheme

@Composable
fun ConfirmingButton(
    text: String,
    areYouSureMsg: String = "Are you sure?",
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {

    val isConfirming = remember { mutableStateOf(false) }

    Row {
        if(!isConfirming.value) {
            Button(onClick = { isConfirming.value = true }) {
                Text(text, style = MaterialTheme.typography.button)
            }
        }
        else {
            Column(modifier=Modifier.background(Color.Yellow)) {
                Text(areYouSureMsg, style = MaterialTheme.typography.button)
                Row {
                    Button(
                        onClick = {
                            isConfirming.value = false
                            onConfirm()
                        }
                    ) {
                        Text("Yes", style = MaterialTheme.typography.button)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            isConfirming.value = false
                            onCancel()
                        }
                    ) {
                        Text("No", style = MaterialTheme.typography.caption)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ConfirmingButtonPreview() {
    VideoGameUserTheme {
        ConfirmingButton("Test", "Are you sure", { println("Confirmed") }, { println("Cancelled") })
    }
}

fun main() {
    confirmButtonTest()

}

fun confirmButtonTest() = application {
    Window(
        onCloseRequest = {  },
        visible = true,
        title = "Confirming Button Test",
    ) {
        VideoGameUserTheme {
            ConfirmingButton("Test", "You sure", { println("Confirmed") }, { println("Cancelled") })
        }
    }
}